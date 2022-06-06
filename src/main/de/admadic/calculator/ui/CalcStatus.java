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
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcStatus extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField status;
	private Color displayColor = new Color(220, 220, 220);

	/**
	 * 
	 */
	public CalcStatus() {
		status = new JTextField();
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		add(status, BorderLayout.CENTER);

		status.setText("(status)");
		status.setFont(new java.awt.Font("Arial",1,10));
		status.setEditable(false);
		status.setAutoscrolls(false);
		status.setOpaque(false);
		//status.setBorder(new LineBorder(Color.gray, 1, false));
		status.setBackground(displayColor);
		status.setAlignmentX(0.0f);
		//status.setBorder(null);
		//status.setMargin(new Insets(2, 2, 2, 2));

		this.setBackground(displayColor);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	/**
	 * @param s
	 */
	public void updateStatus(String s) {
		status.setText(" " + s);
	}
}
