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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Rainer Schwarze
 *
 */
public class FSM {
	public static final int STATUS_NONE = 0;
	public static final int STATUS_RUN = 1;
	public static final int STATUS_ACCEPT = 2;
	public static final int STATUS_ERROR = 3;
	
	// definition of the FSM:
	Hashtable<String,Event> events;
	Hashtable<String,State> states;
	Hashtable<String,Transition> transitions;
	State startStateRef;
	State errorStateRef;
	Hashtable<String,State> endStatesRef;
	// hash( source-state -> hash( event -> transition ) )
	Hashtable <String,Hashtable<String, Transition>> transitionsFunction;
	LinkedList<Event> eventQueue;

	// running the FSM:
	State currentState = null;
	int status = STATUS_NONE;

	//
	ActionListener standardActionListener = null;
	LinkedList<ActionListener> acceptorListeners = null;
	LinkedList<ActionListener> errorListeners = null;

	public FSM() {
		initMembers();
	}

	public void initMembers() {
		events = new Hashtable<String,Event>();
		states = new Hashtable<String,State>();
		transitions = new Hashtable<String,Transition>();
		startStateRef = null;
		endStatesRef = new Hashtable<String,State>();
		transitionsFunction = new Hashtable <String,Hashtable<String, Transition>>();
		standardActionListener = null;
		acceptorListeners = null;
		status = STATUS_NONE;
		eventQueue = new LinkedList<Event>();
	}

	public void registerStandardActionListener(ActionListener al) {
		standardActionListener = al;
	}
	public void addAcceptorListener(ActionListener al) {
		if (acceptorListeners==null) {
			acceptorListeners = new LinkedList<ActionListener>(); 
		}
		acceptorListeners.add(al);
	}
	public void addErrorListener(ActionListener al) {
		if (errorListeners==null) {
			errorListeners = new LinkedList<ActionListener>(); 
		}
		errorListeners.add(al);
	}
	
	public void addEvent(Event event) {
		events.put(event.getName(), event);
	}
	public void addState(State state) {
		if (standardActionListener!=null)
			state.addActionListener(standardActionListener);
		states.put(state.getName(), state);
	}
	public void addTransition(Transition transition) {
		if (standardActionListener!=null)
			transition.addActionListener(standardActionListener);
		transitions.put(transition.getName(), transition);
	}
	public State getState(String name) {
		return states.get(name);
	}
	public Event getEvent(String name) {
		return events.get(name);
	}
	public Transition getTransition(String name) {
		return transitions.get(name);
	}

