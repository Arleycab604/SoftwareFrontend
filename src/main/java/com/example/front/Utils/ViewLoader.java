package com.example.front.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewLoader {

    public static void loadView(String fxmlPath, Stage currentStage, int width, int height) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            currentStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}