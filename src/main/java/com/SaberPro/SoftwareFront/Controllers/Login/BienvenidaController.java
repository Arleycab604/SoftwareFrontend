package com.SaberPro.SoftwareFront.Controllers.Login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.VBox; // Si tu contenedor es un VBox

public class BienvenidaController {

    @FXML
    private Label bienvenidaLabel; // Asegúrate de que el fx:id en el FXML coincida

    @FXML
    private VBox contenedor; // Si necesitas referenciar el contenedor principal para algo como cerrar la ventana

    @FXML
    public void initialize() {
        bienvenidaLabel.setText("¡Bienvenido a SaberPro!");
    }

    @FXML
    private void onSalirClick() {
        if (contenedor != null) {
            Stage stage = (Stage) contenedor.getScene().getWindow();
            stage.close();
        }
    }
}