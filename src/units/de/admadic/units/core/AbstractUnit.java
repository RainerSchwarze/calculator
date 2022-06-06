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

import java.util.Calendar;
import java.util.Date;

/**
 * The AbstractUnit defines the basic actions for a Unit.
 * 
 * A Unit consists of the following parts:
 * <ul>
 * <li>id</li>
 * <li>symbol</li>
 * <li>name</li>
 * </ul>
 * 
 * For instance the unit "Meter" would be:
 * id=".m"  symbol="m" name="meter".
 * (The name is certainly locale specific.)
 * 
 * Because of several possible composite units, the following 
 * notes apply:
 * 
 * Units may have a factor such as "k" for "kilo".
 * Units may be combined such as "m/s".
 * Units may have simple conversion such as "1 Angstroem = 1e-10 m".
 * Units may have complex conversion such as "1 K = fF(1 F)".
 * 
 * To compare units, the following rules are implemented:
 * Factors are denoted by "*" or "/" following by the factor subid - 
 * for instance "*k" in "*k.m".
 * Units are denoted by "." or ":" following the unit subid - 
 * for instance ".m" or ":s" resulting in ".m:s" for "m/s".
 * 
 * Combined units such as "N":
 * id = ".N"
 * nid = ".N"
 * cid = "*k.g.m:s:s"
 * ncid = "*k.g.m:s:s"
 * 
 * Unit ".N:m":
 * id = ".N:m"
 * nid = ".N:m"
 * cid = "*.k.g.m:s:s:m"
 * ncid = "*k.g:s:s"
 * 
 * Identity examples:
 * 
 * sameMagnitude: N = kg.m/(s.s), kN != N
 * sameDimension: kN = N, km/h = m/s
 * sameUnit: N = N, N != kg.m/(s.s)
 * 
 * 
 * @author Rainer Schwarze
 */
public abstract class AbstractUnit implements IUnit {
	Domain domain;
	UnitContext context;
	Date lastChange;
	String lastSymbolViewRaw;
	String lastSymbolView;

	// FIXME: check unit structure:
	// maybe:
	// - primary units (no factor: m)
	// - factored units (km)
	// - subsitute units (h, dg C)
	// - composite units (kg.m/s^2)
	
	/**
	 * 
	 */
	public AbstractUnit() {
		super();
		context = new UnitContext();
	}

	/**
	 * @return	Returns diagnostic information.
	 * @see de.admadic.units.core.IUnit#getDiagnosticInfo()
	 */
	public String getDiagnosticInfo() {
//		Factor f = getFactor();
//		String fs = "";
//		if (f!=null) fs = f.getDisplay();
		return 
			"d:" + //fs + 
			getSymbol() +
			" (dom:" + ((getDomain()!=null) ? getDomain().getId() : "-") + ")" +
			" (id:" + getId() + ")" + 
			" (nid:" + getNormalizedId() + ")" +
			" (cid:" + getCanonicalId() + ")" +
			" (ncid:" + getNormalizedCanonicalId() + ")" +
			" (rid:" + getRootedId() + ")" +
			" long:" + getName();
	}


	/**
	 * @param id
	 * @return	Returns the id for the given unit id.
	 * @see de.admadic.units.core.IUnit#getId(int)
	 */
	public String getId(int id) {
		switch (id) {
		case Constants.UNTID_ID: return getId();
		case Constants.UNTID_NORMID: return getNormalizedId();
		case Constants.UNTID_CANID: return getCanonicalId();
		case Constants.UNTID_NORMCANID: return getNormalizedCanonicalId();
		case Constants.UNTID_ROOTEDID: return getRootedId();
		}
		throw new UnitsError("invalid unitid for getId: " + id);
	}

	/**
	 * @param unit
	 * @return	Returns true, if this IUnit has the same dimension as the 
	 * 			given one.
	 * @see de.admadic.units.core.IUnit#isSameDimension(de.admadic.units.core.IUnit)
	 */
	public boolean isSameDimension(IUnit unit) {
		if (unit==null) return false;
		String id1 = this.getNormalizedCanonicalId();
		String id2 = unit.getNormalizedCanonicalId();
		if (id1==null || id2==null) return false;
		return id1.equals(id2);
	}

	/**
	 * @param unit
	 * @return	Returns true, if this Unit has the same magnitude as the
	 * given unit.
	 * @see de.admadic.units.core.IUnit#isSameMagnitude(de.admadic.units.core.IUnit)
	 */
	public boolean isSameMagnitude(IUnit unit) {
		if (unit==null) return false;
		String id1 = this.getNormalizedId();
		String id2 = unit.getNormalizedId();
		if (id1==null || id2==null) return false;
		if (!id1.equals(id2)) return false;
		Double d1 = this.getRootFactor();
		Double d2 = unit.getRootFactor();
		if (d1==null || d2==null) return false;
		// FIXME: add eps-checking
		if (d1.doubleValue()!=d2.doubleValue()) return false;
		return true;
	}

	/**
	 * @param unit
	 * @return	Returns true, if it is the same unit.
	 * @see de.admadic.units.core.IUnit#isSameUnit(de.admadic.units.core.IUnit)
	 */
	public boolean isSameUnit(IUnit unit) {
		if (unit==null) return false;
		String id1 = this.getId();
		String id2 = unit.getId();
		if (id1==null || id2==null) return false;
		if (!id1.equals(id2)) return false;
		return true;
	}

	
	/**
	 * @return	Returns the domain.
	 * @see de.admadic.units.core.IUnit#getDomain()
	 */
	public Domain getDomain() {
		return domain;
	}


	/**
	 * @param domain The domain to set.
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * @return	Returns the context for this AbstractUnit.
	 * @see de.admadic.units.core.IUnit#getContext()
	 */
	public UnitContext getContext() {
		return context;
	}


	/**
	 * @return	Returns the date of the last change.
	 * @see de.admadic.units.core.IUnit#getLastChange()
	 */
	public Date getLastChange() {
		return lastChange;
	}


	/**
	 * @param date
	 * @see de.admadic.units.core.IUnit#setLastChange(java.util.Date)
	 */
	public void setLastChange(Date date) {
		lastChange = date;
	}


	/**
	 * 
	 * @see de.admadic.units.core.IUnit#touchLastChange()
	 */
	public void touchLastChange() {
		lastChange = Calendar.getInstance().getTime();
	}


	/**
	 * @return	Returns the view optimized symbol string.
	 * @see de.admadic.units.core.IUnit#getSymbolView()
	 */
	public String getSymbolView() {
		String tmp = getSymbol();
		if (lastSymbolViewRaw!=null && tmp.equals(lastSymbolViewRaw)) {
			return lastSymbolView;
		}
		lastSymbolViewRaw = getSymbol();
		lastSymbolView = UnitsUtil.createSymbolView(getId(), lastSymbolViewRaw);
		return lastSymbolView;
	}
}
