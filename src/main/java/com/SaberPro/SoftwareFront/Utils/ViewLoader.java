package com.SaberPro.SoftwareFront.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewLoader {

    public static void loadView(String fxmlPath, Stage currentStage, int width, int height) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ViewLoader.class.getResource("/com/SaberPro/SoftwareFront/" + fxmlPath));
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadView(String fxmlPath, Stage currentStage) {
        loadView(fxmlPath, currentStage, ViewConstants.DEFAULT_WIDTH, ViewConstants.DEFAULT_HEIGHT);
    }
}
