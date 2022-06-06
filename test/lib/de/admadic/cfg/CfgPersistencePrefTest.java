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
package de.admadic.cfg;

import java.util.Enumeration;

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class CfgPersistencePrefTest extends TestCase {
	CfgPersistencePref cpp;
	Cfg testCfg;
	String persPath;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(CfgPersistencePrefTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		testCfg = new Cfg();
		cpp = new CfgPersistencePref(testCfg);
		persPath = "/test.de.admadic.cfg";
		testCfg.putStringValue("a string", "string value");
		testCfg.putIntValue("a int", 17);
		testCfg.putBooleanValue("a boolean", true);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testGroupInterface() {
		cpp.store(persPath);

		{
			Cfg readCfg = new Cfg();
			CfgPersistencePref readCpp = new CfgPersistencePref(readCfg);
			readCpp.load(persPath);
			assertEquals(
					"testGroupInterface: a string not loaded",
					"string value", readCfg.getStringValue("a string", null));
			assertEquals(
					"testGroupInterface: a int not loaded",
					17, readCfg.getIntValue("a int", 0));
			assertEquals(
					"testGroupInterface: a boolean not loaded",
					true, readCfg.getBooleanValue("a boolean", false));
	
			readCpp.removeKeys(persPath, readCfg.getCfgItemKeys());
			Enumeration<String> keys = readCfg.getCfgItemKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				readCfg.removeCfgItem(key);
			}
			readCpp.store(persPath);
		}

		{
			Cfg removeCfg = new Cfg();
			CfgPersistencePref removeCpp = new CfgPersistencePref(removeCfg);
			removeCpp.load(persPath);
	
			assertEquals(
					"testGroupInterface: a string was loaded which should be removed",
					"dummy", removeCfg.getStringValue("a string", "dummy"));
			assertEquals(
					"testGroupInterface: a int was loaded which should be removed",
					42, removeCfg.getIntValue("a int", 42));
			assertEquals(
					"testGroupInterface: a boolean was loaded which should be removed",
					false, removeCfg.getBooleanValue("a boolean", false));
		}
	}

	/**
	 * 
	 */
	public void testRemoveKeys() {
		// nothing yet
	}

	/**
	 * 
	 */
	public void testLoad() {
		// nothing yet
	}

	/**
	 * 
	 */
	public void testStore() {
		// nothing yet
	}

	/**
	 * 
	 */
	public void testPrepareLoad() {
		// nothing yet
	}

	/**
	 * 
	 */
	public void testPrepareStore() {
		// nothing yet
	}

	/**
	 * 
	 */
	public void testGetKeys() {
		// nothing yet
	}

	/**
	 * 
	 */
	public void testLoadCfgItem() {
		// nothing yet
	}

	/**
	 * 
	 */
	public void testStoreCfgItem() {
		// nothing yet
	}

	/**
	 * 
	 */
	public void testFinalizeLoad() {
		// nothing yet
	}

	/**
	 * 
	 */
	public void testFinalizeStore() {
		// nothing yet
	}

}
