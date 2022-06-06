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
 */
public class Action {
	public final static int UNDEFINED = 0;
	public final static int STATE_LEAVE = 1;
	public final static int STATE_TRANSIT = 2;
	public final static int STATE_ENTER = 3;
	public final static int FSM_ACCEPT = 4;
	public final static int FSM_ERROR = 5;

	int action = UNDEFINED;
	State sourceState = null;
	Event event = null;
	State destinationState = null;
	Transition transition = null;
	/**
	 * @param action
	 * @param state
	 * @param event
	 * @param state2
	 * @param transition
	 */
	public Action(
			int action, 
			State sourceState, 
			Event event, 
			State destinationState, 
			Transition transition) {
		super();
		this.action = action;
		this.sourceState = sourceState;
		this.event = event;
		this.destinationState = destinationState;
		this.transition = transition;
	}

	public String toString() {
		String s = "";
		switch (action) {
		case UNDEFINED: 	
			s += "(Undef) "; 
			break;
		case STATE_ENTER: 	
			s += "(Enter) ";
			s += "->(" + ((destinationState==null) ? "null" : destinationState.getName()) + ")";
			break;
		case STATE_TRANSIT: 
			s += "(Trans) "; 
			s += "(" + ((sourceState==null) ? "null" : sourceState.getName()) + ")-";
			s += "[" + ((event==null) ? "null" : event.getName()) + "]->";
			s += "(" + ((destinationState==null) ? "null" : destinationState.getName()) + ") ";
			s += " <-: " + ((transition==null) ? "null" : transition.getName());
			break;
		case STATE_LEAVE: 	
			s += "(Leave) "; 
			s += "(" + ((sourceState==null) ? "null" : sourceState.getName()) + ")->";
			break;
		case FSM_ACCEPT: 	
			s += "(Accpt) "; 
			s += "->((" + ((destinationState==null) ? "null" : destinationState.getName()) + "))";
			break;
		case FSM_ERROR: 	
			s += "(Error) "; 
			s += "->[" + ((destinationState==null) ? "null" : destinationState.getName()) + "]";
			break;
		default:
			s += "(?????) "; break;
		}
		return s;
	}
	
	/**
	 * @return Returns the action.
	 */
	public int getAction() {
		return action;
	}
	/**
	 * @return Returns the destinationState.
	 */
	public State getDestinationState() {
		return destinationState;
	}
	/**
	 * @return Returns the event.
	 */
	public Event getEvent() {
		return event;
	}
	/**
	 * @return Returns the sourceState.
	 */
	public State getSourceState() {
		return sourceState;
	}
	/**
	 * @return Returns the transition.
	 */
	public Transition getTransition() {
		return transition;
	}
}
