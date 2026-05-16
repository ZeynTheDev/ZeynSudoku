/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.zeynthedev.zeynsudoku;

import java.io.IOException;
import java.util.Properties;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Zeyn
 */
public class RecordsController {

    /**
     * Initializes the controller class.
     */
    @FXML private Label lblCategory;
    @FXML private Label lblTotalGames, lblTotalTime, lblTotalHints, lblTotalErrors;
    @FXML private Label lblBestTime, lblBestDetails, lblMostPlayed;
    @FXML private VBox boxBestRecord, boxMostPlayed;    
    
    private Properties stats;
    private final String[] categories = {"Easy", "Medium", "Hard", "AllTime"};
    private int currentIndex = 0;
    
    @FXML
    public void initialize() {
        stats = StatisticManager.loadStats();
        updateUI();
    }
    
    @FXML
    private void actNext() {
        currentIndex++;
        if (currentIndex >= categories.length) currentIndex = 0; // Looping kembali ke awal
        updateUI();
    }

    @FXML
    private void actPrev() {
        currentIndex--;
        if (currentIndex < 0) currentIndex = categories.length - 1; // Looping ke akhir
        updateUI();
    }
    
    private void updateUI() {
        String cat = categories[currentIndex];
        
        //update title
        if (cat.equals("AllTime")) {
            lblCategory.setText("ALL TIME");
        } else {
            lblCategory.setText(cat.toUpperCase());
        }
        
        //update basic stats
        lblTotalGames.setText(stats.getProperty(cat + ".totalGames", "0"));
        lblTotalHints.setText(stats.getProperty(cat + ".totalHints", "0"));
        lblTotalErrors.setText(stats.getProperty(cat + ".totalErrors", "0"));
        
        int totalSecs = Integer.parseInt(stats.getProperty(cat + ".totalTime", "0"));
        lblTotalTime.setText(formatTime(totalSecs));
        
        //show the fit box
        if (cat.equals("AllTime")) {
            boxBestRecord.setVisible(false);
            boxBestRecord.setManaged(false);
            
            boxMostPlayed.setVisible(true);
            boxMostPlayed.setManaged(true);
            
            lblMostPlayed.setText(stats.getProperty("AllTime.mostPlayed", "-"));
        } else {
            boxMostPlayed.setVisible(false);
            boxMostPlayed.setManaged(false);
            
            boxBestRecord.setVisible(true);
            boxBestRecord.setManaged(true);

            int bestSecs = Integer.parseInt(stats.getProperty(cat + ".bestTime", "999999"));
            if (bestSecs == 999999) {
                lblBestTime.setText("Time: No Record Yet");
                lblBestDetails.setText("Play to set a record!");
            } else {
                lblBestTime.setText("Time: " + formatTime(bestSecs) + " (Game #" + stats.getProperty(cat + ".bestGameNum") + ")");
                lblBestDetails.setText("Hints Left: " + stats.getProperty(cat + ".bestHintsLeft") + "  |  Errors Made: " + stats.getProperty(cat + ".bestErrors"));
            }
        }
    }
    
    //function to convert seconds as HH:mm:ss
    private String formatTime(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;
        
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
    
    @FXML
    private void actBack() throws IOException {
        App.setRoot("secondary"); // Kembali ke main menu
    }
}
