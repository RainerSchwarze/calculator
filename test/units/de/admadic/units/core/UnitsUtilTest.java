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

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class UnitsUtilTest extends TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(UnitsUtilTest.class);
	}

	/**
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testCreateSymbolView() {
		UnitManager um = new UnitManager();
		um.addDomain(UnitFactory.createDomain("@length", "Length"));
		um.addDomain(UnitFactory.createDomain("@mass", "Mass"));
		um.addDomain(UnitFactory.createDomain("@volume", "Volume"));
		um.addDomain(UnitFactory.createDomain("@density", "Density"));
		um.addFactor(UnitFactory.createFactor("*k", "k", "kilo", 1000.0));
		um.addFactor(UnitFactory.createFactor("*m", "m", "milli", 0.001));
		um.addUnit(UnitFactory.createBaseUnit(um, ".m", "m", "meter", "@length"));
		um.addUnit(UnitFactory.createBaseUnit(um, ".g", "g", "gram", "@mass"));
		um.addUnit(UnitFactory.createFactorUnit(um, "@length", ".m", "*k"));
		um.addUnit(UnitFactory.createFactorUnit(um, "@length", ".m", "*m"));
		um.addUnit(UnitFactory.createFactorUnit(um, "@mass", ".g", "*k"));
		um.addUnit(
				UnitFactory.createCompUnit(um, "@volume")
				.mul(um.getUnit(".m"))
				.mul(um.getUnit(".m"))
				.mul(um.getUnit(".m"))
				);
		um.addUnit(
				UnitFactory.createCompUnit(um, "@volume")
				.mul(um.getUnit("*m.m"))
				.mul(um.getUnit("*m.m"))
				.mul(um.getUnit("*m.m"))
				);
		um.addUnit(
				UnitFactory.createCompUnit(um, "@density")
				.mul(um.getUnit("*k.g"))
				.div(um.getUnit(".m.m.m"))
				);
		IUnit u = um.getUnit("*k.g:m:m:m");
		assertNotNull("Density main unit could not be retrieved", u);

		System.out.println("symbol = " + u.getSymbol());
		
		String view = UnitsUtil.createSymbolView("*k.g:m:m:m", "_kg~m~m~m");
		assertNotNull("Symbol view could not be created", view);
		System.out.println("symbol view = " + view);
		System.out.println("symbol view2 = " + u.getSymbolView());
	}
}
