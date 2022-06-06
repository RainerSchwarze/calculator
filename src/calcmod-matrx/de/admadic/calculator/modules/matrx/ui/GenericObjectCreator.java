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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * @author Rainer Schwarze
 *
 */
public class GenericObjectCreator {
	// FIXME: check whether that may be put into the calccorelib or admadiclib.
	Hashtable<Class, Method> class2method;

	/**
	 * 
	 */
	public GenericObjectCreator() {
		super();
		class2method = new Hashtable<Class, Method>();
	}

	/**
	 * @param hint
	 * @param svalue
	 * @return	Returns an instance of hint created from svalue.
	 */
	public Object valueOf(Object hint, String svalue) {
		Object o = null;
		Method m = null;
		if (hint==null) return null;
		Class cls = hint.getClass();
		try {
			if (!class2method.containsKey(cls)) {
				m = tryToInsertClass(cls);
			} else {
				m = class2method.get(cls);
			}
			if (m==null) return null;
			o = m.invoke(null, new Object[]{svalue});
		} catch (IllegalArgumentException e) {
			// e.printStackTrace();
		} catch (IllegalAccessException e) {
			// e.printStackTrace();
		} catch (InvocationTargetException e) {
			// e.printStackTrace();
		} catch (SecurityException e) {
			// e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// e.printStackTrace();
		}
		return o;
	}

	private Method tryToInsertClass(Class cls) throws SecurityException, NoSuchMethodException {
		Method m = null;
		m = cls.getMethod("valueOf", new Class[]{String.class});
		if (m==null) return null;
		class2method.put(cls, m);
		return m;
	}
}
