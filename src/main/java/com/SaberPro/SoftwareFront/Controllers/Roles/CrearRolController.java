package com.SaberPro.SoftwareFront.Controllers.Roles;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CrearRolController implements Initializable {


    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField correoTextField;
    @FXML
    private ComboBox<String> tipoRolComboBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> roles = FXCollections.observableArrayList(
                "ESTUDIANTE",
                "DOCENTE",
                "OFICINA_DE_ACREDITACION",
                "COMITE_DE_PROGRAMA",
                "COORDINADOR_SABER_PRO",
                "DIRECTOR_DE_PROGRAMA",
                "DIRECTOR_DE_ESCUELA",
                "DECANATURA"
        );
        tipoRolComboBox.setItems(roles);
    }

    @FXML
    public void onGuardarRolClick() {
        // 1. Get values from UI elements
        String nombre = nombreTextField.getText();
        String correo = correoTextField.getText();
        String tipoRol = tipoRolComboBox.getValue();

        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarAlerta("Error de Validación", "Por favor ingrese el nombre completo.", Alert.AlertType.ERROR);
            return;
        }
        if (correo == null || correo.trim().isEmpty()) {
            mostrarAlerta("Error de Validación", "Por favor ingrese el correo institucional.", Alert.AlertType.ERROR);
            return;
        }
        if (tipoRol == null || tipoRol.isEmpty()) {
            mostrarAlerta("Error de Validación", "Por favor seleccione un tipo de rol.", Alert.AlertType.ERROR);
            return;
        }

       guardarAsignacion(nombre, correo, tipoRol); // Pass all collected data

        mostrarAlerta("Éxito", "El rol ha sido asignado correctamente.", Alert.AlertType.INFORMATION);


        nombreTextField.clear();
        correoTextField.clear();
        tipoRolComboBox.setValue(null);
    }

    //logica del backkkkkkkkkkkkkk
    private void guardarAsignacion(String nombre, String correo, String tipoRol) {
        System.out.printf("Asignación creada: Nombre: %s, Correo: %s, Rol: %s%n", nombre, correo, tipoRol);

    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}