package controllers;

import Debug.MyRandomGenerator;
import com.sun.org.apache.xpath.internal.operations.Bool;
import dataStructure.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import utills.HTTPMessenger;
import utills.XMLParser;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Year;
import java.util.*;

public class MainWindowController implements Initializable {

    @FXML private ListView<Transaction> listView;
    @FXML private ChoiceBox monthPicker;
    @FXML private ChoiceBox yearPicker;
    @FXML private ListView<Category> categoryList;
    @FXML private PieChart pieChart;
    @FXML private VBox chartBox;
    @FXML private Button budgetButton;
    @FXML private Label budgetLabel;
    @FXML private TextField budgetSettings;

    private BarChart barChart;
    private ObservableList<Transaction> transactionObservableList;
    private ObservableList<Transaction> thisMonthTransactions;

    private ObservableList<Category> categories;
    private ObservableList<Month> monthsList;
    private ObservableList<Integer> yearList;
    private Budget budget;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //HTTPMessenger.loadData(transactions,categories); //TODO add actual data from server

        int year;
        if(!yearList.contains(year = GregorianCalendar.getInstance().get(Calendar.YEAR))) {
            yearList.add(year);
        }
        if(!monthsList.contains(Month.values()[GregorianCalendar.getInstance().get(Calendar.MONTH)])){
            monthsList.add(Month.values()[GregorianCalendar.getInstance().get(Calendar.MONTH)]);
//            monthPicker.setValue(monthsList.get(monthsList.size() - 1));
//        }else{
//            monthPicker.setValue(monthsList.get(monthsList.size() - 1));
        }
        monthPicker.setValue(monthsList.get(monthsList.size() - 1));
        yearPicker.setValue(yearList.get(yearList.size() - 1));
        updateTransactionList();


        budget = Budget.getInstance();



