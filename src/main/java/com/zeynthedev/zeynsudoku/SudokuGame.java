/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zeynthedev.zeynsudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author Zeyn
 */
public class SudokuGame {
    private int[][] board;
    private int[][] solution;
    private boolean[][] clues;
    private boolean[][] errors;
    private int[] leftNum;
    
    private String difficulty;
    private int hintLeft;
    private int hintTotal;
    private int secondsRun;
    
    private int errorMadeCount = 0;
    
    public SudokuGame() {
        board = new int[9][9];
        solution = new int[9][9];
        clues = new boolean[9][9];
        errors = new boolean[9][9];
        leftNum = new int[10];
    }
    
    // 1. Game Initialization
    public void startNewGame(String difficulty) {
        this.difficulty = difficulty;
        this.secondsRun = 0;
        this.errorMadeCount = 0;
        
        if (difficulty.equals("Easy")) hintTotal = 10;
        else if (difficulty.equals("Medium")) hintTotal = 6;
        else hintTotal = 3;
        this.hintLeft = hintTotal;
        
        SudokuGenerator generator = new SudokuGenerator();
        int[][] newPuzzle = generator.createPuzzle(difficulty);
        this.solution = generator.getSolutionBoard();
        
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                board[r][c] = newPuzzle[r][c];
                clues[r][c] = (newPuzzle[r][c] != 0);
            }
        }
        
        calcState();
    }
    
    // 2. Load game from zeynsudoku_savegame.dat
    public void loadGame(Properties props) {
        this.difficulty = props.getProperty("difficulty");
        this.secondsRun = Integer.parseInt(props.getProperty("time"));
        this.hintLeft = Integer.parseInt(props.getProperty("hint"));
        
        if (difficulty.equals("Easy")) hintTotal = 10;
        else if (difficulty.equals("Medium")) hintTotal = 6;
        else hintTotal = 3;

        String[] arrBoard = props.getProperty("board").split(",");
        String[] arrClues = props.getProperty("clues").split(",");
        String[] arrSol = props.getProperty("solution").split(",");
        
        int index = 0;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                board[r][c] = Integer.parseInt(arrBoard[index]);
                clues[r][c] = arrClues[index].equals("1");
                solution[r][c] = Integer.parseInt(arrSol[index]);
                index++;
            }
        }
        calcState();
    }
    
    // 3. Logic for placing number (user action)
    public void placeNum(int r, int c, int num) {
        if (!clues[r][c]) {
            board[r][c] = num;
            calcState();
            
            if (num != 0 && errors[r][c]){
                errorMadeCount++;
            }
        }
    }
    
    // 4. Logic for state using-hint (user action)
    public boolean useHint() {
        if (hintLeft <= 0) return false;
        
        List<int[]> emptyCells = new ArrayList<>();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) emptyCells.add(new int[]{r, c});
            }
        }
        
        if(!emptyCells.isEmpty()) {
            int[] target = emptyCells.get(new Random().nextInt(emptyCells.size()));
            int r = target[0];
            int c = target[1];
            
            board[r][c] = solution[r][c];
            clues[r][c] = true;
            hintLeft--;
            calcState();
            return true;
        }
        return false;
    }
    
    // 5. logic to reset board (time not wiped to prevent time scumming)
    public void resetBoard() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (!clues[r][c]) {
                    board[r][c] = 0;
                }
            }
        }
        calcState();
    }
    
    // --------------------------
    // 6. Internal calculation to mapping current condition
    // does it contains error, has a number solved, etc
    // --------------------------
    private void calcState() {
        // reset state
        for (int i = 1; i <= 9; i++) leftNum[i] = 9;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                errors[r][c] = false;
            }
        }
        
        // calculate solved nums or error
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int num = board[r][c];
                if (num == 0) continue;
                
                leftNum[num]--;
                
                //checking row and column
                for (int i = 0; i < 9; i++) {
                    if (i != c && board[r][i] == num) {
                        errors[r][c] = true;
                        errors[r][i] = true;
                    }
                    if (i != r && board[i][c] == num) {
                        errors[r][c] = true;
                        errors[i][c] = true;
                    }
                }
                
                //check 3x3 area
                int startRow = (r / 3) * 3;
                int startCol = (c / 3) * 3;
                for (int i = startRow; i < startRow + 3; i++) {
                    for(int j = startCol; j < startCol + 3; j++) {
                        if((i != r || j != c) && board[i][j] == num) {
                            errors[r][c] = true;
                            errors[i][j] = true;
                        }
                    }
                }
            }
        }
    }
    
    // 7. check win state
    public boolean isGameWon() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0 || errors[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // 8. getter for controller
    public int getNumAt(int r, int c) { return board[r][c]; }
    public boolean isClue(int r, int c) { return clues[r][c]; }
    public boolean isError(int r, int c) { return errors[r][c]; }
    public int getLeftNum(int num) { return leftNum[num]; }
    public String getDifficulty() { return difficulty; }
    public int getHintLeft() { return hintLeft; }
    public int getHintTotal() { return hintTotal; }
    public int getSecondsRun() { return secondsRun; }
    public void setSecondsRun(int secondsRun) { this.secondsRun = secondsRun; }
    
    // 9. getter for save manager
    public int[][] getBoard() { return board; }
    public boolean[][] getClues() { return clues; }
    public int[][] getSolution() { return solution; }
    public int getErrorMadeCount() { return errorMadeCount; }
}
