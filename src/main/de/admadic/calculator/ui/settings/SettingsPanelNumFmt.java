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
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.types.CaDouble;
import de.admadic.calculator.types.CaDoubleFormat;
import de.admadic.calculator.ui.CfgCalc;
import de.admadic.cfg.Cfg;

/**
 * @author Rainer Schwarze
 *
 */
public class SettingsPanelNumFmt extends AbstractSettingsPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Cfg cfg;

	JComboBox cbNumFmtType;
	JSpinner spNumFmtWidth;
	JLabel lbNumFmtWidth;
	JSpinner spNumFmtPrec;
	JScrollPane spNumFmtSamples;
	JTable tblNumFmtSamples;
	JLabel lbNumFmtPrec;
	JLabel lbNumFmtType;
	SpinnerNumberModel spMdlNumFmtPrec;
	SpinnerNumberModel spMdlNumFmtWidth;
	JSlider sldNumFmtWidth;
	JSlider sldNumFmtPrec;
	boolean updateNumFmtPreviewBusy = false;
	CaDoubleFormat numFmt;

	Double [] tblNumFmtSamplesValues = {
			Double.valueOf(1.0),
			Double.valueOf(0.001),
			Double.valueOf(123456789.123456789),
			Double.valueOf(1.23456e15),
	};
	String [][] tblNumFmtSamplesData = {
			{tblNumFmtSamplesValues[0].toString(), "<output>"},
			{tblNumFmtSamplesValues[1].toString(), "<output>"},
			{tblNumFmtSamplesValues[2].toString(), "<output>"},
			{tblNumFmtSamplesValues[3].toString(), "<output>"},
	};
	String [] tblNumFmtSamplesColumns = {"Value", "Output"};
	DefaultTableModel tblMdlNumFmtSamples;


	/**
	 * @param cfg 
	 * 
	 */
	public SettingsPanelNumFmt(Cfg cfg) {
		super();
		this.cfg = cfg;
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#initContents()
	 */
	@Override
	public void initContents() {
		JPanel tabPanelNumFmt = this;
		numFmt = new CaDoubleFormat();
		// tabPanelNumFmt = new JPanel();
		FormLayout thisLayout = new FormLayout(
				"5px, d, 12px, 50dlu, 5px, 50dlu, 12px, 50dlu, 5px, 50dlu, 5px, d:grow, 5px", 
				"5px, d, 5px, d, 1px, d, 5px, d, 5px, d, 5px");
		tabPanelNumFmt.setLayout(thisLayout);

		tabPanelNumFmt.add(
				DefaultComponentFactory.getInstance().createSeparator(
				"Number Format:"),
				new CellConstraints("2, 2, 9, 1, default, default"));

		tabPanelNumFmt.add(
				DefaultComponentFactory.getInstance().createSeparator(
				"Samples:"),
				new CellConstraints("2, 8, 9, 1, default, default"));

		ComboBoxModel cbNumFmtTypeModel = new DefaultComboBoxModel(
				new String[] { 
						CaDoubleFormat.type2DisplayString(CaDoubleFormat.TYPE_AUTO), 
						CaDoubleFormat.type2DisplayString(CaDoubleFormat.TYPE_FIXED), 
						CaDoubleFormat.type2DisplayString(CaDoubleFormat.TYPE_ENG), 
				});
		cbNumFmtType = new JComboBox();
		tabPanelNumFmt.add(
				cbNumFmtType,
				new CellConstraints("2, 6, 1, 1, default, default"));
		cbNumFmtType.setModel(cbNumFmtTypeModel);
		lbNumFmtType = new JLabel();
		tabPanelNumFmt.add(
				lbNumFmtType,
				new CellConstraints("2, 4, 1, 1, default, default"));
		lbNumFmtType.setText("Type:");

		sldNumFmtWidth = new JSlider(0, 20, 0);
		sldNumFmtWidth.setPaintLabels(true);
		sldNumFmtWidth.setPaintTicks(true);
		sldNumFmtWidth.setSnapToTicks(true);
		sldNumFmtWidth.setMajorTickSpacing(5);
		sldNumFmtWidth.setMinorTickSpacing(1);
		tabPanelNumFmt.add(
				sldNumFmtWidth,
				new CellConstraints("4, 6, 3, 1, default, default"));
		spMdlNumFmtWidth = new SpinnerNumberModel(0, 0, 20, 1);
		spNumFmtWidth = new JSpinner();
		tabPanelNumFmt.add(
				spNumFmtWidth,
				new CellConstraints("6, 4, 1, 1, default, default"));
		spNumFmtWidth.setModel(spMdlNumFmtWidth);
		lbNumFmtWidth = new JLabel();
		tabPanelNumFmt.add(
				lbNumFmtWidth,
				new CellConstraints("4, 4, 1, 1, default, default"));
		lbNumFmtWidth.setText("Width:");

		sldNumFmtPrec = new JSlider(0, 20, 0);
		sldNumFmtPrec.setPaintLabels(true);
		sldNumFmtPrec.setPaintTicks(true);
		sldNumFmtPrec.setSnapToTicks(true);
		sldNumFmtPrec.setMajorTickSpacing(5);
		sldNumFmtPrec.setMinorTickSpacing(1);
		tabPanelNumFmt.add(
				sldNumFmtPrec,
				new CellConstraints("8, 6, 3, 1, default, default"));
		spMdlNumFmtPrec = new SpinnerNumberModel(0, 0, 20, 1);
		spNumFmtPrec = new JSpinner();
		tabPanelNumFmt.add(
				spNumFmtPrec,
				new CellConstraints("10, 4, 1, 1, default, default"));
		spNumFmtPrec.setModel(spMdlNumFmtPrec);
		lbNumFmtPrec = new JLabel();
		tabPanelNumFmt.add(
				lbNumFmtPrec,
				new CellConstraints("8, 4, 1, 1, default, default"));
		lbNumFmtPrec.setText("Precision:");

		spNumFmtSamples = new JScrollPane();
		tabPanelNumFmt.add(
				spNumFmtSamples,
				new CellConstraints("2, 10, 9, 1, default, default"));
		tblMdlNumFmtSamples = new DefaultTableModel();
		tblMdlNumFmtSamples.setDataVector(
				tblNumFmtSamplesData, tblNumFmtSamplesColumns);
		tblNumFmtSamples = new JTable();
		spNumFmtSamples.setViewportView(tblNumFmtSamples);
		tblNumFmtSamples.setModel(tblMdlNumFmtSamples);
		tblNumFmtSamples.setPreferredScrollableViewportSize(
				new Dimension(300, 100));
		tblNumFmtSamples.setFont(new Font("Monospaced", Font.PLAIN, 12));

		spMdlNumFmtPrec.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateNumFmtPreview(spMdlNumFmtPrec);
			}
		});
		spMdlNumFmtWidth.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateNumFmtPreview(spMdlNumFmtWidth);
			}
		});
		cbNumFmtType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateNumFmtPreview(cbNumFmtType);
			}
		});
		sldNumFmtWidth.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateNumFmtPreview(sldNumFmtWidth);
