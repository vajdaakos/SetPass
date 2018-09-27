package set_pass;

import oracle.jdbc.driver.OracleConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DB_Agent {
    private String DB_server_attributes;
    private String DB_Username;
    private String DB_old_password;
    private String DB_new_password;

    void setDB_server_attributes(String DB_server_attributes) {
        this.DB_server_attributes = DB_server_attributes;
    }

    void setDB_Username(String DB_Username) {
        this.DB_Username = DB_Username;
    }

    void setDB_old_password(String DB_old_password) {
        this.DB_old_password = DB_old_password;
    }

    void setDB_new_password(String DB_new_password) {
        this.DB_new_password = DB_new_password;
    }


    final void change_password(set_pass.Controller controller) {
        java.util.Properties connInfo = new java.util.Properties();

        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            connInfo.put("user", DB_Username);
            connInfo.put("password", DB_old_password);
            connInfo.put("database", DB_server_attributes);
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@", connInfo);


            String sql = "Alter USER " + DB_Username + " IDENTIFIED BY " + DB_new_password + " REPLACE " + DB_old_password;
            Statement stmt = connection.createStatement();
            stmt.executeQuery(sql);
            controller.grow_to_connect();
            controller.setMessage("A jelszó változtatás sikeres!\nHa újabb jelszóváltoztatást szeretnél,\nkattints az \"Újabb változtatás\" gombra!");
            controller.disable_button();
            controller.make_visible_once_more_button();
            connection.close();

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            if (e.getErrorCode() == 28001) {
                try {
                    connInfo.put("user", DB_Username);
                    connInfo.put("password", DB_old_password);
                    connInfo.put("database", DB_server_attributes);
                    connInfo.setProperty(OracleConnection.CONNECTION_PROPERTY_NEW_PASSWORD, DB_new_password);

                    Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@", connInfo);
                    connection.close();
                } catch (SQLException f) {
                    if (f.getErrorCode() == 1017) {
                        controller.setMessage("A jelszó változtatást nem lehet elvégezni, mert a jelszavad lejárt és ez a verziójú\nadatbázis nem támogatja lejárt jelszó változtatását!\nLépj kapcsolatba az adatbázis rendszergazdájával!");
                    } else {
                        controller.setMessage(f.getMessage());
                    }


                }


            } else {
                controller.setMessage(e.getMessage());
            }


        }


    }


}
