package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
// import javafx.scene.control.Button; // No se usa, se puede eliminar
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
// import javafx.scene.control.TitledPane; // No se usa, se puede eliminar
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
// import javafx.scene.Node; // No se usa, se puede eliminar

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
            // Opcional: Redirigir al login si el tipo de usuario no está definido
            Stage stage = (Stage) contentArea.getScene().getWindow();
            ViewLoader.loadView("/com/SaberPro/SoftwareFront/Login/Login-view.fxml", stage); // Usamos ruta completa con /
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
                // Asegúrate de ocultar también generalesMenuItem si los docentes no lo ven
                ocultarMenuItem(generalesMenuItem);
                break;

            case "ESTUDIANTE":
                ocultarMenu(cargarReportesMenu);
                ocultarMenu(accionesMejoraMenu);
                ocultarMenu(crudMenu);
                // Si el estudiante no tiene acceso a específicos/generales, también ocúltalos
                break;

            default:
                System.out.println("Advertencia: tipoUsuario no reconocido.");
                // Opcional: Ocultar todo si el tipo de usuario no es reconocido
                ocultarMenu(cargarReportesMenu);
                ocultarMenu(accionesMejoraMenu);
                ocultarMenu(crudMenu);
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
     * La ruta 'fxmlPath' DEBE SER ABSOLUTA desde la raíz del classpath
     * (es decir, debe empezar con "/com/SaberPro/SoftwareFront/...").
     * Ejemplo: "/com/SaberPro/SoftwareFront/Login/Login-view.fxml"
     */
    private void loadView(String fxmlPath) {
        try {
            // Se asume que fxmlPath ya es la ruta completa desde la raíz del classpath
            // y por lo tanto ya empieza con una '/'
            // Ejemplo: "/com/SaberPro/SoftwareFront/Login/Bienvenida-view.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath)); // Eliminado el '/' redundante
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista FXML: " + fxmlPath);
            throw new RuntimeException("Error al cargar la vista: " + fxmlPath, e);
        }
    }

    @FXML
    private void onAtrasClick() {
        Stage stage = (Stage) contentArea.getScene().getWindow();
        // Ruta ABSOLUTA para ViewLoader
        ViewLoader.loadView("/com/SaberPro/SoftwareFront/Login/Login-view.fxml", stage);
    }

    @FXML
    private void onButtonDashboardInicio() {
        // Ruta ABSOLUTA para el loadView interno del DashboardController
        loadView("/com/SaberPro/SoftwareFront/Login/Bienvenida-view.fxml");
    }

    @FXML
    private void onButtonDashboardSReporte() {
        // Ruta ABSOLUTA
        loadView("/com/SaberPro/SoftwareFront/CReporte-view.fxml");
    }

    @FXML
    private void onButtonDashboardSInformes() {
        // Ruta ABSOLUTA - Ajustado a una vista más lógica si "Login-view.fxml" no es para informes
        loadView("/com/SaberPro/SoftwareFront/ConsultarHistoricos/VReporteG-view.fxml");
    }

    @FXML
    private void onButtonDashboardGeneral() {
        // Ruta ABSOLUTA
        loadView("/com/SaberPro/SoftwareFront/ConsultarHistoricos/VReporteG-view.fxml");
    }

    @FXML
    private void onButtonDashboardEspecificos() {
        // Ruta ABSOLUTA
        loadView("/com/SaberPro/SoftwareFront/ConsultarHistoricos/VReporteE-view.fxml");
    }

    @FXML
    private void onButtonDashboardAsignar() {
        // Ruta ABSOLUTA
        loadView("/com/SaberPro/SoftwareFront/Acciones/ModulosAccion.fxml");
    }

    @FXML
    private void onButtonDashboardSeguimiento() {
        // Ruta ABSOLUTA - Ajustado a una vista más lógica si "Login-view.fxml" no es para seguimiento
        loadView("/com/SaberPro/SoftwareFront/Cumplimiento/DetalleCumplimiento.fxml");
    }

    @FXML
    private void onButtonDashboardCrearRol() {
        // Ruta ABSOLUTA
        loadView("/com/SaberPro/SoftwareFront/Roles/CrearRol-view.fxml");
    }

    @FXML
    private void onButtonDashboardModificaRol() {
        // Ruta ABSOLUTA
        loadView("/com/SaberPro/SoftwareFront/Roles/ModificaRol-view.fxml");
    }
}