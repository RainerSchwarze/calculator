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
public class CaNumber 
implements Cloneable, CaNumberValue, CaNumberUnity, CaNumberFmt {
	// FIXME: maybe we do not need to have a format/parse method inside the 
	// CaNumber hierarchy?

	/** number state normal (not NaN etc.) */
	public static final int STATE_NORMAL = 0;
	/** number is NaN */
	public static final int STATE_NAN = 1;
	/** number is +Infinite */
	public static final int STATE_POSINF = 2;
	/** number is -Infinite */
	public static final int STATE_NEGINF = 3;
	/** number is +Zero */
	public static final int STATE_POSZERO = 4;
	/** number is -Zero */
	public static final int STATE_NEGZERO = 5;

	int state = STATE_NORMAL;

	/**
	 * 
	 */
	public CaNumber() {
		setZero();
	}

	/**
	 * @return	Returns a clone of this CaNumber.
	 * @throws CloneNotSupportedException
	 * @see java.lang.Object#clone()
	 */
	@Override
	public CaNumber clone() throws CloneNotSupportedException {
		CaNumber v = (CaNumber)super.clone();
		cloneTo(v);
		return v;
	}

	/**
	 * @param v
	 */
	public void cloneTo(CaNumber v) {
		v.state = this.state;
	}

	/**
	 * Base function to set the Number to Zero. Since the base class does not
	 * know about actual values, it only sets the Normal state. Subclasses must
	 * implement the actual zero-value-setting.
	 * @see de.admadic.calculator.types.CaNumberUnity#setZero()
	 */
	public void setZero() {
		// no zero there
		setStateNormal();
	}

	/**
	 * Base function to set the Number to Unity. Since the base class does not
	 * know about actual values, it only sets the Normal state. Subclasses must
	 * implement the actual unity-value-setting.
	 * @see de.admadic.calculator.types.CaNumberUnity#setUnity()
	 */
	public void setUnity() {
		// no zero there
		setStateNormal();
	}

	/**
	 * @return	Returns a String representation of the number state.
	 */
	public String getStateString() {
		switch (state) {
		case STATE_NORMAL: 	return new String("Normal");
		case STATE_NAN: 	return new String("NaN");
		case STATE_POSINF: 	return new String("+Inf");
		case STATE_NEGINF: 	return new String("-Inf");
		case STATE_POSZERO:	return new String("+Zero");
		case STATE_NEGZERO:	return new String("-Zero");
		default:
			return new String("unknown");
		}
	}
	
	/**
	 * Sets the state of this CaNumber to the specified state.
	 * @param state
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * Sets the state of this CaNumber to NORMAL.
	 */
	public void setStateNormal() { 
		setState(STATE_NORMAL); 
	}

	/**
	 * Returns the state of this CaNumber.
	 * @return	Returns the state of this CaNumber.
	 */
	public int getState() {
		return state;
	}

	/** @return Returns true, if the state is not normal. */
	public boolean isNotNormal() { return (state!=STATE_NORMAL); }
	/** @return Returns true, if the state is NaN. */
	public boolean isNaN() { return (state==STATE_NAN); }
	/** @return Returns true, if the state PosInf or NegInf. */
	public boolean isInf() { return ((state==STATE_POSINF) || 
									 (state==STATE_NEGINF)); } 
	/** @return Returns true, if the state is PosInf. */
	public boolean isPosInf() { return (state==STATE_POSINF); } 
	/** @return Returns true, if the state is NegInf. */
	public boolean isNegInf() { return (state==STATE_NEGINF); } 
	/** @return Returns true, if the state is PosZero. */
	public boolean isPosZero() { return (state==STATE_POSZERO); } 
	/** @return Returns true, if the state is NegZero. */
	public boolean isNegZero() { return (state==STATE_NEGZERO); } 

	/** Sets the state of this CaNumber to NaN. */
	public void setNaN() { state = STATE_NAN; }
	/** Sets the state of this CaNumber to PosInf. */
	public void setPosInf() { state = STATE_POSINF; } 
	/** Sets the state of this CaNumber to NegInf. */
	public void setNegInf() { state = STATE_NEGINF; } 
	/** Sets the state of this CaNumber to PosZero. */
	public void setPosZero() { state = STATE_POSZERO; } 
	/** Sets the state of this CaNumber to NegZero. */
	public void setNegZero() { state = STATE_NEGZERO; } 

	/**
	 * @return Returns the CaInteger value of this CaNumber.
	 * @see de.admadic.calculator.types.CaNumberValue#intValue()
	 */
	public CaInteger intValue() {
		return null;
	}

	/**
	 * @return Returns the CaByte value of this CaNumber.
	 * @see de.admadic.calculator.types.CaNumberValue#byteValue()
	 */
	public CaByte byteValue() {
		return null;
	}

	/**
	 * @return Returns the CaComplex value of this CaNumber.
	 * @see de.admadic.calculator.types.CaNumberValue#complexValue()
	 */
	public CaComplex complexValue() {
		return null;
	}

	/**
	 * @return Returns the CaDouble value of this CaNumber.
	 * @see de.admadic.calculator.types.CaNumberValue#doubleValue()
	 */
	public CaDouble doubleValue() {
		return null;
	}

	/**
	 * @return Returns the CaFloat value of this CaNumber.
	 * @see de.admadic.calculator.types.CaNumberValue#floatValue()
	 */
	public CaFloat floatValue() {
		return null;
	}

	/**
	 * @return Returns the CaLong value of this CaNumber.
	 * @see de.admadic.calculator.types.CaNumberValue#longValue()
	 */
	public CaLong longValue() {
		return null;
	}

	/**
	 * @return Returns the CaRatio value of this CaNumber.
	 * @see de.admadic.calculator.types.CaNumberValue#ratioValue()
	 */
	public CaRatio ratioValue() {
		return null;
	}

	/**
	 * @return Returns the CaShort value of this CaNumber.
	 * @see de.admadic.calculator.types.CaNumberValue#shortValue()
	 */
	public CaShort shortValue() {
		return null;
	}

	// //////////////////////////////////////////////////////////
	// interface for CaNumberFmt:
	// //////////////////////////////////////////////////////////
	
	/**
	 * @param fmtr
	 * @return	Returns the String representation for this instance
	 * based on the given CaNumberFormatter.
	 * @see de.admadic.calculator.types.CaNumberFmt#formatNumber(de.admadic.calculator.types.CaNumberFormatter)
	 * @throws NullPointerException when fmtr is null.
	 */
	public String formatNumber(CaNumberFormatter fmtr) {
		return fmtr.formatNumber(this);
	}

	/**
	 * @param string
	 * @param fmtr
	 * @throws NumberFormatException
	 * @see de.admadic.calculator.types.CaNumberFmt#parseNumber(java.lang.String, de.admadic.calculator.types.CaNumberFormatter)
	 */
	public void parseNumber(String string, CaNumberFormatter fmtr) throws NumberFormatException {
		fmtr.parseNumber(string, this);
	}

	/**
	 * @param fmtctx
	 * @return	Returns a String representation of this instance.
	 * @see de.admadic.calculator.types.CaNumberFmt#formatNumber(de.admadic.calculator.types.CaNumberFormatterContext)
	 */
	public String formatNumber(CaNumberFormatterContext fmtctx) {
		CaNumberFormatter fmtr = fmtctx.get(this);
		return formatNumber(fmtr);
	}

	/**
	 * @param string
	 * @param fmtctx
	 * @throws NumberFormatException
	 * @see de.admadic.calculator.types.CaNumberFmt#parseNumber(java.lang.String, de.admadic.calculator.types.CaNumberFormatterContext)
	 */
	public void parseNumber(String string, CaNumberFormatterContext fmtctx) throws NumberFormatException {
		CaNumberFormatter fmtr = fmtctx.get(this);
		parseNumber(string, fmtr);
	}
}
