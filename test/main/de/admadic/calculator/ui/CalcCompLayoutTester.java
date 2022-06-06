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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcCompLayoutTester extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panelMain;
	CalcCompLayout layout;
	CalcCompStyles styles;
	
	/**
	* Auto-generated main method to display this JFrame
	 * @param args 
	*/
	public static void main(String[] args) {
		// make the warning go away:
		if (args==null) { /* nothing */ }
		CalcCompLayoutTester inst = new CalcCompLayoutTester();
		inst.setVisible(true);
	}

	/**
	 * 
	 */
	public CalcCompLayoutTester() {
		super();
		styles = new CalcCompStyles();
		initGUI();
	}
	
	private void initGUI() {
		try {
			boolean usefix = false;
			//setSize(400, 300);
			panelMain = new JPanel();
			this.getContentPane().add(
				panelMain,
				BorderLayout.CENTER);
			generateButtonsFit();
			if (usefix) {
				generateButtonsFix();
			}
			this.addWindowListener( new WindowAdapter() {
		    	@Override
				public void windowClosing( WindowEvent e ) {
		        	System.exit(0);
		    	}
		    });
			this.pack();
			this.setLocationRelativeTo(null);
			this.setAlwaysOnTop(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateButtonsFix() {
		CalcCompLayout layout = new CalcCompLayout(
				new java.awt.Dimension(10, 10));
		panelMain.setLayout(layout);
		layout.setShrinkWrap(true);

		panelMain.add(new javax.swing.JButton("X1"), 
				new CalcCompCellConstraints("X1")
					.definePlace(1, 1)
					.defineSpan(1, 1)
						);
		panelMain.add(new javax.swing.JButton("X2"), 
				new CalcCompCellConstraints("X2")
					.definePlace(3, 1)
					.defineSpan(1, 1)
						);
		panelMain.add(new javax.swing.JButton("X3"), 
				new CalcCompCellConstraints("X3")
					.definePlace(1, 2)
					.defineSpan(3, 1)
						);
	}

	private void generateButtonsFit() {
		layout = new CalcCompLayout(
				new java.awt.Dimension(10, 10));
		panelMain.setLayout(layout);
		layout.setShrinkWrap(true);
		layout.setMajorGridSize(new java.awt.Dimension(1, 1));
		layout.setMinCellSize(new java.awt.Dimension(10, 10));
		layout.setFixedCellSize(true);

		styles.addStyle("sz5", 5, 5, 1, 1);
		styles.addStyle("sz4", 4, 4, 1, 1);
		styles.addStyle("sz3", 3, 3, 1, 1);

		JButton chBtn;
		JToggleButton chTBtn;
		CalcCompCellConstraints cell;
		panelMain.add(chTBtn = new javax.swing.JToggleButton("X1"), 
				cell = new CalcCompCellConstraints("X1")
					.defineFit(CalcCompCellConstraints.FIT_BY_COL)
					.defineMajorGrid(5, 5)
					.defineStretch(true, false)
					.definePan(1, 1)
						);
		styles.addCellForStyle("sz5", cell);
		chTBtn.setMargin(new java.awt.Insets(1, 1, 1, 1));
		chTBtn.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==ItemEvent.SELECTED) {
					CalcCompStyles.Style style;
					style = styles.getStyle("sz5");
					style.width = 5;
					style.height = 5;
					style = styles.getStyle("sz4");
					style.width = 4;
					style.height = 4;
					style = styles.getStyle("sz3");
					style.width = 3;
					style.height = 3;
					styles.updateCellsForAllStyles();

					layout.setMajorGridSize(new java.awt.Dimension(1, 1));
					layout.setMinCellSize(new java.awt.Dimension(5, 5));
					//layout.setFixedCellSize(true);
					layout.invalidateLayout(panelMain);
					CalcCompLayoutTester.this.pack();
				} else {
					CalcCompStyles.Style style;
					style = styles.getStyle("sz5");
					style.width = 10;
					style.height = 10;
					style = styles.getStyle("sz4");
					style.width = 3;
					style.height = 3;
					style = styles.getStyle("sz3");
					style.width = 4;
					style.height = 4;
					styles.updateCellsForAllStyles();
					layout.setMajorGridSize(new java.awt.Dimension(1, 1));
					layout.setMinCellSize(new java.awt.Dimension(10, 10));
					layout.invalidateLayout(panelMain);
					CalcCompLayoutTester.this.pack();
				}
			}
		});
		/*
		panelMain.add(new javax.swing.JButton("X2"), 
				new CalcCompCellConstraints("X2")
					.defineFit(CalcCompCellConstraints.FIT_BY_COL)
					.defineLinkWest("X1")
					.defineSpan(1, 1)
					.definePan(1, 1)
						);
		*/
		panelMain.add(chBtn = new javax.swing.JButton("X3"), 
				cell = new CalcCompCellConstraints("X3")
					.defineFit(CalcCompCellConstraints.FIT_BY_COL)
					.defineLinkNorth("X1")
					.defineMajorGrid(5, 5)
					.defineSpan(3, 1)
					.definePan(1, 1)
						);
		styles.addCellForStyle("sz5", cell);
		chBtn.setMargin(new java.awt.Insets(1, 1, 1, 1));

		CalcCompCellConstraints [] cons;

		cons = CalcCompCellConstraints.createGrid(3, 4, null);
		for (int i=0; i<3; i++) {
			cons[i].setLinkNorth("X3");
		}
		for (int i=0; i<cons.length; i++) {
			cons[i].setMajorGridWidth(5);
			cons[i].setMajorGridHeight(5);
			cons[i].updateSpans();
		}
		styles.addCellsForStyle("sz5", cons);
		for (int i=0; i<cons.length; i++) {
			panelMain.add(
					chBtn = new JButton(cons[i].getName()),
					cons[i]);
			chBtn.setMargin(new java.awt.Insets(1, 1, 1, 1));
		}

		cons = CalcCompCellConstraints.createGrid(
				3, 3, null, "cl", CalcCompCellConstraints.FIT_BY_COL, 
				new CalcCompCellConstraints("<template>")
					.defineLinkNorth("X3")
					.defineLinkWest("X3")
					.definePan(1, 1)
					.defineMajorGrid(3, 3)
		);
		styles.addCellsForStyle("sz3", cons);
		for (int i=0; i<cons.length; i++) {
			panelMain.add(
					chBtn = new JButton(cons[i].getName()),
					cons[i]);
			chBtn.setMargin(new java.awt.Insets(1, 1, 1, 1));
		}

		cons = CalcCompCellConstraints.createGrid(
				3, 3, null, "cc", CalcCompCellConstraints.FIT_BY_COL, 
				new CalcCompCellConstraints("<template>")
					.defineLinkNorth("cl8")
					.defineLinkWest("X3")
					.defineMajorGrid(4, 4)
					.definePan(1, 2)
		);
		styles.addCellsForStyle("sz4", cons);
		for (int i=0; i<cons.length; i++) {
			panelMain.add(
					chBtn = new JButton(cons[i].getName()),
					cons[i]);
			chBtn.setMargin(new java.awt.Insets(1, 1, 1, 1));
		}

		panelMain.add(
				chBtn = new JButton("ROW"), 
				cell = new CalcCompCellConstraints("ROW")
				.defineLinkWest("cc8")
				.defineLinkNorth("X1")
				.defineStretch(false, true)
				.defineMajorGrid(5, 5)
				.defineFit(CalcCompCellConstraints.FIT_BY_COL)
			);
		styles.addCellForStyle("sz5", cell);
		chBtn.setMargin(new java.awt.Insets(1, 1, 1, 1));
	}
}
