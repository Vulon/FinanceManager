package controllers;

import dataStructure.Category;
import dataStructure.Transaction;
import dataStructure.categoryCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utills.HTTPMessenger;
import utills.XMLParser;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;


public class TransactionDialogController implements Initializable {
    @FXML private TextField amountField;
    @FXML private TextField timeField;
    @FXML private TextArea noteArea;
    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;
    @FXML
    private ListView<Category> spendCategoryList;
    @FXML
    private ListView<Category> incomeCategoryList;
    @FXML
    private DatePicker datePicker;
    private Category selectedCategory;
    @FXML public void handleSpendSelection(MouseEvent arg0) {
        selectedCategory = spendCategoryList.getSelectionModel().getSelectedItem();
    }
    @FXML public void handleIncomeSelection(MouseEvent arg0) {
        selectedCategory = incomeCategoryList.getSelectionModel().getSelectedItem();
    }
    private ObservableList<Transaction> transactionObservableList;
    private ArrayList<Category> spendCategories;
    private ArrayList<Category> incomeCategories;
    private boolean isTimeCorrect;
    public void setReturnReference(ObservableList<Transaction> transactionObservableList, ObservableList<Category> categories){
        this.transactionObservableList = transactionObservableList;
        try{
            XMLParser parser = new XMLParser(new File(Paths.get("src", "config", "cat.xml").toUri()));
            spendCategories = parser.getNesExpenses();
            incomeCategories = parser.getNesIncomes();
        }catch (Exception e){
            e.printStackTrace();
            spendCategories = new ArrayList<>();
            incomeCategories = new ArrayList<>();
        }


        spendCategoryList.setItems(FXCollections.observableList(spendCategories));
        spendCategoryList.setCellFactory(categoryList -> new categoryCell());
        incomeCategoryList.setItems(FXCollections.observableList(incomeCategories));
        incomeCategoryList.setCellFactory(categoryList -> new categoryCell());

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedCategory = null;
        datePicker.setValue(LocalDate.now());
        isTimeCorrect = false;
    }
    private boolean isNumber(String line){
        for(char c : line.toCharArray()){
            if(!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }
    @FXML
    private void amountFieldHandler(){
        if(isNumber(amountField.getText())){
            amountField.setStyle("-fx-text-fill: green");
            applyButton.setDisable(false);
        }else{
            amountField.setStyle("-fx-text-fill: red");
            applyButton.setDisable(true);
        }
    }
    @FXML
    private void timeFieldHandler(){
        if(checkTime()){
            timeField.setStyle("-fx-text-fill: green");
            isTimeCorrect = true;
        }else{
            timeField.setStyle("-fx-text-fill: red");
            isTimeCorrect = false;
        }
    }
    private boolean checkTime(){
        boolean flag = true;
        String line = timeField.getText();
        System.out.println("The time field is " + line);
        if(line.length() != 5){
            flag = false;
            System.out.println("Lenght is wrong");
        }

        if('0' <=line.charAt(0) && line.charAt(0) <= '1'){
            if('9' < line.charAt(1) || line.charAt(1) < '0'){
                return false;
            }
        }else if(line.charAt(0) == '2'){
            if('0' > line.charAt(1)  || line.charAt(1) > '4'){
                return false;
            }
        }else {
            return false;
        }
        if('0' > line.charAt(3) || line.charAt(3) > '5'){
            return false;
        }
        if('0' > line.charAt(4) || line.charAt(4) > '9'){
            return false;
        }

        return flag;
    }
    @FXML
    private void applyButtonHandler(ActionEvent actionEvent){
        amountFieldHandler();

        if(applyButton.isDisabled()){
            return;
        }else{
            if(selectedCategory == null){
                selectedCategory = spendCategories.get(0);
            }
            LocalDate localDate = datePicker.getValue();
            GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
            System.out.println("Time is ");
            System.out.println(getHours());
            System.out.println(getMins());
            calendar.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth(), getHours(), getMins());


            Transaction transaction = new Transaction(Integer.parseInt(amountField.getText()), selectedCategory,
                    calendar.getTimeInMillis(),
                    noteArea.getText());
            transaction.setCalendar(calendar);
            transactionObservableList.add(transaction);

            HTTPMessenger.sendNewTransaction(transaction);

            Node source = (Node)  actionEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void cancelButtonHandler(ActionEvent actionEvent){
        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }


    private int getHours(){

        if(isTimeCorrect){
            return Integer.parseInt(timeField.getText().substring(0, 2));
        }else{
            return 0;
        }
    }
    private int getMins(){
        if(isTimeCorrect){
            return Integer.parseInt(timeField.getText().substring(3, 5));
        }else{
            return 0;
        }
    }
}
