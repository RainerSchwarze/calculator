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

import java.util.Locale;

/**
 * The CaNumberFormatter class is the base class for Formatters for special
 * representations like CaDouble. There should be a subclass for every CaNumber 
 * subclass. (CaDoubleFormatter, CaIntegerFormatter, etc.)
 * 
 * FIXME: implement other Formatters! (Complex, Ratio, Float, ...)
 * 
 * @author Rainer Schwarze
 */
public interface CaNumberFormatter {
	/**
	 * @param number 
	 * @return	Returns a String representation of the number.
	 */
	public String formatNumber(CaNumber number);

	/**
	 * @param string 
	 * @param number A CaNumber to be used as a destination.
	 * @return Returns the parsed number.
	 * @throws NumberFormatException
	 */
	public CaNumber parseNumber(String string, CaNumber number) 
	throws NumberFormatException;

	/**
	 * @param locale
	 */
	public void setLocale(Locale locale);
}
