package com.zeynthedev.zeynsudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class StatisticManager {
    private static final String FILE_NAME = "zeyn_stats.dat";
    
    // --- FUNGSI BARU: MENCARI RUMAH YANG AMAN UNTUK DATA ---
    private static File getStatFile() {
        String userHome = System.getProperty("user.home");
        File saveDir = new File(userHome, ".zeynsudoku");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        return new File(saveDir, FILE_NAME);
    }
    
    // 1. load data or set default (0) if there's no save file
    public static Properties loadStats() {
        Properties props = new Properties();
        File file = getStatFile(); // Gunakan fungsi baru
        
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                props.load(in);
            } catch (Exception e) {
//                System.out.println("Gagal membaca file stat: " + e.getMessage());
            }
        }
        
        // SABUK PENGAMAN: Jika file ada tapi isinya kosong/rusak, paksa isi default!
        if (props.isEmpty() || !props.containsKey("Easy.totalGames")) {
            initDefaults(props);
        }
        
        return props;
    }
    
    // 2. crafting initial default save data template
    private static void initDefaults(Properties props) {
        String[] diffs = { "Easy", "Medium", "Hard", "AllTime" };
        
        for (String d : diffs) {
            props.setProperty(d + ".totalGames", "0");
            props.setProperty(d + ".totalTime", "0");
            props.setProperty(d + ".totalHints", "0");
            props.setProperty(d + ".totalErrors", "0"); 
            
            // best record logic (AllTime doesn't need it)
            if (!d.equals("AllTime")) {
                props.setProperty(d + ".bestTime", "999999");
                props.setProperty(d + ".bestGameNum", "0");
                props.setProperty(d + ".bestErrors", "0");
                props.setProperty(d + ".bestHintsLeft", "0");
            } else {
                props.setProperty("AllTime.mostPlayed", "-");
            }
        }
    }
    
    // 3. saving stats logic
    private static void saveStats(Properties props) {
        try (FileOutputStream out = new FileOutputStream(getStatFile())) { // Gunakan fungsi baru
            props.store(out, "Zeyn Sudoku - Hall of Records");
        } catch (Exception e) {
//            System.out.println("Failed saving statistic: " + e.getMessage());
        }
    }
    
    // 4. record win state (main feature)
    public static void recordWin(String difficulty, int timeInSec, int hintUsed, int errorMade, int hintLeft) {
        Properties props = loadStats();
        
        // 4.a. update all-time stats
        props.setProperty("AllTime.totalGames", String.valueOf(Integer.parseInt(props.getProperty("AllTime.totalGames", "0")) + 1));
        props.setProperty("AllTime.totalTime", String.valueOf(Integer.parseInt(props.getProperty("AllTime.totalTime", "0")) + timeInSec));
        props.setProperty("AllTime.totalHints", String.valueOf(Integer.parseInt(props.getProperty("AllTime.totalHints", "0")) + hintUsed));
        props.setProperty("AllTime.totalErrors", String.valueOf(Integer.parseInt(props.getProperty("AllTime.totalErrors", "0")) + errorMade));
        
        // 4.b. update stats by category
        int currentGames = Integer.parseInt(props.getProperty(difficulty + ".totalGames", "0")) + 1;
        props.setProperty(difficulty + ".totalGames", String.valueOf(currentGames));
        props.setProperty(difficulty + ".totalTime", String.valueOf(Integer.parseInt(props.getProperty(difficulty + ".totalTime", "0")) + timeInSec));
        props.setProperty(difficulty + ".totalHints", String.valueOf(Integer.parseInt(props.getProperty(difficulty + ".totalHints", "0")) + hintUsed));
        props.setProperty(difficulty + ".totalErrors", String.valueOf(Integer.parseInt(props.getProperty(difficulty + ".totalErrors", "0")) + errorMade));
        
        // 4.c. most played calculation
        int easyCount = Integer.parseInt(props.getProperty("Easy.totalGames", "0"));
        int medCount = Integer.parseInt(props.getProperty("Medium.totalGames", "0"));
        int hardCount = Integer.parseInt(props.getProperty("Hard.totalGames", "0"));
        
        String mostPlayed = "Easy";
        int max = easyCount;
        
        if (medCount > max) {
            mostPlayed = "Medium";
            max = medCount;
        }
        
        if (hardCount > max) {
            mostPlayed = "Hard";
            max = hardCount;
        }
        
        props.setProperty("AllTime.mostPlayed", mostPlayed + " (" + max + " games)");

        // 4.d Check Best Record
        int bestTime = Integer.parseInt(props.getProperty(difficulty + ".bestTime", "999999"));
        if (timeInSec < bestTime) {
            props.setProperty(difficulty + ".bestTime", String.valueOf(timeInSec));
            props.setProperty(difficulty + ".bestGameNum", String.valueOf(currentGames));
            props.setProperty(difficulty + ".bestErrors", String.valueOf(errorMade));
            props.setProperty(difficulty + ".bestHintsLeft", String.valueOf(hintLeft));
        }

        saveStats(props);
    }
}