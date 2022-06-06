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
public class ProcessorContextA {

	Object [][] stdValues = new Object[][]{
			// pa = processor action
			// display = display text
			// type = action type
			// order = unary or binary or ... 
			// priority
			//				pa,  display,  type, order, prio
			{ProcessorAction.PA_0, null, "num", 0, 0},
			{ProcessorAction.PA_1, null, "num", 0, 0},
			{ProcessorAction.PA_2, null, "num", 0, 0},
			{ProcessorAction.PA_3, null, "num", 0, 0},
			{ProcessorAction.PA_4, null, "num", 0, 0},
			{ProcessorAction.PA_5, null, "num", 0, 0},
			{ProcessorAction.PA_6, null, "num", 0, 0},
			{ProcessorAction.PA_7, null, "num", 0, 0},
			{ProcessorAction.PA_8, null, "num", 0, 0},
			{ProcessorAction.PA_9, null, "num", 0, 0},
			{ProcessorAction.PA_DOT, null, "num", 0, 0},
			{ProcessorAction.PA_SIGN, null, "num", 0, 0},
			{ProcessorAction.PA_BACKSPACE, null, "num", 0, 0},

			{ProcessorAction.PA_EXE, "=", "spc", 0, 0},

			//				pa,    type, order, prio
			{ProcessorAction.PA_ADD, "+", "op", 2, 1},
			{ProcessorAction.PA_SUB, "-", "op", 2, 1},
			{ProcessorAction.PA_MUL, "x", "op", 2, 2},
			{ProcessorAction.PA_DIV, "\u00f7", "op", 2, 2},

			//				pa,    type, order, prio
			{ProcessorAction.PA_PERCENT, "%", "op", 1, 4},
			{ProcessorAction.PA_PERCENT_ADD, "+%", "op", 2, 2},
			{ProcessorAction.PA_PERCENT_SUB, "-%", "op", 2, 2},
			{ProcessorAction.PA_PERCENT_MUL, "*%", "op", 2, 2},
			{ProcessorAction.PA_PERCENT_DIV, "/%", "op", 2, 2},
			{ProcessorAction.PA_PERCENT_EX, "ex%", "op", 2, 2},

			//				pa,    type, order, prio
			{ProcessorAction.PA_M_SQR, 	"sqr", "op", 1, 4},
			{ProcessorAction.PA_M_SQRT, "sqrt", "op", 1, 4},
			{ProcessorAction.PA_M_LN, 	"ln", "op", 1, 4},
			{ProcessorAction.PA_M_LOG, 	"log", "op", 1, 4},
			{ProcessorAction.PA_M_EXP, 	"e^x", "op", 1, 4},
			{ProcessorAction.PA_M_EXP10, "10^x", "op", 1, 4},
			{ProcessorAction.PA_M_XINV, "1/x", "op", 1, 4},
			{ProcessorAction.PA_M_SIN, 	"sin", "op", 1, 4},
			{ProcessorAction.PA_M_COS, 	"cos", "op", 1, 4},
			{ProcessorAction.PA_M_TAN, 	"tan", "op", 1, 4},
			{ProcessorAction.PA_M_COT, 	"cot", "op", 1, 4},
			{ProcessorAction.PA_M_ARCSIN, 	"arcsin", "op", 1, 4},
			{ProcessorAction.PA_M_ARCCOS, 	"arccos", "op", 1, 4},
			{ProcessorAction.PA_M_ARCTAN, 	"arctan", "op", 1, 4},
			{ProcessorAction.PA_M_ARCCOT, 	"arccot", "op", 1, 4},
			{ProcessorAction.PA_M_SINH, "sinh", "op", 1, 4},
			{ProcessorAction.PA_M_COSH, "cosh", "op", 1, 4},
			{ProcessorAction.PA_M_TANH, "tanh", "op", 1, 4},
			{ProcessorAction.PA_M_COTH, "coth", "op", 1, 4},
			{ProcessorAction.PA_M_ARSINH, "arsinh", "op", 1, 4},
			{ProcessorAction.PA_M_ARCOSH, "arcosh", "op", 1, 4},
			{ProcessorAction.PA_M_ARTANH, "artanh", "op", 1, 4},
			{ProcessorAction.PA_M_ARCOTH, "arcoth", "op", 1, 4},
			{ProcessorAction.PA_M_SEC, 	"sec", "op", 1, 4},
			{ProcessorAction.PA_M_COSEC,"cosec", "op", 1, 4},
			{ProcessorAction.PA_M_GAMMA, "gamma", "op", 1, 4},

			{ProcessorAction.PA_M_POW, 	"x^y", "op", 2, 3},
			{ProcessorAction.PA_M_X_RT,	"x-rt", "op", 2, 3},

			{ProcessorAction.PA_M_PI,	"pi", "op", 0, 0},
			{ProcessorAction.PA_M_E,	"e", "op", 0, 0},

			{ProcessorAction.PA_AM_RAD, "rad", "spc", 0, 0},
			{ProcessorAction.PA_AM_DEG, "deg", "spc", 0, 0},
			{ProcessorAction.PA_AM_GRA, "gra", "spc", 0, 0},

			//				pa,    type, order, prio
			{ProcessorAction.PA_CLR_ALL, ".clr", "spc", 0, 0},
			{ProcessorAction.PA_CLR_ENTRY, ".ce", "spc", 0, 0},
			{ProcessorAction.PA_MEM_CLR, ".mc", "spc", 0, 0},
			{ProcessorAction.PA_MEM_ADD, ".m+", "spc", 0, 0},
			{ProcessorAction.PA_MEM_SUB, ".m-", "spc", 0, 0},
			{ProcessorAction.PA_MEM_STORE, ".ms", "spc", 0, 0},
			{ProcessorAction.PA_MEM_READ, ".mr", "spc", 0, 0},
			{ProcessorAction.PA_LOG_CLR, null, "spc", 0, 0},
			{ProcessorAction.PA_LOG_CLRLINE, null, "spc", 0, 0},
	};

	ProcessorActionAttributeManager paam;

	/**
	 * 
	 */
	public ProcessorContextA() {
		super();
		paam = new ProcessorActionAttributeManager();
	}

	/**
	 * 
	 */
	public void init() {
		ProcessorActionAttribute paa;
		int order;
		int prio;
		int type;
		String pa;
		String tmp;
		String display;
		for (int i = 0; i < stdValues.length; i++) {
			pa = (String)stdValues[i][0];
			display = (String)stdValues[i][1];
			tmp = (String)stdValues[i][2];
			if (tmp.equals("num")) {
				type = ProcessorActionAttribute.TYPE_NUM;
			} else if (tmp.equals("op")) {
				type = ProcessorActionAttribute.TYPE_OP;
			} else {
				type = ProcessorActionAttribute.TYPE_SPC;
			}
			order = ((Integer)(stdValues[i][3])).intValue();
			prio = ((Integer)(stdValues[i][4])).intValue();
			paa = new ProcessorActionAttribute(pa, display, order, prio, type);
			paam.add(paa);
		}
	}

	/**
	 * @return	Returns the ProcessorActionAttributeManager.
	 */
	public ProcessorActionAttributeManager getPAAM() {
		return paam;
	}
}
