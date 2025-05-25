package com.SaberPro.SoftwareFront.Controllers.AccionesMejora;

import com.SaberPro.SoftwareFront.Models.EntregaAccion; // Necesitas el modelo de Entrega
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import javafx.stage.Stage; // Para cerrar la ventana si se abre como modal

public class CalificarAccionController {

    @FXML private Label nombreEntregaLabel;
    @FXML private Label estadoEntregaLabel;
    @FXML private Label tiempoRestanteEntregaLabel;
    @FXML private Hyperlink nombreArchivoHyperlink;
    @FXML private Label fechaEnvioArchivoLabel;
    @FXML private Label estadoRetroalimentacionLabel;
    @FXML private TextArea retroalimentacionTextArea;

    private EntregaAccion entregaActual; // Para almacenar la entrega que se está calificando

    // Formateador de fecha y hora
    private static final DateTimeFormatter FORMATTER_FECHA_HORA = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, HH:mm");


    public void setEntrega(EntregaAccion entrega) {
        this.entregaActual = entrega;
        if (entrega != null) {
            nombreEntregaLabel.setText(entrega.getNombreUsuario() + " " + entrega.getDireccionCorreo() + ", " + entrega.getCiudad());
            estadoEntregaLabel.setText(entrega.getEstado());

            // Aquí debes calcular y establecer el tiempo restante o el mensaje de envío
            // Por ejemplo:
            // if (entrega.getTiempoRestante() != null) {
            //     tiempoRestanteEntregaLabel.setText(entrega.getTiempoRestante());
            // } else {
            tiempoRestanteEntregaLabel.setText("La tarea fue enviada 2 horas 19 minutos antes de la fecha límite"); // Dato fijo del ejemplo
            // }


            // Asumo que EntregaAccion tiene un getNombreArchivo() y un getFechaEnvioArchivo()
            if (entrega.getNombreArchivo() != null) {
                nombreArchivoHyperlink.setText(entrega.getNombreArchivo());
                fechaEnvioArchivoLabel.setText(entrega.getFechaEnvioArchivo() != null ? entrega.getFechaEnvioArchivo().format(FORMATTER_FECHA_HORA) : "No definida");
            } else {
                nombreArchivoHyperlink.setText("No hay archivo");
                fechaEnvioArchivoLabel.setText("");
            }


            // Cargar calificación y retroalimentación existentes si la entrega ya fue calificada
            // if (entrega.getCalificacion() != null) {
            //     calificacionTextField.setText(String.valueOf(entrega.getCalificacion()));
            //     estadoRetroalimentacionLabel.setText("Calificado");
            // } else {
            estadoRetroalimentacionLabel.setText("Sin calificar");
            // }
            // retroalimentacionTextArea.setText(entrega.getRetroalimentacion() != null ? entrega.getRetroalimentacion() : "");
        }
    }

    @FXML
    private void handleAtras() {
        // Lógica para cerrar esta ventana. Si se abrió como una ventana modal, esto la cerraría.
        Stage stage = (Stage) nombreEntregaLabel.getScene().getWindow();
        stage.close();
        System.out.println("Cerrando ventana de calificación.");
    }

    @FXML
    private void handleVerArchivo() {
        // Lógica para abrir el archivo (por ejemplo, el PDF)
        // Esto podría implicar llamar a un servicio externo o un componente de visualización de PDF
        System.out.println("Abriendo archivo: " + nombreArchivoHyperlink.getText());
        // Implementar la apertura del archivo (ej. usando Desktop.open() si es un archivo local)
    }

    @FXML
    private void handleGuardarCambios() {
        if (entregaActual != null) {
            try {
                String retroalimentacion = retroalimentacionTextArea.getText();

                // Aquí actualizarías el objeto 'entregaActual' y luego lo guardarías
                // en tu base de datos o sistema de persistencia.
                // entregaActual.setCalificacion(calificacion);
                // entregaActual.setRetroalimentacion(retroalimentacion);
                // entregaActual.setEstado("Calificado"); // Actualizar el estado si es necesario

                // Una vez guardado, podrías cerrar la ventana o notificar al controlador padre para refrescar la tabla.
                handleAtras();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de entrada");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, introduce una calificación numérica válida.");
                alert.showAndWait();
            }
        } else {
            System.err.println("No hay entrega seleccionada para guardar.");
        }
    }

    @FXML
    private void handleGuardarYSiguiente() {
        // Lógica para guardar la calificación actual y cargar la siguiente entrega para calificar
        handleGuardarCambios(); // Guarda la actual
        // Luego, notifica al controlador padre (VerEnvioAccionController) para cargar la siguiente entrega
        // Esto requeriría una referencia o un callback al controlador padre
        System.out.println("Guardando y cargando siguiente entrega...");
    }

    @FXML
    private void handleReiniciar() {
        retroalimentacionTextArea.clear();
        estadoRetroalimentacionLabel.setText("Faltante"); // Resetear estado si se reinicia
        System.out.println("Campos de calificación reiniciados.");
    }
}
