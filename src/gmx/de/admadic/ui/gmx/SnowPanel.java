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
package de.admadic.ui.gmx;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

/**
 * @author Rainer Schwarze
 *
 */
public class SnowPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SnowEngine se;
	Container container;
	
	/**
	 * @param container 
	 * 
	 */
	public SnowPanel(Container container) {
		this(container, SnowConstants.REST_NORTH);
	}
	
	/**
	 * @param container 
	 * @param flags 
	 * 
	 */
	public SnowPanel(Container container, int flags) {
		super();
		this.container = container;
		this.setOpaque(false);
		se = new SnowEngine(container, null, flags);
	}
	
	/**
	 * 
	 */
	public void shake() {
		if (se!=null) se.shake();
	}
	
	/**
	 * @param arg
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean arg) {
		super.setVisible(arg);
		se.setActive(arg);
	}
	
	/**
	 * @param p
	 * @return	Returns always false. The mouse shall never be active over this
	 * panel.
	 * @see java.awt.Component#contains(java.awt.Point)
	 */
	@Override
	public boolean contains(Point p) {
		// we return always false - because that may prevent mouse events
		// return super.contains(p);
		return false;
	}
	
	
	
	/**
	 * @param arg0
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics arg0) {
		// super.paintComponent(arg0);
		se.paint(arg0);
	}
}
