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

import java.util.Vector;

/**
 * @author Rainer Schwarze
 *
 */
public class UnitContext {
	Vector<SubField> vSubFields;
	Vector<SystemOfUnits> vSous;

	String cacheSousString;
	String cacheFieldsString;
	
	/**
	 * 
	 */
	public UnitContext() {
		super();
		vSubFields = new Vector<SubField>();
		vSous = new Vector<SystemOfUnits>();
		cacheSousString = "";
		cacheFieldsString = "";
	}

	/**
	 * @return	Returns display of Sous.
	 */
	public String getSousString() {
		return cacheSousString;
	}

	/**
	 * @return	Returns display of Fields.
	 */
	public String getFieldsString() {
		return cacheFieldsString;
	}

	protected void updateSousCache() {
		String s = "";
		for (SystemOfUnits sou : getSystemOfUnits()) {
			if (s.length()>0) s += ",";
			s += sou.getSymbol();
		}
		cacheSousString = s;
	}

	protected void updateFieldsCache() {
		String s = "";
		for (SubField sf : getSubFields()) {
			if (s.length()>0) s += ",";
			s += sf.getId();
		}
		cacheFieldsString = s;
	}
	
	/**
	 * 
	 */
	public void clear() {
		vSubFields.clear();
		vSous.clear();
		updateSousCache();
		updateFieldsCache();
	}

	/**
	 * 
	 */
	public void clearFields() {
		vSubFields.clear();
		updateFieldsCache();
	}

	/**
	 * 
	 */
	public void clearSous() {
		vSous.clear();
		updateSousCache();
	}

	/**
	 * @param sf
	 */
	public void addSubField(SubField sf) {
		if (vSubFields.contains(sf)) return;
		vSubFields.add(sf);
		updateFieldsCache();
	}

	/**
	 * @param sf
	 */
	public void removeSubField(SubField sf) {
		vSubFields.remove(sf);
		updateFieldsCache();
	}

	/**
	 * @param sou
	 */
	public void addSystemOfUnits(SystemOfUnits sou) {
		if (vSous.contains(sou)) return;
		vSous.add(sou);
		updateSousCache();
	}

	/**
	 * @param sou
	 */
	public void removeSystemOfUnits(SystemOfUnits sou) {
		vSous.remove(sou);
		updateSousCache();
	}

	/**
	 * @return	Returns the list of subfields.
	 */
	public Vector<SubField> getSubFields() {
		return vSubFields;
	}

	/**
	 * @return	Returns the list of SystemOfUnits.
	 */
	public Vector<SystemOfUnits> getSystemOfUnits() {
		return vSous;
	}
}
