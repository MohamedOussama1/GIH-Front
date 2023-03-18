package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class HelloController implements Initializable {
    static String currentContent = "lits";
    @FXML
    AnchorPane anchorContent;
    @FXML
    Button btnDashboard;
    @FXML
    Button btnServices;
    @FXML
    Button btnLits;
    @FXML
    Button btnDms;
    @FXML
    Button btnAmbulances;
    @FXML
    Button btnAdmin;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void onBtnDashboardClick(ActionEvent event){
        if (currentContent == "dashboard")
            return;
        currentContent = "dashboard";
        AnchorPane anchorPaneDashboard = new AnchorPane();
        anchorPaneDashboard.setPrefSize(921, 869);
        anchorPaneDashboard.setStyle("-fx-background-color: red;");
        anchorContent.getChildren().clear();
        anchorContent.getChildren().add(anchorPaneDashboard);

    }
    public void onBtnServiceClick(ActionEvent event){
        if (currentContent == "services")
            return;
        currentContent = "services";
        AnchorPane anchorPaneServices= new AnchorPane();
        anchorPaneServices.setPrefSize(921, 869);
        anchorPaneServices.setStyle("-fx-background-color: red;");
        anchorContent.getChildren().clear();
        anchorContent.getChildren().add(anchorPaneServices);

    }
    public void onBtnLitsClick(ActionEvent event) throws IOException {
        if (currentContent == "lits")
            return;
        currentContent = "lits";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("lits.fxml"));
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 869);
        anchorContent.getChildren().add(pane);
    }
    public void onBtnDmClick(ActionEvent event){
        if (currentContent == "dm")
            return;
        currentContent = "dm";
        AnchorPane anchorPaneDm = new AnchorPane();
        anchorPaneDm.setPrefSize(921, 869);
        anchorPaneDm.setStyle("-fx-background-color: red;");
        anchorContent.getChildren().clear();
        anchorContent.getChildren().add(anchorPaneDm);

    }
    public void onBtnAmbulancesClick(ActionEvent event){
        if (currentContent == "ambulances")
            return;
        currentContent = "ambulances";
        AnchorPane anchorPaneAmbulances = new AnchorPane();
        anchorPaneAmbulances.setPrefSize(921, 869);
        anchorPaneAmbulances.setStyle("-fx-background-color: red;");
        anchorContent.getChildren().clear();
        anchorContent.getChildren().add(anchorPaneAmbulances);

    }
    public void onBtnAdminClick(ActionEvent event){
        if (currentContent == "admin")
            return;
        currentContent = "admin";
        AnchorPane anchorPaneAdmin = new AnchorPane();
        anchorPaneAdmin.setPrefSize(921, 869);
        anchorPaneAdmin.setStyle("-fx-background-color: red;");
        anchorContent.getChildren().clear();
        anchorContent.getChildren().add(anchorPaneAdmin);

    }

}