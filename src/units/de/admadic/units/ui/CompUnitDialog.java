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
import java.util.Collections;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.ui.util.Dialog;
import de.admadic.ui.util.ListElementAccessor;
import de.admadic.ui.util.ListRefListModel;
import de.admadic.units.core.CompositeUnit;
import de.admadic.units.core.Domain;
import de.admadic.units.core.IUnit;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class CompUnitDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JComboBox comboDomain;
	JTextField textCurDisplay;

	JList listMulUnits;
	JScrollPane scrollMulUnits;
	JList listDivUnits;
	JScrollPane scrollDivUnits;
	ListRefListModel lmMul;
	ListRefListModel lmDiv;
	Vector<IUnit> vecMuls;
	Vector<IUnit> vecDivs;

	JButton btnOK;
	JButton btnCancel;
	
	CompositeUnit editUnit;
	boolean editMode;
	
	final static String CMD_OK = "ok";
	final static String CMD_CANCEL = "cancel";

	final static String CMD_TAKE_PREINIT = "cmd.take.preinit";
	final static String CMD_TAKE_MUL = "cmd.take.mul";
	final static String CMD_REMOVE_MUL = "cmd.rem.mul";
	final static String CMD_UP_MUL = "cmd.up.mul";
	final static String CMD_DOWN_MUL = "cmd.down.mul";
	final static String CMD_TAKE_DIV = "cmd.take.div";
	final static String CMD_REMOVE_DIV = "cmd.rem.div";
	final static String CMD_UP_DIV = "cmd.up.div";
	final static String CMD_DOWN_DIV = "cmd.down.div";

	ActionFunction afCreate;
	ActionFunction afTakePreinit;
	ActionFunction afTakeMul;
	ActionFunction afTakeDiv;
	UnitManager um;
	
	/**
	 * @param arg0
	 * @param um 
	 * @throws HeadlessException
	 */
	public CompUnitDialog(Frame arg0, UnitManager um) throws HeadlessException {
		super(arg0);
		this.um = um;
		initContents();
	}

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public CompUnitDialog(java.awt.Dialog arg0) throws HeadlessException {
		super(arg0);
		initContents();
	}

	/**
	 * @param afCreate 
	 * @param afPreinit
	 * @param afMul
	 * @param afDiv
	 */
	public void setActions(
			ActionFunction afCreate,
			ActionFunction afPreinit,
			ActionFunction afMul,
			ActionFunction afDiv
			) {
		this.afCreate = afCreate;
		this.afTakePreinit = afPreinit;
		this.afTakeMul = afMul;
		this.afTakeDiv = afDiv;
	}

	protected void initContents() {
		FormLayout fl = new FormLayout(
				"12px, p, 5px, p:grow, 5px, p, 12px",
				"12px, p, 5px, p, 5px, p, 5px, p, 12px, p, 12px");
		CellConstraints cc = new CellConstraints();

		vecMuls = new Vector<IUnit>();
		vecDivs = new Vector<IUnit>();

		Container c = this.getContentPane();
		c.setLayout(fl);

		setTitle("New Composite Unit");
		
		c.add(new JLabel("Domain:"), cc.xy(2, 2));
		c.add(comboDomain = UnitsUiUtil.createDomainsComboBox(um), cc.xy(4, 2));
		c.add(new JLabel("Display:"), cc.xy(2, 4));
		c.add(textCurDisplay = new JTextField(), cc.xy(4, 4));

		listMulUnits = new JList();
		scrollMulUnits = new JScrollPane(
				listMulUnits,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listMulUnits.setVisibleRowCount(4);
		listDivUnits = new JList();
		scrollDivUnits = new JScrollPane(
				listDivUnits,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listDivUnits.setVisibleRowCount(4);

		lmMul = new ListRefListModel();
		lmMul.setData(vecMuls);
		lmMul.setElementAccessor(new ListElementAccessor(){
			public Object getElement(Object el) {
				IUnit u = (IUnit)el;
				return u.getSymbol() + " - " + u.getName();
			}
		});
		listMulUnits.setModel(lmMul);

		lmDiv = new ListRefListModel();
		lmDiv.setData(vecDivs);
		lmDiv.setElementAccessor(new ListElementAccessor(){
			public Object getElement(Object el) {
				IUnit u = (IUnit)el;
				return u.getSymbol() + " - " + u.getName();
			}
		});
		listDivUnits.setModel(lmDiv);

		c.add(new JLabel("Multiply:"), cc.xy(2, 6));
		c.add(scrollMulUnits, cc.xy(4, 6));
		c.add(new JLabel("Divide:"), cc.xy(2, 8));
		c.add(scrollDivUnits, cc.xy(4, 8));
	
		{
			JButton bt = new JButton("Take Pre-Init");
			bt.setActionCommand(CMD_TAKE_PREINIT);
			bt.addActionListener(this);
			this.add(bt, cc.xy(6, 4));
		}
		{
			JPanel btnPanel = new JPanel();
			this.add(btnPanel, cc.xy(6, 6));
			btnPanel.setLayout(new FormLayout(
					"0px, p, 5px, p, 0px",
					"0px, p, 5px, p, 0px"));
			CellConstraints cc2 = new CellConstraints();
			JButton bt;
			bt = new JButton("Take");
			bt.setActionCommand(CMD_TAKE_MUL);
			bt.addActionListener(this);
			btnPanel.add(bt, cc2.xy(2, 2));
			bt = new JButton("Remove");
			bt.setActionCommand(CMD_REMOVE_MUL);
			bt.addActionListener(this);
			btnPanel.add(bt, cc2.xy(2, 4));
			bt = new JButton("Up");
			bt.setActionCommand(CMD_UP_MUL);
			bt.addActionListener(this);
			btnPanel.add(bt, cc2.xy(4, 2));
			bt = new JButton("Down");
			bt.setActionCommand(CMD_DOWN_MUL);
			bt.addActionListener(this);
			btnPanel.add(bt, cc2.xy(4, 4));
		}
		{
			JPanel btnPanel = new JPanel();
			this.add(btnPanel, cc.xy(6, 8));
			btnPanel.setLayout(new FormLayout(
					"0px, p, 5px, p, 0px",
					"0px, p, 5px, p, 0px"));
			CellConstraints cc2 = new CellConstraints();
			JButton bt;
			bt = new JButton("Take");
			bt.setActionCommand(CMD_TAKE_DIV);
			bt.addActionListener(this);
			btnPanel.add(bt, cc2.xy(2, 2));
			bt = new JButton("Remove");
			bt.setActionCommand(CMD_REMOVE_DIV);
			bt.addActionListener(this);
			btnPanel.add(bt, cc2.xy(2, 4));
			bt = new JButton("Up");
			bt.setActionCommand(CMD_UP_DIV);
			bt.addActionListener(this);
			btnPanel.add(bt, cc2.xy(4, 2));
			bt = new JButton("Down");
			bt.setActionCommand(CMD_DOWN_DIV);
			bt.addActionListener(this);
			btnPanel.add(bt, cc2.xy(4, 4));
		}
		
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
	 * @param u
	 */
	public void addMulUnit(IUnit u) {
		vecMuls.add(u);
		lmMul.fireContentsChanged();
	}

	/**
	 * @param u
	 */
	public void addDivUnit(IUnit u) {
		vecDivs.add(u);
		lmDiv.fireContentsChanged();
	}

	/**
	 * @param d
	 */
	public void setDomain(Domain d) {
		int sel = um.getDomains().indexOf(d);
		comboDomain.setSelectedIndex(sel);
	}

	/**
	 * @param u
	 */
	public void setPreInit(IUnit u) {
		if (!(u instanceof CompositeUnit)) return;
		CompositeUnit cu = (CompositeUnit)u;
		Domain d = cu.getDomain();
		setDomain(d);
		vecMuls.addAll(cu.getUnits(CompositeUnit.MUL));
		vecDivs.addAll(cu.getUnits(CompositeUnit.DIV));
		lmMul.fireContentsChanged();
		lmDiv.fireContentsChanged();
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
			param[0] = um.getDomains().get(sel).getId();
			param[1] = vecMuls;
			param[2] = vecDivs;
			if (!afCreate.run(param)) return;
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
		} else if (cmd.equals(CMD_TAKE_PREINIT)) {
			afTakePreinit.run(null);
		} else if (cmd.equals(CMD_TAKE_MUL)) {
			afTakeMul.run(null);
		} else if (cmd.equals(CMD_TAKE_DIV)) {
			afTakeDiv.run(null);
		} else if (cmd.equals(CMD_REMOVE_MUL)) {
			int [] sel = listMulUnits.getSelectedIndices();
			for (int i=sel.length-1; i>=0; i--) {
				vecMuls.remove(sel[i]);
			}
			lmMul.fireContentsChanged();
		} else if (cmd.equals(CMD_REMOVE_DIV)) {
			int [] sel = listDivUnits.getSelectedIndices();
			for (int i=sel.length-1; i>=0; i--) {
				vecDivs.remove(sel[i]);
			}
			lmDiv.fireContentsChanged();
		} else if (cmd.equals(CMD_UP_MUL)) {
			int sel = listMulUnits.getSelectedIndex();
			if (sel>0) {
				Collections.swap(vecMuls, sel, sel-1);
				listMulUnits.setSelectedIndex(sel-1);
			}
		} else if (cmd.equals(CMD_UP_DIV)) {
			int sel = listDivUnits.getSelectedIndex();
			if (sel>0) {
				Collections.swap(vecDivs, sel, sel-1);
				listDivUnits.setSelectedIndex(sel-1);
			}
		} else if (cmd.equals(CMD_DOWN_MUL)) {
			int sel = listMulUnits.getSelectedIndex();
			if (sel<(vecMuls.size()-1)) {
				Collections.swap(vecMuls, sel, sel+1);
				listMulUnits.setSelectedIndex(sel+1);
			}
		} else if (cmd.equals(CMD_DOWN_DIV)) {
			int sel = listDivUnits.getSelectedIndex();
			if (sel<(vecDivs.size()-1)) {
				Collections.swap(vecDivs, sel, sel+1);
				listDivUnits.setSelectedIndex(sel+1);
			}
		}
	}

	protected void clearData() {
		vecMuls.clear();
		vecDivs.clear();
		lmMul.fireContentsChanged();
		lmDiv.fireContentsChanged();
	}

	/**
	 * @param unit
	 */
	public void editUnit(CompositeUnit unit) {
		setEditMode(true);
		editUnit = unit;
		setPreInit(unit);
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
		if (!editMode) {
			editUnit = null;
		}
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
	public CompositeUnit getEditUnit() {
		return editUnit;
	}

}
