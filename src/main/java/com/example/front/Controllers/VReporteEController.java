package com.example.front.Controllers;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
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
    private TableView<Reporte> tablaReportes;

    @FXML
    private TableColumn<Reporte, String> colNombre;

    @FXML
    private TableColumn<Reporte, String> colRegistro;

    @FXML
    private TableColumn<Reporte, Integer> colAnio;

    @FXML
    private TableColumn<Reporte, String> colPeriodo;

    @FXML
    private TableColumn<Reporte, Double> colPuntajeGlobal;

    @FXML
    private TableColumn<Reporte, String> colTipoModulo;

    @FXML
    private TableColumn<Reporte, String> colDesempeno;

    private ObservableList<Reporte> listaReportes = FXCollections.observableArrayList();

    // Aquí simularíamos la lista de todos los reportes disponibles
    private List<Reporte> todosLosReportes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar las opciones de los ComboBox
        cmbAnio.setItems(FXCollections.observableArrayList(obtenerAniosDisponibles()));
        cmbPeriodo.setItems(FXCollections.observableArrayList("1", "2", "Anual")); // Ejemplo de periodos
        cmbTipoModulo.setItems(FXCollections.observableArrayList(obtenerTiposDeModulo()));
        cmbNivelDesempeno.setItems(FXCollections.observableArrayList(obtenerNivelesDesempeno()));
        cmbOrdenarPor.setItems(FXCollections.observableArrayList("Nombre", "N° Registro", "Año", "Periodo", "Puntaje Global", "Tipo de Módulo"));
        cmbDireccion.setItems(FXCollections.observableArrayList("Ascendente", "Descendente"));
        cmbDireccion.setValue("Ascendente"); // Valor por defecto

        // Inicializar las columnas de la tabla
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colRegistro.setCellValueFactory(new PropertyValueFactory<>("registro"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        colPuntajeGlobal.setCellValueFactory(new PropertyValueFactory<>("puntajeGlobal"));
        colTipoModulo.setCellValueFactory(new PropertyValueFactory<>("tipoModulo"));
        colDesempeno.setCellValueFactory(new PropertyValueFactory<>("desempeno"));

        // Simular la carga de todos los reportes (reemplazar con tu lógica real)
        todosLosReportes = cargarTodosLosReportes();
    }

    @FXML
    protected void buscarReporte() {
        listaReportes.clear(); // Limpiar la tabla antes de cada búsqueda
        List<Reporte> resultadosFiltrados = new ArrayList<>(todosLosReportes);

        // Aplicar filtros
        String nombreFiltro = txtNombre.getText().trim().toLowerCase();
        if (!nombreFiltro.isEmpty()) {
            resultadosFiltrados = resultadosFiltrados.stream()
                    .filter(reporte -> reporte.getNombre().toLowerCase().contains(nombreFiltro))
                    .collect(Collectors.toList());
        }

        Integer anioFiltro = cmbAnio.getValue();
        if (anioFiltro != null) {
            resultadosFiltrados = resultadosFiltrados.stream()
                    .filter(reporte -> reporte.getAnio().equals(anioFiltro))
                    .collect(Collectors.toList());
        }

        String periodoFiltro = cmbPeriodo.getValue();
        if (periodoFiltro != null && !periodoFiltro.isEmpty()) {
            resultadosFiltrados = resultadosFiltrados.stream()
                    .filter(reporte -> reporte.getPeriodo().equals(periodoFiltro))
                    .collect(Collectors.toList());
        }

        try {
            double minPuntaje = txtPuntajeGlobalMin.getText().trim().isEmpty() ? Double.MIN_VALUE : Double.parseDouble(txtPuntajeGlobalMin.getText());
            double maxPuntaje = txtPuntajeGlobalMax.getText().trim().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(txtPuntajeGlobalMax.getText());
            resultadosFiltrados = resultadosFiltrados.stream()
                    .filter(reporte -> reporte.getPuntajeGlobal() >= minPuntaje && reporte.getPuntajeGlobal() <= maxPuntaje)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            // Manejar la excepción si los campos de puntaje no son números válidos
            System.err.println("Error al parsear puntaje global: " + e.getMessage());
        }

        String tipoModuloFiltro = cmbTipoModulo.getValue();
        if (tipoModuloFiltro != null && !tipoModuloFiltro.isEmpty()) {
            resultadosFiltrados = resultadosFiltrados.stream()
                    .filter(reporte -> reporte.getTipoModulo().equals(tipoModuloFiltro))
                    .collect(Collectors.toList());
        }

        String nivelDesempenoFiltro = cmbNivelDesempeno.getValue();
        if (nivelDesempenoFiltro != null && !nivelDesempenoFiltro.isEmpty()) {
            resultadosFiltrados = resultadosFiltrados.stream()
                    .filter(reporte -> determinarNivelDesempeno(reporte.getPuntajeGlobal(), reporte.getTipoModulo()).equals(nivelDesempenoFiltro))
                    .collect(Collectors.toList());
        }

        // Aplicar ordenamiento
        String ordenarPor = cmbOrdenarPor.getValue();
        String direccion = cmbDireccion.getValue();

        Comparator<Reporte> comparador = null;

        switch (ordenarPor) {
            case "Nombre":
                comparador = Comparator.comparing(Reporte::getNombre);
                break;
            case "N° Registro":
                comparador = Comparator.comparing(Reporte::getRegistro);
                break;
            case "Año":
                comparador = Comparator.comparing(Reporte::getAnio);
                break;
            case "Periodo":
                comparador = Comparator.comparing(Reporte::getPeriodo);
                break;
            case "Puntaje Global":
                comparador = Comparator.comparing(Reporte::getPuntajeGlobal);
                break;
            case "Tipo de Módulo":
                comparador = Comparator.comparing(Reporte::getTipoModulo);
                break;
        }

        if (comparador != null) {
            if ("Descendente".equals(direccion)) {
                resultadosFiltrados.sort(comparador.reversed());
            } else {
                resultadosFiltrados.sort(comparador);
            }
        }

        listaReportes.addAll(resultadosFiltrados);
        tablaReportes.setItems(listaReportes);
    }

    // Métodos para obtener las listas de opciones para los ComboBox
    private List<Integer> obtenerAniosDisponibles() {
        // Reemplazar con la lógica para obtener los años únicos de tus reportes
        return List.of(2022, 2023, 2024, 2025);
    }

    private List<String> obtenerTiposDeModulo() {
        // Reemplazar con la lógica para obtener los tipos de módulo únicos
        return List.of("Matemáticas", "Ciencias", "Lenguaje", "Historia");
    }

    private List<String> obtenerNivelesDesempeno() {
        // Reemplazar con la lógica para obtener los niveles de desempeño (esto podría depender del tipo de módulo)
        return List.of("Bajo", "Medio", "Alto", "Excelente");
    }

    // Método para determinar el nivel de desempeño basado en el puntaje y el tipo de módulo
    private String determinarNivelDesempeno(double puntaje, String tipoModulo) {
        // Aquí debes implementar la lógica específica para cada tipo de módulo
        // Esto es solo un ejemplo muy básico
        if (tipoModulo.equals("Matemáticas")) {
            if (puntaje < 60) return "Bajo";
            if (puntaje < 80) return "Medio";
            return "Alto";
        } else if (tipoModulo.equals("Ciencias")) {
            if (puntaje < 50) return "Bajo";
            if (puntaje < 75) return "Medio";
            return "Alto";
        }
        return "Medio"; // Nivel por defecto
    }

    // Clase interna para representar los datos de un Reporte (debes adaptarla a tu modelo de datos)
    public static class Reporte {
        private String nombre;
        private String registro;
        private Integer anio;
        private String periodo;
        private Double puntajeGlobal;
        private String tipoModulo;
        private String desempeño;

        public Reporte(String nombre, String registro, Integer anio, String periodo, Double puntajeGlobal, String tipoModulo) {
            this.nombre = nombre;
            this.registro = registro;
            this.anio = anio;
            this.periodo = periodo;
            this.puntajeGlobal = puntajeGlobal;
            this.tipoModulo = tipoModulo;
            this.desempeño = ""; // Se determinará dinámicamente
        }

        // Getters y setters para todos los atributos
        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getRegistro() {
            return registro;
        }

        public void setRegistro(String registro) {
            this.registro = registro;
        }

        public Integer getAnio() {
            return anio;
        }

        public void setAnio(Integer anio) {
            this.anio = anio;
        }

        public String getPeriodo() {
            return periodo;
        }

        public void setPeriodo(String periodo) {
            this.periodo = periodo;
        }

        public Double getPuntajeGlobal() {
            return puntajeGlobal;
        }

        public void setPuntajeGlobal(Double puntajeGlobal) {
            this.puntajeGlobal = puntajeGlobal;
        }

        public String getTipoModulo() {
            return tipoModulo;
        }

        public void setTipoModulo(String tipoModulo) {
            this.tipoModulo = tipoModulo;
        }

        public String getDesempeno() {
            return desempeño;
        }

        public void setDesempeno(String desempeño) {
            this.desempeño = desempeño;
        }
    }

    // Método simulado para cargar todos los reportes (reemplazar con tu lógica real)
    private List<Reporte> cargarTodosLosReportes() {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(new Reporte("Ana Pérez", "REG001", 2023, "1", 85.5, "Matemáticas"));
        reportes.add(new Reporte("Juan Gómez", "REG002", 2023, "2", 70.0, "Ciencias"));
        reportes.add(new Reporte("Luisa Vargas", "REG003", 2024, "Anual", 92.1, "Lenguaje"));
        reportes.add(new Reporte("Carlos López", "REG004", 2022, "1", 55.8, "Matemáticas"));
        reportes.add(new Reporte("Sofía Díaz", "REG005", 2024, "2", 88.9, "Ciencias"));
        reportes.add(new Reporte("Pedro Ruiz", "REG006", 2023, "1", 62.3, "Lenguaje"));
        reportes.add(new Reporte("Ana Pérez", "REG007", 2024, "1", 78.0, "Ciencias"));
        // ... más reportes
        return reportes;
    }
}