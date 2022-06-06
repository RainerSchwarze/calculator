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

import java.awt.Point;
import java.awt.Rectangle;

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class CfgTest extends TestCase {
	Cfg cfg;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(CfgTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		cfg = new Cfg();

		// we need a standard set of entries:
		cfg.putStringValue("a.string", "string value");
		cfg.putBooleanValue("a.boolean", true);
		cfg.putIntValue("a.int", 17);
		cfg.putPointValue("a.point", new Point(17, 42));
		cfg.putRectangleValue("a.rectangle", new Rectangle(17, 18, 42, 43));
		cfg.putValueArray("a.array.",
				new Object[]{
					"entry0",
					"entry1",
					"entry2"}
			);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testCfg() {
		// nothing yet
	}

	/**
	 * 
	 */
	public void testSetDefaults() {
		// FIXME: implement
	}

	/**
	 * 
	 */
	public void testRegisterObject() {
		// FIXME: implement
	}

	/**
	 * 
	 */
	public void testUnregisterObject() {
		// FIXME: implement
	}

	/**
	 * 
	 */
	public void testPutValue() {
		// FIXME: implement
	}

	/**
	 * 
	 */
	public void testGetValue() {
		// FIXME: implement
	}

	/**
	 * 
	 */
	public void testGetStringValue() {
		String keyname = "tmp.string";
		cfg.putStringValue(keyname, "string value");
		assertEquals(
				"testGetStringValue: failed",
				"string value", cfg.getStringValue(keyname, "default"));
		cfg.removeValue(keyname);
	}

	/**
	 * 
	 */
	public void testGetBooleanValue() {
		String keyname = "tmp.boolean";
		cfg.putBooleanValue(keyname, true);
		assertEquals(
				"testGetBooleanValue: failed",
				true, cfg.getBooleanValue(keyname, false));
		cfg.removeValue(keyname);
	}

	/**
	 * 
	 */
	public void testGetIntValue() {
		String keyname = "tmp.int";
		cfg.putIntValue(keyname, 17);
		assertEquals(
				"testGetIntValue: failed",
				17, cfg.getIntValue(keyname, 42));
		cfg.removeValue(keyname);
	}

	/**
	 * 
	 */
	public void testGetRectangleValue() {
		String keyname = "tmp.rectangle";
		Rectangle r = new Rectangle(17, 18, 42, 43);
		cfg.putRectangleValue(keyname, r);
		assertEquals(
				"testGetRectangleValue: failed",
				r, cfg.getRectangleValue(keyname, new Rectangle(1, 2, 3, 4)));
		cfg.removeValue(keyname);
	}

	/**
	 * 
	 */
	public void testGetPointValue() {
		String keyname = "tmp.point";
		Point p = new Point(17, 42);
		cfg.putPointValue(keyname, p);
		assertEquals(
				"testGetPointValue: failed",
				p, cfg.getPointValue(keyname, new Point(1, 2)));
		cfg.removeValue(keyname);
	}

	/**
	 * 
	 */
	public void testPutStringValue() {
		// already tested with Get
	}

	/**
	 * 
	 */
	public void testPutBooleanValue() {
		// already tested with Get
	}

	/**
	 * 
	 */
	public void testPutIntValue() {
		// already tested with Get
	}

	/**
	 * 
	 */
	public void testPutRectangleValue() {
		// already tested with Get
	}

	/**
	 * 
	 */
	public void testPutPointValue() {
		// already tested with Get
	}

	/**
	 * 
	 */
	public void testGetValueArray() {
		String [] va = {
				"entry0",
				"entry1",
				"entry2",
				"entry3"				
		};
		String keyname = "tmp.array.";
		cfg.putValueArray(keyname, va);
		Object [] ra;
		ra = cfg.getValueArray(keyname);
		assertNotNull(
				"testGetValueArray: returned null instead of value array",
				ra);
		assertEquals(
				"testGetValueArray: array size invalid.",
				va.length, ra.length);
		for (int i = 0; i < ra.length; i++) {
			String v = (String)ra[i];
			assertEquals(
					"testGetValueArray: element mismatch @" + i,
					va[i], v);
		}
		// remove it
		for (int i = 0; i < ra.length; i++) {
			cfg.removeValue(keyname + i);
		}
	}

	/**
	 * 
	 */
	public void testRemoveValueArray() {
		String [] va = {
				"entry0",
				"entry1",
				"entry2",
				"entry3"				
		};
		String keyname = "tmp.array.";
		cfg.putValueArray(keyname, va);
		cfg.removeValueArray(keyname);
		for (int i = 0; i < va.length; i++) {
			String res = cfg.getStringValue(keyname + i, null);
			assertNull(
					"testRemoveValueArray: value is still present @ " + i,
					res);
		}
	}

	/**
	 * 
	 */
	public void testPutValueArray() {
		// already tested with Get
	}

	/**
	 * 
	 */
	public void testLoadObjectFromPreferences() {
		// not yet done
	}

	/**
	 * 
	 */
	public void testStoreObjectToPreferences() {
		// not yet done
	}

	/**
	 * 
	 */
	public void testLoadObjectsFromPreferences() {
		// not yet done
	}

	/**
	 * 
	 */
	public void testStoreObjectsToPreferences() {
		// not yet done
	}

	/**
	 * 
	 */
	public void testLoadPreferences() {
		// not yet done
	}

	/**
	 * 
	 */
	public void testStorePreferences() {
		// not yet done
	}

}
