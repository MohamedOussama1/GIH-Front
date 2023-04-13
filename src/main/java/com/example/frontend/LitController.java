package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.MessageBodyReader;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class LitController implements Initializable {
    @FXML
    TabPane tabPane;
    @FXML
    ChoiceBox<String> chBoxService = new ChoiceBox();
    @FXML
    ChoiceBox<String> chBoxEspace;
    @FXML
    Button btnChercher;
    @FXML
    Button btnAjouter;
    @FXML
    GridPane gridLit;
    @FXML
    ScrollPane scrollPane;

    @FXML
    ChoiceBox<String> chBoxTypeCreer;
    @FXML
    ChoiceBox<String> chBoxModel;
    @FXML
    CheckComboBox<String> comboFonctions = new CheckComboBox<>();
    @FXML
    TextField txtLongeur;
    @FXML
    TextField txtLargeur;
    @FXML
    TextField txtHauteur;
    @FXML
    TextField txtChargeMax;
    @FXML
    TextField txtGarantie;
    @FXML
    TextField txtPrix;
    @FXML
    TextArea txtDescription;
    @FXML
    Spinner<Integer> spinnerQuantityLit;
    @FXML
    ColorPicker colorPicker;
    String service;
    String typeEspace;


    private Client client = ClientBuilder.newClient()
            .register(JacksonFeature.class);

    private WebTarget target = client.target("http://localhost:8081");
    protected void formatTextFieldToNumbersOnly(TextField textField, int max) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("\\D", ""));
            }
            try {
                int newNumValue = Integer.valueOf(newValue);
                if (newNumValue > max){
                    textField.setText(oldValue);
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set up grid
        gridLit = new GridPane();
        gridLit.setVgap(20);
        gridLit.setHgap(20);
        gridLit.setPadding(new Insets(20));
        scrollPane.setContent(gridLit);

        // Load Departements
        Response getResponse = target
                .path("departement")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        System.out.println(getResponse);
        List<String> departements = getResponse.readEntity(List.class);

        // Set up Creation fields
        formatTextFieldToNumbersOnly(txtPrix, 100000);
        formatTextFieldToNumbersOnly(txtChargeMax, 400);
        formatTextFieldToNumbersOnly(txtGarantie,10);
        formatTextFieldToNumbersOnly(txtLargeur,100);
        formatTextFieldToNumbersOnly(txtLongeur, 300);
        formatTextFieldToNumbersOnly(txtHauteur,200);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 40);
        valueFactory.setValue(1);
        spinnerQuantityLit.setValueFactory(valueFactory);
        chBoxTypeCreer.setItems(FXCollections.observableArrayList(Arrays.asList("ELECTRIQUE", "MECANIQUE")));
        chBoxModel.setItems(FXCollections.observableArrayList(Arrays.asList(
                "STANDARD",
                "ROTATION",
                "PESEE",
                "BARIATRIQUE",
                "CIRCULATION",
                "TRAITEMENT"
        )));
        comboFonctions.getItems().addAll(
        "REGLAGE_DE_LA_HAUTEUR",
                "REGLAGE_DOSSIER",
                "POSITION_JAMBES",
                "INCLINAISON_LIT",
                "TRENDELENBURG",
                "ANTI_TRENDELENBURG",
                "BARRIERES_SECURITE",
                "POSITION_LATERALE",
                "POSITION_ASSISE"
        );
        chBoxService.setItems(FXCollections.observableArrayList(departements));
        chBoxEspace.setItems(FXCollections.observableArrayList("Salle", "Chambre"));

        // Listen to Stock event
        gridLit.addEventHandler(WindowChangeEvent.WINDOW_CHANGE_EVENT, event -> {
            chBoxService.setValue(service);
            chBoxEspace.setValue(typeEspace);
            onBtnChercherClick(event);
        });
    }
    public static String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }
    @FXML
    public void onBtnAjouterLit(ActionEvent event){
        WebTarget newTarget = target;
        List<String> queryParams = List.of("type", "model", "dimensions", "chargeMax", "garantie", "prix", "frontColor", "description");
        List<String> queryValues = List.of(
                chBoxTypeCreer.getValue(),
                chBoxModel.getValue(),
                txtLongeur.getText()+"x"+txtLargeur.getText()+"x"+txtHauteur.getText(),
                txtChargeMax.getText(),
                txtGarantie.getText(),
                txtPrix.getText(),
                toRGBCode(colorPicker.getValue()),
                txtDescription.getText());
        for (int i = 0; i < queryValues.size(); i++)
            newTarget = newTarget.queryParam(queryParams.get(i), queryValues.get(i));

        // Insert fonctionsLit
        for (String checkedItem : comboFonctions.getCheckModel().getCheckedItems())
            newTarget = newTarget.queryParam("fonctions", checkedItem);

        Response postResponse = newTarget
                .path("lits")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .method("POST");
        int idLitDescription = postResponse.readEntity(Integer.class);
        Response postItemResponse = target
                .path("lits")
                .path("items")
                .queryParam("quantity", spinnerQuantityLit.getValue())
                .queryParam("idLitDescription", idLitDescription)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .method("POST");
        if (postItemResponse.getStatus() == 200) {
            chBoxTypeCreer.setValue("");
            chBoxModel.setValue("");
            txtHauteur.clear();
            txtLargeur.clear();
            txtLongeur.clear();
            txtChargeMax.clear();
            txtGarantie.clear();
            txtPrix.clear();
            txtDescription.clear();
            spinnerQuantityLit.getValueFactory().setValue(1);
            colorPicker.setValue(Color.WHITE);
        }
    }

    @FXML
    public void onBtnChercherClick(Event event) {
        // Send http request
        gridLit.getChildren().clear();
        Response response;
        service = chBoxService.getSelectionModel().getSelectedItem();
        typeEspace = chBoxEspace.getSelectionModel().getSelectedItem();
        chBoxService.setValue("");
        chBoxEspace.setValue("");
        if (service != null) {
            if (typeEspace == null) {
                response = target
                        .path("departement")
                        .path(service)
                        .path("lits")
                        .request()
                        .get();
            } else {
                response = target
                        .path("departement")
                        .path(service)
                        .path(typeEspace)
                        .path("lits")
                        .request()
                        .get();
            }
            List<String> espaceLitsLst = response.readEntity(List.class);


            // Populate grid with returned List
            int row = 0;
            int column = 0;
            int litInd = 0;
            while (litInd < espaceLitsLst.size()) {
                JSONObject espaceLits = new JSONObject(espaceLitsLst.get(litInd));
                System.out.println(espaceLits);
                JSONArray lits = espaceLits.getJSONArray("litLst");
                List<BedCard> bedCards = new ArrayList<>();
                lits.forEach(elt -> {
                    JSONObject lit = (JSONObject) elt;
                    JSONObject litDescription = (JSONObject) lit.get("litDescription");
                    String colorHex = "#99cc66";
                    if (lit.getBoolean("occupied") == true)
                        colorHex = "orange";
                    BedCard bedCard = new BedCard(
                            lit.getInt("id"),
                            "lit.png",
                            colorHex,
                            lit.getInt("code"),
                            lit.getBoolean("occupied"),
                            lit.getDouble("percentEtat"),
                            litDescription.getDouble("chargeMax"),
                            litDescription.getString("type"),
                            litDescription.getString("modelLit"),
                            litDescription.getString("description")
                    );
                    bedCards.add(bedCard);
                });
                RoomCard roomCard;
                if (typeEspace == "Salle") {
                    JSONObject salle = espaceLits.getJSONObject("salle");
                    roomCard = new RoomCard(2, salle.getString("typeSalle").split("_")[1] + " " + salle.get("numero"), bedCards, service, typeEspace, salle.getInt("numero"), gridLit);
                } else {
                    JSONObject chambre = espaceLits.getJSONObject("chambre");
                    int capacity;
                    String typeChambre = chambre.getString("typeChambre");
                    capacity = typeChambre.equals("SINGLE") ? 1 : typeChambre.equals("DOUBLE") ? 2 : 4;
                    roomCard = new RoomCard(capacity, "Chambre" + chambre.get("numero"), bedCards, service, typeEspace, chambre.getInt("numero"), gridLit);
                }
                gridLit.add(roomCard, column, row, 1, 1);
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
}