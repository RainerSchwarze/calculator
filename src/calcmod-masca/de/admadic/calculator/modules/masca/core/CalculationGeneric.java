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

import com.graphbuilder.math.Expression;
import com.graphbuilder.math.ExpressionTree;
import com.graphbuilder.math.FuncMap;
import com.graphbuilder.math.VarMap;

import de.admadic.units.core.IMeasure;
import de.admadic.units.core.UnitFactory;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class CalculationGeneric extends AbstractCalculation {
	UnitManager um;

	String exprStr;
	String varStr;
	Expression expr;
	FuncMap funcMap;
	VarMap varMap;
	
	/**
	 * @param um 
	 */
	public CalculationGeneric(UnitManager um) {
		super();
		this.um = um;
		funcMap = new FuncMap();
		funcMap.loadDefaultFunctions();
		varMap = new VarMap();
	}

	/**
	 * @param title 
	 * @param desc 
	 * @param varStr
	 * @param exprStr
	 * @return Returns this.
	 */
	public CalculationGeneric setup(String title, String desc, String [] varStr, String exprStr) {
		setTitle(title);
		setDescription(desc);
		// varStr = {".length", "@quant", "value", "uid", "symbol", "name"}, ...
		for (int i = 0; i < varStr.length; i++) {
			if (i==0) {
				addOutput(varStr[i]);
			} else {
				addInput(varStr[i]);
			}
		}
		expr = ExpressionTree.parse(exprStr);
		return this;
	}

	/**
	 * @param ispec
	 */
	public void addInput(String ispec) {
		// varStr = {".length", "@quant", "value", "uid", "symbol", "name"}, ...
		String [] sa = ispec.split(",");
		String mid = sa[0].trim();
		String qid = sa[1].trim();
		String val = sa[2].trim();
		String uid = sa[3].trim();
		String symbol = (sa.length>4) ? sa[4].trim() : null;
		String name = (sa.length>5) ? sa[5].trim() : null;
		addInput(mid, qid, val, uid, symbol, name);
	}

	/**
	 * @param mid
	 * @param qid
	 * @param val
	 * @param uid
	 * @param symbol
	 * @param name
	 */
	public void addInput(
			String mid, String qid, String val, String uid, 
			String symbol, String name) {
		double v = Double.parseDouble(val);
		IMeasure m = UnitFactory.createMeasure(
				um, mid, qid, v, uid, symbol, name);
		getInputs().add(m);
	}
	
	/**
	 * @param ispec
	 */
	public void addOutput(String ispec) {
		// varStr = {".length", "@quant", "value", "uid", "symbol", "name"}, ...
		String [] sa = ispec.split(",");
		String mid = sa[0].trim();
		String qid = sa[1].trim();
		String val = sa[2].trim();
		String uid = sa[3].trim();
		String symbol = (sa.length>4) ? sa[4].trim() : null;
		String name = (sa.length>5) ? sa[5].trim() : null;
		addOutput(mid, qid, val, uid, symbol, name);
	}

	/**
	 * @param mid
	 * @param qid
	 * @param val
	 * @param uid
	 * @param symbol
	 * @param name
	 */
	public void addOutput(
			String mid, String qid, String val, String uid, 
			String symbol, String name) {
		double v = Double.parseDouble(val);
		IMeasure m = UnitFactory.createMeasure(
				um, mid, qid, v, uid, symbol, name);
		getOutputs().add(m);
	}

	/**
	 * @param exprStr
	 */
	public void setExpression(String exprStr) {
		expr = ExpressionTree.parse(exprStr);
	}

	/**
	 * 
	 */
	public void testExpression() {
		for (int i = 0; i < getInputs().size(); i++) {
			IMeasure m = getInputs().get(i);
			varMap.setValue(m.getId().substring(1), m.getRootValue().doubleValue());
		}
		double r = expr.eval(varMap, funcMap);
		getOutputs().firstElement().setRootValue(Double.valueOf(r));
	}
	
	/**
	 * 
	 * @see de.admadic.calculator.modules.masca.core.AbstractCalculation#calculateCore()
	 */
	@Override
	protected void calculateCore() {
		for (int i = 0; i < getInputs().size(); i++) {
			IMeasure m = getInputs().get(i);
			varMap.setValue(m.getId().substring(1), m.getRootValue().doubleValue());
		}
		double r = expr.eval(varMap, funcMap);
		getOutputs().firstElement().setRootValue(Double.valueOf(r));
	}
}
