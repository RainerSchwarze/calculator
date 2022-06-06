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
public class BaseUnit extends AbstractUnit {
	String id;	// internal unique		.m
	String symbol;	// display			m
	String name;	// display long	meter
	IConverter rootConverter;

	/**
	 * @param id
	 * @param symbol
	 * @param name
	 */
	public BaseUnit(String id, String symbol, String name) {
		this(null, id, symbol, name);
	}

	/**
	 * @param domain 
	 * @param id
	 * @param symbol
	 * @param name
	 */
	public BaseUnit(Domain domain, String id, String symbol, String name) {
		super();
		this.id = id;
		this.symbol = symbol;
		this.name = name;
		setDomain(domain);
		rootConverter = new ConverterIdent();
	}

	/**
	 * @param domain
	 * @return	Returns this.
	 */
	public BaseUnit dom(Domain domain) {
		setDomain(domain);
		return this;
	}
	
	/**
	 * @return	Returns the display text.
	 * @see de.admadic.units.core.IUnit#getSymbol()
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol The symbol to set.
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return	Returns the long display text.
	 * @see de.admadic.units.core.IUnit#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The displayLong to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return	Returns the id.
	 * @see de.admadic.units.core.IUnit#getId()
	 */
	public String getId() {
		return id;
	}


	/**
	 * @return	Returns the normalized id.
	 * @see de.admadic.units.core.IUnit#getNormalizedId()
	 */
	public String getNormalizedId() {
		return getId();
	}


	/**
	 * @return	Returns the canonical id.
	 * @see de.admadic.units.core.IUnit#getCanonicalId()
	 */
	public String getCanonicalId() {
		return getId();
	}


	/**
	 * @return	Returns the normalized canonical id.
	 * @see de.admadic.units.core.IUnit#getNormalizedCanonicalId()
	 */
	public String getNormalizedCanonicalId() {
		return getCanonicalId();	// same as that for basic units.
	}

	/**
	 * @return	Returns the rooted id.
	 * @see de.admadic.units.core.IUnit#getRootedId()
	 */
	public String getRootedId() {
		return getNormalizedCanonicalId();
	}


	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns always null, because a BaseUnit never has a factor.
	 * @return Returns the factor.
	 * @see IUnit#getFactor
	 */
	public Factor getFactor() {
		return null;
	}

	/**
	 * @param factor The factor to set.
	 */
	public void setFactor(Factor factor) {
		if (factor!=null) {
			throw new UnitsError("Trying to set a factor for a BaseUnit");
		}
		//new ConverterMul(Double.valueOf(factor.getFactor()));
	}

	/**
	 * @return	Returns a String representation.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "s:" + symbol + "(id:" + id + ")";
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
}
