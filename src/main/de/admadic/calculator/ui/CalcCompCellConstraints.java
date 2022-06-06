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

/**
 * Constraints class for the CalcCompLayout class.
 * Note: name and links are Strings and if they are empty or null, 
 * that property is not set.
 * 
 * @author Rainer Schwarze
 * 
 */
public class CalcCompCellConstraints {
	// FIXME: finish implementation of new Link system!
	/**
	 * @author Rainer Schwarze
	 */
	public static class Link implements Cloneable {
		/**
		 * Link to referenced cell with 'my' amount of outer pan
		 */
		public final static int SRC_OUTERPAN = 0;
		/**
		 * Link to referenced cell with 'my' amount of inner pan
		 */
		public final static int SRC_INNERPAN = 1;
		/**
		 * Link to referenced cell with 'that' amount of outer pan
		 */
		public final static int DST_OUTERPAN = 2;
		/**
		 * Link to referenced cell with 'that' amount of inner pan
		 */
		public final static int DST_INNERPAN = 3;

		String cellName;
		int type;
		int value;
		/**
		 * Creates a link with the specified parameters.
		 * 
		 * @param name	the cell name to link to
		 * @param type
		 * @param value
		 */
		public Link(String name, int type, int value) {
			super();
			this.cellName = name;
			this.type = type;
			this.value = value;
		}
		
		/**
		 * @return	Returns a cloned Link
		 * @throws CloneNotSupportedException
		 * @see java.lang.Object#clone()
		 */
		@Override
		public Link clone() throws CloneNotSupportedException {
			Link dst = (Link)super.clone();
			dst.cellName = this.cellName;
			dst.type = this.type;
			dst.value = this.value;
			return dst;
		}
	}

	// FIXME: check whether needed:
	/** maybe will removed */
	public final static int FIT_NONE = -1;
	/** maybe will removed */
	public final static int FIT_FIXED = 0;
	/** maybe will removed */
	public final static int FIT_BY_COL = 1;
	/** maybe will removed */
	public final static int FIT_BY_ROW = 2;

	/** maybe will removed */
	public final static int FIT_DEFAULT = FIT_BY_ROW;

	/**
	 * Direction NORTH of a Link
	 */
	public final static int NORTH = 0;
	/**
	 * Direction WEST of a Link
	 */
	public final static int WEST = 1;
	/**
	 * Direction EAST of a Link
	 */
	public final static int EAST = 2;
	/**
	 * Direction SOUTH of a Link
	 */
	public final static int SOUTH = 3;
	/**
	 * Number of directions (North, ..., South)
	 */
	public final static int DIRECTIONS = 4;
	
	String name;	// name of the cell

	// the size of the cell:
	int col;
	int row;
	int colspanBase;
	int rowspanBase;

	int colspan;	// calculated
	int rowspan;	// calculated
	boolean stretchColspan;	// is the cell stretchy or not?
	boolean stretchRowspan;	// -''-
	int majorGridWidth;		// grid size of major spacing
	int majorGridHeight;	// grid size of major spacing

	int innerPan;		// pan between elements
	int outerPan;		// pan around elements if it is a group

	boolean majorGrid;	// false=minorgrid

	// fitting:
	int fit;			// add by row or by column

	boolean fitted;		// fitting is finished
	boolean fitting;	// fitting is in progress

	Link [] links;		// array of links (for each direction)

	// deprecated:
	String linkNorth;	// ref to component "above" (in 2D)
	int offsetNorth;
	String linkWest;	// ref to component to the "left"
	int offsetWest;
	String linkSouth;	// ref to component "below" (in 2D)
	int offsetSouth;
	String linkEast;	// ref to component to the "right"
	int offsetEast;

	// deprecated:
	boolean layoutComponents;	// align also the children of a panel?

	// ?:
	boolean template;

