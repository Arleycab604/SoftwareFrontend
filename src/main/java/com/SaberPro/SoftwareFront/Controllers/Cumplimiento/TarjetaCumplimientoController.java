package com.SaberPro.SoftwareFront.Controllers.Cumplimiento;

import com.SaberPro.SoftwareFront.Models.AccionMejora;
import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale; // Necesario para el formato de fecha

public class TarjetaCumplimientoController {

    @FXML
    private Label nombreTareaLabel;
    @FXML private Label fechaAperturaLabel;
    @FXML private Label fechaCierreLabel;
    @FXML private Label fechaLimiteLabel;
    @FXML private Label descripcionLabel;

    private PropuestaMejoraDTO accionMejora;
    private Runnable onEditCallback; // Callback para el botón de editar

    // Formateador de fecha y hora
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));

    public void setAccionMejora(PropuestaMejoraDTO accion) {
        this.accionMejora = accion;
        if (accion != null) {
            // Actualizar el título de la tarea para que diga "ACCIÓN DE MEJORA: [Nombre de la tarea]"
            nombreTareaLabel.setText("ACCIÓN DE MEJORA: " + accion.getNombrePropuesta());
            descripcionLabel.setText(accion.getDescripcion());

            // Rellenar las fechas, usando el formato de la imagen de ejemplo
            fechaAperturaLabel.setText(accion.getFechaCreacion() != null ? accion.getFechaCreacion().format(FORMATTER) : "No definida");
            fechaCierreLabel.setText(accion.getFechaLimiteEntrega() != null ? accion.getFechaLimiteEntrega().format(FORMATTER) : "No definida");
            }
    }

    public void setOnEditCallback(Runnable callback) {
        this.onEditCallback = callback;
    }

    @FXML
    private void handleEditarAccion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/FXML/Cumplimiento/VerTarjetaCumplimiento.fxml"));
            Parent root = loader.load();

            VerTarjetaCumplimientoController controller = loader.getController();
            controller.setPropuestaMejora(accionMejora); // Pasamos el DTO

            Stage stage = new Stage();
            stage.setTitle("Detalle de Propuesta de Mejora");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}