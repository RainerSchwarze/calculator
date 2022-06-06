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
package de.admadic.calculator.modules.indxp.core;

import java.util.EventObject;

/**
 * @author Rainer Schwarze
 *
 */
public class DataEvent extends EventObject {
	/** structure of factors changed */
	public final static int FACTORS_STRUCT_CHANGED = 1<<0;
	/** contents of factors changed */
	public final static int FACTORS_DATA_CHANGED = 1<<1;
	/** structure of factor interactions changed */
	public final static int FACTORINTERACTIONS_STRUCT_CHANGED = 1<<2;
	/** contents of factor interactions changed */
	public final static int FACTORINTERACTIONS_DATA_CHANGED = 1<<3;
	/** structure of runs changed */
	public final static int RUNS_STRUCT_CHANGED = 1<<4;
	/** contents of runs changed */
	public final static int RUNS_DATA_CHANGED = 1<<5;
	/** structure of expresults changed */
	public final static int EXPRESULTS_STRUCT_CHANGED = 1<<6;
	/** contents of expresults changed */
	public final static int EXPRESULTS_DATA_CHANGED = 1<<7;
	/** structure of levelanalysis changed */
	public final static int LEVELANALYSIS_STRUCT_CHANGED = 1<<8;
	/** contents of levelanalysis changed */
	public final static int LEVELANALYSIS_DATA_CHANGED = 1<<9;
	/** minor change */
	public final static int DATA_IS_DIRTY = 1<<10;

	/** constant for all has changed */
	public final static int ALL_CHANGED = 0x07FF;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int mask;

	/**
	 * @param source
	 * @param mask 
	 */
	public DataEvent(Object source, int mask) {
		super(source);
		this.mask = mask;
	}

	/**
	 * @return Returns the mask.
	 */
	public int getMask() {
		return mask;
	}
}
