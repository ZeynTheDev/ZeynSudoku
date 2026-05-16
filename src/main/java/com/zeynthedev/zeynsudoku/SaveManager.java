/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zeynthedev.zeynsudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class SaveManager {
    public static final String FILE_NAME = "zeynsudoku_save.dat";
    
    // --- FUNGSI BARU: MENCARI RUMAH YANG AMAN UNTUK DATA ---
    private static File getSaveFile() {
        String userHome = System.getProperty("user.home");
        File saveDir = new File(userHome, ".zeynsudoku");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        return new File(saveDir, FILE_NAME);
    }
    
    public static void saveGame(String difficulty, int time, int hint, int[][] board, boolean[][] clues, int[][] solution) {
        Properties props = new Properties();
        
        props.setProperty("difficulty", difficulty);
        props.setProperty("time", String.valueOf(time));
        props.setProperty("hint", String.valueOf(hint));
        
        StringBuilder sbBoard = new StringBuilder();
        StringBuilder sbClues = new StringBuilder();
        StringBuilder sbSol = new StringBuilder();
        
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                sbBoard.append(board[r][c]).append(",");
                sbClues.append(clues[r][c] ? "1" : "0").append(",");
                sbSol.append(solution[r][c]).append(",");
            }
        }
        
        props.setProperty("board", sbBoard.toString());
        props.setProperty("clues", sbClues.toString());
        props.setProperty("solution", sbSol.toString());
        
        try (FileOutputStream out = new FileOutputStream(getSaveFile())) { // Gunakan fungsi baru
            props.store(out, "Zeyn Sudoku Save Game");
        } catch (Exception e) {
//            System.out.println("Failed saving game: " + e.getMessage());
        }
    }
    
    public static Properties loadGame() {
        Properties props = new Properties();
        try (FileInputStream in =  new FileInputStream(getSaveFile())) { // Gunakan fungsi baru
            props.load(in);
            return props;
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static void deleteSave() {
        File file = getSaveFile(); // Gunakan fungsi baru
        if (file.exists()) {
            file.delete();
        }
    }
    
    public static boolean hasSave() {
        return getSaveFile().exists(); // Gunakan fungsi baru
    }
}