package controllers;

import Debug.MyRandomGenerator;
import dataStructure.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import dataStructure.Transaction;
import javafx.stage.Stage;
import utills.HTTPMessenger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {


    @FXML
    private ListView<Transaction> listView;
    private ObservableList<Transaction> transactionObservableList;

    private HashMap<Integer, Category> categories; //HashMap with categories, should initialize after sign In via HTTPMessenger
    private ArrayList<Transaction> transactions; //same

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setItems(transactionObservableList);
        listView.setCellFactory(studentListView -> new TransActionCell());
        HTTPMessenger.loadData(transactions,categories);

    }

    public MainWindowController() {
        transactionObservableList = FXCollections.observableArrayList();
        for(int i = 0; i < 10; i++){
            transactionObservableList.add(MyRandomGenerator.generateTransaction());
        }
    }
    @FXML
    private void addTransaction(){
        try{
            Stage newTransactionDialog = new Stage();
            FXMLLoader ntLoader = new FXMLLoader(getClass().getResource("../fxmlLayouts/newTransactionDialog.fxml"));
            Scene mainScene = new Scene(ntLoader.load(), 600, 400);
            newTransactionDialog.setMinHeight(400);
            newTransactionDialog.setMinWidth(600);

            TransactionDialogController newTransactionController = ntLoader.<TransactionDialogController>getController();

            newTransactionDialog.setScene(mainScene);

            newTransactionController.setReturnReference(transactionObservableList);

            newTransactionDialog.show();
        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }
}
