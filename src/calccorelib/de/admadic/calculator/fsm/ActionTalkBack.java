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
package de.admadic.calculator.fsm;

/**
 * @author Rainer Schwarze
 *
 * Specifies things to do when an ActionListener returns.
 */
public class ActionTalkBack {
	String statusMsg;		// the message of the listener
	boolean resultSuccess = true;	// true if success
	// if not success, these fields are checked:
	// (only one of them should be !null)
	State nextState;		// if ! null, the next state
	String nextStateName;	// -"-
	Event nextEvent;		// if ! null, the next event
	String nextEventName;	// -"-
	/**
	 * @return Returns the resultSuccess.
	 */
	public boolean isResultSuccess() {
		return resultSuccess;
	}
	/**
	 * @param resultSuccess The resultSuccess to set.
	 */
	public void setResultSuccess(boolean resultSuccess) {
		this.resultSuccess = resultSuccess;
	}
	/**
	 * @return Returns the statusMsg.
	 */
	public String getStatusMsg() {
		return statusMsg;
	}
	/**
	 * @param statusMsg The statusMsg to set.
	 */
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	/**
	 * @param nextEvent The nextEvent to set.
	 */
	public void setNextEvent(Event nextEvent) {
		this.nextEvent = nextEvent;
		resultSuccess = false;
	}
	/**
	 * @param nextEventName The nextEventName to set.
	 */
	public void setNextEventName(String nextEventName) {
		this.nextEventName = nextEventName;
		resultSuccess = false;
	}
	/**
	 * @param nextState The nextState to set.
	 */
	public void setNextState(State nextState) {
		this.nextState = nextState;
		resultSuccess = false;
	}
	/**
	 * @param nextStateName The nextStateName to set.
	 */
	public void setNextStateName(String nextStateName) {
		this.nextStateName = nextStateName;
		resultSuccess = false;
	}
}
