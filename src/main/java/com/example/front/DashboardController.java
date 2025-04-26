package com.example.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import java.io.IOException;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void onButtonDashboardInicio() {
        loadView("Login-view.fxml");
    }

    @FXML private void onButtonDashboardSReporte() {
        loadView("CReporte-view.fxml");
    }

    @FXML private void onButtonDashboardSInformes() {
        loadView("Login-view.fxml");
    }

    @FXML private void onButtonDashboardGeneral() {
        loadView("VReporteG-view.fxml");
    }

    @FXML private void onButtonDashboardEspecificos() {
        loadView("VReporteE-view.fxml");
    }

    @FXML private void onButtonDashboardAsignar() {
        loadView("Login-view.fxml");
    }

    @FXML private void onButtonDashboardSeguimiento() {
        loadView("Login-view.fxml");
    }

    @FXML private void onButtonDashboardCrearRol() {
        loadView("Login-view.fxml");
    }

    @FXML private void onButtonDashboardBorrarRol() {
        loadView("Login-view.fxml");
    }

    @FXML private void onButtonDashboardModificarRol() {
        loadView("Login-view.fxml");
    }
}


