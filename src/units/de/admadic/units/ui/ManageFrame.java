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

import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.units.core.Domain;
import de.admadic.units.core.IField;
import de.admadic.units.core.IUnit;
import de.admadic.units.core.SubField;
import de.admadic.units.core.SystemOfUnits;
import de.admadic.units.core.UnitContext;
import de.admadic.units.core.UnitFilter;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class ManageFrame extends JFrame implements UnitsCmdItf {
	UnitManager unitManager;
	UnitsActions ua;
	UnitFilter unitFilter;
	JPanel panelMain;

	FieldsPanel fieldsFilterPanel;
	SoUPanel sousFilterPanel;
	DomainsPanel domainsFilterPanel;

	JPanel setPanels;
	JPanel filterPanels;
	
	FieldsPanel fieldsSetPanel;
	SoUPanel sousSetPanel;
	DomainsPanel domainsSetPanel;

	UnitsTablePanel unitsPanel;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param unitManager 
	 * @throws HeadlessException
	 */
	public ManageFrame(UnitManager unitManager) throws HeadlessException {
		super("Unit Management");
		this.unitManager = unitManager;
		this.ua = new UnitsActions(this);
		unitFilter = new UnitFilter(this.unitManager);
		initContents();
	}

	/**
	 * 
	 */
	public void initContents() {
		panelMain = (JPanel)this.getContentPane();
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			/**
			 * @param e
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				setVisible(false);
				dispose();
			}
		});
		FormLayout fl = new FormLayout(
				"12px, p:grow, 5px, p, 12px",
				"12px, p, 5px, p:grow, 12px");
		CellConstraints cc = new CellConstraints();

		panelMain.setLayout(fl);

		filterPanels = new JPanel();
		panelMain.add(filterPanels, cc.xy(2, 2));
		{
			FormLayout fl2 = new FormLayout(
					"0px, p, 5px, p, 5px, p, 0px",
					"0px, p, 0px");
			CellConstraints cc2 = new CellConstraints();
			filterPanels.setLayout(fl2);
			filterPanels.add(
					fieldsFilterPanel = new FieldsPanel(unitManager, ua, true, false), 
					cc2.xy(2, 2));
			filterPanels.add(
					sousFilterPanel = new SoUPanel(unitManager, ua, true, false), 
					cc2.xy(4, 2));
			filterPanels.add(
					domainsFilterPanel = new DomainsPanel(unitManager, ua, true, false), 
					cc2.xy(6, 2));
		}
		panelMain.add(
				unitsPanel = new UnitsTablePanel(unitManager, unitFilter, this), 
				cc.xy(2, 4, CellConstraints.FILL, CellConstraints.FILL));

		setPanels = new JPanel();
		panelMain.add(setPanels, cc.xy(4, 4));
		{
			FormLayout fl2 = new FormLayout(
					"0px, p, 0px",
					"0px, p, 5px, p, 5px, p, 0px");
			CellConstraints cc2 = new CellConstraints();
			setPanels.setLayout(fl2);
			setPanels.add(
					fieldsSetPanel = new FieldsPanel(unitManager, ua, false, true), 
					cc2.xy(2, 2));
			setPanels.add(
					sousSetPanel = new SoUPanel(unitManager, ua, false, true), 
					cc2.xy(2, 4));
			setPanels.add(
					domainsSetPanel = new DomainsPanel(unitManager, ua, false, true), 
					cc2.xy(2, 6));
		}
//		convertPanel = new ConvertPanel(unitManager);
//		panelMain.add(convertPanel, BorderLayout.CENTER);
		
		this.pack();
		setLocationRelativeTo(null);
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doFilterFieldsSet()
	 */
	public void doFilterFieldsSet() {
		IField [] fa = fieldsFilterPanel.getSelectedFields();
		unitFilter.clearFields();
		for (IField f : fa) {
			if (f instanceof SubField) {
				unitFilter.addField(f);
			}
		}
		unitsPanel.updateFilter();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doFilterFieldsClear()
	 */
	public void doFilterFieldsClear() {
		unitFilter.clearFields();
		unitsPanel.updateFilter();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doFilterFieldsAdd()
	 */
	public void doFilterFieldsAdd() {
		IField [] fa = fieldsFilterPanel.getSelectedFields();
		for (IField f : fa) {
			if (f instanceof SubField) {
				unitFilter.addField(f);
			}
		}
		unitsPanel.updateFilter();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doFilterFieldsRemove()
	 */
	public void doFilterFieldsRemove() {
		IField [] fa = fieldsFilterPanel.getSelectedFields();
		unitFilter.clearFields();
		for (IField f : fa) {
			if (f instanceof SubField) {
				unitFilter.removeField(f);
			}
		}
		unitsPanel.updateFilter();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doFilterSousSet()
	 */
	public void doFilterSousSet() {
		SystemOfUnits [] sa = sousFilterPanel.getSelectedSous();
		unitFilter.clearSystemOfUnits();
		for (SystemOfUnits sou : sa) {
			unitFilter.addSystemOfUnits(sou);
		}
		unitsPanel.updateFilter();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doFilterSousClear()
	 */
	public void doFilterSousClear() {
		unitFilter.clearSystemOfUnits();
		unitsPanel.updateFilter();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doFilterSousAdd()
	 */
	public void doFilterSousAdd() {
		SystemOfUnits [] sa = sousFilterPanel.getSelectedSous();
		for (SystemOfUnits sou : sa) {
			unitFilter.addSystemOfUnits(sou);
		}
		unitsPanel.updateFilter();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doFilterSousRemove()
	 */
	public void doFilterSousRemove() {
		SystemOfUnits [] sa = sousFilterPanel.getSelectedSous();
		for (SystemOfUnits sou : sa) {
			unitFilter.removeSystemOfUnits(sou);
		}
		unitsPanel.updateFilter();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doFilterDomainsSet()
	 */
	public void doFilterDomainsSet() {
		Domain [] da = domainsFilterPanel.getSelectedDomains();
		unitFilter.clearDomains();
		for (Domain d : da) {
			unitFilter.addDomain(d);
		}
		unitsPanel.updateFilter();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doFilterDomainsClear()
	 */
	public void doFilterDomainsClear() {
		unitFilter.clearDomains();
		unitsPanel.updateFilter();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doFilterDomainsAdd()
	 */
	public void doFilterDomainsAdd() {
		Domain [] da = domainsFilterPanel.getSelectedDomains();
		for (Domain d : da) {
			unitFilter.addDomain(d);
		}
		unitsPanel.updateFilter();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doFilterDomainsRemove()
	 */
	public void doFilterDomainsRemove() {
		Domain [] da = domainsFilterPanel.getSelectedDomains();
		for (Domain d : da) {
			unitFilter.removeDomain(d);
		}
		unitsPanel.updateFilter();
	}


	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doSetFieldsSet()
	 */
	public void doSetFieldsSet() {
		// get selected fields
		// set them to all selected units
		IField [] fa = fieldsSetPanel.getSelectedFields();
		IUnit [] ua = unitsPanel.getSelectedUnits();

		for (IUnit u : ua) {
			UnitContext uc = u.getContext();
			uc.clearFields();
			for (IField f : fa) {
				if (f instanceof SubField) {
					uc.addSubField((SubField)f);
				}
			}
			u.touchLastChange();
		}
		unitsPanel.fireUnitsTableChanged();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doSetFieldsClear()
	 */
	public void doSetFieldsClear() {
		// get selected fields
		// set them to all selected units
		IUnit [] ua = unitsPanel.getSelectedUnits();

		for (IUnit u : ua) {
			UnitContext uc = u.getContext();
			uc.clearFields();
			u.touchLastChange();
		}
		unitsPanel.fireUnitsTableChanged();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doSetFieldsAdd()
	 */
	public void doSetFieldsAdd() {
		// get selected fields
		// set them to all selected units
		IField [] fa = fieldsSetPanel.getSelectedFields();
		IUnit [] ua = unitsPanel.getSelectedUnits();

		for (IUnit u : ua) {
			UnitContext uc = u.getContext();
			for (IField f : fa) {
				if (f instanceof SubField) {
					uc.addSubField((SubField)f);
				}
			}
			u.touchLastChange();
		}
		unitsPanel.fireUnitsTableChanged();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doSetFieldsRemove()
	 */
	public void doSetFieldsRemove() {
		// get selected fields
		// set them to all selected units
		IField [] fa = fieldsSetPanel.getSelectedFields();
		IUnit [] ua = unitsPanel.getSelectedUnits();

		for (IUnit u : ua) {
			UnitContext uc = u.getContext();
			for (IField f : fa) {
				if (f instanceof SubField) {
					uc.removeSubField((SubField)f);
				}
			}
			u.touchLastChange();
		}
		unitsPanel.fireUnitsTableChanged();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doSetSousSet()
	 */
	public void doSetSousSet() {
		// get selected fields
		// set them to all selected units
		SystemOfUnits [] sa = sousSetPanel.getSelectedSous();
		IUnit [] ua = unitsPanel.getSelectedUnits();

		for (IUnit u : ua) {
			UnitContext uc = u.getContext();
			uc.clearSous();
			for (SystemOfUnits s : sa) {
				uc.addSystemOfUnits(s);
			}
			u.touchLastChange();
		}
		unitsPanel.fireUnitsTableChanged();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doSetSousClear()
	 */
	public void doSetSousClear() {
		// get selected fields
		// set them to all selected units
		IUnit [] ua = unitsPanel.getSelectedUnits();

		for (IUnit u : ua) {
			UnitContext uc = u.getContext();
			uc.clearSous();
			u.touchLastChange();
		}
		unitsPanel.fireUnitsTableChanged();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doSetSousAdd()
	 */
	public void doSetSousAdd() {
		// get selected fields
		// set them to all selected units
		SystemOfUnits [] sa = sousSetPanel.getSelectedSous();
		IUnit [] ua = unitsPanel.getSelectedUnits();

		for (IUnit u : ua) {
			UnitContext uc = u.getContext();
			for (SystemOfUnits s : sa) {
				uc.addSystemOfUnits(s);
			}
			u.touchLastChange();
		}
		unitsPanel.fireUnitsTableChanged();
	}

	/**
	 * 
	 * @see de.admadic.units.ui.UnitsCmdItf#doSetSousRemove()
	 */
	public void doSetSousRemove() {
		// get selected fields
		// set them to all selected units
		SystemOfUnits [] sa = sousSetPanel.getSelectedSous();
		IUnit [] ua = unitsPanel.getSelectedUnits();

		for (IUnit u : ua) {
			UnitContext uc = u.getContext();
			for (SystemOfUnits s : sa) {
				uc.removeSystemOfUnits(s);
			}
			u.touchLastChange();
		}
		unitsPanel.fireUnitsTableChanged();
	}

	public void doSetDomainsSet() {
		// TODO Auto-generated method stub
		
	}

	public void doSetDomainsClear() {
		// TODO Auto-generated method stub
		
	}

	public void doSetDomainsAdd() {
		// TODO Auto-generated method stub
		
	}

	public void doSetDomainsRemove() {
		// TODO Auto-generated method stub
		
	}

}
