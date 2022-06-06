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
package de.admadic.calculator.processor;

/**
 * @author Rainer Schwarze
 *
 */
public class ProcessorAction extends Object {

	/** pa constant ? */
	public final static String PA_0 = "0";
	/** pa constant ? */
	public final static String PA_1 = "1";
	/** pa constant ? */
	public final static String PA_2 = "2";
	/** pa constant ? */
	public final static String PA_3 = "3";
	/** pa constant ? */
	public final static String PA_4 = "4";
	/** pa constant ? */
	public final static String PA_5 = "5";
	/** pa constant ? */
	public final static String PA_6 = "6";
	/** pa constant ? */
	public final static String PA_7 = "7";
	/** pa constant ? */
	public final static String PA_8 = "8";
	/** pa constant ? */
	public final static String PA_9 = "9";

	/** pa constant ? */
	public final static String PA_EXE = "=";
	/** pa constant ? */
	public final static String PA_ADD = "+";
	/** pa constant ? */
	public final static String PA_SUB = "-";
	/** pa constant ? */
	public final static String PA_MUL = "*";
	/** pa constant ? */
	public final static String PA_DIV = "/";

	/** pa constant ? */
	public final static String PA_M_SQR = "sqr";
	/** pa constant ? */
	public final static String PA_M_SQRT = "sqrt";
	/** pa constant ? */
	public final static String PA_M_LN = "ln";
	/** pa constant ? */
	public final static String PA_M_LOG = "log";
	/** pa constant ? */
	public final static String PA_M_EXP = "exp";
	/** pa constant ? */
	public final static String PA_M_EXP10 = "exp10";
	/** pa constant ? */
	public final static String PA_M_XINV = "xinv";
	/** pa constant ? */
	public final static String PA_M_SIN = "sin";
	/** pa constant ? */
	public final static String PA_M_COS = "cos";
	/** pa constant ? */
	public final static String PA_M_TAN = "tan";
	/** pa constant ? */
	public final static String PA_M_COT = "cot";
	/** pa constant ? */
	public final static String PA_M_POW = "pow";
	/** pa constant ? */
	public final static String PA_M_X_RT = "xrt";

	/** pa constant ? */
	public final static String PA_M_ARCSIN = "arcsin";
	/** pa constant ? */
	public final static String PA_M_ARCCOS = "arccos";
	/** pa constant ? */
	public final static String PA_M_ARCTAN = "arctan";
	/** pa constant ? */
	public final static String PA_M_ARCCOT = "arccot";

	/** pa constant ? */
	public final static String PA_M_ARSINH = "arsinh";
	/** pa constant ? */
	public final static String PA_M_ARCOSH = "arcosh";
	/** pa constant ? */
	public final static String PA_M_ARTANH = "artanh";
	/** pa constant ? */
	public final static String PA_M_ARCOTH = "arcoth";

	/** pa constant ? */
	public final static String PA_M_SINH = "sinh";
	/** pa constant ? */
	public final static String PA_M_COSH = "cosh";
	/** pa constant ? */
	public final static String PA_M_TANH = "tanh";
	/** pa constant ? */
	public final static String PA_M_COTH = "coth";

	/** pa constant ? */
	public final static String PA_M_SEC = "sec";
	/** pa constant ? */
	public final static String PA_M_COSEC = "cosec";

	/** pa constant ? */
	public final static String PA_M_GAMMA = "gamma";

	/** pa constant ? */
	public final static String PA_AM_RAD = "am.rad";
	/** pa constant ? */
	public final static String PA_AM_DEG = "am.deg";
	/** pa constant ? */
	public final static String PA_AM_GRA = "am.gra";

	/** pa constant ? */
	public final static String PA_M_PI = "pi";
	/** pa constant ? */
	public final static String PA_M_E = "e";

	/** pa constant ? */
	public final static String PA_DOT = ".";
	/** pa constant ? */
	public final static String PA_SIGN = "+/-";

	/** pa constant ? */
	public final static String PA_BACKSPACE = "BS";

	/** pa constant ? */
	public final static String PA_CLR_ALL = "c.a";
	/** pa constant ? */
	public final static String PA_CLR_ENTRY = "c.e";

	/** pa constant ? */
	public final static String PA_MEM_CLR = "m.c";
	/** pa constant ? */
	public final static String PA_MEM_ADD = "m.+";
	/** pa constant ? */
	public final static String PA_MEM_SUB = "m.-";
	/** pa constant ? */
	public final static String PA_MEM_MUL = "m.*";
	/** pa constant ? */
	public final static String PA_MEM_DIV = "m./";
	/** pa constant ? */
	public final static String PA_MEM_STORE = "m.s";
	/** pa constant ? */
	public final static String PA_MEM_READ = "m.r";

	/** pa constant ? */
	public final static String PA_LOG_CLR = "l.c";
	/** pa constant ? */
	public final static String PA_LOG_CLRLINE = "l.cl";

	/** pa constant ? */
	public final static String PA_PERCENT = "percent";
	/** pa constant ? */
	public final static String PA_PERCENT_ADD = "percent.+";
	/** pa constant ? */
	public final static String PA_PERCENT_SUB = "percent.-";
	/** pa constant ? */
	public final static String PA_PERCENT_MUL = "percent.*";
	/** pa constant ? */
	public final static String PA_PERCENT_DIV = "percent./";
	/** pa constant ? */
	public final static String PA_PERCENT_EX = "percent.ex";

	/**
	 * Action string for extended commands. Append with specific string
	 * for the actual action.
	 */
	public final static String PA_EXT = "ext.";

	String actionCmd;
	
	/**
	 * 
	 */
	public ProcessorAction() {
		super();
		this.actionCmd = null;
	}

	/**
	 * @param cmd
	 */
	public ProcessorAction(String cmd) {
		super();
		this.actionCmd = cmd;
	}

	/**
	 * @return Returns the cmd.
	 */
	public String getActionCmd() {
		return actionCmd;
	}

	/**
	 * @param cmd The cmd to set.
	 */
	public void setActionCmd(String cmd) {
		this.actionCmd = cmd;
	}
}
