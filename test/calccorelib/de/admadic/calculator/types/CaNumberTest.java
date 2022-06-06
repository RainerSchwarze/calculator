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
public class CaNumberTest extends TestCase {
	CaNumber numStates;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(CaNumberTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		numStates = new CaNumber();
	}

	@Override
	protected void tearDown() throws Exception {
		numStates = null;
		super.tearDown();
	}

	/**
	 * Class under test for CaNumber clone()
	 */
	public void testClone() {
		CaNumber numCloned;
		try {
			numCloned = numStates.clone();
			assertEquals("states differ!", numStates.getState(), numCloned.getState());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			fail("clone not supported exception.");
		} finally {
			numCloned = null;
		}
	}

	/**
	 * 
	 */
	public void testCloneTo() {
		CaNumber numCloned;
		try {
			numCloned = numStates.clone();
			assertEquals("states differ!", numStates.getState(), numCloned.getState());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			fail("clone not supported exception.");
		} finally {
			numCloned = null;
		}
	}

	/**
	 * 
	 */
	public void testGetStateString() {
		// what should we test?
	}

	/**
	 * 
	 */
	public void testSetState() {
		for (int i=CaNumber.STATE_NORMAL; i<=CaNumber.STATE_NEGZERO; i++) {
			numStates.setState(i);
			assertEquals("state differs!", numStates.getState(), i);
		}
		numStates.setState(CaNumber.STATE_NORMAL);
	}

	/**
	 * 
	 */
	public void testSetStateNormal() {
		numStates.setStateNormal();
		assertEquals("state differs!", numStates.getState(), CaNumber.STATE_NORMAL);
		numStates.setNaN();
		assertEquals("state differs!", numStates.getState(), CaNumber.STATE_NAN);
		numStates.setStateNormal();
		assertEquals("state differs!", numStates.getState(), CaNumber.STATE_NORMAL);
	}

	/**
	 * 
	 */
	public void testGetState() {
		for (int i=CaNumber.STATE_NORMAL; i<=CaNumber.STATE_NEGZERO; i++) {
			numStates.setState(i);
			assertEquals("state differs!", numStates.getState(), i);
		}
		numStates.setState(CaNumber.STATE_NORMAL);
	}

	/**
	 * 
	 */
	public void testIsNotNormal() {
		numStates.setStateNormal();
		assertFalse("isNotNormal failed", numStates.isNotNormal());
		numStates.setNaN();
		assertTrue("isNotNormal failed", numStates.isNotNormal());
		numStates.setNegInf();
		assertTrue("isNotNormal failed", numStates.isNotNormal());
		numStates.setPosInf();
		assertTrue("isNotNormal failed", numStates.isNotNormal());
		numStates.setNegZero();
		assertTrue("isNotNormal failed", numStates.isNotNormal());
		numStates.setPosZero();
		assertTrue("isNotNormal failed", numStates.isNotNormal());
		numStates.setStateNormal();
	}

	/**
	 * 
	 */
	public void testIsNaN() {
		numStates.setStateNormal();
		assertFalse("isNaN failed", numStates.isNaN());
		numStates.setNaN();
		assertTrue("isNaN failed", numStates.isNaN());
		numStates.setNegInf();
		assertFalse("isNaN failed", numStates.isNaN());
		numStates.setPosInf();
		assertFalse("isNaN failed", numStates.isNaN());
		numStates.setNegZero();
		assertFalse("isNaN failed", numStates.isNaN());
		numStates.setPosZero();
		assertFalse("isNaN failed", numStates.isNaN());
		numStates.setStateNormal();
	}

	/**
	 * 
	 */
	public void testIsInf() {
		numStates.setStateNormal();
		assertFalse("isInf failed", numStates.isInf());
		numStates.setNaN();
		assertFalse("isInf failed", numStates.isInf());
		numStates.setNegInf();
		assertTrue("isInf failed", numStates.isInf());
		numStates.setPosInf();
		assertTrue("isInf failed", numStates.isInf());
		numStates.setNegZero();
		assertFalse("isInf failed", numStates.isInf());
		numStates.setPosZero();
		assertFalse("isInf failed", numStates.isInf());
		numStates.setStateNormal();
	}

	/**
	 * 
	 */
	public void testIsPosInf() {
		numStates.setStateNormal();
		assertFalse("isPosInf failed", numStates.isPosInf());
		numStates.setNaN();
		assertFalse("isPosInf failed", numStates.isPosInf());
		numStates.setNegInf();
		assertFalse("isPosInf failed", numStates.isPosInf());
		numStates.setPosInf();
		assertTrue("isPosInf failed", numStates.isPosInf());
		numStates.setNegZero();
		assertFalse("isPosInf failed", numStates.isPosInf());
		numStates.setPosZero();
		assertFalse("isPosInf failed", numStates.isPosInf());
		numStates.setStateNormal();
	}

