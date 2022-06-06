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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.ui.util.ColumnAccessor;
import de.admadic.ui.util.ListRefTableModel;
import de.admadic.units.core.BaseUnit;
import de.admadic.units.core.CompositeUnit;
import de.admadic.units.core.Domain;
import de.admadic.units.core.Factor;
import de.admadic.units.core.FactorUnit;
import de.admadic.units.core.IConverter;
import de.admadic.units.core.IUnit;
import de.admadic.units.core.SubstUnit;
import de.admadic.units.core.UnitFactory;
import de.admadic.units.core.UnitFilter;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class UnitsTablePanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	UnitManager um;
	UnitFilter uf;
	JFrame owner;

	JTable tableUnits;
	JScrollPane scrollUnits;
	ListRefTableModel tableModelUnits;
	ColumnAccessor tableAccessorUnits;
	Vector<IUnit> dataUnits;

	final static String CMD_FILTER = "cmd.filter";
	final static String CMD_NEW_BASE = "cmd.new.base";
	final static String CMD_NEW_FACT = "cmd.new.fact";
	final static String CMD_NEW_SUBS = "cmd.new.subs";
	final static String CMD_NEW_COMP = "cmd.new.comp";
	final static String CMD_EDIT = "cmd.edit";

	boolean filterOn;

	BaseUnitDialog dialogBaseUnit;
	FactorUnitDialog dialogFactorUnit;
	SubstUnitDialog dialogSubstUnit;
	CompUnitDialog dialogCompUnit;

	/**
	 * @param um 
	 * @param uf 
	 * @param owner 
	 * 
	 */
	public UnitsTablePanel(UnitManager um, UnitFilter uf, JFrame owner) {
		super();
		this.owner = owner;
		this.um = um;
		this.uf = uf;
		dataUnits = new Vector<IUnit>();
		initContents();
	}

	/**
	 * 
	 */
	public void initContents() {
		FormLayout fl = new FormLayout(
				"12px, p:grow, 12px", 
				"12px, p, 5px, p:grow, 12px");
		CellConstraints cc = new CellConstraints();
		this.setLayout(fl);
		// this.setBorder(BorderFactory.createLineBorder(Color.RED));

		tableUnits = new JTable();
		scrollUnits = new JScrollPane(
				tableUnits,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(scrollUnits, cc.xy(2, 4, CellConstraints.FILL, CellConstraints.FILL));
		tableUnits.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		dataUnits.addAll(um.getUnits());
		tableModelUnits = new ListRefTableModel();
		tableModelUnits.setData(dataUnits);
		tableModelUnits.setColumns(new String[]{
				"id", "type", "factor", "symbol", "name", "domain", "lastchg", "SoU", "Fields"
		});
		tableAccessorUnits = new ColumnAccessor() {
			public void setValueAt(Object rowItem, Object value, int columnIndex) {
				// nothing
			}

			public Object getValueAt(Object rowItem, int columnIndex) {
				IUnit u = (IUnit)rowItem;
				switch (columnIndex) {
				case 0: return u.getId();
				case 1:
					String t = "";
					if (u instanceof BaseUnit) {
						t ="base";
					} else if (u instanceof SubstUnit) {
						t = "subs";
					} else if (u instanceof CompositeUnit) {
						t= "comp";
					} else if (u instanceof FactorUnit) {
						t= "fact";
					} else {
						t = "<?>";
					}
					return t;
					//break;
				case 2: return (u.getFactor()!=null) ? u.getFactor().getSymbol() : "-";
				case 3: return u.getSymbol();
				case 4: return u.getName();
				case 5: return (u.getDomain()!=null) ? u.getDomain().getName(): "-";
				case 6: 
					String t2 = "-";
					if (u.getLastChange()!=null) {
						t2 = DateFormat.getDateTimeInstance(
										DateFormat.SHORT, DateFormat.SHORT)
								.format(u.getLastChange());
					}
					return t2;
					// break;
				case 7: return u.getContext().getSousString(); 
				case 8: return u.getContext().getFieldsString(); 
				}
				return null;
			}

			public boolean isCellEditable(Object rowItem, int columnIndex) {
				return false;
			}

			public Class getColumnClass(int columnIndex) {
				return String.class;
			}
		};
		tableModelUnits.setColumnAccessor(tableAccessorUnits);
		tableUnits.setModel(tableModelUnits);
		tableUnits.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JPanel btnPanel = new JPanel();
		this.add(btnPanel, cc.xy(2, 2));
		{
			Vector<AbstractButton> bvec = new Vector<AbstractButton>();
			JToggleButton tb;
			JButton bt;

			tb = new JToggleButton("Filter");
			bvec.add(tb);
			tb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					doFilterOn(e.getStateChange()==ItemEvent.SELECTED);
				}
			});

			bt = new JButton("Edit");
			bvec.add(bt);
			bt.setActionCommand(CMD_EDIT);
			bt.addActionListener(this);

			bt = new JButton("New Base");
			bvec.add(bt);
			bt.setActionCommand(CMD_NEW_BASE);
			bt.addActionListener(this);

			bt = new JButton("New Fact");
			bvec.add(bt);
			bt.setActionCommand(CMD_NEW_FACT);
			bt.addActionListener(this);

			bt = new JButton("New Subs");
			bvec.add(bt);
			bt.setActionCommand(CMD_NEW_SUBS);
			bt.addActionListener(this);

			bt = new JButton("New Comp");
			bvec.add(bt);
			bt.setActionCommand(CMD_NEW_COMP);
			bt.addActionListener(this);

			UnitsUiUtil.createHorizontalButtonPanel(
					btnPanel, bvec.toArray(new AbstractButton[bvec.size()])
					);
		}
	}

	/**
	 * 
	 */
	public void updateFilter() {
		if (filterOn) doFilterOn(filterOn);
	}
	
	/**
	 * @param state
	 */
	protected void doFilterOn(boolean state) {
		filterOn = state;
		if (state) {
			dataUnits.clear();
			uf.populateUnits(dataUnits);
		} else {
			dataUnits.clear();
			dataUnits.addAll(um.getUnits());
		}
		tableModelUnits.fireTableDataChanged();
	}

	/**
	 * @return	Returns a list of selected units.
	 */
	public IUnit[] getSelectedUnits() {
		IUnit [] ua = null;
		int [] sel = tableUnits.getSelectedRows();
		ua = new IUnit[sel.length];
		for (int i = 0; i < sel.length; i++) {
			int j = sel[i];
			ua[i] = dataUnits.get(j);
		}
		return ua;
	}

	/**
	 * 
	 */
	public void fireUnitsTableChanged() {
		int [] sel = tableUnits.getSelectedRows();
		tableModelUnits.fireTableDataChanged();
		for (int i : sel) {
			tableUnits.addRowSelectionInterval(i, i);
		}
	}

	protected void initBaseUnitDialog() {
		dialogBaseUnit = new BaseUnitDialog(
				owner, um,
				new ActionFunction() {
					public boolean run(Object[] param) {
						return doNewBaseUnit(
								(String)param[0],
								(String)param[1],
								(String)param[2],
								(String)param[3]);
					}
			});
	}

	protected void initFactorUnitDialog() {
		dialogFactorUnit = new FactorUnitDialog(
				owner, um,
				new ActionFunction() {
					public boolean run(Object[] param) {
						return doNewFactorUnit(
								(Domain)param[0],	// dom
								(Factor)param[1],	// fac
								(IUnit)param[2]	// unt
								);
					}
				},
				new ActionFunction() {
					public boolean run(Object[] param) {
						IUnit u;
						int sel = tableUnits.getSelectedRow();
						if (sel<0) return false;
						u = dataUnits.get(sel);
						dialogFactorUnit.setBaseUnit(u);
						return true;
					}
				}
		);
	}

	protected void initSubstUnitDialog() {
		dialogSubstUnit = new SubstUnitDialog(
				owner, um,
				new ActionFunction() {
					public boolean run(Object[] param) {
						return doNewSubstUnit(
								(String)param[0],
								(String)param[1],
								(String)param[2],
								(String)param[3],
								(Factor)param[4],
								(IConverter)param[5],
								(IUnit)param[6]);
					}
				},
				new ActionFunction() {
					public boolean run(Object[] param) {
						IUnit u;
						int sel = tableUnits.getSelectedRow();
						if (sel<0) return false;
						u = dataUnits.get(sel);
						dialogSubstUnit.setBaseUnit(u);
						return true;
					}
				});
	}

	protected void initCompUnitDialog() {
		dialogCompUnit = new CompUnitDialog(
				owner, um);
		dialogCompUnit.setActions(
				new ActionFunction() {	// create
					public boolean run(Object[] param) {
						return doNewCompUnit(
								(String)param[0],
								(Vector<IUnit>)param[1],
								(Vector<IUnit>)param[2]);
					}
				},
				new ActionFunction() {	// take preinit
					public boolean run(Object[] param) {
						IUnit u;
						int sel = tableUnits.getSelectedRow();
						if (sel<0) return false;
						u = dataUnits.get(sel);
						dialogCompUnit.setPreInit(u);
						return true;
					}
				},
				new ActionFunction() {	// take mul
					public boolean run(Object[] param) {
						int [] sel = tableUnits.getSelectedRows();
						for (int i : sel) {
							IUnit u = dataUnits.get(i);
							dialogCompUnit.addMulUnit(u);
						}
						return true;
					}
				},
				new ActionFunction() {	// take div
					public boolean run(Object[] param) {
						int [] sel = tableUnits.getSelectedRows();
						for (int i : sel) {
							IUnit u = dataUnits.get(i);
							dialogCompUnit.addDivUnit(u);
						}
						return true;
					}
				}
		);
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_NEW_BASE)) {
			if (dialogBaseUnit==null) {
				initBaseUnitDialog();
			}
			dialogBaseUnit.setVisible(true);
		} else if (cmd.equals(CMD_NEW_FACT)) {
			if (dialogFactorUnit==null) {
				initFactorUnitDialog();
			}
			dialogFactorUnit.setVisible(true);
		} else if (cmd.equals(CMD_NEW_SUBS)) {
			if (dialogSubstUnit==null) {
				initSubstUnitDialog();
			}
			dialogSubstUnit.setVisible(true);
		} else if (cmd.equals(CMD_NEW_COMP)) {
			if (dialogCompUnit==null) {
				initCompUnitDialog();
			}
			dialogCompUnit.setVisible(true);
		} else if (cmd.equals(CMD_EDIT)) {
			doEditUnit();
		}
	}

	protected void doEditUnit() {
		int sel = tableUnits.getSelectedRow();
		if (sel<0) return;
		IUnit u = dataUnits.get(sel);

		if (u instanceof BaseUnit) {
			if (dialogBaseUnit==null) {
				initBaseUnitDialog();
			}
			dialogBaseUnit.editUnit((BaseUnit)u);
			dialogBaseUnit.setVisible(true);
		} else if (u instanceof FactorUnit) {
			if (dialogFactorUnit==null) {
				initFactorUnitDialog();
			}
			dialogFactorUnit.editUnit((FactorUnit)u);
			dialogFactorUnit.setVisible(true);
		} else if (u instanceof SubstUnit) {
			if (dialogSubstUnit==null) {
				initSubstUnitDialog();
			}
			dialogSubstUnit.editUnit((SubstUnit)u);
			dialogSubstUnit.setVisible(true);
		} else if (u instanceof CompositeUnit) {
			if (dialogCompUnit==null) {
				initCompUnitDialog();
			}
			dialogCompUnit.editUnit((CompositeUnit)u);
			dialogCompUnit.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(
					owner,
					"Unknown unit type: " + u.getClass().getSimpleName(),
					"Unknown unit type",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		// update domain tree?
	}


	/**
	 * 
	 * @param did
	 * @param vecMul
	 * @param vecDiv
	 * @return	Returns true, if ok.
	 */
	protected boolean doNewCompUnit(
			String did, Vector<IUnit> vecMul, Vector<IUnit> vecDiv) {
		boolean ok = true;
		CompositeUnit u;
		if (dialogCompUnit.isEditMode()) {
			u = dialogCompUnit.getEditUnit();
			Domain domain = um.getDomain(did);
			u.setDomain(domain);
			u.clearSubUnits();

			for (IUnit tu : vecMul) {
				u.appendUnit(tu, CompositeUnit.MUL);
			}
			for (IUnit tu : vecDiv) {
				u.appendUnit(tu, CompositeUnit.DIV);
			}
			
			tableModelUnits.fireTableDataChanged();
			dialogCompUnit.setEditMode(false);
		} else {
			u = UnitFactory.createCompUnit(um, did);
			for (IUnit tu : vecMul) {
				u.appendUnit(tu, CompositeUnit.MUL);
			}
			for (IUnit tu : vecDiv) {
				u.appendUnit(tu, CompositeUnit.DIV);
			}

			if (um.existsUnit(u.getId())) {
				JOptionPane.showMessageDialog(
						null, 
						"A unit with that id already exists.",
						"Invalid id",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			um.addUnit(u);
			if (filterOn && uf.unitMatchesFilter(u)) {
				dataUnits.add(u);
			} else {
				dataUnits.add(u);
			}
			tableModelUnits.fireTableRowsInserted(
					dataUnits.size()-1, dataUnits.size()-1);
		}
		u.touchLastChange();
		// doFilterOn();
		return ok;
	}

	/**
	 * 
	 * @param id
	 * @param symbol
	 * @param name
	 * @param did
	 * @param factor
	 * @param conv
	 * @param baseUnit 
	 * @return	Returns true, if ok.
	 */
	protected boolean doNewSubstUnit(
			String id, String symbol, String name, 
			String did, Factor factor, IConverter conv, IUnit baseUnit) {
		boolean ok = true;
		SubstUnit u;
		if (dialogSubstUnit.isEditMode()) {
			u = dialogSubstUnit.getEditUnit();
			Domain domain = um.getDomain(did);
			u.setDomain(domain);
			if (factor!=null) u.setFactor(factor);
			u.setBaseUnit(baseUnit);
			u.setSubstConverter(conv);

			tableModelUnits.fireTableDataChanged();
			dialogSubstUnit.setEditMode(false);
		} else {
			if (um.existsUnit(id)) {
				JOptionPane.showMessageDialog(
						null, 
						"A unit with that id already exists.",
						"Invalid id",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			u = UnitFactory.createSubstUnit(
					um, id, symbol, name, baseUnit.getId(), did);
			if (conv!=null) {
				u.setBaseConverter(conv);
			}
			if (factor!=null) {
				u.setFactor(factor);
			}

			um.addUnit(u);
			if (filterOn && uf.unitMatchesFilter(u)) {
				dataUnits.add(u);
			} else {
				dataUnits.add(u);
			}
			tableModelUnits.fireTableRowsInserted(
					dataUnits.size()-1, dataUnits.size()-1);
		}
		u.touchLastChange();
		// doFilterOn();
		return ok;
	}

	/**
	 * 
	 * @param domain 
	 * @param factor 
	 * @param baseUnit 
	 * @return	Returns true, if ok.
	 */
	protected boolean doNewFactorUnit(
			Domain domain, Factor factor, IUnit baseUnit) {
		boolean ok = true;
		FactorUnit fu;
		if (dialogFactorUnit.isEditMode()) {
			fu = dialogFactorUnit.getEditUnit();

			fu.setDomain(domain);
			fu.setFactor(factor);
			fu.setBaseUnit(baseUnit);
			
			tableModelUnits.fireTableDataChanged();
			dialogFactorUnit.setEditMode(false);
			um.updateDatabase(fu);
		} else {
			fu = UnitFactory.createFactorUnit(
					um, domain.getId(), baseUnit.getId(), factor.getId());
			if (um.existsUnit(fu.getId())) {
				JOptionPane.showMessageDialog(
						null, 
						"A unit with that id already exists.",
						"Invalid id",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			um.addUnit(fu);
			if (filterOn && uf.unitMatchesFilter(fu)) {
				dataUnits.add(fu);
			} else {
				dataUnits.add(fu);
			}
			tableModelUnits.fireTableRowsInserted(
					dataUnits.size()-1, dataUnits.size()-1);
		}
		fu.touchLastChange();
		return ok;
	}

	/**
	 * 
	 * @param id
	 * @param symbol
	 * @param name
	 * @param did 
	 * @return	Returns true, if ok.
	 */
	protected boolean doNewBaseUnit(
			String id, String symbol, String name, String did) {
		boolean ok = true;
		if (!dialogBaseUnit.isEditMode() && um.existsUnit(id)) {
			JOptionPane.showMessageDialog(
					null, 
					"A unit with that id already exists.",
					"Invalid id",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		BaseUnit u;
		if (dialogBaseUnit.isEditMode()) {
			u = dialogBaseUnit.getEditUnit();
			u.setDomain(um.getDomain(did));
			u.setId(id);
			u.setSymbol(symbol);
			u.setName(name);
			tableModelUnits.fireTableDataChanged();
			dialogBaseUnit.setEditMode(false);
			um.updateDatabase(u);
		} else {
			u = UnitFactory.createBaseUnit(um, id, symbol, name, did);
			um.addUnit(u);
			if (filterOn && uf.unitMatchesFilter(u)) {
				dataUnits.add(u);
			} else {
				dataUnits.add(u);
			}
			tableModelUnits.fireTableRowsInserted(
					dataUnits.size()-1, dataUnits.size()-1);
		}
		u.touchLastChange();
		// doFilterOn();
		return ok;
	}
}
