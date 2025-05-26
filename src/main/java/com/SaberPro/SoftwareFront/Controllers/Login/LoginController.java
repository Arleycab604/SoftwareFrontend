package com.SaberPro.SoftwareFront.Controllers.Login;

import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox; // Mantener si 'contenedor' es una referencia al VBox principal del login
import javafx.stage.Stage;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;


public class LoginController {

    public VBox contenedor; // Mantener si es el contenedor raíz de Login-view.fxml
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

                // Procesar el token
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue(responseBody, Map.class);
                String token = (String) jsonMap.get("token");

                if (token != null && !token.equals("Credenciales inválidas.")) {
                    // Ocultar mensaje de error si estaba visible
                    mensajeError.setVisible(false);

                    // Decodificar el payload del token y guardar el tipo de usuario
                    String[] parts = token.split("\\.");
                    String payload = new String(Base64.getDecoder().decode(parts[1]));
                    Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
                    String tipoUsuario = (String) payloadMap.get("tipoDeUsuario");

                    TokenManager.setToken(token);
                    TokenManager.setTipoUsuario(tipoUsuario);

                    // Cargar el Dashboard
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    // Asegúrate de que esta ruta al Dashboard-view.fxml sea la correcta y absoluta.
                    ViewLoader.loadView("/com/SaberPro/SoftwareFront/Dashboard-view.fxml", stage); // Asumiendo que ahora se llama Dashboard.fxml
                } else {
                    mensajeError.setText("Usuario o contraseña incorrectos.");
                    mensajeError.setVisible(true);
                    System.out.println("Usuario o contraseña incorrectos.");
                }
            } else {
                mensajeError.setText("Error al iniciar sesión. Código: " + responseCode); // Mensaje más descriptivo para el usuario
                mensajeError.setVisible(true);
                System.out.println("Error en la solicitud: " + responseCode + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensajeError.setText("Error de conexión. Intente más tarde."); // Mensaje para el usuario
            mensajeError.setVisible(true);
            System.out.println("Error al conectar con el backend.");
        }
    }

    @FXML
    private void initialize() {

        // Configuración de eventos de teclado (mantener)
        if (usernameField != null) { // Agregado null check para seguridad
            usernameField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    onLoginButtonClick();
                }
            });
        }

        if (passwordField != null) { // Agregado null check para seguridad
            passwordField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    onLoginButtonClick();
                }
            });
        }
    }

    @FXML
    private void onForgotPasswordClick() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        ViewLoader.loadView("/com/SaberPro/SoftwareFront/Login/CorreoRecovery-view.fxml", stage);
    }
}