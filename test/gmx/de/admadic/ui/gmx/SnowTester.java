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
package de.admadic.ui.gmx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;
import com.l2fprod.gui.plaf.skin.SkinUtils;

/**
 * @author Rainer Schwarze
 *
 */
public class SnowTester {
	/**
	 * 
	 */
	public SnowTester() {
		super();
	}

	/**
	 * @param c
	 * @return	Returns a string encoding of the color.
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

		try {
			SkinLookAndFeel.setSkin(
				SkinLookAndFeel
				.loadThemePackDefinition(
						SkinUtils.toURL(
							new File(
								"D:/data/java/calculator/laf/SkinLF/themes/" +
								"xmas/skinlf-themepack.xml")
							)));
//			SkinLookAndFeel.setSkin(
//				SkinLookAndFeel
//					.loadThemePack(
//						"D:/data/java/calculator/laf/SkinLF/themes/" +
//						"xmas/skinlf-themepack.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
			javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		UIManager.getDefaults().put(
//				"Button.foreground", new ColorUIResource(Color.decode("#FFCC00")));
//		UIManager.getDefaults().put(
//				"Button.background", new ColorUIResource(Color.decode("#590508")));
//		UIManager.getDefaults().put(
//				"TextField.foreground", new ColorUIResource(Color.decode("#FFCC00")));
//		UIManager.getDefaults().put(
//				"TextField.background", new ColorUIResource(Color.decode("#590508")));
//		UIManager.getDefaults().put(
//				"control", new ColorUIResource(Color.decode("#590508")));
//		UIManager.getDefaults().put(
//				"window", new ColorUIResource(Color.decode("#590508")));
        new ClassSelectorFrame().setVisible(true);
	}

	/**
	 * 
	 */
	public static class ClassSelectorFrame extends JFrame {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		JPanel panelMain;
		SnowPanel snowPanel;
		JTextArea textArea;
		JScrollPane textScroll;
		SnowEngine snowEngine;

		JDialog dlg;
	
		/**
		 * 
		 */
		public ClassSelectorFrame() {
			super();
			initComponents();
	    }

	    private void initComponents() {
	    	panelMain = new JPanel();
	    	
	        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        setTitle("Component Color Tester");

	        FormLayout fl = new FormLayout(
	        		"12px, p, 12px, p, 12px, p, 12px",
	        		"12px, p, 12px, p, 12px, p, 12px, p, 12px, p, 12px, p, 12px, p, 12px");
	        panelMain.setLayout(fl);

	        this.getContentPane().setLayout(new BorderLayout());
	        this.getContentPane().add(panelMain, BorderLayout.CENTER);

	        CellConstraints cc = new CellConstraints();
	        for (int row=0; row<2; row++) {
	        	for (int col=0; col<3; col++) {
		        	panelMain.add(
		        			new JButton("A Btn"), cc.xy(col*2+2, row*2+2));
	        	}
	        }

	        panelMain.add(
        			new JTextField("Something"), cc.xywh(0*2+2, 2*2+2, 5, 1));
	        
	        for (int row=3; row<4; row++) {
	        	for (int col=0; col<3; col++) {
	        		panelMain.add(
		        			new JButton("A Btn"), cc.xy(col*2+2, row*2+2));
	        	}
	        }

	        panelMain.add(
        			new JTextField("Something else"), cc.xywh(0*2+2, 4*2+2, 5, 1));

//	        textArea = new JTextArea();
//	        SnowViewport svp = new SnowViewport();
//	        snowEngine = new SnowEngine(
//	        		null, svp, 
//	        		//SnowConstants.REST_NORTH |
//	        		SnowConstants.REST_SOUTH);
//	        svp.setSnowEngine(snowEngine);
//	        textScroll = new JScrollPane();
//	        textScroll.setViewport(svp);
//	        textScroll.setViewportView(textArea);
//	        panelMain.add(textScroll, cc.xywh(0*2+2, 5*2+2, 5, 1));
//	        textScroll.setHorizontalScrollBarPolicy(
//	        		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//	        textScroll.setVerticalScrollBarPolicy(
//	        		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//	        textArea.setRows(8);

//	        for (int row=6; row<7; row++) {
//	        	for (int col=0; col<3; col++) {
//	        		panelMain.add(
//		        			new JButton("A Btn"), cc.xy(col*2+2, row*2+2));
//	        	}
//	        }

//	        CalcProtocol prot = new CalcProtocol();
//	        panelMain.add(prot, cc.xywh(0*2+2, 6*2+2, 5, 1));
//	        prot.setRows(6);

//	        panelMain.setBackground(Color.decode("#A0090D"));

//        	java.awt.Component [] comps = this.getContentPane().getComponents();
//        	for (java.awt.Component c : comps) {
//				if (c instanceof JButton) 
//					c.setBackground(Color.decode("#590508"));
//			}

	        if (false) {
		        JRootPane rp = this.getRootPane();
	        	snowPanel = new SnowPanel(
	        			panelMain, 
	        			SnowConstants.REST_SOUTH |
	        			SnowConstants.REST_NORTH);
	        	rp.setGlassPane(snowPanel);
	        }
        	
	        this.pack();
	        setLocationRelativeTo(null);
	        this.addComponentListener(new ComponentListener() {
				public void componentResized(ComponentEvent arg0) { /* x */ }
				public void componentMoved(ComponentEvent arg0) {
					if (snowPanel!=null) snowPanel.shake();
					if (snowEngine!=null) snowEngine.shake();
				}
				public void componentShown(ComponentEvent arg0) { /* x */ }
				public void componentHidden(ComponentEvent arg0) { /* x */ }
	        });

	        dlg = new JDialog();
	        dlg.setLayout(new BorderLayout());
	        // CalcProtocol p2 = new CalcProtocol();
	        // dlg.add(p2, BorderLayout.CENTER);
	        dlg.setSize(100, 150);
	        dlg.setLocation(200, 300);
	    }

		/**
		 * @param arg0
		 * @see java.awt.Component#setVisible(boolean)
		 */
		@Override
		public void setVisible(boolean arg0) {
			super.setVisible(arg0);
			dlg.setVisible(arg0);
//			this.getRootPane().getGlassPane().setVisible(arg0);
		}

	    
	}

}
