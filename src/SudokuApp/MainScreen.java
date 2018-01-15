package SudokuApp;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.TableView;
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
    private int selected = 0;
    private Font bigFont = new Font("Arial", Font.BOLD, 24);
    private Font numberFont = new Font("Arial", Font.BOLD, 18);

    MainScreen(){
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,430);

        start.setSize(100,50);
        start.setBackground(Color.white);
        start.setFont(bigFont);
        start.addActionListener(new StartEvent());
        start.setBorderPainted(false);

        load.setSize(100,50);
        load.setFont(bigFont);
        load.setBackground(Color.white);
        load.addActionListener(new LoadEvent());
        load.setBorderPainted(false);

        back.setSize(100,50);
        back.setBackground(Color.white);
        back.setBorderPainted(false);
        back.addActionListener(new BackEvent());

        gameField.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < gameField.getColumnCount();i++){
            TableColumn column = gameField.getColumnModel().getColumn(i);
            column.setPreferredWidth(28);
            for (int j = 0; j < gameField.getRowCount();j++){
                gameField.setRowHeight(j, 28);
                gameField.setFont(numberFont);
            }
        }
        gameField.setBackground(Color.white);
        Border compound, bevel1, bevel2;
        bevel1 = BorderFactory.createRaisedBevelBorder();
        bevel2 = BorderFactory.createLoweredBevelBorder();
        compound = BorderFactory.createCompoundBorder(bevel1, bevel2);
        gameField.setBorder(compound);
        gameField.setColumnSelectionAllowed(true);
        gameField.setRowSelectionAllowed(true);

        number_row.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        number_row.setBackground(Color.white);
        for (int i = 0; i < number_row.getColumnCount();i++){
            number_row.isCellEditable(0, i);
            number_row.setValueAt(i+1,0,i);
            TableColumn a = number_row.getColumnModel().getColumn(i);
            a.setPreferredWidth(28);
        }
        number_row.setRowHeight(28);
        number_row.setFont(numberFont);
        number_row.setColumnSelectionAllowed(true);
        number_row.setRowSelectionAllowed(true);
        number_row.enableInputMethods(true);

        for (int j = 0; j < number_row.getColumnCount();j++){
            if (number_row.isCellSelected(0,j)){
                selected = (int)number_row.getValueAt(0,j);
            }
        }

        panel1.setLayout(new FlowLayout(FlowLayout.CENTER,100,100));
        panel1.add(start);
        panel1.add(load);
        panel1.setBackground(Color.white);

        panel2.setLayout(new FlowLayout(FlowLayout.CENTER,0,10));
        panel2.add(gameField);
        panel2.add(number_title);
        panel2.add(number_row);
        panel2.add(back);
        panel2.setBackground(Color.white);

        setContentPane(panel2);
        setContentPane(panel1);
    }

    class StartEvent implements ActionListener{
        public void actionPerformed(ActionEvent e){
            panel1.setVisible(false);
            panel2.setVisible(true);
            setContentPane(panel2);
            repaint();
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
            repaint();
        }
    }

    class SetNumber implements ActionListener{
        public void actionPerformed(ActionEvent e){
            repaint();
        }
    }

    public void fillTheTable(int levelIndex){

    }
}
