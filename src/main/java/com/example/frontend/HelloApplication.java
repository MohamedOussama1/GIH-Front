package com.example.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("GIH.fxml"));
        Pane pane=fxmlLoader.load();
//        pane.setMinHeight(800);
//        pane.setPrefSize(100,100);
        Scene scene = new Scene(pane);

        scene.getStylesheets().add("stylesheet.css");
        stage.setTitle("Hello!");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText(null);
        alert.setContentText("You will close the application");
        stage.setOnCloseRequest(e -> {
            Optional<ButtonType> result = alert.showAndWait();
            e.consume();
            if (result.get() == ButtonType.OK){
                stage.close();
            }
        });
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setMaximized(false);
//        stage.setMinHeight(800);
//        stage.setMinWidth(800);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}