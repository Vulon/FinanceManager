package dataStructure;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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

            mLoader.setController(this);
            try {
                mLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            amountLabel.setText(Integer.toString(item.getAmount()) + " р");
            item.calendar.setTime(item.getDate());
            String date = Integer.toString(item.calendar.get(Calendar.DAY_OF_MONTH));
            date += "." + Integer.toString(item.calendar.get(Calendar.MONTH));
            date += "." + Integer.toString(item.calendar.get(Calendar.YEAR));
            date += " " + Integer.toString(item.calendar.get(Calendar.HOUR)) + ":";
            date += Integer.toString(item.calendar.get(Calendar.MINUTE));
            dateLabel.setText(date);
            noteLabel.setText(item.getNote());
            iconView.setStyle("-fx-background-color: transparent");
            String layoutColor = "-fx-background-color: #" + item.getCategory().getColor().toString().substring(2, 10);
            cellLayout.setStyle(layoutColor);
            if (noteLabel.getText().equals("")){
                noteLabel.setVisible(false);
            }else{
                noteLabel.setVisible(true);
            }
            String imagePath = "icons/" + Integer.toString(item.getCategory().getIconId()) + ".png";
            iconView.setImage(new Image(imagePath));

            setText(null);
            setGraphic(cellLayout);
        }
    }
}
