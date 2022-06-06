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
public class CharCodeOpTest extends TestCase {

	CharCodeOp op26;
	CharCodeOp op10;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(CharCodeOpTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		op26 = new CharCodeOp("abcdefghijklmnopqrstuvwxyz");	//26
		op10 = new CharCodeOp("abcdefghij"); //10
	}

	@Override
	protected void tearDown() throws Exception {
		op26 = null;
		op10 = null;
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testString2Num() {
		try {
			assertEquals("testString2Num: 0 failed",
					0, op10.string2Num("a"));
			assertEquals("testString2Num: 00 failed",
					0, op10.string2Num("aa"));
			assertEquals("testString2Num: j/10 failed",
					9, op10.string2Num("j"));
			assertEquals("testString2Num: jj/10 failed",
					99, op10.string2Num("jj"));
			assertEquals("testString2Num: bcdefghija/10 failed",
					1234567890, op10.string2Num("bcdefghija"));

			assertEquals("testString2Num: ba/26 failed",
					26, op26.string2Num("ba"));
			assertEquals("testString2Num: baa/26 failed",
					26*26, op26.string2Num("baa"));
			assertEquals("testString2Num: bab/26 failed",
					26*26+1, op26.string2Num("bab"));
			assertEquals("testString2Num: zzz/26 failed",
					(25*26 + 25)*26 + 25, op26.string2Num("zzz"));
		} catch (CharCodeOpException e) {
			e.printStackTrace();
			fail("testString2Num: exception: " + e.getMessage());
		}
	}

	/**
	 * 
	 */
	public void testNum2String() {
		assertEquals("testNum2String: 0 failed",
				"a", op10.num2String(0));
		assertEquals("testNum2String: j/10 failed",
				"j", op10.num2String(9));
		assertEquals("testNum2String: jj/10 failed",
				"jj", op10.num2String(99));
		assertEquals("testNum2String: bcdefghija/10 failed",
				"bcdefghija", op10.num2String(1234567890));

		assertEquals("testNum2String: ba/26 failed",
				"ba", op26.num2String(26));
		assertEquals("testNum2String: baa/26 failed",
				"baa", op26.num2String(26*26));
		assertEquals("testNum2String: bab/26 failed",
				"bab", op26.num2String(26*26+1));
		assertEquals("testNum2String: zzz/26 failed",
				"zzz", op26.num2String((25*26 + 25)*26 + 25));

		assertEquals("testNum2String: 0xffffffff -> nxmrlxv/26 failed",
				"nxmrlxv", op26.num2String(0xffffffff));
	}

	/**
	 * 
	 */
	public void testBytes2String() {
		byte [] data;

		data = new byte[]{(byte)0x00, (byte)0x00, (byte)0x00};
		assertEquals("testBytes2String: 0 -> aaaaa/10 failed",
				"aaaaa", op10.bytes2String(data, 3, 5));

		data = new byte[]{(byte)0x01, (byte)0x00, (byte)0x00};
		assertEquals("testBytes2String: 1 -> aaaab/10 failed",
				"aaaab", op10.bytes2String(data, 3, 5));

		data = new byte[]{(byte)0xff, (byte)0xff, (byte)0xff};
		// ffffff -> bksojn
		assertEquals("testBytes2String: 0xffffff -> abksojn/26 failed",
				"abksojn", op26.bytes2String(data, 3, 7));

		data = new byte[]{(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff};
		// ffffffff -> anxmrlxv
		assertEquals("testBytes2String: 0xffffffff -> anxmrlxv/26 failed",
				"anxmrlxv", op26.bytes2String(data, 4, 8));
	}

	/**
	 * 
	 */
	public void testString2Bytes() {
		byte [] dataexp;
		byte [] data;

		try {
			dataexp = new byte[]{(byte)0x00, (byte)0x00, (byte)0x00};
			data = op10.string2Bytes("a", 3);
			for (int i = 0; i < data.length; i++) {
				assertEquals(
						"testString2Bytes: a -> 0 failed @ " + i,
						dataexp[i], data[i]);
			}

			dataexp = new byte[]{(byte)0x01, (byte)0x00, (byte)0x00};
			data = op10.string2Bytes("b", 3);
			for (int i = 0; i < data.length; i++) {
				assertEquals(
						"testString2Bytes: b -> 1 failed @ " + i,
						dataexp[i], data[i]);
			}

			dataexp = new byte[]{(byte)0xff, (byte)0xff, (byte)0xff};
			data = op26.string2Bytes("abksojn", 3);
			for (int i = 0; i < data.length; i++) {
				assertEquals(
						"testString2Bytes: abksojn -> 0xffffff failed @ " + i,
						dataexp[i], data[i]);
			}

			dataexp = new byte[]{(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff};
			data = op26.string2Bytes("anxmrlxv", 4);
			for (int i = 0; i < data.length; i++) {
				assertEquals(
						"testString2Bytes: anxmrlxv -> 0xffffffff failed @ " + i,
						dataexp[i], data[i]);
			}
		} catch (CharCodeOpException e) {
			e.printStackTrace();
			fail("testString2Bytes: exception: " + e.getMessage());
		}
	}

	/**
	 * 
	 */
	public void testEncodeBytes2Strings() {
		byte [] data;
		String [] saexp;
		String [] sa;

		data = new byte[]{
				(byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x01, (byte)0x00, (byte)0x00,
				(byte)0xff, (byte)0xff, (byte)0xff
		};
		saexp = new String[]{
				"aaaaaaa",
				"aaaaaab",
				"abksojn"
		};
		sa = op26.encodeBytes2Strings(data, 3, 7);
		for (int i = 0; i < sa.length; i++) {
			assertEquals(
					"testEncodeBytes2Strings: set #1 failed @ " + i,
					saexp[i], sa[i]);
		}
	}

	/**
	 * 
	 */
	public void testDecodeStrings2Bytes() {
		byte [] dataexp;
		byte [] data;
		String [] sa;

		try {
			dataexp = new byte[]{
					(byte)0x00, (byte)0x00, (byte)0x00,
					(byte)0x01, (byte)0x00, (byte)0x00,
					(byte)0xff, (byte)0xff, (byte)0xff
			};
			sa = new String[]{
					"aaaaaaa",
					"aaaaaab",
					"abksojn"
			};
			data = op26.decodeStrings2Bytes(sa, 3, 9);
			for (int i = 0; i < data.length; i++) {
				assertEquals(
						"testDecodeStrings2Bytes: set #1 failed @ " + i,
						dataexp[i], data[i]);
			}
		} catch (CharCodeOpException e) {
			e.printStackTrace();
			fail("testDecodeStrings2Bytes: exception: " + e.getMessage());
		}
	}

}
