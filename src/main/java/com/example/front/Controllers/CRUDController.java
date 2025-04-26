package com.example.front.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CRUDController {

    @FXML
    private TableView<?> dataTable; // Cambia el tipo según tu modelo de datos
    @FXML
    private TextField searchField;

    @FXML
    private void onCreateButtonClick() {
        // Lógica para crear un nuevo registro
    }

    @FXML
    private void onEditButtonClick() {
        // Lógica para editar el registro seleccionado
    }

    @FXML
    private void onDeleteButtonClick() {
        // Lógica para eliminar el registro seleccionado
    }

    @FXML
    private void onSearchKeyReleased() {
        String searchText = searchField.getText();
        // Lógica para filtrar los datos en la tabla según el texto de búsqueda
    }

    @FXML
    private void onPreviousPageButtonClick() {
        // Lógica para cargar la página anterior de datos
    }

    @FXML
    private void onNextPageButtonClick() {
        // Lógica para cargar la siguiente página de datos
    }
}