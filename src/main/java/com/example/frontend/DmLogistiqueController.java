package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.table.TableRowExpanderColumn;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DmLogistiqueController implements Initializable{
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
    TextField typedm = new TextField();
    @FXML
    TextField nomdm = new TextField();
    @FXML
    TextField qtedm = new TextField();
    @FXML
    Button créertypedm;
    @FXML
    Button ajoutertypedm;
    @FXML
    Label typedmlabel;
    // Traiter demandes
    @FXML
    TableColumn iddCol;
    @FXML
    Button btnAfficherDemandesDm;
    // Afficher demandes

    @FXML
    TableView afficherDemandesTableView1;
    @FXML
    TableColumn idCol1;
    @FXML
    TableColumn serviceCol1;
    @FXML
    TableColumn dateDebutCol1;
    @FXML
    TableColumn etatDemandeCol1;
    @FXML
    Button btnAfficherDemandesDm1;
    @FXML
    Button btnDeleteDemandeDm;
    @FXML
    Button btnConfirmDemandeDm;
    @FXML
    TableView tblViewAddDetailDemandeDm;
    @FXML
    TableColumn typeDmColumn;
    @FXML
    TableColumn nomDmColumn;
    @FXML
    TableColumn qteDmColumn;
    @FXML
    TableColumn serviceDmColumn;
    @FXML
    Button btnAddDemandeDm;
    @FXML
    private ObservableList<DetailDemandeDm> tableData = FXCollections.observableArrayList();
    private List<DetailDemandeDm> demandesAEnvoyer = new ArrayList<>();





    // ============================== rachid   ===========================


    @FXML
    ChoiceBox<String> typedmChoiceBox1 = new ChoiceBox();

    @FXML

    TextField txt_categorie;
//    @FXML
//    TableView<DM> afficherDMTableView;
//    @FXML
//    private TableColumn<DM, Integer> idCol = new TableColumn<>("id");
//    @FXML
//    private TableColumn<DM, Integer> qteColumn = new TableColumn<>("quantité");
//    @FXML
//    private TableColumn<DM, String> nomDMeCol = new TableColumn<>("nom");
//    @FXML
//    private TableColumn<DM, String> typeDMColumn = new TableColumn<>("typeDM");

    @FXML
    private TextField nomdm1;

    @FXML
    private Button btn_create_dm;

    @FXML
    private Button btn_afficher_dm;



    //aya2front


    @FXML
    TableView<DM> SupprimerModifierDMTableView;
    @FXML
    private TableColumn<DM, Integer> idColSM =new TableColumn<>("id");
    @FXML
    private TableColumn<DM, Integer> qteColumnSM =new TableColumn<>("quantité");
    @FXML
    private TableColumn<DM, String> nomDMColSM = new TableColumn<>("nom");
    @FXML
    private TableColumn<DM, String> typeDMColumnSM = new TableColumn<>("typeDM");
    @FXML
    private TableColumn<DM, Void> actionsColSM = new TableColumn<>("Actions");
    @FXML
    Tab tabCréerDemandes;
    @FXML
    Tab tabTraiterDemandes;
    @FXML
    Tab tabAfficherDemandes;
    @FXML
    TabPane tabPaneDemandes;



    //aya

    /////   rachid  //////

    @FXML
    Tab tab_affecter_dm;

    @FXML
    AnchorPane anchorpane_affecter_dm;

    @FXML
    TextField txt_filed_imagePath;

    @FXML
    ImageView image_upload;

    @FXML
    Button btnUpload;


    String last_path = "";

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

        btnUpload.setGraphic(new ImageView(new Image("upload-file.png")));

        SpinnerValueFactory<Integer> valueFactoryLivraison = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200);
        valueFactoryLivraison.setValue(1);
        spinnerQteLivraison.setValueFactory(valueFactoryLivraison);




        SpinnerValueFactory<Integer> valueFactoryDm = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200);
        valueFactoryDm.setValue(1);
        spinnerQuantityDm.setValueFactory(valueFactoryDm);

        // Demandes
        chBoxService.setItems(FXCollections.observableArrayList(
                target
                        .path("departement")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get()
                        .readEntity(List.class)
        ));

        // Youness

        Response getTypeResponse = target
                .path("dm")
                .path("typedm")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        List<HashMap<String, String>> listtypedm = getTypeResponse.readEntity(List.class);
        List<String> lstNomTypesDm = new ArrayList<>();
        listtypedm.forEach(elt -> lstNomTypesDm.add(elt.get("Nom")));
        typedmChoiceBox.setItems(FXCollections.observableArrayList(lstNomTypesDm));
        typeDM2.setItems(FXCollections.observableArrayList(lstNomTypesDm));
        chBoxTypeDm.setItems(FXCollections.observableArrayList(lstNomTypesDm));
        chBoxTypeDm.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            Response response = target
                    .path("dm")
                    .path("dmbyType")
                    .queryParam("typeDM", newValue)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            chBoxNomDm.setItems(FXCollections.observableArrayList(response.readEntity(List.class)));
        }));

        Response getResponse2 = target
                .path("/fournisseur")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        List<String> listFournisseur = getResponse2.readEntity(List.class);
        fournisseurs.setItems(FXCollections.observableArrayList(listFournisseur));
        // fournisseurChoicebox.setItems(FXCollections.observableArrayList(listFournisseur));



        tableDetailLivraison.getColumns().addAll(dmColumn, qteEColumn);
        dmColumn.setCellValueFactory(new PropertyValueFactory<>("dm"));
        qteEColumn.setCellValueFactory(new PropertyValueFactory<>("qte"));


        Response getResponse4 = target.path("/livraisonDM").path("livraisonlist")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();


        if (getResponse4.getStatus() == Response.Status.OK.getStatusCode()) {
            String responseString = getResponse4.readEntity(String.class);
            //System.out.println("Response String: " + responseString);
            JSONArray jsonArray = new JSONArray(responseString);
            ObservableList<LivraisonFx> tableData1 = FXCollections.observableArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String nomFournisseur = jsonObject.getString("fournisseur");
                String date = jsonObject.getString("date");

                LivraisonFx livraisonfx = new LivraisonFx(id, nomFournisseur, date);

                JSONArray detailsLivraison = jsonObject.getJSONArray("detailsLivraison");
                //System.out.println(detailsLivraison);
                List<DetailLivraisonFX> detailsLiv = new ArrayList<>();
                detailsLivraison.forEach(detailLiv -> {
                    JSONObject jsonDetailLivraison = new JSONObject(detailLiv.toString());
                    //System.out.println(jsonDetailLivraison);
                    //System.out.println(detailLiv);
                    DetailLivraisonFX detailLivraisonFX = new DetailLivraisonFX(
                            jsonDetailLivraison.getJSONObject("dm").getString("nom"),
                            String.valueOf(jsonDetailLivraison.getInt("qte")));
                    detailsLiv.add(detailLivraisonFX);
                });

                livraisonfx.setDetailLivraisonFXES(detailsLiv);
                tableData1.add(livraisonfx);
            }

            TableRowExpanderColumn<LivraisonFx> expanderColumn = new TableRowExpanderColumn<>(this::createEditorLiv);
            afficherLivraison.setItems(tableData1);
            afficherLivraison.getColumns().add(0, expanderColumn);
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            fournisseurColumn.setCellValueFactory(new PropertyValueFactory<>("nomFournisseur"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        }


        Response getResponse3 = target.path("fournisseur").path("/listfournisseur")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (getResponse3.getStatus() == Response.Status.OK.getStatusCode()) {
            String responseString = getResponse3.readEntity(String.class);
            //System.out.println("Response String: " + responseString);
            JSONArray jsonArray = new JSONArray(responseString);
            ObservableList<FournisseurFx> tableData = FXCollections.observableArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String nom = jsonObject.getString("nom");
                String email = jsonObject.getString("email");
                String ville = jsonObject.getString("ville");
                String codeape = jsonObject.getString("codeape");
                String formejuridique = jsonObject.getString("formejuridique");
                String numsiren = jsonObject.getString("num_siren");
                String tel = jsonObject.getString("tel");


                FournisseurFx fournisseurFx = new FournisseurFx(id, nom, email, ville, codeape, formejuridique, numsiren, tel);


                tableData.add(fournisseurFx);
            }

            supprimercolumn.setCellFactory(column -> new TableCell<FournisseurFx, String>() {
                private Button btnsupprimer = new Button();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //System.out.println(getIndex());
                    if (empty) {
                        setGraphic(null);
                    } else {
                        FournisseurFx fournisseurFx = getTableView().getItems().get(getIndex());
                        btnsupprimer.setGraphic(new ImageView(new Image("delete.jpg", 20, 20, true, true)));
                        btnsupprimer.setStyle("-fx-background-color: transparent");
                        btnsupprimer.setOnAction(event -> {
                            int id = fournisseurFx.getId();
                            //getTableView().refresh();
                            ObservableList<FournisseurFx> fournisseurFxes = getTableView().getItems();
                            boolean canDelete = true;
                            for (LivraisonFx livraisonFx : afficherLivraison.getItems()){
                                //System.out.println(livraisonFx.getNomFournisseur());
                                //System.out.println("hello");
                                //System.out.println(fournisseurFx.getNom());
                                if (livraisonFx.getNomFournisseur().equals(fournisseurFx.getNom()))
                                    canDelete  = false;
                            }
                            if (canDelete) {
                                Response response = target
                                        .path("/fournisseur")
                                        .path("/deleteFournisseur")
                                        .queryParam("fournisseur_id", id)
                                        .request(MediaType.APPLICATION_JSON_TYPE)
                                        .method("DELETE");
                                //System.out.println(response);
                                fournisseurFxes.remove(fournisseurFx);
                                afficherFournisseurs.setItems(fournisseurFxes);
                                Response getResponse2 = target
                                        .path("/fournisseur")
                                        .request(MediaType.APPLICATION_JSON_TYPE)
                                        .get();
                                List<String> listFournisseur = getResponse2.readEntity(List.class);
                                fournisseurs.setItems(FXCollections.observableArrayList(listFournisseur));
                            }else {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Failed");
                                alert.setHeaderText(null);
                                alert.setContentText("Operation échouée, il existe des livraisons associées à ce fournisseur");
                                alert.showAndWait();
                            }
                        });
                        setGraphic(btnsupprimer);
                    }
                }
            });


            modifiercolumn.setCellFactory(column -> new TableCell<DmController.FournisseurFx, String>() {
                private Button modifierbtn = new Button();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //System.out.println(getIndex());
                    if (empty) {
                        setGraphic(null);
                    } else {
                        DmController.FournisseurFx fournisseurFx = getTableView().getItems().get(getIndex());
                        Stage stage = new Stage();
                        VBox vbox = new VBox();
                        Scene scene = new Scene(vbox, 400, 600);
                        scene.getStylesheets().add("stylesheet.css");
                        stage.setScene(scene);
                        Label lbl = new Label("Modifier fournisseur");
                        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 20");
                        vbox.setSpacing(10);
                        // create fields for the other attributes
                        TextField villeField = new TextField(fournisseurFx.getVille());
                        villeField.setPromptText("Ville");
                        TextField codeapeField = new TextField(fournisseurFx.getCodeape());
                        codeapeField.setPromptText("Code APE");
                        TextField formejuridiqueField = new TextField(fournisseurFx.getFormejuridique());
                        formejuridiqueField.setPromptText("Forme juridique");
                        TextField numsirenField = new TextField(fournisseurFx.getNumsiren());
                        numsirenField.setPromptText("Numéro SIREN");
                        TextField telField = new TextField(fournisseurFx.getTel());
                        telField.setPromptText("Téléphone");

                        TextField nomFourni = new TextField(fournisseurFx.getNom());
                        Button btnOk = new Button("OK");
                        btnOk.getStyleClass().add("button6");
                        Button btnCancel = new Button("Cancel");
                        btnOk.setOnAction(event -> {
                            event.consume();
                            try {
//                                        ObservableList<FournisseurFx> fournisseurFxes = getTableView().getItems();
//                                        int position = fournisseurFxes.indexOf(fournisseurFx);
//                                        fournisseurFxes.remove(fournisseurFx);
                                fournisseurFx.modifierFournisseur(nomFourni.getText(), fournisseurFx.getEmail(),
                                        villeField.getText(), codeapeField.getText(), formejuridiqueField.getText(), numsirenField.getText(), telField.getText());
//                                       fournisseurFxes.add(position, fournisseurFx);
//                                        afficherFournisseurs.setItems(fournisseurFxes);
                                afficherFournisseurs.refresh();
                                Response getResponse2 = target
                                        .path("/fournisseur")
                                        .request(MediaType.APPLICATION_JSON_TYPE)
                                        .get();
                                List<String> listFournisseur = getResponse2.readEntity(List.class);
                                fournisseurs.setItems(FXCollections.observableArrayList(listFournisseur));
                                //Update afficherLivraison
                                stage.close();
                            } catch (Exception e) {
                                //System.out.println(e.getMessage());
                            }
                        });
                        btnCancel.setOnAction(event -> {
                            event.consume();
                            stage.close();
                        });

                        // add the fields to the dialog
                        GridPane grid = new GridPane();
                        grid.getStyleClass().add("grid");
                        grid.setHgap(20);
                        grid.setVgap(20);
                        grid.add(new Label("Nom:"), 0, 0);
                        grid.add(nomFourni, 1, 0);
                        grid.add(new Label("Ville:"), 0, 1);
                        grid.add(villeField, 1, 1);
                        grid.add(new Label("Code APE:"), 0, 2);
                        grid.add(codeapeField, 1, 2);
                        grid.add(new Label("Forme juridique:"), 0, 3);
                        grid.add(formejuridiqueField, 1, 3);
                        grid.add(new Label("Numéro SIREN:"), 0, 4);
                        grid.add(numsirenField, 1, 4);
                        grid.add(new Label("Téléphone:"), 0, 5);
                        grid.add(telField, 1, 5);
                        grid.add(btnOk, 0, 6);
                        grid.add(btnCancel, 1, 6);
                        grid.setStyle("-fx-padding: 10 40 10 40;-fx-background-radius: 10; -fx-background-color:  linear-gradient(to bottom right, #3f51b5, #2196f3); -fx-effect:  dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)");

                        vbox.getChildren().addAll(lbl, grid);
                        vbox.setAlignment(Pos.CENTER);
                        vbox.setStyle("-fx-background-color:  rgb(240,240,255)");
                        modifierbtn.setGraphic(new ImageView(new Image("modify.png", 20, 20, true, true)));
                        modifierbtn.setStyle("-fx-background-color: transparent");
                        modifierbtn.setOnAction(event -> {
                            event.consume();
                            stage.show();
                        });
                        setGraphic(modifierbtn);
                    }
                }
            });


            // Set the table data and cell value factories
            afficherFournisseurs.setItems(tableData);
            //       SupprimerModifierDMTableView.setItems(tableData);
            nomFCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            emailFCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            villeFCol.setCellValueFactory(new PropertyValueFactory<>("ville"));
            codeapeFCol.setCellValueFactory(new PropertyValueFactory<>("codeape"));
            formejuridiqueFCol.setCellValueFactory(new PropertyValueFactory<>("formejuridique"));
            numsirenFCol.setCellValueFactory(new PropertyValueFactory<>("numsiren"));
            telFCol.setCellValueFactory(new PropertyValueFactory<>("tel"));
