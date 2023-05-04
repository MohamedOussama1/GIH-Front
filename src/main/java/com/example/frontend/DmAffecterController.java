package com.example.frontend;


import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONObject;

import java.net.URL;
import java.util.*;

public class DmAffecterController implements Initializable {
    double mouseX;
    double mouseY;
    @FXML
    private Accordion accrodion_0=new Accordion() ;
    @FXML
    private TilePane tilepane_chambre;
    @FXML
    ChoiceBox<String> cbox_chambre=new ChoiceBox<>();
    @FXML
    ChoiceBox<String> cbox_service=new ChoiceBox<>();
    private Client client = ClientBuilder.newClient()
            .register(JacksonFeature.class);
    private Client client2 = ClientBuilder.newClient()
            .register(JacksonFeature.class);
    static String url=Connextion_info.url;
    private WebTarget target = client.target(url);
    private WebTarget target1 = client.target(url);
    private WebTarget target2 = client2.target(url);
    HashMap<String,List<Integer>> map_dmitem_id=new HashMap<>();
    HashMap<String,List<Integer>> map_card_id=new HashMap<>();
    @FXML
    private Button btn_cancel_dm;
    @FXML
    private Button btn_save_dm;
    String nomService;
    String nomService2;
    int N_chambre_now;
    int N_chambre_old;
    HashMap<Integer,String> map_numero_and_type_espace=new HashMap<>();
    HashMap<Integer, Integer> id_numero_chambre=new HashMap<>();

// hashmap of chambre and their dm_s
    Map<JSONObject,List<JSONObject>> data_espace;
    Map<JSONObject,List<JSONObject>> data_stock;
    @FXML
    private ListView<Card> id_list_view_autos=new ListView<>();
    @FXML
    private TextField id_txt_search;
    ObservableList<Card> data_view=FXCollections.observableArrayList();
    @FXML
    private ScrollPane id_view_scroll;

    public void Http_data_to_hashmapat1(String Newvalue){

        Response getResponse_espace = target1
                .path("dm")
                .path("dminespace")
                .queryParam("nomDepartement",Newvalue)
                .request()
                .get();



        Response getResponse_stock = target2
                .path("dm")
                .path("dminstock")
                .queryParam("nomDepartement",Newvalue)
                .request(MediaType.APPLICATION_JSON)
                .get();



       //if(getResponse_stock.readEntity(Map.class)!=null &&getResponse_espace.readEntity(Map.class)!=null) {

            Map<String, List<String>> data_espace1 = getResponse_espace.readEntity(Map.class);
            Map<String, List<String>> data_stock1 = getResponse_stock.readEntity(Map.class);

            Helpers help = new Helpers();
            data_espace = help.MapString_toJson(data_espace1);
            System.out.println("==============   espace size  ==== " + data_espace.size());
            data_stock = help.MapString_toJson(data_stock1);
            System.out.println("==============   Stock size  ==== " + data_stock.size());

       // }

        Response getResponse2 = target
                .path("departement")
                .path("getespacebyservice")
                .queryParam("nomDepartement",Newvalue)
                .request()
                .get();
        List<String> chmabres = getResponse2.readEntity(List.class);

        List<String> chmabres0=this.choice_chambre_fillOut(chmabres);




        cbox_chambre.setItems(FXCollections.observableArrayList(chmabres0));

        this.fillOut_tiledpane();

    }






    public List<String> choice_chambre_fillOut(List<String> chmabres){

        List<String> chmabres0 =new ArrayList<>();
        chmabres.forEach(elt->{

            String[] part=elt.split(":");
            String type_espace=part[0].trim();
            int numero= Integer.parseInt(part[1].trim());
            int id=Integer.parseInt(part[2].trim());
            id_numero_chambre.put(numero,id);

            if(!Set.of(1000,1001,1002,1003,1004,1005).contains(numero)){
                chmabres0.add(type_espace+" : "+numero);
            }

        });

        return chmabres0;

    }