	/**
	 * Returns a String representation of this CalcCompCellConstraints.
	 * (The output is an incomplete display - not all fields are shown.)
	 * 
	 * @return	Returns a String representation of this CalcCompCellConstraints.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "";
		s = "CalcCompCellConstraints:(" +
				" cmd=" + name +
				" col=" + col +
				" row=" + row +
				" colspan=" + colspan +
				" rowspan=" + rowspan;
		s += " )";
		return s;
	}

	/**
	 * A copy-constructor for CalcCompCellConstraints.
	 * @param src	The CalcCompCellConstraints object to copy the fields
	 * 				from.
	 */
	public CalcCompCellConstraints(CalcCompCellConstraints src) {
		super();
		this.template = src.template;
		this.name = src.name;
		this.col = src.col;
		this.row = src.row;
		this.colspan = src.colspan;
		this.rowspan = src.rowspan;
		this.colspanBase = src.colspanBase;
		this.rowspanBase = src.rowspanBase;
		this.majorGrid = src.majorGrid;
		this.majorGridWidth = src.majorGridWidth;
		this.majorGridHeight = src.majorGridHeight;
		this.fit = src.fit;
		this.linkNorth = src.linkNorth;
		this.offsetNorth = src.offsetNorth;
		this.linkWest = src.linkWest;
		this.offsetWest = src.offsetWest;
		this.linkEast = src.linkEast;
		this.offsetEast = src.offsetEast;
		this.linkSouth = src.linkSouth;
		this.offsetSouth = src.offsetSouth;
		this.layoutComponents = src.layoutComponents;
		this.innerPan = src.innerPan;
		this.outerPan = src.outerPan;
		this.fitted = src.fitted;
		this.fitting = src.fitting;
		this.stretchColspan = src.stretchColspan;
		this.stretchRowspan = src.stretchRowspan;

		this.links = new Link[DIRECTIONS];
		if (src.links==null) {
			// ?
		} else {
			try {
				for (int i=0; i<this.links.length; i++) {
					if (src.links[i]!=null) {
						this.links[i] = src.links[i].clone();
					} else {
						this.links[i] = src.links[i];
					}
				}
			} catch (CloneNotSupportedException e) {
				// FIXME: turn that into production code
				// e.printStackTrace();
				// this cannot happen!
			}
		}
	}

	/**
	 * Create an instance of CalcCompCellConstraints with the specified name.
	 * Column and Row are set to 0, ColSpan and RowSpan are set to 1. The
	 * cell is assumed to use majorGrid.
	 * 
	 * @param name	A String specifying the name of the cell.
	 */
	public CalcCompCellConstraints(String name) {
		this(name, 0, 0, 1, 1, FIT_DEFAULT, true);
	}

	/**
	 * Create an instance of CalcCompCellConstraints with the specified name.
	 * ColSpan and RowSpan are set to 1. The cell is assumed to use majorGrid.
	 * 
	 * @param name	A String specifying the name of the cell.
	 * @param col	An int specifying the column of the cell.
	 * @param row 	An int specifying the row of the cell.
	 */
	public CalcCompCellConstraints(String name, int col, int row) {
		this(name, col, row, 1, 1, FIT_DEFAULT, true);
	}

	/**
	 * Create an instance of CalcCompCellConstraints with the specified name.
	 * The cell is assumed to use majorGrid.
	 * 
	 * @param name	A String specifying the name of the cell.
	 * @param col	An int specifying the column of the cell.
	 * @param row 	An int specifying the row of the cell.
	 * @param colspan An int specifying the ColSpan of the cell.
	 * @param rowspan An int specifying the RowSpan of the cell.
	 */
	public CalcCompCellConstraints(String name, int col, int row, int colspan, int rowspan) {
		this(name, col, row, colspan, rowspan, FIT_DEFAULT, true);
	}

	/**
	 * Create an instance of CalcCompCellConstraints with the specified name.
	 * Column and Row are set to 0.
	 * 
	 * @param name	A String specifying the name of the cell.
	 * @param colspan An int specifying the ColSpan of the cell.
	 * @param rowspan An int specifying the RowSpan of the cell.
	 * @param fit	An int spefiying the fitting algorithm.
	 * @param majorGrid	A boolean specifying whether the cell uses majorGrid
	 * 					or minorGrid.
	 */
	public CalcCompCellConstraints(String name, int colspan, int rowspan, int fit, boolean majorGrid) {
		this(name, 0, 0, colspan, rowspan, fit, majorGrid);
	}

