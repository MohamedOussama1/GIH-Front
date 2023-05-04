package com.example.frontend;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;
import java.util.Stack;

public class BedCard extends StackPane {
    int id;
    int code;
    boolean occupied;
    double percentEtat;
    String colorHex;
    Rectangle background;
    ImageView bedIcon;
    Label label;
    Tooltip tooltip;

    public BedCard(int id, String iconPath, String colorHex, int code, boolean occupied, double percentEtat, double chargeMax, String type, String model, String description) {
        this.id = id;
        this.code = code;
        this.occupied = occupied;
        this.percentEtat = percentEtat;
        this.tooltip = new Tooltip();
        tooltip.setText(
                "Type: " + type + "\n" +
                        "Model: " + model + "\n" +
                        "Etat: " + percentEtat + "\n" +
                        "Charge maximal: " + chargeMax + "Kg" + "\n" +
                        "Description: " + description
        );
        Tooltip.install(this, tooltip);
        background = new Rectangle(70, 80);
        background.setFill(Color.web(colorHex));
        background.setArcWidth(10);
        background.setArcHeight(10);
//        background.setStroke(Color.web("#289dc7"));
//        background.setStrokeWidth(2);

        label = new Label(String.valueOf(code));
        label.setPadding(new Insets(5));
        label.setStyle("-fx-font-weight: bold; -fx-background-color: #3f51b5; -fx-border-radius: 10px; -fx-min-width: 70; -fx-alignment: center; -fx-background-radius: 10 10 0 0");
        label.setTextFill(Color.WHITE);

        bedIcon = new ImageView();
        bedIcon.setImage(new Image(iconPath));

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(5));
        vBox.getChildren().addAll(label, bedIcon);

        setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-width: 1px; -fx-border-color: #BDBDBD;");
        setAlignment(Pos.CENTER);
        getChildren().addAll(background, vBox);

    }
    public int getBedId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
        background.setFill(Color.web(colorHex));
    }
}
