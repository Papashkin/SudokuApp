package SudokuApp;

import javax.swing.*;
import javax.swing.border.Border;
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
    private JTable gameField = new JTable(9,9);

    MainScreen(){
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,400);

        start.setSize(100,50);
        start.setBackground(Color.white);
        start.addActionListener(new StartEvent());
        start.setBorderPainted(false);

        load.setSize(100,50);
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
            column.setPreferredWidth(30);
            for (int j = 0; j < gameField.getRowCount();j++){
                gameField.setRowHeight(j, 30);
            }
        }
//        gameField.setSize(270,360);
        gameField.setBackground(Color.white);

        Border compound, bevel1, bevel2;
        bevel1 = BorderFactory.createRaisedBevelBorder();
        bevel2 = BorderFactory.createLoweredBevelBorder();
        compound = BorderFactory.createCompoundBorder(bevel1, bevel2);
        gameField.setBorder(compound);
//        gameField.addAncestorListener();

        panel1.setLayout(new FlowLayout(FlowLayout.CENTER,100,50));
        panel1.add(start);
        panel1.add(load);
        panel1.setBackground(Color.white);

//        panel2.setLayout(new GridLayout(3,3));
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER,0,20));
        panel2.add(gameField);
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
}