	/**
	 * Create an instance of CalcCompCellConstraints with the specified name.
	 * Column and Row are set to 0.
	 * 
	 * @param name	A String specifying the name of the cell.
	 * @param col	An int specifying the column of the cell.
	 * @param row 	An int specifying the row of the cell.
	 * @param colspan An int specifying the ColSpan of the cell.
	 * @param rowspan An int specifying the RowSpan of the cell.
	 * @param fit	An int spefiying the fitting algorithm.
	 * @param majorGrid	A boolean specifying whether the cell uses majorGrid
	 * 					or minorGrid.
	 */
	public CalcCompCellConstraints(String name, int col, int row, int colspan, int rowspan, int fit, boolean majorGrid) {
		super();
		// standard:
		this.template = false;
		this.majorGridWidth = 1;
		this.majorGridHeight = 1;

		this.name = name;
		this.col = col;
		this.row = row;
		this.colspanBase = colspan;
		this.rowspanBase = rowspan;
		this.fit = fit;
		this.majorGrid = majorGrid;

		this.links = new Link[DIRECTIONS];
		// standard settings:
		this.linkNorth = "";
		this.offsetNorth = 0;
		this.linkWest = "";
		this.offsetWest = 0;
		this.linkEast = "";
		this.offsetEast = 0;
		this.linkSouth = "";
		this.offsetSouth = 0;
		this.layoutComponents = false;
		this.innerPan = 0;
		this.outerPan = 0;
		this.fitted = false;
		this.fitting = false;
		this.stretchColspan = false;
		this.stretchRowspan = false;

		updateSpans();
	}

	/**
	 * Create a grid of cells returned in an array of CalcCompCellConstraints.
	 * 
	 * @param cols	Number of columns of the grid.
	 * @param rows	Number of rows of the grid.
	 * @param names	A String[] specifying the names of the cells.
	 * @return	Returns a CalcCompCellConstraints[] representing the grid.
	 */
	public static CalcCompCellConstraints[] createGrid(
			int cols, int rows, String [] names) {
		return CalcCompCellConstraints.createGrid(
				cols, rows, names, null, FIT_BY_ROW, null);
	}

