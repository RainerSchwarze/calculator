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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.modules.masca.core.ValueConstraints;
import de.admadic.units.core.IMeasure;
import de.admadic.units.core.IUnit;

/**
 * @author Rainer Schwarze
 *
 */
public class ValueField extends JPanel implements ActionListener {
	/** */
	private static final long serialVersionUID = 1L;

	JLabel labelName;
	JLabel labelSymbol;
	JTextField textValue;
	UnitSelector comboUnit;

	IMeasure measure;
	Vector<IUnit> units;
	ValueConstraints valueConstraints;
	
	/** input style */
	public final static int STYLE_INPUT = 0;
	/** output style */
	public final static int STYLE_OUTPUT= 1;

	int style = STYLE_INPUT;

	/** field input is ok */ 
	public final static int STATUS_OK = 0;
	/** field input is bad */ 
	public final static int STATUS_BAD = 1;
	/** field input is to be warned of */ 
	public final static int STATUS_WARN = 2;
	int fieldStatus = STATUS_OK;
	
	/**
	 * 
	 */
	public ValueField() {
		super();
		initContents();
		setStyle(STYLE_INPUT);
		setMeasure(null);
	}

	/**
	 * @param style
	 */
	public void setStyle(int style) {
		this.style = style;
		updateStyle();
	}

	
	/**
	 * @return Returns the fieldStatus.
	 */
	public int getFieldStatus() {
		return fieldStatus;
	}

	/**
	 * @param fieldStatus The fieldStatus to set.
	 */
	public void setFieldStatus(int fieldStatus) {
		boolean change = false;
		if (this.fieldStatus!=fieldStatus) change = true;
		this.fieldStatus = fieldStatus;
		if (!change) return;
		switch (this.fieldStatus) {
		case STATUS_OK:
			Border bok = UIManager.getBorder("TextField.border");
			textValue.setBorder(bok);
			break;
		case STATUS_WARN:
			Border bwarn = BorderFactory.createLineBorder(java.awt.Color.ORANGE, 2);
			textValue.setBorder(bwarn);
			break;
		case STATUS_BAD:	// fallthrough
		default:
			Border bbad = BorderFactory.createLineBorder(java.awt.Color.RED, 2);
			textValue.setBorder(bbad);
			break;
		}
	}

	/**
	 *
	 */
	protected void initContents() {
		FormLayout fl = new FormLayout(
				"0px, 80dlu, 5px, 25dlu, 5px, 80dlu, 5px, 40dlu, 0px",
				"0px, p, 0px");
		CellConstraints cc = new CellConstraints();
		this.setLayout(fl);
		this.add(labelName = new JLabel("(name)"), cc.xy(2, 2));
		this.add(labelSymbol = new JLabel("(sym)"), cc.xy(4, 2));
		this.add(textValue = new JTextField("(value)"), cc.xy(6, 2));
		this.add(comboUnit = new UnitSelector(), cc.xy(8, 2));
		comboUnit.addActionListener(this);
	}

	/**
	 * @param units
	 */
	public void setUnits(Vector<IUnit> units) {
		this.units = units;
		comboUnit.setUnits(units);
	}

	/**
	 * @return Returns the measure.
	 */
	public IMeasure getMeasure() {
		return measure;
	}

	/**
	 * @param measure The measure to set.
	 */
	public void setMeasure(IMeasure measure) {
		this.measure = measure;
		updateMeasure();
	}

	/**
	 * 
	 */
	public void refreshDisplay() {
		updateMeasure();
	}
	
	private void updateStyle() {
		if (style==STYLE_INPUT) {
			textValue.setEditable(true);
		} else {
			textValue.setEditable(false);
		}
	}

	private void updateMeasure() {
		if (measure==null) {
			labelName.setText("-");
			labelSymbol.setText("-");
			textValue.setText("");
			comboUnit.setSelectedItem(null);
		} else {
			labelName.setText(measure.getNameDisplay());
			labelSymbol.setText(measure.getSymbolDisplay());
			String tmp = measure.getValueView();
			textValue.setText(tmp);
			IUnit u = measure.getUnit();
			int sel = units.indexOf(u);
			comboUnit.setSelectedIndex(sel);
		}
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// came from comboBox
		int sel = comboUnit.getSelectedIndex();
		if (sel<0) return;
		if (measure==null) return;
		IUnit u = units.get(sel);
		commitInput(null);
		measure.changeUnit(u);
		String tmp = measure.getValueView();
		textValue.setText(tmp);
	}

	/**
	 * @param vfc 
	 * 
	 */
	public void commitInput(ValueFieldContext vfc) {
		Double v;
		try {
			v = Double.valueOf(textValue.getText());
		} catch (NumberFormatException e) {
			if (vfc!=null) 
				vfc.addEntry(
						this, e.getMessage(), 
						ValueFieldContext.STATUS_BAD);
			setFieldStatus(STATUS_BAD);
			return;
		}
		if (valueConstraints!=null && !valueConstraints.checkValue(v.doubleValue())) {
			setFieldStatus(STATUS_WARN);
		} else {
			setFieldStatus(STATUS_OK);
		}
		measure.setValue(v);
	}

	/**
	 * @return Returns the valueConstraints.
	 */
	public ValueConstraints getValueConstraints() {
		return valueConstraints;
	}

	/**
	 * @param valueConstraints The valueConstraints to set.
	 */
	public void setValueConstraints(ValueConstraints valueConstraints) {
		this.valueConstraints = valueConstraints;
	}
}
