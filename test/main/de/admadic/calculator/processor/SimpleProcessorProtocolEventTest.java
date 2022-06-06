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
package de.admadic.calculator.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.admadic.calculator.types.CaDouble;
import de.admadic.calculator.types.CaDoubleFormat;
import de.admadic.calculator.types.CaDoubleFormatter;
import de.admadic.calculator.types.CaNumber;

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class SimpleProcessorProtocolEventTest extends TestCase {
	SimpleProcessor proc;
	Vector<SeqTestCase> seqTestCases;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SimpleProcessorProtocolEventTest.class);
	}

	/**
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		proc = new SimpleProcessor();
		seqTestCases = new Vector<SeqTestCase>();
	}

	/**
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		proc = null;
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testProtocol() {
//		try {
			seqTestCases.clear();
			SimpleProcessorProtocolTestLoader sptl = 
				new SimpleProcessorProtocolTestLoader(seqTestCases);
			sptl.loadTestSet(
				"test/de/admadic/calculator/processor/SimpleProcessorProtocolTestData.txt");
			for (SeqTestCase spte : seqTestCases) {
				System.out.println("testing case: " + spte.getName() + "...");
				proc.clear();

				Seq expSeq = spte.getExpectedSeq();
				Seq actSeq = spte.getActualSeq();
				proc.addProtocolListener(actSeq);
				for (SeqEntry seqEntry : expSeq.sequence) {
					System.out.println("  seq:");
					List<String> inputs = seqEntry.getInput();
					for (String inp : inputs) {
						System.out.println("    inp = " + inp);
						actSeq.addLastInput(inp);
						try {
							proc.processCommand(inp);
						} catch (ProcessorException e) {
							e.printStackTrace();
							fail("failing with exception: " + e.getMessage());
						}
					}
				}
				proc.removeProtocolListener(actSeq);

				// test equality
				assertEquals(
						"number of sequence entries differs",
						expSeq.sequence.size(), 
						actSeq.sequence.size());
				for (int i=0; i<expSeq.sequence.size(); i++) {
					if (i>=actSeq.sequence.size()) {
						fail("seq entry #" + i + " is not available in actual list");
					}
					SeqEntry expSeqE = expSeq.sequence.get(i);
					SeqEntry actSeqE = actSeq.sequence.get(i);

					assertEquals(
							"number of inputs differs for seqEntry #" + i,
							expSeqE.getInput().size(), 
							actSeqE.getInput().size());
					for (int j=0; j<expSeqE.getInput().size(); j++) {
						assertEquals(
								"input #" + j + " differs for seqEntry #" + i,
								expSeqE.getInput().get(j), 
								actSeqE.getInput().get(j));
					}

					assertEquals(
							"type differs for seqEntry #" + i,
							expSeqE.getType(), 
							actSeqE.getType());
					ProtocolEvent expEvent = expSeqE.getEvent();
					ProtocolEvent actEvent = actSeqE.getEvent();

					// if (expEvent.getOperation()!=null) 
					{
						assertEquals(
								"event operation differs for seqEntry #" + i,
								expEvent.getOperation(), 
								actEvent.getOperation());
					}
					// if (expEvent.getSubExpression()!=null) 
					{
						assertEquals(
								"event subEx differs for seqEntry #" + i,
								expEvent.getSubExpression(), 
								actEvent.getSubExpression());
					}
					if (expEvent.getValue()!=null) 
					{
						assertEquals(
								"event value differs for seqEntry #" + i,
								((CaDouble)expEvent.getValue()).getValue(), 
								((CaDouble)actEvent.getValue()).getValue(),
								1e-9);
					}
					
				}
			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("test failed");
//		} catch (Error e) {
//			e.printStackTrace();
//			fail("test failed");
//		}
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static class SeqEntry {
		/** event type op */
		public final static String TYPE_OP = "op";
		/** event type result */
		public final static String TYPE_RS = "rs";
		/** event type sub expression */
		public final static String TYPE_SX = "sx";
		/** event type clear */
		public final static String TYPE_CL = "cl";
		Vector<String> input;
		ProtocolEvent event;
		String type;

		/**
		 */
		public SeqEntry() {
			super();
			this.input = new Vector<String>();
		}

		/**
		 * @return Returns the event.
		 */
		public ProtocolEvent getEvent() {
			return event;
		}

		/**
		 * @return Returns the input.
		 */
		public List<String> getInput() {
			return input;
		}

		/**
		 * @return Returns the type.
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type The type to set.
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @param event The event to set.
		 */
		public void setEvent(ProtocolEvent event) {
			this.event = event;
		}
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static class Seq implements ProtocolEventListener {
		Vector<SeqEntry> sequence;
		SeqEntry curSeqEntry;

		/**
		 * 
		 */
		public Seq() {
			super();
			sequence = new Vector<SeqEntry>();
		}

		private ProtocolEvent deepClone(ProtocolEvent srcEvent) throws CloneNotSupportedException {
			ProtocolEvent dstEvent = new ProtocolEvent(srcEvent.getSource());
			dstEvent.setOperation(srcEvent.getOperation());
			dstEvent.setSubExpression(srcEvent.getSubExpression());
			if (srcEvent.getValue()!=null) {
				dstEvent.setValue(srcEvent.getValue().clone());
			}
			if (srcEvent.getLastSubResult()!=null) {
				dstEvent.setLastSubResult(srcEvent.getLastSubResult().clone());
			}
			if (srcEvent.getCurResult()!=null) {
				dstEvent.setCurResult(srcEvent.getCurResult().clone());
			}
			return dstEvent;
		}
		
		private void addImpl(ProtocolEvent event, String type) {
			System.out.println("adding type " + type);
			if (curSeqEntry==null) {
				fail("curSeqEntry is null @ " + type + " / " + event.toString());
			}
			try {
				curSeqEntry.setEvent(deepClone(event));
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Error("clone error");
			}
			curSeqEntry.setType(type);
			sequence.add(curSeqEntry);

			curSeqEntry = null;
		}
		
		/**
		 * @param event
		 * @see de.admadic.calculator.processor.ProtocolEventListener#addOp(de.admadic.calculator.processor.ProtocolEvent)
		 */
		public void addOp(ProtocolEvent event) {
			addImpl(event, SeqEntry.TYPE_OP);
		}

		/**
		 * @param event
		 * @see de.admadic.calculator.processor.ProtocolEventListener#addSubExprOp(de.admadic.calculator.processor.ProtocolEvent)
		 */
		public void addSubExprOp(ProtocolEvent event) {
			addImpl(event, SeqEntry.TYPE_SX);
		}

		/**
		 * @param event
		 * @see de.admadic.calculator.processor.ProtocolEventListener#addResult(de.admadic.calculator.processor.ProtocolEvent)
		 */
		public void addResult(ProtocolEvent event) {
			addImpl(event, SeqEntry.TYPE_RS);
		}

		/**
		 * @param event
		 * @see de.admadic.calculator.processor.ProtocolEventListener#addClear(de.admadic.calculator.processor.ProtocolEvent)
		 */
		public void addClear(ProtocolEvent event) {
			addImpl(event, SeqEntry.TYPE_CL);
		}

		/**
		 * @param s The lastInput to set.
		 */
		public void addLastInput(String s) {
			if (curSeqEntry==null) {
				curSeqEntry = new SeqEntry();
			}
			curSeqEntry.getInput().add(s);
		}

		/**
		 * @param seqEntry
		 */
		public void addSeqEntry(SeqEntry seqEntry) {
			sequence.add(seqEntry);
		}
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static class SeqTestCase {
		String name;
		Seq expectedSeq;
		Seq actualSeq;

		/**
		 * 
		 */
		public SeqTestCase() {
			super();
			expectedSeq = new Seq();
			actualSeq = new Seq();
		}

		/**
		 * @return Returns the actualSeq.
		 */
		public Seq getActualSeq() {
			return actualSeq;
		}

		/**
		 * @return Returns the expectedSeq.
		 */
		public Seq getExpectedSeq() {
			return expectedSeq;
		}

		/**
		 * @return Returns the name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name The name to set.
		 */
		public void setName(String name) {
			this.name = name;
		}
		
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public class SimpleProcessorProtocolTestLoader {
		List<SeqTestCase> entries;
		int lineno;
		CaDoubleFormatter numFmt;

		/**
		 * @param entries 
		 * 
		 */
		public SimpleProcessorProtocolTestLoader(List<SeqTestCase> entries) {
			super();
			this.entries = entries;
			numFmt = new CaDoubleFormatter();
		}

		/**
		 * @param filename
		 */
		public void loadTestSet(String filename) {
			loadTestSet(new File(filename));
		}

		/**
		 * @param file
		 */
		public void loadTestSet(File file) {
			String line;
			FileReader fr;
			BufferedReader br;
			int formatOK = -1;
			SeqTestCase curEntry = null;
			String left, right;

			lineno = 0;
			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				while ((line = br.readLine())!=null) {
					lineno++;
					line = line.trim();
					if (formatOK<0) {
						if (line.equals("#format=1.0")) {
							formatOK = 1;
						}
					}
					if (line.startsWith("#")) continue;
					if (line.equals("")) continue;
					if (formatOK<1) {
						throw new Error("Error: invalid test file format.");
					}

					if (line.indexOf('#')>=0) {
						line = line.substring(0, line.indexOf('#'));
						line = line.trim();
					}
					line = line.replace('\t', ' ');

					int eqpos = line.indexOf('=');
					if (eqpos<0) {
						if (line.equals("endtest")) {
							entries.add(curEntry);
							curEntry = null;
							continue;
						} else {
							throw new Error("Error: no =-sign found @ line " + lineno);
						}
					}
//					# seq	= inputs	-> type op	value	subex lastsubres	curres
//					test	= input simple 1
//					seq		= 1	0 + 	-> op "" "10.0" <null> "0.0" "10.0"
//					endtest

					left = line.substring(0, eqpos).trim();
					right = line.substring(eqpos+1).trim();

					if (left.equals("test")) {
						curEntry = new SeqTestCase();
						curEntry.setName(right);
					} else if (left.equals("seq")) {
						parseSeqInput(curEntry, right);
					} else {
						throw new Error("Error: invalid left side @ line " + lineno);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * @param entry
		 * @param input
		 */
		public void parseSeqInput(SeqTestCase entry, String input) {
			String [] items = input.split("[ ]+");
			String inp = null;
			int idx = 0;
			if (items.length<1) {
				throw new Error("Error: invalid seq format @ line " + lineno + " (no input)");
			}
			SeqEntry seqEntry = new SeqEntry();
			ProtocolEvent event = new ProtocolEvent(this);
			seqEntry.setEvent(event);
			entry.getExpectedSeq().addSeqEntry(seqEntry);

			// inputs loop:
			while (idx<items.length) {
				inp = items[idx].trim();
				if (inp.equals("->")) {
					break;
				}
				seqEntry.getInput().add(inp.trim());
				idx++;
			}
			if (idx>=items.length) {
				throw new Error("Missing event separator '->' @ line " + lineno);
			}
			idx++;
//			seq		= 1	0 + 	-> op "" "10.0" <null> "0.0" "10.0"
			if (idx>=items.length) {
				throw new Error("No type defined for event @ line " + lineno);
			}
			String op = null;
			String subEx = null;
			CaDouble value = null;
			CaDouble lastSubRes = null;
			CaDouble result = null;
			String type = items[idx].trim();
			if (
					type.equals(SeqEntry.TYPE_OP) ||
					type.equals(SeqEntry.TYPE_RS) ||
					type.equals(SeqEntry.TYPE_SX) ||
					type.equals(SeqEntry.TYPE_CL)
					) {
				seqEntry.setType(type);
			} else {
				throw new Error("invalid type @ line " + lineno + " op: " + type);
			}
			idx++;
			if (idx<items.length) {	// op
				op = items[idx].trim();
				if (op.startsWith("\"") && op.endsWith("\"")) {
					op = op.substring(1, op.length()-1);
					
				} else {
					throw new Error("op must be enclosed in quotes @ line " + lineno);
				}
			}
			idx++;
			if (idx<items.length) { // subex
				subEx = items[idx].trim();
				if (subEx.equals("<null>")) {
					subEx = null;
				} else if (subEx.startsWith("\"") && subEx.endsWith("\"")) {
					subEx = subEx.substring(1, subEx.length()-1);
					subEx = subEx.replace('_', ' ');
				} else {
					throw new Error("subEx must be enclosed in quotes @ line " + lineno);
				}
			}
			idx++;
			if (idx<items.length) { // value
				String tmp = items[idx].trim();
				if (tmp.equals("<null>")) {
					value = null;
				} else if (tmp.startsWith("\"") && tmp.endsWith("\"")) {
					tmp = tmp.substring(1, tmp.length()-1);
					value = new CaDouble();
					numFmt.parseNumber(tmp, value);
				} else {
					throw new Error("value must be enclosed in quotes @ line " + lineno);
				}
			}
			idx++;
			if (idx<items.length) { // value
				String tmp = items[idx].trim();
				if (tmp.equals("<null>")) {
					lastSubRes = null;
				} else if (tmp.startsWith("\"") && tmp.endsWith("\"")) {
					tmp = tmp.substring(1, tmp.length()-1);
					lastSubRes = new CaDouble();
					numFmt.parseNumber(tmp, lastSubRes);
				} else {
					throw new Error("lastSubRes must be enclosed in quotes @ line " + lineno);
				}
			}
			idx++;
			if (idx<items.length) { // value
				String tmp = items[idx].trim();
				if (tmp.equals("<null>")) {
					result = null;
				} else if (tmp.startsWith("\"") && tmp.endsWith("\"")) {
					tmp = tmp.substring(1, tmp.length()-1);
					result = new CaDouble();
					numFmt.parseNumber(tmp, result);
				} else {
					throw new Error("result must be enclosed in quotes @ line " + lineno);
				}
			}

			event.setOperation(op);
			event.setSubExpression(subEx);
			event.setValue(value);
			event.setLastSubResult(lastSubRes);
			event.setCurResult(result);
		}
	}
	
}
