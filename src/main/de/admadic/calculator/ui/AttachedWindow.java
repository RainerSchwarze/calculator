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

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;

/**
 * @author Rainer Schwarze
 *
 */
public class AttachedWindow extends JDialog 
		implements 	ComponentListener, ActionListener, 
					ItemListener {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	PlacementListener placementListener;

	JButton snapBtn;
	JToggleButton lockBtn;
	JPanel btnPanel;
	Image pinDown;
	Image pinUp;
	Image snap;

	boolean deferFocus = true;
	boolean ignoreNextMoveEvent = false;
	boolean ignoreNextResizeEvent = false;

	final static String CMD_SNAP = "snap";
	final static String CMD_LOCK = "lock";

	/**
	 * @param owner
	 */
	public AttachedWindow(JFrame owner) {
		super(owner);
		this.placementListener = null;
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.getRootPane().setDefaultButton(null);

		this.getContentPane().setLayout(new BorderLayout());
		btnPanel = new JPanel();

		java.net.URL url;
		ClassLoader cl = this.getClass().getClassLoader();
		url = cl.getResource(
				"de/admadic/calculator/ui/res/atw-pin-down.png");
		pinDown = Toolkit.getDefaultToolkit().getImage(url);
		url = cl.getResource(
				"de/admadic/calculator/ui/res/atw-pin-up.png");
		pinUp = Toolkit.getDefaultToolkit().getImage(url);
		url = cl.getResource(
				"de/admadic/calculator/ui/res/atw-snap.png");
		snap = Toolkit.getDefaultToolkit().getImage(url);

		snapBtn = new JButton(); // snap
		snapBtn.setActionCommand(CMD_SNAP);
		snapBtn.addActionListener(this);
		snapBtn.setMargin(new java.awt.Insets(1, 1, 1, 1));
		btnPanel.add(snapBtn);
		snapBtn.setIcon(new ImageIcon(snap));

		lockBtn = new JToggleButton(); // lock
		lockBtn.setActionCommand(CMD_LOCK);
		lockBtn.addItemListener(this);
		lockBtn.setMargin(new java.awt.Insets(1, 1, 1, 1));
		btnPanel.add(lockBtn);
		lockBtn.setSelectedIcon(new ImageIcon(pinDown));
		lockBtn.setRolloverSelectedIcon(new ImageIcon(pinDown));
		lockBtn.setIcon(new ImageIcon(pinUp));

		//this.getContentPane().add(btnPanel, BorderLayout.NORTH);
		addComponentListener(this);
	}

	/**
	 * @param l
	 */
	public void addPlacementListener(PlacementListener l) {
		placementListener = l;
	}

	/**
	 * @param l
	 */
	public void removePlacementListener(PlacementListener l) {
		if (l==null) { /* no warn */ }
		placementListener = null;
	}

	/**
	 * @param panel
	 */
	public void placePanel(JPanel panel) {
		placePanel(panel, 0, 5, 5, 5);
	}

	/**
	 * @param panel
	 * @param btop
	 * @param bleft
	 * @param bbottom
	 * @param bright
	 */
	public void placePanel(
			JPanel panel, 
			int btop, int bleft, int bbottom, int bright) {
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
				btop, bleft, bbottom, bright));
		this.pack();
	}

	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
		if (ignoreNextResizeEvent) {
			ignoreNextResizeEvent = false;
			//System.out.println("atw: ignored");
			return;
		}
		//System.out.println("atw: resize: " + e.getComponent().getBounds());
		// + " source=" + e.getSource().getClass() + " comp=" + e.getComponent().getName());
		if (placementListener!=null) {
			placementListener.notifyResize(this);
		}
	}

	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
		if (ignoreNextMoveEvent) {
			ignoreNextMoveEvent = false;
			//System.out.println("atw: ignored");
			return;
		}
		//System.out.println("atw: move: " + e.getComponent().getBounds());
		// + " source=" + e.getSource().getClass() + " comp=" + e.getComponent().getName());
		if (placementListener!=null) {
			placementListener.notifyMove(this);
		}
	}
	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent e) {
		// nothing
	}
	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e) {
		// nothing
	}
	/**
	 * @return Returns the ignoreNextEvent.
	 */
	public boolean isIgnoreNextMoveEvent() {
		return ignoreNextMoveEvent;
	}
	/**
	 * @param ignoreNextMoveEvent The ignoreNextMoveEvent to set.
	 */
	public void setIgnoreNextMoveEvent(boolean ignoreNextMoveEvent) {
		this.ignoreNextMoveEvent = ignoreNextMoveEvent;
	}
	/**
	 * @return Returns the ignoreNextResizeEvent.
	 */
	public boolean isIgnoreNextResizeEvent() {
		return ignoreNextResizeEvent;
	}
	/**
	 * @param ignoreNextResizeEvent The ignoreNextResizeEvent to set.
	 */
	public void setIgnoreNextResizeEvent(boolean ignoreNextResizeEvent) {
		this.ignoreNextResizeEvent = ignoreNextResizeEvent;
	}
	/**
	 * @param b
	 * @see java.awt.Component#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean b) {
		if (deferFocus) {
			// FIXME: turn that on again!
//			java.awt.Component fc = null;
//			fc = awm.mainWindow.getFocusOwner();
			super.setVisible(b);
//			if (b && fc!=null) {
//				fc.requestFocus();
//			}
		} else {
			super.setVisible(b);
		}
	}

	/**
	 * @param r
	 */
	public void placeWindow(Rectangle r) {
		setIgnoreNextMoveEvent(true);
		setBounds(r);
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_SNAP)) {
			if (placementListener!=null) {
				placementListener.snap(this);
			}
		}
	}
	/**
	 * @param e
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource().equals(lockBtn)) {
			boolean lock = (e.getStateChange()==ItemEvent.SELECTED);
			if (placementListener!=null) {
				placementListener.setMotionLock(this, lock);
			}
		}
	}
}