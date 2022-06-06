/**
 *
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
 */
package de.admadic.calculator.functions;

import de.admadic.calculator.math.MathException;
import de.admadic.calculator.types.CaNumber;

/**
 * @author Rainer Schwarze
 *
 */
public class Function {
	public class InputSource {
		public Function function = null;
		public CaNumber argument = null;
		public InputSource(Function f, CaNumber a) {
			function = f;
			argument = a;
		}
	}

	String name = null;
	int order = -1;	// order of function (add = 2)
	CaNumber [] inputs = null;
	InputSource [] inputsources = null;
	CaNumber output = null;

	public Function() {
		// nothing?
		setOrder(0);
	}
	public Function(String name) {
		// nothing?
		setOrder(0);
		setName(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public CaNumber calculate() throws MathException {
		// FIXME: implement function combination
		return null;
	}
	public CaNumber calculate(CaNumber [] args) throws MathException {
		// FIXME: implement function combination
		return null;
	}
	public void updateOrderStatus() {
		if (order>0) {
			inputs = new CaNumber[order];
			inputsources = new InputSource[order];
		} else {
			inputs = null;
			inputsources = null;
		}
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public void setOrder(int order, boolean updatestatus) {
		this.order = order;
		if (updatestatus) {
			updateOrderStatus();
		}
	}
	public int getOrder() {
		return order;
	}
	public CaNumber getOutput() {
		return output;
	}
	public void setInputs(CaNumber [] args) {
		if (inputs.length != args.length) {
			// error
			return;
		}
		for (int i=0; i<inputs.length; i++) {
			inputs[i] = args[i];
		}
	}
	public CaNumber getInput(int index) {
		if (inputs==null) return null;
		if ((index<0) || (index>=inputs.length)) return null;
		return inputs[index];
	}

	public void registerInputSource(int index, Function func, CaNumber arg) {
		// FIXME: add exception
		if (inputsources==null) return; // exception here!
		if ((index<0) || (index>=inputsources.length)) return;

		inputsources[index] = new InputSource(func, arg);
	}
}
