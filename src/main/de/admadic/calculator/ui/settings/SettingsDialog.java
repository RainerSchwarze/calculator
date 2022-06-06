/**
 *
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
 */
package de.admadic.calculator.ui.settings;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.admadic.calculator.appctx.IAppContext;
import de.admadic.cfg.Cfg;
import de.admadic.util.PathManager;

/**
 * 
 * @author Rainer Schwarze
 *
 */
public class SettingsDialog extends de.admadic.ui.util.Dialog 
	implements ActionListener {

	// FIXME: make panels from the cluttered details
	
	final static boolean DBG = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	IAppContext appContext;

	Cfg cfg;
	PathManager pathMan;

	JPanel buttonPanel;
	JButton btnOK;
	JButton btnApply;
	JButton btnCancel;

	// for later easy reference.
	// "bulk" handling is done by the ArrayList
	SettingsPanelNumFmt tabPanelNumFmt;
	SettingsPanelLocale tabPanelLocale;
	SettingsPanelLayout tabPanelLayout;
	SettingsPanelLaFs tabPanelLaFs;
	SettingsPanelMisc tabPanelMisc;
	SettingsPanelAdvanced tabPanelAdvanced;
	SettingsPanelModules tabPanelModules;

	ArrayList<ISettingsPanel> settingsPanels;
	int [] tabIdxForExt;
	
	JTabbedPane tabbedPane;

	JFrame parentFrame;

	protected final String CMD_OK = "stgs.btn.ok";
	protected final String CMD_CANCEL = "stgs.btn.cancel";
	protected final String CMD_APPLY = "stgs.btn.apply";


	/**
	 * @param appContext
	 * @param frame
	 */
	public SettingsDialog(IAppContext appContext, JFrame frame) {
		super(frame);
		this.appContext = appContext;
		this.cfg = appContext.getCfg();
		this.pathMan = appContext.getPathManager();
		this.parentFrame = frame;
		settingsPanels = new ArrayList<ISettingsPanel>();
		initGUI();
		//loadSettings();
	}

	private void initTabLayout() {
		tabPanelLayout = new SettingsPanelLayout(cfg);
		settingsPanels.add(tabPanelLayout);
		tabPanelLayout.initContents();
	}

	private void initTabLaFs() {
		tabPanelLaFs = new SettingsPanelLaFs(cfg, pathMan);
		settingsPanels.add(tabPanelLaFs);
		tabPanelLaFs.initContents();
	}

	protected void updateExtendedSettingsStatus(boolean state) {
		if (tabIdxForExt==null) return;
		// FIXME: maybe there should be a delayed relaunch when the array is null?
		for (int i = 0; i < tabIdxForExt.length; i++) {
			tabbedPane.setEnabledAt(tabIdxForExt[i], state);
		}
	}
	
	private void initTabMisc() {
		tabPanelMisc = new SettingsPanelMisc(cfg);
		settingsPanels.add(tabPanelMisc);
		tabPanelMisc.initContents();
		tabPanelMisc.addExtendedSettingsListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				boolean state = (ev.getStateChange()==ItemEvent.SELECTED);
				updateExtendedSettingsStatus(state);
			}
		});
	}

	private void initTabNumFmt() {
		tabPanelNumFmt = new SettingsPanelNumFmt(cfg);
		settingsPanels.add(tabPanelNumFmt);
		tabPanelNumFmt.initContents();
	}

	private void initTabLocale() {
		tabPanelLocale = new SettingsPanelLocale(appContext);
		settingsPanels.add(tabPanelLocale);
		tabPanelLocale.initContents();
	}

	private void initTabAdvanced() {
		tabPanelAdvanced = new SettingsPanelAdvanced(cfg);
		settingsPanels.add(tabPanelAdvanced);
		tabPanelAdvanced.initContents();
		tabPanelAdvanced.setCallBackForReset(new SettingsPanelCallBack() {
			public void execute(Object hint) {
				setResultCode(RESULT_CANCEL);
				SettingsDialog.this.dispose();
			}
		});
	}

	private void initTabModules() {
		tabPanelModules = new SettingsPanelModules(cfg, pathMan);
		settingsPanels.add(tabPanelModules);
		tabPanelModules.initContents();
	}


	private void initGUI() {
		try {
			this.setModal(true);
			this.setTitle("Calculator Settings");
			{
				buttonPanel = new JPanel();
				this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
				{
					btnOK = new JButton();
					buttonPanel.add(btnOK);
					btnOK.setText("OK");
					btnCancel = new JButton();
					buttonPanel.add(btnCancel);
					btnCancel.setText("Cancel");
					btnApply = new JButton();
					buttonPanel.add(btnApply);
					btnApply.setText("Apply");

					btnOK.setActionCommand(CMD_OK);
					btnCancel.setActionCommand(CMD_CANCEL);
					btnApply.setActionCommand(CMD_APPLY);
					btnOK.addActionListener(this);
					btnCancel.addActionListener(this);
					btnApply.addActionListener(this);
				}
			}
			{
				tabbedPane = new JTabbedPane();
				this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
				{
					initTabNumFmt();
					initTabLocale();
					initTabMisc();
					initTabLaFs();
					initTabModules();
					initTabLayout();
					initTabAdvanced();
					tabbedPane.addTab("Number Format", null, tabPanelNumFmt, null);
					tabbedPane.addTab("Locale", null, tabPanelLocale, null);
					tabbedPane.addTab("Misc", null, tabPanelMisc, null);
					tabbedPane.addTab("Look & Feels", null, tabPanelLaFs, null);
					tabbedPane.addTab("Modules", null, tabPanelModules, null);
					tabbedPane.addTab("Layout", null, tabPanelLayout, null);
					tabbedPane.addTab("Advanced", null, tabPanelAdvanced, null);
					// the idx's for layout and advanced:
					tabIdxForExt = new int[]{5, 6};
				}
			}
			registerEnterAction(btnOK);
			this.pack();
			updateGUIPlacement();
		} catch (Exception e) {
			e.printStackTrace();
			// FIXME: message anywhere?
//			JOptionPane.showMessageDialog(
//					null,
//					"There was an error initializing the Settings Dialog.\n"+
//					"The Settings Dialog cannot be shown.\n"+
//					"Please contact customer support.",
//					"Error showing Settings Dialog",
//					JOptionPane.ERROR_MESSAGE);
			// this.dispose();
		}
	}

	/**
	 * 
	 */
	public void updateGUIPlacement() {
		this.setLocationRelativeTo(parentFrame);
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_OK)) {
			storeSettings();
			setResultCode(RESULT_OK);
			SettingsDialog.this.dispose();
		} else if (cmd.equals(CMD_APPLY)) {
			storeSettings();
		} else if (cmd.equals(CMD_CANCEL)) {
			setResultCode(RESULT_CANCEL);
			SettingsDialog.this.dispose();
		} else {
			// don't know
		}
	}
	
	/**
	 * Reset as if it were newly created.
	 */
	public void resetSettings() {
		for (ISettingsPanel stgspnl : settingsPanels) {
			stgspnl.resetSettings();
		}
	}

	/**
	 * 
	 */
	public void loadSettings() {
		if (cfg==null) return;
		for (ISettingsPanel stgspnl : settingsPanels) {
			stgspnl.loadSettings();
		}
	}

	/**
	 * 
	 */
	public void storeSettings() {
		if (cfg==null) 
			return;

		for (ISettingsPanel stgspnl : settingsPanels) {
			stgspnl.storeSettings();
		}

		boolean needRestart = false;
		for (ISettingsPanel stgspnl : settingsPanels) {
			needRestart |= stgspnl.isNeedRestart();
		}

		if (needRestart) {
			JOptionPane.showMessageDialog(
					this, 
					"Please restart the Calculator to let all changes apply",
					"Restart the Program",
					JOptionPane.WARNING_MESSAGE);
		}
	}
}
