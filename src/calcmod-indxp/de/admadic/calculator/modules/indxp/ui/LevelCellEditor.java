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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * @author Rainer Schwarze
 *
 */
public class LevelCellEditor extends AbstractCellEditor
implements TableCellEditor, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Integer currentValue;
	JButton button;
	JLabel label;

	protected static final String EDIT = "edit";
	
	/**
	 * 
	 */
	public LevelCellEditor() {
//		Set up the editor (from the table's point of view),
//		which is a button.
//		This button brings up the color chooser dialog,
//		which is the editor from the user's point of view.
		button = new JButton();
		button.setActionCommand(EDIT);
		button.addActionListener(this);
		button.setBorderPainted(false);
		label = new JLabel();
	}
	
	/**
	 * Handles events from the editor button and from
	 * the dialog's OK button.
	 * @param e 
	 */
	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
//			The user has clicked the cell, so
//			bring up the dialog.
			// ?
//			button.setBackground(currentColor);
			currentValue = Integer.valueOf(-currentValue.intValue());
//			Make the renderer reappear.
			fireEditingStopped();
		}
	}
	
//	Implement the one CellEditor method that AbstractCellEditor doesn't.
	/**
	 * @return	Returns the value of the cell editor.
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return currentValue;
	}
	
//	Implement the one method defined by TableCellEditor.
	/**
	 * @param table
	 * @param value
	 * @param isSelected
	 * @param row
	 * @param column
	 * @return	Returns the button
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(
			JTable table,
			Object value,
			boolean isSelected,
			int row,
			int column) {
		currentValue = (Integer)value;
		return button;
		// return label;
	}
}
