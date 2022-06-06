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
 * 
 * @author Rainer Schwarze
 *
 */
public class StringUtilTest extends TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(StringUtilTest.class);
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
	public void testJoin() {
		String [] sa = {"ab", "cd", "ef"};
		assertEquals(
				"testJoin: join std failed",
				"ab-cd-ef", StringUtil.join(sa, "-"));
		assertEquals(
				"testJoin: join with empty separator failed",
				"abcdef", StringUtil.join(sa, ""));
		assertEquals(
				"testJoin: join with null-separator failed",
				"abcdef", StringUtil.join(sa, null));
		assertEquals(
				"testJoin: join with lrg string failed",
				"ab---cd---ef", StringUtil.join(sa, "---"));
		assertEquals(
				"testJoin: join with null array failed",
				"", StringUtil.join(null, "-"));
	}

	/**
	 * 
	 */
	public void testFill() {
		Object [][] testCases = {
				{Character.valueOf('='), Integer.valueOf(10), "=========="},
				{Character.valueOf('='), Integer.valueOf(0), ""},
				{Character.valueOf('='), Integer.valueOf(1), "="},
				{Character.valueOf('a'), Integer.valueOf(1), "a"},
		};

		for (int i = 0; i < testCases.length; i++) {
			Object [] oa = testCases[i];
			String exp = (String)oa[2];
			char ch = ((Character)oa[0]).charValue();
			int len = ((Integer)oa[1]).intValue();

			String res = StringUtil.fill(ch, len);
			
			assertEquals(
					"testFill: test case failed @ " + i,
					exp, res);
		}
	}
}
