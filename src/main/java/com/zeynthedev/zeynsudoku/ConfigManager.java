/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zeynthedev.zeynsudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
/**
 *
 * @author Zeyn
 */
public class ConfigManager {
    private static final String FILE_NAME = "zeyn_config.dat";
    
    public static Properties loadConfig() {
        Properties props = new Properties();
        
        File file = new File(FILE_NAME);
        
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                props.load(in);
            } catch (Exception e) {
//                System.out.println("Failed reading config file: " + e);
            }
        }
        
        if (props.isEmpty() || !props.containsKey("theme")) {
            initDefaults(props);
        }

        return props;
    }
    
    private static void initDefaults(Properties props) {
        props.setProperty("theme", "Light");
        props.setProperty("bgmVolume", "50");
        props.setProperty("sfxVolume", "70");
        props.setProperty("bgmPack", "0"); //1: lofi, 2: classic, 3: touhou, 4: TBA/custom
        
        saveConfig(props);
    }
    
    private static void saveConfig(Properties props) {
        try (FileOutputStream out = new FileOutputStream(FILE_NAME)) {
            props.store(out, "Zeyn Sudoku - User Settings");
        } catch (Exception e) {
            System.out.println("Failed saving config: " + e.getMessage());
        }
    }
    
    public static void saveUserSettings(String theme, double bgmVolume, double sfxVolume, int bgmPack) {
        Properties props = loadConfig();
        
        props.setProperty("theme", theme);
        props.setProperty("bgmVolume", String.valueOf((int) bgmVolume));
        props.setProperty("sfxVolume", String.valueOf((int) sfxVolume));
        props.setProperty("bgmPack", String.valueOf(bgmPack));
        
        saveConfig(props);
//        System.out.println("Setting saved: " + theme + " | BGM Vol: " + (int)bgmVolume + " | SFX Vol: " + (int)sfxVolume + " | Pack: " + bgmPack);
    }
}
