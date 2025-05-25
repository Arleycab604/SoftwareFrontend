package com.SaberPro.SoftwareFront.Controllers.Login;

import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class RecoveryController {

    @FXML
    private TextField codeField;
    @Setter
    private String nombreUsuario;

    @FXML
    private void onNextButtonClick() {
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Usuario no definido.");
            return;
        }

        try {
            Map<String,String> params = new HashMap<>();
            params.put("nombreUsuario", nombreUsuario);
            params.put("codigo", codeField.getText());
            HttpResponse<String> response = BuildRequest.getInstance()
                    .GETParams("http://localhost:8080/SaberPro/usuarios/verifyCode", params,false);

            if (response.statusCode() == 200) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/Login/Restore-view.fxml"));
                Parent root = loader.load();

                RestoreController controller = loader.getController();
                controller.setNombreUsuario(nombreUsuario); // nombreUsuario ya es un String

                Stage stage = (Stage) codeField.getScene().getWindow(); // Usa cualquier nodo que esté en la escena actual
                stage.setScene(new Scene(root));
                stage.show();

            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Código inválido", response.body());
            }

        } catch (IOException | InterruptedException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo contactar con el servidor.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onReenviarClick() {
        // Puedes implementar lógica para reenviar desde el backend
        mostrarAlerta(Alert.AlertType.INFORMATION, "Reenvío", "Código reenviado (simulado).");
    }

    @FXML
    private void onAtrasClick() {
        Stage stage = (Stage) codeField.getScene().getWindow();
        ViewLoader.loadView("Login-view.fxml", stage);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
