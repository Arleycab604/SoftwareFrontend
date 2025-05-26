package com.SaberPro.SoftwareFront.Controllers.Acciones.Detalles;

import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.TokenManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;

public class DetallesAccionController {

    @FXML private Label tituloLabel;
    @FXML private Label tipoLabel;
    @FXML private Label fechaCreacionLabel;
    @FXML private Label fechaCierreLabel;
    @FXML private Label estadoLabel;
    @FXML private Label moduloLabel;
    @FXML private TextArea justificacionTextArea;
    @FXML private ListView<String> evidenciasListView;
    @FXML private Button aceptarButton;
    @FXML private Button rechazarButton;
    @FXML private Button volverButton;

    private PropuestaMejoraDTO propuesta;

    @FXML
    private void initialize() {

        if (TokenManager.getToken().equals("COMITE_DE_PROGRAMA")) {
            OcultarButton(aceptarButton);
            OcultarButton(rechazarButton);
        }
    }
    public void setPropuesta(PropuestaMejoraDTO propuesta) {
        this.propuesta = propuesta;
        cargarDatos();
    }

    private void cargarDatos() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        tituloLabel.setText(propuesta.getNombrePropuesta());
        tipoLabel.setText(propuesta.getUsuarioProponente()); // Opcional: puedes cambiar esto si hay otro campo para tipo
        fechaCreacionLabel.setText(propuesta.getFechaCreacion() != null ? propuesta.getFechaCreacion().format(formatter) : "No disponible");
        fechaCierreLabel.setText(propuesta.getFechaLimiteEntrega() != null ? propuesta.getFechaLimiteEntrega().format(formatter) : "No disponible");
        estadoLabel.setText(propuesta.getEstadoPropuesta() != null ? propuesta.getEstadoPropuesta().toString() : "Sin estado");
        moduloLabel.setText(propuesta.getModuloPropuesta() != null ? propuesta.getModuloPropuesta().toString() : "No asignado");

        justificacionTextArea.setText(propuesta.getDescripcion() != null ? propuesta.getDescripcion() : "Sin descripción");

        evidenciasListView.getItems().clear();
        evidenciasListView.getItems().addAll(propuesta.getUrlsDocumentoDetalles());
    }

    public void setDatos(PropuestaMejoraDTO propuesta) {
        this.propuesta = propuesta;
        cargarDatos();
    }

    @FXML
    private void OnAceptarButton() {
        if (propuesta != null && propuesta.getIdPropuestaMejora() != 0) {
            try {
                String url = "http://localhost:8080/api/propuestas/" + propuesta.getIdPropuestaMejora() + "/aceptar";
                HttpResponse<String> response = BuildRequest.getInstance().POSTJson(url, "", true);

                if (response.statusCode() == 200) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Propuesta aceptada", response.body());
                    aceptarButton.setDisable(true);
                    rechazarButton.setDisable(true);
                    estadoLabel.setText("Aceptada");
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al aceptar", "Código: " + response.statusCode() + "\n" + response.body());
                }
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error inesperado", e.getMessage());
            }
        }
    }

    @FXML
    private void OnRechazarButton() {
        if (propuesta != null && propuesta.getIdPropuestaMejora() != 0) {
            try {
                String url = "http://localhost:8080/api/propuestas/" + propuesta.getIdPropuestaMejora() + "/rechazar";
                HttpResponse<String> response = BuildRequest.getInstance().POSTJson(url, "", true);

                if (response.statusCode() == 200) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Propuesta rechazada", response.body());
                    aceptarButton.setDisable(true);
                    rechazarButton.setDisable(true);
                    estadoLabel.setText("Rechazada");
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al rechazar", "Código: " + response.statusCode() + "\n" + response.body());
                }
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error inesperado", e.getMessage());
            }
        }
    }
    @FXML
    private void OnVolverButton() {
        // Aquí puedes implementar la lógica para volver a la pantalla anterior
        // Por ejemplo, cerrar la ventana actual o cambiar a otra vista
        aceptarButton.getScene().getWindow().hide();
    }

    private void OcultarButton(Button button) {
        if (button != null) {
            button.setVisible(false);
            button.setManaged(false);
        }
    }
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

}
