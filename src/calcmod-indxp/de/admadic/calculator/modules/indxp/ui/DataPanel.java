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
package de.admadic.calculator.modules.indxp.ui;

import java.awt.Component;
import java.awt.Container;
import java.util.Vector;

import javax.swing.JPanel;

import de.admadic.calculator.modules.indxp.core.DataEventDispatcher;
import de.admadic.calculator.modules.indxp.core.DataEventServer;
import de.admadic.calculator.modules.indxp.core.DataItemStatusServer;

/**
 * @author Rainer Schwarze
 *
 */
public class DataPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected DataEventDispatcher dataEventDispatcher;
	protected DataEventServer dataEventServer;
	protected DataItemStatusServer dataItemStatusServer;

	/**
	 * 
	 */
	public DataPanel() {
		super();
	}

	/**
	 * @return Returns the dataEventDispatcher.
	 */
	public DataEventDispatcher getDataEventDispatcher() {
		return dataEventDispatcher;
	}

	/**
	 * @param dataEventDispatcher The dataEventDispatcher to set.
	 */
	public void setDataEventDispatcher(DataEventDispatcher dataEventDispatcher) {
		this.dataEventDispatcher = dataEventDispatcher;
	}

	/**
	 * @return Returns the dataEventServer.
	 */
	public DataEventServer getDataEventServer() {
		return dataEventServer;
	}

	/**
	 * @param dataEventServer The dataEventServer to set.
	 */
	public void setDataEventServer(DataEventServer dataEventServer) {
		this.dataEventServer = dataEventServer;
	}

	/**
	 * @return Returns the dataItemStatusServer.
	 */
	public DataItemStatusServer getDataItemStatusServer() {
		return dataItemStatusServer;
	}

	/**
	 * @param dataItemStatusServer The dataItemStatusServer to set.
	 */
	public void setDataItemStatusServer(DataItemStatusServer dataItemStatusServer) {
		this.dataItemStatusServer = dataItemStatusServer;
		updateLockStatus();
	}

	/**
	 * 
	 */
	public void updateLockStatus() {
		// override in subclasses!
	}

	/**
	 * @param parent
	 * @param enable
	 * @param except
	 */
	public static void enableComponents(
			Container parent, boolean enable, Component except) {
		enableComponents(parent, enable, new Component[]{except});
	}

	/**
	 * @param parent
	 * @param enable
	 * @param except
	 */
	public static void enableComponents(
			Container parent, boolean enable, Component [] except) {
		Component [] ca = parent.getComponents();
		Vector<Component> excV = new Vector<Component>();
		for (Component c : except) {
			excV.add(c);
		}
		for (Component c : ca) {
			if (excV.contains(c)) continue;
			c.setEnabled(enable);
		}
	}
}
