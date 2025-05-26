package com.SaberPro.SoftwareFront.Controllers.Acciones;

import com.SaberPro.SoftwareFront.Controllers.Acciones.Detalles.DetallesAccionController;
import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.Enums.ModulosSaberPro;
import com.SaberPro.SoftwareFront.Utils.Enums.TipoUsuario;
import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class ModulosAccionController {

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<ModulosSaberPro> cmbModulos;
    @FXML private VBox vboxPropuestas;

    private List<PropuestaMejoraDTO> propuestasOriginales;
    private final TipoUsuario tipoUsuarioActual = TipoUsuario.valueOf(TokenManager.getTipoUsuario());

    @FXML
    public void initialize() {
        cmbModulos.getItems().setAll(ModulosSaberPro.values());
        cmbModulos.setOnAction(e -> filtrarYMostrar());
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> filtrarYMostrar());

        cargarPropuestas();
    }

    private void cargarPropuestas() {
        try {
            String urlBase = "http://localhost:8080/SaberPro/propuestas";
            HttpResponse<String> response;

            switch (tipoUsuarioActual) {
                case DOCENTE:
                    ModulosSaberPro modulo = cmbModulos.getValue();
                    if (modulo == null) {
                        vboxPropuestas.getChildren().clear();
                        return;
                    }
                    response = BuildRequest.getInstance().GETParams(urlBase + "/modulo/" + modulo.name(), null);
                    break;

                case ESTUDIANTE:
                    vboxPropuestas.getChildren().clear();
                    return;

                default:
                    response = BuildRequest.getInstance().GETParams(urlBase, null);
                    break;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();

            List<PropuestaMejoraDTO> propuestas = mapper.readValue(response.body(), new TypeReference<>() {});
            propuestasOriginales = propuestas;

            filtrarYMostrar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filtrarYMostrar() {
        vboxPropuestas.getChildren().clear();

        if (propuestasOriginales == null) return;

        for (PropuestaMejoraDTO propuesta : propuestasOriginales) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/Acciones/PropuestaMejoraItem.fxml"));
                Parent propuestaNode = loader.load();

                PropuestaMejoraItemController controller = loader.getController();
                controller.setData(propuesta);
                controller.setParentController(this);

                vboxPropuestas.getChildren().add(propuestaNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void abrirDetalle(PropuestaMejoraDTO dto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/AccionesMejora/DetallesAccion.fxml"));
            Parent root = loader.load();
            DetallesAccionController controller = loader.getController();
            controller.setPropuesta(dto);

            Stage stage = new Stage();
            stage.setTitle(dto.getNombrePropuesta());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}