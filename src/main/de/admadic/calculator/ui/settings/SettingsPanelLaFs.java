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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.ui.CfgCalc;
import de.admadic.calculator.ui.CustomTableModel;
import de.admadic.cfg.Cfg;
import de.admadic.ui.util.Dialog;
import de.admadic.ui.util.LaFAddDialog;
import de.admadic.util.FileUtil;
import de.admadic.util.JarLister;
import de.admadic.util.PathManager;

/**
 * @author Rainer Schwarze
 *
 */
public class SettingsPanelLaFs extends AbstractSettingsPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Cfg cfg;
	PathManager pathMan;

	JTable tableLaFs;
	CustomTableModel tableModelLaFs;
	JTable tableSkins;
	CustomTableModel tableModelSkins;

	boolean needRestart = false;

	JButton buttonAddLaF;
	JButton buttonRemLaF;
	JButton buttonAddTheme;
	JButton buttonRemTheme;
	
	ArrayList<Object[]> lafsTableData;
	ArrayList<ArrayList<Object[]>> skinsTableDataArray;

	int [] lafColumnWidths = { 100, 30 };
	int [] skinsColumnsWidths = { 100, 30 };
	String [] lafColNames = { "Look & Feel", "Enable" };

	protected final String CMD_LAF_ADD = "stgs.btn.laf.add";
	protected final String CMD_LAF_REM = "stgs.btn.laf.rem";
	protected final String CMD_THM_ADD = "stgs.btn.thm.add";
	protected final String CMD_THM_REM = "stgs.btn.thm.rem";

	/**
	 * @param cfg 
	 * @param pathMan 
	 * 
	 */
	public SettingsPanelLaFs(Cfg cfg, PathManager pathMan) {
		super();
		this.cfg = cfg;
		this.pathMan = pathMan;
	}
	
	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#initContents()
	 */
	@Override
	public void initContents() {
		// tabPanelLaFs = new JPanel();
		JPanel tabPanelLaFs = this;
		
		FormLayout thisLayout = new FormLayout(
				"5px, d:grow(0.5), 5px, d:grow(0.5), 5px", 
				"5px, d:grow, 5px, d, 5px, d, 5px");
		// new CellConstraints("2, 2, 1, 1, fill, fill")
		tabPanelLaFs.setLayout(thisLayout);

		tableModelLaFs = new CustomTableModel();
		tableModelLaFs.setColumns(lafColNames);
		//tm.setColumns(cn2);
		// FIXME: tm.setData(td2);
		tableLaFs = new JTable();
		tableLaFs.setModel(tableModelLaFs);
		tableLaFs.setPreferredScrollableViewportSize(new Dimension(150, 150));
		JScrollPane scrollPane = new JScrollPane(tableLaFs);
		scrollPane.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		tabPanelLaFs.add(
				scrollPane, 
				new CellConstraints("2, 2, 1, 1, fill, fill"));

		tableLaFs.getColumnModel().getColumn(0).setPreferredWidth(lafColumnWidths[0]);
		tableLaFs.getColumnModel().getColumn(1).setPreferredWidth(lafColumnWidths[1]);

//		table.getColumnModel().getColumn(1).setCellEditor(
//				new SpinnerCellEditor(0, 50, 1));
//		table.getColumnModel().getColumn(2).setCellEditor(
//				new SpinnerCellEditor(0, 50, 1));

		tableModelLaFs.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
//		        int row = e.getFirstRow();
//		        int column = e.getColumn();
//		        CustomTableModel model = (CustomTableModel)e.getSource();
//		        String columnName = model.getColumnName(column);
//		        Object data = model.getValueAt(row, column);
//		        // Do something with the data...
//		        //System.err.println("data changed: " + data);
		    }
		});
		tableLaFs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableLaFs.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
				        int index;
						if (e.getValueIsAdjusting()) return;

				        ListSelectionModel lsm =
				            (ListSelectionModel)e.getSource();
				        if (lsm.isSelectionEmpty()) {
				            index = -1;
				            //return; // now rows selected
				        } else {
				        	index = lsm.getMinSelectionIndex();
				        }
				        updateSkinsTable(index);
					}
		});

		String [] skcolNames = { "Theme", "Enable" };
		tableModelSkins = new CustomTableModel();
		tableModelSkins.setColumns(skcolNames);
		//tm.setColumns(cn2);
		// FIXME: tm.setData(td2);
		tableSkins = new JTable();
		tableSkins.setModel(tableModelSkins);
		tableSkins.setPreferredScrollableViewportSize(new Dimension(150, 150));
		JScrollPane skscrollPane = new JScrollPane(tableSkins);
		skscrollPane.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		tabPanelLaFs.add(
				skscrollPane, 
				new CellConstraints("4, 2, 1, 1, fill, fill"));

		tableSkins.getColumnModel().getColumn(0).setPreferredWidth(skinsColumnsWidths[0]);
		tableSkins.getColumnModel().getColumn(1).setPreferredWidth(skinsColumnsWidths[1]);

