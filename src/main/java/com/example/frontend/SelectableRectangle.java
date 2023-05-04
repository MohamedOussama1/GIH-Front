package com.example.frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectableRectangle extends Rectangle {
    int row;
    int column;
    RoomCard roomCard;

    public SelectableRectangle(RoomCard roomCard, int row, int column, double v1, double v2, String nomDepartement, String typeEspace, String numEspace, GridPane gridPane) {
        super(v1, v2);
        this.roomCard = roomCard;
        this.row = row;
        this.column = column;
        this.setFill(Color.web("#3f51b5"));
        this.setArcWidth(10);
        this.setArcHeight(10);
        this.setOnMouseClicked(event -> {
            LitController.selectedRoomCard = roomCard;
            LitController.selectedRectangle = this;
            LitServiceController.selectedRoomCard = roomCard;
            LitServiceController.selectedRectangle = this;
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("stock.fxml"));
            fxmlLoader.setController(new StockController(row, column, nomDepartement, typeEspace, numEspace, gridPane));
            try {
                Scene scene = new Scene(fxmlLoader.load(), 915, 350);
                Stage stockStage = new Stage();
                stockStage.setScene(scene);
                stockStage.setTitle("Stock " + nomDepartement);
                Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stockStage.initOwner(oldStage);
                stockStage.initModality(Modality.APPLICATION_MODAL);
                stockStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
