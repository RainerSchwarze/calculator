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
package de.admadic.calculator.ui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * @author Rainer Schwarze
 */
public class CustomTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] columns = null;
//	private Object[][] data = null;
	int lockedRow = -1;
	private ArrayList<Object[]> datax;

	// # of columns beginning with left most which are read only: 
	int readOnlyColumns;

	/**
	 * The data is copied!
	 * @param data
	 */
	public void setDataCopy(Object[][] data) {
//		this.data = data;
		if (data==null) {
			datax = null;
			return;
		}
		this.datax = new ArrayList<Object[]>();
		for (int i = 0; i < data.length; i++) {
			datax.add(data[i]);
		}
	}

	/**
	 * THe data is not copied.
	 * @param data
	 */
	public void setData(ArrayList<Object[]> data) {
		this.datax = data;
	}
	
	/**
	 * @param columns
	 */
	public void setColumns(String[] columns) {
		this.columns = columns;
	}
	
	/**
	 * @return	the number of columns
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		if (columns==null) return 0;
		return columns.length;
	}
	
	/**
	 * @return	the number of rows
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if (datax==null) 
			return 0;
		return datax.size();
	}
	
	/**
	 * @param col
	 * @return	the name of the column
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int col) {
		if (col<0 || columns==null || col>=columns.length) {
			return "<none>";
		}
		return columns[col];
	}
	
	/**
	 * @param row
	 * @param col
	 * @return	the value at the table cell
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		if (row==lockedRow && col==0) {
			Object [] oa = datax.get(row);
			String s = (String)oa[col];
//			String s = (String)data[row][col];
			s = "@ " + s;
			return s;
		}
		Object [] oa = datax.get(row);
//		return data[row][col];
		return oa[col];
	}
	
	/**
	 * @param c
	 * @return	the class for the columns data
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int c) {
		if (getRowCount()>0) {
			return getValueAt(0, c).getClass();
		} else {
			return String.class;
		}
	}
	
	/**
	 * Don't need to implement this method unless your table's
	 * editable.
	 * @param row 
	 * @param col 
	 * @return true if the cell is editable
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,
		//no matter where the cell appears onscreen.
		if (col < readOnlyColumns) {
			return false;
		} else {
			if (row==lockedRow) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * Don't need to implement this method unless your table's
	 * data can change.
	 * @param value 
	 * @param row 
	 * @param col 
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {
		Object [] oa = datax.get(row);
		oa[col] = value;
//		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

	/**
	 * @param row
	 */
	public void removeRow(int row) {
		if (row==lockedRow) return;
		if (datax==null) return;
		datax.remove(row);
		fireTableRowsDeleted(row, row);
	}

	/**
	 * @return Returns the readOnlyColumns.
	 */
	public int getReadOnlyColumns() {
		return readOnlyColumns;
	}
	/**
	 * @param readOnlyColumns The readOnlyColumns to set.
	 */
	public void setReadOnlyColumns(int readOnlyColumns) {
		this.readOnlyColumns = readOnlyColumns;
	}
	/**
	 * @return Returns the lockedRow.
	 */
	public int getLockedRow() {
		return lockedRow;
	}
	/**
	 * @param lockedRow The lockedRow to set.
	 */
	public void setLockedRow(int lockedRow) {
		this.lockedRow = lockedRow;
	}
}