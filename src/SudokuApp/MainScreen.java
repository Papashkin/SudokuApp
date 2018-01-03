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
    private JButton back = new JButton("Back");
    private JTextField gameField;

    MainScreen(){
        setTitle("Sudoku");
        setBackground(Color.cyan);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,500);
        start.setSize(100,50);
        load.setSize(100,50);
        start.setBorderPainted(false);
        start.setBackground(Color.cyan);

        gameField = new JTextField ("", 9);
        start.addActionListener(new StartEvent());
        back.addActionListener(new BackEvent());

//        panel1.setLayout(new BorderLayout());
        panel1.setLayout(new FlowLayout(FlowLayout.LEADING,110,100));
        panel1.add(start);
        panel1.add(load);

        panel2.setLayout(new GridLayout(10, 9));
        panel2.add(gameField);
        gameField.setSize(2,2);
        panel2.add(back);

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
}
