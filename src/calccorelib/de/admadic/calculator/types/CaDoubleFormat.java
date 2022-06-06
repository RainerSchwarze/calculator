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
 * @author Rainer Schwarze
 *
 */
public class CaDoubleFormat {
	int width;
	int prec;
	int type;
	Locale locale;

	/**
	 * Output format "auto" (= 'g')
	 */
	public static final int TYPE_AUTO = 0; 
	/**
	 * Output format "fixed" (= 'f')
	 */
	public static final int TYPE_FIXED = 1; 
	/**
	 * Output format "eng" (= 'e')
	 */
	public static final int TYPE_ENG = 2;
	String fmtCache;


	/**
	 * Creates an instance of an CaDoubleFormat.
	 */
	public CaDoubleFormat() {
		this(TYPE_AUTO, 1, 0);
	}

	/**
	 * @param type
	 * @param width
	 * @param prec
	 */
	public CaDoubleFormat(int type, int width, int prec) {
		super();
		this.type = type;
		this.width = width;
		this.prec = prec;
		this.locale = null;
		fmtCache = updateFmt(type, width, prec);
	}

	/**
	 * @return	Returns the string representation of this CaDoubleFormat.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "";
		s += type2String(type);
		s += "," + width;
		s += "," + prec;
		return s;
	}

	/**
	 * @param s
	 * @return	Returns a CaDoubleFormat from the given String representation.
	 */
	public static CaDoubleFormat valueOf(String s) {
		CaDoubleFormat f = null;
		String [] sa = s.split(",");
		if (sa.length!=3) return f;
		int type = string2Type(sa[0]);
		int width = Integer.parseInt(sa[1]);
		int prec = Integer.parseInt(sa[2]);
		f = new CaDoubleFormat(type, width, prec);
		return f;
	}

	/**
	 * Updates the fmt String.
	 */
	protected void updateFmt() {
		fmtCache = updateFmt(type, width, prec);
	}

	/**
	 * 
	 * @param type
	 * @param width
	 * @param prec
	 * @return	Returns the format String.
	 */
	protected static String updateFmt(int type, int width, int prec) {
		String fmt;
		switch (type) {
		case TYPE_FIXED: 
			if (width!=0) {
				fmt = "%" + width + "." + prec + "f";
			} else {
				fmt = "%" + "." + prec + "f";
			}
			break;
		case TYPE_ENG: 
			if (width!=0) {
				fmt = "%" + width + "." + prec + "e";
			} else {
				fmt = "%" + "." + prec + "e";
			}
			break;
		case TYPE_AUTO:
		default:
			// repeat AUTO:
			if (width!=0) {
				fmt = "%" + width + "." + prec + "g";
			} else {
				fmt = "%" + "." + prec + "g";
			}
			break;
		}
		return fmt;
	}

	/**
	 * @param type
	 * @return	Returns the string representation of the type.
	 */
	public static String type2String(int type) {
		switch (type) {
		case TYPE_AUTO:		return "auto";
		case TYPE_FIXED:	return "fixed";
		case TYPE_ENG:		return "eng";
		default:			return "auto";
		}
	}

	/**
	 * @param type
	 * @return	Returns the string representation of the type.
	 */
	public static String type2DisplayString(int type) {
		switch (type) {
		case TYPE_FIXED:	return "Fix.";
		case TYPE_ENG:		return "Eng.";
		case TYPE_AUTO:
		default:			return "Auto";
		}
	}

	/**
	 * @param typeStr
	 * @return	Returns the type id for the given String.
	 */
	public static int string2Type(String typeStr) {
		if (typeStr.equals("auto")) {
			return TYPE_AUTO;
		} else if (typeStr.equals("fixed")) {
			return TYPE_FIXED;
		} else if (typeStr.equals("eng")) {
			return TYPE_ENG;
		} else {
			return TYPE_AUTO;
		}
	}

	/**
	 * @param v
	 * @return	Returns the formatted String for the given value.
	 */
	public String format(CaDouble v) {
		// if locale is null, the default is used.
		return String.format(locale, fmtCache, Double.valueOf(v.getValue()));
	}

	/**
	 * This method uses String.format and does not specify a Locale!
	 * @param type
	 * @param width
	 * @param prec
	 * @param v
	 * @return	Returns the formatted String.
	 */
	public static String format(int type, int width, int prec, CaDouble v) {
		String fmt = updateFmt(type, width, prec);
		return String.format(fmt, Double.valueOf(v.getValue()));
	}

	/**
	 * @param locale 
	 * @param type
	 * @param width
	 * @param prec
	 * @param v
	 * @return	Returns the formatted String.
	 */
	public static String format(Locale locale, int type, int width, int prec, CaDouble v) {
		String fmt = updateFmt(type, width, prec);
		return String.format(locale, fmt, Double.valueOf(v.getValue()));
	}

	/**
	 * @return Returns the prec.
	 */
	public int getPrec() {
		return prec;
	}

	/**
	 * @param prec The prec to set.
	 */
	public void setPrec(int prec) {
		this.prec = prec;
		updateFmt();
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
		updateFmt();
	}

	/**
	 * @return Returns the width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setWidth(int width) {
		this.width = width;
		updateFmt();
	}

	/**
	 * @return Returns the locale.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale The locale to set.
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
