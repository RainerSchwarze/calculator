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
package de.admadic.calculator.ui;

import java.awt.MediaTracker;

import de.admadic.calculator.processor.ProcessorAction;

/**
 * @author Rainer Schwarze
 *
 */
public class CmdSetSimple extends CmdSet {
	// a #-sign in front of a font name indicates that it is a local resource
	String [][] specGrp = {
			// nm,  color,     font,     bd,     sz,   pan,  pancolor
			{"std",  "",        "",      "",     "",   "false", "#000000"},	
			{"num",  "#000000", "Dialog", "", "12", "false", ""},	
			{"mem",  "#008000", "Dialog", "", "11", "false", "#C0E0C0"},	
			{"memc", "#FF8000", "Dialog", "", "11", "false", "#FF8040"},	
			{"op",   "#0000FF", "Dialog", "", "12", "false", "#C0C0FF"},	
			{"clr",  "#FF0000", "Dialog", "", "10", "false", "#FFC0C0"},	
			{"exe",  "#000000", "Dialog", "", "12", "false", "#D0D0D0"},	
			{"log",  "#000000", "Dialog", "", "11", "false", ""},	
			{"lgw",  "#000000", "Dialog", "", "10", "false", ""},	
			{"dsp",  "#000000", "Courier", "bold", "12", "true", ""},	
			{"sts",  "#000000", "Dialog", "", "10", "false", ""},	
			{"mth",  "#0000FF", "Dialog", "",  "9", "false", ""},	
			{"mth2",  "#000000", "Dialog", "",  "9", "false", ""},	
			{"mod",  "",        "",      "",    "", "false", ""},	
	};
	static final int SPECCMD_FIX_COLS = 5;
	static final int SPECCMD_GFX_COLS = 6;
	String [][] specCmd = {
		{ProcessorAction.PA_0, 	"", "btn", "num", "Digit Zero"},
		{ProcessorAction.PA_1, 	"", "btn", "num", "Digit One"},
		{ProcessorAction.PA_2, 	"", "btn", "num", "Digit Two"},
		{ProcessorAction.PA_3, 	"", "btn", "num", "Digit Three"},
		{ProcessorAction.PA_4, 	"", "btn", "num", "Digit Four"},
		{ProcessorAction.PA_5, 	"", "btn", "num", "Digit Five"},
		{ProcessorAction.PA_6, 	"", "btn", "num", "Digit Six"},
		{ProcessorAction.PA_7,	"", "btn", "num", "Digit Seven"},
		{ProcessorAction.PA_8, 	"", "btn", "num", "Digit Eight"},
		{ProcessorAction.PA_9, 	"", "btn", "num", "Digit Nine"},
		{ProcessorAction.PA_DOT, ".", "btn", "num", "Dot"}, // "." "\u2219" "\u2022"
		{ProcessorAction.PA_SIGN, "\u00b1", "btn", "num", "Toggle Sign"},
		{ProcessorAction.PA_BACKSPACE, 	"BS", "btn", "exe", "Backspace"},
		{ProcessorAction.PA_ADD, 	"+", "btn", "op", "Add", "true", "", "btn-op-add.png"},
		{ProcessorAction.PA_SUB, 	"-", "btn", "op", "Subtract", "true", "", "btn-op-sub.png"},
		{ProcessorAction.PA_MUL, 	"*", "btn", "op", "Multiply", "true", "", "btn-op-mul.png"},
		{ProcessorAction.PA_DIV, 	"/", "btn", "op", "Divide", "true", "", "btn-op-div.png"},
		{ProcessorAction.PA_EXE, 	"=", "btn", "exe", "Execute/Calculate"},
		{ProcessorAction.PA_PERCENT, "%", "btn", "op", "Percentage", "true"},
		{ProcessorAction.PA_PERCENT_ADD, "+%", "btn", "op", "Add Percentage", "true"},
		{ProcessorAction.PA_PERCENT_SUB, "-%", "btn", "op", "Subtract Percentage", "true"},
		{ProcessorAction.PA_PERCENT_MUL, "*%", "btn", "op", "Multiply Percentage", "true"},
		{ProcessorAction.PA_PERCENT_DIV, "/%", "btn", "op", "Divide Percentage", "true"},
		{ProcessorAction.PA_PERCENT_EX,  "e%", "btn", "op", "Extract Percentage (net from gross)", "true"},
		{ProcessorAction.PA_MEM_ADD, "M+", "btn", "mem", "Add to Memory"},
		{ProcessorAction.PA_MEM_SUB, "M-", "btn", "mem", "Subtract from Memory"},
		{ProcessorAction.PA_MEM_STORE, "MS", "btn", "mem", "Store to Memory"},
		{ProcessorAction.PA_MEM_READ, "MR", "btn", "mem", "Read from Memory"},
		{ProcessorAction.PA_MEM_CLR, "MC", "btn", "memc", "Clear Memory"},
		{ProcessorAction.PA_CLR_ALL, "C", "btn", "clr", "Clear All"},
		{ProcessorAction.PA_CLR_ENTRY, "CE", "btn", "clr", "Clear Entry"},
		{ProcessorAction.PA_LOG_CLR, "C", "btn", "log", "Clear Log", "false", "", "btn-prot-clr.png"},
		{ProcessorAction.PA_LOG_CLRLINE, "Cl", "btn", "log", "Clear Last Line", "false", "", "btn-prot-clrll.png"},
		{"l.p", "P", "btn", "log", "Print Log", "false", "", "btn-print.png"},
		{"l.s", "S", "btn", "log", "Save Log", "false", "", "btn-save.png"},
		{"off", "Off", "btn", "clr", "Turn off the calculator"},
		{"dsp.display", "Display", "dsp", "dsp", "Display of Calculator"},
		{"dsp.status", "Status", "spc", "sts", "Status of Calculator"},
		{"dsp.mem", "Mem", "dsp", "spc", "Memory Status of Calculator"},
		{"log.window", "Log Window", "spc", "lgw", "Log Window of Calculator"},
		{"log.toggle", "Log", "tgl", "log", "Toggle Log Window of Calculator", "false", "", "btn-prot.png"},
		{"math.toggle", "Math", "tgl", "op", "Toggle Math Window of Calculator", "false", "", "btn-math.png"},
		{"aot.toggle", "AoT", "tgl", "log", "Toggle Always on Top Status of Calculator", "false", "", "btn-aot.png"},
		{"atw.snap", "Snap", "btn", "log", "Snap Attached Windows to Main Window", "false", "", "btn-snap.png"},
		{"atw.pin", "Pin", "tgl", "log", "Fix Positions of Attached Windows", "false", "", "btn-pin-up.png"},
		{"atw.pin.prottxt", "Pin", "tgl", "log", "Fix Position of Text Protocol Windows", "false", "", "btn-pin-up.png"},
		{"atw.pin.prottab", "Pin", "tgl", "log", "Fix Position of Tabular Protocol Windows", "false", "", "btn-pin-up.png"},
		{"mnu.show", "Menu", "btn", "log", "Show the Menu", "false", "", "btn-mnu.png"},
		{ProcessorAction.PA_M_SQR, 	"sqr", "btn", "mth", "Square", "true", "", "btn-mth-sqr.png"},
		{ProcessorAction.PA_M_SQRT, "sqrt", "btn", "mth", "Square Root", "true", "", "btn-mth-sqrt.png"},
		{ProcessorAction.PA_M_LN,	"ln", "btn", "mth", "Natural Logarithm", "true", "", "btn-mth-ln.png"},
		{ProcessorAction.PA_M_LOG, 	"log", "btn", "mth", "Base 10 Logarithm", "true", "", "btn-mth-log.png"},
		{ProcessorAction.PA_M_EXP, 	"e^x", "btn", "mth", "Euler's Number e raised to the power of x", "true", "", "btn-mth-exp.png"},
		{ProcessorAction.PA_M_EXP10, "10^x", "btn", "mth", "10 raised to the power of x", "true", "", "btn-mth-exp10.png"},
		{ProcessorAction.PA_M_XINV, "1/x", "btn", "mth", "Inverse of x (multiplication)", "true", "", "btn-mth-xinv.png"},
		{ProcessorAction.PA_M_SIN, 	"sin", "btn", "mth", "Sine", "true", "", "btn-mth-sin.png"},
		{ProcessorAction.PA_M_COS, 	"cos", "btn", "mth", "Cosine", "true", "", "btn-mth-cos.png"},
		{ProcessorAction.PA_M_TAN, 	"tan", "btn", "mth", "Tangent", "true", "", "btn-mth-tan.png"},
		{ProcessorAction.PA_M_COT, 	"cot", "btn", "mth", "Cotangent", "true", "", "btn-mth-cot.png"},
		{ProcessorAction.PA_M_ARCSIN, 	"arcsin", "btn", "mth", "Arcus Sine", "true", "", "btn-mth-arcsin.png"},
		{ProcessorAction.PA_M_ARCCOS, 	"arccos", "btn", "mth", "Arcus Cosine", "true", "", "btn-mth-arccos.png"},
		{ProcessorAction.PA_M_ARCTAN, 	"arctan", "btn", "mth", "Arcus Tangent", "true", "", "btn-mth-arctan.png"},
		{ProcessorAction.PA_M_ARCCOT, 	"arccot", "btn", "mth", "Arcus Cotangent", "true", "", "btn-mth-arccot.png"},
		{ProcessorAction.PA_M_SINH, 	"sinh", "btn", "mth", "Sine Hyperbolicus", "true", "", "btn-mth-sinh.png"},
		{ProcessorAction.PA_M_COSH, 	"cosh", "btn", "mth", "Cosine Hyperbolicus", "true", "", "btn-mth-cosh.png"},
		{ProcessorAction.PA_M_TANH, 	"tanh", "btn", "mth", "Tangent Hyperbolicus", "true", "", "btn-mth-tanh.png"},
		{ProcessorAction.PA_M_COTH, 	"coth", "btn", "mth", "Cotangent Hyperbolicus", "true", "", "btn-mth-coth.png"},
		{ProcessorAction.PA_M_ARSINH, 	"arsinh", "btn", "mth", "Area Sine Hyperbolicus", "true", "", "btn-mth-arsinh.png"},
		{ProcessorAction.PA_M_ARCOSH, 	"arcosh", "btn", "mth", "Area Cosine Hyperbolicus", "true", "", "btn-mth-arcosh.png"},
		{ProcessorAction.PA_M_ARTANH, 	"artanh", "btn", "mth", "Area Tangent Hyperbolicus", "true", "", "btn-mth-artanh.png"},
		{ProcessorAction.PA_M_ARCOTH, 	"arcoth", "btn", "mth", "Area Cotangent Hyperbolicus", "true", "", "btn-mth-arcoth.png"},
		{ProcessorAction.PA_M_SEC, 	"sec", "btn", "mth", "Secans", "true", "", "btn-mth-sec.png"},
		{ProcessorAction.PA_M_COSEC, "cosec", "btn", "mth", "Cosecans", "true", "", "btn-mth-cosec.png"},
		{ProcessorAction.PA_M_POW,	"x^y", "btn", "mth", "x raised to the power of y", "true", "", "btn-mth-pow.png"},
		{ProcessorAction.PA_M_X_RT,	"x rt", "btn", "mth", "x'th rt", "true", "", "btn-mth-xrt.png"},
		{ProcessorAction.PA_M_PI, 	"pi", "btn", "mth", "Pi (3.1415926...)", "true", "", "btn-mth-pi.png"},
		{ProcessorAction.PA_M_E, 	"e", "btn", "mth", "Euler's Number e (2.1718282...)", "true", "", "btn-mth-e.png"},
		{ProcessorAction.PA_M_GAMMA,"gamma", "btn", "mth", "Gamma (G[n+1]=n!)", "true", "", "btn-mth-gamma.png"},
		{ProcessorAction.PA_AM_RAD, "rad", "tgl", "mth2", "Angular Mode: Rad (pi/2)", "true"},
		{ProcessorAction.PA_AM_DEG, "deg", "tgl", "mth2", "Angular Mode: Deg (90)", "true"},
		{ProcessorAction.PA_AM_GRA, "gra", "tgl", "mth2", "Angular Mode: Gra (100)", "true"},
		{"mod.0", "Module#0", "btn", "mod", "Access Module #0", "false"},
		{"mod.1", "Module#1", "btn", "mod", "Access Module #1", "false"},
		{"mod.2", "Module#2", "btn", "mod", "Access Module #2", "false"},
		{"mod.3", "Module#3", "btn", "mod", "Access Module #3", "false"},
		{"mod.4", "Module#4", "btn", "mod", "Access Module #4", "false"},
		{"mod.5", "Module#5", "btn", "mod", "Access Module #5", "false"},
		{"mod.6", "Module#6", "btn", "mod", "Access Module #6", "false"},
		{"mod.7", "Module#7", "btn", "mod", "Access Module #7", "false"},
		{"mod.8", "Module#8", "btn", "mod", "Access Module #8", "false"},
		{"mod.9", "Module#9", "btn", "mod", "Access Module #9", "false"},
	};
	//"tpl", "std", "prs", "rvr", "sel", "dis", "rvrsel"},
	String [] stdBtnAry = null; 
	//{"button2.gif", "button2.gif", "button2prs.gif", "button2rvr.gif", "", ""};

