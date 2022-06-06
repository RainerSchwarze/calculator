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
 * Analysis of Level effects in the experimental results.
 * FIXME: this class depends on the existense of the two levels
 * high and low. If more levels are to be supported major changes
 * have to be done.
 * 
 * @author Rainer Schwarze
 *
 */
public abstract class LevelAnalysis {
	ArrayList<FactorInteraction> factorInteractionsLink;
	ArrayList<Run> runsLink;
	String name;	// the name to display

	/** choose results for level high */
	public static final int RES_LEV_HIGH = 0;
	/** choose results for level low */
	public static final int RES_LEV_LOW = 1;
	/** choose results for level high */
	public static final int RES_LEV_DELTA = 2;
	
	Hashtable<FactorInteraction,Double> levelHighResults;
	Hashtable<FactorInteraction,Double> levelLowResults;
	Hashtable<FactorInteraction,Double> levelResultsDelta;

	/**
	 * @param factorInteractionsLink
	 * @param runsLink
	 */
	public LevelAnalysis(
			ArrayList<FactorInteraction> factorInteractionsLink, 
			ArrayList<Run> runsLink) {
		super();
		this.factorInteractionsLink = factorInteractionsLink;
		this.runsLink = runsLink;

		levelHighResults = new Hashtable<FactorInteraction,Double>();
		levelLowResults = new Hashtable<FactorInteraction,Double>();
		levelResultsDelta = new Hashtable<FactorInteraction,Double>();
	}

	/** 
	 * to be overridden! 
	 * return yavg or sigma for instance 
	 * @param runIndex 
	 * @return Returns the statistical value to be used for the level analysis.
	 */
	public abstract Double getValue(int runIndex);

	/**
	 * Perform the actual level analysis calculation.
	 */
	public void calculate() {
		levelHighResults.clear();
		levelLowResults.clear();
		levelResultsDelta.clear();

		calculateAvgsAtLevel(levelHighResults, Integer.valueOf(+1));
		calculateAvgsAtLevel(levelLowResults, Integer.valueOf(-1));
		calculateDelta();
	}

	/**
	 * helper function.
	 * @param levelResults
	 * @param level
	 */
	private void calculateAvgsAtLevel(
			Hashtable<FactorInteraction, Double> levelResults, 
			Integer level) {
		ArrayList<Double> v = new ArrayList<Double>();
		for (FactorInteraction fi : factorInteractionsLink) {
			v.clear();
			for (int i=0; i<runsLink.size(); i++) {
				Run r = runsLink.get(i);
				Integer lv = r.getFiLevel(fi);
				if (lv.intValue()==level.intValue()) {
					v.add(getValue(i));
				}
			}
			Double avg = MathHelper.calcAvg(v);
			levelResults.put(fi, avg);
		}
	}

	/**
	 * helper function.
	 */
	private void calculateDelta() {
		for (FactorInteraction fi : factorInteractionsLink) {
			Double high = null;
			Double low = null;
			if (levelHighResults.containsKey(fi)) 
				high = levelHighResults.get(fi);
			if (levelLowResults.containsKey(fi)) 
				low = levelLowResults.get(fi);
			if (high!=null && low!=null) {
				Double delta = Double.valueOf(
						high.doubleValue() - low.doubleValue());
				levelResultsDelta.put(fi, delta);
			}
		}
	}

	/**
	 * @param fi
	 * @return	Returns the high level analysis value for the given 
	 * FactorInteraction. 
	 */
	public Double getHighValue(FactorInteraction fi) {
		return levelHighResults.get(fi);
	}

	/**
	 * @param fi
	 * @return	Returns the low level analysis value for the given 
	 * FactorInteraction. 
	 */
	public Double getLowValue(FactorInteraction fi) {
		return levelLowResults.get(fi);
	}

	/**
	 * @param fi
	 * @return	Returns the level delta analysis value for the given 
	 * FactorInteraction. 
	 */
	public Double getDeltaValue(FactorInteraction fi) {
		return levelResultsDelta.get(fi);
	}

	/**
	 * @param id
	 * @param fi
	 * @return	Returns the statistical result for the given id.
	 */
	public Double getStatValue(int id, FactorInteraction fi) {
		Hashtable<FactorInteraction,Double> table;
		switch (id) {
		case RES_LEV_HIGH: table = levelHighResults; break;
		case RES_LEV_LOW: table = levelLowResults; break;
		case RES_LEV_DELTA: table = levelResultsDelta; break;
		default:
			throw new Error("invalid selector passed for getStatValue");
		}
		return table.get(fi);
	}

	/**
	 * @param id
	 * @param fi
	 * @param v 
	 */
	public void setStatValue(int id, FactorInteraction fi, Double v) {
		Hashtable<FactorInteraction,Double> table;
		switch (id) {
		case RES_LEV_HIGH: table = levelHighResults; break;
		case RES_LEV_LOW: table = levelLowResults; break;
		case RES_LEV_DELTA: table = levelResultsDelta; break;
		default:
			throw new Error("invalid selector passed for setStatValue");
		}
		if (v==null) {	// clear
			if (table.containsKey(fi)) {
				table.remove(fi);
			}
		} else {
			table.put(fi, v);
		}
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 */
	public void resetData() {
		levelHighResults.clear();
		levelLowResults.clear();
		levelResultsDelta.clear();
	}

	/**
	 * @param which
	 * @return Returns a Double[] where [0] is min, [1] is max.
	 */
	public Double[] getRange(int which) {
		Double [] res = new Double[] { null, null };
		Hashtable<FactorInteraction,Double> table;
		switch (which) {
		case RES_LEV_HIGH: table = levelHighResults; break;
		case RES_LEV_LOW: table = levelLowResults; break;
		case RES_LEV_DELTA: table = levelResultsDelta; break;
		default:
			throw new Error("invalid selector passed for getRange");
		}
		for (Double d : table.values()) {
			if (res[0]==null || d.doubleValue()<res[0].doubleValue()) {
				res[0] = d;
			}
			if (res[1]==null || d.doubleValue()>res[1].doubleValue()) {
				res[1] = d;
			}
		}
		return res;
	}
}