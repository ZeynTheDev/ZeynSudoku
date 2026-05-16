package com.zeynthedev.zeynsudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("secondary"));
        
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                event.consume();
            }
        });
        
        // --- PANGGIL TEMA SAAT PERTAMA BUKA APLIKASI ---
        applyTheme(); 
        
        stage.setTitle("Zeyn Sudoku");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        
        // --- PANGGIL TEMA SAAT PINDAH LAYAR ---
        applyTheme();
    }

    // --- FUNGSI KHUSUS PENYUNTIK TEMA ---
    public static void applyTheme() {
        java.util.Properties config = ConfigManager.loadConfig();
        String theme = config.getProperty("theme", "Light").toLowerCase().replace(" ", "");
        
        scene.getStylesheets().clear();
        
        java.net.URL cssUrl = App.class.getResource("css/" + theme + ".css");
        
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
//            System.out.println("Theme " + theme + " is loaded.");
        } else {
//            System.out.println("CSS file for " + theme + " is not exist!");
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}