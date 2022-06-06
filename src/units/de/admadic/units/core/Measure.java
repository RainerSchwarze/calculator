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
public class Measure implements IMeasure {
	String id;
	IQuantity quantity;
	Double value;
	IUnit unit;

	String name;
	String symbol;

	MeasureFormatter mf;
	
	/**
	 * @param id 
	 * @param quantity
	 * @param value
	 * @param unit
	 * @param symbol 
	 * @param name 
	 */
	public Measure(
			String id, 
			IQuantity quantity, 
			Double value, IUnit unit, 
			String symbol, String name) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.value = value;
		this.unit = unit;
		this.symbol = symbol;
		this.name = name;
	}

	/**
	 * @param id
	 * @param quantity
	 * @param value
	 * @param unit
	 */
	public Measure(String id, IQuantity quantity, Double value, IUnit unit) {
		this(id, quantity, value, unit, null, null);
	}

	/**
	 * @param id 
	 * @param quantity
	 * @param value
	 * @param unit
	 */
	public Measure(String id, IQuantity quantity, double value, IUnit unit) {
		this(id, quantity, Double.valueOf(value), unit, null, null);
	}

	/**
	 * @param quantity
	 * @param value
	 * @param unit
	 */
	public Measure(IQuantity quantity, Double value, IUnit unit) {
		this("", quantity, value, unit);
	}

	/**
	 * @param quantity
	 * @param value
	 * @param unit
	 */
	public Measure(IQuantity quantity, double value, IUnit unit) {
		this(quantity, Double.valueOf(value), unit);
	}

	/**
	 * @param mf
	 */
	public void setMeasureFormatter(MeasureFormatter mf) {
		this.mf = mf;
	}
	
	/**
	 * @return Returns the quantity.
	 */
	public IQuantity getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity The quantity to set.
	 */
	public void setQuantity(IQuantity quantity) {
		this.quantity = quantity;
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
	 * FIXME: solve uncertain situations
	 * - what if there is no existing unit?
	 * 
	 * @param u
	 * @see de.admadic.units.core.IMeasure#changeUnit(de.admadic.units.core.IUnit)
	 */
	public void changeUnit(IUnit u) {
		if (u==getUnit()) return; // nothing to do
		Double rootv = getRootValue();
		setUnit(u);
		IConverter conv = getUnit().getRootConverter();
		Double newv = conv.iconvert(rootv);
		setValue(newv);
	}

	/**
	 * @return Returns the value.
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @return	Returns the value in root unit space.
	 * @see de.admadic.units.core.IMeasure#getRootValue()
	 */
	public Double getRootValue() {
		if (this.value==null) return null;
		double v = this.value.doubleValue();
		IConverter conv = this.unit.getRootConverter();
		v = conv.convert(v);
		return Double.valueOf(v);
	}
	
	/**
	 * @param value The value to set.
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * @return	Returns a display string.
	 * @see de.admadic.units.core.IMeasure#getDisplay()
	 */
	public String getDisplay() {
		return quantity.getSymbol() + "=" + value + " " + unit.getSymbol();
	}


	/**
	 * @return	Returns diagnostic information.
	 * @see de.admadic.units.core.IMeasure#getDiagnosticInfo()
	 */
	public String getDiagnosticInfo() {
		return 
			quantity.getSymbol() + "=" + 
			value + " " + unit.getSymbol() +
			" id:" + getId() + 
			" n=" + unit.getNormalizedCanonicalId();
	}


	/**
	 * @return	Returns the id of this measure.
	 * @see de.admadic.units.core.IMeasure#getId()
	 */
	public String getId() {
		return id;
	}


	/**
	 * @return	Returns the MeasureFormatter, if there is any.
	 * @see de.admadic.units.core.IMeasure#getMeasureFormatter()
	 */
	public MeasureFormatter getMeasureFormatter() {
		return mf;
	}

	/**
	 * @return	Returns a display view of the value.
	 * @see de.admadic.units.core.IMeasure#getValueView()
	 */
	public String getValueView() {
		if (mf!=null) {
			return mf.format(getValue());
		} else {
			return getValue().toString();
		}
	}


	/**
	 * @param v
	 * @see de.admadic.units.core.IMeasure#setRootValue(java.lang.Double)
	 */
	public void setRootValue(Double v) {
		if (v==null) return; // ? FIXME: is that ok?
		IConverter conv = this.unit.getRootConverter();
		v = conv.iconvert(v);
		setValue(v);
	}

	/**
	 * @return	Returns a display of the name.
	 */
	public String getNameDisplay() {
		if (getName()!=null) return name;
		if (getQuantity()!=null) return getQuantity().getName();
		return null;
	}

	/**
	 * @return	Returns a display of the symbol.
	 */
	public String getSymbolDisplay() {
		if (getSymbol()!=null) return symbol;
		if (getQuantity()!=null) return getQuantity().getSymbol();
		return null;
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
	 * @return Returns the symbol.
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
}
