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
package de.admadic.calculator.appctx;

import javax.swing.event.EventListenerList;

/**
 * @author Rainer Schwarze
 *
 */
public class AppEventListenerSupport {
	EventListenerList listenerList = new EventListenerList();

	Object eventSource;
	IAppContext appContext;
	
	/**
	 * @param eventSource 
	 * @param appContext 
	 */
	public AppEventListenerSupport(Object eventSource, IAppContext appContext) {
		super();
		this.eventSource = eventSource;
		this.appContext = appContext;
	}

	/**
	 * @param l
	 */
	public void addAppListener(AppEventListener l) {
		listenerList.add(AppEventListener.class, l);
	}

	/**
	 * @param l
	 */
	public void removeAppListener(AppEventListener l) {
		listenerList.remove(AppEventListener.class, l);
	}

	/**
	 * 
	 * @param phase
	 * @throws CancelPhaseException 
	 */
	public void fireAppEvent(int phase) throws CancelPhaseException {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		AppEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==AppEventListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new AppEvent(eventSource, appContext);
					e.setPhase(phase);
				}
				((AppEventListener)listeners[i+1]).processPhase(e);
			}
		}
	}
}
