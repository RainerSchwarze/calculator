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

/**
 * @author Rainer Schwarze
 *
 */
public class SimpleProcessorTestLoader {
	ArrayList<SimpleProcessorTestEntry> entries;
	int lineno;

	/**
	 * @param entries 
	 * 
	 */
	public SimpleProcessorTestLoader(ArrayList<SimpleProcessorTestEntry> entries) {
		super();
		this.entries = entries;
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
		SimpleProcessorTestEntry curEntry = null;
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
				left = line.substring(0, eqpos).trim();
				right = line.substring(eqpos+1).trim();

				if (left.equals("test")) {
					curEntry = new SimpleProcessorTestEntry();
					curEntry.setName(right);
				} else if (left.equals("seqmode")) {
					curEntry.setSeqMode();
				} else if (left.equals("input")) {
					parseInput(curEntry, right);
				} else if (left.equals("seq")) {
					parseSeqInput(curEntry, right);
				} else if (left.equals("result")) {
					Double v = Double.valueOf(right);
					curEntry.setResult(v);
				} else if (left.equals("eps")) {
					Double v = Double.valueOf(right);
					curEntry.setEps(v);
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
	public void parseInput(SimpleProcessorTestEntry entry, String input) {
		String [] items = input.split(" ");
		for (int i = 0; i < items.length; i++) {
			if (items[i]==null) continue;
			if (items[i].equals("")) continue;
			if (items[i].equals(" ")) continue;
			entry.addInput(items[i]);
		}
	}

	/**
	 * @param entry
	 * @param input
	 */
	public void parseSeqInput(SimpleProcessorTestEntry entry, String input) {
		String [] items = input.split(" ");
		String inp = null;
		Double res = null;
		Double eps = null;
		if (items.length<1) {
			throw new Error("Error: invalid seq format @ line " + lineno + " (no input)");
		}
		if (items.length>0) {
			// always:
			inp = items[0].trim();
		}
		if (items.length>1) {
			res = Double.valueOf(items[1].trim());
		}
		if (items.length>2) {
			eps = Double.valueOf(items[2].trim());
		}
		entry.addSeqInput(inp, res, eps, Integer.valueOf(lineno));
	}
}
