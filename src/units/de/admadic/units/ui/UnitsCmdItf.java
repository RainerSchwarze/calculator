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
package de.admadic.units.ui;

/**
 * @author Rainer Schwarze
 *
 */
public interface UnitsCmdItf {
	/**
	 * 
	 */
	public abstract void doFilterFieldsSet();

	/**
	 * 
	 */
	public abstract void doFilterFieldsClear();

	/**
	 * 
	 */
	public abstract void doFilterFieldsAdd();

	/**
	 * 
	 */
	public abstract void doFilterFieldsRemove();

	/**
	 * 
	 */
	public abstract void doFilterSousSet();

	/**
	 * 
	 */
	public abstract void doFilterSousClear();

	/**
	 * 
	 */
	public abstract void doFilterSousAdd();

	/**
	 * 
	 */
	public abstract void doFilterSousRemove();

	/**
	 * 
	 */
	public abstract void doFilterDomainsSet();

	/**
	 * 
	 */
	public abstract void doFilterDomainsClear();

	/**
	 * 
	 */
	public abstract void doFilterDomainsAdd();

	/**
	 * 
	 */
	public abstract void doFilterDomainsRemove();

	/**
	 * 
	 */
	public abstract void doSetFieldsSet();

	/**
	 * 
	 */
	public abstract void doSetFieldsClear();

	/**
	 * 
	 */
	public abstract void doSetFieldsAdd();

	/**
	 * 
	 */
	public abstract void doSetFieldsRemove();

	/**
	 * 
	 */
	public abstract void doSetSousSet();

	/**
	 * 
	 */
	public abstract void doSetSousClear();

	/**
	 * 
	 */
	public abstract void doSetSousAdd();

	/**
	 * 
	 */
	public abstract void doSetSousRemove();

	/**
	 * 
	 */
	public abstract void doSetDomainsSet();

	/**
	 * 
	 */
	public abstract void doSetDomainsClear();

	/**
	 * 
	 */
	public abstract void doSetDomainsAdd();

	/**
	 * 
	 */
	public abstract void doSetDomainsRemove();
}