	public void setStartState(String name) {
		State state = states.get(name);
		startStateRef = state;
	}
	public void setErrorState(String name) {
		State state = states.get(name);
		errorStateRef = state;
	}
	public void addEndState(String name) {
		State state = states.get(name);
		endStatesRef.put(name, state);
	}
	public boolean isEndState(State state) {
		if (endStatesRef.get(state.getName())!=null) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isErrorState(State state) {
		if (errorStateRef==null) return false;
		if (errorStateRef.getName().equals(state.getName())) {
			return true;
		} else {
			return false;
		}
	}

	public void buildTransitionFunction() {
		Iterator<Transition> it = transitions.values().iterator();
		Transition tr, trdst;
		Hashtable<String,Transition> eventMap;

		while (it.hasNext()) {
			tr = it.next();

			eventMap = transitionsFunction.get(tr.getSourceState().getName());
			if (eventMap==null) {
				eventMap = new Hashtable<String,Transition>();
				transitionsFunction.put(
						tr.getSourceState().getName(), 
						eventMap);
			}

			eventMap.put(tr.getEvent().getName(), tr);
		}
	}

	public void fsmStart() {
		fsmEnterState(startStateRef, null);
		status = STATUS_RUN;
	}

	public void fsmAddEvent(String name) {
		Event event = events.get(name);
		if (event==null) {
			System.err.println("addEventToQueue: no such event: " + name);
			return;
		}
		eventQueue.add(event);
	}

	public void fsmDoEvent(String name) {
		fsmAddEvent(name);
		fsmProcessEventQueue(10);
/*
 		Event event = events.get(name);
		fsmDoEvent(event);
*/
	}

	public boolean fsmIsEventQueueEmpty() {
		return eventQueue.isEmpty();
	}

	public void fsmProcessEventQueue(int maxevents) {
		Event event;
		for (int i=0; i<maxevents; i++) {
			event = eventQueue.poll();
			if (event==null) break;

			fsmDoEvent(event);
		}
	}

	public void fsmDoEvent(Event event) {
		State srcstate = null;
		State dststate = null;
		Transition trans = null;

		srcstate = currentState;
		trans = findTransition(srcstate, event);
		if (trans==null) {
			System.out.println("Error: could not find transition for ("+
					srcstate.getName() + ")-[" + event.getName() + "]->");
			return;
		}
		dststate = trans.getDestinationState();

		if (!fsmLeaveState(srcstate, event)) {
			//currentState = srcstate;
			return;
		}
		if (!fsmTransit(trans, event)) return;
		if (!fsmEnterState(dststate, event)) return;
	}

	public Transition findTransition(State srcState, Event event) {
		// hash( source-state -> hash( event -> transition ) )
		// Hashtable <String,Hashtable<String, Transition>> transitionsFunction;
		Transition trans = null;
		Hashtable<String,Transition> eventmap = null;
		eventmap = transitionsFunction.get(srcState.getName());
		if (eventmap==null) return null;
		trans = eventmap.get(event.getName());
		return trans;
	}

	public void updateActionTalkBack(ActionTalkBack atb) {
		if (atb.nextEvent==null) {
			if (atb.nextEventName!=null) {
				if (!atb.nextEventName.equals("")) {
					atb.nextEvent = events.get(atb.nextEventName);
				}
			}
		}
		if (atb.nextState==null) {
			if (atb.nextStateName!=null) {
				if (!atb.nextStateName.equals("")) {
					atb.nextState = states.get(atb.nextStateName);
				}
			}
		}
	}

	public boolean fsmEnterState(State state, Event event) {
		{ // pre check for enter:
			Iterator<ActionListener> it;
			it = state.getActionListenerIterator();
			if (it==null) {
				// nothing to do
			} else {
				Action action;
				ActionTalkBack atb;
				ActionListener al;

				action = new Action(Action.STATE_ENTER, null, event, state, null);
				while (it.hasNext()) {
					al = it.next();
					atb = new ActionTalkBack();
					al.actionPerformed(action, atb);
					if (!atb.resultSuccess) {
						updateActionTalkBack(atb);
						if (atb.nextEvent!=null) {
							fsmAddEvent(atb.nextEvent.getName());
						}
						if (atb.nextState!=null) {
							fsmEnterState(atb.nextState, null);
							//	NOTE: we *must* return here, because the rest of
							//	this method shall not be executed!
							//	otherwise the currentState is reset to the 
							//	originally passed state here.
							return false;
						}
					}
				}
			}
		}
		// actually store the new state:
		currentState = state;

		if (isEndState(state)) {
			status = STATUS_ACCEPT;
			Iterator<ActionListener> it = null;
			if (acceptorListeners!=null)
				it = acceptorListeners.iterator();
			if (it==null) {
				// nothing to do
			} else {
				Action action;
				ActionTalkBack atb;
				ActionListener al;

				action = new Action(Action.FSM_ACCEPT, null, event, state, null);
				while (it.hasNext()) {
					al = it.next();
					atb = new ActionTalkBack();
					al.actionPerformed(action, atb);
					if (!atb.resultSuccess) {
						// ? what now?
						// because we already reached the end state
						// we do not process any items.
						System.err.println("Warning: ActionTalkBack with error in an EndState!");
					}
				}
			}
		}
		if (isErrorState(state)) {
			status = STATUS_ERROR;
			Iterator<ActionListener> it = null;
			if (errorListeners!=null)
				it = errorListeners.iterator();
			if (it==null) {
				// nothing to do
			} else {
				Action action;
				ActionTalkBack atb;
				ActionListener al;

				action = new Action(Action.FSM_ERROR, null, event, state, null);
				while (it.hasNext()) {
					al = it.next();
					atb = new ActionTalkBack();
					al.actionPerformed(action, atb);
					if (!atb.resultSuccess) {
						// ? what now?
						// because we reached the error state
						// we do not process any items.
						System.err.println("Warning: ActionTalkBack with error in an ErrorState!");
					}
				}
			}
		}
		return true;
	}

	public boolean fsmLeaveState(State state, Event event) {
		{ // pre checks for leave:
			Iterator<ActionListener> it;
			it = state.getActionListenerIterator();
			if (it==null) {
				// nothing to do
			} else {
				Action action;
				ActionTalkBack atb;
				ActionListener al;

				action = new Action(Action.STATE_LEAVE, state, event, null, null);
				while (it.hasNext()) {
					al = it.next();
					atb = new ActionTalkBack();
					al.actionPerformed(action, atb);
					if (!atb.resultSuccess) {
						updateActionTalkBack(atb);
						if (atb.nextEvent!=null) {
							fsmAddEvent(atb.nextEvent.getName());
						}
						if (atb.nextState!=null) {
							fsmEnterState(atb.nextState, null);
							//	NOTE: we *must* return here, because the rest of
							//	this method shall not be executed!
							//	otherwise the currentState is reset to the 
							//	originally passed state here.
							return false;
						}
						if (atb.nextEvent!=null) {
							// allow the state to be processed.
							// if thats not the case, then now skip
							return false;
						}
					}
				}
			}
		}
		//currentState = null;
		return true;
	}

	public boolean fsmTransit(Transition trans, Event event) {
		{
			Iterator<ActionListener> it;
			it = trans.getActionListenerIterator();
			if (it==null) {
				// nothing to do
			} else {
				Action action;
				ActionTalkBack atb;
				ActionListener al;

				action = new Action(
						Action.STATE_TRANSIT, 
						trans.getSourceState(), 
						trans.getEvent(), 
						trans.getDestinationState(), 
						trans);
				while (it.hasNext()) {
					al = it.next();
					atb = new ActionTalkBack();
					al.actionPerformed(action, atb);
					if (!atb.resultSuccess) {
						//currentState = trans.sourceState;
						updateActionTalkBack(atb);
						if (atb.nextEvent!=null) {
							fsmAddEvent(atb.nextEvent.getName());
						}
						if (atb.nextState!=null) {
							fsmEnterState(atb.nextState, null);
							//	NOTE: we *must* return here, because the rest of
							//	this method shall not be executed!
							//	otherwise the currentState is reset to the 
							//	originally passed state here.
							return false;
						}
						return false;
					}
				}
			}
		}
		// currentState = state;
		return true;
	}
}
