package com.SaberPro.SoftwareFront.Controllers.Acciones;

import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.Enums.ModulosSaberPro;
import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class CrearPropuestaController {

    @FXML private TextField txtNombrePropuesta;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<String> cmbModulo;
    @FXML private DatePicker dpFechaLimite;

    private ModulosAccionController parentController;

    public void setParentController(ModulosAccionController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        cmbModulo.getItems().addAll(ModulosSaberPro.INGLÉS.toString(),
                ModulosSaberPro.COMPETENCIAS_CIUDADANAS.toString(),
                ModulosSaberPro.COMUNICACION_ESCRITA.toString(),
                ModulosSaberPro.RAZONAMIENTO_CUANTITATIVO.toString(),
                ModulosSaberPro.LECTURA_CRITICA.toString(),
                ModulosSaberPro.FORMULACION_DE_PROYECTOS_DE_INGENIERIA.toString(),
                ModulosSaberPro.PENSAMIENTO_CIENTIFICO_MATEMATICAS_Y_ESTADISTICA.toString(),
                ModulosSaberPro.DISEÑO_DE_SOFTWARE.toString()); // Ejemplo de módulos
    }

    @FXML
    private void handleCrear() {
        try {
            String nombre = txtNombrePropuesta.getText();
            String descripcion = txtDescripcion.getText();
            String modulo = cmbModulo.getValue();
            LocalDate fechaLimite = dpFechaLimite.getValue();

            if (nombre.isEmpty() || descripcion.isEmpty() || modulo == null || fechaLimite == null) {
                mostrarError("Todos los campos son obligatorios.");
                return;
            }

            PropuestaMejoraDTO propuesta = new PropuestaMejoraDTO();
            propuesta.setNombrePropuesta(nombre);
            if(TokenManager.getNombreUsuario() == null){
                System.out.println("ERROR que wea paso aca");
            }
            propuesta.setUsuarioProponente(TokenManager.getNombreUsuario());
            propuesta.setDescripcion(descripcion);
            propuesta.setModuloPropuesta(ModulosSaberPro.valueOf(modulo));
            propuesta.setFechaLimiteEntrega(fechaLimite.atStartOfDay());

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String json = mapper.writeValueAsString(propuesta);

            HttpResponse<String> response = BuildRequest.getInstance().POSTJson("http://localhost:8080/SaberPro/propuestas/crear", json);
            System.out.println(response.body());

            if (response.statusCode() == 200) {
                mostrarError("Propuesta creada con éxito.");
                parentController.cargarPropuestas(); // Actualizar la lista en el controlador principal
                cerrarVentana();
            } else {
                mostrarError("Error al crear la propuesta: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            mostrarError("Error al enviar la propuesta.");
        }
    }

    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombrePropuesta.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}