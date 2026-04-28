package com.zeynthedev.zeynsudoku;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SecondaryController {
    
    @FXML
    private Button btnContinue;
    
    @FXML
    public void initialize() {
        //turning off btnContinue if there's no save
        if (btnContinue != null) {
            btnContinue.setDisable(!SaveManager.hasSave());
        }
    }
    
    @FXML
    private void actContinue() throws IOException {
        GameState.isContinue = true;
        App.setRoot("primary");
    }

    @FXML
    private void actNewGame() throws IOException {
        GameState.isContinue = false;
        //move to main scene (primary.fxml)
        App.setRoot("primary");
    }
    
    @FXML
    private void actExit(){
        Platform.exit();
        System.exit(0);
    }
}