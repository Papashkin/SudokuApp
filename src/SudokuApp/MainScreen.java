package SudokuApp;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame{

    private JPanel mainPanel = new JPanel();
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
    private int lvl = 10;
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
                setTheField(gameField, lvl);
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
            setContentPane(mainPanel);
            number_row.clearSelection();
            gameField.clearSelection();
        }
    }

    private void setTheField(JTable table, int lvl){
        int columns = table.getColumnCount();
        int rows = table.getRowCount();
        int[] aRow = {1,2,3,4,5,6,7,8,9};
        for (int j = 0; j < columns; j++){
            table.setValueAt(aRow[j],0,j);
        }
        for (int i = 1; i < rows; i++){
            if (i == 3 || i == 6){
                aRow = shiftLeft(aRow, 1);
            }
            aRow = shiftLeft(aRow, 3);
            for (int j = 0; j < columns; j++){
                table.setValueAt(aRow[j],i,j);
            }
        }
        dtm.fireTableDataChanged();
        table.repaint();
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
    }

    private void setBtnParameters(JButton button, Font aFont){
        button.setSize(100,50);
        button.setBackground(Color.white);
        button.setFont(aFont);
        button.setBorderPainted(false);
    }
}