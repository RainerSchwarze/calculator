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
public class SubstUnit extends AbstractUnit {
	// if the composite unit has a substitute:
	// (for instance kg m / s2 = N)
	Factor factor;

	String id;
	String symbol;
	String name;

	IUnit baseUnit;

	ConverterSeq rootConverter;			// seq converter for:
	IConverter factorConverter;			// factor such as k for kN
	IConverter substConverter;			// conversion for h->s or K->F
	IConverter baseConverter;			// converter of base unit

	final static int CV_FACTOR = 0;
	final static int CV_SUBST = 1;
	final static int CV_BASE = 2;
	
	// sample for kN/km
	String cacheSymbol = "";				// kN/kM
	String cacheName = "";					// ...
	String cacheId = "";					// *k.N/k:m
	String cacheNormalizedId = "";			// .N:m
	String cacheCanonicalId = "";			// *k*k.g.m:s:s/k:m
	String cacheNormalizedCanonicalId = ""; // *k.g:s:s
	String cacheRootedId = "";				// .g:s:s
	
	/**
	 * @param id 
	 * @param symbol 
	 * @param name 
	 * @param baseUnit 
	 * 
	 */
	public SubstUnit(String id, String symbol, String name, IUnit baseUnit) {
		super();
		factorConverter = null;
		rootConverter = new ConverterSeq();
		// don't change the order!
		// (otherwise adjust the CV_ constants)
		rootConverter.add(factorConverter);
		rootConverter.add(substConverter);
		rootConverter.add(baseConverter);
		setBaseUnit(baseUnit);
		setSubst(id, symbol, name);
		// updateCache();
	}

	/**
	 * @param id
	 * @param symbol
	 * @param name
	 */
	public void setSubst(String id, String symbol, String name) {
		this.id = id;
		this.symbol = symbol;
		this.name = name;
		updateCache();
	}
	
	/**
	 * @param baseUnit The baseUnit to set.
	 */
	public void setBaseUnit(IUnit baseUnit) {
		this.baseUnit = baseUnit;
		baseConverter = baseUnit.getRootConverter();
		rootConverter.set(CV_BASE, baseConverter);
		updateCache();
	}

	/**
	 * @param f
	 * @return	Returns this
	 */
	public SubstUnit fac(Factor f) {
		setFactor(f);
		return this;
	}

	/**
	 * @param domain
	 * @return	Returns this.
	 */
	public SubstUnit dom(Domain domain) {
		setDomain(domain);
		return this;
	}
	
	/**
	 * @return Returns the factor.
	 */
	public Factor getFactor() {
		return factor;
	}

	/**
	 * @param factor The factor to set.
	 */
	public void setFactor(Factor factor) {
		this.factor = factor;
		factorConverter = factor.getRootConverter();
		rootConverter.set(CV_FACTOR, factorConverter);
		updateCache();
	}

	/**
	 * @param conv
	 */
	public void setBaseConverter(IConverter conv) {
		baseConverter = conv;
		rootConverter.set(CV_BASE, baseConverter);
	}
	
	/**
	 * 
	 * @param id
	 * @return	Sort the ids, first mul, then div.
	 */
	private String sortIdMulDiv(String id) {
		String res = "";
		// create array which includes the elements and the tag:
		String [] ida = id.split(Constants.REGEX_SPLIT_ID);
		String m = "";
		String d = "";
		for (String s : ida) {
			if (s.equals("")) continue;
			String v = s.substring(1);
			char c = s.charAt(0);
			switch (c) {
			case Constants.CH_FAC_MUL: m += Constants.CH_FAC_MUL + v; break;
			case Constants.CH_UNT_MUL: m += Constants.CH_UNT_MUL + v; break;
			case Constants.CH_FAC_DIV: d += Constants.CH_FAC_DIV + v; break;
			case Constants.CH_UNT_DIV: d += Constants.CH_UNT_DIV + v; break;
			default:
				throw new Error("invalid element id: [" + c + "] with " + s);
			}
		}
		res = m + d;
		return res;
	}

