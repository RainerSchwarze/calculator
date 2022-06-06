/**
 *
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
 */
package de.admadic.calculator.fsm;


/**
 * @author Rainer Schwarze
 *
 */
public class Test {
	/**
	 * Test cases:
	 *             +-4-+
	 *             |   |
	 *              \ /
	 * ((1))  -1->  (2)  -1->  (3)  -1->  ((4))
	 *            |  |                      |
	 *            |  +----2--> (5) --2->----+
	 *            |             |
	 *            +----<-3-<----+
	 *
	 *              (e)  (= error)
	 */
	FSM fsm = null;
	int testctr;

	/**
	 * 
	 */
	public void initFSM() {
		fsm = new FSM();
		testctr = 0;

		fsm.registerStandardActionListener(new ActionListener() {
			public void actionPerformed(Action action, ActionTalkBack atb) {
				actionLog(action);
			}
		});
		fsm.addAcceptorListener(new ActionListener() {
			public void actionPerformed(Action action, ActionTalkBack atb) {
				System.out.println("ACCEPT: " + action.toString());
			}
		});

		fsm.addState(new State("1"));
		fsm.addState(new State("2"));
		fsm.addState(new State("3"));
		fsm.addState(new State("4"));
		fsm.addState(new State("5"));
		fsm.addState(new State("e"));

		fsm.addEndState("4");
		fsm.setStartState("1");

		fsm.addEvent(new Event("1"));
		fsm.addEvent(new Event("2"));
		fsm.addEvent(new Event("3"));
		fsm.addEvent(new Event("4"));

		fsm.addTransition(new Transition(
				"1", fsm.getState("1"), fsm.getEvent("1"), fsm.getState("2")));
		fsm.addTransition(new Transition(
				"2", fsm.getState("2"), fsm.getEvent("1"), fsm.getState("3")));
		fsm.addTransition(new Transition(
				"3", fsm.getState("3"), fsm.getEvent("1"), fsm.getState("4")));
		fsm.addTransition(new Transition(
				"4", fsm.getState("2"), fsm.getEvent("2"), fsm.getState("5")));
		fsm.addTransition(new Transition(
				"5", fsm.getState("5"), fsm.getEvent("2"), fsm.getState("4")));
		fsm.addTransition(new Transition(
				"6", fsm.getState("5"), fsm.getEvent("3"), fsm.getState("2")));
		fsm.addTransition(new Transition(
				"7", fsm.getState("2"), fsm.getEvent("4"), fsm.getState("2")));

		fsm.getTransition("2").addActionListener(new ActionListener() {
			public void actionPerformed(Action action, ActionTalkBack atb) {
				if (
						(action.getAction()==Action.STATE_TRANSIT) &&
						(action.getSourceState().getName().equals("2"))
					) {
					if (testctr==0) {
						testctr++;
						System.out.println("!!!! I don't like that. -> new Event");
						atb.setNextEventName("4");
					}
				}
			}
		});

		fsm.buildTransitionFunction();
	}

	/**
	 * 
	 */
	public void runFSM() {
		String [] ev = {"1", "2", "3", "2", "3", "1", "1"};
		System.out.println("starting FSM run:");
		fsm.fsmStart();
		for (int i=0; i<ev.length; i++) {
			fsm.fsmDoEvent(ev[i]);
		}
		System.out.println("FSM finished.");
	}

	/**
	 * @param action
	 */
	public void actionLog(Action action) {
		System.out.println("action: " + action.toString());
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Test test = new Test();
		System.out.println("FSM Test driver");
		test.initFSM();
		test.runFSM();
	}

}
