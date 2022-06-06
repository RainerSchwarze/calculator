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
package de.admadic.units.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class UnitsIoTest extends TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(UnitsIoTest.class);
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
	 * Class under test for void readFile(File)
	 */
	public void testReadFileFile() {
		UnitManager um = new UnitManager();
		UnitsIo uio = new UnitsIo(um);
		uio.readFile(new File("./src/de/admadic/units/unitstdset-save.xml"));
	}

	/**
	 * Class under test for void readFile(URL)
	 */
	public void testReadFileURL() {
		UnitManager um = new UnitManager();
		UnitsIo uio = new UnitsIo(um);
		try {
			URLClassLoader cl = new URLClassLoader(new URL[] {
					new File("./test/de/admadic/units/core/unitstdset.jar").toURL()
			}, null); // parent shall be null, otherwise the xml file will be taken
			// from plain fs.
			URL r = cl.getResource("de/admadic/units/unitstdset-save.xml");
			System.out.println("res = " + r);
			uio.readFile(r);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("URL error");
		}
		Domain d = um.getDomain("@len");
		assertNotNull(d);
	}

}