    public void Http_data_to_hashmapat0(){

        Response getResponse = target
                .path("departement")
                .request()
                .get();
        List<String> departements = getResponse.readEntity(List.class);

        cbox_service.setItems(FXCollections.observableArrayList(departements));

        cbox_service.getSelectionModel().selectedItemProperty().addListener((observable,Oldvalue,Newvalue) -> {

            if(Newvalue==null){
                Newvalue="Cardiologie";
            }
            accrodion_0.setVisible(true);

            id_view_scroll.setVisible(false);


            tilepane_chambre.setDisable(true);
            cbox_chambre.getSelectionModel().clearSelection();
            id_numero_chambre.clear();
            tilepane_chambre.getChildren().clear();

            System.out.println("Oldvalue "+Oldvalue);
            System.out.println("Newvalue "+Newvalue);

            nomService=Newvalue;
            nomService2=Oldvalue;

            this.Http_data_to_hashmapat1(Newvalue);


            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!    ###############################");
            DmstockCard dmdm= (DmstockCard) ((TitledPane)accrodion_0.getPanes().get(0)).getContent();

           Card card= (Card) dmdm.vbox.getChildren().get(0);
            System.out.println(card.title);

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!    ###############################");

        });



    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_cancel_dm.setVisible(false);

        id_view_scroll.setVisible(false);
        // Fill out choiceBox of services

        this.Http_data_to_hashmapat0();
        // Create a filtered list and bind it to the searchField text property
        FilteredList<Card> filteredList = new FilteredList<>(data_view);
        id_txt_search.textProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue == null || newValue.trim().isEmpty()) {
                accrodion_0.setVisible(true);
                id_view_scroll.setVisible(false);
                return;
            }
            else {
                accrodion_0.setVisible(false);
                id_view_scroll.setVisible(true);

            }

            System.out.println("@@@@@@@ ============ @@@@@@@@ =============");

