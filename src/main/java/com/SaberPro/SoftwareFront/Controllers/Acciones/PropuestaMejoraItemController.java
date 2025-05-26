package com.SaberPro.SoftwareFront.Controllers.Acciones;

import com.SaberPro.SoftwareFront.Controllers.Acciones.Detalles.DetallesAccionController;
import com.SaberPro.SoftwareFront.Controllers.Acciones.ModulosAccionController;
import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;

public class PropuestaMejoraItemController {

    @FXML private VBox contentBox;
    @FXML private Label nombreLabel;
    private PropuestaMejoraDTO propuesta;

    public void setData(PropuestaMejoraDTO propuesta) {
        this.propuesta = propuesta;
        nombreLabel.setText(propuesta.getNombrePropuesta());
    }


    private void cargarVistaDetalle(PropuestaMejoraDTO dto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/Vistas/detalle.fxml"));
            Parent root = loader.load();
            DetallesAccionController controller = loader.getController();
            controller.setDatos(dto);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Detalle de Propuesta");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Setter
    private ModulosAccionController parentController;

    @FXML
    private void handleClick() {
        if (parentController != null && propuesta != null) {
            parentController.abrirDetalle(propuesta);
        }
    }
}