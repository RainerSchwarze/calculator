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
public class VersionUtilTest extends TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(VersionUtilTest.class);
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
	public void testGetVersionNameFromFileName() {
		String [][] testCases = {
				{"1.2.3", "", "1.2.3", ""},
				{"1.2.3-r4", "", "1.2.3-r4", ""},
				{"filename-1.2.3-r4", "filename-", "1.2.3-r4", ""},
				{"filename-1.2.3-r4.jar", "filename-", "1.2.3-r4", ".jar"},
				{"filename-1.2.3-r4.rar", "filename-", "1.2.3-r4", ".rar"},
				{"filename-1.2.3.jar", "filename-", "1.2.3", ".jar"},
				{"filename-1.2.3.rar", "filename-", "1.2.3", ".rar"},
				{"file-name-1.2.3.rar", "file-name-", "1.2.3", ".rar"},
				{"file17-name42-1.2.3.rar", "file17-name42-", "1.2.3", ".rar"},
				{"file17-name42-1.2.3.tar.gz", "file17-name42-", "1.2.3", ".tar.gz"},
				{"file17-name42-1.2.3-r4.tar.gz", "file17-name42-", "1.2.3-r4", ".tar.gz"},
				{"file17-name42-10.20.30-r40.tar.gz", "file17-name42-", "10.20.30-r40", ".tar.gz"},
		};
		for (int i = 0; i < testCases.length; i++) {
			VersionName vn;
			VersionRecord vr;
			String [] testCase = testCases[i];
			String fn = testCase[0];
			String fp = testCase[1];
			String vs = testCase[2];
			String fs = testCase[3];
			vn = VersionUtil.getVersionNameFromFileName(fn);
			assertNotNull(
					"testGetVersionNameFromFileName: test case x # " + i + " failed (vn).",
					vn);
			vr = vn.getVersionRecord();
			assertNotNull(
					"testGetVersionNameFromFileName: test case x # " + i + " failed (vr).",
					vr);
			assertEquals(
					"testGetVersionNameFromFileName: test case x # " + i + " failed.",
					fp, vn.getPrefix());
			assertEquals(
					"testGetVersionNameFromFileName: test case x # " + i + " failed.",
					vs, vr.getMjMnMcRvVersionString());
			assertEquals(
					"testGetVersionNameFromFileName: test case x # " + i + " failed.",
					fs, vn.getSuffix());
			assertEquals(
					"testGetVersionNameFromFileName: test case x # " + i + " failed.",
					fn, vn.getCombinedName());
		}

	}

	/**
	 * 
	 */
	public void testRemoveOldVersions() {
		String [] testList = new String[]{
				"test-123.jar",
				"test-134.jar",
				"test-1.2.3.jar",
				"test2-2.0.1.jar",
				"test2-2.1.0.jar",
				"test2-2.1.0-r10.jar",
				"./lib/test3-1.0.0.jar",
				"./mod/test3-1.1.0.jar",
				"./laf/test3-1.0.5.jar",
		};
		ClassPathExtender cpe1 = new ClassPathExtender();
		cpe1.setURLs(testList);
		cpe1.checkFileVersions();
		for (int i = 0; i < testList.length; i++) {
			String s = testList[i];
			File f = cpe1.files.get(i);
			System.out.println(
					"src: " + s + " dst: " + ((f!=null) ? f.getPath() : "<>"));
		}
	}
}
