package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;
import java.util.Scanner;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            // Conectar al backend
            URL url = new URL("http://localhost:8080/api/usuario/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Crear el JSON con las credenciales
            String jsonInput = String.format("{\"nombreUsuario\":\"%s\", \"password\":\"%s\"}", username, password);

            // Enviar la solicitud
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            // Leer la respuesta
            if (connection.getResponseCode() == 200) {
                Scanner scanner = new Scanner(connection.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                // Procesar el token
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue(response, Map.class);
                String token = (String) jsonMap.get("token");

                if (token != null && !token.equals("Credenciales inválidas.")) {
                    // Decodificar el payload del token
                    String[] parts = token.split("\\.");
                    String payload = new String(Base64.getDecoder().decode(parts[1]));
                    Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
                    String tipoUsuario = (String) payloadMap.get("tipoDeUsuario");

                    // Guardar el token y el tipo de usuario
                    TokenManager.setToken(token);
                    TokenManager.setTipoUsuario(tipoUsuario);

                    // Cargar el Dashboard
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    ViewLoader.loadView("Dashboard-view.fxml", stage, 450, 480);
                } else {
                    System.out.println("Usuario o contraseña incorrectos.");
                }
            } else {
                System.out.println("Error en la solicitud: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al conectar con el backend.");
        }
    }

    @FXML
    private void onForgotPasswordClick() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        ViewLoader.loadView("Recovery-view.fxml", stage, 450, 480);
    }
}