//        typeDMColumnSM.setCellValueFactory(new PropertyValueFactory<>("typeDM"));
        }






        // ============================== rachid   ===========================

        Response getResponse = target
                .path("dm")
                .path("typedm")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        List<HashMap> listtypedm0 = getResponse.readEntity(List.class);


        // System.out.println(listtypedm);

        List<String> lststr = new ArrayList<>();

        listtypedm0.forEach(elt -> {

            HashMap elt0 = (HashMap) elt;

            lststr.add((String) elt0.get("Nom") + " | " + elt0.get("categorie"));
        });

        // System.out.println(lststr);

        //System.out.println(listtypedm.get(0).getClass());

        if (!lststr.isEmpty()) {
            typedmChoiceBox1.setItems(FXCollections.observableArrayList(lststr));
        }

        typedm.setVisible(false);
        ajoutertypedm.setVisible(false);
        typedmlabel.setVisible(false);
        txt_categorie.setVisible(false);
        créertypedm.setOnAction(event -> {

            if (!ajoutertypedm.isVisible()) {
                typedm.setVisible(true);
                ajoutertypedm.setVisible(true);
                typedmlabel.setVisible(true);
                txt_categorie.setVisible(true);
//                isShow=true;
            } else {
                typedm.setVisible(false);
                ajoutertypedm.setVisible(false);
                typedmlabel.setVisible(false);
                txt_categorie.setVisible(false);
//                isShow=false;

            }
        });






        //////////////////         <rachid>          ////////////////////


        // load DmAffecterController
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("Dm_Affecter.fxml"));
        AnchorPane pane = null;
        try {
            pane = (AnchorPane) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        anchorpane_affecter_dm.getChildren().clear();


        anchorpane_affecter_dm.getChildren().add(pane);

        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);

        image_upload.setImage(new Image("plus.png"));


        // ============================== rachid   ===========================




    }









    private VBox createEditorLiv(TableRowExpanderColumn.TableRowDataFeatures<LivraisonFx> param){
        VBox editor = new VBox();
        editor.setAlignment(Pos.CENTER);
        LivraisonFx livraisonFx = param.getValue();
        TableView tableView = new TableView<>(FXCollections.observableArrayList(livraisonFx.getDetailLivraisonFXES()));

        TableColumn typeDmCola = new TableColumn("Dm");
        TableColumn qteCola = new TableColumn("Qte");

        tableView.getColumns().addAll(typeDmCola, qteCola);

        typeDmCola.setCellValueFactory(new PropertyValueFactory<>("dm"));
        qteCola.setCellValueFactory(new PropertyValueFactory<>("qte"));


        ObservableList<DetailLivraisonFX> data = FXCollections.observableArrayList(livraisonFx.getDetailLivraisonFXES());
        tableView.setItems(data);
        tableView.getStylesheets().add("expandedTable.css");
        tableView.setFixedCellSize(30);
        tableView.prefHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(Bindings.size(tableView.getItems()).add(1.18)));
        tableView.minHeightProperty().bind(tableView.prefHeightProperty());
        tableView.maxHeightProperty().bind(tableView.prefHeightProperty());
        editor.getChildren().add(tableView);
        tableView.setStyle("-fx-background-color: white");
        return editor;
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



    // Youness



    @FXML
    ChoiceBox<String> dms = new ChoiceBox<>() ;

    @FXML
    Spinner<Integer> spinnerQteLivraison;


    @FXML
    ChoiceBox<String> fournisseurs = new ChoiceBox();

    @FXML
    Button ajouterDetailLivraison;

    @FXML
    ChoiceBox<String> typeDM2 = new ChoiceBox<>();
    @FXML
    Button btnAjouterFournisseur;
    @FXML
    TextField nomFournisseur;
    @FXML
    TextField numFournisseur;
    @FXML
    TextField emailFournisseur;
    @FXML
    TextField villeFournisseur;
    @FXML
    TextField codeapeFournisseur;
    @FXML
    TextField numsirenFournisseur;
    @FXML
    TextField formejuridiqueFournisseur;
    @FXML
    TableView<FournisseurFx> afficherFournisseurs;
    @FXML
    private TableColumn<FournisseurFx, String> nomFCol = new TableColumn<>("nom");
    @FXML
    private TableColumn<FournisseurFx, String> emailFCol = new TableColumn<>("email");
    @FXML
    private TableColumn<FournisseurFx, String> villeFCol = new TableColumn<>("ville");
    @FXML
    private TableColumn<FournisseurFx, String> codeapeFCol = new TableColumn<>("codeape");
    @FXML
    private TableColumn<FournisseurFx, String> formejuridiqueFCol = new TableColumn<>("formejuridique");
    @FXML
    private TableColumn<FournisseurFx, String> numsirenFCol = new TableColumn<>("num_siren");
    @FXML
    private TableColumn<FournisseurFx, String> telFCol = new TableColumn<>("tel");

    @FXML
    private TableColumn supprimercolumn = new TableColumn("supprimer");

    @FXML
    private TableColumn modifiercolumn = new TableColumn("modifier");

    @FXML
    Button afficher;





    @FXML
    private TableView<DetailLivraisonFX> tableDetailLivraison = new TableView<>();

    @FXML
    private TableColumn<DetailLivraisonFX, String> dmColumn = new TableColumn<>("DM");

    @FXML
    private TableColumn<DetailLivraisonFX, String> qteEColumn = new TableColumn<>("Quantité");

    @FXML
    Button supprimerFournisseur;




    @FXML
    private TableView<LivraisonFx> afficherLivraison = new TableView<>();
    @FXML
    private TableColumn<LivraisonFx,Integer> idColumn = new TableColumn<>();
    @FXML
    private TableColumn<LivraisonFx,String> fournisseurColumn = new TableColumn<>();
    @FXML
    private TableColumn<LivraisonFx,String> dateColumn = new TableColumn<>();

    //
    //
    @FXML
     private TableRowExpanderColumn expanderColumn = new TableRowExpanderColumn<>(this::createEditor);

