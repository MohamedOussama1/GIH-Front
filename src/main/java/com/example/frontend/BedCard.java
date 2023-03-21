package com.example.frontend;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class BedCard extends BorderPane {
    int numEspace;
    String typeEspace;
    String etatLit;
    String description;

    public BedCard(int numEspace, String typeEspace, String etatLit, String description) {
        this.numEspace = numEspace;
        this.typeEspace = typeEspace;
        this.etatLit = etatLit;
        this.description = description;

        // Description
        Tooltip tooltip = new Tooltip();
        tooltip.setText(description);
        Tooltip.install(this, tooltip);

        // Icon
        Image image = new Image("lit.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(70);
        StackPane center = new StackPane();
        center.getChildren().add(imageView);

        // Top
        HBox top = new HBox();
        top.setSpacing(30);
        top.setAlignment(Pos.CENTER);
        top.getChildren().addAll(new Label(typeEspace), new Label(String.valueOf(numEspace)));

        // Bottom
        AnchorPane bottom = new AnchorPane();
        bottom.getChildren().add(0, new Label(etatLit));

        this.bottomProperty().set(bottom);
        this.topProperty().set(top);
        this.centerProperty().set(center);

        // Style
        this.setStyle("-fx-background-color: rgb(200,200,200); -fx-background-radius: 20px;");
    }

    public BedCard(Node center, int numEspace, String typeEspace, String etatLit) {
        super(center);
        this.numEspace = numEspace;
        this.typeEspace = typeEspace;
        this.etatLit = etatLit;
    }

    public BedCard(Node center, Node top, Node right, Node bottom, Node left, int numEspace, String typeEspace, String etatLit) {
        super(center, top, right, bottom, left);
        this.numEspace = numEspace;
        this.typeEspace = typeEspace;
        this.etatLit = etatLit;
    }
}
