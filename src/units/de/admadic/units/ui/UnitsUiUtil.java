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

import java.awt.FlowLayout;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.ui.util.ListElementAccessor;
import de.admadic.ui.util.ListRefComboBoxModel;
import de.admadic.units.core.Domain;
import de.admadic.units.core.Factor;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class UnitsUiUtil {

	/**
	 * No Instance
	 */
	protected UnitsUiUtil() {
		super();
	}

	/**
	 * @param um
	 * @return	Returns a combo box.
	 */
	public static JComboBox createDomainsComboBox(UnitManager um) {
		JComboBox combo = null;
		combo = new JComboBox();
		ListRefComboBoxModel lm = new ListRefComboBoxModel(um.getDomains());
		lm.setElementAccessor(new ListElementAccessor() {
			public Object getElement(Object el) {
				return ((Domain)el).getName();
			}
		});
		combo.setModel(lm);
		if (combo.getItemCount()>0) {
			combo.setSelectedIndex(0);
		}
		return combo;
	}

	/**
	 * @param um
	 * @return	Returns a combo box.
	 */
	public static JComboBox createFactorsComboBox(UnitManager um) {
		JComboBox combo = null;
		combo = new JComboBox();
		ListRefComboBoxModel lm = new ListRefComboBoxModel(um.getFactors());
		lm.setElementAccessor(new ListElementAccessor() {
			public Object getElement(Object el) {
				return ((Factor)el).getName();
			}
		});
		combo.setModel(lm);
		if (combo.getItemCount()>0) {
			combo.setSelectedIndex(0);
		}
		return combo;
	}

	/**
	 * @param panel
	 * @param buttons
	 */
	public static void createHorizontalButtonPanel(
			JPanel panel, AbstractButton [] buttons) {
		if (panel==null) throw new NullPointerException("panel must not be null.");
		if (buttons==null) throw new NullPointerException("buttons[] must not be null.");

		panel.setLayout(new FlowLayout());
		for (AbstractButton b : buttons) {
			panel.add(b);
		}
	}

	/**
	 * @param panel
	 * @param buttons
	 */
	public static void createVerticalButtonPanel(
			JPanel panel, AbstractButton [] buttons) {
		if (panel==null) throw new NullPointerException("panel must not be null.");
		if (buttons==null) throw new NullPointerException("buttons[] must not be null.");

		String rowspec = "";
		for (int i = 0; i < buttons.length; i++) {
			if (rowspec.length()>0) rowspec += ", 5px";
			rowspec += ", p";
		}
		FormLayout fl = new FormLayout("p", rowspec);
		CellConstraints cc = new CellConstraints();

		panel.setLayout(fl);
		for (int i = 0; i < buttons.length; i++) {
			AbstractButton b = buttons[i];
			panel.add(b, cc.xy(1, i*2 + 1));
		}
	}
}