	/**
	 * Generate standard set of commands.
	 * @param comp 
	 */
	public CmdSetSimple(java.awt.Component comp) {
		super();
		MediaTracker mt = new MediaTracker(comp);
		CmdEntry cmdE;
		CmdStyleGrp cmdG;
//		{"std",  "", "", 0, 0},	
//		{"num",  "#000000", "Arial", 1, 16},
		for (int i=0; i<specGrp.length; i++) {
			cmdG = new CmdStyleGrp(
					specGrp[i][0],
					specGrp[i][1],
					specGrp[i][2], specGrp[i][3], specGrp[i][4],
					specGrp[i][5], specGrp[i][6]);
			this.add(cmdG);
		}
		for (int i=0; i<specCmd.length; i++) {
			if (specCmd[i][1].equals("")) {
				specCmd[i][1] = specCmd[i][0];
			}
			cmdE = new CmdEntry(
					specCmd[i][0],		// the cmd 
					specCmd[i][1],		// the display text
					specCmd[i][3],		// the group
					specCmd[i][4]		// the tooltip
			);
			cmdE.setCmdType(specCmd[i][2]);
			if (specCmd[i].length>SPECCMD_FIX_COLS) {
				// add image names:
				for (int j=SPECCMD_GFX_COLS; j<specCmd[i].length; j++) {
					cmdE.setIconName(j-SPECCMD_GFX_COLS, specCmd[i][j]);
				}
				cmdE.loadImages(mt);
				if (specCmd[i][SPECCMD_FIX_COLS].equals("true")) {
					cmdE.setAdjustIconColor(true);
				}
			} else {
				if (stdBtnAry!=null) {
					for (int j=0; j<stdBtnAry.length; j++) {
						if (stdBtnAry[j]!=null && !stdBtnAry[j].equals("")) {
							cmdE.setIconName(j, stdBtnAry[j]);
						}
					}
					cmdE.loadImages(mt);
				}
			}
			this.add(cmdE);
		}
		try {
			mt.waitForAll();
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}
}
