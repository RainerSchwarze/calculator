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
public class Constants {

	/** placeholder for nothing */
	public final static int PRI_QTY_NONE = 0;
	/** quantity length */
	public final static int PRI_QTY_LENGTH = 1;
	/** quantity time */
	public final static int PRI_QTY_TIME = 2;
	/** quantity mass */
	public final static int PRI_QTY_MASS = 3;
	/** quantity temperature */
	public final static int PRI_QTY_TEMPERATURE = 4;
	/** quantity electric current */
	public final static int PRI_QTY_ELCURRENT = 5;
	/** quantity luminosity */
	public final static int PRI_QTY_LUMINOSITY = 6;
	/** quantity amount of substance */
	public final static int PRI_QTY_AMTSUBSTANCE = 7;

	/** constant for domain id */
	public final static char CH_DOM = '@';
	
	/** constant for factor mul mode */
	public final static char CH_FAC_MUL = '*';
	/** constant for factor div mode */
	public final static char CH_FAC_DIV = '/';
	/** constant for unit mul mode */
	public final static char CH_UNT_MUL = '.';
	/** constant for unit div mode */
	public final static char CH_UNT_DIV = ':';
	/** constant for unit symbol mul mode */
	public final static char CH_UNTSYM_MUL = '_';
	/** constant for unit symbol div mode */
	public final static char CH_UNTSYM_DIV = '~';

	/** regex for splitting ids into the mul/div fields for fac and unt */
	public final static String REGEX_SPLIT_ID = "(?=\\.|\\*|\\:|\\/)";
	/** regex for splitting symbols into the mul/div fields */
	public final static String REGEX_SPLIT_SYMBOL = "(?=\\_|\\~)";

	/** id for id */
	public final static int UNTID_ID = 0;
	/** id for normalized id */
	public final static int UNTID_NORMID = 1;
	/** id for canonical id */
	public final static int UNTID_CANID = 2;
	/** id for normalized canonical id */
	public final static int UNTID_NORMCANID = 3;
	/** id for rooted id */
	public final static int UNTID_ROOTEDID = 4;

	/**
	 * 
	 */
	protected Constants() {
		super();
	}

}
