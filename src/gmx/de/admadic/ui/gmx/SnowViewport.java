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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JViewport;

/**
 * @author Rainer Schwarze
 *
 */
public class SnowViewport extends JViewport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SnowEngine se;
	ComponentListener viewCL;
	ComponentListener viewportCL;

	/**
	 * 
	 */
	public SnowViewport() {
		super();
		viewCL = new ComponentListener() {
			public void componentResized(ComponentEvent e) { /* x */ }
			public void componentMoved(ComponentEvent e) { /* x */ }
			public void componentShown(ComponentEvent e) {
//				System.out.println("show");
				if (se!=null) se.setActive(true);
			}
			public void componentHidden(ComponentEvent e) {
//				System.out.println("hide");
				if (se!=null) se.setActive(false);
			}
		};
		viewportCL = new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				if (se!=null) {
//					System.out.println("resize");
					se.setTarget(getView(), SnowViewport.this);
				}
			}
			public void componentMoved(ComponentEvent e) { /* x */ }
			public void componentShown(ComponentEvent e) { /* x */ }
			public void componentHidden(ComponentEvent e) { /* x */ }
		};
		this.addComponentListener(viewportCL);
	}

	/**
	 * @param se
	 */
	public void setSnowEngine(SnowEngine se) {
		this.se = se;
//		System.out.println("set");
		se.setTarget(getView(), this);
		if (getView()!=null && getView().isVisible()) {
			se.setActive(true);
		}
	}

	/**
	 * @param g
	 * @see javax.swing.JViewport#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (se!=null) se.paint(g);
	}

	/**
	 * @param view
	 * @see javax.swing.JViewport#setView(java.awt.Component)
	 */
	@Override
	public void setView(Component view) {
		if (getView()!=null) {
			getView().removeComponentListener(viewCL);
		}
//		System.out.println("setView");
		super.setView(view);
		if (view!=null) {
//			System.out.println("add");
			view.addComponentListener(viewCL);
		}
		if (view.isVisible() && se!=null) {
//			System.out.println("isVis");
			se.setTarget(view, this);
			se.setActive(true);
		}
	}
}
