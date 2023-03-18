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

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class LitController implements Initializable {

    @FXML
    ChoiceBox<String> chBoxService = new ChoiceBox();
    @FXML
    ChoiceBox<String> chBoxEspace;
    @FXML
    ChoiceBox<String> chBoxEtat;
    @FXML
    Button btnChercher;
    @FXML
    Button btnAjouter;
    @FXML
    Button btnOccuper;
    @FXML
    Button btnReservations;
    @FXML
    GridPane gridLit = new GridPane();
    @FXML
    DatePicker dpDateDebut;
    @FXML
    DatePicker dpDateFin;
    @FXML
    TextField txtId;
    @FXML
    ListView<String> lstReservations;

    private Client client = ClientBuilder.newClient();

    private WebTarget target = client.target("http://localhost:8081");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Response getResponse = target
                .path("departement")
                .request()
                .get();
        List<String> departements = getResponse.readEntity(List.class);
        chBoxService.setItems(FXCollections.observableArrayList(departements));
        chBoxEspace.setItems(FXCollections.observableArrayList(Arrays.asList("Salle", "Chambre")));
        chBoxEtat.setItems(FXCollections.observableArrayList(Arrays.asList("Disponible", "Occupé")));
    }

    @FXML
    public void onBtnReservations(ActionEvent event){
        Response response = target
                .queryParam("idLit", txtId.getText())
                .path("lits")
                .path("reservation")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        List<String> reservations = response.readEntity(List.class);
        System.out.println(reservations);
        lstReservations.setItems(FXCollections.observableArrayList(reservations));
    }
    @FXML
    public void onBtnOccuperLit(ActionEvent event){
        String reservation = "{\"id\":\"" + txtId.getText() +
                "\", \"dateDebut\":\"" + LocalDateTime.of(dpDateDebut.getValue(), LocalTime.MIDNIGHT) +
                "\", \"dateFin\":\"" + LocalDateTime.of(dpDateFin.getValue(), LocalTime.MIDNIGHT) + "\"}";
        System.out.println(reservation);
        Response response = target
                .path("lits")
                .path("reservation")
                .request()
                .post(Entity.json(reservation));
        System.out.println(response.readEntity(String.class));
    }
    @FXML
    public void onBtnAjouterLit(ActionEvent event){
        WebTarget newTarget = target;
        List<String> queryParams = List.of("type", "model", "dimensions", "chargeMax", "garantie", "prix", "description");
        List<String> queryValues = List.of("ELECTRIQUE", "PESEE", "200x80x30", "200", "5", "10000", "Lit neuf");
        for (int i = 0; i < 6; i++)
            newTarget = newTarget.queryParam(queryParams.get(i), queryValues.get(i));
        System.out.println(newTarget);
        Response postResponse = newTarget
                .path("lits")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .method("POST");
    }
    @FXML
    public void onBtnChercherClick(ActionEvent event) {
        gridLit.getChildren().clear();
        Response response = null;
        String service = chBoxService.getSelectionModel().getSelectedItem();
        String espace = chBoxEspace.getSelectionModel().getSelectedItem();
        String etat = chBoxEtat.getSelectionModel().getSelectedItem();
        if (service != null) {
            if ((espace == null)) {
                response = target
                        .path("departement")
                        .path(service)
                        .path("lits")
                        .request()
                        .get();
            } else {
                if (etat == null) {
                    response = target
                            .path("departement")
                            .path(service)
                            .path(espace)
                            .path("lits")
                            .request()
                            .get();
                } else {
                    response = target
                            .path("departement")
                            .path(service)
                            .path(espace)
                            .path("lits")
                            .path(etat)
                            .request()
                            .get();
                }
            }
        }
        List<String> lits = response.readEntity(List.class);
        System.out.println(lits);

        int row = 0;
        int column = 0;
        int litInd = 0;
        while (litInd < lits.size()) {
            JSONObject lit = new JSONObject(lits.get(litInd));
            BedCard bedCard = new BedCard(lit.getInt("numEspace"), lit.getString("espace"), lit.getString("etat"), lit.getString("description"));
            gridLit.add(bedCard, column, row, 1, 1);
            if (column == 4) {
                column = 0;
                row += 1;
            } else {
                column += 1;
            }
            litInd += 1;
        }
    }
}