package com.SaberPro.SoftwareFront.Controllers.AccionesEvidencia;

import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class EvidenciaListController {

    @FXML
    private ListView<PropuestaMejoraDTO> listaPropuestas;

    public void initialize() throws IOException, InterruptedException {
        String url = "http://localhost:8080/SaberPro/propuestas/modulo/";

        // Llamada HTTP para obtener propuestas según el tipo de usuario
        HttpResponse<String> response = BuildRequest.getInstance().GETParams(
                url + TokenManager.getTipoUsuario(), null
        );

        // Configuración del mapper para convertir JSON a lista de DTOs
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        List<PropuestaMejoraDTO> propuestas = mapper.readValue(
                response.body(),
                new TypeReference<List<PropuestaMejoraDTO>>() {}
        );

        // Cargar propuestas en la ListView
        listaPropuestas.getItems().addAll(propuestas);

        // Configurar evento de selección
        listaPropuestas.setOnMouseClicked(this::onPropuestaSeleccionada);
    }


    private void onPropuestaSeleccionada(MouseEvent event) {
        PropuestaMejoraDTO seleccionada = listaPropuestas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("detalle_propuesta.fxml"));
                Parent root = loader.load();

                DetallePropuestaController controller = loader.getController();
                controller.setPropuesta(seleccionada);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Detalle de Propuesta");
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
