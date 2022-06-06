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
public interface IConverter {
	/**
	 * @param conv
	 */
	public abstract void chainIn(IConverter conv);

	/**
	 * @param value
	 * @return	Returns the converted value.
	 */
	public abstract Double convert(Double value);

	/**
	 * @param value
	 * @return	Returns the converted value.
	 */
	public abstract double convert(double value);

	/**
	 * @param value
	 * @return	Returns the inversely converted value.
	 */
	public abstract Double iconvert(Double value);

	/**
	 * @param value
	 * @return	Returns the inversely converted value.
	 */
	public abstract double iconvert(double value);

	/**
	 * @param value
	 * @return	Returns the converted value.
	 */
	public abstract Double convertScaled(Double value);

	/**
	 * @param value
	 * @return	Returns the converted value.
	 */
	public abstract double convertScaled(double value);

	/**
	 * @param value
	 * @return	Returns the inversely converted value.
	 */
	public abstract Double iconvertScaled(Double value);

	/**
	 * @param value
	 * @return	Returns the inversely converted value.
	 */
	public abstract double iconvertScaled(double value);

	/**
	 * @return	Returns the context for the converter.
	 */
	public abstract ConverterContext getContext();
}
