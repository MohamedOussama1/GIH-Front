package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.controlsfx.control.CheckComboBox;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.frontend.LitController.populateGrid;
import static com.example.frontend.LitController.getBedCards;

public class LitLogistiqueController implements Initializable{

        @FXML
        ChoiceBox<String> ServicesDemandesChoiceBox1 = new ChoiceBox();
    @FXML
    TableView<DemandeAffectationFE> affecterDemandesTableView;

    @FXML
    private TableColumn<DemandeAffectationFE, Integer> idCBColumn =new TableColumn<>("ID");
    @FXML
    private TableColumn<DemandeAffectationFE, String> etatDemandeCBColumn = new TableColumn<>("Etat Demande");
    @FXML
    private TableColumn<DemandeAffectationFE, String> date_debutCBColumn = new TableColumn<>("Date Debut");
    @FXML
    private TableColumn<DemandeAffectationFE, Integer> qteCBColumn = new TableColumn<>("QTE") ;
    @FXML
    private TableColumn<DemandeAffectationFE, String> typeLitCBColumn = new TableColumn<>("Type Lit");
    @FXML
    private TableColumn<DemandeAffectationFE, String> modelLitCBColumn = new TableColumn<>("Model Lit");
    @FXML
    private TableColumn<DemandeAffectationFE, String> departementCBColumn = new TableColumn<>("Service");

    @FXML
    private TableColumn<DemandeAffectationFE, String> date_finCBColumn = new TableColumn<>("Date Fin");

    // Create
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
        @FXML
        Button btnAffecter;
        @FXML
        static
        GridPane gridStockAffecter;
        @FXML
        static
        GridPane gridLitsToAdd;
        @FXML
        ChoiceBox<String> chBoxServiceStock;
        @FXML
        ScrollPane scrollPaneToAdd;
        @FXML
        ScrollPane scrollPaneAffecter;
        @FXML
        Label lblStockService;
        @FXML
        Label lblStock;
        static String service;
        static int toAddRow = 0;
        static int toAddColumn = 0;
        static String userName_Login;
        static String password_Login;
        String test = userName_Login + ":" + password_Login;

        //</rachid>

        private static Client client = ClientBuilder.newClient()
                .register(JacksonFeature.class);

        private static WebTarget target = client.target(Connextion_info.url);

