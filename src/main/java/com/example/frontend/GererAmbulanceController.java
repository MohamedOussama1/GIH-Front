package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONObject;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class GererAmbulanceController implements Initializable {
    private static Client client = ClientBuilder.newClient()
            .register(JacksonFeature.class);

    private static WebTarget target = client.target(Connextion_info.url);
    private static Revision currentRevision;
    private static int currentIndex;
    @FXML
    ChoiceBox<String> chBoxTypeRevision;
    @FXML
    Button btnUpdate;
    @FXML
    TableView<Revision> tblRevisions;
    @FXML
    TableColumn startDateCol;
    @FXML
    TableColumn typeCol;

    @FXML
    TableColumn endDateCol;
    @FXML
    TableColumn ancienEtatCol;
    @FXML
    TableColumn nouvelEtatCol;
    @FXML
    TableColumn ancienKmCol;
    @FXML
    TableColumn nouvelKmCol;
    @FXML
    TableColumn descriptionCol;
    @FXML
    private TableColumn supprimercolumn = new TableColumn("supprimer");

    @FXML
    private TableColumn modifiercolumn = new TableColumn("modifier");
    @FXML
    DatePicker datePicker;
    @FXML
    HBox hBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chBoxTypeRevision.setItems(FXCollections.observableArrayList(
                target
                        .path("revision")
                        .path("type")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get()
                        .readEntity(List.class)
        ));
        ancienKmCol.setCellValueFactory(new PropertyValueFactory<>("ancienKm"));
        ancienEtatCol.setCellValueFactory(new PropertyValueFactory<>("ancienEtat"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        nouvelKmCol.setCellValueFactory(new PropertyValueFactory<>("nouvelKm"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        nouvelEtatCol.setCellValueFactory(new PropertyValueFactory<>("nouvelEtat"));


        List<String> revisionsAmbulance = target
                .path("revision")
                .queryParam("immatriculation", AmbulanceCard.selectedAmbulanceImmatriculation)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(List.class);
        populateRevisionTable(tblRevisions, revisionsAmbulance);
        boolean isInRevision = false;
        for (int i = 0; i < tblRevisions.getItems().size(); i++){
            Revision revision = tblRevisions.getItems().get(i);
            if (revision.getEndDate() == null){
                isInRevision = true;
                currentRevision = revision;
                currentIndex = i;
            }
        }

        if(!isInRevision)
            btnUpdate.setOnAction(this::onUpdateClick);
        else{
            btnUpdate.setOnAction(this::onEndRevisionClick);
            btnUpdate.setText("Stop");
            btnUpdate.setStyle("-fx-background-color: red");
           tblRevisions.getItems().remove(chBoxTypeRevision);
           tblRevisions.refresh();
        }


        supprimercolumn.setCellFactory(column -> new TableCell<Revision, String>() {
            private Button btnsupprimer = new Button();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Revision revision = getTableView().getItems().get(getIndex());
                    btnsupprimer.setGraphic(new ImageView(new Image("delete.jpg", 20, 20, true, true)));
                    btnsupprimer.setStyle("-fx-background-color: transparent");
                    btnsupprimer.setOnAction(event -> {
                        int id = revision.getId();
                        ObservableList<Revision> revisionItems = getTableView().getItems();
                        Response response = target
                                .path("revision")
                                .queryParam("revisionId", id)
                                .request(MediaType.APPLICATION_JSON_TYPE)
                                .method("DELETE");
                        if (response.getStatus() == 200)
                            System.out.println("Successfully deleted Revision");
                        revisionItems.remove(revision);
                        tblRevisions.setItems(revisionItems);
                    });
                    setGraphic(btnsupprimer);
                }
            }
        });


        modifiercolumn.setCellFactory(column -> new TableCell<Revision, String>() {
            private Button modifierbtn = new Button();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                //System.out.println(getIndex());
                if (empty) {
                    setGraphic(null);
                } else {
                    Revision revision = getTableView().getItems().get(getIndex());
                    Stage stage = new Stage();
                    VBox vbox = new VBox();
                    Scene scene = new Scene(vbox, 400, 600);
                    scene.getStylesheets().add("stylesheet.css");
                    stage.setScene(scene);
                    Label lbl = new Label("Modifier fournisseur");
                    lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 20");
                    vbox.setSpacing(10);
                    // create fields for the other attributes
                    TextField ancienKmField = new TextField(String.valueOf(revision.getAncienKm()));
                    ancienKmField.setPromptText("Ancien Km");
                    TextField nouvelKmField = new TextField(String.valueOf(revision.getNouvelKm()));
                    nouvelKmField.setPromptText("Nouvel Km");
                    ChoiceBox<String> chBoxTypeUpdate = new ChoiceBox(FXCollections.observableArrayList("S","CD","LD"));
                    chBoxTypeUpdate.setValue(revision.getType());
                    TextArea description = new TextArea(revision.getDescription());
                    description.setPromptText("Description");
                    DatePicker datePickerUpdateStart= new DatePicker(LocalDate.parse(revision.getStartDate()));
                    datePickerUpdateStart.setPromptText("Start Date");
                    DatePicker datePickerUpdateEnd= new DatePicker(LocalDate.parse(revision.getStartDate()));
                    datePickerUpdateEnd.setPromptText("End Date");
                    Button btnOk = new Button("OK");
                    btnOk.getStyleClass().add("button6");
                    Button btnCancel = new Button("Cancel");
                    btnOk.setOnAction(event -> {
                        try {
                            Response updateRevisionResponse = target
                                    .path("revision")
                                    .path("update")
                                    .path(String.valueOf(revision.getId()))
                                    .queryParam("ancienKm", ancienKmField.getText())
                                    .queryParam("nouvelKm", nouvelKmField.getText())
                                    .queryParam("dateDebut", datePickerUpdateStart.getValue())
                                    .queryParam("dateSortie", datePickerUpdateEnd.getValue())
                                    .queryParam("description", description.getText())
                                    .queryParam("typeRevision", chBoxTypeUpdate.getValue())
                                    .request(MediaType.APPLICATION_JSON_TYPE)
                                    .method("PUT");
                            System.out.println(updateRevisionResponse);
                            if(updateRevisionResponse.getStatus() == 200) {
                                System.out.println("Successfully updated Revision");
                                getTableView().getItems().remove(revision);
                                revision.setAncienKm(Integer.parseInt(ancienKmField.getText()));
                                revision.setNouvelKm(Integer.parseInt(nouvelKmField.getText()));
                                revision.setStartDate(datePickerUpdateStart.getValue().toString());
                                revision.setEndDate(datePickerUpdateEnd.getValue().toString());
                                revision.setDescription((description.getText()));
                                getTableView().getItems().add(getIndex(), revision);
                                getTableView().refresh();
                            }
                            //Update afficherLivraison
                            event.consume();
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
                    grid.add(new Label("Ancien Km:"), 0, 0);
                    grid.add(ancienKmField, 1, 0);
                    grid.add(new Label("Nouvel Km:"), 0, 1);
                    grid.add(nouvelKmField, 1, 1);
                    grid.add(new Label("Start Date:"), 0, 2);
                    grid.add(datePickerUpdateStart, 1, 2);
                    grid.add(new Label("End Date:"), 0, 3);
                    grid.add(datePickerUpdateEnd, 1, 3);
                    grid.add(new Label("Description:"), 0, 4);
                    grid.add(description, 1, 4);
                    grid.add(new Label("Type:"), 0, 5);
                    grid.add(chBoxTypeUpdate, 1, 5);
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
        tblRevisions.getColumns().add(modifiercolumn);
        tblRevisions.getColumns().add(supprimercolumn);
    }
    private void populateRevisionTable(TableView<Revision> tableRevisions, List<String> jsonRevisions){
        for (String  rev: jsonRevisions){
            JSONObject revision = new JSONObject(rev);
            tableRevisions.getItems().add(
                    new Revision(
                            revision.getInt("id"),
                            revision.getString("startDate"),
                            revision.getString("nomEtat"),
                            revision.getJSONObject("ambulance").getInt("km"),
                            revision.getString("typeRevision")
                    )
            );
        }
        tableRevisions.refresh();
    }
    @FXML
    private void onUpdateClick(ActionEvent event){
            // Send Request
            Response response = target
                    .path("revision")
                    .queryParam("immatriculation", AmbulanceCard.selectedAmbulanceImmatriculation)
                    .queryParam("dateDebut", datePicker.getValue())
                    .queryParam("typeRevision",  chBoxTypeRevision.getValue())
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.json("Hello"));
        List<String> revisionsAmbulance = target
                .path("revision")
                .queryParam("immatriculation", AmbulanceCard.selectedAmbulanceImmatriculation)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(List.class);
        populateRevisionTable(tblRevisions, revisionsAmbulance);
        chBoxTypeRevision.getSelectionModel().clearSelection();
    }
    @FXML
    private void onEndRevisionClick(ActionEvent event){
        // Send Request
//        Response response = target
//                .path("revision")
//                .queryParam("immatriculation", AmbulanceCard.selectedAmbulanceImmatriculation)
//                .queryParam("endDate", datePicker.getValue())
//                .request(MediaType.APPLICATION_JSON_TYPE)
//                .put(Entity.json("Hello"));
//        List<String> revisionsAmbulance = target
//                .path("revision")
//                .queryParam("immatriculation", AmbulanceCard.selectedAmbulanceImmatriculation)
//                .request(MediaType.APPLICATION_JSON_TYPE)
//                .get(List.class);
//        populateRevisionTable(tblRevisions, revisionsAmbulance);
//        chBoxTypeRevision.getSelectionModel().clearSelection();
        Revision revision = currentRevision;
        Stage stage = new Stage();
        VBox vbox = new VBox();
        Scene scene = new Scene(vbox, 400, 600);
        scene.getStylesheets().add("stylesheet.css");
        stage.setScene(scene);
        Label lbl = new Label("Modifier fournisseur");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 20");
        vbox.setSpacing(10);
        // create fields for the other attributes
        TextField nouvelKmField = new TextField(String.valueOf(revision.getNouvelKm()));
        nouvelKmField.setPromptText("Nouvel Km");
        TextArea description = new TextArea(revision.getDescription());
        description.setPromptText("Description");
        DatePicker datePickerUpdateEnd= new DatePicker(LocalDate.parse(revision.getStartDate()));
        datePickerUpdateEnd.setPromptText("End Date");
        Button btnOk = new Button("OK");
        btnOk.getStyleClass().add("button6");
        Button btnCancel = new Button("Cancel");
        btnOk.setOnAction(okEvent -> {
            System.out.println("here");
            try {
                System.out.println("out");
                System.out.println(currentRevision.getType());
                Response updateRevisionResponse = target
                        .path("revision")
                        .path("update")
                        .path(String.valueOf(revision.getId()))
                        .queryParam("ancienKm", currentRevision.getAncienKm())
                        .queryParam("nouvelKm", nouvelKmField.getText())
                        .queryParam("dateDebut", currentRevision.getStartDate())
                        .queryParam("dateSortie", datePickerUpdateEnd.getValue())
                        .queryParam("description", description.getText())
                        .queryParam("typeRevision", currentRevision.getType())
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .put(Entity.json("Hello"));
                System.out.println("yo");
                System.out.println(updateRevisionResponse);
                if(updateRevisionResponse.getStatus() == 200) {
                    System.out.println("Successfully updated Revision");
                    tblRevisions.getItems().remove(revision);
                    revision.setNouvelKm(Integer.parseInt(nouvelKmField.getText()));
                    revision.setEndDate(datePickerUpdateEnd.getValue().toString());
                    revision.setDescription((description.getText()));
                    revision.setNouvelEtat("F");
                    tblRevisions.getItems().add(currentIndex, revision);
                    tblRevisions.refresh();
                }
                //Update afficherLivraison
                okEvent.consume();
                stage.close();
            } catch (Exception e) {
                //System.out.println(e.getMessage());
            }
        });
        btnCancel.setOnAction(cancelEvent-> {
            cancelEvent.consume();
            stage.close();
        });

        // add the fields to the dialog
        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid");
        grid.setHgap(20);
        grid.setVgap(20);
        grid.add(new Label("Nouvel Km:"), 0, 0);
        grid.add(nouvelKmField, 1, 0);
        grid.add(new Label("End Date:"), 0, 1);
        grid.add(datePickerUpdateEnd, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(description, 1, 2);
        grid.add(btnOk, 0, 3);
        grid.add(btnCancel, 1, 3);
        grid.setStyle("-fx-padding: 10 40 10 40;-fx-background-radius: 10; -fx-background-color:  linear-gradient(to bottom right, #3f51b5, #2196f3); -fx-effect:  dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)");

        vbox.getChildren().addAll(lbl, grid);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color:  rgb(240,240,255)");
        stage.show();
    }
}
