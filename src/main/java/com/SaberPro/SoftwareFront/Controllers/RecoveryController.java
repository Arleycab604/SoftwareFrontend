package com.SaberPro.SoftwareFront.Controllers;

import com.SaberPro.SoftwareFront.Utils.ViewLoader;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RecoveryController {
    @FXML
    private TextField codeField;

    @FXML
    private void onNextButtonClick() {
        //despues de validar que el codigo sea correcto, se envia a Restore
        Stage stage = (Stage) codeField.getScene().getWindow();
        ViewLoader.loadView("Restore-view.fxml", stage, 600, 600);

        System.out.println("Biennnnnn");
    }

    @FXML
    private void onReenviarClick(){
        System.out.println("codigo reenviado");
    }


    @FXML
    private void onAtrasClick() {
        Stage stage = (Stage) codeField.getScene().getWindow();
        ViewLoader.loadView("Login-view.fxml", stage, 600, 600);
    }
}



