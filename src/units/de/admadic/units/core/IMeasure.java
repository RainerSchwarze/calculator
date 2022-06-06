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
public interface IMeasure {
	/**
	 * @return	Returns the id of this measure.
	 */
	public abstract String getId();
	
	/**
	 * @return	Returns the quantity of the measure. ("Force")
	 */
	public abstract IQuantity getQuantity();

	/**
	 * @param v
	 */
	public abstract void setValue(Double v);
	
	/**
	 * @return	Returns the value of the measure. ("10.2")
	 */
	public abstract Double getValue();

	/**
	 * @return	Returns a formatted view of the value.
	 */
	public abstract String getValueView();
	
	/**
	 * @return	Returns the value in root unit space.
	 */
	public abstract Double getRootValue();

	/**
	 * @param v
	 */
	public abstract void setRootValue(Double v);
	
	/**
	 * @return	Returns the unit of this measure. ("kN")
	 */
	public abstract IUnit getUnit();

	/**
	 * @return	Returns a display string.
	 */
	public abstract String getDisplay();

	/**
	 * @param u
	 */
	public abstract void changeUnit(IUnit u);
	
	/**
	 * @return	Returns a diagnostic information.
	 */
	public abstract String getDiagnosticInfo();

	/**
	 * @param mf
	 */
	public abstract void setMeasureFormatter(MeasureFormatter mf);

	/**
	 * @return	Returns the MeasureFormatter, if any.
	 */
	public abstract MeasureFormatter getMeasureFormatter();

	/**
	 * @return	Returns the name.
	 */
	public abstract String getNameDisplay();

	/**
	 * @return	Returns the symbol.
	 */
	public abstract String getSymbolDisplay();
}
