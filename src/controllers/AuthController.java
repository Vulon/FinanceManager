package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import utills.DatabaseManager;
import utills.HTTPMessenger;
import utills.SceneManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AuthController {
    @FXML
    private TextField loginRegField;
    @FXML
    private PasswordField passRegField;
    @FXML
    private PasswordField pass2RegField;
    @FXML
    private Button signUPButton;
    @FXML
    private TextField loginINfield;
    @FXML
    private PasswordField passINField;
    @FXML
    private CheckBox rememberPass;
    @FXML
    private Button signINButton;
    @FXML
    private Label errorMsg1;
    @FXML
    private Label errorMsg2;
    private static SceneManager sceneManager;
    public static void setUpSceneManager(SceneManager manager){
        sceneManager = manager;
    }

    @FXML
    private void signINHandler() {
        signINButton.setDisable(true);
        String login = loginINfield.getText();
        String password = passINField.getText();
        int response = HTTPMessenger.authLogin(login, password);
        switch (response) {
            case 200: {
                sceneManager.activateScreen("main");
                return;
            }
            case 404: {
                signINButton.setDisable(false);
                errorMsg2.setVisible(true);
                errorMsg2.setText("Login does not match password");
                passINField.setText("");
                return;
            }
            case 500: {
                signINButton.setDisable(false);
                errorMsg2.setVisible(true);
                errorMsg2.setText("Network connection trouble");
                return;
            }
        }
        //TODO change sign IN handler
    }
    @FXML
    private void testHandler(){
        System.out.println("USING SELECT");


        sceneManager.activateScreen("main");
    }
    @FXML
    private void signUPHandler() {
        String login = loginRegField.getText();
        String password1 = passRegField.getText();
        String password2 = pass2RegField.getText();
        signUPButton.setDisable(true);
        if (password1.equals(password2)) {
            int response = HTTPMessenger.authRegister(login, password1);
            switch (response) {
                case 200: {
                    errorMsg1.setVisible(true);
                    errorMsg1.setTextFill(Color.GREEN);
                    errorMsg1.setText("Registration complete");
                    return;
                }
                case 409: {
                    signUPButton.setDisable(false);
                    errorMsg1.setVisible(true);
                    errorMsg1.setText("Username is already occupied");
                }
                case 500: {
                    signUPButton.setDisable(false);
                    errorMsg1.setVisible(true);
                    errorMsg1.setText("Network connection trouble");
                    return;
                }
            }
        } else {
            signUPButton.setDisable(false);
            errorMsg1.setVisible(true);
            errorMsg1.setText("Passwords do not match");
        }
    }
}
