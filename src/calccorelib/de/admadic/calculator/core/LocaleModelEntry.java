package de.admadic.calculator.core;

import java.util.Locale;
import java.util.Vector;

/**
 * @author Rainer Schwarze
 *
 */
public class LocaleModelEntry {
	Locale locale;
	Locale displayLocale;

	/** display only language */
	public static final int TYPE_LANG = 1;
	/** display only country */
	public static final int TYPE_COUNTRY = 2;
	/** display language and country */
	public static final int TYPE_LANG_AND_COUNTRY = 3;

	int type = TYPE_LANG_AND_COUNTRY;

	/**
	 * @param locale
	 * @param displayLocale
	 */
	public LocaleModelEntry(Locale locale, Locale displayLocale) {
		this(locale, displayLocale, TYPE_LANG_AND_COUNTRY);
	}

	/**
	 * @param locale
	 * @param displayLocale
	 * @param type 
	 */
	public LocaleModelEntry(Locale locale, Locale displayLocale, int type) {
		super();
		this.locale = locale;
		this.displayLocale = displayLocale;
		this.type = type;
		if (this.displayLocale==null) {
			this.displayLocale = Locale.getDefault();
		}
	}
	/**
	 * @return	Returns a String representation
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String c = locale.getCountry();
		String l = locale.getLanguage();
		String s = "";
		switch (type) {
		case TYPE_LANG:
			s += (l.equals("")) ? " " : l;
			s += "\t";
			s += locale.getDisplayLanguage(displayLocale);
			break;
		case TYPE_COUNTRY:
			s += (c.equals("")) ? " " : c;
			s += "\t";
			s += locale.getDisplayCountry(displayLocale);
			break;
		case TYPE_LANG_AND_COUNTRY:
		default:
			s += (l.equals("")) ? " " : l;
			s += "\t";
			s += (c.equals("")) ? " " : c;
			s += "\t";
			s += locale.getDisplayName(displayLocale);
			break;
		}
		return s;
	}

	/**
	 * @return Returns the locale.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @return Returns the displayLocale.
	 */
	public Locale getDisplayLocale() {
		return displayLocale;
	}

	/**
	 * @param includeMajors 
	 * @param displayLocale 
	 * @return	Returns a list of LocaleModelEntry representing the 
	 * 			available Locales.
	 */
	public static Vector<LocaleModelEntry> getLocaleList(
			boolean includeMajors,
			Locale displayLocale) {
		Vector<LocaleModelEntry> v = new Vector<LocaleModelEntry>();
		Locale [] la = Locale.getAvailableLocales();
		if (includeMajors) {
			Locale dl = displayLocale;
			int ty = LocaleModelEntry.TYPE_LANG_AND_COUNTRY;
			v.add(new LocaleModelEntry(Locale.ENGLISH, dl, ty));
			v.add(new LocaleModelEntry(Locale.FRENCH, dl, ty));
			v.add(new LocaleModelEntry(Locale.GERMAN, dl, ty));
			v.add(new LocaleModelEntry(Locale.ITALIAN, dl, ty));
			v.add(new LocaleModelEntry(Locale.CHINESE, dl, ty));
			v.add(new LocaleModelEntry(Locale.JAPANESE, dl, ty));
			v.add(new LocaleModelEntry(Locale.KOREAN, dl, ty));
			v.add(null);	// separator
		}
		for (Locale l : la) {
			v.add(new LocaleModelEntry(
					l, displayLocale, 
					LocaleModelEntry.TYPE_LANG_AND_COUNTRY));
		}
		return v;
	}