	/**
	 * Create a grid of cells returned in an array of CalcCompCellConstraints.
	 * The names array can be null. In that case the names are generated.
	 * A generated cell name contains the cellPrefix or "cell", if the 
	 * namePrefix is null and an increasing number is appended to the name.
	 * The numbering starts with 0. If the names array has too few elements 
	 * for the complete grid (rows * cols) the names are filled with generated
	 * cell names.
	 * A template cell can be specified.
	 * FIXME: improve doc for that.
	 * 
	 * @param cols	Number of columns of the grid.
	 * @param rows	Number of rows of the grid.
	 * @param names	A String[] specifying the names of the cells (can be null).
	 * @param namePrefix	A String specifying a common prefix of the cell 
	 * 						names (can be null).
	 * @param fit	An int specifying the fitting algorithm.
	 * @param tmplCell	A CalcCompCellConstraints serving as a template for the
	 * 				cells of the grid.
	 * @return	Returns a CalcCompCellConstraints[] representing the grid.
	 */
	public static CalcCompCellConstraints[] createGrid(
			int cols, int rows, String [] names, String namePrefix, int fit,
			CalcCompCellConstraints tmplCell) {
		CalcCompCellConstraints [] cons;	// the result
		int cellcount = cols * rows;
		if (tmplCell==null) {
			// create a template cell if none is specified:
			tmplCell = new CalcCompCellConstraints("<template>");
			tmplCell.defineSpan(1, 1);
		}
		if (fit!=FIT_NONE)
			tmplCell.defineFit(fit);
		if (names==null) {
			// create the names array, if none is specified.
			names = new String[cellcount];
		} else if (names.length<cellcount) {
			// fill the names array, if it does not have enough elements:
			String [] names2 = new String[cellcount];
			System.arraycopy(names, 0, names2, 0, names.length);
			names = names2;
		}
		// set the un-initialised cell names:
		for (int i=0; i<names.length; i++) {
			if (names[i]==null || names[i].equals("")) {
				names[i] = ((namePrefix==null) ? "cell" : namePrefix) + 
					Integer.toString(i);
			}
		}
		// allocate the output array:
		cons = new CalcCompCellConstraints[cellcount];

		int row, col;
		for (int i=0; i<cellcount; i++) {
			cons[i] = new CalcCompCellConstraints(tmplCell);
			cons[i].resetLinks();
			cons[i].setName(names[i]);
			switch (tmplCell.fit) {
			case FIT_NONE: 
			case FIT_FIXED:
				throw new Error(
						"CalcCompCellConstraints" + 
						".createGrid: fit NONE or FIXED is not allowed.");
				//break;
			case FIT_BY_COL: // first fill columns (downwards)
				col = (i / rows);
				row = (i % rows);
				if (col>0) {
					cons[i].setLinkWest(
							cons[i-rows].getName(),
							0/*tmplCell.getInnerPan()*/
							);
				} else {
					if (tmplCell.hasWestLink()) {
						cons[i].setLinkWest(
								tmplCell.getLinkWest(),
								tmplCell.getOffsetWest());
					}
				}
				if (row>0) {
					cons[i].setLinkNorth(
							cons[i-1].getName(),
							0/*tmplCell.getInnerPan()*/
							);
				} else {
					if (tmplCell.hasNorthLink()) {
						cons[i].setLinkNorth(
								tmplCell.getLinkNorth(),
								tmplCell.getOffsetNorth());
					}
				}
				break;
			case FIT_BY_ROW: // first fill rows (sidewards)
				col = (i % cols);
				row = (i / cols);
				if (col>0) {
					cons[i].setLinkWest(
							cons[i-1].getName(),
							0/*tmplCell.getInnerPan()*/
							);
				} else {
					if (tmplCell.hasWestLink()) {
						cons[i].setLinkWest(
								tmplCell.getLinkWest(),
								tmplCell.getOffsetWest());
					}
				}
				if (row>0) {
					cons[i].setLinkNorth(
							cons[i-cols].getName(),
							0/*tmplCell.getInnerPan()*/
							);
				} else {
					if (tmplCell.hasNorthLink()) {
						cons[i].setLinkNorth(
								tmplCell.getLinkNorth(),
								tmplCell.getOffsetNorth());
					}
				}
				break;
			default:
				throw new Error(
						"CalcCompCellConstraints" + 
						".createGrid: invalid fit value: " + tmplCell.fit);
			}
			col *= tmplCell.getColspan();
			row *= tmplCell.getRowspan();
			cons[i].setCol(col);
			cons[i].setRow(row);
		}
		return cons;
	}

	/**
	 * Merges the two specified cells. The cell <code>dst</code> is modified
	 * accordingly.
	 * 
	 * @param dst	The cell to merge into.
	 * @param src	The cell to merge into dst.
	 */
	public static void mergeCells(
			CalcCompCellConstraints dst,
			CalcCompCellConstraints src) {
		// horz or vert merge?
		// [  ]  [  ]  [  ]
		//
		// [  ]
		// [  ]
		// [  ]
		if (dst.getCol()==src.getCol()) {
			// vertically
			if (!dst.hasWestLink()) {
				if (src.hasWestLink()) {
					dst.setLinkWest(
							src.getLinkWest(),
							src.getOffsetWest());
				}
			}
			if (!dst.hasEastLink()) {
				if (src.hasEastLink()) {
					dst.setLinkEast(
							src.getLinkEast(),
							src.getOffsetEast());
				}
			}
			if (!dst.hasSouthLink()) {
				if (src.hasSouthLink()) {
					dst.setLinkSouth(
							src.getLinkSouth(),
							src.getOffsetSouth());
				}
			}
			//dst.rowspan += dst.innerPan + src.rowspan;
			dst.rowspanBase += src.rowspanBase;
		} else if (dst.getRow()==src.getRow()) {
			// horizontally
			if (!dst.hasNorthLink()) {
				if (src.hasNorthLink()) {
					dst.setLinkNorth(
							src.getLinkNorth(),
							src.getOffsetNorth());
				}
			}
			if (!dst.hasSouthLink()) {
				if (src.hasSouthLink()) {
					dst.setLinkSouth(
							src.getLinkSouth(),
							src.getOffsetSouth());
				}
			}
			if (!dst.hasEastLink()) {
				if (src.hasEastLink()) {
					dst.setLinkEast(
							src.getLinkEast(),
							src.getOffsetEast());
				}
			}
			//dst.colspan += dst.innerPan + src.colspan;
			dst.colspanBase += src.colspanBase;
		} else {
			// complex?!
			// not supported yet
			throw new Error("Cannot merge cells without same col or same row");
		}
	}

