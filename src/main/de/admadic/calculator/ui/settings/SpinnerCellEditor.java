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
package de.admadic.calculator.ui.settings;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

/**
 * @author Rainer Schwarze
 *
 */
public class SpinnerCellEditor extends AbstractCellEditor 
		implements TableCellEditor, FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static boolean DBGforce = false;
	String me;

	Integer value;
	Integer fallbackValue;
	JSpinner spinner;

	boolean editing = false;

	int gtcecCol = -1;
	int gtcecRow = -1;

	/**
	 * @param min 
	 * @param max 
	 * @param step 
	 */
	public SpinnerCellEditor(int min, int max, int step) {
		super();
		if (DBGforce) System.err.println("sce: (" + me + ") <init>");
		spinner = new JSpinner();
		SpinnerModel spm = new SpinnerNumberModel(
				min, // initial
				min, // min
				max, // max
				step); // step
		spinner.setModel(spm);
		spinner.setBorder(null);
	}

	/**
	 * Dummy function. Can safely be removed after testing.
	 * @param me
	 */
	public void setMe(String me) {
		this.me = me;
	}

	/**
	 * @param l
	 */
	public void addChangeListener(ChangeListener l) {
		if (DBGforce) System.err.println("sce: (" + me + ") addChangeListener");
		spinner.getModel().addChangeListener(l);
	}

	/**
	 * @return	true, if cell editing as stopped indeed
	 * @see javax.swing.AbstractCellEditor#stopCellEditing()
	 */
	@Override
	public boolean stopCellEditing() {
		try {
			if (DBGforce) System.err.println(
					"sce: (" + me + ") stopCellEditing, "+
					"spinner.commitEdit()");
			spinner.commitEdit();
		}
		catch (ParseException pe) {
			if (DBGforce) System.err.println(
					"sce: (" + me + ") stopCellEditing, "+
					"spinner.commitEdit(): threw ParseException");
			// Edited value is invalid, spinner.getValue() will return
			// the last valid value, you could revert the spinner to show that:
			JComponent editor = spinner.getEditor();
			if (editor instanceof JSpinner.DefaultEditor) {
				((JSpinner.DefaultEditor)editor).getTextField().setValue(spinner.getValue());
			}
			// reset the value to some known value:
			spinner.setValue(fallbackValue);
			// or treat the last valid value as the current, in which
			// case you don't need to do anything.
		}
		   //return spinner.getValue();
		value = (Integer)spinner.getValue();
		if (DBGforce) System.err.println(
				"sce: (" + me + ") stopCellEditing, "+
				"value=" + value + ", " +
				"r,c was=" + gtcecRow + "," + gtcecCol);
		//fireEditingStopped();
		setEditing(false);
		return super.stopCellEditing();
	}

	/**
	 * @return	the value of the editor for the current cell
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		if (DBGforce) System.err.println(
				"sce: (" + me + ") getCellEditorValue, value=" + value + ", " +
				"r,c was=" + gtcecRow + "," + gtcecCol);
		return value;
	}


	/**
	 * @param table
	 * @param value
	 * @param isSelected
	 * @param row
	 * @param column
	 * @return	the component used as an editor for that cell
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(
			JTable table, Object value, boolean isSelected, 
			int row, int column) {
		if (DBGforce) System.err.println(
				"sce: (" + me + ") getTableCellEditorComponent, "+
				"r,c=" + row + "," + column + ", " +
				"isSel=" + isSelected + ", " +
				"got value=" + value + 
				", returning spinner with that value");
		this.gtcecCol = column;
		this.gtcecRow = row;
		this.value = (Integer)value;
		this.fallbackValue = this.value;
		// first fire itself with the current value:
		//spinner.setValue(spinner.getValue());
		spinner.setValue(value);
		if (DBGforce) System.err.println("  sce: (" + me + ") " + "(value set)");
		setEditing(true);
		return spinner;
	}

	/**
	 * @param e
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {
		if (DBGforce) System.err.println("sce: (" + me + ") focus gained: " + e);
	}

	/**
	 * @param e
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		if (DBGforce) System.err.println("sce: (" + me + ") focus lost: " + e);
	}

	/**
	 * @return Returns the editing.
	 */
	public boolean isEditing() {
		return editing;
	}

	/**
	 * @param editing The editing to set.
	 */
	public void setEditing(boolean editing) {
		this.editing = editing;
	}
}
