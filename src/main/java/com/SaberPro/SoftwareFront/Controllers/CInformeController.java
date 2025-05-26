package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.fasterxml.jackson.databind.ObjectMapper; // Importar ObjectMapper
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets; // Importar para el manejo de bytes en descarga de error
import java.util.Collection;
import java.util.Collections; // Importar para Collections.emptyList()
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

public class CInformeController {

    private static final String MSG_UPLOAD_SUCCESS = "¡Archivos subidos exitosamente!";
    private static final String MSG_UPLOAD_ERROR = "Error al subir archivos. Revisa la consola.";
    private static final String MSG_CANCELLED = "Operación de carga cancelada.";
    private static final String MSG_NO_FILES_SELECTED = "No se seleccionaron archivos.";
    private static final String MSG_NO_FILES_TO_UPLOAD = "No hay archivos seleccionados para subir.";
    private static final String MSG_PDF_ONLY = "Solo se permiten archivos PDF.";
    private static final String MSG_DRAG_DROP_AREA = "Arrastra y suelta archivos aquí o haz click en el botón 'Seleccionar Archivos PDF'.";
    private static final String BASE_URL = "http://localhost:8080/SaberPro/informes";

    @FXML private Label mensajeConfirmacion;
    @FXML private Label mensajeError;

    @FXML private Button botonSeleccionarArchivos;
    @FXML private Button botonSubirDatos;
    @FXML private Button botonCancelar;

    @FXML private VBox uploadContainer;
    @FXML private Label uploadMessageLabel;
    @FXML private ListView<File> listaArchivosPendientes;

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbOrdenarPor;
    @FXML private ListView<String> listaInformesHistoricos;

    private ObservableList<File> archivosAPendientes = FXCollections.observableArrayList();
    private ObservableList<String> todosLosInformesHistoricos = FXCollections.observableArrayList();
    private FilteredList<String> filteredInformes;
    private SortedList<String> sortedInformes;

