package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StockController implements Initializable {
    private Client client = ClientBuilder.newClient()
            .register(JacksonFeature.class);

    private WebTarget target = client.target("http://localhost:8081");
    @FXML
    ScrollPane scrollPaneStock;
    @FXML
    GridPane gridStock;
    String newEspace;
    String numEspace;
    String nomDepartement;
    GridPane gridPane;


    public StockController(String nomDepartement, String newEspace, String numEspace, GridPane gridPane) {
        this.nomDepartement = nomDepartement;
        this.newEspace = newEspace;
        this.numEspace = numEspace;
        this.gridPane = gridPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Response stockResponse = target
                .path("lits")
                .path("stock")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        Response departementStockResponse = target
                .path("departement")
                .path("stock")
                .queryParam("nomDepartement", nomDepartement)
                .request()
                .get();
        List<String> litsStock = departementStockResponse.readEntity(List.class);
        System.out.println(litsStock);

        gridStock = new GridPane();
        scrollPaneStock.setContent(gridStock);

        gridStock.setVgap(17);
        gridStock.setHgap(17);
        gridStock.setPadding(new Insets(13));
        int row = 0;
        int column = 0;
        int litInd = 0;
        while (litInd < litsStock.size()) {
            JSONObject lit = new JSONObject(litsStock.get(litInd));
            JSONObject litDescription = (JSONObject) lit.get("litDescription");
            System.out.println(lit);
            BedCard bedCard = new BedCard(
                    lit.getInt("id"),
                    "lit.png",
                    litDescription.getString("frontColor"),
                    lit.getInt("code"),
                    lit.getBoolean("occupied"),
                    lit.getDouble("percentEtat"),
                    litDescription.getDouble("chargeMax"),
                    litDescription.getString("type"),
                    litDescription.getString("modelLit"),
                    litDescription.getString("description")
            );
            bedCard.setOnMouseClicked(event -> {

                // Move lit
                int id = Integer.valueOf(lit.getInt("id"));
                Response response = target
                        .path("departement")
                        .path(nomDepartement)
                        .queryParam("idLit", id)
                        .queryParam("typeEspace", newEspace)
                        .queryParam("numEspace", numEspace)
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .put(Entity.json("Hello"));

                // Insert BedCard in selected Position
                if (response.getStatus() == 200) {
                    gridPane.fireEvent(new WindowChangeEvent());
                }

                // Close window
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            });
            gridStock.add(bedCard, column, row, 1, 1);
            if (column == 8) {
                column = 0;
                row += 1;
            } else {
                column += 1;
            }
            litInd += 1;
        }
    }
}
