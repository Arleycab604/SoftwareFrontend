package com.SaberPro.SoftwareFront.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class CReporteController {

    // Constantes para mensajes estáticos
    private static final String MSG_ARCHIVOS_SELECCIONADOS = "Archivo(s) seleccionados correctamente. Total: ";
    private static final String MSG_NO_SELECCIONADOS = "No se seleccionaron archivos. Intenta nuevamente.";
    private static final String MSG_DATOS_SUBIDOS = "¡Datos subidos exitosamente!";
    private static final String MSG_CANCELADO = "Operación cancelada.";

    @FXML
    private TableView<?> tablaVistaPrevia; // Tabla para vista previa (opcional por ahora)
    @FXML
    private Label mensajeConfirmacion; // Etiqueta para mensajes visibles de éxito
    @FXML
    private Label mensajeError; // Etiqueta para mensajes visibles de error
    @FXML
    private Button botonSubirDatos; // Botón de subir datos (inicia deshabilitado)

    // Métodos privados para mostrar mensajes (reducen duplicación)
    private void mostrarMensajeExito(String mensaje) {
        mensajeConfirmacion.setText(mensaje);
        mensajeConfirmacion.setVisible(true);
        mensajeError.setVisible(false);
    }

    private void mostrarMensajeError(String mensaje) {
        mensajeError.setText(mensaje);
        mensajeError.setVisible(true);
        mensajeConfirmacion.setVisible(false);
    }

    // Muestra un mensaje por consola (para acciones internas)
    private void logConsola(String mensaje) {
        System.out.println(mensaje);
    }

    // Acción: Seleccionar archivos
    @FXML
    public void handleSeleccionarArchivos(ActionEvent event) {
        // Crear diálogo para seleccionar archivos
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivos");

        // Filtros de archivos permitidos
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos CSV y Excel", "*.csv", "*.xlsx"),
                new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Archivos Excel", "*.xlsx")
        );

        // Mostrar diálogo para seleccionar múltiples archivos
        Stage stage = new Stage();
        List<File> archivosSeleccionados = fileChooser.showOpenMultipleDialog(stage);

        if (archivosSeleccionados != null && !archivosSeleccionados.isEmpty()) {
            // Mostrar archivos seleccionados en consola
            logConsola("=== Archivos seleccionados ===");
            archivosSeleccionados.forEach(archivo ->
                    logConsola("Archivo seleccionado: " + archivo.getAbsolutePath())
            );

            // Mostrar mensaje en la vista
            mostrarMensajeExito(MSG_ARCHIVOS_SELECCIONADOS + archivosSeleccionados.size());

            // Activar botón de subir datos
            botonSubirDatos.setDisable(false);
        } else {
            // Si se canceló o no se seleccionaron archivos
            logConsola("Acción cancelada: No se seleccionaron archivos.");

            // Mostrar mensaje de error en la vista
            mostrarMensajeError(MSG_NO_SELECCIONADOS);
        }
    }

    // Acción: Subir los datos seleccionados
    @FXML
    public void handleSubirDatos(ActionEvent event) {
        // Lógica para procesar los datos seleccionados
        logConsola("Los datos han sido subidos exitosamente.");

        // Mensaje de éxito en la vista
        mostrarMensajeExito(MSG_DATOS_SUBIDOS);
    }

    // Acción: Cancelar operación
    @FXML
    public void handleCancel(ActionEvent event) {
        // Log por consola (para desarrolladores)
        logConsola("Acción cancelada por el usuario.");

        // Mostrar mensaje en la vista (opcional)
        mostrarMensajeError(MSG_CANCELADO);

        // Desactivar botón de "Subir Datos" como medida preventiva
        botonSubirDatos.setDisable(true);
    }
}