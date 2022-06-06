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
package de.admadic.calculator.modules.indxp.core;

import java.util.ArrayList;

/**
 * @author Rainer Schwarze
 *
 */
public class RunsBuilder {
	ArrayList<FactorInteraction> factorInteractionsLink;
	ArrayList<Factor> factorsLink;
//	ArrayList<Level> levelsLink;
	ArrayList<Run> runsLink;
	

	/**
	 * @param factorInteractionsLink 
	 * @param factorsLink 
	 * @param runsLink 
	 */
	public RunsBuilder(
			ArrayList<FactorInteraction> factorInteractionsLink, 
			ArrayList<Factor> factorsLink,
			ArrayList<Run> runsLink) {
		super();
		this.factorInteractionsLink = factorInteractionsLink;
		this.factorsLink = factorsLink;
		this.runsLink = runsLink;
	}

	/**
	 * Creates a full factorial set of Runs.
	 */
	public void createFullFactorialRuns() {
		// for all factor/level combinations:
		ArrayList<Pair<Factor,Integer>> workspace = 
			new ArrayList<Pair<Factor,Integer>>();
		runsLink.clear();
		createRunAtLevel(workspace, 0, factorsLink.size(), -1);
//		fireDataEvent(null, DataEvent.RUNS_STRUCT_CHANGED);
	}

	// /////////////////////////////////////////////
	// build engine methods
	// /////////////////////////////////////////////
	
	/**
	 * helper method.
	 * @param workspace
	 * @param level
	 * @param length
	 * @param maxRuns -1 if dont care
	 */
	private void createRunAtLevel(
			ArrayList<Pair<Factor, Integer>> workspace,
			int level, int length, int maxRuns) {
		if (workspace.size()==length) {
			if (maxRuns>=0 && runsLink.size()>=maxRuns) return;

			Run run = new Run(factorsLink, factorInteractionsLink);
			for (Pair<Factor, Integer> pair : workspace) {
				run.setLevel(pair.getFirst(), pair.getSecond());
			}
			run.updateFactorInteractionLevels();
			run.setId(String.valueOf(runsLink.size()+1));
			runsLink.add(run);
		} else {
			workspace.add(
				new Pair<Factor,Integer>(
						factorsLink.get(level),
						Integer.valueOf(-1)));
			createRunAtLevel(workspace, level+1, length, maxRuns);
			workspace.remove(workspace.size()-1);

			workspace.add(
				new Pair<Factor,Integer>(
						factorsLink.get(level),
						Integer.valueOf(+1)));
			createRunAtLevel(workspace, level+1, length, maxRuns);
			workspace.remove(workspace.size()-1);
		}
	}

	/**
	 * @param runCount
	 */
	public void createRangedRuns(int runCount) {
		ArrayList<Pair<Factor,Integer>> workspace = 
			new ArrayList<Pair<Factor,Integer>>();
		runsLink.clear();
		createRunAtLevel(workspace, 0, factorsLink.size(), runCount);
	}
}
