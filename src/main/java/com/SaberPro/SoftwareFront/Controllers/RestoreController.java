package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.ResourceBundle;

public class RestoreController implements Initializable {

    // Campo 1
    @FXML private PasswordField r1_passwordField;
    @FXML private TextField r1_textFieldVisible;
    @FXML private Button togglePassword1;

    // Campo 2
    @FXML private PasswordField r2_passwordField;
    @FXML private TextField r2_textFieldVisible;
    @FXML private Button togglePassword2;

    @FXML private ImageView iconoOjo1;
    @FXML private ImageView iconoOjo2;

    private boolean mostrando1 = false;
    private boolean mostrando2 = false;

    @Setter
    private String nombreUsuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iconoOjo1.setImage(new Image(Objects.requireNonNull(getClass().getResource("/Images/VistaC.png")).toExternalForm()));
        iconoOjo2.setImage(new Image(Objects.requireNonNull(getClass().getResource("/Images/VistaC.png")).toExternalForm()));
    }

    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        Object source = event.getSource();

        if (source == togglePassword1) {
            if (mostrando1) {
                r1_passwordField.setText(r1_textFieldVisible.getText());
                r1_passwordField.setVisible(true);
                r1_passwordField.setManaged(true);
                r1_textFieldVisible.setVisible(false);
                r1_textFieldVisible.setManaged(false);
                iconoOjo1.setImage(new Image(Objects.requireNonNull(getClass().getResource("/Images/VistaC.png")).toExternalForm()));
                mostrando1 = false;
            } else {
                r1_textFieldVisible.setText(r1_passwordField.getText());
                r1_passwordField.setVisible(false);
                r1_passwordField.setManaged(false);
                r1_textFieldVisible.setVisible(true);
                r1_textFieldVisible.setManaged(true);
                iconoOjo1.setImage(new Image(Objects.requireNonNull(getClass().getResource("/Images/VistaA.png")).toExternalForm()));
                mostrando1 = true;
            }
        } else if (source == togglePassword2) {
            if (mostrando2) {
                r2_passwordField.setText(r2_textFieldVisible.getText());
                r2_passwordField.setVisible(true);
                r2_passwordField.setManaged(true);
                r2_textFieldVisible.setVisible(false);
                r2_textFieldVisible.setManaged(false);
                iconoOjo2.setImage(new Image(Objects.requireNonNull(getClass().getResource("/Images/VistaC.png")).toExternalForm()));
                mostrando2 = false;
            } else {
                r2_textFieldVisible.setText(r2_passwordField.getText());
                r2_passwordField.setVisible(false);
                r2_passwordField.setManaged(false);
                r2_textFieldVisible.setVisible(true);
                r2_textFieldVisible.setManaged(true);
                iconoOjo2.setImage(new Image(Objects.requireNonNull(getClass().getResource("/Images/VistaA.png")).toExternalForm()));
                mostrando2 = true;
            }
        }
    }

    @FXML
    private void onRestore_passwordButtonClick() {
        String pass1 = mostrando1 ? r1_textFieldVisible.getText() : r1_passwordField.getText();
        String pass2 = mostrando2 ? r2_textFieldVisible.getText() : r2_passwordField.getText();

        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Usuario no definido.");
            return;
        }

        if (pass1.isEmpty() || pass2.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, completa ambos campos.");
            return;
        }

        if (!pass1.equals(pass2)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Contraseñas no coinciden", "Asegúrate de que ambas contraseñas sean iguales.");
            return;
        }

        try {
            String jsonBody = "{\"nombreUsuario\":\"" + nombreUsuario + "\", \"nuevaContrasena\":\"" + pass1 + "\"}";

            HttpResponse<String> response = BuildRequest.getInstance()
                    .POSTJson("http://localhost:8080/SaberPro/usuarios/changePassword", jsonBody,false);

            if (response.statusCode() == 200) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Contraseña actualizada", "La contraseña se cambió correctamente.");
                Stage stage = (Stage) r1_passwordField.getScene().getWindow();
                ViewLoader.loadView("login-view.fxml", stage);
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", response.body());
            }

        } catch (IOException | InterruptedException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de conexión", "No se pudo contactar con el servidor.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackButtonClick() {
        Stage stage = (Stage) r1_passwordField.getScene().getWindow();
        ViewLoader.loadView("Recovery-view.fxml", stage);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