            filteredList.setPredicate(item -> newValue == null || newValue.trim().isEmpty() ||
                    item.title.toLowerCase().contains(newValue.toLowerCase()));
        });

        // Bind the filtered list to the list view
        id_list_view_autos.setItems(filteredList);
        ///  fill out tilepane-chambre

        cbox_chambre.getSelectionModel().selectedItemProperty().addListener((observable,oldvalue,newvalue)->{

            tilepane_chambre.setDisable(false);
            System.out.println("n "+newvalue);
            System.out.println("o "+oldvalue);

            if(oldvalue != null){
                String[] part2 = oldvalue.split(":");
                N_chambre_old=Integer.parseInt(part2[1].trim());
                map_numero_and_type_espace.put(N_chambre_old,oldvalue);
            }
            if(newvalue !=null) {
                String[] parts = newvalue.split(":");
                N_chambre_now = Integer.parseInt(parts[1].trim());
                map_numero_and_type_espace.put(N_chambre_now,newvalue);
                System.out.println(cbox_service.getSelectionModel().getSelectedItem());
                this.fill_out_tilePane_Chambre(newvalue);
                this.make_Draggable_to_tilepane(tilepane_chambre);
            }
        });
        tilepane_chambre.setPadding(new Insets(10));
        tilepane_chambre.setHgap(10);
        tilepane_chambre.setVgap(10);
        tilepane_chambre.setTileAlignment(Pos.TOP_LEFT);
                try {
            // Your code that throws the exception

            this.cbox_service.getSelectionModel().select(1);
            this.cbox_service.getSelectionModel().select(0);
            this.cbox_service.getSelectionModel().clearSelection();
            this.accrodion_0.getPanes().clear();
        } catch (Exception e) {
            System.out.println("MessageBodyProviderNotFoundException caught: " );
            System.out.println("Printing a custom message instead of throwing the exception");
        }
    }
    public void fill_out_tilePane_Chambre(String espace){

        map_dmitem_id.clear();
        tilepane_chambre.getChildren().clear();

        String[] parts = espace.split(":");
        int numero = Integer.parseInt(parts[1].trim());
        // traverse this list data2.entrySet() that represents all rooms in a service
        data_espace.entrySet().forEach((elt)->{
             int numero2= (int) elt.getKey().get("numero");
            System.out.println(numero2);

            if(elt.getValue() !=null && numero2==numero) {

                List<JSONObject> json_list = (List<JSONObject>) elt.getValue();
                // traverse the json_list(list) which represents a room, and we want add its dispositif medical to it ;
                json_list.forEach(elt3->{

                    JSONObject elt3_cast=(JSONObject) elt3;

                    Image image = new Image((String) ((JSONObject)elt3_cast.get("dm")).get("image_path"));

                    int id_dmitem= (int) elt3_cast.get("id_dmitem");
                    System.out.println("=========="+id_dmitem+"=======");

                    String image_name1=image.getUrl();
                    int start=image_name1.lastIndexOf("\\");
                    int end=image_name1.lastIndexOf(".");
                    String image_name2=image_name1.substring(start+1,end);

                    DmItem dm = new DmItem(image_name2, image, 1);
                    if(tilepane_chambre.getChildren().contains(dm)){
                        map_dmitem_id.get(dm.title).add(id_dmitem);
                        DmItem dm00= (DmItem) tilepane_chambre.getChildren().get(tilepane_chambre.getChildren().indexOf(dm));
                        dm00.setQuantite(dm00.quantite+1);
                        //lst_dmitem_id.add()
                    }
                  else {
                      ArrayList<Integer> array=new ArrayList<>();
                        array.add(id_dmitem);
                        map_dmitem_id.put(dm.title,array);
                        tilepane_chambre.getChildren().add(dm);
                    }
                });
            }
        });
    }
    public void fillOut_tiledpane() {
        map_card_id.clear();
        accrodion_0.getPanes().clear();
        // traverse this list data2.entrySet() that represents all rooms in a service
        data_stock.entrySet().forEach((elt)->{
            int numero2= (int) elt.getKey().get("numero");
           // System.out.println(numero2);
            if(elt.getValue() !=null && Set.of(1001,1002,1003,1004).contains(numero2)) {
                List<JSONObject> json_list = (List<JSONObject>) elt.getValue();
                List<String> lst_categorie=new ArrayList<>();
                json_list.forEach(elt3->{
                    JSONObject elt3_cast=(JSONObject) elt3;
                    JSONObject str=(JSONObject) ((JSONObject)elt3_cast.get("dm")).get("typeDM");
                    String categorie= (String) str.get("categorie");
                    if(!lst_categorie.contains(categorie)){
                        lst_categorie.add(categorie);
                        TitledPane tiledpane_0 = new TitledPane();
                        tiledpane_0.setText(categorie);
                        DmstockCard stock=new DmstockCard();
                        //this.make_Draggable_to_Vbox(stock.vbox);
                        tiledpane_0.setContent(stock);
                        tiledpane_0.setExpanded(false);
                        accrodion_0.getPanes().add(tiledpane_0);
                    }
                });
                json_list.forEach(elt3->{

                    //////////// ==========================

                    JSONObject elt3_cast=(JSONObject) elt3;
                    JSONObject str=(JSONObject) ((JSONObject)elt3_cast.get("dm")).get("typeDM");
                    String categorie= (String) str.get("categorie");

                    //////////// ===============

                    int id_card= (int) elt3_cast.get("id_dmitem");
                    Image image = new Image((String) ((JSONObject) elt3_cast.get("dm")).get("image_path"));
                    String image_name1 = image.getUrl();
                    int start = image_name1.lastIndexOf("\\");
                    int end = image_name1.lastIndexOf(".");
                    String image_name2 = image_name1.substring(start + 1, end);

                    // add to map of card the name as a key and the list of id of dm
                    if(map_card_id.containsKey(image_name2)){

                        if(!map_card_id.get(image_name2).contains(id_card)) {
                            map_card_id.get(image_name2).add(id_card);
                        }
                    }
                    else{
                        ArrayList<Integer> lst_id_card2=new ArrayList<>();
                        lst_id_card2.add(id_card);
                        map_card_id.put(image_name2,lst_id_card2);

                    }

                    ////////////////////

                    for (TitledPane tile:accrodion_0.getPanes()) {
                        if(tile.getText().contains(categorie)){
                            DmstockCard stock= (DmstockCard) tile.getContent();
                            this.make_Draggable_to_Vbox(stock);
                            Card card=new Card(image_name2,image,1);

                            // add to listView
                            if (data_view == null || data_view.stream().noneMatch(card_elt -> card_elt.title.contains(card.title))) {
                                data_view.add(card);
                            }

                            ////    add right click
                            MenuItem deleteItem2 = new MenuItem("Affecter plusieur");
                            MenuItem deleteItem3 = new MenuItem("properties");

                            deleteItem2.setOnAction(actionEvent ->{

                                // Handle delete action
                                System.out.println("Delete item clicked for book: " );

                                Stage primaryStage = (Stage) accrodion_0.getScene().getWindow();
                                Stage popupStage=new Stage();
                                popupStage.initOwner(primaryStage);
                                popupStage.initModality(Modality.APPLICATION_MODAL);

                                // Create the pop-up window content
                                VBox vbox=new VBox();
                                vbox.setAlignment(Pos.CENTER);

                                ImageView im_view=new ImageView(card.coverImage);
                                im_view.setFitHeight(80);
                                im_view.setFitWidth(80);
                                vbox.getChildren().add(im_view);
                                Label popupLabel = new Label("title");
                                popupLabel.setStyle("-fx-font-size: 20px;");
                                vbox.getChildren().add(popupLabel);

                                Spinner<Integer> spinner = new Spinner<>(0, 10, 0);
                                spinner.setPrefSize(62,20);
                                Button button_spinner=new Button("Affecter");
                                button_spinner.setPrefSize(65,30);
                                Label label_spinner=new Label();
                                label_spinner.setPrefSize(65,30);

                                vbox.getChildren().addAll(spinner,label_spinner,button_spinner);
                                vbox.setPadding(new Insets(10));

                                // Add the pop-up window content to the Stage
                                Scene scene=new Scene(vbox,300,200);
                                popupStage.setScene(scene);
                                // Show the pop-up window
                                popupStage.showAndWait();
                            });
                            deleteItem2.setStyle("-fx-font-weight: bold;");
                            deleteItem3.setStyle("-fx-font-weight: bold;");
                            ContextMenu contextMenu = new ContextMenu(deleteItem2,deleteItem3);

                            // Show context menu on right-click
                            card.setOnContextMenuRequested(event -> {
                                double mouseX = event.getScreenX();
                                double mouseY = event.getScreenY();
                                contextMenu.show(card, mouseX, mouseY);
                            });
                            ////  end of affecter dm with quantity
                            stock.setHbox1(card);
                            //break;
                        }
                    }
                });
            }
        });
    }
    public void make_Draggable_to_tilepane(TilePane pane){
        pane.setOnDragOver(event ->
            event.acceptTransferModes(TransferMode.ANY));
        pane.setOnDragDropped(event -> {
            btn_cancel_dm.setVisible(true);
            Card button = (Card) event.getGestureSource();
          if(button.quantite>1) {
              button.setQuantite_moins();
              int id_card_removed = map_card_id.get(button.title).remove(0);
              this.add_to_TilePane_quantity(pane,button.title,button.coverImage,button.quantite,id_card_removed);
          }
          else{
              outerloop:
              for(TitledPane node:accrodion_0.getPanes()){
                  DmstockCard stock=(DmstockCard)node.getContent();
                  for(Node stock1:stock.vbox.getChildren()){
                      Card card= (Card) stock1;
                      if(card.equals(button)){
                          int id_card_removed = map_card_id.get(button.title).remove(0);
                          stock.vbox.getChildren().remove(button);
                          map_card_id.remove(button.title);
                          this.add_to_TilePane_quantity(pane,button.title,button.coverImage,button.quantite,id_card_removed);
                          break outerloop;
                      }
                  }
              }
          }
            event.setDropCompleted(true);
            event.setDropCompleted(true);
              event.consume();
        });
    }
    public void add_to_TilePane_quantity(TilePane pane,String title,Image image,int quantite,int id_card_removed){
        DmItem dm=new DmItem(title,image,1);
        Boolean test=true;
        List<Node> lstnode=pane.getChildren();
        for (int i = 0; i < lstnode.size(); i++)
        {
            DmItem dmitem=(DmItem) lstnode.get(i);
//
            if(dmitem.titleLabel.getText().contains(title)) {

                DmItem dmitem2=new DmItem(title,image,dmitem.quantite+1);

                map_dmitem_id.get(title).add(id_card_removed);
                pane.getChildren().remove(dmitem);
                pane.getChildren().add(i,dmitem2);
                test=false;
                break;
            }
        }
        if(test){
            ArrayList array=new ArrayList();
            array.add(id_card_removed);
            map_dmitem_id.put(title,array);
            pane.getChildren().add(dm);
        }
    }
    public void make_Draggable_to_Vbox(DmstockCard pane){
        pane.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
        });
        pane.setOnDragDropped(event -> {
            DmItem button = (DmItem) event.getGestureSource();
            ImageView imageview=button.coverImageView;
            Card card=new Card(button.titleLabel.getText(),imageview.getImage(),1);
            pane.setHbox1(card);
            DmItem dm= (DmItem) tilepane_chambre.getChildren().get(tilepane_chambre.getChildren().indexOf(button));
            if(dm.quantite>1){
                dm.setQuantite(dm.quantite-1);
                btn_cancel_dm.setVisible(true);
            }
            else{
                tilepane_chambre.getChildren().remove(button);
                btn_cancel_dm.setVisible(true);
            }
            int id_dmitm=map_dmitem_id.get(button.title).remove(0);
            if(!map_card_id.containsKey(button.title)) {
                ArrayList<Integer> array=new ArrayList<>();
                array.add(id_dmitm);
                map_card_id.put(button.title,array);
            }else{
                map_card_id.get(button.title).add(id_dmitm);
            }
           // this.make_drag_detected(hbox1);
            event.setDropCompleted(true);
            event.setDropCompleted(true);
            event.consume();
        });
    }
    @FXML
    public void Onbtn_cancel_dmClick(ActionEvent event) {
        map_card_id.clear();
        map_dmitem_id.clear();
        cbox_service.getSelectionModel().clearSelection();
        cbox_chambre.getSelectionModel().clearSelection();
        cbox_service.getSelectionModel().select(nomService2);
       cbox_chambre.getSelectionModel().select(map_numero_and_type_espace.get(N_chambre_old));

        System.out.println("=================================================");
        System.out.println(N_chambre_old);
        System.out.println(N_chambre_now);
        System.out.println(map_numero_and_type_espace);

        map_numero_and_type_espace.clear();
        btn_cancel_dm.setVisible(false);
    }
    @FXML
    void Onbtn_save_dmClick(ActionEvent event) {
        map_card_id.forEach((key,value)->{
            int numero_stock;
            switch (nomService) {
                case "Cardiologie":
                    numero_stock = 22;
                    break;
                case "Neurologie":
                    numero_stock = 25;
                    break;
                case "Radiologie":
                    numero_stock = 23;
                    break;
                case "Oncologie":
                    numero_stock = 24;
                    break;
                default:
                    numero_stock = 0;
                    break;
            }

            value.forEach(elt->{
                Response getResponse = target
                        .path("dm")
                        .path("affecter_dm")
                        .queryParam("id_dmItem",elt)
                        .queryParam("id_espace",numero_stock)
                        .request(MediaType.APPLICATION_JSON)
                        // .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(test.getBytes()))
                        .put(Entity.json("{}"));

                System.out.println("Http Response   ====> "+getResponse.getStatus());
            });
        });
//
        map_dmitem_id.forEach((key,value)->{

            value.forEach(elt->{

                Response getResponse = target
                        .path("dm")
                        .path("affecter_dm")
                        .queryParam("id_dmItem",elt)
                        .queryParam("id_espace",id_numero_chambre.get(N_chambre_now))
                        .request(MediaType.APPLICATION_JSON)
                        // .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(test.getBytes()))
                        .put(Entity.json("{}"));
                System.out.println("Http Response =====> "+getResponse.getStatus());

            });
        });

        System.out.println(nomService);
//        System.out.println(numero_stock);
        System.out.println(N_chambre_now);
        System.out.println(id_numero_chambre.get(N_chambre_now));
        System.out.println("===========  Map 1 ================ ");
        System.out.println(map_card_id);
        System.out.println("===========  Map 2 ================ ");
        System.out.println(map_dmitem_id);

        Stage primaryStage = (Stage) accrodion_0.getScene().getWindow();
        Stage popupStage=new Stage();
        popupStage.initOwner(primaryStage);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        Text text=new Text();
        text.setText("Save Successefully");
        text.setStyle("-fx-font-size: 18;-fx-font-family: 'Arial Black';-fx-background-color: Black");
        // Create the pop-up window content
        VBox vbox=new VBox();
        vbox.getChildren().add(text);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        // Add the pop-up window content to the Stage
        Scene scene=new Scene(vbox,300,200);
        popupStage.setScene(scene);
        // Show the pop-up window
        popupStage.show();
        popupStage.setOnCloseRequest(even->{
            System.out.println("jamalalsfjoagh =====================  @@@@@@@@@@@@@@");
            Onbtn_cancel_dmClick(new ActionEvent());
        });
    }
    @FXML
    public void On_auto_searchType_KyeBoard(InputMethodEvent event) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@");
        accrodion_0.setVisible(false);
        id_view_scroll.setVisible(true);

        // Create a filtered list and bind it to the searchField text property
        FilteredList<Card> filteredList = new FilteredList<>(data_view);
        id_txt_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(item -> newValue == null || newValue.trim().isEmpty() ||
                    item.title.toLowerCase().contains(newValue.toLowerCase()));
        });
        // Bind the filtered list to the list view
        id_list_view_autos.setItems(filteredList);
    }

