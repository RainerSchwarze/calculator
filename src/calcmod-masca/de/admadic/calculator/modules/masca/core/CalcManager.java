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

import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcManager {
	CalcCategoryTree categoryTree;
	Vector<AbstractCalculation> calculations;
	Hashtable<String,AbstractCalculation> idToCalculation;

	/**
	 * 
	 */
	public CalcManager() {
		super();
		categoryTree = new CalcCategoryTree();
		calculations = new Vector<AbstractCalculation>();
		idToCalculation = new Hashtable<String,AbstractCalculation>();
	}

	/**
	 * @param ce
	 */
	public void addCalculation(AbstractCalculation ce) {
		calculations.add(ce);
		idToCalculation.put(ce.getId(), ce);
	}

	/**
	 * @param ce
	 * @param catid
	 */
	public void addCalculationToCategory(AbstractCalculation ce, String catid) {
		categoryTree.addCalculation(catid, ce);
	}
	
	/**
	 * @param id
	 * @return	Returns the calculation for the given id.
	 */
	public AbstractCalculation getCalculation(String id) {
		return idToCalculation.get(id);
	}

	/**
	 * @param id
	 */
	public void removeCalculation(String id) {
		AbstractCalculation ce = idToCalculation.get(id);
		if (ce!=null) {
			idToCalculation.remove(id);
			calculations.remove(ce);
		}
	}

	/**
	 * @return	Returns the list of calculations.
	 */
	public Vector<AbstractCalculation> getCalculations() {
		return calculations;
	}

	/**
	 * @param parentId
	 * @param cc
	 */
	public void addCategory(String parentId, CalcCategory cc) {
		categoryTree.addCategory(parentId, cc);
	}

	/**
	 * @return Returns the categoryTree.
	 */
	public CalcCategoryTree getCategoryTree() {
		return categoryTree;
	}
}
