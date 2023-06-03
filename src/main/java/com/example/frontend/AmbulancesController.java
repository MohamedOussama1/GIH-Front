package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.*;

public class AmbulancesController implements Initializable {

    @FXML
    GridPane gridPane;
    private static Client client = ClientBuilder.newClient();

    private static WebTarget target = client.target("http://localhost:8081");

    // Aya
    @FXML
    private TextField imatriculationTextField;
    @FXML
    private TextField kmTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private ChoiceBox<String> typeAmbulanceChoiceBox = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> typeAmbulanceChoiceBox1 = new ChoiceBox<>();
    @FXML
    private DatePicker dateMiseEnService;
    @FXML
    TableView<JSONObject> afficherAmbulancesTableView;
    @FXML
    private TableColumn<JSONObject, String> immatriculationCol = new TableColumn<>("immatriculation");
    @FXML
    private TableColumn<JSONObject, String> dateMiseEnServiceCol = new TableColumn<>("dateMiseEnService");
    @FXML
    private TableColumn<JSONObject, Integer> kmCol = new TableColumn<>("km");
    @FXML
    private TableColumn<JSONObject, Integer> idCol = new TableColumn<>("id");
    @FXML
    private TableColumn<JSONObject, String> etatAmbulanceCol = new TableColumn<>("etatAmbulance");
    @FXML
    private TableColumn<JSONObject, String> typeAmbulanceCol = new TableColumn<>("typeAmbulance");
    @FXML
    private TableColumn<JSONObject, String> modelCol = new TableColumn<>("model");
    @FXML
    private TableColumn<JSONObject, Void> actionCol = new TableColumn<>("Actions");

    ObservableList<String> ambulanceTypes = FXCollections.observableArrayList(
            "soins de base", "soins intensifs", "secours d'urgence");
    ObservableList<JSONObject> ambumanceItems = FXCollections.observableArrayList();


