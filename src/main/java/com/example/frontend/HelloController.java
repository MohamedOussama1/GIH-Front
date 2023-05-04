package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class HelloController implements Initializable {
    static String currentContent;
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
    @FXML
    Button buttonLogin=new Button();
    @FXML
    ImageView profileImg;
    @FXML
    private String role = "admin";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    @FXML
    void OnLoginClick(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("login.fxml"));
        Pane pane;
        try {
            pane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene=new Scene(pane);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.setHeight(700);
        stage.setWidth(800);

        stage.setResizable(false);
        stage.setAlwaysOnTop(true);


        // display a stage with block the application untill i close stage (like dialog)
        Stage current = (Stage) buttonLogin.getScene().getWindow();
        stage.initOwner(current);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {

                Stage dd=(Stage)buttonLogin.getScene().getWindow();
                dd.close();

            }
        });

    }
    public void onBtnDashboardClick(ActionEvent event) throws IOException{
        if (currentContent == "dashboard")
            return;
        currentContent = "dashboard";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("dashboard.fxml"));
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 741);
        anchorContent.getChildren().add(pane);

    }
    public void onBtnServiceClick(ActionEvent event) throws IOException{
        if (currentContent == "services")
            return;
        currentContent = "services";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("services.fxml"));
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 741);
        anchorContent.getChildren().add(pane);
    }
    public void onBtnLitsClick(ActionEvent event) throws IOException {
        if (currentContent == "lits")
            return;
        currentContent = "lits";
        FXMLLoader fxmlLoader = new FXMLLoader();
        switch (role){
            case "admin":
                fxmlLoader = new FXMLLoader(HelloController.class.getResource("lits.fxml"));
                break;
            case "chef logistique":
                fxmlLoader = new FXMLLoader(HelloController.class.getResource("litsLogistique.fxml"));
                break;
            case "chef service":
                fxmlLoader = new FXMLLoader(HelloController.class.getResource("litsService.fxml"));
                break;
            default:
                System.out.println("Invalid role");
        }
        TabPane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 741);
        anchorContent.getChildren().add(pane);
    }
    public void onBtnDmClick(ActionEvent event) throws IOException{
        if (currentContent == "dm")
            return;
        currentContent = "dm";
        FXMLLoader fxmlLoader = new FXMLLoader();
        switch (role){
            case "admin":
                fxmlLoader = new FXMLLoader(HelloController.class.getResource("dms.fxml"));
                break;
            case "chef logistique":
                fxmlLoader = new FXMLLoader(HelloController.class.getResource("dmsLogistique.fxml"));
                break;
            case "chef service":
                fxmlLoader = new FXMLLoader(HelloController.class.getResource("dmsService.fxml"));
                break;
            default:
                System.out.println("Invalid role");
        }
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 741);
        anchorContent.getChildren().add(pane);

    }
    public void onBtnAmbulancesClick(ActionEvent event) throws IOException{
        if (currentContent == "ambulances")
            return;
        currentContent = "ambulances";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("ambulances.fxml"));
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 741);
        anchorContent.getChildren().add(pane);

    }
    public void onBtnAdminClick(ActionEvent event) throws IOException{
        if (currentContent == "admin")
            return;
        currentContent = "admin";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("admin.fxml"));
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 741);
        anchorContent.getChildren().add(pane);
    }
    public void onBtnLogistiqueClick(ActionEvent event) throws IOException{
        if (currentContent == "logistique")
            return;
        currentContent = "logistique";
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("logistique.fxml"));
        Pane pane = fxmlLoader.load();
        anchorContent.getChildren().clear();
        anchorContent.setPrefSize(921, 741);
        anchorContent.getChildren().add(pane);
    }

}