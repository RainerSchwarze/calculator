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
import java.util.Vector;

/**
 * @author Rainer Schwarze
 *
 */
public class FactorInteraction {
	Vector<Factor> factors;
	boolean aliased;
	String aliasName;

	/** */
	public FactorInteraction() {
		super();
		aliased = false;
		aliasName = null;
		factors = new Vector<Factor>();
	}
	
	/**
	 * @param workspace
	 */
	public void addFactors(ArrayList<Factor> workspace) {
		factors.addAll(workspace);
	}

	/**
	 * @param f
	 */
	public void addFactor(Factor f) {
		factors.add(f);
	}

	/**
	 * @return	Returns the factors for this interaction. (never null)
	 */
	public Vector<Factor> getFactors() {
		return factors;
	}


	/**
	 * @return	Returns a display string
	 */
	public String getDisplay() {
		String v = "";
		for (Factor f : factors) {
			v += f.getName();
		}
		if (isAliased()) {
			v = aliasName + "=" + v;
		}
		return v;
	}

	/**
	 * @return	Returns an Entity information
	 */
	public String getEntitiesDisplay() {
		String v = "";
		for (Factor f : factors) {
			if (v.length()!=0) v += ", ";
			v += f.getEntity();
		}
		return v;
	}


	/**
	 * @return Returns the aliased.
	 */
	public boolean isAliased() {
		return aliased;
	}


	/**
	 * @param aliased The aliased to set.
	 * @param name 
	 */
	public void setAliased(boolean aliased, String name) {
		this.aliased = aliased;
		this.aliasName = name;
	}

	/**
	 * @return Returns the aliasName.
	 */
	public String getAliasName() {
		return aliasName;
	}

	/**
	 * @param aliasName The aliasName to set.
	 */
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
}
