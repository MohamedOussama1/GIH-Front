package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.controlsfx.control.table.TableRowExpanderColumn;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DmLogistiqueController implements Initializable{
    @FXML
    ChoiceBox<String> chBoxEtatDemande;
    private Client client = ClientBuilder.newClient().register(JacksonFeature.class);

    private WebTarget target = client.target(Connextion_info.url);
    @FXML
    TableView afficherDemandesTableView;
    @FXML
    TableColumn idCol;
    @FXML
    TableColumn serviceCol;
    @FXML
    TableColumn dateDebutCol;
    @FXML
    TableColumn etatDemandeCol;
    @FXML
    Button btnAfficherDemandesDm;
    @FXML
    ChoiceBox<String> chBoxService = new ChoiceBox<>();
    @FXML
    ChoiceBox<String> chBoxTypeDm = new ChoiceBox<>();
    @FXML
    ChoiceBox<String> chBoxNomDm = new ChoiceBox<>();
    @FXML
    Spinner<Integer> spinnerQuantityDm = new Spinner<>();

    @FXML
    ChoiceBox<String> typedmChoiceBox = new ChoiceBox();
    @FXML
    TextField typedm;
    @FXML
    TextField nomdm;
    @FXML
    TextField qtedm;
    @FXML
    Button créertypedm;
    @FXML
    Button ajoutertypedm;
    @FXML
    Label typedmlabel;
    private ObservableList<DetailDemandeDm> tableData = FXCollections.observableArrayList();
    private List<DetailDemandeDm> demandesAEnvoyer = new ArrayList<>();

    @FXML

    protected void onAjouterTypeDM(ActionEvent event) throws IOException {
        String nomtypedm = typedm.getText();
        Response postResponse = target.path("dm")
                .path("posttypedm")
                .queryParam("nomtypedm", nomtypedm)
                .request()
                .post(Entity.json(""));
        if (postResponse.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println("Type DM created successfully!");
            Response getResponse = target.path("dm")
                    .path("typedm")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            List<String> listtypedm = getResponse.readEntity(List.class);
            typedmChoiceBox.setItems(FXCollections.observableArrayList(listtypedm));
        } else {
            System.out.println("Error adding type DM!");
        }
    }
    @FXML
    protected void onAjouterDM(ActionEvent event) throws IOException {
        String typeDM = typedmChoiceBox.getValue();
        String dm = nomdm.getText();
        int qte = Integer.parseInt(qtedm.getText());
        Response postResponse = target.path("dm")
                .path("postdm")
                .queryParam("nom", dm)
                .queryParam("qte", qte)
                .queryParam("typedm", typeDM)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(""));
        if (postResponse.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println("Type DM created successfully!");
        } else {
            System.out.println("Error adding type DM!");
        }
    }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Response getResponse = target
                .path("dm")
                .path("typedm")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        List<String> listtypedm = getResponse.readEntity(List.class);
        typedmChoiceBox.setItems(FXCollections.observableArrayList(listtypedm));

        typedm.setVisible(false);
        ajoutertypedm.setVisible(false);
        typedmlabel.setVisible(false);
        créertypedm.setOnAction(event -> {

            typedm.setVisible(true);
            ajoutertypedm.setVisible(true);
            typedmlabel.setVisible(true);
        });
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200);
        valueFactory.setValue(1);
        spinnerQuantityDm.setValueFactory(valueFactory);

        // Demandes
        chBoxService.setItems(FXCollections.observableArrayList(
                target
                        .path("departement")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get()
                        .readEntity(List.class)
        ));
        chBoxTypeDm.setItems(FXCollections.observableArrayList(listtypedm));
        chBoxTypeDm.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            Response response = target
                    .path("dm")
                    .path("dmbyType")
                    .queryParam("typeDM", newValue)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            chBoxNomDm.setItems(FXCollections.observableArrayList(response.readEntity(List.class)));
        }));

    }
    @FXML
    protected void onAfficherDemandesDm(ActionEvent event) throws IOException {
        Response getResponse = target
                .path("demandeDm")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (getResponse.getStatus() == 200) {
            List<String> responseString = getResponse.readEntity(List.class);
            System.out.println("Response String: " + responseString);
            JSONArray jsonArray = new JSONArray(responseString);
            ObservableList<DemandeDm> tableData = FXCollections.observableArrayList();


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));

                // DemandeDm
                JSONObject demande = new JSONObject(jsonObject.getString("demande"));
                int id = demande.getInt("id");
                String etatDemande = demande.getString("etatDemande");
                String date_debut = demande.getString("dateDebut");
                String departement = demande.getString("nomDepartement");
                DemandeDm demandeDm = new DemandeDm();
                demandeDm.setId(id);
                demandeDm.setEtatDemande(etatDemande);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dateDebut = LocalDate.parse(date_debut, formatter);
                demandeDm.setDateDebut(dateDebut);
                demandeDm.setDepartement(departement);

                // DetailsDemande
                JSONArray detailsDemande = jsonObject.getJSONArray("detailsDemande");
                List<DetailDemandeDm> detailsDemandeDm = new ArrayList<>();
                detailsDemande.forEach(detailDemande -> {
                    JSONObject jsonDetailDemande = new JSONObject(detailDemande.toString());
                    System.out.println(jsonDetailDemande);
                    System.out.println(detailDemande);
                    DetailDemandeDm detailDemandeDm = new DetailDemandeDm(
                            jsonDetailDemande.getString("etatDetail"),
                            jsonDetailDemande.getInt("id"),
                            jsonDetailDemande.getInt("qte"),
                            jsonDetailDemande.getJSONObject("dm").getJSONObject("typeDM").getString("nomType"),
                            jsonDetailDemande.getJSONObject("dm").getString("nom"),
                            jsonDetailDemande.getJSONObject("dm").getInt("id"));
                    detailsDemandeDm.add(detailDemandeDm);
                    System.out.println(detailDemandeDm.getEtat());
                });
                demandeDm.setDetailsDemandeDm(detailsDemandeDm);
                boolean done = true;
                for (DetailDemandeDm detailDemandeDm : detailsDemandeDm) {
                    if (detailDemandeDm.getEtat().equals("CONFIRMÉE"))
                        done = false;
                }
                if (done)
                    demandeDm.setEtatDemande("TRAITÉE");
                tableData.add(demandeDm);
            }


            TableRowExpanderColumn<DemandeDm> expanderColumn = new TableRowExpanderColumn<>(this::createEditor);

            // Set the table data and cell value factories
            System.out.println(tableData);
            afficherDemandesTableView.setItems(tableData);
            if (!afficherDemandesTableView.getColumns().get(0).getClass().equals(expanderColumn.getClass()))
                afficherDemandesTableView.getColumns().add(0, expanderColumn);
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            etatDemandeCol.setCellValueFactory(new PropertyValueFactory<>("etatDemande"));
            dateDebutCol.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
            serviceCol.setCellValueFactory(new PropertyValueFactory<>("departement"));
        }

    }
    private VBox createEditor(TableRowExpanderColumn.TableRowDataFeatures<DemandeDm> param){
        VBox editor = new VBox();
        editor.setAlignment(Pos.CENTER);
        DemandeDm demandeDm = param.getValue();
        TableView tableView = new TableView<>(FXCollections.observableArrayList(demandeDm.getDetailsDemandeDm()));

        TableColumn typeDmCola = new TableColumn("Type");
        TableColumn nomDmCola = new TableColumn("Nom");
        TableColumn qteCola = new TableColumn("Quantité");
        TableColumn accepterCola = new TableColumn("Accepter");
        TableColumn rejeterCola = new TableColumn("Rejeter");
        tableView.getColumns().addAll(typeDmCola, nomDmCola, qteCola, accepterCola, rejeterCola);

        typeDmCola.setCellValueFactory(new PropertyValueFactory<>("typeDm"));
        nomDmCola.setCellValueFactory(new PropertyValueFactory<>("nomDm"));
        qteCola.setCellValueFactory(new PropertyValueFactory<>("qte"));

        accepterCola.setCellFactory(column -> new TableCell<DetailDemandeDm, String>() {
            private Button btnAccepter = new Button();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else if (getTableView().getItems().get(getIndex()).getEtat().equals("REJETÉE")){
                    setGraphic(null);
                } else if (getTableView().getItems().get(getIndex()).getEtat().equals("ACCEPTÉE")) {
                    Label lblAccepte = new Label("Acceptée");
                    lblAccepte.setStyle("-fx-text-fill: #76ff76");
                    setGraphic(lblAccepte);
                } else {
                    DetailDemandeDm detailDemandeDm = getTableView().getItems().get(getIndex());
                    btnAccepter.setGraphic(new ImageView(new Image("plus.png", 20,20, true, true)));
                    btnAccepter.setStyle("-fx-background-color: transparent");
                    btnAccepter.setOnAction(event -> {
                        detailDemandeDm.setEtat("ACCEPTÉE");
                        Response response = target
                                .path("demandeDm")
                                .path("detailDemande")
                                .queryParam("idDetail", detailDemandeDm.getId())
                                .queryParam("etatDetail", "acceptée")
                                .request(MediaType.APPLICATION_JSON_TYPE)
                                .put(Entity.json("Hello"));
                        System.out.println(detailDemandeDm.getEtat());
                        getTableView().refresh();
                    });
                    setGraphic(btnAccepter);
                }
            };});
        rejeterCola.setCellFactory(column -> new TableCell<DetailDemandeDm, String>() {
            private Button btnRejeter = new Button();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                System.out.println(getIndex());
                if (empty) {
                    setGraphic(null);
                } else if (getTableView().getItems().get(getIndex()).getEtat().equals("ACCEPTÉE")){
                    setGraphic(null);
                } else if (getTableView().getItems().get(getIndex()).getEtat().equals("REJETÉE")) {
                    Label lblRejete = new Label("Rejetée");
                    lblRejete.setStyle("-fx-text-fill: #ff6464");
                    setGraphic(lblRejete);
                } else {
                    DetailDemandeDm detailDemandeDm = getTableView().getItems().get(getIndex());
                    btnRejeter.setGraphic(new ImageView(new Image("delete.png", 20,20, true, true)));
                    btnRejeter.setStyle("-fx-background-color: transparent");
                    btnRejeter.setOnAction(event -> {
                        detailDemandeDm.setEtat("REJETÉE");
                        Response response = target
                                .path("demandeDm")
                                .path("detailDemande")
                                .queryParam("idDetail", detailDemandeDm.getId())
                                .queryParam("etatDetail", "rejetée")
                                .request(MediaType.APPLICATION_JSON_TYPE)
                                .put(Entity.json("Hello"));
                        System.out.println(detailDemandeDm.getEtat());
                        getTableView().refresh();
                    });
                    setGraphic(btnRejeter);
                }
            };});

        ObservableList<DetailDemandeDm> data = FXCollections.observableArrayList(demandeDm.getDetailsDemandeDm());
        tableView.setItems(data);
        tableView.getStylesheets().add("expandedTable.css");
        tableView.setFixedCellSize(30);
        tableView.prefHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(Bindings.size(tableView.getItems()).add(1.18)));
        tableView.minHeightProperty().bind(tableView.prefHeightProperty());
        tableView.maxHeightProperty().bind(tableView.prefHeightProperty());
        System.out.println(demandeDm.getDetailsDemandeDm());

        editor.getChildren().add(tableView);
        return editor;
    }

}