	/**
	 * @param includeMajors 
	 * @param displayLocale 
	 * @return	Returns a list of LocaleModelEntry representing the 
	 * 			available Languages.
	 */
	public static Vector<LocaleModelEntry> getLanguageList(
			boolean includeMajors,
			Locale displayLocale) {
		Vector<LocaleModelEntry> v = new Vector<LocaleModelEntry>();
		String [] sa = Locale.getISOLanguages();
		if (includeMajors) {
			Locale dl = displayLocale;
			int ty = LocaleModelEntry.TYPE_LANG;
			v.add(new LocaleModelEntry(Locale.ENGLISH, dl, ty));
			v.add(new LocaleModelEntry(Locale.FRENCH, dl, ty));
			v.add(new LocaleModelEntry(Locale.GERMAN, dl, ty));
			v.add(new LocaleModelEntry(Locale.ITALIAN, dl, ty));
			v.add(new LocaleModelEntry(Locale.CHINESE, dl, ty));
			v.add(new LocaleModelEntry(Locale.JAPANESE, dl, ty));
			v.add(new LocaleModelEntry(Locale.KOREAN, dl, ty));
			v.add(null);	// separator
		}
		for (String l : sa) {
			v.add(new LocaleModelEntry(
					new Locale(l, ""), displayLocale, 
					LocaleModelEntry.TYPE_LANG));
		}
		return v;
	}

	/**
	 * @param includeMajors 
	 * @param displayLocale 
	 * @return	Returns a list of LocaleModelEntry representing the 
	 * 			available Countries.
	 */
	public static Vector<LocaleModelEntry> getCountryList(
			boolean includeMajors,
			Locale displayLocale) {
		Vector<LocaleModelEntry> v = new Vector<LocaleModelEntry>();
		String [] sa = Locale.getISOCountries();
		if (includeMajors) {
			Locale dl = displayLocale;
			int ty = LocaleModelEntry.TYPE_COUNTRY;
			v.add(new LocaleModelEntry(Locale.US, dl, ty));
			v.add(new LocaleModelEntry(Locale.UK, dl, ty));
			v.add(new LocaleModelEntry(Locale.GERMANY, dl, ty));
			v.add(new LocaleModelEntry(Locale.FRANCE, dl, ty));
			v.add(new LocaleModelEntry(Locale.CANADA, dl, ty));
			v.add(new LocaleModelEntry(Locale.ITALY, dl, ty));
			v.add(new LocaleModelEntry(Locale.CHINA, dl, ty));
			v.add(new LocaleModelEntry(Locale.JAPAN, dl, ty));
			v.add(new LocaleModelEntry(Locale.KOREA, dl, ty));
			v.add(new LocaleModelEntry(Locale.TAIWAN, dl, ty));
			v.add(null);	// separator
		}
		for (String c : sa) {
			v.add(new LocaleModelEntry(
					new Locale("", c), displayLocale, 
					LocaleModelEntry.TYPE_COUNTRY));
		}
		return v;
	}

	/**
	 * @param v
	 * @param la
	 * @return	Returns the index of the first matching entry. -1 if there is
	 * 			none.
	 */
	public static int getFirstMatchForLanguage(
			Vector<LocaleModelEntry> v, String la) {
		for (int i = 0; i < v.size(); i++) {
			LocaleModelEntry e = v.get(i);
			if (e==null) continue;
			if (e.getLocale()==null) continue;
			if (e.getLocale().getLanguage()==null) continue;
			if (e.getLocale().getLanguage().equals(la))
				return i;
		}
		return -1;
	}

	/**
	 * @param v
	 * @param co
	 * @return	Returns the index of the first matching entry. -1 if there is
	 * 			none.
	 */
	public static int getFirstMatchForCountry(
			Vector<LocaleModelEntry> v, String co) {
		for (int i = 0; i < v.size(); i++) {
			LocaleModelEntry e = v.get(i);
			if (e==null) continue;
			if (e.getLocale().getCountry().equals(co))
				return i;
		}
		return -1;
	}

	/**
	 * @param v
	 * @param la
	 * @param co
	 * @return	Returns the first match for language and/or country.
	 */
	public static int getFirstMatchForLaCo(
			Vector<LocaleModelEntry> v, String la, String co) {
		boolean match;
		boolean laempty = la.equals("");
		boolean coempty = co.equals("");
		for (int i = 0; i < v.size(); i++) {
			LocaleModelEntry e = v.get(i);
			if (e==null) continue;
			match = true;
			if (!coempty && !e.getLocale().getCountry().equals(co))
				match = false;
			if (!laempty && !e.getLocale().getLanguage().equals(la))
				match = false;
			if (match) 
				return i;
		}
		return -1;
	}
}
