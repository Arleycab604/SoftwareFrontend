package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Models.InputQueryDTO;
import com.SaberPro.SoftwareFront.Models.ReporteDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Year; // Necesario para obtener el año actual
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CReporteController {
    // Constantes para mensajes estáticos
    private static final String MSG_ARCHIVOS_SELECCIONADOS = "Archivo(s) seleccionados correctamente. Total: ";
    private static final String MSG_NO_SELECCIONADOS = "No se seleccionaron archivos. Intenta nuevamente.";
    private static final String MSG_DATOS_SUBIDOS = "¡Datos subidos exitosamente!";
    private static final String MSG_CANCELADO = "Operación cancelada.";

    @FXML
    private TableView<ReporteDTO> tablaReportesSubidos;
    @FXML
    private Label mensajeConfirmacion;
    @FXML
    private Button botonSeleccionarArchivos;
    @FXML
    private Button botonSubirDatos;
    @FXML
    private Label mensajeError;

    // --- ANTES ERAN TextField, AHORA SON ComboBox PERO MANTIENEN LOS MISMOS FX:ID ---
    @FXML
    private ComboBox<String> campoAnio; // Tipo cambiado de TextField a ComboBox<String>
    @FXML
    private ComboBox<String> campoPeriodo; // Tipo cambiado de TextField a ComboBox<String>
    // ----------------------------------------------------------------------------------

    private File archivoSeleccionado;

    // Métodos privados para mostrar mensajes (reducen duplicación)
    private void mostrarMensajeExito(String mensaje) {
        mensajeConfirmacion.setText(mensaje);
        mensajeConfirmacion.setVisible(true);
        mensajeError.setVisible(false);
    }

    public void initialize(){
        // Inicializar la tabla
        configurarColumnasTabla();
        // --- Nuevo: Inicializar los ComboBox con los valores ---
        inicializarComboBoxes();
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

    // --- Nuevo método para inicializar los ComboBox ---
    private void inicializarComboBoxes() {

        int currentYear = Year.now().getValue();
        List<String> years = new ArrayList<>();
        for (int year = 2003; year <= currentYear; year++) { // si lo quiere expandir currentYear+2 pa 2027
            years.add(String.valueOf(year));
        }
        campoAnio.setItems(FXCollections.observableArrayList(years));

        // Inicializar campoPeriodo (ej: 1, 2)
        List<String> periodos = new ArrayList<>();
        periodos.add("1"); // Primer semestre/periodo
        periodos.add("2"); // Segundo semestre/periodo
        campoPeriodo.setItems(FXCollections.observableArrayList(periodos));
    }
    // ----------------------------------------------------

    @FXML
    public void handleSeleccionarArchivos() {
        FileChooser fileChooser = new FileChooser();
        // Puedes añadir aquí filtros para Excel si vas a soportar ambos
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Archivos Excel (XLSX)", "*.xlsx") // Si usas Apache POI para Excel
        );
        archivoSeleccionado = fileChooser.showOpenDialog(new Stage());

        // Habilitar o deshabilitar el botón de subir según la selección
        if (archivoSeleccionado != null) {
            botonSubirDatos.setDisable(false);
            mensajeError.setText(""); // Limpiar mensaje de error si se selecciona archivo
        } else {
            botonSubirDatos.setDisable(true);
            mensajeError.setText("No se seleccionó ningún archivo. Intenta nuevamente.");
        }
    }

    @FXML private TableColumn<ReporteDTO, Long> colDocumento;
    @FXML private TableColumn<ReporteDTO, String> colNombre;
    @FXML private TableColumn<ReporteDTO, String> colRegistro;
    @FXML private TableColumn<ReporteDTO, Integer> colAnio;
    @FXML private TableColumn<ReporteDTO, String> colPeriodo;
    @FXML private TableColumn<ReporteDTO, Double> colPuntajeGlobal;
    @FXML private TableColumn<ReporteDTO, String> colTipoModulo;
    @FXML private TableColumn<ReporteDTO, String> colDesempeno;
    @FXML private TableColumn<ReporteDTO, String> colNombrePrograma;
    @FXML private TableColumn<ReporteDTO, String> colGrupoDeReferencia;
    @FXML private TableColumn<ReporteDTO, Integer> colPercentilGlobal;
    @FXML private TableColumn<ReporteDTO, Integer> colPuntajeModulo;
    @FXML private TableColumn<ReporteDTO, Integer> colPercentilModulo;
    @FXML private TableColumn<ReporteDTO, String> colNovedades;

    private void configurarColumnasTabla() {
        // Configurar columnas con los atributos de ReporteDTO
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colRegistro.setCellValueFactory(new PropertyValueFactory<>("numeroRegistro"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        colNombrePrograma.setCellValueFactory(new PropertyValueFactory<>("nombrePrograma"));
        colGrupoDeReferencia.setCellValueFactory(new PropertyValueFactory<>("grupoDeReferencia"));
        colPuntajeGlobal.setCellValueFactory(new PropertyValueFactory<>("puntajeGlobal"));
        colPercentilGlobal.setCellValueFactory(new PropertyValueFactory<>("percentilGlobal"));
        colTipoModulo.setCellValueFactory(new PropertyValueFactory<>("tipoModulo"));
        colPuntajeModulo.setCellValueFactory(new PropertyValueFactory<>("puntajeModulo"));
        colPercentilModulo.setCellValueFactory(new PropertyValueFactory<>("percentilModulo")); // Corregido: .setCellValueFactory
        colDesempeno.setCellValueFactory(new PropertyValueFactory<>("nivelDesempeno"));
        colNovedades.setCellValueFactory(new PropertyValueFactory<>("novedades"));
    }

    @FXML
    public void handleSubirDatos() {
        if (archivoSeleccionado == null) {
            mostrarMensajeError("No se ha seleccionado ningún archivo.");
            System.out.println("DEBUG: No se seleccionó ningún archivo para subir.");
            return;
        }

        // --- Obtener valores de los ComboBox (utilizando los mismos fx:id) ---
        String anioSeleccionado = campoAnio.getSelectionModel().getSelectedItem();
        String periodoSeleccionado = campoPeriodo.getSelectionModel().getSelectedItem();

        if (anioSeleccionado == null || periodoSeleccionado == null) {
            mostrarMensajeError("Debe seleccionar el año y el periodo.");
            System.out.println("DEBUG: Año o periodo no seleccionados.");
            return;
        }
        // ---------------------------------------------------------------------

        try {
            int anio = Integer.parseInt(anioSeleccionado);
            int periodo = Integer.parseInt(periodoSeleccionado);

            String url = "http://localhost:8080/SaberPro/upload";
            Map<String, String> params = Map.of(
                    "year", String.valueOf(anio),
                    "periodo", String.valueOf(periodo)
            );

            // Llamar al método de BuildRequest para subir el archivo
            HttpResponse<String> response = BuildRequest.getInstance().uploadFile(url, archivoSeleccionado, params);

            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode == 200) {
                InputQueryDTO filtros = new InputQueryDTO();
                filtros.setYear(anio); filtros.setPeriodo(periodo);
                HttpResponse<String> resultadosData = BuildRequest.getInstance().POSTInputDTO("http://localhost:8080/SaberPro/reportes/Query",filtros);
                List<ReporteDTO> resultados = new ObjectMapper().readValue(resultadosData.body(), new TypeReference<List<ReporteDTO>>() {});
                ObservableList<ReporteDTO> resultadosSubidos = FXCollections.observableArrayList(resultados);
                tablaReportesSubidos.setItems(resultadosSubidos);

                mostrarMensajeExito("Archivo subido exitosamente.");
                System.out.println("DEBUG: Archivo subido exitosamente. Respuesta del servidor: " + responseBody);
            } else {
                mostrarMensajeError("Error al subir el archivo. Servidor respondió: " + responseBody);
                System.out.println("DEBUG: Error al subir el archivo. Código de estado: " + statusCode + ", Respuesta: " + responseBody);
            }
        } catch (NumberFormatException e) {
            mostrarMensajeError("Error de formato en año o periodo.");
            System.out.println("DEBUG: Error de formato en año o periodo. Detalles: " + e.getMessage());
        } catch (Exception e) {
            mostrarMensajeError("Error al subir el archivo: " + e.getMessage());
            System.out.println("DEBUG: Error al subir el archivo. Detalles: " + e.getMessage());
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        logConsola("Acción cancelada por el usuario.");
        mostrarMensajeError(MSG_CANCELADO);
        botonSubirDatos.setDisable(true);

        campoAnio.getSelectionModel().clearSelection(); // Limpia la selección, mostrando el promptText
        campoPeriodo.getSelectionModel().clearSelection(); // Limpia la selección, mostrando el promptText
        archivoSeleccionado = null;
    }
}