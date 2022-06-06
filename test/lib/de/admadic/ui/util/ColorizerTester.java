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
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.ColorUIResource;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Rainer Schwarze
 *
 */
public class ColorizerTester {
	/**
	 * 
	 */
	public ColorizerTester() {
		super();
	}

	/**
	 * @param c
	 * @return	Returns String encoding
	 */
	public static String encode(Color c) {
		return String.format(
				"#%02x%02x%02x", 
				Integer.valueOf(c.getRed()), 
				Integer.valueOf(c.getGreen()), 
				Integer.valueOf(c.getBlue())
				);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UIManager.getDefaults().put(
				"Button.foreground", new ColorUIResource(Color.decode("#FFCC00")));
        new ColorizerFrame().setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// nothing yet
			}
        });
	}

	/**
	 * 
	 */
	public static class ColorizerFrame extends JFrame {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Vector<JComponent> comps;
		
		/**
		 * 
		 */
		public ColorizerFrame() {
			super();
			comps = new Vector<JComponent>();
			initComponents();
	    }

	    private void initComponents() {
	        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        setTitle("Component Color Tester");
//	        setSize(400,300);
	        FormLayout fl = new FormLayout(
	        		"5px, p, 5px, p, 5px, p, 5px, p, 5px, p, 5px",
	        		"5px, p, 5px, p, 5px, p, 5px, p, 5px, p, 5px");
	        this.getContentPane().setLayout(fl);
	        Image img, img2;
			{
				MediaTracker mt = new MediaTracker(this);
				
				img = Toolkit.getDefaultToolkit().getImage(
						this.getClass().getClassLoader().getResource(
							"de/admadic/calculator/ui/res/btn-mth-sin.png"));
				img2 = Toolkit.getDefaultToolkit().getImage(
						this.getClass().getClassLoader().getResource(
							"de/admadic/calculator/ui/res/btn-aot.png"));
				mt.addImage(img, 0);
				mt.addImage(img2, 0);
				try {
					mt.waitForAll();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

	        CellConstraints cc = new CellConstraints();
	        for (int row=0; row<5; row++) {
	        	for (int col=0; col<5; col++) {
	        		int index = row*5 + col;
	        		comps.add(new JButton("Button"));
	        		this.getContentPane().add(
	        				comps.get(index),
	        				cc.xy(col*2+2, row*2+2));
	        	}
	        }
	        comps.get(0).setBackground(Color.BLUE);
	        comps.get(1).setBackground(Color.RED);
	        comps.get(2).setBackground(Color.GREEN);
	        comps.get(5).setBackground(Color.decode("#003f00"));
	        comps.get(6).setBackground(Color.decode("#a0090d"));

	        Color [] setColors;
	        Color [] adjColors;

	        setColors = new Color[]{
	        	new Color(255, 0, 0),	
	        	new Color(0, 255, 0),	
	        	new Color(0, 0, 255),	
	        	new Color(0, 0, 0),	
	        	new Color(255, 255, 255),	
	        };
	        adjColors = new Color[5];
	        for (int i=10; i<15; i++) {
	        	Color bg = Color.decode("#003f00");
	        	JComponent jc = comps.get(i);
        		adjColors[i-10] = Colorizer.adjustColor(
        				bg,
        				jc.getForeground(),
        				setColors[i-10],
        				0.25);
        		System.out.println(
        				"bg=" + encode(bg) + 
        				" fg=" + encode(jc.getForeground()) +
        				" sc=" + encode(setColors[i-10]) + 
        				" ac=" + encode(adjColors[i-10]) 
        				);
		        jc.setForeground(adjColors[i-10]);
		        jc.setBackground(bg);
	        }

	        setColors = new Color[]{
		        	new Color(255, 0, 0),	
		        	new Color(0, 255, 0),	
		        	new Color(0, 0, 255),	
		        	new Color(0, 0, 0),	
		        	new Color(255, 255, 255),	
		        };
	        adjColors = new Color[5];
	        for (int i=15; i<20; i++) {
	        	Color bg = Color.decode("#FFFFFF");
	        	JComponent jc = comps.get(i);
	        	jc.setForeground(Color.BLACK);
	        	adjColors[i-15] = Colorizer.adjustColor(
	        			bg,
	        			jc.getForeground(),
	        			setColors[i-15],
	        			0.25);
	        	System.out.println(
	        			"bg=" + encode(bg) + 
	        			" fg=" + encode(jc.getForeground()) +
	        			" sc=" + encode(setColors[i-15]) + 
	        			" ac=" + encode(adjColors[i-15]) 
	        	);
	        	jc.setForeground(adjColors[i-15]);
	        }

	        for (int row=0; row<5; row++) {
	        	for (int col=0; col<5; col++) {
	        		int index = row*5 + col;
	        		if (col==0) {
	        			JComponent jc = comps.get(index);
		        		ImageIcon tmpImg = Colorizer.adjustImageColor(
		        				img, 
		        				jc.getBackground(), 
		        				Color.decode("#FFCC00"),
		        				Color.decode("#FFCC00"));
		        		// jc.getForeground()
	        			((JButton)jc).setIcon(tmpImg);
	        		} else if (row==2 || row==3) {
	        			JComponent jc = comps.get(index);
		        		ImageIcon tmpImg = Colorizer.adjustImageColor(
		        				img, 
		        				jc.getBackground(), 
		        				jc.getForeground(),
		        				jc.getForeground()
		        				//null //Color.decode("#FFCC00"),
		        				);
		        		ImageIcon tmpImg2 = Colorizer.adjustImageColor(
		        				img2, 
		        				jc.getBackground(), 
		        				jc.getForeground(),
		        				null);
		        		ImageIcon tmpImg3 = new ImageIcon(img2);
		        		
	        			((JButton)jc).setIcon(tmpImg);
	        			((JButton)jc).setRolloverIcon(tmpImg2);
	        			((JButton)jc).setPressedIcon(tmpImg3);
	        		}
	        	}
	        }
	        this.pack();
	        setLocationRelativeTo(null);
	    }
	}

}
