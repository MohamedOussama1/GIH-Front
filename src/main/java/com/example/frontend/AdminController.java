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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
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


    private Client client = ClientBuilder.newClient();

    private WebTarget target = client.target("http://localhost:8081");

    @FXML
    protected void onAfficherService(ActionEvent event) throws IOException {
        Response getResponse = target
                .path("departement")
                .request()
                .get();
        List<String> demandes = getResponse.readEntity(List.class);
        ListServiceAfficher.setItems(FXCollections.observableArrayList(demandes));


    }
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
        String nomDepartement = ServicesDemandesChoiceBox.getValue();
        Response getResponse = target.path("demandeaffectation")
                .path("demandesDepartement")
                .queryParam("nomDepartement", nomDepartement)
                .request()
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

        @FXML
        protected void onChangerEtatDemandes(ActionEvent event) throws IOException {
        String nomDepartement = ServicesDemandesChoiceBox1.getValue();
            Response getResponse;

            if (nomDepartement == null || nomDepartement.equals("Default")) {
                getResponse = target.path("demandeaffectation")
                        .request()
                        .get();
            } else {
                getResponse = target.path("demandeaffectation")
                        .path("demandesDepartement")
                        .queryParam("nomDepartement", nomDepartement)
                        .request()
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
                            .request()
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

private ObservableList<DemandeAffectationFE> tableData = FXCollections.observableArrayList();
private List<DemandeAffectationFE> demandesAEnvoyer = new ArrayList<>();

    public void onAjouterDemandeAffectation(ActionEvent event) throws IOException {

            String typeLit = TypeLitChoiceBox.getValue();
            String modelLit = ModelLitChoiceBox.getValue();
            String service = ServiceChoiceBox.getValue();
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
                String departement = demandeAffectation.getDepartement();
                int qte = demandeAffectation.getQte();

                WebTarget currentTarget = demandeaffectationEndpoint
                        .queryParam("typeLit", typeLit)
                        .queryParam("modelLit", modelLit)
                        .queryParam("Departement", departement)
                        .queryParam("qte", qte);

                Response response = currentTarget.request().post(Entity.json(""));
                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed to save demandeaffectation: " + response.getStatus());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Clear the tableData after all demandeaffectations have been posted
        tableData.clear();
    }







    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> listTypeLit= new ArrayList<>();
        listTypeLit.addAll(Arrays.asList("MECANIQUE", "ELECTRIQUE"));
        List<String> listTypeEspace= new ArrayList<>();
        listTypeEspace.addAll(Arrays.asList("Salle", "Chambre"));
        List<String> listModelLit= new ArrayList<>();
        listModelLit.addAll(Arrays.asList("STANDARD", "ROTATION", "PESEE", "BARIATRIQUE", "CIRCULATION", "TRAITEMENT"));
        TypeLitChoiceBox.setItems(FXCollections.observableArrayList(listTypeLit));
        ModelLitChoiceBox.setItems(FXCollections.observableArrayList(listModelLit));
        TypeEspaceChoiceBox.setItems(FXCollections.observableArrayList(listTypeEspace));


        Response getmyResponse = target
                .path("departement")
                .request()
                .get();
        List<String> departements = getmyResponse.readEntity(List.class);
        ServiceChoiceBox.setItems(FXCollections.observableArrayList(departements));
        supprimerServiceChoiceBox.setItems(FXCollections.observableArrayList(departements));
        ServicesDemandesChoiceBox.setItems(FXCollections.observableArrayList(departements));
        ServicesDemandesChoiceBox1.setItems(FXCollections.observableArrayList(departements));
        ServicesDemandesChoiceBox1.getItems().add("Default");




        TypeEspaceChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String selectedDepartement = ServiceChoiceBox.getValue();
            if (newValue != null) {
                if (newValue.equals("Salle")) {
                    if (selectedDepartement != null) {
                        Response getSalleResponse = target
                                .path("departement")
                                .path("salle")
                                .queryParam("nomDepartement", selectedDepartement)
                                .request()
                                .get();
                        String jsonString = getSalleResponse.readEntity(String.class);
                        JSONArray jsonArray = new JSONArray(jsonString);
                        List<String> salleList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String salle = jsonArray.getString(i);
                            salleList.add(salle);
                        }
                        System.out.println(salleList);
                        ListEspacesChoiceBox.setItems(FXCollections.observableArrayList(salleList));}}

                if (newValue.equals("Chambre")){
                    if (selectedDepartement != null) {
                        Response getEspaceResponse = target
                                .path("departement")
                                .path("chambre")
                                .queryParam("nomDepartement", selectedDepartement)
                                .request()
                                .get();
                        String jsonString = getEspaceResponse.readEntity(String.class);
                        JSONArray jsonArray = new JSONArray(jsonString);
                        List<String> chambreList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String chambre = jsonArray.getString(i);
                            chambreList.add(chambre);
                        }
                        System.out.println(chambreList);
                        ListEspacesChoiceBox.setItems(FXCollections.observableArrayList(chambreList));
                    }
                }
            }
            else {
                ListEspacesChoiceBox.setItems(FXCollections.observableArrayList(Arrays.asList("NULL")));

            }
        });
        qteColumn.setCellValueFactory(new PropertyValueFactory<>("qte"));
        typeLitColumn.setCellValueFactory(new PropertyValueFactory<>("typeLit"));
        modelLitColumn.setCellValueFactory(new PropertyValueFactory<>("modelLit"));
        departementColumn.setCellValueFactory(new PropertyValueFactory<>("departement"));

        tableData = FXCollections.observableArrayList();
        ajouterDemandesTableView.setItems(tableData);



                }



    }



