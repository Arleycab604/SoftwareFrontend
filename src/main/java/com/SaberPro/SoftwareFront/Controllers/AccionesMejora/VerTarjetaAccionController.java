package com.SaberPro.SoftwareFront.Controllers.AccionesMejora;

import com.SaberPro.SoftwareFront.Models.AccionMejora; // Importa tu modelo AccionMejora
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox; // Si se abre en el mismo stage
import javafx.stage.Stage; // Para abrir nuevas ventanas si es necesario
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class VerTarjetaAccionController {

    @FXML private Label nombreTareaHeaderLabel;
    @FXML private Label navegacionTareaLabel;
    @FXML private Label fechaAperturaLabel;
    @FXML private Label fechaCierreLabel;
    @FXML private Label descripcionLabel;
    @FXML private Label ocultadoEstudiantesLabel;
    @FXML private Label participantesLabel;
    @FXML private Label borradoresLabel;
    @FXML private Label enviadosLabel;
    @FXML private Label pendientesCalificarLabel;
    @FXML private Label tiempoRestanteLabel;
    @FXML private TextField buscarTextField; // Para el campo "Buscar como fecha"

    @FXML private ChoiceBox<String> siguienteActividadChoiceBox; // Para la ChoiceBox de "Siguiente actividad"


    private AccionMejora accionMejoraActual; // La acción de mejora que se está visualizando

    // Formateador de fecha y hora
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de'yyyy, HH:mm", new Locale("es", "ES"));

    public void initialize() {
        // Inicializar cualquier cosa que necesites al cargar la vista
        // Por ejemplo, poblar el ChoiceBox si tiene opciones estáticas
        siguienteActividadChoiceBox.getItems().addAll("Opción 1", "Opción 2", "Opción 3");
        siguienteActividadChoiceBox.setValue("Opción 1"); // Establecer un valor por defecto
    }

    public void setAccionMejora(AccionMejora accion) {
        this.accionMejoraActual = accion;
        if (accion != null) {
            nombreTareaHeaderLabel.setText("TAREA: " + accion.getNombreTarea());
            navegacionTareaLabel.setText("Tarea: " + accion.getNombreTarea());
            fechaAperturaLabel.setText(accion.getPermitirEntregasDesde() != null ? accion.getPermitirEntregasDesde().format(FORMATTER) : "No definida");
            fechaCierreLabel.setText(accion.getFechaEntregaLimite() != null ? accion.getFechaEntregaLimite().format(FORMATTER) : "No definida");
            descripcionLabel.setText(accion.getDescripcionGeneral());

            // Rellenar el sumario de calificaciones (estos datos vendrían de tu lógica de negocio)
            ocultadoEstudiantesLabel.setText("No"); // Asumiendo el valor del ejemplo
            participantesLabel.setText("25");
            borradoresLabel.setText("1");
            enviadosLabel.setText("12");
            pendientesCalificarLabel.setText("12");
            tiempoRestanteLabel.setText("Tiempo pendiente"); // O calcular la diferencia de tiempo real
        }
    }

    @FXML
    private void handleIrPaginaPrincipal() {
        System.out.println("Navegar a Página principal.");
        // Lógica para cambiar la vista principal (ej. a un dashboard general)
    }

    @FXML
    private void handleIrMisCursos() {
        System.out.println("Navegar a Mis cursos.");
        // Lógica para ir a la vista de cursos
    }

    @FXML
    private void handleVerEntregas() {
        if (accionMejoraActual == null) {
            System.err.println("No hay una acción de mejora seleccionada para ver las entregas.");
            return;
        }
        System.out.println("Navegar a la vista de VerEnvioAccion para la tarea: " + accionMejoraActual.getNombreTarea());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/Fxml/AccionesMejora/VerEnvioAccion.fxml"));
            Parent root = loader.load();
            VerEnvioAccionController controller = loader.getController();
            controller.setNombreTarea(accionMejoraActual.getNombreTarea()); // Pasar el nombre de la tarea

            // Si necesitas pasar el objeto AccionMejora completo al controlador de envíos
            // controller.setAccionMejora(accionMejoraActual); // Asegúrate de que VerEnvioAccionController tenga este método

            // Esto asume que VerEnvioAccion se abre en la misma ventana
            // Si se abre en una nueva ventana, usarías un nuevo Stage.
            Scene scene = new Scene(root);
            Stage stage = (Stage) nombreTareaHeaderLabel.getScene().getWindow(); // Obtener el stage actual
            stage.setScene(scene);
            stage.setTitle("Entregas de " + accionMejoraActual.getNombreTarea());
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar VerEnvioAccion.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCalificarDirecto() {
        if (accionMejoraActual == null) {
            System.err.println("No hay una acción de mejora seleccionada para calificar.");
            return;
        }
        System.out.println("Navegar directamente a la interfaz de calificación para una entrega de la tarea: " + accionMejoraActual.getNombreTarea());
        // Esto podría abrir la ventana de calificación para la primera entrega pendiente,
        // o simplemente llevarte a la vista de tabla de entregas (`VerEnvioAccion`)
        // desde donde puedes seleccionar qué calificar.
        handleVerEntregas(); // Por simplicidad, por ahora te lleva a la tabla de entregas.
    }

    // Puedes añadir métodos para manejar las otras pestañas si son funcionales
    // @FXML private void handleConfiguracionTabSelected() { ... }
    // @FXML private void handleCalificacionAvanzadaTabSelected() { ... }
}