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
package de.admadic.calculator.processor;

import java.util.Stack;

import javax.swing.event.EventListenerList;

import de.admadic.calculator.math.DMath;
import de.admadic.calculator.types.CaDouble;

/**
 * @author Rainer Schwarze
 *
 */
public class SimpleProcessorV1 implements IProcessor {

	final static boolean DBG = true;
	
	// FIXME: validate function visibility and event firing
	// status: event firing is ok right now (21.Sep.2005) -- rsc
	EventListenerList listenerList = new EventListenerList();

	// state:
	CaDouble acc = null;
	CaDouble accmem = null;
	int maxinputlength = 17;
	int displaywidth = 20;
	int sign = +1;
	String inputstr = null;
	String lastop = null;
	String lastcmd = null;
	boolean appendToInput = true;
	boolean hasNewInput = true;
	boolean accChanged = false;
	String logstr = null;
	Stack<String> cmdStack = null;
	Stack<CaDouble> accStack = null;

	
	// ///////////////////////////////////////////////////
	// public access methods
	// ///////////////////////////////////////////////////

	/**
	 * Creates an instance of a SimpleProcessor.
	 */
	public SimpleProcessorV1() {
		if (DBG) System.out.println("SimpleProcessor: <init>");
		acc = new CaDouble();
		accmem = new CaDouble();
		accStack = new Stack<CaDouble>();
		cmdStack = new Stack<String>();
		appendToInput = true;
		hasNewInput = true;
		accChanged = false;
		inputstr = "0";
		lastop = "";
		logstr = ""; 
		lastcmd = "";
	}

	// ///////////////////////////////////////////////////
	// public status retrieval methods
	// ///////////////////////////////////////////////////

	/**
	 * @return Returns accu value
	 * @throws ProcessorException
	 */
	public CaDouble getAccuValue() throws ProcessorException {
		try {
			return acc.clone();
		} catch (CloneNotSupportedException e) {
			// e.printStackTrace();
			throw new ProcessorException(
					"calculateStack: clone failed."+
					" check Number implementation.");
		}
	}

	/**
	 * @return	Returns the display value of the accu plus potential status
	 * information.
	 * @see de.admadic.calculator.processor.IProcessor#getDisplay()
	 */
	public String getDisplay() {
		String res = "";
		// inputstring
		if (appendToInput) {
			res += (sign<0) ? "-"+inputstr : " "+inputstr;
		} else {
			res += getAccDisplay();
		}
		//res += "\n" + getStatusDisplay();
		return res + " ";
	}

	/**
	 * @return	Returns the display value of the accu.
	 */
	public String getAccDisplay() {
		if (acc.isNotNormal()) {
			return acc.getStateString();
		}
		/*
		return acc.toString();
		*/
		String res = acc.toString(0); //displaywidth);
		//String res = String.format("%12.2f", acc.getValue());
		return res;
	}

	/**
	 * @return	Returns the protocol
	 * @see de.admadic.calculator.processor.IProcessor#getLogDisplay()
	 */
	public String getLogDisplay() {
		return logstr;
	}

	/**
	 * @return	Returns a string representation of the processor status.
	 */
	public String getStatusDisplay() {
		return " " + getMemDisplay() + " "; // + getStackDisplay();
	}

	/**
	 * @return	Returns a string representation of the memory status.
	 * @see de.admadic.calculator.processor.IProcessor#getMemDisplay()
	 */
	public String getMemDisplay() {
		return (testMemDisplay() ? "M" : " ");
	}

