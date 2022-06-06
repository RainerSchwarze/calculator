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
public class FactorsTableModel extends AbstractTableModel 
implements DataEventListener {

	final static boolean DBG = false;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Factor> data;
	String [] columns = {
			"Name", "Entity", "Unit", "Low", "High"
	};
	DataEventDispatcher dataEventDispatcher;
	DataEventServer dataEventServer;

	boolean locked;

	/**
	 * @param data
	 */
	public void setData(ArrayList<Factor> data) {
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
	 * @return	Returns the number of rows.
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if (data!=null) {
			return data.size();
		}
		return 0;
	}

	/**
	 * @return	Returns the number of fields for a Factor.
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 5;	// factor fields
	}

	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @return	Returns the value...
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (data!=null) {
			if (rowIndex<data.size()) {
				Factor f = data.get(rowIndex);
				switch (columnIndex) {
				case 0: return f.getName();
				case 1: return f.getEntity();
				case 2: return f.getUnit();
				case 3: return f.getValueLow();
				case 4: return f.getValueHigh();
				}
			}
		}
		return null;
	}

	/**
	 * @param columnIndex
	 * @return	Returns the data class.
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	/**
	 * @param column
	 * @return	Returns the name of the column
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @return	Returns true - always.
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (isLocked()) return false;
		return true;
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
		if (data!=null) {
			Factor f;
			f = data.get(rowIndex);
			switch (columnIndex) {
			case 0: f.setName((String)aValue); break;
			case 1: f.setEntity((String)aValue); break;
			case 2: f.setUnit((String)aValue); break;
			case 3: f.setValueLow((String)aValue); break;
			case 4: f.setValueHigh((String)aValue); break;
			default:
				// error!
				System.err.println("setValueAt error!");
			}
			if (dataEventDispatcher!=null) {
				dataEventDispatcher.notifyEvent(
						this, DataEvent.FACTORS_DATA_CHANGED);
			}
		}
	}


	/**
	 * @param e
	 * @see de.admadic.calculator.modules.indxp.core.DataEventListener#dataEventSignalled(de.admadic.calculator.modules.indxp.core.DataEvent)
	 */
	public void dataEventSignalled(DataEvent e) {
		if (e.getSource()==this) return;
		if ((e.getMask() & DataEvent.FACTORS_DATA_CHANGED)!=0) {
			fireTableDataChanged();
			if (DBG) System.out.println("FactorsTableModel: fireTableDataChanged.");
		}
		if ((e.getMask() & DataEvent.FACTORS_STRUCT_CHANGED)!=0) {
			fireTableStructureChanged();
			if (DBG) System.out.println("FactorsTableModel: fireTableStructureChanged.");
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
	}
}