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
package de.admadic.calculator.processor;

import java.util.EventObject;

/**
 * @author Rainer Schwarze
 *
 */
public class ProcessorEvent extends EventObject {
	/** */
	private static final long serialVersionUID = 1L;
	/** memory has changed */
	public final static int MEMORY = 1<<0;
	/** stack has changed */
	public final static int STACK = 1<<1;
	/** angular arg mode has changed, @since calulator 1.1.1 */
	public final static int ANGARGMODE = 1<<2;
	int mask;

	/**
	 * @param source
	 */
	public ProcessorEvent(Object source) {
		super(source);
	}

	/**
	 * @return Returns the mask.
	 */
	public int getMask() {
		return mask;
	}

	/**
	 * @param mask The mask to set.
	 */
	public void setMask(int mask) {
		this.mask = mask;
	}
}
