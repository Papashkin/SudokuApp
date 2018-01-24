package SudokuApp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SudokuCellRenderer extends DefaultTableCellRenderer {
    Font font = new Font("Arial", Font.BOLD, 18);

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
//        setFocusable(hasFocus);
        setValue(value);
        setFont(font);
        if (isSelected){
            hasFocus = true;
            setBackground(Color.gray);
        }else {
            setBackground(Color.white);
            hasFocus = false;
        }
        setFocusable(hasFocus);

        return this;
    }

    public SudokuCellRenderer(int column, int row){
        repaint();
    }
}
