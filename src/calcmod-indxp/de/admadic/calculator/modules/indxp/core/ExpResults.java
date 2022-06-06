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
public class ExpResults {
	int replicateCount;				// no. of experimental sub results
	ArrayList<Double> replicates;	// the sub results (called replicates)

	// the actual results could be externalized, but for now
	// the typical is yavg and sigma
	Double yAvg;
	Double sigma;

	/**
	 * Note: for one experiment, it is recommended to have the same
	 * sub result count for all ExpResult instances.
	 * @param count	Number of sub results to support.
	 */
	public ExpResults(int count) {
		super();
		replicates = new ArrayList<Double>();
		replicateCount = count;
		for (int i=0; i<replicateCount; i++) {
			replicates.add(null);	// no result yet
		}
	}

	/**
	 * @param index
	 * @param result
	 */
	public void setResult(int index, Double result) {
		replicates.set(index, result);
	}

	/**
	 * @param index
	 * @return	Returns the sub result at the given index.
	 */
	public Double getResult(int index) {
		return replicates.get(index);
	}

	/**
	 * Calculate the "statistics" for this instance.
	 */
	public void calculate() {
		yAvg = MathHelper.calcAvg(replicates);
		sigma = MathHelper.calcSigma(replicates, yAvg);
	}

	/**
	 * @return Returns the sAvg.
	 */
	public Double getSigma() {
		return sigma;
	}

	/**
	 * @return Returns the yAvg.
	 */
	public Double getYAvg() {
		return yAvg;
	}

	/**
	 * @return Returns the replicateCount.
	 */
	public int getReplicateCount() {
		return replicateCount;
	}

	/**
	 * 
	 */
	public void resetData() {
		for (int i=0; i<replicates.size(); i++) {
			replicates.set(i, null);
		}
	}


	/**
	 * @param sigma The sigma to set.
	 */
	public void setSigma(Double sigma) {
		this.sigma = sigma;
	}

	/**
	 * @param avg The yAvg to set.
	 */
	public void setYAvg(Double avg) {
		this.yAvg = avg;
	}
}
