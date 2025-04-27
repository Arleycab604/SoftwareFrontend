package com.SaberPro.SoftwareFront.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SelectionModel;

public class ConsultarRolController {

    // Tabla de datos
    @FXML
    private TableView<Object> dataTable; // Cambia Object por tu clase modelo, p. ej., Rol

    @FXML
    private TableColumn<Object, String> idColumn;

    @FXML
    private TableColumn<Object, String> nameColumn;

    @FXML
    private TableColumn<Object, String> rolColumn;

    @FXML
    private TableColumn<Object, String> startDateColumn;

    // Campos de búsqueda
    @FXML
    private TextField searchByIdField;

    @FXML
    private TextField searchByNameField;

    @FXML
    private DatePicker searchByStartDatePicker;

    // Botones de acciones
    @FXML
    private Button buttonBuscar;

    @FXML
    private Button buttonAnterior;

    @FXML
    private Button buttonSiguiente;

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
     *
     * @return El objeto seleccionado o `null` si no hay selección.
     */
    private Object getSelectedItem() {
        SelectionModel<Object> selectionModel = dataTable.getSelectionModel();
        return selectionModel.getSelectedItem();
    }

    /**
     * Mostrar alertas genéricas.
     *
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

    /**
     * Inicializador. Ejecuta acciones tras cargar la vista.
     */
    @FXML
    private void initialize() {
        // TODO: Cargar datos en la tabla si es necesario. Puedes configurar las columnas aquí.
        System.out.println("Vista inicializada correctamente.");
    }
}