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
public class CaDouble extends CaNumber implements Cloneable {
	public double value;
	public double upperlimit = +Double.MAX_VALUE;
	public double lowerlimit = -Double.MAX_VALUE;

	public CaDouble() {
		super();
	}
	public CaDouble(double v) {
		super();
		value = v;
	}
	public CaDouble clone() throws CloneNotSupportedException {
		CaDouble v = (CaDouble)super.clone();
		cloneTo(v);
		return v;
	}
	public void cloneTo(CaDouble v) {
		super.cloneTo(v);
		v.value = this.value;
		v.upperlimit = this.upperlimit;
		v.lowerlimit = this.lowerlimit;
	}

	public void setValue(double v) {
		value = v;
		setStateNormal();
	}

	public double getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#setZero()
	 */
	public void setZero() {
		super.setZero();
		value = 0.0;
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.Number#setUnity()
	 */
	public void setUnity() {
		super.setUnity();
		value = 1.0;
	}

	public String toString() {
		if (isNotNormal()) {
			return getStateString();
		}
		return String.valueOf(value);
	}

	public String toString(int width) {
		String fmt, s;
		double limit;
		if (isNotNormal()) {
			return getStateString();
		}
		if (width==0) {
			s = "" + value;
		} else {
			limit = Math.abs(Math.log10(Math.abs(value)));
			if ((value==0.0) || (limit<(width-3))) {
				fmt = "%" + String.format("%d", width) + ".2f";
			} else {
				fmt = "%" + String.format("%d", width) + "g";
			}
			s = String.format(fmt, value);
		}
		return s;
	}

	public void checkStates(CaDouble v) {
		if (getState() != CaNumber.STATE_NORMAL) {
			v.setState(getState());
		} else if (this.value>v.upperlimit) {
			v.setState(CaNumber.STATE_POSINF);
		} else if (this.value<v.lowerlimit) {
			v.setState(CaNumber.STATE_NEGINF);
		} else {
			v.setValue(this.value);
		}
	}

	public void checkStates(CaComplex v) {
		if (getState() != CaNumber.STATE_NORMAL) {
			v.setState(getState());
		} else if (this.value>v.upperlimit) {
			v.setState(CaNumber.STATE_POSINF);
		} else if (this.value<v.lowerlimit) {
			v.setState(CaNumber.STATE_NEGINF);
		} else {
			v.setValue(this.value, 0.0);
		}
	}

	public void checkStates(CaLong v) {
		if (isNaN()) {
			v.setState(CaNumber.STATE_NAN);
		} else if (isPosZero()) {
			//?: v.value = 0;
			v.setState(CaNumber.STATE_POSZERO);
		} else if (isNegZero()) {
			//?: v.value = 0;
			v.setState(CaNumber.STATE_NEGZERO);
		} else if (isInf()) {
			v.setState(getState());
		} else if (this.value>v.upperlimit) {
			v.setState(CaNumber.STATE_POSINF);
		} else if (this.value<v.lowerlimit) {
			v.setState(CaNumber.STATE_NEGINF);
		} else {
			v.setValue(Math.round(this.value));
		}
	}

	public void checkStates(CaRatio v) {
		if (getState() != CaNumber.STATE_NORMAL) {
			v.setState(getState());
		} else if (this.value>v.upperlimit) {
			v.setState(CaNumber.STATE_POSINF);
		} else if (this.value<v.lowerlimit) {
			v.setState(CaNumber.STATE_NEGINF);
		} else {
			v.calcRatio(this.value, 0.0); // 0.0 = epsilon
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
		CaRatio v = new CaRatio();
		checkStates(v);
		return v;
	}

}
