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
package de.admadic.calculator.modules.masca.ui;

import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.modules.masca.core.ValueConstraints;
import de.admadic.units.core.IMeasure;
import de.admadic.units.core.MeasureFormatter;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcPanel extends JPanel {
	/** */
	private static final long serialVersionUID = 1L;

	UnitManager um;
	MeasureFormatter mf;

	JLabel textTitle;
	JTextPane textDesc;
	JComponent labelInputs;
	JComponent labelOutputs;
	JButton btnCalc;
	Vector<ValueField> inputs;
	Vector<ValueField> outputs;
	ValueFieldContext vfc;
	Hashtable<IMeasure,ValueConstraints> inputConstraints;

	/**
	 * @param um 
	 * @param mf 
	 * 
	 */
	public CalcPanel(UnitManager um, MeasureFormatter mf) {
		super();
		this.um = um;
		this.mf = mf;
		inputs = new Vector<ValueField>();
		outputs = new Vector<ValueField>();
		vfc = new ValueFieldContext();
		inputConstraints = new Hashtable<IMeasure,ValueConstraints>();
	}

	/**
	 * @param title 
	 * @param desc 
	 * @param inpcount 
	 * @param outpcount 
	 * 
	 */
	public void initPlain(
			String title, String desc, 
			int inpcount, int outpcount) {

		// clear all existing items:
		this.removeAll();
		inputs.clear();
		outputs.clear();
		inputConstraints.clear();
		
		String colspec = "5px, p, 5px";
		String rowspec = "5px, p, 5px, 30dlu, 5px, p, 5px";
		for (int i=0; i<(1 + inpcount + 1 + outpcount); i++) {
			rowspec += ", p, 5px";
		}
		FormLayout fl = new FormLayout(colspec, rowspec);
		CellConstraints cc = new CellConstraints();
		this.setLayout(fl);
		int row = 0;

		this.add(
				textTitle = new JLabel(title), 
				cc.xy(2, row+=2));
		this.add(
				textDesc = new JTextPane(), 
				cc.xy(2, row+=2, CellConstraints.FILL, CellConstraints.FILL));
		textDesc.setText(desc);
		this.add(btnCalc = new JButton("Calculate"), cc.xy(2, row+=2));

		labelInputs = DefaultComponentFactory.getInstance().createSeparator(
			"Inputs:");
		labelOutputs = DefaultComponentFactory.getInstance().createSeparator(
			"Outputs:");

		textDesc.setEditable(false);

		for (int i = 0; i < inpcount; i++) {
			inputs.add(new ValueField());
		}
		for (int i = 0; i < outpcount; i++) {
			outputs.add(new ValueField());
		}

		this.add(labelInputs, cc.xy(2, row+=2));
		for (int i = 0; i < inputs.size(); i++) {
			ValueField f = inputs.get(i);
			f.setStyle(ValueField.STYLE_INPUT);
			this.add(f, cc.xy(2, row+=2));
		}
		this.add(labelOutputs, cc.xy(2, row+=2));
		for (int i = 0; i < outputs.size(); i++) {
			ValueField f = outputs.get(i);
			f.setStyle(ValueField.STYLE_OUTPUT);
			this.add(f, cc.xy(2, row+=2));
		}
	}

	/**
	 * @param inputs
	 * @param outputs
	 */
	public void registerMeasures(Vector<IMeasure> inputs, Vector<IMeasure> outputs) {
		for (int i = 0; i < inputs.size(); i++) {
			IMeasure m = inputs.get(i);
			m.setMeasureFormatter(mf);
			ValueField f = this.inputs.get(i);
			f.setUnits(um.getUnitsForDomain(m.getUnit().getDomain(), null));
			f.setMeasure(m);
			// if null is returned, the constraints are cleared. thats intended.
			f.setValueConstraints(inputConstraints.get(m));
		}
		for (int i = 0; i < outputs.size(); i++) {
			IMeasure m = outputs.get(i);
			m.setMeasureFormatter(mf);
			ValueField f = this.outputs.get(i);
			f.setUnits(um.getUnitsForDomain(m.getUnit().getDomain(), null));
			f.setMeasure(m);
		}
	}

	/**
	 * 
	 */
	public void refreshDisplay() {
		for (ValueField f : inputs) {
			f.refreshDisplay();
		}
		for (ValueField f : outputs) {
			f.refreshDisplay();
		}
	}

	/**
	 * @param m
	 * @param vc
	 */
	public void addInputConstraints(IMeasure m, ValueConstraints vc) {
		inputConstraints.put(m, vc);
	}

	/**
	 * 
	 */
	public void clearInputConstraints() {
		inputConstraints.clear();
	}
	
	/**
	 * @return Returns true, if parsing was successful and calculation can 
	 * proceed.
	 * 
	 */
	public boolean commitInputs() {
		vfc.clear();
		for (ValueField f : inputs) {
			f.commitInput(vfc);
		}
		if (vfc.getRecordCount()>0) return false;
		return true;
	}

	/**
	 * @param l
	 * @see javax.swing.AbstractButton#addActionListener(java.awt.event.ActionListener)
	 */
	public void addActionListener(ActionListener l) {
		if (btnCalc!=null) btnCalc.addActionListener(l);
	}

	/**
	 * @param l
	 * @see javax.swing.AbstractButton#removeActionListener(java.awt.event.ActionListener)
	 */
	public void removeActionListener(ActionListener l) {
		if (btnCalc!=null) btnCalc.removeActionListener(l);
	}
}
