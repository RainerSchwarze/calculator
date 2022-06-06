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

import java.util.List;

/**
 * @author Rainer Schwarze
 *
 */
public class MathHelper {
	/**
	 * Calculates the average of the given list of values.
	 * If an element of the list is null or NaN, that element 
	 * is not used for calculation. (avg of (1, 2, NaN, 3) = (1+2+3)/3 = 2
	 * @param v
	 * @return	Returns the average
	 */
	public static Double calcAvg(List<Double> v) {
		double avgv = 0.0;
		int count = 0;
		for (Double d : v) {
			if (d==null) continue;
			if (d.isNaN()) continue;
			
			avgv += d.doubleValue();
			count++;
		}
		if (count==0) {
			return Double.valueOf(Double.NaN);
		}
		avgv /= count;
		return Double.valueOf(avgv);
	}

	/**
	 * @param v
	 * @return	Returns sigma
	 */
	public static Double calcSigma(List<Double> v) {
		Double avg = calcAvg(v);
		if (avg.isNaN()) return avg;
		return calcSigma(v, avg);
	}

	/**
	 * use this function, if the avg is already truly known.
	 * @param v
	 * @param avg
	 * @return	Returns sigma.
	 */
	public static Double calcSigma(List<Double> v, Double avg) {
		if (avg.isNaN()) return avg;

		double avgv = avg.doubleValue();
		double dvsum = 0.0;
		int count = 0;
		for (Double d : v) {
			if (d==null) continue;
			if (d.isNaN()) continue;

			double dv = d.doubleValue()-avgv;
			dv *= dv;
			dvsum += dv;
			count++;
		}

		if (count<2) {
			return Double.valueOf(Double.NaN);
		}
		dvsum /= count;
		dvsum = Math.sqrt(dvsum);
		return Double.valueOf(dvsum);
	}
}