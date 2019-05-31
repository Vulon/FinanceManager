package controllers;

import com.jfoenix.controls.JFXTimePicker;
import dataStructure.Category;
import dataStructure.ObservableData;
import dataStructure.Transaction;
import dataStructure.categoryCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import utills.DatabaseManager;
import utills.HTTPMessenger;

import java.net.Inet4Address;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;


public class TransactionDialogController implements Initializable {
    public static final int CREATE_MODE = 0;
    public static final int MODIFY_MODE = 1;
    private int MODE;
    private int transactionID;
    @FXML private TextField amountField;
    @FXML private TextArea noteArea;
    @FXML private Button applyButton;

    @FXML private Button deleteButton;
    @FXML private ListView<Category> spendCategoryList;
    @FXML private ListView<Category> incomeCategoryList;
    DatabaseManager databaseManager;
    @FXML private DatePicker datePicker;
    @FXML private JFXTimePicker timePicker;
    private ObservableData observable;
    private Category selectedCategory;

    @FXML public void handleSpendSelection(MouseEvent arg0) {
        selectedCategory = spendCategoryList.getSelectionModel().getSelectedItem();
    }
    @FXML public void handleIncomeSelection(MouseEvent arg0) {
        selectedCategory = incomeCategoryList.getSelectionModel().getSelectedItem();
    }
    private ArrayList<Category> spendCategories;
    private ArrayList<Category> incomeCategories;


    public void setReturnReference(ObservableData observable, int mode, int transactionID){
        this.observable = observable;
        this.MODE = mode;
        this.transactionID = transactionID;
        databaseManager = DatabaseManager.getInstance();
        spendCategories = databaseManager.getExpenseCategories();
        incomeCategories = databaseManager.getIncomeCategories();
        if (mode == CREATE_MODE){
            deleteButton.setVisible(false);
        }
        spendCategoryList.setItems(FXCollections.observableList(spendCategories));
        spendCategoryList.setCellFactory(categoryList -> new categoryCell());
        incomeCategoryList.setItems(FXCollections.observableList(incomeCategories));
        incomeCategoryList.setCellFactory(categoryList -> new categoryCell());
        selectedCategory = null;
        datePicker.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override public String toString(LocalDate localDate) {
                if(localDate==null) return "";
                return dateTimeFormatter.format(localDate);
            }
            @Override public LocalDate fromString(String dateString) {
                if(dateString==null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
        timePicker.setConverter(new StringConverter<LocalTime>() {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("kk:mm");
            @Override public String toString(LocalTime localTime) {
                if(localTime==null) return "";
                return dateTimeFormatter.format(localTime);
            }
            @Override public LocalTime fromString(String timeString) {
                if(timeString==null || timeString.trim().isEmpty()) {
                    return null;
                }
                return LocalTime.parse(timeString,dateTimeFormatter);
            }
        });
        if(MODE == CREATE_MODE){
            datePicker.setValue(LocalDate.now());
            timePicker.setValue(LocalTime.now());
            applyButton.setDisable(true);
        }else{
            Transaction transaction = databaseManager.getTransaction(transactionID);
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTimeInMillis(transaction.getDate());
            datePicker.setValue(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
            timePicker.setValue(LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0));
            selectedCategory = transaction.getCategory();
            amountField.setText(Double.toString(transaction.getAmount()));
            noteArea.setText(transaction.getNote());
            applyButton.setDisable(false);
        }

        timePicker.set24HourView(true);


        datePicker.requestFocus();

        amountField.textProperty().addListener((_observable, oldValue, newValue) -> {
            amountFieldHandler();
        });
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

//
    @FXML
    private void amountFieldHandler(){
        try{
            Double.valueOf(amountField.getText());
            amountField.setStyle("-fx-text-fill: green");
            applyButton.setDisable(false);
        }
        catch (Exception ex){
            amountField.setStyle("-fx-text-fill: red");
            applyButton.setDisable(true);
        }

//
    }

    @FXML
    private void applyButtonHandler(ActionEvent actionEvent){
        if(selectedCategory == null) {//
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Incomplete Form");
            alert.setHeaderText("Category not selected");
            alert.setContentText("Please select a category from the left panel to proceed.");
            alert.showAndWait();
        } else {
            LocalDate localDate = datePicker.getValue();
            GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
            calendar.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth(),
                    timePicker.getValue().getHour(),
                    timePicker.getValue().getMinute());

            Transaction transaction = new Transaction(transactionID, Double.parseDouble(amountField.getText()), selectedCategory,
                    calendar.getTimeInMillis(),
                    noteArea.getText());
            if(MODE == CREATE_MODE){
                System.out.println("CREATE MODE");
                databaseManager.insertTransaction(transaction);
                //TODO re-enable this
                //HTTPMessenger.sendNewTransaction(transaction);
                observable.changed =true;
                observable.notifyObservers();
            }else{
                Transaction old = databaseManager.getTransaction(transactionID);
                if (!old.equals(transaction)){
                    System.out.println("UPDATING TRANSACTION");
                    databaseManager.updateTransaction(transaction);
                    HTTPMessenger httpMessenger = HTTPMessenger.getInstance();
                    httpMessenger.updateTransaction(transaction);
                    observable.changed =true;
                    observable.notifyObservers();
                }else{
                    System.out.println("NOT UPDATING TRANSACTION");
                }
            }





            Node source = (Node)  actionEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void deleteButtonHandler(ActionEvent actionEvent){
        if(MODE == MODIFY_MODE){
            databaseManager.deleteByID(transactionID);
            HTTPMessenger httpMessenger = HTTPMessenger.getInstance();
            httpMessenger.deleteTransaction(transactionID);
            observable.changed =true;
            observable.notifyObservers();
        }
        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
