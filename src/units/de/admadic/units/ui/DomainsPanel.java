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
import de.admadic.units.core.Domain;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class DomainsPanel extends JPanel {
	UnitManager um;
	UnitsActions ua;

	boolean enaFil;
	boolean enaSet;
	
	JList listDomains;
	JScrollPane scrollDomains;
	ListRefListModel listModelDomains;
	ListElementAccessor listAccessorDomains;
	Vector<Domain> dataDomains;
	
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
	public DomainsPanel(UnitManager um, UnitsActions ua, boolean enaFil, boolean enaSet) {
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

		listDomains = new JList();
		scrollDomains = new JScrollPane(
				listDomains,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(scrollDomains, cc.xy(4, 2));
		// listDomains.setVisibleRowCount(8);
		scrollDomains.setPreferredSize(new java.awt.Dimension(200, 150));

		dataDomains = um.getDomains();
		listModelDomains = new ListRefListModel();
		listModelDomains.setData(dataDomains);
		listAccessorDomains = new ListElementAccessor() {
			public Object getElement(Object el) {
				return ((Domain)el).getName();
			}
		};
		listModelDomains.setElementAccessor(listAccessorDomains);
		listDomains.setModel(listModelDomains);

		if (enaFil) {
			JPanel panFil = new JPanel();
			this.add(panFil, cc.xy(4, 4));
			UnitsUiUtil.createHorizontalButtonPanel(
				panFil,
				new AbstractButton[]{
					new JButton(ua.getAction(UnitsActions.FilterDomainsSetAction.class)),
					new JButton(ua.getAction(UnitsActions.FilterDomainsClearAction.class)),
					new JButton(ua.getAction(UnitsActions.FilterDomainsAddAction.class)),
					new JButton(ua.getAction(UnitsActions.FilterDomainsRemoveAction.class)),
				}
			);
		}

		if (enaSet) {
			JPanel panSet = new JPanel();
			this.add(panSet, cc.xy(2, 2));
			UnitsUiUtil.createVerticalButtonPanel(
				panSet,
				new AbstractButton[]{
					new JButton(ua.getAction(UnitsActions.SetDomainsSetAction.class)),
					new JButton(ua.getAction(UnitsActions.SetDomainsClearAction.class)),
					new JButton(ua.getAction(UnitsActions.SetDomainsAddAction.class)),
					new JButton(ua.getAction(UnitsActions.SetDomainsRemoveAction.class)),
				}
			);
		}
	}

	/**
	 * @return	Returns the list of selected domains.
	 */
	public Domain[] getSelectedDomains() {
		Domain [] da;
		int [] sel = listDomains.getSelectedIndices();
		da = new Domain[sel.length];
		for (int i = 0; i < sel.length; i++) {
			int j = sel[i];
			da[i] = dataDomains.get(j);
		}
		return da;
	}
}
