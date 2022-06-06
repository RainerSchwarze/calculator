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

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcCompCellConstraintsTest extends TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(CalcCompCellConstraintsTest.class);
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
		if (rows==0) { /* no warn */ }
		return row*cols + col;
	}

	protected int cellIdxColsFirst(int col, int row, int cols, int rows) {
		if (cols==0) { /* no warn */ }
		return col*rows + row;
	}

	/**
	 * Class under test for CalcCompCellConstraints[] createGrid(int, int, String[])
	 */
	public void testCreateGridintintStringArray() {
		String testFuncName = "testCreateGridintintStringArray";
		String testLocation;

		CalcCompCellConstraints [] cons;
		int cols = 3;
		int rows = 4;
		cons = CalcCompCellConstraints.createGrid(cols, rows, null);

		// check cell names, col and row, link north and link west: 
		// (we assume that cells are fill in rows first)
		for (int row=0; row<rows; row++) {
			for (int col=0; col<cols; col++) {
				int cellidx = cellIdxRowsFirst(col, row, cols, rows);
				CalcCompCellConstraints cell = cons[cellidx];
				testLocation = " @ (c=" + col + ",r=" + row + ")";

				assertEquals(
						testFuncName + ": cell name wrong"+
						testLocation,
						"cell"+cellidx, cell.getName());
				assertEquals(
						testFuncName + ": cell col wrong"+
						testLocation,
						col, cell.getCol());
				assertEquals(
						testFuncName + ": cell max col wrong"+
						testLocation,
						col, cell.getMaxCol());
				assertEquals(
						testFuncName + ": cell row wrong"+
						testLocation,
						row, cell.getRow());
				assertEquals(
						testFuncName + ": cell max row wrong"+
						testLocation,
						row, cell.getMaxRow());

				// check link north:
				if (row>0) {
					String refname = 
						"cell" + cellIdxRowsFirst(col, row-1, cols, rows);
					assertNotNull(
							testFuncName + ": cell link is null"+
							testLocation,
							cell.getLinkNorth());
					assertEquals(
							testFuncName + ": cell name wrong"+
							testLocation,
							refname, cell.getLinkNorth());
				} else {
					// first row
					assertNull(
							testFuncName + ": cell link is not null"+
							testLocation,
							cell.getLinkNorth());
				}
				// check link west:
				if (col>0) {
					String refname = 
						"cell" + cellIdxRowsFirst(col-1, row, cols, rows);
					assertNotNull(
							testFuncName + ": cell link is null"+
							testLocation,
							cell.getLinkWest());
					assertEquals(
							testFuncName + ": cell name wrong"+
							testLocation,
							refname, cell.getLinkWest());
				} else {
					// first row
					assertNull(
							testFuncName + ": cell link is not null"+
							testLocation,
							cell.getLinkWest());
				}
			}
		} // (for each cell)
	}

	protected void impl_testCreate_AllPar_FitByCol() {
		String testFuncName = "testCreate_AllPar_FitByCol";
		String testLocation;

		CalcCompCellConstraints [] cons;
		int cols = 3;
		int rows = 4;
		cons = CalcCompCellConstraints.createGrid(
				cols, rows, 
				null, null,
				CalcCompCellConstraints.FIT_BY_COL,
				null);

		// check cell names, col and row, link north and link west: 
		// (we assume that cells are fill in rows first)
		for (int row=0; row<rows; row++) {
			for (int col=0; col<cols; col++) {
				int cellidx = cellIdxColsFirst(col, row, cols, rows);
				CalcCompCellConstraints cell = cons[cellidx];
				testLocation = " @ (c=" + col + ",r=" + row + ")";

				assertEquals(
						testFuncName + ": cell name wrong"+
						testLocation,
						"cell"+cellidx, cell.getName());
				assertEquals(
						testFuncName + ": cell col wrong"+
						testLocation,
						col, cell.getCol());
				assertEquals(
						testFuncName + ": cell max col wrong"+
						testLocation,
						col, cell.getMaxCol());
				assertEquals(
						testFuncName + ": cell row wrong"+
						testLocation,
						row, cell.getRow());
				assertEquals(
						testFuncName + ": cell max row wrong"+
						testLocation,
						row, cell.getMaxRow());

				// check link north:
				if (row>0) {
					String refname = 
						"cell" + cellIdxColsFirst(col, row-1, cols, rows);
					assertNotNull(
							testFuncName + ": cell link is null"+
							testLocation,
							cell.getLinkNorth());
					assertEquals(
							testFuncName + ": cell name wrong"+
							testLocation,
							refname, cell.getLinkNorth());
				} else {
					// first row
					assertNull(
							testFuncName + ": cell link is not null"+
							testLocation,
							cell.getLinkNorth());
				}
				// check link west:
				if (col>0) {
					String refname = 
						"cell" + cellIdxColsFirst(col-1, row, cols, rows);
					assertNotNull(
							testFuncName + ": cell link is null"+
							testLocation,
							cell.getLinkWest());
					assertEquals(
							testFuncName + ": cell name wrong"+
							testLocation,
							refname, cell.getLinkWest());
				} else {
					// first row
					assertNull(
							testFuncName + ": cell link is not null"+
							testLocation,
							cell.getLinkWest());
				}
			} // (for each cell)
		} // (for each cell)
	}

	protected void impl_testCreate_AllPar_Names() {
		String testFuncName = "testCreate_AllPar_Names";
		String testLocation;

		CalcCompCellConstraints [] cons;
		int cols = 3;
		int rows = 4;
		String [] names = new String[cols*rows];
		for (int row=0; row<rows; row++) {
			for (int col=0; col<cols; col++) {
				int cellidx = cellIdxRowsFirst(col, row, cols, rows);
				names[cellidx] = "name"+cellidx+"test";
			}
		}
		cons = CalcCompCellConstraints.createGrid(
				cols, rows, 
				names, null,
				CalcCompCellConstraints.FIT_DEFAULT,
				null);

		// check cell names, col and row, link north and link west: 
		// (we assume that cells are fill in rows first)
		for (int row=0; row<rows; row++) {
			for (int col=0; col<cols; col++) {
				int cellidx = cellIdxRowsFirst(col, row, cols, rows);
				CalcCompCellConstraints cell = cons[cellidx];
				testLocation = " @ (c=" + col + ",r=" + row + ")";

				assertEquals(
						testFuncName + ": cell name wrong"+
						testLocation,
						names[cellidx], cell.getName());
			} // (for each cell)
		} // (for each cell)
	}

	protected void impl_testCreate_AllPar_NamePrefix() {
		String testFuncName = "testCreate_AllPar_NamePrefix";
		String testLocation;

		CalcCompCellConstraints [] cons;
		int cols = 3;
		int rows = 4;
		cons = CalcCompCellConstraints.createGrid(
				cols, rows, 
				null, "testme",
				CalcCompCellConstraints.FIT_DEFAULT,
				null);

		// check cell names, col and row, link north and link west: 
		// (we assume that cells are fill in rows first)
		for (int row=0; row<rows; row++) {
			for (int col=0; col<cols; col++) {
				int cellidx = cellIdxRowsFirst(col, row, cols, rows);
				CalcCompCellConstraints cell = cons[cellidx];
				testLocation = " @ (c=" + col + ",r=" + row + ")";

				assertEquals(
						testFuncName + ": cell name wrong"+
						testLocation,
						"testme"+cellidx, cell.getName());
			} // (for each cell)
		} // (for each cell)
	}

	protected void impl_testCreate_AllPar_TmplCell() {
		String testFuncName = "testCreate_AllPar_TmplCell";
		String testLocation;

		CalcCompCellConstraints [] cons;
		CalcCompCellConstraints tmpl;
		int opan = 2;
		int ipan = 1;

		tmpl = new CalcCompCellConstraints("<template>")
					.defineLinkNorth("northcell", opan)
					.defineLinkWest("westcell", opan)
					.definePan(ipan, opan);

		int cols = 3;
		int rows = 4;
		cons = CalcCompCellConstraints.createGrid(
				cols, rows, 
				null, null,
				CalcCompCellConstraints.FIT_DEFAULT,
				tmpl);

		// check cell names, col and row, link north and link west: 
		// (we assume that cells are fill in rows first)
		for (int row=0; row<rows; row++) {
			for (int col=0; col<cols; col++) {
				int cellidx = cellIdxRowsFirst(col, row, cols, rows);
				CalcCompCellConstraints cell = cons[cellidx];
				testLocation = " @ (c=" + col + ",r=" + row + ")";

				assertEquals(
						testFuncName + ": cell name wrong"+
						testLocation,
						"cell"+cellidx, cell.getName());
				assertEquals(
						testFuncName + ": cell col wrong"+
						testLocation,
						col, cell.getCol());
				assertEquals(
						testFuncName + ": cell max col wrong"+
						testLocation,
						col, cell.getMaxCol());
				assertEquals(
						testFuncName + ": cell row wrong"+
						testLocation,
						row, cell.getRow());
				assertEquals(
						testFuncName + ": cell max row wrong"+
						testLocation,
						row, cell.getMaxRow());

				// check link north:
				String refname;

				if (row>0) {
					refname = "cell" + cellIdxRowsFirst(col, row-1, cols, rows);
				} else {
					refname = "northcell";
				}
				assertNotNull(
						testFuncName + ": north cell link is null"+
						testLocation,
						cell.getLinkNorth());
				assertEquals(
						testFuncName + ": north cell link name wrong"+
						testLocation,
						refname, cell.getLinkNorth());

				if (col>0) {
					refname = "cell" + cellIdxRowsFirst(col-1, row, cols, rows);
				} else {
					refname = "westcell";
				}
				assertNotNull(
						testFuncName + ": west cell link is null"+
						testLocation,
						cell.getLinkWest());
				assertEquals(
						testFuncName + ": west cell link name wrong"+
						testLocation,
						refname, cell.getLinkWest());

				if (row>0) {
					assertEquals(
							testFuncName + ": north link ofs wrong"+
							testLocation,
							ipan, cell.getOffsetNorth());
				} else {
					assertEquals(
							testFuncName + ": north link ofs wrong"+
							testLocation,
							opan, cell.getOffsetNorth());
				}
				
				if (col>0) {
					assertEquals(
							testFuncName + ": west link ofs wrong"+
							testLocation,
							ipan, cell.getOffsetWest());
				} else {
					assertEquals(
							testFuncName + ": west link ofs wrong"+
							testLocation,
							opan, cell.getOffsetWest());
				}
			} // (for each cell)
		} // (for each cell)
	}

	/**
	 * Class under test for 
	 * CalcCompCellConstraints[] createGrid(
	 * 			int, int, 
	 * 			String[], String, 
	 * 			int, 
	 * 			CalcCompCellConstraints)
	 */
	public void testCreateGridintintStringArrayStringintCalcCompCellConstraints() {
		impl_testCreate_AllPar_FitByCol();
		impl_testCreate_AllPar_Names();
		impl_testCreate_AllPar_NamePrefix();
		impl_testCreate_AllPar_TmplCell();
	}

	/**
	 * Class under test for void mergeCells(CalcCompCellConstraints, CalcCompCellConstraints)
	 */
	public void testMergeCellsCalcCompCellConstraintsCalcCompCellConstraints() {
		// not yet done
		// FIXME: implement a few tests!
	}

	/**
	 * Class under test for CalcCompCellConstraints[] mergeCells(CalcCompCellConstraints[], String[])
	 */
	public void testMergeCellsCalcCompCellConstraintsArrayStringArray() {
		// not yet done
		// FIXME: implement a few tests!
	}

	/**
	 * 
	 */
	public void testRemoveCells() {
		// not yet done
		// FIXME: implement a few tests!
	}
}
