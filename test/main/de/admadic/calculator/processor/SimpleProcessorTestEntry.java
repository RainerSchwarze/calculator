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

import java.util.ArrayList;

/**
 * @author Rainer Schwarze
 *
 */
public class SimpleProcessorTestEntry {
	String name;
	ArrayList<String> input;
	Double result;
	Double eps;

	boolean seqMode;
	/*
	 * The following lists are "synchronised" to the input list.
	 * null entries are allowed in these two lists. A result of null means
	 * that the result shall not be tested, eps of null means, that the standard
	 * eps shall be used.
	 */
	ArrayList<Double> resList;
	ArrayList<Double> epsList;
	ArrayList<Integer> lnoList;

	/**
	 * 
	 */
	public SimpleProcessorTestEntry() {
		super();
		input = new ArrayList<String>();
		eps = new Double(1e-9);
		seqMode = false;
	}

	/**
	 * Activates sequence mode, in which the result is checked after each
	 * input entry.
	 */
	public void setSeqMode() {
		seqMode = true;
		resList = new ArrayList<Double>();
		epsList = new ArrayList<Double>();
		lnoList = new ArrayList<Integer>();
	}
	
	/**
	 * @return Returns the eps.
	 */
	public Double getEps() {
		return eps;
	}

	/**
	 * @param eps The eps to set.
	 */
	public void setEps(Double eps) {
		this.eps = eps;
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

	/**
	 * @return Returns the result.
	 */
	public Double getResult() {
		return result;
	}

	/**
	 * @param result The result to set.
	 */
	public void setResult(Double result) {
		this.result = result;
	}

	/**
	 * @return Returns the input.
	 */
	public ArrayList<String> getInput() {
		return input;
	}

	/**
	 * @param cmd
	 */
	public void addInput(String cmd) {
		input.add(cmd);
	}

	/**
	 * Adds a sequence of input, result and eps to the test list.
	 * 
	 * @param inp	A String being the input command.
	 * @param res	A Double being the result, or null.
	 * @param eps	A Double being the eps, or null.
	 * @param lno	An Integer being the line number of the test spec. 
	 */
	public void addSeqInput(String inp, Double res, Double eps, Integer lno) {
		if (!seqMode) {
			throw new Error("SimpleProcessorTestEntry: seqmode required for addSeq");
		}
		input.add(inp);
		resList.add(res);
		epsList.add(eps);
		lnoList.add(lno);
	}

	/**
	 * @return Returns the epsList.
	 */
	public ArrayList<Double> getEpsList() {
		return epsList;
	}

	/**
	 * @return Returns the resList.
	 */
	public ArrayList<Double> getResList() {
		return resList;
	}

	/**
	 * @return Returns the seqMode.
	 */
	public boolean isSeqMode() {
		return seqMode;
	}

	/**
	 * @param seqidx
	 * @return	Returns the line number in the test file for the given seq entry.
	 */
	public int getLNo(int seqidx) {
		if (!seqMode) {
			throw new Error("SimpleProcessorTestEntry: seqmode required for getLNo");
		}
		return lnoList.get(seqidx).intValue();
	}
}
