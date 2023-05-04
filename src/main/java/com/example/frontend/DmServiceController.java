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

public class DmServiceController implements Initializable {
    @FXML
    ChoiceBox<String> chBoxEtatDemande;

    private Client client = ClientBuilder.newClient().register(JacksonFeature.class);

    private WebTarget target = client.target(Connextion_info.url);
    @FXML
    TableView afficherDemandesTableView;
    @FXML
    TableColumn idCol;
    @FXML
    TableColumn dateDebutCol;
    @FXML
    TableColumn etatDemandeCol;
    @FXML
    Button btnAfficherDemandesDm;
    @FXML
    ChoiceBox<String> chBoxTypeDm = new ChoiceBox<>();
    @FXML
    ChoiceBox<String> chBoxNomDm = new ChoiceBox<>();
    @FXML
    Button btnDeleteDemandeDm;
    @FXML
    Button btnConfirmDemandeDm;
    @FXML
    Spinner<Integer> spinnerQuantityDm;
    @FXML
    TableView tblViewAddDetailDemandeDm;
    @FXML
    TableColumn typeDmColumn;
    @FXML
    TableColumn nomDmColumn;
    @FXML
    TableColumn qteDmColumn;
    @FXML
    Button btnAddDemandeDm;
    private ObservableList<DetailDemandeDm> tableData = FXCollections.observableArrayList();
    private List<DetailDemandeDm> demandesAEnvoyer = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Response getResponse = target
                .path("dm")
                .path("typedm")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        List<String> listtypedm = getResponse.readEntity(List.class);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200);
        valueFactory.setValue(1);
        spinnerQuantityDm.setValueFactory(valueFactory);

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
        chBoxEtatDemande.setItems(FXCollections.observableArrayList("CONFIRMÉE", "TRAITÉE"));
        chBoxEtatDemande.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            Response response = target
                    .path("demandeDm")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            if (response.getStatus() == 200) {
                List<String> responseString = response.readEntity(List.class);
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
                                jsonDetailDemande.getJSONObject("dm").getString("nom"));
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
                    if(demandeDm.getEtatDemande().equals(newValue))
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
            }
        });
    }

    @FXML
    public void onAjouterDetailDemandeDm(ActionEvent event) throws IOException {
        String typeDm = chBoxTypeDm.getValue();
        String nomDm = chBoxNomDm.getValue();
        int qte = spinnerQuantityDm.getValue();
        DetailDemandeDm detailDemandeDm = new DetailDemandeDm(qte, typeDm, nomDm);
        tableData.add(detailDemandeDm);
        tblViewAddDetailDemandeDm.setItems(tableData);
        qteDmColumn.setCellValueFactory(new PropertyValueFactory<>("qte"));
        typeDmColumn.setCellValueFactory(new PropertyValueFactory<>("typeDm"));
        nomDmColumn.setCellValueFactory(new PropertyValueFactory<>("nomDm"));
        tblViewAddDetailDemandeDm.refresh();
        demandesAEnvoyer.add(detailDemandeDm);
    }
    @FXML
    public void onConfirmDemandeDm(ActionEvent event) throws IOException {
        if (tableData.size()==0)
            return;
        Response postDemandeResponse = target
                .path("demandeDm")
                .queryParam("nomDepartement", "Cardiologie")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(null));
        int idDemandeDm = postDemandeResponse.readEntity(Integer.class);
        if (postDemandeResponse.getStatus() == 200) {
            for (DetailDemandeDm detailDetailDemandeDm : tableData) {
                try {
                    String nomDM = detailDetailDemandeDm.getNomDm();
                    int qte = detailDetailDemandeDm.getQte();
                    Response response = target
                            .path("demandeDm")
                            .path("detailDemande")
                            .queryParam("idDemandeDm", idDemandeDm)
                            .queryParam("nomDm", nomDM)
                            .queryParam("qte", qte)
                            .request(MediaType.APPLICATION_JSON_TYPE)
                            .post(Entity.json(null));
                    if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed to save detailDemandeDm: " + response.getStatus());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tableData.clear();
            Response cofirmDemandeResponse = target
                    .path("demandeDm")
                    .path(String.valueOf(idDemandeDm))
                    .path("confirmée")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .put(Entity.json("Hello"));
            System.out.println(cofirmDemandeResponse.getStatus());
        }

    }
    @FXML
    public void onDeleteDetailDemandeDm(ActionEvent event) throws IOException {
        int lastIndex = tableData.size() - 1;
        if (lastIndex >= 0) {
            tableData.remove(lastIndex);
            tblViewAddDetailDemandeDm.getSelectionModel().clearSelection();
        }
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
                            jsonDetailDemande.getJSONObject("dm").getString("nom"));
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
        TableColumn etatCola = new TableColumn("Etat");
        tableView.getColumns().addAll(typeDmCola, nomDmCola, qteCola, etatCola);

        typeDmCola.setCellValueFactory(new PropertyValueFactory<>("typeDm"));
        nomDmCola.setCellValueFactory(new PropertyValueFactory<>("nomDm"));
        qteCola.setCellValueFactory(new PropertyValueFactory<>("qte"));
        etatCola.setCellValueFactory(new PropertyValueFactory<>("etat"));

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
