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

/**
 * @author Rainer Schwarze
 *
 */
public class UnitFactory {

	/**
	 * No instance.
	 */
	protected UnitFactory() {
		super();
	}

	/**
	 * @param id
	 * @param display
	 * @param displayLong
	 * @param factor
	 * @return	Returns the factor created.
	 */
	public static Factor createFactor(String id, String display, String displayLong, double factor) {
		return new Factor(id, display, displayLong, factor);
	}

	/**
	 * @param id
	 * @param display
	 * @param displayLong
	 * @return	Returns the unit created.
	 */
	public static IUnit createUnit(String id, String display, String displayLong) {
		return new BaseUnit(id, display, displayLong);
	}

	/**
	 * @param um	UnitManager
	 * @param fid	Factor ID
	 * @param mid1	Unit ID (mul)
	 * @return	Returns a composite unit.
	 */
	public static IUnit createUnitFM(UnitManager um, String fid, String mid1) {
		return new CompositeUnit()
			.fac(um.getFactor(fid))
			.mul(um.getUnit(mid1));
	}

	/**
	 * @param um
	 * @param mid1
	 * @param did1
	 * @return	Returns the unit created.
	 */
	public static IUnit createUnitMD(UnitManager um, String mid1, String did1) {
		return new CompositeUnit()
			.mul(um.getUnit(mid1))
			.div(um.getUnit(did1));
	}

	/**
	 * @param um	UnitManager
	 * @param fid	Factor ID
	 * @param mid1	Unit ID (mul)
	 * @param mid2 
	 * @return	Returns a composite unit.
	 */
	public static IUnit createUnitFMM(UnitManager um, String fid, String mid1, String mid2) {
		return new CompositeUnit()
			.fac(um.getFactor(fid))
			.mul(um.getUnit(mid1))
			.mul(um.getUnit(mid2));
	}

	/**
	 * @param um
	 * @param fid
	 * @param mid1
	 * @param mid2
	 * @param did1
	 * @param did2
	 * @return	Return the created unit.
	 */
	public static IUnit createUnitFMMDD(
			UnitManager um, String fid,
			String mid1, String mid2,
			String did1, String did2) {
		CompositeUnit cu = new CompositeUnit();
		if (fid!=null) cu.fac(um.getFactor(fid));
		if (mid1!=null) cu.mul(um.getUnit(mid1));
		if (mid2!=null) cu.mul(um.getUnit(mid2));
		if (did1!=null) cu.div(um.getUnit(did1));
		if (did2!=null) cu.div(um.getUnit(did2));
		return cu;
	}

	/**
	 * @param um	UnitManager
	 * @param fid	Factor ID
	 * @param substId 
	 * @param substSymbol 
	 * @param substName 
	 * @param mid1	Unit ID (mul)
	 * @param mid2 
	 * @param did1 
	 * @param did2 
	 * @return	Returns a composite unit.
	 */
	public static IUnit createUnitFSMMDD(
			UnitManager um, String fid,
			String substId, String substSymbol, String substName,
			String mid1, String mid2,
			String did1, String did2) {
		CompositeUnit cu = new CompositeUnit();
		if (fid!=null) cu.fac(um.getFactor(fid));
		if (substSymbol!=null || substName!=null) 
			cu.subst(substId, substSymbol, substName);
		if (mid1!=null) cu.mul(um.getUnit(mid1));
		if (mid2!=null) cu.mul(um.getUnit(mid2));
		if (did1!=null) cu.div(um.getUnit(did1));
		if (did2!=null) cu.div(um.getUnit(did2));
		return cu;
	}

	/**
	 * @param um 
	 * @param fid 
	 * @param substId 
	 * @param substSymbol 
	 * @param substName 
	 * @param baseConv 
	 * @param mid1 
	 * @param did1 
	 * @return	Returns a composite unit.
	 */
	public static IUnit createUnitFSBMD(
			UnitManager um, String fid,
			String substId, String substSymbol, String substName,
			IConverter baseConv,
			String mid1, String did1) {
		CompositeUnit cu = new CompositeUnit();
		if (fid!=null) cu.fac(um.getFactor(fid));
		if (substSymbol!=null || substName!=null) 
			cu.subst(substId, substSymbol, substName);
		if (baseConv!=null) cu.setBaseConverter(baseConv);
		if (mid1!=null) cu.mul(um.getUnit(mid1));
		if (did1!=null) cu.div(um.getUnit(did1));
		return cu;
	}
	
	
	/**
	 * @param um
	 * @param id
	 * @param symbol
	 * @param name
	 * @param uid
	 * @return	Returns the created IQuantity.
	 */
	public static IQuantity createQuantity(UnitManager um, String id, String symbol, String name, String uid) {
		return new Quantity(id, symbol, name, um.getUnit(uid));
	}