	/**
	 * Merge cells in the specified array of CalcCompCellConstraints.
	 * The cells to merge are defined by the array of cell names. The
	 * first element in the array is the cell which is the resulting 
	 * merged cell.
	 * 
	 * @param cons
	 * @param names
	 * @return	Returns the array of CalcCompCellConstraints containing
	 * 		the now merged cells.
	 */
	public static CalcCompCellConstraints [] mergeCells(
			CalcCompCellConstraints [] cons,
			String [] names) {
		// FIXME: complete implementation of this function (mergeCells)
		if (cons==null) return null;
		if (names==null) return cons;
		if (names.length<=1) return cons;

		int considx4names[] = new int[names.length]; 

		for (int i=0; i<names.length; i++) {
			if (names[i]==null) {
				throw new Error("some names for cells to merge are null");
			}
			if (names[i].equals("")) {
				throw new Error("some names for cells to merge are empty");
			}
			considx4names[i] = -1;
			for (int j=0; j<cons.length; j++) {
				if (cons[j].getName().equals(names[i])) {
					considx4names[i] = j;
					break;
				}
			}
			if (considx4names[i]==-1) {
				throw new Error("some names cannot be found in constraints (" + names[i] + ")");
			}
		}

		CalcCompCellConstraints [] cons2 = 
			new CalcCompCellConstraints[cons.length-names.length+1];  

		int cons2i = 0;
		for (int i=0; i<cons.length; i++) {
			boolean matchMerge = false;
			int mergeSrc = -1;
			// #0 is not checked, because it is the destination
			for (int j=1; j<considx4names.length; j++) {
				if (i==considx4names[j]) {
					matchMerge = true;
					mergeSrc = j;
					break;
				}
			}

			if (matchMerge) {
				CalcCompCellConstraints consrc, condst;
				condst = cons[considx4names[0]];
				consrc = cons[considx4names[mergeSrc]];

				mergeCells(condst, consrc);
			} else {
				cons2[cons2i] = cons[i];
				cons2i++;
			}
		}

		return cons2;
	}

	/**
	 * Remove the cells listed in the array of cell names from the array of 
	 * CalcCompCellConstraints.
	 *  
	 * @param cons
	 * @param names
	 * @return	Returns the array of CalcCompCellConstraints with the 
	 * 		remaining cells.
	 */
	public static CalcCompCellConstraints [] removeCells(
			CalcCompCellConstraints [] cons,
			String [] names) {
		if (cons==null) return null;
		if (names==null) return cons;
		if (names.length<=1) return cons;

		for (int i=0; i<names.length; i++) {
			if (names[i]==null) {
				throw new Error("some names for cells to remove are null");
			}
			if (names[i].equals("")) {
				throw new Error("some names for cells to remove are empty");
			}
		}

		CalcCompCellConstraints [] cons2 = 
			new CalcCompCellConstraints[cons.length-names.length];  

		int cons2i = 0;
		for (int i=0; i<cons.length; i++) {
			boolean matchRemove = false;
			for (int j=0; j<names.length; j++) {
				if (cons[i].getName().equals(names[j])) {
					matchRemove = true;
					break;
				}
			}
			if (matchRemove) {
				// nothing
			} else {
				cons2[cons2i] = cons[i];
				cons2i++;
			}
		}

		return cons;
	}

	// ///////////////////////////////////////////////////////
	// methods for easy generation:
	// ///////////////////////////////////////////////////////

