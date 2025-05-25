package com.SaberPro.SoftwareFront.Controllers.Cumplimiento;

import com.SaberPro.SoftwareFront.Models.AccionMejora; // Importamos AccionMejora
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox; // Importa HBox para la vista de tarjeta/lista
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.time.LocalDateTime; // Necesario para AccionMejora
import java.time.format.DateTimeFormatter; // Para formatear fechas en las sub-vistas
import java.util.Comparator;
import java.util.List;
import java.util.UUID; // Para generar IDs simulados
import java.util.stream.Collectors;

public class DetalleCumplimientoController {

    @FXML private Label lblTareaTitle;
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbOrdenarPor;
    @FXML private ComboBox<String> cmbVista;
    @FXML private VBox vbContenedorAsignaciones; // Este VBox contendrá las Asignaciones

    private String moduloSeleccionadoNombre;
    private ObservableList<AccionMejora> todasLasAsignaciones = FXCollections.observableArrayList();
    private ObservableList<AccionMejora> asignacionesFiltradasYOrdenadas = FXCollections.observableArrayList();

    public enum VistaTipo { LISTA, TARJETA }
    private VistaTipo vistaActual = VistaTipo.LISTA;

    @FXML
    public void initialize() {
        // Inicializar ComboBoxes
        cmbOrdenarPor.setItems(FXCollections.observableArrayList("Nombre (A-Z)", "Fecha Límite (Próximas)", "Estado"));
        cmbOrdenarPor.getSelectionModel().selectFirst();

        cmbVista.setItems(FXCollections.observableArrayList("Lista", "Tarjeta"));
        cmbVista.getSelectionModel().selectFirst();

        // Añadir listeners para filtrar y ordenar
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltrosYOrden());
        cmbOrdenarPor.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltrosYOrden());
        cmbVista.valueProperty().addListener((obs, oldVal, newVal) -> cambiarVista(newVal));

        vbContenedorAsignaciones.setSpacing(10); // Espacio entre cada elemento de asignación

        // Cargar datos de prueba iniciales
        //cargarAsignacionesSimuladas();
    }

    // Método llamado desde el controlador anterior para establecer el módulo seleccionado
    public void setModuloSeleccionado(String moduloNombre) {
        this.moduloSeleccionadoNombre = moduloNombre;
        lblTareaTitle.setText("Asignaciones para: " + moduloNombre);
        cargarAsignacionesParaModulo(moduloNombre); // Cargar datos específicos de este módulo
    }

    // --- Carga de Datos y Filtrado ---
