package com.example.frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;

public class ServiceCard extends BorderPane {
    private String nomService;
    private int totalBeds;
    private int dispoBeds;
    private int occupiedBeds;

    public ServiceCard(String nomService, int totalBeds, int dispoBeds, int occupiedBeds) {
        this.nomService = nomService;
        this.totalBeds = totalBeds;
        this.dispoBeds = dispoBeds;
        this.occupiedBeds = occupiedBeds;

        Label serviceLabel = new Label(nomService);
        serviceLabel.setStyle("-fx-font-size: 24px;-fx-text-fill: white; -fx-font-weight: bold;");

        Label totalBedsLabel = new Label(Integer.toString(totalBeds));
        totalBedsLabel.setStyle("-fx-text-fill: white;-fx-font-size: 16px;");
        Label dispoBedsLabel = new Label(Integer.toString(dispoBeds));
        dispoBedsLabel.setStyle("-fx-text-fill: white;-fx-font-size: 16px;");
        Label occupiedBedsLabel = new Label(Integer.toString(occupiedBeds));
        occupiedBedsLabel.setStyle("-fx-text-fill: white;-fx-font-size: 16px;");

        ImageView bed = new ImageView("regularbed.png");
        bed.setFitWidth(30);
        bed.setFitHeight(30);
        ImageView dispobed = new ImageView("dispobed.png");
        dispobed.setFitWidth(30);
        dispobed.setFitHeight(30);
        ImageView occupiedbed = new ImageView("occupebed.png");
        occupiedbed.setFitWidth(30);
        occupiedbed.setFitHeight(30);

        PieChart chart = createChart(dispoBeds, occupiedBeds);

        HBox chartBox = new HBox();
        chartBox.setAlignment(Pos.CENTER);
        chartBox.setSpacing(10);
        chartBox.getChildren().addAll(chart);

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER);
        top.getChildren().addAll(serviceLabel,chartBox);

        HBox center = new HBox();
        center.setAlignment(Pos.CENTER);
        center.setSpacing(30);
        center.getChildren().addAll(bed, totalBedsLabel);

        HBox bottom1 = new HBox();
        bottom1.setSpacing(30);
        bottom1.setAlignment(Pos.CENTER);
        bottom1.getChildren().addAll(dispobed, dispoBedsLabel);

        HBox bottom2 = new HBox();
        bottom2.setAlignment(Pos.CENTER);
        bottom2.setSpacing(30);
        bottom2.getChildren().addAll(occupiedbed, occupiedBedsLabel);

        VBox bottom = new VBox();
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(40);
        bottom.getChildren().addAll(bottom1, bottom2);


        this.setStyle("-fx-background-color: linear-gradient(to bottom right, #3f51b5, #2196f3); -fx-background-radius: 20px;" +
                "-fx-effect:  dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)");

        // Create Pie Chart


        // Create VBox to hold Pie Chart and labels


        this.topProperty().set(top);
        this.centerProperty().set(center);
        this.bottomProperty().set(bottom);


        // Add the chart to the right side of the card
        //this.setRight(chartBox);
    }

    // Helper method to create the Pie Chart
    private PieChart createChart(int dispoBeds, int occupiedBeds) {
        PieChart.Data[] pieChartData = {
                new PieChart.Data("",occupiedBeds),
                new PieChart.Data("", dispoBeds)
        };

        PieChart chart = new PieChart();
        //chart.setTitle("Bed Occupancy");
        chart.setLegendSide(Side.RIGHT);

        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList(Arrays.asList(pieChartData));
        chart.setData(chartData);
        chart.setMaxSize(10,10);



        return chart;
    }
    }