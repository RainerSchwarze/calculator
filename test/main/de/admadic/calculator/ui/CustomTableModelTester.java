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

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.ui.settings.SpinnerCellEditor;


/**
 * @author Rainer Schwarze
 *
 */
public class CustomTableModelTester extends javax.swing.JFrame {
	final static boolean DBG = true;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel panel;
	JScrollPane scrollPane;
	JTable table;
	JButton button;

	CustomTableModel tableModel;
	Object [][] tableData;

	SpinnerCellEditor sce1, sce2;

	/**
	* Auto-generated main method to display this JFrame
	 * @param args 
	*/
	public static void main(String[] args) {
		CustomTableModelTester inst = new CustomTableModelTester();
		inst.setVisible(true);
	}
	
	/**
	 * 
	 */
	public CustomTableModelTester() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			panel = new JPanel();
			FormLayout panelLayout = new FormLayout(
					"max(p;5px), max(p;5px), max(p;5px), max(p;5px)", 
					"max(p;5px), max(p;5px), max(p;5px), max(p;5px)");
			panel.setLayout(panelLayout);
			this.getContentPane().add(panel, BorderLayout.CENTER);
			scrollPane = new JScrollPane();
			panel.add(scrollPane, new CellConstraints(
			"2, 2, 1, 1, default, default"));
			tableModel = new CustomTableModel();
			table = new JTable();
			tableModel.setColumns(new String[]{"Name", "Value1", "Value2"});
			tableData = new Object[][]{
					new Object[]{"Row 0", Integer.valueOf(1), Integer.valueOf(2)},
					new Object[]{"Row 1", Integer.valueOf(3), Integer.valueOf(4)},
					new Object[]{"Row 2", Integer.valueOf(5), Integer.valueOf(6)},
			};
			tableModel.setDataCopy(tableData);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			scrollPane.setViewportView(table);
			scrollPane.setVerticalScrollBarPolicy(
					ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			table.setModel(tableModel);
			table.setPreferredScrollableViewportSize(new Dimension(300, 300));
	
			button = new JButton();
			panel.add(button, new CellConstraints(
			"2, 4, 1, 1, default, default"));
			button.setText("A Button");

			sce1 = new SpinnerCellEditor(0, 50, 1);
			sce2 = new SpinnerCellEditor(0, 50, 1);
			sce1.setMe("1");
			sce2.setMe("2");
			table.getColumnModel().getColumn(1).setCellEditor(sce1);
			table.getColumnModel().getColumn(2).setCellEditor(sce2);

			sce1.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (!sce1.isEditing()) {
						if (DBG) System.err.println(
								"StgDlg: sce1.stateChanged, but not in editing mode");
						return;
					}
					SpinnerNumberModel snm = (SpinnerNumberModel)e.getSource();
					Integer v = (Integer)snm.getValue();
					int row = table.getSelectedRow();
					if (DBG) System.err.println(
							"StgDlg: sce1.stateChanged, row=" + row + ", v=" + v + " (storing that value)");
					if (row<0) return;
					tableData[row][1] = v;
				}
			});
			sce2.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (!sce2.isEditing()) {
						if (DBG) System.err.println(
								"StgDlg: sce2.stateChanged, but not in editing mode");
						return;
					}
					SpinnerNumberModel snm = (SpinnerNumberModel)e.getSource();
					Integer v = (Integer)snm.getValue();
					int row = table.getSelectedRow();
					if (DBG) System.err.println(
							"StgDlg: sce2.stateChanged, row=" + row + ", v=" + v + " (storing that value)");
					if (row<0) return;
					tableData[row][2] = v;
				}
			});

			tableModel.addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
					if (DBG) System.err.println("tableChanged: " + e);
//			        int row = e.getFirstRow();
//			        int column = e.getColumn();
//			        CustomTableModel model = (CustomTableModel)e.getSource();
//			        String columnName = model.getColumnName(column);
//			        Object data = model.getValueAt(row, column);
			        // Do something with the data...
			        //System.err.println("data changed: " + data);
			    }
			});


			this.pack();
			this.setLocationRelativeTo(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