/*
    // Simula la carga de todas las asignaciones (objetos AccionMejora).
    // En una aplicación real, esto provendría de tu servicio de backend.
    private void cargarAsignacionesSimuladas() {
        todasLasAsignaciones.clear(); // Limpiar datos existentes

        // Añadir algunos objetos AccionMejora de prueba que representan asignaciones
        // Usando el constructor simplificado (el que añadí a tu clase AccionMejora)
        todasLasAsignaciones.add(new AccionMejora(UUID.randomUUID().toString(), "Ensayo sobre Shakespeare", "Escribir un ensayo analítico sobre Hamlet.", "LITERATURA", LocalDateTime.of(2025, 6, 10, 23, 59), "Pendiente"));
        todasLasAsignaciones.add(new AccionMejora(UUID.randomUUID().toString(), "Ejercicios de Cálculo I", "Resolver los problemas 1 al 10 del capítulo 3.", "MATEMÁTICAS", LocalDateTime.of(2025, 6, 5, 18, 0), "Completada"));
        todasLasAsignaciones.add(new AccionMejora(UUID.randomUUID().toString(), "Presentación Grupal", "Preparar diapositivas para la exposición de física.", "FÍSICA", LocalDateTime.of(2025, 6, 15, 10, 0), "En Revisión"));
        todasLasAsignaciones.add(new AccionMejora(UUID.randomUUID().toString(), "Lectura Cap. 5", "Leer el capítulo 5 de 'Historia Universal'.", "HISTORIA", LocalDateTime.of(2025, 6, 12, 23, 59), "Pendiente"));
        todasLasAsignaciones.add(new AccionMejora(UUID.randomUUID().toString(), "Laboratorio #3", "Realizar el experimento de química y presentar informe.", "QUÍMICA", LocalDateTime.of(2025, 6, 20, 17, 0), "Pendiente"));
        todasLasAsignaciones.add(new AccionMejora(UUID.randomUUID().toString(), "Proyecto Programación", "Desarrollar un CRUD simple en Java.", "PROGRAMACIÓN", LocalDateTime.of(2025, 7, 1, 23, 59), "Pendiente"));
        todasLasAsignaciones.add(new AccionMejora(UUID.randomUUID().toString(), "Test Modulo Inglés", "Test de vocabulario unidad 1 y 2", "INGLÉS", LocalDateTime.of(2025, 5, 28, 14, 0), "Pendiente"));
        todasLasAsignaciones.add(new AccionMejora(UUID.randomUUID().toString(), "Grammar Practice", "Complete grammar exercises from workbook.", "INGLÉS", LocalDateTime.of(2025, 6, 3, 23, 59), "Pendiente"));
        todasLasAsignaciones.add(new AccionMejora(UUID.randomUUID().toString(), "Vocabulary Quiz", "Prepare for the vocabulary quiz on Chapter 3.", "INGLÉS", LocalDateTime.of(2025, 6, 7, 10, 0), "Pendiente"));

        aplicarFiltrosYOrden(); // Aplicar filtros/ordenar inicialmente
    }*/

    // Este método filtra los objetos AccionMejora cargados según el módulo seleccionado.
    private void cargarAsignacionesParaModulo(String moduloNombre) {
        //cargarAsignacionesSimuladas(); // Siempre recargar todos los datos simulados primero para esta simulación

        List<AccionMejora> filteredByModule = todasLasAsignaciones.stream()
                .filter(accion -> accion.getModuloAsignadoNombre() != null &&
                        accion.getModuloAsignadoNombre().equalsIgnoreCase(moduloNombre))
                .collect(Collectors.toList());

        todasLasAsignaciones.setAll(filteredByModule); // Actualizar la lista maestra
        aplicarFiltrosYOrden(); // Volver a aplicar filtros/ordenar para los nuevos datos del módulo
    }

    private void aplicarFiltrosYOrden() {
        String textoBusqueda = txtBuscar.getText() != null ? txtBuscar.getText().toLowerCase() : "";
        String ordenSeleccionado = cmbOrdenarPor.getSelectionModel().getSelectedItem();

        List<AccionMejora> tempAsignaciones = todasLasAsignaciones.stream()
                .filter(accion -> accion.getNombreTarea().toLowerCase().contains(textoBusqueda) ||
                        accion.getDescripcionGeneral().toLowerCase().contains(textoBusqueda))
                .collect(Collectors.toList());

        switch (ordenSeleccionado) {
            case "Nombre (A-Z)":
                tempAsignaciones.sort(Comparator.comparing(AccionMejora::getNombreTarea));
                break;
            case "Fecha Límite (Próximas)":
                // Ordenar por fecha límite, nulos al final (para sin fecha de vencimiento), luego orden natural (más próximos primero)
                tempAsignaciones.sort(Comparator.comparing(AccionMejora::getFechaEntregaLimite, Comparator.nullsLast(Comparator.naturalOrder())));
                break;
            case "Estado":
                // Ordenar por estado (alfabético para "Pendiente", "Completada", "En Revisión")
                tempAsignaciones.sort(Comparator.comparing(AccionMejora::getEstado));
                break;
            default:
                // Ordenamiento predeterminado (ej., por nombre como alternativa)
                tempAsignaciones.sort(Comparator.comparing(AccionMejora::getNombreTarea));
                break;
        }

        asignacionesFiltradasYOrdenadas.setAll(tempAsignaciones);
        actualizarVistaAsignaciones();
    }

    private void cambiarVista(String nuevaVista) {
        if ("Lista".equals(nuevaVista)) {
            vistaActual = VistaTipo.LISTA;
        } else if ("Tarjeta".equals(nuevaVista)) {
            vistaActual = VistaTipo.TARJETA;
        }
        actualizarVistaAsignaciones();
    }

    // --- Generación Dinámica de UI ---

    private void actualizarVistaAsignaciones() {
        vbContenedorAsignaciones.getChildren().clear(); // Limpiar entradas existentes

        if (asignacionesFiltradasYOrdenadas.isEmpty()) {
            Label noResultsLabel = new Label("No hay asignaciones que coincidan con los criterios.");
            noResultsLabel.setStyle("-fx-text-fill: #777; -fx-font-size: 1.1em; -fx-padding: 20px;");
            vbContenedorAsignaciones.getChildren().add(noResultsLabel);
            return;
        }

        for (AccionMejora accion : asignacionesFiltradasYOrdenadas) {
            try {
                FXMLLoader loader;
                Parent itemView;

                if (vistaActual == VistaTipo.TARJETA) {
                    // Carga el FXML para la vista de tarjeta
                    loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/Cumplimiento/TarjetaCumplimiento.fxml"));
                    itemView = loader.load();
                    // Configura los datos de la AccionMejora en la vista de tarjeta
                    configureTarjetaAsignacion(itemView, accion);
                } else { // Vista de Lista
                    // Carga el FXML para la vista de lista
                    loader = new FXMLLoader(getClass().getResource("com/SaberPro/SoftwareFront/Cumplimiento/TarjetaCumplimiento.fxml"));
                    itemView = loader.load();
                    // Configura los datos de la AccionMejora en la vista de lista
                    configureListaAsignacion(itemView, accion);
                }
                vbContenedorAsignaciones.getChildren().add(itemView);
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista de asignación.", e.getMessage());
            }
        }
    }

    // --- Métodos de configuración para los elementos internos del FXML ---
    // (Estos reemplazan los controladores separados para Lista/Tarjeta)

    private void configureListaAsignacion(Parent view, AccionMejora accion) {
        // Asignaciones de FXML IDs dentro del HBox/Lista
        Label lblNombreTarea = (Label) view.lookup("#lblNombreTarea");
        Label lblModuloAsignado = (Label) view.lookup("#lblModuloAsignado");
        Label lblFechaLimite = (Label) view.lookup("#lblFechaLimite");
        Label lblEstado = (Label) view.lookup("#lblEstado");

        if (lblNombreTarea != null) lblNombreTarea.setText(accion.getNombreTarea());
        if (lblModuloAsignado != null) lblModuloAsignado.setText(accion.getModuloAsignadoNombre());
        if (lblFechaLimite != null) {
            if (accion.getFechaEntregaLimite() != null) {
                lblFechaLimite.setText("Fecha Límite: " + accion.getFechaEntregaLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            } else {
                lblFechaLimite.setText("Fecha Límite: N/A");
            }
        }
        if (lblEstado != null) {
            lblEstado.setText("Estado: " + accion.getEstado());
            // Aplicar estilo basado en el estado
            lblEstado.getStyleClass().removeAll("status-pendiente", "status-completada", "status-revision");
            if (accion.getEstado() != null) {
                switch (accion.getEstado().toLowerCase()) {
                    case "pendiente":
                        lblEstado.getStyleClass().add("status-pendiente");
                        break;
                    case "completada":
                        lblEstado.getStyleClass().add("status-completada");
                        break;
                    case "en revisión":
                        lblEstado.getStyleClass().add("status-revision");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void configureTarjetaAsignacion(Parent view, AccionMejora accion) {
        // Asignaciones de FXML IDs dentro del VBox/Tarjeta
        Label lblNombreTarea = (Label) view.lookup("#lblNombreTarea");
        Label lblDescripcionGeneral = (Label) view.lookup("#lblDescripcionGeneral");
        Label lblModuloAsignado = (Label) view.lookup("#lblModuloAsignado");
        Label lblFechaLimite = (Label) view.lookup("#lblFechaLimite");
        Label lblEstado = (Label) view.lookup("#lblEstado");

        if (lblNombreTarea != null) lblNombreTarea.setText(accion.getNombreTarea());
        if (lblDescripcionGeneral != null) lblDescripcionGeneral.setText(accion.getDescripcionGeneral());
        if (lblModuloAsignado != null) lblModuloAsignado.setText("Módulo: " + accion.getModuloAsignadoNombre());
        if (lblFechaLimite != null) {
            if (accion.getFechaEntregaLimite() != null) {
                lblFechaLimite.setText("Fecha Límite: " + accion.getFechaEntregaLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            } else {
                lblFechaLimite.setText("Fecha Límite: N/A");
            }
        }
        if (lblEstado != null) {
            lblEstado.setText("Estado: " + accion.getEstado());
            // Aplicar estilo basado en el estado
            lblEstado.getStyleClass().removeAll("status-pendiente", "status-completada", "status-revision");
            if (accion.getEstado() != null) {
                switch (accion.getEstado().toLowerCase()) {
                    case "pendiente":
                        lblEstado.getStyleClass().add("status-pendiente");
                        break;
                    case "completada":
                        lblEstado.getStyleClass().add("status-completada");
                        break;
                    case "en revisión":
                        lblEstado.getStyleClass().add("status-revision");
                        break;
                    default:
                        break;
                }
            }
        }
    }


    // Método de utilidad para mostrar alertas
    private void mostrarAlerta(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}