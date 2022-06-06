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
import java.util.Hashtable;

/**
 * @author Rainer Schwarze
 *
 */
public class Run {
	String id;
	ArrayList<Factor> factorsLink;
	ArrayList<FactorInteraction> factorInteractionsLink;

	Hashtable<Factor, Integer> factorLevels;
	Hashtable<FactorInteraction, Integer> factorInteractionLevels;

	/**
	 * @param factors 
	 * @param factorInteractions 
	 * 
	 */
	public Run(
			ArrayList<Factor> factors, 
			ArrayList<FactorInteraction> factorInteractions) {
		super();
		this.factorsLink = factors;
		this.factorInteractionsLink = factorInteractions;
		factorLevels = new Hashtable<Factor, Integer>();
		factorInteractionLevels = new Hashtable<FactorInteraction, Integer>();
	}

	/**
	 * @param factor
	 * @param level
	 */
	public void setLevel(Factor factor, Integer level) {
		factorLevels.put(factor, level);
	}

	/**
	 * Calculates the levels of the FactorInteractions.
	 * This is done by multiplying +1 or -1 of the Factors levels
	 * according to the selected factors in a FactorInteraction.
	 */
	public void updateFactorInteractionLevels() {
		for (FactorInteraction fi : factorInteractionsLink) {
			int iv = +1;
			for (Factor f : fi.getFactors()) {
				Integer itg = factorLevels.get(f);
				iv *= itg.intValue();
			}
			factorInteractionLevels.put(fi, Integer.valueOf(iv));
		}
	}

	/**
	 * @param fi
	 * @return	Get the level of the given FactorInteraction.
	 */
	public Integer getFiLevel(FactorInteraction fi) {
		Integer level = factorInteractionLevels.get(fi);
		return level;
	}

	/**
	 * @param f
	 * @return	Returns the level for the given factor.
	 */
	public Integer getFactorLevel(Factor f) {
		Integer level = factorLevels.get(f);
		return level;
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
