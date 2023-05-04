package com.example.frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.frontend.LitController.getRoomCards;
import static com.example.frontend.LitController.populateGrid;

public class LitServiceController implements Initializable{
    @FXML
    ScrollPane scrollReservation;
    @FXML
    TabPane tabPaneLitsService;
    @FXML
    Tab tabLits;
    @FXML
    Tab tabDemandes;
    @FXML
    Tab tabHistorique;
    @FXML
    Tab tabAdmission;
    @FXML
    ChoiceBox<String> chBoxService = new ChoiceBox();
    @FXML
    ChoiceBox<String> chBoxEspaceService = new ChoiceBox<>();
    @FXML
    static
    GridPane gridLit;
    @FXML
    ScrollPane scrollPane;
    @FXML
    Label lblConsulterService;
    @FXML
    Label lblStockService;
    static String service = "Cardiologie";
    static String typeEspace;
    static int selectedGridRow = 0;
    static int selectedGridColumn = 0;
    static RoomCard selectedRoomCard;
    static SelectableRectangle selectedRectangle;
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
    ChoiceBox<String> chBoxReservationService11 = new ChoiceBox<>();

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
    @FXML
    ChoiceBox<String> chBoxHistorique = new ChoiceBox<>();
    List<String> list_of_all_reservation=new ArrayList<>();
    static String userName_Login;
    static String password_Login;
    String test=userName_Login+":"+password_Login;
//    </rachid>

//    Aya

    @FXML
    ChoiceBox<String> ServiceChoiceBox = new ChoiceBox();

    @FXML
    ChoiceBox<String> EspaceChoiceBox = new ChoiceBox();
    @FXML
    ChoiceBox<String> ListEspacesChoiceBox = new ChoiceBox();
    @FXML
    ChoiceBox<String> TypeLitChoiceBox = new ChoiceBox();
    @FXML
    ChoiceBox<String> ModelLitChoiceBox = new ChoiceBox();
    @FXML
    ChoiceBox<String> TypeEspaceChoiceBox = new ChoiceBox();
    @FXML
    ChoiceBox<String> ServicesDemandesChoiceBox = new ChoiceBox();
    @FXML
    ChoiceBox<String> ServicesDemandesChoiceBox1 = new ChoiceBox();

    @FXML
    Button Commander;
    @FXML
    Button afficherDemandes;
    @FXML
    TableView<DemandeAffectationFE> afficherDemandesTableView;
    @FXML
    TableView<DemandeAffectationFE> ajouterDemandesTableView;
    @FXML
    TableView<DemandeAffectationFE> affecterDemandesTableView;

    @FXML
    private TableColumn<DemandeAffectationFE, Integer> idCol =new TableColumn<>("ID");
    @FXML
    private TableColumn<DemandeAffectationFE, Integer> idCBColumn =new TableColumn<>("ID");
    @FXML
    private TableColumn<DemandeAffectationFE, String> etatDemandeCol = new TableColumn<>("Etat Demande");
    @FXML
    private TableColumn<DemandeAffectationFE, String> etatDemandeCBColumn = new TableColumn<>("Etat Demande");

    @FXML
    private TableColumn<DemandeAffectationFE, String> date_debutCol = new TableColumn<>("Date Debut"); @FXML
    private TableColumn<DemandeAffectationFE, String> date_debutCBColumn = new TableColumn<>("Date Debut");
    @FXML
    private TableColumn<DemandeAffectationFE, Integer> qteCol = new TableColumn<>("QTE") ;@FXML
    private TableColumn<DemandeAffectationFE, Integer> qteCBColumn = new TableColumn<>("QTE") ;
    @FXML
    private TableColumn<DemandeAffectationFE, String> typeLitCol = new TableColumn<>("Type Lit");@FXML
    private TableColumn<DemandeAffectationFE, String> typeLitCBColumn = new TableColumn<>("Type Lit");
    @FXML
    private TableColumn<DemandeAffectationFE, String> modelLitCol = new TableColumn<>("Model Lit"); @FXML
    private TableColumn<DemandeAffectationFE, String> modelLitCBColumn = new TableColumn<>("Model Lit");
    @FXML
    private TableColumn<DemandeAffectationFE, String> departementCol = new TableColumn<>("Service"); @FXML
    private TableColumn<DemandeAffectationFE, String> departementCBColumn = new TableColumn<>("Service");

