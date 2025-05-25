package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button; // No se usa, se puede eliminar
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane; // No se usa, se puede eliminar
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Node; // No se usa, se puede eliminar

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
     * La ruta 'fxmlPath' debe ser relativa a la raíz de 'src/main/resources'.
     * Ejemplo: "com/SaberPro/SoftwareFront/Login-view.fxml" o "Views/Login-view.fxml"
     */
    private void loadView(String fxmlPath) {
        try {
            // Se asume que fxmlPath ya incluye la ruta completa desde la raíz de resources
            // Por ejemplo: "com/SaberPro/SoftwareFront/Login-view.fxml"
            // O si tienes una carpeta "Views" en resources: "Views/Login-view.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlPath)); // <-- Aquí está el cambio clave
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            // Mejorar el mensaje de error para depuración
            System.err.println("Error al cargar la vista FXML: " + fxmlPath);
            throw new RuntimeException("Error al cargar la vista: " + fxmlPath, e);
        }
    }

    @FXML
    private void onAtrasClick() {
        Stage stage = (Stage) contentArea.getScene().getWindow();
        // Asegúrate que "Login-view.fxml" sea la ruta completa desde resources
        ViewLoader.loadView("com/SaberPro/SoftwareFront/Login/Login-view.fxml", stage);
    }

    @FXML
    private void onButtonDashboardInicio() {
        // Asegúrate que "Login-view.fxml" sea la ruta completa desde resources
        loadView("com/SaberPro/SoftwareFront/Login/Bienvenida-view.fxml");
    }

    @FXML
    private void onButtonDashboardSReporte() {
        loadView("com/SaberPro/SoftwareFront/CReporte-view.fxml");
    }

    @FXML
    private void onButtonDashboardSInformes() {
        loadView("com/SaberPro/SoftwareFront/Login/Login-view.fxml"); // Usando Login-view de nuevo
    }

    @FXML
    private void onButtonDashboardGeneral() {
        loadView("com/SaberPro/SoftwareFront/ConsultarHistoricos/VReporteG-view.fxml");
    }

    @FXML
    private void onButtonDashboardEspecificos() {
        loadView("com/SaberPro/SoftwareFront/ConsultarHistoricos/VReporteE-view.fxml");
    }

    @FXML
    private void onButtonDashboardAsignar() {
        // Esta ruta parece ser la que estás cargando
        loadView("com/SaberPro/SoftwareFront/AccionesMejora/ModulosAccion.fxml"); // <-- Correcto
    }

    @FXML
    private void onButtonDashboardSeguimiento() {
        loadView("com/SaberPro/SoftwareFront/Login-view.fxml"); // Usando Login-view de nuevo
    }

    @FXML
    private void onButtonDashboardCrearRol() {
        loadView("com/SaberPro/SoftwareFront/Roles/CrearRol-view.fxml");
    }

    @FXML
    private void onButtonDashboardModificaRol() {
        loadView("com/SaberPro/SoftwareFront/Roles/ModificaRol-view.fxml");
    }
}