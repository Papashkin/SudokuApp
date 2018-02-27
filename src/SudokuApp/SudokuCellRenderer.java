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
                setBorder(BorderFactory.createMatteBorder(0,0,2,2, Color.black));
            } else {
                setBorder(BorderFactory.createMatteBorder(0,0,0,2, Color.black));
            }
        }
        else {
            if (row == 2 || row == 5){
                setBorder(BorderFactory.createMatteBorder(0,0,2,0, Color.black));
            } else {
                setBorder(BorderFactory.createMatteBorder(0,0,0,0, Color.black));
            }
        }
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(font);
        if (0 != (int)value){
            setValue(value);
        } else setValue(null);

        if (isSelected) setBackground(Color.lightGray);
        else setBackground(Color.white);

        if (!hasFocus && (0 == (int) value)) setBackground(Color.white);

        if (!isUnique(table, row, column, (int)value)){
            setForeground(Color.red);
        } else {
            setForeground(Color.black);
        }
        return this;
    }

    public SudokuCellRenderer(){
    }

    private boolean isUnique (JTable table, int row, int col, int value){
        boolean unique = true;
        int[] area;
        int aVal;
        int i = 0; int j = 0;
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

        area = getAreaIndex(row, col);          // is unique in box 3x3 area
        for (int r = 0;r < area.length;r++) {
            area[r] = area[r] * 3;
        }
        for (int r = 0;r < 3;r++){
            for (int c = 0;c < 3;c++){
                aVal = (int)table.getValueAt(area[0]+r,area[1]+c);
                if (aVal == value && ((area[0]+r) != row) && ((area[1]+c) != col)) {
                    unique = false;
                    continue;
                }
            }
        }
    return unique;
    }

    private int[] getAreaIndex (int row, int col) {
        int[] area = new int[2];
        int[] areaIndex = {0,0,0,1,1,1,2,2,2};
        area[0] = areaIndex[row];
        area[1] = areaIndex[col];
        return  area;
    }
}
