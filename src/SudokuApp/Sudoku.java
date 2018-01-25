package SudokuApp;

public class Sudoku {
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
            do{ area2 = (int)(3*Math.random()); } while (area2 == area1);
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
            do{ area2 = (int)(3*Math.random()); } while (area2 == area1);
            area1 = area1*3;
            area2 = area2*3;
            for (int col = 0;col < 3;col++){
                for (int row = 0;row < mBoard.length;row++){
                    int val = mBoard[row][area1+col];
                    outSudoku[row][area1+col] = mBoard[row][area2+col];
                    outSudoku[row][area2+col] = val;
                }
            }
        }
        mBoard = outSudoku.clone();
    }
}
