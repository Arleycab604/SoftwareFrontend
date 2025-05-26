package com.SaberPro.SoftwareFront.Controllers.AccionesMejora;

import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

public class ModulosAccController {
    @FXML
    private void handleModuloClick(MouseEvent event) {
        if (event.getSource() instanceof HBox) {
            HBox clickedBox = (HBox) event.getSource();
            String moduloId = (String) clickedBox.getUserData();

            if (moduloId == null) {
                System.err.println("El HBox no tiene un userData configurado.");
                return;
            }

            System.out.println("Módulo clickeado: " + moduloId);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/Acciones/Detalles/DetallesAccion.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) clickedBox.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlertaError("Error al cargar la página de detalles del módulo. Por favor, verifica la ruta del FXML.");
            }
        } else {
            System.err.println("El evento no proviene de un HBox.");
        }
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Navegación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void abrirDetalle(PropuestaMejoraDTO propuesta) {
        // Implementar lógica si es necesario
    }
}