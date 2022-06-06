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
public class LevelAnalysisTableModel extends AbstractTableModel 
implements DataEventListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<FactorInteraction> factorInteractions;
	LevelAnalysis levelAnalysis;

	DataEventDispatcher dataEventDispatcher;
	DataEventServer dataEventServer;


	/**
	 * @param factorInteractions
	 * @param levelAnalysis
	 */
	public void setData(
			ArrayList<FactorInteraction> factorInteractions,
			LevelAnalysis levelAnalysis) {
		this.factorInteractions = factorInteractions;
		this.levelAnalysis = levelAnalysis;
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
	 * @return	Returns the number of rows (currently 3)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return 3;
	}
	
	/**
	 * @return	Returns the number of columns (currently row name + no. of
	 * FactorInteractions)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		if (factorInteractions!=null && factorInteractions.size()>0) { 
			// row name + fi's
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
		if (factorInteractions==null) return null;
		if (factorInteractions.size()<1) return null;
		
		if (columnIndex==0) {
			switch (rowIndex) {
			case 0: return levelAnalysis.getName() + "@+1";
			case 1: return levelAnalysis.getName() + "@-1";
			case 2: return "Delta";
			}
			return null;
		} else {
			FactorInteraction fi = factorInteractions.get(columnIndex-1);
			switch (rowIndex) {
			case 0: return levelAnalysis.getHighValue(fi);
			case 1: return levelAnalysis.getLowValue(fi);
			case 2: return levelAnalysis.getDeltaValue(fi);
			}
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
		if (columnIndex<1) return String.class;
		return Double.class;
	}
	
	/**
	 * @param columnIndex
	 * @return	Returns the name of the given column.
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int columnIndex) {
		if (factorInteractions==null) return null;
		if (columnIndex<0 || columnIndex>=(factorInteractions.size()+1)) 
			return null;
		if (columnIndex==0) {
			return "LvAn";
		}
		FactorInteraction fi = factorInteractions.get(columnIndex-1);
		String name = fi.getDisplay();
		return name;
	}
	
	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @return	returns always false. (no cell is editable)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
//		return super.isCellEditable(arg0, arg1);
	}


	/**
	 * @param e
	 * @see de.admadic.calculator.modules.indxp.core.DataEventListener#dataEventSignalled(de.admadic.calculator.modules.indxp.core.DataEvent)
	 */
	public void dataEventSignalled(DataEvent e) {
		if (e.getSource()==this) return;
		if ((e.getMask() & DataEvent.LEVELANALYSIS_DATA_CHANGED)!=0) {
			fireTableDataChanged();
		}
		if ((e.getMask() & DataEvent.LEVELANALYSIS_STRUCT_CHANGED)!=0) {
			fireTableStructureChanged();
		} else if ((e.getMask() & DataEvent.FACTORINTERACTIONS_DATA_CHANGED)!=0) {
			fireTableStructureChanged();
		} else if ((e.getMask() & DataEvent.FACTORINTERACTIONS_STRUCT_CHANGED)!=0) {
			fireTableStructureChanged();
		}
	}
}
