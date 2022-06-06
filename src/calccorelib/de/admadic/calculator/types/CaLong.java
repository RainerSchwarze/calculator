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
package de.admadic.calculator.types;

/**
 * @author Rainer Schwarze
 *
 */
public class CaLong extends CaNumber implements Cloneable {
	long value = 0;
	long upperlimit = Long.MAX_VALUE;
	long lowerlimit = Long.MIN_VALUE;

	public CaLong() {
		super();
	}

	public CaLong(long v) {
		super();
		value = v;
	}

	public CaLong clone() throws CloneNotSupportedException {
		CaLong v = (CaLong)super.clone();
		cloneTo(v);
		return v;
	}
	public void cloneTo(CaLong v) {
		super.cloneTo(v);
		v.value = this.value;
		v.upperlimit = this.upperlimit;
		v.lowerlimit = this.lowerlimit;
	}
	
	public void setValue(long v) {
		value = v;
		setStateNormal();
	}

	public void setZero() {
		super.setZero();
		value = 0;
	}

	public void setUnity() {
		super.setUnity();
		value = 1;
	}

	public void checkStates(CaLong v) {
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

	public void checkStates(CaDouble v) {
		if (getState() != CaNumber.STATE_NORMAL) {
			v.setState(getState());
		} else if (this.value>v.upperlimit) {
			v.setState(CaNumber.STATE_POSINF);
		} else if (this.value<v.lowerlimit) {
			v.setState(CaNumber.STATE_NEGINF);
		} else {
			v.setValue((double)this.value);
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
			v.setValue(this.value, 1);
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
	 * @see de.admadic.calculator.Number#complexValue()
	 */
	public CaComplex complexValue() {
		CaComplex v;
		CaDouble vd;
		vd = doubleValue();
		v = vd.complexValue();
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
	 * @see de.admadic.calculator.Number#floatValue()
	 */
	public CaFloat floatValue() {
		CaFloat v = new CaFloat();
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