	/**
	 * 
	 */
	public void testIsNegInf() {
		numStates.setStateNormal();
		assertFalse("isNegInf failed", numStates.isNegInf());
		numStates.setNaN();
		assertFalse("isNegInf failed", numStates.isNegInf());
		numStates.setNegInf();
		assertTrue("isNegInf failed", numStates.isNegInf());
		numStates.setPosInf();
		assertFalse("isNegInf failed", numStates.isNegInf());
		numStates.setNegZero();
		assertFalse("isNegInf failed", numStates.isNegInf());
		numStates.setPosZero();
		assertFalse("isNegInf failed", numStates.isNegInf());
		numStates.setStateNormal();
	}

	/**
	 * 
	 */
	public void testIsPosZero() {
		numStates.setStateNormal();
		assertFalse("isPosZero failed", numStates.isPosZero());
		numStates.setNaN();
		assertFalse("isPosZero failed", numStates.isPosZero());
		numStates.setNegInf();
		assertFalse("isPosZero failed", numStates.isPosZero());
		numStates.setPosInf();
		assertFalse("isPosZero failed", numStates.isPosZero());
		numStates.setNegZero();
		assertFalse("isPosZero failed", numStates.isPosZero());
		numStates.setPosZero();
		assertTrue("isPosZero failed", numStates.isPosZero());
		numStates.setStateNormal();
	}

	/**
	 * 
	 */
	public void testIsNegZero() {
		numStates.setStateNormal();
		assertFalse("isNegZero failed", numStates.isNegZero());
		numStates.setNaN();
		assertFalse("isNegZero failed", numStates.isNegZero());
		numStates.setNegInf();
		assertFalse("isNegZero failed", numStates.isNegZero());
		numStates.setPosInf();
		assertFalse("isNegZero failed", numStates.isNegZero());
		numStates.setNegZero();
		assertTrue("isNegZero failed", numStates.isNegZero());
		numStates.setPosZero();
		assertFalse("isNegZero failed", numStates.isNegZero());
		numStates.setStateNormal();
	}

	/**
	 * 
	 */
	public void testSetNaN() {
		numStates.setStateNormal();
		assertEquals("state not normal", numStates.getState(), CaNumber.STATE_NORMAL);
		numStates.setNaN();
		assertEquals("state not NaN", numStates.getState(), CaNumber.STATE_NAN);
		numStates.setStateNormal();
	}

	/**
	 * 
	 */
	public void testSetPosInf() {
		numStates.setStateNormal();
		assertEquals("state not normal", numStates.getState(), CaNumber.STATE_NORMAL);
		numStates.setPosInf();
		assertEquals("state not PosInf", numStates.getState(), CaNumber.STATE_POSINF);
		numStates.setStateNormal();
	}

	/**
	 * 
	 */
	public void testSetNegInf() {
		numStates.setStateNormal();
		assertEquals("state not normal", numStates.getState(), CaNumber.STATE_NORMAL);
		numStates.setNegInf();
		assertEquals("state not NegInf", numStates.getState(), CaNumber.STATE_NEGINF);
		numStates.setStateNormal();
	}

	/**
	 * 
	 */
	public void testSetPosZero() {
		numStates.setStateNormal();
		assertEquals("state not normal", numStates.getState(), CaNumber.STATE_NORMAL);
		numStates.setPosZero();
		assertEquals("state not PosZero", numStates.getState(), CaNumber.STATE_POSZERO);
		numStates.setStateNormal();
	}

	/**
	 * 
	 */
	public void testSetNegZero() {
		numStates.setStateNormal();
		assertEquals("state not normal", numStates.getState(), CaNumber.STATE_NORMAL);
		numStates.setNegZero();
		assertEquals("state not NegZero", numStates.getState(), CaNumber.STATE_NEGZERO);
		numStates.setStateNormal();
	}

	/**
	 * 
	 */
	public void testValueGetters() {
		assertNull("intValue() failed", numStates.intValue());
		assertNull("longValue() failed", numStates.longValue());
		assertNull("shortValue() failed", numStates.shortValue());
		assertNull("byteValue() failed", numStates.byteValue());
		assertNull("doubleValue() failed", numStates.doubleValue());
		assertNull("floatValue() failed", numStates.floatValue());
		assertNull("ratioValue() failed", numStates.ratioValue());
		assertNull("complexValue() failed", numStates.complexValue());
	}
}
