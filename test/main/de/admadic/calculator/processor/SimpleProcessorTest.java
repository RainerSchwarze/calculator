/**
 *
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
 */
package de.admadic.calculator.processor;

import java.util.ArrayList;

import de.admadic.calculator.types.CaDouble;

import junit.framework.TestCase;

/**
 * 
 * @author Rainer Schwarze
 *
 */
public class SimpleProcessorTest extends TestCase {
	SimpleProcessor proc;
	ArrayList<SimpleProcessorTestEntry> testCases;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SimpleProcessorTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		proc = new SimpleProcessor();
		testCases = new ArrayList<SimpleProcessorTestEntry>();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testProcessCommand() {
		try {
			testCases.clear();
			SimpleProcessorTestLoader sptl = new SimpleProcessorTestLoader(testCases);
			sptl.loadTestSet(
				"test/de/admadic/calculator/processor/SimpleProcessorTestData.txt");
			for (SimpleProcessorTestEntry spte : testCases) {
				System.out.println("testing case: " + spte.getName() + "...");
				proc.clear();
				if (spte.isSeqMode()) {
					ArrayList<String> inpList = spte.getInput();
					ArrayList<Double> resList = spte.getResList();
					ArrayList<Double> epsList = spte.getEpsList();
					String cmd;
					Double res;
					Double eps;
					CaDouble accu; 
					for (int i = 0; i < inpList.size(); i++) {
						cmd = inpList.get(i);
						res = resList.get(i);
						eps = epsList.get(i);
						
						proc.processCommand(cmd);
						if (res==null) 
							continue;	// next input, no result here.
						if (eps==null) {
							eps = spte.getEps();
						}

						accu = proc.getAccuValue();
						assertTrue(
								"test case failed: " + spte.getName() + 
								" not normal @ seqidx " + i + 
								" @ line " + spte.getLNo(i),
								!accu.isNotNormal());
						assertEquals(
								"test case failed: " + spte.getName() + 
								" result bad @ seqidx " + i + 
								" @ line " + spte.getLNo(i),
								res.doubleValue(), accu.value, 
								eps.doubleValue());
					}
				} else {
					// only one result for all input:
					ArrayList<String> inpList = spte.getInput();
					String cmd;
					for (int i = 0; i < inpList.size(); i++) {
						cmd = inpList.get(i);
						proc.processCommand(cmd);
					}
					CaDouble accu = proc.getAccuValue();
					assertTrue(
							"test case failed: " + spte.getName() + " not normal!",
							!accu.isNotNormal());
					assertEquals(
							"test case failed: " + spte.getName() + " result bad!",
							spte.getResult().doubleValue(), accu.value, 
							spte.getEps().doubleValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("test failed");
		} catch (Error e) {
			e.printStackTrace();
			fail("test failed");
		}
	}
}
