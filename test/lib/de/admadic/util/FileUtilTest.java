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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import junit.framework.TestCase;

/**
 * 
 * @author Rainer Schwarze
 *
 */
public class FileUtilTest extends TestCase {
	Object [][] testCases;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(FileUtilTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// array of cases which are "match", {{test, bool}, {test,bool}, ...}
		testCases = new Object[][] {
				new Object[]{ "*",
						new Object[][] {
							{"test.txt", 	Boolean.TRUE},
							{"a.jar", 		Boolean.TRUE},
							{"abc", 		Boolean.TRUE},
							{"test.testtest", 	Boolean.TRUE},
						}
				},
				new Object[]{ "*.*",
						new Object[][] {
							{"test.txt", 	Boolean.TRUE},
							{"a.jar", 		Boolean.TRUE},
							{"abc",		 	Boolean.FALSE},
							{"test.testtest", 	Boolean.TRUE},
							{"test.test.test", 	Boolean.TRUE},
						}
				},
				new Object[]{ "*.txt",
						new Object[][] {
							{"test.txt", 	Boolean.TRUE},
							{"a.jar", 		Boolean.FALSE},
							{"abc",		 	Boolean.FALSE},
							{"test.txttest", 	Boolean.FALSE},
							{"test.testtxt", 	Boolean.FALSE},
							{"test.test.txt", 	Boolean.TRUE},
						}
				},
				new Object[]{ "a?c.txt",
						new Object[][] {
							{"test.txt", 	Boolean.FALSE},
							{"a.jar", 		Boolean.FALSE},
							{"abc",		 	Boolean.FALSE},
							{"abc.txt",	 	Boolean.TRUE},
							{"a.c.txt",	 	Boolean.TRUE},
							{"abbc.txt",	Boolean.FALSE},
							{"abc.test",	Boolean.FALSE},
						}
				},
				new Object[]{ "a?c.*r",
						new Object[][] {
							{"test.txt", 	Boolean.FALSE},
							{"a.jar", 		Boolean.FALSE},
							{"abc",		 	Boolean.FALSE},
							{"abc.txt",	 	Boolean.FALSE},
							{"abbc.jar",	Boolean.FALSE},
							{"abc.jar",		Boolean.TRUE},
							{"abc.testjar",	Boolean.TRUE},
							{"abc.test",	Boolean.FALSE},
							{"abc.test.jar",	Boolean.TRUE},
							{"abc.test.r",	Boolean.TRUE},
						}
				},
		};
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testFileUtil() {
		// nothing to test right now for ctor
	}

	/**
	 * 
	 */
	public void testConvertWildcardToRegexp() {
		for (int i = 0; i < testCases.length; i++) {
			String wildcard = (String)testCases[i][0];
			Object [][] tests = (Object[][])testCases[i][1];

			String regexp = FileUtil.convertWildcardToRegex(wildcard);
			//System.out.println("convert: " + wildcard + " -> " + regexp);

			for (int j = 0; j < tests.length; j++) {
				String f = (String)tests[j][0];
				Boolean b = (Boolean)tests[j][1];
				assertEquals(
						"testConvertWildcardToRegexp: test case failed: " +
						wildcard + " -> " + regexp + " f = " + f + 
						"(should match: " + b + ")",
						b.booleanValue(), f.matches(regexp));
			}
		}
	}

	/**
	 * 
	 */
	public void testFilterFilesByWildcard() {
		File [] in;
		File [] exp;
		File [] out;
		int count;
		int dragindex;
		for (int i = 0; i < testCases.length; i++) {
			String wildcard = (String)testCases[i][0];
			Object [][] tests = (Object[][])testCases[i][1];

			in = new File[tests.length];
			count = 0;
			for (int j = 0; j < tests.length; j++) {
				String f = (String)tests[j][0];
				Boolean b = (Boolean)tests[j][1];
				in[j] = new File(f);
				if (b.booleanValue()) count++;
			}

			exp = new File[count];
			dragindex = 0;
			for (int j = 0; j < tests.length; j++) {
				String f = (String)tests[j][0];
				Boolean b = (Boolean)tests[j][1];
				if (b.booleanValue()) {
					exp[dragindex++] = new File(f);
				}
			}

			out = FileUtil.filterFilesByWildcard(in, wildcard, false);

			assertEquals(
					"testFilterFilesByWildcard: output length does not match",
					exp.length, out.length);

			for (int j = 0; j < exp.length; j++) {
				assertEquals(
						"testFilterFilesByWildcard: item @ " + j + " does not match",
						exp[j], out[j]);
			}
		}
	}

	/**
	 * 
	 */
	public void testCopyFile() {
		// nothing yet
	}

	/**
	 * 
	 */
	public void testFixFileName1() {
		String res;
		String exp;

		exp = "/D:\\data";
		res = FileUtil.fixFileName1("D:\\data");
		assertEquals(
				"testFixFileName1: test set 1 failed.",
				exp, res);
	}

	
	protected void testGenericFileOps_Case(String dir) {
		File fdir = new File(dir);
		System.out.println("dir = " + dir);
		testGenericFileOps_Case(fdir);
	}

	protected void testGenericFileOps_Case(File fdir) {
		System.out.println("fdir = " + fdir);
		File [] fl = fdir.listFiles();
		if (fl==null) {
			System.out.println("no files!");
		} else {
			System.out.println("file count: " + fl.length);
		}
	}

	/**
	 * 
	 */
	public void testGenericFileOps() {
		testGenericFileOps_Case("D:\\Program Files");
		testGenericFileOps_Case("D:\\Program%20Files");
		URL u = null;
		URI ui = null;
		File f = new File("D:\\Program Files");
		try {
			u = f.toURL();
			ui = f.toURI();
		} catch (MalformedURLException e) {
			e.printStackTrace(System.out);
		}
		System.out.println(u.toString());
		System.out.println(ui.toString());
		f = new File(ui);
		System.out.println(f.toString());

		System.out.println("user.home = " + System.getProperty("user.home"));

		testGenericFileOps_Case(
				new File("D:\\Program Files"));
		testGenericFileOps_Case(
				new File(new File("D:\\Program Files"), "admadic"));
		testGenericFileOps_Case(
				new File(new File("D:\\Program Files"), "./admadic"));
		testGenericFileOps_Case(
				new File(new File("D:\\Program Files"), "./admadic/."));

		try {
			System.out.println("-- section 1");
			f = new File("D:\\Program Files");
			u = f.toURL();
			System.out.println("url: st " + u.toString());
			System.out.println("url: ef " + u.toExternalForm());
			ui = f.toURI();
			System.out.println("uri: st " + ui.toString());
			System.out.println("uri: gp " + ui.getPath());
			u = ui.toURL();
			System.out.println("url: st " + u.toString());

			System.out.println("-- section 2");
			f = new File(u.getPath());
			System.out.println("fil: st " + f.toString());
			System.out.println("fil: cp " + f.getCanonicalPath());
			testGenericFileOps_Case(f);

			System.out.println("-- section 3");
			f = new File(f.getCanonicalPath());
			u = f.toURL();
			System.out.println("url: st " + u.toString());
			System.out.println("url: gp " + u.getPath());
			ui = new URI(u.toString());
			System.out.println("uri: st " + ui.toString());

			System.out.println("-- section 4");
			f = new File(ui.getPath());
			System.out.println("fil: st " + f.toString());
			testGenericFileOps_Case(f);
			u = f.toURI().toURL();
			System.out.println("url: st " + u.toString());
			ui = new URI(null, u.getHost(), u.getPath(), null);
			System.out.println("uri: st " + ui.toString());
			f = new File(ui.getPath());
			System.out.println("fil: st " + f.toString());
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
