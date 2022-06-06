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
public class CompositeUnit extends AbstractUnit {
	// if the composite unit has a substitute:
	// (for instance kg m / s2 = N)
	Factor factor;

	String substId;
	String substSymbol;
	String substName;

	ConverterSeq rootConverter;			// seq converter for:
	IConverter factorConverter;			// factor such as k for kN
	IConverter baseConverter;			// conversion for h->s or K->F
	ConverterMulDivSeq seqConverter;	// conversion for m/s type etc.

	final static int CV_FACTOR = 0;
	final static int CV_BASE = 1;
	final static int CV_SEQ = 2;
	
	/** unit element is multiplied m*s */
	public final static int MUL = 0;
	/** unit element is divided m*(s^-1) */
	public final static int DIV = 1;

	final static Integer I_MUL = Integer.valueOf(MUL);
	final static Integer I_DIV = Integer.valueOf(DIV);
	// access as itgMap[MUL] etc.
	Integer [] itgMap = { I_MUL, I_DIV };

	//ArrayList<Integer> ops;
	Vector<IUnit> unitsMul;
	Vector<IUnit> unitsDiv;

	// sample for kN/km
	String cacheSymbol = "";				// kN/kM
	String cacheName = "";					// ...
	String cacheId = "";					// *k.N/k:m
	String cacheNormalizedId = "";			// .N:m
	String cacheCanonicalId = "";			// *k*k.g.m:s:s/k:m
	String cacheNormalizedCanonicalId = ""; // *k.g:s:s
	String cacheRootedId = "";				// .g:s:s
	
	/**
	 * 
	 */
	public CompositeUnit() {
		super();
		//ops = new ArrayList<Integer>();
		unitsMul = new Vector<IUnit>();
		unitsDiv = new Vector<IUnit>();
		// create the identity as a default:
		seqConverter = new ConverterMulDivSeq();
		factorConverter = null;
		baseConverter = null;
		rootConverter = new ConverterSeq();
		// don't change the order!
		// (otherwise adjust the CV_ constants)
		rootConverter.add(factorConverter);
		rootConverter.add(baseConverter);
		rootConverter.add(seqConverter);
	}

	/**
	 * @param id 
	 * @param symbol
	 * @param name
	 */
	public void setSubst(String id, String symbol, String name) {
		System.err.println("Warning: CompositeUnit with id " + cacheId + " shall be substituted.");
		substId = id;
		substSymbol = symbol;
		substName = name;
	}
	
	/**
	 * @param id 
	 * @param symbol
	 * @param name
	 * @return	Returns this
	 */
	public CompositeUnit subst(String id, String symbol, String name) {
		setSubst(id, symbol, name);
		return this;
	}

	/**
	 * @param id 
	 * @param symbol
	 * @param name
	 * @param f
	 * @return	Returns this
	 */
	public CompositeUnit subst(String id, String symbol, String name, Factor f) {
		setSubst(id, symbol, name);
		setFactor(f);
		return this;
	}

	/**
	 * @param f
	 * @return	Returns this
	 */
	public CompositeUnit fac(Factor f) {
		setFactor(f);
		return this;
	}

	/**
	 * @param domain
	 * @return	Returns this.
	 */
	public CompositeUnit dom(Domain domain) {
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
		System.err.println("Warning: CompositeUnit with id " + cacheId + " shall be factored.");
		this.factor = factor;
		factorConverter = factor.getRootConverter();
		rootConverter.set(CV_FACTOR, factorConverter);
	}

	/**
	 * 
	 */
	public void clearSubUnits() {
		unitsMul.clear();
		unitsDiv.clear();
		cacheSymbol = "";				// kN/kM
		cacheName = "";					// ...
		cacheId = "";					// *k.N/k:m
		cacheNormalizedId = "";			// .N:m
		cacheCanonicalId = "";			// *k*k.g.m:s:s/k:m
		cacheNormalizedCanonicalId = ""; // *k.g:s:s
		cacheRootedId = "";				// .g:s:s
		seqConverter.clearSeq();
	}
	
