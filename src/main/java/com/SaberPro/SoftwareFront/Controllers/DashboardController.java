package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Node;

public class DashboardController {

    @FXML
    private Menu cargarReportesMenu, accionesMejoraMenu, crudMenu;

    @FXML
    private MenuItem especificosMenuItem, generalesMenuItem, asignarAccionesMenuItem, seguimientoAccionesMenuItem;

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        String tipoUsuario = TokenManager.getTipoUsuario();
        if (tipoUsuario == null) {
            System.out.println("Error: tipoUsuario no definido.");
            return;
        }

        switch (tipoUsuario) {
            case "DECANATURA":
                break;

            case "OFICINA DE ACREDITACION":
                ocultarMenu(crudMenu);
                break;

            case "COORDINADOR SABER PRO":
                ocultarMenu(cargarReportesMenu);
                ocultarMenu(crudMenu);
                break;

            case "DIRECTOR DE ESCUELA":
                ocultarMenu(cargarReportesMenu);
                break;

            case "DIRECTOR DE PROGRAMA":
                ocultarMenu(crudMenu);
                break;

            case "DOCENTE":
                ocultarMenu(cargarReportesMenu);
                ocultarMenu(accionesMejoraMenu);
                ocultarMenu(crudMenu);
                ocultarMenuItem(especificosMenuItem);
                break;

            case "ESTUDIANTE":
                ocultarMenu(cargarReportesMenu);
                ocultarMenu(accionesMejoraMenu);
                ocultarMenu(crudMenu);
                break;

            default:
                System.out.println("Advertencia: tipoUsuario no reconocido.");
                break;
        }
    }

    private void ocultarMenu(Menu menu) {
        if (menu != null) {
            menu.setVisible(false);
            menu.setDisable(true);
        }
    }

    private void ocultarMenuItem(MenuItem item) {
        if (item != null) {
            item.setVisible(false);
            item.setDisable(true);
        }
    }


    /**
     * Carga una vista en el área principal con el archivo FXML especificado.
     */
    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/" + fxml));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar la vista: " + fxml, e);
        }
    }

    @FXML
    private void onAtrasClick() {
        Stage stage = (Stage) contentArea.getScene().getWindow();
        ViewLoader.loadView("login-view.fxml", stage);
    }

    @FXML
    private void onButtonDashboardInicio() {
        loadView("Login-view.fxml");
    }

    @FXML
    private void onButtonDashboardSReporte() {
        loadView("CReporte-view.fxml");
    }

    @FXML
    private void onButtonDashboardSInformes() {
        loadView("Login-view.fxml");
    }

    @FXML
    private void onButtonDashboardGeneral() {
        loadView("VReporteG-view.fxml");
    }

    @FXML
    private void onButtonDashboardEspecificos() {
        loadView("VReporteE-view.fxml");
    }

    @FXML
    private void onButtonDashboardAsignar() {
        loadView("Login-view.fxml");
    }

    @FXML
    private void onButtonDashboardSeguimiento() {
        loadView("Login-view.fxml");
    }

    @FXML
    private void onButtonDashboardCrearRol() {
        loadView("CrearRol-view.fxml");
    }

    @FXML
    private void onButtonDashboardModificaRol() {
        loadView("ModificaRol-view.fxml");
    }
}