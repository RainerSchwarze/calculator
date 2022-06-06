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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.ui.CfgCalc;
import de.admadic.cfg.Cfg;

/**
 * @author Rainer Schwarze
 *
 */
public class SettingsPanelAdvanced extends AbstractSettingsPanel implements 
	ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Cfg cfg;
	
	JButton btnReset;
	JButton btnClearPref;

	protected final String CMD_RESET = "stgs.btn.reset";
	protected final String CMD_CLEAR_PREF = "stgs.btn.clrpref";

	SettingsPanelCallBack callBackForReset;
	
	/**
	 * @param cfg 
	 * 
	 */
	public SettingsPanelAdvanced(Cfg cfg) {
		super();
		this.cfg = cfg;
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.AbstractSettingsPanel#initContents()
	 */
	@Override
	public void initContents() {
		// tabPanelAdvanced = new JPanel();
		JPanel tabPanelAdvanced = this;
		FormLayout thisLayout = new FormLayout(
				"5px, d:grow, 5px", 
				"5px, d, 5px, d, 5px");
		// new CellConstraints("2, 2, 1, 1, fill, fill")
		tabPanelAdvanced.setLayout(thisLayout);

		btnReset = new JButton();
		tabPanelAdvanced.add(
				btnReset,
				new CellConstraints("2, 2, 1, 1, left, default"));
		btnReset.setText("Reset...");
		btnReset.addActionListener(this);
		btnReset.setActionCommand(CMD_RESET);

		btnClearPref = new JButton();
		tabPanelAdvanced.add(
				btnClearPref,
				new CellConstraints("2, 4, 1, 1, left, default"));
		btnClearPref.setText("Clear Preferences...");
		btnClearPref.addActionListener(this);
		btnClearPref.setActionCommand(CMD_CLEAR_PREF);
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if (cmd.equals(CMD_RESET)) {
			int result;
			// FIXME: make "NO" the default button
			result = JOptionPane.showConfirmDialog(
					this, 
					"You are about to reset the settings of the application"+
					" to the default values.\n"+
					"These settings will be applied"+
					" immediately and the settings dialog will be closed.\n"+
					"Are you sure you want to reset the settings?", 
					"Reset configuration", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE);
			if (result==JOptionPane.YES_OPTION) {
				// FIXME: maybe keep the dialog open and let the user see
				// what happens when reset has been pressed. but for now
				// that complicates logic too much.
				// ok. lets do it:
				//btnCancel.setEnabled(false);
				cfg.setDefaults();
				//loadSettings();
				//result = RESULT_OK;
				if (callBackForReset!=null) {
					callBackForReset.execute(null);
					// setResultCode(RESULT_CANCEL);
					// SettingsDialog.this.dispose();
				}
			}
		} else if (cmd.equals(CMD_CLEAR_PREF)) {
			int result;
			// FIXME: make "NO" the default button
			result = JOptionPane.showConfirmDialog(
					this, 
					"You are about to clear the stored preferences.\n"+
					"The current settings will not be changed. To change also"+
					" the current settings use 'Reset' instead.\n"+
					"Are you sure you want to clear the stored preferences?", 
					"Clear Stored Preferences", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE);
			if (result==JOptionPane.YES_OPTION) {
				if (cfg.clearPreferences(CfgCalc.PREFERENCES_PATH)) {
					JOptionPane.showMessageDialog(
							this, 
							"The stored preferences were successfully cleared.",
							"Clear Stored Preferences", 
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(
							this, 
							"There was an error clearing the stored preferences\n"+
							"Please contact customer support.", 
							"Clear Stored Preferences", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * @param callBackForReset The callBackForReset to set.
	 */
	public void setCallBackForReset(SettingsPanelCallBack callBackForReset) {
		this.callBackForReset = callBackForReset;
	}
}
