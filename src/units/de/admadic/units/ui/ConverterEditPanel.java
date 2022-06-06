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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Rainer Schwarze
 *
 */
public class ConverterEditPanel extends JPanel implements ActionListener {

	JComboBox comboType;
	JTextField textM;
	JTextField textN;
	JTextField textXM;
	JTextField textXN;
	
	/** converter type none */
	public final static int TYPE_NONE = 0;
	/** converter type identity */
	public final static int TYPE_IDENTITY = 1;
	/** converter type mul */
	public final static int TYPE_MUL = 2;
	/** converter type linear */
	public final static int TYPE_LINEAR = 3;
	String [] types = { "None", "Identity", "Multiplier", "Linear" };

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ConverterEditPanel() {
		super();
		initContents();
	}

	protected void initContents() {
		FormLayout fl = new FormLayout(
				"0px, p, 5px, p:grow, 0px",
				"0px, p, 5px, p, 5px, p, 5px, p, 5px, p, 0px");
		CellConstraints cc = new CellConstraints();

		// this.setBorder(BorderFactory.createLineBorder(java.awt.Color.RED));
		this.setLayout(fl);
		
		this.add(new JLabel("Type:"), cc.xy(2, 2));
		this.add(comboType = new JComboBox(types), cc.xy(4, 2));
		this.add(new JLabel("Param. m:"), cc.xy(2, 4));
		this.add(textM = new JTextField("1.0"), cc.xy(4, 4));
		this.add(new JLabel("Param. xm:"), cc.xy(2, 6));
		this.add(textXM = new JTextField(""), cc.xy(4, 6));
		this.add(new JLabel("Param. n:"), cc.xy(2, 8));
		this.add(textN = new JTextField("0.0"), cc.xy(4, 8));
		this.add(new JLabel("Param. xn:"), cc.xy(2, 10));
		this.add(textXN = new JTextField(""), cc.xy(4, 10));

		comboType.addActionListener(this);
		// the initial selection is NONE:
		enableFields(false, false);
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		int sel = comboType.getSelectedIndex();
		switch (sel) {
		case TYPE_NONE: enableFields(false, false); break;
		case TYPE_IDENTITY: enableFields(false, false); break;
		case TYPE_MUL: enableFields(true, false); break;
		case TYPE_LINEAR: enableFields(true, true); break;
		}
	}

	private void enableFields(boolean m, boolean n) {
		textM.setEnabled(m);
		textXM.setEnabled(m);
		textN.setEnabled(n);
		textXN.setEnabled(n);
	}

	/**
	 * @return	Returns the selected type.
	 */
	public int getType() {
		return comboType.getSelectedIndex();
	}

	/**
	 * @return	Returns the value for M.
	 */
	public String getParamM() {
		return textM.getText();
	}

	/**
	 * @return	Returns the value for N.
	 */
	public String getParamN() {
		return textN.getText();
	}

	/**
	 * @return	Returns the value for M.
	 */
	public String getParamXM() {
		return textXM.getText();
	}

	/**
	 * @return	Returns the value for N.
	 */
	public String getParamXN() {
		return textXN.getText();
	}

	/**
	 * @param t
	 */
	public void setType(int t) {
		comboType.setSelectedIndex(t);
	}

	/**
	 * @param v
	 */
	public void setParamM(String v) {
		textM.setText(v);
	}

	/**
	 * @param v
	 */
	public void setParamN(String v) {
		textN.setText(v);
	}

	/**
	 * @param v
	 */
	public void setParamXM(String v) {
		textXM.setText(v);
	}

	/**
	 * @param v
	 */
	public void setParamXN(String v) {
		textXN.setText(v);
	}
}
