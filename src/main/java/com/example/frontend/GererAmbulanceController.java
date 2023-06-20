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
import javafx.geometry.Pos;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONObject;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

public class GererAmbulanceController implements Initializable {
    private static Client client = ClientBuilder.newClient()
            .register(JacksonFeature.class);

    private static WebTarget target = client.target(Connextion_info.url);
    private static Revision currentRevision;
    private static boolean isInRevision = false;
    @FXML
    Button btnPredict;
    @FXML
    ChoiceBox<String> chBoxTypeRevision;
    @FXML
    Button btnUpdate;
    @FXML
    TableView<Revision> tblRevisions = new TableView<>();
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
    ObservableList<Revision> revisionItems = tblRevisions.getItems();
    @FXML
    DatePicker datePicker;
    @FXML
    HBox hBox;
    @FXML
    HBox hBoxPredict;
    LocalDate currentRevisionStartDate;


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
        updateTblRevisions();
        if (!isInRevision) {
            btnUpdate.setOnAction(this::onUpdateClick);
            btnUpdate.setStyle("-fx-background-color: green");
            hBox.getChildren().clear();
            datePicker.setDayCellFactory(this::disablePastDays);
            hBox.getChildren().addAll(chBoxTypeRevision, datePicker, btnUpdate);
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
                        System.out.println(id);
                        Response response = target
                                .path("revision")
                                .queryParam("revisionId", id)
                                .request(MediaType.APPLICATION_JSON_TYPE)
                                .method("DELETE");
                        response.close();
                        if (response.getStatus() == 200){
                            System.out.println("Successfully deleted Revision");
                            ObservableList<Revision> revisions = getTableView().getItems();
                            revisions.remove(revision);
                            tblRevisions.setItems(revisions);
                            if (revision == currentRevision){
                                btnUpdate.setOnAction(event1-> onUpdateClick(event1));
                                btnUpdate.setStyle("-fx-background-color: green");
                                btnUpdate.setText("Update");
                                datePicker.setDayCellFactory(new GererAmbulanceController()::disablePastDays);
                                hBox.getChildren().clear();
                                hBox.getChildren().addAll(chBoxTypeRevision, datePicker, btnUpdate);
                                hBoxPredict.getChildren().clear();
                                hBoxPredict.getChildren().add(btnPredict);
                            }
                        }
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
                if (empty) {
                    setGraphic(null);
                } else {
                    Revision revision = getTableView().getItems().get(getIndex());
                    Stage stage = new Stage();
                    VBox vbox = new VBox();
                    Scene scene = new Scene(vbox, 800, 600);
                    scene.getStylesheets().add("stylesheet.css");
                    stage.setScene(scene);
                    Label lbl = new Label("Modifier Revision");
                    lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 15");
                    vbox.setSpacing(10);
                    // create fields for the other attributes
                    TextField ancienKmField = new TextField(String.valueOf(revision.getAncienKm()));
                    ancienKmField.setPromptText("Ancien Km");
                    TextField nouvelKmField = new TextField(String.valueOf(revision.getNouvelKm()));
                    if (revision.getNouvelKm() == 0)
                        nouvelKmField.setText(ancienKmField.getText());
                    nouvelKmField.setPromptText("Nouvel Km");
                    LitController.formatTextFieldToNumbersOnly(ancienKmField, Integer.parseInt(nouvelKmField.getText()));
                    ChoiceBox<String> chBoxTypeUpdate = new ChoiceBox(FXCollections.observableArrayList("S","CD","LD"));
                    chBoxTypeUpdate.setValue(revision.getType());
                    TextArea description = new TextArea(revision.getDescription());
                    description.setPromptText("Description");
                    DatePicker datePickerUpdateStart= new DatePicker(LocalDate.parse(revision.getStartDate()));
                    datePickerUpdateStart.setPromptText("Start Date");
                    DatePicker datePickerUpdateEnd= new DatePicker(LocalDate.parse(revision.getStartDate()));
                    datePickerUpdateEnd.setPromptText("End Date");
                    datePickerUpdateStart.setDayCellFactory(picker -> new DateCell() {
                        @Override
                        public void updateItem(LocalDate date, boolean empty) {
                            super.updateItem(date, empty);
                            if(date.isAfter(datePickerUpdateEnd.getValue())){
                                setDisable(true);
                                setStyle("-fx-background-color: red;");
                            }
                        }});
                    datePickerUpdateEnd.setDayCellFactory(picker -> new DateCell() {
                        @Override
                        public void updateItem(LocalDate date, boolean empty) {
                            super.updateItem(date, empty);
                            if(date.isBefore(datePickerUpdateStart.getValue())){
                                setDisable(true);
                                setStyle("-fx-background-color: red;");
                            }

                        }});
                    Button btnOk = new Button("OK");
                    btnOk.getStyleClass().add("button6");
                    Button btnCancel = new Button("Cancel");
                    btnOk.setOnAction(event -> {
                        if (Integer.parseInt(nouvelKmField.getText()) < Integer.parseInt(ancienKmField.getText()) )
                            nouvelKmField.setText(ancienKmField.getText());
                        try {
                            Response updateRevisionResponse = target
                                    .path("revision")
                                    .path("update")
                                    .path(String.valueOf(revision.getId()))
                                    .queryParam("ancienKm", revision.getAncienKm())
                                    .queryParam("nouvelKm", nouvelKmField.getText())
                                    .queryParam("dateDebut", revision.getStartDate())
                                    .queryParam("dateSortie", datePickerUpdateEnd.getValue())
                                    .queryParam("description", description.getText())
                                    .queryParam("typeRevision", revision.getType())
                                    .request(MediaType.APPLICATION_JSON_TYPE)
                                    .put(Entity.json("Hello"));
                            updateRevisionResponse.close();
                            if(updateRevisionResponse.getStatus() == 200) {
                                System.out.println("Successfully updated Revision");
                                revision.setAncienKm(Integer.parseInt(ancienKmField.getText()));
                                revision.setNouvelKm(Integer.parseInt(nouvelKmField.getText()));
                                revision.setStartDate(datePickerUpdateStart.getValue().toString());
                                revision.setEndDate(datePickerUpdateEnd.getValue().toString());
                                revision.setDescription((description.getText()));
                                getTableView().refresh();
                                updateTblRevisions();
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
                    if (revision.getEndDate() == null){
                        grid.add(new Label("Ancien Km:"), 0, 0);
                        grid.add(ancienKmField, 1, 0);
                        grid.add(new Label("Start Date:"), 0, 1);
                        grid.add(datePickerUpdateStart, 1, 1);
                        grid.add(new Label("Type:"), 0, 2);
                        grid.add(chBoxTypeUpdate, 1, 2);
                        grid.add(btnOk, 0, 3);
                        grid.add(btnCancel, 1, 3);
                    }else {
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
                    }
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
   public void updateTblRevisions() {
       for (Revision revision : tblRevisions.getItems()) {
           LocalDate startDate = LocalDate.parse(revision.getStartDate());
           LocalDate now = LocalDate.now();
           if (revision.getEndDate() == null) {
               String lblStatusRevision;
               currentRevision = revision;
               isInRevision = true;
               if (startDate.isBefore(now) || startDate.isEqual(now)) {
                   lblStatusRevision = currentRevision.getType().equals("S")
                           ?
                           "Revision Simple en cours"
                           : currentRevision.getType().equals("CD")
                           ?
                           "Revision Courte Durée en cours"
                           : "Revision Longue Durée en cours";
                   btnUpdate.setOnAction(this::onEndRevisionClick);
                   btnUpdate.setText("Stop");
                   btnUpdate.setStyle("-fx-background-color: red");
                   datePicker.setDayCellFactory(this::disableSpecificDates);
                   hBox.getChildren().clear();
                   Label label = new Label(lblStatusRevision);
                   label.setStyle("-fx-font-weight: bold; -fx-font-size: 15; -fx-font-family: 'Arial Black'");
                   hBox.getChildren().addAll(label, btnUpdate);
               } else {
                   String dateDebut = startDate.toString();
                   lblStatusRevision = currentRevision.getType().equals("S")
                           ?
                           "Revision Simple débute le " + dateDebut
                           : currentRevision.getType().equals("CD")
                           ?
                           "Revision Courte débute le " + dateDebut
                           : "Revision Longue Durée débute le " + dateDebut;
                   hBox.getChildren().clear();
                   Label label = new Label(lblStatusRevision);
                   label.setStyle("-fx-font-weight: bold; -fx-font-size: 15; -fx-font-family: 'Arial Black'");
                   hBox.getChildren().add(label);
               }
           } else {
               LocalDate endDate = LocalDate.parse(revision.getEndDate());
               if ((endDate.isAfter(now)) || (endDate.isEqual(now))) {
                   String lblStatusRevision;
                   isInRevision = true;
                   currentRevision = revision;
                   if (startDate.isBefore(now) || startDate.isEqual(now)) {
                       lblStatusRevision = currentRevision.getType().equals("S")
                               ?
                               "Revision Simple en cours"
                               : currentRevision.getType().equals("CD")
                               ?
                               "Revision Courte Durée en cours"
                               : "Revision Longue Durée en cours";
                   } else {
                       String dateDebut = startDate.toString();
                       lblStatusRevision = currentRevision.getType().equals("S")
                               ?
                               "Revision Simple débute le " + dateDebut
                               : currentRevision.getType().equals("CD")
                               ?
                               "Revision Courte débute le " + dateDebut
                               : "Revision Longue Durée débute le " + dateDebut;
                   }
                   hBox.getChildren().clear();
                   Label label = new Label(lblStatusRevision);
                   label.setStyle("-fx-font-weight: bold; -fx-font-size: 15; -fx-font-family: 'Arial Black'");
                   hBox.getChildren().add(label);
               }
           }
       }
   }
    private void populateRevisionTable(TableView<Revision> tableRevisions, List<String> jsonRevisions){
        for (String  rev: jsonRevisions){
            JSONObject revision = new JSONObject(rev);
            String endDate = null;
            String description = null;
            String nouvelEtat = null;
            try{
                endDate = revision.getString("endDate");
                description = revision.getString("description");
                nouvelEtat = revision.getString("nouvelEtat");
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            tableRevisions.getItems().add(
                    new Revision(
                            revision.getInt("id"),
                            revision.getString("startDate"),
                            endDate,
                            description,
                            revision.getString("ancienEtat"),
                            nouvelEtat,
                            revision.getInt("nouvelKm"),
                            revision.getJSONObject("ambulance").getInt("km"),
                            revision.getString("typeRevision")
                    )
            );
            tableRevisions.refresh();
        }
    }
    @FXML
    private void onUpdateClick(ActionEvent event){
            int id = target
                    .path("revision")
                    .queryParam("immatriculation", AmbulanceCard.selectedAmbulanceImmatriculation)
                    .queryParam("dateDebut", datePicker.getValue())
                    .queryParam("typeRevision",  chBoxTypeRevision.getValue())
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.json("Hello"))
                    .readEntity(Integer.class);
            ObservableList<Revision> revisions = tblRevisions.getItems();
            Revision revision = new Revision(
                id,
                datePicker.getValue().toString(),
                AmbulanceCard.selectedAmbulanceEtat,
                AmbulanceCard.selectedAmbulanceKm,
                chBoxTypeRevision.getValue()
                );
            revisions.add(revision);
            revisionItems.add(revision);
            tblRevisions.setItems(revisions);
            updateTblRevisions();
        boolean isInRevision = false;
        if(isInRevision) {
            btnUpdate.setOnAction(this::onEndRevisionClick);
            btnUpdate.setText("Stop");
            btnUpdate.setStyle("-fx-background-color: red");
            hBox.getChildren().clear();
            datePicker.setDayCellFactory(this::disableSpecificDates);
            hBox.getChildren().addAll(chBoxTypeRevision, datePicker, btnUpdate);
            tblRevisions.refresh();
        }
        id+=1;
    }
    @FXML
    private void onEndRevisionClick(ActionEvent event){
        Platform.runLater(() -> {
            Stage stage = new Stage();
            VBox vbox = new VBox();
            Scene scene = new Scene(vbox, 400, 600);
            scene.getStylesheets().add("stylesheet.css");
            stage.setScene(scene);
            Label lbl = new Label("Finir revision");
            lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 15");
            vbox.setSpacing(10);
            // create fields for the other attributes
            TextField nouvelKmField = new TextField(String.valueOf(currentRevision.getNouvelKm()));
            nouvelKmField.setPromptText("Nouvel Km");
            TextArea description = new TextArea(currentRevision.getDescription());
            description.setPromptText("Description");
            DatePicker datePickerUpdateEnd= new DatePicker(LocalDate.parse(currentRevision.getStartDate()));
            datePickerUpdateEnd.setPromptText("End Date");
            datePickerUpdateEnd.setDayCellFactory(this::disableSpecificDates);
            datePickerUpdateEnd.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if(date.isBefore(LocalDate.parse(currentRevision.getStartDate()))){
                        setDisable(true);
                        setStyle("-fx-background-color: red;");
                    }

                }});
            // Custom DayCellFactory to disable specific dates

            Button btnOk = new Button("OK");
            btnOk.getStyleClass().add("button6");
            Button btnCancel = new Button("Cancel");
            btnOk.setOnAction(okEvent -> {
                try {
                    Response updateRevisionResponse = target
                            .path("revision")
                            .path("update")
                            .path(String.valueOf(currentRevision.getId()))
                            .queryParam("ancienKm", currentRevision.getAncienKm())
                            .queryParam("nouvelKm", nouvelKmField.getText())
                            .queryParam("dateDebut", currentRevision.getStartDate())
                            .queryParam("dateSortie", datePickerUpdateEnd.getValue())
                            .queryParam("description", description.getText())
                            .queryParam("typeRevision", currentRevision.getType())
                            .request(MediaType.APPLICATION_JSON_TYPE)
                            .put(Entity.json("Hello"));
//                    if(updateRevisionResponse.getStatus() == 200) {
                        currentRevision.setNouvelKm(Integer.parseInt(nouvelKmField.getText()));
                        currentRevision.setEndDate(datePickerUpdateEnd.getValue().plusDays(1).toString());
                        currentRevision.setDescription((description.getText()));
                        currentRevision.setNouvelEtat("F");
                        tblRevisions.refresh();
//                    }
                    hBox.getChildren().remove(btnUpdate);
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
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        });}
    private DateCell disableSpecificDates(DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Disable specific dates here
                if ((date.isBefore(currentRevisionStartDate)) || (date.isEqual(currentRevisionStartDate))){
                    setDisable(true);
                    setStyle("-fx-background-color: red;"); // Optional: Change the style of disabled dates
                }
            }
        };
    }
    private DateCell disablePastDays(DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Disable specific dates here
                if ((date.isBefore(LocalDate.now()))){
                    setDisable(true);
                    setStyle("-fx-background-color: red;"); // Optional: Change the style of disabled dates
                }
            }
        };
    }
    @FXML
    private void onPredictClick(ActionEvent event){
        if (isInRevision){
        Double y = target.path("revision")
                .path("predict_y")
                .queryParam("immatriculation", AmbulanceCard.selectedAmbulanceImmatriculation)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get()
                .readEntity(Double.class);
        Double dobl = y - ChronoUnit.DAYS.between(LocalDate.parse(currentRevision.getStartDate()), LocalDate.now());
        dobl = dobl >= 0 ? dobl : 0;
        int inter = dobl.intValue();
        String text = "Il reste " + inter + " jours avant la fin de la révision";
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 15; -fx-font-family: 'Arial Black'");
        hBoxPredict.getChildren().clear();
        hBoxPredict.getChildren().add(label);
    }else{
        String etat = currentRevision.getAncienEtat();
        String text = etat.equals("F")
                ? "Il reste " + (200 - ChronoUnit.DAYS.between(LocalDate.parse(currentRevision.getStartDate()), LocalDate.now())) + " jours pour la prochaine revision"
                : etat.equals("NFCD")
                ? "Il reste " + (130 - ChronoUnit.DAYS.between(LocalDate.parse(currentRevision.getStartDate()), LocalDate.now())) + " jours pour la prochaine revision"
                : "Il reste " + (130 - ChronoUnit.DAYS.between(LocalDate.parse(currentRevision.getStartDate()), LocalDate.now())) + " jours pour la prochaine revision";
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 15; -fx-font-family: 'Arial Black'");
        hBoxPredict.getChildren().clear();
        hBoxPredict.getChildren().add(label);
        }
    }
}