    @FXML
    private TableColumn<DemandeAffectationFE, String> date_finCBColumn = new TableColumn<>("Date Fin");

    @FXML
    private TableColumn<DemandeAffectationFE, Integer> qteColumn = new TableColumn<>("QTE") ;
    @FXML
    private TableColumn<DemandeAffectationFE, String> typeLitColumn = new TableColumn<>("Type Lit");
    @FXML
    private TableColumn<DemandeAffectationFE, String> modelLitColumn = new TableColumn<>("Model Lit");
    @FXML
    private TableColumn<DemandeAffectationFE, String> departementColumn = new TableColumn<>("Service");

    @FXML
    private Button ajouterService;
    @FXML
    private Button ajouterDemandeAffectation;
    @FXML
    private TextField qteTextField;

    @FXML
    private Button supprimerTableViewDemandes;
    @FXML
    private ListView<String>ListServiceAfficher= new ListView<>();
    @FXML
    private ChoiceBox<String> supprimerServiceChoiceBox = new ChoiceBox<>();
    private ObservableList<DemandeAffectationFE> tableData = FXCollections.observableArrayList();
    private List<DemandeAffectationFE> demandesAEnvoyer = new ArrayList<>();

    private static Client client = ClientBuilder.newClient()
            .register(JacksonFeature.class);

    private static WebTarget target = client.target(Connextion_info.url);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set up tabPane listener
        tabPaneLitsService.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
                try {
                    if (oldTab == tabAdmission) {
                        onShowHistorique();
                        chBoxEspaceService.setValue(typeEspace);
                    } else if (oldTab == tabLits) {
                        onBtnSaveAllreservationClick();
                        onShowHistorique();
                    } else if (oldTab == tabHistorique) {
                        chBoxEspaceService.setValue(typeEspace);
                        onBtnSaveAllreservationClick();
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });
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

        // Populate ChoiceBoxes
        chBoxService.setItems(FXCollections.observableArrayList(departements));
        chBoxReservationService11.setItems(FXCollections.observableArrayList(departements));
        chBoxHistorique.setItems(FXCollections.observableArrayList(departements));
        chBoxEspaceService.setItems(FXCollections.observableArrayList("Salle", "Chambre"));

        //Aya
        TypeLitChoiceBox.setItems(FXCollections.observableArrayList(
                target
                        .path("lits")
                        .path("types")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get()
                        .readEntity(List.class)
        ));
        ModelLitChoiceBox.setItems(FXCollections.observableArrayList(
                target
                        .path("lits")
                        .path("models")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get()
                        .readEntity(List.class)
        ));
        TypeEspaceChoiceBox.setItems(FXCollections.observableArrayList("Salle", "Chambre"));
        ServiceChoiceBox.setItems(FXCollections.observableArrayList(departements));
        supprimerServiceChoiceBox.setItems(FXCollections.observableArrayList(departements));
        ServicesDemandesChoiceBox.setItems(FXCollections.observableArrayList(departements));
        ServicesDemandesChoiceBox1.setItems(FXCollections.observableArrayList(departements));
        ServicesDemandesChoiceBox1.getItems().add("Default");

        qteColumn.setCellValueFactory(new PropertyValueFactory<>("qte"));
        typeLitColumn.setCellValueFactory(new PropertyValueFactory<>("typeLit"));
        modelLitColumn.setCellValueFactory(new PropertyValueFactory<>("modelLit"));
        departementColumn.setCellValueFactory(new PropertyValueFactory<>("departement"));

        tableData = FXCollections.observableArrayList();
        ajouterDemandesTableView.setItems(tableData);


        // Listen to Stock event (Insert bed in selectedRectangle)
        gridLit.addEventHandler(WindowChangeEvent.WINDOW_CHANGE_EVENT, event -> {
            // give bed card green color (disponible)
            StockController.selectedBed.setColorHex("green");
            Platform.runLater(() -> {
                selectedRoomCard.getGrid().getChildren().remove(selectedRectangle);
                selectedRoomCard.getGrid().add(StockController.selectedBed, selectedGridRow, selectedGridColumn);
            });
        });

