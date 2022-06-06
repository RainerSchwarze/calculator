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
package de.admadic.units.ui;

import java.awt.Container;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.ui.util.Dialog;
import de.admadic.units.core.AbstractConverter;
import de.admadic.units.core.ConverterContext;
import de.admadic.units.core.ConverterIdent;
import de.admadic.units.core.ConverterLin;
import de.admadic.units.core.ConverterMul;
import de.admadic.units.core.Domain;
import de.admadic.units.core.IConverter;
import de.admadic.units.core.IUnit;
import de.admadic.units.core.SubstUnit;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class SubstUnitDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	IUnit baseUnit;
	JTextField textBaseUnit;
	JButton btnTakeBU;
	JTextField textId;
	JTextField textSymbol;
	JTextField textName;
	JComboBox comboDomain;
	JComboBox comboFactors;
	JCheckBox checkFactor;
	ConverterEditPanel panelConv;

	JButton btnOK;
	JButton btnCancel;
	
	SubstUnit editUnit;
	boolean editMode;
	
	final static String CMD_OK = "ok";
	final static String CMD_CANCEL = "cancel";
	
	ActionFunction af;
	ActionFunction afTake;
	UnitManager um;
	
	/**
	 * @param arg0
	 * @param um 
	 * @param af 
	 * @param afTake 
	 * @throws HeadlessException
	 */
	public SubstUnitDialog(
			Frame arg0, UnitManager um, 
			ActionFunction af, ActionFunction afTake) throws HeadlessException {
		super(arg0);
		this.um = um;
		this.af = af;
		this.afTake = afTake;
		initContents();
	}

	/**
	 * @param arg0
	 * @param af 
	 * @throws HeadlessException
	 */
	public SubstUnitDialog(java.awt.Dialog arg0, ActionFunction af) throws HeadlessException {
		super(arg0);
		this.af = af;
		initContents();
	}


	protected void initContents() {
		FormLayout fl = new FormLayout(
				"12px, p, 5px, p:grow, 5px, p, 12px",
				"12px, p, 5px, p, "+	// take
				"5px, p, 5px, p, "+	// id-symb
				"5px, p, 5px, p, "+	// dom/fact
				"5px, p, 5px, p, "+ // conv + label
				"12px, p, 12px");
		CellConstraints cc = new CellConstraints();

		Container c = this.getContentPane();
		c.setLayout(fl);

		setTitle("New BaseUnit");

		c.add(new JLabel("Base Unit:"), cc.xy(2, 2));
		c.add(textBaseUnit = new JTextField(), cc.xy(4, 2));
		c.add(btnTakeBU = new JButton("Take"), cc.xy(6, 2));
		btnTakeBU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				afTake.run(null);
			}
		});
		c.add(new JLabel("Id:"), cc.xy(2, 4));
		c.add(textId = new JTextField(), cc.xy(4, 4));
		c.add(new JLabel("Symbol:"), cc.xy(2, 6));
		c.add(textSymbol = new JTextField(), cc.xy(4, 6));
		c.add(new JLabel("Name:"), cc.xy(2, 8));
		c.add(textName = new JTextField(), cc.xy(4, 8));
		c.add(new JLabel("Domain:"), cc.xy(2, 10));
		c.add(comboDomain = UnitsUiUtil.createDomainsComboBox(um), cc.xy(4, 10));
		c.add(new JLabel("Factor:"), cc.xy(2, 12));
		c.add(comboFactors = UnitsUiUtil.createFactorsComboBox(um), cc.xy(4, 12));
		c.add(checkFactor = new JCheckBox(), cc.xy(6, 12));
		c.add(new JLabel("Converter:"), cc.xy(2, 14));
		c.add(panelConv = new ConverterEditPanel(), cc.xywh(2, 16, 3, 1));

		checkFactor.setSelected(true);
		checkFactor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				comboFactors.setEnabled(e.getStateChange()==ItemEvent.SELECTED);
			}
		});
		checkFactor.setSelected(false);
		
		JPanel btnPanel = new JPanel();
		c.add(btnPanel, cc.xywh(2, 18, 3, 1));

		btnOK = new JButton("Add");
		btnOK.setActionCommand(CMD_OK);
		btnOK.addActionListener(this);
		btnPanel.add(btnOK);
		btnCancel = new JButton("Close");
		btnCancel.setActionCommand(CMD_CANCEL);
		btnCancel.addActionListener(this);
		btnPanel.add(btnCancel);

		this.pack();
		this.setLocationRelativeTo(getOwner());
	}

	/**
	 * @param u
	 */
	public void setBaseUnit(IUnit u) {
		baseUnit = u;
		updateBaseUnit();
	}

	/**
	 * @return	Returns the selected base unit.
	 */
	public IUnit getBaseUnit() {
		return baseUnit;
	}
	
	protected void updateBaseUnit() {
		textBaseUnit.setText(
				baseUnit.getSymbol() + ": " + baseUnit.getName());
		Domain dom = baseUnit.getDomain();
		int sel = um.getDomains().indexOf(dom);
		comboDomain.setSelectedIndex(sel);
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_OK)) {
			Object [] param = new Object[7];
			param[0] = textId.getText();
			param[1] = textSymbol.getText();
			param[2] = textName.getText();
			int sel = comboDomain.getSelectedIndex();
			param[3] = um.getDomains().get(sel).getId();
			param[4] = null; // factor
			if (checkFactor.isSelected()) {
				int fs = comboFactors.getSelectedIndex();
				param[4] = um.getFactors().get(fs);
			}
			param[5] = null; // converter
			if (panelConv.getType()!=ConverterEditPanel.TYPE_NONE) {
				IConverter conv = null;
				Double m = null;
				Double n = null;
				String xm = null;
				String xn = null;
				switch (panelConv.getType()) {
				case ConverterEditPanel.TYPE_IDENTITY:
					conv = new ConverterIdent();
					break;
				case ConverterEditPanel.TYPE_MUL:
					m = Double.valueOf(panelConv.getParamM());
					xm = panelConv.getParamXM();
					conv = new ConverterMul(m);
					if (xm!=null && !xm.equals("")) {
						((AbstractConverter)conv).setContext(new ConverterContext());
						conv.getContext().add("xm", xm);
					}
					break;
				case ConverterEditPanel.TYPE_LINEAR:
					m = Double.valueOf(panelConv.getParamM());
					n = Double.valueOf(panelConv.getParamN());
					xm = panelConv.getParamXM();
					xn = panelConv.getParamXN();
					conv = new ConverterLin(m, n);
					ConverterContext cc = null;
					if (xm!=null && !xm.equals("")) {
						if (cc==null) {
							((AbstractConverter)conv).setContext(
									cc = new ConverterContext());
						}
						conv.getContext().add("xm", xm);
					}
					if (xn!=null && !xn.equals("")) 
						if (cc==null) {
							((AbstractConverter)conv).setContext(
									cc = new ConverterContext());
						}
						conv.getContext().add("xn", xn);
					break;
				}
				param[5] = conv;
			}
			param[6] = baseUnit;
			if (!af.run(param)) return;
			clearData();
			setResultCode(RESULT_OK);
			// setVisible(false);
		} else if (cmd.equals(CMD_CANCEL)) {
			if (isEditMode()) {
				setEditMode(false);
				clearData();
			}
			setResultCode(RESULT_CANCEL);
			setVisible(false);
		}
	}


	protected void clearData() {
		panelConv.setParamM("1.0");
		panelConv.setParamXM("");
		panelConv.setParamN("0.0");
		panelConv.setParamXN("");
	}

	/**
	 * @param unit
	 */
	public void editUnit(SubstUnit unit) {
		setEditMode(true);
		editUnit = unit;
		setBaseUnit(unit.getBaseUnit());
		// FIXME: directly access the subst fields!
		textId.setText(unit.getId());
		textSymbol.setText(unit.getSymbol());
		textName.setText(unit.getName());
		int dsel = um.getDomains().indexOf(unit.getDomain());
		comboDomain.setSelectedIndex(dsel);
		if (unit.getFactor()==null) {
			checkFactor.setSelected(false);
		} else {
			checkFactor.setSelected(true);
			int fsel = um.getFactors().indexOf(unit.getFactor());
			comboFactors.setSelectedIndex(fsel);
		}
		IConverter conv = unit.getSubstConverter();
		ConverterContext cc = null;
		if (conv!=null) cc = conv.getContext();

		if (conv==null) {
			panelConv.setType(ConverterEditPanel.TYPE_NONE);
		} else if (conv instanceof ConverterIdent) {
			panelConv.setType(ConverterEditPanel.TYPE_IDENTITY);
		} else if (conv instanceof ConverterMul) {
			panelConv.setType(ConverterEditPanel.TYPE_MUL);
			ConverterMul cm = (ConverterMul)conv;
			panelConv.setParamM(cm.getM().toString());
			String xm = null;
			if (cc!=null) xm = cc.getValue("xm");
			if (xm!=null) panelConv.setParamXM(xm);
		} else if (conv instanceof ConverterLin) {
			panelConv.setType(ConverterEditPanel.TYPE_LINEAR);
			ConverterLin cl = (ConverterLin)conv;
			panelConv.setParamM(cl.getM().toString());
			String xm = null;
			if (cc!=null) xm = cc.getValue("xm");
			if (xm!=null) panelConv.setParamXM(xm);
			panelConv.setParamN(cl.getN().toString());
			String xn = null;
			if (cc!=null) xn = cc.getValue("xn");
			if (xn!=null) panelConv.setParamXN(xn);
		} else {
			JOptionPane.showMessageDialog(
					null,
					"Unknown Converter: " + conv.getClass().getSimpleName(),
					"Unknown Converter",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @return Returns the editMode.
	 */
	public boolean isEditMode() {
		return editMode;
	}

	/**
	 * @param editMode The editMode to set.
	 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
		if (!editMode) editUnit = null;
		if (editMode) {
			btnOK.setText("Change");
			btnCancel.setText("Close");
		} else {
			btnOK.setText("Add");
			btnCancel.setText("Close");
		}
	}

	/**
	 * @return Returns the editUnit.
	 */
	public SubstUnit getEditUnit() {
		return editUnit;
	}
}
