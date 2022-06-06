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

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JWindow;

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcCompLayoutTest extends TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(CalcCompLayoutTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected int cellIdxRowsFirst(int col, int row, int cols, int rows) {
		// make the warning go away:
		if (rows==0) { /* nothing */ }
		return row*cols + col;
	}
	protected int cellIdxColsFirst(int col, int row, int cols, int rows) {
		// make the warning go away:
		if (cols==0) { /* nothing */ }
		return col*rows + row;
	}

	protected void impl_testSet1() {
		JWindow parent;
		CalcCompLayout layout;
		parent = new JWindow();
		//parent.setTitle("CalcCompLayoutTest Frame");
		layout = new CalcCompLayout();
		parent.setLayout(layout);

		layout.setFixedCellSize(true);
		layout.setForceFixedCellSize(true);
		layout.setMinCellSize(new Dimension(4, 4));

		int cols = 3;
		int rows = 4;
		CalcCompCellConstraints [] cons = new CalcCompCellConstraints[cols*rows];
		cons = CalcCompCellConstraints.createGrid(cols, rows, null);

		JButton [] btns = new JButton[cols*rows];
	
		for (int row=0; row<rows; row++) {
			for (int col=0; col<cols; col++) {
				int cellidx = cellIdxRowsFirst(col, row, cols, rows);
				CalcCompCellConstraints cell = cons[cellidx];
				btns[cellidx] = new JButton(cell.getName());
				btns[cellidx].setMargin(new Insets(1, 1, 1, 1));

				parent.getContentPane().add(btns[cellidx], cons[cellidx]);
			}
		}

		parent.pack();
//		parent.setVisible(true);

		for (JButton button : btns) {
			assertEquals(
					"impl_testSet1: button width failed",
					4, button.getWidth());
			assertEquals(
					"impl_testSet1: button height failed",
					4, button.getHeight());
		}

//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// nothing
//		}

//		parent.setVisible(false);
	}

	protected void impl_testSet2() {
		JWindow parent;
		CalcCompLayout layout;
		parent = new JWindow();
		//parent.setTitle("CalcCompLayoutTest Frame");
		layout = new CalcCompLayout();
		parent.setLayout(layout);

		layout.setFixedCellSize(true);
		layout.setForceFixedCellSize(true);
		layout.setMinCellSize(new Dimension(4, 4));
		layout.setMajorGridSize(new Dimension(1, 1));
		layout.setShrinkWrap(true);
		layout.setShrinkPan(2);

		int cols = 3;
		int rows = 4;
		int cellW = 7;
		int cellH = 5;

		CalcCompCellConstraints [] cons = new CalcCompCellConstraints[cols*rows];
		cons = CalcCompCellConstraints.createGrid(
				cols, rows, 
				null, null,
				CalcCompCellConstraints.FIT_DEFAULT,
				new CalcCompCellConstraints("<template>")
					.defineMajorGrid(cellW, cellH)
					.defineSpan(1, 1)
					.definePan(0, 2)
			);

		JButton [] btns = new JButton[cols*rows];
	
		for (int row=0; row<rows; row++) {
			for (int col=0; col<cols; col++) {
				int cellidx = cellIdxRowsFirst(col, row, cols, rows);
				CalcCompCellConstraints cell = cons[cellidx];
				btns[cellidx] = new JButton(cell.getName());
				btns[cellidx].setMargin(new Insets(1, 1, 1, 1));

				parent.getContentPane().add(btns[cellidx], cons[cellidx]);
			}
		}

		parent.pack();
		parent.setVisible(true);

		for (JButton button : btns) {
			assertEquals(
					"impl_testSet1: button width failed @ " + button.getText(),
					7*4, button.getWidth());
			assertEquals(
					"impl_testSet1: button height failed @ " + button.getText(),
					5*4, button.getHeight());
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// nothing
		}

		parent.setVisible(false);
	}

	/**
	 * 
	 */
	public void testLayoutContainer() {
		//impl_testSet1();
		impl_testSet2();
	}
}
