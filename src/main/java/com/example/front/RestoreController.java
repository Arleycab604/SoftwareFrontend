package com.example.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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

    // Iconos del botón ojo
    @FXML private ImageView iconoOjo1;
    @FXML private ImageView iconoOjo2;

    // Estado de visibilidad
    private boolean mostrando1 = false;
    private boolean mostrando2 = false;

    // Cargar imágenes al iniciar
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iconoOjo1.setImage(new Image(getClass().getResource("/Images/VistaC.png").toExternalForm()));
        iconoOjo2.setImage(new Image(getClass().getResource("/Images/VistaC.png").toExternalForm()));
    }

    // Alternar visibilidad de los campos
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

                iconoOjo1.setImage(new Image(getClass().getResource("/Images/VistaC.png").toExternalForm()));
                mostrando1 = false;
            } else {
                r1_textFieldVisible.setText(r1_passwordField.getText());
                r1_passwordField.setVisible(false);
                r1_passwordField.setManaged(false);

                r1_textFieldVisible.setVisible(true);
                r1_textFieldVisible.setManaged(true);

                iconoOjo1.setImage(new Image(getClass().getResource("/Images/VistaA.png").toExternalForm()));
                mostrando1 = true;
            }
        } else if (source == togglePassword2) {
            if (mostrando2) {
                r2_passwordField.setText(r2_textFieldVisible.getText());
                r2_passwordField.setVisible(true);
                r2_passwordField.setManaged(true);

                r2_textFieldVisible.setVisible(false);
                r2_textFieldVisible.setManaged(false);

                iconoOjo2.setImage(new Image(getClass().getResource("/Images/VistaC.png").toExternalForm()));
                mostrando2 = false;
            } else {
                r2_textFieldVisible.setText(r2_passwordField.getText());
                r2_passwordField.setVisible(false);
                r2_passwordField.setManaged(false);

                r2_textFieldVisible.setVisible(true);
                r2_textFieldVisible.setManaged(true);

                iconoOjo2.setImage(new Image(getClass().getResource("/Images/VistaA.png").toExternalForm()));
                mostrando2 = true;
            }
        }
    }

    // Acción del botón para cambiar contraseña
    @FXML
    private void onRestore_passwordButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene RolScene = new Scene(fxmlLoader.load(), 450, 480);
            Stage stage = (Stage) r1_passwordField.getScene().getWindow();
            stage.setScene(RolScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Acción del botón atrás
    @FXML
    private void onBackButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Recovery-view.fxml"));
            Scene RecoveryScene = new Scene(fxmlLoader.load(), 450, 480);
            Stage stage = (Stage) r1_passwordField.getScene().getWindow();
            stage.setScene(RecoveryScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

