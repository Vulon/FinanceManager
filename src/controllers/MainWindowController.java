package controllers;

import Debug.MyRandomGenerator;
import dataStructure.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utills.HTTPMessenger;
import utills.XMLParser;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class MainWindowController implements Initializable {


    @FXML
    private ListView<Transaction> listView;
    @FXML
    private ChoiceBox monthPicker;
    @FXML
    private ListView<Category> categoryList;
    @FXML
    private PieChart pieChart;
    @FXML
    private VBox chartBox;
    private BarChart barChart;
    private ObservableList<Transaction> transactionObservableList;
    private ObservableList<Transaction> thisMonthTransactions;

    private ObservableList<Category> categories;
    private ObservableList<Month> monthsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //HTTPMessenger.loadData(transactions,categories); //TODO add actual data from server

        if(!monthsList.contains(Month.values()[GregorianCalendar.getInstance().get(Calendar.MONTH)])){
            monthsList.add(Month.values()[GregorianCalendar.getInstance().get(Calendar.MONTH)]);
            monthPicker.setValue(monthsList.get(monthsList.size() - 1));
        }else{
            monthPicker.setValue(monthsList.get(monthsList.size() - 1));
        }
        changeThisMothTransactions();


        listView.setItems(thisMonthTransactions);
        listView.setCellFactory(listView -> new TransActionCell());
        categoryList.setItems(categories);
        categoryList.setCellFactory(categoryList -> new categoryCell());
        listView.setFixedCellSize(110);
        initializePieChart();
        initializeMonthPicker();
        initializeBarChart();
        transactionObservableList.addListener(new ListChangeListener<Transaction>() {
            @Override
            public void onChanged(Change<? extends Transaction> c) {
                System.out.println("TRIGGERED");
                initializePieChart();

                updateBarChart();
                updateMonthPicker();
            }
        });
    }

    public MainWindowController() {
        transactionObservableList = FXCollections.observableArrayList();
        monthsList = FXCollections.observableArrayList();
        categories = FXCollections.observableArrayList();
        thisMonthTransactions = FXCollections.observableArrayList();

        //MyRandomGenerator.fillBaseCategories(categories);//TODO change category generation
        try{
            XMLParser parser = new XMLParser(new File(Paths.get("src", "config", "cat.xml").toUri()));
            categories.addAll(parser.getNesIncomes());
            categories.addAll(parser.getNesExpenses());
        }catch (Exception e){
            MyRandomGenerator.fillBaseCategories(categories);
            e.printStackTrace();
        }
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
    private void initializePieChart(){
        pieChart.getData().clear();
        int chartData[] = new int[categories.size() + 1];
        for(int  i : chartData){
            i = 0;
        }

        for(Transaction tr : thisMonthTransactions){
            if(!tr.getCategory().checkIsIncome()){
                chartData[tr.getCategory().getID()]+= tr.getAmount();
            }
        }
        for(Category category : categories){
            if(chartData[category.getID()] > 0){
                PieChart.Data slice = new PieChart.Data(category.getName(), chartData[category.getID()]);
                pieChart.getData().add(slice);
                slice.getNode().setStyle("-fx-pie-color: " + category.getColor());
            }
        }

    }
    private void updateMonthPicker(){
        for(Transaction tr : transactionObservableList){
            if(monthsList.contains(Month.values()[tr.getCalendar().get(Calendar.MONTH)])){
            }else{
                monthsList.add(Month.values()[tr.getCalendar().get(Calendar.MONTH)]);
            }
        }
    }
    private void initializeMonthPicker(){
        updateMonthPicker();
        monthPicker.setItems(monthsList);
        monthPicker.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                monthPicker.setValue(monthPicker.getItems().get(newValue.intValue()));
                changeThisMothTransactions();
                initializePieChart();
            }
        });
    }
    private void changeThisMothTransactions(){
        Month selectedM = (Month)monthPicker.getValue();
        System.out.println("selected value : " + selectedM.ordinal());
        System.out.println("Selected " + selectedM);
        thisMonthTransactions.clear();
        for(Transaction tr : transactionObservableList){
            if(tr.getCalendar().get(Calendar.MONTH) == selectedM.ordinal()){
                thisMonthTransactions.add(tr);
            }
        }

    }
    private void updateBarChart(){
        barChart.getData().clear();
        XYChart.Series<String, Number> incSeries = new XYChart.Series<String, Number>();
        incSeries.setName("Income");
        XYChart.Series<String, Number> expSeries = new XYChart.Series<String, Number>();
        expSeries.setName("Expense");
        for(int i = 0; i < monthPicker.getItems().size(); i++){
            int totalExpenses = 0;
            int totalIncomes = 0;
            for(Transaction tr : transactionObservableList){
                if(tr.getCalendar().get(Calendar.MONTH) == ((Month)monthPicker.getItems().get(i)).ordinal()){
                    if(tr.getCategory().checkIsIncome()){
                        totalIncomes += tr.getAmount();
                    }else{
                        totalExpenses += tr.getAmount();
                    }
                }
            }
            Month month = (Month)monthPicker.getItems().get(i);
            incSeries.getData().add(new XYChart.Data<String, Number>(month.toString(), totalIncomes));
            expSeries.getData().add(new XYChart.Data<String, Number>(month.toString(), totalExpenses));
        }
        barChart.getData().addAll(incSeries, expSeries);
    }
    private void initializeBarChart(){

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Money");
        barChart = new BarChart(xAxis, yAxis);
        chartBox.getChildren().add(0, barChart);
        updateBarChart();
    }

}
