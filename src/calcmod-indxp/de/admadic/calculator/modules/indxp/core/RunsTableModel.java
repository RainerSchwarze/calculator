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
public class RunsTableModel extends AbstractTableModel 
implements DataEventListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Run> runs;
	ArrayList<FactorInteraction> factorInteractions;

	DataEventDispatcher dataEventDispatcher;
	DataEventServer dataEventServer;

	boolean locked;

	/**
	 * @param runs
	 * @param factorInteractions
	 */
	public void setData(
			ArrayList<Run> runs, 
			ArrayList<FactorInteraction> factorInteractions) {
		this.runs = runs;
		this.factorInteractions = factorInteractions;
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
	 * @return	Returns the number of rows. (number of runs)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if (runs!=null) return runs.size();
		return 0;
	}
	
	/**
	 * @return	Returns the number of columns (run id + no. of 
	 * FactorInteractions.)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		if (factorInteractions!=null) { 
			// run id + fi's
			return 1 + factorInteractions.size();
		}
		return 0;
	}
	
	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @return	Returns the value at the given location.
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (runs==null || factorInteractions==null) return null;
		if (columnIndex==0) {
			return runs.get(rowIndex).getId();
		} else {
			FactorInteraction fi = factorInteractions.get(columnIndex-1);
			Integer level = runs.get(rowIndex).getFiLevel(fi); 
			return level;
		}
	}

	/**
	 * @param aValue
	 * @param rowIndex
	 * @param columnIndex
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (runs==null || factorInteractions==null) return;
		if (columnIndex==0) {
			return;
		} else {
			FactorInteraction fi = factorInteractions.get(columnIndex-1);
			Factor f = fi.getFactors().firstElement();
			Integer level = (Integer)aValue;
			Run r = runs.get(rowIndex);
			r.setLevel(f, level);
			r.updateFactorInteractionLevels();
			fireTableRowsUpdated(rowIndex, rowIndex);
			if (dataEventDispatcher!=null) {
				dataEventDispatcher.notifyEvent(this, DataEvent.DATA_IS_DIRTY);
			}
		}
	}


	/**
	 * @param columnIndex
	 * @return	Returns the data class of the given column.
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex<1) return String.class;
		return Integer.class;
	}
	
	/**
	 * @param columnIndex
	 * @return	Returns the name for the given column.
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int columnIndex) {
		if (factorInteractions==null) return null;
		if (columnIndex<0 || columnIndex>=(factorInteractions.size()+1)) 
			return null;
		if (columnIndex==0) {
			return "Run";
		}
		FactorInteraction fi = factorInteractions.get(columnIndex-1);
		return fi.getDisplay();
//		String name = "";
//		for (Factor f : fi.getFactors()) {
//			name += f.getName();
//		}
//		return name;
	}
	
	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @return	Returns always false (no cell is editable)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (runs==null || factorInteractions==null) return false;
		if (isLocked()) return false;
		if (columnIndex==0) {
			return false;
		} else {
			FactorInteraction fi = factorInteractions.get(columnIndex-1);
			int fcount = fi.getFactors().size();
			if (fcount==1) return true;
		}
		return false;
//		return super.isCellEditable(arg0, arg1);
	}

	/**
	 * @param e
	 * @see de.admadic.calculator.modules.indxp.core.DataEventListener#dataEventSignalled(de.admadic.calculator.modules.indxp.core.DataEvent)
	 */
	public void dataEventSignalled(DataEvent e) {
		if (e.getSource()==this) return;
		if ((e.getMask() & DataEvent.RUNS_DATA_CHANGED)!=0) {
			fireTableDataChanged();
		}
		if ((e.getMask() & DataEvent.RUNS_STRUCT_CHANGED)!=0) {
			fireTableStructureChanged();
		}
		if ((e.getMask() & DataEvent.FACTORINTERACTIONS_DATA_CHANGED)!=0) {
			fireTableStructureChanged();
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
}
