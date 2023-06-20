package com.example.frontend;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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

public class DmController implements Initializable {

    private Client client = ClientBuilder.newClient().register(JacksonFeature.class);
    private WebTarget target = client.target(Connextion_info.url);


    // Traiter demandes
    @FXML
    TableView afficherDemandesTableView;
    @FXML
    TableColumn iddCol;
    @FXML
    TableColumn serviceCol;
    @FXML
    TableColumn dateDebutCol;
    @FXML
    TableColumn etatDemandeCol;
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
    ChoiceBox<String> chBoxService = new ChoiceBox<>();
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
    TableColumn serviceDmColumn;
    @FXML
    Button btnAddDemandeDm;
    @FXML
    ChoiceBox<String> typedmChoiceBox = new ChoiceBox();
    private ObservableList<DetailDemandeDm> tableData = FXCollections.observableArrayList();
    private List<DetailDemandeDm> demandesAEnvoyer = new ArrayList<>();





    // ============================== rachid   ===========================


    @FXML
    ChoiceBox<String> typedmChoiceBox1 = new ChoiceBox();
    @FXML
    TextField typedm;

    @FXML

    TextField txt_categorie;
    @FXML
    TextField nomdm;
    @FXML
    Button créertypedm;
    @FXML
    Button ajoutertypedm;
    @FXML
    Label typedmlabel;
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

    Boolean isShow = false;




    // ============================== rachid   ===========================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnUpload.setGraphic(new ImageView(new Image("upload-file.png")));

