package com.SaberPro.SoftwareFront.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent; // Necesario para fxmlLoader.load()
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL; // Necesario para getResource()

public class ViewLoader {

    public static void loadView(String fxmlPath, Stage currentStage, int width, int height) {
        try {
            // fxmlPath YA DEBE SER UNA RUTA ABSOLUTA QUE EMPIEZA CON '/'
            URL fxmlUrl = ViewLoader.class.getResource(fxmlPath);

            if (fxmlUrl == null) {
                // Mensaje de error más claro si el recurso no se encuentra.
                System.err.println("Error: Recurso FXML no encontrado en la ruta: " + fxmlPath);
                throw new IOException("Recurso FXML no encontrado: " + fxmlPath);
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Parent root = fxmlLoader.load(); // Carga la raíz de la vista FXML
            Scene scene = new Scene(root, width, height); // Crea la escena con la raíz y dimensiones
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Imprime el stack trace completo para depuración
            System.err.println("Excepción al cargar la vista FXML: " + fxmlPath);
            // Re-lanzar como RuntimeException para que el error sea visible y no se "trague" silenciosamente.
            throw new RuntimeException("Fallo al cargar la vista: " + fxmlPath, e);
        }
    }

    public static void loadView(String fxmlPath, Stage currentStage) {
        // Llama al método sobrecargado con las dimensiones por defecto
        loadView(fxmlPath, currentStage, ViewConstants.DEFAULT_WIDTH, ViewConstants.DEFAULT_HEIGHT);
    }

    /*
    public static Parent loadView(String fxmlPath) {
        try {
            URL fxmlUrl = ViewLoader.class.getResource(fxmlPath);

            if (fxmlUrl == null) {
                System.err.println("Error: Recurso FXML no encontrado en la ruta: " + fxmlPath);
                throw new IOException("Recurso FXML no encontrado: " + fxmlPath);
            }
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista (devuelve Parent): " + fxmlPath);
            throw new RuntimeException("Fallo al cargar la vista: " + fxmlPath, e);
        }
    }
    */
}