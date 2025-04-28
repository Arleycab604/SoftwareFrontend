package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Classes.ReporteDTO;
import com.SaberPro.SoftwareFront.InputQueryDTO;
import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class VReporteEController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox<Integer> cmbAnio;
    @FXML
    private ComboBox<String> cmbPeriodo;
    @FXML
    private TextField txtPuntajeGlobalMin;
    @FXML
    private TextField txtPuntajeGlobalMax;
    @FXML
    private ComboBox<String> cmbTipoModulo;
    @FXML
    private ComboBox<String> cmbNivelDesempeno;
    @FXML
    private Button btnBuscar;
    @FXML
    private ComboBox<String> cmbOrdenarPor;
    @FXML
    private ComboBox<String> cmbDireccion;
    @FXML
    private TableView<ReporteDTO> tablaReportes;
    @FXML
    private TableColumn<ReporteDTO, String> colNombre;
    @FXML
    private TableColumn<ReporteDTO, String> colRegistro;
    @FXML
    private TableColumn<ReporteDTO, Integer> colAnio;
    @FXML
    private TableColumn<ReporteDTO, String> colPeriodo;
    @FXML
    private TableColumn<ReporteDTO, Double> colPuntajeGlobal;
    @FXML
    private TableColumn<ReporteDTO, String> colTipoModulo;
    @FXML
    private TableColumn<ReporteDTO, String> colDesempeno;

    private ObservableList<ReporteDTO> listaReportes = FXCollections.observableArrayList();

    // Aquí simularíamos la lista de todos los reportes disponibles
    private List<ReporteDTO> todosLosReportes;
    @FXML
    private TableColumn<ReporteDTO, Long> colDocumento;
    @FXML
    private TableColumn<ReporteDTO, String> colNombrePrograma;
    @FXML
    private TableColumn<ReporteDTO, String> colGrupoDeReferencia;
    @FXML
    private TableColumn<ReporteDTO, String> colNumeroRegistro;
    @FXML
    private TableColumn<ReporteDTO, Integer> colYear;
    @FXML
    private TableColumn<ReporteDTO, Integer> colPercentilGlobal;
    @FXML
    private TableColumn<ReporteDTO, Integer> colPuntajeModulo;
    @FXML
    private TableColumn<ReporteDTO, Integer> colPercentilModulo;
    @FXML
    private TableColumn<ReporteDTO, String> colNovedades;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar las columnas existentes
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colRegistro.setCellValueFactory(new PropertyValueFactory<>("numeroRegistro"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        colPuntajeGlobal.setCellValueFactory(new PropertyValueFactory<>("puntajeGlobal"));
        colTipoModulo.setCellValueFactory(new PropertyValueFactory<>("tipoModulo"));
        colDesempeno.setCellValueFactory(new PropertyValueFactory<>("nivelDesempeno"));

        // Configurar las nuevas columnas
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colNombrePrograma.setCellValueFactory(new PropertyValueFactory<>("nombrePrograma"));
        colGrupoDeReferencia.setCellValueFactory(new PropertyValueFactory<>("grupoDeReferencia"));
        colNumeroRegistro.setCellValueFactory(new PropertyValueFactory<>("numeroRegistro"));
        colPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colPercentilGlobal.setCellValueFactory(new PropertyValueFactory<>("percentilGlobal"));
        colPuntajeModulo.setCellValueFactory(new PropertyValueFactory<>("puntajeModulo"));
        colPercentilModulo.setCellValueFactory(new PropertyValueFactory<>("percentilModulo"));
        colNovedades.setCellValueFactory(new PropertyValueFactory<>("novedades"));

        // Cargar datos iniciales desde el backend
        cargarDatosIniciales();
    }

    // Método para cargar datos iniciales desde el backend
    private void cargarDatosIniciales() {
        try {
            // Configurar la URL del backend
            String url = "http://localhost:8080/api/reportes/filtrar";
            listaReportes.forEach(System.out::println);
            // Crear el cliente HTTP
            HttpClient httpClient = HttpClient.newHttpClient();
            String encodedAuth = Base64.getEncoder().encodeToString("user:password".getBytes());

            // Crear una solicitud vacía para obtener todos los reportes
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic "+ Base64.getEncoder().encodeToString("user:password".getBytes()))
                    .POST(HttpRequest.BodyPublishers.ofString("{}")) // Sin filtros
                    .build();

            // Enviar la solicitud y procesar la respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<ReporteDTO> resultados = objectMapper.readValue(response.body(), new TypeReference<List<ReporteDTO>>() {});
                listaReportes.addAll(resultados);
                tablaReportes.setItems(listaReportes);
            } else {
                System.err.println("Error al cargar datos iniciales: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error al conectar con el backend: " + e.getMessage());
        }
    }

    @FXML
    protected void buscarReporte() {
        listaReportes.clear(); // Limpiar la tabla antes de cada búsqueda

        // Crear el objeto InputQueryDTO con los filtros
        InputQueryDTO filtros = new InputQueryDTO();
        filtros.setYear(cmbAnio.getValue() != null ? cmbAnio.getValue() : 0);
        filtros.setPeriodo(cmbPeriodo.getValue() != null ? Integer.parseInt(cmbPeriodo.getValue()) : 0);
        filtros.setNombreUsuario(txtNombre.getText().trim().isEmpty() ? null : txtNombre.getText().trim());
        filtros.setPuntajeGlobalMinimo(!txtPuntajeGlobalMin.getText().isEmpty() ? (int) Double.parseDouble(txtPuntajeGlobalMin.getText()) : 0);
        filtros.setPuntajeGlobalMaximo(!txtPuntajeGlobalMax.getText().isEmpty() ? (int) Double.parseDouble(txtPuntajeGlobalMax.getText()) : 0);
        filtros.setTipoModulo(cmbTipoModulo.getValue() != null ? cmbTipoModulo.getValue() : null);
        filtros.setNivelDesempeno(cmbNivelDesempeno.getValue() != null ? cmbNivelDesempeno.getValue() : null);

        try {
            // Configurar la URL del backend
            String url = "http://localhost:8080/api/reportes/filtrar";

            // Crear el cliente HTTP
            HttpClient httpClient = HttpClient.newHttpClient();

            // Convertir los filtros a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(filtros);

            // Crear la solicitud HTTP POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic "+ Base64.getEncoder().encodeToString("user:password".getBytes()))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar el código de respuesta
            if (response.statusCode() == 200) {
                // Convertir la respuesta JSON a una lista de ReporteDTO
                List<ReporteDTO> resultados = objectMapper.readValue(response.body(), new TypeReference<List<ReporteDTO>>() {});

                // Actualizar la tabla con los resultados
                listaReportes.addAll(resultados);
                tablaReportes.setItems(listaReportes);
            } else {
                System.err.println("Error en la respuesta del servidor: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Error al realizar la solicitud HTTP: " + e.getMessage());
        }
    }
}
