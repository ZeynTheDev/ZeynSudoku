/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.zeynthedev.zeynsudoku;

import java.io.IOException;
import java.util.Properties;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class SettingsController {

    @FXML private ComboBox<String> cmbTheme;
    @FXML private ComboBox<String> cmbPack;
    @FXML private Slider sldBgm;
    @FXML private Slider sldSfx;
    @FXML private Label lblBgmVal;
    @FXML private Label lblSfxVal;

    @FXML
    public void initialize() {
        // 1. Muat pengaturan saat ini
        Properties config = ConfigManager.loadConfig();
        
        // 2. Set nilai Tema
        cmbTheme.setValue(config.getProperty("theme", "Light"));
        
        // 3. Set nilai BGM Pack (Indeks 0 = "Pack 1: Basic Lofi", dst)
        int packIndex = Integer.parseInt(config.getProperty("bgmPack", "0"));
        cmbPack.getSelectionModel().select(packIndex);
        
        // 4. Set nilai Slider dan teks persentasenya
        double bgmVol = Double.parseDouble(config.getProperty("bgmVolume", "50"));
        double sfxVol = Double.parseDouble(config.getProperty("sfxVolume", "70"));
        
        sldBgm.setValue(bgmVol);
        lblBgmVal.setText((int)bgmVol + "%");
        
        sldSfx.setValue(sfxVol);
        lblSfxVal.setText((int)sfxVol + "%");
        
        // 5. Beri efek real-time saat slider digeser
        sldBgm.valueProperty().addListener((observable, oldValue, newValue) -> {
            lblBgmVal.setText(newValue.intValue() + "%");
            // Setel suara aslinya secara real-time agar user tahu seberapa keras suaranya
            AudioManager.getInstance().setBgmVolume(newValue.doubleValue()); 
        });
        
        sldSfx.valueProperty().addListener((observable, oldValue, newValue) -> {
            lblSfxVal.setText(newValue.intValue() + "%");
        });
    }

    @FXML
    private void actApply() throws IOException {
        // Ambil data dari UI
        String selectedTheme = cmbTheme.getValue();
        int selectedPack = cmbPack.getSelectionModel().getSelectedIndex();
        double bgmVol = sldBgm.getValue();
        double sfxVol = sldSfx.getValue();

        // Cek apakah pack lagunya diganti
        Properties oldConfig = ConfigManager.loadConfig();
        int oldPack = Integer.parseInt(oldConfig.getProperty("bgmPack", "0"));

        // Simpan ke file .dat
        ConfigManager.saveUserSettings(selectedTheme, bgmVol, sfxVol, selectedPack);

        // Jika pack lagunya diganti, hentikan lagu yang sekarang (efek senyap dramatis)
        if (selectedPack != oldPack) {
            AudioManager.getInstance().stopBGM();
        }

        // Kembali ke main menu
        App.setRoot("secondary");
    }

    @FXML
    private void actCancel() throws IOException {
        // Jika dibatalkan, kembalikan volume BGM ke angka semula (berjaga-jaga jika sempat digeser)
        Properties config = ConfigManager.loadConfig();
        double oldBgmVol = Double.parseDouble(config.getProperty("bgmVolume", "50"));
        AudioManager.getInstance().setBgmVolume(oldBgmVol);
        
        App.setRoot("secondary");
    }
}
