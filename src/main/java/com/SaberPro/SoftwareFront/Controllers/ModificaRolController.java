package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ModificaRolController {
    @FXML
    private ComboBox<String> tipoRolComboBox;

    @FXML
    private ComboBox<String> docenteComboBox;

    @FXML
    private DatePicker fechaInicioDatePicker;

    @FXML
    private TextField duracionField;

    @FXML
    private void initialize() {
        inicializarComboBoxRoles();
        cargarUsuariosDesdeAPI();
    }

    private void inicializarComboBoxRoles() {
        tipoRolComboBox.setItems(FXCollections.observableArrayList(
                "DIRECTOR DE PROGRAMA",
                "DOCENTE",
                "COORDINADOR SABER PRO"
        ));
    }

    private void cargarUsuariosDesdeAPI() {
        try {
            // Realizar la solicitud GET usando BuildRequest
            HttpResponse<String> response = BuildRequest.getInstance().GETParams(
                    "http://localhost:8080/SaberPro/usuarios",
                    Map.of()
            );
            // Manejar la respuesta
            int responseCode = response.statusCode();
            System.out.println("Código de respuesta: " + responseCode);

            if (responseCode == 200) {
                String jsonResponse = response.body();
                System.out.println("Respuesta JSON: " + jsonResponse); // Depuración

                // Procesar la respuesta JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(jsonResponse);
                for (JsonNode usuarioNode : rootNode) {
                    docenteComboBox.getItems().add(usuarioNode.get("nombreUsuario").asText());
                }
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los usuarios.");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al cargar los usuarios: " + e.getMessage());
            e.printStackTrace(); // Depuración
        }
    }

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
        // Obtener el usuario seleccionado y el nuevo rol
        String usuarioSeleccionado = docenteComboBox.getValue();
        String nuevoRol = tipoRolComboBox.getValue();
        if (usuarioSeleccionado == null || nuevoRol == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un usuario y un rol.");
            return;
        }
        System.out.println("Usuario seleccionado: " + usuarioSeleccionado);
        System.out.println("Nuevo rol: " + nuevoRol);
        try {
            // Recibe la url del endpoint y un mapa con los paramentros
            HttpResponse<String> response = BuildRequest.getInstance().PUTParams(
                    "http://localhost:8080/SaberPro/usuarios/assignRole",
                    Map.of("nombreUsuario", usuarioSeleccionado,
                            "nuevoRol", nuevoRol));

            // Manejar la respuesta
            int responseCode = response.statusCode();
            System.out.println("Código de respuesta: " + responseCode);

            if (responseCode == 200) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Rol modificado correctamente.");
            } else {
                String responseBody = response.body();
                System.out.println("Error en la respuesta: " + responseBody);
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo modificar el rol: " + responseBody);
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al modificar el rol: " + e.getMessage());
            e.printStackTrace(); // Depuración
        }
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