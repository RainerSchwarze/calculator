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
public class Factor {
	// FIXME: check whether a factor should return the tag '*' with the id.
	
	String id;		// unique internal
	String symbol;	// display name	(en_US) k
	String name;	// kilo
	double factor;	// factor for calculation

	IConverter rootConverter;
	
	/**
	 * @param id
	 * @param symbol
	 * @param name 
	 * @param factor
	 */
	public Factor(String id, String symbol, String name, double factor) {
		super();
		this.id = id;
		this.symbol = symbol;
		this.name = name;
		setFactor(factor);
	}

	/**
	 * @return Returns the display.
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol The display to set.
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return Returns the factor.
	 */
	public double getFactor() {
		return factor;
	}

	/**
	 * @param factor The factor to set.
	 */
	public void setFactor(double factor) {
		this.factor = factor;
		rootConverter = new ConverterMul(Double.valueOf(factor));
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the displayLong.
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
	 * @return	Returns a string representation.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id + ":" + symbol + ":" + factor;
	}

	/**
	 * @return	Returns the converter which converts this unit into
	 * 			the normalized root unit.
	 */
	public IConverter getRootConverter() {
		return rootConverter;
	}
}
