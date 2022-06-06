/**
 *
 * #license-begin#
 * MIT License
 *
 * Copyright (c) 2005 - 2022 admaDIC GbR - http://www.admadic.de/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * #license-end#
 *
 * $Id$ 
 */
package de.admadic.calculator.modules.indxp.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * @author Rainer Schwarze
 *
 */
public class LevelCellRenderer extends DefaultTableCellRenderer
                           implements TableCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Border borderUnselected = null;
    Border borderSelected = null;
    boolean isBordered = true;
    boolean isReadOnly = true;

    Color colorLevelHigh;
    Color colorLevelLow;
    Color colorLevelHighRO;
    Color colorLevelLowRO;
    Color colorLevelNone;

    /**
     * @param isBordered
     * @param isReadOnly 
     */
    public LevelCellRenderer(boolean isBordered, boolean isReadOnly) {
        this.isBordered = isBordered;
        this.isReadOnly = isReadOnly;
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.RIGHT);
        colorLevelHigh = Color.decode("#00FF00"); 
        colorLevelHighRO = Color.decode("#80FF80");
        colorLevelLow = Color.decode("#FF0000"); 
        colorLevelLowRO = Color.decode("#FF8080");
//        colorLevelHigh = Color.decode("#80FF80"); 
//        colorLevelHighRO = Color.decode("#80C080");
//        colorLevelLow = Color.decode("#FFB080"); 
//        colorLevelLowRO = Color.decode("#C09880");
        colorLevelNone = Color.GRAY;
    }

    /**
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return	Returns the component which renders the cell.
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
	public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
    	Component comp = super.getTableCellRendererComponent(
    			table, value, isSelected, hasFocus, row, column);
    	if (comp==null) { /* no warn */ }

    	Integer level = (Integer)value;
    	String text = String.format("%+d", level);
        Color displayColor;
        boolean editable = table.isCellEditable(row, column);
        switch (level.intValue()) {
        case -1: displayColor = editable ? colorLevelLow : colorLevelLowRO; break;
        case +1: displayColor = editable ? colorLevelHigh : colorLevelHighRO; break;
        default: displayColor = colorLevelNone; break;
        }
        
        // setBackground(displayColor);
        setText(text);
        if (isBordered) {
            if (borderSelected == null) {
                borderSelected = BorderFactory.createMatteBorder(
                		2, 5, 2, 5,
                        table.getSelectionBackground());
            }
            if (borderUnselected == null) {
                borderUnselected = BorderFactory.createMatteBorder(
                		2, 5, 2, 5,
                        table.getBackground());
            }

//            Border border;
//            if (isSelected) {
//                border = borderSelected;
//            } else {
//                border = borderUnselected;
//            }
//        	if (hasFocus) {
//        	    border = UIManager.getBorder("Table.focusCellHighlightBorder");
////        	    if (!isSelected && table.isCellEditable(row, column)) {
////	                Color col;
////	                col = UIManager.getColor("Table.focusCellForeground");
////	                if (col != null) {
////	                    super.setForeground(col);
////	                }
////	                col = UIManager.getColor("Table.focusCellBackground");
////	                if (col != null) {
////	                    super.setBackground(col);
////	                }
////        	    }
//        	} else {
//        	    // nothing
//        	}
            Border border = getBorder();
            Border border2 = BorderFactory.createMatteBorder(
                		0, 0, 0, 5, displayColor);
            border = BorderFactory.createCompoundBorder(border, border2);
        	setBorder(border);
        }
        
//        setToolTipText("RGB value: " + displayColor.getRed() + ", "
//                                     + displayColor.getGreen() + ", "
//                                     + displayColor.getBlue());
        return this;
    }
}