	/**
	 * @param conv
	 */
	public void setBaseConverter(IConverter conv) {
		baseConverter = conv;
		rootConverter.set(CV_BASE, baseConverter);
	}

	/**
	 * @return Returns the baseConverter.
	 */
	public IConverter getBaseConverter() {
		return baseConverter;
	}


	/**
	 * @param unit
	 * @param op
	 */
	public void appendUnit(IUnit unit, int op) {
		// checkAcyclic(unit);
		//ops.add(itgMap[op]);
		if (op==MUL) {
			unitsMul.add(unit);
		} else {
			unitsDiv.add(unit);
		}
		updateCache(unit, op);
		updateRootConverter(unit, op);
	}

	private void updateRootConverter(IUnit unit, int op) {
		if (op==MUL) {
			seqConverter.addMul(unit.getRootConverter());
		} else {
			seqConverter.addDiv(unit.getRootConverter());
		}
	}

	/**
	 * @param which
	 * @return	Returns the list of units.
	 */
	public Vector<IUnit> getUnits(int which) {
		if (which==MUL) {
			return unitsMul;
		} else {
			return unitsDiv;
		}
	}
	
	private void updateCache(IUnit unit, int op) {
		// mul: all muls append to mul, all divs append to div
		// div: all muls append to div, all divs append to mul
		if (op==MUL) {
			cacheId = UnitsUtil.sortIdMulDiv(cacheId + unit.getId());
			cacheCanonicalId = UnitsUtil.sortIdMulDiv(cacheCanonicalId + unit.getCanonicalId());
			if (cacheSymbol.length()!=0)
				cacheSymbol += "_";
			cacheSymbol += unit.getSymbol();
			cacheName += unit.getName();
		} else {	// DIV
			cacheId = UnitsUtil.sortIdMulDiv(cacheId + UnitsUtil.invertIdDivToMul(unit.getId()));
			cacheCanonicalId = UnitsUtil.sortIdMulDiv(cacheCanonicalId + UnitsUtil.invertIdDivToMul(unit.getCanonicalId()));
			if (cacheSymbol.length()!=0)
				cacheSymbol += "~";
			cacheSymbol += UnitsUtil.invertSymbolDivToMul(unit.getSymbol());
			cacheName += unit.getName();
		}
		// refer to getId() because that takes care of the factor to be added:
		cacheNormalizedId = UnitsUtil.sortNormalizedCache(getId());
		cacheNormalizedCanonicalId = UnitsUtil.sortNormalizedCache(getCanonicalId());
		cacheRootedId = UnitsUtil.stripFactorIds(cacheNormalizedCanonicalId);
	}

	/**
	 * @param unit
	 * @return	Returns this.
	 */
	public CompositeUnit mul(IUnit unit) {
		appendUnit(unit, MUL);
		return this;
	}

	/**
	 * @param unit
	 * @return	Returns this.
	 */
	public CompositeUnit div(IUnit unit) {
		appendUnit(unit, DIV);
		return this;
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
		if (substSymbol!=null) {
			return f + substSymbol;
		} else {
			return f + cacheSymbol;
		}
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
		if (substName!=null) {
			return f + substName;
		} else {
			return f + cacheName;
		}
	}


	/**
	 * @return	Returns the unit id.
	 * @see de.admadic.units.core.IUnit#getId()
	 */
	public String getId() {
		if (substSymbol!=null) {
			if (factor!=null) {
				return factor.getId() + substId;
			} else {
				return substId;
			}
		} else {
			if (factor!=null) {
				return factor.getId() + cacheId;
			} else {
				return cacheId;
			}
		}
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
		for (IUnit u : unitsMul) {
			u.checkAcyclic(unit);
		}
		for (IUnit u : unitsDiv) {
			u.checkAcyclic(unit);
		}
	}

	/**
	 * @return Returns the substId.
	 */
	public String getSubstId() {
		return substId;
	}

	/**
	 * @return Returns the substName.
	 */
	public String getSubstName() {
		return substName;
	}

	/**
	 * @return Returns the substSymbol.
	 */
	public String getSubstSymbol() {
		return substSymbol;
	}
}
