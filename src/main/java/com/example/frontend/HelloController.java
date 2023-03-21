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
    @FXML
    Button btnLogistique;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void onBtnDashboardClick(ActionEvent event) throws IOException{
        if (currentContent == "dashboard")
            return;
        currentContent = "dashboard";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("dashboard.fxml"));
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 869);
        anchorContent.getChildren().add(pane);

    }
    public void onBtnServiceClick(ActionEvent event) throws IOException{
        if (currentContent == "services")
            return;
        currentContent = "services";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("services.fxml"));
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 869);
        anchorContent.getChildren().add(pane);

    }
    public void onBtnLitsClick(ActionEvent event) throws IOException {
        if (currentContent == "lits")
            return;
        currentContent = "lits";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("lits.fxml"));
        TabPane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 869);
        anchorContent.getChildren().add(pane);
    }
    public void onBtnDmClick(ActionEvent event) throws IOException{
        if (currentContent == "dm")
            return;
        currentContent = "dm";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("dms.fxml"));
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 869);
        anchorContent.getChildren().add(pane);

    }
    public void onBtnAmbulancesClick(ActionEvent event) throws IOException{
        if (currentContent == "ambulances")
            return;
        currentContent = "ambulances";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("ambulances.fxml"));
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 869);
        anchorContent.getChildren().add(pane);

    }
    public void onBtnAdminClick(ActionEvent event) throws IOException{
        if (currentContent == "admin")
            return;
        currentContent = "admin";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("admin.fxml"));
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 869);
        anchorContent.getChildren().add(pane);
    }
    public void onBtnLogistiqueClick(ActionEvent event) throws IOException{
        if (currentContent == "logistique")
            return;
        currentContent = "logistique";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("logistique.fxml"));
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 869);
        anchorContent.getChildren().add(pane);
    }

}