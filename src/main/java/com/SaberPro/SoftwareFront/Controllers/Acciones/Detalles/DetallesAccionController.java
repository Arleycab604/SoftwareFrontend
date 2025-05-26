package com.SaberPro.SoftwareFront.Controllers.Acciones.Detalles;

import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    // En DetallesAccionController.java
    public void setDatos(PropuestaMejoraDTO propuesta) {
        this.propuesta = propuesta;
        cargarDatos();
    }

}
