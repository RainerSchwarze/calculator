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
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.ui.util.ListElementAccessor;
import de.admadic.ui.util.ListRefListModel;
import de.admadic.units.core.Domain;
import de.admadic.units.core.IConverter;
import de.admadic.units.core.IUnit;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class ConvertPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	UnitManager um;

	JLabel labelDomains;
	JList listDomains;
	JScrollPane scrollDomains;
	ListRefListModel listModelDomains;
	ListElementAccessor listAccessorDomains;
	Vector<Domain> dataDomains;

	JList listSourceUnit;
	JList listDestinationUnit;
	ListRefListModel listModelUnits;
	ListElementAccessor listAccessorUnits;
	JScrollPane scrollSourceUnit;
	JScrollPane scrollDestinationUnit;
	Vector<IUnit> dataUnits;
	
	JLabel labelSource;
	JLabel labelDestination;
	JComboBox comboSource;
	JComboBox comboDestination;
	JButton btnConvert;
	JButton btnIConvert;
	JLabel labelUnitSource;
	JLabel labelUnitDestination;
	JLabel labelNameSource;
	JLabel labelNameDestination;

	IUnit unitSource;
	IUnit unitDestination;

	/**
	 * @param um 
	 * 
	 */
	public ConvertPanel(UnitManager um) {
		super();
		this.um = um;
		initContents();
	}

	/**
	 * 
	 */
	public void initContents() {
		FormLayout fl = new FormLayout(
				"12px, p:grow(0.5), 5px, p, 12px, p, 5px, p:grow(0.5), 12px",
				"12px, p, 5px, p:grow(0.5), 5px, p:grow(0.5), 12px, p, 5px, p, 5px, p, 5px, p, 12px");
		this.setLayout(fl);
		CellConstraints cc = new CellConstraints();

		labelDomains = new JLabel("Domains:");
		listDomains = new JList();
		scrollDomains = new JScrollPane(
				listDomains,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		listSourceUnit = new JList();
		listDestinationUnit = new JList();
		scrollSourceUnit = new JScrollPane(
				listSourceUnit,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollDestinationUnit = new JScrollPane(
				listDestinationUnit,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		labelSource = new JLabel("Source:");
		labelDestination = new JLabel("Destination:");
		comboSource = new JComboBox();
		comboDestination = new JComboBox();
		labelUnitSource = new JLabel("(unit)");
		labelUnitDestination = new JLabel("(unit)");
		labelNameSource = new JLabel("(name)");
		labelNameDestination = new JLabel("(name)");

		btnConvert = new JButton("=>");
		btnIConvert = new JButton("<=");
		
		this.add(labelDomains, cc.xywh(2, 2, 7, 1));
		this.add(scrollDomains, cc.xywh(2, 4, 7, 1));

		this.add(scrollSourceUnit, cc.xywh(2, 6, 3, 1));
		this.add(scrollDestinationUnit, cc.xywh(6, 6, 3, 1));
		
		this.add(labelSource, cc.xy(2, 8));
		this.add(comboSource, cc.xy(2, 10));
		this.add(labelUnitSource, cc.xy(2, 12));
		this.add(labelNameSource, cc.xy(2, 14));

		this.add(btnConvert, cc.xy(4, 10));
		this.add(btnIConvert, cc.xy(6, 10));
		
		this.add(labelDestination, cc.xy(8, 8));
		this.add(comboDestination, cc.xy(8, 10));
		this.add(labelUnitDestination, cc.xy(8, 12));
		this.add(labelNameDestination, cc.xy(8, 14));

		dataDomains = um.getDomains();
		listModelDomains = new ListRefListModel();
		listModelDomains.setData(dataDomains);
		listAccessorDomains = new ListElementAccessor() {
			public Object getElement(Object el) {
				return ((Domain)el).getName();
			}
		};
		listDomains.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				updateUnitLists();
			}
		});
		listModelDomains.setElementAccessor(listAccessorDomains);
		listDomains.setModel(listModelDomains);
		listDomains.setLayoutOrientation(JList.VERTICAL_WRAP);

		dataUnits = um.getUnits();
		listModelUnits = new ListRefListModel();
		listModelUnits.setData(dataUnits);
		listAccessorUnits = new ListElementAccessor() {
			public Object getElement(Object el) {
				return 
					((IUnit)el).getSymbol() + " / " + 
					((IUnit)el).getName(); 
			}
		};
		listModelUnits.setElementAccessor(listAccessorUnits);
		listSourceUnit.setModel(listModelUnits);
		listDestinationUnit.setModel(listModelUnits);
		listSourceUnit.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				updateUnitSource();
				updateCalculation();
			}
		});
		listDestinationUnit.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				updateUnitDestination();
				updateCalculation();
			}
		});

		comboSource.setEditable(true);
		comboDestination.setEditable(true);
		comboSource.getEditor().setItem("1.0");

		btnConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCalculation();
			}
		});
		btnIConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateICalculation();
			}
		});
	}

	protected void updateUnitLists() {
		int sel = listDomains.getSelectedIndex();
		if (sel<0) {
			dataUnits.clear();
		} else {
			Domain dom = dataDomains.get(sel);
			dataUnits = um.getUnitsForDomain(dom, dataUnits);
			// listModelUnits.setData(dataUnits);
		}
		listModelUnits.fireContentsChanged();
	}

	protected void updateUnitSource() {
		int sel = listSourceUnit.getSelectedIndex();
		if (sel<0) return;
		unitSource = dataUnits.get(sel);
		if (unitSource!=null) {
			labelUnitSource.setText(unitSource.getSymbol());
			labelNameSource.setText(unitSource.getName());
		} else {
			labelUnitSource.setText("-");
			labelNameSource.setText("-");
		}
	}

	protected void updateUnitDestination() {
		int sel = listDestinationUnit.getSelectedIndex();
		if (sel<0) return;
		unitDestination = dataUnits.get(sel);
		if (unitDestination!=null) {
			labelUnitDestination.setText(unitDestination.getSymbol());
			labelNameDestination.setText(unitDestination.getName());
		} else {
			labelUnitDestination.setText("-");
			labelNameDestination.setText("-");
		}
	}

	protected void updateCalculation() {
		if (unitSource==null) return;
		if (unitDestination==null) return;

		IConverter srcConv = unitSource.getRootConverter();
		IConverter dstConv = unitDestination.getRootConverter();

		Double v = Double.valueOf((String)(comboSource.getEditor().getItem()));
		v = srcConv.convert(v);
		v = dstConv.iconvert(v);
		comboDestination.getEditor().setItem(v.toString());
	}

	protected void updateICalculation() {
		if (unitSource==null) return;
		if (unitDestination==null) return;

		IConverter srcConv = unitSource.getRootConverter();
		IConverter dstConv = unitDestination.getRootConverter();

		Double v = Double.valueOf((String)(comboSource.getEditor().getItem()));
		v = srcConv.convert(v);
		v = dstConv.iconvert(v);
		comboDestination.getEditor().setItem(v.toString());
	}
}
