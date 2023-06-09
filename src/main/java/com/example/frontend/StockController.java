package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StockController implements Initializable {
    private static Client client = ClientBuilder.newClient()
            .register(JacksonFeature.class);

    private static WebTarget target = client.target(Connextion_info.url);
    @FXML
    ScrollPane scrollPaneStock;
    @FXML
    static GridPane gridStock;
    static String newEspace;
    static String numEspace;
    static String nomDepartement;
    static GridPane gridPane;
    static int row;
    static int column;
    static BedCard selectedBed;


    public StockController(int row, int column, String nomDepartement, String newEspace, String numEspace, GridPane gridPane) {
        this.row = row;
        this.column = column;
        this.nomDepartement = nomDepartement;
        this.newEspace = newEspace;
        this.numEspace = numEspace;
        this.gridPane = gridPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set up grid
        gridStock = new GridPane();
        scrollPaneStock.setContent(gridStock);
        gridStock.setVgap(17);
        gridStock.setHgap(17);
        gridStock.setPadding(new Insets(13));

        populateServiceStock();
    }

    public static void populateServiceStock() {

        Response departementStockResponse = target
                .path("departement")
                .path("stock")
                .queryParam("nomDepartement", nomDepartement)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        List<String> litsStock = departementStockResponse.readEntity(List.class);
        List<JSONObject> litsStockJson = litsStock.stream().map(lit -> new JSONObject(lit)).collect(Collectors.toList());

        List<BedCard> bedCards = LitController.getBedCards(new JSONArray(litsStockJson));
        addStockAction(bedCards);
        List<Node> nodes = bedCards.stream().map(bed -> (Node) bed).collect(Collectors.toList());
        LitController.populateGrid(gridStock, nodes, 8);
    }

    public static void addStockAction(List<BedCard> bedCards) {
       bedCards.forEach(bedCard -> bedCard.setOnMouseClicked(event -> {
                selectedBed = bedCard;
               // Move lit
               int id = Integer.valueOf(bedCard.getBedId());

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
                   LitController.selectedGridRow = row;
                   LitController.selectedGridColumn = column;
                   LitServiceController.selectedGridRow = row;
                   LitServiceController.selectedGridColumn = column;
               }

               // Close window
               Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
               stage.close();
           })
       );
   }
}