//			    JSlider source = (JSlider)e.getSource();
//			    int val = (int)source.getValue();
//			    if (!source.getValueIsAdjusting()) { //done adjusting
//			    } else { //value is adjusting; just set the text
//			    }
			}
		});
		sldNumFmtPrec.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateNumFmtPreview(sldNumFmtPrec);
//			    JSlider source = (JSlider)e.getSource();
//			    int val = (int)source.getValue();
//			    if (!source.getValueIsAdjusting()) { //done adjusting
//			    } else { //value is adjusting; just set the text
//			    }
			}
		});
	}

	void updateNumFmtPreview(Object source) {
		if (updateNumFmtPreviewBusy) return;
		updateNumFmtPreviewBusy = true;

		if (source==spMdlNumFmtWidth) {
			sldNumFmtWidth.setValue(
					((Integer)spMdlNumFmtWidth.getValue()).intValue());
		}
		if (source==spMdlNumFmtPrec) {
			sldNumFmtPrec.setValue(
					((Integer)spMdlNumFmtPrec.getValue()).intValue());
		}
		if (source==sldNumFmtWidth) {
			spMdlNumFmtWidth.setValue(
					Integer.valueOf(sldNumFmtWidth.getValue()));
		}
		if (source==sldNumFmtPrec) {
			spMdlNumFmtPrec.setValue(
					Integer.valueOf(sldNumFmtPrec.getValue()));
		}

		// nothing
		int sel = cbNumFmtType.getSelectedIndex();
		if (sel<0) return;	// nothing to update
		int t = 0;
		int w = ((Integer)spMdlNumFmtWidth.getValue()).intValue();
		int p = ((Integer)spMdlNumFmtPrec.getValue()).intValue();

		updateNumFmtPreviewBusy = false;

		switch (sel) {
		case 1: t = CaDoubleFormat.TYPE_FIXED; break;
		case 2: t = CaDoubleFormat.TYPE_ENG; break;
		case 0: 
		default: t = CaDoubleFormat.TYPE_AUTO; break;
		}

		for (int i = 0; i < tblNumFmtSamplesData.length; i++) {
			tblNumFmtSamplesData[i][0] = tblNumFmtSamplesValues[i].toString();
			tblNumFmtSamplesData[i][1] = CaDoubleFormat.format(
					t, w, p, 
					new CaDouble(tblNumFmtSamplesValues[i].doubleValue()));
		}
		tblMdlNumFmtSamples.setDataVector(
				tblNumFmtSamplesData, tblNumFmtSamplesColumns);
		//tblMdlNumFmtSamples.fireTableDataChanged();
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#loadSettings()
	 */
	@Override
	public void loadSettings() {
		updateNumFmtPreviewBusy = true;
		String fmt = cfg.getStringValue(
				CfgCalc.KEY_UI_NUMBER_FORMAT, 
				CfgCalc.UI_NUMBER_FORMAT_DEFAULT);
		CaDoubleFormat tmpFmt = CaDoubleFormat.valueOf(fmt);
		if (tmpFmt==null) {
			tmpFmt = new CaDoubleFormat();
		}
		if (tmpFmt.getType()==CaDoubleFormat.TYPE_AUTO) {
			cbNumFmtType.setSelectedIndex(0);
		} else if (tmpFmt.getType()==CaDoubleFormat.TYPE_FIXED) {
			cbNumFmtType.setSelectedIndex(1);
		} else if (tmpFmt.getType()==CaDoubleFormat.TYPE_ENG) {
			cbNumFmtType.setSelectedIndex(2);
		} else {
			cbNumFmtType.setSelectedIndex(0);
		}

		int w,p;
		w = tmpFmt.getWidth();
		p = tmpFmt.getPrec();
		sldNumFmtWidth.setValue(w);
		sldNumFmtPrec.setValue(p);
		spNumFmtWidth.setValue(Integer.valueOf(w));
		spNumFmtPrec.setValue(Integer.valueOf(p));
		updateNumFmtPreviewBusy = false;
		numFmt = tmpFmt;
		updateNumFmtPreview(null);
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#storeSettings()
	 */
	@Override
	public void storeSettings() {
		int numFmtType;
		int numFmtWidth;
		int numFmtPrec;
		numFmtType = cbNumFmtType.getSelectedIndex();
		numFmtWidth = sldNumFmtWidth.getValue();
		numFmtPrec = sldNumFmtPrec.getValue();

		switch (numFmtType) {
		case 0: numFmt.setType(CaDoubleFormat.TYPE_AUTO); break;
		case 1: numFmt.setType(CaDoubleFormat.TYPE_FIXED); break;
		case 2: numFmt.setType(CaDoubleFormat.TYPE_ENG); break;
		default: numFmt.setType(CaDoubleFormat.TYPE_AUTO); break;
		}
		numFmt.setWidth(numFmtWidth);
		numFmt.setPrec(numFmtPrec);

		cfg.putStringValue(CfgCalc.KEY_UI_NUMBER_FORMAT, numFmt.toString());
	}

}
