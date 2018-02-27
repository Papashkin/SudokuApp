package SudokuApp;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.MessageFormat;
import java.util.Scanner;

public class MainScreen extends JFrame{

    private JPanel mainPanel = new JPanel();
    private JPanel gamePanel = new JPanel();
    private JPanel addPanel = new JPanel();
    private JButton start = new JButton("Новая игра");
    private JButton load = new JButton("Продолжить");
    private JButton back = new JButton("Назад");
    private JButton bPrint = new JButton("Печать");
    private File savedSudoku = new File("savedSudoku.txt");
    private FileReader reader;
    private FileWriter writer;

    private DefaultTableModel dtm = new DefaultTableModel(9,9);
    private JTable gameField = new JTable(dtm){
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex){ return false; }
    };

    private JLabel number_title = new JLabel("Числа для заполнения:");
    private JTable numberTab = new JTable(1,9){
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    };
    private int selectedValue = 0;
    private int lvl = 10;
    private Sudoku sudoku = new Sudoku(9,9);
    private Sudoku solvedSudoku = new Sudoku(9,9);

    private Object[] level = {"Легкий", "Средний", "Тяжелый"};
    private Object[] choise = {"Да","Нет","Отмена"};

    private Font bigFont = new Font("Arial", Font.BOLD, 24);
    private Font numberFont = new Font("Arial", Font.BOLD, 18);

    MainScreen() throws Exception {
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setSize(300,450);
        setLocation(dimension.width/2 - 150,dimension.height/2 - 225);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int count = 0;
                for (int r = 0;r < sudoku.rowLength();r++){
                    for (int c = 0;c < sudoku.columnLength();c++){
                        if (0 != (int)sudoku.getValue(r,c)) count++;
                    }
                }
                if (count != 0){ saveToFile(); }
                System.exit(0);
            }
        });

        setBtnParameters(start, bigFont);
        start.addActionListener(event -> {
            JOptionPane dialogPane = new JOptionPane();
            dialogPane.setLocation(100,200);
            lvl = dialogPane.showOptionDialog(mainPanel,"Выберете уровень сложности:",
                    "Сложность",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,
                    null,level,10);
            if (lvl == 0 || lvl == 1 || lvl == 2) {
                try{
                    if (savedSudoku.isFile()){
                        savedSudoku.delete();
                    };
                }catch (Exception exс){
                    JOptionPane.showMessageDialog(null,  "Операция не выполнилась :( ");
                }
                gameField.repaint();
                generateSudoku(gameField, lvl);
                for (int row = 0;row < sudoku.rowLength();row++){
                    for (int col = 0;col < sudoku.columnLength();col++){
                        gameField.setValueAt(sudoku.getValue(row,col), row,col);
                    }
                }
                gamePanel.repaint();
                setContentPane(gamePanel);
            }
        });

        setBtnParameters(load,bigFont);
        load.addActionListener(event ->{
            int count = sudoku.rowLength()*sudoku.columnLength();
            char[] sSudoku = new char[count];
            char[] sTable = new char[count];
            int iSudoku, iTable;
            try{
                reader = new FileReader(savedSudoku);
                Scanner scan = new Scanner(reader);
                int i = 1;
                count = 0;
                while (scan.hasNextLine()){
                    if (i == 1) {
                        sSudoku = scan.nextLine().toCharArray();
                        i++;
                    } else sTable = scan.nextLine().toCharArray();
                }
                reader.close();
                for (int row = 0;row < sudoku.rowLength();row ++){
                    for (int col = 0;col < sudoku.columnLength();col++){
                        iSudoku = sSudoku[count] - '0';     // iSudoku = Character.getNumericValue(sSudoku[count]);
                        iTable = sTable[count] - '0';       // iTable = Character.getNumericValue(sTable[count]);
                        sudoku.setValue(row,col,iSudoku);
                        gameField.setValueAt(iTable,row,col);
                        count++;
                    }
                }
                gamePanel.repaint();
                setContentPane(gamePanel);
            } catch (Exception exc){}
        });

        if (savedSudoku.isFile()){
            load.setVisible(true);
        } else {
            load.setVisible(false);
        }

        setBtnParameters(back,bigFont);
        back.addActionListener(event ->
        {
            int answer = -1;
            JOptionPane returnPane = new JOptionPane();
            returnPane.setLocation(100,200);
            answer = returnPane.showOptionDialog(gamePanel,"Сохранить?",
                "Сохранение",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,
                null, choise,10);
            if (answer == 0 || answer == 1){
                if (answer == 0) saveToFile();              // save the sudoku and a game grid
                if (answer == 1) load.setVisible(false);
                numberTab.clearSelection();
                gameField.clearSelection();
                selectedValue = 0;
                sudoku.erase();
                mainPanel.repaint();
                setContentPane(mainPanel);
            }
        }
        );

        setBtnParameters(bPrint, bigFont);
        bPrint.addActionListener(event ->  {
            MessageFormat header = new MessageFormat("Судоку. " + level[lvl] + " уровень сложности");
            MessageFormat footer = new MessageFormat(" - s - u - d - o - k - u - ");
            try{
                gameField.print(JTable.PrintMode.FIT_WIDTH, header, footer);
            } catch (PrinterException pe){
                JOptionPane.showMessageDialog(null, pe);
            }
        });

        setTableParameters(gameField, numberFont);
        setTableParameters(numberTab, numberFont);
        for (int i = 0; i < numberTab.getColumnCount();i++){
            numberTab.isCellEditable(0, i);
            numberTab.setValueAt(i+1,0,i);
        }
        gameField.addMouseListener(new SudokuClick());
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
                                if (0 == (int)sudoku.getValue(row,col)){
                                    if (selectedValue != 0) {
                                        gameField.setValueAt(selectedValue,row, col);
                                    } else {
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
            if (solve()){
                JOptionPane winnerPane = new JOptionPane();
                winnerPane.setLocation(100,200);
                winnerPane.showMessageDialog(gamePanel,"Поздравляем!\n" + "Судоку разгадан.","Поздравляем!",
                        JOptionPane.INFORMATION_MESSAGE);
                numberTab.clearSelection();
                gameField.clearSelection();
                selectedValue = 0;
                sudoku.erase();
                mainPanel.repaint();
                setContentPane(mainPanel);
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
        sudoku.swapRowsArea(40);
        sudoku.swapColsArea(40);
        sudoku.swapRows(80);
        sudoku.swapCols(80);
        for (int i = 0; i < sudoku.getRowCount();i++){
            for (int j = 0; j < sudoku.getColumnCount();j++){
                solvedSudoku.setValue(i,j,sudoku.getValue(i,j));
            }
        }
        sudoku.cleanCells(lvl);
    }

    private void saveToFile(){
        if (savedSudoku.isFile()){
            savedSudoku.delete();
        }
        String val;
        try{
            savedSudoku.createNewFile();
            writer = new FileWriter(savedSudoku);
            for(int row = 0;row < sudoku.rowLength();row++){
                for (int col = 0;col < sudoku.columnLength();col++){
                    val = sudoku.getValue(row,col).toString();
                    writer.append(val);
                }
            }
            writer.append('\n');
            for(int row = 0;row < gameField.getRowCount();row++){
                for (int col = 0;col < gameField.getColumnCount();col++){
                    val = gameField.getValueAt(row,col).toString();
                    writer.append(val);
                }
            }
            writer.close();
            load.setVisible(true);
        }catch (Exception exс){
            JOptionPane.showMessageDialog(null,  "Операция отменена. Попробуйте в другой раз");
            savedSudoku.delete();
        }
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
                    tabCol.setCellRenderer(new SudokuCellRenderer());
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

    private boolean solve(){        // check the sudoku's solution.
        boolean isFinish = false;
        int count = 0;
        int solution = 0;
        for(int row = 0;row < gameField.getRowCount();row++) {
            for (int col = 0; col < gameField.getColumnCount(); col++) {
                solvedSudoku.setValue(row, col, gameField.getValueAt(row, col));
                if (0 == (int) gameField.getValueAt(row, col)) count++;
            }
        }
        if (count == 0) {
            for(int r = 0;r < solvedSudoku.rowLength();r++){
                for (int c = 0;c < solvedSudoku.columnLength();c++) {
                    int val = (int)solvedSudoku.getValue(r, c);
                    if (!isUniqueSolve(r,c,val)) solution++;
                }
            }
            if (solution == 0) {
                isFinish = true;
            }
        }
        return  isFinish;
    }

    public boolean isUniqueSolve (int row, int col, int val){ // check the solution of the sudoku (must be one solution)
        int aVal;
        int[] area;
        for (int i = 0; i < solvedSudoku.rowLength(); i++){    // is unique in row
            aVal = (int) solvedSudoku.getValue(row,i);
            if (aVal == val && (i != col)){
                return false;
            }
        }
        for (int i = 0; i < solvedSudoku.columnLength(); i++){    // is unique in column
            aVal = (int) solvedSudoku.getValue(i, col);
            if (aVal == val && i != row){
                return false;
            }
        }
        area = solvedSudoku.getAreaIndex(row, col);          // is unique in box 3x3 area
        for (int i = 0;i < area.length;i++) {
            area[i] = area[i] * 3;
        }
        for (int i = 0;i < 3;i++){
            for (int j = 0;j < 3;j++){
                aVal = (int)solvedSudoku.getValue(area[0]+i, area[1]+i);
                if (aVal == val && (row != area[0]+i) && (col != area[1]+i)){
                    return false;
                }
            }
        }
        return true;
    }
}