	/**
	 * @param colspan
	 * @param rowspan
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineSpan(int colspan, int rowspan) {
		this.colspanBase = colspan;
		this.rowspanBase = rowspan;
		updateSpans();
		return this;
	}
	/**
	 * @param fit
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineFit(int fit) {
		this.fit = fit;
		return this;
	}
	/**
	 * @param col
	 * @param row
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints definePlace(int col, int row) {
		this.col = col;
		this.row = row;
		return this;
	}
	/**
	 * @param width
	 * @param height
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineMajorGrid(int width, int height) {
		this.majorGridWidth = width;
		this.majorGridHeight = height;
		updateSpans();
		return this;
	}
	/**
	 * @param linkNorth
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineLinkNorth(String linkNorth) {
		this.linkNorth = linkNorth;
		return this;
	}
	/**
	 * @param linkWest
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineLinkWest(String linkWest) {
		this.linkWest = linkWest;
		return this;
	}
	/**
	 * @param linkEast
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineLinkEast(String linkEast) {
		this.linkEast = linkEast;
		return this;
	}
	/**
	 * @param linkSouth
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineLinkSouth(String linkSouth) {
		this.linkSouth = linkSouth;
		return this;
	}
	/**
	 * @param linkNorth
	 * @param offsetNorth
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineLinkNorth(String linkNorth, int offsetNorth) {
		this.linkNorth = linkNorth;
		this.offsetNorth = offsetNorth;
		return this;
	}
	/**
	 * @param linkWest
	 * @param offsetWest
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineLinkWest(String linkWest, int offsetWest) {
		this.linkWest = linkWest;
		this.offsetWest = offsetWest;
		return this;
	}
	/**
	 * @param linkEast
	 * @param offsetEast
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineLinkEast(String linkEast, int offsetEast) {
		this.linkEast = linkEast;
		this.offsetEast = offsetEast;
		return this;
	}
	/**
	 * @param linkSouth
	 * @param offsetSouth
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineLinkSouth(String linkSouth, int offsetSouth) {
		this.linkSouth = linkSouth;
		this.offsetSouth = offsetSouth;
		return this;
	}
	/**
	 * @param layoutComponents
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineLayoutComponents(boolean layoutComponents) {
		this.layoutComponents = layoutComponents;
		return this;
	}
	/**
	 * @param innerPan
	 * @param outerPan
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints definePan(int innerPan, int outerPan) {
		this.innerPan = innerPan;
		this.outerPan = outerPan;
		updateSpans();
		return this;
	}
	/**
	 * @param stretchColspan
	 * @param stretchRowspan
	 * @return	Returns <code>this</code> which containts the updated fields.
	 */
	public CalcCompCellConstraints defineStretch(boolean stretchColspan, boolean stretchRowspan) {
		this.stretchColspan = stretchColspan;
		this.stretchRowspan = stretchRowspan;
		return this;
	}


	/**
	 * Translates the cell by (col,row)
	 * 
	 * @param col	the column by which to translate
	 * @param row	the row by which to translate
	 */
	public void translate(int col, int row) {
		this.col += col;
		this.row += row;
	}

	/**
	 * Clears all the links of this CalcCompCellConstraints.
	 */
	public void resetLinks() {
		setLinkNorth(null);
		setLinkWest(null);
		setLinkEast(null);
		setLinkSouth(null);
	}

	/**
	 * Update the actually used colspan and rowspan on base of 
	 * colspanbase and majorgridsize (including inner panning).
	 */
	public void updateSpans() {
		colspan = colspanBase*majorGridWidth + (colspanBase-1)*innerPan;
		rowspan = rowspanBase*majorGridHeight + (rowspanBase-1)*innerPan;
	}
	
