package com.SaberPro.SoftwareFront.Controllers.ConsultarHistoricos;

import com.SaberPro.SoftwareFront.Models.ReporteDTO;
import com.SaberPro.SoftwareFront.Models.InputQueryDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
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

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;
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
    // FIN MODIFICACIÓN


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mostrarVistaTablas(); // Esto es correcto para iniciar mostrando la tabla

        configurarComboBoxes(); // Inicializar ComboBoxes de filtro (izquierda)
        configurarRangeSliderPuntajeGlobal(); // Configurar RangeSlider para puntaje global
        configurarRangeSliderPuntajeModulo(); // Configurar RangeSlider para puntaje módulo
        configurarTipoModuloOptions(); // Configurar opciones de tipo de módulo (checkboxes)
        configurarColumnasTabla(); // Configurar columnas de la tabla
        cargarDatosIniciales(); // Cargar datos iniciales para la tabla

        // INICIO MODIFICACIÓN: Configurar ComboBoxes del gráfico y sus listeners
        configurarComboBoxesGrafico(); // Nuevo método para poblar los ComboBox del gráfico
        // Añadir listeners a los ComboBoxes del gráfico para actualizarlo
        comboAnioGrafico.valueProperty().addListener((obs, oldVal, newVal) -> actualizarGrafico());
        comboPeriodoGrafico.valueProperty().addListener((obs, oldVal, newVal) -> actualizarGrafico());
        comboModuloGrafico.valueProperty().addListener((obs, oldVal, newVal) -> actualizarGrafico());
        // FIN MODIFICACIÓN
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


    private void configurarRangeSliderPuntajeGlobal() {
        // ... (Tu código existente para RangeSlider) ...
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
        // ... (Tu código existente para RangeSlider) ...
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

                    // Agrupar los datos por módulo y calcular el puntaje promedio
                    Map<String, Double> puntajesPorModulo = resultados.stream()
                            .filter(r -> r.getPuntajeModulo() != 0) // Asegúrate de que el puntaje del módulo no sea nulo
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
    // FIN MODIFICACIÓN
}