package com.example.frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;


public class RoomCard extends VBox {

    private  GridPane grid;
    private  List<BedCard> beds;
    String typeEspace;
    int numEspace;
    private  Label label;
    private int capacity;
    GridPane gridPane;
    public RoomCard(){}
    public RoomCard(int capacity, String labelText, List<BedCard> bedCards, String nomDepartement, String typeEspace, int numEspace, GridPane gridPane) {
        this.grid = new GridPane();
        this.capacity = capacity;
        this.beds = bedCards;
        this.label = new Label();
        this.numEspace = numEspace;
        this.typeEspace = typeEspace;
        this.gridPane = gridPane;

        // Set up the grid pane
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));
        grid.setPrefSize(200, 200);

        // Set up the beds
        for (int i = 0; i < capacity; i++) {
            if (i >= bedCards.size()) {
                SelectableRectangle background = new SelectableRectangle(90, 90, nomDepartement, typeEspace, String.valueOf(numEspace), gridPane);
                grid.add(background, i%2, i/2);
            }
            else {
                BedCard bed = bedCards.get(i);
                grid.add(bed, i % 2, i / 2);
            }
        }

        // Set up the label
        label.setText(labelText);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        VBox.setMargin(label, new Insets(5));

        // Add the grid and label to the room card
        getChildren().addAll(grid, label);
        setAlignment(Pos.CENTER);
        setSpacing(10);
        setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-width: 1px; -fx-border-color: #BDBDBD;");
    }
}
