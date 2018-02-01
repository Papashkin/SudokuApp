package SudokuApp;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;

public class MainScreen extends JFrame{

    private JPanel mainPanel = new JPanel();
    private JPanel gamePanel = new JPanel();
    private JPanel addPanel = new JPanel();
    private JButton start = new JButton("Start");
    private JButton load = new JButton("Load");
    private JButton back = new JButton("Back");
    private JButton bPrint = new JButton("Print");

    private DefaultTableModel dtm = new DefaultTableModel(9,9);
    private JTable gameField = new JTable(dtm){
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    };

    private JLabel number_title = new JLabel("Числа для заполнения:");
    private JTable numberTab = new JTable(1,9){
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    };
    private int selectedValue = 0;
    private int lvl = 10;   // complication level;
    private Sudoku sudoku = new Sudoku(9,9);
    private Sudoku solvedSudoku = new Sudoku(9,9);

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

        setBtnParameters(bPrint, bigFont);
        bPrint.addActionListener(new PrintEvent());

        setTableParameters(gameField, numberFont);
        gameField.addMouseListener(new SudokuClick());

        setTableParameters(numberTab, numberFont);
        for (int i = 0; i < numberTab.getColumnCount();i++){
            numberTab.isCellEditable(0, i);
            numberTab.setValueAt(i+1,0,i);
        }
        numberTab.addMouseListener(new NumberChoice());

        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER,100,110));
        mainPanel.add(start);
        mainPanel.add(load);
        mainPanel.setBackground(Color.white);

        gamePanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,15));
        addPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        addPanel.add(back);
        addPanel.add(bPrint);
        gamePanel.add(gameField);
        gamePanel.add(number_title);
        gamePanel.add(numberTab);
        gamePanel.add(addPanel);
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
                for (int row = 0;row < sudoku.rowLength();row++){
                    for (int col = 0;col < sudoku.columnLength();col++){
                        gameField.setValueAt(sudoku.getValue(row,col), row,col);
                    }
                }
                gamePanel.repaint();
                setContentPane(gamePanel);
            }
        }
    }

    class LoadEvent implements ActionListener{
        public void actionPerformed(ActionEvent e){
        }
    }

    class BackEvent implements ActionListener{
        public void actionPerformed(ActionEvent e){
            numberTab.clearSelection();
            gameField.clearSelection();
            selectedValue = 0;
            setContentPane(mainPanel);
        }
    }

    class PrintEvent implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] level = {"Easy", "Normal", "Hard"};
            MessageFormat header = new MessageFormat("Sudoku. " + level[lvl] + " level");
            MessageFormat footer = new MessageFormat("");
            try{
                gameField.print(JTable.PrintMode.FIT_WIDTH, header, footer);
            } catch (PrinterException pe){
                JOptionPane.showMessageDialog(null, pe);
            }
        }
    }

    class NumberChoice implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount()==2) {
                selectedValue = 0;
                numberTab.clearSelection();
            } else if (e.getClickCount() == 1) {
                for (int i = 0; i < numberTab.getColumnCount();i++){
                    if (numberTab.isCellSelected(0,i)) selectedValue = (int) numberTab.getValueAt(0,i);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) { }
        @Override
        public void mouseReleased(MouseEvent e) { }
        @Override
        public void mouseEntered(MouseEvent e) { }
        @Override
        public void mouseExited(MouseEvent e) { }
    }

    class SudokuClick implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            for (int row = 0; row < gameField.getRowCount();row++){
                for (int col = 0; col < gameField.getColumnCount();col++){
                    if (gameField.isCellSelected(row,col)){
                        switch (e.getClickCount()){
                            case 1:
                                if (selectedValue != 0) {
                                    if (0 == (int)sudoku.getValue(row,col)){
                                        gameField.setValueAt(selectedValue,row, col);
                                    }
                                } else {
                                    if (0 == (int)sudoku.getValue(row,col)) {
                                        gameField.setValueAt(0,row, col);
                                    }
                                }
                                break;
                            case 2:
                                gameField.clearSelection();
                        }
                        break;
                    }
                }
            }
            gameField.repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) { }
        @Override
        public void mouseReleased(MouseEvent e) { }
        @Override
        public void mouseEntered(MouseEvent e) { }
        @Override
        public void mouseExited(MouseEvent e) { }
    }

    private void generateSudoku(JTable table, int lvl){
        int columns = table.getColumnCount();
        int rows = table.getRowCount();
        int[] aRow = setFirstRow();
        for (int j = 0; j < columns; j++){
            sudoku.setValue(0,j,aRow[j]);
        }
        for (int i = 1; i < rows; i++){
            if (i == 3 || i == 6){
                aRow = shiftLeft(aRow, 1);
            }
            aRow = shiftLeft(aRow, 3);
            for (int j = 0; j < columns; j++){
                sudoku.setValue(i,j,aRow[j]);
            }
        }
        sudoku.transpose(Math.random());
        sudoku.overturn(Math.random());
        sudoku.swapRowsArea(2);
        sudoku.swapColsArea(2);
        sudoku.swapRows(5);
        sudoku.swapCols(5);
        for (int i = 0; i < sudoku.getRowCount();i++){
            for (int j = 0; j < sudoku.getColumnCount();j++){
                solvedSudoku.setValue(i,j,sudoku.getValue(i,j));
            }
        }
        sudoku.cleanCells(lvl);
    }

    private int[] shiftLeft (int[] inVector, int shiftCount){      //shift the number vector to the left by shiftCount value
        int[] outVector = new int[inVector.length];
        for (int j = 0 ;j < shiftCount;j++){
            outVector[j+(inVector.length)-shiftCount] = inVector[j];
        }
        for (int j = 0;j < (inVector.length)-shiftCount;j++){
            outVector[j] = inVector[j+shiftCount];
        }
        return outVector;
    }

    private void setTableParameters(JTable table, Font aFont){
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
                    tabCol.setCellRenderer(new SudokuCellRenderer(i,j,0));
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

    private int[] setFirstRow(){        // build first row for matrix via random method
        int rndNum, position;
        boolean isUniq = true;
        int i = 0;
        int[] aVector = new int[9];
        do{
            rndNum = (int)(Math.random()*9)+1;        // random value from 1 to 9 including
            for (int num = 0;num < aVector.length;num++){
                if (rndNum == aVector[num]){
                   isUniq = false;
                   break;
                }
            }
            if (isUniq){
                do{
                    position = (int)(Math.random()*9);  // random position in the row
                }
                while (aVector[position] != 0);
                aVector[position] = rndNum;
                i++;
            } else isUniq = true;
        }
        while (i < aVector.length);
        return aVector;
    }
}