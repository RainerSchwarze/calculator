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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * @author Rainer Schwarze
 *
 */
public class ProcessorUIAction extends AbstractAction {
	ProcessorAction action;
	IProcessor processor;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param processor 
	 * 
	 */
	public ProcessorUIAction(IProcessor processor) {
		super();
		this.action = null;
		this.processor = processor;
	}

	/**
	 * @param processor 
	 * @param name
	 * @param icon
	 */
	public ProcessorUIAction(IProcessor processor, String name, Icon icon) {
		super(name, icon);
		this.action = new ProcessorAction(name);
		this.processor = processor;
	}

	/**
	 * @param processor 
	 * @param name
	 */
	public ProcessorUIAction(IProcessor processor, String name) {
		super(name);
		this.action = new ProcessorAction(name);
		this.processor = processor;
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// FIXME: the exception situation is not good with actionPerformed! fix that.
		if (processor!=null && action!=null) {
			try {
				processor.processCommand(action.getActionCmd());
			} catch (ProcessorException e1) {
				//e1.printStackTrace();
				// now what?
			}
		}
	}
}
