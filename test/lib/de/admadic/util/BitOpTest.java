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
public class BitOpTest extends TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(BitOpTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	boolean testAllZero(byte [] data) {
		boolean ret = true;
		for (byte b : data) {
			if (b!=0) return false;
		}
		return ret;
	}

	/**
	 * 
	 */
	public void testXor() {
		byte [] data;
		byte [] dataorg;
		byte [] dataexp;
		byte [] xorop;

		data  = new byte[]{(byte)0x01, (byte)0xff, (byte)0x55, (byte)0x80};
		xorop = new byte[]{(byte)0x01, (byte)0xff, (byte)0x55, (byte)0x80};
		dataorg = new byte[4];
		System.arraycopy(data, 0, dataorg, 0, data.length);

		BitOp.xor(data, xorop);
		if (testAllZero(data)) {
			// passed
		} else {
			fail("testXor: xor with itself did not give zero");
		}

		xorop = new byte[]{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
		System.arraycopy(dataorg, 0, data, 0, data.length);
		BitOp.xor(data, xorop);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testXor: xor with 0x00 did not remain unchanged",
					dataorg[i], data[i]);
		}

		xorop = new byte[]{(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff};
		System.arraycopy(dataorg, 0, data, 0, data.length);
		BitOp.xor(data, xorop);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testXor: xor with 0xff did not result in complement",
					dataorg[i], ~data[i]);
		}

		data     = new byte[]{(byte)0x01, (byte)0xff, (byte)0x55, (byte)0x80};
		xorop    = new byte[]{(byte)0x55, (byte)0xAA};
		dataexp  = new byte[]{(byte)0x54, (byte)0x55, (byte)0x00, (byte)0x2A};
		BitOp.xor(data, xorop);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testXor: smaller xorop vector did not repeat (probably)",
					dataexp[i], data[i]);
		}
	}

	/**
	 * 
	 */
	public void testXorOut() {
		byte [] data;
		byte [] xorop;
		byte [] out;

		data  = new byte[]{(byte)0x01, (byte)0xff, (byte)0x55, (byte)0x80};
		xorop = new byte[]{(byte)0xfe, (byte)0x00, (byte)0xaa, (byte)0x7f};

		out = BitOp.xorOut(data, xorop);

		assertNotNull("testXorOut: output vector has not been created", out);
		assertEquals(
				"testXorOut: output vector does not have expected length", 
				data.length, out.length);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testXorOut: out was not created properly" +
					" (index " + i + ")", 
					(byte)0xff, out[i]);
		}
	}

	/**
	 * 
	 */
	public void testCalculateBitCount() {
		assertEquals(
				"testCalculateBitCount: all 1 bits failed",
				32, BitOp.calculateBitCount(0xffffffff));
		assertEquals(
				"testCalculateBitCount: all 0 bits failed",
				0, BitOp.calculateBitCount(0x00000000));
		for (int i=0; i<32; i++) {
			int mask = 1<<i;
			assertEquals(
					"testCalculateBitCount: shifted 1 bit failed",
					1, BitOp.calculateBitCount(mask));
		}
		for (int i=0; i<24; i++) {
			int mask = 0x11<<i;
			assertEquals(
					"testCalculateBitCount: shifted 1 0000 0001 pattern failed",
					2, BitOp.calculateBitCount(mask));
		}
		{
			int mask = 0;
			for (int i=1; i<32; i++) {
				mask = (mask << 1) | 1;
				assertEquals(
						"testCalculateBitCount: growing 0..<1 pattern failed",
						i, BitOp.calculateBitCount(mask));
			}
		}
	}

	/**
	 * 
	 */
	public void testCalculateByteBase() {
		assertEquals(
				"testCalculateByteBase: all 0 failed",
				0, BitOp.calculateByteBase(0x00000000));
		assertEquals(
				"testCalculateByteBase: all 1 failed",
				4, BitOp.calculateByteBase(0xffffffff));
		for (int i=0; i<4; i++) {
			int mask = 0x01 << (i*8);
			assertEquals(
					"testCalculateByteBase: shifted 0x01 byte failed",
					i+1, BitOp.calculateByteBase(mask));
		}
		for (int i=0; i<3; i++) {
			int mask = 0x0101 << (i*8);
			assertEquals(
					"testCalculateByteBase: shifted 0x0101 failed",
					i+2, BitOp.calculateByteBase(mask));
		}
	}

	/**
	 * 
	 */
	public void testCalculateBitIndexTable() {
		int mask = 0x12345678;
		int [][] tableexp = {
			// biti, bytei
				{ 3, 0 },	// 8
				{ 4, 0 },	// 7 ...
				{ 5, 0 },
				{ 6, 0 },
				{ 9, 1 },	// 6 ...
				{10, 1 },
				{12, 1 },	// 5 ...
				{14, 1 },
				{18, 2 },	// 4
				{20, 2 },	// 3 ... 
				{21, 2 }, 
				{25, 3 },	// 2 
				{28, 3 },	// 1 
		};

		int bitcount = BitOp.calculateBitCount(mask);
		int [][] table = new int[bitcount][2];
		BitOp.calculateBitIndexTable(mask, table);
		for (int i = 0; i < table.length; i++) {
			assertEquals("testCalculateBitIndexTable: bitindex failed",
					tableexp[i][0], table[i][0]);
			assertEquals("testCalculateBitIndexTable: byteindex failed",
					tableexp[i][1], table[i][1]);
		}
	}

	/**
	 * 
	 */
	public void testExtractBits() {
		byte [] data;
		byte [] out;
		int mask;
		byte [] outexp;

		data = new byte[]{
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
		};
		out = BitOp.extractBits(data, 0x00);
		assertNull("testExtractBits: empty mask did not give null output", out);

		// half size
		mask = 0x55;
		outexp = new byte[]{
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
		};
		out = BitOp.extractBits(data, mask);
		assertEquals(
				"testExtractBits: output vector size not correct", 
				outexp.length, out.length);
		for (int i = 0; i < outexp.length; i++) {
			assertEquals(
					"testExtractBits: extraction of zero src should be all zero", 
					outexp[i], out[i]);
		}

		data = new byte[]{
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff,
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff
		};
		mask = 0x55;
		outexp = new byte[]{
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff
		};
		out = BitOp.extractBits(data, mask);
		for (int i = 0; i < outexp.length; i++) {
			assertEquals(
					"testExtractBits: extraction of all 1 src should be all 1", 
					outexp[i], out[i]);
		}
		data = new byte[]{
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff,
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff
		};
		mask = 0x55;
		outexp = new byte[]{
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff
		};
		out = BitOp.extractBits(data, mask);
		for (int i = 0; i < outexp.length; i++) {
			assertEquals(
					"testExtractBits: extraction of all 1 src should be all 1", 
					outexp[i], out[i]);
		}

		data = new byte[]{
				(byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00,
				(byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00
		};
		mask = 0x55;
		outexp = new byte[]{
				(byte)0x0f, (byte)0x0f, (byte)0x0f, (byte)0x0f
		};
		out = BitOp.extractBits(data, mask);
		for (int i = 0; i < outexp.length; i++) {
			assertEquals(
					"testExtractBits: 0xff.0x00 [0x55] -> 0x0f", 
					outexp[i], out[i]);
		}

		data = new byte[]{
				// bit field: 0000.0001.0010.0011. ... .1111
				(byte)0x10, (byte)0x32, (byte)0x54, (byte)0x76,
				(byte)0x98, (byte)0xba, (byte)0xdc, (byte)0xfe
		};
		mask = 0x8421;	// mask:  1000 0100 0010 0001
		// in field = 
		// 1111.1110.1101.1100.1011.1010.1001.1000
		// 0111.0110.0101.0100.0011.0010.0001.0000
		// mask:
		// 1000 0100 0010 0001 1000 0100 0010 0001
		//>1     1     0     0 1     0     0     0 >> C8
		//>0     1     0     0 0     0     0     0 >> 40
		outexp = new byte[]{
				(byte)0x40, (byte)0xc8
		};
		out = BitOp.extractBits(data, mask);
		for (int i = 0; i < outexp.length; i++) {
			assertEquals(
					"testExtractBits: .seq", 
					outexp[i], out[i]);
		}
	}

	/**
	 * 
	 */
	public void testInsertBits() {
		byte [] data;
		byte [] dataexp;
		byte [] bits;
		int mask;

		data = new byte[]{
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff,
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff
		};
		dataexp = new byte[]{
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff,
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff
		};
		mask = 0x8421;
		bits = BitOp.extractBits(data, mask);
		BitOp.maskBits(data, mask, 0);
		BitOp.insertBits(data, bits, mask);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testInsertBits: extract/insert on 0xff should give 0xff", 
					dataexp[i], data[i]);
		}

		data = new byte[]{
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
		};
		bits = new byte[]{
				(byte)0x5a, (byte)0xa5
						// 1010 0101 0101 1010 <<<
		};
		mask = 0x8421;	// 1000 0100 0010 0001 <<<
		dataexp = new byte[]{
				(byte)0x20, (byte)0x80, (byte)0x01, (byte)0x04,
				(byte)0x01, (byte)0x04, (byte)0x20, (byte)0x80
		};
		BitOp.insertBits(data, bits, mask);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testInsertBits: test set #1 @ " + i, 
					dataexp[i], data[i]);
		}
	}

	/**
	 * 
	 */
	public void testMaskBits() {
		byte [] data;
		byte [] dataexp;

		data = new byte[]{
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 
		};
		BitOp.maskBits(data, 0x00, 0);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testMaskBits: masking zero with 0x00/0 should give 0",
					(byte)0x00, data[i]);
		}
		BitOp.maskBits(data, 0x00, 1);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testMaskBits: masking zero with 0x00/1 should give 0",
					(byte)0x00, data[i]);
		}
		BitOp.maskBits(data, 0xff, 0);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testMaskBits: masking zero with 0xff/0 should give 0",
					(byte)0x00, data[i]);
		}
		BitOp.maskBits(data, 0xff, 1);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testMaskBits: masking zero with 0xff/1 should give 1",
					(byte)0xff, data[i]);
		}
		BitOp.maskBits(data, 0xff, 0);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testMaskBits: masking 1 with 0xff/0 should give 0",
					(byte)0x00, data[i]);
		}
		BitOp.maskBits(data, 0x5a, 1);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testMaskBits: masking zero with 0x5a/1 should give 0x5a",
					(byte)0x5a, data[i]);
		}

		data = new byte[]{
				// bit field: 0000.0001.0010.0011. ... .1111
				(byte)0x10, (byte)0x32, (byte)0x54, (byte)0x76,
				(byte)0x98, (byte)0xba, (byte)0xdc, (byte)0xfe
		};
		// mask = 1000 0100 0010 0001
		dataexp = new byte[]{ // OR:
				(byte)0x31, (byte)0xb6, (byte)0x75, (byte)0xf6,
				(byte)0xb9, (byte)0xbe, (byte)0xfd, (byte)0xfe
		};
		BitOp.maskBits(data, 0x8421, 1);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testMaskBits: test set #1",
					dataexp[i], data[i]);
		}

		data = new byte[]{
				// bit field: 0000.0001.0010.0011. ... .1111
				(byte)0x10, (byte)0x32, (byte)0x54, (byte)0x76,
				(byte)0x98, (byte)0xba, (byte)0xdc, (byte)0xfe
		};
		// mask = 1000 0100 0010 0001
		dataexp = new byte[]{ // AND:
				(byte)0x10, (byte)0x32, (byte)0x54, (byte)0x72,
				(byte)0x98, (byte)0x3a, (byte)0xdc, (byte)0x7a
		};
		BitOp.maskBits(data, 0x8421, 0);
		for (int i = 0; i < data.length; i++) {
			assertEquals(
					"testMaskBits: test set #2",
					dataexp[i], data[i]);
		}
	}

	/**
	 * 
	 */
	public void testBytes2Hex() {
		// nothing to test
	}

	/**
	 * 
	 */
	public void testInt2Bytes() {
		{
			byte [] dataexp = {0x01, 0x02, 0x03, 0x04};
			byte [] out = new byte[4];
			int exp = 0x04030201;
			BitOp.int2Bytes(exp, out, 0, 4);
			for (int i = 0; i < out.length; i++) {
				assertEquals(
						"testInt2Bytes: test set #1",
						dataexp[i], out[i]);
			}
		}
		{
			byte [] dataexp = {0x00, 0x00, 0x00, 0x00};
			byte [] out = new byte[4];
			int exp = 0x00000000;
			BitOp.int2Bytes(exp, out, 0, 4);
			for (int i = 0; i < out.length; i++) {
				assertEquals(
						"testInt2Bytes: test set #2",
						dataexp[i], out[i]);
			}
		}
		{
			byte [] dataexp = {(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff};
			byte [] out = new byte[4];
			int exp = 0xffffffff;
			BitOp.int2Bytes(exp, out, 0, 4);
			for (int i = 0; i < out.length; i++) {
				assertEquals(
						"testInt2Bytes: test set #3",
						dataexp[i], out[i]);
			}
		}
	}

	/**
	 * 
	 */
	public void testBytes2Int() {
		{
			byte [] data = {0x01, 0x02, 0x03, 0x04};
			int exp = 0x04030201;
			int v;
			v = BitOp.bytes2Int(data, 0, 4);
			assertEquals(
					"testBytes2Int: test set #1",
					exp, v);
		}
		{
			byte [] data = {0x00, 0x00, 0x00, 0x00};
			int exp = 0x00000000;
			int v;
			v = BitOp.bytes2Int(data, 0, 4);
			assertEquals(
					"testBytes2Int: test set #2",
					exp, v);
		}
		{
			byte [] data = {(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff};
			int exp = 0xffffffff;
			int v;
			v = BitOp.bytes2Int(data, 0, 4);
			assertEquals(
					"testBytes2Int: test set #3",
					exp, v);
		}
	}

}
