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
package de.admadic.ui.util;

import java.awt.Color;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Rainer Schwarze
 *
 */
public class ColorGradientTest {
	/**
	 * 
	 */
	public ColorGradientTest() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        new Frame().setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// nothing yet
			}
        });
	}

	/**
	 * 
	 */
	public static class Frame extends JFrame {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Vector<JComponent> comps;
		
		/**
		 * 
		 */
		public Frame() {
			super();
			comps = new Vector<JComponent>();
			initComponents();
	    }

	    private void initComponents() {
	        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        setTitle("Color Gradient Tester");
//	        setSize(400,300);
	        FormLayout fl = new FormLayout(
	        		"5px, p, 5px, p, 5px",
	        		"5px, p, 5px, p, 5px, p, 5px, p, 5px, p, "+
	        		"5px, p, 5px, p, 5px, p, 5px, p, 5px, p, "+
	        		"5px, p, "+
	        		"5px");
	        this.getContentPane().setLayout(fl);
	        CellConstraints cc = new CellConstraints();
	        ColorGradient cg = ColorGradient.JET();

	        for (int i=0; i<=10; i++) {
	        	Color c = cg.calculateColorAtPos(i/10.0);
	        	JComponent comp = new JLabel(".  .  .  .  .  .  .  .  .  .");
	        	// comp.setBackground(c);
	        	// comp.setBorder(BorderFactory.createLineBorder(c, 2));
	        	comp.setBorder(BorderFactory.createMatteBorder(
	        			0, 0, 2, 0, c));
	        	comp.setOpaque(true);
	        	this.getContentPane().add(comp, cc.xy(2, i*2+2));
	        }
	        
	        this.pack();
	        setLocationRelativeTo(null);
	    }
	}

}