//    private <S> Node createEditor(TableRowExpanderColumn.TableRowDataFeatures<S> sTableRowDataFeatures) {


    private ObservableList<DetailLivraisonFX> details = FXCollections.observableArrayList();


    public void onAjouterDetailLivraison(ActionEvent event) {
        String dm = dms.getValue();
        String qte = String.valueOf(spinnerQteLivraison.getValue());
        if (dm == null)
            return;
        for (DetailLivraisonFX detail : details){
            if (detail.getDm().equals(dm)){
                detail.setQte(String.valueOf(Integer.valueOf(qte) + Integer.valueOf(detail.getQte())));
                tableDetailLivraison.refresh();
                return;
            }
        }
        dmColumn = new TableColumn<>("DM");
        qteEColumn =  new TableColumn<>("Quantité");
        DetailLivraisonFX detailLivraison = new DetailLivraisonFX(dm, qte);
        details.add(detailLivraison);
        tableDetailLivraison.setItems(details);
        typeDM2.setValue(null);
        dms.setValue(null);
        spinnerQteLivraison.getValueFactory().setValue(1);
    }

    public void onAjouterLivraison(ActionEvent event) {
        if (details.size() == 0)
            return;
        String fournisseur = fournisseurs.getValue();
        if (fournisseur == null)
            return;
        List<String> jsonArray = new ArrayList<>();
        for (DetailLivraisonFX elt : details){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dm",elt.getDm());
            jsonObject.put("qte",elt.getQte());
            jsonArray.add(jsonObject.toString());
        }
        JSONObject lst = new JSONObject();
        JSONArray jsonArray1= new JSONArray(jsonArray);
        lst.put("detailsLst", jsonArray1.toString());
        lst.put("fournisseur", fournisseur);
        //System.out.println(jsonArray);
        //System.out.println(lst);
        // String fournisseurEncoded = URLEncoder.encode(fournisseur, StandardCharsets.UTF_8);
        Response postResponse = target.path("livraisonDM")
                .path("postLivraison")
                .queryParam("detailsFournisseur", "{lst}")
                .resolveTemplate("lst", lst.toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(""));
        if (postResponse.getStatus() == 200) {
            //System.out.println("Livraison created successfully!");


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Livraison Ajoutée!");
            alert.showAndWait();


            details.forEach(detail -> {
                Response pooostResponse = target
                        .path("fournisseur")
                        .path("update_qte")
                        .queryParam("nom_dm", detail.getDm())
                        .queryParam("qte_dm", detail.getQte())
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .put(Entity.json(""));
                //System.out.println("Response poooost =    " + pooostResponse.getStatus());
            });

            tableDetailLivraison.setItems(null);




            Response getResponse4 = target.path("/livraisonDM").path("livraisonlist")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();


            if (getResponse4.getStatus() == Response.Status.OK.getStatusCode()) {
                String responseString = getResponse4.readEntity(String.class);
                //System.out.println("Response String: " + responseString);
                JSONArray jsonArray2 = new JSONArray(responseString);
                ObservableList<LivraisonFx> tableData1 = FXCollections.observableArrayList();

                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject jsonObject = jsonArray2.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String nomFournisseur = jsonObject.getString("fournisseur");
                    String date = jsonObject.getString("date");

                    JSONArray detailsJson = jsonObject.getJSONArray("detailsLivraison");

                    LivraisonFx livraisonfx = new LivraisonFx(id,nomFournisseur,date);

                    List<DetailLivraisonFX> detailsLiv = new ArrayList<>();
                    detailsJson.forEach(detailLiv -> {
                        JSONObject jsonDetailLivraison = new JSONObject(detailLiv.toString());
                        //System.out.println(jsonDetailLivraison);
                        //System.out.println(detailLiv);
                        DetailLivraisonFX detailLivraisonFX = new DetailLivraisonFX(
                                jsonDetailLivraison.getJSONObject("dm").getString("nom"),
                                String.valueOf(jsonDetailLivraison.getInt("qte")));
                        detailsLiv.add(detailLivraisonFX);
                    });
                    tableData1.add(livraisonfx);
                    livraisonfx.setDetailLivraisonFXES(detailsLiv);
                }
                afficherLivraison.setItems(tableData1);
                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                fournisseurColumn.setCellValueFactory(new PropertyValueFactory<>("nomFournisseur"));
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            }

        } else {
            //System.out.println("Error adding livraison!");
        }
        details = FXCollections.observableArrayList();
    }

    public void ajouterFournisseur(ActionEvent event) {
        String nom = nomFournisseur.getText();
        String tel = numFournisseur.getText();
        String email =  emailFournisseur.getText();
        String ville = villeFournisseur.getText();
        String codeape = codeapeFournisseur.getText();
        String numerosiren = numsirenFournisseur.getText();
        String formeJuridique = formejuridiqueFournisseur.getText();

        if ((nom == null) || (tel == null) || (email == null) || (ville == null))
            return;

        Response postResponse =  target.path("/fournisseur")
                .path("/postFournisseur")

                .queryParam("fournisseur_nom", nom)
                .queryParam("fournisseur_ville", ville)
                .queryParam("fournisseur_email", email)
                .queryParam("fournisseur_codeape", codeape)
                .queryParam("fournisseur_formejuridique", formeJuridique)
                .queryParam("fournisseur_numsiren",numerosiren)
                .queryParam("fournisseur_tel", tel)
                .request(MediaType.APPLICATION_JSON_PATCH_JSON_TYPE)
                .method("POST");

        if (postResponse.getStatus() == 200) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully added!");
            alert.showAndWait();
            FournisseurFx fournisseurFx = new FournisseurFx(1,nom,email,ville,codeape,formeJuridique,numerosiren,tel);


            nomFournisseur.clear();
            numFournisseur.clear();
            emailFournisseur.clear();
            villeFournisseur.clear();
            codeapeFournisseur.clear();
            numsirenFournisseur.clear();
            formejuridiqueFournisseur.clear();

        }
        Response getResponse2 = target
                .path("/fournisseur")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        List<String> listFournisseur = getResponse2.readEntity(List.class);
        fournisseurs.setItems(FXCollections.observableArrayList(listFournisseur));
        //fournisseurChoicebox.setItems(FXCollections.observableArrayList(listFournisseur));

        Response getResponse = target.path("fournisseur").path("/listfournisseur")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (getResponse.getStatus() == Response.Status.OK.getStatusCode()) {
            String responseString = getResponse.readEntity(String.class);
            //System.out.println("Response String: " + responseString);
            JSONArray jsonArray = new JSONArray(responseString);
            ObservableList<FournisseurFx> tableData = FXCollections.observableArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String nom1 = jsonObject.getString("nom");
                String email1 = jsonObject.getString("email");
                String ville1 = jsonObject.getString("ville");
                String codeape1 = jsonObject.getString("codeape");
                String formejuridique1 = jsonObject.getString("formejuridique");
                String numsiren1 = jsonObject.getString("num_siren");
                String tel1 = jsonObject.getString("tel");

                FournisseurFx fournisseurFx = new FournisseurFx(id,nom1,email1,ville1,codeape1,formejuridique1,numsiren1,tel1);

                tableData.add(fournisseurFx);
            }

            // Set the table data and cell value factories
            afficherFournisseurs.setItems(tableData);
            //       SupprimerModifierDMTableView.setItems(tableData);
            nomFCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            emailFCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            villeFCol.setCellValueFactory(new PropertyValueFactory<>("ville"));
            codeapeFCol.setCellValueFactory(new PropertyValueFactory<>("codeape"));
            formejuridiqueFCol.setCellValueFactory(new PropertyValueFactory<>("formejuridique"));
            numsirenFCol.setCellValueFactory(new PropertyValueFactory<>("numsiren"));
            telFCol.setCellValueFactory(new PropertyValueFactory<>("tel"));
