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

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Rainer Schwarze
 *
 */
public class Transition {
	String name = null;
	State sourceState = null;
	Event event = null;
	State destinationState = null;
	// transit actions:
	LinkedList<ActionListener> actionListeners = null;

	public Transition(String name) {
		this.name = name;
	}

	public Transition(String name, State sourceState, Event event, State destinationState) {
		setName(name);
		setTransition(sourceState, event, destinationState);
	}

	public void addActionListener(ActionListener al) {
		if (al==null) return;
		if (actionListeners==null) {
			actionListeners = new LinkedList<ActionListener>();
		}
		actionListeners.add(al);
	}

	public Iterator<ActionListener> getActionListenerIterator() {
		if (actionListeners==null) return null;
		return actionListeners.iterator();
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
	 * @param sourceState The sourceState to set.
	 * @param event The event to set.
	 * @param destinationState The destinationState to set.
	 */
	public void setTransition(State sourceState, Event event, State destinationState) {
		this.sourceState = sourceState;
		this.event = event;
		this.destinationState = destinationState;
	}
	/**
	 * @return Returns the destinationState.
	 */
	public State getDestinationState() {
		return destinationState;
	}
	/**
	 * @param destinationState The destinationState to set.
	 */
	public void setDestinationState(State destinationState) {
		this.destinationState = destinationState;
	}
	/**
	 * @return Returns the sourceState.
	 */
	public State getSourceState() {
		return sourceState;
	}
	/**
	 * @param sourceState The sourceState to set.
	 */
	public void setSourceState(State sourceState) {
		this.sourceState = sourceState;
	}
	/**
	 * @return Returns the event.
	 */
	public Event getEvent() {
		return event;
	}
	/**
	 * @param event The event to set.
	 */
	public void setEvent(Event event) {
		this.event = event;
	}
}
