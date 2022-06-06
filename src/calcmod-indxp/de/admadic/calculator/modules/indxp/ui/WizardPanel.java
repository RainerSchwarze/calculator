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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Rainer Schwarze
 *
 */
public class WizardPanel extends JPanel 
implements ActionListener, ChangeListener {
	JTabbedPane tabbedPane;
	JPanel panelButtons;
	JButton btnPrev;
	JButton btnNext;
	final static String CMD_NEXT = "wiz.next";
	final static String CMD_PREV = "wiz.prev";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public WizardPanel() {
		super();
		initialize();
	}

	private void initialize() {
		BorderLayout l = new BorderLayout();
		// GridLayout l = new GridLayout(1, 1);
		this.setLayout(l);
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane, BorderLayout.CENTER);
		// this.add(tabbedPane);
		tabbedPane.addChangeListener(this);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		panelButtons = new JPanel();
		this.add(panelButtons, BorderLayout.PAGE_END);
		btnNext = new JButton("Next >");
		btnNext.setActionCommand(CMD_NEXT);
		btnNext.addActionListener(this);
		btnPrev = new JButton("< Back");
		btnPrev.setActionCommand(CMD_PREV);
		btnPrev.addActionListener(this);
		panelButtons.add(btnPrev);
		panelButtons.add(btnNext);
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_NEXT)) {
			doNext();
		} else if (cmd.equals(CMD_PREV)) {
			doPrev();
		}
	}

	/**
	 * @param panel
	 */
	public void addPanel(JPanel panel) {
		addPanel(panel, null);
	}

	/**
	 * @param panel
	 * @param name 
	 */
	public void addPanel(JPanel panel, String name) {
		if (name==null) {
			name = "Panel" + tabbedPane.getTabCount() + 1;
		}
		tabbedPane.addTab(name, panel);
		updateButtonState();
	}

	/**
	 * @return	Returns number of panels
	 */
	public int getPanelCount() {
		return tabbedPane.getTabCount();
	}

	/**
	 * @param idx
	 */
	public void removePanel(int idx) {
		tabbedPane.removeTabAt(idx);
		updateButtonState();
	}

	/**
	 * 
	 */
	public void doPrev() {
		int idx = tabbedPane.getSelectedIndex();
		if (idx<0) {
			// ? what now?
			return;
		}
		idx--;
		if (idx>=0) {
			tabbedPane.setSelectedIndex(idx);
		}
	}

	/**
	 * 
	 */
	public void doNext() {
		int idx = tabbedPane.getSelectedIndex();
		if (idx<0) {
			// ? what now?
			return;
		}
		idx++;
		if (idx<tabbedPane.getTabCount()) {
			tabbedPane.setSelectedIndex(idx);
		}
	}


	/**
	 * @param e
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		Object src = e.getSource();
		if (src!=tabbedPane) return;
		updateButtonState();
	}

	protected void updateButtonState() {
		int sel = tabbedPane.getSelectedIndex();
		if (sel<0) return;
		boolean enaB = true;
		boolean enaN = true;
		if (sel==0) { // disable "back"
			enaB = false;
		}
		if (sel==(tabbedPane.getTabCount()-1)) { // disable "next"
			enaN = false;
		}
		btnPrev.setEnabled(enaB);
		btnNext.setEnabled(enaN);
	}
}
