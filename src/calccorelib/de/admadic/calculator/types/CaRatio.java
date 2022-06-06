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
package de.admadic.calculator.types;

import java.util.LinkedList;

/**
 * @author Rainer Schwarze
 *
 */
public class CaRatio extends CaNumber implements Cloneable {
	int valueSig = +1;		// +1 / -1
	long valueInt = 0;		// integer part
	long valueNum = 0;		// numerator 
	long valueDen = 1;		// denominator

	// FIXME: implement the false condition 
	boolean useIntPart = true;
	// FIXME: implement the maxDen handling (rounding etc.)
	long maxDen = 0;		// maximum denominator (0= disabled)

	long upperlimit = Long.MAX_VALUE;
	long lowerlimit = Long.MIN_VALUE;

	public CaRatio() {
		super();
		setZero();
	}
	public CaRatio(long vint, long vnum, long vden) {
		super();
		setValue(vint, vnum, vden);
	}
	public CaRatio(long vnum, long vden) {
		super();
		setValue(vnum, vden);
	}
	public CaRatio(long vnum) {
		super();
		setValue(vnum);
	}

	public CaRatio clone() throws CloneNotSupportedException {
		CaRatio v = (CaRatio)super.clone();
		cloneTo(v);
		return v;
	}
	public void cloneTo(CaRatio v) {
		super.cloneTo(v);
		v.valueSig = this.valueSig;
		v.valueInt = this.valueInt;
		v.valueNum = this.valueNum;
		v.valueDen = this.valueDen;
		v.useIntPart = this.useIntPart;
		v.maxDen = this.maxDen;
		v.upperlimit = this.upperlimit;
		v.lowerlimit = this.lowerlimit;
	}
	
	public void setValue(long vint, long vnum, long vden) {
		valueSig = +1;
		valueInt = vint;
		valueNum = vnum;
		valueDen = vden;
		setStateNormal();
	}

	public void setValue(long vnum, long vden) {
		setValue(0, vnum, vden);
	}

	public void setValue(long vnum) {
		setValue(0, vnum, 1);
	}

	public void calcRatio(double v, double eps) {
		// FIXME: implement a proper test suite for that
		int sign;
		long intpart;
		double f;
		LinkedList<Long> rary = new LinkedList<Long>();
		int loopmax = 1000;
		int loopctr = 0;
		long tmpv;
		long tmpn;
		long tmpd;

		sign = (v>=0) ? +1 : -1;
		if (v<0) v = -v;
		intpart = (long)Math.floor(v);
		f = v - intpart;
		if (f<=eps) {
			setValue(sign*intpart, 0, 1);
			return;
		}

		while (f>eps) {
			f = 1/f;
			tmpv = (long)Math.floor(f);	// integer part
			f = f - tmpv;				// fraction part
			rary.add(new Long(tmpv));

			// prevent infinite loop because of rounding errors:
			loopctr++;
			if (loopctr>=loopmax) {
				// FIXME: implement a message module which can be hooked:
				System.err.printf(
						"Warning: calcRatio stopped after "+
						"%d steps with an error of %f\n",
						loopctr, f);
				break;
			}
		}

		tmpn = 0;
		tmpd = 1;
		while (!rary.isEmpty()) {
			tmpv = rary.getLast().longValue();
			tmpn = tmpn + tmpv * tmpd;
			// now swap
			tmpv = tmpn;
			tmpn = tmpd;
			tmpd = tmpv;
			// now ready for next
			rary.removeLast();
		}

		setValue(sign*intpart, tmpn, tmpd);
	}

	/* (non-Javadoc)
	 * @see de.admadi c.calculator.Number#setZero()
	 */
	public void setZero() {
		super.setZero();
		setValue(0, 0, 1);
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#setUnity()
	 */
	public void setUnity() {
		super.setUnity();
		setValue(0, 1, 1);
	}

	public void checkStates(CaLong v) {
		if (isNaN()) {
			v.setState(CaNumber.STATE_NAN);
		} else if (isPosZero()) {
			v.value = 0;
		} else if (isNegZero()) {
			v.value = 0;
		} else if (isInf()) {
			v.setState(getState());
		} else {
			long vtmp;
			vtmp = valueInt;
			if (valueNum>valueDen) {
				vtmp += valueNum / valueDen;
			}
			vtmp *= valueSig;
			if (vtmp>v.upperlimit) {
				v.setState(CaNumber.STATE_POSINF);
			}else if (vtmp<v.lowerlimit) {
				v.setState(CaNumber.STATE_NEGINF);
			} else {
				v.value = vtmp;
			}
		}
	}

	public void checkStates(CaDouble v) {
		if (getState() != CaNumber.STATE_NORMAL) {
			v.setState(getState());
		} else if (valueDen==0) {
			v.setState(CaNumber.STATE_NAN);
		} else {
			double vtmp;
			vtmp = valueInt;
			vtmp += ((double)valueNum) / valueDen;	// cast expr to double
			vtmp *= valueSig;
			if (vtmp>v.upperlimit) {
				v.setState(CaNumber.STATE_POSINF);
			} else if (vtmp<v.lowerlimit) {
				v.setState(CaNumber.STATE_NEGINF);
			} else {
				v.value = vtmp;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#byteValue()
	 */
	public CaByte byteValue() {
		CaByte v = new CaByte();
		checkStates(v);
		return v;
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#shortValue()
	 */
	public CaShort shortValue() {
		CaShort v = new CaShort();
		checkStates(v);
		return v;
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#intValue()
	 */
	public CaInteger intValue() {
		CaInteger v = new CaInteger();
		checkStates(v);
		return v;
	}

	public CaLong longValue() {
		CaLong v = new CaLong();
		checkStates(v);
		return v;
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#floatValue()
	 */
	public CaFloat floatValue() {
		CaFloat v = new CaFloat();
		checkStates(v);
		return v;
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#doubleValue()
	 */
	public CaDouble doubleValue() {
		CaDouble v = new CaDouble();
		checkStates(v);
		return v;
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#complexValue()
	 */
	public CaComplex complexValue() {
		CaComplex v;
		CaDouble vd = new CaDouble();
		checkStates(vd);
		v = vd.complexValue();
		return v;
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#ratioValue()
	 */
	public CaRatio ratioValue() {
		CaRatio v = new CaRatio();
		if (getState() != CaNumber.STATE_NORMAL) {
			v.setState(getState());
		} else {
			v.valueSig = this.valueSig;
			v.valueInt = this.valueInt;
			v.valueNum = this.valueNum;
			v.valueDen = this.valueDen;
			v.setState(CaNumber.STATE_NORMAL);
		}
		return v;
	}
}
