package com.SaberPro.SoftwareFront.Controllers.Roles;

import com.SaberPro.SoftwareFront.Models.UsuarioDTO;
import com.SaberPro.SoftwareFront.Utils.BuildRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.http.HttpResponse;
import java.util.Map;

public class ConsultarRolController {

    // Tabla de datos
    @FXML
    private TableView<UsuarioDTO> dataTable; // Cambia Object por tu clase modelo, p. ej., Rol
    @FXML
    private TableColumn<Object, String> idColumn;
    @FXML
    private TableColumn<Object, String> nameColumn;
    @FXML
    private TableColumn<Object, String> rolColumn;
    @FXML
    private TableColumn<Object, String> startDateColumn;
    // Tabla de datos
    @FXML
    private TableColumn<UsuarioDTO, String> nombreUsuarioColumn;
    @FXML
    private TableColumn<UsuarioDTO, String> tipoDeUsuarioColumn;
    @FXML
    private TableColumn<UsuarioDTO, String> correoColumn;

    // Campos de búsqueda
    @FXML
    private TextField searchByIdField;
    @FXML
    private TextField searchByNameField;
    @FXML
    private DatePicker searchByStartDatePicker;
    private final ObservableList<UsuarioDTO> usuarios = FXCollections.observableArrayList();

    // Botones de acciones
    @FXML
    private Button buttonBuscar;

    @FXML
    private Button buttonAnterior;

    @FXML
    private Button buttonSiguiente;

    @FXML
    private void initialize() {
        configurarColumnas();
        cargarUsuariosDesdeAPI();
    }

    private void configurarColumnas() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        rolColumn.setCellValueFactory(new PropertyValueFactory<>("tipoDeUsuario"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("correo"));
        dataTable.setItems(usuarios);
    }

    /**
     * Método para realizar el filtrado en la tabla según los campos de búsqueda.
     */
    @FXML
    private void onSearchButtonClick() {
        String idText = searchByIdField.getText().trim();
        String nameText = searchByNameField.getText().trim();
        String startDate = (searchByStartDatePicker.getValue() != null)
                ? searchByStartDatePicker.getValue().toString()
                : "";

        // Actualizar lógica según tus necesidades
        System.out.println("Filtrar por: ID='" + idText + "', Nombre='" + nameText + "', Fecha='" + startDate + "'");
        // Aquí podrías aplicar un filtro a la tabla con datos dinámicos.
    }

    /**
     * Método para ir a la página anterior de la tabla.
     */
    @FXML
    private void onPreviousPageButtonClick() {
        // TODO: Implementar lógica para cargar la página anterior
        System.out.println("Cargar página anterior...");
        // Aquí podrías ajustar índices en una paginación.
    }

    /**
     * Método para ir a la siguiente página de la tabla.
     */
    @FXML
    private void onNextPageButtonClick() {
        // TODO: Implementar lógica para cargar la página siguiente
        System.out.println("Cargar siguiente página...");
        // Esto debería manejar índices y cargar más datos si trabajas con paginación.
    }

    /**
     * Obtener el elemento seleccionado en la tabla.
     * @return El objeto seleccionado o `null` si no hay selección.
     */
    private Object getSelectedItem() {
        SelectionModel<UsuarioDTO> selectionModel = dataTable.getSelectionModel();
        return selectionModel.getSelectedItem();
    }

    private void cargarUsuariosDesdeAPI() {
        try {
            // Realizar la solicitud GET usando BuildRequest
            HttpResponse<String> response = BuildRequest.getInstance().GETParams(
                    "http://localhost:8080/SaberPro/usuarios",
                    Map.of() // Sin parámetros adicionales
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

                    String nombreUsuario = usuarioNode.get("nombreUsuario").asText();
                    String tipoDeUsuario = usuarioNode.get("tipoDeUsuario").asText();
                    String correo = usuarioNode.get("correo").asText();
                    if(tipoDeUsuario == "ESTUDIANTE"){
                        continue;
                    }
                    usuarios.add(new UsuarioDTO(nombreUsuario, tipoDeUsuario, correo));
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los usuarios.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al cargar los usuarios: " + e.getMessage());
            e.printStackTrace(); // Depuración
        }
    }
    /**
     * Mostrar alertas genéricas.
     * @param alertType Tipo de la alerta.
     * @param title     Título de la alerta.
     * @param content   Contenido de la alerta.
     */
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Sin cabecera extra.
        alert.setContentText(content);
        alert.showAndWait(); // Espera interacción del usuario.
    }
}