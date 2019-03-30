package dataStructure;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.spi.CalendarNameProvider;

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
    @FXML
    private BorderPane iconPane;

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
            GregorianCalendar calendar = item.getCalendar();
            String date = Integer.toString(calendar.get(Calendar.YEAR));
            date += "." + Integer.toString(1 + calendar.get(Calendar.MONTH));
            date += "." + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            date += " " + Integer.toString(calendar.get(Calendar.HOUR)) + ":";
            date += Integer.toString(calendar.get(Calendar.MINUTE));
            dateLabel.setText(date);
            //dateLabel.setTextFill(Color.WHITE);
            //noteLabel.setTextFill(Color.WHITE);
            if(item.getCategory().checkIsIncome()){
                amountLabel.setTextFill(Color.GREEN);
            }else{
                amountLabel.setTextFill(Color.RED);
            }
            noteLabel.setText(item.getNote());
            String layoutColor = "-fx-background-color: " + item.getCategory().getColor();
            //iconView.setStyle("-fx-background-color: transparent");
            //iconView.setStyle(layoutColor);
            iconPane.setStyle(layoutColor);
            //cellLayout.setStyle(layoutColor);
            if (noteLabel.getText().equals("")){
                noteLabel.setVisible(false);
            }else{
                noteLabel.setVisible(true);
            }
            String imagePath = "icons/" + Integer.toString(item.getCategory().getIconId()) + ".png";
            iconView.setImage(new Image(imagePath));
            noteLabel.setTextAlignment(TextAlignment.JUSTIFY);
            noteLabel.setWrapText(true);


            setText(null);
            setGraphic(cellLayout);
        }
    }
}
