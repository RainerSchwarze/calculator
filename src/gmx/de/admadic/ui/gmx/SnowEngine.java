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
import java.awt.Container;
import java.awt.Graphics;

import javax.swing.JViewport;

/**
 * @author Rainer Schwarze
 *
 */
public class SnowEngine implements Runnable {
	int sleeptime;
	int threshold;
	int counter;
	long lastshake;
	Thread ticker;
	SnowWorld sw;
	Container container;
	Component target;
	Component sizeTarget;

	/**
	 * Creates a new SnowEngine and installs it in the first JScrollPane
	 * found in the given container. The SnowEngine is returned. 
	 * The method returns <code>null</code> if a JScrollPane could not be 
	 * found.
	 * This method installs a SnowViewport as a viewport to the JScrollPane.
	 * 
	 * @param container 
	 * @param flags 
	 * @return	Returns the snowEngine, already linked to the JScrollPane
	 */
	public static SnowEngine createInFirstScrollpane(
			Container container,
			int flags) {
		SnowEngine snowEngine = null;
		javax.swing.JScrollPane scroll = null;
		Component [] ca = container.getComponents();
		for (int i = 0; i < ca.length; i++) {
			Component c = ca[i];
			if (c instanceof javax.swing.JScrollPane) {
				scroll = (javax.swing.JScrollPane)c;
				break;
			}
		}
		if (scroll==null) 
			return null;
//		System.out.println("doing snowengine");
        SnowViewport svp = new SnowViewport();
        JViewport vp = scroll.getViewport();
        Component view = vp.getView();
        scroll.setViewport(svp);
        scroll.setViewportView(view);
        snowEngine = new SnowEngine(
        		null, view, svp, flags);
        svp.setSnowEngine(snowEngine);
		svp.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        return snowEngine;
	}

	/**
	 * Creates a new SnowEngine and installs it in the first JScrollPane
	 * found in the given container. The SnowEngine is returned. 
	 * The method returns <code>null</code> if a JScrollPane could not be 
	 * found.
	 * This method installs a SnowViewport as a viewport to the JScrollPane.
	 * 
	 * @param container 
	 */
	public static void removeFromFirstScrollpane(
			Container container) {
		SnowEngine snowEngine = null;
		javax.swing.JScrollPane scroll = null;
		Component [] ca = container.getComponents();
		for (int i = 0; i < ca.length; i++) {
			Component c = ca[i];
			if (c instanceof javax.swing.JScrollPane) {
				scroll = (javax.swing.JScrollPane)c;
				break;
			}
		}
		if (scroll==null) 
			return;	// FIXME: maybe error?

//		System.out.println("doing snowengine");
        JViewport vp = scroll.getViewport();
        if (!(vp instanceof SnowViewport)) 
        	return;
        SnowViewport svp = (SnowViewport)vp;
        Component view = vp.getView();
        scroll.setViewport(null);
        scroll.setViewportView(view);	// lets create std
        snowEngine = svp.se;
        if (snowEngine!=null) snowEngine = null;
        svp = null;
	}
	
	/**
	 * @param container 
	 * @param target 
	 * @param flags 
	 * 
	 */
	public SnowEngine(Container container, Component target, int flags) {
		this(container, target, null, flags);
	}

	/**
	 * @param container 
	 * @param target 
	 * @param sizeTarget 
	 * @param flags 
	 * 
	 */
	public SnowEngine(Container container, Component target, Component sizeTarget, int flags) {
		super();
		this.container = container;
		sw = new SnowWorld(flags);
		setTarget(target, sizeTarget);
		counter = 0;
		threshold = 10;
		sleeptime = 50;
		lastshake = -1;
	}

	/**
	 * @param target
	 */
	public void setTarget(Component target) {
		setTarget(target, null);
	}

	/**
	 * @param target
	 * @param sizeTarget 
	 */
	public void setTarget(Component target, Component sizeTarget) {
//		System.out.println("setTarget");
		this.container = null;
		this.sizeTarget = sizeTarget;
		if (ticker!=null) {
			if (this.target!=null) setActive(false);
			this.target = target;
			if (this.target!=null) setActive(true);
		} else {
			this.target = target;
		}
	}

	/**
	 * 
	 */
	public void shake() {
		if (sw!=null) {
			long thisshake = System.currentTimeMillis();
			if ((thisshake-lastshake)>200) {
				sw.shake();
				lastshake = thisshake;
			}
		}
	}
	
	/**
	 * @param arg
	 */
	public void setActive(boolean arg) {
		if (arg) {
			if (ticker==null) {
				counter = 0;
				if (container!=null) {
					sw.initFromContainer(container);
				} else {
					sw.initFromComponent(target, sizeTarget);
				}
				ticker = new Thread(this);
				ticker.start();
			}
		} else {
			if (ticker!=null) {
				ticker = null;
			}
		}
	}

	/**
	 * @param g
	 */
	public void paint(Graphics g) {
//		System.out.println("paint");
		if (sw!=null) sw.paint(g);
	}
	
	/**
	 * 
	 */
	synchronized public void updateSnow() {
		sw.updateSnow();
	}

	/**
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Thread curT = Thread.currentThread();
		while (ticker!=null && curT==ticker) {
			counter++;
			if (counter<threshold) {
				// not yet
			} else {
				if (container!=null && container.isVisible()) {
					updateSnow();
					this.container.repaint();
				}
				if (target!=null && target.isVisible()) {
					updateSnow();
	//				System.out.println("tick");
					if (sizeTarget!=null) {
						sizeTarget.repaint();
					} else {
						target.repaint();
					}
				}
			}
			try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				/* nothing */
			}
		}
	}
}