    //    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        int row = 0;
//        int column = 0;
//        Response getResponse3 = target.path("ambulances")
//                .request(MediaType.APPLICATION_JSON_TYPE)
//                .get();
//        if (getResponse3.getStatus() == Response.Status.OK.getStatusCode()) {
//            String responseString = getResponse3.readEntity(String.class);
//            System.out.println("Response String: " + responseString);
//            JSONArray jsonArray = new JSONArray(responseString);
//            ObservableList<AmbulancesController.AmbulanceFX> tableData = FXCollections.observableArrayList();
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String immatriculation = jsonObject.getString("immatriculation");
//                String etat = jsonObject.getString("etatAmbulance");
//                int km = jsonObject.getInt("km");
//
//                AmbulanceFX am = new AmbulanceFX(immatriculation,etat, km);
//
//                AmbulanceCard ambulanceCard = new AmbulanceCard(immatriculation,etat, km);
//                System.out.println(am);
//
//
//
//                gridPane.add(ambulanceCard,column,row);
//
//                if (column==1){
//                    column = 0;
//                    row += 1;
//
//                }
//                else {
//                    column+=1;
//                }
//
//
//            }
//
//        }
//        afficherAmbulancesTableView.setItems(ambumanceItems);
//        loadDataInParallel();
//    }
//    private void loadDataInParallel() {
//            JSONArray jsonArray1  = new JSONArray(target.path("ambulances")
//                    .request(MediaType.APPLICATION_JSON_TYPE)
//                    .get()
//                    .readEntity(String.class)
//            );
//                    Platform.runLater(() -> {
//                        //PopulateChoiceBoxes
//                        typeAmbulanceChoiceBox.setItems(ambulanceTypes);
//                        typeAmbulanceChoiceBox1.setItems(ambulanceTypes);
//
//                        // PopulateTable
//                        for (int k = 0; k < jsonArray1.length(); k++) {
//                            JSONObject jsonObject = jsonArray1.getJSONObject(k);
//                            ambumanceItems.add(jsonObject);
//                        }
//
//                        // Bind the columns to the corresponding JSON objects
//                        immatriculationCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("immatriculation")));
//                        dateMiseEnServiceCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get("date_mise_service").toString()));
//                        kmCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getInt("km")).asObject());
//                        idCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getInt("id")).asObject());
//                        etatAmbulanceCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("etatAmbulance")));
//                        typeAmbulanceCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("typeAmbulance")));
//                        modelCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("model")));
//
//                        typeAmbulanceChoiceBox1.setItems(ambulanceTypes);
//                        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
//                        LitController.formatTextFieldToNumbersOnly(kmTextField, Integer.MAX_VALUE);
//                        idCol.setVisible(false);
//                        afficherAmbulancesTableView.getColumns().addAll(actionCol);
//                        actionCol.setCellFactory(param -> new TableCell<>() {
//                            final Button updateButton = new Button("Update");
//                            final Button deleteButton = new Button("Delete");
//
//                            {
//                                updateButton.setOnAction(event -> {
//                                    JSONObject ambulance = getTableView().getItems().get(getIndex());
//                                    int id = ambulance.getInt("id");
//                                    String immatriculation = ambulance.getString("immatriculation");
//                                    String date_mise_en_service = ambulance.getString("date_mise_service");
//                                    int km = ambulance.getInt("km");
//                                    String dateWithoutTime = date_mise_en_service.substring(0, 10);
//                                    ChoiceBox<String> typeAmbulanceChoiceBox = new ChoiceBox<>();
//                                    typeAmbulanceChoiceBox.setItems(ambulanceTypes);
//
//                                    updateAlert(id, immatriculation, dateWithoutTime, km, typeAmbulanceChoiceBox, ambulance.getString("model"));
//                                });
//
//                                deleteButton.setOnAction(event -> {
//                                    JSONObject ambulance = getTableView().getItems().get(getIndex());
//                                    int id = ambulance.getInt("id");
//                                    deleteAlert(id);
//                                });
//                            }
//
//                            @Override
//                            protected void updateItem(Void item, boolean empty) {
//                                super.updateItem(item, empty);
//
//                                if (empty) {
//                                    setGraphic(null);
//                                } else {
//                                    HBox buttonsContainer = new HBox(updateButton, deleteButton);
//                                    buttonsContainer.setSpacing(10);
//                                    setGraphic(buttonsContainer);
//                                }
//                            }
//                        });
//                    });
//    }
//    @FXML
//    protected void onAjouterAmbulance(ActionEvent event) {
//        String immatriculation = imatriculationTextField.getText();
//        LocalDate selectedDate = dateMiseEnService.getValue();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        int km = Integer.parseInt(kmTextField.getText());
//        String model = modelTextField.getText();
//        String typeAmbulance = getTypeAmbulanceValue(typeAmbulanceChoiceBox1.getValue());
//
//        String formattedDate = selectedDate.format(formatter);
//
////        String formattedDate = selectedDate.format(formatter);
//
//        // Send a POST request to the server with query parameters
//            Response postResponse = target.path("ambulances")
//                    .queryParam("immatriculation", immatriculation)
//                    .queryParam("date_mise_en_service", formattedDate)
//                    .queryParam("km", km)
//                    .queryParam("type_ambulance", typeAmbulance)
//                    .queryParam("model", model)
//                    .request(MediaType.APPLICATION_JSON_TYPE)
//                    .post(Entity.json("Hello"));
//
//            // Check the response status
//            if (postResponse.getStatus() == Response.Status.OK.getStatusCode()) {
//                Platform.runLater(() -> {
//                    // Display a success message
//                    String successMessage = "Ambulance ajoutée avec succès.";
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION, successMessage);
//                    alert.showAndWait();
//                    // Clear the input fields
//                    imatriculationTextField.setText("");
//                    dateMiseEnService.setValue(null);
//                    kmTextField.setText("");
//                    modelTextField.setText("");
//                    typeAmbulanceChoiceBox1.setValue("");
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("immatriculation", immatriculation);
//                    jsonObject.put("date_mise_service", dateMiseEnService.getValue());
//                    jsonObject.put("km", km);
//                    jsonObject.put("typeAmbulance", typeAmbulance);
//                    jsonObject.put("model", model);
//                    jsonObject.put("etatAmbulance", "F");
//                    ambumanceItems.add(jsonObject);
//                });
//            } else {
//                // Display an error message
//                String errorMessage = "Erreur lors de l'ajout de l'ambulance.";
//                Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage);
//                alert.showAndWait();
//            }
////        Platform.runLater(() -> {
////            try {
////                onAfficherAmbulances(null);
////            } catch (IOException e) {
////                throw new RuntimeException(e);
////            }
////        });
//    }
//
//
//
//    protected void updateAmbulance(int id, String immatriculation, String date_mise_en_service, int km, String typeAmbulance, String model) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        LocalDate date = LocalDate.parse(date_mise_en_service, formatter);
//        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//        Entity<?> entity = Entity.entity("", MediaType.APPLICATION_JSON_TYPE);
//        target.path("/ambulances/updateambulance")
//                .queryParam("id", id)
//                .queryParam("immatriculation", immatriculation)
//                .queryParam("date_mise_en_service", formattedDate)
//                .queryParam("km", km)
//                .queryParam("type_ambulance", typeAmbulance)
//                .queryParam("model", model)
//                .request(MediaType.APPLICATION_JSON)
//                .put(entity);
//        for (JSONObject jsonObject : ambumanceItems){
//            if (jsonObject.getInt("id") == id){
//                System.out.println("we win");
//                jsonObject.put("immatriculation", immatriculation);
//                jsonObject.put("date_mise_service", formattedDate);
//                jsonObject.put("km", km);
//                jsonObject.put("typeAmbulance", typeAmbulance);
//                jsonObject.put("model", model);
//                System.out.println(jsonObject);
//            }
//        }
//        afficherAmbulancesTableView.refresh();
//    }
//
//
//    protected void deleteAmbulance(int id) {
//        Response response = target.path("/ambulances/deleteambulance")
//                .queryParam("id", id)
//                .request(MediaType.APPLICATION_JSON)
//                .delete();
//        if(response.getStatus() != 200){
//            Platform.runLater(() -> {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Failed");
//                alert.setHeaderText(null);
//                alert.setContentText("Operation échouée, il existe des révisions liées à cet ambulance.");
//                alert.showAndWait();
//            });
//        }else{
//            ambumanceItems.removeIf(item -> item.getInt("id") == id);
//            afficherAmbulancesTableView.refresh();
//        }
//    }
//    protected void deleteAlert(int id) {
//        Platform.runLater(() -> {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Delete Ambulance");
//            alert.setHeaderText("Are you sure you want to delete this ambulance?");
//
//            Optional<ButtonType> result = alert.showAndWait();
//            if (result.isPresent() && result.get() == ButtonType.OK) {
//                deleteAmbulance(id);
//            }
//        });
//    }
//
//
//
//    protected void updateAlert(int id, String immatriculation, String date_mise_en_service, int km, ChoiceBox<String> typeAmbulanceChoiceBox, String model) {
//        Platform.runLater(() -> {
//            // Create the dialog
//            Dialog<Pair<String, LocalDate>> dialog = new Dialog<>();
//            dialog.setTitle("Update Ambulance");
//            dialog.setHeaderText("Veuillez entrer les nouvelles informations :");
//
//            // Set the button types
//            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
//            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);
//
//            // Create the layout
//            GridPane gridPane = new GridPane();
//            gridPane.setHgap(10);
//            gridPane.setVgap(10);
//            gridPane.setPadding(new Insets(20, 150, 10, 10));
//
//            // Add the text fields and choice box
//            TextField immatriculationField = new TextField(immatriculation);
//            TextField modelField = new TextField(model);
//            DatePicker datePicker = new DatePicker(LocalDate.parse(date_mise_en_service, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//            TextField kmField = new TextField(Integer.toString(km));
//            typeAmbulanceChoiceBox.setValue("soins de base"); // Set default value
//            gridPane.add(new Label("Immatriculation:"), 0, 0);
//            gridPane.add(immatriculationField, 1, 0);
//            gridPane.add(new Label("Date Mise En Service:"), 0, 1);
//            gridPane.add(datePicker, 1, 1);
//            gridPane.add(new Label("Km:"), 0, 2);
//            gridPane.add(kmField, 1, 2);
//            gridPane.add(new Label("Type Ambulance:"), 0, 3);
//            gridPane.add(typeAmbulanceChoiceBox, 1, 3);
//            gridPane.add(new Label("Model:"), 0, 4);
//            gridPane.add(modelField, 1, 4);
//
//            // Set the layout and show the dialog
//            dialog.getDialogPane().setContent(gridPane);
//            Platform.runLater(() -> immatriculationField.requestFocus());
//            dialog.setResultConverter(dialogButton -> {
//                if (dialogButton == updateButtonType) {
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//                    String formattedDate = datePicker.getValue().format(formatter);
//                    return new Pair<>(immatriculationField.getText(), LocalDate.parse(formattedDate, formatter));
//                }
//                return null;
//            });
//            Optional<Pair<String, LocalDate>> result = dialog.showAndWait();
//            result.ifPresent(pair -> {
//                String formattedDate = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//                String typeAmbulance = getTypeAmbulanceValue(typeAmbulanceChoiceBox.getValue());
//                String model1 = modelField.getText();
//                updateAmbulance(id, pair.getKey(), formattedDate, Integer.parseInt(kmField.getText()), typeAmbulance, model1);
//            });
//        });
//    }
//
//    private String getTypeAmbulanceValue(String typeAmbulanceString) {
//        switch (typeAmbulanceString) {
//            case "soins de base":
//                return "SOINS_DE_BASE";
//            case "soins intensifs":
//                return "SOINS_INTENSIFS";
//            case "secours d'urgence":
//                return "SECOURS_URGENCE";
//            default:
//                throw new IllegalArgumentException("Invalid typeAmbulance value: " + typeAmbulanceString);
//        }
//    }
     public class AmbulanceFX{
        private String im;
        private String etat;
        private double km;

        public String getIm() {
            return im;
        }
//
        public String getEtat() {
            return etat;
        }
        public AmbulanceFX(String im, String etat, double km){
            this.im=im;
            this.etat=etat;
            this.km = km;
        }
        public String toString(){
            return "immatriculation : " + this.im + "etat : " + this.etat + "km : " + this.km;
        }
    }