//    public void On_auto_searchType_KyeBoard(InputMethodEvent inputMethodEvent) {
//    }

    public static class DmItem extends Pane {
             ImageView coverImageView;
             Label titleLabel;
             Label authorLabel=new Label();
            double mouseX ;
            double mouseY ;
            Stage popupStage;
            String fristname;
            String password;
            String test;

            int quantite;
            String title;
            Image coverImage;
            Text t0;
            int id_dmitem;
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                DmItem dmItem = (DmItem) o;
                return
                        Objects.equals(title, dmItem.title) &&
                        Objects.equals(coverImage.getUrl(), dmItem.coverImage.getUrl());
            }
            public DmItem(String title,Image coverImage,int quantite) {
                this.coverImage=coverImage;
                this.quantite=quantite;
                this.title=title;
        //        this.setBorder(new Border(new BorderStroke(Color.BLACK,
        //                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                coverImageView = new ImageView(coverImage);
                coverImageView.setFitWidth(80);
                coverImageView.setPreserveRatio(true);
                titleLabel = new Label(title);
                titleLabel.setStyle("-fx-font-weight: bold;");
                titleLabel.setAlignment(Pos.CENTER);
              ImageView image=new ImageView(new Image("plus.png"));
              image.setFitWidth(15);
              image.setFitHeight(15);

               //authorLabel.setText(this.quantite+"");
                HBox hbox=new HBox();
                hbox.setSpacing(5);

                t0=new Text(quantite+"");
                t0.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                t0.setFill(Color.GREEN);
                hbox.getChildren().addAll(image,t0);
              authorLabel.setGraphic(hbox);
        //        authorLabel.setG(t0);
               authorLabel.setAlignment(Pos.BOTTOM_RIGHT);
                VBox vb=new VBox();
                vb.setAlignment(Pos.CENTER);
                vb.getChildren().addAll(coverImageView,titleLabel,authorLabel);

                // Add components to the BookCard pane
                getChildren().add(vb);

                //initialize the client to send http request

                // Create context menu with a "Delete" option
                MenuItem deleteItem = new MenuItem("Delete");
                MenuItem deleteItem2 = new MenuItem("Modify");
                MenuItem deleteItem3 = new MenuItem("properties");

                //MenuItem deleteItem4 = new MenuItem("Liberer Lit");
                deleteItem.setStyle("-fx-font-weight: bold;");

                // Delete lit
                deleteItem.setOnAction(event -> {
                });

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
                    popupPane.setPadding(new Insets(10));
                    popupStage.setScene(new Scene(popupPane, 300, 200));

                    // Show the pop-up window
                    popupStage.showAndWait();
                });

                DropShadow shadow = new DropShadow();
                shadow.setColor(Color.BLACK);
                coverImageView.setEffect(shadow);

                ContextMenu contextMenu = new ContextMenu(deleteItem,deleteItem2,deleteItem3);

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
                this.setOnMousePressed(event -> {
                    this.setEffect(null);
                });

                this.setOnMouseReleased(event -> {
                    this.setEffect(null);
                });
                this.make_drag_detected(this);
            }
            public void setQuantite(int quantite) {
                this.quantite = quantite;

                this.t0.setText(quantite+"");;
            }
        public void make_drag_detected(Node pane){
            pane.setOnDragDetected(mouseEvent -> {
                pane.setMouseTransparent(false);
                Dragboard db = pane.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParams = new SnapshotParameters();
                snapshotParams.setFill(Color.TRANSPARENT);
                WritableImage snapshot = pane.snapshot(snapshotParams, null);
                db.setDragView(snapshot);
                ClipboardContent content = new ClipboardContent();
                content.putString("salam");
                db.setContent(content);
            });
        }
    }
    public static class Helpers {
        public Map JsonToMap(String input){

            Map<String, String> map = new HashMap<>();

    // remove the curly braces from the input string
            String cleanedInput = input.substring(1, input.length() - 1);

    // split the string into key-value pairs
            String[] pairs = cleanedInput.split(", ");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                String key = keyValue[0];
                String value = keyValue[1];

                // remove the quotes from the value string
                value = value.substring(1, value.length() - 1);

                map.put(key, value);
            }
            return map;

        }
        public Map<JSONObject, List<JSONObject>>  MapString_toJson(Map<String, List<String>> map_in){

            Map<JSONObject, List<JSONObject>> map_out=new HashMap<>();

            map_in.entrySet().forEach(elt->{

                List<JSONObject> lstmap=new ArrayList<>();

                elt.getValue().forEach(elt0->{

                    //JSONObject js0=

                    lstmap.add(new JSONObject(elt0));

                });
                map_out.put(new JSONObject(elt.getKey()),lstmap);
            });
            return map_out;
        }
    }
    static class Card extends HBox{
        Image coverImage;
        int quantite;
        String title;
        Text t0=new Text();
        Label label2;
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Card dmItem = (Card) o;
            return
                    Objects.equals(title, dmItem.title) &&
                            Objects.equals(coverImage.getUrl(), dmItem.coverImage.getUrl());
        }

        public Card(String title, Image coverImage, int quantite) {
            this.quantite = quantite;
            this.title = title;
            this.coverImage = coverImage;
            ImageView imageplus = new ImageView(new Image("plus.png"));
            imageplus.setFitWidth(13);
            imageplus.setFitHeight(13);
            t0.setText(this.quantite + "");
            t0.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            t0.setFill(Color.GREEN);

            HBox hbox_plus = new HBox();
            hbox_plus.setAlignment(Pos.CENTER);
            hbox_plus.getChildren().addAll(imageplus, t0);
            label2= new Label();
            label2.setGraphic(hbox_plus);
            Label label = new Label();
            label.setText(title);
            ImageView image_view = new ImageView();
            image_view.setImage(coverImage);
            image_view.setFitHeight(30);
            image_view.setFitWidth(30);
            this.setSpacing(10);
            this.getChildren().addAll(image_view, label, label2);
        }
        public void setQuantite(int quantite){
            this.quantite=this.quantite+quantite;
            ImageView imageplus = new ImageView(new Image("plus.png"));
            imageplus.setFitWidth(13);
            imageplus.setFitHeight(13);

            t0.setText(this.quantite + "");
            t0.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            t0.setFill(Color.GREEN);
            HBox hbox_plus = new HBox();
            hbox_plus.setAlignment(Pos.CENTER);
            hbox_plus.getChildren().addAll(imageplus, t0);
            label2.setGraphic(hbox_plus);
        }
        public void setQuantite_moins(){
            this.quantite=this.quantite-1;
            ImageView imageplus = new ImageView(new Image("plus.png"));
            imageplus.setFitWidth(13);
            imageplus.setFitHeight(13);
            t0.setText(this.quantite + "");
            t0.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            t0.setFill(Color.GREEN);
            HBox hbox_plus = new HBox();
            hbox_plus.setAlignment(Pos.CENTER);
            hbox_plus.getChildren().addAll(imageplus, t0);
            label2.setGraphic(hbox_plus);
        }
    }
    static class DmstockCard extends ScrollPane {
        VBox vbox;
        public DmstockCard() {
           vbox=new VBox();
           this.setContent(vbox);
        }
        public void setHbox1(Card card) {
            this.make_drag_detected(card);
            this.check_increment_qantite(card);
        }
        public void make_drag_detected(Node pane){
            pane.setOnDragDetected(mouseEvent -> {
                pane.setMouseTransparent(false);
                Dragboard db = pane.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParams = new SnapshotParameters();
                snapshotParams.setFill(Color.TRANSPARENT);
                WritableImage snapshot = pane.snapshot(snapshotParams, null);
                db.setDragView(snapshot);
                ClipboardContent content = new ClipboardContent();
                content.putString("salam");
                db.setContent(content);
            });
        }
        public void check_increment_qantite(Card card){
            if(vbox.getChildren().contains(card)){
                Card card1= (Card) vbox.getChildren().get(vbox.getChildren().indexOf(card));
                card1.setQuantite(card.quantite);
            }
            else{
                vbox.getChildren().add(card);
            }
        }

    }
}








