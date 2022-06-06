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

/**
 * @author Rainer Schwarze
 *
 */
public class ProcessorActionAttribute {
	int order;
	int priority;	// 0 reserved, 1 lowest
	final String pa;
	int type;
	String display;

	/**
	 * Number type action (constructs numbers)
	 */
	public final static int TYPE_NUM = 1;
	/**
	 * Operation type action (performs calculations)
	 */
	public final static int TYPE_OP = 2;
	/**
	 * Special action (may be Number or Operation depending on context)
	 */
	public final static int TYPE_SPC = 3;

	/**
	 * @param pa
	 * @param order
	 * @param priority
	 * @param type 
	 */
	public ProcessorActionAttribute(final String pa, int order, int priority, int type) {
		this(pa, null, order, priority, type);
	}

	/**
	 * @param pa
	 * @param display 
	 * @param order
	 * @param priority
	 * @param type 
	 */
	public ProcessorActionAttribute(
			final String pa, String display, int order, int priority, int type) {
		super();
		this.pa = pa;
		this.display = (display!=null) ? display : pa;
		this.order = order;
		this.priority = priority;
		this.type = type;
	}

	/**
	 * @return Returns the order.
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @return Returns the pa.
	 */
	public String getPa() {
		return pa;
	}

	/**
	 * @return Returns the priority.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return Returns the display.
	 */
	public String getDisplay() {
		return display;
	}
}
