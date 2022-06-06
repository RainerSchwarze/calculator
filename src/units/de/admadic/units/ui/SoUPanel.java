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

import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.ui.util.ListElementAccessor;
import de.admadic.ui.util.ListRefListModel;
import de.admadic.units.core.SystemOfUnits;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class SoUPanel extends JPanel {
	UnitManager um;
	UnitsActions ua;
	boolean enaFil;
	boolean enaSet;

	JList listSous;
	JScrollPane scrollSous;
	ListRefListModel listModelSous;
	ListElementAccessor listAccessorSous;
	Vector<SystemOfUnits> dataSous;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param um 
	 * @param ua 
	 * @param enaFil 
	 * @param enaSet 
	 * 
	 */
	public SoUPanel(UnitManager um, UnitsActions ua, boolean enaFil, boolean enaSet) {
		super();
		this.enaFil = enaFil;
		this.enaSet = enaSet;
		this.um = um;
		this.ua = ua;
		initContents();
	}

	/**
	 * 
	 */
	public void initContents() {
		FormLayout fl = new FormLayout(
				"12px, p, 5px, p:grow, 12px", 
				"12px, p:grow, 5px, p, 12px");
		CellConstraints cc = new CellConstraints();
		this.setLayout(fl);

		listSous = new JList();
		scrollSous = new JScrollPane(
				listSous,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(scrollSous, cc.xy(4, 2));
		// listSous.setVisibleRowCount(8);
		scrollSous.setPreferredSize(new java.awt.Dimension(200, 150));

		dataSous = um.getSystemOfUnits();
		listModelSous = new ListRefListModel();
		listModelSous.setData(dataSous);
		listAccessorSous = new ListElementAccessor() {
			public Object getElement(Object el) {
				return ((SystemOfUnits)el).getSymbol();
			}
		};
		listModelSous.setElementAccessor(listAccessorSous);
		listSous.setModel(listModelSous);

		if (enaFil) {
			JPanel panFil = new JPanel();
			this.add(panFil, cc.xy(4, 4));
			UnitsUiUtil.createHorizontalButtonPanel(
				panFil,
				new AbstractButton[]{
					new JButton(ua.getAction(UnitsActions.FilterSousSetAction.class)),
					new JButton(ua.getAction(UnitsActions.FilterSousClearAction.class)),
					new JButton(ua.getAction(UnitsActions.FilterSousAddAction.class)),
					new JButton(ua.getAction(UnitsActions.FilterSousRemoveAction.class)),
				}
			);
		}

		if (enaSet) {
			JPanel panSet = new JPanel();
			this.add(panSet, cc.xy(2, 2));
			UnitsUiUtil.createVerticalButtonPanel(
				panSet,
				new AbstractButton[]{
					new JButton(ua.getAction(UnitsActions.SetSousSetAction.class)),
					new JButton(ua.getAction(UnitsActions.SetSousClearAction.class)),
					new JButton(ua.getAction(UnitsActions.SetSousAddAction.class)),
					new JButton(ua.getAction(UnitsActions.SetSousRemoveAction.class)),
				}
			);
		}
	}

	/**
	 * @return	Returns the selected SystemOfUnits.
	 */
	public SystemOfUnits[] getSelectedSous() {
		SystemOfUnits [] sousa;
		int [] sel = listSous.getSelectedIndices();
		sousa = new SystemOfUnits[sel.length];
		for (int i = 0; i < sel.length; i++) {
			int j = sel[i];
			sousa[i] = dataSous.get(j);
		}
		return sousa;
	}
}
