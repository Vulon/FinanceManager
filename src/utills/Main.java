package utills;

import controllers.AuthController;
import dataStructure.Category;
import dataStructure.Transaction;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {
    private AuthController authController; //Controller for sign In\Up scene
    private Scene mainScene; //Main Scene. It is needed to change scenes, maybe i'll delete it soon

    SceneManager sceneManager; //Manager, that holds scenes and can change them
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxmlLayouts/sample.fxml"));
        Parent root = loader.load();
        mainScene = new Scene(root, 800, 600);
        sceneManager = new SceneManager(mainScene);
        sceneManager.addScreen("auth", (AnchorPane)FXMLLoader.load(getClass().getResource("../fxmlLayouts/sample.fxml")));
        sceneManager.addScreen("main", (GridPane)FXMLLoader.load(getClass().getResource("../fxmlLayouts/mainWindow.fxml")));
        primaryStage.setScene(mainScene);
        authController = loader.getController();
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(530);
        primaryStage.setTitle("Finance manager");
        authController.setUpSceneManager(sceneManager);

        primaryStage.show();
    }






    public static void main(String[] args) {
        launch(args);

    }
}
