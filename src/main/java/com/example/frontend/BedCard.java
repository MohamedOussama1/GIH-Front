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
    double mouseX;
    double mouseY;
    int id;
    int code;
    boolean occupied;
    double percentEtat;
    Boolean selectable;
    Rectangle background;
    ImageView bedIcon;
    Label label;
    boolean selected = false;
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
        label.setStyle("-fx-font-weight: bold; -fx-background-color: #0099cc; -fx-border-radius: 10px; -fx-min-width: 70; -fx-alignment: center; -fx-background-radius: 10 10 0 0");
        label.setTextFill(Color.WHITE);

        bedIcon = new ImageView();
        bedIcon.setImage(new Image(iconPath));

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(5));
        vBox.getChildren().addAll(label, bedIcon);

        setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-width: 1px; -fx-border-color: #BDBDBD;");
        setAlignment(Pos.CENTER);
//        setPadding(new Insets(5));
        getChildren().addAll(background, vBox);

//        this.setOnMouseClicked(event -> {
//            selected = !selected;
//            System.out.println(selected);
//            applyInnerShadow(this, selected);
//        });
    }
    public void setSelectable(Boolean selectable){
        this.selectable =  selectable;
        if (!selectable){
            selected = false;
            applyInnerShadow(this, false);
        }
    }
    public void setSelectable(Boolean selectable, List<BedCard> bedFriends){
        this.selectable = selectable;
        if (selectable) {
            this.setOnMouseClicked(event -> {
                selected = !selected;
                System.out.println(selected);
                applyInnerShadow(this, selected);
                bedFriends.forEach(bedCard -> bedCard.setSelectable(false));
            });
        }else {
            this.setOnMouseClicked(event -> {
                selected = false;
                applyInnerShadow(this, false);
            });
        }
    }
    private void makeDraggable(Node node){
        node.setOnMousePressed(e -> {
            mouseX = e.getSceneX() - node.getTranslateX();
            mouseY = e.getSceneY() - node.getTranslateY();
        });
        node.setOnMouseDragged(e -> {
            node.setTranslateX(e.getSceneX() - mouseX);
            node.setTranslateY(e.getSceneY() - mouseY);
        });
        node.setOnMouseReleased(releaseEvent -> {
            /* Handle mouse release event */
            boolean onRectangle = false;
            for (Node child : node.getParent().getChildrenUnmodifiable()) {
                if (child instanceof Rectangle && child.getBoundsInParent().contains(releaseEvent.getSceneX(), releaseEvent.getSceneY())) {
                    /* Cursor is on a rectangle */
                    System.out.println("Hello");
                    onRectangle = true;
                    break;
                }else {
                    if (!onRectangle) {
                        /* Cursor is not on a rectangle */
                        node.setLayoutX(0);
                        node.setLayoutY(0);
                    }
                }
            }
        });
    }
        private  void applyInnerShadow(Node node, boolean isSelected) {
        if (isSelected) {
            node.setStyle("-fx-background-color: #0099cc; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-width: 1px; -fx-border-color: #BDBDBD;");
        } else {
            node.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-width: 1px; -fx-border-color: #BDBDBD;");
        }
    }
    public static void dragAndDrop(BedCard sourceBed, BedCard target) {
        sourceBed.setOnDragDetected(event -> {
            Dragboard dragboard = sourceBed.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("teal");
            dragboard.setContent(content);
        });

// Define the drag events on the target node
        target.setOnDragOver(event -> {
            if (event.getGestureSource() != target && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        target.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                target.setStyle("-fx-background-color: " + dragboard.getString());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        target.setOnDragExited(event -> {
            target.setStyle("-fx-border-color: black;");
            event.consume();
        });

        target.setOnDragEntered(event -> {
            target.setStyle("-fx-border-color: blue;");
            event.consume();
        });
    }


    public int getBedId() {
        return code;
    }
}
