package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import dataStructure.Transaction;

import java.io.IOException;
import java.util.Calendar;

public class TransActionCell extends ListCell<Transaction> {
    @FXML
    private ImageView iconView;
    @FXML
    private Label amountLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label noteLabel;
    @FXML
    private VBox cellLayout;
    FXMLLoader mLoader;

    @Override
    protected void updateItem(Transaction item, boolean empty) {
        super.updateItem(item, empty);;
        if(empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLoader == null) {
            }
            mLoader = new FXMLLoader(getClass().getResource("../fxmlLayouts/transActionCell.fxml"));
            //mLoader.setLocation();
            mLoader.setController(this);
            try {
                mLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            amountLabel.setText(Integer.toString(item.getAmount()) + " р");
            item.calendar.setTime(item.getDate());
            String date = Integer.toString(item.calendar.get(Calendar.DAY_OF_YEAR));
            date += "." + Integer.toString(item.calendar.get(Calendar.MONTH));
            date += "." + Integer.toString(item.calendar.get(Calendar.YEAR));
            date += " " + Integer.toString(item.calendar.get(Calendar.HOUR)) + ":";
            date += Integer.toString(item.calendar.get(Calendar.MINUTE));
            dateLabel.setText(date);
            noteLabel.setText(item.getNote());
            if (noteLabel.getText().equals("")){
                noteLabel.setVisible(false);
            }else{
                noteLabel.setVisible(true);
            }
            //iconView.setImage(); //TODO FIX ICON ON CELL

            setText(null);
            setGraphic(cellLayout);
        }
    }
}
