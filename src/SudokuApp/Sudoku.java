package SudokuApp;

import javax.swing.*;

public class Sudoku extends JTable{
    private int[][] mBoard;
    private int boxSize;
    private int boardSize;

    public Sudoku(int rows, int cols)
    {
        mBoard = new int[rows][cols];
        boardSize = mBoard.length;
        boxSize = (int) Math.sqrt(boardSize);
    }

    public void erase(){
        for (int i = 0; i < mBoard.length;i++){
            for (int j = 0; j < mBoard[0].length;j++){
                mBoard[i][j] = 0;
            }
        }
    }

    public void setValue(int row, int col, Object value){
        mBoard[row][col] = (int) value;
    }

    public Object getValue(int row, int col){
        return mBoard[row][col];
    }

    public int rowLength(){
        return mBoard.length;
    }

    public int columnLength(){
        return mBoard.length;
    }

    public void transpose(double random){   // Method allows to transpose the sudoku matrix.
        if (random > 0.5d){
            int row = mBoard.length;
            int col = mBoard.length;
            int[][] oSudoku = new int[row][col];
            for (int i = 0; i < row;i++){
                for (int j = 0; j < col;j++){
                    oSudoku[j][i] = mBoard[i][j];
                }
            }
            mBoard = oSudoku.clone();
        }
    }

    public void overturn(double random){   // Method allows to overturn the sudoku matrix.
        if (random > 0.5d){
            int row = mBoard.length;
            int col = mBoard.length;
            int[][] oSudoku = new int[row][col];
            for (int i = 0; i < row;i++){
                for (int j = 0; j < col;j++){
                    oSudoku[col-i-1][j] = mBoard[i][j];
                }
            }
            mBoard = oSudoku.clone();
        }
    }

    public void swapRowsArea (int count){   // Method allows to swap areas in horizontal
        int area1, area2;
        int[][] outSudoku = mBoard.clone();
        for (int step = 0; step < count; step ++){
            area1 = (int)(3*Math.random());
            do area2 = (int)(3*Math.random());
            while (area2 == area1);
            area1 = area1*3;
            area2 = area2*3;
            for (int row = 0;row < 3;row++){
                for (int col = 0;col < mBoard.length;col++){
                    int val = mBoard[area1+row][col];
                    outSudoku[area1+row][col] = mBoard[area2+row][col];
                    outSudoku[area2+row][col] = val;
                }
            }
        }
        mBoard = outSudoku.clone();
    }

    public void swapColsArea (int count){   // Method allows to swap areas in vertical
        int area1, area2;
        int[][] outSudoku = mBoard.clone();
        for (int step = 0; step < count; step ++){
            area1 = (int)(3*Math.random());
            do area2 = (int)(3*Math.random());
            while (area2 == area1);
            area1 = area1*boxSize;
            area2 = area2*boxSize;
            for (int col = 0;col < boxSize;col++){
                for (int row = 0;row < mBoard.length;row++){
                    int val = mBoard[row][area1+col];
                    outSudoku[row][area1+col] = mBoard[row][area2+col];
                    outSudoku[row][area2+col] = val;
                }
            }
        }
        mBoard = outSudoku.clone();
    }


    public void cleanCells(int lvl){        // create well-played game field from the matrix (via random method)
        int filledCells;
        int[] cellValue;
        int i = (int)Math.pow(boardSize,2);
        if (lvl == 0) {
            filledCells = 35;
        } else if (lvl ==1){
            filledCells = 30;
        } else filledCells = 25;

        do{
            cellValue = getCellValue();
            mBoard[cellValue[0]][cellValue[1]] = 0;
            if (isUniqueSolve(cellValue)) {
                i--;
            } else {
                mBoard[cellValue[0]][cellValue[1]] = cellValue[2];
            }
        }while (i > filledCells);
    }

    private int[] getCellValue(){
       int[] cell = new int[3];
       int val, row, col;
       do {
           row = (int)(9*Math.random());
           col = (int)(9*Math.random());
           val = mBoard[row][col];
       }
       while (val == 0);
        cell[0] = row;
        cell[1] = col;
        cell[2] = val;
       return cell;
    }

    private boolean isUniqueSolve (int[] cell){
        int aVal;
        int row = cell[0];
        int col = cell[1];
        int val = cell[2];
        int[] area;
        for (int i = 0; i < boardSize; i++){    // is unique in row
            aVal = mBoard[row][i];
            if (aVal == val){
                return false;
            }
        }
        for (int i = 0; i < boardSize; i++){    // is unique in column
            aVal = mBoard[i][col];
            if (aVal == val){
                return false;
            }
        }
        area = getAreaIndex(row, col);          // is unique in box 3x3 area
        for (int i = 0;i < area.length;i++) {
            area[i] = area[i] * 3;
        }
        for (int i = 0;i < 3;i++){
            for (int j = 0;j < 3;j++){
                aVal = mBoard[area[0]+i][area[1]+j];
                if (aVal == val){
                    return false;
                }
            }
        }
        return true;
    }

    private int[] getAreaIndex (int row, int col) {
        int[] area = new int[2];
        int[] areaIndex = {0,0,0,1,1,1,2,2,2};
        area[0] = areaIndex[row];
        area[1] = areaIndex[col];
        return  area;
    }

    private int zeroCount(){
        int count = 0;
        for (int row = 0;row < boardSize;row++){
            for (int col = 0;col < 3;col++){
                if (mBoard[row][col] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

}