//        typeDMColumnSM.setCellValueFactory(new PropertyValueFactory<>("typeDM"));
        }
    }

    public void onAfficherFournisseurs(SortEvent<TableView> tableViewSortEvent) {
        Response getResponse = target.path("fournisseur").path("/listfournisseur")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (getResponse.getStatus() == Response.Status.OK.getStatusCode()) {
            String responseString = getResponse.readEntity(String.class);
            //System.out.println("Response String: " + responseString);
            JSONArray jsonArray = new JSONArray(responseString);
            ObservableList<FournisseurFx> tableData = FXCollections.observableArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String nom = jsonObject.getString("nom");
                String email = jsonObject.getString("email");
                String ville = jsonObject.getString("ville");
                String codeape = jsonObject.getString("codeape");
                String formejuridique = jsonObject.getString("formejuridique");
                String numsiren = jsonObject.getString("num_siren");
                String tel = jsonObject.getString("tel");
                FournisseurFx fournisseurFx = new FournisseurFx(id,nom,email,ville,codeape,formejuridique,numsiren,tel);
                tableData.add(fournisseurFx);
            }

            // Set the table data and cell value factories
            afficherFournisseurs.setItems(tableData);
            //       SupprimerModifierDMTableView.setItems(tableData);
            nomFCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            emailFCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            villeFCol.setCellValueFactory(new PropertyValueFactory<>("ville"));
            codeapeFCol.setCellValueFactory(new PropertyValueFactory<>("codeape"));
            formejuridiqueFCol.setCellValueFactory(new PropertyValueFactory<>("formejuridique"));
            numsirenFCol.setCellValueFactory(new PropertyValueFactory<>("numsiren"));
            telFCol.setCellValueFactory(new PropertyValueFactory<>("tel"));
//        typeDMColumnSM.setCellValueFactory(new PropertyValueFactory<>("typeDM"));
        }
    }

    public void OnBtnUploadClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        if (last_path.length() > 2) {

            fileChooser.setInitialDirectory(new File(last_path));
        }

        File selectedFile = fileChooser.showOpenDialog(btnUpload.getScene().getWindow());

        last_path = selectedFile.getParent();

        txt_filed_imagePath.setText(selectedFile.getAbsolutePath());

        image_upload.setImage(new Image(selectedFile.getAbsolutePath()));


    }

}