    public void initialize() {
        listaArchivosPendientes.setItems(archivosAPendientes);
        listaArchivosPendientes.setCellFactory(lv -> new ListCell<File>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        botonSubirDatos.setDisable(true);

        cmbOrdenarPor.setItems(FXCollections.observableArrayList("Nombre (A-Z)", "Nombre (Z-A)"));
        cmbOrdenarPor.getSelectionModel().selectFirst();

        filteredInformes = new FilteredList<>(todosLosInformesHistoricos, p -> true);
        sortedInformes = new SortedList<>(filteredInformes);
        listaInformesHistoricos.setItems(sortedInformes);

        listaInformesHistoricos.setCellFactory(lv -> new ListCell<String>() {
            private final HBox hbox = new HBox(10);
            private final Label label = new Label();
            private final Button deleteButton = new Button("X");

            {
                deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 2 6; -fx-font-size: 10px;");
                deleteButton.setOnAction(event -> {
                    String fileNameToDelete = getItem();
                    if (fileNameToDelete != null) {
                        handleDeletePdf(fileNameToDelete);
                    }
                });

                Button downloadButton = new Button("Descargar");
                downloadButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 2 6; -fx-font-size: 10px;");
                downloadButton.setOnAction(event -> {
                    String fileNameToDownload = getItem();
                    if (fileNameToDownload != null) {
                        handleDownloadPdf(fileNameToDownload);
                    }
                });

                hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                hbox.getChildren().addAll(label, downloadButton, deleteButton);
                HBox.setHgrow(label, Priority.ALWAYS);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    label.setText(item);
                    setGraphic(hbox);
                    label.setStyle("-fx-text-fill: #007bff; -fx-underline: true;");
                }
            }
        });

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> applyFiltersAndSortHistoricos());
        cmbOrdenarPor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> applyFiltersAndSortHistoricos());

        loadHistoricoPdfs();
    }

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

    private void logConsola(String mensaje) {
        System.out.println(mensaje);
    }

    @FXML
    public void handleSeleccionarArchivos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            archivosAPendientes.clear();
            archivosAPendientes.addAll(selectedFiles);
            botonSubirDatos.setDisable(false);
            mensajeError.setVisible(false);
            mensajeConfirmacion.setVisible(false);
            uploadMessageLabel.setText("Archivos seleccionados: " + archivosAPendientes.size() + " pendiente(s) de subir.");
        } else {
            if (archivosAPendientes.isEmpty()) {
                botonSubirDatos.setDisable(true);
                uploadMessageLabel.setText(MSG_DRAG_DROP_AREA);
            }
            mostrarMensajeError(MSG_NO_FILES_SELECTED);
        }
    }

    @FXML
    public void handleSubirDatos() {
        if (archivosAPendientes.isEmpty()) {
            mostrarMensajeError(MSG_NO_FILES_TO_UPLOAD);
            logConsola("DEBUG: No se seleccionó ningún archivo para subir.");
            return;
        }

        botonSubirDatos.setDisable(true);
        botonSeleccionarArchivos.setDisable(true);
        botonCancelar.setDisable(true);

        CompletableFuture.runAsync(() -> {
            try {
                // Iterar sobre cada archivo pendiente y subirlo individualmente
                // El backend espera una lista, pero el método uploadFile del frontend está diseñado para uno a la vez.
                // Si el backend puede recibir múltiples archivos en una sola petición, BuildRequest.uploadFile
                // debería ser modificado para construir un cuerpo multipart con todos los archivos.
                // Por simplicidad y para que coincida con el método actual de BuildRequest, se hace uno por uno.
                // Sin embargo, el backend de InformesController.java espera una lista de MultipartFile,
                // por lo que lo ideal sería que el frontend envíe múltiples archivos en una sola petición multipart.
                // Para el problema actual, vamos a llamar a uploadFile para cada archivo, lo cual generará
                // múltiples peticiones al backend (una por archivo). Esto es menos eficiente pero funcionará
                // con la implementación actual de BuildRequest.
                // SI TU BACKEND ESPERA UNA LISTA EN UNA SOLA PETICIÓN, NECESITAS MODIFICAR BuildRequest.uploadFile
                // para que acepte List<File> y construya un único cuerpo multipart.
                // Aquí simulamos que se envía un archivo a la vez para coincidir con BuildRequest.uploadFile(File file).

                boolean allUploadsSuccessful = true;
                for (File file : archivosAPendientes) {
                    Map<String, String> params = new HashMap<>(); // Puedes añadir parámetros si el backend los necesita
                    HttpResponse<String> response = BuildRequest.getInstance().uploadFile(BASE_URL + "/upload", file, params);

                    if (response.statusCode() != 200) {
                        allUploadsSuccessful = false;
                        logConsola("Error al subir archivo " + file.getName() + ". Código: " + response.statusCode() + " Mensaje: " + response.body());
                        // Opcional: Podrías salir aquí o intentar subir los demás
                        break; // Si uno falla, consideramos que la operación total falló
                    }
                }


                boolean finalAllUploadsSuccessful = allUploadsSuccessful;
                javafx.application.Platform.runLater(() -> {
                    if (finalAllUploadsSuccessful) {
                        mostrarMensajeExito(MSG_UPLOAD_SUCCESS);
                        archivosAPendientes.clear();
                        uploadMessageLabel.setText(MSG_DRAG_DROP_AREA);
                        loadHistoricoPdfs();
                    } else {
                        mostrarMensajeError(MSG_UPLOAD_ERROR + " Algunos archivos fallaron al subir.");
                    }
                    botonSubirDatos.setDisable(true);
                    botonSeleccionarArchivos.setDisable(false);
                    botonCancelar.setDisable(false);
                });
            } catch (IOException | InterruptedException e) {
                javafx.application.Platform.runLater(() -> {
                    mostrarMensajeError(MSG_UPLOAD_ERROR + " Excepción: " + e.getMessage());
                    logConsola("Excepción durante la subida: " + e.getMessage());
                    botonSubirDatos.setDisable(archivosAPendientes.isEmpty());
                    botonSeleccionarArchivos.setDisable(false);
                    botonCancelar.setDisable(false);
                });
            }
        });
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        logConsola("Operación de carga cancelada.");
        archivosAPendientes.clear();
        botonSubirDatos.setDisable(true);
        mostrarMensajeError(MSG_CANCELLED);
        uploadMessageLabel.setText(MSG_DRAG_DROP_AREA);
        mensajeConfirmacion.setVisible(false);
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != uploadContainer && event.getDragboard().hasFiles()) {
            boolean allPdfs = event.getDragboard().getFiles().stream()
                    .allMatch(file -> file.getName().toLowerCase().endsWith(".pdf"));
            if (allPdfs) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                uploadContainer.setStyle("-fx-border-color: #007bff; -fx-border-style: dashed; -fx-border-width: 2; -fx-background-color: #E0EFFF; -fx-background-radius: 5;");
            }
        }
        event.consume();
    }

    @FXML
    private void handleDragDropped(DragEvent event) {
        boolean success = false;
        if (event.getDragboard().hasFiles()) {
            List<File> droppedFiles = event.getDragboard().getFiles();
            List<File> pdfFiles = droppedFiles.stream()
                    .filter(file -> file.getName().toLowerCase().endsWith(".pdf"))
                    .collect(Collectors.toList());

            if (!pdfFiles.isEmpty()) {
                archivosAPendientes.clear();
                archivosAPendientes.addAll(pdfFiles);
                botonSubirDatos.setDisable(false);
                mensajeError.setVisible(false);
                mensajeConfirmacion.setVisible(false);
                uploadMessageLabel.setText("Archivos seleccionados: " + archivosAPendientes.size() + " pendiente(s) de subir.");
                success = true;
            } else {
                mostrarMensajeError(MSG_PDF_ONLY);
            }
        }
        event.setDropCompleted(success);
        event.consume();
        uploadContainer.setStyle("-fx-border-color: #CCC; -fx-border-style: dashed; -fx-border-width: 2; -fx-background-color: #F9F9F9; -fx-background-radius: 5;");
    }

    @FXML
    private void handleDragExited(DragEvent event) {
        uploadContainer.setStyle("-fx-border-color: #CCC; -fx-border-style: dashed; -fx-border-width: 2; -fx-background-color: #F9F9F9; -fx-background-radius: 5;");
        event.consume();
    }

    private void loadHistoricoPdfs() {
        logConsola("DEBUG: Cargando informes históricos desde el backend...");
        CompletableFuture.supplyAsync(() -> {
            try {
                // Llama a GETParams, que devuelve String, y asume que no hay parámetros para listar
                HttpResponse<String> response = BuildRequest.getInstance().GETParams(BASE_URL + "/list", null);
                if (response.statusCode() == 200) {
                    String jsonBody = response.body();
                    ObjectMapper mapper = new ObjectMapper();
                    // Lee el JSON como una lista de Strings
                    return mapper.readValue(jsonBody, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                } else {
                    logConsola("Error al listar informes históricos. Código: " + response.statusCode() + " Mensaje: " + response.body());
                    return Collections.emptyList();
                }
            } catch (IOException | InterruptedException e) {
                logConsola("Excepción al cargar informes históricos: " + e.getMessage());
                return Collections.emptyList();
            }
        }).thenAccept(fileNames -> {
            javafx.application.Platform.runLater(() -> {
                todosLosInformesHistoricos.clear();
                todosLosInformesHistoricos.addAll(String.valueOf(fileNames)); // Ya es List<String>
                applyFiltersAndSortHistoricos();
            });
        });
    }

    private void applyFiltersAndSortHistoricos() {
        filteredInformes.setPredicate(fileName -> {
            String searchText = txtBuscar.getText();
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            return fileName.toLowerCase().contains(searchText.toLowerCase());
        });

        String selectedOrder = cmbOrdenarPor.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            switch (selectedOrder) {
                case "Nombre (A-Z)":
                    sortedInformes.setComparator(Comparator.comparing(String::toString));
                    break;
                case "Nombre (Z-A)":
                    sortedInformes.setComparator(Comparator.comparing(String::toString).reversed());
                    break;
                default:
                    sortedInformes.setComparator(null);
                    break;
            }
        } else {
            sortedInformes.setComparator(null);
        }
    }

    private void handleDeletePdf(String fileName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Estás seguro de que quieres eliminar el archivo '" + fileName + "'?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logConsola("DEBUG: Intentando eliminar archivo: " + fileName);
            CompletableFuture.runAsync(() -> {
                try {
                    // Llama al nuevo método DELETE de BuildRequest
                    HttpResponse<String> response = BuildRequest.getInstance().DELETE(BASE_URL + "/delete/" + fileName);
                    javafx.application.Platform.runLater(() -> {
                        if (response.statusCode() == 200) {
                            mostrarMensajeExito("El archivo '" + fileName + "' ha sido eliminado exitosamente.");
                            loadHistoricoPdfs();
                        } else {
                            mostrarMensajeError("Error al eliminar el archivo '" + fileName + "'. Código: " + response.statusCode() + " Mensaje: " + response.body());
                            logConsola("Error al eliminar: " + response.body());
                        }
                    });
                } catch (IOException | InterruptedException e) {
                    javafx.application.Platform.runLater(() -> {
                        mostrarMensajeError("Excepción al intentar eliminar '" + fileName + "': " + e.getMessage());
                        logConsola("Excepción al eliminar: " + e.getMessage());
                    });
                }
            });
        }
    }

    private void handleDownloadPdf(String fileName) {
        logConsola("DEBUG: Intentando descargar archivo: " + fileName);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo PDF");
        fileChooser.setInitialFileName(fileName);
        File saveFile = fileChooser.showSaveDialog(new Stage());

        if (saveFile != null) {
            CompletableFuture.runAsync(() -> {
                try {
                    // Llama al nuevo método GETBytes de BuildRequest
                    HttpResponse<byte[]> response = BuildRequest.getInstance().GETBytes(BASE_URL + "/download/" + fileName, null, true);
                    javafx.application.Platform.runLater(() -> {
                        if (response.statusCode() == 200) {
                            try {
                                Files.write(saveFile.toPath(), response.body());
                                mostrarMensajeExito("El archivo '" + fileName + "' ha sido descargado exitosamente en: " + saveFile.getAbsolutePath());
                            } catch (IOException e) {
                                mostrarMensajeError("Error al guardar el archivo descargado: " + e.getMessage());
                                logConsola("Error al guardar archivo: " + e.getMessage());
                            }
                        } else {
                            mostrarMensajeError("Error al descargar el archivo '" + fileName + "'. Código: " + response.statusCode());
                            // Si el cuerpo de la respuesta de error es un String, se convierte a String para mostrar
                            logConsola("Error al descargar: " + response.statusCode() + " " + (response.body() != null ? new String(response.body(), StandardCharsets.UTF_8) : ""));
                        }
                    });
                } catch (IOException | InterruptedException e) {
                    javafx.application.Platform.runLater(() -> {
                        mostrarMensajeError("Excepción al intentar descargar '" + fileName + "': " + e.getMessage());
                        logConsola("Excepción al descargar: " + e.getMessage());
                    });
                }
            });
        } else {
            mostrarMensajeError("Operación de descarga cancelada.");
        }
    }
}