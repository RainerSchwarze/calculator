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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcDisplayExt extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	class Display extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String display2;
		String status;

		Font displayFont;
		Font statusFont;

		/**
		 * @param display
		 * @param status
		 */
		public void updateText(String display, String status) {
			this.display2 = display;
			this.status = status;
			this.displayFont = new Font("Arial", Font.PLAIN, 20);
			this.statusFont = new Font("Arial", Font.PLAIN, 9);
		}
		/**
		 * @param g
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Insets insets = getInsets();
			Graphics2D g2d = (Graphics2D)g.create();

			Font font;
			FontMetrics fm;
			Rectangle2D rdsp;
//			Rectangle2D rsta;

			font = displayFont;
			int maxSteps = 3;
			int i = 0;
			while (true) {
				// second step and beyond:
				if (i>0) {
					font = font.deriveFont(font.getSize()*0.80f);
				}
				fm = g2d.getFontMetrics(font); 
				rdsp = fm.getStringBounds(display2, g2d);
				if (rdsp.getWidth()<=(getWidth()-insets.left-insets.right)) {
					break;
				}
				if (++i >= maxSteps) break;
			}
			g2d.setFont(font);
			g2d.drawString(
					display2, 
					getWidth()-insets.right - (int)rdsp.getWidth(), 
					insets.top + fm.getMaxAscent());
//			rsta = fm.getStringBounds(status, g2d);

			fm = g2d.getFontMetrics(statusFont); 
			g2d.setFont(statusFont);
//			g2d.drawString(
//					status, 
//					insets.left, 
//					insets.top + (int)rdsp.getHeight() + fm.getMaxAscent());
			g2d.drawString(
					status, 
					insets.left, 
					getHeight() - insets.bottom - fm.getMaxDescent());

			g2d.dispose();
		}
		
	}
	String lastDisplay;
	String lastStatus;
	Display display;

//	private Color displayColor = new Color(220, 220, 220);

	/**
	 * 
	 */
	public CalcDisplayExt() {
		display = new Display();
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		add(display, BorderLayout.CENTER);

		display.updateText("(display)", "(status)");
		display.setFont(new java.awt.Font("Courier New",1,14));
		//display.setColumns(20);
		//display.setHorizontalAlignment(JTextField.RIGHT);
		//display.setEditable(false);
		display.setAutoscrolls(false);
//		display.setBackground(displayColor);
		display.setAlignmentX(0.0f);
		display.setOpaque(false);
		//display.setBorder(null);
		//display.setMargin(new Insets(2, 2, 2, 2));
		//display.setB

//		this.setBackground(displayColor);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	/**
	 * @param s
	 */
	public void updateDisplay(String s) {
		updateOutput(s, lastStatus);
	}

	/**
	 * @param s
	 */
	public void updateStatus(String s) {
		updateOutput(lastDisplay, s);
	}

	/**
	 * @param dsp
	 * @param sta
	 */
	public void updateOutput(String dsp, String sta) {
		if (dsp==null) dsp = "";
		if (sta==null) sta = "";

		display.updateText(dsp, sta);

		display.repaint();
	}
}
