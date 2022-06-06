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

import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * @author Rainer Schwarze
 *
 */
public class ProcessorUIActionFactory {

	/**
	 * 
	 */
	public ProcessorUIActionFactory() {
		super();
	}

	/**
	 * @param processor 
	 * @param key 
	 * @return	Returns the created processor action.
	 */
	public static ProcessorUIAction createProcessorUIAction(
			IProcessor processor, String key) {
		ProcessorUIAction pa = null;
		String paname = null;
		if (key.equals("0")) paname = ProcessorAction.PA_0;
		if (key.equals("1")) paname = ProcessorAction.PA_1;
		if (key.equals("2")) paname = ProcessorAction.PA_2;
		if (key.equals("3")) paname = ProcessorAction.PA_3;
		if (key.equals("4")) paname = ProcessorAction.PA_4;
		if (key.equals("5")) paname = ProcessorAction.PA_5;
		if (key.equals("6")) paname = ProcessorAction.PA_6;
		if (key.equals("7")) paname = ProcessorAction.PA_7;
		if (key.equals("8")) paname = ProcessorAction.PA_8;
		if (key.equals("9")) paname = ProcessorAction.PA_9;
		if (key.equals("+")) paname = ProcessorAction.PA_ADD;
		if (key.equals("-")) paname = ProcessorAction.PA_SUB;
		if (key.equals("*")) paname = ProcessorAction.PA_MUL;
		if (key.equals("/")) paname = ProcessorAction.PA_DIV;
		if (key.equals(".")) paname = ProcessorAction.PA_DOT;
		if (key.equals("=")) paname = ProcessorAction.PA_EXE;
		if (key.equals("%")) paname = ProcessorAction.PA_PERCENT;
		if (key.equals("escape")) paname = ProcessorAction.PA_CLR_ALL;
		if (key.equals("enter")) paname = ProcessorAction.PA_EXE;
		if (key.equals("backspace")) paname = ProcessorAction.PA_BACKSPACE;
		pa = new ProcessorUIAction(processor, paname);
		return pa;
	}

	/**
	 * @param processor
	 * @param comp	The component (if a rootpane is to be accessed, pass that!)
	 */
	public static void fillMaps(
			IProcessor processor, 
			JComponent comp) {
		InputMap iMap = comp.getInputMap(
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap aMap = comp.getActionMap();
		fillMaps(processor, iMap, aMap);
	}

	/**
	 * @param processor
	 * @param iMap
	 * @param aMap
	 */
	public static void fillMaps(
			IProcessor processor, 
			InputMap iMap, ActionMap aMap) {
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER	, 0), "enter"	);
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE	, 0), "escape"	);
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "backspace"	);
		iMap.put(KeyStroke.getKeyStroke('+'), "+"		);
		iMap.put(KeyStroke.getKeyStroke('-'), "-"		);
		iMap.put(KeyStroke.getKeyStroke('*'), "*"		);
		iMap.put(KeyStroke.getKeyStroke('/'), "/"		);
		iMap.put(KeyStroke.getKeyStroke('.'), "."		);
		iMap.put(KeyStroke.getKeyStroke(','), "."		);
		iMap.put(KeyStroke.getKeyStroke('0'), "0"		);
		iMap.put(KeyStroke.getKeyStroke('1'), "1"		);
		iMap.put(KeyStroke.getKeyStroke('2'), "2"		);
		iMap.put(KeyStroke.getKeyStroke('3'), "3"		);
		iMap.put(KeyStroke.getKeyStroke('4'), "4"		);
		iMap.put(KeyStroke.getKeyStroke('5'), "5"		);
		iMap.put(KeyStroke.getKeyStroke('6'), "6"		);
		iMap.put(KeyStroke.getKeyStroke('7'), "7"		);
		iMap.put(KeyStroke.getKeyStroke('8'), "8"		);
		iMap.put(KeyStroke.getKeyStroke('9'), "9"		);
		iMap.put(KeyStroke.getKeyStroke('='), "="		);
		iMap.put(KeyStroke.getKeyStroke('%'), "%"		);
		
		aMap.put("enter"	, createProcessorUIAction(processor, "enter"));
		aMap.put("escape"	, createProcessorUIAction(processor, "escape"));
		aMap.put("backspace", createProcessorUIAction(processor, "backspace"));
		aMap.put("0"		, createProcessorUIAction(processor, "0"));
		aMap.put("1"		, createProcessorUIAction(processor, "1"));
		aMap.put("2"		, createProcessorUIAction(processor, "2"));
		aMap.put("3"		, createProcessorUIAction(processor, "3"));
		aMap.put("4"		, createProcessorUIAction(processor, "4"));
		aMap.put("5"		, createProcessorUIAction(processor, "5"));
		aMap.put("6"		, createProcessorUIAction(processor, "6"));
		aMap.put("7"		, createProcessorUIAction(processor, "7"));
		aMap.put("8"		, createProcessorUIAction(processor, "8"));
		aMap.put("9"		, createProcessorUIAction(processor, "9"));
		aMap.put("+"		, createProcessorUIAction(processor, "+"));
		aMap.put("-"		, createProcessorUIAction(processor, "-"));
		aMap.put("*"		, createProcessorUIAction(processor, "*"));
		aMap.put("/"		, createProcessorUIAction(processor, "/"));
		aMap.put("."		, createProcessorUIAction(processor, "."));
		aMap.put("="		, createProcessorUIAction(processor, "="));
		aMap.put("%"		, createProcessorUIAction(processor, "%"));
	}
}
