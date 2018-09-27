import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    private final DB_Agent db_agent = new DB_Agent();
    private final Database emerald = new Database("XE", "localhost", 1521);
    private final Database test = new Database("tesztdb", "localhost", 1522);
    private final Database develop = new Database("develop", "localhost", 1523);
    private final ArrayList<Database> databases = new ArrayList<>();
    @FXML
    private Label message;

    @FXML
    private ComboBox adatbazis;

    @FXML
    private TextField username;
    @FXML
    private TextField oldpassword;
    @FXML
    private TextField newpassword1;
    @FXML
    private TextField newpassword2;
    @FXML
    private StackPane sp_newpassword;
    @FXML
    private PasswordField pf_oldpassword;
    @FXML
    private PasswordField pf_newpassword1;
    @FXML
    private PasswordField pf_newpassword2;
    @FXML
    private CheckBox cb_visible;
    @FXML
    private MenuItem nevjegy;
    @FXML
    private Button btn;
    @FXML
    private Button btn_once_more;
    @FXML
    private Button exit_button;


    @FXML
    private void initialize() {

        databases.add(emerald);
        databases.add(test);
        databases.add(develop);
        pf_oldpassword.textProperty().bindBidirectional(oldpassword.textProperty());
        pf_oldpassword.visibleProperty().bind(cb_visible.selectedProperty().not());
        oldpassword.visibleProperty().bind(cb_visible.selectedProperty());

        pf_newpassword1.textProperty().bindBidirectional(newpassword1.textProperty());
        pf_newpassword1.visibleProperty().bind(cb_visible.selectedProperty().not());
        newpassword1.visibleProperty().bind(cb_visible.selectedProperty());

        pf_newpassword2.textProperty().bindBidirectional(newpassword2.textProperty());
        pf_newpassword2.visibleProperty().bind(cb_visible.selectedProperty().not());
        newpassword2.visibleProperty().bind(cb_visible.selectedProperty());
        make_invisible_once_more_button();


        ObservableList<String> database_names = FXCollections.observableArrayList(databases.get(0).getName(), databases.get(1).getName(), databases.get(2).getName());
        adatbazis.setItems(database_names);
        adatbazis.setValue(database_names.get(0));
    }

    public void press_button(ActionEvent pressed) {

        if (is_matches() && oldpassword.getText().length() > 0 && username.getText().length() > 0) {
            if (is_complex_enough()) {


                db_agent.setDB_server_attributes((databases.get(adatbazis.getSelectionModel().getSelectedIndex()).getURL()) + ":" +
                        databases.get(adatbazis.getSelectionModel().getSelectedIndex()).getPort() + ":" + databases.get(adatbazis.getSelectionModel().getSelectedIndex()).getName());
                db_agent.setDB_new_password(newpassword1.getText());
                db_agent.setDB_Username(username.getText());
                db_agent.setDB_old_password(oldpassword.getText());
                db_agent.change_password(this);
            } else {
                message.setText("Az új jelszavak nem felenek meg az összetettségi követelnényeknek!\n(A jelszónak legalább 8 karaketrból kell állnia. Tartalmaznia kell legalább egy kis betűt\nés legalább egy NAGY betűt és legalább 1 számot.)\nSpeciális karaktereket ne használj!");
            }
        } else {
            message.setText("Az új jelszavak nem egyeznek meg!");
        }
    }

    public void Set_Button_Activate(ActionEvent pressed) {
        enable_button();

        make_invisible_once_more_button();
        setMessage("");

    }

    public void nevjegy_pressed(ActionEvent pressed) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App_Start.class.getResource("nevjegy.fxml"));
        Parent nevjegy = loader.load();
        Stage add_dialog_stage = new Stage();
        add_dialog_stage.setTitle("Névjegy");
        add_dialog_stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(nevjegy, 300, 400);
        add_dialog_stage.setResizable(false);
        add_dialog_stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/icon.png")));
        add_dialog_stage.setScene(scene);
        add_dialog_stage.showAndWait();

    }

    public void exit_button_pressed(ActionEvent pressed) {
        Stage stage = (Stage) exit_button.getScene().getWindow();
        stage.close();
    }

    private boolean is_matches() {
        return newpassword1.getText().equals(newpassword2.getText());

    }

    private boolean is_complex_enough() {

        int is_upper_case = 0;
        int is_lower_case = 0;
        int is_numeric = 0;
        for (int k = 0; k < newpassword1.getText().length(); k++) {

            if (Character.isUpperCase(newpassword1.getText().charAt(k))) is_upper_case++;
            else if (Character.isLowerCase(newpassword1.getText().charAt(k))) is_lower_case++;
            else if (Character.isDigit(newpassword1.getText().charAt(k))) is_numeric++;
            else return false;
        }
        return is_upper_case > 0 && is_lower_case > 0 && is_numeric > 0 && newpassword1.getText().length() > 7;
    }

    void setMessage(String message) {
        this.message.setText(message);
    }

    void grow_to_connect() {
        this.message.setFont(Font.font(24));
    }

    void disable_button() {
        btn.setDisable(true);
    }

    private void enable_button() {
        btn.setDisable(false);
    }

    void make_visible_once_more_button() {
        btn_once_more.setVisible(true);
    }

    private void make_invisible_once_more_button() {
        btn_once_more.setVisible(false);
    }
}
