package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


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

            Stage stage = (Stage) usernameField.getScene().getWindow();
            ViewLoader.loadView("Dashboard-view.fxml", stage, 450, 480);

        } else {
            System.out.println("Usuario o contraseña incorrectos.");
        }

    }
    @FXML
    private void onForgotPasswordClick() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        ViewLoader.loadView("Recovery-view.fxml", stage, 450, 480);
    }
}