package com.SaberPro.SoftwareFrontT;

import com.SaberPro.SoftwareFront.Utils.TokenManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

public class testController {

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
        if (cargarReportesPane == null || verReportesPane == null || accionesMejoraPane == null || crudPane == null) {
            System.out.println("Advertencia: Uno o más TitledPanes no están inicializados correctamente.");
            return;
        }

        String tipoUsuario = TokenManager.getTipoUsuario();

        if (tipoUsuario == null) {
            System.out.println("Error: tipoUsuario no definido. Verifica los detalles del login.");
            return;
        }

        switch (tipoUsuario) {
            case "DECANATURA":
                break;
            case "OFICINA DE ACREDITACION":
                ocultarPane(crudPane);
                break;
            case "COORDINADOR SABER PRO":
                ocultarPane(cargarReportesPane);
                ocultarPane(crudPane);
                break;
            case "DIRECTOR DE ESCUELA":
                ocultarPane(cargarReportesPane);
                break;
            case "DIRECTOR DE PROGRAMA":
                ocultarPane(crudPane);
                break;
            case "DOCENTE":
                ocultarPane(cargarReportesPane);
                ocultarPane(accionesMejoraPane);
                ocultarPane(crudPane);
                ocultarButton(especificosButton);
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

    private void ocultarPane(Node node) {
        if (node != null) {
            node.setVisible(false);
            node.setManaged(false);
        }
    }

    private void ocultarButton(Button... botones) {
        for (Button boton : botones) {
            if (boton != null) {
                boton.setVisible(false);
                boton.setManaged(false);
            }
        }
    }
}
