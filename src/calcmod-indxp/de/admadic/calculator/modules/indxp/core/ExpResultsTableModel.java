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
package de.admadic.calculator.modules.indxp.core;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * @author Rainer Schwarze
 *
 */
public class ExpResultsTableModel extends AbstractTableModel 
implements DataEventListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<ExpResults> data;
	boolean locked;

	DataEventDispatcher dataEventDispatcher;
	DataEventServer dataEventServer;

	/**
	 * @param data
	 */
	public void setData(ArrayList<ExpResults> data) {
		this.data = data;
	}


	/**
	 * Registeres the data event dispatcher. Pass null to remove it.
	 * @param v
	 */
	public void setDataEventDispatcher(DataEventDispatcher v) {
		dataEventDispatcher = v;
	}

	
	/**
	 * Registeres the data event server. Pass null to remove it.
	 * The data event server provides the interface to add and remove
	 * data listeners.
	 * @param dataEventServer The dataEventServer to set.
	 */
	public void setDataEventServer(DataEventServer dataEventServer) {
		this.dataEventServer = dataEventServer;
	}


	/**
	 * @return	Returns the number of rows of this table.
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if (data!=null) {
			return data.size();
		}
		return 0;
	}

	/**
	 * @return	Returns the number of columns.
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		if (data==null) return 0;
		if (data.size()<1) return 1;
		ExpResults er = data.get(0);
		return er.getReplicateCount() + 2;	// results + yavg + sigma
	}

	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @return	Returns the value at the given row and column.
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (data==null) return null;

		ExpResults er = data.get(rowIndex);
		if (columnIndex<er.getReplicateCount()) {
			return er.getResult(columnIndex);
		} else if (columnIndex==er.getReplicateCount()) {
			return er.getYAvg();
		} else if (columnIndex==(er.getReplicateCount()+1)) {
			return er.getSigma();
		} else {
			// error?!
			return null;
		}
	}

	/**
	 * @param columnIndex
	 * @return	Returns the data class of the given column.
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Double.class;
	}

	/**
	 * @param column
	 * @return	Returns the name of the given column.
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		if (data==null) return null;
		if (data.size()<1) return "Replicates";

		ExpResults er = data.get(0);
		if (column<er.getReplicateCount()) {
			return "y" + (column+1);
		} else if (column==er.getReplicateCount()) {
			return "y#";
		} else if (column==(er.getReplicateCount()+1)) {
			return "s*";
		}

		return null;
	}

	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @return	Returns true for all sub result columns, false for the 
	 * statistics.
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (isLocked()) return false;
		ExpResults er = data.get(rowIndex);
		if (columnIndex<er.getReplicateCount()) return true;
		return false;
		// super.isCellEditable(rowIndex, columnIndex);
	}

	/**
	 * @param aValue
	 * @param rowIndex
	 * @param columnIndex
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		ExpResults er = data.get(rowIndex);
		if (columnIndex<er.getReplicateCount()) {
			er.setResult(columnIndex, (Double)aValue);
			if (dataEventDispatcher!=null) {
				dataEventDispatcher.notifyEvent(this, DataEvent.DATA_IS_DIRTY);
			}
		} else {
			// cannot happen!?
		}
	}

	/**
	 * @return Returns the locked.
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked The locked to set.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
		fireTableDataChanged();
	}


	/**
	 * @param e
	 * @see de.admadic.calculator.modules.indxp.core.DataEventListener#dataEventSignalled(de.admadic.calculator.modules.indxp.core.DataEvent)
	 */
	public void dataEventSignalled(DataEvent e) {
		if (e.getSource()==this) return;
		if ((e.getMask() & DataEvent.EXPRESULTS_STRUCT_CHANGED)!=0) {
			fireTableStructureChanged();
		}
		if ((e.getMask() & DataEvent.EXPRESULTS_DATA_CHANGED)!=0) {
			fireTableDataChanged();
		}
	}
}
