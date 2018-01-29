package SudokuApp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SudokuCellRenderer extends DefaultTableCellRenderer {
    Font font = new Font("Arial", Font.BOLD,18);

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
            if (0 != (int)value){
                setValue(value);
            } else setValue(null);

            if (!isUnique(table, row, column, (int)value)){
            setForeground(Color.red);
            } else setForeground(Color.black);

//            if (isSelected) setValue(value);

        setFont(font);
        return this;
    }

    public SudokuCellRenderer(int column, int row, int value){
    }

    private boolean isUnique (JTable table, int row, int col, int value){
        boolean unique = true;
        int i = 0;
        int j = 0;
        int scndValue;
        do{
            if (i < table.getRowCount()){
                scndValue = (int) table.getValueAt(i, col);
                if (scndValue == value && i != row){
                    unique = false;
                    break;
                }
            }
            if (j < table.getColumnCount()){
                scndValue = (int) table.getValueAt(row, j);
                if (scndValue == value && j != col){
                    unique = false;
                    break;
                }
            }
            i++;
            j++;
        } while (unique && i < 9 && j < 9);
    return unique;
    }
}
