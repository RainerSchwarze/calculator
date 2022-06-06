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
public class Quantity implements IQuantity {
	String id;
	String display;
	String displayLong;
	IUnit unit;	// primary

	/**
	 * @param id
	 * @param display
	 * @param displayLong
	 * @param unit
	 */
	public Quantity(String id, String display, String displayLong, IUnit unit) {
		super();
		this.id = id;
		this.display = display;
		this.displayLong = displayLong;
		this.unit = unit;
	}

	/**
	 * @return Returns the display.
	 */
	public String getSymbol() {
		return display;
	}

	/**
	 * @param display The display to set.
	 */
	public void setDisplay(String display) {
		this.display = display;
	}

	/**
	 * @return Returns the displayLong.
	 */
	public String getName() {
		return displayLong;
	}

	/**
	 * @param displayLong The displayLong to set.
	 */
	public void setDisplayLong(String displayLong) {
		this.displayLong = displayLong;
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
	 * @return Returns the unit.
	 */
	public IUnit getUnit() {
		return unit;
	}

	/**
	 * @param unit The unit to set.
	 */
	public void setUnit(IUnit unit) {
		this.unit = unit;
	}


	/**
	 * @return	Returns diagnostic information.
	 * @see de.admadic.units.core.IQuantity#getDiagnosticInfo()
	 */
	public String getDiagnosticInfo() {
		return 
			"d:" + getSymbol() + 
			" u:" + unit.getSymbol() +
			" (id:" + getId() + ")" +
			" long:" + getName();
	}
}
