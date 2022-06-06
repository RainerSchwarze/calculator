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
package de.admadic.calculator.modules.indxp.core;

/**
 * @author Rainer Schwarze
 *
 */
public class Factor {
	String name;
	String entity;
	String unit;
	// that may already go into another class!:
	String valueLow;
	String valueHigh;

	/**
	 * 
	 */
	public Factor() {
		this(null, null, null, null, null);
	}
	
	/**
	 * @param name
	 */
	public Factor(String name) {
		this(name, null, null, null, null);
	}

	/**
	 * @param name
	 * @param entity 
	 * @param unit
	 */
	public Factor(String name, String entity, String unit) {
		this(name, entity, unit, null, null);
	}

	/**
	 * @param name
	 * @param entity 
	 * @param unit
	 * @param low
	 * @param high
	 */
	public Factor(String name, String entity, String unit, String low, String high) {
		super();
		this.name = name;
		this.entity = entity;
		this.unit = unit;
		valueLow = low;
		valueHigh = high;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the unit.
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit The unit to set.
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return Returns the valueHigh.
	 */
	public String getValueHigh() {
		return valueHigh;
	}

	/**
	 * @param valueHigh The valueHigh to set.
	 */
	public void setValueHigh(String valueHigh) {
		this.valueHigh = valueHigh;
	}

	/**
	 * @return Returns the valueLow.
	 */
	public String getValueLow() {
		return valueLow;
	}

	/**
	 * @param valueLow The valueLow to set.
	 */
	public void setValueLow(String valueLow) {
		this.valueLow = valueLow;
	}

	/**
	 * @return Returns the entity.
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param entity The entity to set.
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}
}