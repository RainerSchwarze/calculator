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
package de.admadic.calculator.modules.matrx.ui;

import java.util.Locale;

/**
 * @author Rainer Schwarze
 *
 */
public class FloatingPointFormatter {
	String formatString;
	Locale locale;

	static final FloatingPointFormatter FPF_SIMPLE = createSimpleFormatter();
	
	/**
	 * No direct constructor calls.
	 */
	private FloatingPointFormatter() {
		super();
	}

	/**
	 * No direct constructor calls.
	 * @param fmtStr 
	 * @param locale 
	 */
	private FloatingPointFormatter(String fmtStr, Locale locale) {
		super();
		this.formatString = fmtStr;
		this.locale = locale;
	}

	/**
	 * @return	Returns a simple formatter.
	 */
	public static FloatingPointFormatter getSimpleFormatter() {
		return FPF_SIMPLE;
	}

	/**
	 * @return	Returns a simple formatter.
	 */
	public static FloatingPointFormatter createSimpleFormatter() {
		FloatingPointFormatter mf = new FloatingPointFormatter();
		return mf;
	}

	/**
	 * @param fmtStr
	 * @param locale
	 * @return	Returns the formatter for the given parameters.
	 */
	public static FloatingPointFormatter createFormatter(String fmtStr, Locale locale) {
		FloatingPointFormatter mf = new FloatingPointFormatter(fmtStr, locale);
		return mf;
	}
	
	/**
	 * @param v
	 * @return	Returns the value as String.
	 */
	public String format(Double v) {
		if (v==null) return null;
		if (formatString==null) return v.toString();
		return String.format(locale, formatString, v);
	}

	/**
	 * @return Returns the formatString.
	 */
	public String getFormatString() {
		return formatString;
	}

	/**
	 * @param formatString The formatString to set.
	 */
	public void setFormatString(String formatString) {
		this.formatString = formatString;
	}
}
