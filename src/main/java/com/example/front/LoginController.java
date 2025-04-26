package com.example.front;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();


        if ("admin".equals(username) && "1234".equals(password)) {
            System.out.println("Inicio de sesión exitoso.");

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dashboard-view.fxml"));
                Scene DashboardScene = new Scene(fxmlLoader.load(), 450, 480);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(DashboardScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Usuario o contraseña incorrectos.");
        }

    }
    @FXML
    private void onForgotPasswordClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Recovery-view.fxml"));
            Scene DashboardScene = new Scene(fxmlLoader.load(), 450, 480);

            // Obtener la ventana (Stage) actual desde alguno de los nodos del login-view
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(DashboardScene); // Cambiar la escena a la nueva
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}