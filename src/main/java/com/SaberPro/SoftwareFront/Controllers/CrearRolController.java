package com.SaberPro.SoftwareFront.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class CrearRolController {

    @FXML
    private ComboBox<String> tipoRolComboBox;

    @FXML
    private ComboBox<String> docenteComboBox;

    @FXML
    private DatePicker fechaInicioDatePicker;

    @FXML
    private TextField duracionField;

    /**
     * Método que se ejecuta al hacer clic en el botón "Crear Rol".
     */
    @FXML
    private void onGuardarRolClick() {
        if (validarCampos()) {
            String tipoRol = tipoRolComboBox.getValue();
            String docente = docenteComboBox.getValue();
            LocalDate fechaInicio = fechaInicioDatePicker.getValue();
            String duracion = duracionField.getText().trim();

            // TODO: Implementar la lógica para almacenar el nuevo rol en la base de datos o gestionarlo.
            System.out.println("Crear Rol: ");
            System.out.println("Tipo de Rol: " + tipoRol);
            System.out.println("Docente: " + docente);
            System.out.println("Fecha de Inicio: " + fechaInicio);
            System.out.println("Duración: " + duracion + " meses");

            // Mostrar mensaje de éxito
            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Rol Creado",
                    "El rol ha sido creado exitosamente."
            );
        }
    }

    /**
     * Método que se ejecuta al hacer clic en el botón "Modificar Usuario".
     */
    @FXML
    private void onModificarRolClick() {
        // TODO: Implementar la lógica correspondiente a la modificación del rol
        mostrarAlerta(
                Alert.AlertType.INFORMATION,
                "Modificar Rol",
                "Opción de modificación disponible para ser implementada."
        );
    }

    /**
     * Valida que todos los campos requeridos del formulario hayan sido llenados correctamente.
     *
     * @return True si los campos son válidos, de lo contrario muestra un mensaje de error.
     */
    private boolean validarCampos() {
        if (tipoRolComboBox.getValue() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "Debe seleccionar un Tipo de Rol.");
            return false;
        }

        if (docenteComboBox.getValue() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "Debe seleccionar un Docente.");
            return false;
        }

        if (fechaInicioDatePicker.getValue() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "Debe seleccionar una Fecha de Inicio.");
            return false;
        }

        String duracion = duracionField.getText().trim();
        if (duracion.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "Debe ingresar la Duración del Rol.");
            return false;
        }

        try {
            int duracionMeses = Integer.parseInt(duracion);
            if (duracionMeses <= 0) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "La duración debe ser un número positivo.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "La duración debe ser un número válido.");
            return false;
        }

        return true;
    }

    /**
     * Inicializa el formulario con valores en el ComboBox.
     */
    @FXML
    private void initialize() {
        inicializarComboBoxRoles();
        inicializarComboBoxDocentes();
    }

    /**
     * Inicializa valores del ComboBox de Tipo de Rol.
     */
    private void inicializarComboBoxRoles() {
        tipoRolComboBox.getItems().addAll(
                "Administrador",
                "Docente",
                "Estudiante",
                "Coordinador",
                "Supervisor"
        );
    }

    /**
     * Inicializa valores del ComboBox de Docentes.
     */
    private void inicializarComboBoxDocentes() {
        // TODO: Cargar docentes del sistema o base de datos
        docenteComboBox.getItems().addAll(
                "Docente Juan",
                "Docente María",
                "Docente Pedro",
                "Docente Ana"
        );
    }

    /**
     * Mostrar un cuadro de diálogo de alerta para diversos casos (error, información, etc.).
     *
     * @param tipo    Tipo de alerta (ERROR, INFORMATION, WARNING, etc.)
     * @param titulo  Título del cuadro de alerta.
     * @param mensaje Mensaje que se mostrará en el cuerpo del cuadro de alerta.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null); // No header text
        alerta.setContentText(mensaje);
        alerta.showAndWait(); // Esperar a que el usuario cierre el cuadro
    }
}