//		table.getColumnModel().getColumn(1).setCellEditor(
//				new SpinnerCellEditor(0, 50, 1));
//		table.getColumnModel().getColumn(2).setCellEditor(
//				new SpinnerCellEditor(0, 50, 1));

		tableModelSkins.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
//		        int row = e.getFirstRow();
//		        int column = e.getColumn();
//		        CustomTableModel model = (CustomTableModel)e.getSource();
//		        if (row<0 || row>=model.getRowCount()) return;
//		        if (column<0 || column>=model.getColumnCount()) return;
//		        String columnName = model.getColumnName(column);
//		        Object data = model.getValueAt(row, column);
//		        // Do something with the data...
//		        //System.err.println("data changed: " + data);
		    }
		});
		tableSkins.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

//		JPanel dummy = new JPanel();
//		dummy.setSize(10, 10);
//		tabPanelLaFs.add(skscrollPane, BorderLayout.CENTER);

		buttonAddLaF = new JButton("Add Look and Feel...");
		buttonAddLaF.setActionCommand(CMD_LAF_ADD);
		buttonAddLaF.addActionListener(this);
		tabPanelLaFs.add(
				buttonAddLaF, 
				new CellConstraints("2, 4, 1, 1, default, default"));

		buttonRemLaF = new JButton("Remove Look and Feel...");
		buttonRemLaF.setActionCommand(CMD_LAF_REM);
		buttonRemLaF.addActionListener(this);
		tabPanelLaFs.add(
				buttonRemLaF, 
				new CellConstraints("2, 6, 1, 1, default, default"));

		buttonAddTheme = new JButton("Add Theme...");
		buttonAddTheme.setActionCommand(CMD_THM_ADD);
		buttonAddTheme.addActionListener(this);
		tabPanelLaFs.add(
				buttonAddTheme, 
				new CellConstraints("4, 4, 1, 1, default, default"));

		buttonRemTheme = new JButton("Remove Theme...");
		buttonRemTheme.setActionCommand(CMD_THM_REM);
		buttonRemTheme.addActionListener(this);
		tabPanelLaFs.add(
				buttonRemTheme, 
				new CellConstraints("4, 6, 1, 1, default, default"));
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#loadSettings()
	 */
	@Override
	public void loadSettings() {
		Object [] lafsAvail;
		Object [] lafsUse;
		String [] sa;
		lafsAvail = cfg.getValueArray(CfgCalc.KEY_UI_LAF_LISTAVAIL_BASE);
		lafsUse = cfg.getValueArray(CfgCalc.KEY_UI_LAF_LISTUSE_BASE);

		lafsTableData = new ArrayList<Object[]>();
		for (int i = 0; i < lafsAvail.length; i++) {
			sa = ((String)lafsAvail[i]).split(CfgCalc.ITEM_SEPARATOR_STR);
			// FIXME: link the 5 to a constant in CfgCalc
			lafsTableData.add(i, new Object[5]);
			lafsTableData.get(i)[0] = sa[1];	// the display name
			lafsTableData.get(i)[1] = new Boolean(false);
			// not visible:
			lafsTableData.get(i)[2] = sa[0];	// type
			lafsTableData.get(i)[3] = sa[2];	// the class name
			lafsTableData.get(i)[4] = (sa.length>3) ? sa[3] : null;
												// impl class name
		}
		for (int i = 0; i < lafsUse.length; i++) {
			Integer idx = (Integer)lafsUse[i];
			lafsTableData.get(idx.intValue())[1] = new Boolean(true);
			//System.err.println("activated entry " + lafsTableData[idx][0]);
		}
		tableModelLaFs.setData(lafsTableData);

		int lockIdx = -1;
		String selectedLaF = cfg.getStringValue(CfgCalc.KEY_UI_LAF_SELECT, null);
		if (selectedLaF!=null) {
			for (int i = 0; i < lafsTableData.size(); i++) {
				if (lafsTableData.get(i)[0].equals(selectedLaF)) {
					lockIdx = i;
					//lafsTableData[i][0] = lafsTableData[i][0] + " (active:locked)"; 
				}
			}
		}
		tableModelLaFs.setLockedRow(lockIdx);

//		skinsTableDataArray = new Object[lafsTableData.length][][];
		skinsTableDataArray = new ArrayList<ArrayList<Object[]>>();
		for (int i = 0; i < lafsTableData.size(); i++) {
			// if we have a skin list avail entry, do it, otherwise not
			String s;
			s = cfg.getStringValue(
					CfgCalc.KEY_UI_LAF_SKIN_LISTAVAIL_BASE + 
					lafsTableData.get(i)[0] + ".0", 
					null);
			if (s==null) {
				skinsTableDataArray.add(i, null);
				continue;
			}

			Object [] skinsAvail;
			Object [] skinsUse;
			String [] sask;
			skinsAvail = cfg.getValueArray(
					CfgCalc.KEY_UI_LAF_SKIN_LISTAVAIL_BASE + 
					lafsTableData.get(i)[0] + ".");
			skinsUse = cfg.getValueArray(
					CfgCalc.KEY_UI_LAF_SKIN_LISTUSE_BASE + 
					lafsTableData.get(i)[0] + ".");

			skinsTableDataArray.add(i, new ArrayList<Object[]>());
			// skinsAvail.length
			for (int j = 0; j < skinsAvail.length; j++) {
				sask = ((String)skinsAvail[j]).split(CfgCalc.ITEM_SEPARATOR_STR);
				// FIXME: link the 3 to a constant in CfgCalc
				skinsTableDataArray.get(i).add(j, new Object[3]);
				skinsTableDataArray.get(i).get(j)[0] = sask[0];	// display name
				skinsTableDataArray.get(i).get(j)[1] = new Boolean(false);
				// not visible:
				skinsTableDataArray.get(i).get(j)[2] = sask[1];	// file name
			}
			for (int j = 0; j < skinsUse.length; j++) {
				Integer idx = (Integer)skinsUse[j];
				skinsTableDataArray.get(i).get(idx.intValue())[1] = new Boolean(true);
				//System.err.println("skins: activated entry " + skinsTableDataArray[i][j][0]);
			}
		}

		tableLaFs.getColumnModel().getColumn(0).setPreferredWidth(lafColumnWidths[0]);
		tableLaFs.getColumnModel().getColumn(1).setPreferredWidth(lafColumnWidths[1]);

		if (tableLaFs.getRowCount()>0) {
			tableLaFs.removeRowSelectionInterval(0, tableLaFs.getRowCount()-1);
		}
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#storeSettings()
	 */
	@Override
	public void storeSettings() {
		String [] lafArray = new String[lafsTableData.size()];
		Integer [] useIndex = new Integer[lafsTableData.size()];
		int count = 0;
		for (int i=0; i<lafsTableData.size(); i++) {
			String lafStr;
			Object [] oa = lafsTableData.get(i);
//			lafsTableData.get(i)[0] = sa[1];	// the display name
//			lafsTableData.get(i)[1] = new Boolean(false);
//			// not visible:
//			lafsTableData.get(i)[2] = sa[0];	// type
//			lafsTableData.get(i)[3] = sa[2];	// the class name
//			lafsTableData.get(i)[4] = (sa.length>=3) ? sa[3] : null;
			lafStr = 
				(String)oa[2] + CfgCalc.ITEM_SEPARATOR_STR +
				(String)oa[0] + CfgCalc.ITEM_SEPARATOR_STR +
				(String)oa[3];
			if (oa[4]!=null) {
				lafStr = lafStr + CfgCalc.ITEM_SEPARATOR_STR + (String)oa[4]; 
			}
			lafArray[i] = lafStr;

			Object v = lafsTableData.get(i)[1];
			if (((Boolean)v).booleanValue()) {
				useIndex[count] = new Integer(i);
				count++;
			} else {
				// nothing
			}

			if (skinsTableDataArray.get(i)!=null) {
				Integer [] useSkinsIndex = 
					new Integer[skinsTableDataArray.get(i).size()];
				int skinCount = 0;
				String [] skinArray = new String[skinsTableDataArray.get(i).size()];
				
				for (int j = 0; j < skinsTableDataArray.get(i).size(); j++) {
					Object [] skoa = skinsTableDataArray.get(i).get(j);
					String skStr;
					skStr = 
						(String)skoa[0] + CfgCalc.ITEM_SEPARATOR_STR +
						(String)skoa[2];
					skinArray[j] = skStr;

					Object sv = skinsTableDataArray.get(i).get(j)[1];
					if (((Boolean)sv).booleanValue()) {
						useSkinsIndex[skinCount] = new Integer(j);
						skinCount++;
					} else {
						// nothing
					}
				}
				Integer [] saveSkinsIndex = new Integer[skinCount];
				System.arraycopy(useSkinsIndex, 0, saveSkinsIndex, 0, skinCount);
				cfg.putValueArray(
						CfgCalc.KEY_UI_LAF_SKIN_LISTAVAIL_BASE + 
						lafsTableData.get(i)[0] + ".", 
						skinArray);
				cfg.putValueArray(
						CfgCalc.KEY_UI_LAF_SKIN_LISTUSE_BASE + 
						lafsTableData.get(i)[0] + ".", 
						saveSkinsIndex);
			} // has data in skins table
		} // for LaFs
		Integer [] saveIndex = new Integer[count];
		System.arraycopy(useIndex, 0, saveIndex, 0, count);
		cfg.putValueArray(
				CfgCalc.KEY_UI_LAF_LISTAVAIL_BASE, 
				lafArray);
		cfg.putValueArray(
				CfgCalc.KEY_UI_LAF_LISTUSE_BASE, 
				saveIndex);
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#resetSettings()
	 */
	@Override
	public void resetSettings() {
		lafsTableData = null;
		tableModelLaFs.setData(null);
		skinsTableDataArray = null;
		tableModelSkins.setData(null);
	}

	protected void updateSkinsTable(int index) {
		if (index<0 || index>=skinsTableDataArray.size()) {
			return;
		}
		int lockRow;
		lockRow = -1;
		if (skinsTableDataArray.get(index)==null) {
			tableModelSkins.setData(null);
		} else {
			tableModelSkins.setData(skinsTableDataArray.get(index));
		}
		tableModelSkins.setLockedRow(lockRow);
		if (skinsTableDataArray.get(index)==null) {
			tableModelSkins.fireTableDataChanged();
			return;
		}
		// get name of skin:
		String selSkin;
		String lafName;
		lafName = (String)(lafsTableData.get(index)[0]);
		selSkin = cfg.getStringValue(
				CfgCalc.KEY_UI_LAF_SKIN_SELECT_BASE + lafName, null);
		if (selSkin==null) {
			tableModelSkins.fireTableDataChanged();
			return;
		}
		for (int i = 0; i < skinsTableDataArray.get(index).size(); i++) {
			if (((String)skinsTableDataArray.get(index).get(i)[0]).equals(selSkin)) {
				lockRow = i;
				break;
			}
		}
		tableModelSkins.setLockedRow(lockRow);
		tableModelSkins.fireTableDataChanged();
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_LAF_ADD)) {
			doLaFAdd();
		} else if (cmd.equals(CMD_LAF_REM)) {
			doLaFRem();
		} else if (cmd.equals(CMD_THM_ADD)) {
			doThemeAdd();
		} else if (cmd.equals(CMD_THM_REM)) {
			doThemeRem();
		}
	}


	protected void doLaFAdd() {
		File file = SettingsUtils.getJarFile(this);
		if (file==null) return;

		JarLister jl = new JarLister();
		try {
			jl.appendList(file.getPath(), null, null);
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			JOptionPane.showMessageDialog(
					this,
					"Could not access the file:\n" +
					file.getPath(),
					"Error accessing file",
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (IOException e) {
			// e.printStackTrace();
			JOptionPane.showMessageDialog(
					this,
					"Could not read the file:\n" +
					file.getPath(),
					"Error reading file",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		LaFAddDialog dlg = new LaFAddDialog((java.awt.Frame)null);
		dlg.addClasses(jl.getClassListDotted());
		dlg.updateControls();
		dlg.setFilter("LookAndFeel");
		dlg.setVisible(true);
		if (dlg.getResultCode()!=Dialog.RESULT_OK) {
			return;
		}
		if (dlg.getResultNamesCount()==0) {
			return;	// nothing to do.
		}

		// storeSettingsLaFs();

		// lafs require restart:
		setNeedRestart(true);
		
		int newcount = dlg.getResultNamesCount();
		for (int i = 0; i < newcount; i++) {
			// FIXME: link the 5 to cfgcalc constant
			Object [] oa = new Object[5];
			oa[0] = dlg.getResultDisplayName(i);	// the display name
			oa[1] = new Boolean(true);
			// not visible:
			oa[2] = CfgCalc.UI_LAF_TYPE_EXTRA;	// type
			oa[3] = dlg.getResultClassName(i);	// the class name
			oa[4] = null;

			lafsTableData.add(oa);
			skinsTableDataArray.add(null);
		}
		tableModelLaFs.fireTableStructureChanged();
		if (newcount>0) {
			// file contains the source name.
			SettingsUtils.installFile(
					this, pathMan, file, PathManager.SYS_LAF_DIR);
		}
	}

	protected void doLaFRem() {
		int sel = tableLaFs.getSelectedRow();
		if (sel<0) {
			JOptionPane.showMessageDialog(
					this,
					"Please select a Look-and-Feel first.",
					"Nothing selected",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (tableModelLaFs.getLockedRow()==sel) {
			JOptionPane.showMessageDialog(
					this,
					"Cannot remove the active Look-and-Feel.",
					"Row is locked",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		int result = JOptionPane.showConfirmDialog(
				this,
				"Please confirm, that the selected Look-and-Feel shall be removed.\n"+
				"The Look-and-Feel can be added again, by choosing 'Add Look-and-Feel...'.\n"+
				"If you only want to disable it, turn off the checkbox in the list of Look-and-Feels.",
				"Confirm Remove",
				JOptionPane.OK_CANCEL_OPTION);
		if (result!=JOptionPane.OK_OPTION) 
			return;
//		lafsTableData.remove(sel);

		lafsTableData.remove(sel);
		skinsTableDataArray.remove(sel);
		tableModelLaFs.fireTableStructureChanged();
	}

	protected void doThemeAdd() {
		int sel = tableLaFs.getSelectedRow();
		if (sel<0) {
			JOptionPane.showMessageDialog(
					this,
					"The SkinLF Look-and-Feel must be selected.",
					"Nothing selected",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		String name = (String)lafsTableData.get(sel)[0];
		if (!name.equals(CfgCalc.UI_LAF_IMPL_SKINLF_NAME)) {
			JOptionPane.showMessageDialog(
					this,
					"The SkinLF Look-and-Feel must be selected.",
					"Nothing selected",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
//		String lafName = CfgCalc.UI_LAF_IMPL_SKINLF_NAME;

		// select the ZIP file to add:
		File file = SettingsUtils.getZipFile(this);
		if (file==null) return;

		String themeName = file.getName();
		if (themeName.toLowerCase().endsWith(".zip")) {
			themeName = themeName.substring(0, themeName.length()-".zip".length());
		}
		if (themeName.toLowerCase().endsWith("themepack")) {
			themeName = themeName.substring(0, themeName.length()-"themepack".length());
		}
		if (themeName.equals("")) {
			themeName = file.getName();
		}

		Hashtable<String,Boolean> skinsHash = new Hashtable<String,Boolean>();
		for (Object object : skinsTableDataArray.get(sel)) {
			Object [] oa = (Object[])object;
			skinsHash.put((String)oa[0], Boolean.TRUE);
		}

		{
			int limit = 10;
			int index = 0;
			while (skinsHash.containsKey(themeName)) {
				themeName = themeName + "1";
				if (index++>limit) {
					// what now?
					break;
				}
			}
		}

		boolean again = false;
		while (true) {
			String newName = JOptionPane.showInputDialog(
					this,
					again ? 
							"The theme name existed, this is the suggested name:" :
							"Please review the name of the theme:",
					themeName);
			if (newName==null) 
				return;
			themeName = newName;

			if (!skinsHash.containsKey(themeName)) {
				break;	// thats the way out...
			}
			{
				int limit = 10;
				int index = 0;
				while (skinsHash.containsKey(themeName)) {
					themeName = themeName + "1";
					if (index++>limit) {
						// what now?
						break;
					}
				}
			}
			again = true;
		}

		File dstfile = null;
		boolean docopy = true;
		String fileName = file.getName();
		String dstPath = pathMan.getPathString(PathManager.SYS_APP_DIR);
		while (docopy) {
			if (dstPath==null) {
				JOptionPane.showMessageDialog(
						this,
						"Could not detect the system path for the calculator.\n"+
						"The theme will be registered, but you need to copy the themepack\n"+
						"to the directory by yourself.",
						"Could not detect LaF-path",
						JOptionPane.ERROR_MESSAGE);
				docopy = false;
				break;	// out of copy block
			}
			String dstSubPath = cfg.getStringValue(
					CfgCalc.KEY_UI_LAF_IMPL_SKINLF_THEMEPACK_PATH, null);
			if (dstSubPath==null || dstSubPath.equals("")) {
				JOptionPane.showMessageDialog(
						this,
						"Could not detect themepack path.\n"+
						"Please contact customer support.\n"+
						"The theme will not be registered.",
						"Could not detect themepack path",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			File dstfilepath = new File(dstPath);
			dstfilepath = new File(dstfilepath, dstSubPath);
			dstfile = new File(dstfilepath, fileName);
			{
				try {
					if (file.getCanonicalPath().startsWith(
							dstfilepath.getCanonicalPath())) {
						// the file is already there.
						docopy = false;
						break; // out of the copy checks
					}
				} catch (IOException e) {
					// e.printStackTrace();
					// nothing, just continue
				}
			}
			while (dstfile.exists()) {
				System.out.println("exists: " + dstfile.toString());
				String newFileName = JOptionPane.showInputDialog(
						this,
						"The file already exists. Please choose another name (keep the .zip extension!):",
						fileName);
				if (newFileName==null) 
					return;
				fileName = newFileName;
				dstfile = new File(dstfilepath, fileName);
			}
			break; // out of the copy check
		}
		// we come here after the copy checks:
		if (docopy && dstfile!=null) {
			if (!FileUtil.copyFile(file.getPath(), dstfile.getPath())) {
				JOptionPane.showMessageDialog(
						this,
						"Could not copy the themepack file to the destination:\n"+
						"File = " + dstfile.getPath() + "\n"+
						"The themepack will be registered, but you must copy the \n"+
						"themepack file manually to the destination.",
						"Could not copy themepack",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		skinsTableDataArray.get(sel).add(new Object[]{
				themeName, new Boolean(true), dstfile.getName()
		});
		tableModelSkins.fireTableStructureChanged();

//		cfg.putStringValue(
//				CfgCalc.KEY_UI_LAF_SKIN_LISTAVAIL_BASE + 
//				lafName + "." + skinsAvail.length,
//				themeName + CfgCalc.ITEM_SEPARATOR + dstfile.getName());
//		cfg.putIntValue(
//				CfgCalc.KEY_UI_LAF_SKIN_LISTUSE_BASE + 
//				lafName + "." + skinsUse.length,
//				skinsAvail.length);
	}

	protected void doThemeRem() {
		int lafsel = tableLaFs.getSelectedRow();
		if (lafsel<0) {
			JOptionPane.showMessageDialog(
					this,
					"Please select a Look-and-Feel first.",
					"Nothing selected",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		int sksel = tableSkins.getSelectedRow();
		if (sksel<0) {
			JOptionPane.showMessageDialog(
					this,
					"Please select a Skin first.",
					"Nothing selected",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (tableModelLaFs.getLockedRow()==lafsel) {
			if (tableModelSkins.getLockedRow()==sksel) {
				JOptionPane.showMessageDialog(
						this,
						"Cannot remove the active Skin.",
						"Row is locked",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else {
			if (tableModelSkins.getLockedRow()==sksel) {
				JOptionPane.showMessageDialog(
						this,
						"That skin is active for another Look-and-Feel.\n"+
						"To remove it, activate another skin of that Look-and-Feel\n"+
						"or simply remove its Look-and-Feel instead.",
						"Row is locked",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		int result = JOptionPane.showConfirmDialog(
				this,
				"Please confirm, that the selected Skin shall be removed.\n"+
				"The Skin can be added again, by choosing 'Add skin...'.\n"+
				"If you only want to disable it, turn off the checkbox in the list of Skins.",
				"Confirm Remove",
				JOptionPane.OK_CANCEL_OPTION);
		if (result!=JOptionPane.OK_OPTION) 
			return;

		tableModelSkins.removeRow(sksel);
	}


	/**
	 * @return Returns the needRestart.
	 */
	@Override
	public boolean isNeedRestart() {
		return needRestart;
	}

	/**
	 * @param needRestart The needRestart to set.
	 */
	public void setNeedRestart(boolean needRestart) {
		this.needRestart = needRestart;
	}
	
}
