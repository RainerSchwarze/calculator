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
public class FactorUnit extends AbstractUnit {
	String cacheId;	// internal unique		.m
	String cacheSymbol;	// display			m
	String cacheName;	// display long	meter

	ConverterSeq rootConverter;
	IConverter factorConverter;
	IConverter baseConverter;

	String cacheNormalizedId;	// internal unique		.m
	String cacheCanonicalId;	// internal unique		.m
	String cacheNormalizedCanonicalId;	// internal unique		.m
	String cacheRootedId;	// internal unique		.m

	Factor factor;
	IUnit baseUnit;
	
	/**
	 * @param domain 
	 * @param baseUnit 
	 * @param factor 
	 */
	public FactorUnit(Domain domain, IUnit baseUnit, Factor factor) {
		super();
		rootConverter = new ConverterSeq();
		rootConverter.add(factorConverter);
		rootConverter.add(baseConverter);

		setDomain(domain);
		setBaseUnit(baseUnit);
		setFactor(factor);
	}

	protected void updateCache() {
		cacheId = "";
		cacheId += (factor!=null) ? factor.getId() : "";
		cacheId += (baseUnit!=null) ? baseUnit.getId() : "";
		cacheId = UnitsUtil.sortIdMulDiv(cacheId);
		cacheCanonicalId = "";
		cacheCanonicalId += (factor!=null) ? factor.getId() : "";
		cacheCanonicalId += (baseUnit!=null) ? baseUnit.getCanonicalId() : "";
		cacheCanonicalId = UnitsUtil.sortIdMulDiv(cacheCanonicalId);
		cacheSymbol = "";
		if (factor!=null) cacheSymbol += factor.getSymbol();
		if (baseUnit!=null) cacheSymbol += baseUnit.getSymbol();
		cacheName = "";
		if (factor!=null) cacheName += factor.getName();
		if (baseUnit!=null) cacheName += baseUnit.getName();

		cacheNormalizedId = UnitsUtil.sortNormalizedCache(cacheId);
		cacheNormalizedCanonicalId = UnitsUtil.sortNormalizedCache(cacheCanonicalId);
		cacheRootedId = UnitsUtil.stripFactorIds(cacheNormalizedCanonicalId);
	}
	
	/**
	 * @param baseUnit
	 */
	public void setBaseUnit(IUnit baseUnit) {
		this.baseUnit = baseUnit;
		baseConverter = baseUnit.getRootConverter();
		rootConverter.set(1, baseConverter);
		updateCache();
	}

	/**
	 * @return	Returns the display text.
	 * @see de.admadic.units.core.IUnit#getSymbol()
	 */
	public String getSymbol() {
		return cacheSymbol;
	}

	/**
	 * @return	Returns the long display text.
	 * @see de.admadic.units.core.IUnit#getName()
	 */
	public String getName() {
		return cacheName;
	}

	/**
	 * @return	Returns the id.
	 * @see de.admadic.units.core.IUnit#getId()
	 */
	public String getId() {
		return cacheId;
	}


	/**
	 * @return	Returns the normalized id.
	 * @see de.admadic.units.core.IUnit#getNormalizedId()
	 */
	public String getNormalizedId() {
		return cacheNormalizedId;
	}


	/**
	 * @return	Returns the canonical id.
	 * @see de.admadic.units.core.IUnit#getCanonicalId()
	 */
	public String getCanonicalId() {
		return cacheCanonicalId;
	}


	/**
	 * @return	Returns the normalized canonical id.
	 * @see de.admadic.units.core.IUnit#getNormalizedCanonicalId()
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
	 * Returns always null, because a BaseUnit never has a factor.
	 * @return Returns the factor.
	 * @see IUnit#getFactor
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
		rootConverter.set(0, factorConverter);
		updateCache();
	}

	/**
	 * @return	Returns a String representation.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "s:" + cacheSymbol + "(id:" + cacheId + ")";
	}

	/**
	 * @return	Returns the converter turing this units value into the one
	 * 			in root units.
	 * @see de.admadic.units.core.IUnit#getRootConverter()
	 */
	public IConverter getRootConverter() {
		return rootConverter;
	}

	/**
	 * @return	Returns the factor to convert this unit into root units.
	 * @see de.admadic.units.core.IUnit#getRootFactor()
	 */
	public Double getRootFactor() {
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
	}

	/**
	 * @return Returns the baseUnit.
	 */
	public IUnit getBaseUnit() {
		return baseUnit;
	}
}
