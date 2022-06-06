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
package de.admadic.calculator.modules.matrx.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * @author Rainer Schwarze
 *
 */
public class QuarterSplitPane extends JPanel implements PropertyChangeListener {
	/** */
	private static final long serialVersionUID = 1L;

	JSplitPane spHorz;
	JSplitPane spVertUpr;
	JSplitPane spVertLwr;

	Object forgetSync = new Object();
	int forgetNextUprEvent;
	int forgetNextLwrEvent;

	/**
	 * 
	 */
	public QuarterSplitPane() {
		super();
		initContents();
	}

	/**
	 * 
	 */
	void initContents() {
		this.setLayout(new BorderLayout());
		spHorz = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.add(spHorz, BorderLayout.CENTER);
		spVertUpr = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		spVertLwr = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		spHorz.setTopComponent(spVertUpr);
		spHorz.setBottomComponent(spVertLwr);

		spVertUpr.add(createTableScroll(), JSplitPane.LEFT);
		spVertUpr.add(createTableScroll(), JSplitPane.RIGHT);
		spVertLwr.add(createTableScroll(), JSplitPane.LEFT);
		spVertLwr.add(createTableScroll(), JSplitPane.RIGHT);

		spVertUpr.addPropertyChangeListener(
				JSplitPane.DIVIDER_LOCATION_PROPERTY, this);
		spVertLwr.addPropertyChangeListener(
				JSplitPane.DIVIDER_LOCATION_PROPERTY, this);

		spVertUpr.setContinuousLayout(true);
		spVertLwr.setContinuousLayout(true);
	}

	/**
	 * @return	Returns a scroll pane
	 */
	private JScrollPane createTableScroll() {
		JScrollPane sp = new JScrollPane();
		TableModel tm = new DefaultTableModel(
			new String[][] { { "One", "Two" },
					{ "Three", "Four" } },
			new String[] { "Column 1", "Column 2" });
		JTable tbl = new JTable();
		sp.setViewportView(tbl);
		tbl.setModel(tm);
		return sp;
	}

	/**
	 * @param evt
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName () == JSplitPane.DIVIDER_LOCATION_PROPERTY)  {
			Integer newpos = (Integer)evt.getNewValue();
			synchronized(forgetSync) {
				if (evt.getSource()==spVertUpr) {
					if (forgetNextUprEvent>0) {
						forgetNextUprEvent--;
						return;
					}
					forgetNextLwrEvent++;
					spVertLwr.setDividerLocation(newpos.intValue());
				}
				if (evt.getSource()==spVertLwr) {
					if (forgetNextLwrEvent>0) {
						forgetNextLwrEvent--;
						return;
					}
					forgetNextUprEvent++;
					spVertUpr.setDividerLocation(newpos.intValue());
				}
			}
		}
	}

	/**
	 * @param propLocHorz
	 * @param propLocVert 
	 * @see javax.swing.JSplitPane#setDividerLocation(double)
	 */
	public void setDividerLocation(double propLocHorz, double propLocVert) {
		spHorz.setResizeWeight(propLocHorz);
		spHorz.setDividerLocation(propLocHorz);
		spVertUpr.setResizeWeight(propLocVert);
		spVertLwr.setResizeWeight(propLocVert);
		spVertUpr.setDividerLocation(propLocVert);
	}

	/**
	 * @param comp
	 * @see javax.swing.JSplitPane#setRightComponent(java.awt.Component)
	 */
	public void setTopRightComponent(Component comp) {
		spVertUpr.setRightComponent(comp);
	}
	
	/**
	 * @param comp
	 * @see javax.swing.JSplitPane#setRightComponent(java.awt.Component)
	 */
	public void setTopLeftComponent(Component comp) {
		spVertUpr.setLeftComponent(comp);
	}

	/**
	 * @param comp
	 * @see javax.swing.JSplitPane#setRightComponent(java.awt.Component)
	 */
	public void setBottomRightComponent(Component comp) {
		spVertLwr.setRightComponent(comp);
	}

	/**
	 * @param comp
	 * @see javax.swing.JSplitPane#setRightComponent(java.awt.Component)
	 */
	public void setBottomLeftComponent(Component comp) {
		spVertLwr.setLeftComponent(comp);
	}
}