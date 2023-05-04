package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private Client client = ClientBuilder.newClient()
            .register(JacksonFeature .class);
    private WebTarget target = client.target(Connextion_info.url);

    @FXML
    CategoryAxis xAxis = new CategoryAxis();
    @FXML
    NumberAxis yAxis = new NumberAxis();
    @FXML
    LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

    @FXML
    CategoryAxis service = new CategoryAxis();
    @FXML
    NumberAxis bedsAxis = new NumberAxis();
    @FXML
    StackedBarChart<String, Number> barChart = new StackedBarChart<>(service, bedsAxis);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        barChart.setTitle("Lits par Service");

        Response  getDepartements = target
                .path("departement")
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<String> departements = getDepartements.readEntity(List.class);

        for (String departement : departements) {
            service.getCategories().add(departement);
        }

        XYChart.Series<String, Number> dispobedsSeries = new XYChart.Series<>();
        dispobedsSeries.setName("Lits disponibles");

        XYChart.Series<String, Number> occupiedbedsSeries = new XYChart.Series<>();
        occupiedbedsSeries.setName("Lits occupés");

        for (String elt : departements) {
            Response getResponse2 = target
                    .path("departement")
                    .path("stock")
                    .queryParam("nomDepartement", elt)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            List<String> allBeds = getResponse2.readEntity(List.class);

            Response getResponse3 = target
                    .path("departement")
                    .path(elt)
                    .path("/Chambre")
                    .path("/lits")
                    .path("false")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            List<String> alldispobedch = getResponse3.readEntity(List.class);

            Response getResponse4 = target
                    .path("departement")
                    .path(elt)
                    .path("/Salle")
                    .path("/lits")
                    .path("false")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            List<String> alldispobedsalle = getResponse4.readEntity(List.class);

            Response getResponse5 = target
                    .path("departement")
                    .path(elt)
                    .path("/Chambre")
                    .path("/lits")
                    .path("true")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            List<String> alloccupiedch = getResponse5.readEntity(List.class);

            Response getResponse6 = target
                    .path("departement")
                    .path(elt)
                    .path("/Salle")
                    .path("/lits")
                    .path("true")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            List<String> alloccupiedsalle = getResponse6.readEntity(List.class);

            int dispobeds = alldispobedch.size() + alldispobedsalle.size();
            int occupiedbeds = alloccupiedch.size() + alloccupiedsalle.size();

            dispobedsSeries.getData().add(new XYChart.Data<>(elt, dispobeds));
            occupiedbedsSeries.getData().add(new XYChart.Data<>(elt, occupiedbeds));
        }

        barChart.getData().addAll(dispobedsSeries, occupiedbedsSeries);


        xAxis.setLabel("Week");
        yAxis.setLabel("Total Demandes");


        JSONObject groupedData = new JSONObject();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (String departement : departements) {
            Response getResponse = target.path("demandeaffectation")
                    .path("demandesDepartement")
                    .queryParam("nomDepartement", departement)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
            String responseString = getResponse.readEntity(String.class);
            JSONArray jsonArray = new JSONArray(responseString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String dateStr = jsonObject.getString("date_debut");
                LocalDate date = LocalDate.parse(dateStr, formatter);
                int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                int qte = jsonObject.getInt("qte");
                JSONObject departementData = groupedData.optJSONObject(departement);
                if (departementData == null) {
                    departementData = new JSONObject();
                    groupedData.put(departement, departementData);
                }
                int totalDemandes = departementData.optInt(String.valueOf(week), 0);
                departementData.put(String.valueOf(week), totalDemandes + qte);
            }
        }
        for (String departement : groupedData.keySet()) {
            JSONObject departementData = groupedData.getJSONObject(departement);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(departement);

            for (String weekStr : departementData.keySet()) {
                int week = Integer.parseInt(weekStr);
                String weekLabel = "Week " + week;
                int totalDemandes = departementData.getInt(weekStr);
                XYChart.Data<String, Number> data = new XYChart.Data<>(weekLabel, totalDemandes);
                series.getData().add(data);
            }

            lineChart.getData().add(series);
        }

        lineChart.setTitle("Total de Lits Demandés par semaine");
    }


}