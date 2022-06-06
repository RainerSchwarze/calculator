/**
 *  
 *  Based on <code>GraphPaperLayout</code> written by Micheal Martak/sun
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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcPanel extends JPanel implements 
		MouseListener, 
		MouseMotionListener,
		ComponentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image bgImage = null;
	JFrame frame = null;
	Point lastp;
	boolean dragging = false;
	boolean dragging2 = false;
	JWindow hint = null;

	/**
	 * @param imgname
	 */
	public void initBgImage(String imgname) {
		MediaTracker mt = new MediaTracker(this);
		java.net.URL url = this.getClass().getClassLoader().getResource(
				"de/admadic/calculator/ui/res/" + imgname);
		bgImage = Toolkit.getDefaultToolkit().getImage(url);
		mt.addImage(bgImage, 0);
		try {
			mt.waitForAll();
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}
	@Override
	protected void paintComponent(Graphics arg0) {
		if (bgImage==null) {
			super.paintComponent(arg0);
		} else {
			//arg0.drawImage(bgImage, 0, 0, null, null);
			arg0.drawImage(bgImage, 
					0, 0, getWidth(), getHeight(),
					0, 0, bgImage.getWidth(null), bgImage.getHeight(null),
					null);
		}
	}

	/**
	 * @param fr
	 */
	public void linkSurfaceDragging(JFrame fr) {
		this.frame = fr;
		if (frame!=null) {
			addMouseMotionListener(this);
			addMouseListener(this);
			frame.addComponentListener(this);
		}
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if (dragging) {
			if (hint==null) {
				hint = new JWindow();
				hint.setBounds(frame.getBounds());
				//hint.setUndecorated(true);
				hint.setVisible(true);
				//hint.toBack();
				frame.toFront();
			}
			//System.out.println(e);
			Point p = hint.getLocation();
			if (lastp.distance(e.getPoint())==0.0) {
				return;
			}
			p.translate(e.getX()-lastp.x, e.getY()-lastp.y);
			lastp.setLocation(e.getPoint());
			//dragging2 = true;
			//frame.setLocation(p);
			hint.setLocation(p);
		}
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		/* nothing */
	}
	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		/* nothing */
	}
	/**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		/* nothing */
	}
	/**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		/* nothing */
	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		//System.out.println(e);
		dragging = true;
		if (lastp==null) {
			lastp = new Point(e.getPoint());
		} else {
			lastp.setLocation(e.getPoint());
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		//System.out.println(e);
		dragging = false;
		if (hint!=null) {
			Point p = hint.getLocation();
			hint.dispose();
			hint = null;
			frame.setLocation(p);
		}
	}
	/**
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e) {
		/* nothing */
	}

	/**
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
		/* nothing */
		//System.out.println(e);
		//dragging2 = false;
	}

	/**
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
		/* nothing */
	}

	/**
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent e) {
		/* nothing */
	}
}
