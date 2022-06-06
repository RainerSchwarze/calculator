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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.appctx.IAppContext;
import de.admadic.calculator.core.LocaleModelEntry;
import de.admadic.calculator.ui.CfgCalc;
import de.admadic.cfg.Cfg;
import de.admadic.ui.util.ListRefListModel;
import de.admadic.ui.util.SeparatedComboBoxListener;
import de.admadic.ui.util.SeparatedListCellRenderer;

/**
 * @author Rainer Schwarze
 *
 */
public class SettingsPanelLocale extends AbstractSettingsPanel 
implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static class FireableComboBoxModel extends DefaultComboBoxModel {

		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public FireableComboBoxModel() {
			super();
		}

		/**
		 * @param items
		 */
		public FireableComboBoxModel(Object[] items) {
			super(items);
		}

		/**
		 * @param v
		 */
		public FireableComboBoxModel(Vector<?> v) {
			super(v);
		}

		/**
		 * @param from
		 * @param to
		 */
		public void fireIntervalAdded(int from, int to) {
			fireIntervalAdded(this, from, to);
		}

		/**
		 * @param from
		 * @param to
		 */
		public void fireIntervalRemoved(int from, int to) {
			fireIntervalRemoved(this, from, to);
		}

		/**
		 * @param from
		 * @param to
		 */
		public void fireContentsChanged(int from, int to) {
			fireContentsChanged(this, from, to);
		}
	}
	
	IAppContext appContext;
	Cfg cfg;

	class LocaleRec {
		JLabel label;
		JComboBox comboLocale;
		JButton btnCustom;
		SeparatedListCellRenderer renderer;
		SeparatedComboBoxListener comboListener;
		FireableComboBoxModel comboModel;
	}
	LocaleRec [] localeRecs;
	String [] localeLabels;

	final static int REC_DEFAULT = 0;
	final static int REC_INPUT = 1;
	final static int REC_OUTPUT = 2;
	final static int REC_EXPORT = 3;

	JList listLanguages;
	JScrollPane scrollLanguages;
	ListRefListModel listModelLanguages;
	SeparatedListCellRenderer listRendererLanguages;

	JList listCountries;
	JScrollPane scrollCountries;
	ListRefListModel listModelCountries;
	SeparatedListCellRenderer listRendererCountries;

	
	final static String CMD_CUSTOM_DEFAULT = "btn.custom.default";
	final static String CMD_CUSTOM_INPUT = "btn.custom.input";
	final static String CMD_CUSTOM_OUTPUT = "btn.custom.output";
	final static String CMD_CUSTOM_EXPORT = "btn.custom.export";

	final static String CMD_SELECTION = "combo.selection";
	
	JCheckBox checkSame;

	Vector<LocaleModelEntry> vectorLocales;
	Vector<LocaleModelEntry> vectorLanguages;
	Vector<LocaleModelEntry> vectorCountries;

	Hashtable<String,Hashtable<String,LocaleModelEntry>> bighash;
	// hash:
	// la => (co => lme)
	// empty la -> "??"
	// empty co -> "??"
	// languages only are put into the bighash with co="" in the part hash
	// countries only are put into the bighash entry ""
	
	boolean needRestart = false;

	/**
	 * @param appContext 
	 * 
	 */
	public SettingsPanelLocale(IAppContext appContext) {
		super();
		this.appContext = appContext;
		this.cfg = appContext.getCfg();
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#initContents()
	 */
	@Override
	public void initContents() {
		// tabPanelMisc = new JPanel();
		JPanel tabPanelLocale = this;
		FormLayout thisLayout = new FormLayout(
				"12px, p, 5px, min(p;150px):grow(0.4), 5px, p, 12px, "+
				"min(p;150):grow(0.3), 5px, min(p;150):grow(0.3), 12px", 
				"12px, p, 5px, "+	// labels
				"p, 5px, p, 5px, "+	// default + same
				"p, 5px, "+ 		// input
				"p, 5px, "+ 		// output
				"p, 12px" 			// export
				);
		CellConstraints cc = new CellConstraints();
		tabPanelLocale.setLayout(thisLayout);

		tabPanelLocale.add(new JLabel("Locales:"), cc.xy(2, 2));

		Locale displayLoc = appContext.getLocaleManager().getDefaultLocale();
		vectorLocales = LocaleModelEntry.getLocaleList(true, displayLoc);
		vectorLanguages = LocaleModelEntry.getLanguageList(true, displayLoc);
		vectorCountries = LocaleModelEntry.getCountryList(true, displayLoc);
		buildBigHash();
		
		localeRecs = new LocaleRec[4];
		localeLabels = new String[]{
				"Default:", "Input:", "Output:", "Export:"
		};
		for (int i=0; i<4; i++) {
			localeRecs[i] = new LocaleRec();
			localeRecs[i].label = new JLabel(localeLabels[i]);
			localeRecs[i].comboLocale = new JComboBox();
			localeRecs[i].btnCustom = new JButton("<=");
			localeRecs[i].btnCustom.setToolTipText("Create from custom selection");
			localeRecs[i].btnCustom.addActionListener(this);
			localeRecs[i].comboListener = new SeparatedComboBoxListener(
					localeRecs[i].comboLocale);
			localeRecs[i].comboModel = new FireableComboBoxModel(vectorLocales);
			localeRecs[i].renderer = new SeparatedListCellRenderer();
			localeRecs[i].renderer.setDefaultTab(5);
			localeRecs[i].comboLocale.setRenderer(
					localeRecs[i].renderer);
			localeRecs[i].comboLocale.addActionListener(
					localeRecs[i].comboListener);
			localeRecs[i].comboLocale.setModel(localeRecs[i].comboModel);

			localeRecs[i].comboLocale.addActionListener(this);
			localeRecs[i].comboLocale.setActionCommand(CMD_SELECTION);

			int row = (i<1) ? i : i + 1;
			row = 4 + (row*2);
			tabPanelLocale.add(localeRecs[i].label, cc.xy(2, row));
			tabPanelLocale.add(localeRecs[i].comboLocale, cc.xy(4, row));
			tabPanelLocale.add(localeRecs[i].btnCustom, cc.xy(6, row));
		}

		localeRecs[REC_DEFAULT].btnCustom.setActionCommand(CMD_CUSTOM_DEFAULT);
		localeRecs[REC_INPUT].btnCustom.setActionCommand(CMD_CUSTOM_INPUT);
		localeRecs[REC_OUTPUT].btnCustom.setActionCommand(CMD_CUSTOM_OUTPUT);
		localeRecs[REC_EXPORT].btnCustom.setActionCommand(CMD_CUSTOM_EXPORT);

		checkSame = new JCheckBox("Same as default");
		tabPanelLocale.add(checkSame, cc.xywh(2, 6, 3, 1));
		checkSame.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateSameStatus();
			}
		});

		tabPanelLocale.add(new JLabel("Languages:"), cc.xy(8, 2));
		listModelLanguages = new ListRefListModel();
		listModelLanguages.setData(vectorLanguages);
		listLanguages = new JList();
		listLanguages.setModel(listModelLanguages);
		listRendererLanguages = new SeparatedListCellRenderer();
		listRendererLanguages.setDefaultTab(5);
		listLanguages.setCellRenderer(listRendererLanguages);
		scrollLanguages = new JScrollPane(
				listLanguages,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		tabPanelLocale.add(scrollLanguages, cc.xywh(8, 4, 1, 9));

		tabPanelLocale.add(new JLabel("Countries:"), cc.xy(10, 2));
		listModelCountries = new ListRefListModel();
		listModelCountries.setData(vectorCountries);
		listCountries = new JList();
		listCountries.setModel(listModelCountries);
		listRendererCountries = new SeparatedListCellRenderer();
		listRendererCountries.setDefaultTab(5);
		listCountries.setCellRenderer(listRendererCountries);
		scrollCountries = new JScrollPane(
				listCountries,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		tabPanelLocale.add(scrollCountries, cc.xywh(10, 4, 1, 9));
	}

	private void buildBigHash() {
		Hashtable<String,LocaleModelEntry> parthash;
		bighash = new Hashtable<String,Hashtable<String,LocaleModelEntry>>();
		// hash:
		// la => (co => lme)
		// empty la -> "??"
		// empty co -> "??"

		// create all languages.
		// note that we have to create a la-hash for each:
		for (LocaleModelEntry lme : vectorLanguages) {
			if (lme==null) continue;
			String la = lme.getLocale().getLanguage();
			parthash = new Hashtable<String,LocaleModelEntry>();
			bighash.put(la, parthash);
			// and add an empty country into the part hash:
			parthash.put("", lme);
		}
		if (!bighash.contains("")) {
			parthash = new Hashtable<String,LocaleModelEntry>();
			bighash.put("", parthash);
		}
		// get the part hash for country only lookup
		parthash = bighash.get("");
		for (LocaleModelEntry lme : vectorCountries) {
			if (lme==null) continue;
			String co = lme.getLocale().getCountry();
			parthash.put(co, lme);
		}
		// now build up the locales details hash:
		for (LocaleModelEntry lme : vectorLocales) {
			if (lme==null) continue;
			String la = lme.getLocale().getLanguage();
			String co = lme.getLocale().getCountry();
			parthash = bighash.get(la);
			if (parthash != null && !parthash.containsKey(co)) {
				parthash.put(co, lme);
			}
		}
	}

	/**
	 * @param lme 
	 */
	public void addToBigHash(LocaleModelEntry lme) {
		Hashtable<String,LocaleModelEntry> parthash;
		String la = lme.getLocale().getLanguage();
		String co = lme.getLocale().getCountry();
		parthash = bighash.get(la);
		if (!parthash.containsKey(co)) {
			parthash.put(co, lme);
		}
	}

	protected void updateSameStatus() {
		boolean isSame = checkSame.isSelected();
		for (int i=1; i<4; i++) {
			localeRecs[i].comboLocale.setEnabled(!isSame);
			localeRecs[i].btnCustom.setEnabled(!isSame);
			localeRecs[i].label.setEnabled(!isSame);
		}
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#loadSettings()
	 */
	@Override
	public void loadSettings() {
		String [] keys = {
				CfgCalc.KEY_UI_MAIN_LOCALE_DEFAULT,
				CfgCalc.KEY_UI_MAIN_LOCALE_INPUT,
				CfgCalc.KEY_UI_MAIN_LOCALE_OUTPUT,
				CfgCalc.KEY_UI_MAIN_LOCALE_EXPORT,
		};
		for (int i=0; i<keys.length; i++) {
			String v = cfg.getStringValue(keys[i], null);
			if (v==null) {
				// should not be possible, lets use the std entry then
				continue; 
			}
			String [] sa = v.split(CfgCalc.ITEM_SEPARATOR_STR);
			String la = "";
			String co = "";
			if (sa.length>0) la = sa[0];
			if (sa.length>1) co = sa[1];
			int idx = getFirstMatch(vectorLocales, la, co);
			if (idx>=0) {
				localeRecs[i].comboLocale.setSelectedIndex(idx);
			}
		}

		if (checkSame != null) {
			checkSame.setSelected(cfg.getBooleanValue(
					CfgCalc.KEY_UI_MAIN_LOCALE_SAMEASDEFAULT, true));
		}
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#storeSettings()
	 */
	@Override
	public void storeSettings() {
		String [] keys = {
				CfgCalc.KEY_UI_MAIN_LOCALE_DEFAULT,
				CfgCalc.KEY_UI_MAIN_LOCALE_INPUT,
				CfgCalc.KEY_UI_MAIN_LOCALE_OUTPUT,
				CfgCalc.KEY_UI_MAIN_LOCALE_EXPORT,
		};
		boolean isSame;
		isSame = checkSame.isSelected();
		cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_LOCALE_SAMEASDEFAULT, isSame);
		for (int i=0; i<keys.length; i++) {
			int sel = localeRecs[i].comboLocale.getSelectedIndex();
			if (sel<0) continue;
			LocaleModelEntry lme = vectorLocales.get(sel);
			if (lme==null) continue;
			String la = lme.getLocale().getLanguage();
			String co = lme.getLocale().getCountry();
			cfg.putStringValue(keys[i], la + CfgCalc.ITEM_SEPARATOR_STR + co);
		}
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_SELECTION)) {
			doLocaleSelection(e.getSource());
		} else if (cmd.equals(CMD_CUSTOM_DEFAULT)) {
			doSetCustom(REC_DEFAULT);
		} else if (cmd.equals(CMD_CUSTOM_INPUT)) {
			doSetCustom(REC_INPUT);
		} else if (cmd.equals(CMD_CUSTOM_OUTPUT)) {
			doSetCustom(REC_OUTPUT);
		} else if (cmd.equals(CMD_CUSTOM_EXPORT)) {
			doSetCustom(REC_EXPORT);
		}
	}

	private void doSetCustom(int recId) {
		String la = "";
		String co = "";

		int lasel = listLanguages.getSelectedIndex();
		int cosel = listCountries.getSelectedIndex();
		if (lasel<0 && cosel<0) {
			JOptionPane.showMessageDialog(
					null,
					"No language or country selected.\n"+
					"You need to select a language or country or both.",
					"Nothing selected",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (lasel>=0) {
			Object v = listLanguages.getSelectedValue();
			if (v!=null) {
				LocaleModelEntry lme = (LocaleModelEntry)v;
				la = lme.getLocale().getLanguage();
			}
		}
		if (cosel>=0) {
			Object v = listCountries.getSelectedValue();
			if (v!=null) {
				LocaleModelEntry lme = (LocaleModelEntry)v;
				co = lme.getLocale().getCountry();
			}
		}

		int losel = getFirstMatch(vectorLocales, la, co);
		if (losel<0) {
			// add entry
			addLocaleModelEntry(recId, la, co);
		} else {
			localeRecs[recId].comboLocale.setSelectedIndex(losel);
		}
	}

	private LocaleModelEntry getLMEFromHash(String la, String co) {
		Hashtable<String,LocaleModelEntry> parthash;
		if (!bighash.containsKey(la)) {
			return null;
		}
		parthash = bighash.get(la);
		if (!parthash.containsKey(co)) {
			return null;
		}
		return parthash.get(co);
	}
	
	private int getFirstMatch(
			Vector<LocaleModelEntry> v, String la, String co) {
		// LocaleModelEntry.getFirstMatchForLaCo(v, la, co);
		LocaleModelEntry lme = getLMEFromHash(la, co);
		if (lme==null) return -1;
		return v.indexOf(lme);
	}

	private void addLocaleModelEntry(int recId, String la, String co) {
		LocaleModelEntry org = vectorLocales.firstElement();
		LocaleModelEntry lme = new LocaleModelEntry(
				new Locale(la, co),
				(org!=null) ? org.getDisplayLocale() : Locale.getDefault());
		int [] lastsel = new int[4];
		for (int i=0; i<4; i++) {
			lastsel[i] = localeRecs[i].comboLocale.getSelectedIndex();
		}
		addToBigHash(lme);
		vectorLocales.add(0, lme);
		for (int i=0; i<4; i++) {
			localeRecs[i].comboModel.fireIntervalAdded(0, 0);
			if (i==recId) {
				localeRecs[i].comboLocale.setSelectedIndex(0);
			} else {
				if (lastsel[i]<0) {
					// nothing
				} else {
					localeRecs[i].comboLocale.setSelectedIndex(lastsel[i]+1);
				}
			}
		}
	}

	private void doLocaleSelection(Object src) {
		// find which record we have as the source:
		int id = -1;
		for (int i=0; i<4; i++) {
			if (src==localeRecs[i].comboLocale) {
				id = i;
				break;
			}
		}
		if (id<0) {
			// error?
			System.out.println("Could not locate source of selection action");
			return;
		}
		if (!localeRecs[id].comboLocale.hasFocus()) {
			// only if element has focus
			return;
		}
		// retrieve the LocaleModelEntry from it:
		Object v = localeRecs[id].comboLocale.getSelectedItem();
		if (v==null) return;
		LocaleModelEntry lme = (LocaleModelEntry)v;

		// select the language in the language list
		String la = lme.getLocale().getLanguage();
		if (la.equals("")) {
			listLanguages.clearSelection();
		} else {
			int sel = getFirstMatch(vectorLanguages, la, "");
			if (sel<0) {
				listLanguages.clearSelection();
			} else {
				listLanguages.setSelectedIndex(sel);
				listLanguages.ensureIndexIsVisible(sel);
			}
		}

		// select the country in the country list
		String co = lme.getLocale().getCountry();
		if (co.equals("")) {
			listCountries.clearSelection();
		} else {
			int sel = getFirstMatch(vectorCountries, "", co);
			if (sel<0) {
				listCountries.clearSelection();
			} else {
				listCountries.setSelectedIndex(sel);
				listCountries.ensureIndexIsVisible(sel);
			}
		}
	}
}
