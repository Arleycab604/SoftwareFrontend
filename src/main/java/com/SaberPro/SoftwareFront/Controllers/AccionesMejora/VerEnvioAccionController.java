package com.SaberPro.SoftwareFront.Controllers.AccionesMejora;

import com.SaberPro.SoftwareFront.Models.EntregaAccion; // Necesitas un modelo para las entregas
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class VerEnvioAccionController {}/*

    @FXML private Label nombreTareaLabel;
    @FXML private TableView<EntregaAccion> entregasTableView;

    // Columnas de la tabla (fx:id coincide con el FXML)
    @FXML private TableColumn<EntregaAccion, String> nombreApellidoColumn;
    @FXML private TableColumn<EntregaAccion, String> nombreUsuarioColumn; // Documento del docente
    @FXML private TableColumn<EntregaAccion, String> correoColumn;
    @FXML private TableColumn<EntregaAccion, String> telefonoColumn;
    @FXML private TableColumn<EntregaAccion, String> ciudadColumn;
    @FXML private TableColumn<EntregaAccion, String> estadoColumn;
    @FXML private TableColumn<EntregaAccion, String> ultimaModificacionEntregaColumn;
    @FXML private TableColumn<EntregaAccion, String> archivosEnviadosColumn;
    @FXML private TableColumn<EntregaAccion, String> comentariosEntregaColumn;
    @FXML private TableColumn<EntregaAccion, String> ultimaModificacionCalificacionColumn;
    @FXML private TableColumn<EntregaAccion, String> comentariosRetroalimentacionColumn;
    @FXML private TableColumn<EntregaAccion, String> calificacionFinalColumn;

    // Para la columna de "Calificación" que tiene un botón
    @FXML private TableColumn<EntregaAccion, Void> calificacionActionColumn;


    private ObservableList<EntregaAccion> listaEntregas = FXCollections.observableArrayList();

    // Formateador de fecha y hora para mostrar en la tabla
    private static final DateTimeFormatter TABLE_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));

    public void initialize() {
        // Inicializar las columnas con PropertyValueFactory
        nombreApellidoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreApellido"));
        nombreUsuarioColumn.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario")); // Mapeado a Documento Docente
        correoColumn.setCellValueFactory(new PropertyValueFactory<>("direccionCorreo"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        ciudadColumn.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Para las fechas, necesitamos formatearlas si el modelo guarda LocalDateTime
        ultimaModificacionEntregaColumn.setCellValueFactory(cellData -> {
            LocalDateTime dateTime = cellData.getValue().getUltimaModificacionEntrega();
            return new ReadOnlyStringWrapper(dateTime != null ? dateTime.format(TABLE_DATE_FORMATTER) : "N/A");
        });

        archivosEnviadosColumn.setCellValueFactory(new PropertyValueFactory<>("archivosEnviados"));
        comentariosEntregaColumn.setCellValueFactory(new PropertyValueFactory<>("comentariosEntrega"));

        ultimaModificacionCalificacionColumn.setCellValueFactory(cellData -> {
            LocalDateTime dateTime = cellData.getValue().getUltimaModificacionCalificacion();
            return new ReadOnlyStringWrapper(dateTime != null ? dateTime.format(TABLE_DATE_FORMATTER) : "N/A");
        });

        comentariosRetroalimentacionColumn.setCellValueFactory(new PropertyValueFactory<>("comentariosRetroalimentacion"));
        calificacionFinalColumn.setCellValueFactory(new PropertyValueFactory<>("calificacionFinal"));


        // Configurar la columna del botón "Calificar"
        calificacionActionColumn.setCellFactory(param -> new TableCell<EntregaAccion, Void>() {
            private final Button calificarBtn = new Button("Calificar");
            {
                calificarBtn.getStyleClass().add("calificar-button"); // Estilo CSS opcional
                calificarBtn.setOnAction(event -> {
                    EntregaAccion entrega = getTableView().getItems().get(getIndex());
                    handleCalificarEntrega(entrega); // Llama al método para calificar con el objeto de la entrega
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Puedes cambiar el texto o visibilidad del botón según el estado de la entrega
                    setGraphic(calificarBtn);
                }
            }
        });

        entregasTableView.setItems(listaEntregas);

        // Cargar datos de ejemplo al inicio
        cargarDatosDeEjemplo();
    }

    public void setNombreTarea(String nombre) {
        nombreTareaLabel.setText("TAREA: " + nombre);
    }

    private void cargarDatosDeEjemplo() {
        listaEntregas.clear(); // Limpiar antes de añadir nuevos

        // Datos de ejemplo basados en tu imagen
        listaEntregas.add(new EntregaAccion(
                "droncancillo@unillanos.edu.co", "DR", "16000527", "Villavicencio", "Enviado para calificar",
                LocalDateTime.of(2025, 3, 24, 22, 48), "Expo_Archivoxml.pptx", "Comentarios (2)",
                null, null, null // Asumo null para campos no presentes en el ejemplo de imagen
        ));
        listaEntregas.add(new EntregaAccion(
                "labelandria@unillanos.edu.co", "LB", "16000505", "Villavicencio", "Enviado para calificar",
                LocalDateTime.of(2025, 4, 22, 9, 57), "taller_expos_operaciones.pptx", "Comentarios (0)",
                null, "35 días 9 horas después", null
        ));
        // Aquí irían más datos reales desde tu servicio/base de datos
    }

    @FXML

        private void handleAtras() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/FXML/AccionesMejora/VerTarjetaAccion.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) nombreTareaHeaderLabel.getScene().getWindow(); // O cualquier nodo del FXML actual
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    @FXML
    private void handleDescargarEntregas() {
        System.out.println("Descargando todas las entregas...");
        // Lógica para iniciar la descarga
    }

    /*private void handleCalificarEntrega(EntregaAccion entrega) {
        System.out.println("Calificando entrega de: " + entrega.getNombreUsuario());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/SaberPro/SoftwareFront/Fxml/AccionesMejora/CalificarAccion.fxml"));
            Parent root = loader.load();
            CalificarAccionController controller = loader.getController();
            controller.setEntrega(entrega); // Pasar la entrega a calificar

            Stage stage = new Stage();
           // stage.setTitle("Calificar Entrega: " + entrega.getNombreUsuario());
            stage.setScene(new Scene(root));
            stage.showAndWait(); // showAndWait() hace que la ventana sea modal, bloqueando la de atrás

            // Después de que la ventana de calificación se cierra, puedes refrescar la tabla
            // para reflejar los cambios en la calificación.
            cargarDatosDeEjemplo(); // O una forma más eficiente de refrescar un elemento de la tabla
        } catch (IOException e) {
            System.err.println("Error al abrir la ventana de calificación: " + e.getMessage());
            e.printStackTrace();
        }
    }}*/
