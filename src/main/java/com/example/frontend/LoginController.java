package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
     Button btnlogin;

    @FXML
     TextField txtfirstname;

    @FXML
     PasswordField txtpassword;


    String fristname;
    String password;

    Response getResponse;

    @FXML
    void OnLoginClick(ActionEvent event) throws IOException {

         fristname=txtfirstname.getText().trim();
//        fristname=txtfirstname.getText().trim();
         password=txtpassword.getText().trim();
        FXMLLoader fxmlLoader2=new FXMLLoader(getClass().getResource("GIH.fxml"));
        AnchorPane ppane=fxmlLoader2.load();
        HelloController hello=fxmlLoader2.getController();


//        hello.userName_Login=fristname;
//        hello.password_Login=password;
//
//        System.out.println("@@@@@@@!!!!!!!!###### "+hello.userName_Login);

         String test=fristname+":"+password;

        Client client = ClientBuilder.newClient();

        WebTarget target = client.target("http://localhost:8081");

         getResponse = target
                .path("login/all")
                .request()
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(test.getBytes()))
                .get();

        if(getResponse.getStatus() == 200){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Valid Account");
            alert.setHeaderText(null);
            alert.setContentText("Your account is valid.");

            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(okButton);



            Stage current=(Stage)btnlogin.getScene().getWindow();

            alert.initOwner(current);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == okButton) {
                // Perform action when "OK" button is clicked
                System.out.println("OK button clicked");
                current.close();
            }


        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("InValid Account");
            alert.setHeaderText(null);
            alert.setContentText("Your account is InValid.");

            Stage current=(Stage)btnlogin.getScene().getWindow();

            alert.initOwner(current);
            alert.initModality(Modality.APPLICATION_MODAL);

            alert.showAndWait();
//
        }



    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Stage stageChild=(Stage) btnlogin.getScene().getWindow();
//        stageChild.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent windowEvent) {
//                if(getResponse.getStatus() == 200){
//                    stageChild.close();
//                }
//                HelloController hello=new HelloController();
//                Stage state=(Stage)hello.buttonLogin.getScene().getWindow();
//                stageChild.close();
//                state.close();
//
//            }
//        });
    }
}
