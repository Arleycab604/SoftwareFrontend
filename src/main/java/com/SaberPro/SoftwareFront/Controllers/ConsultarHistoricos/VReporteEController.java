package com.SaberPro.SoftwareFront.Controllers.ConsultarHistoricos;

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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart; // Necesario para BarChart
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
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors; // Necesario para Collectors

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

    @FXML private ScrollPane filtersSection;
    @FXML private Button btnToggleFilters;
    @FXML private Button btnAplicarFiltros;
    @FXML private VBox vistaTabla;
    @FXML private VBox vistaGrafico;
    @FXML private Button btnTablas;
    @FXML private Button btnGraficos;

    // INICIO MODIFICACIÓN: Para el Label de vista actual
    @FXML private Label lblVistaActual;
    // FIN MODIFICACIÓN

    // INICIO MODIFICACIÓN: Nombres de los ComboBoxes para el gráfico
    @FXML private ComboBox<Integer> comboAnioGrafico;
    @FXML private ComboBox<String> comboPeriodoGrafico;
    @FXML private ComboBox<String> comboModuloGrafico; // Para el tipo de módulo
    @FXML private javafx.scene.chart.BarChart<String, Number> graficoBarras;
    @FXML private CategoryAxis ejeX;
    @FXML private NumberAxis ejeY;

    //Yo soy rabiosa - shakira ft. no se quien
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mostrarVistaTablas();

        configurarComboBoxes();
        configurarTipoModuloOptions();
        configurarColumnasTabla();

        // Usar la función general para sincronizar sliders y campos de texto
        sincronizarRangeSliderConTextFields(rangeSliderPuntajeGlobal, txtPuntajeGlobalMin, txtPuntajeGlobalMax);
        sincronizarRangeSliderConTextFields(rangeSliderPuntajeModulo, txtPuntajeModuloMin, txtPuntajeModuloMax);

        cargarDatosIniciales();
        configurarComboBoxesGrafico();
        comboAnioGrafico.valueProperty().addListener((obs, oldVal, newVal) -> actualizarGrafico());
        comboPeriodoGrafico.valueProperty().addListener((obs, oldVal, newVal) -> actualizarGrafico());
        comboModuloGrafico.valueProperty().addListener((obs, oldVal, newVal) -> actualizarGrafico());
    }

    private void sincronizarRangeSliderConTextFields(RangeSlider rangeSlider, TextField txtMin, TextField txtMax) {
        // Actualizar campos de texto al mover slider
        rangeSlider.lowValueProperty().addListener((obs, oldVal, newVal) -> {
            txtMin.setText(String.valueOf(newVal.intValue()));
        });
        rangeSlider.highValueProperty().addListener((obs, oldVal, newVal) -> {
            txtMax.setText(String.valueOf(newVal.intValue()));
        });

        // Agregar listeners a TextFields con la función generalizada
        agregarListenerTextField(txtMin,
                rangeSlider::getHighValue,
                rangeSlider::setLowValue,
                true);

        agregarListenerTextField(txtMax,
                rangeSlider::getLowValue,
                rangeSlider::setHighValue,
                false);
    }
    private void agregarListenerTextField(TextField txtField, Supplier<Double> getValorSliderContrario,
                                          Consumer<Double> setValorSliderPropio,
                                          boolean esMinimo) {
        txtField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (esNumeroValido(newVal)) {
                double val = Double.parseDouble(newVal);
                double valorContrario = getValorSliderContrario.get();
                boolean valido = esMinimo ? (val <= valorContrario) : (val >= valorContrario);
                if (valido) {
                    setValorSliderPropio.accept(val);
                } else {
                    txtField.setText(oldVal);
                }
            } else {
                txtField.setText(oldVal);
            }
        });
    }
    @FXML
    private void mostrarVistaTablas() {
        vistaTabla.setVisible(true);
        vistaGrafico.setVisible(false);
        // INICIO MODIFICACIÓN: Actualizar lblVistaActual
        lblVistaActual.setText("TABLAS");
        // FIN MODIFICACIÓN
    }

    @FXML
    private void mostrarVistaGraficos() {
        vistaTabla.setVisible(false);
        vistaGrafico.setVisible(true);
        // INICIO MODIFICACIÓN: Actualizar lblVistaActual y cargar/actualizar gráfico
        lblVistaActual.setText("GRÁFICOS");
        actualizarGrafico(); // Llama a la función para cargar/actualizar el gráfico cuando se cambia a esta vista
        // FIN MODIFICACIÓN
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

    // INICIO MODIFICACIÓN: Nuevo método para configurar los ComboBoxes del gráfico
    private void configurarComboBoxesGrafico() {
        // Poblar ComboBoxes de año y periodo con los mismos datos que los filtros
        comboAnioGrafico.setItems(cmbAnio.getItems());
        comboPeriodoGrafico.setItems(cmbPeriodo.getItems());

        // Poblar ComboBox de módulo con los mismos tipos de módulo que los checkboxes
        List<String> modulos = tipoModuloOptions.stream()
                .map(CheckBox::getText)
                .collect(Collectors.toList());
        comboModuloGrafico.setItems(FXCollections.observableArrayList(modulos));

        // Establecer un valor predeterminado si es necesario (ej. el primer elemento)
        if (!comboAnioGrafico.getItems().isEmpty()) {
            comboAnioGrafico.getSelectionModel().selectFirst();
        }
        if (!comboPeriodoGrafico.getItems().isEmpty()) {
            comboPeriodoGrafico.getSelectionModel().selectFirst();
        }
        if (!comboModuloGrafico.getItems().isEmpty()) {
            comboModuloGrafico.getSelectionModel().selectFirst();
        }
    }
    // FIN MODIFICACIÓN

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
        colDesempeno.setCellValueFactory(new PropertyValueFactory<>("desempeno"));
        colNovedades.setCellValueFactory(new PropertyValueFactory<>("novedades"));
        tablaReportes.setItems(listaReportes);
    }

    // Método para cargar datos iniciales desde el backend
    private void cargarDatosIniciales() {
        buscarReporteConFiltros(new InputQueryDTO());
    }
    private void buscarReporteConFiltros(InputQueryDTO filtros) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String requestBody = mapper.writeValueAsString(filtros);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/SaberPro/reportes/Query"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<ReporteDTO> reportes = mapper.readValue(response.body(), new TypeReference<>() {});
                listaReportes.setAll(reportes);
            } else {
                mostrarAlerta("Error", "No se pudieron obtener los reportes.");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al procesar los filtros.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al comunicarse con el servidor.");
        }
    }

    // INICIO MODIFICACIÓN: Método para actualizar el gráfico
    private void actualizarGrafico() {
        graficoBarras.getData().clear(); // Limpiar datos anteriores del gráfico

        Integer anioSeleccionado = comboAnioGrafico.getValue();
        String periodoSeleccionado = comboPeriodoGrafico.getValue();
        String moduloSeleccionado = comboModuloGrafico.getValue();

        // Solo actualizar si hay selecciones válidas
        if (anioSeleccionado != null && periodoSeleccionado != null && moduloSeleccionado != null) {
            // Prepara los filtros para la consulta al backend
            InputQueryDTO filtrosGrafico = new InputQueryDTO();
            filtrosGrafico.setYear(anioSeleccionado);
            filtrosGrafico.setPeriodo(Integer.parseInt(periodoSeleccionado));
            filtrosGrafico.setTipoModulo(moduloSeleccionado);

            // Puedes añadir otros filtros si el gráfico necesita datos más específicos (ej. puntaje global min/max)
            // Para este ejemplo, solo usaremos año, periodo y módulo para el gráfico.

            try {
                HttpResponse<String> response = BuildRequest.getInstance().POSTInputDTO("http://localhost:8080/SaberPro/reportes/Query", filtrosGrafico);
                ObjectMapper objectMapper = new ObjectMapper();

                if (response.statusCode() == 200) {
                    List<ReporteDTO> resultados = objectMapper.readValue(response.body(), new TypeReference<List<ReporteDTO>>() {});

                    // Derly como te fokin odio para eso estan los reportes generales
                    Map<String, Double> puntajesPorModulo = resultados.stream()
                            .filter(r -> r.getPuntajeModulo() != 0) //Esta linea es redundante, los puntajes no pueden ser nulos
                            .collect(Collectors.groupingBy(
                                    ReporteDTO::getTipoModulo,
                                    Collectors.averagingDouble(ReporteDTO::getPuntajeModulo)
                            ));

                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName("Puntaje Promedio por Módulo"); // Nombre de la serie para la leyenda

                    // Si necesitas un solo módulo, podrías filtrar y agregar solo el puntaje promedio de ese módulo
                    // O si quieres que muestre todos los módulos para el año/periodo seleccionado:
                    for (Map.Entry<String, Double> entry : puntajesPorModulo.entrySet()) {
                        series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                    }

                    // Si quieres que el gráfico muestre específicamente el promedio del módulo seleccionado
                    if (puntajesPorModulo.containsKey(moduloSeleccionado)) {
                        XYChart.Series<String, Number> selectedModuloSeries = new XYChart.Series<>();
                        selectedModuloSeries.setName("Puntaje Promedio de " + moduloSeleccionado);
                        selectedModuloSeries.getData().add(new XYChart.Data<>(moduloSeleccionado, puntajesPorModulo.get(moduloSeleccionado)));
                        graficoBarras.getData().add(selectedModuloSeries);
                    } else {
                        // Si el módulo seleccionado no tiene datos para esa combinación, puedes mostrar un mensaje o dejarlo vacío
                        System.out.println("No hay datos para el módulo seleccionado: " + moduloSeleccionado);
                    }


                } else {
                    System.err.println("Error al cargar datos para el gráfico: " + response.statusCode() + " - " + response.body());
                }
            } catch (Exception e) {
                System.err.println("Error al solicitar datos para el gráfico: " + e.getMessage());
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}