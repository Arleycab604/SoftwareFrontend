package com.example.front.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CReporteController {

    // Estudiante
    @FXML private TextField documentoField;
    @FXML private ComboBox<String> tipoDocumentoBox;
    @FXML private TextField nombreEstudianteField;
    @FXML private TextField ciudadField;

    // Reporte
    @FXML private TextField yearField;
    @FXML private TextField periodoField;
    @FXML private TextField puntajeGlobalField;
    @FXML private TextField percentilGlobalField;
    @FXML private TextField novedadesField;

    // Módulo
    @FXML private TextField tipoModuloField;
    @FXML private TextField puntajeModuloField;
    @FXML private TextField nivelDesempenoField;
    @FXML private TextField percentilNacionalField;

    // Botón
    @FXML private Button guardarButton;

    @FXML
    public void initialize() {
        tipoDocumentoBox.getSelectionModel().selectFirst(); // Selección inicial por defecto
    }

    @FXML
    private void guardarReporte() {
        String documento = documentoField.getText();
        String tipoDoc = tipoDocumentoBox.getValue();
        String nombreEstudiante = nombreEstudianteField.getText();
        String ciudad = ciudadField.getText();

        String year = yearField.getText();
        String periodo = periodoField.getText();
        String puntajeGlobal = puntajeGlobalField.getText();
        String percentilGlobal = percentilGlobalField.getText();
        String novedades = novedadesField.getText();

        String tipoModulo = tipoModuloField.getText();
        String puntajeModulo = puntajeModuloField.getText();
        String nivelDesempeno = nivelDesempenoField.getText();
        String percentilNacional = percentilNacionalField.getText();

        // Simulación de impresión / lógica para guardar
        System.out.println("REPORTE GUARDADO:");
        System.out.println("Estudiante: " + tipoDoc + " " + documento + " - " + nombreEstudiante + " (" + ciudad + ")");
        System.out.println("Reporte: Año " + year + ", Periodo " + periodo + ", Puntaje Global: " + puntajeGlobal + ", Percentil Global: " + percentilGlobal + ", Novedades: " + novedades);
        System.out.println("Módulo: " + tipoModulo + ", Puntaje: " + puntajeModulo + ", Nivel: " + nivelDesempeno + ", Percentil Nacional: " + percentilNacional);
    }
}
