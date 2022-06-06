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
package de.admadic.calculator.core;

import java.util.Locale;

/**
 * @author Rainer Schwarze
 *
 */
public interface LocaleProvider {

	/**
	 * @return	Returns the default locale
	 */
	public abstract Locale getDefaultLocale();

	/**
	 * Returns the locale for the given id. 
	 * If the attribute "isSameAsDefault" is set, for all
	 * ids the default locale is returned.
	 * 
	 * @param id
	 * @return	Returns the locale for the given id.
	 */
	public abstract Locale queryLocale(int id);

	/**
	 * @param l
	 */
	public abstract void addLocaleListener(LocaleListener l);

	/**
	 * @param l
	 */
	public abstract void removeLocaleListener(LocaleListener l);
}