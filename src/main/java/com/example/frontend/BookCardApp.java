package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.*;

public class BookCardApp  {

    ScrollPane scroll;

    AnchorPane primaryStage=new AnchorPane();

    FlowPane parentGridPane;

    String fristname;

    String password;

    String test;

    Client client ;
    WebTarget target ;




    public BookCardApp(){
        // Create the parent GridPane
       parentGridPane = new FlowPane();

        scroll=new ScrollPane();

        scroll.setContent(parentGridPane);

        parentGridPane.setPadding(new Insets(20));

        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);

             //   scroll.setStyle("-fx-background-color: rgba(0,0,0); ");

//        parentGridPane.setStyle("-fx-background-color: rgb(255,255,255); ");
     parentGridPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, rgba(63,144,155,0.71), rgba(80,152,203,0.65),rgba(195,206,217,0.76));");
        parentGridPane.setHgap(5);
        parentGridPane.setVgap(5);
//        parentGridPane.setPadding(new Insets(20));





        ////  HTTP request ///////////////////////////////

         fristname="user1";

         password="1234,user";

         test=fristname+":"+password;

         client = ClientBuilder.newClient();

         target = client.target("http://localhost:8081");

        Response getResponse = target
                .path("lits")
                .path("litinespace")
                .request()
//                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(test.getBytes()))
                .get();


        Map<String, List<String>> data= getResponse.readEntity(Map.class);

        Map<JSONObject,List<JSONObject>> data2=new HashMap<>();


        for (String key : data.keySet()) {
            List<JSONObject> litJsonList = new ArrayList<>();
            for (String lit : data.get(key)) {

                lit = lit.replaceAll("\\s*:\\s*", "\":\"").replaceAll("\\s*,\\s*", "\",\"").
                        replaceAll("\\{", "{\"").
                        replaceAll("}", "\"}");
                litJsonList.add(new JSONObject(lit));
            }

            key = key.replaceAll("\\s*:\\s*", "\":\"").replaceAll("\\s*,\\s*", "\",\"").
                    replaceAll("\\{", "{\"").
                    replaceAll("}", "\"}");



            JSONObject spaceJson = new JSONObject(key);
            data2.put(spaceJson, litJsonList);
        }


        for(JSONObject key: data2.keySet()){

            BorderStroke borderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THICK);
            Border border = new Border(borderStroke);


            // create flow pane
            FlowPane g1=new FlowPane();

            // create Border pane
            BorderPane bp=new BorderPane();
            // set flow pane in center of border pane
            bp.setCenter(g1);
            // set border to flow pane
            g1.setBorder(border);

            Label barLabel = new Label("                                            Chambre "+key.get("idEspace"));
            barLabel.setStyle("-fx-font-family: 'Arial Black'");



            HBox labelBox = new HBox(barLabel);
            labelBox.setAlignment(Pos.CENTER_RIGHT);
            labelBox.setPadding(new Insets(5));
            labelBox.setStyle("-fx-background-color: blue");

            // Bind the width of the label box to the width of the center content of the BorderPane
            labelBox.prefWidthProperty().bind(bp.widthProperty());
           // add Hbox to the button of border pane
            bp.setBottom(barLabel);



            g1.setPadding(new Insets(10,10,10,10));
//            g1.setStyle("-fx-background-color: rgb(177,225,31);");
            g1.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, rgb(255,255,255), rgb(255,255,255),rgb(255,255,255));");

            for(JSONObject value:data2.get(key)){

                if(value.getBoolean("occupied")) {

                    BookCard b1 = new BookCard("The Lord ", value.get("idLit").toString(),
                            new Image("red.png"));

                    g1.getChildren().add(b1);
                }
                else{
                    BookCard b1 = new BookCard("The Lord ", value.get("idLit").toString(),
                            new Image("green.png"));

                    g1.getChildren().add(b1);

                }

            }

            g1.setPadding(new Insets(50,50,50,50));

            g1.setVgap(10);
            g1.setHgap(10);

            parentGridPane.getChildren().add(bp);


        }





          primaryStage.getChildren().add(scroll);

        primaryStage.setLeftAnchor(scroll,0.0);
        primaryStage.setRightAnchor(scroll,0.0);



    }



}

