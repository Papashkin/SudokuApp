package SudokuApp;

public class SudokuMatrix {
    private int[][] sudokuMat;

    public SudokuMatrix(int rows, int cols){
        sudokuMat = new int[rows][cols];
    }

    public SudokuMatrix(){
        sudokuMat = new int[9][9];
    }

    public void restart(){
        for (int i = 0; i < sudokuMat.length;i++){
            for (int j = 0; j < sudokuMat[0].length;j++){
                sudokuMat[i][j] = 0;
            }
        }
    }

    public void setValue(int row, int col, Object aValue){
        sudokuMat[row][col] = (int) aValue;
    }

    public Object getValue(int row, int col){
        return sudokuMat[row][col];
    }

    public int rowLength(){
        return sudokuMat.length;
    }

    public int columnLength(){
        return sudokuMat[0].length;
    }

    public void transpose(double random){   // Method allows to transpose the sudoku matrix.
        if (random > 0.5d){
            int row = sudokuMat.length;
            int col = sudokuMat[0].length;
            int[][] oSudoku = sudokuMat.clone();
            for (int i = 0; i < row;i++){
                for (int j = 0; j < col;j++){
                    sudokuMat[j][i] = oSudoku[i][j];
                }
            }
        }
    }

    public void overturn(double random){   // Method allows to overturn the sudoku matrix.
        if (random > 0.5d){
            int row = sudokuMat.length;
            int col = sudokuMat[0].length;
            int[][] oSudoku = sudokuMat.clone();
            for (int i = 0; i < row;i++){
                for (int j = 0; j < col;j++){
                    sudokuMat[col-i-1][j] = oSudoku[i][j];
                }
            }
        }
    }

}
