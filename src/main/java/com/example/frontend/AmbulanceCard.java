package com.example.frontend;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AmbulanceCard extends BorderPane {
    static AmbulanceCard selectedAmbulance;
    static String selectedAmbulanceImmatriculation;
    static String selectedAmbulanceEtat;
    static int selectedAmbulanceKm;
    static Revision selectedAmbulanceCurrentRevision;
    String immatriculation;
    String etatAmbulance;
    int km;

    public AmbulanceCard(String immatriculation,String etatAmbulance, int km){
        this.immatriculation=immatriculation;
        this.etatAmbulance=etatAmbulance;
        this.km = km;

        Label labelAM = new Label(immatriculation);
        labelAM.setStyle("-fx-font-size: 24px;-fx-text-fill: white; -fx-font-weight: bold;");


        Label label = new Label("Etat Actuel :");
        Label etat = new Label(etatAmbulance);
        label.setStyle("-fx-text-fill: white;-fx-font-size: 16px;");
        etat.setStyle("-fx-text-fill: white;-fx-font-size: 16px;");


        ImageView ambulanceImage = new ImageView("ambulance1.png");
        ambulanceImage.setFitWidth(40);
        ambulanceImage.setFitHeight(40);
        HBox top = new HBox();
        top.setAlignment(Pos.CENTER);
        top.setSpacing(10);
        top.getChildren().addAll(ambulanceImage,labelAM);


        HBox center = new HBox();
        center.setAlignment(Pos.CENTER);
        center.setSpacing(10);
        center.getChildren().addAll(label, etat);


        ImageView tresbien = new ImageView("100.png");
        tresbien.setFitWidth(50);
        tresbien.setFitHeight(50);
        ImageView bien = new ImageView("70.png");
        bien.setFitWidth(50);
        bien.setFitHeight(50);
        ImageView defectuex = new ImageView("30.png");
        defectuex.setFitWidth(50);
        defectuex.setFitHeight(50);
        ImageView enpanne = new ImageView("0.png");
        enpanne.setFitWidth(50);
        enpanne.setFitHeight(50);

        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER);
        if (etatAmbulance.equals("F")){bottom.getChildren().addAll(tresbien);}
        else if (etatAmbulance.equals("NFCD")){bottom.getChildren().addAll(bien);}
        else if(etatAmbulance.equals("NFLD")){bottom.getChildren().addAll(defectuex);}
        else {bottom.getChildren().addAll(enpanne);}
        this.setStyle("-fx-background-color: linear-gradient(to bottom right, #3f51b5, #2196f3); -fx-background-radius: 20px;");
        this.topProperty().set(top);
        this.centerProperty().set(center);
        this.bottomProperty().set(bottom);

        this.setOnMouseClicked(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gererAmbulance.fxml"));
//            fxmlLoader.setController(new GererAmbulanceController());
            try {
                Scene scene = new Scene(fxmlLoader.load(), 1094, 586);
                Stage stockStage = new Stage();
                stockStage.setScene(scene);
                stockStage.setTitle("Ambulance " + immatriculation);
                Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stockStage.initOwner(oldStage);
                stockStage.initModality(Modality.APPLICATION_MODAL);
                stockStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            selectedAmbulanceKm = km;
            selectedAmbulanceEtat = etatAmbulance;
            selectedAmbulanceImmatriculation = immatriculation;
            selectedAmbulance = this;
        });



    }
}
