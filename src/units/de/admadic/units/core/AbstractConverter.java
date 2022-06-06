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
public abstract class AbstractConverter implements IConverter {
	IConverter chainedIn;
	ConverterContext context;

	/**
	 * 
	 */
	public AbstractConverter() {
		super();
	}

	/**
	 * @param conv
	 * @see de.admadic.units.core.IConverter#chainIn(de.admadic.units.core.IConverter)
	 */
	public void chainIn(IConverter conv) {
		chainedIn = conv;
	}

	/**
	 * @param value
	 * @return	Return the converted value.
	 * @see de.admadic.units.core.IConverter#convert(java.lang.Double)
	 */
	public Double convert(Double value) {
		if (getChainedIn()!=null) {
			return convertImpl(getChainedIn().convert(value));
		} else {
			return convertImpl(value);
		}
	}

	/**
	 * @param value
	 * @return	Returns the converted value.
	 */
	public abstract Double convertImpl(Double value);
	
	/**
	 * The default implementation of this method is to redirect the call
	 * to convert(Double). So as a convenience, if not necessary otherwise,
	 * the subclass can implement the convert(Double) method only.
	 * 
	 * @param value
	 * @return	Returns the converted value.
	 * @see de.admadic.units.core.IConverter#convert(double)
	 */
	public double convert(double value) {
		return convert(Double.valueOf(value)).doubleValue();
	}

	/**
	 * @param value
	 * @return	Returns the inversely converted value.
	 * @see de.admadic.units.core.IConverter#iconvert(java.lang.Double)
	 */
	public Double iconvert(Double value) {
		if (getChainedIn()!=null) {
			return getChainedIn().iconvert(iconvertImpl(value));
		} else {
			return iconvertImpl(value);
		}
	}

	/**
	 * @param value
	 * @return	Returns the inversely converted value.
	 */
	public abstract Double iconvertImpl(Double value);

	/**
	 * The default implementation of this method is to redirect the call
	 * to iconvert(Double). So as a convenience, if not necessary otherwise,
	 * the subclass can implement the convert(Double) method only.
	 * 
	 * @param value
	 * @return	Returns the inversely converted value.
	 * @see de.admadic.units.core.IConverter#iconvert(double)
	 */
	public double iconvert(double value) {
		return iconvert(Double.valueOf(value)).doubleValue();
	}

	/**
	 * @param value
	 * @return	Returns the converted value (restricted op to scale)
	 * @see de.admadic.units.core.IConverter#convertScaled(java.lang.Double)
	 */
	public Double convertScaled(Double value) {
		if (getChainedIn()!=null) {
			return convertScaledImpl(getChainedIn().convertScaled(value));
		} else {
			return convertScaledImpl(value);
		}
	}

	/**
	 * @param value
	 * @return	Returns the converted value (restricted op to scale)
	 */
	public abstract Double convertScaledImpl(Double value);

	/**
	 * The default implementation of this method is to redirect the call
	 * to convertScaled(Double). So as a convenience, if not necessary 
	 * otherwise, the subclass can implement the convert(Double) method only.
	 * 
	 * @param value
	 * @return	Returns the converted value (restricted op to scale)
	 * @see de.admadic.units.core.IConverter#convertScaled(double)
	 */
	public double convertScaled(double value) {
		return convertScaled(Double.valueOf(value)).doubleValue();
	}

	/**
	 * @param value
	 * @return	Returns the inversely converted value (restricted op to scale)
	 * @see de.admadic.units.core.IConverter#iconvertScaled(java.lang.Double)
	 */
	public Double iconvertScaled(Double value) {
		if (getChainedIn()!=null) {
			return getChainedIn().iconvertScaled(iconvertScaledImpl(value));
		} else {
			return iconvertScaledImpl(value);
		}
	}

	/**
	 * @param value
	 * @return	Returns the inversely converted value (restr. op to scale)
	 */
	public abstract Double iconvertScaledImpl(Double value);

	/**
	 * The default implementation of this method is to redirect the call
	 * to iconvertScaled(Double). So as a convenience, if not necessary 
	 * otherwise, the subclass can implement the convert(Double) method only.
	 * 
	 * @param value
	 * @return	Returns the inversely converted value (restricted op to scale)
	 * @see de.admadic.units.core.IConverter#iconvertScaled(double)
	 */
	public double iconvertScaled(double value) {
		return iconvertScaled(Double.valueOf(value)).doubleValue();
	}

	/**
	 * @return Returns the chainedIn.
	 */
	protected IConverter getChainedIn() {
		return chainedIn;
	}

	/**
	 * @param chainedIn The chainedIn to set.
	 */
	protected void setChainedIn(IConverter chainedIn) {
		this.chainedIn = chainedIn;
	}


	/**
	 * @return Returns the context.
	 */
	public ConverterContext getContext() {
		return context;
	}

	/**
	 * @param context The context to set.
	 */
	public void setContext(ConverterContext context) {
		this.context = context;
	}
}
