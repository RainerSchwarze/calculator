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
package de.admadic.units.core;

/**
 * @author Rainer Schwarze
 *
 */
public class ConverterMul extends AbstractConverter {
	Double m;

	/**
	 * @param m 
	 * 
	 */
	public ConverterMul(Double m) {
		super();
		this.m = m;
	}

	/**
	 * @param value
	 * @return	Returns the converted value.
	 * @see de.admadic.units.core.AbstractConverter#convertImpl(java.lang.Double)
	 */
	@Override
	public Double convertImpl(Double value) {
		if (value==null) return null;
		return Double.valueOf(value.doubleValue()*m.doubleValue());
	}

	/**
	 * @param value
	 * @return	Returns the inversely converted value.
	 * @see de.admadic.units.core.AbstractConverter#iconvertImpl(java.lang.Double)
	 */
	@Override
	public Double iconvertImpl(Double value) {
		if (value==null) return null;
		return Double.valueOf(value.doubleValue()/m.doubleValue());
	}

	/**
	 * @param value
	 * @return	Returns the scaled conversion.
	 * @see de.admadic.units.core.AbstractConverter#convertScaledImpl(java.lang.Double)
	 */
	@Override
	public Double convertScaledImpl(Double value) {
		// same as std
		return convertImpl(value);
	}

	/**
	 * @param value
	 * @return	Return the inversely converted value scaled.
	 * @see de.admadic.units.core.AbstractConverter#iconvertScaledImpl(java.lang.Double)
	 */
	@Override
	public Double iconvertScaledImpl(Double value) {
		// same as std
		return iconvertImpl(value);
	}

	/**
	 * @return	Returns the m-parameter
	 */
	public Double getM() {
		return m;
	}
}
