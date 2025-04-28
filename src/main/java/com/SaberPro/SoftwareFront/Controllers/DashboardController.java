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

public class DashboardController {

    @FXML
    private TitledPane cargarReportesPane, verReportesPane, accionesMejoraPane, crudPane;
    @FXML
    private Button subirReportesButton, subirInformesButton, generalesButton, especificosButton;
    @FXML
    private Button asignarAccionesButton, seguimientoAccionesButton, crearRolButton, modificarRolButton;

    @FXML
    public void initialize() {
        if (cargarReportesPane == null) {
            System.out.println("Error: cargarReportesPane no está inicializado. Verifica el archivo FXML.");
        }
        if (verReportesPane == null) {
            System.out.println("Error: verReportesPane no está inicializado. Verifica el archivo FXML.");
        }
        String tipoUsuario = TokenManager.getTipoUsuario();

        if (tipoUsuario == null) {
            System.out.println("Error: tipoUsuario es null. Verifica que se haya establecido correctamente.");
            return; // Detenemos la ejecución si no hay un tipo de usuario válido
        }

        switch (tipoUsuario) {
            case "DECANATURA":
                // Acceso completo, no se deshabilita nada
                break;
            case "OFICINA DE ACREDITACION":
                //Subir reportes, ver reportes y acciones de mejora
                assert crudPane != null;
                crudPane.setVisible(false);

                break;
            case "COORDINADOR SABER PRO":
                assert cargarReportesPane != null;
                cargarReportesPane.setVisible(false);
                assert crudPane != null;
                crudPane.setVisible(false);
                //Puede ver reportes y acciones de mejora
                break;
            case "DIRECTOR DE ESCUELA":
                // Ver reportes, acciones de mejora, ver informes de docentes sobre ello y el crud
                assert cargarReportesPane != null;
                cargarReportesPane.setVisible(false);
                break;
            case "DIRECTOR DE PROGRAMA":
                //No puede crear usuarios de ningun tipo
                assert crudPane != null;
                crudPane.setVisible(false);
                break;
            case "DOCENTE":
                //Puede subir sus reportes de implementacion e acciones de mejora
                //Puede ver informes generales

                assert especificosButton != null;
                especificosButton.setVisible(false);

                assert accionesMejoraPane != null;
                accionesMejoraPane.setVisible(false);
                assert cargarReportesPane != null;
                cargarReportesPane.setVisible(false);

                assert crudPane != null;
                crudPane.setVisible(false);
                break;
            case "ESTUDIANTE":
                assert crudPane != null;
                crudPane.setVisible(false);

                assert cargarReportesPane != null;
                cargarReportesPane.setVisible(false);

                assert accionesMejoraPane != null;
                accionesMejoraPane.setVisible(false);

                break;
            default:
                System.out.println("Error: tipoUsuario no reconocido.");
                break;
        }
    }

    private void disableAll() {
        cargarReportesPane.setDisable(true);
        verReportesPane.setDisable(true);
        accionesMejoraPane.setDisable(true);
        crudPane.setDisable(true);

        subirReportesButton.setDisable(true);
        subirInformesButton.setDisable(true);
        generalesButton.setDisable(true);
        especificosButton.setDisable(true);
        asignarAccionesButton.setDisable(true);
        seguimientoAccionesButton.setDisable(true);
        crearRolButton.setDisable(true);
        modificarRolButton.setDisable(true);
    }

    @FXML
    private StackPane contentArea;

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
        ViewLoader.loadView("login-view.fxml", stage, 450, 480);
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
        loadView("CrearRol-view.fxml");
    }

    @FXML private void onButtonDashboardConsultarRol() {
        loadView("ConsultarRol-view.fxml");
    }
}