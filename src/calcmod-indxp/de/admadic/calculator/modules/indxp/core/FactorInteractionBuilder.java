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
package de.admadic.calculator.modules.indxp.core;

import java.util.ArrayList;

/**
 * @author Rainer Schwarze
 *
 */
public class FactorInteractionBuilder {
	ArrayList<Factor> factorsLink;
	ArrayList<FactorInteraction> factorInteractionsLink;

	/**
	 * @param factorsLink 
	 * @param factorInteractionsLink 
	 */
	public FactorInteractionBuilder(
			ArrayList<Factor> factorsLink, 
			ArrayList<FactorInteraction> factorInteractionsLink) {
		super();
		this.factorsLink = factorsLink;
		this.factorInteractionsLink = factorInteractionsLink;
	}

	/**
	 * Creates a full factorial set of FactorInteractions.
	 */
	public void createFullFactorialInteractions() {
		factorInteractionsLink.clear();
		for (int i=1; i<=factorsLink.size(); i++) {
			createFactorialInteractionsWithLength(i);
		}
		// fireDataEvent(null, DataEvent.FACTORINTERACTIONS_STRUCT_CHANGED);
	}

	/**
	 * @param minLength
	 * @param maxLength
	 */
	public void createFactorialInteractions(int minLength, int maxLength) {
		factorInteractionsLink.clear();
		if (minLength<1 || minLength>factorsLink.size()) {
			throw new IllegalArgumentException(
					"minLength out of range (1..factor.size())");
		}
		if (maxLength<1 || maxLength>factorsLink.size()) {
			throw new IllegalArgumentException(
					"maxLength out of range (1..factor.size())");
		}
		if (minLength>maxLength) {
			throw new IllegalArgumentException(
					"minLength is larger than maxLength");
		}
		for (int i=minLength; i<=maxLength; i++) {
			createFactorialInteractionsWithLength(i);
		}
		// fireDataEvent(null, DataEvent.FACTORINTERACTIONS_STRUCT_CHANGED);
	}

	// //////////////////////////////////////////////////////
	// private engine methods
	// //////////////////////////////////////////////////////
	
	/** 
	 * helper function 
	 * @param workspace 
	 * @param fidxFrom 
	 * @param level 
	 * @param length 
	 */
	private void appendFactors(
			ArrayList<Factor> workspace,
			int fidxFrom,
			int level, 
			int length) {
		int fidxMax = factorsLink.size() - length + level;
		for (int i=fidxFrom; i<=fidxMax; i++) {
			workspace.add(factorsLink.get(i));
			if (workspace.size()>length) {
				throw new Error("IDEData: factorial interactions design flaw.");
			}
			if (workspace.size()==length) {
				FactorInteraction fi = new FactorInteraction();
				for (Factor factor : workspace) {
					fi.addFactor(factor);
				}
				factorInteractionsLink.add(fi);
			} else {
				// recursive call!
				appendFactors(workspace, i+1, level+1, length);
			}
			workspace.remove(workspace.size()-1);
		}
	}
	
	/**
	 * helper function.
	 * @param length
	 */
	private void createFactorialInteractionsWithLength(int length) {
		ArrayList<Factor> workspace = new ArrayList<Factor>();
		appendFactors(workspace, 0, 0, length);
	}
}
