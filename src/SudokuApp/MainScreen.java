package SudokuApp;

import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame {

    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();
    private JButton start = new JButton("Start");
    private JButton load = new JButton("Load");
    private JTextField[] gameField = new JTextField[81];
    private JButton back = new JButton("back");

    MainScreen(){
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,300);

        panel1.setVisible(true);
        panel1.setLayout(new GridLayout(2,1));
        panel1.add(start);
        panel1.add(load);
        panel1.setVisible(true);

        panel1.setLayout(new GridLayout(10,9));
//        for (int i = 1; i < gameField.length; i++){
//            panel2.add(gameField[i]);
//        }
        panel2.add(back);
        panel2.setVisible(false);

        setContentPane(panel1);
        setContentPane(panel2);
    }

    class StartEvent implements ActionListener{
        public void actionPerformed(ActionEvent e){
            panel1.setVisible(false);
            panel2.setVisible(true);
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
        }
    }
}
