package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import utills.HTTPMessenger;
import utills.SceneManager;

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
    private void signINHandler(){
        signINButton.setDisable(true);
        String login = loginINfield.getText();
        String password = passINField.getText();
        String response = HTTPMessenger.authLogin(login, password);
        if(response.equals("OK")){

        }else if(response.equals("Wrong URL")){
            signINButton.setDisable(false);
            errorMsg2.setVisible(true);
            errorMsg2.setText("Network connection trouble");
        }else if(response.equals("Connection timeout")){
            signINButton.setDisable(false);
            errorMsg2.setVisible(true);
            errorMsg2.setText("Network connection trouble");
        }else if(response.equals("NOTMATCH")){
            signINButton.setDisable(false);
            errorMsg2.setVisible(true);
            errorMsg2.setText("Login does not match password");
            passINField.setText("");
        }
        sceneManager.activateScreen("main"); //TODO change sign IN handler
    }
    @FXML
    private void signUPHandler(){
        String login = loginRegField.getText();
        String password1 = passRegField.getText();
        String password2 = pass2RegField.getText();
        signUPButton.setDisable(true);
        if (password1.equals(password2)){
            String response = HTTPMessenger.authRegister(login, password1);
            if(response.equals("OK")){
                errorMsg1.setVisible(true);
                errorMsg1.setTextFill(Color.GREEN);
                errorMsg1.setText("Registration complete");
            }else if(response.equals("INUSE")){
                signUPButton.setDisable(false);
                errorMsg1.setVisible(true);
                errorMsg1.setText("Username is already occupied");
            }else if(response.equals("Wrong URL")){
                signUPButton.setDisable(false);
                errorMsg1.setVisible(true);
                errorMsg1.setText("Network connection trouble");
            }else if(response.equals("Connection timeout")){
                signUPButton.setDisable(false);
                errorMsg1.setVisible(true);
                errorMsg1.setText("Network connection trouble");
            }
        }else{
            signUPButton.setDisable(false);
            errorMsg1.setVisible(true);
            errorMsg1.setText("Passwords do not match");
        }
    }



}