class BookCard extends Pane {
    private ImageView coverImageView;
    private Label titleLabel;
    private Label authorLabel;

    double mouseX ;
    double mouseY ;

    Stage popupStage;


    String fristname;

    String password;

    String test;

    Client client ;
    WebTarget target ;




    public BookCard(String title, String idLit, Image coverImage) {
        coverImageView = new ImageView(coverImage);
        coverImageView.setFitWidth(100);
        coverImageView.setPreserveRatio(true);
        titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.CENTER);

        authorLabel = new Label(idLit);
        authorLabel.setStyle("-fx-font-style: italic;");
        authorLabel.setAlignment(Pos.BOTTOM_RIGHT);

        VBox vb=new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(titleLabel,coverImageView,authorLabel);

        // Add components to the BookCard pane
        getChildren().add(vb);


        //initialize the client to send http request

        fristname="user1";

        password="1234,user";

        test=fristname+":"+password;

        client = ClientBuilder.newClient();

        target = client.target("http://localhost:8081");


        // Create context menu with a "Delete" option
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem deleteItem2 = new MenuItem("Modify");
        MenuItem deleteItem3 = new MenuItem("properties");
        MenuItem deleteItem4 = new MenuItem("Liberer Lit");
        deleteItem.setStyle("-fx-font-weight: bold;");


        // liberer lit

        deleteItem4.setOnAction(event -> {

            if(coverImageView.getImage().getUrl().contains("green.png")){
                coverImageView.setImage(new Image("red.png"));
//
//
          }
//            if(coverImageView.getImage().getUrl().contains("red.png")){
            else{
                coverImageView.setImage(new Image("green.png"));

            }

            // Handle delete action
            Response getResponse = target
                    .path("lits")
                    .path("occuperlit")
                    .queryParam("id",idLit)
                    .request(MediaType.APPLICATION_JSON)
                   // .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(test.getBytes()))
                    .put(Entity.json("{}"));
//


            if (getResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                System.out.println("HTTP PUT request succeeded.");
            } else {
                System.out.println("HTTP PUT request failed with status code " + getResponse.getStatus());
            }

            // close the JAX-RS client instance
//            client.close();
        });


       // when i click on Item this Action will happend
        deleteItem3.setOnAction(event -> {

            // Handle delete action
            System.out.println("Delete item clicked for book: " + title);

            Stage primaryStage = (Stage) getScene().getWindow();
            popupStage.initOwner(primaryStage);
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Create the pop-up window content
            Label popupLabel = new Label(title);
            popupLabel.setStyle("-fx-font-size: 20px;");

            // Add the pop-up window content to the Stage
            StackPane popupPane = new StackPane(popupLabel);
            popupPane.setPadding(new Insets(20));
            popupStage.setScene(new Scene(popupPane, 300, 200));


            // Show the pop-up window
            popupStage.showAndWait();
        });

        ContextMenu contextMenu = new ContextMenu(deleteItem,deleteItem2,deleteItem3,deleteItem4);

        // Show context menu on right-click
        setOnContextMenuRequested(event -> {

            // all this code for retrieve cursor conrdonate x et y ====> to pop up popupStage near to the curson is cool
            popupStage=new Stage();

            mouseX = event.getScreenX();
            mouseY = event.getScreenY();

            popupStage.setX(mouseX);
            popupStage.setY(mouseY);

            ///////////////////////////////////////////

            contextMenu.show(this, mouseX, mouseY);

        });

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setOffsetX(10);
        shadow.setOffsetY(10);

        this.setOnMousePressed(event -> {
            this.setEffect(shadow);
        });

        this.setOnMouseReleased(event -> {
            this.setEffect(null);
    });
    }




    static int gRNum() {
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1; // generates a random number between 1 and 100 (inclusive)
        return randomNumber;
    }
}
