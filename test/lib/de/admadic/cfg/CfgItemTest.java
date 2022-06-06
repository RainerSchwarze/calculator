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
 * 
 * @author Rainer Schwarze
 *
 */
public class CfgItemTest extends TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(CfgItemTest.class);
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
	 * Class under test for void CfgItem(String, String, CfgItem.Type, int)
	 */
	public void testCfgItemStringObjectint() {
		CfgItem ci;
		try {
			ci = new CfgItem("a key", "a value", CfgItem.FLG_NONE);
			assertEquals(
					"testCfgItemStringObjectint: key storing failed",
					"a key", ci.getCiKey());
			assertEquals(
					"testCfgItemStringObjectint: value storing failed",
					"a value", ci.getCiValue());
			assertEquals(
					"testCfgItemStringObjectint: type storing failed",
					CfgItem.Type.STRING, ci.getCiType());
			assertEquals(
					"testCfgItemStringObjectint: flags storing failed",
					CfgItem.FLG_NONE, ci.getCiFlags());
		} catch (CfgException e) {
			fail("testCfgItemStringObjectint: failed to create instance.");
		}
	}

	/**
	 * 
	 */
	public void testEncodeKey() {
		CfgItem ci;
		String enc;
		try {
			ci = new CfgItem("a key", "a value", CfgItem.FLG_NONE);
			enc = ci.encodeKey();
			assertEquals(
					"testEncodeKey: set 1 failed.",
					"a key", enc);

			ci = new CfgItem("a key", new Integer(1), CfgItem.FLG_NONE);
			enc = ci.encodeKey();
			assertEquals(
					"testEncodeKey: set 2 failed.",
					"a key", enc);

			ci = new CfgItem("a key", "a value", CfgItem.FLG_NORESET);
			enc = ci.encodeKey();
			assertEquals(
					"testEncodeKey: set 3 failed.",
					"a key", enc);
		} catch (CfgException e) {
			e.printStackTrace();
			fail("testEncodeKey: exception caught.");
		}
	}

	/**
	 * 
	 */
	public void testEncodeMeta() {
		CfgItem ci;
		String enc;
		try {
			ci = new CfgItem("a key", "a value", CfgItem.FLG_NONE);
			enc = ci.encodeMeta();
			assertEquals(
					"testEncodeMeta: set 1 failed.",
					"[s,n]", enc);

			ci = new CfgItem("a key", new Integer(1), CfgItem.FLG_NONE);
			enc = ci.encodeMeta();
			assertEquals(
					"testEncodeMeta: set 2 failed.",
					"[i,n]", enc);

			ci = new CfgItem("a key", "a value", CfgItem.FLG_NORESET);
			enc = ci.encodeMeta();
			assertEquals(
					"testEncodeMeta: set 3 failed.",
					"[s,r]", enc);

			ci = new CfgItem("a key", new Helper(17, true, "test"), CfgItem.FLG_NORESET);
			enc = ci.encodeMeta();
			assertEquals(
					"testEncodeMeta: set 4 failed.",
					"[o:"+this.getClass().getName()+"$Helper"+",r]", enc);
		} catch (CfgException e) {
			e.printStackTrace();
			fail("testEncodeKey: exception caught.");
		}
	}

	/**
	 * 
	 */
	public void testDecodeMeta() {
		CfgItem ci;
		String enc;
		try {
			ci = new CfgItem("key", "");
			enc = "[s,n]";
			ci.decodeMeta(enc);
			assertEquals(
					"testDecodeMeta: enc=" + enc + " failed with type",
					CfgItem.Type.STRING, ci.getCiType());
			assertEquals(
					"testDecodeMeta: enc=" + enc + " failed with flags",
					CfgItem.FLG_NONE, ci.getCiFlags());

			ci = new CfgItem("key", "");
			enc = "[i,r]";
			ci.decodeMeta(enc);
			assertEquals(
					"testDecodeMeta: enc=" + enc + " failed with type",
					CfgItem.Type.INT, ci.getCiType());
			assertEquals(
					"testDecodeMeta: enc=" + enc + " failed with flags",
					CfgItem.FLG_NORESET, ci.getCiFlags());
		} catch (CfgException e) {
			e.printStackTrace();
			fail("testDecodeKey: exception caught.");
		}
	}

	/**
	 * 
	 */
	public void testPutObjectValue() {
		// done by ...Boolean
	}

	/**
	 * 
	 */
	public void testPutObjectValueBoolean() {
		CfgItem ci = null;
		try {
			ci = new CfgItem("a key", "a value");
		} catch (CfgException e1) {
			fail("testPutObjectValueBoolean(): could not create instance");
		}

		try {
			ci.putObjectValue(new Integer(1), false);
			fail("testPutObjectValueBoolean(false): Integer over String should fail but didn't.");
		} catch (CfgException e) { /* */ }

		try {
			ci.putObjectValue(new Boolean(true), false);
			fail("testPutObjectValueBoolean(false): Boolean over String should fail but didn't.");
		} catch (CfgException e) { /* */ }

		try {
			ci.putObjectValue(new String("hi ho"), false);
		} catch (CfgException e) { 
			fail("testPutObjectValueBoolean(false): String over String should not fail but did.");
		}

		try {
			ci.putObjectValue(new Integer(1), true);
		} catch (CfgException e) {
			fail("testPutObjectValueBoolean(true): Integer over String should not fail but did.");
		}

		try {
			ci.putObjectValue(new Boolean(true), true);
		} catch (CfgException e) {
			fail("testPutObjectValueBoolean(true): Boolean over Integer should not fail but did.");
		}

		try {
			ci.putObjectValue(new String("hi ho"), true);
		} catch (CfgException e) { 
			fail("testPutObjectValueBoolean(true): String over Boolean should not fail but did.");
		}
	}

	/**
	 * 
	 */
	public void testGetObjectValue() {
		int count = 6;
		CfgItem [] ci = new CfgItem[count];
		try {
			ci[0] = new CfgItem("a key string", new String("a value"));
			ci[1] = new CfgItem("a key int", new Integer(17));
			ci[2] = new CfgItem("a key boolean", new Boolean(true));
			ci[3] = new CfgItem("a key rectangle", new Rectangle(1,2,3,4));
			ci[4] = new CfgItem("a key point", new Point(1,2));
			ci[5] = new CfgItem("a key object", new Helper(17, true, "test"));
		} catch (CfgException e) {
			fail("testGetObjectValue: failed to create instances");
		}

		assertEquals(
				"testGetObjectValue: string failed",
				"a value", ci[0].getObjectValue(null));
		assertEquals(
				"testGetObjectValue: int failed",
				Integer.valueOf(17), ci[1].getObjectValue(null));
		assertEquals(
				"testGetObjectValue: boolean failed",
				Boolean.valueOf(true), ci[2].getObjectValue(null));
		assertEquals(
				"testGetObjectValue: rectangle failed",
				new Rectangle(1, 2, 3, 4), ci[3].getObjectValue(null));
		assertEquals(
				"testGetObjectValue: point failed",
				new Point(1, 2), ci[4].getObjectValue(null));
		assertEquals(
				"testGetObjectValue: object failed",
				new Helper(17, true, "test"), ci[5].getObjectValue(null));
	}

	/**
	 * 
	 */
	public void testCreate() {
		Object [] oa = {
				new String("a string"),
				new Integer(17),
				new Boolean(true),
				new Rectangle(1, 2, 3, 4),
				new Point(1, 2),
				new Helper(17, true, "test")
		};
		CfgItem [] cia = new CfgItem[oa.length];
		for (int i = 0; i < cia.length; i++) {
			cia[i] = CfgItem.create("key" + i, oa[i]);
		}

		for (int i = 0; i < cia.length; i++) {
			assertNotNull(
					"testCreate: CfgItem not created @ " + i,
					cia[i]);
			assertEquals(
					"testCreate: key failed @ " + i,
					"key" + i, cia[i].getCiKey());
			assertEquals(
					"testCreate: value test failed @ " + i,
					oa[i], cia[i].getObjectValue(null));
		}
		assertEquals(
				"testCreate: type failed for string",
				CfgItem.Type.STRING, cia[0].getCiType());
		assertEquals(
				"testCreate: type failed for int",
				CfgItem.Type.INT, cia[1].getCiType());
		assertEquals(
				"testCreate: type failed for boolean",
				CfgItem.Type.BOOLEAN, cia[2].getCiType());
		assertEquals(
				"testCreate: type failed for rectangle",
				CfgItem.Type.RECTANGLE, cia[3].getCiType());
		assertEquals(
				"testCreate: type failed for point",
				CfgItem.Type.POINT, cia[4].getCiType());
		assertEquals(
				"testCreate: type failed for object",
				CfgItem.Type.OBJECT, cia[5].getCiType());
	}

	/**
	 * helper class for get/putObjectValue
	 */
	protected static class Helper {
		int i;
		boolean b;
		String s;
		/**
		 * @param i
		 * @param b
		 * @param s
		 */
		public Helper(int i, boolean b, String s) {
			super();
			this.i = i;
			this.b = b;
			this.s = s;
		}

		/**
		 * @param s
		 * @return	Returns a Helper object from the String
		 */
		public static Helper valueOf(String s) {
			Helper h = null;
			String [] sa = s.split(",");
			int i1;
			boolean b1;
			if (sa.length!=3) return null;
			try {
				i1 = Integer.parseInt(sa[0]);
				b1 = Boolean.parseBoolean(sa[1]);
			} catch (Exception e) {
				return null;
			}
			String s1 = sa[2];
			h = new Helper(i1, b1, s1);
			return h;
		}

		/**
		 * @return	Returns the String encoding of the Helper object
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			String sv = "";
			sv += i;
			sv += "," + b;
			sv += "," + s;
			return sv;
		}

		/**
		 * @param obj
		 * @return	Returns whether the objects are equal
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj==null) return false;
			if (!this.getClass().equals(obj.getClass())) return false;
			Helper dst = (Helper)obj;
			if (dst.i!=this.i) return false;
			if (dst.b!=this.b) return false;
			if (!dst.s.equals(this.s)) return false;
			return true;
		}

		
	}
}
