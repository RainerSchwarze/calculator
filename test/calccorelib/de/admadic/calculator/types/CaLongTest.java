/**
 *
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
 */
package de.admadic.calculator.types;

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class CaLongTest extends TestCase {
	CaLong cl;
	CaLong cllim;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(CaLongTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		cl = new CaLong();
		cllim = new CaLong();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Class under test for void cloneTo(CaLong)
	 */
	public void testCloneToCaLong() {
		CaLong clcl; 
		cl.setValue(17);
		clcl = new CaLong();
		cl.cloneTo(clcl);
		assertEquals("value is not the same", cl.value, clcl.value);
		assertEquals("state is not the same", cl.getState(), clcl.getState());
		clcl = null;
		cllim.lowerlimit = -10;
		cllim.upperlimit = +20;
		clcl = new CaLong();
		cllim.cloneTo(clcl);
		assertEquals("lowerlimit is not cloned", cllim.lowerlimit, -10);
		assertEquals("upperlimit is not cloned", cllim.upperlimit, +20);
		clcl = null;
		// reset cllim:
		cllim = new CaLong();
	}

	/**
	 * 
	 */
	public void testClone() {
		CaLong clcl; 
		try {
			cl.setValue(17);
			clcl = cl.clone();
			assertEquals("value is not the same", cl.value, clcl.value);
			assertEquals("state is not the same", cl.getState(), clcl.getState());
			clcl = null;
			cllim.lowerlimit = -10;
			cllim.upperlimit = +20;
			clcl = cllim.clone();
			assertEquals("lowerlimit is not cloned", cllim.lowerlimit, -10);
			assertEquals("upperlimit is not cloned", cllim.upperlimit, +20);
			clcl = null;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			fail("clone not supported exception");
		} finally {
			// reset cllim:
			cllim = new CaLong();
		}
	}

	/**
	 * 
	 */
	public void testCheckStatesLong() {
		// scenarios:
		//	if NotNormal -> copy state and ignore value
		//	if out of limit -> set Inf state and ignore value
		//	else -> copy value
		// pre:
		//	- working clone
		CaLong tmpl = new CaLong();
		CaLong dst, src;
		tmpl.upperlimit = 20;
		tmpl.lowerlimit = -10;

		try {
			{ // test notnormal:
				int states[] = {
						CaNumber.STATE_NAN, 
						CaNumber.STATE_NEGINF, CaNumber.STATE_POSINF,
						CaNumber.STATE_NEGZERO, CaNumber.STATE_POSZERO};
				for (int i=0; i<states.length; i++) {
					src = new CaLong();
					src.setState(states[i]);
					dst = tmpl.clone();
					src.checkStates(dst);
					assertEquals("states not copied", states[i], dst.getState());
				}
			} // (test notnormal)

			{ // test out of limit:
				dst = tmpl.clone();
				src = new CaLong(+50);
				src.checkStates(dst);
				assertTrue("PosInf failed (+50)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaLong(+21);
				src.checkStates(dst);
				assertTrue("PosInf failed (+21)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaLong(+20);
				src.checkStates(dst);
				assertFalse("PosInf failed (+20)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaLong(-50);
				src.checkStates(dst);
				assertTrue("NegInf failed (-50)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaLong(-11);
				src.checkStates(dst);
				assertTrue("NegInf failed (-11)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaLong(-10);
				src.checkStates(dst);
				assertFalse("NegInf failed (-10)", dst.isNegInf());
			} // (test out of limit)

			{ // test normal value
				dst = tmpl.clone();
				src = new CaLong(+17);
				src.checkStates(dst);
				assertEquals("value not copied", dst.value, +17);
				assertEquals("state not normal", dst.getState(), CaNumber.STATE_NORMAL);
			} // (test normal value)
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			fail("clone not supported exception");
		} finally {
			// nothing to clean
		}
	} // (testCheckStatesLong)

	/**
	 * 
	 */
	public void testCheckStatesDouble() {
		// scenarios:
		//	if NotNormal -> copy state and ignore value
		//	if out of limit -> set Inf state and ignore value
		//	else -> copy value
		// pre:
		//	- working clone
		CaDouble tmpl = new CaDouble();
		CaDouble dst;
		CaLong src;
		tmpl.upperlimit = 20.5;
		tmpl.lowerlimit = -10.5;

		try {
			{ // test notnormal:
				int states[] = {
						CaNumber.STATE_NAN, 
						CaNumber.STATE_NEGINF, CaNumber.STATE_POSINF,
						CaNumber.STATE_NEGZERO, CaNumber.STATE_POSZERO};
				for (int i=0; i<states.length; i++) {
					src = new CaLong();
					src.setState(states[i]);
					dst = tmpl.clone();
					src.checkStates(dst);
					assertEquals("states not copied", states[i], dst.getState());
				}
			} // (test notnormal)

			{ // test out of limit:
				dst = tmpl.clone();
				src = new CaLong(+50);
				src.checkStates(dst);
				assertTrue("PosInf failed (+50)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaLong(+21);
				src.checkStates(dst);
				assertTrue("PosInf failed (+21)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaLong(+20);
				src.checkStates(dst);
				assertFalse("PosInf failed (+20)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaLong(-50);
				src.checkStates(dst);
				assertTrue("NegInf failed (-50)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaLong(-11);
				src.checkStates(dst);
				assertTrue("NegInf failed (-11)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaLong(-10);
				src.checkStates(dst);
				assertFalse("NegInf failed (-10)", dst.isNegInf());
			} // (test out of limit)

			{ // test normal value
				dst = tmpl.clone();
				src = new CaLong(+17);
				src.checkStates(dst);
				// need to cast to double so that the correct assert function
				// is choosen:
				assertEquals("value not copied", 
						Double.valueOf(dst.value), Double.valueOf(+17));
				assertEquals("state not normal", 
						dst.getState(), CaNumber.STATE_NORMAL);
			} // (test normal value)
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			fail("clone not supported exception");
		} finally {
			/* nothing */
		}
		
	} // (testCheckStatesDouble)

	/**
	 * 
	 */
	public void testCheckStatesRatio() {
		// scenarios:
		//	if NotNormal -> copy state and ignore value
		//	if out of limit -> set Inf state and ignore value
		//	else -> copy value
		// pre:
		//	- working clone
		CaRatio tmpl = new CaRatio();
		CaRatio dst;
		CaLong src;
		tmpl.upperlimit = 20;
		tmpl.lowerlimit = -10;

		try {
			{ // test notnormal:
				int states[] = { CaNumber.STATE_NAN, CaNumber.STATE_NEGINF,
						CaNumber.STATE_POSINF, CaNumber.STATE_NEGZERO,
						CaNumber.STATE_POSZERO };
				for (int i = 0; i < states.length; i++) {
					src = new CaLong();
					src.setState(states[i]);
					dst = tmpl.clone();
					src.checkStates(dst);
					assertEquals("states not copied", states[i], dst.getState());
				}
			} // (test notnormal)

			{ // test out of limit:
				dst = tmpl.clone();
				src = new CaLong(+50);
				src.checkStates(dst);
				assertTrue("PosInf failed (+50)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaLong(+21);
				src.checkStates(dst);
				assertTrue("PosInf failed (+21)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaLong(+20);
				src.checkStates(dst);
				assertFalse("PosInf failed (+20)", dst.isPosInf());

				dst = tmpl.clone();
				src = new CaLong(-50);
				src.checkStates(dst);
				assertTrue("NegInf failed (-50)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaLong(-11);
				src.checkStates(dst);
				assertTrue("NegInf failed (-11)", dst.isNegInf());

				dst = tmpl.clone();
				src = new CaLong(-10);
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
