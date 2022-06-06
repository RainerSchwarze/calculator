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
public class UnitManager {
	Vector<IUnit> units;
	// extend that for Pair<qtyId,id> as well!
	Hashtable<String,IUnit> idToUnit;

	Vector<Factor> factors;
	Hashtable<String,Factor> idToFactor;

	Vector<IQuantity> quantities;
	Hashtable<String,IQuantity> idToQuantity;

	Vector<IMeasure> measures;
	Hashtable<String,IMeasure> idToMeasure;

	Vector<Domain> domains;
	Hashtable<String,Domain> idToDomain;
	Hashtable<String,Hashtable<String,IUnit>> domainToUnits;

	Vector<Field> fields;
	Hashtable<String,Field> idToField;
	Vector<SubField> subfields;
	Hashtable<String,SubField> idToSubField;

	Vector<SystemOfUnits> sous;
	Hashtable<String,SystemOfUnits> idToSou;

	/**
	 * 
	 */
	public UnitManager() {
		super();
		units = new Vector<IUnit>();
		idToUnit = new Hashtable<String,IUnit>();
		factors = new Vector<Factor>();
		idToFactor = new Hashtable<String,Factor>();
		quantities = new Vector<IQuantity>();
		idToQuantity = new Hashtable<String,IQuantity>();
		measures = new Vector<IMeasure>();
		idToMeasure = new Hashtable<String,IMeasure>();
		domains = new Vector<Domain>();
		idToDomain = new Hashtable<String,Domain>();
		domainToUnits = new Hashtable<String,Hashtable<String,IUnit>>();
		fields = new Vector<Field>();
		idToField = new Hashtable<String,Field>();
		subfields = new Vector<SubField>();
		idToSubField = new Hashtable<String,SubField>();
		sous = new Vector<SystemOfUnits>();
		idToSou = new Hashtable<String,SystemOfUnits>();
	}

	/**
	 * @param unit
	 */
	public void addUnit(IUnit unit) {
		units.add(unit);
		idToUnit.put(unit.getId(), unit);
		addToDomain(unit);
	}

	/**
	 * @param unit
	 */
	public void removeUnit(IUnit unit) {
		idToUnit.remove(unit.getId());
		units.remove(unit);
		removeFromDomain(unit);
	}

	/**
	 * @param id
	 * @return	Returns true, if a unit with this id exists.
	 */
	public boolean existsUnit(String id) {
		return idToUnit.containsKey(id);
	}
	
	/**
	 * @param id
	 * @return	Returns the unit for the given id.
	 */
	public IUnit getUnit(String id) {
		if (!existsUnit(id)) {
			tryToCreateUnit(id);
		}
		return idToUnit.get(id);
	}

	// FIXME: add method to create simple combined units? (.m.m, .m.m.m)
	/**
	 * Try to create simple units such as km, mm, ns, etc.
	 * @param id
	 */
	protected void tryToCreateUnit(String id) {
//		System.out.println(
//				"try to create unit: " + id);
		if (id.length()<1) return;	// nope
		if (id.charAt(0)!=Constants.CH_FAC_MUL) return; // nope
		String [] ida = id.split(Constants.REGEX_SPLIT_ID);
		String [] tmp = {"", ""};
		int emptycnt = 0;
		int idx = 0;
		for (String s : ida) {
			if (s.equals("")) {
				emptycnt++;
				continue;
			}
			if (idx>=2) return; // nope
			tmp[idx++] = s;
		}
		ida = tmp;
		Factor f = getFactor(ida[0]);
		IUnit u = getUnit(ida[1]);
		if (f==null || u==null) return;
		IUnit newunit = UnitFactory.createFactorUnit(
				this, 
				u.getDomain().getId(),
				u.getId(),
				f.getId());
		addUnit(newunit);
//		System.out.println(
//				"magically created unit: " + newunit.getDiagnosticInfo());
	}

	/**
	 * @return Returns the units.
	 */
	public Vector<IUnit> getUnits() {
		return units;
	}

	/**
	 * @param unit
	 */
	public void addToDomain(IUnit unit) {
		String did = "";
		if (unit.getDomain()!=null) {
			did = unit.getDomain().getId();
		}
		Hashtable<String,IUnit> parthash;
		parthash = domainToUnits.get(did);
		if (parthash==null) {
			parthash = new Hashtable<String,IUnit>();
			domainToUnits.put(did, parthash);
		}
		parthash.put(unit.getId(), unit);
	}

	/**
	 * @param unit
	 */
	public void removeFromDomain(IUnit unit) {
		String did = "";
		if (unit.getDomain()!=null) {
			did = unit.getDomain().getId();
		}
		Hashtable<String,IUnit> parthash;
		parthash = domainToUnits.get(did);
		parthash.remove(unit.getId());
	}

	/**
	 * @param uid
	 * @param did
	 * @return Returns the unit with the given id in the given domain.
	 */
	public IUnit getUnitFromDomain(String uid, String did) {
		Hashtable<String,IUnit> parthash;
		parthash = domainToUnits.get(did);
		return parthash.get(uid);
	}