	/**
	 * @param um
	 * @param qid
	 * @param value
	 * @param uid
	 * @return	Returns the created measure.
	 */
	public static IMeasure createMeasure(
			UnitManager um, String qid, double value, String uid) {
		return new Measure(um.getQuantity(qid), value, um.getUnit(uid));
	}

	/**
	 * @param um
	 * @param id
	 * @param qid
	 * @param value
	 * @param uid
	 * @return	Returns the created measure.
	 */
	public static IMeasure createMeasure(
			UnitManager um, String id, String qid, double value, String uid) {
		return new Measure(id, um.getQuantity(qid), value, um.getUnit(uid));
	}

	/**
	 * @param um
	 * @param id
	 * @param qid
	 * @param value
	 * @param uid
	 * @param symbol 
	 * @param name 
	 * @return	Returns the created measure.
	 */
	public static IMeasure createMeasure(
			UnitManager um, String id, String qid, double value, String uid,
			String symbol, String name) {
		return new Measure(
				id, um.getQuantity(qid), Double.valueOf(value), um.getUnit(uid),
				symbol, name);
	}


	/**
	 * @param id
	 * @param name
	 * @return	Returns the created domain.
	 */
	public static Domain createDomain(String id, String name) {
		return new Domain(id, name);
	}

	/**
	 * @param id
	 * @param name
	 * @return	Returns the created field.
	 */
	public static Field createField(String id, String name) {
		return new Field(id, name);
	}

	/**
	 * @param um 
	 * @param fid 
	 * @param id
	 * @param name
	 * @return	Returns the created subfield.
	 */
	public static SubField createSubField(UnitManager um, String fid, String id, String name) {
		IField field = um.getField(fid);
		return new SubField(field, id, name);
	}

	/**
	 * @param id
	 * @param symbol 
	 * @param name
	 * @return	Returns the created SystemOfUnits.
	 */
	public static SystemOfUnits createSystemOfUnits(
			String id, String symbol, String name) {
		return new SystemOfUnits(id, symbol, name);
	}

	/**
	 * @param um
	 * @param id
	 * @param symbol
	 * @param name
	 * @param did
	 * @return	Returns the created BaseUnit.
	 */
	public static BaseUnit createBaseUnit(
			UnitManager um, String id, String symbol, String name, String did) {
		Domain domain = um.getDomain(did);
		BaseUnit bu = new BaseUnit(domain, id, symbol, name);
		return bu;
	}

	/**
	 * @param um
	 * @param did 
	 * @param ubasid 
	 * @param fid 
	 * @return	Returns the created FactorUnit.
	 */
	public static FactorUnit createFactorUnit(
			UnitManager um, String did, String ubasid, String fid) {
		Domain domain = um.getDomain(did);
		IUnit unit = um.getUnit(ubasid);
		Factor factor = um.getFactor(fid);
		FactorUnit fu = new FactorUnit(domain, unit, factor);
		return fu;
	}

	/**
	 * @param um
	 * @param id
	 * @param symbol
	 * @param name
	 * @param buid
	 * @param did
	 * @return	Returns the created SubstUnit.
	 */
	public static SubstUnit createSubstUnit(
			UnitManager um, String id, String symbol, String name, 
			String buid, 
			String did) {
		Domain domain = um.getDomain(did);
		IUnit bu = um.getUnit(buid);
		SubstUnit su = new SubstUnit(id, symbol, name, bu);
		su.setDomain(domain);
		return su;
	}

	/**
	 * @param um
	 * @param did
	 * @return	Returns the created CompositeUnit
	 */
	public static CompositeUnit createCompUnit(UnitManager um, String did) {
		CompositeUnit cu = new CompositeUnit();
		Domain domain = um.getDomain(did);
		cu.setDomain(domain);
		return cu;
	}
}
