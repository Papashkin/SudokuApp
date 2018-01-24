package SudokuApp;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame{

    private JPanel mainPanel = new JPanel();
//    private JPanel levelPanel = new JPanel();
    private JPanel gamePanel = new JPanel();
    private JButton start = new JButton("Start");
    private JButton load = new JButton("Load");
    private JButton back = new JButton("Back");

    private DefaultTableModel dtm = new DefaultTableModel(9,9);
    private JTable gameField = new JTable(dtm){
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    };

    private JLabel number_title = new JLabel("Числа для заполнения:");
    private JTable number_row = new JTable(1,9){
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    };
    private int selected = 10;
    private int lvl = 10;                   // complication level;
    private int[][] sudoku = new int[9][9]; // sudoku matrix;
//    private SudokuMatrix sudoku2 = new SudokuMatrix();  //alternative variant of the sudoku matrix

    private Font bigFont = new Font("Arial", Font.BOLD, 24);
    private Font numberFont = new Font("Arial", Font.BOLD, 18);

    MainScreen(){
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,445);
        setLocation(200,100);
        setResizable(false);

        setBtnParameters(start, bigFont);
        start.addActionListener(new StartEvent());

        setBtnParameters(load,bigFont);
        load.addActionListener(new LoadEvent());

        setBtnParameters(back,bigFont);
        back.addActionListener(new BackEvent());

        setParameters(gameField, numberFont);
        setParameters(number_row, numberFont);
        for (int i = 0; i < number_row.getColumnCount();i++){
            number_row.isCellEditable(0, i);
            number_row.setValueAt(i+1,0,i);
        }

        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER,100,110));
        mainPanel.add(start);
        mainPanel.add(load);
        mainPanel.setBackground(Color.white);

