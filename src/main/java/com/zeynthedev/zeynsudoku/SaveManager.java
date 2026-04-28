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
public class SaveManager {
    public static final String FILE_NAME = "zeynsudoku_save.dat";
    
    // write data to file
    public static void saveGame(String difficulty, int time, int hint, int[][] board, boolean[][] clues, int[][] solution) {
        Properties props = new Properties();
        
        props.setProperty("difficulty", difficulty);
        props.setProperty("time", String.valueOf(time));
        props.setProperty("hint", String.valueOf(hint));
        
        // convert array 2D to String CSV-styled
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
        
        try (FileOutputStream out = new FileOutputStream(FILE_NAME)) {
            props.store(out, "Zeyn Sudoku Save Game");
        } catch (Exception e) {
            System.out.println("Failed saving game: " + e.getMessage());
        }
    }
    
    // read save data
    public static Properties loadGame() {
        Properties props = new Properties();
        try (FileInputStream in =  new FileInputStream(FILE_NAME)) {
            props.load(in);
            return props;
        } catch (Exception ex) {
            return null;
        }
    }
    
    // delete save data
    public static void deleteSave() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }
    
    // checking availability of saveGame
    public static boolean hasSave() {
        return new File(FILE_NAME).exists();
    }
}
