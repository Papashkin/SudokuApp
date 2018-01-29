package SudokuApp;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
    private JTable numberTab = new JTable(1,9){
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    };
    private int selectedValue = 0;
    private int lvl = 10;   // complication level;
    private Sudoku sudoku = new Sudoku(9,9);

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
        gamePanel.add(gameField);
        gamePanel.add(number_title);
        gamePanel.add(numberTab);
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
                gamePanel.repaint();
                setContentPane(gamePanel);
            }
        }
    }

    class LoadEvent implements ActionListener{
        public void actionPerformed(ActionEvent e){
        }
    }

    class NumberChoice implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            for (int i = 0; i < numberTab.getColumnCount();i++){
                if (numberTab.isCellSelected(0,i)) selectedValue = (int) numberTab.getValueAt(0,i);
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
            if (selectedValue != 0){
                e.getComponent();
                for (int row = 0; row < gameField.getRowCount();row++){
                    for (int col = 0; col < gameField.getColumnCount();col++){
                        if (gameField.isCellSelected(row,col)){
                            gameField.setValueAt(selectedValue,row, col);
                        }
                    }
                }
            }
            sudoku.repaint();
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

    class BackEvent implements ActionListener{
        public void actionPerformed(ActionEvent e){
            numberTab.clearSelection();
            gameField.clearSelection();
            selectedValue = 0;
            setContentPane(mainPanel);
        }
    }

    private void generateSudoku(JTable table, int lvl){
        int columns = table.getColumnCount();
        int rows = table.getRowCount();
        int[] aRow = {1,2,3,4,5,6,7,8,9};
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
        sudoku.cleanCells(lvl);

        for (int row = 0;row < sudoku.rowLength();row++){
            for (int col = 0;col < sudoku.columnLength();col++){
                table.setValueAt(sudoku.getValue(row,col), row,col);
            }
        }
        table.repaint();
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
}