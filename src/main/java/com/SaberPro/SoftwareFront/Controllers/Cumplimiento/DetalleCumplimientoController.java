package com.SaberPro.SoftwareFront.Controllers.Cumplimiento;

import com.SaberPro.SoftwareFront.Models.accionMejora.PropuestaMejoraDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class DetalleCumplimientoController {

    @FXML private Label lblTareaTitle;
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbOrdenarPor;
    @FXML private VBox vbContenedorAsignaciones;

    private static final String BASE_URL = "http://localhost:8080/SaberPro";
    private ObservableList<PropuestaMejoraDTO> todasLasPropuestas = FXCollections.observableArrayList();

    // ObjectMapper estático configurado para toda la clase
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @FXML
    public void initialize() {
        cmbOrdenarPor.setItems(FXCollections.observableArrayList("Nombre (A-Z)", "Fecha Límite (Próximas)"));
        cmbOrdenarPor.getSelectionModel().selectFirst();

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> filtrarYMostrar());
        cmbOrdenarPor.valueProperty().addListener((obs, oldVal, newVal) -> filtrarYMostrar());

        cargarPropuestas();
    }

    private void cargarPropuestas() {
        try {
            String nombreUsuario = TokenManager.getNombreUsuario();
            String tipoUsuario = TokenManager.getTipoUsuario();

            // Obtener el módulo del usuario
            String modulo = obtenerModuloUsuario(nombreUsuario, tipoUsuario);
            if (modulo == null) {
                mostrarError("No se pudo obtener el módulo del usuario.");
                return;
            }
            List<PropuestaMejoraDTO> propuestas;

            if (!"DOCENTE".equalsIgnoreCase(tipoUsuario)) {
                // Si NO es docente, obtener todas las propuestas
                propuestas = obtenerPropuestas();
            } else {
                // Si es docente, obtener propuestas por módulo
                propuestas = obtenerPropuestasPorModulo(modulo);
            }

            if (propuestas == null || propuestas.isEmpty()) {
                mostrarError("No se encontraron propuestas para el módulo: " + modulo);
                return;
            }

            todasLasPropuestas.setAll(propuestas);
            filtrarYMostrar();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al cargar las propuestas: " + e.getMessage());
        }
    }

    private String obtenerModuloUsuario(String nombreUsuario, String tipoUsuario) throws IOException, InterruptedException {
        if (!"DOCENTE".equalsIgnoreCase(tipoUsuario)) {
            return tipoUsuario; // Si no es docente, el tipo de usuario es el módulo
        }

        String url = BASE_URL + "/Usuarios/" + nombreUsuario + "/modulo";
        HttpResponse<String> response = BuildRequest.getInstance().GETParams(url, null);

        if (response.statusCode() == 200) {
            return response.body(); // El módulo se devuelve como texto
        } else {
            System.err.println("Error al obtener el módulo del usuario: " + response.body());
            return null;
        }
    }

    private List<PropuestaMejoraDTO> obtenerPropuestasPorModulo(String modulo) throws IOException, InterruptedException {
        String url = BASE_URL + "/propuestas/modulo/" + modulo;
        HttpResponse<String> response = BuildRequest.getInstance().GETParams(url, null);

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<PropuestaMejoraDTO>>() {});
        } else {
            System.err.println("Error al obtener las propuestas: " + response.body());
            return null;
        }
    }

    private List<PropuestaMejoraDTO> obtenerPropuestas() throws IOException, InterruptedException {
        String url = BASE_URL + "/propuestas";
        HttpResponse<String> response = BuildRequest.getInstance().GETParams(url, null);

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<PropuestaMejoraDTO>>() {});
        } else {
            System.err.println("Error al obtener las propuestas: " + response.body());
            return null;
        }
    }

    private void filtrarYMostrar() {
        vbContenedorAsignaciones.getChildren().clear();

        String textoBusqueda = txtBuscar.getText() != null ? txtBuscar.getText().toLowerCase() : "";
        String ordenSeleccionado = cmbOrdenarPor.getSelectionModel().getSelectedItem();

        List<PropuestaMejoraDTO> propuestasFiltradas = todasLasPropuestas.stream()
                .filter(propuesta -> propuesta.getNombrePropuesta().toLowerCase().contains(textoBusqueda)
                        || propuesta.getDescripcion().toLowerCase().contains(textoBusqueda)) // También filtra por descripción si lo deseas
                .sorted((p1, p2) -> {
                    if ("Nombre (A-Z)".equals(ordenSeleccionado)) {
                        return p1.getNombrePropuesta().compareToIgnoreCase(p2.getNombrePropuesta());
                    } else if ("Fecha Límite (Próximas)".equals(ordenSeleccionado)) {
                        return p1.getFechaLimiteEntrega().compareTo(p2.getFechaLimiteEntrega());
                    }
                    return 0;
                })
                .toList();

        if (propuestasFiltradas.isEmpty()) {
            Label noResultsLabel = new Label("No se encontraron propuestas.");
            noResultsLabel.setStyle("-fx-text-fill: #777; -fx-font-size: 14px;");
            vbContenedorAsignaciones.getChildren().add(noResultsLabel);
        } else {
            for (PropuestaMejoraDTO propuesta : propuestasFiltradas) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/Cumplimiento/TarjetaCumplimiento.fxml"));
                    Node tarjeta = loader.load();

                    TarjetaCumplimientoController controller = loader.getController();
                    controller.setAccionMejora(propuesta);

                    tarjeta.setOnMouseClicked(event -> abrirDetalleTarjetaCumplimiento(propuesta));

                    vbContenedorAsignaciones.getChildren().add(tarjeta);

                } catch (IOException e) {
                    e.printStackTrace();
                    Label errorLabel = new Label("Error al cargar tarjeta: " + propuesta.getNombrePropuesta());
                    errorLabel.setStyle("-fx-text-fill: red;");
                    vbContenedorAsignaciones.getChildren().add(errorLabel);
                }
            }
        }
    }


    public void cargarTarjetas(List<PropuestaMejoraDTO> tareas) {
        vbContenedorAsignaciones.getChildren().clear();
        for (PropuestaMejoraDTO tarea : tareas) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/TarjetaCumplimiento.fxml"));
                Node tarjeta = loader.load();

                // Obtener el controller para pasar datos si es necesario
                TarjetaCumplimientoController controller = loader.getController();
                controller.setAccionMejora(tarea);

                // Agregar manejador de click a la tarjeta completa
                tarjeta.setOnMouseClicked(event -> {
                    abrirDetalleTarjetaCumplimiento(tarea);
                });

                vbContenedorAsignaciones.getChildren().add(tarjeta);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void abrirDetalleTarjetaCumplimiento(PropuestaMejoraDTO tarea) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/Cumplimiento/VerTarjetaCumplimiento.fxml"));
            Parent root = loader.load();

            VerTarjetaCumplimientoController controller = loader.getController();
            controller.setPropuestaMejora(tarea);

            // Aquí abres la nueva ventana con el detalle
            Stage stage = new Stage();
            stage.setTitle("Detalle de Cumplimiento");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void mostrarError(String mensaje) {
        vbContenedorAsignaciones.getChildren().clear();
        Label errorLabel = new Label(mensaje);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        vbContenedorAsignaciones.getChildren().add(errorLabel);
    }
}
