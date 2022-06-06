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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.modules.matrx.core.DMatrix;

/**
 * @author Rainer Schwarze
 *
 */
public class MatrixPanel extends JPanel {

	/** */
	private static final long serialVersionUID = 1L;

	DMatrix mtx;

	JLabel labelMatrix;
	JTable tableMatrix;
	MatrixTableModel tableModelMatrix;
	DoubleCellRenderer cellRenderer;
	JScrollPane scrollMatrix;
	boolean editable;
	FloatingPointFormatter fpf;

	/**
	 * @param mtx 
	 * @param label 
	 * @param fpf 
	 * 
	 */
	public MatrixPanel(DMatrix mtx, String label, FloatingPointFormatter fpf) {
		super();
		this.fpf = fpf;
		this.mtx = mtx;
		initContents(label);
	}

	/**
	 * @param label 
	 * @param fpf 
	 * 
	 */
	public MatrixPanel(String label, FloatingPointFormatter fpf) {
		this(null, label, fpf);
	}

	/**
	 * 
	 */
	public MatrixPanel() {
		this(null, null, null);
	}

	/**
	 * @param label 
	 * 
	 */
	private void initContents(String label) {
		FormLayout fl;
		fl = new FormLayout(
				"5px, d:grow, 5px",
				"5px, d, 5px, d:grow, 5px");
		this.setLayout(fl);
		CellConstraints cc = new CellConstraints();

		labelMatrix = new JLabel();
		if (label!=null) labelMatrix.setText(label);
		this.add(labelMatrix, cc.xy(2, 2));
		
		tableMatrix = new JTable();
		scrollMatrix = new JScrollPane(tableMatrix,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(scrollMatrix, cc.xy(2, 4));

		tableModelMatrix = new MatrixTableModel();
		tableMatrix.setModel(tableModelMatrix);

		cellRenderer = new DoubleCellRenderer();
		cellRenderer.setFloatingPointFormatter(fpf);
		tableMatrix.setDefaultRenderer(Double.class, cellRenderer);

		tableMatrix.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// ((DefaultCellEditor)tableMatrix.getDefaultEditor(
		// Double.class)).setClickCountToStart(0);

		this.setBorder(BorderFactory.createEtchedBorder());
		
		setMatrix(mtx);
	}


	/**
	 * @param mtx
	 */
	public void setMatrix(DMatrix mtx) {
		this.mtx = mtx;
		tableModelMatrix.setData(this.mtx);
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static class MatrixTableModel extends AbstractTableModel {
		/** */
		private static final long serialVersionUID = 1L;

		DMatrix mtx;
		boolean editable;

		/**
		 * 
		 */
		public MatrixTableModel() {
			super();
			editable = true;
		}

		/**
		 * @param mtx
		 */
		public void setData(DMatrix mtx) {
			this.mtx = mtx;
			fireTableStructureChanged();
		}

		/**
		 * @return	Returns the number of rows.
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			if (mtx==null) return 0;
			return mtx.getRowCount();
		}

		/**
		 * @return	Returns the number columns
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			if (mtx==null) return 0;
			return mtx.getColumnCount();
		}

		/**
		 * @param rowIndex
		 * @param columnIndex
		 * @return	Returns the value at the given location.
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (mtx==null) return null;
			return mtx.elementAt(rowIndex, columnIndex);
		}

		/**
		 * @param columnIndex
		 * @return	Returns the class of the columns contents.
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
			return "" + (column + 1);
		}

		/**
		 * @param rowIndex
		 * @param columnIndex
		 * @return	Returns true, if the cell is editable.
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return isEditable();
		}

		/**
		 * @param aValue
		 * @param rowIndex
		 * @param columnIndex
		 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
		 */
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			mtx.setElementAt(rowIndex, columnIndex, (Double)aValue);
		}

		/**
		 * @return Returns the editable.
		 */
		public boolean isEditable() {
			return editable;
		}

		/**
		 * @param editable The editable to set.
		 */
		public void setEditable(boolean editable) {
			this.editable = editable;
			// FIXME: should we check whether editing is in progress now 
			// and cancel that?
		}
		
	}

	/**
	 * @return Returns the editable.
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * @param editable The editable to set.
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * @return	Returns the select row or -1
	 */
	public int getSelectedRow() {
		return tableMatrix.getSelectedRow();
	}

	/**
	 * @return	Returns the selected column or -1
	 */
	public int getSelectedColumn() {
		return tableMatrix.getSelectedColumn();
	}
}
