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
package de.admadic.calculator.math;

import de.admadic.calculator.types.CaDouble;
import junit.framework.TestCase;

/**
 * 
 * @author Rainer Schwarze
 *
 */
public class DMathTest extends TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(DMathTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testAdd() {
		CaDouble arg0, arg1;
		arg0 = new CaDouble(10.0);
		arg1 = new CaDouble(0.0);
		double eps = 1.0e-9;

		DMath.add(arg0, arg1);
		assertEquals(
				"testAdd: set #1 failed",
				10.0, arg0.value, eps);

		arg1 = new CaDouble(10.0);
		DMath.add(arg0, arg1);
		assertEquals(
				"testAdd: set #2 failed",
				20.0, arg0.value, eps);

		arg1 = new CaDouble(-10.0);
		DMath.add(arg0, arg1);
		assertEquals(
				"testAdd: set #3 failed",
				10.0, arg0.value, eps);

		arg1 = new CaDouble(-20.0);
		DMath.add(arg0, arg1);
		assertEquals(
				"testAdd: set #4 failed",
				-10.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testSub() {
		CaDouble arg0, arg1;
		arg0 = new CaDouble(10.0);
		arg1 = new CaDouble(0.0);
		double eps = 1.0e-9;

		DMath.sub(arg0, arg1);
		assertEquals(
				"testSub: set #1 failed",
				10.0, arg0.value, eps);

		arg1 = new CaDouble(-10.0);
		DMath.sub(arg0, arg1);
		assertEquals(
				"testSub: set #2 failed",
				20.0, arg0.value, eps);

		arg1 = new CaDouble(10.0);
		DMath.sub(arg0, arg1);
		assertEquals(
				"testSub: set #3 failed",
				10.0, arg0.value, eps);

		arg1 = new CaDouble(20.0);
		DMath.sub(arg0, arg1);
		assertEquals(
				"testSub: set #4 failed",
				-10.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testMul() {
		CaDouble arg0, arg1;
		arg0 = new CaDouble(10.0);
		arg1 = new CaDouble(1.0);
		double eps = 1.0e-9;

		DMath.mul(arg0, arg1);
		assertEquals(
				"testMul: set #1 failed",
				10.0, arg0.value, eps);

		arg1 = new CaDouble(-10.0);
		DMath.mul(arg0, arg1);
		assertEquals(
				"testMul: set #2 failed",
				-100.0, arg0.value, eps);

		arg1 = new CaDouble(0.1);
		DMath.mul(arg0, arg1);
		assertEquals(
				"testMul: set #3 failed",
				-10.0, arg0.value, eps);

		arg1 = new CaDouble(0.0);
		DMath.mul(arg0, arg1);
		assertEquals(
				"testMul: set #4 failed",
				0.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testDiv() {
		CaDouble arg0, arg1;
		arg0 = new CaDouble(10.0);
		arg1 = new CaDouble(1.0);
		double eps = 1.0e-9;

		DMath.div(arg0, arg1);
		assertEquals(
				"testDiv: set #1 failed",
				10.0, arg0.value, eps);

		arg1 = new CaDouble(-10.0);
		DMath.div(arg0, arg1);
		assertEquals(
				"testDiv: set #2 failed",
				-1.0, arg0.value, eps);

		arg1 = new CaDouble(0.1);
		DMath.div(arg0, arg1);
		assertEquals(
				"testDiv: set #3 failed",
				-10.0, arg0.value, eps);

		arg1 = new CaDouble(-1.0);
		DMath.div(arg0, arg1);
		assertEquals(
				"testDiv: set #4 failed",
				+10.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testNeg() {
		CaDouble arg0;
		arg0 = new CaDouble(10.0);
		double eps = 1.0e-9;

		DMath.neg(arg0);
		assertEquals(
				"testNeg: set #1 failed",
				-10.0, arg0.value, eps);

		DMath.neg(arg0);
		assertEquals(
				"testNeg: set #2 failed",
				10.0, arg0.value, eps);

		arg0 = new CaDouble(0.0);
		DMath.neg(arg0);
		assertEquals(
				"testNeg: set #3 failed",
				0.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testSqr() {
		CaDouble arg0;
		double eps = 1.0e-9;

		arg0 = new CaDouble(100.0);
		DMath.sqr(arg0);
		assertEquals(
				"testSqr: set #1 failed",
				10000.0, arg0.value, eps);

		arg0 = new CaDouble(1.0);
		DMath.sqr(arg0);
		assertEquals(
				"testSqr: set #2 failed",
				1.0, arg0.value, eps);

		arg0 = new CaDouble(-1.0);
		DMath.sqr(arg0);
		assertEquals(
				"testSqr: set #3 failed",
				1.0, arg0.value, eps);

		arg0 = new CaDouble(0.1);
		DMath.sqr(arg0);
		assertEquals(
				"testSqr: set #4 failed",
				0.01, arg0.value, eps);

		arg0 = new CaDouble(0.0);
		DMath.sqr(arg0);
		assertEquals(
				"testSqr: set #5 failed",
				0.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testSqrt() {
		CaDouble arg0;
		double eps = 1.0e-9;

		arg0 = new CaDouble(100.0);
		DMath.sqrt(arg0);
		assertEquals(
				"testSqrt: set #1 failed",
				10.0, arg0.value, eps);

		arg0 = new CaDouble(1.0);
		DMath.sqrt(arg0);
		assertEquals(
				"testSqrt: set #2 failed",
				1.0, arg0.value, eps);

		arg0 = new CaDouble(0.01);
		DMath.sqrt(arg0);
		assertEquals(
				"testSqrt: set #3 failed",
				0.1, arg0.value, eps);

		arg0 = new CaDouble(0.0);
		DMath.sqrt(arg0);
		assertEquals(
				"testSqrt: set #4 failed",
				0.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testLn() {
		CaDouble arg0;
		double eps = 1.0e-9;
		double ln0p1 = Math.log(0.1);
		double ln10 = Math.log(10.0);

		arg0 = new CaDouble(1.0);
		DMath.ln(arg0);
		assertEquals(
				"testLn: set #1 failed",
				0.0, arg0.value, eps);

		arg0 = new CaDouble(0.1);
		DMath.ln(arg0);
		assertEquals(
				"testLn: set #2 failed",
				ln0p1, arg0.value, eps);

		arg0 = new CaDouble(10.0);
		DMath.ln(arg0);
		assertEquals(
				"testLn: set #3 failed",
				ln10, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testLog() {
		CaDouble arg0;
		double eps = 1.0e-9;
		double log0p1 = Math.log10(0.1);
		double log10 = Math.log10(10.0);

		arg0 = new CaDouble(1.0);
		DMath.log(arg0);
		assertEquals(
				"testLog: set #1 failed",
				0.0, arg0.value, eps);

		arg0 = new CaDouble(0.1);
		DMath.log(arg0);
		assertEquals(
				"testLog: set #2 failed",
				log0p1, arg0.value, eps);

		arg0 = new CaDouble(10.0);
		DMath.log(arg0);
		assertEquals(
				"testLog: set #3 failed",
				log10, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testExp() {
		CaDouble arg0;
		double eps = 1.0e-9;
		double exp0p1 = Math.exp(0.1);
		double exp10 = Math.exp(10.0);

		arg0 = new CaDouble(0.0);
		DMath.exp(arg0);
		assertEquals(
				"testExp: set #1 failed",
				1.0, arg0.value, eps);

		arg0 = new CaDouble(0.1);
		DMath.exp(arg0);
		assertEquals(
				"testExp: set #2 failed",
				exp0p1, arg0.value, eps);

		arg0 = new CaDouble(10.0);
		DMath.exp(arg0);
		assertEquals(
				"testExp: set #3 failed",
				exp10, arg0.value, eps);

		arg0 = new CaDouble(-0.1);
		DMath.exp(arg0);
		assertEquals(
				"testExp: set #4 failed",
				1/exp0p1, arg0.value, eps);

		arg0 = new CaDouble(-10.0);
		DMath.exp(arg0);
		assertEquals(
				"testExp: set #5 failed",
				1/exp10, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testExp10() {
		CaDouble arg0;
		double eps = 1.0e-9;

		arg0 = new CaDouble(0.0);
		DMath.exp10(arg0);
		assertEquals(
				"testExp10: set #1 failed",
				1.0, arg0.value, eps);

		arg0 = new CaDouble(2.0);
		DMath.exp10(arg0);
		assertEquals(
				"testExp10: set #2 failed",
				100.0, arg0.value, eps);

		arg0 = new CaDouble(-2.0);
		DMath.exp10(arg0);
		assertEquals(
				"testExp: set #3 failed",
				0.01, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testInvx() {
		CaDouble arg0;
		double eps = 1.0e-9;

		arg0 = new CaDouble(1.0);
		DMath.invx(arg0);
		assertEquals(
				"testInvx: set #1 failed",
				1.0, arg0.value, eps);

		arg0 = new CaDouble(-1.0);
		DMath.invx(arg0);
		assertEquals(
				"testInvx: set #2 failed",
				-1.0, arg0.value, eps);

		arg0 = new CaDouble(10.0);
		DMath.invx(arg0);
		assertEquals(
				"testInvx: set #3 failed",
				0.1, arg0.value, eps);

		arg0 = new CaDouble(-0.1);
		DMath.invx(arg0);
		assertEquals(
				"testInvx: set #4 failed",
				-10.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testSin() {
		CaDouble arg0;
		double eps = 1.0e-9;

		arg0 = new CaDouble(0.0);
		DMath.sin(arg0);
		assertEquals(
				"testSin: set #1 failed",
				0.0, arg0.value, eps);

		arg0 = new CaDouble(Math.PI);
		DMath.sin(arg0);
		assertEquals(
				"testSin: set #2 failed",
				0.0, arg0.value, eps);

		arg0 = new CaDouble(-Math.PI);
		DMath.sin(arg0);
		assertEquals(
				"testSin: set #3 failed",
				0.0, arg0.value, eps);

		arg0 = new CaDouble(Math.PI/2);
		DMath.sin(arg0);
		assertEquals(
				"testSin: set #4 failed",
				1.0, arg0.value, eps);

		arg0 = new CaDouble(Math.PI*3/2);
		DMath.sin(arg0);
		assertEquals(
				"testSin: set #5 failed",
				-1.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testCos() {
		CaDouble arg0;
		double eps = 1.0e-9;

		arg0 = new CaDouble(0.0);
		DMath.cos(arg0);
		assertEquals(
				"testCos: set #1 failed",
				1.0, arg0.value, eps);

		arg0 = new CaDouble(Math.PI);
		DMath.cos(arg0);
		assertEquals(
				"testCos: set #2 failed",
				-1.0, arg0.value, eps);

		arg0 = new CaDouble(-Math.PI);
		DMath.cos(arg0);
		assertEquals(
				"testCos: set #3 failed",
				-1.0, arg0.value, eps);

		arg0 = new CaDouble(Math.PI/2);
		DMath.cos(arg0);
		assertEquals(
				"testCos: set #4 failed",
				0.0, arg0.value, eps);

		arg0 = new CaDouble(Math.PI*3/2);
		DMath.cos(arg0);
		assertEquals(
				"testCos: set #5 failed",
				0.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testTan() {
		CaDouble arg0;
		double eps = 1.0e-9;

		arg0 = new CaDouble(0.0);
		DMath.tan(arg0);
		assertEquals(
				"testTan: set #1 failed",
				0.0, arg0.value, eps);

		arg0 = new CaDouble(Math.PI);
		DMath.tan(arg0);
		assertEquals(
				"testTan: set #2 failed",
				0.0, arg0.value, eps);

		arg0 = new CaDouble(-Math.PI);
		DMath.tan(arg0);
		assertEquals(
				"testTan: set #3 failed",
				0.0, arg0.value, eps);

		arg0 = new CaDouble(Math.PI/4);
		DMath.tan(arg0);
		assertEquals(
				"testTan: set #4 failed",
				1.0, arg0.value, eps);

		arg0 = new CaDouble(Math.PI*3/4);
		DMath.tan(arg0);
		assertEquals(
				"testTan: set #5 failed",
				-1.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testCot() {
		CaDouble arg0;
		double eps = 1.0e-9;

		arg0 = new CaDouble(Math.PI/2);
		DMath.cot(arg0);
		assertEquals(
				"testCot: set #1 failed",
				0.0, arg0.value, eps);

		arg0 = new CaDouble(-Math.PI/2);
		DMath.cot(arg0);
		assertEquals(
				"testCot: set #2 failed",
				0.0, arg0.value, eps);

		arg0 = new CaDouble(Math.PI/4);
		DMath.cot(arg0);
		assertEquals(
				"testCot: set #3 failed",
				1.0, arg0.value, eps);

		arg0 = new CaDouble(-Math.PI/4);
		DMath.cot(arg0);
		assertEquals(
				"testCot: set #4 failed",
				-1.0, arg0.value, eps);

		arg0 = new CaDouble(Math.PI*3/4);
		DMath.cot(arg0);
		assertEquals(
				"testCot: set #5 failed",
				-1.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testExpx() {
		CaDouble arg0, arg1;
		double eps = 1.0e-9;

		arg0 = new CaDouble(10.0);
		arg1 = new CaDouble(1.0);
		DMath.expx(arg0, arg1);
		assertEquals(
				"testExpx: set #1 failed",
				10.0, arg0.value, eps);

		arg0 = new CaDouble(10.0);
		arg1 = new CaDouble(0.0);
		DMath.expx(arg0, arg1);
		assertEquals(
				"testExpx: set #2 failed",
				1.0, arg0.value, eps);

		arg0 = new CaDouble(10.0);
		arg1 = new CaDouble(10.0);
		DMath.expx(arg0, arg1);
		assertEquals(
				"testExpx: set #3 failed",
				1e10, arg0.value, 10);

		arg0 = new CaDouble(10.0);
		arg1 = new CaDouble(-2.0);
		DMath.expx(arg0, arg1);
		assertEquals(
				"testExpx: set #4 failed",
				0.01, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testSqrtx() {
		CaDouble arg0, arg1;
		double eps = 1.0e-9;

		arg0 = new CaDouble(2.0);
		arg1 = new CaDouble(16.0);
		DMath.sqrtx(arg0, arg1);
		assertEquals(
				"testSqrtx: set #1 failed",
				4.0, arg0.value, eps);

		arg0 = new CaDouble(1.0);
		arg1 = new CaDouble(10.0);
		DMath.sqrtx(arg0, arg1);
		assertEquals(
				"testSqrtx: set #2 failed",
				10.0, arg0.value, eps);

		arg0 = new CaDouble(10.0);
		arg1 = new CaDouble(1.0);
		DMath.sqrtx(arg0, arg1);
		assertEquals(
				"testSqrtx: set #3 failed",
				1.0, arg0.value, 10);

		arg0 = new CaDouble(10.0);
		arg1 = new CaDouble(1e10);
		DMath.sqrtx(arg0, arg1);
		assertEquals(
				"testSqrtx: set #4 failed",
				10.0, arg0.value, eps);
	}

	/**
	 * 
	 */
	public void testGamma() {
		CaDouble arg0;
		double eps = 1.0e-6;
		double [] facts = new double[10];
		for (int i = 0; i < facts.length; i++) {
			if (i==0) {
				facts[i] = 1.0;
			} else {
				facts[i] = 1.0*i*facts[i-1];
			}
		}
		// gamma(x+1) = x!

		arg0 = new CaDouble(1.0);
		DMath.gamma(arg0);
		assertFalse("result not normal", arg0.isNotNormal());
		assertEquals(
				"testGamma: set #1 failed",
				facts[0], arg0.value, eps);

		arg0 = new CaDouble(2.0);
		DMath.gamma(arg0);
		assertFalse("result not normal", arg0.isNotNormal());
		assertEquals(
				"testGamma: set #2 failed",
				facts[1], arg0.value, eps);

		arg0 = new CaDouble(3.0);
		DMath.gamma(arg0);
		assertFalse("result not normal", arg0.isNotNormal());
		assertEquals(
				"testGamma: set #3 failed",
				facts[2], arg0.value, eps);

		arg0 = new CaDouble(9.0);
		DMath.gamma(arg0);
		assertFalse("result not normal", arg0.isNotNormal());
		assertEquals(
				"testGamma: set #4 failed",
				facts[8], arg0.value, eps);

		arg0 = new CaDouble(1.10);
		DMath.gamma(arg0);
		assertFalse("result not normal", arg0.isNotNormal());
		assertEquals(
				"testGamma: set #5 failed",
				0.95135, arg0.value, 1e-5);

		arg0 = new CaDouble(1.50);
		DMath.gamma(arg0);
		assertFalse("result not normal", arg0.isNotNormal());
		assertEquals(
				"testGamma: set #6 failed",
				0.88623, arg0.value, 1e-5);
		arg0 = new CaDouble(1.90);
		DMath.gamma(arg0);
		assertFalse("result not normal", arg0.isNotNormal());
		assertEquals(
				"testGamma: set #7 failed",
				0.96177, arg0.value, 1e-5);
	}
}
