package com.SaberPro.SoftwareFront.Controllers.Cumplimiento;

import com.SaberPro.SoftwareFront.Models.AccionMejora;
import com.SaberPro.SoftwareFront.Models.EntregaAccion;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VerTarjetaCumplimientoController {

    @FXML private Label fechaAperturaLabel;
    @FXML private Label fechaCierreLabel;
    @FXML private Hyperlink existingFileHyperlink;
    @FXML private Label existingFileDateLabel;
    @FXML private Button uploadFileButton; // Button to trigger file chooser
    @FXML private VBox selectedFilesVBox; // Container to display selected file names

    private AccionMejora accionMejoraActual;
    private List<File> filesToUpload = new ArrayList<>(); // To store files selected by the user

    // Formateador de fecha y hora (matching the image format for display)
    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de'yyyy, HH:mm", new Locale("es", "ES"));
    private static final DateTimeFormatter SUBMISSION_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd 'de' MMMM 'de'yyyy, HH:mm", new Locale("es", "ES"));


    public void initialize() {
        // You can add initialization logic here if needed
        // For example, setting up drag and drop listeners on the upload area
    }

    public void setAccionMejora(AccionMejora accion) {
        this.accionMejoraActual = accion;
        if (accion != null) {
            // Set task details (like in VerTarjetaCumplimientoController)
            fechaAperturaLabel.setText(accion.getPermitirEntregasDesde() != null ? accion.getPermitirEntregasDesde().format(DISPLAY_FORMATTER) : "No definida");
            fechaCierreLabel.setText(accion.getFechaEntregaLimite() != null ? accion.getFechaEntregaLimite().format(DISPLAY_FORMATTER) : "No definida");

            // Populate existing submission details if any
            // This part would require fetching the student's existing submission for this task
            // For now, let's use the static example from your image
            existingFileHyperlink.setText("Parcial II PS_.pdf");
            existingFileDateLabel.setText("14 de mayo de 2025, 20:48");
        }
    }

    @FXML
    private void handleMarcarComoHecho() {
        System.out.println("Marcar como hecho (logic not fully implemented for this button).");
        // This might signify a manual completion without file submission
    }

    @FXML
    private void handleVerArchivoExistente() {
        System.out.println("Abriendo archivo existente: " + existingFileHyperlink.getText());
        // Logic to open the existing file, similar to CalificarAccionController
        // You'd need the actual path to the file on the server/local machine
    }

    @FXML
    private void handleSelectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo para entrega");
        // Optional: Set file extensions filters
        // fileChooser.getExtensionFilters().addAll(
        //         new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
        //         new FileChooser.ExtensionFilter("All Files", "*.*")
        // );

        Stage currentStage = (Stage) uploadFileButton.getScene().getWindow();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(currentStage);

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            filesToUpload.clear(); // Clear previous selections if allowing only new ones
            selectedFilesVBox.getChildren().clear(); // Clear display

            for (File file : selectedFiles) {
                filesToUpload.add(file);
                Label fileNameLabel = new Label(file.getName());
                selectedFilesVBox.getChildren().add(fileNameLabel);
                System.out.println("Archivo seleccionado: " + file.getAbsolutePath());
            }
        }
    }

    @FXML
    private void handleGuardarCambios() {
        if (filesToUpload.isEmpty()) {
            System.out.println("No hay archivos para guardar.");
            // Optionally show an alert to the user
            return;
        }

        // Here's where you'd perform the actual file upload and save submission details
        System.out.println("Guardando cambios y subiendo archivos...");

        for (File file : filesToUpload) {
            // In a real application, you would:
            // 1. Upload the 'file' to a server or move it to a persistent storage location.
            //    This would involve network requests (HTTP POST with multipart form data)
            //    or file system operations.
            // 2. Create an EntregaAccion object with the relevant details.
            //    You'll need user info, task info, and file details.

            // Example of creating an EntregaAccion (dummy data for demonstration)
            EntregaAccion newSubmission = new EntregaAccion(
                    "estudiante@example.com", // direccionCorreo (dummy)
                    "Nombre Apellido", // nombreApellido (dummy)
                    "usuario123", // nombreUsuario (dummy)
                    "Villavicencio", // ciudad (dummy)
                    "Enviado", // estado
                    LocalDateTime.now(), // ultimaModificacionEntrega
                    file.getName(), // nombreArchivo
                    LocalDateTime.now(), // fechaEnvioArchivo
                    "Comentarios de la entrega (si hubiera un campo de texto)", // comentariosEntrega
                    null // retroalimentacion (initially null for a new submission)
            );

            // Set the path to the stored file (after actual upload)
            newSubmission.setRutaArchivo("/path/to/uploaded/files/" + file.getName()); // Placeholder

            // 3. Save the newSubmission object to your database.
            //    This would typically involve calling a service layer or DAO.
            System.out.println("Simulando guardar entrega para: " + newSubmission.getNombreArchivo() +
                    ", ruta: " + newSubmission.getRutaArchivo());
            // Persistence logic here (e.g., database insert)
        }

        // After successful upload and saving, you might:
        // 1. Close the current window/stage
        Stage stage = (Stage) uploadFileButton.getScene().getWindow();
        stage.close();
        System.out.println("Entrega guardada y ventana cerrada.");

        // 2. Navigate back to a previous view (e.g., VerTarjetaCumplimiento)
        // You would need a reference to the parent controller or a callback mechanism.
    }

    @FXML
    private void handleCancelar() {
        System.out.println("Cancelando entrega.");
        Stage stage = (Stage) uploadFileButton.getScene().getWindow();
        stage.close();
    }

    // You can add more handlers for the optional icons if needed
    @FXML
    private void handleIcon1() {
        System.out.println("Icon 1 clicked (e.g., for adding more files).");
    }

    @FXML
    private void handleIcon2() {
        System.out.println("Icon 2 clicked (e.g., for managing files).");
    }
}