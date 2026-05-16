package com.zeynthedev.zeynsudoku;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SecondaryController {
    
    @FXML
    private Button btnContinue;
    
    @FXML
    public void initialize() {
        //turning off btnContinue if there's no save
        AudioManager.getInstance().playBGM();
        
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
        // 1. TANYAKAN DIFFICULTY DULU DI SINI!
        String difficulty = showDifficulty();
        
        // 2. Jika user MENEKAN LEVEL (Bukan Cancel), baru pindah layar
        if (difficulty != null) {
            GameState.isContinue = false;
            GameState.targetDifficulty = difficulty; // Titipkan level ke kurir
            App.setRoot("primary");
        }
        // Jika Cancel, kode akan berhenti di sini dan user tetap aman di Main Menu.
    }
    
    @FXML
    private void actRecords() throws IOException {
        App.setRoot("records");
    }
    
    @FXML
    private void actSettings() throws IOException {
        App.setRoot("settings");
    }
    
    @FXML
    private void actCredits() throws IOException {
        App.setRoot("credits");
    }
    
    @FXML
    private void actExit(){
        Platform.exit();
        System.exit(0);
    }

    // --- COPY DARI PRIMARY: FUNGSI DIALOG DIFFICULTY ---
    private String showDifficulty() {
        final String[] result = {null};
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("dialog-root"); 
        
        Label title = new Label("Choose The Difficulty");
        title.getStyleClass().add("dialog-title"); 
        
        Button btnEasy = new Button("Easy (10 Hint)");
        Button btnMedium = new Button("Medium (6 Hint)");
        Button btnHard = new Button("Hard (3 Hint)");
        Button btnCancel = new Button("Cancel");
        
        btnEasy.setPrefWidth(150); btnMedium.setPrefWidth(150); btnHard.setPrefWidth(150);
        
        btnEasy.setOnAction(e -> { result[0] = "Easy"; dialog.close(); });
        btnMedium.setOnAction(e -> { result[0] = "Medium"; dialog.close(); });
        btnHard.setOnAction(e -> { result[0] = "Hard"; dialog.close(); });
        btnCancel.setOnAction(e -> { dialog.close(); });
        
        layout.getChildren().addAll(title, btnEasy, btnMedium, btnHard, btnCancel);
        Scene scene = new Scene(layout);
        
        // Suntik CSS Tema
        java.util.Properties config = ConfigManager.loadConfig();
        String theme = config.getProperty("theme", "Light").toLowerCase().replace(" ", "");
        java.net.URL cssUrl = App.class.getResource("css/" + theme + ".css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
        
        dialog.setScene(scene);
        dialog.showAndWait();
        return result[0];
    }
}