	private void updateCache() {
		cacheId = id;
		cacheSymbol = symbol;
		cacheName = name;

		// cacheId = sortIdMulDiv(id + baseUnit.getId());
		// refer to getId() because that takes care of the factor to be added:
		cacheNormalizedId = UnitsUtil.sortNormalizedCache(
				((factor!=null) ? factor.getId() : "") + 
				cacheId);
		cacheCanonicalId = sortIdMulDiv(
				((factor!=null) ? factor.getId() : "") + 
				((baseUnit!=null) ? baseUnit.getCanonicalId() : ""));
		cacheNormalizedCanonicalId = UnitsUtil.sortNormalizedCache(
				cacheCanonicalId);
		cacheRootedId = UnitsUtil.stripFactorIds(
				cacheNormalizedCanonicalId);
	}

	/**
	 * @return	Returns the unit display.
	 * @see de.admadic.units.core.IUnit#getSymbol()
	 */
	public String getSymbol() {
		String f = "";
		if (factor!=null) {
			f = factor.getSymbol();
		}
		return f + cacheSymbol;
	}

	/**
	 * @return	Returns the long display text.
	 * @see de.admadic.units.core.IUnit#getName()
	 */
	public String getName() {
		String f = "";
		if (factor!=null) {
			f = factor.getName();
		}
		return f + cacheName;
	}


	/**
	 * @return	Returns the unit id.
	 * @see de.admadic.units.core.IUnit#getId()
	 */
	public String getId() {
		String f = "";
		if (factor!=null) {
			f = factor.getId();
		}
		return f + cacheId;
	}

	/**
	 * @return	Returns the canonical id.
	 * @see de.admadic.units.core.IUnit#getCanonicalId()
	 */
	public String getCanonicalId() {
		if (factor!=null) {
			return factor.getId() + cacheCanonicalId;
		} else {
			return cacheCanonicalId;
		}
	}

	/**
	 * @return	Returns the normalized id.
	 * @see de.admadic.units.core.IUnit#getNormalizedId()
	 */
	public String getNormalizedId() {
		return cacheNormalizedId;
	}

	/**
	 * @return Returns the normalized canonical id.
	 */
	public String getNormalizedCanonicalId() {
		return cacheNormalizedCanonicalId;
	}

	/**
	 * @return	Returns the rooted id.
	 * @see de.admadic.units.core.IUnit#getRootedId()
	 */
	public String getRootedId() {
		return cacheRootedId;
	}

	/**
	 * Note that composite units do not have a QtyId.
	 * @return	Returns the quantity id.
	 * @see de.admadic.units.core.IUnit#getQtyId()
	 */
	public int getQtyId() {
		return Constants.PRI_QTY_NONE;
	}

	/**
	 * @return	Returns a String representation.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "s:" + getSymbol() + "(id:" + getId() + ")(cid:" + getCanonicalId() + ")";
	}


	/**
	 * @return	Returns the root converter.
	 * @see de.admadic.units.core.IUnit#getRootConverter()
	 */
	public IConverter getRootConverter() {
		return rootConverter;
	}


	/**
	 * @return	Returns the factor to convert this units value
	 * into root units.
	 * @see de.admadic.units.core.IUnit#getRootFactor()
	 */
	public Double getRootFactor() {
		// FIXME: add check whether a simple factor works or not 
		// (dg C does not!)
		return rootConverter.convert(Double.valueOf(1.0));
	}

	/**
	 * @param unit
	 * @see de.admadic.units.core.IUnit#checkAcyclic(de.admadic.units.core.IUnit)
	 */
	public void checkAcyclic(IUnit unit) {
		if (this==unit) {
			throw new UnitsError("Units are cyclic: " + unit.getDiagnosticInfo());
		}
		baseUnit.checkAcyclic(unit);
	}


	/**
	 * @return Returns the baseUnit.
	 */
	public IUnit getBaseUnit() {
		return baseUnit;
	}

	/**
	 * @return Returns the baseConverter.
	 */
	public IConverter getBaseConverter() {
		return baseConverter;
	}

	/**
	 * @return Returns the substConverter.
	 */
	public IConverter getSubstConverter() {
		return substConverter;
	}

	/**
	 * @param substConverter The substConverter to set.
	 */
	public void setSubstConverter(IConverter substConverter) {
		this.substConverter = substConverter;
		rootConverter.set(CV_SUBST, substConverter);
	}
}
