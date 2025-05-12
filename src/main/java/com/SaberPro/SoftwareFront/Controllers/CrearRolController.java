package com.SaberPro.SoftwareFront.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

public class CrearRolController {

    @FXML
    private ComboBox<String> tipoRolComboBox;

    @FXML
    private ComboBox<String> docenteComboBox;




    /**
     * Evento accionado al hacer clic en el botón "Guardar Rol".
     */
    @FXML
    public void onGuardarRolClick() {
        String tipoRol = tipoRolComboBox.getValue();
        String docenteSeleccionado = docenteComboBox.getValue();

        // Validaciones
        if (tipoRol == null || tipoRol.isEmpty()) {
            mostrarAlerta("Error", "Seleccione un tipo de rol.", Alert.AlertType.ERROR);
            return;
        }
        if (docenteSeleccionado == null || docenteSeleccionado.isEmpty()) {
            mostrarAlerta("Error", "Seleccione un docente.", Alert.AlertType.ERROR);
            return;
        }

        // Lógica para guardar la asignación
        guardarAsignacion(tipoRol, docenteSeleccionado);

        // Mostrar confirmación
        mostrarAlerta("Éxito", "El rol ha sido asignado correctamente.", Alert.AlertType.INFORMATION);

        // Limpiar selección
        tipoRolComboBox.setValue(null);
        docenteComboBox.setValue(null);
    }

    /**
     * Lógica para almacenar la asignación de rol.
     */
    private void guardarAsignacion(String tipoRol, String docenteSeleccionado) {
        // Aquí va la lógica para guardar la asignación en base de datos o backend
        System.out.printf("Asignación creada. Rol: %s, Docente: %s%n", tipoRol, docenteSeleccionado);
    }

    /**
     * Muestra una alerta al usuario.
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}