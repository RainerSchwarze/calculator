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
package de.admadic.calculator.math;

import de.admadic.calculator.types.CaDouble;
import de.admadic.calculator.types.CaNumber;

/**
 * @author Rainer Schwarze
 *
 */
public class CaMath {
	
	/**
	 * @param arg0
	 * @param arg1
	 * @return	Returns the sum of the arguments.
	 * @throws MathException
	 */
	public static CaNumber add(CaNumber arg0, CaNumber arg1) throws MathException {
		CaNumber v;
		try {
			v = arg0.clone();
		} catch (CloneNotSupportedException e) {
			// e.printStackTrace();
			throw new MathException("number.clone failed. check number implementation");
		}
		if (v instanceof CaDouble) {
			DMath.add((CaDouble)v, arg1);
		}
		return v;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return	Returns the difference of the arguments.
	 * @throws MathException
	 */
	public static CaNumber sub(CaNumber arg0, CaNumber arg1) throws MathException {
		CaNumber v;
		try {
			v = arg0.clone();
		} catch (CloneNotSupportedException e) {
			// e.printStackTrace();
			throw new MathException("number.clone failed. check number implementation");
		}
		if (v instanceof CaDouble) {
			DMath.sub((CaDouble)v, arg1);
		}
		return v;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return	Returns the multiplication of the arguments.
	 * @throws MathException
	 */
	public static CaNumber mul(CaNumber arg0, CaNumber arg1) throws MathException {
		CaNumber v;
		try {
			v = arg0.clone();
		} catch (CloneNotSupportedException e) {
			// e.printStackTrace();
			throw new MathException("number.clone failed. check number implementation");
		}
		if (v instanceof CaDouble) {
			DMath.mul((CaDouble)v, arg1);
		}
		return v;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return	Returns the division of the arguments.
	 * @throws MathException
	 */
	public static CaNumber div(CaNumber arg0, CaNumber arg1) throws MathException {
		CaNumber v;
		try {
			v = arg0.clone();
		} catch (CloneNotSupportedException e) {
			// e.printStackTrace();
			throw new MathException("number.clone failed. check number implementation");
		}
		if (v instanceof CaDouble) {
			DMath.div((CaDouble)v, arg1);
		}
		return v;
	}

	/**
	 * @param arg0
	 * @return	Returns the negated value of the argument.
	 * @throws MathException
	 */
	public static CaNumber neg(CaNumber arg0) throws MathException {
		CaNumber v;
		try {
			v = arg0.clone();
		} catch (CloneNotSupportedException e) {
			// e.printStackTrace();
			throw new MathException("number.clone failed. check number implementation");
		}
		if (v instanceof CaDouble) {
			DMath.neg((CaDouble)v);
		}
		return v;
	}
}
