package com.SaberPro.SoftwareFront;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginApplication extends Application {
    @Override

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("Login/Login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // Ajusta el tamaño de la primera ventana
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(); // Inicia la aplicación
    }
}