	/**
	 * @param domain
	 * @param v 
	 * @return	Returns a list of IUnits for the given Domain.
	 */
	public Vector<IUnit> getUnitsForDomain(Domain domain, Vector<IUnit> v) {
		if (v==null) v = new Vector<IUnit>();
		Hashtable<String,IUnit> tmp = domainToUnits.get(domain.getId());
		v.clear();
		v.addAll(tmp.values());
		return v;
	}

	/**
	 * @param f
	 */
	public void addFactor(Factor f) {
		factors.add(f);
		idToFactor.put(f.getId(), f);
	}

	/**
	 * @param f
	 */
	public void removeFactor(Factor f) {
		factors.remove(f);
		idToFactor.remove(f.getId());
	}

	/**
	 * @param id
	 */
	public void removeFactor(String id) {
		Factor f = null;
		f = idToFactor.get(id);
		if (f==null) return;
		factors.remove(f);
		idToFactor.remove(id);
	}

	/**
	 * @param id
	 * @return	Returns the factor for the id.
	 */
	public Factor getFactor(String id) {
		return idToFactor.get(id);
	}

	/**
	 * @return Returns the factors.
	 */
	public Vector<Factor> getFactors() {
		return factors;
	}

	/**
	 * @param quantity
	 */
	public void addQuantity(IQuantity quantity) {
		quantities.add(quantity);
		idToQuantity.put(quantity.getId(), quantity);
	}

	/**
	 * @param quantity
	 */
	public void removeQuantity(IQuantity quantity) {
		idToQuantity.remove(quantity.getId());
		quantities.remove(quantity);
	}	

	/**
	 * @param id
	 * @return	Returns the Quantity for the given id.
	 */
	public IQuantity getQuantity(String id) {
		return idToQuantity.get(id);
	}


	/**
	 * @return	Returns the list of quantities.
	 */
	public Vector<IQuantity> getQuantities() {
		return quantities;
	}

	/**
	 * @param measure
	 */
	public void addMeasure(IMeasure measure) {
		measures.add(measure);
		idToMeasure.put(measure.getId(), measure);
	}

	/**
	 * @param measure
	 */
	public void removeMeasure(IMeasure measure) {
		idToMeasure.remove(measure.getId());
		measures.remove(measure);
	}

	/**
	 * @param id
	 * @return	Returns the Measure for the given id.
	 */
	public IMeasure getMeasure(String id) {
		return idToMeasure.get(id);
	}
	
	/**
	 * @return	Returns the list of measures. 
	 */
	public Vector<IMeasure> getMeasures() {
		return measures;
	}

	/**
	 * @param domain
	 */
	public void addDomain(Domain domain) {
		domains.add(domain);
		idToDomain.put(domain.getId(), domain);
	}

	/**
	 * @param domain
	 */
	public void removeDomain(Domain domain) {
		idToDomain.remove(domain.getId());
		domains.remove(domain);
	}

	/**
	 * @param id
	 * @return	Returns the Domain for the given id.
	 */
	public Domain getDomain(String id) {
		return idToDomain.get(id);
	}

	/**
	 * @return	Returns the list of domains.
	 */
	public Vector<Domain> getDomains() {
		return domains;
	}
	
	/**
	 * @param field
	 */
	public void addField(Field field) {
		fields.add(field);
		idToField.put(field.getId(), field);
	}

	/**
	 * @param field
	 */
	public void removeField(Field field) {
		fields.remove(field);
		idToField.remove(field.getId());
	}

	/**
	 * @param id
	 * @return	Returns the Field for the given id.
	 */
	public IField getField(String id) {
		return idToField.get(id);
	}

	/**
	 * @return	Return the list of Fields.
	 */
	public Vector<Field> getFields() {
		return fields;
	}
	
	/**
	 * @param subfield
	 */
	public void addSubField(SubField subfield) {
		subfields.add(subfield);
		idToSubField.put(subfield.getId(), subfield);
	}

	/**
	 * @param subfield
	 */
	public void removeSubField(SubField subfield) {
		subfields.remove(subfield);
		idToSubField.remove(subfield.getId());
	}

	/**
	 * @param id
	 * @return	Returns the SubField for the given id.
	 */
	public SubField getSubField(String id) {
		return idToSubField.get(id);
	}

	/**
	 * @return	Return the list of SubFields.
	 */
	public Vector<SubField> getSubFields() {
		return subfields;
	}

	/**
	 * @param sou
	 */
	public void addSystemOfUnits(SystemOfUnits sou) {
		sous.add(sou);
		idToSou.put(sou.getId(), sou);
	}

	/**
	 * @param sou
	 */
	public void removeSystemOfUnits(SystemOfUnits sou) {
		sous.remove(sou);
		idToSou.remove(sou.getId());
	}

	/**
	 * @param id
	 * @return	Returns the SystemOfUnits for the given id.
	 */
	public SystemOfUnits getSystemOfUnits(String id) {
		return idToSou.get(id);
	}

	/**
	 * @return	Returns the list of SystemOfUnits.
	 */
	public Vector<SystemOfUnits> getSystemOfUnits() {
		return sous;
	}

	/**
	 * @param u
	 */
	public void updateDatabase(IUnit u) {
		// TODO: implement that
	}
}
