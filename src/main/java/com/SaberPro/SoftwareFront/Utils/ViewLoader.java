/*package com.SaberPro.SoftwareFront.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewLoader {

    public static void loadView(String fxmlPath, Stage currentStage, int width, int height) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ViewLoader.class.getResource("/com/SaberPro/SoftwareFront/" + fxmlPath));
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadView(String fxmlPath, Stage currentStage) {
        loadView(fxmlPath, currentStage, ViewConstants.DEFAULT_WIDTH, ViewConstants.DEFAULT_HEIGHT);
    }
}*/
package com.SaberPro.SoftwareFront.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent; // Necesario para fxmlLoader.load()
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL; // Necesario para getResource()

public class ViewLoader {

    // Asumimos que ViewConstants.DEFAULT_WIDTH y ViewConstants.DEFAULT_HEIGHT existen.
    // Si no es así, necesitarás definirlos aquí o crear la clase ViewConstants.

    /**
     * Carga una vista FXML y la establece como la escena de una Stage, con dimensiones específicas.
     *
     * @param fxmlPath     La ruta ABSOLUTA al archivo FXML desde la raíz del classpath.
     * Debe empezar con '/' (ejemplo: "/com/SaberPro/SoftwareFront/Login/Login-view.fxml").
     * @param currentStage La Stage (ventana) donde se cargará la vista.
     * @param width        El ancho deseado para la escena.
     * @param height       La altura deseada para la escena.
     */
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

    /**
     * Carga una vista FXML y la establece como la escena de una Stage, usando dimensiones por defecto.
     *
     * @param fxmlPath     La ruta ABSOLUTA al archivo FXML desde la raíz del classpath.
     * Debe empezar con '/' (ejemplo: "/com/SaberPro/SoftwareFront/Login/Login-view.fxml").
     * @param currentStage La Stage (ventana) donde se cargará la vista.
     */
    public static void loadView(String fxmlPath, Stage currentStage) {
        // Llama al método sobrecargado con las dimensiones por defecto
        loadView(fxmlPath, currentStage, ViewConstants.DEFAULT_WIDTH, ViewConstants.DEFAULT_HEIGHT);
    }

    // Si también usas un método loadView que solo devuelve Parent (sin Stage),
    // asegúrate de que también espere una ruta ABSOLUTA.
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