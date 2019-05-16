package controllers;

import com.jfoenix.controls.JFXTimePicker;
import com.sun.org.apache.xpath.internal.operations.Bool;
import dataStructure.Category;
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
import utills.HTTPMessenger;
import utills.XMLParser;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;


public class TransactionDialogController implements Initializable {
    @FXML private TextField amountField;
//    @FXML private TextField timeField;    Depreciated, use
                                            /**{@link TransactionDialogController#timePicker}*/
    @FXML private TextArea noteArea;
    @FXML private Button applyButton;

    @FXML private Button cancelButton;
    @FXML private ListView<Category> spendCategoryList;
    @FXML private ListView<Category> incomeCategoryList;
    @FXML private DatePicker datePicker;
    @FXML private JFXTimePicker timePicker;

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
//    private boolean isTimeCorrect;
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
        datePicker.setValue(LocalDate.now());
        timePicker.setValue(LocalTime.now());
        timePicker.set24HourView(true);

        applyButton.setDisable(true);
        datePicker.requestFocus();

        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            amountFieldHandler();
        });
//        isTimeCorrect = false;
    }

//    No Longer required, as Implemented method itself
    /** {@link TransactionDialogController#amountFieldHandler()}*/

//    private boolean isNumber(String line){
//        for(char c : line.toCharArray()){
//            if(!Character.isDigit(c)){
//                return false;
//            }
//        }
//        return true;
//    }
    @FXML
    private void amountFieldHandler(){
        //Improved approach that will handle decimal points, as well
        try{
            Double.valueOf(amountField.getText());
            // Is Valid
            amountField.setStyle("-fx-text-fill: green");
            applyButton.setDisable(false);
        }
        catch (Exception ex){
            // Is Invalid
            amountField.setStyle("-fx-text-fill: red");
            applyButton.setDisable(true);
        }

//        if(isNumber(amountField.getText())){
//            amountField.setStyle("-fx-text-fill: green");
//            applyButton.setDisable(false);
//        }else{
//            amountField.setStyle("-fx-text-fill: red");
//            applyButton.setDisable(true);
//        }
    }


// No longer needed, as the Time Picker control handles these methods
    /** {@link TransactionDialogController#timePicker} */
//    @FXML
//    private void timeFieldHandler(){
//        if(checkTime()){
//            timeField.setStyle("-fx-text-fill: green");
//            isTimeCorrect = true;
//        }else{
//            timeField.setStyle("-fx-text-fill: red");
//            isTimeCorrect = false;
//        }
//    }

//    private boolean checkTime(){
//        boolean flag = true;
//        String line = timeField.getText();
//        System.out.println("The time field is " + line);
//        if(line.length() != 5){
//            flag = false;
//            System.out.println("Lenght is wrong");
//        }
//
//        if('0' <=line.charAt(0) && line.charAt(0) <= '1'){
//            if('9' < line.charAt(1) || line.charAt(1) < '0'){
//                return false;
//            }
//        }else if(line.charAt(0) == '2'){
//            if('0' > line.charAt(1)  || line.charAt(1) > '4'){
//                return false;
//            }
//        }else {
//            return false;
//        }
//        if('0' > line.charAt(3) || line.charAt(3) > '5'){
//            return false;
//        }
//        if('0' > line.charAt(4) || line.charAt(4) > '9'){
//            return false;
//        }
//
//        return flag;
//    }
    @FXML
    private void applyButtonHandler(ActionEvent actionEvent){

                                    /** {@link TransactionDialogController#initialize} */
//        amountFieldHandler();     No Longer Needed as I have added a Listener in the initialize this method

        /* If apply button is disabled, then how would we get into this method in the first place..... -_-  */
//        if (applyButton.isDisabled()){
//
//        }else

        if(selectedCategory == null) {
//            selectedCategory = spendCategories.get(0);  /*What if there are no categories?, then the app will crash? */

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Incomplete Form");
            alert.setHeaderText("Category not selected");
            alert.setContentText("Please select a category from the left panel to proceed.");
            alert.showAndWait();

        } else {
            LocalDate localDate = datePicker.getValue();
            GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
//            System.out.println("Time is ");
//            System.out.println(getHours());
//            System.out.println(getMins());
            calendar.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth(),
                    timePicker.getValue().getHour(),
                    timePicker.getValue().getMinute());


            Transaction transaction = new Transaction(Double.parseDouble(amountField.getText()), selectedCategory,
                    calendar.getTimeInMillis(),
                    noteArea.getText());
            transaction.setCalendar(calendar);
            transactionObservableList.add(transaction);

            //TODO re-enable this
            //HTTPMessenger.sendNewTransaction(transaction);

            cancelButtonHandler(actionEvent);

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

// No longer needed, as the Time Picker control handles these methods
                        /** {@link TransactionDialogController#timePicker} */
//    private int getHours(){
//
//        if(isTimeCorrect){
//            return Integer.parseInt(timeField.getText().substring(0, 2));
//        }else{
//            return 0;
//        }
//    }
//    private int getMins(){
//        if(isTimeCorrect){
//            return Integer.parseInt(timeField.getText().substring(3, 5));
//        }else{
//            return 0;
//        }
//    }
}
