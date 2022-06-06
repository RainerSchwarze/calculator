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

import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.ui.CfgCalc;
import de.admadic.cfg.Cfg;

/**
 * @author Rainer Schwarze
 *
 */
public class SettingsPanelMisc extends AbstractSettingsPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Cfg cfg;

	JCheckBox checkLaFEnable;
	JCheckBox checkLoadBtnGfx;
	JCheckBox checkButtonPaintFocus;
	JCheckBox checkCustomBtnFont;
	JCheckBox checkPreloadUI;
	JCheckBox checkSkinMenuFlat;
	JCheckBox checkUseClasspathExtender;

	JCheckBox checkExtendedSettings;
	JCheckBox checkValueInTitle;
	JCheckBox checkShowTxtProtocol;
	JCheckBox checkShowTabProtocol;
	JCheckBox checkGmxSnow;

	boolean needRestart = false;

	/**
	 * @param cfg 
	 * 
	 */
	public SettingsPanelMisc(Cfg cfg) {
		super();
		this.cfg = cfg;
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#initContents()
	 */
	@Override
	public void initContents() {
		// tabPanelMisc = new JPanel();
		JPanel tabPanelMisc = this;
		FormLayout thisLayout = new FormLayout(
				"12px, d:grow(0.5), 12px, d:grow(0.5), 12px", 
				"12px, d, 5px, d, 5px, d, "+
				"5px, d, 5px, d, 5px, d, "+
				"5px, d, 5px, d, 5px, d, 5px, d, "+
				"5px, d, 5px, d, 5px, d, 12px");
		// new CellConstraints("2, 2, 1, 1, fill, fill")
		tabPanelMisc.setLayout(thisLayout);

		tabPanelMisc.add(
				DefaultComponentFactory.getInstance().createSeparator(
						"Require Restart of Calculator:"),
				new CellConstraints("2, 2, 1, 1, default, default"));

		tabPanelMisc.add(
				DefaultComponentFactory.getInstance().createSeparator(
						"General:"),
				new CellConstraints("4, 2, 1, 1, default, default"));

		checkLaFEnable = new JCheckBox("Enable Skins (Java Look and Feels)");
		tabPanelMisc.add(
				checkLaFEnable,
				new CellConstraints("2, 4, 1, 1, default, default"));
		
	
		checkLoadBtnGfx = new JCheckBox("Use Custom Graphics for Buttons");
		tabPanelMisc.add(
				checkLoadBtnGfx,
				new CellConstraints("2, 6, 1, 1, default, default"));

		checkButtonPaintFocus = new JCheckBox("Paint Focus in Buttons");
		tabPanelMisc.add(
				checkButtonPaintFocus,
				new CellConstraints("2, 8, 1, 1, default, default"));

		checkCustomBtnFont = new JCheckBox("Use Custom Fonts for Buttons");
		tabPanelMisc.add(
				checkCustomBtnFont,
				new CellConstraints("2, 10, 1, 1, default, default"));

		checkPreloadUI = new JCheckBox("Preload User Interface on Startup");
		tabPanelMisc.add(
				checkPreloadUI,
				new CellConstraints("2, 12, 1, 1, default, default"));

		checkUseClasspathExtender = new JCheckBox("Internally Extend Classpath");
		tabPanelMisc.add(
				checkUseClasspathExtender,
				new CellConstraints("2, 14, 1, 1, default, default"));

		checkSkinMenuFlat = new JCheckBox("Flat Skin Menu");
		tabPanelMisc.add(
				checkSkinMenuFlat,
				new CellConstraints("2, 16, 1, 1, default, default"));

		checkExtendedSettings = new JCheckBox("Show Extended Settings");
		// FIXME: hack
		// by default the settings are shown. so the default is "on".
		// if then the settings shall be "off" after loadSettings, the 
		// itemChange event is fired. (otherwise there would be no change 
		// and the event would not be fired.) 
		checkExtendedSettings.setSelected(true);
		tabPanelMisc.add(
				checkExtendedSettings,
				new CellConstraints("4, 4, 1, 1, default, default"));

		checkValueInTitle = new JCheckBox("Show Value in Titlebar");
		tabPanelMisc.add(
				checkValueInTitle,
				new CellConstraints("4, 6, 1, 1, default, default"));

		checkShowTxtProtocol = new JCheckBox("Show plain text protocol");
		tabPanelMisc.add(
				checkShowTxtProtocol,
				new CellConstraints("4, 8, 1, 1, default, default"));

		checkShowTabProtocol = new JCheckBox("Show tabular/editable protocol");
		tabPanelMisc.add(
				checkShowTabProtocol,
				new CellConstraints("4, 10, 1, 1, default, default"));

		checkGmxSnow = new JCheckBox("Snow with Xmas-Skin");
		tabPanelMisc.add(
				checkGmxSnow,
				new CellConstraints("4, 12, 1, 1, default, default"));
	}

	/**
	 * @param extStgsListener
	 */
	public void addExtendedSettingsListener(ItemListener extStgsListener) {
		if (extStgsListener!=null) {
			checkExtendedSettings.addItemListener(extStgsListener);
		}
	}
	
	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#loadSettings()
	 */
	@Override
	public void loadSettings() {
		checkLaFEnable.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_UI_LAF_ENABLE, true));
		checkLoadBtnGfx.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_LOADGFX, true));
		checkButtonPaintFocus.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_PAINTFOCUS, true));
		checkCustomBtnFont.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_CUSTOMFONT, true));
		checkPreloadUI.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_PRELOAD_UI, true));
		checkUseClasspathExtender.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_CLASSPATH_USE_EXTENDER, true));
		checkSkinMenuFlat.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_SKINMENU_FLAT, true));

		checkExtendedSettings.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_EXTENDEDSETTINGS, false));
		// updateExtendedSettingsStatus(checkExtendedSettings.isSelected());
		// (should not need it...)
		checkValueInTitle.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_VALUE_IN_TITLE, true));
		checkShowTxtProtocol.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_SHOW_TXT_PROT, false));
		checkShowTabProtocol.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_SHOW_TAB_PROT, true));
		checkGmxSnow.setSelected(
				cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_GMX_SNOW, true));
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#storeSettings()
	 */
	@Override
	public void storeSettings() {
		if (cfg.getBooleanValue(CfgCalc.KEY_UI_LAF_ENABLE, true)!=
			checkLaFEnable.isSelected()) {
			needRestart = true;
		}
		if (cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_LOADGFX, true)!=
			checkLoadBtnGfx.isSelected()) {
			needRestart = true;
		}
		if (cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_PAINTFOCUS, true)!=
			checkButtonPaintFocus.isSelected()) {
			needRestart = true;
		}
		if (cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_CUSTOMFONT, true)!=
			checkCustomBtnFont.isSelected()) {
			needRestart = true;
		}
		if (cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_PRELOAD_UI, true)!=
			checkPreloadUI.isSelected()) {
			needRestart = true;
		}
		if (cfg.getBooleanValue(CfgCalc.KEY_CLASSPATH_USE_EXTENDER, true)!=
			checkUseClasspathExtender.isSelected()) {
			needRestart = true;
		}
		if (cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_SKINMENU_FLAT, true)!=
			checkSkinMenuFlat.isSelected()) {
			needRestart = true;
		}

		cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_EXTENDEDSETTINGS, checkExtendedSettings.isSelected());
		cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_VALUE_IN_TITLE, checkValueInTitle.isSelected());
		cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_SHOW_TXT_PROT, checkShowTxtProtocol.isSelected());
		cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_SHOW_TAB_PROT, checkShowTabProtocol.isSelected());
		cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_GMX_SNOW, checkGmxSnow.isSelected());

		cfg.putBooleanValue(CfgCalc.KEY_UI_LAF_ENABLE, checkLaFEnable.isSelected());
		cfg.putBooleanValue(CfgCalc.KEY_UI_BUTTON_LOADGFX, checkLoadBtnGfx.isSelected());
		cfg.putBooleanValue(CfgCalc.KEY_UI_BUTTON_PAINTFOCUS, checkButtonPaintFocus.isSelected());
		cfg.putBooleanValue(CfgCalc.KEY_UI_BUTTON_CUSTOMFONT, checkCustomBtnFont.isSelected());
		cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_PRELOAD_UI, checkPreloadUI.isSelected());
		cfg.putBooleanValue(CfgCalc.KEY_CLASSPATH_USE_EXTENDER, checkUseClasspathExtender.isSelected());
		cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_SKINMENU_FLAT, checkSkinMenuFlat.isSelected());
	}
}
