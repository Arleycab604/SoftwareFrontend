package com.example.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RecoveryController {
    @FXML
    private TextField codeField;

    @FXML
    private void onNextButtonClick() {
        //despues de validar que el codigo sea correcto, se envia a Restore

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Restore-view.fxml"));
                Scene RestoreScene = new Scene(fxmlLoader.load(), 450, 480);

                // Obtener la ventana (Stage) actual desde alguno de los nodos del login-view
                Stage stage = (Stage) codeField.getScene().getWindow();
                stage.setScene(RestoreScene); // Cambiar la escena a la nueva
            } catch (IOException e) {
                e.printStackTrace();
            }

        System.out.println("Biennnnnn");
    }

    @FXML
    private void onReenviarClick(){
        System.out.println("codigo reenviado");
    }


    @FXML
    private void onAtrasClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login-view.fxml"));
            Scene loginScene = new Scene(fxmlLoader.load(), 450, 480);
            Stage stage = (Stage) codeField.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



