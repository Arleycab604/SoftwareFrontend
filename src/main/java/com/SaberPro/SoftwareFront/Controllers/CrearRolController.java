package com.SaberPro.SoftwareFront.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class CrearRolController {
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
            URL url = new URL("http://localhost:8080/api/usuario/excluirPorTipo?tipoExcluido=estudiante");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder jsonResponse = new StringBuilder();
                while (scanner.hasNext()) {
                    jsonResponse.append(scanner.nextLine());
                }
                scanner.close();

                System.out.println("Respuesta JSON: " + jsonResponse.toString()); // Depuración

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(jsonResponse.toString());
                for (JsonNode usuarioNode : rootNode) {
                    docenteComboBox.getItems().add(usuarioNode.get("nombreUsuario").asText());
                }
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los usuarios.");
            }
            conn.disconnect();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al cargar los usuarios.");
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
        try {
            // Obtener el usuario seleccionado y el nuevo rol
            String usuarioSeleccionado = docenteComboBox.getValue();
            String nuevoRol = tipoRolComboBox.getValue();

            if (usuarioSeleccionado == null || nuevoRol == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un usuario y un rol.");
                return;
            }

            System.out.println("Usuario seleccionado: " + usuarioSeleccionado);
            System.out.println("Nuevo rol: " + nuevoRol);

            String usuarioCodificado = java.net.URLEncoder.encode(usuarioSeleccionado, "UTF-8");
            String rolCodificado = java.net.URLEncoder.encode(nuevoRol, "UTF-8");

            String endpoint = String.format("http://localhost:8080/SaberPro/usuario/cambiarRol?nombreUsuario=%s&nuevoRol=%s",
                    usuarioCodificado, rolCodificado);

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Rol modificado correctamente.");
            } else {
                // Verificar si el flujo de error no es nulo
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    Scanner scanner = new Scanner(errorStream);
                    StringBuilder errorResponse = new StringBuilder();
                    while (scanner.hasNext()) {
                        errorResponse.append(scanner.nextLine());
                    }
                    scanner.close();
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo modificar el rol: " + errorResponse.toString());
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo modificar el rol. El servidor no proporcionó detalles.");
                }
            }

            conn.disconnect();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al modificar el rol: " + e.getMessage());
            e.printStackTrace(); // Depuración: Imprimir el stack trace
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
