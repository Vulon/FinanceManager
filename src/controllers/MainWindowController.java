package controllers;

import Debug.MyRandomGenerator;
import dataStructure.Category;
import dataStructure.TransActionCell;
import dataStructure.categoryCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import dataStructure.Transaction;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utills.HTTPMessenger;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {


    @FXML
    private ListView<Transaction> listView;
    @FXML
    private ListView<Category> categoryList;
    private ObservableList<Transaction> transactionObservableList;

    private ObservableList<Category> categories;
    //private ArrayList<Transaction> transactions; //same

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //HTTPMessenger.loadData(transactions,categories); //TODO add actual data from server
        //transactionObservableList.setAll(transactions);

        listView.setItems(transactionObservableList);
        listView.setCellFactory(listView -> new TransActionCell());
        categoryList.setItems(categories);
        categoryList.setCellFactory(categoryList -> new categoryCell());
    }

    public MainWindowController() {
        transactionObservableList = FXCollections.observableArrayList();
        categories = FXCollections.observableArrayList();
        MyRandomGenerator.fillBaseCategories(categories);//TODO change category generation
        for(int i = 0; i < 10; i++){
            transactionObservableList.add(MyRandomGenerator.generateTransaction(categories));
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

            newTransactionController.setReturnReference(transactionObservableList, categories);

            newTransactionDialog.show();
        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }
}