        tabPaneDemandes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
                if (oldTab == tabCréerDemandes) {
                    onAfficherDemandesDm();
                    onAfficherDemandesDm1();
                } else if (oldTab == tabTraiterDemandes) {
                    onAfficherDemandesDm1();
                }
            }
        });

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


        typeDM2.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            try {
                dms.setItems(getDMByType(newValue));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

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


            modifiercolumn.setCellFactory(column -> new TableCell<FournisseurFx, String>() {
                private Button modifierbtn = new Button();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //System.out.println(getIndex());
                    if (empty) {
                        setGraphic(null);
                    } else {
                        FournisseurFx fournisseurFx = getTableView().getItems().get(getIndex());
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
        try {
            this.onAfficherDMs(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

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


        actionsColSM.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    //System.out.println("rachid edit button");

                    DM dm = getTableView().getItems().get(getIndex());
//                    DM dm = SupprimerModifierDMTableView.getSelectionModel().getSelectedItem();
                    if (dm != null) {

                        TextInputDialog dialog = new TextInputDialog(dm.getNom());
                        dialog.setTitle("Edit DM Name");
                        dialog.setHeaderText("Enter new name for DM");
                        dialog.setContentText("New Name:");

                        Optional<String> result = dialog.showAndWait();
                        if (result.isPresent()) {
                            String newNomDM = result.get();
                            onModifierDM(dm.getNom(), newNomDM);
                        }
                    }
                    try {
                        onAfficherDMs(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                deleteButton.setOnAction(event -> {
                    DM dm  = getTableView().getItems().get(getIndex());
//                    DM dm = SupprimerModifierDMTableView.getSelectionModel().getSelectedItem();
                    if (dm != null) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete DM");
                        alert.setHeaderText("Are you sure you want to delete " + dm.getNom() + "?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            SupprimerDM(dm.getNom());
                        }
                    }
                    try {
                        onAfficherDMs(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
            }

            @Override
            protected void updateItem(Void aVoid, boolean b) {
                super.updateItem(aVoid, b);

                if (!b) {
                    setGraphic(new HBox(editButton, deleteButton));
                } else {
                    setGraphic(null);
                }
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
    @FXML
    public void onAjouterDetailDemandeDm(ActionEvent event) throws IOException {
        String typeDm = chBoxTypeDm.getValue();
        String nomDm = chBoxNomDm.getValue();
        int qte = spinnerQuantityDm.getValue();
        if ((nomDm == null))
            return;
        DetailDemandeDm detailDemandeDm = new DetailDemandeDm(qte, typeDm, nomDm);
        for (DetailDemandeDm detail : tableData){
            if (detail.getNomDm().equals(nomDm)){
                detail.setQte(detail.getQte() + qte);
                tblViewAddDetailDemandeDm.refresh();
                return;
            }
        }
        tableData.add(detailDemandeDm);
        tblViewAddDetailDemandeDm.setItems(tableData);
        qteDmColumn.setCellValueFactory(new PropertyValueFactory<>("qte"));
        typeDmColumn.setCellValueFactory(new PropertyValueFactory<>("typeDm"));
        nomDmColumn.setCellValueFactory(new PropertyValueFactory<>("nomDm"));
        tblViewAddDetailDemandeDm.refresh();
        demandesAEnvoyer.add(detailDemandeDm);
        chBoxNomDm.getSelectionModel().clearSelection();
        chBoxTypeDm.getSelectionModel().clearSelection();
        spinnerQuantityDm.getValueFactory().setValue(1);
    }

    @FXML
    public void onConfirmDemandeDm(ActionEvent event) throws IOException {
        if (tableData.size() == 0)
            return;
        String service = chBoxService.getValue();
        if (service == null)
            return;
        Response postDemandeResponse = target
                .path("demandeDm")
                .queryParam("nomDepartement", service)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("Hello"));
        int idDemandeDm = postDemandeResponse.readEntity(Integer.class);
        if (postDemandeResponse.getStatus() == 200 ||postDemandeResponse.getStatus() == 201) {
            for (DetailDemandeDm detailDemandeDm : tableData) {
                try {
                    String nomDM = detailDemandeDm.getNomDm();
                    int qte = detailDemandeDm.getQte();
                    Response response = target
                            .path("demandeDm")
                            .path("detailDemande")
                            .queryParam("idDemandeDm", idDemandeDm)
                            .queryParam("nomDm", nomDM)
                            .queryParam("qte", qte)
                            .request(MediaType.APPLICATION_JSON_TYPE)
                            .post(Entity.json("Hello"));
                    detailDemandeDm.setDepartement(service);
                    response.close();
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
            //System.out.println(cofirmDemandeResponse.getStatus());
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
    protected void onAfficherDemandesDm1() {
        Response getResponse = target
                .path("demandeDm")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (getResponse.getStatus() == 200) {
            List<String> responseString = getResponse.readEntity(List.class);
            //System.out.println("Response String: " + responseString);
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
                    //System.out.println(jsonDetailDemande);
                    //System.out.println(detailDemande);
                    DetailDemandeDm detailDemandeDm = new DetailDemandeDm(
                            jsonDetailDemande.getString("etatDetail"),
                            jsonDetailDemande.getInt("id"),
                            jsonDetailDemande.getInt("qte"),
                            jsonDetailDemande.getJSONObject("dm").getJSONObject("typeDM").getString("nomType"),
                            jsonDetailDemande.getJSONObject("dm").getString("nom"),
                            jsonDetailDemande.getJSONObject("dm").getInt("id"));
                    detailsDemandeDm.add(detailDemandeDm);
                    //System.out.println(detailDemandeDm.getEtat());
                });
                demandeDm.setDetailsDemandeDm(detailsDemandeDm);
                boolean done = true;
                boolean accepted = true;
                boolean rejected = true;
                for (DetailDemandeDm detailDemandeDm : detailsDemandeDm) {
                    if (detailDemandeDm.getEtat().equals("CONFIRMÉE")) {
                        done = false;
                    } else if (detailDemandeDm.getEtat().equals("ACCEPTÉE")) {
                        rejected = false;
                    } else if (detailDemandeDm.getEtat().equals("REJETÉE")) {
                        accepted = false;
                    }
                }
                if (done) {
                    String newEtat = accepted ? "ACCEPTÉE" : rejected ? "REJETÉE" : "TRAITÉE";
                    Response response = target
                            .path("demandeDm")
                            .path(String.valueOf(demandeDm.getId()))
                            .path(newEtat)
                            .request(MediaType.APPLICATION_JSON_TYPE)
                            .put(Entity.json("Hello"));
                    demandeDm.setEtatDemande(newEtat);
                    //System.out.println(response);
                }
                tableData.add(demandeDm);
            }
            TableRowExpanderColumn<DemandeDm> expanderColumn = new TableRowExpanderColumn<>(this::createEditor1);
            // Set the table data and cell value factories
            afficherDemandesTableView1.setItems(tableData);
            if (!afficherDemandesTableView1.getColumns().get(0).getClass().equals(expanderColumn.getClass()))
                afficherDemandesTableView1.getColumns().add(0, expanderColumn);
            idCol1.setCellValueFactory(new PropertyValueFactory<>("id"));
            etatDemandeCol1.setCellValueFactory(new PropertyValueFactory<>("etatDemande"));
            dateDebutCol1.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
            serviceCol1.setCellValueFactory(new PropertyValueFactory<>("departement"));
        }
    }

    private VBox createEditor1(TableRowExpanderColumn.TableRowDataFeatures<DemandeDm> param) {
        VBox editor = new VBox();
        editor.setAlignment(Pos.CENTER);
        DemandeDm demandeDm = param.getValue();
        TableView tableView = new TableView<>(FXCollections.observableArrayList(demandeDm.getDetailsDemandeDm()));

        TableColumn typeDmCola = new TableColumn("Type");
        TableColumn nomDmCola = new TableColumn("Nom");
        TableColumn qteCola = new TableColumn("Quantité");
        TableColumn etatCola = new TableColumn("État");
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

        editor.getChildren().add(tableView);

        return editor;
    }
    protected void onAfficherDemandesDm(){
        Response getResponse = target
                .path("demandeDm")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (getResponse.getStatus() == 200) {
            List<String> responseString = getResponse.readEntity(List.class);
            //System.out.println("Response String: " + responseString);
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
                    //System.out.println(jsonDetailDemande);
                    //System.out.println(detailDemande);
                    DetailDemandeDm detailDemandeDm = new DetailDemandeDm(
                            jsonDetailDemande.getString("etatDetail"),
                            jsonDetailDemande.getInt("id"),
                            jsonDetailDemande.getInt("qte"),
                            jsonDetailDemande.getJSONObject("dm").getJSONObject("typeDM").getString("nomType"),
                            jsonDetailDemande.getJSONObject("dm").getString("nom"),
                            jsonDetailDemande.getJSONObject("dm").getInt("id"));
                    detailDemandeDm.setDepartement(demandeDm.getDepartement());
                    detailsDemandeDm.add(detailDemandeDm);
                    //System.out.println(detailDemandeDm.getEtat());
                });
                demandeDm.setDetailsDemandeDm(detailsDemandeDm);
                boolean done = true;
                boolean accepted = true;
                boolean rejected = true;
                for (DetailDemandeDm detailDemandeDm : detailsDemandeDm) {
                    if (detailDemandeDm.getEtat().equals("CONFIRMÉE")) {
                        done = false;
                    } else if (detailDemandeDm.getEtat().equals("ACCEPTÉE")) {
                        rejected = false;
                    } else if (detailDemandeDm.getEtat().equals("REJETÉE")) {
                        accepted = false;
                    }
                }
                if (done) {
                    String newEtat = accepted ? "ACCEPTÉE" : rejected ? "REJETÉE" : "TRAITÉE";
                    Response response = target
                            .path("demandeDm")
                            .path(String.valueOf(demandeDm.getId()))
                            .path(newEtat)
                             .request(MediaType.APPLICATION_JSON_TYPE)
                            .put(Entity.json("Hello"));
                    demandeDm.setEtatDemande(newEtat);
                    //System.out.println(response);
                }
                tableData.add(demandeDm);
            }


            TableRowExpanderColumn<DemandeDm> expanderColumn = new TableRowExpanderColumn<>(this::createEditor);

            // Set the table data and cell value factories
            afficherDemandesTableView.setItems(tableData);
            if (!afficherDemandesTableView.getColumns().get(0).getClass().equals(expanderColumn.getClass()))
                afficherDemandesTableView.getColumns().add(0, expanderColumn);
            iddCol.setCellValueFactory(new PropertyValueFactory<>("id"));
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
                    //System.out.println("Im here");
                    DetailDemandeDm detailDemandeDm = getTableView().getItems().get(getIndex());
                    btnAccepter.setGraphic(new ImageView(new Image("plus.png", 20,20, true, true)));
                    btnAccepter.setStyle("-fx-background-color: transparent");
                    btnAccepter.setOnAction(event -> {
                        //System.out.println("hello");
//                         Déplacer du stock général vers stock service spécifié
                        Response putResponse = target
                                .path("dm")
                                .path("stock")
                                .queryParam("nomDepartement", detailDemandeDm.getDepartement())
                                .queryParam("nomDm", detailDemandeDm.getNomDm())
                                .queryParam("qte", detailDemandeDm.getQte())
                                .request(MediaType.APPLICATION_JSON_TYPE)
                                .put(Entity.json("Hello"));
                        int isSuccess = putResponse.readEntity(Integer.class);
                        if (isSuccess == 1){
                            detailDemandeDm.setEtat("ACCEPTÉE");
                            Response response = target
                                    .path("demandeDm")
                                    .path("detailDemande")
                                    .queryParam("idDetail", detailDemandeDm.getId())
                                    .queryParam("etatDetail", "acceptée")
                                    .request(MediaType.APPLICATION_JSON_TYPE)
                                    .put(Entity.json("Hello"));
                            response.close();
                            getTableView().refresh();
                            boolean doRefresh = true;
                            for (DetailDemandeDm detail : getTableView().getItems()){
                                if ((!detail.getEtat().equals("ACCEPTÉE")) && (!detail.getEtat().equals("REJETÉE")))
                                    doRefresh = false;
                            }
                            if (doRefresh) {
                                onAfficherDemandesDm();
                            }
                        }else{
                            Dialog<String> dialog = new Dialog<>();
                            dialog.setTitle("Opération échouée");
                            dialog.setHeaderText("La quantité dans le stock général est insuffisante pour exécuter cette opération, veuillez passer des livraison pour remplire ce dernier");
                            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                            dialog.showAndWait();
                        }
                        putResponse.close();

                    });
                    setGraphic(btnAccepter);
                }
            };});
        rejeterCola.setCellFactory(column -> new TableCell<DetailDemandeDm, String>() {
            private Button btnRejeter = new Button();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                //System.out.println(getIndex());
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
                        //System.out.println(detailDemandeDm.getEtat());
                        getTableView().refresh();
                        boolean doRefresh = true;
                        for (DetailDemandeDm detail : getTableView().getItems()){
                            if ((!detail.getEtat().equals("ACCEPTÉE")) && (!detail.getEtat().equals("REJETÉE")))
                                doRefresh = false;
                        }
                        if (doRefresh) {
                                onAfficherDemandesDm();
                        }
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
    // private TableRowExpanderColumn expanderColumn = new TableRowExpanderColumn<>(this::createEditor);

//    private <S> Node createEditor(TableRowExpanderColumn.TableRowDataFeatures<S> sTableRowDataFeatures) {


    private ObservableList<DetailLivraisonFX> details = FXCollections.observableArrayList();







    @FXML
    protected void onAjouterLivraison(ActionEvent event) throws IOException {
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
    protected ObservableList<String> getDMByType(String type) throws IOException{
        Response getResponse = target
                .path("dm")
                .path("dmbyType")
                .queryParam("typeDM", type)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        List<String> listdmbytype = getResponse.readEntity(List.class);
        return FXCollections.observableArrayList(listdmbytype);


    }
//    List<DetailLivraisonFX> detailliv = new ArrayList<>();
    @FXML
    protected void onAjouterDetailLivraison(ActionEvent event) throws IOException {
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
    @FXML
    protected void ajouterFournisseur(ActionEvent event) throws IOException {
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

    @FXML
    protected void onAfficherFournisseurs(ActionEvent event) throws IOException {
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
    public class DetailLivraisonFX {
        private String dm;
        private String qte;

        public DetailLivraisonFX(String dm, String qte) {
            this.dm = dm;
            this.qte = qte;
        }



        public String getDm() {
            return dm;
        }

        public String getQte() {
            return qte;
        }
      public void setQte(String qte){
            this.qte = qte;
      }

    }

    public class LivraisonFx{
        private int id;
        private String nomFournisseur;
        private String date;

        private List<DetailLivraisonFX> detailLivraisonFXES;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNomFournisseur() {
            return nomFournisseur;
        }

        public void setNomFournisseur(String nomFournisseur) {
            this.nomFournisseur = nomFournisseur;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<DetailLivraisonFX> getDetailLivraisonFXES() {
            return detailLivraisonFXES;
        }

        public void setDetailLivraisonFXES(List<DetailLivraisonFX> detailLivraisonFXES) {
            this.detailLivraisonFXES = detailLivraisonFXES;
        }

        public LivraisonFx(int id, String nomFournisseur, String date) {
            this.id = id;
            this.nomFournisseur = nomFournisseur;
            this.date = date;
        }
    }

    public class FournisseurFx {
        private  int id;
        private  String nom;
        private  String email;
        private  String ville;
        private  String codeape;
        private  String formejuridique;
        private  String numsiren;
        private  String tel;

        public FournisseurFx(int id,String nom,String email, String ville,String codeape,String formejuridique,String numsiren, String tel){
            this.id = id;
            this.nom=nom;
            this.email=email;
            this.ville=ville;
            this.codeape=codeape;
            this.formejuridique=formejuridique;
            this.numsiren=numsiren;
            this.tel=tel;
        }
        public int getId(){
            return id;
        }

        public String getNom() {
            return nom;
        }

        public String getEmail() {
            return email;
        }

        public String getVille() {
            return ville;
        }

        public String getCodeape() {
            return codeape;
        }

        public String getFormejuridique() {
            return formejuridique;
        }

        public String getNumsiren() {
            return numsiren;
        }

        public String getTel() {
            return tel;
        }

        public void modifierFournisseur(String nom, String email, String ville, String codeape, String formejuridique, String numsiren, String tel) {
            // update the information of the Fournisseur
            this.nom = nom;
            this.email = email;
            this.ville = ville;
            this.codeape = codeape;
            this.formejuridique = formejuridique;
            this.numsiren = numsiren;
            this.tel = tel;

            // save the updated Fournisseur to the database
            Response response = target.path("/fournisseur")
                    .path("/updateFournisseur")
                    .queryParam("fournisseur_id", this.id)
                    .queryParam("fournisseur_nom", this.nom)
                    .queryParam("fournisseur_email", this.email)
                    .queryParam("fournisseur_ville", this.ville)
                    .queryParam("fournisseur_codeape", this.codeape)
                    .queryParam("fournisseur_formejuridique", this.formejuridique)
                    .queryParam("fournisseur_numsiren", this.numsiren)
                    .queryParam("fournisseur_tel", this.tel)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .put(Entity.json(""));
            //System.out.println(response);
        }}




// rachid =======================   rachid ===========================


    @FXML
    protected void onAjouterTypeDM(ActionEvent event) throws IOException {
        String nomtypedm = typedm.getText().trim();
        String categorie = txt_categorie.getText().trim();
        Response postResponse = target.path("dm")
                .path("posttypedm")
                .queryParam("nomtypedm", nomtypedm)
                .queryParam("categorie", categorie)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(""));
        if (postResponse.getStatus() == Response.Status.OK.getStatusCode()) {
            //System.out.println("Type DM created successfully!");
            Response getResponse = target.path("dm")
                    .path("typedm")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            List<String> listtypedm = getResponse.readEntity(List.class);
            typedmChoiceBox1.setItems(FXCollections.observableArrayList(listtypedm));
        } else {
            //System.out.println("Error adding type DM!");
        }
    }

    public void upload_image(String image_path,String name){


        File file = new File(image_path);

        FormDataMultiPart formData = new FormDataMultiPart();

        formData.bodyPart(new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE));

        Response response = target
                .path("dm")
                .path("uploadfile")
                .queryParam("name",name)
                .request(MediaType.APPLICATION_JSON_TYPE)

                .post(Entity.entity(formData, formData.getMediaType()));

        //System.out.println(response.getStatus());

    }


    @FXML
    protected void onAjouterDM(ActionEvent event) throws IOException {

//        scroll_table.setFitToHeight(true);
//        scroll_table.setFitToWidth(true);

        try {
            this.onAfficherDMs(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String typeDM = typedmChoiceBox1.getValue().trim();
        String dm = nomdm1.getText().trim();

        //int qte = Integer.parseInt(qtedm1.getText().trim());

        String image_path = txt_filed_imagePath.getText();

        File file=new File(image_path);

        Response postResponse = target.path("dm")
                .path("postdm")
                .queryParam("nom", dm)
                .queryParam("qte", 0)
                .queryParam("typedm", typeDM)
                .queryParam("image_path", "http://141.94.205.104:8081/dm/images?image="+file.getName())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(""));

        //System.out.println(postResponse.getStatus());
        if (postResponse.getStatus() == 201) {

            this.upload_image(image_path,file.getName());

           SupprimerModifierDMTableView.refresh();


            //System.out.println("Type DM created successfully!");

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Success");
            dialog.setHeaderText("DM added successfully!");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
            dialog.showAndWait();

            try {
                this.onAfficherDMs(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //System.out.println("Error adding type DM!");

            // Create a dialog box to display an error message
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Error");
            dialog.setHeaderText("Error adding DM!");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
            dialog.showAndWait();
        }

        txt_filed_imagePath.clear();
        image_upload.setImage(null);
        typedmChoiceBox1.getSelectionModel().clearSelection();
        nomdm1.clear();
    }



    ///////    rachid ///////

    @FXML
    void OnBtnUploadClick(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        if (last_path.length() > 2) {

            fileChooser.setInitialDirectory(new File(last_path));
        }

        File selectedFile = fileChooser.showOpenDialog(btnUpload.getScene().getWindow());

        last_path = selectedFile.getParent();

        txt_filed_imagePath.setText(selectedFile.getAbsolutePath());

        image_upload.setImage(new Image(selectedFile.getAbsolutePath()));


    }


    //aya 2

    protected void onModifierDM(String oldNomDM, String newNomDM) {
        Entity<?> entity = Entity.entity("", MediaType.APPLICATION_JSON_TYPE);
        Response response = target.path("dm")
                .path("/updatenomdm")
                .queryParam("oldnamedm", oldNomDM)
                .queryParam("newnamedm", newNomDM)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(entity);

    }


    protected void SupprimerDM(String nomDM) {
        Response response = target.path("dm")
                .path("/deletenomdm")
                .queryParam("nomdm", nomDM)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        response.close();
        SupprimerModifierDMTableView.refresh();
    }





    @FXML
    protected void onAfficherDMs(ActionEvent event) throws IOException {


        idColSM.setCellValueFactory(new PropertyValueFactory<>("id"));
        qteColumnSM.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        nomDMColSM.setCellValueFactory(new PropertyValueFactory<>("nom"));
        typeDMColumnSM.setCellValueFactory(new PropertyValueFactory<>("typeDM"));


        Response getResponse = target.path("dm")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (getResponse.getStatus() == Response.Status.OK.getStatusCode()) {
            String responseString = getResponse.readEntity(String.class);
            //System.out.println("Response String: " + responseString);
            JSONArray jsonArray = new JSONArray(responseString);
            ObservableList<DM> tableData = FXCollections.observableArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String nom = jsonObject.getString("nom");
                int quantite = jsonObject.getInt("quantite");
                String typeDM = jsonObject.getString("typeDM");

                DM dm = new DM();
                dm.setId(id);
                dm.setNom(nom);
                dm.setQuantite(quantite);
                dm.setTypeDM(typeDM);

                tableData.add(dm);
            }


            SupprimerModifierDMTableView.setItems(tableData);

        }
    }



}
