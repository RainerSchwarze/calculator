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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.ui.util.Dialog;
import de.admadic.units.core.Domain;
import de.admadic.units.core.FactorUnit;
import de.admadic.units.core.IUnit;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class FactorUnitDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	IUnit baseUnit;
	JTextField textBaseUnit;
	JButton btnTakeBU;
	JComboBox comboDomain;
	JComboBox comboFactors;

	JButton btnOK;
	JButton btnCancel;
	
	final static String CMD_OK = "ok";
	final static String CMD_CANCEL = "cancel";
	
	ActionFunction af;
	ActionFunction afTake;
	UnitManager um;

	boolean editMode;
	FactorUnit editUnit;
	
	/**
	 * @param arg0
	 * @param um 
	 * @param af 
	 * @param afTake 
	 * @throws HeadlessException
	 */
	public FactorUnitDialog(
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
	public FactorUnitDialog(java.awt.Dialog arg0, ActionFunction af) throws HeadlessException {
		super(arg0);
		this.af = af;
		initContents();
	}


	protected void initContents() {
		FormLayout fl = new FormLayout(
				"12px, p, 5px, p:grow, 5px, p, 12px",
				"12px, p, 5px, p, "+	// take
				"5px, p, 5px, p, "+	// dom/fact
				"12px, p, 12px");
		CellConstraints cc = new CellConstraints();

		Container c = this.getContentPane();
		c.setLayout(fl);

		setTitle("New FactorUnit");

		c.add(new JLabel("Base Unit:"), cc.xy(2, 2));
		c.add(textBaseUnit = new JTextField(), cc.xy(4, 2));
		c.add(btnTakeBU = new JButton("Take"), cc.xy(6, 2));
		btnTakeBU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				afTake.run(null);
			}
		});
		c.add(new JLabel("Domain:"), cc.xy(2, 4));
		c.add(comboDomain = UnitsUiUtil.createDomainsComboBox(um), cc.xy(4, 4));
		c.add(new JLabel("Factor:"), cc.xy(2, 6));
		c.add(comboFactors = UnitsUiUtil.createFactorsComboBox(um), cc.xy(4, 6));

		JPanel btnPanel = new JPanel();
		c.add(btnPanel, cc.xywh(2, 8, 3, 1));

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
			Object [] param = new Object[3];
			int sel = comboDomain.getSelectedIndex();
			param[0] = um.getDomains().get(sel);
			int fs = comboFactors.getSelectedIndex();
			param[1] = um.getFactors().get(fs);
			param[2] = baseUnit;
			if (!af.run(param)) return;
			setResultCode(RESULT_OK);
			// setVisible(false);
		} else if (cmd.equals(CMD_CANCEL)) {
			setResultCode(RESULT_CANCEL);
			setVisible(false);
		}
	}

	/**
	 * @param unit
	 */
	public void editUnit(FactorUnit unit) {
		setEditMode(true);
		editUnit = unit;
		setBaseUnit(unit.getBaseUnit());
		int dsel = um.getDomains().indexOf(unit.getDomain());
		comboDomain.setSelectedIndex(dsel);
		int fsel = um.getFactors().indexOf(unit.getFactor());
		comboFactors.setSelectedIndex(fsel);
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
		if (!editMode)
			editUnit = null;
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
	public FactorUnit getEditUnit() {
		return editUnit;
	}
}