@FXML
protected void onAfficherAmbulances(ActionEvent event) throws IOException {
    Response getResponse3 = target.path("ambulances")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get();
    if (getResponse3.getStatus() == Response.Status.OK.getStatusCode()) {
        String responseString = getResponse3.readEntity(String.class);
        System.out.println("Response String: " + responseString);
        JSONArray jsonArray = new JSONArray(responseString);

        ObservableList<JSONObject> data = FXCollections.observableArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Integer id = jsonObject.getInt("id");
            String immatriculation = jsonObject.getString("immatriculation");
            String dateMiseEnService = jsonObject.getString("date_mise_service");
            Integer km = jsonObject.getInt("km");
            String etatAmbulance = jsonObject.getString("etatAmbulance");
            String typeAmbulance = jsonObject.getString("typeAmbulance");
            String modelAmbulance = jsonObject.getString("model");

            JSONObject row = new JSONObject();
            row.put("id", id);
            row.put("immatriculation", immatriculation);
            row.put("dateMiseEnService", dateMiseEnService);
            row.put("km", km);
            row.put("etatAmbulance", etatAmbulance);
            row.put("typeAmbulance", typeAmbulance);
            row.put("model", modelAmbulance);

            data.add(row);
        }

        // Bind the columns to the corresponding JSON objects
        immatriculationCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("immatriculation")));
        dateMiseEnServiceCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("dateMiseEnService")));
        kmCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getInt("km")).asObject());
        idCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getInt("id")).asObject());
        etatAmbulanceCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("etatAmbulance")));
        typeAmbulanceCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("typeAmbulance")));
        modelCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getString("model")));


        // Set the data to the table view
        afficherAmbulancesTableView.setItems(data);
    }
}

    @FXML
    protected void onAjouterAmbulance(ActionEvent event) {
        // Get the values entered by the user
        String immatriculation = imatriculationTextField.getText();
        LocalDate selectedDate = dateMiseEnService.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        int km = Integer.parseInt(kmTextField.getText());
        String model = modelTextField.getText();
        String typeAmbulance = getTypeAmbulanceValue(typeAmbulanceChoiceBox1.getValue());

        String formattedDate = selectedDate.format(formatter);

        // Send a POST request to the server with query parameters
        try {
            Response postResponse = target.path("ambulances")
                    .queryParam("immatriculation", immatriculation)
                    .queryParam("date_mise_en_service", formattedDate)
                    .queryParam("km", km)
                    .queryParam("type_ambulance", typeAmbulance)
                    .queryParam("model", model)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(null);

            // Check the response status
            if (postResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                // Display a success message
                String successMessage = "Ambulance ajoutée avec succès.";
                Alert alert = new Alert(Alert.AlertType.INFORMATION, successMessage);
                alert.showAndWait();

                // Clear the input fields
                imatriculationTextField.setText("");
                dateMiseEnService.setValue(null);
                kmTextField.setText("");
                modelTextField.setText("");
                typeAmbulanceChoiceBox1.setValue("");
                onAfficherAmbulances(null);
            } else {
                // Display an error message
                String errorMessage = "Erreur lors de l'ajout de l'ambulance.";
                Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage);
                alert.showAndWait();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() -> {
            try {
                onAfficherAmbulances(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }



    protected void updateAmbulance(int id, String immatriculation, String date_mise_en_service, int km, String typeAmbulance, String model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(date_mise_en_service, formatter);
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Entity<?> entity = Entity.entity("", MediaType.APPLICATION_JSON_TYPE);
        Response response = target.path("/ambulances/updateambulance")
                .queryParam("id", id)
                .queryParam("immatriculation", immatriculation)
                .queryParam("date_mise_en_service", formattedDate)
                .queryParam("km", km)
                .queryParam("type_ambulance", typeAmbulance)
                .queryParam("model", model)
                .request(MediaType.APPLICATION_JSON)
                .put(entity);
        response.close();
        System.out.println("Method has been called");
        try {
            onAfficherAmbulances(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected void deleteAmbulance(int id) {
        Response response = target.path("/ambulances/deleteambulance")
                .queryParam("id", id)
                .request(MediaType.APPLICATION_JSON)
                .delete();
        if (response.getStatus() != 200) {
//            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Failed");
                alert.setHeaderText(null);
                alert.setContentText("Operation échouée, il existe des révisions liées à cet ambulance.");
                alert.showAndWait();
//            });
        } else {
//                Platform.runLater(() ->
//                {
                    try {
                        onAfficherAmbulances(null);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
//                });
            }
        response.close();
    }
    protected void deleteAlert(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Ambulance");
        alert.setHeaderText("Are you sure you want to delete this ambulance?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteAmbulance(id);
        }
    }



    protected void updateAlert(int id, String immatriculation, String date_mise_en_service, int km, ChoiceBox<String> typeAmbulanceChoiceBox, String model) {
        // Create the dialog
        Dialog<Pair<String, LocalDate>> dialog = new Dialog<>();
        dialog.setTitle("Update Ambulance");
        dialog.setHeaderText("Veuillez entrer les nouvelles informations :");

        // Set the button types
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create the layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        // Add the text fields and choice box
        TextField immatriculationField = new TextField(immatriculation);
        TextField modelField = new TextField(model);
        DatePicker datePicker = new DatePicker(LocalDate.parse(date_mise_en_service, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        TextField kmField = new TextField(Integer.toString(km));
        typeAmbulanceChoiceBox.setValue("soins de base"); // Set default value
        gridPane.add(new Label("Immatriculation:"), 0, 0);
        gridPane.add(immatriculationField, 1, 0);
        gridPane.add(new Label("Date Mise En Service:"), 0, 1);
        gridPane.add(datePicker, 1, 1);
        gridPane.add(new Label("Km:"), 0, 2);
        gridPane.add(kmField, 1, 2);
        gridPane.add(new Label("Type Ambulance:"), 0, 3);
        gridPane.add(typeAmbulanceChoiceBox, 1, 3);
        gridPane.add(new Label("Model:"), 0, 4);
        gridPane.add(modelField, 1, 4);

        // Set the layout and show the dialog
        dialog.getDialogPane().setContent(gridPane);
        Platform.runLater(() -> immatriculationField.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = datePicker.getValue().format(formatter);
                return new Pair<>(immatriculationField.getText(), LocalDate.parse(formattedDate, formatter));
            }
            return null;
        });

        Optional<Pair<String, LocalDate>> result = dialog.showAndWait();
        result.ifPresent(pair -> {

            String formattedDate = pair.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String typeAmbulance = getTypeAmbulanceValue(typeAmbulanceChoiceBox.getValue());
            String model1 = modelField.getText();
            updateAmbulance(id, pair.getKey(), formattedDate, Integer.parseInt(kmField.getText()), typeAmbulance, model1);
            System.out.println(formattedDate);
        });
    }

    private String getTypeAmbulanceValue(String typeAmbulanceString) {
        switch (typeAmbulanceString) {
            case "soins de base":
                return "SOINS_DE_BASE";
            case "soins intensifs":
                return "SOINS_INTENSIFS";
            case "secours d'urgence":
                return "SECOURS_URGENCE";
            default:
                throw new IllegalArgumentException("Invalid typeAmbulance value: " + typeAmbulanceString);
        }
    }



    public void initialize(URL url, ResourceBundle resourceBundle) {
        int row = 0;
        int column = 0;
        Response getResponse3 = target.path("ambulances")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (getResponse3.getStatus() == Response.Status.OK.getStatusCode()) {
            String responseString = getResponse3.readEntity(String.class);
            System.out.println("Response String: " + responseString);
            JSONArray jsonArray = new JSONArray(responseString);
            ObservableList<AmbulancesController.AmbulanceFX> tableData = FXCollections.observableArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String immatriculation = jsonObject.getString("immatriculation");
                String etat = jsonObject.getString("etatAmbulance");
                int km = jsonObject.getInt("km");

                AmbulanceFX am = new AmbulanceFX(immatriculation, etat, km);

                AmbulanceCard ambulanceCard = new AmbulanceCard(immatriculation, etat, km);
                System.out.println(am);


                gridPane.add(ambulanceCard, column, row);

                if (column == 1) {
                    column = 0;
                    row += 1;

                } else {
                    column += 1;
                }



            }

        }
        LitController.formatTextFieldToNumbersOnly(kmTextField, Integer.MAX_VALUE);
        idCol.setVisible(false);
//        typeAmbulanceChoiceBox.setItems(ambulanceTypes);
        typeAmbulanceChoiceBox1.setItems(ambulanceTypes);


        try {
            onAfficherAmbulances(null);
            afficherAmbulancesTableView.getColumns().addAll(actionCol);
            actionCol.setCellFactory(param -> new TableCell<>() {
                final Button updateButton = new Button("Update");
                final Button deleteButton = new Button("Delete");

                {
                    updateButton.setOnAction(event -> {
                        JSONObject ambulance = getTableView().getItems().get(getIndex());
                        int id = ambulance.getInt("id");
                        String immatriculation = ambulance.getString("immatriculation");
                        String date_mise_en_service = ambulance.getString("dateMiseEnService");
                        int km = ambulance.getInt("km");
                        String dateWithoutTime = date_mise_en_service.substring(0, 10);
                        String model = ambulance.getString("model");
                        ChoiceBox<String> typeAmbulanceChoiceBox = new ChoiceBox<>();
                        typeAmbulanceChoiceBox.setItems(ambulanceTypes);

                        updateAlert(id, immatriculation, dateWithoutTime, km,typeAmbulanceChoiceBox,ambulance.getString("model"));
                    });

                    deleteButton.setOnAction(event -> {
                        JSONObject ambulance = getTableView().getItems().get(getIndex());
                        int id = ambulance.getInt("id");
                        deleteAlert(id);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox buttonsContainer = new HBox(updateButton, deleteButton);
                        buttonsContainer.setSpacing(10);
                        setGraphic(buttonsContainer);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
