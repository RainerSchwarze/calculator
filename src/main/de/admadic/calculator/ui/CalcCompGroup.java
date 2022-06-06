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
import java.util.Hashtable;
import java.util.Iterator;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcCompGroup {
	String name;
	CalcCompCellConstraints groupCell;
	Hashtable<String,CalcCompCellConstraints> cells;
	Dimension gridSize;

	/**
	 * @param name
	 */
	public CalcCompGroup(String name) {
		this(name, 1, 1);
	}
	/**
	 * @param name
	 * @param rows
	 * @param cols
	 */
	public CalcCompGroup(String name, int cols, int rows) {
		super();
		cells = new Hashtable<String,CalcCompCellConstraints>();
		this.name = name;
		this.gridSize = new Dimension(cols, rows);
		this.groupCell = new CalcCompCellConstraints("(group)", 0, 0, cols, rows);
	}

	/**
	 * @param cmd
	 */
	public void addByRow(String cmd) {
		addByRow(cmd, 1);
	}

	/**
	 * @param cmd
	 */
	public void addByCol(String cmd) {
		addByCol(cmd, 1);
	}

	/**
	 * rowspan is not honored!
	 * @param cmd 
	 * @param colspan 
	 */
	public void addByRow(String cmd, int colspan) {
		if (cells.contains(cmd))
			return;
			
		CalcCompCellConstraints cell;
		int [] maxcol = new int[gridSize.height];
		int maxrow = 0;
		for (int i=0; i<maxcol.length; i++) {
			maxcol[i] = -1;
		}
		Iterator<CalcCompCellConstraints> it = cells.values().iterator();
		while (it.hasNext()) {
			cell = it.next();
			if (cell.getMaxCol()>maxcol[cell.row]) maxcol[cell.row] = cell.getMaxCol();
			if (cell.row>maxrow) maxrow = cell.row;
		}
		int row, col;
		row = maxrow;
		col = maxcol[row];
		col++;
		if ((col+colspan-1)>=gridSize.width) {
			row++;
			col = 0;
		}
		if (row>=gridSize.height) {
			return; // too many
		}
		cell = new CalcCompCellConstraints(cmd, col, row, colspan, 1);
		cells.put(cmd, cell);
	}

	/**
	 * colspan is not honored!
	 * @param cmd 
	 * @param rowspan 
	 */
	public void addByCol(String cmd, int rowspan) {
		if (cells.contains(cmd))
			return;
			
		CalcCompCellConstraints cell;
		int [] maxrow = new int[gridSize.width];
		int maxcol = 0;
		for (int i=0; i<maxrow.length; i++) {
			maxrow[i] = -1;
		}
		Iterator<CalcCompCellConstraints> it = cells.values().iterator();
		while (it.hasNext()) {
			cell = it.next();
			if (cell.getMaxRow()>maxrow[cell.col]) maxrow[cell.col] = cell.getMaxRow();
			if (cell.col>maxcol) maxcol = cell.col;
		}
		int row, col;
		col = maxcol;
		row = maxrow[col];
		row++;
		if ((row+rowspan-1)>=gridSize.height) {
			col++;
			row = 0;
		}
		if (col>=gridSize.width) {
			return; // too many
		}
		cell = new CalcCompCellConstraints(cmd, col, row, 1, rowspan);
		cells.put(cmd, cell);
	}

	/**
	 * @param cmd
	 * @param col
	 * @param row
	 * @param colspan
	 * @param rowspan
	 */
	public void addAt(String cmd, int col, int row, int colspan, int rowspan) {
		if (cells.contains(cmd))
			return;
			
		CalcCompCellConstraints cell;
		cell = new CalcCompCellConstraints(cmd, col, row, colspan, rowspan);
		cells.put(cmd, cell);
	}

	/**
	 * @param panSpace
	 * @param btnSize
	 * @param extraPanSpace
	 * @return	Returns the CalcCompGroup with expanded grid.
	 */
	public CalcCompGroup expandGrid(int panSpace, int btnSize, int extraPanSpace) {
		CalcCompGroup ccg = null;
		int expW;
		int expH;

		//   +-------------------------------+
		//   | +---+ +---+ +---+ +---+ +---+ |
		//   | | 0 | | 1 | | 2 | | 3 | | 4 | |
		expW = gridSize.width*(panSpace + btnSize) + panSpace + 2*extraPanSpace;
		expH = gridSize.height*(panSpace + btnSize) + panSpace + 2*extraPanSpace;
		ccg = new CalcCompGroup(name, expW, expH);

		CalcCompCellConstraints cell;
		CalcCompCellConstraints expCell;
		Iterator<CalcCompCellConstraints> it = getIterator();
		if (it==null) return null;

		while (it.hasNext()) {
			cell = it.next();
			expCell = new CalcCompCellConstraints(
					cell.name,
					extraPanSpace + panSpace + (cell.col)*(btnSize+panSpace),
					extraPanSpace + panSpace + (cell.row)*(btnSize+panSpace),
					cell.colspan*(panSpace + btnSize) - panSpace,
					cell.rowspan*(panSpace + btnSize) - panSpace);
			ccg.cells.put(expCell.name, expCell);
		}
		ccg.updateGridSize();

		return ccg;
	}

	/**
	 * @param atRow
	 * @param numRows
	 */
	public void shrinkRow(int atRow, int numRows) {
		// easy: shrink groupCell:
		groupCell.rowspan -= numRows;

		CalcCompCellConstraints cell;
		Iterator<CalcCompCellConstraints> it = getIterator();
		if (it==null) return;

		while (it.hasNext()) {
			cell = it.next();
			if (cell.getMaxRow()<atRow) {
				// nothing to do
				
			} else if (cell.getRow()>(atRow+numRows-1)) {
				// translate upwards
				cell.translate(0, -numRows);
			} else {
				int xlt = 0;
				// we are in between:
				if (cell.getRow()>atRow) {
					xlt = cell.getRow() - atRow;
					cell.translate(0, -xlt);
				}
				cell.rowspan -= numRows - xlt;
			}
		}
	}

	/**
	 * @return	Returns a string representation.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "";
		CalcCompCellConstraints cell;
		Iterator<CalcCompCellConstraints> it;

		s += this.getClass() + ":[";
		s += "name=" + name;
		s += ", grid=" + gridSize;
		s += ", groupCell=" + groupCell + "\n";
		s += "elements (" + cells.size() + ")\n";

		it = getIterator();
		if (it!=null) {
			while (it.hasNext()) {
				cell = it.next();
				s += "  cell=" + cell + "\n";
			}
		}
		s += "]";
		return s;
	}

	/**
	 * 
	 */
	public void updateGridSize() {
    	Iterator<CalcCompCellConstraints> it;
    	CalcCompCellConstraints cell;
    	int maxcol = 0;
    	int maxrow = 0;
    	int mincol = Integer.MAX_VALUE;
    	int minrow = Integer.MAX_VALUE;
    	if (cells==null) return;

    	it = cells.values().iterator();
    	while (it.hasNext()) {
    		cell = it.next();
    		if (cell.getMaxCol()>maxcol)
    			maxcol = cell.getMaxCol();
    		if (cell.getMaxRow()>maxrow)
    			maxrow = cell.getMaxRow();
    		if (cell.getCol()<mincol)
    			mincol = cell.getCol();
    		if (cell.getRow()<minrow)
    			minrow = cell.getRow();
    	}

    	cell = groupCell;
		if (cell.getMaxCol()>maxcol)
			maxcol = cell.getMaxCol();
		if (cell.getMaxRow()>maxrow)
			maxrow = cell.getMaxRow();
		if (cell.getCol()<mincol)
			mincol = cell.getCol();
		if (cell.getRow()<minrow)
			minrow = cell.getRow();

    	int cols = maxcol - mincol + 1;
    	int rows = maxrow - minrow + 1;

    	setGridSize(cols, rows);
	}

	/**
	 * @param col
	 * @param row
	 */
	public void translate(int col, int row) {
		CalcCompCellConstraints cell;
		Iterator<CalcCompCellConstraints> it = getIterator();
		if (it==null) return;

		while (it.hasNext()) {
			cell = it.next();
			cell.translate(col, row);
		}
		groupCell.translate(col, row);
		updateGridSize();
	}

	/**
	 * @return	Returns an iterator over the list of cell constraints.
	 */
	public Iterator<CalcCompCellConstraints> getIterator() {
		if (cells.isEmpty()) {
			return null;
		} else {
			return cells.values().iterator();
		}
	}

	/**
	 * @param cmd
	 * @return	Returns the constraints for the given command.
	 */
	public CalcCompCellConstraints getCellInfo(String cmd) {
		if (cells.contains(cmd)) {
			return cells.get(cmd);
		} else {
			return null;
		}
	}

	/**
	 * @return Returns the groupCell.
	 */
	public CalcCompCellConstraints getGroupCell() {
		return groupCell;
	}
	/**
	 * @param groupCell The groupCell to set.
	 */
	public void setGroupCell(CalcCompCellConstraints groupCell) {
		this.groupCell = groupCell;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the gridSize.
	 */
	public Dimension getGridSize() {
		return new Dimension(gridSize);
	}
	/**
	 * @param width
	 * @param height
	 */
	public void setGridSize(int width, int height) {
		gridSize = new Dimension(width, height);
	}

	/**
	 * @param dim
	 */
	public void setGridSize(Dimension dim) {
		gridSize = new Dimension(dim);
	}
}