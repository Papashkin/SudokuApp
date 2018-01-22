package SudokuApp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame {

    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();
    private JButton start = new JButton("Start");
    private JButton load = new JButton("Load");
    private JButton back = new JButton("Back");
    private JTable gameField = new JTable(9,9){
//        @Override
//        public boolean isCellEditable(int rowIndex, int columnIndex) {
//            return false;
//        }
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
        setSize(300,440);
        setLocation(200,100);
        setResizable(false);

        setBtnParameters(start, bigFont);
        start.addActionListener(new StartEvent());

        setBtnParameters(load,bigFont);
        load.addActionListener(new LoadEvent());

        setBtnParameters(back,bigFont);
        back.addActionListener(new BackEvent());

        setParameters(gameField, numberFont);
        for (int i = 0; i < gameField.getColumnCount();i++){
            TableColumn column = gameField.getColumnModel().getColumn(i);
            column.setPreferredWidth(30);
            for (int j = 0; j < gameField.getRowCount();j++){
//                column.setCellRenderer(new JustCellRenderer(i,j));
            }
        }
        setTheField(0);
        gameField.setFillsViewportHeight(true);

        setParameters(number_row, numberFont);
        number_row.enableInputMethods(true);
        for (int i = 0; i < number_row.getColumnCount();i++){
            TableColumn a = number_row.getColumnModel().getColumn(i);
            a.setPreferredWidth(30);
            number_row.isCellEditable(0, i);
            number_row.setValueAt(i+1,0,i);
        }
        for (int j = 0; j < number_row.getColumnCount();j++){
            if (number_row.isCellSelected(0,j)){
                selected = (int)number_row.getValueAt(0,j);
            }
        }

        panel1.setLayout(new FlowLayout(FlowLayout.CENTER,100,110));
        panel1.add(start);
        panel1.add(load);
        panel1.setBackground(Color.white);

        panel2.setLayout(new FlowLayout(FlowLayout.CENTER,0,15));
        panel2.add(gameField);
        panel2.add(number_title);
        panel2.add(number_row);
        panel2.add(back);
        panel2.setBackground(Color.white);

        setContentPane(panel2);
        setContentPane(panel1);
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
                setTheField(lvl);
                panel1.setVisible(false);
                panel2.setVisible(true);
                panel2.repaint();
                setContentPane(panel2);
            }
        }
    }

    class LoadEvent implements ActionListener{
        public void actionPerformed(ActionEvent e){
        }
    }

    class BackEvent implements ActionListener{
        public void actionPerformed(ActionEvent e){
            panel1.setVisible(true);
            panel2.setVisible(false);
            setContentPane(panel1);
            number_row.clearSelection();
            repaint();
        }
    }

    private void setTheField(int lvl){
        int columns = gameField.getColumnCount();
        int rows = gameField.getRowCount();
        int[] aRow = {1,2,3,4,5,6,7,8,9};
        for (int j = 0; j < columns; j++){
            gameField.setValueAt(aRow[j],0,j);
        }
        for (int i = 1; i < rows; i++){
            if (i == 3 || i == 6){
                aRow = shiftLeft(aRow, 1);
            }
            aRow = shiftLeft(aRow, 3);
            for (int j = 0; j < columns; j++){
                gameField.setValueAt(aRow[j],i,j);
            }
        }
    }

    public int[] shiftLeft (int[] vector, int shiftCount){
        int index = shiftCount - 1;
        int[] vectorEnd = new int[vector.length];
        for (int j = 0 ;j < shiftCount;j++){
            vectorEnd[j+(vector.length)-shiftCount] = vector[j];
        }
        for (int j = 0;j < (vector.length)-shiftCount;j++){
            vectorEnd[j] = vector[j+shiftCount];
        }
        return vectorEnd;
    }

    private static void setParameters(JTable table, Font aFont){
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setBackground(Color.white);
        table.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.black));
        table.setRowHeight(30);
        table.setColumnSelectionAllowed(true);
        table.setRowSelectionAllowed(true);
        table.setFont(aFont);
        DefaultTableCellRenderer cellRenderer = (DefaultTableCellRenderer) table.getDefaultRenderer(String.class);
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private static void setBtnParameters(JButton button, Font aFont){
        button.setSize(100,50);
        button.setBackground(Color.white);
        button.setFont(aFont);
        button.setBorderPainted(false);
    }

    public class JustCellRenderer extends JLabel implements TableCellRenderer{
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            if (column == 2 || column == 5){
                if (row == 2 || row == 5){
                    setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.black));
                } else {
                    setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.black));
                }
            }
            else {
                if (row == 2 || row == 5){
                    setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black));
                } else {
                    setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.black));
                }
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            return this;
        }

        public JustCellRenderer(int columnIndex, int rowIndex){
        }
    }
}
