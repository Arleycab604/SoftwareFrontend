package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;


public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label mensajeError;

    @FXML
    private void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = BuildRequest.getInstance().POSTJson(
                    "http://localhost:8080/SaberPro/usuarios/login",
                     String.format("{\"nombreUsuario\":\"%s\", \"password\":\"%s\"}", username, password),false);

            // Manejar la respuesta
            int responseCode = response.statusCode();
            System.out.println("Código de respuesta: " + responseCode);

            if (responseCode == 200) {
                String responseBody = response.body();
                System.out.println("Respuesta del servidor: " + responseBody);

                // Procesar el token (si es necesario)
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue(responseBody, Map.class);
                String token = (String) jsonMap.get("token");

                if (token != null && !token.equals("Credenciales inválidas.")) {
                    // Decodificar el payload del token

                    // Ocultar mensaje de error si estaba visible
                    mensajeError.setVisible(false);
                    String[] parts = token.split("\\.");
                    String payload = new String(Base64.getDecoder().decode(parts[1]));
                    Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
                    String tipoUsuario = (String) payloadMap.get("tipoDeUsuario");

                    // Guardar el token y el tipo de usuario
                    TokenManager.setToken(token);
                    TokenManager.setTipoUsuario(tipoUsuario);

                    // Cargar el Dashboard
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    ViewLoader.loadView("Dashboard-view.fxml", stage);
                } else {
                    mensajeError.setText("Usuario o contraseña incorrectos.");
                    mensajeError.setVisible(true);
                    System.out.println("Usuario o contraseña incorrectos.");
                }
            } else {
                System.out.println("Error en la solicitud: " + responseCode + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al conectar con el backend.");
        }
    }


    @FXML
    private void initialize() {
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onLoginButtonClick();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onLoginButtonClick();
            }
        });
    }


    @FXML
    private void onForgotPasswordClick() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        ViewLoader.loadView("CorreoRecovery-view.fxml", stage);
    }

}