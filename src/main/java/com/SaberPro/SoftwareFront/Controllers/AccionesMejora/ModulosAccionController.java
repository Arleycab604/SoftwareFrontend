package com.SaberPro.SoftwareFront.Controllers.AccionesMejora;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

public class ModulosAccionController {

    @FXML
    private void handleModuloClick(MouseEvent event) {
        // Obtiene el HBox que fue clickeado
        HBox clickedBox = (HBox) event.getSource();
        // Obtiene el 'userData' para identificar qué módulo fue clickeado
        String moduloId = (String) clickedBox.getUserData();

        System.out.println("Módulo clickeado: " + moduloId); // Para depuración

        try {
            // Carga el FXML de la página de detalles
            // CAMBIO AQUI: La ruta debe ser absoluta desde la raíz del classpath
            // Asumiendo que DetallesAccion.fxml está en src/main/resources/com/SaberPro/SoftwareFront/AccionesMejora/DetallesAccion.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/AccionesMejora/DetallesAccion.fxml"));
            Parent root = loader.load();

            // Puedes pasar información al controlador de detalles si lo necesitas
            // Es buena práctica obtener el controlador DESPUÉS de loader.load()
            // DetallesAccionController detallesController = loader.getController();
            // detallesController.setModulo(moduloId); // Ejemplo: Pasar el ID del módulo

            // Obtiene el Stage actual y cambia la escena
            Stage stage = (Stage) clickedBox.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Manejar el error de carga del FXML
            System.err.println("Error al cargar la página de detalles: " + e.getMessage());
            // Opcional: Mostrar una alerta al usuario
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Navegación");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo cargar la página de detalles del módulo. Por favor, verifica la ruta del FXML.");
            alert.showAndWait();
        }
    }
}