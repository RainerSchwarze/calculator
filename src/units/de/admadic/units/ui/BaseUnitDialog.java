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
import de.admadic.units.core.BaseUnit;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class BaseUnitDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTextField textId;
	JTextField textSymbol;
	JTextField textName;
	JComboBox comboDomain;

	JButton btnOK;
	JButton btnCancel;
	
	final static String CMD_OK = "ok";
	final static String CMD_CANCEL = "cancel";
	
	ActionFunction af;
	UnitManager um;

	BaseUnit editUnit;
	boolean editMode;
	
	/**
	 * @param arg0
	 * @param um 
	 * @param af 
	 * @throws HeadlessException
	 */
	public BaseUnitDialog(Frame arg0, UnitManager um, ActionFunction af) throws HeadlessException {
		super(arg0);
		this.um = um;
		this.af = af;
		initContents();
	}

	/**
	 * @param arg0
	 * @param af 
	 * @throws HeadlessException
	 */
	public BaseUnitDialog(java.awt.Dialog arg0, ActionFunction af) throws HeadlessException {
		super(arg0);
		this.af = af;
		initContents();
	}


	protected void initContents() {
		FormLayout fl = new FormLayout(
				"12px, p, 5px, p:grow, 12px",
				"12px, p, 5px, p, 5px, p, 5px, p, 12px, p, 12px");
		CellConstraints cc = new CellConstraints();

		Container c = this.getContentPane();
		c.setLayout(fl);

		setTitle("New BaseUnit");
		
		c.add(new JLabel("Id:"), cc.xy(2, 2));
		c.add(textId = new JTextField(), cc.xy(4, 2));
		c.add(new JLabel("Symbol:"), cc.xy(2, 4));
		c.add(textSymbol = new JTextField(), cc.xy(4, 4));
		c.add(new JLabel("Name:"), cc.xy(2, 6));
		c.add(textName = new JTextField(), cc.xy(4, 6));
		c.add(new JLabel("Domain:"), cc.xy(2, 8));
		c.add(comboDomain = UnitsUiUtil.createDomainsComboBox(um), cc.xy(4, 8));
		
		JPanel btnPanel = new JPanel();
		c.add(btnPanel, cc.xywh(2, 10, 3, 1));

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
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_OK)) {
			Object [] param = new Object[4];
			param[0] = textId.getText();
			param[1] = textSymbol.getText();
			param[2] = textName.getText();
			int sel = comboDomain.getSelectedIndex();
			param[3] = um.getDomains().get(sel).getId();
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
	public void editUnit(BaseUnit unit) {
		setEditMode(true);
		editUnit = unit;
		textId.setText(unit.getId());
		textSymbol.setText(unit.getSymbol());
		textName.setText(unit.getName());
		int sel = um.getDomains().indexOf(unit.getDomain());
		comboDomain.setSelectedIndex(sel);
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
	public BaseUnit getEditUnit() {
		return editUnit;
	}

}
