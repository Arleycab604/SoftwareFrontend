package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.TokenManager;
import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;

public class DashboardController {

    @FXML
    private Menu cargarReportesMenu, accionesMejoraMenu, crudMenu; // verReportesMenu está en FXML pero no como @FXML aquí

    @FXML
    private MenuItem especificosMenuItem, generalesMenuItem, asignarAccionesMenuItem, seguimientoAccionesMenuItem;

    @FXML
    private MenuItem subirReportesMenuItem; // fx:id del MenuItem "Subir reportes"
    @FXML
    private MenuItem subirInformesMenuItem; // fx:id del MenuItem "Subir informes"
    @FXML
    private Menu verReportesMenu; // fx:id del Menu "Ver reportes"
    @FXML
    private MenuItem asignarMenuItem; // fx:id del MenuItem "Asignar"
    @FXML
    private MenuItem seguimientoMenuItem; // fx:id del MenuItem "Seguimiento"
    @FXML
    private MenuItem crearRolMenuItem; // fx:id del MenuItem "Crear rol"
    @FXML
    private MenuItem modificarRolMenuItem; // fx:id del MenuItem "Modificar rol"


    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        String tipoUsuario = TokenManager.getTipoUsuario();
        if (tipoUsuario == null) {
            System.out.println("Error: tipoUsuario no definido.");
            Stage stage = (Stage) contentArea.getScene().getWindow();
            ViewLoader.loadView("/com/SaberPro/SoftwareFront/Login/Login-view.fxml", stage);
            return;
        }

        // Por defecto, ocultamos todos los menús y ítems que no todos los roles deberían ver
        // Luego, en cada case, revelamos lo que el rol específico sí debe ver.
        ocultarMenu(cargarReportesMenu);
        ocultarMenu(accionesMejoraMenu);
        ocultarMenu(crudMenu);
        ocultarMenuItem(especificosMenuItem);
        ocultarMenuItem(generalesMenuItem);
        ocultarMenuItem(subirReportesMenuItem);
        ocultarMenuItem(subirInformesMenuItem);
        ocultarMenuItem(asignarMenuItem);
        ocultarMenuItem(seguimientoMenuItem); // Ocultar por defecto
        ocultarMenuItem(crearRolMenuItem);
        ocultarMenuItem(modificarRolMenuItem);


