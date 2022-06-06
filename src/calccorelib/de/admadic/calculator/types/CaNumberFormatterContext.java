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

import java.util.Hashtable;

/**
 * @author Rainer Schwarze
 *
 */
public class CaNumberFormatterContext {
	Hashtable<Class, CaNumberFormatter> numberFormatters;

	// FIXME: do we need the lookup-class/call formatter code in here?

	/**
	 * 
	 */
	public CaNumberFormatterContext() {
		super();
		numberFormatters = new Hashtable<Class, CaNumberFormatter>();
	}

	/**
	 * @param cls
	 * @return	Returns the CaNumberFormatter for the given class.
	 * @see java.util.Hashtable#get(java.lang.Object)
	 */
	public synchronized CaNumberFormatter get(Class cls) {
		return numberFormatters.get(cls);
	}

	/**
	 * @param cls
	 * @param value
	 * @return	Returns?
	 * @see java.util.Hashtable#put(Object, Object)
	 */
	public synchronized CaNumberFormatter put(Class cls, CaNumberFormatter value) {
		return numberFormatters.put(cls, value);
	}

	/**
	 * @param cls
	 * @return	Returns the removed object.
	 * @see java.util.Hashtable#remove(java.lang.Object)
	 */
	public synchronized CaNumberFormatter remove(Object cls) {
		return numberFormatters.remove(cls);
	}

	/**
	 * @param number
	 * @return	Returns the CaNumberFormatter registered for the class of the
	 * given CaNumber.
	 * @throws NullPointerException when number is null.
	 */
	public synchronized CaNumberFormatter get(CaNumber number) {
		Class cls = number.getClass();
		return get(cls);
	}
}