        listView.setItems(thisMonthTransactions);
        listView.setCellFactory(listView -> new TransActionCell());
        categoryList.setItems(categories);
        categoryList.setCellFactory(categoryList -> new categoryCell());
        listView.setFixedCellSize(110);
        initializePieChart();
        initializeMonthPicker();
        initializeYearPicker();
        initializeBarChart();
        transactionObservableList.addListener(new ListChangeListener<Transaction>() {
            @Override
            public void onChanged(Change<? extends Transaction> c) {
                System.out.println("TRIGGERED");
                initializePieChart();

                updateYearPicker(); /* Calls the following methods as well:
                                       updateMonthPicker
                                       updateTransactionList
                                       updateBarChart();
                                        */
            }
        });

    }

    public MainWindowController() {
        transactionObservableList = FXCollections.observableArrayList();
        monthsList = FXCollections.observableArrayList();
        yearList = FXCollections.observableArrayList();
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
            Scene mainScene = new Scene(ntLoader.load(), 700, 580);     //Changed from 600: 400
            newTransactionDialog.setMinHeight(580);
            newTransactionDialog.setMinWidth(700);
            newTransactionDialog.setScene(mainScene);

            TransactionDialogController newTransactionController = ntLoader.<TransactionDialogController>getController();
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

        for(Category category : categories){
            if(chartData[category.getID()] > 0){
                PieChart.Data slice = new PieChart.Data(category.getName(), chartData[category.getID()]);
                pieChart.getData().add(slice);
                slice.getNode().setStyle("-fx-pie-color: " + category.getColor());
            }
        }
        updateBudget();

    }


    /* The bellow two methods, are utility methods for the newly added*//**{@link MainWindowController#yearPicker*//*
        they provide similar functionality as the methods implemented for the monthPicker*/
    private void initializeYearPicker(){
        updateYearPicker();

        yearPicker.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(-1)) {
                yearPicker.setValue(yearPicker.getItems().get(newValue.intValue()));
                updateMonthPicker();
                updateBarChart();
            }
        });
    }
    private void updateYearPicker(){
        int prvVal = (int)yearPicker.getValue();    /* will be used later */

        /* Clear the list and repopulate the list from the beginning,
        in a case a transaction was deleted and as such there may be no other transaction with such year */
        yearList.clear();

        for(Transaction tr : transactionObservableList){
            if(!yearList.contains(tr.getCalendar().get(Calendar.YEAR))){
                yearList.add(tr.getCalendar().get(Calendar.YEAR));
            }
        }

        /* we sort the list, so that it looks pretty and organized :D */
        Comparator<Integer> comparator = Comparator.comparingInt(Integer::intValue);
        yearList.sort(comparator);

        /* Here we save the previous value of the yearPicker before re-setting it's item List,
         * then we check if the new set list has the saved value,
         * if yes, we set it back,
         * else we set the picker to the last item (if it's not empty) */
        yearPicker.setItems(yearList);
        if (yearList.contains(prvVal)) {
            yearPicker.setValue(prvVal);

        } else if(yearList.size() > 0) {
            yearPicker.setValue(yearList.get(yearList.size() - 1));
        }
    }

    private void initializeMonthPicker(){
        updateMonthPicker();

//        monthPicker.setItems(monthsList);     /* Moved to updateMonthPicker*/
        monthPicker.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!newValue.equals(-1)) {
                    monthPicker.setValue(monthPicker.getItems().get(newValue.intValue()));
                    updateTransactionList();
                    initializePieChart();
                }
            }
        });
    }
    private void updateMonthPicker(){
        Month prvVal = (Month)monthPicker.getValue();

        monthsList.clear();
        for(Transaction tr : transactionObservableList){
//            if(monthsList.contains(Month.values()[tr.getCalendar().get(Calendar.MONTH)])){
//            }else{
//                monthsList.add(Month.values()[tr.getCalendar().get(Calendar.MONTH)]);
//            }

            if ((!monthsList.contains(Month.values()[tr.getCalendar().get(Calendar.MONTH)]))
                    && (tr.getCalendar().get(Calendar.YEAR) == (int)yearPicker.getValue()))
            {
                monthsList.add(Month.values()[tr.getCalendar().get(Calendar.MONTH)]);
            }
        }

        /* TODO: Implement a sort method for the "Month" enum type or simply replace with a int type List*/

        monthPicker.setItems(monthsList);
        if (monthsList.contains(prvVal)) {
            monthPicker.setValue(prvVal);

        } else if(monthsList.size() > 0) { /* Just in case, shouldn't really ever be an empty list*/
            monthPicker.setValue(monthsList.get(monthsList.size() - 1));
        }
    }


    /* Renamed from changeThisMothTransactions to updateTransactionList*/
    private void updateTransactionList(){
        Month selectedM = (Month)monthPicker.getValue();
        int selectedY = (int)yearPicker.getValue();
        System.out.println("selected value : " + selectedY + " " + selectedM + "(" + selectedM.ordinal() + ")");
//        System.out.println("Selected " + selectedM);
        thisMonthTransactions.clear();
        for(Transaction tr : transactionObservableList){
            if(tr.getCalendar().get(Calendar.MONTH) == selectedM.ordinal() && (tr.getCalendar().get(Calendar.YEAR) == selectedY)){
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
                if((tr.getCalendar().get(Calendar.MONTH) == ((Month)monthPicker.getItems().get(i)).ordinal())
                                            && (tr.getCalendar().get(Calendar.YEAR) == (int)yearPicker.getValue())){    /* added so that the bar chart reflects the correct year */
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


    //Deya started programming HERE:
    public void addCategory(ActionEvent actionEvent) {
    }
    private void updateBudget(){
        int totalSpent = 0;
        for(Transaction tr : thisMonthTransactions){
            if(!tr.getCategory().checkIsIncome()){
                totalSpent += tr.getAmount();
            }
        }
        Integer monthKey = ((Month)monthPicker.getSelectionModel().getSelectedItem()).ordinal();
        Integer yearKey = (Integer)yearPicker.getSelectionModel().getSelectedItem();
        System.out.println("Budget check: " + monthKey.toString());
        if (budget.history.containsKey(new Pair<>(monthKey, yearKey))){
            budget.currentLimit = budget.history.get(new Pair<>(monthKey, yearKey));
        }else{
            budget.currentLimit = budget.defaultValue;
        }
        if(totalSpent > budget.currentLimit){
            budgetLabel.setStyle("-fx-text-fill: red");
        }else{
            budgetLabel.setStyle("-fx-text-fill: green");
        }
        budgetLabel.setText("Spent this month: " + Integer.toString(totalSpent) + "/ limit: " + Integer.toString(budget.currentLimit));
    }
    @FXML private void budgetButtonHandler(){
        Integer monthKey = ((Month)monthPicker.getSelectionModel().getSelectedItem()).ordinal();
        Integer yearKey = (Integer)yearPicker.getSelectionModel().getSelectedItem();

        try{
            int limit = Integer.valueOf(budgetSettings.getText());
            if(budget.history.containsKey(new Pair(monthKey, yearKey))){
                budget.history.replace(new Pair<>(monthKey, yearKey), limit);
            }else{
                budget.history.put(new Pair<>(monthKey, yearKey), limit);
            }
            updateBudget();
        }catch (Exception e){

        }

    }
}
