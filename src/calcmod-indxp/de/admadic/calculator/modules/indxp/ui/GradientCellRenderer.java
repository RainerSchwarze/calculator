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
import java.awt.Insets;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import de.admadic.calculator.modules.indxp.core.Pair;
import de.admadic.ui.util.ColorGradient;

/**
 * @author Rainer Schwarze
 *
 */
public class GradientCellRenderer extends DefaultTableCellRenderer
                           implements TableCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    boolean on = false;
    Insets borderInsets;
    int rangeId;
    /** horizontal range of cells */
    public final static int RANGE_HORZ = 0;
    /** vertical range of cells */
    public final static int RANGE_VERT = 1;

    /** box around the cell */
    final public static Insets BORDER_BOX = new Insets(1, 1, 1, 1); 
    /** bar below the cell */
    final public static Insets BORDER_BOTTOM = new Insets(0, 0, 2, 0); 
    /** bar to right of the cell */
    final public static Insets BORDER_RIGHT = new Insets(0, 0, 0, 2); 
    /** bar top of the cell */
    final public static Insets BORDER_TOP = new Insets(2, 0, 0, 0); 
    /** bar to left of the cell */
    final public static Insets BORDER_LEFT = new Insets(0, 2, 0, 0); 

    ColorGradient colorGradient;
    Hashtable<Integer,Pair<Double,Double>> cellRanges;

    FloatingPointFormatter fpf;

    /**
     * @param cg 
     * @param borderInsets 
     * @param rangeId 
     */
    public GradientCellRenderer(
    		ColorGradient cg, Insets borderInsets, int rangeId) {
        this.colorGradient = cg;
        this.borderInsets = borderInsets;
        this.rangeId = rangeId;
        if (this.borderInsets==null) {
        	this.borderInsets = BORDER_BOX;
        }
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.RIGHT);
        cellRanges = new Hashtable<Integer,Pair<Double,Double>>();
    }

    /**
     * @param cellId 
     * @param min
     * @param max
     */
    public void addCellRange(int cellId, double min, double max) {
    	addCellRange(Integer.valueOf(cellId), Double.valueOf(min), Double.valueOf(max));
    }

    /**
     * @param cellId 
     * @param min
     * @param max
     */
    public void addCellRange(Integer cellId, Double min, Double max) {
    	cellRanges.put(cellId, new Pair<Double,Double>(min, max));
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
            	text = String.format("%f", dval);
        	}
    	} else {
    		text = "./.";
    	}
        setText(text);

        if (dval==null || !isOn()) return this;
        
        Color color;
        int cId;
        if (rangeId==RANGE_HORZ) {
        	cId = row;
        } else {
        	cId = column;
        }
        if (!cellRanges.containsKey(Integer.valueOf(cId))) return this;

        double dnorm = normalizeForCellId(dval.doubleValue(), cId);
        color = colorGradient.calculateColorAtPos(dnorm);
        // setBackground(displayColor);
        Border border = getBorder();
        Border border2 = BorderFactory.createMatteBorder(
            		borderInsets.top,
            		borderInsets.left,
            		borderInsets.bottom,
            		borderInsets.right,
            		color);
        border = BorderFactory.createCompoundBorder(border, border2);
    	setBorder(border);
        
//        setToolTipText("RGB value: " + displayColor.getRed() + ", "
//                                     + displayColor.getGreen() + ", "
//                                     + displayColor.getBlue());
        return this;
    }

	private double normalizeForCellId(double v, int cellId) {
		Integer ritg = Integer.valueOf(cellId);
		if (cellRanges.containsKey(ritg)) {
			Pair<Double,Double> pd = cellRanges.get(ritg);
			if (pd.getFirst()==null || pd.getSecond()==null) return 0.5;
			double d1 = pd.getFirst().doubleValue();
			double d2 = pd.getSecond().doubleValue();
			if (d1==d2) {
				return 0.5;
			}
			return (v-d1)/(d2-d1);
		}
		return 0.5;
	}

	/**
	 * @return Returns the on.
	 */
	public boolean isOn() {
		return on;
	}

	/**
	 * @param on The on to set.
	 */
	public void setOn(boolean on) {
		this.on = on;
	}

	/**
	 * 
	 */
	public void clearRanges() {
		cellRanges.clear();
	}

	/**
	 * @param fpf
	 */
	public void setFloatingPointFormatter(FloatingPointFormatter fpf) {
		this.fpf = fpf;
	}
}