	/**
	 * @return Returns the cells name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param cmd The cmd to set.
	 */
	public void setName(String cmd) {
		this.name = cmd;
	}
	/**
	 * @return Returns the col.
	 */
	public int getCol() {
		return col;
	}
	/**
	 * @param col The col to set.
	 */
	public void setCol(int col) {
		this.col = col;
	}
	/**
	 * @return Returns the colspan.
	 */
	public int getColspan() {
		return colspan;
	}
	/**
	 * @param colspan The colspan to set.
	 */
	public void setColspan(int colspan) {
		this.colspan = colspan;
	}
	/**
	 * @return Returns the row.
	 */
	public int getRow() {
		return row;
	}
	/**
	 * @param row The row to set.
	 */
	public void setRow(int row) {
		this.row = row;
	}
	/**
	 * @return Returns the rowspan.
	 */
	public int getRowspan() {
		return rowspan;
	}
	/**
	 * @param rowspan The rowspan to set.
	 */
	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}
	/**
	 * @return Returns the maximum row of this cell.
	 */
	public int getMaxRow() {
		return row + rowspan - 1;
	}
	/**
	 * @return Returns the maximum col of this cell.
	 */
	public int getMaxCol() {
		return col + colspan - 1;
	}

	/**
	 * @return Returns the layoutComponents.
	 */
	public boolean isLayoutComponents() {
		return layoutComponents;
	}

	/**
	 * @param layoutComponents The layoutComponents to set.
	 */
	public void setLayoutComponents(boolean layoutComponents) {
		this.layoutComponents = layoutComponents;
	}

	/**
	 * @return Returns the linkEast.
	 */
	public String getLinkEast() {
		return linkEast;
	}

	/**
	 * @param linkEast The linkEast to set.
	 */
	public void setLinkEast(String linkEast) {
		setLinkEast(linkEast, 0);
	}

	/**
	 * @param linkEast The linkEast to set.
	 * @param offsetEast The offsetEast to set.
	 */
	public void setLinkEast(String linkEast, int offsetEast) {
		this.linkEast = linkEast;
		this.offsetEast = offsetEast;
	}

	/**
	 * @return Returns the linkNorth.
	 */
	public String getLinkNorth() {
		return linkNorth;
	}

	/**
	 * @param linkNorth The linkNorth to set.
	 */
	public void setLinkNorth(String linkNorth) {
		setLinkNorth(linkNorth, 0);
	}

	/**
	 * @param linkNorth The linkNorth to set.
	 * @param offsetNorth The offsetNorth to set.
	 */
	public void setLinkNorth(String linkNorth, int offsetNorth) {
		this.linkNorth = linkNorth;
		this.offsetNorth = offsetNorth;
	}

	/**
	 * @return Returns the linkSouth.
	 */
	public String getLinkSouth() {
		return linkSouth;
	}

	/**
	 * @param linkSouth The linkSouth to set.
	 */
	public void setLinkSouth(String linkSouth) {
		setLinkSouth(linkSouth, 0);
	}

	/**
	 * @param linkSouth The linkSouth to set.
	 * @param offsetSouth The offsetSouth to set.
	 */
	public void setLinkSouth(String linkSouth, int offsetSouth) {
		this.linkSouth = linkSouth;
		this.offsetSouth = offsetSouth;
	}

	/**
	 * @return Returns the linkWest.
	 */
	public String getLinkWest() {
		return linkWest;
	}

	/**
	 * @param linkWest The linkWest to set.
	 */
	public void setLinkWest(String linkWest) {
		setLinkWest(linkWest, 0);
	}

	/**
	 * @param linkWest The linkWest to set.
	 * @param offsetWest The offsetWest to set.
	 */
	public void setLinkWest(String linkWest, int offsetWest) {
		this.linkWest = linkWest;
		this.offsetWest = offsetWest;
	}

	/**
	 * @return Returns the offsetEast.
	 */
	public int getOffsetEast() {
		return offsetEast;
	}

	/**
	 * @param offsetEast The offsetEast to set.
	 */
	public void setOffsetEast(int offsetEast) {
		this.offsetEast = offsetEast;
	}

	/**
	 * @return Returns the offsetNorth.
	 */
	public int getOffsetNorth() {
		return offsetNorth;
	}

	/**
	 * @param offsetNorth The offsetNorth to set.
	 */
	public void setOffsetNorth(int offsetNorth) {
		this.offsetNorth = offsetNorth;
	}

	/**
	 * @return Returns the offsetSouth.
	 */
	public int getOffsetSouth() {
		return offsetSouth;
	}

	/**
	 * @param offsetSouth The offsetSouth to set.
	 */
	public void setOffsetSouth(int offsetSouth) {
		this.offsetSouth = offsetSouth;
	}

	/**
	 * @return Returns the offsetWest.
	 */
	public int getOffsetWest() {
		return offsetWest;
	}

	/**
	 * @param offsetWest The offsetWest to set.
	 */
	public void setOffsetWest(int offsetWest) {
		this.offsetWest = offsetWest;
	}

	/**
	 * @return Returns the majorGrid.
	 */
	public boolean isMajorGrid() {
		return majorGrid;
	}

	/**
	 * @param majorGrid The majorGrid to set.
	 */
	public void setMajorGrid(boolean majorGrid) {
		this.majorGrid = majorGrid;
	}

	/**
	 * @return Returns the innerPan.
	 */
	public int getInnerPan() {
		return innerPan;
	}

	/**
	 * @param innerPan The innerPan to set.
	 */
	public void setInnerPan(int innerPan) {
		this.innerPan = innerPan;
	}

	/**
	 * @return Returns the outerPan.
	 */
	public int getOuterPan() {
		return outerPan;
	}

	/**
	 * @param outerPan The outerPan to set.
	 */
	public void setOuterPan(int outerPan) {
		this.outerPan = outerPan;
	}

	/**
	 * @return Returns the fitted.
	 */
	public boolean isFitted() {
		return fitted;
	}

	/**
	 * @param fitted The fitted to set.
	 */
	public void setFitted(boolean fitted) {
		this.fitted = fitted;
	}

	/**
	 * @return Returns the fitting.
	 */
	public boolean isFitting() {
		return fitting;
	}

	/**
	 * @param fitting The fitting to set.
	 */
	public void setFitting(boolean fitting) {
		this.fitting = fitting;
	}

	/**
	 * @return Returns whether a north link is there or not
	 */
	public boolean hasNorthLink() {
		return linkNorth!=null && !linkNorth.equals("");
	}
	/**
	 * @return Returns whether a west link is there or not
	 */
	public boolean hasWestLink() {
		return linkWest!=null && !linkWest.equals("");
	}
	/**
	 * @return Returns whether a east link is there or not
	 */
	public boolean hasEastLink() {
		return linkEast!=null && !linkEast.equals("");
	}
	/**
	 * @return Returns whether a south link is there or not
	 */
	public boolean hasSouthLink() {
		return linkSouth!=null && !linkSouth.equals("");
	}

	/**
	 * @return Returns whether the cell is fixed or fitted
	 */
	public boolean isFixed() {
		return fit==FIT_FIXED;
	}	
	/**
	 * @return Returns whether the is stretching
	 */
	public boolean isStretching() {
		return stretchColspan || stretchRowspan;
	}

	/**
	 * @return Returns the stretchColspan.
	 */
	public boolean isStretchColspan() {
		return stretchColspan;
	}

	/**
	 * @param stretchColspan The stretchColspan to set.
	 */
	public void setStretchColspan(boolean stretchColspan) {
		this.stretchColspan = stretchColspan;
	}

	/**
	 * @return Returns the stretchRowspan.
	 */
	public boolean isStretchRowspan() {
		return stretchRowspan;
	}

	/**
	 * @param stretchRowspan The stretchRowspan to set.
	 */
	public void setStretchRowspan(boolean stretchRowspan) {
		this.stretchRowspan = stretchRowspan;
	}

	/**
	 * @return Returns the colspanBase.
	 */
	public int getColspanBase() {
		return colspanBase;
	}

	/**
	 * @param colspanBase The colspanBase to set.
	 */
	public void setColspanBase(int colspanBase) {
		this.colspanBase = colspanBase;
	}

	/**
	 * @return Returns the rowspanBase.
	 */
	public int getRowspanBase() {
		return rowspanBase;
	}

	/**
	 * @param rowspanBase The rowspanBase to set.
	 */
	public void setRowspanBase(int rowspanBase) {
		this.rowspanBase = rowspanBase;
	}

	/**
	 * @return Returns the majorGridHeight.
	 */
	public int getMajorGridHeight() {
		return majorGridHeight;
	}

	/**
	 * @param majorGridHeight The majorGridHeight to set.
	 */
	public void setMajorGridHeight(int majorGridHeight) {
		this.majorGridHeight = majorGridHeight;
	}

	/**
	 * @return Returns the majorGridWidth.
	 */
	public int getMajorGridWidth() {
		return majorGridWidth;
	}

	/**
	 * @param majorGridWidth The majorGridWidth to set.
	 */
	public void setMajorGridWidth(int majorGridWidth) {
		this.majorGridWidth = majorGridWidth;
	}

	/**
	 * @return Returns the template.
	 */
	public boolean isTemplate() {
		return template;
	}

	/**
	 * @param template The template to set.
	 */
	public void setTemplate(boolean template) {
		this.template = template;
	}	
}