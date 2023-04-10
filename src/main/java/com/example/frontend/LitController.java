package com.example.frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class LitController implements Initializable {


///   Reservation
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
    ScrollPane scrollPane=new ScrollPane();

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


    //<rachid>
     @FXML
    Label labelService;
    @FXML
     AnchorPane anchorpaneHistorique=new AnchorPane();

    @FXML
     ScrollPane scrollhistorique;

    @FXML
     FlowPane flowHistorique;



    List<Stage> childwindow=new ArrayList<>();

    Stage stagevalid;

    Button btnvalid = new Button("OK");

//    int idlit;

    @FXML
    Button btnReservationSearch=new Button();

    @FXML
     ChoiceBox<String> chBoxReservationService11;

    @FXML
     TableView<Map<String, Object>> tableReservation = new TableView<>();
    @FXML

     TableColumn<Map<String, Object>, String> tabecolumn1 = new TableColumn<>("id");
    @FXML
     TableColumn<Map<String, Object>, String> tabecolumn2 = new TableColumn<>("espace");
    @FXML
     TableColumn<Map<String, Object>, Map<String, Object>> actioncolumn = new TableColumn<>("Action");

    @FXML
    Button btnOccuper;
    @FXML
    Button btnReservations;

    @FXML
    ListView<String> lstReservations=new ListView<>();

    @FXML
     Button btnSaveAllreservation=new Button();

     List<String> list_of_all_reservation=new ArrayList<>();

    //</rachid>

     Client client = ClientBuilder.newClient();

     WebTarget target = client.target("http://localhost:8081");
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

//    public LitController(){
//
//        initialize(null,null);
//    }

    @FXML
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
                .request()
                .get();
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

        chBoxReservationService11.setItems(FXCollections.observableArrayList(departements));
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

        labelService.setVisible(false);

        BookCardApp book = new BookCardApp();

        flowHistorique = book.parentGridPane;
        scrollhistorique.setContent(flowHistorique);

        scrollhistorique.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, rgba(83,155,63,0.71), rgba(83,155,63,0.65),rgba(83,155,63,0.76));");

        scrollhistorique.setFitToWidth(true);

        scrollhistorique.setFitToHeight(true);
        scrollhistorique.setPrefHeight(650);

        tabecolumn1.setText("ID");
        tabecolumn2.setText("Chambre");



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




    public void onBtnReservationSearch(ActionEvent actionEvent) throws JsonProcessingException {


        tableReservation.getItems().clear();
        list_of_all_reservation.clear();

        String department=chBoxReservationService11.getValue();

        Response response = target
                .queryParam("nomDepartement", department)
                .queryParam("espaceType", "espace")
                .path("lits")
                .path("litdisponible")
                .request(MediaType.APPLICATION_JSON_TYPE)
              //  .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user1:1234,user".getBytes()))
                .get();

        tabecolumn2.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get("espace").toString()));
        tabecolumn1.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get("id").toString()));


        actioncolumn.setCellFactory(new Callback<TableColumn<Map<String, Object>, Map<String, Object>>, TableCell<Map<String, Object>, Map<String, Object>>>() {
            @Override
            public TableCell<Map<String, Object>, Map<String, Object>> call(
                    final TableColumn<Map<String, Object>, Map<String, Object>> param) {
                final TableCell<Map<String, Object>, Map<String, Object>> cell = new TableCell<Map<String, Object>, Map<String, Object>>() {


                    private final HBox hbox=new HBox();
                    private final Button addButton = new Button("Add");

                    {
                        hbox.getChildren().add(addButton);
                        hbox.setAlignment(Pos.CENTER);
                        addButton.setStyle("-fx-background-color: green");
                        addButton.setOnAction((ActionEvent event) -> {


                            Map<String, Object> data = getTableView().getItems().get(getIndex());

                            if(addButton.getText()=="Add") {
                                addButton.setText("Added");
                                addButton.setStyle("-fx-background-color: red");
                                list_of_all_reservation.add(data.get("id").toString());
                                System.out.println(list_of_all_reservation.size());

                            }
                            else{
                                addButton.setText("Add");
                                addButton.setStyle("-fx-background-color: green");
                                list_of_all_reservation.remove(data.get("id").toString());
                                System.out.println(list_of_all_reservation.size());




                            }


                            System.out.println("selectedData: " + data.get("id"));
                        });
                    }

                    @Override
                    public void updateItem(Map<String, Object> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(addButton);
                        }
                    }
                };
                return cell;
            }
        });


        tabecolumn1.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get("id").toString()));
        tabecolumn2.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get("Espace").toString()));

// Create an ObservableList of Map objects
        ObservableList<Map<String, Object>> items = FXCollections.observableArrayList();
//        items.addAll(row1, row2);
//
        List<String> lstlitdisponbile = response.readEntity(List.class);
////

//        for (int i = 0; i <2 ; i++) {


            for (String lit : lstlitdisponbile) {
                lit = lit.replaceAll("\\s*:\\s*", "\":\"")
                        .replaceAll("\\s*,\\s*", "\",\"")
                        .replaceAll("\\{", "{\"")
                        .replaceAll("}", "\"}");
//
                ObjectMapper objectMapper = new ObjectMapper();

                Map<String, Object> hashMap = objectMapper.readValue(lit, HashMap.class);
                System.out.println(hashMap);
                items.add(hashMap);

        }

        tableReservation.setItems(items);

//        tableReservation.getItems().addAll(itemss);

    }



    @FXML
    void onBtnSaveAllreservationClick(ActionEvent event) {

        for(String elt:list_of_all_reservation){

            Response response = target
                    .queryParam("id", elt)
                    .path("lits")
                    .path("admin")
                    .request(MediaType.APPLICATION_JSON_TYPE)
//                    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user1:1234,user".getBytes()))
                    .method("POST");

            System.out.println(" rachid "+response.getStatus());

        }

//        this.onBtnReservationSearch(new ActionEvent());
//        tableReservation.refresh();




    }









    public void OnMouseClicked(MouseEvent mouseEvent) {

        int selectedItem = tableReservation.getSelectionModel().getSelectedIndex();

        System.out.println(selectedItem);
//        if (selectedItem != null) {
        stagevalid = new Stage();
        Label label = new Label("Validation!!!!");
        // create and set text for the button

        btnvalid.setPrefSize(70,40);

        VBox root = new VBox(); // create a VBox as the layout container
        root.getChildren().addAll(label, btnvalid); // add the label and button to the VBox

        Scene scene = new Scene(root, 300, 200);

        root.setAlignment(Pos.CENTER);

        stagevalid.setScene(scene);
        stagevalid.setResizable(false);
        stagevalid.show();

    }
}