package resources.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import resources.Main;
import resources.database.DatabaseConnection;
import resources.modules.global_variable;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Handles Login actions and events
 */
public class Login implements javafx.fxml.Initializable {

    /**
     * Use to get and set mouse location on screen for dragging application
     */
    private double xOffset,yOffset = 0;

    /**
     * Message Label for validate login credentials
     */
    @FXML
    private Label LoginMessageLabel;

    /**
     * Program Title Bar
     */
    @FXML
    private HBox TitleBar;

    /**
     * Password Field
     */
    @FXML
    private PasswordField PasscodeTextField;

    /**
     * Username Field
     */
    @FXML
    private TextField UsernameTextField;

    /**
     * Close the program on mouseClickEvent
     * @param event close button click
     */
    @FXML
    void Close(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Maximize the program on mouseClickEvent
     * @param event maximize button click
     */
    @FXML
    void Maximize(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setFullScreen(true);
    }

    /**
     * Minimize the program on mouseClickEvent
     * @param event minimize button click
     */
    @FXML
    void Minimize(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Responsible for Draggable Title Bar with mouse event
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TitleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        TitleBar.setOnMouseDragged(event -> {
            Main.MainStage.setX(event.getScreenX() - xOffset);
            Main.MainStage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * Check User Biometrics in database
     */
    @FXML
    void biometrics( ) {
        LoginMessageLabel.setText("Sorry, this is under maintenance");
    }
    /**
     * Clear username and password field inputs
     */
    @FXML
    void Cancel() {
        UsernameTextField.clear();
        PasscodeTextField.clear();
        ConsoleLog.consoleLog.clear();
    }

    /**
     * Validate logIn credentials in database
     * @param event login button click
     * @throws SQLException sql connection error
     */
    @FXML
    void LoginRequest(MouseEvent event) throws SQLException {
        if (UsernameTextField.getLength() > 0 && PasscodeTextField.getLength() > 0) {
            ConsoleLog.setConsoleLog("Validating Login Credential");
            ValidateLogin(event);
        } else if (UsernameTextField.getLength() == 0 && PasscodeTextField.getLength() > 0)
            LoginMessageLabel.setText("Please enter Username");
        else if (UsernameTextField.getLength() > 0 && PasscodeTextField.getLength() == 0)
            LoginMessageLabel.setText("Please enter passcode");
        else
            LoginMessageLabel.setText("Please enter username and passcode");
    }

    /**
     * Calls login button on click when pressed enter
     * @param event enter key press
     */
    @FXML
    void PasswordTextField_KeyPressed(KeyEvent event) {
//        LoginRequest(MouseEvent event);
    }

    /**
     * Login credential validation method
     * @param event login on mouse click
     * @throws SQLException error
     */
    private void ValidateLogin(MouseEvent event) throws SQLException {
        ConsoleLog.setConsoleLog("\nLogging in....");

        String username, passcode;
        username = UsernameTextField.getText();
        passcode = PasscodeTextField.getText();

        ConsoleLog.setConsoleLog("\nLogin Credentials -> Username (" + username + "), Passcode(" + passcode + ")");

        Connection connection = DatabaseConnection.getInstance().getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        String sql_validate = "select accountType from employee where username = '" + username + "' and passcode = '" + passcode + "'";

        ConsoleLog.setConsoleLog("\nConnection: " + connection.toString());
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql_validate);

            ConsoleLog.setConsoleLog("\nChecking Database");

                if (resultSet.next()){
                    int accountType = resultSet.getInt("accountType");
                    Main.loadDashboard(event, accountType);
                    Main.AccountInfo = new global_variable(username, passcode);
                    ConsoleLog.setConsoleLog("\nDashboard Load");
                } else {
                    LoginMessageLabel.setText("Login failed!");
                    ConsoleLog.setConsoleLog("\n\nLogin Credentials not found in database");
                }
        } finally {
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (SQLException e){
                ConsoleLog.setConsoleLog("\nSQL Exception: " + e.toString());
                System.out.println(e);
            }
        }
    }
}