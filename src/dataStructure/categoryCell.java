package dataStructure;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;

public class categoryCell extends ListCell<Category> {
    FXMLLoader mLoader;
    @FXML
    private HBox boxLayout;
    @FXML
    private ImageView iconView;
    @FXML
    private Label nameLabel;
    @Override
    protected void updateItem(Category item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLoader == null) {
            }
            mLoader = new FXMLLoader(getClass().getResource("../fxmlLayouts/categoryListCell.fxml"));
            mLoader.setController(this);
            try {
                mLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String imagePath = "icons/" + Integer.toString(item.getIconId()) + ".png";
            Image image = new Image(imagePath);
            iconView.setImage(image);

            nameLabel.setText(item.getName());
            //iconView.setStyle("-fx-background-color: transparent");
            String layoutColor = "-fx-background-color: #" + item.getColor().toString().substring(2, 10);
            boxLayout.setStyle(layoutColor);


            setText(null);
            setGraphic(boxLayout);
        }
    }
}
