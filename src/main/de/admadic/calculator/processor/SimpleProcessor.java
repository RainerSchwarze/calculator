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

import com.graphbuilder.math.Expression;
import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.ExpressionTree;
import com.graphbuilder.math.FuncMap;
import com.graphbuilder.math.VarMap;

import de.admadic.calculator.core.LocaleEvent;
import de.admadic.calculator.core.LocaleListener;
import de.admadic.calculator.core.LocaleNumberFormatEvent;
import de.admadic.calculator.core.LocaleProvider;
import de.admadic.calculator.math.DMath;
import de.admadic.calculator.processor.func.*;
import de.admadic.calculator.types.CaDouble;
import de.admadic.calculator.types.CaDoubleFormat;
import de.admadic.calculator.types.CaNumber;
import de.admadic.util.StringUtil;

/**
 * @author Rainer Schwarze
 *
 */
public class SimpleProcessor 
implements IProcessor, IProcessorCalculation, LocaleListener {

	final static boolean DBG = false;

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static class ActionStack {
		private Stack<Object> actionStack = null;
		private CaDouble accuRef;
		private SimpleProcessor procRef;
		private CaDouble lastSubResultRef;

		/**
		 * Creates an empty instance of an ActionStack.
		 * @param proc 
		 */
		public ActionStack(SimpleProcessor proc) {
			super();
			this.procRef = proc;
			actionStack = new Stack<Object>();
			accuRef = new CaDouble();
			actionStack.push(accuRef);
			lastSubResultRef = accuRef;
		}

		/**
		 * @return	Returns the internal action stack object.
		 */
		public Stack<Object> getActionStack() {
			return actionStack;
		}

		/**
		 * Pushes the given operation onto the action stack.
		 * @param op
		 */
		public void pushOp(String op) {
			actionStack.push(op);
			procRef.fireStatusEvent(ProcessorEvent.STACK);
		}

		/**
		 * Pushes the given value onto the action stack.
		 * @param v
		 */
		public void pushValue(CaDouble v) {
			actionStack.push(v);
			procRef.fireStatusEvent(ProcessorEvent.STACK);
		}

		/**
		 * Pushes the given action onto the action stack.
		 * @param o
		 */
		public void pushAction(Object o) {
			actionStack.push(o);
			procRef.fireStatusEvent(ProcessorEvent.STACK);
		}

		/**
		 * @return	Returns true, if the stack is empty. 
		 */
		public boolean isEmpty() {
			return actionStack.isEmpty();
		}
		
		/**
		 * @return	Returns the value from the top of the stack, or null, 
		 * if the stack is empty.
		 */
		public Object pop() {
			if (actionStack.isEmpty()) {
				return null;
			} else {
				Object o = actionStack.pop();
				procRef.fireStatusEvent(ProcessorEvent.STACK);
				return o;
			}
		}

		/**
		 * @return	Returns the value from the top of the stack, or null, 
		 * if the stack is empty.
		 */
		public Object peek() {
			if (actionStack.isEmpty()) {
				return null;
			} else {
				return actionStack.peek();
			}
		}

		/**
		 * @return	Returns the current accumulator.
		 */
		public CaDouble getAccu() {
			return accuRef;
		}

		/**
		 * @return	Returns the size of the action stack.
		 */
		public int size() {
			return actionStack.size();
		}

		/**
		 * Clears the action stack.
		 */
		public void clear() {
			actionStack.clear();
			accuRef = new CaDouble();
			actionStack.push(accuRef);
			lastSubResultRef = accuRef;
			procRef.fireStatusEvent(ProcessorEvent.STACK);
		}

		/**
		 * Prepare a new accu for input.
		 * If the stack has size 1, the only entry is reused, otherwise
		 * a new value is pushed to the stack.
		 */
		public void newAccuForInput() {
			if (actionStack.size()==1) {
				if (DBG) System.out.println("SimpleProcessor: updateInputString(): stack=1, reusing");
				// only accu left, replace it:
				accuRef.setZero();
			} else {
				if (DBG) System.out.println("SimpleProcessor: updateInputString(): stack>1, new element");
				accuRef = new CaDouble();
				actionStack.push(accuRef);
			}
			procRef.fireStatusEvent(ProcessorEvent.STACK);
		}

		/**
		 * @param idx
		 * @return	Returns the elements at the given index.
		 */
		public Object elementAt(int idx) {
			return actionStack.elementAt(idx);
		}

		/**
		 * @param idx
		 * @return	Returns the next value on the stack below the given index.
		 */
		public int getNextValueIdx(int idx) {
			int vidx = actionStack.size()-1;
			if (idx>=0) {
				vidx = idx;
			}
			Object o;
			while (vidx>=0) {
				o = actionStack.elementAt(vidx);
				if (o instanceof CaDouble) {
					break;
				}
				vidx--;
			}
			return vidx;
		}

		/**
		 * @param idx
		 * @return	Returns the next op on the stack below the given index.
		 */
		public int getNextOpIdx(int idx) {
			int oidx = actionStack.size()-1;
			if (idx>=0) {
				oidx = idx;
			}
			Object o;
			while (oidx>=0) {
				o = actionStack.elementAt(oidx);
				if (o instanceof String) {
					break;
				}
				oidx--;
			}
			return oidx;
		}

		/**
		 * @param idx
		 */
		public void removeElementAt(int idx) {
			Object o = actionStack.elementAt(idx);
			if (o==accuRef) {
				int ni = getNextValueIdx(idx-1);
				if (ni<0) {
					throw new Error("The single accu is being removed!");
				}
				accuRef = (CaDouble)actionStack.elementAt(ni);
				if (DBG) System.out.println("ActionStack: accu removed from " + idx + ", reset @ " + ni);
			}
			if (o==lastSubResultRef) {
				if (DBG) System.out.println("ActionStack: lastSubResult removed from " + idx);
				lastSubResultRef = null;
			}
			actionStack.removeElementAt(idx);
			procRef.fireStatusEvent(ProcessorEvent.STACK);
		}

		/**
		 * @return	Returns true, if the TOS element is CaNumber.
		 */
		public boolean isTOSActionNum() {
			if (!actionStack.isEmpty()) {
				Object o = actionStack.peek();
				if (o instanceof CaNumber) {
					return true;
				}
			}
			return false;
		}

		/**
		 * @return	Returns true, if the TOS element is String.
		 */
		public boolean isTOSActionOp() {
			if (!actionStack.isEmpty()) {
				Object o = actionStack.peek();
				if (o instanceof String) {
					return true;
				}
			}
			return false;
		}

		/**
		 * @return Returns the lastSubResultRef.
		 */
		public CaDouble getLastSubResult() {
			return lastSubResultRef;
		}

		/**
		 * @param lastSubResultRef The lastSubResultRef to set.
		 */
		public void setLastSubResult(CaDouble lastSubResultRef) {
			this.lastSubResultRef = lastSubResultRef;
		}
	}

	// FIXME: validate function visibility and event firing
	// status: event firing is ok right now (21.Sep.2005) -- rsc
	// update: (05.11.2005) reworking engine code -> validation invalid
	EventListenerList listenerList = new EventListenerList();

	ProcessorActionAttributeManager paam;
	LocaleProvider localeProvider;

	// state:
	int maxinputlength = 17;
	int displaywidth = 20;
	int sign = +1;
	String inputstr = null;
	boolean editable;
	boolean logStart = true;
	boolean logStartNotForOp = false;
	final static int LOGSTART_SCOPE_INPUT = 0;
	final static int LOGSTART_SCOPE_OP = 1;

	CaDoubleFormat numberFormat;

	CaDouble memory = null;

	String logstr = null;

	ActionStack actionStack = null;
	VarMap stdVarMap;
	FuncMap stdFuncMap;

	/** angular arguents in rad (pi/2) , @since calccore 1.0.2 */
	public final static int ANGULARARG_RAD = 0;
	/** angular arguents in deg (90) , @since calccore 1.0.2  */
	public final static int ANGULARARG_DEG = 1;
	/** angular arguents in gra (100) , @since calccore 1.0.2  */
	public final static int ANGULARARG_GRA = 2;

	/* @since calccore 1.0.2 */
	int angularArgMode = ANGULARARG_RAD;
	
//	Stack<String> cmdStack = null;
//	Stack<CaDouble> accStack = null;
//	boolean appendToInput = true;
//	boolean hasNewInput = true;
//	boolean accChanged = false;

	
	// ///////////////////////////////////////////////////
	// public access methods
	// ///////////////////////////////////////////////////

	/**
	 * Creates an instance of a SimpleProcessor.
	 */
	public SimpleProcessor() {
		if (DBG) System.out.println("SimpleProcessor: <init>");
		actionStack = new ActionStack(this);
		ProcessorContextA pc = new ProcessorContextA();
		pc.init();
		paam = pc.getPAAM();

		memory = new CaDouble();
		numberFormat = new CaDoubleFormat(CaDoubleFormat.TYPE_FIXED, 12, 2);

		inputstr = "0";
		editable = true;

		logstr = ""; 
		initVarFuncMaps();

//		accStack = new Stack<CaDouble>();
//		cmdStack = new Stack<String>();
//		acc = new CaDouble();
//		appendToInput = true;
//		hasNewInput = true;
//		accChanged = false;

//		lastop = "";
//		lastcmd = "";
	}

	/**
	 * 
	 */
	private void initVarFuncMaps() {
		stdVarMap = new VarMap();
		stdFuncMap = new FuncMap();

		stdFuncMap.setFunction(ProcessorAction.PA_M_SQR, new FuncSqr());
		stdFuncMap.setFunction(ProcessorAction.PA_M_SQRT, new FuncSqrt());
		stdFuncMap.setFunction(ProcessorAction.PA_M_LN, new FuncLn());
		stdFuncMap.setFunction(ProcessorAction.PA_M_LOG, new FuncLog());
		stdFuncMap.setFunction(ProcessorAction.PA_M_EXP, new FuncExp());
		stdFuncMap.setFunction(ProcessorAction.PA_M_EXP10, new FuncExp10());
		stdFuncMap.setFunction(ProcessorAction.PA_M_XINV, new FuncXinv());
		stdFuncMap.setFunction(ProcessorAction.PA_M_SIN, new FuncSin());
		stdFuncMap.setFunction(ProcessorAction.PA_M_COS, new FuncCos());
		stdFuncMap.setFunction(ProcessorAction.PA_M_TAN, new FuncTan());
		stdFuncMap.setFunction(ProcessorAction.PA_M_COT, new FuncCot());
		stdFuncMap.setFunction(ProcessorAction.PA_M_ARCSIN, new FuncArcSin());
		stdFuncMap.setFunction(ProcessorAction.PA_M_ARCCOS, new FuncArcCos());
		stdFuncMap.setFunction(ProcessorAction.PA_M_ARCTAN, new FuncArcTan());
		stdFuncMap.setFunction(ProcessorAction.PA_M_ARCCOT, new FuncArcCot());
		stdFuncMap.setFunction(ProcessorAction.PA_M_SINH, new FuncSinh());
		stdFuncMap.setFunction(ProcessorAction.PA_M_COSH, new FuncCosh());
		stdFuncMap.setFunction(ProcessorAction.PA_M_TANH, new FuncTanh());
		stdFuncMap.setFunction(ProcessorAction.PA_M_COTH, new FuncCoth());
		stdFuncMap.setFunction(ProcessorAction.PA_M_ARSINH, new FuncArSinh());
		stdFuncMap.setFunction(ProcessorAction.PA_M_ARCOSH, new FuncArCosh());
		stdFuncMap.setFunction(ProcessorAction.PA_M_ARTANH, new FuncArTanh());
		stdFuncMap.setFunction(ProcessorAction.PA_M_ARCOTH, new FuncArCoth());
		stdFuncMap.setFunction(ProcessorAction.PA_M_SEC, new FuncSec());
		stdFuncMap.setFunction(ProcessorAction.PA_M_COSEC, new FuncCosec());
		stdFuncMap.setFunction(ProcessorAction.PA_M_PI, new FuncPi());
		stdFuncMap.setFunction(ProcessorAction.PA_M_E, new FuncE());
		stdFuncMap.setFunction(ProcessorAction.PA_M_GAMMA, new FuncGamma());
	}

	// ///////////////////////////////////////////////////
	// public status retrieval methods
	// ///////////////////////////////////////////////////

	/**
	 * @return Returns the localeProvider.
	 */
	public LocaleProvider getLocaleProvider() {
		return localeProvider;
	}

	/**
	 * @param localeProvider The localeProvider to set.
	 */
	public void setLocaleProvider(LocaleProvider localeProvider) {
		if (this.localeProvider!=null) {
			this.localeProvider.removeLocaleListener(this);
		}
		this.localeProvider = localeProvider;
		if (this.localeProvider!=null) {
			numberFormat.setLocale(localeProvider.getDefaultLocale());
			this.localeProvider.addLocaleListener(this);
		}
	}

	/**
	 * @return Returns accu value
	 * @throws ProcessorException
	 */
	public CaDouble getAccuValue() throws ProcessorException {
		try {
			return actionStack.getAccu().clone();
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
		if (editable) {
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
		CaDouble a = actionStack.getAccu();
		if (a.isNotNormal()) {
			return a.getStateString();
		}
		//String res = a.toString(0); //displaywidth);
		String res = numberFormat.format(a); //displaywidth);
		res.trim();
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
		if (memory.getValue()!=0.0) {
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
		return "Stack: |action|=" + actionStack.size();
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

	/**
	 * @param l
	 * @see de.admadic.calculator.processor.IProcessor#addProtocolListener(de.admadic.calculator.processor.ProtocolEventListener)
	 */
	public void addProtocolListener(ProtocolEventListener l) {
		if (DBG) System.out.println("SimpleProcessor: addProtocolListener");
	    listenerList.add(ProtocolEventListener.class, l);
	}

	/**
	 * @param l
	 * @see de.admadic.calculator.processor.IProcessor#removeProtocolListener(de.admadic.calculator.processor.ProtocolEventListener)
	 */
	public void removeProtocolListener(ProtocolEventListener l) {
		if (DBG) System.out.println("SimpleProcessor: removeProtocolListener");
		listenerList.remove(ProtocolEventListener.class, l);
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
		ProcessorActionAttribute paa;
		paa = paam.get(cmd);
		if (paa!=null) {
			if (cmd.equals(ProcessorAction.PA_PERCENT)) {
				opPercent(cmd);
			} else if (paa.getType()==ProcessorActionAttribute.TYPE_NUM) {
				updateInputString(cmd);
			} else if (
					paa.getType()==ProcessorActionAttribute.TYPE_OP && 
					paa.getOrder()==2) {
				opBinary(cmd);
			} else if (
					paa.getType()==ProcessorActionAttribute.TYPE_OP && 
					paa.getOrder()==1) {
				opUnary(cmd);
			} else if (
					paa.getType()==ProcessorActionAttribute.TYPE_OP && 
					paa.getOrder()==0) {
				opConstant(cmd);
			} else if (cmd.equals(ProcessorAction.PA_EXE)) {
				opExecute();
			}
			if (cmd.equals(ProcessorAction.PA_CLR_ALL)) {
				clear();
			}
			if (cmd.equals(ProcessorAction.PA_CLR_ENTRY)) {
				clearEntry();
			}
			if (cmd.equals(ProcessorAction.PA_MEM_CLR)) {
				memoryClear();
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
		} else {
			// nothing
			if (DBG) System.out.println("SimpleProcessor: command not in PAAM: " + cmd);
		}
	}

	// ///////////////////////////////////////////////////
	// public processor command interface (programmatical access)
	// ///////////////////////////////////////////////////
	
	/**
	 * Completely clears the internal states and values of this SimpleProcessor.
	 */
	public void clear() {
		if (DBG) System.out.println("SimpleProcessor: clear()");

		actionStack.clear();
		inputstr = "0";
		sign = +1;
		editable = true;

		appendLog(
				ProcessorAction.PA_CLR_ALL, null, 
				actionStack.getAccu(), 
				actionStack.getLastSubResult(),
				null);

		setLogStart(true);

		fireDisplayEvent();
		fireStatusEvent(ProcessorEvent.STACK);
	}

	/**
	 * Clear the memory storage.
	 */
	public void memoryClear() {
		if (DBG) System.out.println("SimpleProcessor: clearMem()");
		memory.setZero();
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
		if (actionStack.isTOSActionNum()) {
			//accuRef.setZero();
			inputstr = "0";
			sign = +1;
			editable = true;
			setAccuFromInput();
		} else if (actionStack.isTOSActionOp()) {
			// pop the command:
			actionStack.pop(); // discard the object
		} else {
			// nothing?
		}
		fireDisplayEvent();
	}


	/**
	 * Add the accu to the memory space.
	 */
	public void memoryPlus() {
		if (DBG) System.out.println("SimpleProcessor: memoryPlus()");
		if (editable) {
			closeInput();
			//setAccuFromInput();
		}
		DMath.add(memory, actionStack.getAccu());
		fireStatusEvent(ProcessorEvent.MEMORY);
	}

	/**
	 * Subtract the accu from the memory space.
	 */
	public void memoryMinus() {
		if (DBG) System.out.println("SimpleProcessor: memoryMinus()");
		if (editable) {
			closeInput();
			//setAccuFromInput();
		}
		DMath.sub(memory, actionStack.getAccu());
		fireStatusEvent(ProcessorEvent.MEMORY);
	}

	/**
	 * Store the accu to the memory space.
	 */
	public void memoryStore() {
		if (DBG) System.out.println("SimpleProcessor: memoryStore()");
		if (editable) {
			closeInput();
			//setAccuFromInput();
		}
		actionStack.getAccu().cloneTo(memory);
		fireStatusEvent(ProcessorEvent.MEMORY);
	}

	/**
	 * Read the accu from the memory space.
	 */
	public void memoryRead() {
		if (DBG) System.out.println("SimpleProcessor: clearRead()");
		if (actionStack.isTOSActionOp()) {
			actionStack.newAccuForInput();
		}
		memory.cloneTo(actionStack.getAccu());
		getAccuToInput();

		// already in getAccuToInput
		// closeInput(); //editable = false;
		// no fireEvent, because memory does not change

		fireDisplayEvent();
	}

	// ///////////////////////////////////////////////////
	// core calculation interface:
	// ///////////////////////////////////////////////////
	/**
	 * @param subExpr
	 * @param result
	 * @param doLog 
	 * @throws ProcessorUnknownOpException 
	 * @see de.admadic.calculator.processor.IProcessorCalculation#calcSubExpr(java.lang.String, de.admadic.calculator.types.CaNumber)
	 */
	public void calcSubExprImpl(String subExpr, CaNumber result, boolean doLog) 
	throws ProcessorUnknownOpException {
		String exprS = subExpr.trim();
		if (exprS.contains("%")) {
			if (exprS.startsWith("ex")) {
				exprS = exprS.replaceAll("%", "");
				exprS = exprS.replaceAll("ex", "");
				exprS = exprS.replaceAll(":", "*");
				int pos = exprS.indexOf('*');
				if (pos<0) {
					throw new ProcessorUnknownOpException(
							"%-expression not supported: " + subExpr);
				}
				String exprS2 = exprS.substring(pos+1);
				exprS = exprS.substring(0, pos);
				exprS = "((" + exprS + ")*0.01)";
				exprS = exprS + " / (1.0 + " + exprS + ") * " + exprS2;
			} else {
				exprS = exprS.replaceAll("%", "*0.01");
			}
		}
		// System.out.println("subExpr: " + subExpr + " -> " + exprS);
		Expression expr;
		double v;
		try {
			expr = ExpressionTree.parse(exprS);
			v = expr.eval(stdVarMap, stdFuncMap);
		} catch (ExpressionParseException e) {
			throw new ProcessorUnknownOpException(
					"expression not supported (1): " + subExpr);
		} catch (RuntimeException e) {
			throw new ProcessorUnknownOpException(
					"expression not supported (2): " + subExpr);
		}
		((CaDouble)result).setValue(v);
	}

	/**
	 * @param op
	 * @param arg0
	 * @param arg1
	 * @param result
	 * @param doLog 
	 * @throws ProcessorUnknownOpException 
	 * @see de.admadic.calculator.processor.IProcessorCalculation#calcBinaryOp(java.lang.String, de.admadic.calculator.types.CaNumber, de.admadic.calculator.types.CaNumber, de.admadic.calculator.types.CaNumber)
	 */
	public void calcBinaryOpImpl(
			String op, CaNumber arg0, CaNumber arg1, CaNumber result,
			boolean doLog) 
	throws ProcessorUnknownOpException {
		if (result!=null) {
			arg0.cloneTo(result);
			// result = arg0;
			arg0 = result;
		}

		String cmd = op;
		CaDouble acc1 = (CaDouble)arg0;
		CaDouble acc2 = (CaDouble)arg1;

		CaDouble log1 = null;
		CaDouble log2 = null;
		CaDouble loglsr = null;
		String logcmd = null;
		String logsubexpr = null;
		try {
			log1 = acc1.clone();
			log2 = acc2.clone();
			logcmd = cmd;
			if (actionStack.getLastSubResult()!=null)
				loglsr = actionStack.getLastSubResult().clone();
		} catch (CloneNotSupportedException e) {
			// should never happen!
			e.printStackTrace();
		}
		
		if (cmd.equals(ProcessorAction.PA_ADD)) {
			DMath.add(acc1, acc2);
		} else
		if (cmd.equals(ProcessorAction.PA_SUB)) {
			DMath.sub(acc1, acc2);
		} else
		if (cmd.equals(ProcessorAction.PA_MUL)) {
			DMath.mul(acc1, acc2);
		} else
		if (cmd.equals(ProcessorAction.PA_DIV)) {
			DMath.div(acc1, acc2);
		} else
		if (cmd.equals(ProcessorAction.PA_M_POW)) {
			DMath.expx(acc1, acc2);
		} else
		if (cmd.equals(ProcessorAction.PA_M_X_RT)) {
			DMath.sqrtx(acc1, acc2);
		} else 
		if (cmd.equals(ProcessorAction.PA_PERCENT_ADD)) { 
			String tmp = acc2.toString();
			logsubexpr = tmp + "%"; //" \u00b7 " + acc1.toString(); 
			logcmd = "+"; // + logsubexpr;
			DMath.mul(acc2, new CaDouble(0.01));
			DMath.mul(acc2, acc1);
			acc2.cloneTo(log2);
			DMath.add(acc1, acc2);
			// appendLog("+" + tmp + "%", acc2);
		} else 
		if (cmd.equals(ProcessorAction.PA_PERCENT_SUB)) { 
			String tmp = acc2.toString();
			logsubexpr = tmp + "%"; //" \u00b7 " + acc1.toString();
			logcmd = "-"; // + logsubexpr;
			DMath.mul(acc2, new CaDouble(0.01));
			DMath.mul(acc2, acc1);
			acc2.cloneTo(log2);
			DMath.sub(acc1, acc2);
			// appendLog("-" + tmp + "%", acc2);
		} else 
		if (cmd.equals(ProcessorAction.PA_PERCENT_MUL)) { 
			String tmp = acc2.toString();
			logsubexpr = tmp + "%"; //" \u00b7 " + acc1.toString();
			logcmd = "*"; // + logsubexpr;
			DMath.mul(acc2, new CaDouble(0.01));
			acc2.cloneTo(log2);
			DMath.mul(acc1, acc2);
			// appendLog("*" + tmp + "%", acc2);
		} else 
		if (cmd.equals(ProcessorAction.PA_PERCENT_DIV)) { 
			String tmp = acc2.toString();
			logsubexpr = tmp + "%"; //" \u00b7 " + acc1.toString();
			logcmd = "/"; // + logsubexpr;
			DMath.mul(acc2, new CaDouble(0.01));
			acc2.cloneTo(log2);
			DMath.div(acc1, acc2);
			// appendLog("/" + tmp + "%", acc2);
		} else 
		if (cmd.equals(ProcessorAction.PA_PERCENT_EX)) { 
			String tmp = acc2.toString();
			logsubexpr = "ex " + tmp + "%"; //" : " + acc1.toString();
			logcmd = "-"; // + logsubexpr;
			CaDouble tmpa = new CaDouble();
			CaDouble tmpb = new CaDouble();
			acc2.cloneTo(tmpa);
			acc2.cloneTo(tmpb);
			DMath.mul(tmpa, new CaDouble(0.01));
			DMath.mul(tmpb, new CaDouble(0.01));
			DMath.add(tmpb, new CaDouble(1.0));
			DMath.div(tmpa, tmpb);
			// DMath.mul(acc2, tmpb);
			// DMath.mul(acc1, acc2);
			DMath.mul(tmpa, acc1);
			tmpa.cloneTo(log2);
			DMath.sub(acc1, tmpa);
			// appendLog("e" + tmp + "%", acc2);
		} else {
			if (DBG) System.out.println("SimpleProcessor: calcBinaryOp(): unsupported cmd " + cmd);
			throw new ProcessorUnknownOpException("calcBinaryOp(): unsupported cmd " + cmd);
		}
		// acc1 is now the new last subresult:
		if (doLog) {
			// do it only, if we are called internally - the doLog also 
			// signals, that we are called internally:
			actionStack.setLastSubResult(acc1);
		}

		if (doLog) {
			checkLogStart(log1, LOGSTART_SCOPE_OP);
//			if (isLogStart()) {
//				if (DBG) System.out.println("opBinary: logStart");
//				appendLog("", null, log1, null, null);
//				setLogStart(false);
//			}
			appendLog(logcmd, logsubexpr, log2, loglsr, null);
		}
	}

	/**
	 * @param op
	 * @param arg0
	 * @param result
	 * @param doLog 
	 * @throws ProcessorUnknownOpException 
	 * @see de.admadic.calculator.processor.IProcessorCalculation#calcUnaryOp(java.lang.String, de.admadic.calculator.types.CaNumber, de.admadic.calculator.types.CaNumber)
	 */
	public void calcUnaryOpImpl(
			String op, CaNumber arg0, CaNumber result, boolean doLog) 
	throws ProcessorUnknownOpException {
		if (result!=null) {
			arg0.cloneTo(result);
			// result = arg0;
			arg0 = result;
		}

		String cmd = op;
		CaDouble acc1 = (CaDouble)arg0;
		String logcmd = cmd;
		CaDouble log1 = new CaDouble();
		CaDouble loglsr = new CaDouble();
		acc1.cloneTo(log1);
		acc1.cloneTo(loglsr);
		// String logsubexpr = cmd + "(" + acc1.toString() + ")";
		String logsubexpr = "ans"; // special function for last result

		if (cmd.equals(ProcessorAction.PA_M_SQR)) {
			DMath.sqr(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_SQRT)) {
			DMath.sqrt(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_LN)) {
			DMath.ln(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_LOG)) {
			DMath.log(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_EXP)) {
			DMath.exp(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_EXP10)) {
			DMath.exp10(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_XINV)) {
			DMath.invx(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_SIN)) {
			acc1 = convertToRad(acc1);
			DMath.sin(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_COS)) {
			acc1 = convertToRad(acc1);
			DMath.cos(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_TAN)) {
			acc1 = convertToRad(acc1);
			DMath.tan(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_COT)) {
			acc1 = convertToRad(acc1);
			DMath.cot(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_ARCSIN)) {
			DMath.arcsin(acc1);
			acc1 = convertFromRad(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_ARCCOS)) {
			DMath.arccos(acc1);
			acc1 = convertFromRad(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_ARCTAN)) {
			DMath.arctan(acc1);
			acc1 = convertFromRad(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_ARCCOT)) {
			DMath.arccot(acc1);
			acc1 = convertFromRad(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_SINH)) {
			DMath.sinh(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_COSH)) {
			DMath.cosh(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_TANH)) {
			DMath.tanh(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_COTH)) {
			DMath.coth(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_ARSINH)) {
			DMath.arsinh(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_ARCOSH)) {
			DMath.arcosh(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_ARTANH)) {
			DMath.artanh(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_ARCOTH)) {
			DMath.arcoth(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_SEC)) {
			acc1 = convertToRad(acc1);
			DMath.sec(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_COSEC)) {
			acc1 = convertFromRad(acc1);
			DMath.cosec(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_M_GAMMA)) {
			DMath.gamma(acc1);
		} else
		if (cmd.equals(ProcessorAction.PA_PERCENT)) {
			String tmp = acc1.toString();
			logsubexpr = tmp + "%";
			DMath.mul(acc1, new CaDouble(0.01));
		} else {
			if (DBG) System.out.println("SimpleProcessor: calculateStack(): unsupported cmd " + cmd);
			throw new ProcessorUnknownOpException("calcUnaryOp(): unsupported cmd " + cmd);
		}

		if (doLog) {
			checkLogStart(log1, LOGSTART_SCOPE_OP);
//			if (isLogStart()) {
//				if (DBG) System.out.println("opUnary: logStart");
//				appendLog("", null, log1, null, null);
//				setLogStart(false);
//			}
			appendLog(logcmd, logsubexpr, acc1, loglsr, null);
		}
//		if (doLog) appendLog(cmd + "->", acc1);
	}

	/**
	 * @param op
	 * @param result
	 * @param doLog
	 * @throws ProcessorUnknownOpException
	 */
	public void calcConstantOpImpl(
			String op, CaNumber result, boolean doLog) 
	throws ProcessorUnknownOpException {
		String cmd = op;
		double v;
		if (cmd.equals(ProcessorAction.PA_M_E)) {
			v = Math.E;
		} else if (cmd.equals(ProcessorAction.PA_M_PI)) {
			v = Math.PI;
		} else {
			if (DBG) System.out.println("SimpleProcessor: opConstant(): unknown constant");
			throw new ProcessorUnknownOpException(
					"operation " + op + " is not supported.");
		}
		((CaDouble)result).setValue(v);
	}

	// ///////////////////////////////////////////////////
	// processor calculation interface:
	// ///////////////////////////////////////////////////

	/**
	 * @param cmd
	 * @return	Returns the order of the command.
	 * @throws ProcessorUnknownOpException
	 * @see de.admadic.calculator.processor.IProcessorCalculation#getOpOrder(java.lang.String)
	 */
	public int getOpOrder(String cmd) throws ProcessorUnknownOpException {
		int order = -1;
		ProcessorActionAttribute paa;
		paa = paam.get(cmd);
		if (paa==null) {
			throw new ProcessorUnknownOpException(
					"command " + cmd + " is unknown.");
		}
		order = paa.getOrder();
		return order;
	}

	/**
	 * @param subExpr
	 * @param result
	 * @throws ProcessorUnknownOpException 
	 * @see de.admadic.calculator.processor.IProcessorCalculation#calcSubExpr(java.lang.String, de.admadic.calculator.types.CaNumber)
	 */
	public void calcSubExpr(String subExpr, CaNumber result) 
	throws ProcessorUnknownOpException {
		calcSubExprImpl(subExpr, result, false);
	}

	/**
	 * @param op
	 * @param arg0
	 * @param arg1
	 * @param result
	 * @throws ProcessorUnknownOpException 
	 * @see de.admadic.calculator.processor.IProcessorCalculation#calcBinaryOp(java.lang.String, de.admadic.calculator.types.CaNumber, de.admadic.calculator.types.CaNumber, de.admadic.calculator.types.CaNumber)
	 */
	public void calcBinaryOp(
			String op, CaNumber arg0, CaNumber arg1, CaNumber result) 
	throws ProcessorUnknownOpException {
		calcBinaryOpImpl(op, arg0, arg1, result, false);
	}

	/**
	 * @param op
	 * @param arg0
	 * @param result
	 * @throws ProcessorUnknownOpException 
	 * @see de.admadic.calculator.processor.IProcessorCalculation#calcUnaryOp(java.lang.String, de.admadic.calculator.types.CaNumber, de.admadic.calculator.types.CaNumber)
	 */
	public void calcUnaryOp(String op, CaNumber arg0, CaNumber result) 
	throws ProcessorUnknownOpException {
		calcUnaryOpImpl(op, arg0, result, false);
	}

	/**
	 * @param op
	 * @param result
	 * @throws ProcessorUnknownOpException
	 * @see de.admadic.calculator.processor.IProcessorCalculation#calcConstantOp(java.lang.String, de.admadic.calculator.types.CaNumber)
	 */
	public void calcConstantOp(String op, CaNumber result) throws ProcessorUnknownOpException {
		calcConstantOpImpl(op, result, false);
	}

	
	// ///////////////////////////////////////////////////
	// internal processor operation
	// ///////////////////////////////////////////////////

	protected void closeInput() {
		editable = false;
		//appendLog(".", actionStack.getAccu());
		// get the last value and the op before that for display:
		int vi, oi;
		vi = actionStack.size()-1;
		vi = actionStack.getNextValueIdx(vi);
		oi = vi - 1;
		oi = actionStack.getNextOpIdx(oi);
		if (vi>=0) {
			String op = ".";	// new 
			CaDouble v = (CaDouble)actionStack.elementAt(vi);
			if (oi>=0) {
				op = (String)actionStack.elementAt(oi);
			}
			checkLogStart(v, LOGSTART_SCOPE_INPUT);
//			if (isLogStart()) {
//				if (DBG) System.out.println("closeInput: logStart");
//				appendLog("", null, v, null, null);
//				setLogStart(false);
//			}
			// FIXME: WAS LOG
			// appendLog(op, v);
		}
		
		fireStatusEvent(ProcessorEvent.STACK);
	}

	/**
	 * Update the input string with the specified command.
	 * @param cmd
	 */
	protected void updateInputString(String cmd) {
		if (DBG) System.out.println("SimpleProcessor: updateInputString(): " + cmd);

		if (!editable) {
			if (DBG) System.out.println("SimpleProcessor: updateInputString(): was not editable, starting new");
			// start new editing:
			sign = +1;
			inputstr = "0";
			actionStack.newAccuForInput();
			editable = true;
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

		// update accu:
		if (DBG) System.out.println("SimpleProcessor: updateInputString(): updating accu");
		setAccuFromInput();
		fireStatusEvent(ProcessorEvent.STACK);

		fireDisplayEvent();
	}

	/**
	 * Process the given binary operation.
	 * 
	 * @param cmd
	 */
	protected void opPercent(String cmd) {
		if (DBG) System.out.println("SimpleProcessor: opPercent(): " + cmd);

		// in any case, we make the accu non-editable:
		if (editable) {
			if (DBG) System.out.println("SimpleProcessor: opPercent(): editable<-false");
			closeInput(); //editable = false;
		}

		// if the last operation is a binary operation: replace it:
		Object lastAction = actionStack.peek();
		if (lastAction instanceof String) {
			String pa = (String)lastAction;
			ProcessorActionAttribute paa = paam.get(pa);
			if (paa!=null) {
				if (
						paa.getType()==ProcessorActionAttribute.TYPE_OP &&
						paa.getOrder()==2
					) {
					if (DBG) System.out.println(
							"SimpleProcessor: opPercent(): "+
							"replacing " + paa.getPa() + " with " + cmd);
					actionStack.pop();
					// replacing push comes as a standard op below:
				}
			}
		} // (if need to replace last op)
		actionStack.pushAction(cmd);
		// check for percent calculation replacement
		opPercentFixup();

		calculateStack();

		fireDisplayEvent();	// FIXME: move that into calculateStack
	}

	protected void opPercentFixup() {
		// stack must be:
		// ...[num0] [op] [num1] [%]
		// where [op] is [+] or [-]
		// do we have enough elements?
		if (actionStack.size()<4) {
			if (DBG) System.out.println(
					"SimpleProcessor: opPercentFixup(): too few elements");
			return;
		}

		int curidx = actionStack.size() - 1;
		Object opPerc = actionStack.elementAt(curidx);
		Object num1 = actionStack.elementAt(curidx-1);
		Object op = actionStack.elementAt(curidx-2);
		Object num0 = actionStack.elementAt(curidx-3);

		if (DBG) {
			System.out.println(
				"SimpleProcessor: opPercentFixup(): \n"+
				"opPerc = " + opPerc.toString() + "\n" +
				"num1 = " + num1.toString() + "\n" +
				"op = " + op.toString() + "\n" +
				"num0 = " + num0.toString());
		}
		
		if (
				!(opPerc instanceof String) || 
				!opPerc.equals(ProcessorAction.PA_PERCENT)) {
			if (DBG) System.out.println(
					"SimpleProcessor: opPercentFixup(): op not plain %");
			return;
		}
		if (!(num1 instanceof CaNumber)) {
			if (DBG) System.out.println(
					"SimpleProcessor: opPercentFixup(): num1 no number");
			return;
		}
		if (!(
				(op instanceof String) &&
				(
					op.equals(ProcessorAction.PA_ADD) || 
					op.equals(ProcessorAction.PA_SUB) ||
					op.equals(ProcessorAction.PA_MUL) ||
					op.equals(ProcessorAction.PA_DIV) ||
					op.equals(ProcessorAction.PA_PERCENT_EX)
				)
				)) {
			if (DBG) System.out.println(
					"SimpleProcessor: opPercentFixup(): op not + or -");
			return;
		}
		if (!(num0 instanceof CaNumber)) {
			if (DBG) System.out.println(
					"SimpleProcessor: opPercentFixup(): num0 no number");
			return;
		}

		if (DBG) System.out.println(
				"SimpleProcessor: opPercentFixup(): fixing up");
		Object newop = null;
		if (op.equals(ProcessorAction.PA_ADD)) {
			newop = ProcessorAction.PA_PERCENT_ADD;
		} else if (op.equals(ProcessorAction.PA_SUB)) { // 
			newop = ProcessorAction.PA_PERCENT_SUB;
		} else if (op.equals(ProcessorAction.PA_MUL)) { // 
			newop = ProcessorAction.PA_PERCENT_MUL;
		} else if (op.equals(ProcessorAction.PA_DIV)) { // 
			newop = ProcessorAction.PA_PERCENT_DIV;
		} else if (op.equals(ProcessorAction.PA_PERCENT_EX)) { //
			// actually we do not replace the ex% - we simply 
			// only remove the %, if there is any
			newop = ProcessorAction.PA_PERCENT_EX;
		} else {
			// no replacement?
			throw new Error(
					"Software is probably corrupted. "+
					"Please contact customer support.");
		}
		if (DBG) System.out.println(
				"SimpleProcessor: opPercentFixup(): newop is: " + 
				newop.toString());
		// remove [%], [num], [op]
		// add [newop], [num]
		actionStack.removeElementAt(curidx);	// %
		actionStack.removeElementAt(curidx-1);	// num
		actionStack.removeElementAt(curidx-2);	// op
		
		actionStack.pushAction(newop);			// newop
		actionStack.pushAction(num1);			// num
	}
	
	/**
	 * Process the given binary operation.
	 * 
	 * @param cmd
	 */
	protected void opBinary(String cmd) {
		if (DBG) System.out.println("SimpleProcessor: opBinary(): " + cmd);

		// in any case, we make the accu non-editable:
		if (editable) {
			if (DBG) System.out.println("SimpleProcessor: opBinary(): editable<-false");
			closeInput(); //editable = false;
		}
		checkLogStart(actionStack.getAccu(), LOGSTART_SCOPE_OP);
//		if (isLogStart()) {
//			if (DBG) System.out.println("opBinary1: logStart");
//			appendLog("", null, actionStack.getAccu(), null, null);
//			setLogStart(false);
//		}


		// if the last operation is a binary operation: replace it:
		Object lastAction = actionStack.peek();
		if (lastAction instanceof String) {
			String pa = (String)lastAction;
			ProcessorActionAttribute paa = paam.get(pa);
			if (paa!=null) {
				if (
						paa.getType()==ProcessorActionAttribute.TYPE_OP &&
						paa.getOrder()==2
					) {
					if (DBG) System.out.println(
							"SimpleProcessor: opBinary(): "+
							"replacing " + paa.getPa() + " with " + cmd);
					actionStack.pop();
					// replacing push comes as a standard op below:
				}
			}
		} // (if need to replace last op)
		actionStack.pushAction(cmd);

		calculateStack();

		fireDisplayEvent();	// FIXME: move that into calculateStack
	}

	/**
	 * Process the given unary operation.
	 * 
	 * @param cmd
	 */
	protected void opUnary(String cmd) {
		if (DBG) System.out.println("SimpleProcessor: opUnary(): " + cmd);

		if (editable) {
			if (DBG) System.out.println("SimpleProcessor: opUnary(): editable<-false");
			closeInput(); //editable = false
		}
		checkLogStart(actionStack.getAccu(), LOGSTART_SCOPE_OP);
//		if (isLogStart()) {
//			if (DBG) System.out.println("opUnary1: logStart");
//			appendLog("", null, actionStack.getAccu(), null, null);
//			setLogStart(false);
//		}

		// if the last operation is a binary operation: replace it:
		Object lastAction = actionStack.peek();
		if (lastAction instanceof String) {
			String pa = (String)lastAction;
			ProcessorActionAttribute paa = paam.get(pa);
			if (paa!=null) {
				if (
						paa.getType()==ProcessorActionAttribute.TYPE_OP &&
						paa.getOrder()==2
					) {
					if (DBG) System.out.println(
							"SimpleProcessor: opUnary(): "+
							"replacing " + paa.getPa() + " with " + cmd);
					actionStack.pop();
					// replacing push comes as a standard op below:
				}
			}
		} // (if need to replace last op)
		calculateStack();
		actionStack.pushAction(cmd);

		calculateStack();

		fireDisplayEvent();	// FIXME: move that into calculateStack
	}

	/**
	 * @param cmd
	 */
	protected void opConstant(String cmd) {
		if (DBG) System.out.println("SimpleProcessor: opConstant(): " + cmd);
		double v;
		if (cmd.equals(ProcessorAction.PA_M_E)) {
			v = Math.E;
		} else if (cmd.equals(ProcessorAction.PA_M_PI)) {
			v = Math.PI;
		} else {
			if (DBG) System.out.println("SimpleProcessor: opConstant(): unknown constant");
			return; // unknown!
		}

		if (editable) {
			if (DBG) System.out.println("SimpleProcessor: opConstant(): editable: replacing input");
			actionStack.getAccu().setValue(v);
			getAccuToInput();
			closeInput(); //editable = false;
		} else {
			if (DBG) System.out.println("SimpleProcessor: opConstant(): not editable: pushing new accu");
			// FIXME: maybe we need automatic EXE here?
			actionStack.newAccuForInput();
			actionStack.getAccu().setValue(v);
			closeInput(); //editable = false;
		}
		getAccuToInput();

		fireDisplayEvent();
	}

	/**
	 * Do the "EXE" command.
	 */
	protected void opExecute() {
		if (DBG) System.out.println("SimpleProcessor: opExecute(): ");

		if (editable) {
			if (DBG) System.out.println("SimpleProcessor: opExecute(): editable: editable<-false");
			closeInput(); //editable = false;
		}

		actionStack.pushAction(ProcessorAction.PA_EXE);
		int lastsize = -1;
		while (actionStack.size()>1) {
			if (actionStack.size()==lastsize) {
				if (DBG) System.out.println("SimpleProcessor: opExecute(): actionStack did not change, leaving loop");
				break;	// was return 
			}
			lastsize = actionStack.size();

			if (DBG) System.out.println("SimpleProcessor: opExecute(): actionStack>1: calc-loop-step");
			calculateStack();
		}
		if (DBG) System.out.println("SimpleProcessor: opExecute(): finished");
		appendLog(
				ProcessorAction.PA_EXE, null, 
				actionStack.getAccu(), 
				actionStack.getLastSubResult(),
				null);

		setLogStart(true);
		
		fireDisplayEvent();	// move to calculateStack?
	}

	/**
	 * Processes the stack as much as possible
	 */
	protected void calculateStack() {
		if (DBG) System.out.println("SimpleProcessor: calculateStack()");
		// check stack: we need 2 accus and 1 cmd
		String cmd;

		// find op index:
		int opidx = actionStack.size()-1;

		while (true) {
			cmd = null;
			while (opidx>=0) {
				Object o = actionStack.elementAt(opidx);
				if (o instanceof String) {
					cmd = (String)o;
					break;
				}
				opidx--;
			}
			if (cmd==null) {
				if (DBG) System.out.println("SimpleProcessor: calculateStack(): no op found on stack");
				return;	// nothing to do, no command/op
			}
			if (DBG) System.out.println("SimpleProcessor: calculateStack(): op found on stack: " + cmd);
			ProcessorActionAttribute paa;
			paa = paam.get(cmd);
			if (paa==null) {
				if (DBG) System.out.println("SimpleProcessor: calculateStack(): op not in PAAM");
				return;
			}
			if (calculateStack(paa, opidx)) {
				// ok
				return;
			} else {
				// another try
				opidx--;
				if (DBG) System.out.println("SimpleProcessor: calculateStack(): another try @ " + opidx);
			}
		}
	}

	final static CaDouble factorDegToRad = new CaDouble(Math.PI/180.0);
	final static CaDouble factorGraToRad = new CaDouble(Math.PI/200.0);
	final static CaDouble factorRadToDeg = new CaDouble(180.0/Math.PI);
	final static CaDouble factorRadToGra = new CaDouble(200.0/Math.PI);

	/**
	 * @param dv
	 * @return	Returns the parameter converted from the current
	 * angular argument mode to rad.
	 */
	protected CaDouble convertToRad(CaDouble dv) {
		switch (getAngularArgMode()) {
		case ANGULARARG_DEG: 
			DMath.mul(dv, factorDegToRad);
			break;
		case ANGULARARG_GRA: 
			DMath.mul(dv, factorGraToRad);
			break;
		case ANGULARARG_RAD:	// fall through
		default:
			// nothing
			break;
		}
		return dv;
	}

	/**
	 * @param dv
	 * @return	Returns the parameter converted from rad to the current
	 * angular argument mode.
	 */
	protected CaDouble convertFromRad(CaDouble dv) {
		switch (getAngularArgMode()) {
		case ANGULARARG_DEG: 
			DMath.mul(dv, factorRadToDeg);
			break;
		case ANGULARARG_GRA: 
			DMath.mul(dv, factorRadToGra);
			break;
		case ANGULARARG_RAD:	// fall through
		default:
			// nothing
			break;
		}
		return dv;
	}

	/**
	 * @param paa
	 * @param opidx
	 * @return	Returns false, if another op should be searched.
	 */
	protected boolean calculateStack(ProcessorActionAttribute paa, int opidx) {
		CaDouble acc1;
		CaDouble acc2;
		String cmd = paa.getPa();

		if (paa.getOrder()==1) {
			if (DBG) System.out.println("SimpleProcessor: calculateStack(): op has order 1");
			acc1 = (CaDouble)actionStack.elementAt(opidx-1);
			try {
				calcUnaryOpImpl(cmd, acc1, null, true);
			} catch (ProcessorUnknownOpException e) {
				// FIXME: remove printStackTrace
				e.printStackTrace();
			}
			setLogStart(true, true); // for [2] [sin] [3] [+] (not showing the 3)

//			// we make a log entry, because unary ops will otherwise
//			// be skipped.
//			appendLog(cmd + "->", acc1);

			if (DBG) System.out.println("SimpleProcessor: calculateStack(): removing op @ " + opidx);
			// remove the op:
			actionStack.removeElementAt(opidx);
		} else if (paa.getOrder()==2) {
			if (DBG) System.out.println("SimpleProcessor: calculateStack(): op has order 2");
			if ((opidx+1)>=actionStack.size()) {
				if (DBG) System.out.println("SimpleProcessor: calculateStack(): not enough elements, leaving");
				return false;
			}

			acc1 = (CaDouble)actionStack.elementAt(opidx-1);
			acc2 = (CaDouble)actionStack.elementAt(opidx+1);

			try {
				calcBinaryOpImpl(cmd, acc1, acc2, null, true);
			} catch (ProcessorUnknownOpException e) {
				// FIXME: remove printStackTrace
				e.printStackTrace();
			}

			if (DBG) System.out.println(
					"SimpleProcessor: calculateStack(): removing ac @ " + 
					(opidx+1));
			if (DBG) System.out.println(
					"SimpleProcessor: calculateStack(): removing op @ " + 
					opidx);
			// order is important!:
			actionStack.removeElementAt(opidx+1);
			actionStack.removeElementAt(opidx);
		} else {
			if (cmd.equals(ProcessorAction.PA_EXE)) {
				if (DBG) System.out.println("SimpleProcessor: calculateStack(): special case EXE");
				actionStack.removeElementAt(opidx);
			} else {
				// unsupported cmd
				if (DBG) System.out.println("SimpleProcessor: calculateStack(): unsupported cmd " + cmd);
				actionStack.removeElementAt(opidx);
			}
		}
		fireStatusEvent(ProcessorEvent.STACK);
		return true;
	}

	// ///////////////////////////////////////////////////
	// protected internal engine functions
	// ///////////////////////////////////////////////////

	/**
	 * Set the accu from the input string
	 */
	protected void setAccuFromInput() {
		double v;
		v = Double.valueOf(inputstr).doubleValue();
		if (sign<0) v = -v;
		actionStack.getAccu().setValue(v);
		if (DBG) System.out.println("SimpleProcessor: setAccuFromInput: " + v);
	}

	/**
	 * @param cd
	 */
	protected void setAccu(CaDouble cd) {
		cd.cloneTo(actionStack.getAccu());
//		try {
//			acc = cd.clone();
//			appendToInput = false;
//			hasNewInput = false;
//			accChanged = true;
//			//System.out.println("acc=" + acc.toString());
//			// FIXME: maybe fire?
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//			throw new ProcessorException(
//					"calculateStack: clone failed."+
//					" check Number implementation.");
//		}
	}

	/**
	 * Set the input string with the value from the Accu 
	 */
	protected void getAccuToInput() {
		inputstr = actionStack.getAccu().toString();
		if (inputstr.charAt(0)=='-') {
			sign = -1;
			inputstr = inputstr.substring(1, inputstr.length()-1);
		} else {
			sign = +1;
		}
		closeInput(); //editable = false;
		// FIXME: maybe fire?
	}

	/**
	 * Append the value and the command to the protocol.
	 *
	 * @param cmd
	 * @param subExpr 
	 * @param value
	 * @param lastSubResult 
	 * @param curResult 
	 */
	protected void appendLog(
			String cmd, String subExpr, CaDouble value, 
			CaDouble lastSubResult, CaDouble curResult) {
		String cmddisp = cmd;
		if (DBG) System.out.println("SimpleProcessor: appendLog(): " + 
				cmd + " / " + 
				((value!=null) ? value.toString() : "<none>"));
		ProcessorActionAttribute paa = paam.get(cmd);
		if (paa!=null) 
			cmddisp = paa.getDisplay();
		if (cmd.equals(ProcessorAction.PA_CLR_ALL)) {
			logstr += "\n" + cmddisp + "\n";
			fireProtocolEvent();
			return;
		}
		if (cmd.equals("")) {
			cmddisp = ".";
		}
		if (cmd.equals(ProcessorAction.PA_EXE)) {
			logstr += "\n" + StringUtil.fill('-', getProtocolWidth());
		}
		// logstr += "\n" + cmddisp + " " + accu.toString(displaywidth - cmddisp.length() + 1);
		String cmddisp2 = cmddisp;
		if (subExpr!=null) {
			logstr += "\n-> " + subExpr;
		}
		// if (subExpr!=null) cmddisp2 += subExpr;
		if (cmddisp2.length()>6) {
			logstr += "\n" + 
				"-> " + cmddisp2 + "\n" +
				(cmd + "      ").substring(0, 6) + " " + 
				((value!=null) ? numberFormat.format(value) : "./.");
		} else {
			logstr += "\n" + 
				(cmddisp2 + "     ").substring(0, 6) + " " + 
				((value!=null) ? numberFormat.format(value) : "./.");
		}
		if (cmd.equals(ProcessorAction.PA_EXE)) {
			logstr += "\n" + StringUtil.fill('=', getProtocolWidth());
			logstr += "\n";
		}

		if (cmd.equals("=")) {
			// fire result
			fireProtocolAddResultEvent(
					cmd, value, lastSubResult, curResult);
		} else if (subExpr!=null) {
			// fire subexpr
			fireProtocolAddSubExprEvent(
					cmd, subExpr, value, lastSubResult, curResult);
		} else {
			// plain op
			fireProtocolAddOpEvent(
					cmd, value, lastSubResult, curResult);
		}
		
		fireProtocolEvent();
	}

	/**
	 * @return	Returns the width of the protocol.
	 */
	public int getProtocolWidth() {
		return 6 + 1 + numberFormat.getWidth();
	}
	
	/**
	 * FIXME: either this should be protected, or the other fire methods
	 * should also be public.
	 * Fire all events which are Display, Status(memory,stack), Protocol 
	 */
	public void fireAll() {
		fireDisplayEvent();
		fireStatusEvent(
				ProcessorEvent.MEMORY | 
				ProcessorEvent.STACK |
				ProcessorEvent.ANGARGMODE);
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

	protected void fireProtocolAddOpEvent(
			String op, CaNumber value, 
			CaNumber lastSubResult, CaNumber curResult) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ProtocolEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==ProtocolEventListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new ProtocolEvent(
							this, op, null, value, lastSubResult, curResult);
				((ProtocolEventListener)listeners[i+1]).addOp(e);
			}
		}
	}

	protected void fireProtocolAddSubExprEvent(
			String op, String subExpr, CaNumber value, 
			CaNumber lastSubResult, CaNumber curResult) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ProtocolEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==ProtocolEventListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new ProtocolEvent(
							this, op, subExpr, value, lastSubResult, curResult);
				((ProtocolEventListener)listeners[i+1]).addSubExprOp(e);
			}
		}
	}

	protected void fireProtocolAddResultEvent(
			String op, CaNumber value, 
			CaNumber lastSubResult, CaNumber curResult) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ProtocolEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==ProtocolEventListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new ProtocolEvent(
							this, op, null, value, lastSubResult, curResult);
				((ProtocolEventListener)listeners[i+1]).addResult(e);
			}
		}
	}

	protected void fireProtocolAddClearEvent() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ProtocolEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==ProtocolEventListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new ProtocolEvent(this, null, null, null, null, null);
				((ProtocolEventListener)listeners[i+1]).addClear(e);
			}
		}
	}

	/**
	 * @return Returns the actionStack.
	 */
	public Stack<Object> getActionStack() {
		return actionStack.getActionStack();
	}

	/**
	 * @return Returns the numberFormat.
	 */
	public CaDoubleFormat getNumberFormat() {
		return numberFormat;
	}

	/**
	 * @param numberFormat The numberFormat to set.
	 */
	public void setNumberFormat(CaDoubleFormat numberFormat) {
		this.numberFormat = numberFormat;
		if (localeProvider!=null) {
			this.numberFormat.setLocale(localeProvider.getDefaultLocale());
		}
	}

	/**
	 * @param e
	 * @see de.admadic.calculator.core.LocaleListener#localeChanged(de.admadic.calculator.core.LocaleEvent)
	 */
	public void localeChanged(LocaleEvent e) {
		numberFormat.setLocale(localeProvider.getDefaultLocale());
		fireDisplayEvent();
	}

	/**
	 * @param e
	 * @see de.admadic.calculator.core.LocaleListener#localeNumberFormatChanged(de.admadic.calculator.core.LocaleNumberFormatEvent)
	 */
	public void localeNumberFormatChanged(LocaleNumberFormatEvent e) {
		numberFormat.setLocale(localeProvider.getDefaultLocale());
		fireDisplayEvent();
	}


	/**
	 * @return Returns the angularArgMode.
	 * @since calccore 1.0.2 
	 */
	public int getAngularArgMode() {
		return angularArgMode;
	}

	/**
	 * @param angularArgMode The angularArgMode to set.
	 * @since calccore 1.0.2 
	 */
	public void setAngularArgMode(int angularArgMode) {
		this.angularArgMode = angularArgMode;
		// FIXME: fire status change event?
	}

	/**
	 * @return Returns the logStart.
	 */
	public boolean isLogStart() {
		return logStart;
	}

	/**
	 * @param logStart The logStart to set.
	 */
	public void setLogStart(boolean logStart) {
		this.logStart = logStart;
		this.logStartNotForOp = false;
	}

	/**
	 * @param logStart The logStart to set.
	 * @param logStartNotForOp 
	 */
	public void setLogStart(boolean logStart, boolean logStartNotForOp) {
		this.logStart = logStart;
		this.logStartNotForOp = logStartNotForOp;
	}

	/**
	 * @param value
	 * @param scope
	 */
	public void checkLogStart(CaDouble value, int scope) {
		if (isLogStart()) {
			if (isLogStartNotForOp()) {
				switch (scope) {
				case LOGSTART_SCOPE_INPUT:
					if (DBG) System.out.println("checkLogStart: logStart");
					appendLog("", null, value, null, null);
					setLogStart(false);
					break;
				case LOGSTART_SCOPE_OP:
					// only reset
					setLogStart(false);
					break;
				}
			} else {
				if (DBG) System.out.println("checkLogStart: logStart");
				appendLog("", null, value, null, null);
				setLogStart(false);
			}
		}
	}

	/**
	 * @return Returns the logStartNotForOp.
	 */
	public boolean isLogStartNotForOp() {
		return logStartNotForOp;
	}
}