	/**
	 * @return	Returns true, if the memory is used.
	 */
	public boolean testMemDisplay() {
		if (accmem.getValue()!=0.0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return	Returns a string representation of the stack status.
	 * @see de.admadic.calculator.processor.IProcessor#getStackDisplay()
	 */
	public String getStackDisplay() {
		return "Stack: |acc|=" + accStack.size() + " |cmd|=" + cmdStack.size() + "";
	}

	// ///////////////////////////////////////////////////
	// event listener handling
	// ///////////////////////////////////////////////////

	 /**
	 * @param l
	 * @see de.admadic.calculator.processor.IProcessor#addProcessorListener(de.admadic.calculator.processor.ProcessorEventListener)
	 */
	public void addProcessorListener(ProcessorEventListener l) {
		if (DBG) System.out.println("SimpleProcessor: addProcessorListener");
	    listenerList.add(ProcessorEventListener.class, l);
	 }

	 /**
	 * @param l
	 * @see de.admadic.calculator.processor.IProcessor#removeProcessorListener(de.admadic.calculator.processor.ProcessorEventListener)
	 */
	public void removeProcessorListener(ProcessorEventListener l) {
		if (DBG) System.out.println("SimpleProcessor: removeProcessorListener");
		listenerList.remove(ProcessorEventListener.class, l);
	}

	// ///////////////////////////////////////////////////
	// public processor command dispatch interface
	// ///////////////////////////////////////////////////

	/**
	 * @param cmd
	 * @throws ProcessorException
	 * @see de.admadic.calculator.processor.IProcessor#processCommand(java.lang.String)
	 */
	public void processCommand(String cmd) throws ProcessorException {
		if (DBG) System.out.println("SimpleProcessor: processCommand: " + cmd);
		if (
				cmd.equals(ProcessorAction.PA_BACKSPACE) ||
				cmd.equals(ProcessorAction.PA_0) ||
				cmd.equals(ProcessorAction.PA_1) ||
				cmd.equals(ProcessorAction.PA_2) ||
				cmd.equals(ProcessorAction.PA_3) ||
				cmd.equals(ProcessorAction.PA_4) ||
				cmd.equals(ProcessorAction.PA_5) ||
				cmd.equals(ProcessorAction.PA_6) ||
				cmd.equals(ProcessorAction.PA_7) ||
				cmd.equals(ProcessorAction.PA_8) ||
				cmd.equals(ProcessorAction.PA_9) ||
				cmd.equals(ProcessorAction.PA_SIGN) ||
				cmd.equals(ProcessorAction.PA_DOT)
			) {
			updateInputString(cmd);
		}
		if (
				cmd.equals(ProcessorAction.PA_ADD) ||	
				cmd.equals(ProcessorAction.PA_SUB) ||	
				cmd.equals(ProcessorAction.PA_MUL) ||	
				cmd.equals(ProcessorAction.PA_DIV)	
			) {
			opBinary(cmd);
		}
		if (
				cmd.equals(ProcessorAction.PA_M_SQR) ||	
				cmd.equals(ProcessorAction.PA_M_SQRT) ||	
				cmd.equals(ProcessorAction.PA_M_LN) ||	
				cmd.equals(ProcessorAction.PA_M_LOG) ||	
				cmd.equals(ProcessorAction.PA_M_EXP) ||	
				cmd.equals(ProcessorAction.PA_M_EXP10) ||	
				cmd.equals(ProcessorAction.PA_M_XINV) ||	
				cmd.equals(ProcessorAction.PA_M_SIN) ||	
				cmd.equals(ProcessorAction.PA_M_COS) ||	
				cmd.equals(ProcessorAction.PA_M_TAN) ||	
				cmd.equals(ProcessorAction.PA_M_COT)	
				//cmd.equals(ProcessorAction.PA_M_POW) ||	
				//cmd.equals(ProcessorAction.PA_M_X_RT)	
			) {
			opUnary(cmd);
		}
		if (
				cmd.equals(ProcessorAction.PA_EXE)	
			) {
			opExecute();
		}
		if (
				cmd.equals(ProcessorAction.PA_M_PI) ||	
				cmd.equals(ProcessorAction.PA_M_E)	
			) {
			opConstant(cmd);
		}
		if (cmd.equals(ProcessorAction.PA_CLR_ALL)) {
			clear();
			appendLog(ProcessorAction.PA_CLR_ALL);
		}
		if (cmd.equals(ProcessorAction.PA_CLR_ENTRY)) {
			clearEntry();
		}
		if (cmd.equals(ProcessorAction.PA_MEM_CLR)) {
			clearMem();
		}
		if (cmd.equals(ProcessorAction.PA_LOG_CLR)) {
			clearLog();
		}
		if (cmd.equals(ProcessorAction.PA_LOG_CLRLINE)) {
			clearLastLogLine();
		}
		if (cmd.equals(ProcessorAction.PA_MEM_ADD)) {
			memoryPlus();
		}
		if (cmd.equals(ProcessorAction.PA_MEM_SUB)) {
			memoryMinus();
		}
		if (cmd.equals(ProcessorAction.PA_MEM_STORE)) {
			memoryStore();
		}
		if (cmd.equals(ProcessorAction.PA_MEM_READ)) {
			memoryRead();
		}
		if (DBG) System.out.println("SimpleProcessor: lastcmd <- " + cmd);
		lastcmd = cmd;
	}

	// ///////////////////////////////////////////////////
	// public processor command interface (programmatical access)
	// ///////////////////////////////////////////////////
	
	/**
	 * Completely clears the internal states and values of this SimpleProcessor.
	 */
	public void clear() {
		if (DBG) System.out.println("SimpleProcessor: clear()");
		acc.setZero();
		accStack.clear();
		cmdStack.clear();
		appendToInput = true;
		hasNewInput = true;
		accChanged = false;
		inputstr = "0";
		sign = +1;
		lastop = "";
		lastcmd = "";
		fireDisplayEvent();
		fireStatusEvent(ProcessorEvent.STACK);
	}

	/**
	 * Clear the memory storage.
	 */
	public void clearMem() {
		if (DBG) System.out.println("SimpleProcessor: clearMem()");
		accmem.setZero();
		fireStatusEvent(ProcessorEvent.MEMORY);
	}

	/**
	 * Clear the protocol of this SimpleProcessor. 
	 */
	public void clearLog() {
		if (DBG) System.out.println("SimpleProcessor: clearLog()");
		logstr = "";
		fireProtocolEvent();
	}

	/**
	 * Clears the last line of the protocol.
	 */
	public void clearLastLogLine() {
		if (DBG) System.out.println("SimpleProcessor: clearLastLogLine()");
		int i = logstr.lastIndexOf("\n");
		if (i<0) {
			logstr = "";
		} else {
			logstr = logstr.substring(0, i);
		}
		fireProtocolEvent();
	}

	/**
	 * Execute the "CE" command.
	 */
	public void clearEntry() {
		if (DBG) System.out.println("SimpleProcessor: clearEntry()");
		acc.setZero();
		inputstr = "0";
		sign = +1;
		appendToInput = true;
		hasNewInput = true;
		fireDisplayEvent();
	}


	/**
	 * Add the accu to the memory space.
	 */
	public void memoryPlus() {
		if (DBG) System.out.println("SimpleProcessor: memoryPlus()");
		setAccuFromInput();
		DMath.add(accmem, acc);
		fireStatusEvent(ProcessorEvent.MEMORY);
	}

	/**
	 * Subtract the accu from the memory space.
	 */
	public void memoryMinus() {
		if (DBG) System.out.println("SimpleProcessor: memoryMinus()");
		setAccuFromInput();
		DMath.sub(accmem, acc);
		fireStatusEvent(ProcessorEvent.MEMORY);
	}

	/**
	 * Store the accu to the memory space.
	 */
	public void memoryStore() {
		if (DBG) System.out.println("SimpleProcessor: memoryStore()");
		setAccuFromInput();
		acc.cloneTo(accmem);
		fireStatusEvent(ProcessorEvent.MEMORY);
	}

	/**
	 * Read the accu from the memory space.
	 */
	public void memoryRead() {
		if (DBG) System.out.println("SimpleProcessor: clearRead()");
		accmem.cloneTo(acc);
		hasNewInput = true;
		getAccu();
		// no fireEvent, because memory does not change
	}

	
	// ///////////////////////////////////////////////////
	// internal processor operation
	// ///////////////////////////////////////////////////

	/**
	 * Update the input string with the specified command.
	 * @param cmd
	 */
	protected void updateInputString(String cmd) {
		if (DBG) System.out.println("SimpleProcessor: updateInputString(): " + cmd);
		hasNewInput = true;
		if (!appendToInput) {
			if (DBG) System.out.println("SimpleProcessor: updateInputString(): appendToInput was false, reset to '0'");
			inputstr = "0";
			appendToInput = true;
		}

		if (cmd.equals(ProcessorAction.PA_BACKSPACE)) {
			if (inputstr.equals("0")) {
				if (DBG) System.out.println("SimpleProcessor: updateInputString(): backspace on '0', ignored");
				// nothing
			} else {
				if (DBG) System.out.println("SimpleProcessor: updateInputString(): backspace: clearing last digit");
				inputstr = inputstr.substring(0, inputstr.length()-1);
				if (inputstr.length()<1) {
					inputstr = "0";
				}
			}
		}

		if (cmd.equals(ProcessorAction.PA_DOT)) {
			if (inputstr.indexOf(".")>=0) {
				if (DBG) System.out.println("SimpleProcessor: updateInputString(): dot: dot already there");
				// ignore
			} else {
				if (DBG) System.out.println("SimpleProcessor: updateInputString(): dot: appending");
				if (inputstr.length()<maxinputlength) inputstr += ".";
			}
		}

		if (cmd.equals(ProcessorAction.PA_SIGN)) {
			if (DBG) System.out.println("SimpleProcessor: updateInputString(): toggle sign");
			sign = -sign;
		}

		if (cmd.equals("ex")) { // exponent?
			// not implemented!
		}

		if (
				cmd.equals(ProcessorAction.PA_0) ||
				cmd.equals(ProcessorAction.PA_1) ||
				cmd.equals(ProcessorAction.PA_2) ||
				cmd.equals(ProcessorAction.PA_3) ||
				cmd.equals(ProcessorAction.PA_4) ||
				cmd.equals(ProcessorAction.PA_5) ||
				cmd.equals(ProcessorAction.PA_6) ||
				cmd.equals(ProcessorAction.PA_7) ||
				cmd.equals(ProcessorAction.PA_8) ||
				cmd.equals(ProcessorAction.PA_9)
			) {
			if (inputstr.equals("0")) {
				if (DBG) System.out.println("SimpleProcessor: updateInputString(): 0..9: replace pure 0");
				inputstr = cmd;
			} else {
				if (DBG) System.out.println("SimpleProcessor: updateInputString(): 0..9: append");
				if (inputstr.length()<maxinputlength) inputstr += cmd;
			}
		}
		fireDisplayEvent();
	}

	/**
	 * Process the given binary operation.
	 * 
	 * @param cmd
	 * @throws ProcessorException
	 */
	protected void opBinary(String cmd) throws ProcessorException {
		if (DBG) System.out.println("SimpleProcessor: opBinary(): " + cmd);
		if (
				lastcmd.equals(ProcessorAction.PA_ADD) ||
				lastcmd.equals(ProcessorAction.PA_SUB) ||
				lastcmd.equals(ProcessorAction.PA_MUL) ||
				lastcmd.equals(ProcessorAction.PA_DIV)
			) {
			// we only change the lastop - everything else stays the same
			popCmd();
			pushCmd(cmd);
			lastop = cmd;
			if (DBG) System.out.println("SimpleProcessor: updateBinary(): replacing last op to " + cmd);
			return;
		}
		setAccuFromInput();
		if (accStack.size()<1) {
			if (DBG) System.out.println("SimpleProcessor: updateBinary(): accStack empty, making log");
			appendLog("", acc);
		}
		if (DBG) System.out.println("SimpleProcessor: updateBinary(): storing acc and calculating if necessary...");
		pushAccu(acc);
		calculateStack();
		if (DBG) System.out.println("SimpleProcessor: updateBinary(): pushing cmd on stack: " + cmd);
		pushCmd(cmd);
		appendToInput = false;
//		calculate();
		lastop = cmd;
		if (DBG) System.out.println("SimpleProcessor: updateBinary(): lastop <- " + cmd);
		fireDisplayEvent();
	}

	/**
	 * Process the given unary operation.
	 * 
	 * @param cmd
	 * @throws ProcessorException
	 */
	protected void opUnary(String cmd) throws ProcessorException {
		if (DBG) System.out.println("SimpleProcessor: opUnary(): " + cmd);
//		if (
//				lastcmd.equals(ProcessorAction.PA_ADD) ||
//				lastcmd.equals(ProcessorAction.PA_SUB) ||
//				lastcmd.equals(ProcessorAction.PA_MUL) ||
//				lastcmd.equals(ProcessorAction.PA_DIV)
//			) {
//			// we only change the lastop - everything else stays the same
//			popCmd();
//			pushCmd(cmd);
//			lastop = cmd;
//			return;
//		}
		setAccuFromInput();
		if (accStack.size()<1) {
			if (DBG) System.out.println("SimpleProcessor: opUnary(): accStack empty, appending log entry");
			appendLog("", acc);
		}
		if (DBG) System.out.println("SimpleProcessor: opUnary(): push acc to stack");
		pushAccu(acc);
//		calculateStack();
		if (DBG) System.out.println("SimpleProcessor: opUnary(): push cmd to stack");
		pushCmd(cmd);
		if (DBG) System.out.println("SimpleProcessor: opUnary(): calculating");
		calculateStack();
		appendToInput = false;
//		calculate();
		if (DBG) System.out.println("SimpleProcessor: opUnary(): lastop <- " + cmd);
		lastop = cmd;
		fireDisplayEvent();
	}

	/**
	 * @param cmd
	 * @throws ProcessorException 
	 */
	protected void opConstant(String cmd) throws ProcessorException {
		if (DBG) System.out.println("SimpleProcessor: opConstant(): " + cmd);
		if (cmd.equals(ProcessorAction.PA_M_E)) {
			double v = Math.E;
			acc.setValue(v);
			if (DBG) System.out.println("SimpleProcessor: opConstant(): pushing constant on acc stack");
			pushAccu(acc);
		} else if (cmd.equals(ProcessorAction.PA_M_PI)) {
			double v = Math.PI;
			acc.setValue(v);
			if (DBG) System.out.println("SimpleProcessor: opConstant(): pushing constant on acc stack");
			pushAccu(acc);
		} else {
			if (DBG) System.out.println("SimpleProcessor: opConstant(): unknown constant");
			return; // unknown!
		}
		appendToInput = false;
		if (DBG) System.out.println("SimpleProcessor: opConstant(): lastop <- " + cmd);
		lastop = cmd;
		fireDisplayEvent();
	}

	/**
	 * Do the "EXE" command.
	 * 
	 * @throws ProcessorException
	 */
	protected void opExecute() throws ProcessorException {
		if (DBG) System.out.println("SimpleProcessor: opExecute(): ");
		if (
				lastcmd.equals(ProcessorAction.PA_ADD) ||
				lastcmd.equals(ProcessorAction.PA_SUB) ||
				lastcmd.equals(ProcessorAction.PA_MUL) ||
				lastcmd.equals(ProcessorAction.PA_DIV)
				) {
			popCmd();
			//pushCmd("=");
			lastop = "";
		} else {
			setAccuFromInput();
			if (accStack.size()<1) {
				appendLog("", acc);
			}
			pushAccu(acc);
			calculateStack();
		}

		pushCmd(ProcessorAction.PA_EXE);
		while (!cmdStack.isEmpty()) {
			calculateStack();
		}

		appendToInput = false;
		//calculate();
		lastop = "";
		//appendLog("=");
		fireDisplayEvent();
	}

	/**
	 * Processes the stack as much as possible
	 * @throws ProcessorException
	 */
	protected void calculateStack() throws ProcessorException {
		// check stack: we need 2 accus and 1 cmd
		CaDouble acc1;
		CaDouble acc2;
		String cmd;
		if (cmdStack.size()>=1) {
			cmd = popCmd();
			if (cmd.equals(ProcessorAction.PA_ADD)) {
				acc2 = popAccu();
				acc1 = popAccu();
				appendLog(cmd, acc2);
				DMath.add(acc1, acc2);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_SUB)) {
				acc2 = popAccu();
				acc1 = popAccu();
				appendLog(cmd, acc2);
				DMath.sub(acc1, acc2);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_MUL)) {
				acc2 = popAccu();
				acc1 = popAccu();
				appendLog(cmd, acc2);
				DMath.mul(acc1, acc2);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_DIV)) {
				acc2 = popAccu();
				acc1 = popAccu();
				appendLog(cmd, acc2);
				DMath.div(acc1, acc2);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_SQR)) {
				acc1 = popAccu();
				appendLog(cmd, acc1);
				DMath.sqr(acc1);
				setAccu(acc1);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_SQRT)) {
				acc1 = popAccu();
				appendLog(cmd, acc1);
				DMath.sqrt(acc1);
				setAccu(acc1);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_LN)) {
				acc1 = popAccu();
				appendLog(cmd, acc1);
				DMath.ln(acc1);
				setAccu(acc1);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_LOG)) {
				acc1 = popAccu();
				appendLog(cmd, acc1);
				DMath.log(acc1);
				setAccu(acc1);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_EXP)) {
				acc1 = popAccu();
				appendLog(cmd, acc1);
				DMath.exp(acc1);
				setAccu(acc1);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_EXP10)) {
				acc1 = popAccu();
				appendLog(cmd, acc1);
				DMath.exp10(acc1);
				setAccu(acc1);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_XINV)) {
				acc1 = popAccu();
				appendLog(cmd, acc1);
				DMath.invx(acc1);
				setAccu(acc1);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_SIN)) {
				acc1 = popAccu();
				appendLog(cmd, acc1);
				DMath.sin(acc1);
				setAccu(acc1);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_COS)) {
				acc1 = popAccu();
				appendLog(cmd, acc1);
				DMath.cos(acc1);
				setAccu(acc1);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_TAN)) {
				acc1 = popAccu();
				appendLog(cmd, acc1);
				DMath.tan(acc1);
				setAccu(acc1);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_COT)) {
				acc1 = popAccu();
				appendLog(cmd, acc1);
				DMath.cot(acc1);
				setAccu(acc1);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_POW)) {
				acc2 = popAccu();
				acc1 = popAccu();
				appendLog(cmd, acc2);
				DMath.expx(acc1, acc2);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_M_X_RT)) {
				acc2 = popAccu();
				acc1 = popAccu();
				appendLog(cmd, acc2);
				DMath.sqrtx(acc1, acc2);
				pushAccu(acc1);
			} else
			if (cmd.equals(ProcessorAction.PA_EXE)) {
				acc2 = popAccu();
				appendLog(cmd, acc2);
				try {
					acc = acc2.clone();
				} catch (CloneNotSupportedException e) {
					// e.printStackTrace();
					throw new ProcessorException(
							"calculateStack: clone failed."+
							" check Number implementation.");
				}
			} else {
				// unsupported cmd
			}
		}
	}

	// ///////////////////////////////////////////////////
	// protected internal engine functions
	// ///////////////////////////////////////////////////

	/**
	 * Set the accu from the input string
	 */
	protected void setAccuFromInput() {
		double v;
		if (!hasNewInput) return;
		v = Double.valueOf(inputstr).doubleValue();
		if (sign<0) v = -v;
		acc = new CaDouble(v);
		appendToInput = false;
		hasNewInput = false;
		accChanged = true;
		//System.out.println("acc=" + acc.toString());
		// FIXME: maybe fire?
	}

	/**
	 * @param cd
	 * @throws ProcessorException
	 */
	protected void setAccu(CaDouble cd) throws ProcessorException {
		try {
			acc = cd.clone();
			appendToInput = false;
			hasNewInput = false;
			accChanged = true;
			//System.out.println("acc=" + acc.toString());
			// FIXME: maybe fire?
		} catch (CloneNotSupportedException e) {
			// e.printStackTrace();
			throw new ProcessorException(
					"calculateStack: clone failed."+
					" check Number implementation.");
		}
	}

	/**
	 * Set the input string with the value from the Accu 
	 */
	protected void getAccu() {
		inputstr = acc.toString();
		if (inputstr.charAt(0)=='-') {
			sign = -1;
			inputstr = inputstr.substring(1, inputstr.length()-1);
		} else {
			sign = +1;
		}
		//appendToInput = true;
		hasNewInput = true;
		// FIXME: maybe fire?
	}

	/**
	 * Push the value to the accu stack.
	 * @param v
	 * @throws ProcessorException
	 */
	protected void pushAccu(CaDouble v) throws ProcessorException {
		try {
			accStack.push(v.clone());
			fireStatusEvent(ProcessorEvent.STACK);
		} catch (CloneNotSupportedException e) {
			// e.printStackTrace();
			throw new ProcessorException("pushAccu: v.clone() threw CloneNotSupportedException. Check implementation of Number types.");
		}
	}

	/**
	 * @return	Returns the top value from the accu stack
	 */
	protected CaDouble popAccu() {
		if (accStack.isEmpty()) {
			return null;
		} else {
			CaDouble cd = accStack.pop();
			fireStatusEvent(ProcessorEvent.STACK);
			return cd;
		}
	}

	/**
	 * Push the given command to the command stack
	 * @param cmd
	 */
	protected void pushCmd(String cmd) {
		cmdStack.push(cmd);
		fireStatusEvent(ProcessorEvent.STACK);
	}

	/**
	 * @return	Returns the top value from the accu stack
	 */
	protected String popCmd() {
		if (cmdStack.isEmpty()) {
			return null;
		} else {
			String s = cmdStack.pop();
			fireStatusEvent(ProcessorEvent.STACK);
			return s;
		}
	}

	/**
	 * Append the accu to the protocol with the specified command.
	 * @param cmd
	 */
	protected void appendLog(String cmd) {
		appendLog(cmd, acc);
	}

	/**
	 * Append the value and the command to the protocol.
	 *
	 * @param cmd
	 * @param accu
	 */
	protected void appendLog(String cmd, CaDouble accu) {
		String cmddisp = cmd;
		if (cmd.equals(ProcessorAction.PA_CLR_ALL)) {
			logstr += "\n.clear\n";
			fireProtocolEvent();
			return;
		}
		if (cmd.equals("")) {
			cmddisp = ".";
		}
		if (cmd.equals(ProcessorAction.PA_EXE)) {
			logstr += "\n"+"======================";
		}
		logstr += "\n" + cmddisp + " " + accu.toString(displaywidth - cmddisp.length() + 1);
		if (cmd.equals(ProcessorAction.PA_EXE)) {
			logstr += "\n";
		}
		fireProtocolEvent();
	}

	
	/**
	 * FIXME: either this should be protected, or the other fire methods
	 * should also be public.
	 * Fire all events which are Display, Status(memory,stack), Protocol 
	 */
	public void fireAll() {
		fireDisplayEvent();
		fireStatusEvent(ProcessorEvent.MEMORY | ProcessorEvent.STACK);
		fireProtocolEvent();
	}
	
	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance 
	 * is lazily created using the parameters passed into 
	 * the fire method.
	 */	
	protected void fireDisplayEvent() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ProcessorEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==ProcessorEventListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new ProcessorEvent(this);
				((ProcessorEventListener)listeners[i+1]).passedDisplayEvent(e);
			}
		}
	}

	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance 
	 * is lazily created using the parameters passed into 
	 * the fire method.
	 * @param mask 
	 */	
	protected void fireStatusEvent(int mask) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ProcessorEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==ProcessorEventListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new ProcessorEvent(this);
				e.setMask(mask);
				((ProcessorEventListener)listeners[i+1]).passedStatusEvent(e);
			}
		}
	}

	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance 
	 * is lazily created using the parameters passed into 
	 * the fire method.
	 */	
	protected void fireProtocolEvent() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ProcessorEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==ProcessorEventListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new ProcessorEvent(this);
				((ProcessorEventListener)listeners[i+1]).passedProtocolEvent(e);
			}
		}
	}


	// ///////////////////////////////////////////
	// methods added to keep compatibility in project
	// ///////////////////////////////////////////
	/**
	 * @param l
	 * @see de.admadic.calculator.processor.IProcessor#addProtocolListener(de.admadic.calculator.processor.ProtocolEventListener)
	 */
	public void addProtocolListener(ProtocolEventListener l) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param l
	 * @see de.admadic.calculator.processor.IProcessor#removeProtocolListener(de.admadic.calculator.processor.ProtocolEventListener)
	 */
	public void removeProtocolListener(ProtocolEventListener l) {
		// TODO Auto-generated method stub
		
	}
}
