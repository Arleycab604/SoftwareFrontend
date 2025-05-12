package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class CorreoRecoveryController {

    @FXML
    private TextField usuarioField;

    @FXML
    private void onEnviarClick() {
        String usuario = usuarioField.getText().trim();
        if (usuario.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campo vacío", "Debes ingresar un nombre de usuario.");
            return;
        }

        try {
            Map<String, String> params = new HashMap<>();
            params.put("nombreUsuario", usuario);

            HttpResponse<String> response = BuildRequest.getInstance()
                    .GETParams("http://localhost:8080/SaberPro/usuarios/recoverPassword", params,false);

            if (response.statusCode() == 200) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Se envió el código. Revisa tu correo.");

                // Cargar vista recovery-view.fxml y pasar usuario
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/recovery-view.fxml"));
                Parent root = loader.load();

                RecoveryController controller = loader.getController();
                controller.setNombreUsuario(usuario);

                Stage stage = (Stage) usuarioField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se encontró el usuario o hubo un error.");
            }

        } catch (IOException | InterruptedException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo contactar con el servidor.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onLoginClick() {
        Stage stage = (Stage) usuarioField.getScene().getWindow();
        ViewLoader.loadView("Login-view.fxml", stage);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
