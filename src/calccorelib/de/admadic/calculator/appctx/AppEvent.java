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
package de.admadic.calculator.appctx;

import java.util.EventObject;

/**
 * @author Rainer Schwarze
 *
 */
public class AppEvent extends EventObject {
	/** initialization of application */
	final static public int PHASE_INIT_CORE = 1;
	/** initialization of application frame */
	final static public int PHASE_INIT_FRAME = 2;
	/** activation of application frame */
	final static public int PHASE_SHOW_FRAME = 3;
	/** showing phase of application */
	final static public int PHASE_SHOW_ALL = 4;
	/** running phase of application */
	final static public int PHASE_RUN = 5;
	/** query for exit */
	final static public int PHASE_TRY_EXIT = 6;
	/** force exit */
	final static public int PHASE_EXIT = 7;

	int phase;
	IAppContext appContext;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param source
	 */
	public AppEvent(Object source) {
		this(source, null);
	}

	/**
	 * @param source
	 * @param appContext 
	 */
	public AppEvent(Object source, IAppContext appContext) {
		super(source);
		this.appContext = appContext;
	}

	/**
	 * @return	Returns a String representation of the Instance.
	 * @see java.util.EventObject#toString()
	 */
	@Override
	public String toString() {
		String s = super.toString();
		s += "[" + "phase=" + phaseToString(this.phase) + "]";
		return s;
	}

	/**
	 * @param phase
	 * @return	Returns a String representation of the given phase id.
	 */
	public static String phaseToString(int phase) {
		String s = "";
		switch (phase) {
		case PHASE_INIT_CORE: s += "INIT_CORE"; break;
		case PHASE_INIT_FRAME: s += "INIT_FRAME"; break;
		case PHASE_SHOW_FRAME: s += "SHOW_FRAME"; break;
		case PHASE_SHOW_ALL: s += "SHOW_ALL"; break;
		case PHASE_RUN: s += "RUN"; break;
		case PHASE_TRY_EXIT: s += "TRY_EXIT"; break;
		case PHASE_EXIT: s += "EXIT"; break;
		default:
			s += "(unknown:" + phase + ")";
		}
		return s;
	}
	

	/**
	 * @return Returns the appContext.
	 */
	public IAppContext getAppContext() {
		return appContext;
	}

	/**
	 * @param appContext The appContext to set.
	 */
	public void setAppContext(IAppContext appContext) {
		this.appContext = appContext;
	}

	/**
	 * @return Returns the phase.
	 */
	public int getPhase() {
		return phase;
	}

	/**
	 * @param phase The phase to set.
	 */
	public void setPhase(int phase) {
		this.phase = phase;
	}
}
