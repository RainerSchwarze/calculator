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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.Version;
import de.admadic.calculator.appmod.ModuleSpec;
import de.admadic.calculator.appmod.ModuleUtil;
import de.admadic.calculator.ui.CfgCalc;
import de.admadic.cfg.Cfg;
import de.admadic.ui.util.ColumnAccessor;
import de.admadic.ui.util.ListRefTableModel;
import de.admadic.util.PathManager;
import de.admadic.util.VersionRecord;

/**
 * @author Rainer Schwarze
 *
 */
public class SettingsPanelModules extends AbstractSettingsPanel 
implements ActionListener {

	static class ModuleSpecColumnAccessor implements ColumnAccessor {
		/**
		 * @param rowItem
		 * @param value
		 * @param columnIndex
		 * @see de.admadic.ui.util.ColumnAccessor#setValueAt(java.lang.Object, java.lang.Object, int)
		 */
		public void setValueAt(Object rowItem, Object value, int columnIndex) {
			ModuleSpec ms = (ModuleSpec)rowItem;
			switch (columnIndex) {
			case 0: break; // nothing
			case 1: ms.setEnabled(((Boolean)value).booleanValue()); break;
			case 2: break; // nothing
			default:
				// error!
			}
		}

		/**
		 * @param rowItem
		 * @param columnIndex
		 * @return	Returns the value at the given row.
		 * @see de.admadic.ui.util.ColumnAccessor#getValueAt(java.lang.Object, int)
		 */
		public Object getValueAt(Object rowItem, int columnIndex) {
			ModuleSpec ms = (ModuleSpec)rowItem;
			switch (columnIndex) {
			case 0: return ms.getName();
			case 1: return Boolean.valueOf(ms.isEnabled());
			case 2: return ms.getClassName();
			default:
				// error!
			}
			return null;
		}

		/**
		 * @param rowItem
		 * @param columnIndex
		 * @return	Returns true, if the cell is editable.
		 * @see de.admadic.ui.util.ColumnAccessor#isCellEditable(java.lang.Object, int)
		 */
		public boolean isCellEditable(Object rowItem, int columnIndex) {
			if (columnIndex==1) return true;
			return false;
		}

		/**
		 * @param columnIndex
		 * @return	Returns the class for the column.
		 * @see de.admadic.ui.util.ColumnAccessor#getColumnClass(int)
		 */
		public Class getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0: return String.class;
			case 1: return Boolean.class;
			case 2: return String.class;
			}
			return null;
		}
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Cfg cfg;
	PathManager pathMan;

	boolean needRestart = false;
	
	JTable tableModules;
	ListRefTableModel tableModelModules;

	JPanel panelButtons;
	JButton buttonAddModule;
	JButton buttonRemModule;
	JButton buttonUpModule;
	JButton buttonDownModule;
	
	ArrayList<ModuleSpec> modulesTableData;

	int [] modulesColumnWidths = { 80, 40, 250 };
	String [] modulesColNames = { "Name", "Enable", "Class Name" };

	protected final String CMD_MOD_ADD = "stgs.btn.mod.add";
	protected final String CMD_MOD_REM = "stgs.btn.mod.rem";
	protected final String CMD_MOD_UP = "stgs.btn.mod.up";
	protected final String CMD_MOD_DOWN = "stgs.btn.mod.down";

	/**
	 * @param cfg 
	 * @param pathMan 
	 * 
	 */
	public SettingsPanelModules(Cfg cfg, PathManager pathMan) {
		super();
		this.cfg = cfg;
		this.pathMan = pathMan;
		this.modulesTableData = new ArrayList<ModuleSpec>();
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.AbstractSettingsPanel#initContents()
	 */
	@Override
	public void initContents() {
		// tabPanelLaFs = new JPanel();
		JPanel panel = this;
		
		FormLayout thisLayout = new FormLayout(
				"5px, d:grow, 5px, d, 5px", 
				"5px, d:grow, 5px");
		CellConstraints cc = new CellConstraints();

		panel.setLayout(thisLayout);

		tableModelModules = new ListRefTableModel();
		tableModelModules.setColumnAccessor(new ModuleSpecColumnAccessor());
		tableModelModules.setColumns(modulesColNames);
		//tm.setColumns(cn2);
		tableModelModules.setData(modulesTableData);
		tableModules = new JTable();
		tableModules.setModel(tableModelModules);
		tableModules.setPreferredScrollableViewportSize(new Dimension(250, 150));
		JScrollPane scrollPane = new JScrollPane(tableModules);
		scrollPane.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane, cc.xy(2, 2));

		tableModules.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < modulesColumnWidths.length; i++) {
			tableModules.getColumnModel().getColumn(i).setPreferredWidth(
					modulesColumnWidths[i]);
		}

		tableModules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		{
			panelButtons = new JPanel();
			panel.add(panelButtons, cc.xy(4, 2));
			FormLayout flb = new FormLayout(
					"0px, d, 0px",
					"0px, d, 5px, d, 5px, d, 5px, d, 0px");
			panelButtons.setLayout(flb);
			CellConstraints ccb = new CellConstraints();

			buttonAddModule = new JButton("Add...");
			buttonAddModule.setActionCommand(CMD_MOD_ADD);
			buttonAddModule.addActionListener(this);

			buttonRemModule = new JButton("Remove...");
			buttonRemModule.setActionCommand(CMD_MOD_REM);
			buttonRemModule.addActionListener(this);

			buttonUpModule = new JButton("Up");
			buttonUpModule.setActionCommand(CMD_MOD_UP);
			buttonUpModule.addActionListener(this);

			buttonDownModule = new JButton("Down");
			buttonDownModule.setActionCommand(CMD_MOD_DOWN);
			buttonDownModule.addActionListener(this);

			panelButtons.add(buttonAddModule, ccb.xy(2, 2)); 
			panelButtons.add(buttonRemModule, ccb.xy(2, 4)); 
			panelButtons.add(buttonUpModule, ccb.xy(2, 6)); 
			panelButtons.add(buttonDownModule, ccb.xy(2, 8)); 
		}

	}

	/**
	 * @return	Returns true, if the calculator should be restarted.
	 * @see de.admadic.calculator.ui.settings.AbstractSettingsPanel#isNeedRestart()
	 */
	@Override
	public boolean isNeedRestart() {
		return needRestart;
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.AbstractSettingsPanel#loadSettings()
	 */
	@Override
	public void loadSettings() {
		Object [] modules;
		String [] sa;
		modules = cfg.getValueArray(CfgCalc.KEY_MODULE_LIST_BASE);

		modulesTableData.clear();
		for (int i = 0; i < modules.length; i++) {
			sa = ((String)modules[i]).split(CfgCalc.ITEM_SEPARATOR_STR);
			ModuleSpec ms;
			boolean b = Boolean.parseBoolean(sa[2]);
			ms = new ModuleSpec(sa[0], sa[1], b);
			modulesTableData.add(ms);
		}
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.AbstractSettingsPanel#resetSettings()
	 */
	@Override
	public void resetSettings() {
		modulesTableData.clear();
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.AbstractSettingsPanel#storeSettings()
	 */
	@Override
	public void storeSettings() {
		Object [] modules;

		modules = new Object[modulesTableData.size()];
		for (int i = 0; i < modulesTableData.size(); i++) {
			ModuleSpec ms = modulesTableData.get(i);

			modules[i] = 
				ms.getName() + CfgCalc.ITEM_SEPARATOR_STR +
				ms.getClassName() + CfgCalc.ITEM_SEPARATOR_STR +
				Boolean.toString(ms.isEnabled());
		}
		
		cfg.putValueArray(CfgCalc.KEY_MODULE_LIST_BASE, modules);
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_MOD_ADD)) {
			doAddMod();
		} else if (cmd.equals(CMD_MOD_REM)) {
			doRemMod();
		} else if (cmd.equals(CMD_MOD_UP)) {
			doUpMod();
		} else if (cmd.equals(CMD_MOD_DOWN)) {
			doDownMod();
		}
	}

	void doAddMod() {
		File file = SettingsUtils.getModFile(this);
		if (file==null) return;

		ModuleSpec ms = ModuleUtil.extractModuleSpec(file);
		if (ms==null) {
			JOptionPane.showMessageDialog(
					this,
					"A module specification could not be found in the selected file:\n"+
					file.toString(),
					"No module found in file",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		// check version:
		if (ms.getRequiredAppVersion()!=null) {
			VersionRecord rav = ms.getRequiredAppVersion();
			VersionRecord cav = VersionRecord.valueOf(Version.version);
			if (rav!=null && cav!=null) {
				if (rav.compareTo(cav)>0) {
					// required is larger than current version!
					JOptionPane.showMessageDialog(
							this,
							"The module requires at least version " + 
								rav.getMjMnMcRvVersionString() +
								" of the calculator.\n"+
							"The calculator which is currently running has" +
								" the version " + 
								cav.getMjMnMcRvVersionString() +
								".\n" +
							"The module cannot be installed.\n"+
							"If you think this is an error, please contact"+
								" customer support.",
							"Invalid version",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				// log an error!
			}
		}
		String existMsg = "";
		if (existsModuleName(ms.getName())) {
			existMsg += "A module with that name already exists!\n";
		}
		int result = JOptionPane.showConfirmDialog(
				this,
				existMsg + 
				"Do you wish to add the module named '" + ms.getName() + "'?",
				"Confirm adding module",
				JOptionPane.YES_NO_OPTION);
		if (result!=JOptionPane.YES_OPTION)
			return;

		// file contains the source name.
		int rc = SettingsUtils.installModFile(this, pathMan, file);
		if (rc==SettingsUtils.RCINS_NOINSTALL) {
			return;
		}
		if (rc==SettingsUtils.RCINS_RESTART) {
			needRestart = true;
		}
		modulesTableData.add(ms);
		tableModelModules.fireTableRowsInserted(
				modulesTableData.size()-1, modulesTableData.size()-1);
//		if (rc==SettingsUtils.RCINS_RESTART) {
//			JOptionPane.showMessageDialog(
//					this,
//					"Installed the Module.\n"+
//					"Please restart the calculator.");
//		}
	}

	private boolean existsModuleName(String name) {
		for (ModuleSpec ms : modulesTableData) {
			if (ms.getName().equals(name)) return true;
		}
		return false;
	}

	void doRemMod() {
		int sel = tableModules.getSelectedRow();
		if (sel<0) {
			JOptionPane.showMessageDialog(
					this,
					"Please select a module first.",
					"Nothing selected",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		int result = JOptionPane.showConfirmDialog(
				this,
				"Please confirm, that the selected module shall be removed.\n"+
				"The module can be added again, by choosing 'Add...'.\n"+
				"If you only want to disable it, turn off the checkbox in the list of modules.",
				"Confirm Remove",
				JOptionPane.OK_CANCEL_OPTION);
		if (result!=JOptionPane.OK_OPTION) 
			return;

		needRestart = true;
		modulesTableData.remove(sel);
		tableModelModules.fireTableDataChanged();
	}

	void doUpMod() {
		int sel = tableModules.getSelectedRow();
		if (sel<0) return; // nothing selected to be moved
		if (sel<1) return; // already at top

		needRestart = true;
		Collections.swap(modulesTableData, sel-1, sel);
		tableModules.setRowSelectionInterval(sel-1, sel-1);
	}

	void doDownMod() {
		int sel = tableModules.getSelectedRow();
		if (sel<0) return; // nothing selected to be moved
		if (sel>=(modulesTableData.size()-1)) return; // already at bottom

		needRestart = true;
		Collections.swap(modulesTableData, sel+1, sel);
		tableModules.setRowSelectionInterval(sel+1, sel+1);
	}
}
