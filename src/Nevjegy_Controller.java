import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class Nevjegy_Controller {
    @FXML
    private Label label;

    @FXML
    private void initialize() {

        label.setText("set_pass\nver. 1.1\n\nAdatbázisjelszó változtató alkalmazás\n\nKészítette:\nVajda Ákos Péter\npec05856\n\n2018. 09. 11.");
    }
}
