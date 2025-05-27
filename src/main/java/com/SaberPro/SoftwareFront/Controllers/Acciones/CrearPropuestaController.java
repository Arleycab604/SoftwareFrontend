package com.SaberPro.SoftwareFront.Controllers.Acciones;

import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.Enums.ModulosSaberPro;
import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class CrearPropuestaController {

    @FXML private TextField txtNombrePropuesta;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<String> cmbModulo;
    @FXML private DatePicker dpFechaLimite;

    private ModulosAccionController parentController;

    public void setParentController(ModulosAccionController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        cmbModulo.getItems().addAll(ModulosSaberPro.INGLES.toString(),
                ModulosSaberPro.COMPETENCIAS_CIUDADANAS.toString(),
                ModulosSaberPro.COMUNICACION_ESCRITA.toString(),
                ModulosSaberPro.RAZONAMIENTO_CUANTITATIVO.toString(),
                ModulosSaberPro.LECTURA_CRITICA.toString(),
                ModulosSaberPro.FORMULACION_DE_PROYECTOS_DE_INGENIERIA.toString(),
                ModulosSaberPro.PENSAMIENTO_CIENTIFICO_MATEMATICAS_Y_ESTADISTICA.toString(),
                ModulosSaberPro.DISENO_DE_SOFTWARE.toString()); // Ejemplo de módulos
    }

    @FXML
    private void handleCrear() {
        try {
            String nombre = txtNombrePropuesta.getText();
            String descripcion = txtDescripcion.getText();
            ModulosSaberPro modulo = ModulosSaberPro.valueOf( cmbModulo.getValue());
            LocalDate fechaLimite = dpFechaLimite.getValue();

            if (nombre.isEmpty() || descripcion.isEmpty() || modulo == null || fechaLimite == null) {
                mostrarError("Todos los campos son obligatorios.");
                return;
            }

            // Abre selector de archivos para arrastrar/subir
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecciona archivos para subir");
            List<File> archivosSeleccionados = fileChooser.showOpenMultipleDialog(null); // Puedes mover esto a un botón separado si prefieres

            String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
            String lineSeparator = "\r\n";

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            // Campos de texto
            writeTextPart(dos, "nombrePropuesta", nombre, boundary);
            writeTextPart(dos, "descripcion", descripcion, boundary);
            writeTextPart(dos, "moduloPropuesta", modulo.toString(), boundary);
            writeTextPart(dos, "fechaCreacion", LocalDate.now().toString(), boundary);
            writeTextPart(dos, "fechaLimiteEntrega", fechaLimite.toString(), boundary);
            writeTextPart(dos, "estadoPropuesta", "PENDIENTE", boundary);
            writeTextPart(dos, "usuarioProponente", TokenManager.getNombreUsuario(), boundary); // Si lo tienes dinámico, cámbialo

            // URLs de documentos existentes (si aplica)
            List<String> urls = List.of("http://miapp.com/doc1.pdf", "http://miapp.com/doc2.pdf");
            for (String url : urls) {
                writeTextPart(dos, "urlsDocumentoDetalles", url, boundary);
            }

            // Archivos
            if (archivosSeleccionados != null) {
                for (File file : archivosSeleccionados) {
                    writeFilePart(dos, "archivos", file, boundary);
                }
            }

            // Cerrar multipart
            dos.writeBytes("--" + boundary + "--" + lineSeparator);
            dos.flush();

            // Crear solicitud HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/SaberPro/propuestas/crear"))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .header("Authorization", "Bearer " + TokenManager.getToken())
                    .POST(HttpRequest.BodyPublishers.ofByteArray(baos.toByteArray()))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                mostrarError("Propuesta creada con éxito.");
                parentController.cargarPropuestas();
                cerrarVentana();
            } else {
                mostrarError("Error al crear la propuesta: " + response.body());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            mostrarError("Error al enviar la propuesta.");
        }
    }
    private void writeTextPart(DataOutputStream dos, String name, String value, String boundary) throws IOException {
        String lineSeparator = "\r\n";
        dos.writeBytes("--" + boundary + lineSeparator);
        dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + lineSeparator);
        dos.writeBytes(lineSeparator);
        dos.writeBytes(value + lineSeparator);
    }

    private void writeFilePart(DataOutputStream dos, String name, File file, String boundary) throws IOException {
        String lineSeparator = "\r\n";
        String fileName = file.getName();
        String mimeType = Files.probeContentType(file.toPath());

        dos.writeBytes("--" + boundary + lineSeparator);
        dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"" + lineSeparator);
        dos.writeBytes("Content-Type: " + (mimeType != null ? mimeType : "application/octet-stream") + lineSeparator);
        dos.writeBytes(lineSeparator);
        Files.copy(file.toPath(), dos);
        dos.writeBytes(lineSeparator);
    }


    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombrePropuesta.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}