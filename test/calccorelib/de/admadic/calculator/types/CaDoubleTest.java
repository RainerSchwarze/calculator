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
package de.admadic.calculator.types;

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class CaDoubleTest extends TestCase {
	CaDouble cd;
	CaDouble cdlim;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(CaDoubleTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		cd = new CaDouble();
		cdlim = new CaDouble();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Class under test for CaDouble clone()
	 */
	public void testClone() {
		CaDouble cdcd;
		try {
			cd.setValue(17);
			cdcd = cd.clone();
			assertEquals("value is not the same", 
					Double.valueOf(cd.value), Double.valueOf(cdcd.value));
			assertEquals("state is not the same", 
					cd.getState(), cdcd.getState());
			cdcd = null;
			cdlim.lowerlimit = -10.3;
			cdlim.upperlimit = +20.3;
			cdcd = cdlim.clone();
			assertEquals("lowerlimit is not cloned", 
					Double.valueOf(cdlim.lowerlimit), Double.valueOf(-10.3));
			assertEquals("upperlimit is not cloned", 
					Double.valueOf(cdlim.upperlimit), Double.valueOf(+20.3));
			cdcd = null;
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			fail("clone not supported exception");
		} finally {
			// reset cdlim:
			cdlim = new CaDouble();
		}
	}

	/**
	 * Class under test for void cloneTo(CaDouble)
	 */
	public void testCloneToCaDouble() {
		CaDouble cdcd; 
		cd.setValue(17.42);
		cdcd = new CaDouble();
		cd.cloneTo(cdcd);
		assertEquals("value is not the same", 
				Double.valueOf(cd.value), Double.valueOf(cdcd.value));
		assertEquals("state is not the same", 
				cd.getState(), cdcd.getState());
		cdcd = null;
		cdlim.lowerlimit = -10.3;
		cdlim.upperlimit = +20.3;
		cdcd = new CaDouble();
		cdlim.cloneTo(cdcd);
		assertEquals("lowerlimit is not cloned", 
				Double.valueOf(cdlim.lowerlimit), Double.valueOf(-10.3));
		assertEquals("upperlimit is not cloned", 
				Double.valueOf(cdlim.upperlimit), Double.valueOf(+20.3));
		cdcd = null;
		// reset cdlim:
		cdlim = new CaDouble();
	}

	/**
	 * Class under test for void checkStates(CaDouble)
	 */
	public void testCheckStatesCaDouble() {
		// scenarios:
		//	if NotNormal -> copy state and ignore value
		//	if out of limit -> set Inf state and ignore value
		//	else -> copy value
		// pre:
		//	- working clone
		CaDouble tmpl = new CaDouble();
		CaDouble dst, src;
		tmpl.upperlimit = 20.3;
		tmpl.lowerlimit = -10.3;

		
		try {
			{ // test notnormal:
				int states[] = { CaNumber.STATE_NAN, CaNumber.STATE_NEGINF,
						CaNumber.STATE_POSINF, CaNumber.STATE_NEGZERO,
						CaNumber.STATE_POSZERO };
				for (int i = 0; i < states.length; i++) {
					src = new CaDouble();
					src.setState(states[i]);
					dst = tmpl.clone();
					src.checkStates(dst);
					assertEquals("states not copied", states[i], dst.getState());
				}
			} // (test notnormal)

			{ // test out of limit:
				dst = tmpl.clone();
				src = new CaDouble(+50.5);
				src.checkStates(dst);
				assertTrue("PosInf failed (+50.1)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaDouble(+21.1);
				src.checkStates(dst);
				assertTrue("PosInf failed (+21.1)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaDouble(+20.1);
				src.checkStates(dst);
				assertFalse("PosInf failed (+20.1)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaDouble(-50.1);
				src.checkStates(dst);
				assertTrue("NegInf failed (-50.1)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaDouble(-11.1);
				src.checkStates(dst);
				assertTrue("NegInf failed (-11.1)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaDouble(-10.1);
				src.checkStates(dst);
				assertFalse("NegInf failed (-10.1)", dst.isNegInf());
			} // (test out of limit)

			{ // test normal value
				dst = tmpl.clone();
				src = new CaDouble(+17.42);
				src.checkStates(dst);
				assertEquals("value not copied", 
						Double.valueOf(dst.value), Double.valueOf(+17.42));
				assertEquals("state not normal", 
						dst.getState(),
						CaNumber.STATE_NORMAL);
			} // (test normal value)
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			fail("clone not supported exception");
		} finally {
			/* nothing */
		}
	} // (testCheckStatesCaDouble)

	/**
	 * Class under test for void checkStates(CaComplex)
	 */
	public void testCheckStatesCaComplex() {
		// scenarios:
		//	if NotNormal -> copy state and ignore value
		//	if out of limit -> set Inf state and ignore value
		//	else -> copy value
		// pre:
		//	- working clone
		CaComplex tmpl = new CaComplex();
		CaComplex dst;
		CaDouble src;
		tmpl.upperlimit = 20.3;
		tmpl.lowerlimit = -10.3;

		try {
			{ // test notnormal:
				int states[] = { CaNumber.STATE_NAN, CaNumber.STATE_NEGINF,
						CaNumber.STATE_POSINF, CaNumber.STATE_NEGZERO,
						CaNumber.STATE_POSZERO };
				for (int i = 0; i < states.length; i++) {
					src = new CaDouble();
					src.setState(states[i]);
					dst = tmpl.clone();
					src.checkStates(dst);
					assertEquals("states not copied", states[i], dst.getState());
				}
			} // (test notnormal)

			{ // test out of limit:
				dst = tmpl.clone();
				src = new CaDouble(+50.5);
				src.checkStates(dst);
				assertTrue("PosInf failed (+50.1)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaDouble(+21.1);
				src.checkStates(dst);
				assertTrue("PosInf failed (+21.1)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaDouble(+20.1);
				src.checkStates(dst);
				assertFalse("PosInf failed (+20.1)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaDouble(-50.1);
				src.checkStates(dst);
				assertTrue("NegInf failed (-50.1)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaDouble(-11.1);
				src.checkStates(dst);
				assertTrue("NegInf failed (-11.1)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaDouble(-10.1);
				src.checkStates(dst);
				assertFalse("NegInf failed (-10.1)", dst.isNegInf());
			} // (test out of limit)

			{ // test normal value
				dst = tmpl.clone();
				src = new CaDouble(+17.42);
				src.checkStates(dst);
				assertEquals("value not copied (re)", 
						Double.valueOf(dst.valueRe), Double.valueOf(+17.42));
				assertEquals("value not copied (im)", 
						Double.valueOf(dst.valueIm), Double.valueOf(+0.0));
				assertEquals("state not normal", 
						dst.getState(),
						CaNumber.STATE_NORMAL);
			} // (test normal value)
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			fail("clone not supported exception");
		} finally {
			/* nothing */
		}
	} // (testCheckStatesCaComplex)

	/**
	 * Class under test for void checkStates(CaLong)
	 */
	public void testCheckStatesCaLong() {
		// scenarios:
		//	if NotNormal -> copy state and ignore value
		//	if out of limit -> set Inf state and ignore value
		//	else -> copy value
		// pre:
		//	- working clone
		CaLong tmpl = new CaLong();
		CaLong dst;
		CaDouble src;
		tmpl.upperlimit = 20;
		tmpl.lowerlimit = -10;

		try {
			{ // test notnormal:
				int states[] = { CaNumber.STATE_NAN, CaNumber.STATE_NEGINF,
						CaNumber.STATE_POSINF, CaNumber.STATE_NEGZERO,
						CaNumber.STATE_POSZERO };
				for (int i = 0; i < states.length; i++) {
					src = new CaDouble();
					src.setState(states[i]);
					dst = tmpl.clone();
					src.checkStates(dst);
					assertEquals("states not copied", states[i], dst.getState());
				}
			} // (test notnormal)

			{ // test out of limit:
				dst = tmpl.clone();
				src = new CaDouble(+50.5);
				src.checkStates(dst);
				assertTrue("PosInf failed (+50.1)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaDouble(+21.0);
				src.checkStates(dst);
				assertTrue("PosInf failed (+21.0)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaDouble(+20.0);
				src.checkStates(dst);
				assertFalse("PosInf failed (+20.0)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaDouble(-50.1);
				src.checkStates(dst);
				assertTrue("NegInf failed (-50.1)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaDouble(-11.0);
				src.checkStates(dst);
				assertTrue("NegInf failed (-11.0)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaDouble(-10.0);
				src.checkStates(dst);
				assertFalse("NegInf failed (-10.0)", dst.isNegInf());
			} // (test out of limit)

			{ // test normal value
				dst = tmpl.clone();
				src = new CaDouble(+17.0);
				src.checkStates(dst);
				assertEquals("value not copied", dst.value, +17);
				assertEquals("state not normal", dst.getState(),
						CaNumber.STATE_NORMAL);
			} // (test normal value)
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			fail("clone not supported exception");
		} finally {
			/* nothing */
		}
	} // (testCheckStatesCaComplex)

	/**
	 * Class under test for void checkStates(CaRatio)
	 */
	public void testCheckStatesCaRatio() {
		// scenarios:
		//	if NotNormal -> copy state and ignore value
		//	if out of limit -> set Inf state and ignore value
		//	else -> copy value
		// pre:
		//	- working clone
		CaRatio tmpl = new CaRatio();
		CaRatio dst;
		CaDouble src;
		tmpl.upperlimit = 20;
		tmpl.lowerlimit = -10;

		try {
			{ // test notnormal:
				int states[] = { CaNumber.STATE_NAN, CaNumber.STATE_NEGINF,
						CaNumber.STATE_POSINF, CaNumber.STATE_NEGZERO,
						CaNumber.STATE_POSZERO };
				for (int i = 0; i < states.length; i++) {
					src = new CaDouble();
					src.setState(states[i]);
					dst = tmpl.clone();
					src.checkStates(dst);
					assertEquals("states not copied", states[i], dst.getState());
				}
			} // (test notnormal)

			{ // test out of limit:
				dst = tmpl.clone();
				src = new CaDouble(+50);
				src.checkStates(dst);
				assertTrue("PosInf failed (+50)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaDouble(+21);
				src.checkStates(dst);
				assertTrue("PosInf failed (+21)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaDouble(+20);
				src.checkStates(dst);
				assertFalse("PosInf failed (+20)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaDouble(-50);
				src.checkStates(dst);
				assertTrue("NegInf failed (-50)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaDouble(-11);
				src.checkStates(dst);
				assertTrue("NegInf failed (-11)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaDouble(-10);
				src.checkStates(dst);
				assertFalse("NegInf failed (-10)", dst.isNegInf());
			} // (test out of limit)

			{ // test normal value
				// we do not test that, because that is part of the CaRatio 
				// testing suite.
			} // (test normal value)
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			fail("clone not supported exception");
		} finally {
			/* nothing */
		}
	} // (testCheckStatesDouble)
}