        // Populate Grid Lit
        chBoxEspaceService.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null)
                return;
            typeEspace = new String(newValue);
            gridLit.getChildren().clear();
            // Send http request
            Response response;
            response = target
                    .path("departement")
                    .path(service)
                    .path(typeEspace)
                    .path("lits")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            System.out.println(response);
            // Retrieve list
            List<String> espaces = response.readEntity(List.class);
            List<JSONObject> espacesJson =
                    espaces
                            .stream()
                            .map(espace -> new JSONObject(espace))
                            .collect(Collectors.toList());


            // Convert List of JSONObjects to List of RoomCards
            List<Node> nodes = getRoomCards(espacesJson, service, typeEspace, gridLit).stream().map(espace -> (Node) espace).collect(Collectors.toList());

            // Populate grid
            populateGrid(gridLit, nodes, 3);
            scrollPane.setVisible(true);
            lblConsulterService.setText(typeEspace + "s" + " du service de " + service);
            lblConsulterService.autosize();
            lblConsulterService.setVisible(true);
            System.out.println("Hello");
            chBoxEspaceService.getSelectionModel().clearSelection();
        });

        try {
            onShowHistorique();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Reservation des lits
        scrollReservation.setFitToHeight(true);
        scrollReservation.setContent(tableReservation);
        tabecolumn1.setText("Lit");
        tabecolumn2.setText("Espace");
        tabecolumn2.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get("numEspace").toString()));
        tabecolumn1.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get("code").toString()));
        actioncolumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Map<String, Object>, Map<String, Object>> call(
                    final TableColumn<Map<String, Object>, Map<String, Object>> param) {
                final TableCell<Map<String, Object>, Map<String, Object>> cell = new TableCell<>() {
                    private final HBox hbox = new HBox();
                    private final Button addButton = new Button("Add");

                    {
                        hbox.getChildren().add(addButton);
                        hbox.setAlignment(Pos.CENTER);
                        addButton.setStyle("-fx-background-color: green");
                        addButton.setOnAction((ActionEvent event) -> {

                            Map<String, Object> data = getTableView().getItems().get(getIndex());

                            if (addButton.getText().equals("Add")) {
                                addButton.setText("Added");
                                addButton.setStyle("-fx-background-color: red");
                                list_of_all_reservation.add(data.get("id").toString());
                                System.out.println(list_of_all_reservation.size());
                            } else {
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
                cell.setPrefHeight(30);
                return cell;
            }
        });



        // populate table reservation
        populateTableReservation();

        double rowHeight = 30;
        double tableHeight = tableReservation.getItems().size() * rowHeight;
        tableReservation.setPrefHeight(tableHeight);
        scrollhistorique.setPrefHeight(300);
    }

    @FXML
    public void onShowHistorique() throws JsonProcessingException {
        BookCardApp book = new BookCardApp(service);
        flowHistorique = book.parentGridPane;
        scrollhistorique.setContent(flowHistorique);
        scrollhistorique.setStyle("-fx-background-color:  linear-gradient(to bottom right, #3f51b5, #2196f3)");
        scrollhistorique.setFitToHeight(true);
        scrollhistorique.setPrefHeight(650);
    }

    public void populateTableReservation() {
        tableReservation.getItems().clear();
        tableReservation.refresh();
        list_of_all_reservation.clear();
        Response response = target
                .queryParam("nomDepartement", service)
                .queryParam("espaceType", "espace")
                .path("lits")
                .path("litdisponible")
                .request(MediaType.APPLICATION_JSON_TYPE)
                //  .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user1:1234,user".getBytes()))
                .get();

// Create an ObservableList of Map objects
        ObservableList<Map<String, Object>> items = FXCollections.observableArrayList();

//        items.addAll(row1, row2);
        List<String> lstlitdisponbile = response.readEntity(List.class);

        for (String lit : lstlitdisponbile) {
            lit = lit.replaceAll("\\s*:\\s*", "\":\"")
                    .replaceAll("\\s*,\\s*", "\",\"")
                    .replaceAll("\\{", "{\"")
                    .replaceAll("}", "\"}");

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> hashMap = null;
            try {
                hashMap = objectMapper.readValue(lit, HashMap.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            System.out.println(hashMap);
            items.add(hashMap);
        }
        tableReservation.setItems(items);
    }

    // Aya
    @FXML
    protected void onSupprimerTableViewDemandes(ActionEvent event) throws IOException {
        int lastIndex = tableData.size() - 1;
        if (lastIndex >= 0) {
            tableData.remove(lastIndex);
            ajouterDemandesTableView.getSelectionModel().clearSelection();
        }
    }


    @FXML

    protected void onAfficherDemandes(ActionEvent event) throws IOException {
        Response getResponse = target.path("demandeaffectation")
                .path("demandesDepartement")
                .queryParam("nomDepartement", service)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

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
                demandeAffectation.setDateDebut(dateDebut);
                demandeAffectation.setQte(qte);
                demandeAffectation.setTypeLit(typeLit);
                demandeAffectation.setModelLit(modelLit);
                demandeAffectation.setDepartement(departement);

                tableData.add(demandeAffectation);
            }

            // Set the table data and cell value factories
            afficherDemandesTableView.setItems(tableData);

            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            etatDemandeCol.setCellValueFactory(new PropertyValueFactory<>("etatDemande"));
            date_debutCol.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
            qteCol.setCellValueFactory(new PropertyValueFactory<>("qte"));
            typeLitCol.setCellValueFactory(new PropertyValueFactory<>("typeLit"));
            modelLitCol.setCellValueFactory(new PropertyValueFactory<>("modelLit"));
            departementCol.setCellValueFactory(new PropertyValueFactory<>("departement"));
        }
    }
    public void onAjouterDemandeAffectation(ActionEvent event) throws IOException {
        String typeLit = TypeLitChoiceBox.getValue();
        String modelLit = ModelLitChoiceBox.getValue();
        int qte = Integer.parseInt(qteTextField.getText());
        DemandeAffectationFE DAFE = new DemandeAffectationFE(0, null, null, qte, typeLit, modelLit, service);
        tableData.add(DAFE);
        ajouterDemandesTableView.setItems(tableData);
        qteColumn.setCellValueFactory(new PropertyValueFactory<>("qte"));
        typeLitColumn.setCellValueFactory(new PropertyValueFactory<>("typeLit"));
        modelLitColumn.setCellValueFactory(new PropertyValueFactory<>("modelLit"));
        departementColumn.setCellValueFactory(new PropertyValueFactory<>("departement"));
        ajouterDemandesTableView.refresh();
        demandesAEnvoyer.add(DAFE);
    }

    public void onCommanderButtonClicked (ActionEvent event) throws IOException {
        System.out.println("Sending demandeaffectation to server");
        WebTarget demandeaffectationEndpoint = target.path("/demandeaffectation");

        for (DemandeAffectationFE demandeAffectation : tableData) {
            try {
                String typeLit = demandeAffectation.getTypeLit();
                String modelLit = demandeAffectation.getModelLit();
                int qte = demandeAffectation.getQte();

                WebTarget currentTarget = demandeaffectationEndpoint
                        .queryParam("typeLit", typeLit)
                        .queryParam("modelLit", modelLit)
                        .queryParam("Departement", service)
                        .queryParam("qte", qte);

                Response response = currentTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(""));
                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed to save demandeaffectation: " + response.getStatus());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tableData.clear();
    }

    // Rachid
    @FXML
    void onBtnSaveAllreservationClick() throws JsonProcessingException {

        for(String elt:list_of_all_reservation){

            Response response = target
                    .queryParam("id", elt)
                    .path("lits")
                    .path("admin")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user1:1234,user".getBytes()))
                    .method("POST");

            System.out.println(" rachid "+response.getStatus());

        }

//        this.onBtnReservationSearch(event);
        String value = chBoxReservationService11.getValue();
        chBoxReservationService11.getSelectionModel().clearSelection();
        chBoxReservationService11.setValue(value);
        populateTableReservation();
//        tableReservation.refresh();
        onShowHistorique();
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
