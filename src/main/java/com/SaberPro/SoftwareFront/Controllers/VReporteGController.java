package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Models.InputFilterYearDTO;
import com.SaberPro.SoftwareFront.Models.InputQueryDTO;
import javafx.scene.control.ScrollPane;
import com.SaberPro.SoftwareFront.Models.ReporteYearDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.controlsfx.control.RangeSlider;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VReporteGController {
    private final int MIN_PUNTAJE = 0, MAX_PUNTAJE = 300;
    public VBox filtersSection;
    @FXML private RangeSlider rangeMediaPeriodo, rangeCoefVarPeriodo, rangeMediaModulo, rangeCoefVarModulo;
    private ObservableList<ReporteYearDTO> listaReportes = FXCollections.observableArrayList();
    @FXML private TextField txtMediaPeriodoMin;
    @FXML private TextField txtMediaPeriodoMax;
    @FXML private TextField txtCoefVarPeriodoMin;
    @FXML private TextField txtCoefVarPeriodoMax;
    @FXML private TextField txtMediaModuloMin;
    @FXML private TextField txtMediaModuloMax;
    @FXML private TextField txtCoefVarModuloMin;
    @FXML private TextField txtCoefVarModuloMax;
    @FXML private VBox tipoModuloCheckboxes;

    @FXML private Button btnToggleFilters;
    @FXML private Button btnAplicarFiltros;

    @FXML private ComboBox<Integer> cmbYear;
    @FXML private ComboBox<Integer> cmbPeriodo;

    @FXML private TableView<ReporteYearDTO> tablaReportes;

    @FXML private TableColumn<ReporteYearDTO, Integer> colYear, colPeriodo;
    @FXML private TableColumn<ReporteYearDTO, Double> colMediaPeriodo, colVarianzaPeriodo, colCoefVarPeriodo;
    @FXML private TableColumn<ReporteYearDTO, String> colTipoModulo;
    @FXML private TableColumn<ReporteYearDTO, Double> colMediaModulo, colVarianzaModulo, colCoefVarModulo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        configurarTabla();
        configurarTipoModuloOptions();
        configurarRangeSliders();
        configurarBotonesFiltros();
        cargarDatosIniciales();
    }
    private void configurarBotonesFiltros() {
        btnToggleFilters.setOnAction(e -> filtersSection.setVisible(!filtersSection.isVisible()));
        btnAplicarFiltros.setOnAction(e -> aplicarFiltros());
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
            tipoModuloCheckboxes.getChildren().add(checkBox);
        }
    }

    private void configurarRangeSliders() {
        configurarRangeSliderMediaPeriodo();
        configurarRangeSliderCoefVarPeriodo();
        configurarRangeSliderMediaModulo();
        configurarRangeSliderCoefVarModulo();

        //configurarRangeSliderCoefVar(rangeCoefVarPeriodo);
        //configurarRangeSlider(rangeMediaModulo);
        //configurarRangeSliderCoefVar(rangeCoefVarModulo);
    }
    private void configurarRangeSliderMediaPeriodo() {
        // Sincronizar RangeSlider con los campos de texto
        rangeMediaPeriodo.lowValueProperty().addListener((obs, oldVal, newVal) -> {
            txtMediaPeriodoMin.setText(String.valueOf(newVal.intValue()));
        });
        rangeMediaPeriodo.highValueProperty().addListener((obs, oldVal, newVal) -> {
            txtMediaPeriodoMax.setText(String.valueOf(newVal.intValue()));
        });
    }
    private void configurarRangeSliderCoefVarPeriodo() {
        // Sincronizar RangeSlider con los campos de texto
        rangeCoefVarPeriodo.lowValueProperty().addListener((obs, oldVal, newVal) -> {
            txtCoefVarPeriodoMin.setText(String.valueOf(newVal.intValue()));
        });
        rangeMediaPeriodo.highValueProperty().addListener((obs, oldVal, newVal) -> {
            txtCoefVarPeriodoMax.setText(String.valueOf(newVal.intValue()));
        });
    }
    private void configurarRangeSliderMediaModulo() {
        // Sincronizar RangeSlider con los campos de texto
        rangeMediaModulo.lowValueProperty().addListener((obs, oldVal, newVal) -> {
            txtMediaModuloMin.setText(String.valueOf(newVal.intValue()));
        });
        rangeMediaModulo.highValueProperty().addListener((obs, oldVal, newVal) -> {
            txtMediaModuloMax.setText(String.valueOf(newVal.intValue()));
        });
    }
    private void configurarRangeSliderCoefVarModulo() {
        // Sincronizar RangeSlider con los campos de texto
        rangeCoefVarModulo.lowValueProperty().addListener((obs, oldVal, newVal) -> {
            txtCoefVarModuloMin.setText(String.valueOf(newVal.intValue()));
        });
        rangeMediaPeriodo.highValueProperty().addListener((obs, oldVal, newVal) -> {
            txtCoefVarModuloMax.setText(String.valueOf(newVal.intValue()));
        });
    }

    private void configurarRangeSlider(RangeSlider slider) {
        slider.setMin(MIN_PUNTAJE);      // o el mínimo realista que necesites
        slider.setMax(MAX_PUNTAJE);    // ajusta según tus datos
        slider.setLowValue(40);
        slider.setHighValue(270);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
    }
    private void configurarRangeSliderCoefVar(RangeSlider slider) {
        slider.setMin(0.00);
        slider.setMax(1.00);
        slider.setLowValue(0.10);
        slider.setHighValue(0.90);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(0.1);
        slider.setBlockIncrement(0.01);
    }

    private void configurarTabla() {
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        colMediaPeriodo.setCellValueFactory(new PropertyValueFactory<>("mediaPeriodo"));
        colVarianzaPeriodo.setCellValueFactory(new PropertyValueFactory<>("varianzaPeriodo"));
        colCoefVarPeriodo.setCellValueFactory(new PropertyValueFactory<>("coefVarPeriodo"));
        colTipoModulo.setCellValueFactory(new PropertyValueFactory<>("tipoModulo"));
        colMediaModulo.setCellValueFactory(new PropertyValueFactory<>("mediaModulo"));
        colVarianzaModulo.setCellValueFactory(new PropertyValueFactory<>("varianzaModulo"));
        colCoefVarModulo.setCellValueFactory(new PropertyValueFactory<>("coefVarModulo"));
    }
    private void cargarDatosIniciales() {
        /*
         * FALTA ALGO IMPORTANTE, sería recomendable guardar en memoria
         * los filtros generados por este usuario, de modo que los filtros se guarden mientras la app esté abierta
         */
        try {
            // Configurar la URL del backend
            InputQueryDTO filtros = new InputQueryDTO();
            listaReportes.forEach(System.out::println);
            HttpResponse<String> response = BuildRequest.getInstance().POSTInputDTO("http://localhost:8080/SaberPro/reporteYear/Query",filtros);

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<ReporteYearDTO> resultados = objectMapper.readValue(response.body(), new TypeReference<>() {});
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
    public void aplicarFiltros() {
        try {
            // Obtener valores de los filtros
            double mediaPeriodoMin = rangeMediaPeriodo.getLowValue();
            double mediaPeriodoMax = rangeMediaPeriodo.getHighValue();
            double coefVarPeriodoMin = rangeCoefVarPeriodo.getLowValue();
            double coefVarPeriodoMax = rangeCoefVarPeriodo.getHighValue();
            double mediaModuloMin = rangeMediaModulo.getLowValue();
            double mediaModuloMax = rangeMediaModulo.getHighValue();
            double coefVarModuloMin = rangeCoefVarModulo.getLowValue();
            double coefVarModuloMax = rangeCoefVarModulo.getHighValue();

            // Obtener tipos de módulo seleccionados
            List<String> tiposModulo = new ArrayList<>();
            tipoModuloCheckboxes.getChildren().forEach(node -> {
                if (node instanceof CheckBox && ((CheckBox) node).isSelected()) {
                    tiposModulo.add(((CheckBox) node).getText());
                }
            });

            String tipoModulo = String.join(",", tiposModulo);

            // Crear el objeto InputFilterYearDTO
            InputFilterYearDTO filtros = new InputFilterYearDTO();
            filtros.setMediaPeriodoMin(mediaPeriodoMin);
            filtros.setMediaPeriodoMax(mediaPeriodoMax);
            filtros.setCoefVarPeriodoMin(coefVarPeriodoMin);
            filtros.setCoefVarPeriodoMax(coefVarPeriodoMax);
            filtros.setMediaModuloMin(mediaModuloMin);
            filtros.setMediaModuloMax(mediaModuloMax);
            filtros.setCoefVarModuloMin(coefVarModuloMin);
            filtros.setCoefVarModuloMax(coefVarModuloMax);
            filtros.setTipoModulo(tipoModulo);

            // Enviar la solicitud al backend
            HttpResponse<String> response = BuildRequest.getInstance().POSTJson(
                    "http://localhost:8080/SaberPro/reporteYear/Query",
                    new ObjectMapper().writeValueAsString(filtros)
            );

            if (response.statusCode() == 200) {
                // Procesar la respuesta
                List<ReporteYearDTO> reportes = new ObjectMapper().readValue(
                        response.body(),
                        new TypeReference<>() {}
                );
                tablaReportes.getItems().setAll(reportes);
            } else {
                mostrarAlerta("Error", "No se pudieron cargar los datos. Código: " + response.statusCode());
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private ScrollPane filtersScrollPane; // Necesitas inyectar el ScrollPane

    @FXML
    private Separator separatorFiltros; // Necesitas inyectar el Separator

    @FXML
    private void toggleFilters() {
        boolean isVisible = filtersSection.isVisible();
        filtersSection.setVisible(!isVisible);
        filtersSection.setManaged(!isVisible);
        filtersScrollPane.setVisible(!isVisible);
        filtersScrollPane.setManaged(!isVisible);
        if (separatorFiltros != null) {
            separatorFiltros.setVisible(isVisible);
            separatorFiltros.setManaged(isVisible);
        }
        btnToggleFilters.setText(isVisible ? "Mostrar Filtros" : "Ocultar Filtros");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}