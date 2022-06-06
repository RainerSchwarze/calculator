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

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.event.EventListenerList;

/*
 * FIXME: add locking mechanism
 */

/*
 * FIXME: listeners for data change
 * Add a listener interface which allows panels and views to attach as 
 * listeners. Then add notify functions which spread the news if there is 
 * any.
 */

/**
 * This class holds all data for an analysis of an Industrial Designed
 * Experiment. Important: The data collections used shall only be changed,
 * but not replaced by other collections.
 * TableModels depend on direct access to these collections and will 
 * fail when the collection itself is replaced in the Data instance.
 * 
 * @author Rainer Schwarze
 *
 */
public class InDXpData implements 
		DataEventDispatcher, DataEventServer, 
		DataItemStatusServer, DataEventListener {
	final static boolean DBG = false;

	boolean dirty;
	File fileName;
	
	ArrayList<Factor> factors;
//	ArrayList<Level> levels;
	ArrayList<FactorInteraction> factorInteractions;
	ArrayList<Run> runs;
	int replicateCount = 3;	// FIXME: make input field from that
	ArrayList<ExpResults> expResults;
	LevelAnalysisYAvg levelAnalysisYAvg;
	LevelAnalysisSigma levelAnalysisSigma;

	Hashtable<Integer,DataItemStatus> dataItemStatus;
	
	FactorInteractionBuilder factorInteractionBuilder;
	RunsBuilder runsBuilder;
	
	EventListenerList listenerList = new EventListenerList();
	
	/**
	 * 
	 */
	public InDXpData() {
		super();
		factors = new ArrayList<Factor>();
		factorInteractions = new ArrayList<FactorInteraction>();
		runs = new ArrayList<Run>();
		expResults = new ArrayList<ExpResults>();
		levelAnalysisYAvg = new LevelAnalysisYAvg(
				factorInteractions, runs, expResults);
		levelAnalysisSigma = new LevelAnalysisSigma(
				factorInteractions, runs, expResults);
		factorInteractionBuilder = new FactorInteractionBuilder(
				factors, factorInteractions);
		runsBuilder = new RunsBuilder(factorInteractions, factors, runs);
		dataItemStatus = new Hashtable<Integer,DataItemStatus>();
		dataItemStatus.put(
				Integer.valueOf(DataItemStatus.DI_FACTORS), 
				new DataItemStatus()); 
		dataItemStatus.put(
				Integer.valueOf(DataItemStatus.DI_FACTORINTERACTIONS), 
				new DataItemStatus()); 
		dataItemStatus.put(
				Integer.valueOf(DataItemStatus.DI_RUNS), 
				new DataItemStatus()); 
		dataItemStatus.put(
				Integer.valueOf(DataItemStatus.DI_EXPRESULTS), 
				new DataItemStatus());
		setDirty(false);
		this.addDataListener(this);
	}

	/**
	 * 
	 */
	public void resetData() {
		factors.clear();
		factorInteractions.clear();
		runs.clear();
		expResults.clear();
		levelAnalysisYAvg.resetData();
		levelAnalysisSigma.resetData();
		dataItemStatus.get(
				Integer.valueOf(DataItemStatus.DI_FACTORS)).reset();
		dataItemStatus.get(
				Integer.valueOf(DataItemStatus.DI_FACTORINTERACTIONS)).reset();
		dataItemStatus.get(
				Integer.valueOf(DataItemStatus.DI_RUNS)).reset();
		dataItemStatus.get(
				Integer.valueOf(DataItemStatus.DI_EXPRESULTS)).reset();

		this.notifyEvent(this, DataEvent.FACTORS_STRUCT_CHANGED);
		this.notifyEvent(this, DataEvent.FACTORINTERACTIONS_STRUCT_CHANGED);
		this.notifyEvent(this, DataEvent.RUNS_STRUCT_CHANGED);
		this.notifyEvent(this, DataEvent.EXPRESULTS_STRUCT_CHANGED);
		this.notifyEvent(this, DataEvent.LEVELANALYSIS_STRUCT_CHANGED);

		setDirty(false);
	}

	/**
	 * @param id
	 * @return	Returns the DataItemStatus for the given id
	 * @see de.admadic.calculator.modules.indxp.core.DataItemStatusServer#getDataItemStatus(int)
	 */
	public DataItemStatus getDataItemStatus(int id) {
		if (!dataItemStatus.containsKey(Integer.valueOf(id))) return null;
		return dataItemStatus.get(Integer.valueOf(id));
	}

	/**
	 * @param id
	 * @param dis
	 */
	public void setDataItemStatus(int id, DataItemStatus dis) {
		dataItemStatus.put(Integer.valueOf(id), dis);
	}
	
	/**
	 * @return Returns the factorInteractionBuilder.
	 */
	public FactorInteractionBuilder getFactorInteractionBuilder() {
		return factorInteractionBuilder;
	}


	/**
	 * @return Returns the runsBuilder.
	 */
	public RunsBuilder getRunsBuilder() {
		return runsBuilder;
	}


	/**
	 * @param f
	 */
	public void addFactor(Factor f) {
		factors.add(f);
		fireDataEvent(null, DataEvent.FACTORS_STRUCT_CHANGED);
	}

	/**
	 * @param f
	 */
	public void removeFactor(Factor f) {
		factors.remove(f);
		fireDataEvent(null, DataEvent.FACTORS_STRUCT_CHANGED);
	}

	/**
	 * @param name
	 * @return	Returns the factor for the given name.
	 */
	public Factor getFactor(String name) {
		for (Factor f : factors) {
			String n = f.getName();
			if (n!=null && n.equals(name)) {
				return f;
			}
		}
		return null;
	}

	/**
	 * @return	Returns the internal representation of the Factor list.
	 */
	public ArrayList<Factor> getFactorList() {
		return factors;
	}

	/**
	 * @return	Returns the internal representation of the 
	 * FactorInteraction list.
	 */
	public ArrayList<FactorInteraction> getFactorInteractionList() {
		return factorInteractions;
	}

	/**
	 * @return	Returns the internal representation of the Run list.
	 */
	public ArrayList<Run> getRunList() {
		return runs;
	}

	/**
	 * @return	Returns the internal representation of the ExpResult list.
	 */
	public ArrayList<ExpResults> getExpResultsList() {
		return expResults;
	}

	/**
	 * @return Returns the levelAnalysisSigma.
	 */
	public LevelAnalysisSigma getLevelAnalysisSigma() {
		return levelAnalysisSigma;
	}

	/**
	 * @return Returns the levelAnalysisYAvg.
	 */
	public LevelAnalysisYAvg getLevelAnalysisYAvg() {
		return levelAnalysisYAvg;
	}

	/**
	 * Creates a full factorial set of FactorInteractions.
	 */
	public void createFullFactorialInteractions() {
		factorInteractionBuilder.createFullFactorialInteractions();
		fireDataEvent(null, DataEvent.FACTORINTERACTIONS_STRUCT_CHANGED);
	}

	/**
	 * Creates a full factorial set of Runs.
	 * FIXME: externalize this to a creation class.
	 */
	public void createRuns() {
		// for all factor/level combinations:
		ArrayList<Pair<Factor,Integer>> workspace = 
			new ArrayList<Pair<Factor,Integer>>();
		runs.clear();
		createRunAtLevel(workspace, 0, factors.size());
		fireDataEvent(null, DataEvent.RUNS_STRUCT_CHANGED);
	}

	/**
	 * helper class.
	 * @param workspace
	 * @param level
	 * @param length
	 */
	private void createRunAtLevel(
			ArrayList<Pair<Factor, Integer>> workspace,
			int level, int length) {
		if (workspace.size()==length) {
			Run run = new Run(factors, factorInteractions);
			for (Pair<Factor, Integer> pair : workspace) {
				run.setLevel(pair.getFirst(), pair.getSecond());
			}
			run.updateFactorInteractionLevels();
			run.setId(String.valueOf(runs.size()+1));
			runs.add(run);
		} else {
			workspace.add(
				new Pair<Factor,Integer>(
						factors.get(level),
						Integer.valueOf(-1)));
			createRunAtLevel(workspace, level+1, length);
			workspace.remove(workspace.size()-1);

			workspace.add(
				new Pair<Factor,Integer>(
						factors.get(level),
						Integer.valueOf(+1)));
			createRunAtLevel(workspace, level+1, length);
			workspace.remove(workspace.size()-1);
		}
	}

	/**
	 * Creates a un-initialized set of ExpResults.
	 * FIXME: check whether this needs to be externalized.
	 */
	public void createExpResults() {
		expResults.clear();
		for (int i = 0; i < runs.size(); i++) {
			expResults.add(new ExpResults(replicateCount));
		}
		fireDataEvent(null, DataEvent.EXPRESULTS_STRUCT_CHANGED);
	}

	/**
	 * Helper class for testing.
	 */
	public void createRandomResults() {
		for (ExpResults er : expResults) {
			for (int i=0; i<er.getReplicateCount(); i++) {
				er.setResult(
						i,
						Double.valueOf(
								Math.round(Math.random()*100)/20.0));
			}
		}
		fireDataEvent(null, DataEvent.EXPRESULTS_DATA_CHANGED);
	}

	/**
	 * Calculate (the average and sigma) values of the exp results.
	 * The actual calculations are done in ExpResults.
	 */
	public void calculateYS() {
		for (ExpResults er : expResults) {
			er.calculate();
		}
		fireDataEvent(null, DataEvent.EXPRESULTS_DATA_CHANGED);
	}


	/**
	 * Calculate the level analysis.
	 */
	public void calculateLA() {
		levelAnalysisYAvg.calculate();
		levelAnalysisSigma.calculate();
		fireDataEvent(null, DataEvent.LEVELANALYSIS_DATA_CHANGED);
	}

	// ///////////////////////////////////////////////////
	// event listener handling
	// ///////////////////////////////////////////////////

	/**
	 * @param l
	 * @see de.admadic.calculator.modules.indxp.core.DataEventServer#addDataListener(de.admadic.calculator.modules.indxp.core.DataEventListener)
	 */
	public void addDataListener(DataEventListener l) {
	    listenerList.add(DataEventListener.class, l);
	 }

	/**
	 * @param l
	 * @see de.admadic.calculator.modules.indxp.core.DataEventServer#removeDataListener(de.admadic.calculator.modules.indxp.core.DataEventListener)
	 */
	public void removeDataListener(DataEventListener l) {
		listenerList.remove(DataEventListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance 
	 * is lazily created using the parameters passed into 
	 * the fire method.
	 * @param source 
	 * @param mask 
	 */	
	protected void fireDataEvent(Object source, int mask) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		DataEvent e = null;
		if (source==null) source = this;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==DataEventListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new DataEvent(source, mask);
				((DataEventListener)listeners[i+1]).dataEventSignalled(e);
			}
		}
	}

	/**
	 * @param source
	 * @param mask
	 * @see de.admadic.calculator.modules.indxp.core.DataEventDispatcher#notifyEvent(java.lang.Object, int)
	 */
	public void notifyEvent(Object source, int mask) {
		fireDataEvent(source, mask);
	}


	/**
	 * @param e
	 * @see de.admadic.calculator.modules.indxp.core.DataEventListener#dataEventSignalled(de.admadic.calculator.modules.indxp.core.DataEvent)
	 */
	public void dataEventSignalled(DataEvent e) {
		if (e.getMask()!=0) {
			// something changed.
			setDirty(true);
		}
	}

	/**
	 * @return Returns the dirty.
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @param dirty The dirty to set.
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * @return Returns the fileName.
	 */
	public File getFileName() {
		return fileName;
	}

	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(File fileName) {
		this.fileName = fileName;
	}

}
