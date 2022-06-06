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


import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Icon;

/**
 * @author Rainer Schwarze
 *
 */
public class ComponentStateAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Component component;
	private ComponentStateActionListener componentStateActionListener;

	/**
	 * @param component 
	 * @param csal 
	 * 
	 */
	public ComponentStateAction(
			Component component, 
			ComponentStateActionListener csal) {
		super();
		this.component = component;
		this.componentStateActionListener = csal;
	}

	/**
	 * @param name
	 * @param icon
	 */
	public ComponentStateAction(String name, Icon icon) {
		super(name, icon);
	}

	/**
	 * @param name
	 */
	public ComponentStateAction(String name) {
		super(name);
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object src;
		src = e.getSource();
		if (src!=null && src instanceof AbstractButton) {
			AbstractButton btn = (AbstractButton)src;
			boolean vis = btn.isSelected();
			if (componentStateActionListener!=null) {
				componentStateActionListener.stateChanged(vis);
			}
		}
	}

	/**
	 * @return Returns the componentStateActionListener.
	 */
	public ComponentStateActionListener getComponentStateActionListener() {
		return componentStateActionListener;
	}

	/**
	 * @param componentStateActionListener The componentStateActionListener to set.
	 */
	public void setComponentStateActionListener(
			ComponentStateActionListener componentStateActionListener) {
		this.componentStateActionListener = componentStateActionListener;
	}
}
