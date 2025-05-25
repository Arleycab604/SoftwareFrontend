package com.SaberPro.SoftwareFront.Controllers.AccionesMejora;

import com.SaberPro.SoftwareFront.Models.AsignarAccionModel; // Importa el modelo de asignación si es necesario para crear
import com.SaberPro.SoftwareFront.Models.AccionMejora; // Importa tu modelo AccionMejora (la clase simple de datos)
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList; // Importar ArrayList para la lista de archivos
import java.util.Comparator;
import java.util.List;
import java.util.UUID; // Para generar IDs simples
import java.util.stream.Collectors;

public class DetallesAccionController {

    @FXML private Label lblModuloTitle;
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbOrdenarPor;
    @FXML private ComboBox<String> cmbVista;
    @FXML private VBox vbContenedorAcciones;

    private String moduloSeleccionadoId; // El ID del módulo (ej. "INGLÉS")
    private ObservableList<AccionMejora> todasLasAcciones = FXCollections.observableArrayList();
    private ObservableList<AccionMejora> accionesFiltradasYOrdenadas = FXCollections.observableArrayList();

    public enum VistaTipo { LISTA, TARJETA }
    private VistaTipo vistaActual = VistaTipo.LISTA;

    @FXML
    public void initialize() {
        cmbOrdenarPor.setItems(FXCollections.observableArrayList("Nombre de la tarea (A-Z)", "Fecha de Entrega (Reciente)"));
        cmbOrdenarPor.getSelectionModel().selectFirst();

        cmbVista.setItems(FXCollections.observableArrayList("Lista", "Tarjeta"));
        cmbVista.getSelectionModel().selectFirst();

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltrosYOrden());
        cmbOrdenarPor.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltrosYOrden());
        cmbVista.valueProperty().addListener((obs, oldVal, newVal) -> cambiarVista(newVal));

        vbContenedorAcciones.setSpacing(10);
    }

    public void setModuloSeleccionado(String moduloId) {
        this.moduloSeleccionadoId = moduloId;
        // Aquí ajustamos el título para que muestre el nombre del módulo
        lblModuloTitle.setText("Acciones de Mejora para " + moduloId); // Asumiendo que moduloId es el nombre legible

        //cargarAccionesDeMejoraParaModulo(moduloId);
        aplicarFiltrosYOrden();
    }


    /*private void cargarAccionesDeMejoraParaModulo(String moduloId) {
        todasLasAcciones.clear();

        if ("COMUNICACIÓN ESCRITA".equals(moduloId)) {
            todasLasAcciones.add(new AccionMejora(
                    UUID.randomUUID().toString(), "Ensayo sobre Argumentación", "Escribir un ensayo persuasivo sobre un tema dado.",
                    "Instrucciones detalladas de formato y extensión...",
                    moduloId, "COMUNICACIÓN ESCRITA", "", // Reemplazado "SEMESTRE V" con ""
                    LocalDateTime.of(2025, 3, 21, 8, 0), LocalDateTime.of(2025, 3, 24, 23, 59),
                    LocalDateTime.of(2025, 4, 1, 23, 59), LocalDateTime.of(2025, 4, 4, 8, 0),
                    5, 10, ".docx,.pdf", 2025, "COM-ESC-001", "Activa",
                    false, true, new ArrayList<>()
            ));
            todasLasAcciones.add(new AccionMejora(
                    UUID.randomUUID().toString(), "Presentación Oral", "Preparar y exponer sobre un tema de investigación.",
                    "Se evaluará fluidez, contenido y uso de apoyos visuales.",
                    moduloId, "COMUNICACIÓN ESCRITA", "", // Reemplazado "SEMESTRE V" con ""
                    LocalDateTime.of(2025, 6, 5, 8, 0), LocalDateTime.of(2025, 6, 15, 17, 0),
                    null, null, 2, 20, ".pptx,.pdf", 2025, "COM-ESC-002", "Pendiente",
                    true, false, new ArrayList<>()
            ));
        } else if ("LECTURA CRÍTICA".equals(moduloId)) {
            todasLasAcciones.add(new AccionMejora(
                    UUID.randomUUID().toString(), "Análisis de Texto Filosófico", "Realizar un análisis crítico de un fragmento de filosofía.",
                    "Identificar la tesis, argumentos y contraargumentos.",
                    moduloId, "LECTURA CRÍTICA", "", // Reemplazado "SEMESTRE V" con ""
                    LocalDateTime.of(2025, 5, 15, 0, 0), LocalDateTime.of(2025, 5, 28, 23, 59),
                    null, null, 1, 5, ".pdf", 2025, "LEC-CRIT-001", "Activa",
                    false, true, new ArrayList<>()
            ));
        } else if ("INGLÉS".equals(moduloId)) {
            todasLasAcciones.add(new AccionMejora(
                    UUID.randomUUID().toString(), "Reporte de Libro en Inglés", "Escribir un resumen y opinión de un libro en inglés.",
                    "Debe ser al menos 500 palabras.",
                    moduloId, "INGLÉS", "", // Reemplazado "SEMESTRE V" con ""
                    LocalDateTime.of(2025, 5, 1, 0, 0), LocalDateTime.of(2025, 5, 25, 23, 59),
                    null, null, 1, 2, ".docx", 2025, "ING-001", "Activa",
                    false, true, new ArrayList<>()
            ));
            todasLasAcciones.add(new AccionMejora(
                    UUID.randomUUID().toString(), "Exposición Oral en Inglés", "Presentación de un tema libre en inglés.",
                    "Fluidez y pronunciación serán evaluadas.",
                    moduloId, "INGLÉS", "", // Reemplazado "SEMESTRE V" con ""
                    LocalDateTime.of(2025, 6, 1, 0, 0), LocalDateTime.of(2025, 6, 10, 23, 59),
                    null, null, 3, 15, ".pptx", 2025, "ING-002", "Pendiente",
                    true, false, new ArrayList<>()
            ));
        }
    }*/

    private void aplicarFiltrosYOrden() {
        String textoBusqueda = txtBuscar.getText() != null ? txtBuscar.getText().toLowerCase() : "";
        String ordenSeleccionado = cmbOrdenarPor.getSelectionModel().getSelectedItem();

        List<AccionMejora> tempAcciones = todasLasAcciones.stream()
                .filter(accion -> accion.getNombreTarea().toLowerCase().contains(textoBusqueda) ||
                        accion.getDescripcionGeneral().toLowerCase().contains(textoBusqueda))
                .collect(Collectors.toList());

        if ("Nombre de la tarea (A-Z)".equals(ordenSeleccionado)) {
            tempAcciones.sort(Comparator.comparing(AccionMejora::getNombreTarea));
        } else if ("Fecha de Entrega (Reciente)".equals(ordenSeleccionado)) {
            tempAcciones.sort(Comparator.comparing(AccionMejora::getFechaEntregaLimite, Comparator.nullsLast(Comparator.reverseOrder())));
        }

        accionesFiltradasYOrdenadas.setAll(tempAcciones);
        actualizarVistaAcciones();
    }

    private void cambiarVista(String nuevaVista) {
        if ("Lista".equals(nuevaVista)) {
            vistaActual = VistaTipo.LISTA;
        } else if ("Tarjeta".equals(nuevaVista)) {
            vistaActual = VistaTipo.TARJETA;
        }
        actualizarVistaAcciones();
    }

    private void actualizarVistaAcciones() {
        vbContenedorAcciones.getChildren().clear();

        if (vistaActual == VistaTipo.LISTA) {
            vbContenedorAcciones.setStyle("-fx-spacing: 5;");
            for (AccionMejora accion : accionesFiltradasYOrdenadas) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/AccionesMejora/TarjetaAccionMejora.fxml"));
                    VBox tarjetaRoot = loader.load();
                    TarjetaAccionMejoraController tarjetaController = loader.getController();
                    tarjetaController.setAccionMejora(accion);

                    tarjetaController.setOnEditCallback(() -> abrirFormularioAccionMejora(accion));

                    tarjetaRoot.setMaxWidth(Double.MAX_VALUE);
                    vbContenedorAcciones.getChildren().add(tarjetaRoot);
                } catch (IOException e) {
                    e.printStackTrace();
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar la tarjeta de acción.", "Error al cargar la vista para la acción: " + accion.getNombreTarea());
                }
            }
        } else if (vistaActual == VistaTipo.TARJETA) {
            vbContenedorAcciones.setStyle("-fx-spacing: 20; -fx-padding: 10;");
            GridPane gridPane = new GridPane();
            gridPane.setHgap(20);
            gridPane.setVgap(20);

            int col = 0;
            int row = 0;
            for (AccionMejora accion : accionesFiltradasYOrdenadas) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/AccionesMejora/TarjetaAccionMejora.fxml"));
                    VBox tarjetaRoot = loader.load();
                    TarjetaAccionMejoraController tarjetaController = loader.getController();
                    tarjetaController.setAccionMejora(accion);

                    tarjetaController.setOnEditCallback(() -> abrirFormularioAccionMejora(accion));

                    // Ajustar el tamaño de la tarjeta para la vista de cuadrícula
                    tarjetaRoot.setPrefWidth(300); // Ancho preferido para cada tarjeta
                    tarjetaRoot.setMaxWidth(300);   // Ancho máximo para cada tarjeta

                    gridPane.add(tarjetaRoot, col, row);
                    col++;
                    if (col == 3) { // 3 columnas por fila para un mejor diseño de cuadrícula
                        col = 0;
                        row++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar la tarjeta de acción.", "Error al cargar la vista para la acción: " + accion.getNombreTarea());
                }
            }
            vbContenedorAcciones.getChildren().add(gridPane);
        }
    }

    @FXML
    private void handleNuevaAccion() {
        abrirFormularioAccionMejora(null);
    }

    public void abrirFormularioAccionMejora(AccionMejora accionParaEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/AccionesMejora/AsignarAccion.fxml"));
            Parent root = loader.load();

            AsignarAccionController asignarController = loader.getController();

            if (accionParaEditar != null) {
                // Si es una edición, cargar los datos de AccionMejora en AsignarAccionModel
                // Esto es crucial para que el formulario se precargue con los datos existentes
                asignarController.cargarDatosParaEdicion(accionParaEditar);
            } else {
                // Si es una nueva acción, solo establecer el módulo
                asignarController.setModuloActual(moduloSeleccionadoId);
            }

            // Aquí pasamos una referencia a este controlador para que el formulario de "subir/editar" pueda llamarnos
            // y pedir que se refresque la lista de acciones.
            asignarController.setRefreshCallback(this::refreshAcciones);


            Stage stage = new Stage();
            stage.setTitle(accionParaEditar == null ? "Crear Nueva Acción de Mejora" : "Editar Acción de Mejora");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // La llamada a refreshAcciones() se hace ahora a través del callback desde SubirAccionMejoraController
            // cuando se guardan los cambios.
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir el formulario.", e.getMessage());
        }
    }

    public void refreshAcciones() {
        //cargarAccionesDeMejoraParaModulo(moduloSeleccionadoId);
        aplicarFiltrosYOrden();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Actualización", "Acciones de mejora actualizadas.", null);
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}