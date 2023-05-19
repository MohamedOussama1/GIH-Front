package com.example.frontend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainTest extends Application {
    public static void main(String[] args) throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] commands = {"python", "mohamedOussama.py"};
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

// Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s;
        StringBuffer stringBuffer = new StringBuffer();
        while ((s = stdInput.readLine()) != null) {
            stringBuffer.append(s);
            stringBuffer.append("\n");
        }

// Read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }
        System.out.println(stringBuffer);
        Platform.runLater(() -> launch());
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        ImageView imageView = new ImageView(new Image("secondTry.png"));
        pane.getChildren().add(imageView);
        Scene scene = new Scene(pane,600, 800);
        stage.setScene(scene);
        stage.show();
    }
}
