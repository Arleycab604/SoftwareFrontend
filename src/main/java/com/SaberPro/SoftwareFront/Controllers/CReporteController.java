package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Models.InputQueryDTO;
import com.SaberPro.SoftwareFront.Models.ReporteDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.HttpEntity;
import java.io.File;

import org.apache.hc.client5.http.entity.EntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CReporteController {
    // Constantes para mensajes estáticos
    private static final String MSG_ARCHIVOS_SELECCIONADOS = "Archivo(s) seleccionados correctamente. Total: ";
    private static final String MSG_NO_SELECCIONADOS = "No se seleccionaron archivos. Intenta nuevamente.";
    private static final String MSG_DATOS_SUBIDOS = "¡Datos subidos exitosamente!";
    private static final String MSG_CANCELADO = "Operación cancelada.";

    private List<ReporteDTO> resultadosSubidos = new ArrayList<ReporteDTO>();
    @FXML
    private TableView<ReporteDTO> tablaReportesSubidos; // Tabla para vista previa (opcional por ahora)
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

    public void initialize(){
        // Inicializar la tabla
        configurarColumnasTabla();
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
        colPercentilModulo.setCellValueFactory(new PropertyValueFactory<>("percentilModulo"));
        colDesempeno.setCellValueFactory(new PropertyValueFactory<>("nivelDesempeno"));
        colNovedades.setCellValueFactory(new PropertyValueFactory<>("novedades"));
    }
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
                ObjectMapper objectMapper = new ObjectMapper();
                InputQueryDTO filtros = new InputQueryDTO();
                filtros.setYear(anio); filtros.setPeriodo(periodo);
                HttpResponse<String> resultadosData = BuildRequest.getInstance().POSTInputDTO("http://localhost:8080/SaberPro/reportes/Query",filtros);
                List<ReporteDTO> resultados = objectMapper.readValue(resultadosData.body(), new TypeReference<List<ReporteDTO>>() {});

                if(!resultadosSubidos.isEmpty()){
                    resultadosSubidos.clear();
                }
                assert resultadosSubidos != null;
                resultadosSubidos.addAll(resultados);
                tablaReportesSubidos.setItems((ObservableList<ReporteDTO>) resultadosSubidos);

                mensajeError.setText("Archivo subido exitosamente.");
                mensajeError.setStyle("-fx-text-fill: green;");
                System.out.println("DEBUG: Archivo subido exitosamente. Respuesta del servidor: " + responseBody);
            } else {
                mensajeError.setText("Error al subir el archivo. Servidor respondió: " + responseBody);
                mensajeError.setStyle("-fx-text-fill: red;");
                System.out.println("DEBUG: Error al subir el archivo. Código de estado: " + statusCode + ", Respuesta: " + responseBody);
            }
        } catch (NumberFormatException e) {
            mensajeError.setText("El año y el periodo deben ser números enteros.");
            mensajeError.setStyle("-fx-text-fill: red;");
            System.out.println("DEBUG: Error de formato en año o periodo. Detalles: " + e.getMessage());
        } catch (Exception e) {
            mensajeError.setText("Error al subir el archivo: " + e.getMessage());
            mensajeError.setStyle("-fx-text-fill: red;");
            System.out.println("DEBUG: Error al subir el archivo. Detalles: " + e.getMessage());
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