        protected void formatTextFieldToNumbersOnly(TextField textField, int max) {
            textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("\\D", ""));
                }
                try {
                    int newNumValue = Integer.valueOf(newValue);
                    if (newNumValue > max) {
                        textField.setText(oldValue);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {


            // Set up grid
            gridStockAffecter = new GridPane();
            scrollPaneAffecter.setContent(gridStockAffecter);
            gridStockAffecter.setVgap(17);
            gridStockAffecter.setHgap(17);
            gridStockAffecter.setPadding(new Insets(13));

            // Set up grid
            gridLitsToAdd = new GridPane();
            scrollPaneToAdd.setContent(gridLitsToAdd);
            gridLitsToAdd.setVgap(17);
            gridLitsToAdd.setHgap(17);
            gridLitsToAdd.setPadding(new Insets(13));

            // Load general stock
            populateStock();

            // Make ChoiceBox interactive
            chBoxServiceStock.getSelectionModel().selectedItemProperty().addListener(
                    ((observableValue, oldValue, newValue) -> {
                        if (newValue == null)
                            return;
                        service = newValue;
                        populateStock();
                        lblStockService.setText("Stock " + newValue);
                        gridLitsToAdd.getChildren().clear();
                        lblStock.setVisible(true);
                        scrollPaneAffecter.setVisible(true);
                        lblStockService.setVisible(true);
                        scrollPaneToAdd.setVisible(true);
                        btnAffecter.setVisible(true);
                    }));

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
            formatTextFieldToNumbersOnly(txtGarantie, 10);
            formatTextFieldToNumbersOnly(txtLargeur, 100);
            formatTextFieldToNumbersOnly(txtLongeur, 300);
            formatTextFieldToNumbersOnly(txtHauteur, 200);
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 40);
            valueFactory.setValue(1);
            spinnerQuantityLit.setValueFactory(valueFactory);


            // Populate ChoiceBoxes
            chBoxServiceStock.setItems(FXCollections.observableArrayList(departements));
            ServicesDemandesChoiceBox1.setItems(FXCollections.observableArrayList(departements));
            chBoxTypeCreer.setItems(FXCollections.observableArrayList(
                    target
                            .path("lits")
                            .path("types")
                            .request(MediaType.APPLICATION_JSON_TYPE)
                            .get()
                            .readEntity(List.class))
            );
            chBoxTypeCreer.setValue(chBoxTypeCreer.getItems().get(0));
            chBoxModel.setItems(FXCollections.observableArrayList(
                    target
                            .path("lits")
                            .path("models")
                            .request(MediaType.APPLICATION_JSON_TYPE)
                            .get()
                            .readEntity(List.class))
            );
            chBoxModel.setValue(chBoxModel.getItems().get(0));
            comboFonctions.getItems().addAll(
                    target
                            .path("lits")
                            .path("fonctions")
                            .request(MediaType.APPLICATION_JSON_TYPE)
                            .get()
                            .readEntity(List.class)
            );
        }

        private static void populateStock() {
            gridStockAffecter.getChildren().clear();
            Response stockResponse = target
                    .path("lits")
                    .path("stock")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();

            List<String> litsStock = stockResponse.readEntity(List.class);
            System.out.println(litsStock);
            List<JSONObject> litsStockJson = litsStock.stream().map(lit -> new JSONObject(lit)).collect(Collectors.toList());

            List<BedCard> bedCards = getBedCards(new JSONArray(litsStockJson));
            addAffecterAction(bedCards);
            List<Node> nodes = bedCards.stream().map(bed -> (Node) bed).collect(Collectors.toList());
            populateGrid(gridStockAffecter, nodes, 8);
        }

        @FXML
        public void onAffecterClick() {
            String service = chBoxServiceStock.getValue();
            System.out.println(service);
            if (service.equals(null))
                return;
            System.out.println("Hello");

            for (Node node : gridLitsToAdd.getChildren()) {
                BedCard bedCard = (BedCard) node;
                int numStock = service.equals("Neurologie") ? 1 : service.equals("Cardiologie") ? 2 : service.equals("Oncologie") ? 3 : 4;
                Response response = target
                        .path("departement")
                        .path(service)
                        .queryParam("idLit", bedCard.getBedId())
                        .queryParam("typeEspace", "Salle")
                        .queryParam("numEspace", 1000 + numStock)
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .put(Entity.json("Hello"));
            }
            ;
            List<Node> nodes = gridStockAffecter.getChildren();
            gridStockAffecter.getChildren().clear();
            populateGrid(gridStockAffecter, nodes, 8);
            StockController.populateServiceStock();
            gridLitsToAdd.getChildren().clear();
            toAddColumn = 0;
            toAddRow = 0;
            lblStock.setVisible(false);
            scrollPaneAffecter.setVisible(false);
            lblStockService.setVisible(false);
            scrollPaneToAdd.setVisible(false);
            btnAffecter.setVisible(false);
            chBoxServiceStock.getSelectionModel().clearSelection();
        }

        public static void addAffecterAction(List<BedCard> bedCards) {
            bedCards.forEach(bedCard -> {
                bedCard.setOnMouseClicked(event -> {
                    // Check if bedCard is still in stock géneral
                    if (gridStockAffecter.getChildren().contains(bedCard)) {
                        gridLitsToAdd.add(bedCard, toAddColumn, toAddRow, 1, 1);
                        if (toAddColumn == 8) {
                            toAddColumn = 0;
                            toAddRow += 1;
                        } else {
                            toAddColumn += 1;
                        }
                    }
                });
            });
        }

        public static String toRGBCode(Color color) {
            return String.format("#%02X%02X%02X",
                    (int) (color.getRed() * 255),
                    (int) (color.getGreen() * 255),
                    (int) (color.getBlue() * 255));
        }

        @FXML
        public void onBtnAjouterLit(ActionEvent event) {
            WebTarget newTarget = target;
            List<String> queryParams = List.of("type", "model", "dimensions", "chargeMax", "garantie", "prix", "frontColor", "description");
            List<String> queryValues = List.of(
                    chBoxTypeCreer.getValue(),
                    chBoxModel.getValue(),
                    txtLongeur.getText() + "x" + txtLargeur.getText() + "x" + txtHauteur.getText(),
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
                txtHauteur.clear();
                txtLargeur.clear();
                txtLongeur.clear();
                txtChargeMax.clear();
                txtGarantie.clear();
                txtPrix.clear();
                txtDescription.clear();
                spinnerQuantityLit.getValueFactory().setValue(1);
                colorPicker.setValue(Color.WHITE);
                populateStock();
            }
        }
    @FXML
    protected void onChangerEtatDemandes(ActionEvent event) throws IOException {
        String nomDepartement = ServicesDemandesChoiceBox1.getValue();
        Response getResponse;

        if (nomDepartement == null || nomDepartement.equals("Default")) {
            getResponse = target.path("demandeaffectation")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
        } else {
            getResponse = target.path("demandeaffectation")
                    .path("demandesDepartement")
                    .queryParam("nomDepartement", nomDepartement)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
        }


        if (getResponse.getStatus() == Response.Status.OK.getStatusCode()) {
            String responseString = getResponse.readEntity(String.class);
            System.out.println("Response String: " + responseString);
            JSONArray jsonArray = new JSONArray(responseString);
            ObservableList<DemandeAffectationFE> tableData = FXCollections.observableArrayList();


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String etatDemande = jsonObject.getString("etatDemande");
                String date_debut = jsonObject.getString("date_debut");
                String date_fin = jsonObject.getString("date_fin");
                int qte = jsonObject.getInt("qte");
                String typeLit = jsonObject.getString("typeLit");
                String modelLit = jsonObject.getString("modelLit");
                String departement = jsonObject.getString("departement");
                // Do something with the individual parameters here
                DemandeAffectationFE demandeAffectation = new DemandeAffectationFE();
                demandeAffectation.setId(id);
                demandeAffectation.setEtatDemande(etatDemande);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dateDebut = LocalDate.parse(date_debut, formatter);
                LocalDate dateFin = LocalDate.parse(date_fin, formatter);
                demandeAffectation.setDateDebut(dateDebut);
                demandeAffectation.setDateFin(dateFin);
                demandeAffectation.setQte(qte);
                demandeAffectation.setTypeLit(typeLit);
                demandeAffectation.setModelLit(modelLit);
                demandeAffectation.setDepartement(departement);

                tableData.add(demandeAffectation);
            }


            affecterDemandesTableView.setItems(tableData);

            idCBColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            etatDemandeCBColumn.setCellValueFactory(new PropertyValueFactory<>("etatDemande"));
            date_debutCBColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
            date_finCBColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
            qteCBColumn.setCellValueFactory(new PropertyValueFactory<>("qte"));
            typeLitCBColumn.setCellValueFactory(new PropertyValueFactory<>("typeLit"));
            modelLitCBColumn.setCellValueFactory(new PropertyValueFactory<>("modelLit"));
            departementCBColumn.setCellValueFactory(new PropertyValueFactory<>("departement"));
        }
        etatDemandeCBColumn.setCellFactory(column -> {
            return new TableCell<DemandeAffectationFE, String>() {
                private ChoiceBox<String> choiceBox = new ChoiceBox<>();

                {
                    choiceBox.getItems().addAll("NONTRAITÉE", "REJETÉE", "TRAITÉE","ENCOURS");
                    choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        DemandeAffectationFE demande = getTableView().getItems().get(getIndex());
                        int id = demande.getId();
                        Response response = target
                                .path("demandeaffectation/" + id + "/" + newValue)
                                .request(MediaType.APPLICATION_JSON_TYPE)
                                .put(Entity.entity("", MediaType.APPLICATION_JSON));


                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        DemandeAffectationFE demande = getTableView().getItems().get(getIndex());
                        choiceBox.setValue(demande.getEtatDemande());
                        setGraphic(choiceBox);
                    }
                };};});}

}
