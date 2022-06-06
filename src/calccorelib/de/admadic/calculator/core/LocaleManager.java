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

import java.util.Hashtable;
import java.util.Locale;

import javax.swing.event.EventListenerList;

/**
 * @author Rainer Schwarze
 *
 */
public class LocaleManager implements LocaleProvider {
	Hashtable<Integer,Locale> locales;

	boolean sameAsDefault;

	/** default locale, if other specs don't match */
	static final Integer I_LOCALE_DEFAULT = 
		Integer.valueOf(LocaleConstants.LOCALE_DEFAULT);
	/** locale for input (keyboard) */
	static final Integer I_LOCALE_INPUT = 
		Integer.valueOf(LocaleConstants.LOCALE_INPUT);
	/** locale for output (display, print) */
	static final Integer I_LOCALE_OUTPUT = 
		Integer.valueOf(LocaleConstants.LOCALE_OUTPUT);
	/** locale for export (export as text) */
	static final Integer I_LOCALE_EXPORT = 
		Integer.valueOf(LocaleConstants.LOCALE_EXPORT);

	EventListenerList listenerList = new EventListenerList();

	/**
	 * 
	 */
	public LocaleManager() {
		super();
		locales = new Hashtable<Integer,Locale>();
	}

	/**
	 * Map int constants to Integer constants to be used in hashtables.
	 * @param id
	 * @return	Returns the Integer constant corresponding to the int const.
	 */
	private Integer idToItg(int id) {
		switch (id) {
		case LocaleConstants.LOCALE_DEFAULT: return I_LOCALE_DEFAULT;
		case LocaleConstants.LOCALE_INPUT: return I_LOCALE_INPUT;
		case LocaleConstants.LOCALE_OUTPUT: return I_LOCALE_OUTPUT;
		case LocaleConstants.LOCALE_EXPORT: return I_LOCALE_EXPORT;
		default:
			return I_LOCALE_DEFAULT;
		}
	}
	
	/**
	 * @return	Returns the default locale.
	 * @see de.admadic.calculator.core.LocaleProvider#getDefaultLocale()
	 */
	public Locale getDefaultLocale() {
		return locales.get(I_LOCALE_DEFAULT);
	}
	
	/**
	 * @param id
	 * @return	Returns the locale for the given id.
	 */
	public Locale getLocale(int id) {
		Integer i = idToItg(id);
		if (!locales.containsKey(i)) {
			throw new ArrayIndexOutOfBoundsException(id);
		}
		return locales.get(i);
	}

	/**
	 * @param id
	 * @param locale
	 */
	public void setLocale(int id, Locale locale) {
		Integer i = idToItg(id);
		locales.put(i, locale);
	}

	/**
	 * @param locale
	 */
	public void setAllLocales(Locale locale) {
		locales.put(I_LOCALE_DEFAULT, locale);
		locales.put(I_LOCALE_INPUT, locale);
		locales.put(I_LOCALE_OUTPUT, locale);
		locales.put(I_LOCALE_EXPORT, locale);
	}


	/**
	 * @return Returns the sameAsDefault.
	 */
	public boolean isSameAsDefault() {
		return sameAsDefault;
	}

	/**
	 * @param sameAsDefault The sameAsDefault to set.
	 */
	public void setSameAsDefault(boolean sameAsDefault) {
		this.sameAsDefault = sameAsDefault;
	}

	/**
	 * @param id
	 * @return	Return the locale for the id.
	 * @see de.admadic.calculator.core.LocaleProvider#queryLocale(int)
	 */
	public Locale queryLocale(int id) {
		if (isSameAsDefault()) {
			id = LocaleConstants.LOCALE_DEFAULT;
		}
		return getLocale(id);
	}

	/**
	 * @param l
	 * @see de.admadic.calculator.core.LocaleProvider#addLocaleListener(de.admadic.calculator.core.LocaleListener)
	 */
	public void addLocaleListener(LocaleListener l) {
	    listenerList.add(LocaleListener.class, l);
	}

	/**
	 * @param l
	 * @see de.admadic.calculator.core.LocaleProvider#removeLocaleListener(de.admadic.calculator.core.LocaleListener)
	 */
	public void removeLocaleListener(LocaleListener l) {
	    listenerList.remove(LocaleListener.class, l);
	}

	/**
	 * 
	 */
	protected void fireLocaleChanged() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		LocaleEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==LocaleListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new LocaleEvent(this);
				((LocaleListener)listeners[i+1]).localeChanged(e);
			}
		}
	}

	/**
	 * 
	 */
	protected void fireLocaleNumberFormatChanged() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		LocaleNumberFormatEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==LocaleListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new LocaleNumberFormatEvent(this);
				((LocaleListener)listeners[i+1]).localeNumberFormatChanged(e);
			}
		}
	}

	/**
	 * 
	 */
	public void notifyLocaleChanged() {
		fireLocaleChanged();
	}

	/**
	 * 
	 */
	public void notifyLocaleNumberFormatChanged() {
		fireLocaleNumberFormatChanged();
	}
}
