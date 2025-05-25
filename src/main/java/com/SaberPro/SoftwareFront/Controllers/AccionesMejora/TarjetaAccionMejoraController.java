package com.SaberPro.SoftwareFront.Controllers.AccionesMejora;

import com.SaberPro.SoftwareFront.Models.AccionMejora; // Importa tu modelo
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;
import java.util.Locale; // Necesario para el formato de fecha

public class TarjetaAccionMejoraController {

    @FXML private Label nombreTareaLabel;
    @FXML private Label fechaAperturaLabel;
    @FXML private Label fechaCierreLabel;
    @FXML private Label fechaLimiteLabel;
    @FXML private Label recordarCalificarLabel;
    @FXML private Label descripcionLabel;

    private AccionMejora accionMejora;
    private Runnable onEditCallback; // Callback para el botón de editar

    // Formateador de fecha y hora
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));

    public void setAccionMejora(AccionMejora accion) {
        this.accionMejora = accion;
        if (accion != null) {
            // Actualizar el título de la tarea para que diga "ACCIÓN DE MEJORA: [Nombre de la tarea]"
            nombreTareaLabel.setText("ACCIÓN DE MEJORA: " + accion.getNombreTarea());
            descripcionLabel.setText(accion.getDescripcionGeneral());

            // Rellenar las fechas, usando el formato de la imagen de ejemplo
            fechaAperturaLabel.setText(accion.getPermitirEntregasDesde() != null ? accion.getPermitirEntregasDesde().format(FORMATTER) : "No definida");
            fechaCierreLabel.setText(accion.getFechaEntregaLimite() != null ? accion.getFechaEntregaLimite().format(FORMATTER) : "No definida");
            fechaLimiteLabel.setText(accion.getFechaLimiteFinal() != null ? accion.getFechaLimiteFinal().format(FORMATTER) : "No definida");
            recordarCalificarLabel.setText(accion.getRecordarCalificarEn() != null ? accion.getRecordarCalificarEn().format(FORMATTER) : "No definida");
        }
    }

    public void setOnEditCallback(Runnable callback) {
        this.onEditCallback = callback;
    }

    @FXML
    private void handleEditarAccion() {
        if (onEditCallback != null) {
            onEditCallback.run(); // Ejecutar el callback definido en el controlador padre
        }
    }
}