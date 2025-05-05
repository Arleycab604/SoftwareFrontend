package com.SaberPro.SoftwareFront.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.HttpEntity;
import java.io.File;
import javafx.scene.control.TextField;
import org.apache.hc.client5.http.entity.EntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import java.io.IOException;


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
    private Button botonSeleccionarArchivos;
    @FXML
    private Button botonSubirDatos;
    @FXML
    private Label mensajeError;

    private File archivoSeleccionado;

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


    @FXML
    public void handleSeleccionarArchivos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));
        archivoSeleccionado = fileChooser.showOpenDialog(new Stage());

        if (archivoSeleccionado != null) {
            botonSubirDatos.setDisable(false);
            mensajeError.setText("");
        } else {
            botonSubirDatos.setDisable(true);
            mensajeError.setText("No se seleccionó ningún archivo. Intenta nuevamente.");
        }
    }
    @FXML
    private TextField campoAnio;

    @FXML
    private TextField campoPeriodo;

    @FXML
    public void handleSubirDatos() {
        if (archivoSeleccionado == null) {
            mensajeError.setText("No se ha seleccionado ningún archivo.");
            System.out.println("DEBUG: No se seleccionó ningún archivo para subir.");
            return;
        }

        String anioTexto = campoAnio.getText();
        String periodoTexto = campoPeriodo.getText();

        if (anioTexto.isEmpty() || periodoTexto.isEmpty()) {
            mensajeError.setText("Debe ingresar el año y el periodo.");
            System.out.println("DEBUG: Año o periodo no ingresados.");
            return;
        }

        try {
            int anio = Integer.parseInt(anioTexto);
            int periodo = Integer.parseInt(periodoTexto);

            String url = "http://localhost:8080/SaberPro/upload/csv?year=" + anio + "&periodo=" + periodo;

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost uploadFile = new HttpPost(url);

                HttpEntity entity = EntityBuilder.create()
                        .setFile(archivoSeleccionado)
                        .setContentType(ContentType.MULTIPART_FORM_DATA)
                        .build();

                uploadFile.setEntity(entity);

                try (CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                    int statusCode = response.getCode();
                    String reasonPhrase = response.getReasonPhrase();

                    if (statusCode == 200) {
                        mensajeError.setText("Archivo subido exitosamente.");
                        mensajeError.setStyle("-fx-text-fill: green;");
                        System.out.println("DEBUG: Archivo subido exitosamente. Respuesta del servidor: " + reasonPhrase);
                    } else {
                        mensajeError.setText("Error al subir el archivo. Servidor respondió: " + reasonPhrase);
                        mensajeError.setStyle("-fx-text-fill: red;");
                        System.out.println("DEBUG: Error al subir el archivo. Código de estado: " + statusCode + ", Razón: " + reasonPhrase);
                    }
                }
            }
        } catch (NumberFormatException e) {
            mensajeError.setText("El año y el periodo deben ser números enteros.");
            mensajeError.setStyle("-fx-text-fill: red;");
            System.out.println("DEBUG: Error de formato en año o periodo. Detalles: " + e.getMessage());
        } catch (IOException e) {
            mensajeError.setText("Error al subir el archivo: " + e.getMessage());
            mensajeError.setStyle("-fx-text-fill: red;");
            System.out.println("DEBUG: Error de IO al subir el archivo. Detalles: " + e.getMessage());
        }
    }

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