//        levelPanel.setLayout(new FlowLayout(FlowLayout.CENTER,100,110));
//        levelPanel.add(start);
//        levelPanel.add(load);
//        levelPanel.setBackground(Color.white);

        gamePanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,15));
        gamePanel.add(gameField);
        gamePanel.add(number_title);
        gamePanel.add(number_row);
        gamePanel.add(back);
        gamePanel.setBackground(Color.white);

        setContentPane(mainPanel);
    }

    class StartEvent extends Component implements ActionListener{
        public void actionPerformed(ActionEvent e){
            Object[] level = {"Easy", "Normal", "Hard"};
            JOptionPane dialogPane = new JOptionPane();
            dialogPane.setLocation(100,200);
            lvl = dialogPane.showOptionDialog(this,"Choose the level",
                    "Levels",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,
                    null,level,10);
            if (lvl == 0 || lvl == 1 || lvl == 2) {
                generateSudoku(gameField, lvl);
                setContentPane(gamePanel);
                gamePanel.repaint();
            }
        }
    }

    class LoadEvent implements ActionListener{
        public void actionPerformed(ActionEvent e){
        }
    }

    class BackEvent implements ActionListener{
        public void actionPerformed(ActionEvent e){
            number_row.clearSelection();
            gameField.clearSelection();
            setContentPane(mainPanel);
        }
    }

    private void generateSudoku(JTable table, int lvl){
        int columns = table.getColumnCount();
        int rows = table.getRowCount();
//        sudoku2.restart();
        int[] aRow = {1,2,3,4,5,6,7,8,9};
        for (int j = 0; j < columns; j++){
            sudoku[0][j] = aRow[j];
//            sudoku2.setValue(0,j,aRow[j]);
        }
        for (int i = 1; i < rows; i++){
            if (i == 3 || i == 6){
                aRow = shiftLeft(aRow, 1);
            }
            aRow = shiftLeft(aRow, 3);
            for (int j = 0; j < columns; j++){
                sudoku[i][j] =  aRow[j];
//                sudoku2.setValue(i,j,aRow[j]);
            }
        }
        sudoku = transpose(sudoku, Math.random());
        sudoku = overturn(sudoku, Math.random());
        sudoku = swapRowsArea(sudoku,6*Math.random());

//        sudoku2.transpose(Math.random());
//        sudoku2.overturn(Math.random());

        for (int row = 0;row < sudoku.length;row++){
            for (int col = 0;col < sudoku[0].length;col++){
                table.setValueAt(sudoku[row][col],row,col);
            }
        }
//        for (int row = 0;row < sudoku2.rowLength();row++){
//            for (int col = 0;col < sudoku2.columnLength();col++){
//                table.setValueAt((sudoku2.getValue(row,col)),row,col);
//            }
//        }

        table.repaint();
    }

    public int[][] swapRowsArea (int[][] iSudoku, double random){               // Method allows to swap areas in horizontal
        int area1, area2;
        int[][] oSudoku = iSudoku.clone();
        int count = (int) random;
        for (int step = 0; step < count; step ++){
            area1 = (int)(3*Math.random());
            do{
                area2 = (int)(3*Math.random());
            }
            while (area2 == area1);
            area1 = area1*3;
            area2 = area2*3;
            for (int row = 0;row < 3;row++){
                for (int col = 0;col < iSudoku[0].length;col++){
                    int val = iSudoku[area1+row][col];
                    oSudoku[area1+row][col] = iSudoku[area2+row][col];
                    oSudoku[area2+row][col] = val;
                }
            }
        }
        return oSudoku;
    }

    public int[][] transpose(int[][] iSudoku, double random){   // Method allows to transpose the sudoku matrix.
        if (random > 0.5d){
            int row = iSudoku.length;
            int col = iSudoku[0].length;
            int[][] oSudoku = new int[row][col];
            for (int i = 0; i < row;i++){
                for (int j = 0; j < col;j++){
                    oSudoku[j][i] = iSudoku[i][j];
                }
            }
            return oSudoku;
        } else { return iSudoku;}
    }

    public int[][] overturn(int[][] iSudoku, double random){   // Method allows to overturn the sudoku matrix.
        if (random > 0.5d){
            int row = iSudoku.length;
            int col = iSudoku[0].length;
            int[][] oSudoku = new int[row][col];
            for (int i = 0; i < row;i++){
                for (int j = 0; j < col;j++){
                    oSudoku[col-i-1][j] = iSudoku[i][j];
                }
            }
            return oSudoku;
        } else { return iSudoku;}
    }

    public int[] shiftLeft (int[] vector, int shiftCount){
        int[] vectorEnd = new int[vector.length];
        for (int j = 0 ;j < shiftCount;j++){
            vectorEnd[j+(vector.length)-shiftCount] = vector[j];
        }
        for (int j = 0;j < (vector.length)-shiftCount;j++){
            vectorEnd[j] = vector[j+shiftCount];
        }
        return vectorEnd;
    }

    private void setParameters(JTable table, Font aFont){
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setBackground(Color.white);
        table.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.black));
        gameField.enableInputMethods(true);
        table.setRowHeight(30);
        for (int i = 0; i < table.getColumnCount();i++) {
            TableColumn tabCol = table.getColumnModel().getColumn(i);
            tabCol.setPreferredWidth(30);
            if (table.getRowCount() != 1){
                for (int j = 0; j < table.getRowCount(); j++) {
                    tabCol.setCellRenderer(new SudokuCellRenderer(i,j));
                }
            }
        }
        table.setColumnSelectionAllowed(true);
        table.setRowSelectionAllowed(true);
        table.setFont(aFont);
        DefaultTableCellRenderer cellRenderer = (DefaultTableCellRenderer) table.getDefaultRenderer(String.class);
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        repaint();
    }

    private void setBtnParameters(JButton button, Font aFont){
        button.setSize(100,50);
        button.setBackground(Color.white);
        button.setFont(aFont);
        button.setBorderPainted(false);
    }
}