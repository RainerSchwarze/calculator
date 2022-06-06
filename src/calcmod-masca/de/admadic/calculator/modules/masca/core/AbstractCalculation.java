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
package de.admadic.calculator.modules.masca.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import de.admadic.calculator.modules.masca.ui.CalcPanel;
import de.admadic.units.core.IMeasure;

/**
 * @author Rainer Schwarze
 *
 */
public abstract class AbstractCalculation implements ActionListener {
	Vector<IMeasure> inputs;
	Vector<IMeasure> outputs;
	Hashtable<IMeasure,ValueConstraints> inputConstraints;
	String id;
	String title;
	String description;
	CalcPanel calcPanel;

	/**
	 * 
	 */
	public AbstractCalculation() {
		super();
		inputs = new Vector<IMeasure>();
		outputs = new Vector<IMeasure>();
		inputConstraints = new Hashtable<IMeasure,ValueConstraints>();
	}

	/**
	 * @param calcPanel
	 */
	public void linkToCalcPanel(CalcPanel calcPanel) {
		unlinkFromCalcPanel();
		this.calcPanel = calcPanel;
		setupPanel();
		if (this.calcPanel!=null) {
			this.calcPanel.addActionListener(this);
		}
	}

	/**
	 * 
	 */
	public void unlinkFromCalcPanel() {
		if (this.calcPanel!=null) {
			this.calcPanel.removeActionListener(this);
		}
	}
	
	/**
	 * 
	 */
	public void setupPanel() {
		calcPanel.initPlain(title, description, inputs.size(), outputs.size());
		for (IMeasure m : inputConstraints.keySet()) {
			calcPanel.addInputConstraints(m, inputConstraints.get(m));
		}
		calcPanel.registerMeasures(inputs, outputs);
		//calcPanel.addActionListener(this);
		calcPanel.repaint();
	}
	
	/**
	 * Perform the calculation.
	 */
	protected abstract void calculateCore();

	/**
	 * 
	 */
	public void calculate() {
		if (!getCalcPanel().commitInputs()) return;
		calculateCore();
		getCalcPanel().refreshDisplay();
	}
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the inputs.
	 */
	public Vector<IMeasure> getInputs() {
		return inputs;
	}

	/**
	 * @return Returns the outputs.
	 */
	public Vector<IMeasure> getOutputs() {
		return outputs;
	}

	/**
	 * @return Returns the calcPanel.
	 */
	public CalcPanel getCalcPanel() {
		return calcPanel;
	}

	/**
	 * @param calcPanel The calcPanel to set.
	 */
	public void setCalcPanel(CalcPanel calcPanel) {
		this.calcPanel = calcPanel;
	}

	/**
	 * @param m
	 * @return	Returns the ValueConstraints object for the IMeasure, 
	 * null, if there is none.
	 */
	public ValueConstraints getInputConstraints(IMeasure m) {
		return inputConstraints.get(m);
	}

	/**
	 * @param m
	 * @param vc
	 */
	public void addInputConstraints(IMeasure m, ValueConstraints vc) {
		inputConstraints.put(m, vc);
	}


	/**
	 * @param mid
	 * @param vc
	 */
	public void addInputConstraints(String mid, ValueConstraints vc) {
		for (IMeasure m : inputs) {
			if (m.getId().equals(mid)) {
				addInputConstraints(m, vc);
				return;
			}
		}
		throw new Error("Could not find input with id " + mid + 
				" in calculation with id " + getId());
	}

	/**
	 * 
	 */
	public void clearInputConstraints() {
		inputConstraints.clear();
	}
	
	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		calculate();
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
}
