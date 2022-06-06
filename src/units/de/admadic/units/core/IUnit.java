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

import java.util.Date;

/**
 * The IUnit declares the generic interface for a unit.
 * See AbstractUnit for more details.
 * 
 * @author Rainer Schwarze
 */
public interface IUnit {
	/**
	 * @return	Returns the date of last change.
	 */	
	public abstract Date getLastChange();

	/**
	 * Sets the current date as last change.
	 */
	public abstract void touchLastChange();

	/**
	 * @param date
	 */
	public abstract void setLastChange(Date date);
	
	/**
	 * @return Returns the symbol.
	 */
	public abstract String getSymbol();

	/**
	 * @return	Returns the view optimized symbol string.
	 */
	public abstract String getSymbolView();
	
	/**
	 * @return Returns the name.
	 */
	public abstract String getName();

	/**
	 * @return Returns the id.
	 */
	public abstract String getId();

	/**
	 * @return Returns the normalized id.
	 */
	public abstract String getNormalizedId();

	/**
	 * @return	Returns the canonical id.
	 */
	public abstract String getCanonicalId();

	/**
	 * @return	Returns the normalized canonical id.
	 */
	public abstract String getNormalizedCanonicalId();

	/**
	 * @return Returns the rooted id.
	 */
	public abstract String getRootedId();

	/**
	 * @param id
	 * @return	Returns the id for the given constant.
	 */
	public abstract String getId(int id);
	
	/**
	 * @return	Returns the context for this unit.
	 */
	public abstract UnitContext getContext();
	
	/**
	 * @return Returns the factor of the unit.
	 */
	public abstract Factor getFactor();

	/**
	 * @return	Returns a diagnostic information about the item.
	 */
	public abstract String getDiagnosticInfo();

	/**
	 * @param unit
	 * @return	Returns true, if this Unit has the same magnitude as the 
	 * given one.
	 */
	public abstract boolean isSameMagnitude(IUnit unit);
	
	/**
	 * @param unit
	 * @return	Returns true, if this IUnit has the same dimension as the given
	 * 			one.
	 */
	public abstract boolean isSameDimension(IUnit unit);

	/**
	 * @param unit
	 * @return	Returns true, if this Unit has the same unit as the given unit.
	 */
	public abstract boolean isSameUnit(IUnit unit);

	/**
	 * @return	Returns the factor to convert this units value to the 
	 * root unit, null if this is not possible.
	 */
	public abstract Double getRootFactor();
	
	/**
	 * @return	Returns the converter which converts this unit into
	 * 			the normalized root unit.
	 */
	public abstract IConverter getRootConverter();

	/**
	 * Checks for an acyclic unit dependency.
	 * An instance must check, whether it is itself equal to the
	 * given unit and if so, then raise an Error.
	 * Then call this method for all subunits.
	 *
	 * @param unit
	 */
	public abstract void checkAcyclic(IUnit unit);

	/**
	 * @return	Returns the Domain for this Unit.
	 */
	public abstract Domain getDomain();
}
