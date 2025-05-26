package com.SaberPro.SoftwareFront.Controllers.ConsultarHistoricos;

import com.SaberPro.SoftwareFront.Models.ReporteDTO;
import com.SaberPro.SoftwareFront.Models.InputQueryDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.TokenManager;
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
import java.net.URL;
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
    @FXML private ComboBox<String> cmbTipoModulo;

    // INICIO MODIFICACIÓN: Para el Label de vista actual
    @FXML private Label lblVistaActual;
    // FIN MODIFICACIÓN

    // INICIO MODIFICACIÓN: Nombres de los ComboBoxes para el gráfico
    @FXML private ComboBox<Integer> comboAnioGrafico;
    @FXML private ComboBox<String> comboPeriodoGrafico;
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

        // Establecer un valor predeterminado si es necesario (ej. el primer elemento)
        if (!comboAnioGrafico.getItems().isEmpty()) {
            comboAnioGrafico.getSelectionModel().selectFirst();
        }
        if (!comboPeriodoGrafico.getItems().isEmpty()) {
            comboPeriodoGrafico.getSelectionModel().selectFirst();
        }
    }

    // MODIFIED: Now configures the ComboBox cmbTipoModulo
    private void configurarTipoModuloOptions() {
        List<String> modulos = new ArrayList<>(List.of( // Use ArrayList to allow adding "Todos los módulos"
                "COMUNICACIÓN ESCRITA",
                "LECTURA CRÍTICA",
                "FORMULACIÓN DE PROYECTOS DE INGENIERÍA",
                "COMPETENCIAS CIUDADANAS",
                "INGLÉS",
                "DISEÑO DE SOFTWARE",
                "RAZONAMIENTO CUANTITATIVO",
                "PENSAMIENTO CIENTÍFICO - MATEMÁTICAS Y ESTADÍSTICA"
        ));
        // Add an option for "Todos los módulos"
        modulos.add(0, "Todos los módulos");
        cmbTipoModulo.setItems(FXCollections.observableArrayList(modulos));
    }

    // FIN MODIFICACIÓN


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
        if(TokenManager.getTipoUsuario().equals("ESTUDIANTE")) {
            filtros.setNombreUsuario(TokenManager.getNombreUsuario());
        }
        if(txtDocumento.getText() != null && !txtDocumento.getText().trim().isEmpty()) {
            filtros.setDocumento(txtDocumento.getText().trim());
        }
        if (cmbAnio.getValue() != null) {
            filtros.setYear(cmbAnio.getValue());
        }
        if (cmbPeriodo.getValue() != null) {
            filtros.setPeriodo(Integer.parseInt(cmbPeriodo.getValue()));
        }
        if (rangeSliderPuntajeGlobal.getLowValue() >= MIN_PUNTAJE || rangeSliderPuntajeGlobal.getHighValue() <= MAX_PUNTAJE) {
            filtros.setPuntajeGlobalMinimo((int) rangeSliderPuntajeGlobal.getLowValue());
            filtros.setPuntajeGlobalMaximo((int) rangeSliderPuntajeGlobal.getHighValue() == 0 ? 300 : (int) rangeSliderPuntajeGlobal.getHighValue());
        }
        if (rangeSliderPuntajeModulo.getLowValue() >= MIN_PUNTAJE || rangeSliderPuntajeModulo.getHighValue() <= MAX_PUNTAJE) {
            filtros.setPuntajeModuloMinimo((int) rangeSliderPuntajeModulo.getLowValue());
            filtros.setPuntajeModuloMaximo((int) rangeSliderPuntajeModulo.getHighValue() == 0 ? 300 : (int) rangeSliderPuntajeModulo.getHighValue());
        }

        // MODIFIED: Get the selected value from the ComboBox
        String moduloSeleccionado = cmbTipoModulo.getValue();
        if (moduloSeleccionado != null && !moduloSeleccionado.isEmpty() && !"Todos los módulos".equals(moduloSeleccionado)) {
            filtros.setTipoModulo(moduloSeleccionado);
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
        colDesempeno.setCellValueFactory(new PropertyValueFactory<>("nivelDesempeno"));
        colNovedades.setCellValueFactory(new PropertyValueFactory<>("novedades"));
        tablaReportes.setItems(listaReportes);
    }

    // Método para cargar datos iniciales desde el backend
    private void cargarDatosIniciales() {
        if(TokenManager.getTipoUsuario().equals("ESTUDIANTE")) {
            InputQueryDTO filtros = new InputQueryDTO();
            filtros.setNombreUsuario(TokenManager.getNombreUsuario());
            buscarReporteConFiltros(filtros);
            return;
        }
        buscarReporteConFiltros(new InputQueryDTO());
    }
    private void buscarReporteConFiltros(InputQueryDTO filtros) {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Enviando filtros al backend: " + filtros);
        try {
            HttpResponse<String> response = BuildRequest.getInstance().POSTInputDTO("http://localhost:8080/SaberPro/reportes/Query"
                    ,filtros);

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
        graficoBarras.setTitle("Puntajes por Módulo: Estudiante vs. Promedio General"); // Asegurar título

        Integer anioSeleccionado = comboAnioGrafico.getValue();
        String periodoSeleccionado = comboPeriodoGrafico.getValue();

        // Validar que se haya seleccionado año y periodo
        if (anioSeleccionado == null || periodoSeleccionado == null) {
            mostrarAlerta("Información", "Por favor, seleccione un Año y un Periodo para visualizar el gráfico.");
            return;
        }

        // 1. Obtener los datos del estudiante
        String documentoEstudiante = txtDocumento.getText();
        if (documentoEstudiante == null || documentoEstudiante.trim().isEmpty()) {
            mostrarAlerta("Información", "Por favor, ingrese el *documento del estudiante* en la sección de filtros para ver el gráfico de comparación.");
            return;
        }

        InputQueryDTO filtrosEstudiante = new InputQueryDTO();
        filtrosEstudiante.setNombreUsuario(documentoEstudiante.trim());
        filtrosEstudiante.setYear(anioSeleccionado);
        filtrosEstudiante.setPeriodo(Integer.parseInt(periodoSeleccionado));
        // No filtrar por tipo de módulo aquí, queremos todos los módulos del estudiante

        // 2. Obtener los promedios generales (para el cálculo en el frontend)
        // Se realizará una consulta general sin el filtro de documento para obtener todos los reportes
        // de ese año y periodo, y así calcular los promedios.
        InputQueryDTO filtrosPromedios = new InputQueryDTO();
        filtrosPromedios.setYear(anioSeleccionado);
        filtrosPromedios.setPeriodo(Integer.parseInt(periodoSeleccionado));


        try {
            // Realizar las dos consultas al backend
            HttpResponse<String> responseEstudiante = BuildRequest.getInstance().POSTInputDTO("http://localhost:8080/SaberPro/reportes/Query", filtrosEstudiante);
            HttpResponse<String> responseGeneral = BuildRequest.getInstance().POSTInputDTO("http://localhost:8080/SaberPro/reportes/Query", filtrosPromedios);

            ObjectMapper objectMapper = new ObjectMapper();

            List<ReporteDTO> resultadosEstudiante = new ArrayList<>();
            if (responseEstudiante.statusCode() == 200) {
                resultadosEstudiante = objectMapper.readValue(responseEstudiante.body(), new TypeReference<List<ReporteDTO>>() {});
            } else {
                System.err.println("Error al cargar datos del estudiante para el gráfico: " + responseEstudiante.statusCode() + " - " + responseEstudiante.body());
                mostrarAlerta("Error", "No se pudieron obtener los datos del estudiante para el gráfico.");
                return;
            }

            List<ReporteDTO> resultadosGenerales = new ArrayList<>();
            if (responseGeneral.statusCode() == 200) {
                resultadosGenerales = objectMapper.readValue(responseGeneral.body(), new TypeReference<List<ReporteDTO>>() {});
            } else {
                System.err.println("Error al cargar datos generales para el gráfico: " + responseGeneral.statusCode() + " - " + responseGeneral.body());
                // Si hay un error al cargar promedios, se continuará solo con los datos del estudiante o se mostrará un 0.
            }

            // Calcular promedios de los resultados generales
            Map<String, Double> promediosGenerales = resultadosGenerales.stream()
                    // Assuming ReporteDTO.getPuntajeModulo() returns int, no null check needed.
                    .filter(r -> r.getPuntajeModulo() >= MIN_PUNTAJE && r.getPuntajeModulo() <= MAX_PUNTAJE)
                    .collect(Collectors.groupingBy(
                            ReporteDTO::getTipoModulo,
                            Collectors.averagingDouble(ReporteDTO::getPuntajeModulo)
                    ));

            // Configurar las series del gráfico
            XYChart.Series<String, Number> serieEstudiante = new XYChart.Series<>();
            serieEstudiante.setName("Puntaje del Estudiante");

            XYChart.Series<String, Number> seriePromedio = new XYChart.Series<>();
            seriePromedio.setName("Promedio General");

            // Lista de todos los módulos para asegurar el orden y la completitud en el gráfico
            List<String> todosLosModulos = List.of(
                    "COMUNICACIÓN ESCRITA",
                    "LECTURA CRÍTICA",
                    "FORMULACIÓN DE PROYECTOS DE INGENIERÍA",
                    "COMPETENCIAS CIUDADANAS",
                    "INGLÉS",
                    "DISEÑO DE SOFTWARE",
                    "RAZONAMIENTO CUANTITATIVO",
                    "PENSAMIENTO CIENTÍFICO - MATEMÁTICAS Y ESTADÍSTICA"
            );

            // Poblar las series
            for (String modulo : todosLosModulos) {
                // Puntaje del estudiante para este módulo
                Optional<ReporteDTO> reporteEstudianteModulo = resultadosEstudiante.stream()
                        .filter(r -> modulo.equals(r.getTipoModulo()))
                        .findFirst(); // Asume un solo reporte por estudiante por módulo

                // If getPuntajeModulo() returns primitive int, no null check is possible or needed.
                if (reporteEstudianteModulo.isPresent()) {
                    serieEstudiante.getData().add(new XYChart.Data<>(modulo, reporteEstudianteModulo.get().getPuntajeModulo()));
                } else {
                    serieEstudiante.getData().add(new XYChart.Data<>(modulo, 0));
                }

                // Promedio general para este módulo
                Double promedio = promediosGenerales.getOrDefault(modulo, 0.0); // 0.0 si no hay promedio para este módulo
                seriePromedio.getData().add(new XYChart.Data<>(modulo, promedio));
            }

            // Añadir las series al gráfico
            graficoBarras.getData().addAll(serieEstudiante, seriePromedio);

            // Configurar etiquetas del eje X para que no se superpongan
            ejeX.setLabel("Módulo");
            ejeX.setTickLabelRotation(90); // Rotar etiquetas para que quepan si son largas
            ejeY.setLabel("Puntaje");
            ejeY.setLowerBound(MIN_PUNTAJE);
            ejeY.setUpperBound(MAX_PUNTAJE);


        } catch (Exception e) {
            System.err.println("Error al solicitar datos para el gráfico: " + e.getMessage());
            mostrarAlerta("Error", "Ocurrió un error al cargar los datos del gráfico: " + e.getMessage());
        }
    }


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}