/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zeynthedev.zeynsudoku;

import java.util.Random;

/**
 *
 * @author Zeyn
 */
public class SudokuGenerator {
    private int[][] board;
    private Random random;
    
    private int[][] solutionBoard = new int[9][9];

    public int[][] getSolutionBoard() {
        return solutionBoard;
    }
    
    public SudokuGenerator() {
        board = new int[9][9];
        random = new Random();
    }
    
    public int[][] createPuzzle(String difficulty) {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                board[i][j] = 0;
            }
        }
        
        //reset & fil the entire board
        fillBoard();
        
        //saving a copy first to create some hint from it
        for (int i = 0; i < 9; i++) {
            System.arraycopy(board[i], 0, solutionBoard[i], 0, 9);
        }
        
        // deleting box content based on difficulty (so the box become the answer box)
        int delTotal = 30;
        if (difficulty.equals("Medium"))delTotal = 45;
        if (difficulty.equals("Hard")) delTotal = 60;
        
        deleteBox(delTotal);
        
        return board;
    }
    
    private Boolean fillBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                //find empty box
                if (board[row][col] == 0) {
                    //fill with random num 1-9
                    int[] randomNum = random1to9();
                    
                    for (int num : randomNum) {
                        if (isSafe(row, col, num)) {
                            //if safe, guess the num
                            board[row][col] = num;
                            
                            // recursive step (step ahead to the next box)
                            if (fillBoard()) {
                                return true; //board is filled true
                            } else {
                                //backtracking, the box will be rewritten with the next number
                                board[row][col] = 0;
                            }
                        }
                    }
                    //if was tried 1-9 but no one true, so it should step behind
                    return false;
                }
            }
        }
        return true;
    }
    
    private Boolean isSafe(int row, int col, int num) {
        //checking the conflict based on row or column
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board [i][col] == num) {
                return false;
            }
            
        }
        //checking conflict on the internal area (on 3x3 area)
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private int[] random1to9() {
        int [] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int i =8; i > 0; i-- ) {
            int j = random.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }
    
    private void deleteBox(int totalDel) {
        int count = 0;
        while (count < totalDel) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);

            if(board[row][col] != 0) {
                board[row][col] = 0;
                count++;
            }
        }
    }
}
