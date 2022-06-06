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



/**
 * @author Rainer Schwarze
 *
 */
public class CaComplex extends CaNumber implements Cloneable {
	double valueRe = 0.0;
	double valueIm = 0.0;
	double upperlimit = +Double.MAX_VALUE;
	double lowerlimit = -Double.MAX_VALUE;

	public CaComplex() {
		super();
		setZero();
	}
	public CaComplex(double vre, double vim) {
		super();
		setValue(vre, vim);
	}
	public CaComplex clone() throws CloneNotSupportedException {
		CaComplex v = (CaComplex)super.clone();
		cloneTo(v);
		return v;
	}
	public void cloneTo(CaComplex v) {
		super.cloneTo(v);
		v.valueRe = this.valueRe;
		v.valueIm = this.valueIm;
		v.upperlimit = this.upperlimit;
		v.lowerlimit = this.lowerlimit;
	}
	
	public void setValue(double vre, double vim) {
		valueRe = vre;
		valueIm = vim;
		setStateNormal();
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#setZero()
	 */
	public void setZero() {
		super.setZero();
		setValue(0.0, 0.0);
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#setUnity()
	 */
	public void setUnity() {
		super.setUnity();
		setValue(1.0, 0.0);
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
		} else if (this.valueRe>v.upperlimit) {
			v.setState(CaNumber.STATE_POSINF);
		} else if (this.valueRe<v.lowerlimit) {
			v.setState(CaNumber.STATE_NEGINF);
		} else {
			v.value = Math.round(this.valueRe);
		}
	}

	public void checkStates(CaDouble v) {
		if (getState() != CaNumber.STATE_NORMAL) {
			v.setState(getState());
		} else {
			// FIXME: support different modes of calc. value complex
			if (this.valueRe>v.upperlimit) {
				v.setState(CaNumber.STATE_POSINF);
			} else if (this.valueRe<v.lowerlimit) {
				v.setState(CaNumber.STATE_NEGINF);
			} else {
				v.value = this.valueRe;
			}
		}
	}

	public void checkStates(CaComplex v) {
		if (getState() != CaNumber.STATE_NORMAL) {
			v.setState(getState());
		} else {
			v.valueRe = this.valueRe;
			v.valueIm = this.valueIm;
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

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#longValue()
	 */
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
		CaComplex v = new CaComplex();
		checkStates(v);
		return v;
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#ratioValue()
	 */
	public CaRatio ratioValue() {
		CaRatio v;
		CaDouble vd = doubleValue();
		v = vd.ratioValue();
		return v;
	}
}
