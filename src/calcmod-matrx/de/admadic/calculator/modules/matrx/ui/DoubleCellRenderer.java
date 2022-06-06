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
package de.admadic.calculator.modules.matrx.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * @author Rainer Schwarze
 *
 */
public class DoubleCellRenderer extends DefaultTableCellRenderer
                           implements TableCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    FloatingPointFormatter fpf;

    /**
     * 
     */
    public DoubleCellRenderer() {
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.RIGHT);
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
    	
    	Double dval = (Double)value;
    	String text; 
    	if (dval!=null) {
        	if (fpf!=null) {
    	    	text = fpf.format(dval);
        	} else {
            	text = String.format("%g", dval);
        	}
    	} else {
    		text = "./.";
    	}
        setText(text);

        return this;
    }

	/**
	 * @param fpf
	 */
	public void setFloatingPointFormatter(FloatingPointFormatter fpf) {
		this.fpf = fpf;
	}
}
