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

import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Rainer Schwarze
 *
 */
public class UnitFilter {
	// filter sets:
	Vector<IField> filterFields;
	Vector<SystemOfUnits> filterSous;
	Vector<Domain> filterDomains;

	UnitManager um;

	Vector<Object> hashkeys;
	Hashtable<Object,Hashtable<Object,Boolean>> bighash;
	
	/**
	 * @param um 
	 * 
	 */
	public UnitFilter(UnitManager um) {
		super();
		filterFields = new Vector<IField>();
		filterSous = new Vector<SystemOfUnits>();
		filterDomains = new Vector<Domain>();
		this.um = um;
	}

	/**
	 * @param field
	 */
	public void addField(IField field) {
		if (filterFields.contains(field)) return;
		filterFields.add(field);
	}

	/**
	 * @param sou
	 */
	public void addSystemOfUnits(SystemOfUnits sou) {
		if (filterSous.contains(sou)) return;
		filterSous.add(sou);
	}

	/**
	 * @param domain
	 */
	public void addDomain(Domain domain) {
		filterDomains.add(domain);
	}

	/**
	 * 
	 */
	public void clear() {
		filterFields.clear();
		filterSous.clear();
		filterDomains.clear();
	}

	/**
	 * 
	 */
	public void updateHashes() {
		hashkeys = new Vector<Object>();
		bighash = new Hashtable<Object,Hashtable<Object,Boolean>>();

		// hashkeys.addAll(um.getFields());
		hashkeys.addAll(um.getSubFields());
		hashkeys.addAll(um.getSystemOfUnits());
		hashkeys.addAll(um.getDomains());

		for (Object o : hashkeys) {
			bighash.put(o, new Hashtable<Object,Boolean>());
		}

		for (IUnit u : um.getUnits()) {
			UnitContext uc = u.getContext();
			for (SubField sf : uc.getSubFields()) {
				updateEntry(sf, u);
			}
			for (SystemOfUnits sou : uc.getSystemOfUnits()) {
				updateEntry(sou, u);
			}
		}
	}

	protected void updateEntry(Object key, IUnit unit) {
		Hashtable<Object,Boolean> ph;
		ph = bighash.get(key);
		if (ph.containsKey(unit)) {
			// nothing
		} else {
			ph.put(unit, Boolean.TRUE);
		}
	}
	
	/**
	 * @param units
	 */
	public void populateUnits(Vector<IUnit> units) {
		for (IUnit u : um.getUnits()) {
			if (unitMatchesFilter(u)) {
				units.add(u);
			}
		}
	}

	/**
	 * @param u
	 * @return	Returns true, if the filter matches.
	 */
	public boolean unitMatchesFilter(IUnit u) {
		boolean match = true;
		UnitContext uc = u.getContext();
		if (!filterDomains.contains(u.getDomain())) return false;
		if (filterSous.size()>0) {
			boolean match1 = false;
			for (SystemOfUnits sou : uc.getSystemOfUnits()) {
				if (filterSous.contains(sou)) {
					match1 = true;
					break;
				}
			}
			if (!match1) return false;
		}
		if (filterFields.size()>0) {
			boolean match1 = false;
			for (SubField sf : uc.getSubFields()) {
				if (filterFields.contains(sf)) {
					match1 = true;
					break;
				}
			}
			if (!match1) return false;
		}
		return match;
	}

	/**
	 * 
	 */
	public void clearFields() {
		filterFields.clear();
	}

	/**
	 * 
	 */
	public void clearSystemOfUnits() {
		filterSous.clear();
	}

	/**
	 * @param f
	 */
	public void removeField(IField f) {
		filterFields.remove(f);
	}

	/**
	 * @param sou
	 */
	public void removeSystemOfUnits(SystemOfUnits sou) {
		filterSous.remove(sou);
	}

	/**
	 * @param d
	 */
	public void removeDomain(Domain d) {
		filterDomains.remove(d);
	}

	/**
	 * 
	 */
	public void clearDomains() {
		filterDomains.clear();
	}
}
