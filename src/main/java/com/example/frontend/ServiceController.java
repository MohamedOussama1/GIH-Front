package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ServiceController implements Initializable {
    @FXML
    GridPane gridPane;
    @FXML
    Label titre;

    private Client client = ClientBuilder.newClient();

    private WebTarget target = client.target("http://localhost:8081");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Response getResponse1 = target
                .path("departement")
                .request()
                .get();
        List<String> departements = getResponse1.readEntity(List.class);
        int row = 0;
        int column = 0;

        for (String elt : departements){
            Response getResponse2 = target
                    .path("departement")
                    .path(elt)
                    .path("/lits")
                    .request()
                    .get();
            List<String> allBeds = getResponse2.readEntity(List.class);

            Response getResponse3 = target
                    .path("departement")
                    .path(elt)
                    .path("/Chambre")
                    .path("/lits")
                    .path("False")
                    .request()
                    .get();
            List<String> alldispobedch = getResponse3.readEntity(List.class);


            Response getResponse4 = target
                    .path("departement")
                    .path(elt)
                    .path("/Salle")
                    .path("/lits")
                    .path("False")
                    .request()
                    .get();
            List<String> alldispobedsalle = getResponse4.readEntity(List.class);

            Response getResponse5 = target
                    .path("departement")
                    .path(elt)
                    .path("/Chambre")
                    .path("/lits")
                    .path("True")
                    .request()
                    .get();
            List<String> alloccupiedch = getResponse5.readEntity(List.class);

            Response getResponse6 = target
                    .path("departement")
                    .path(elt)
                    .path("/Salle")
                    .path("/lits")
                    .path("True")
                    .request()
                    .get();
            List<String> alloccupiedsalle = getResponse6.readEntity(List.class);

            ServiceCard serviceCard = new ServiceCard(elt,allBeds.size(),alldispobedch.size()+alldispobedsalle.size(),alloccupiedch.size()+alloccupiedsalle.size());



            gridPane.add(serviceCard,row,column);

            if (column==2){
                column = 0;
                row += 1;

            }
            else {
                column+=1;
            }
        }





    }

}

