package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Node;

public class DashboardController {

    @FXML
    private TitledPane cargarReportesPane, verReportesPane, accionesMejoraPane, crudPane;

    @FXML
    private Button subirReportesButton, subirInformesButton, generalesButton, especificosButton;
    @FXML
    private Button asignarAccionesButton, seguimientoAccionesButton, crearRolButton, modificarRolButton;

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        // Comprobación de inicialización de los nodos FXML
        if (cargarReportesPane == null || verReportesPane == null || accionesMejoraPane == null || crudPane == null) {
            System.out.println("Advertencia: Uno o más TitledPanes no están inicializados correctamente.");
            return;
        }

        // Obtiene el tipo de usuario desde el token
        String tipoUsuario = TokenManager.getTipoUsuario();

        if (tipoUsuario == null) {
            System.out.println("Error: tipoUsuario no definido. Verifica los detalles del login.");
            return;
        }

        // Oculta los elementos según el tipo de usuario
        switch (tipoUsuario) {
            case "DECANATURA":
                // Acceso completo: todos los elementos están visibles
                break;

            case "OFICINA DE ACREDITACION":
                //Cargar reportes, gestionari usarios
                ocultarPane(crudPane); // No puede gestionar usuarios
                break;

            case "COORDINADOR SABER PRO":
                ocultarPane(cargarReportesPane); // Sin acceso para cargar reportes
                ocultarPane(crudPane); // Sin acceso para gestionar usuarios
                break;

            case "DIRECTOR DE ESCUELA":
                ocultarPane(cargarReportesPane); // No puede cargar reportes
                break;

            case "DIRECTOR DE PROGRAMA":
                ocultarPane(crudPane); // Sin acceso para gestionar usuarios
                break;

            case "DOCENTE":
                ocultarPane(cargarReportesPane);
                ocultarPane(accionesMejoraPane);
                ocultarPane(crudPane);
                ocultarButton(especificosButton); // Sin reportes específicos
                break;

            case "ESTUDIANTE":
                ocultarPane(crudPane);
                ocultarPane(cargarReportesPane);
                ocultarPane(accionesMejoraPane);
                break;

            default:
                System.out.println("Advertencia: tipoUsuario no reconocido.");
                break;
        }
    }

    /**
     * Oculta el nodo especificado (Pane, TitledPane, etc.) y libera su espacio en la interfaz.
     */
    private void ocultarPane(Node node) {
        if (node != null) {
            node.setVisible(false);
            node.setManaged(false); // Libera espacio en el layout
        }
    }

    /**
     * Oculta el botón especificado y libera su espacio en la interfaz.
     */
    private void ocultarButton(Button button) {
        if (button != null) {
            button.setVisible(false);
            button.setManaged(false);
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
        loadView("ModificaRol-view.fxml");
    }

    @FXML
    private void onButtonDashboardConsultarRol() {
        loadView("ConsultarRol-view.fxml");
    }
}