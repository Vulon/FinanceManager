package controllers;

import dataStructure.Category;
import dataStructure.Transaction;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utills.HTTPMessenger;

import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;


public class TransactionDialogController implements Initializable {
    @FXML private TextField amountField;
    @FXML private TextField dateField;
    @FXML private TextArea noteArea;
    @FXML
    private FlowPane iconPane;
    @FXML
    private Button applyButton;
    @FXML
    private Button cancelButton;
    private ObservableList<Transaction> transactionObservableList;
    public void setReturnReference(ObservableList<Transaction> transactionObservableList){
        this.transactionObservableList = transactionObservableList;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int counter = 1;
        int x = 0;
        int y = 0;
        int columns = 3;
        for(int i = 1; i <= Category.MAXICONCOUNT; i++){
            String imagePath = "/icons/" + Integer.toString(i) + ".png";
            ImageView tempImageView = new ImageView(imagePath);
            tempImageView.setId("imv" + Integer.toString(i));
            tempImageView.setVisible(true);
            iconPane.getChildren().add(tempImageView);

        }

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
    private void dateFieldHandler(){
        if(checkDate(dateField.getText())){
            dateField.setStyle("-fx-text-fill: green");
            applyButton.setDisable(false);

        }else{
            dateField.setStyle("-fx-text-fill: red");
            applyButton.setDisable(true);
        }
    }
    @FXML
    private void applyButtonHandler(ActionEvent actionEvent){
        amountFieldHandler();
        dateFieldHandler();

        if(applyButton.isDisabled()){
            return;
        }else{
            Transaction transaction = new Transaction(Integer.parseInt(amountField.getText()), 0,
                    parseDate(dateField.getText()),
                    noteArea.getText());
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

    private Date parseDate(String line){
        if(line.length() != 16){
            return new Date();
        }
        char[] number = line.toCharArray();
        int day = Integer.parseInt(String.copyValueOf(number, 0, 2));
        int mounth = Integer.parseInt(String.copyValueOf(number, 3, 2));
        int year = Integer.parseInt(String.copyValueOf(number, 6, 4));
        int hours = Integer.parseInt(String.copyValueOf(number, 11, 2));
        int mins = Integer.parseInt(String.copyValueOf(number, 14, 2));

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(year, mounth, day, hours, mins);

        return calendar.getTime();


    }

    private boolean checkDate(String line){
        char[] number = line.toCharArray();
        if(line.length() != 16){
            return false;
        }
        if(number[0] < '0' || number[0] > '3'){
            return false;
        }
        if(!Character.isDigit(number[1])){
            return false;
        }
        if(number[3] < '0' || number[3] > '1'){
            return false;
        }
        if(!Character.isDigit(number[4])){
            return false;
        }
        if(!Character.isDigit(number[6])){
            return false;
        }
        if(!Character.isDigit(number[7])){
            return false;
        }
        if(!Character.isDigit(number[8])){
            return false;
        }
        if(!Character.isDigit(number[9])){
            return false;
        }
        if(number[11] < '0' || number[11] > '2'){
            return false;
        }
        if(!Character.isDigit(number[12])){
            return false;
        }
        if(number[14] < '0' || number[14] > '5'){
            return false;
        }
        if(!Character.isDigit(number[15])){
            return false;
        }
        return true;
    }

    private boolean isNumber(String line){
        for(char c : line.toCharArray()){
            if(!Character.isDigit(c)){
                if(c != '-'){
                    return false;
                }
            }
        }
        return true;
    }
}
