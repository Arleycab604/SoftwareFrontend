package com.SaberPro.SoftwareFront.Controllers.AccionesMejora;

import com.SaberPro.SoftwareFront.Models.AsignarAccionModel;
import com.SaberPro.SoftwareFront.Models.AccionMejora; // Importar AccionMejora
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox; // Importar VBox
import javafx.stage.Stage;
import javafx.stage.FileChooser; // Importar FileChooser
import javafx.scene.input.DragEvent; // Importar DragEvent
import javafx.scene.input.TransferMode; // Importar TransferMode

import java.io.File; // Importar File
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AsignarAccionController {
    // FXML elements
    @FXML private TextField nombreTareaField;
    @FXML private TextArea descripcionArea;
    @FXML private TextArea instruccionesActividadArea;
    @FXML private Label moduloLabel;
    @FXML private CheckBox mostrarDescripcionCheck;

    // Disponibilidad
    @FXML private CheckBox habilitarDesdeCheck;
    @FXML private DatePicker fechaInicioPicker;
    @FXML private ComboBox<String> mesInicioCombo;
    @FXML private TextField anioInicioField;
    @FXML private ComboBox<String> horaInicioCombo;
    @FXML private ComboBox<String> minutoInicioCombo;

    @FXML private CheckBox habilitarEntregaCheck;
    @FXML private DatePicker fechaEntregaPicker;
    @FXML private ComboBox<String> mesEntregaCombo;
    @FXML private TextField anioEntregaField;
    @FXML private ComboBox<String> horaEntregaCombo;
    @FXML private ComboBox<String> minutoEntregaCombo;

    @FXML private CheckBox habilitarLimiteCheck;
    @FXML private DatePicker fechaLimitePicker;
    @FXML private ComboBox<String> mesLimiteCombo;
    @FXML private TextField anioLimiteField;
    @FXML private ComboBox<String> horaLimiteCombo;
    @FXML private ComboBox<String> minutoLimiteCombo;

    @FXML private CheckBox habilitarRecordatorioCheck;
    @FXML private DatePicker fechaRecordatorioPicker;
    @FXML private ComboBox<String> mesRecordatorioCombo;
    @FXML private TextField anioRecordatorioField;
    @FXML private ComboBox<String> horaRecordatorioCombo;
    @FXML private ComboBox<String> minutoRecordatorioCombo;



    // Tipos de entrega
    @FXML private CheckBox textoEnLineaCheck;
    @FXML private CheckBox archivosEnviadosCheck;
    @FXML private Spinner<Integer> maxArchivosSpinner;
    @FXML private ComboBox<String> maxTamanoCombo;
    @FXML private TextField tiposArchivoField;

    // Archivos adicionales - NUEVOS ELEMENTOS FXML
    @FXML private VBox archivosContainer; // El contenedor para arrastrar y soltar
    @FXML private Label dropMessageLabel; // Etiqueta para mensajes dentro del drag-and-drop area
    @FXML private Button btnSeleccionarArchivos; // Botón para abrir el FileChooser
    @FXML private ListView<String> listaArchivos; // ListView para mostrar los nombres de los archivos


    private ObservableList<File> archivosAdjuntos = FXCollections.observableArrayList();
    private AsignarAccionModel model = new AsignarAccionModel();
    private Runnable refreshCallback;
    private AccionMejora accionParaEditar;

    @FXML
    public void initialize() {
        maxArchivosSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));

        configureTimeComboBoxes(horaInicioCombo, minutoInicioCombo);
        configureTimeComboBoxes(horaEntregaCombo, minutoEntregaCombo);
        configureTimeComboBoxes(horaLimiteCombo, minutoLimiteCombo);
        configureTimeComboBoxes(horaRecordatorioCombo, minutoRecordatorioCombo);

        maxTamanoCombo.setItems(FXCollections.observableArrayList(
                "1 MB", "5 MB", "10 MB", "20 MB", "50 MB", "100 MB"
        ));
        maxTamanoCombo.getSelectionModel().select("50 MB");

        nombreTareaField.textProperty().bindBidirectional(model.nombreTareaProperty());
        descripcionArea.textProperty().bindBidirectional(model.descripcionGeneralProperty());
        instruccionesActividadArea.textProperty().bindBidirectional(model.instruccionesActividadProperty());
        tiposArchivoField.textProperty().bindBidirectional(model.tiposArchivosAceptadosProperty());

        maxTamanoCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    long size = Long.parseLong(newVal.replaceAll("[^\\d]", ""));
                    model.setTamanoMaximoArchivoMB(size);
                } catch (NumberFormatException e) {
                    System.err.println("Error al parsear el tamaño máximo: " + newVal);
                }
            }
        });

        maxArchivosSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                model.setMaxArchivosSubidos(newValue);
            }
        });

        model.maxArchivosSubidosProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                maxArchivosSpinner.getValueFactory().setValue(newValue.intValue());
            }
        });

        setupDateAndTimeEnabling(habilitarDesdeCheck, fechaInicioPicker, mesInicioCombo, anioInicioField, horaInicioCombo, minutoInicioCombo);
        setupDateAndTimeEnabling(habilitarEntregaCheck, fechaEntregaPicker, mesEntregaCombo, anioEntregaField, horaEntregaCombo, minutoEntregaCombo);
        setupDateAndTimeEnabling(habilitarLimiteCheck, fechaLimitePicker, mesLimiteCombo, anioLimiteField, horaLimiteCombo, minutoLimiteCombo);
        setupDateAndTimeEnabling(habilitarRecordatorioCheck, fechaRecordatorioPicker, mesRecordatorioCombo, anioRecordatorioField, horaRecordatorioCombo, minutoRecordatorioCombo);

        habilitarDesdeCheck.setSelected(false);
        habilitarEntregaCheck.setSelected(false);
        habilitarLimiteCheck.setSelected(false);
        habilitarRecordatorioCheck.setSelected(false);

        textoEnLineaCheck.setSelected(false);
        archivosEnviadosCheck.setSelected(true);

        // Inicializar el ListView para mostrar los nombres de los archivos
        listaArchivos.setItems(FXCollections.observableArrayList()); // Inicializar con una lista vacía
        // Listener para actualizar el ListView cuando se añaden/quitan archivos a la lista observable
        archivosAdjuntos.addListener((javafx.collections.ListChangeListener<File>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    actualizarListaArchivosUI();
                }
            }
        });
    }

    private void actualizarListaArchivosUI() {
        // Limpiar y volver a añadir los nombres de los archivos al ListView
        listaArchivos.getItems().clear();
        for (File file : archivosAdjuntos) {
            listaArchivos.getItems().add(file.getName());
        }
        // Opcional: mostrar un mensaje diferente si no hay archivos
        if (archivosAdjuntos.isEmpty()) {
            dropMessageLabel.setText("Arrastra y suelta archivos aquí");
        } else {
            dropMessageLabel.setText("Archivos adjuntos:");
        }
    }


    private void configureTimeComboBoxes(ComboBox<String> horaCombo, ComboBox<String> minutoCombo) {
        List<String> hours = IntStream.range(0, 24)
                .mapToObj(i -> String.format("%02d", i))
                .collect(Collectors.toList());
        List<String> minutes = IntStream.range(0, 60)
                .mapToObj(i -> String.format("%02d", i))
                .collect(Collectors.toList());

        horaCombo.setItems(FXCollections.observableArrayList(hours));
        minutoCombo.setItems(FXCollections.observableArrayList(minutes));

        horaCombo.getSelectionModel().select("08");
        minutoCombo.getSelectionModel().select("00");
    }

    private void setupDateAndTimeEnabling(CheckBox checkBox, DatePicker datePicker,
                                          ComboBox<String> mesCombo, TextField anioField,
                                          ComboBox<String> horaCombo, ComboBox<String> minutoCombo) {
        boolean isEnabled = checkBox.isSelected();
        datePicker.setDisable(!isEnabled);
        if (mesCombo != null) mesCombo.setDisable(!isEnabled);
        if (anioField != null) anioField.setDisable(!isEnabled);
        if (horaCombo != null) horaCombo.setDisable(!isEnabled);
        if (minutoCombo != null) minutoCombo.setDisable(!isEnabled);

        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            datePicker.setDisable(!newVal);
            if (mesCombo != null) mesCombo.setDisable(!newVal);
            if (anioField != null) anioField.setDisable(!newVal);
            if (horaCombo != null) horaCombo.setDisable(!newVal);
            if (minutoCombo != null) minutoCombo.setDisable(!newVal);
        });
    }

    public void setModuloActual(String modulo) {
        this.model.setModuloAsignado(modulo);
        moduloLabel.setText("Actualizando Tarea en REPORTE DE NOTAS - DEFINITIVAS 1-2025 (Módulo: " + modulo + ")");
        System.out.println("Módulo asignado automáticamente para nueva acción: " + modulo);
    }

    public void cargarDatosParaEdicion(AccionMejora accion) {
        this.accionParaEditar = accion;

        moduloLabel.setText("Actualizando Tarea en REPORTE DE NOTAS - DEFINITIVAS 1-2025 (Editando: " + accion.getNombreTarea() + ")");

        model.setId(accion.getId());
        model.setNombreTarea(accion.getNombreTarea());
        model.setDescripcionGeneral(accion.getDescripcionGeneral());
        model.setInstruccionesActividad(accion.getInstruccionesActividad());
        model.setModuloAsignado(accion.getModuloAsignadoNombre());

        setDateTimeControls(habilitarDesdeCheck, fechaInicioPicker, horaInicioCombo, minutoInicioCombo, accion.getPermitirEntregasDesde());
        setDateTimeControls(habilitarEntregaCheck, fechaEntregaPicker, horaEntregaCombo, minutoEntregaCombo, accion.getFechaEntregaLimite());
        setDateTimeControls(habilitarLimiteCheck, fechaLimitePicker, horaLimiteCombo, minutoLimiteCombo, accion.getFechaLimiteFinal());
        setDateTimeControls(habilitarRecordatorioCheck, fechaRecordatorioPicker, horaRecordatorioCombo, minutoRecordatorioCombo, accion.getRecordarCalificarEn());

        maxArchivosSpinner.getValueFactory().setValue(accion.getMaxArchivosSubidos());
        maxTamanoCombo.getSelectionModel().select(accion.getTamanoMaximoArchivoMB() + " MB");
        tiposArchivoField.setText(accion.getTiposArchivosAceptados());

        // Si tu modelo AccionMejora tiene la info de archivos, cárgala aquí
        // Por ahora, no hay lógica para cargar archivos existentes en el formulario
    }

    private void setDateTimeControls(CheckBox checkBox, DatePicker datePicker,
                                     ComboBox<String> horaCombo, ComboBox<String> minutoCombo,
                                     LocalDateTime dateTime) {
        if (dateTime != null) {
            checkBox.setSelected(true);
            datePicker.setValue(dateTime.toLocalDate());
            horaCombo.getSelectionModel().select(String.format("%02d", dateTime.getHour()));
            minutoCombo.getSelectionModel().select(String.format("%02d", dateTime.getMinute()));
        } else {
            checkBox.setSelected(false);
            datePicker.setValue(null);
            horaCombo.getSelectionModel().select("00");
            minutoCombo.getSelectionModel().select("00");
        }
        setupDateAndTimeEnabling(checkBox, datePicker, null, null, horaCombo, minutoCombo);
    }


    public void setRefreshCallback(Runnable callback) {
        this.refreshCallback = callback;
    }

    @FXML
    private void handleGuardar() {
        if (nombreTareaField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error de Validación", "El nombre de la tarea no puede estar vacío.");
            return;
        }
        if (descripcionArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error de Validación", "La descripción no puede estar vacía.");
            return;
        }

        model.setPermitirEntregasDesde(getLocalDateTimeFromControls(
                habilitarDesdeCheck, fechaInicioPicker, horaInicioCombo, minutoInicioCombo));
        model.setFechaEntregaLimite(getLocalDateTimeFromControls(
                habilitarEntregaCheck, fechaEntregaPicker, horaEntregaCombo, minutoEntregaCombo));
        model.setFechaLimiteFinal(getLocalDateTimeFromControls(
                habilitarLimiteCheck, fechaLimitePicker, horaLimiteCombo, minutoLimiteCombo));
        model.setRecordarCalificarEn(getLocalDateTimeFromControls(
                habilitarRecordatorioCheck, fechaRecordatorioPicker, horaRecordatorioCombo, minutoRecordatorioCombo));

        // Aquí podrías guardar el estado de los checkboxes de tipo de entrega
        model.setTextoEnLinea(textoEnLineaCheck.isSelected());
        model.setArchivosEnviados(archivosEnviadosCheck.isSelected());

        // Podrías pasar la lista de archivos adjuntos al modelo si tu modelo soporta una List<File>
        // model.setArchivosAdjuntos(new ArrayList<>(archivosAdjuntos));


        AccionMejora accionFinal;
        if (accionParaEditar != null) {
            // EDITAR acción existente
            accionFinal = accionParaEditar;

            accionFinal.setNombreTarea(model.getNombreTarea());
            accionFinal.setDescripcionGeneral(model.getDescripcionGeneral());
            accionFinal.setInstruccionesActividad(model.getInstruccionesActividad());

            accionFinal.setPermitirEntregasDesde(model.getPermitirEntregasDesde());
            accionFinal.setFechaEntregaLimite(model.getFechaEntregaLimite());
            accionFinal.setFechaLimiteFinal(model.getFechaLimiteFinal());
            accionFinal.setRecordarCalificarEn(model.getRecordarCalificarEn());

            accionFinal.setMaxArchivosSubidos(model.getMaxArchivosSubidos());
            accionFinal.setTamanoMaximoArchivoMB(model.getTamanoMaximoArchivoMB());
            accionFinal.setTiposArchivosAceptados(model.getTiposArchivosAceptados());

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Acción de mejora actualizada correctamente.");
        } else {
            // CREAR nueva acción
            accionFinal = new AccionMejora(
                    UUID.randomUUID().toString(),
                    "SEMESTRE V",
                    "COD-" + System.currentTimeMillis(),
                    "Activa",
                    model
            );
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Nueva acción de mejora creada correctamente.");
        }

        if (refreshCallback != null) {
            refreshCallback.run();
        }

        Stage stage = (Stage) nombreTareaField.getScene().getWindow();
        stage.close();
    }

    private LocalDateTime getLocalDateTimeFromControls(CheckBox checkBox, DatePicker datePicker,
                                                       ComboBox<String> horaCombo, ComboBox<String> minutoCombo) {
        if (checkBox.isSelected()) {
            LocalDate date = datePicker.getValue();
            if (date == null) {
                showAlert(Alert.AlertType.WARNING, "Advertencia de Fecha", "Por favor, selecciona una fecha válida para la opción habilitada.");
                return null;
            }
            int hour = (horaCombo.getSelectionModel().getSelectedItem() != null) ?
                    Integer.parseInt(horaCombo.getSelectionModel().getSelectedItem()) : 0;
            int minute = (minutoCombo.getSelectionModel().getSelectedItem() != null) ?
                    Integer.parseInt(minutoCombo.getSelectionModel().getSelectedItem()) : 0;

            return LocalDateTime.of(date, LocalTime.of(hour, minute));
        }
        return null;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // --- Métodos para manejar la carga de archivos ---

    @FXML
    private void handleSeleccionarArchivos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivos Adicionales");

        // Definir filtros de extensión basados en el contenido de tiposArchivoField
        String acceptedTypes = tiposArchivoField.getText();
        if (acceptedTypes != null && !acceptedTypes.trim().isEmpty()) {
            // Ejemplo: "pdf,jpg,png,doc,docx" -> "*.pdf", "*.jpg", "*.png", "*.doc", "*.docx"
            String[] types = acceptedTypes.split(",");
            for (String type : types) {
                String trimmedType = type.trim();
                if (!trimmedType.isEmpty()) {
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                            trimmedType.toUpperCase() + " Files", "*." + trimmedType.toLowerCase()));
                }
            }
        }
        // Añadir un filtro para "Todos los archivos" por defecto
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));


        // Usar la Stage de la ventana actual como padre para el FileChooser
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(btnSeleccionarArchivos.getScene().getWindow());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            // Aquí puedes añadir validación adicional si lo necesitas (e.g., tamaño máximo, número máximo)
            for (File file : selectedFiles) {
                // Validación para número máximo de archivos
                if (archivosAdjuntos.size() >= model.getMaxArchivosSubidos()) {
                    showAlert(Alert.AlertType.WARNING, "Límite de Archivos",
                            "Has alcanzado el límite de " + model.getMaxArchivosSubidos() + " archivos.");
                    break; // Salir del bucle si se alcanza el límite
                }

                // Validación de tipo de archivo (rudimentaria, puedes mejorarla)
                if (!validarTipoArchivo(file.getName(), tiposArchivoField.getText())) {
                    showAlert(Alert.AlertType.WARNING, "Tipo de Archivo Inválido",
                            "El archivo '" + file.getName() + "' no es un tipo permitido. Tipos aceptados: " + tiposArchivoField.getText());
                    continue; // Saltar este archivo
                }

                // Validación de tamaño de archivo
                if (file.length() > model.getTamanoMaximoArchivoMB() * 1024 * 1024) {
                    showAlert(Alert.AlertType.WARNING, "Tamaño de Archivo Excedido",
                            "El archivo '" + file.getName() + "' excede el tamaño máximo permitido de " + model.getTamanoMaximoArchivoMB() + " MB.");
                    continue; // Saltar este archivo
                }

                archivosAdjuntos.add(file); // Añadir el archivo a la lista observable
            }
            actualizarListaArchivosUI(); // Actualizar la vista después de añadir archivos
        }
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != archivosContainer && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            archivosContainer.setStyle("-fx-border-color: #007bff; -fx-border-style: dashed; -fx-border-width: 2; -fx-background-color: #E0EFFF; -fx-background-radius: 5;"); // Estilo al arrastrar sobre
        }
        event.consume();
    }

    @FXML
    private void handleDragDropped(DragEvent event) {
        boolean success = false;
        if (event.getDragboard().hasFiles()) {
            List<File> files = event.getDragboard().getFiles();
            for (File file : files) {
                // Validación para número máximo de archivos
                if (archivosAdjuntos.size() >= model.getMaxArchivosSubidos()) {
                    showAlert(Alert.AlertType.WARNING, "Límite de Archivos",
                            "Has alcanzado el límite de " + model.getMaxArchivosSubidos() + " archivos.");
                    break; // Salir del bucle si se alcanza el límite
                }

                // Validación de tipo de archivo
                if (!validarTipoArchivo(file.getName(), tiposArchivoField.getText())) {
                    showAlert(Alert.AlertType.WARNING, "Tipo de Archivo Inválido",
                            "El archivo '" + file.getName() + "' no es un tipo permitido. Tipos aceptados: " + tiposArchivoField.getText());
                    continue; // Saltar este archivo
                }

                // Validación de tamaño de archivo
                if (file.length() > model.getTamanoMaximoArchivoMB() * 1024 * 1024) {
                    showAlert(Alert.AlertType.WARNING, "Tamaño de Archivo Excedido",
                            "El archivo '" + file.getName() + "' excede el tamaño máximo permitido de " + model.getTamanoMaximoArchivoMB() + " MB.");
                    continue; // Saltar este archivo
                }

                archivosAdjuntos.add(file); // Añadir el archivo a la lista observable
            }
            success = true;
            actualizarListaArchivosUI(); // Actualizar la vista después de añadir archivos
        }
        event.setDropCompleted(success);
        event.consume();
        archivosContainer.setStyle("-fx-border-color: #CCC; -fx-border-style: dashed; -fx-border-width: 2; -fx-background-color: #F9F9F9; -fx-background-radius: 5;"); // Restaurar estilo
    }

    @FXML
    private void handleDragExited(DragEvent event) {
        archivosContainer.setStyle("-fx-border-color: #CCC; -fx-border-style: dashed; -fx-border-width: 2; -fx-background-color: #F9F9F9; -fx-background-radius: 5;"); // Restaurar estilo
        event.consume();
    }

    // Método de utilidad para validar el tipo de archivo
    private boolean validarTipoArchivo(String fileName, String acceptedExtensions) {
        if (acceptedExtensions == null || acceptedExtensions.trim().isEmpty() || acceptedExtensions.equals("*.*")) {
            return true; // Si no hay restricciones o es comodín, todos son válidos
        }
        String[] allowedExtensions = acceptedExtensions.toLowerCase().split(",");
        String fileExtension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            fileExtension = fileName.substring(dotIndex + 1).toLowerCase();
        }

        for (String ext : allowedExtensions) {
            if (ext.trim().equals(fileExtension)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    private void handleCancel() {
        // Get the current stage (window)
        Stage stage = (Stage) nombreTareaField.getScene().getWindow();
        // Close the stage
        stage.close();
    }
}


