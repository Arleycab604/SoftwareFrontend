package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Models.ReporteDTO;
import com.SaberPro.SoftwareFront.Models.InputQueryDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.controlsfx.control.RangeSlider;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class VReporteEController implements Initializable {
    private final int MIN_PUNTAJE = 0, MAX_PUNTAJE = 300;
    //Filtros
    @FXML private TextField txtDocumento;
    @FXML private ComboBox<Integer> cmbAnio;
    @FXML private ComboBox<String> cmbPeriodo;

    @FXML private RangeSlider rangeSliderPuntajeGlobal;

    @FXML private TextField txtPuntajeGlobalMin;
    @FXML private TextField txtPuntajeGlobalMax;

    @FXML private RangeSlider rangeSliderPuntajeModulo;
    @FXML private TextField txtPuntajeModuloMin;
    @FXML private TextField txtPuntajeModuloMax;

    @FXML private VBox tipoModuloCheckboxes;

    //Tabla
    private final List<CheckBox> tipoModuloOptions = new ArrayList<>(); // Lista de CheckBox
    private ObservableList<ReporteDTO> listaReportes = FXCollections.observableArrayList();
    @FXML private TableView<ReporteDTO> tablaReportes;

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

    @FXML private VBox filtersSection;
    @FXML private Button btnToggleFilters;
    @FXML private Button btnAplicarFiltros;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarComboBoxes(); // Inicializar ComboBoxes
        configurarRangeSliderPuntajeGlobal(); // Configurar RangeSlider para puntaje global
        configurarRangeSliderPuntajeModulo(); // Configurar RangeSlider para puntaje módulo
        configurarTipoModuloOptions(); // Configurar opciones de tipo de módulo
        configurarColumnasTabla(); // Configurar columnas de la tabla
        cargarDatosIniciales(); // Cargar datos iniciales
    }

    private void configurarRangeSliderPuntajeGlobal() {
        // Sincronizar RangeSlider con los campos de texto
        rangeSliderPuntajeGlobal.lowValueProperty().addListener((obs, oldVal, newVal) -> {
            txtPuntajeGlobalMin.setText(String.valueOf(newVal.intValue()));
        });
        rangeSliderPuntajeGlobal.highValueProperty().addListener((obs, oldVal, newVal) -> {
            txtPuntajeGlobalMax.setText(String.valueOf(newVal.intValue()));
        });

        // Actualizar RangeSlider cuando se editen los campos de texto
        txtPuntajeGlobalMin.textProperty().addListener((obs, oldVal, newVal) -> {
            if (esNumeroValido(newVal)) {
                rangeSliderPuntajeGlobal.setLowValue(Double.parseDouble(newVal));
            } else {
                txtPuntajeGlobalMin.setText(oldVal);
            }
        });
        txtPuntajeGlobalMax.textProperty().addListener((obs, oldVal, newVal) -> {
            if (esNumeroValido(newVal)) {
                rangeSliderPuntajeGlobal.setHighValue(Double.parseDouble(newVal));
            } else {
                txtPuntajeGlobalMax.setText(oldVal);
            }
        });
    }
    private void configurarRangeSliderPuntajeModulo() {
        // Sincronizar RangeSlider con los campos de texto
        rangeSliderPuntajeModulo.lowValueProperty().addListener((obs, oldVal, newVal) -> {
            txtPuntajeModuloMin.setText(String.valueOf(newVal.intValue()));
        });
        rangeSliderPuntajeModulo.highValueProperty().addListener((obs, oldVal, newVal) -> {
            txtPuntajeModuloMax.setText(String.valueOf(newVal.intValue()));
        });

        // Actualizar RangeSlider cuando se editen los campos de texto
        txtPuntajeModuloMin.textProperty().addListener((obs, oldVal, newVal) -> {
            if (esNumeroValido(newVal)) {
                rangeSliderPuntajeModulo.setLowValue(Double.parseDouble(newVal));
            } else {
                txtPuntajeModuloMin.setText(oldVal);
            }
        });
        txtPuntajeModuloMax.textProperty().addListener((obs, oldVal, newVal) -> {
            if (esNumeroValido(newVal)) {
                rangeSliderPuntajeModulo.setHighValue(Double.parseDouble(newVal));
            } else {
                txtPuntajeModuloMax.setText(oldVal);
            }
        });
    }

    private boolean esNumeroValido(String valor) {
        try { return Integer.parseInt(valor) >= MIN_PUNTAJE && Integer.parseInt(valor) <= MAX_PUNTAJE;
        } catch (NumberFormatException e) {return false;}
    }
    private void configurarComboBoxes() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Integer> years = new ArrayList<>();
        for (int i = 2003; i <= currentYear; i++) {
            years.add(i);
        }
        cmbAnio.setItems(FXCollections.observableArrayList(years));
        cmbPeriodo.setItems(FXCollections.observableArrayList("1", "2"));
    }

    private void configurarTipoModuloOptions() {
        List<String> modulos = List.of(
                "COMUNICACIÓN ESCRITA",
                "LECTURA CRÍTICA",
                "FORMULACIÓN DE PROYECTOS DE INGENIERÍA",
                "COMPETENCIAS CIUDADANAS",
                "INGLÉS",
                "DISEÑO DE SOFTWARE",
                "RAZONAMIENTO CUANTITATIVO",
                "PENSAMIENTO CIENTÍFICO - MATEMÁTICAS Y ESTADÍSTICA"
        );

        for (String modulo : modulos) {
            CheckBox checkBox = new CheckBox(modulo);
            tipoModuloCheckboxes.getChildren().add(checkBox); // Agregar al VBox
            tipoModuloOptions.add(checkBox); // Agregar a la lista
        }
    }
    // Método para configurar validación de puntajes (0 a 200)

    @FXML
    private void toggleFilters() {
        boolean isVisible = filtersSection.isVisible();
        filtersSection.setVisible(!isVisible);
        filtersSection.setManaged(!isVisible);
        btnToggleFilters.setText(isVisible ? "Mostrar Filtros" : "Ocultar Filtros");
    }
    @FXML
    private void aplicarFiltros() {
        listaReportes.clear();

        // Crear objeto de filtros
        InputQueryDTO filtros = new InputQueryDTO();

        // Validar y asignar valores solo si no son nulos o vacíos
        if (txtDocumento.getText() != null && !txtDocumento.getText().trim().isEmpty()) {
            filtros.setNombreUsuario(txtDocumento.getText().trim());
        }
        if (cmbAnio.getValue() != null) {
            filtros.setYear(cmbAnio.getValue());
        }
        if (cmbPeriodo.getValue() != null) {
            filtros.setPeriodo(Integer.parseInt(cmbPeriodo.getValue()));
        }
        if (rangeSliderPuntajeGlobal.getLowValue() >= MIN_PUNTAJE || rangeSliderPuntajeGlobal.getHighValue() <= MAX_PUNTAJE) {
            filtros.setPuntajeGlobalMinimo((int) rangeSliderPuntajeGlobal.getLowValue());
            filtros.setPuntajeGlobalMaximo((int) rangeSliderPuntajeGlobal.getHighValue());
        }
        if (rangeSliderPuntajeModulo.getLowValue() >= MIN_PUNTAJE || rangeSliderPuntajeModulo.getHighValue() <= MAX_PUNTAJE) {
            filtros.setPuntajeModuloMinimo((int) rangeSliderPuntajeModulo.getLowValue());
            filtros.setPuntajeModuloMaximo((int) rangeSliderPuntajeModulo.getHighValue());
        }
        List<String> modulosSeleccionados = tipoModuloOptions.stream()
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .toList();
        if (!modulosSeleccionados.isEmpty()) {
            filtros.setTipoModulo(String.join(",", modulosSeleccionados));
        }

        // Imprimir el objeto InputQueryDTO
        System.out.println("Filtros aplicados: " + filtros);

        // Enviar filtros al backend
        buscarReporteConFiltros(filtros);
    }
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
    // Método para cargar datos iniciales desde el backend
    private void cargarDatosIniciales() {
        /*
         * FALTA ALGO IMPORTANTE, sería recomendable guardar en memoria
         * los filtros generados por este usuario, de modo que los filtros se guarden mientras la app esté abierta
        */
        try {

            // Configurar la URL del backend
            InputQueryDTO filtros = new InputQueryDTO();
            listaReportes.forEach(System.out::println);
            HttpResponse<String> response = BuildRequest.getInstance().POSTInputDTO("http://localhost:8080/SaberPro/reportes/Query",filtros);

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
    private void buscarReporteConFiltros(InputQueryDTO filtros) {
        try {
            HttpResponse<String> response = BuildRequest.getInstance().POSTInputDTO("http://localhost:8080/SaberPro/reportes/Query",filtros);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(filtros);
            System.out.println("JSON enviado al backend: " + json);
            // Verificar el código de respuesta
            if (response.statusCode() == 200) {
                // Convertir la respuesta JSON a una lista de ReporteDTO
                List<ReporteDTO> resultados = objectMapper.readValue(response.body(), new TypeReference<List<ReporteDTO>>() {});

                // Actualizar la tabla con los resultados
                listaReportes.clear();
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
