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

import java.io.File;

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class PathManagerTest extends TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(PathManagerTest.class);
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
	public void testExpandFilename() {
/*
	 * %h = users home directory (user.home)
	 * %w = users working directory (user.dir)
	 * %t = default temp dir (java.io.tmpdir)
	 * %j = java installation directory (java.home)
	 * %% = percent sign
 */
		String [][] testCases = {
				{"j", "%j", "ITEM"},
				{"j", "test/%j", "ITEM"},
				{"j", "test%j", "ITEM"},
				{"h", "%h", "ITEM"},
				{"h", "test/%h", "ITEM"},
				{"h", "test%h", "ITEM"},
				{"h", "test%h/test", "ITEM/test"},
				{"w", "%w", "ITEM"},
				{"t", "%t", "ITEM"},
				{"t", "%t/test", "ITEMtest"},
				{"t", "%t/test/%%", "ITEMtest/%"},
		};
		for (String[] strings : testCases) {
			String pathpart = "";
			if (strings[0].equals("j")) {
				pathpart = System.getProperty("java.home");
			} else if (strings[0].equals("h")) {
				pathpart = System.getProperty("user.home");
			} else if (strings[0].equals("w")) {
				pathpart = System.getProperty("user.dir");
			} else if (strings[0].equals("t")) {
				pathpart = System.getProperty("java.io.tmpdir");
			} else {
				fail("invalid test code");
			}

			int ip = strings[2].indexOf("ITEM");
			strings[2] = 
				strings[2].substring(ip, ip) +
				pathpart + 
				strings[2].substring(ip+4);
			System.out.println("test case: " + strings[1] + " > " + strings[2]);
		}

		for (String[] strings : testCases) {
			File fe, fa;
			fa = PathManager.expandFilename(strings[1]);
			fe = new File(strings[2]);
			assertEquals(
					"test case " + strings[1] + " failed.",
					fe.toString(), fa.toString());
		}
	}

	/**
	 * 
	 */
	public void testInit() {
		PathManager pm = new PathManager();
		pm.init("admadic", "generictest", "0.1", this.getClass());
		pm.dump();
	}

}
