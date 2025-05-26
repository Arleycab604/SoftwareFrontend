package com.SaberPro.SoftwareFront.Controllers.Cumplimiento;

import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.TokenManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class VerTarjetaCumplimientoController {

    @FXML private VBox selectedFilesVBox;
    @FXML private Label fechaAperturaLabel;
    @FXML private Label fechaCierreLabel;
    @FXML private Label archivoSeleccionadoLabel;

    private static final String BASE_URL = "http://localhost:8080/SaberPro";

    private PropuestaMejoraDTO accionMejora;
    private List<File> archivosSeleccionados = new ArrayList<>();

    @FXML
    public void initialize() {
        cargarPropuestas(); // si quieres cargar propuestas generales aquí
    }

    public void setPropuestaMejora(PropuestaMejoraDTO tarea) {
        this.accionMejora = tarea;
        System.out.println("Tarea recibida: " + tarea);
        if (tarea != null) {
            fechaAperturaLabel.setText(tarea.getFechaCreacion() != null ? tarea.getFechaCreacion().toString() : "No definida");
            fechaCierreLabel.setText(tarea.getFechaLimiteEntrega() != null ? tarea.getFechaLimiteEntrega().toString() : "No definida");

            selectedFilesVBox.getChildren().clear();
            Label nombreLabel = new Label("Propuesta: " + tarea.getNombrePropuesta());
            Label descripcionLabel = new Label("Descripción: " + tarea.getDescripcion());
            selectedFilesVBox.getChildren().addAll(nombreLabel, descripcionLabel);
        }
    }

    @FXML
    private void handleSeleccionarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar evidencias");
        List<File> archivos = fileChooser.showOpenMultipleDialog(null);

        if (archivos != null && !archivos.isEmpty()) {
            archivosSeleccionados = archivos;

            // Mostrar nombres en label
            StringBuilder nombres = new StringBuilder();
            for (File file : archivosSeleccionados) {
                nombres.append(file.getName()).append("\n");
            }
            archivoSeleccionadoLabel.setText(nombres.toString());
        } else {
            archivoSeleccionadoLabel.setText("No se seleccionaron archivos.");
        }
    }

    @FXML
    private void handleSubirEvidencia() {
        if (archivosSeleccionados != null && !archivosSeleccionados.isEmpty() && accionMejora != null) {
            try {
                // Crea un mapa con los campos de texto
                Map<String, String> camposTexto = Map.of(
                        "nombreDocente", TokenManager.getNombreUsuario(),
                        "idPropuestaMejora", String.valueOf(accionMejora.getIdPropuestaMejora())
                );

                // Realiza el POST con múltiples archivos
                HttpResponse<String> response = BuildRequest.getInstance().POSTMultipartWithFiles(
                        BASE_URL + "/evidencias/crear", camposTexto, archivosSeleccionados
                );

                if (response.statusCode() == 200) {
                    mostrarError("Evidencias subidas con éxito.");
                } else {
                    mostrarError("Error al subir evidencias: " + response.body());
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                mostrarError("Excepción al subir archivos.");
            }
        } else {
            mostrarError("Debe seleccionar al menos un archivo.");
        }
    }

    private void mostrarError(String mensaje) {
        selectedFilesVBox.getChildren().clear();
        Label errorLabel = new Label(mensaje);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 5px;");
        selectedFilesVBox.getChildren().add(errorLabel);
    }

    private void cargarPropuestas() {
        // Puedes implementar esta parte si quieres listar todas las propuestas del módulo
    }
    @FXML
    private void handleGuardarCambios() {
        // Aquí debes implementar la lógica para guardar los cambios realizados
        // Por ejemplo, validar datos, hacer la llamada al backend o actualizar la UI

        // Ejemplo básico:
        if (accionMejora != null) {
            // Supongamos que quieres enviar cambios al servidor o actualizar algo local
            System.out.println("Guardando cambios para la propuesta: " + accionMejora.getNombrePropuesta());

            // Luego podrías mostrar un mensaje de éxito o actualizar la interfaz
            mostrarError("Cambios guardados correctamente.");  // Reutiliza el método para mostrar mensajes
        } else {
            mostrarError("No hay propuesta seleccionada para guardar cambios.");
        }
    }

    @FXML
    private void handleCancelar() {
        // Lógica para cancelar la operación y cerrar la ventana actual o limpiar campos

        // Si este controlador está en una ventana separada, la cerramos:
        Stage stage = (Stage) archivoSeleccionadoLabel.getScene().getWindow();
        stage.close();

        // O si quieres limpiar campos, puedes hacerlo también aquí
    }
    @FXML
    private void handleVerArchivoExistente() {
        if (accionMejora == null) {
            mostrarError("No hay propuesta seleccionada.");
            return;
        }

        String[] archivosArray = accionMejora.getArchivos();

        // Validar que no sea null ni vacío
        if (archivosArray == null || archivosArray.length == 0) {
            mostrarError("No hay archivos asociados a esta propuesta.");
            return;
        }

        List<String> archivos = Arrays.asList(archivosArray);

        // Ya sabemos que archivos no está vacío, entonces este acceso es seguro
        ChoiceDialog<String> dialog = new ChoiceDialog<>(archivos.get(0), archivos);
        dialog.setTitle("Seleccionar archivo");
        dialog.setHeaderText("Selecciona el archivo a descargar");
        dialog.setContentText("Archivos:");

        Optional<String> resultado = dialog.showAndWait();
        if (resultado.isPresent()) {
            String nombreArchivo = resultado.get();
            // Aquí ya puedes llamar a descargarArchivoDesdeUrl
            // Pasando el nombreArchivo y la URL que corresponda (de tu lista urlsDocumentoDetalles)
            // Ejemplo (si tienes la lista urlsDocumentoDetalles sincronizada con archivos):
            int index = archivos.indexOf(nombreArchivo);
            if (index >= 0 && index < accionMejora.getUrlsDocumentoDetalles().size()) {
                String url = accionMejora.getUrlsDocumentoDetalles().get(index);
                descargarArchivoDesdeUrl(nombreArchivo, url);
            } else {
                mostrarError("No se encontró URL para el archivo seleccionado.");
            }
        } else {
            mostrarError("No se seleccionó ningún archivo.");
        }
    }



    private void descargarArchivoDesdeUrl(String nombreArchivo, String urlArchivo) {
        try {
            // Aquí construyes la URI de tu endpoint REST pasando la URL como parámetro
            String endpoint = "http://localhost:8080/documento/download/url?url=" + URLEncoder.encode(urlArchivo, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .GET()
                    .build();

            HttpResponse<byte[]> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar archivo");
                fileChooser.setInitialFileName(nombreArchivo);
                File fileDestino = fileChooser.showSaveDialog(null);

                if (fileDestino != null) {
                    Files.write(fileDestino.toPath(), response.body());
                    mostrarError("Archivo descargado correctamente: " + fileDestino.getAbsolutePath());
                } else {
                    mostrarError("Descarga cancelada.");
                }
            } else {
                mostrarError("Error al descargar el archivo. Código HTTP: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            mostrarError("Error al descargar el archivo.");
        }
    }


}

