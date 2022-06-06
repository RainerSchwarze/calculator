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
public class GetOptTest extends TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(GetOptTest.class);
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
	public void testGetOpt() {
		String [] args = 
			"-aaa -b true -f theFile -w -80 -h3.33 arg1 arg2".split(" ");
		GetOpt go = new GetOpt(args, "Uab:f:h:w:");
		go.optErr = true;
		int ch = -1;
		// process options in command line arguments
		boolean usagePrint = false;                 // set
		int aflg = 0;                               // default
		boolean bflg = false;                       // values
		String filename = "out";                    // of
		int width = 80;                             // options
		double height = 1;                          // here

		while ((ch = go.getopt()) != GetOpt.optEOF) {
			if      ((char)ch == 'U') usagePrint = true;
			else if ((char)ch == 'a') aflg++;
			else if ((char)ch == 'b')
				bflg = go.processArg(go.optArgGet(), bflg);
			else if ((char)ch == 'f') filename = go.optArgGet();
			else if ((char)ch == 'h')
				height = go.processArg(go.optArgGet(), height);
			else if ((char)ch == 'w')
				width = go.processArg(go.optArgGet(), width);
			else System.exit(1);                     // undefined option
		}                                           // getopt() returns '?'
		if (usagePrint) {
			System.out.println("Usage: -a -b bool -f file -h height -w width");
			System.exit(0);
		}
		System.out.println("These are all the command line arguments " +
		"before processing with GetOpt:");
		for (int i=0; i<args.length; i++) System.out.print(" " + args[i]);
		System.out.println();
		System.out.println("-U " + usagePrint);
		System.out.println("-a " + aflg);
		System.out.println("-b " + bflg);
		System.out.println("-f " + filename);
		System.out.println("-h " + height);
		System.out.println("-w " + width);
		// process non-option command line arguments
		for (int k = go.optIndexGet(); k < args.length; k++) {
			System.out.println("normal argument " + k + " is " + args[k]);
		}
		assertEquals("testGetOpt: -U failed:", false, usagePrint);
		assertEquals("testGetOpt: -a failed:", 3, aflg);
		assertEquals("testGetOpt: -b failed:", true, bflg);
		assertEquals("testGetOpt: -f failed:", "theFile", filename);
		assertEquals("testGetOpt: -h failed:", 
				Double.valueOf(3.33), Double.valueOf(height));
		assertEquals("testGetOpt: -w failed:", -80, width);
		assertEquals("testGetOpt: args[8]:", "arg1", args[8]);
		assertEquals("testGetOpt: args[9]:", "arg2", args[9]);
	}
}
