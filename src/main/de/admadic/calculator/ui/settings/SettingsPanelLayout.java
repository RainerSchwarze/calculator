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

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.ui.CfgCalc;
import de.admadic.calculator.ui.CustomTableModel;
import de.admadic.cfg.Cfg;

/**
 * @author Rainer Schwarze
 *
 */
public class SettingsPanelLayout extends AbstractSettingsPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static boolean DBG = false;

	Cfg cfg;

	SpinnerCellEditor editorSpinnerCell1;
	SpinnerCellEditor editorSpinnerCell2;

	JTable tableStyles;
	CustomTableModel tableModelStyles;
	JCheckBox checkBoxFixedCellSize;
	ArrayList<Object[]> cellsTableData;
	String[] cellsTableDataKeys;

	/**
	 * @param cfg 
	 */
	public SettingsPanelLayout(Cfg cfg) {
		super();
		this.cfg = cfg;
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#initContents()
	 */
	@Override
	public void initContents() {
		//int [] columnWidths = { 100, 100, 100 };
		String [] colNames = { "Style", "Width", "Height"};
		// tabPanelLayout = new JPanel();
		JPanel tabPanelLayout = this;
		FormLayout thisLayout = new FormLayout(
				"12px, d:grow, 12px", 
				"12px, d:grow, 5px, d, 12px");
		// new CellConstraints("2, 2, 1, 1, fill, fill")
		tabPanelLayout.setLayout(thisLayout);

		tableModelStyles = new CustomTableModel();
		tableModelStyles.setColumns(colNames);
		//tm.setColumns(cn2);
		// FIXME: tm.setData(td2);
		tableStyles = new JTable();
		tableStyles.setModel(tableModelStyles);
		tableStyles.setPreferredScrollableViewportSize(new Dimension(300, 150));
		JScrollPane scrollPane = new JScrollPane(tableStyles);
		scrollPane.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		tabPanelLayout.add(
				scrollPane, 
				new CellConstraints("2, 2, 1, 1, fill, fill"));
		
		//TableColumn column = null;
		editorSpinnerCell1 = new SpinnerCellEditor(0, 50, 1);
		editorSpinnerCell2 = new SpinnerCellEditor(0, 50, 1);
		editorSpinnerCell1.setMe("1");
		editorSpinnerCell2.setMe("2");
		tableStyles.getColumnModel().getColumn(1).setCellEditor(editorSpinnerCell1);
		tableStyles.getColumnModel().getColumn(2).setCellEditor(editorSpinnerCell2);

		editorSpinnerCell1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!editorSpinnerCell1.isEditing()) {
					if (DBG) System.err.println(
							"StgDlg: sce1.stateChanged, but not in editing mode");
					return;
				}
				SpinnerNumberModel snm = (SpinnerNumberModel)e.getSource();
				Integer v = (Integer)snm.getValue();
				int row = tableStyles.getSelectedRow();
				if (DBG) System.err.println(
						"StgDlg: sce1.stateChanged, row=" + row + ", v=" + v);
				if (row<0) return;
				cellsTableData.get(row)[1] = v;
			}
		});
		editorSpinnerCell2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!editorSpinnerCell2.isEditing()) {
					if (DBG) System.err.println(
							"StgDlg: sce2.stateChanged, but not in editing mode");
					return;
				}
				SpinnerNumberModel snm = (SpinnerNumberModel)e.getSource();
				Integer v = (Integer)snm.getValue();
				int row = tableStyles.getSelectedRow();
				if (DBG) System.err.println(
						"StgDlg: sce2.stateChanged, row=" + row + ", v=" + v);
				if (row<0) return;
				cellsTableData.get(row)[2] = v;
			}
		});

		tableModelStyles.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
//		        int row = e.getFirstRow();
//		        int column = e.getColumn();
//		        CustomTableModel model = (CustomTableModel)e.getSource();
//		        String columnName = model.getColumnName(column);
//		        Object data = model.getValueAt(row, column);
		        // Do something with the data...
		        //System.err.println("data changed: " + data);
		    }
		});
		tableStyles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		checkBoxFixedCellSize = new JCheckBox("Fixed cell size");
		tabPanelLayout.add(
				checkBoxFixedCellSize,
				new CellConstraints("2, 4, 1, 1, default, default"));
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#loadSettings()
	 */
	@Override
	public void loadSettings() {
		cellsTableData = new ArrayList<Object[]>();
		cellsTableDataKeys = new String[3];

		cellsTableData.add(0, new Object[]{
				new String(CfgCalc.UI_STYLE_BUTTON_STANDARD),
				new Integer(cfg.getIntValue(
					CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + 
					CfgCalc.UI_STYLE_BUTTON_STANDARD + ".width", 7)),
				new Integer(cfg.getIntValue(
					CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + 
					CfgCalc.UI_STYLE_BUTTON_STANDARD + ".height", 7)),
			});
		cellsTableData.add(1, new Object[]{
				new String(CfgCalc.UI_STYLE_BUTTON_SMALL),
				new Integer(cfg.getIntValue(
					CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + 
					CfgCalc.UI_STYLE_BUTTON_SMALL + ".width", 7)),
				new Integer(cfg.getIntValue(
					CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + 
					CfgCalc.UI_STYLE_BUTTON_SMALL + ".height", 7)),
			});
		cellsTableData.add(2, new Object[]{
				new String("mincell"),
				new Integer(cfg.getIntValue(
					CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".width", 4)),
				new Integer(cfg.getIntValue(
					CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".height", 4)),
			});

		cellsTableDataKeys[0] = 
			CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + 
			CfgCalc.UI_STYLE_BUTTON_STANDARD;
		cellsTableDataKeys[1] = 
			CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + 
			CfgCalc.UI_STYLE_BUTTON_SMALL;
		cellsTableDataKeys[2] = 
			CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE;

		tableModelStyles.setData(cellsTableData);

		checkBoxFixedCellSize.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_UI_LAYOUT_CCL_FIXEDCELLSIZE, true));
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#storeSettings()
	 */
	@Override
	public void storeSettings() {
		for (int i = 0; i < cellsTableDataKeys.length; i++) {
			cfg.putIntValue(
					cellsTableDataKeys[i] + ".width", 
					((Integer)cellsTableData.get(i)[1]).intValue());
			cfg.putIntValue(
					cellsTableDataKeys[i] + ".height", 
					((Integer)cellsTableData.get(i)[2]).intValue());
		}

		cfg.putBooleanValue(CfgCalc.KEY_UI_LAYOUT_CCL_FIXEDCELLSIZE, checkBoxFixedCellSize.isSelected());
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#resetSettings()
	 */
	@Override
	public void resetSettings() {
		cellsTableData = null;
		cellsTableDataKeys = null;
		tableModelStyles.setData(null);
	}
}