        switch (tipoUsuario) {
            case "DECANATURA":
                // Decanatura puede ver todo (o casi todo)
                cargarReportesMenu.setVisible(true); cargarReportesMenu.setDisable(false);
                verReportesMenu.setVisible(true); verReportesMenu.setDisable(false);
                accionesMejoraMenu.setVisible(true); accionesMejoraMenu.setDisable(false);
                crudMenu.setVisible(true); crudMenu.setDisable(false);

                // Y los items dentro de ellos
                subirReportesMenuItem.setVisible(true); subirReportesMenuItem.setDisable(false);
                subirInformesMenuItem.setVisible(true); subirInformesMenuItem.setDisable(false);
                generalesMenuItem.setVisible(true); generalesMenuItem.setDisable(false);
                especificosMenuItem.setVisible(true); especificosMenuItem.setDisable(false);
                asignarMenuItem.setVisible(true); asignarMenuItem.setDisable(false);
                seguimientoMenuItem.setVisible(true); seguimientoMenuItem.setDisable(false);
                crearRolMenuItem.setVisible(true); crearRolMenuItem.setDisable(false);
                modificarRolMenuItem.setVisible(true); modificarRolMenuItem.setDisable(false);
                break;

            case "OFICINA DE ACREDITACION":
                // Tienen acceso a cargar reportes y ver reportes
                cargarReportesMenu.setVisible(true); cargarReportesMenu.setDisable(false);
                verReportesMenu.setVisible(true); verReportesMenu.setDisable(false);
                accionesMejoraMenu.setVisible(true); accionesMejoraMenu.setDisable(false);

                subirReportesMenuItem.setVisible(true); subirReportesMenuItem.setDisable(false);
                generalesMenuItem.setVisible(true); generalesMenuItem.setDisable(false);
                especificosMenuItem.setVisible(true); especificosMenuItem.setDisable(false);
                asignarMenuItem.setVisible(true); asignarMenuItem.setDisable(false);
                seguimientoMenuItem.setVisible(true); seguimientoMenuItem.setDisable(false);
                // El CRUD se mantiene oculto por el "por defecto"
                break;

            case "COORDINADOR SABER PRO":
                // Pueden ver reportes y acciones de mejora
                verReportesMenu.setVisible(true); verReportesMenu.setDisable(false);
                accionesMejoraMenu.setVisible(true); accionesMejoraMenu.setDisable(false);

                generalesMenuItem.setVisible(true); generalesMenuItem.setDisable(false);
                especificosMenuItem.setVisible(true); especificosMenuItem.setDisable(false);
                asignarMenuItem.setVisible(true); asignarMenuItem.setDisable(false);
                seguimientoMenuItem.setVisible(true); seguimientoMenuItem.setDisable(false);
                // Cargar reportes y CRUD se mantienen ocultos por el "por defecto"
                break;

            case "DIRECTOR DE ESCUELA":
                // Pueden ver reportes y acciones de mejora
                verReportesMenu.setVisible(true); verReportesMenu.setDisable(false);
                accionesMejoraMenu.setVisible(true); accionesMejoraMenu.setDisable(false);
                crudMenu.setVisible(true); crudMenu.setDisable(false); // Asumiendo que el Director de Escuela también puede gestionar roles

                generalesMenuItem.setVisible(true); generalesMenuItem.setDisable(false);
                especificosMenuItem.setVisible(true); especificosMenuItem.setDisable(false);
                asignarMenuItem.setVisible(true); asignarMenuItem.setDisable(false);
                seguimientoMenuItem.setVisible(true); seguimientoMenuItem.setDisable(false);
                modificarRolMenuItem.setVisible(true); modificarRolMenuItem.setDisable(false);
                // Cargar reportes se mantiene oculto
                break;

            case "DIRECTOR DE PROGRAMA":
                // Pueden ver reportes y acciones de mejora
                verReportesMenu.setVisible(true); verReportesMenu.setDisable(false);
                accionesMejoraMenu.setVisible(true); accionesMejoraMenu.setDisable(false);
                cargarReportesMenu.setVisible(true); cargarReportesMenu.setDisable(false);

                subirInformesMenuItem.setVisible(true); subirInformesMenuItem.setDisable(false);

                generalesMenuItem.setVisible(true); generalesMenuItem.setDisable(false);
                especificosMenuItem.setVisible(true); especificosMenuItem.setDisable(false);
                asignarMenuItem.setVisible(true); asignarMenuItem.setDisable(false);
                seguimientoMenuItem.setVisible(true); seguimientoMenuItem.setDisable(false);
                // CRUD y Cargar reportes se mantienen ocultos
                break;

            case "DOCENTE":
                // Los docentes solo pueden ver reportes y seguimiento de acciones
                verReportesMenu.setVisible(true); verReportesMenu.setDisable(false);
                accionesMejoraMenu.setVisible(true); accionesMejoraMenu.setDisable(false); // Mostrar el menú de acciones de mejora

                generalesMenuItem.setVisible(true); generalesMenuItem.setDisable(false); // Mostrar generales
                seguimientoMenuItem.setVisible(true); seguimientoMenuItem.setDisable(false); // HACER VISIBLE SEGUIMIENTO
                // Los demás menús y items se mantienen ocultos por el "por defecto"
                break;

            case "COMITE DE PROGRAMA":
                // Asumimos que el comité tiene acceso a ver reportes y acciones de mejora
                verReportesMenu.setVisible(true); verReportesMenu.setDisable(false);
                accionesMejoraMenu.setVisible(true); accionesMejoraMenu.setDisable(false);

                generalesMenuItem.setVisible(true); generalesMenuItem.setDisable(false);
                especificosMenuItem.setVisible(true); especificosMenuItem.setDisable(false);
                asignarMenuItem.setVisible(true); asignarMenuItem.setDisable(false);
                seguimientoMenuItem.setVisible(true); seguimientoMenuItem.setDisable(false);
                // Los demás se mantienen ocultos
                break;

            case "ESTUDIANTE":
                // Los estudiantes solo pueden ver reportes específicos y generales (si aplica)
                verReportesMenu.setVisible(true); verReportesMenu.setDisable(false);

                generalesMenuItem.setVisible(true); generalesMenuItem.setDisable(false);
                especificosMenuItem.setVisible(true); especificosMenuItem.setDisable(false);
                // Todo lo demás se mantiene oculto por el "por defecto"
                break;

            default:
                System.out.println("Advertencia: tipoUsuario no reconocido. Ocultando todos los menús.");
                // Todos los menús y items ya están ocultos por el "por defecto" inicial
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

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
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
        ViewLoader.loadView("/com/SaberPro/SoftwareFront/Login/Login-view.fxml", stage);
    }

    @FXML
    private void onButtonDashboardInicio() {
        loadView("/com/SaberPro/SoftwareFront/Login/Bienvenida-view.fxml");
    }

    @FXML
    private void onButtonDashboardSReporte() {
        loadView("/com/SaberPro/SoftwareFront/CReporte-view.fxml");
    }

    @FXML
    private void onButtonDashboardSInformes() {
        loadView("/com/SaberPro/SoftwareFront/CInforme-view.fxml");
    }

    @FXML
    private void onButtonDashboardGeneral() {
        loadView("/com/SaberPro/SoftwareFront/ConsultarHistoricos/VReporteG-view.fxml");
    }

    @FXML
    private void onButtonDashboardEspecificos() {
        loadView("/com/SaberPro/SoftwareFront/ConsultarHistoricos/VReporteE-view.fxml");
    }

    @FXML
    private void onButtonDashboardAsignar() {
        loadView("/com/SaberPro/SoftwareFront/Acciones/ModulosAccion.fxml");
    }

    @FXML
    private void onButtonDashboardSeguimiento() {
        loadView("/com/SaberPro/SoftwareFront/Cumplimiento/DetalleCumplimiento.fxml");
    }

    @FXML
    private void onButtonDashboardCrearRol() {
        loadView("/com/SaberPro/SoftwareFront/Roles/CrearRol-view.fxml");
    }

    @FXML
    private void onButtonDashboardModificaRol() {
        loadView("/com/SaberPro/SoftwareFront/Roles/ModificaRol-view.fxml");
    }
}