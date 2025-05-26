package com.SaberPro.SoftwareFront.Controllers.Acciones;

import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import lombok.Setter;

public class PropuestaMejoraItemController {

    @FXML private VBox contentBox;
    @FXML private Label nombreLabel;

    private PropuestaMejoraDTO propuesta;  // mejor nombre que dto

    @Setter
    private ModulosAccionController parentController;

    public void setData(PropuestaMejoraDTO propuesta) {
        this.propuesta = propuesta;
        if (propuesta != null && propuesta.getNombrePropuesta() != null) {
            nombreLabel.setText(propuesta.getNombrePropuesta());
        } else {
            nombreLabel.setText("Nombre no disponible");
        }
    }

    @FXML
    private void handleClick(MouseEvent event) {
        if (propuesta != null && parentController != null) {
            parentController.abrirDetalle(propuesta);
        }
    }
}
