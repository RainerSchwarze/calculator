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
package de.admadic.util;

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class VersionRecordTest extends TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(VersionRecordTest.class);
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
	public void testGetVersionString() {
		VersionRecord vr;

		vr = new VersionRecord(1, 2, 3, 4);
		assertEquals(
				"testGetVersionString: test case #1 failed.",
				"1", vr.getVersionString(VersionRecord.MAJOR, ".", "-"));
		assertEquals(
				"testGetVersionString: test case #2 failed.",
				"1.2", vr.getVersionString(VersionRecord.MINOR, ".", "-"));
		assertEquals(
				"testGetVersionString: test case #3 failed.",
				"1.2.3", vr.getVersionString(VersionRecord.MICRO, ".", "-"));
		assertEquals(
				"testGetVersionString: test case #4 failed.",
				"1.2.3-r4", vr.getVersionString(VersionRecord.REVISION, ".", "-"));
		assertEquals(
				"testGetVersionString: test case #5 failed.",
				"1~2~3*r4", vr.getVersionString(VersionRecord.REVISION, "~", "*"));

		vr = new VersionRecord(1, 2, VersionRecord.NONE, VersionRecord.NONE);
		assertEquals(
				"testGetVersionString: test case #6 failed.",
				"1", vr.getVersionString(VersionRecord.MAJOR, ".", "-"));
		assertEquals(
				"testGetVersionString: test case #7 failed.",
				"1.2", vr.getVersionString(VersionRecord.MINOR, ".", "-"));
		assertEquals(
				"testGetVersionString: test case #8 failed.",
				"1.2", vr.getVersionString(VersionRecord.MICRO, ".", "-"));
		assertEquals(
				"testGetVersionString: test case #9 failed.",
				"1.2", vr.getVersionString(VersionRecord.REVISION, ".", "-"));
		assertEquals(
				"testGetVersionString: test case #10 failed.",
				"1~2", vr.getVersionString(VersionRecord.REVISION, "~", "*"));
	}

	/**
	 *
	 */
	public void testEquals() {
		assertEquals(
				"testEquals: test case #1 failed.",
				new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 3, 4));
		assertFalse(
				"testEquals: test case #2 failed.",
				new VersionRecord(1, 2, 3, 0).equals(
						new VersionRecord(1, 2, 3, 4)));
		assertFalse(
				"testEquals: test case #3 failed.",
				new VersionRecord(1, 2, 0, 4).equals(
						new VersionRecord(1, 2, 3, 4)));
		assertFalse(
				"testEquals: test case #4 failed.",
				new VersionRecord(1, 0, 3, 4).equals(
						new VersionRecord(1, 2, 3, 4)));
		assertFalse(
				"testEquals: test case #5 failed.",
				new VersionRecord(0, 2, 3, 4).equals(
						new VersionRecord(1, 2, 3, 4)));

		Object [][] testCases = {
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 3, 4), 
					Integer.valueOf(VersionRecord.MAJOR), Boolean.TRUE},	
				{new VersionRecord(2, 2, 3, 4), new VersionRecord(1, 2, 3, 4), 
					Integer.valueOf(VersionRecord.MAJOR), Boolean.FALSE},	
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 3, 4), 
					Integer.valueOf(VersionRecord.MINOR), Boolean.TRUE},	
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 3, 4), 
					Integer.valueOf(VersionRecord.MICRO), Boolean.TRUE},	
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 3, 4), 
					Integer.valueOf(VersionRecord.REVISION), Boolean.TRUE},	

				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 3, 0), 
					Integer.valueOf(VersionRecord.REVISION), Boolean.FALSE},	
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 3, 0), 
					Integer.valueOf(VersionRecord.MICRO), Boolean.TRUE},	
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 3, 0), 
					Integer.valueOf(VersionRecord.MINOR), Boolean.TRUE},	
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 3, 0), 
					Integer.valueOf(VersionRecord.MAJOR), Boolean.TRUE},	

				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 0, 0), 
					Integer.valueOf(VersionRecord.REVISION), Boolean.FALSE},	
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 0, 0), 
					Integer.valueOf(VersionRecord.MICRO), Boolean.FALSE},	
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 0, 0), 
					Integer.valueOf(VersionRecord.MINOR), Boolean.TRUE},	
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 0, 0), 
					Integer.valueOf(VersionRecord.MAJOR), Boolean.TRUE},	

				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 0, 0, 0), 
					Integer.valueOf(VersionRecord.REVISION), Boolean.FALSE},	
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 0, 0, 0), 
					Integer.valueOf(VersionRecord.MICRO), Boolean.FALSE},	
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 0, 0, 0), 
					Integer.valueOf(VersionRecord.MINOR), Boolean.FALSE},	
				{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 0, 0, 0), 
					Integer.valueOf(VersionRecord.MAJOR), Boolean.TRUE},	
		};
		for (int i = 0; i < testCases.length; i++) {
			Object[] testCase = testCases[i];
			VersionRecord vr1 = (VersionRecord)testCase[0];
			VersionRecord vr2 = (VersionRecord)testCase[1];
			int upto = ((Integer)testCase[2]).intValue();
			boolean kind = ((Boolean)testCase[3]).booleanValue();
			if (kind) {
				assertTrue(
						"testEquals: test case xT #" + i + " failed.",
						vr1.equalsUpto(vr2, upto));
			} else {
				assertFalse(
						"testEquals: test case xF #" + i + " failed.",
						vr1.equalsUpto(vr2, upto));
			}
		}
	}

	/**
	 * Class under test for VersionRecord valueOf(String)
	 */
	public void testValueOfString() {
		Object [][] testCases = {
				{"1.2.3-r4", new VersionRecord(1, 2, 3, 4)},
				{"1.2.3", new VersionRecord(1, 2, 3, -1)},
				{"1.2", new VersionRecord(1, 2, -1, -1)},
				{"1", new VersionRecord(1, -1, -1, -1)},
				{"10.20.30-r40", new VersionRecord(10, 20, 30, 40)},
		};
		for (int i = 0; i < testCases.length; i++) {
			Object[] testCase = testCases[i];
			String s = (String)testCase[0];
			VersionRecord vr = (VersionRecord)testCase[1];
			VersionRecord vr2 = VersionRecord.valueOf(s);
			assertEquals(
					"testValueOfString: test case x # " + i + " failed.",
					vr, vr2);
		}
	}

	/**
	 * Class under test for VersionRecord valueOf(String, String, String)
	 */
	public void testValueOfStringStringString() {
		Object [][] testCases = {
				{"1.2.3-r4", new VersionRecord(1, 2, 3, 4), ".", "-"},
				{"1.2.3", new VersionRecord(1, 2, 3, -1), ".", "-"},
				{"1.2", new VersionRecord(1, 2, -1, -1), ".", "-"},
				{"1", new VersionRecord(1, -1, -1, -1), ".", "-"},
				{"10-20-30*r40", new VersionRecord(10, 20, 30, 40), "-", "*"},
		};
		for (int i = 0; i < testCases.length; i++) {
			Object[] testCase = testCases[i];
			String s = (String)testCase[0];
			VersionRecord vr = (VersionRecord)testCase[1];
			String vs = (String)testCase[2];
			String rs = (String)testCase[3];
			VersionRecord vr2 = VersionRecord.valueOf(s, vs, rs);
			assertEquals(
					"testValueOfStringStringString: test case x # " + i + " failed.",
					vr, vr2);
		}
	}

	/**
	 * 
	 */
	public void testCompareTo() {
		final Integer SML = Integer.valueOf(-1);
		final Integer EQU = Integer.valueOf(0);
		final Integer LRG = Integer.valueOf(+1);
		Object [][] testCases = {
			{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 3, 4), EQU},
			{new VersionRecord(1, 2, 3, -1), new VersionRecord(1, 2, 3, 4), EQU},
			{new VersionRecord(1, 2, -1, -1), new VersionRecord(1, 2, 3, 4), EQU},
			{new VersionRecord(1, -1, -1, -1), new VersionRecord(1, 2, 3, 4), EQU},
			{new VersionRecord(1, 2, 3, 1), new VersionRecord(1, 2, 3, 4), SML},
			{new VersionRecord(1, 2, 2, 4), new VersionRecord(1, 2, 3, 4), SML},
			{new VersionRecord(1, 1, 3, 4), new VersionRecord(1, 2, 3, 4), SML},
			{new VersionRecord(0, 2, 3, 4), new VersionRecord(1, 2, 3, 4), SML},
			{new VersionRecord(1, 2, 3, 5), new VersionRecord(1, 2, 3, 4), LRG},
			{new VersionRecord(1, 2, 4, 4), new VersionRecord(1, 2, 3, 4), LRG},
			{new VersionRecord(1, 3, 3, 4), new VersionRecord(1, 2, 3, 4), LRG},
			{new VersionRecord(2, 2, 3, 4), new VersionRecord(1, 2, 3, 4), LRG},
			{new VersionRecord(1, 2, 2, 5), new VersionRecord(1, 2, 3, 4), SML},
		};
		for (int i = 0; i < testCases.length; i++) {
			Object[] testCase = testCases[i];
			VersionRecord vr1 = (VersionRecord)testCase[0];
			VersionRecord vr2 = (VersionRecord)testCase[1];
			int res = ((Integer)testCase[2]).intValue();
			assertEquals(
					"testCompareTo: test case x # " + i + " failed.",
					res, vr1.compareTo(vr2));
		}
	}

	/**
	 * 
	 */
	public void testCompareToUpto() {
		final Integer SML = Integer.valueOf(-1);
		final Integer EQU = Integer.valueOf(0);
		final Integer LRG = Integer.valueOf(+1);
		final Integer MJR = Integer.valueOf(VersionRecord.MAJOR);
		final Integer MNR = Integer.valueOf(VersionRecord.MINOR);
		final Integer MCR = Integer.valueOf(VersionRecord.MICRO);
		final Integer REV = Integer.valueOf(VersionRecord.REVISION);
		Object [][] testCases = {
			{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 3, 4), MJR, EQU},
			{new VersionRecord(1, 2, 3, 5), new VersionRecord(1, 2, 3, 4), MCR, EQU},
			{new VersionRecord(1, 2, 3, 5), new VersionRecord(1, 2, 3, 4), REV, LRG},
			{new VersionRecord(1, 3, 3, 4), new VersionRecord(1, 2, 3, 4), MJR, EQU},
			{new VersionRecord(1, 3, 3, 4), new VersionRecord(1, 2, 3, 4), MNR, LRG},
			{new VersionRecord(1, 3, 3, 4), new VersionRecord(1, 2, 3, 4), MCR, LRG},
			{new VersionRecord(1, 1, 3, 4), new VersionRecord(1, 2, 3, 4), MJR, EQU},
			{new VersionRecord(1, 1, 3, 4), new VersionRecord(1, 2, 3, 4), MNR, SML},
			{new VersionRecord(1, 1, 3, 4), new VersionRecord(1, 2, 3, 4), MCR, SML},
		};
		for (int i = 0; i < testCases.length; i++) {
			Object[] testCase = testCases[i];
			VersionRecord vr1 = (VersionRecord)testCase[0];
			VersionRecord vr2 = (VersionRecord)testCase[1];
			int upto = ((Integer)testCase[2]).intValue();
			int res = ((Integer)testCase[3]).intValue();
			assertEquals(
					"testCompareToUpto: test case x # " + i + " failed.",
					res, vr1.compareTo(vr2, upto));
		}
	}

	/**
	 * 
	 */
	public void testCompareToUptoDontIgnoreNull() {
		final Integer SML = Integer.valueOf(-1);
		final Integer EQU = Integer.valueOf(0);
		final Integer LRG = Integer.valueOf(+1);
		final Integer MJR = Integer.valueOf(VersionRecord.MAJOR);
		final Integer MNR = Integer.valueOf(VersionRecord.MINOR);
		final Integer MCR = Integer.valueOf(VersionRecord.MICRO);
		final Integer REV = Integer.valueOf(VersionRecord.REVISION);
		Object [][] testCases = {
			{new VersionRecord(1, 2, 3, 4), new VersionRecord(1, 2, 3, 4), MJR, EQU},
			{new VersionRecord(1, 2, 3, -1), new VersionRecord(1, 2, 3, 4), MJR, EQU},
			{new VersionRecord(1, 2, 3, -1), new VersionRecord(1, 2, 3, 4), REV, SML},
			{new VersionRecord(1, 2, 3, -1), new VersionRecord(1, 2, 3, 0), REV, SML},
			{new VersionRecord(1, 2, 3, -1), new VersionRecord(1, 2, 3, -1), REV, EQU},
			{new VersionRecord(1, 2, 3, 0), new VersionRecord(1, 2, 3, -1), REV, LRG},
			{new VersionRecord(1, 2, -1, -1), new VersionRecord(1, 2, -1, -1), MNR, EQU},
			{new VersionRecord(1, 2, -1, -1), new VersionRecord(1, 2, -1, -1), MNR, EQU},
			{new VersionRecord(1, 2, -1, -1), new VersionRecord(1, 2, -1, -1), MCR, EQU},
			{new VersionRecord(1, -1, -1, -1), new VersionRecord(1, 2, -1, -1), MNR, SML},
			{new VersionRecord(1, 2, -1, -1), new VersionRecord(1, -1, -1, -1), MNR, LRG},
		};
		for (int i = 0; i < testCases.length; i++) {
			Object[] testCase = testCases[i];
			VersionRecord vr1 = (VersionRecord)testCase[0];
			VersionRecord vr2 = (VersionRecord)testCase[1];
			int upto = ((Integer)testCase[2]).intValue();
			int res = ((Integer)testCase[3]).intValue();
			assertEquals(
					"testCompareToUptoDontIgnoreNull: test case x # " + i + " failed.",
					res, vr1.compareTo(vr2, upto, true));
		}
	}
}
