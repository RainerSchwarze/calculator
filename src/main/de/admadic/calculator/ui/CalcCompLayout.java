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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * The <code>GraphPaperLayout</code> class is a layout manager that
 * lays out a container's components in a rectangular grid, similar
 * to GridLayout.  Unlike GridLayout, however, components can take
 * up multiple rows and/or columns.  The layout manager acts as a
 * sheet of graph paper.  When a component is added to the layout
 * manager, the location and relative size of the component are
 * simply supplied by the constraints as a Rectangle.
 * <p><code><pre>
 * import java.awt.*;
 * import java.applet.Applet;
 * public class ButtonGrid extends Applet {
 *     public void init() {
 *         setLayout(new GraphPaperLayout(new Dimension(5,5)));
 *         // Add a 1x1 Rect at (0,0)
 *         add(new Button("1"), new Rectangle(0,0,1,1));
 *         // Add a 2x1 Rect at (2,0)
 *         add(new Button("2"), new Rectangle(2,0,2,1));
 *         // Add a 1x2 Rect at (1,1)
 *         add(new Button("3"), new Rectangle(1,1,1,2));
 *         // Add a 2x2 Rect at (3,2)
 *         add(new Button("4"), new Rectangle(3,2,2,2));
 *         // Add a 1x1 Rect at (0,4)
 *         add(new Button("5"), new Rectangle(0,4,1,1));
 *         // Add a 1x2 Rect at (2,3)
 *         add(new Button("6"), new Rectangle(2,3,1,2));
 *     }
 * }
 * </pre></code>
 *
 * @author      Michael Martak
 * @author		Rainer Schwarze
 */

public class CalcCompLayout implements LayoutManager2 {
	boolean DBGforce = false;
	boolean DBGfitting = false;
	boolean DBGgridsize = false;
	boolean DBGreset = false;
	
	/*
	 * Helper classes for layout process: They hold the positions of
	 * columns and rows of the grid. Thus instead of calculating
	 * the actual bounds of components each time, the cell of it
	 * is determined and the actual bound values are taken from the 
	 * list of ???Specs. Otherwise rounding of int operations may lead
	 * to a uneven distributed component alignment.
	 */
	class HorzSpec {
		int xl;
		int xr;
		/**
		 * @param xl
		 * @param xr
		 */
		public HorzSpec(int xl, int xr) {
			this.xl = xl;
			this.xr = xr;
		}
	}
	class VertSpec {
		int yt;
		int yb;
		/**
		 * @param yt
		 * @param yb
		 */
		public VertSpec(int yt, int yb) {
			this.yt = yt;
			this.yb = yb;
		}
	}

    Dimension gridSize;  //grid size in logical units (n x m)
    Dimension majorGridSize;
    Dimension minCellSize;
    boolean fixedCellSize;
    boolean forceFixedCellSize;
    Hashtable<Component, CalcCompCellConstraints> compTable; //constraints (CellConstraints)
	HorzSpec [] colBounds;	// xleft and xright of columns
	VertSpec [] rowBounds;	// ytop and ybottom of rows
	float alignmentX;
	float alignmentY;
	boolean shrinkWrap;
	int shrinkPan;
	Hashtable <String,CalcCompCellConstraints> conHash;

	Dimension lastPreferredSize;
	
	/**
     * Creates a graph paper layout with a default of a 1 x 1 graph.
     */
    public CalcCompLayout() {
        this(new Dimension(1,1));
    }

    /**
     * Creates a graph paper layout with the given grid size.
     * @param gridSize size of the graph paper in logical units (n x m)
     */
    public CalcCompLayout(Dimension gridSize) {
        if ((gridSize.width <= 0) || (gridSize.height <= 0)) {
            throw new IllegalArgumentException(
                "dimensions must be greater than zero");
        }
        this.gridSize = new Dimension(gridSize);
        this.majorGridSize = new Dimension(1, 1);
        this.minCellSize = null;
        compTable = new Hashtable<Component, CalcCompCellConstraints>();
        alignmentX = 0.5f;
        alignmentY = 0.5f;
        shrinkWrap = false;
        shrinkPan = 0;
        conHash = null;
        fixedCellSize = false;
    }

    /**
     * @return the size of the graph paper in logical units (n x m)
     */
    public Dimension getGridSize() {
        return new Dimension( gridSize );
    }

    /**
     * Set the size of the graph paper in logical units (n x m)
     * @param d 
     */
    public void setGridSize( Dimension d ) {
        setGridSize( d.width, d.height );
    }

    /**
     * Set the size of the graph paper in logical units (n x m)
     * @param width 
     * @param height 
     */
    public void setGridSize( int width, int height ) {
        gridSize = new Dimension( width, height );
    }

    /**
     * @param comp
     * @param constraints
     */
    public void setConstraints(Component comp, CalcCompCellConstraints constraints) {
    	if (constraints.isTemplate()) {
    		constraints = new CalcCompCellConstraints(constraints);
    		constraints.setTemplate(false);
    	}
        compTable.put(comp, constraints);
    }

    /**
     * Adds the specified component with the specified name to
     * the layout.  This does nothing in GraphPaperLayout, since constraints
     * are required.
     * @param name 
     * @param comp 
     */
    public void addLayoutComponent(String name, Component comp) {
    	// nothing
    }

    /**
     * Removes the specified component from the layout.
     * @param comp the component to be removed
     */
    public void removeLayoutComponent(Component comp) {
        compTable.remove(comp);
    }

    /**
     * Calculates the preferred size dimensions for the specified
     * panel given the components in the specified parent container.
     * @param parent the component to be laid out
     * @return Returns the preferred Layout size
     *
     * @see #minimumLayoutSize
     */
    public Dimension preferredLayoutSize(Container parent) {
    	lastPreferredSize = getLayoutSize(parent, true);
    	return new Dimension(lastPreferredSize);
    }

    /**
     * Calculates the minimum size dimensions for the specified
     * panel given the components in the specified parent container.
     * @param parent the component to be laid out
     * @return Returns the minimum layout size (same as preferred)
     * @see #preferredLayoutSize
     */
    public Dimension minimumLayoutSize(Container parent) {
        return getLayoutSize(parent, false);
    }

    /**
     * Returns the maximum size of this component.
     * @param parent 
     * @return Returns the maximum layout size
     * @see java.awt.Component#getMinimumSize()
     * @see java.awt.Component#getPreferredSize()
     * @see LayoutManager
     */
    public Dimension maximumLayoutSize(Container parent) {
    	return getLayoutSize(parent, true);
        // return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Determine the underlying grid size for the layout.
     */
    protected void updateGridSize() {
    	Iterator<CalcCompCellConstraints> it;
    	CalcCompCellConstraints cell;
    	int maxcol = 0;
    	int maxrow = 0;
    	int mincol = Integer.MAX_VALUE;
    	int minrow = Integer.MAX_VALUE;
    	if (compTable==null) return;
    	boolean DBG = false;
    	String DBGci = "";

    	it = compTable.values().iterator();
    	while (it.hasNext()) {
    		cell = it.next();
    		if (DBG || DBGgridsize) DBGci = " < cell=" + cell.getName() + 
    			" (w/h)=(" + cell.majorGridWidth + "/" + cell.majorGridHeight + ")";
    		if (cell.getMaxCol()>maxcol) {
    			maxcol = cell.getMaxCol();
    			if (DBG || DBGgridsize) System.err.println("    >>maxcol = " + maxcol + DBGci);
    		}
    		if (cell.getMaxRow()>maxrow) {
    			maxrow = cell.getMaxRow();
    			if (DBG || DBGgridsize) System.err.println("  vv  maxrow = " + maxrow + DBGci);
    		}
    		if (cell.getCol()<mincol) {
    			mincol = cell.getCol();
    			if (DBG || DBGgridsize) System.err.println("<<    mincol = " + mincol + DBGci);
    		}
    		if (cell.getRow()<minrow) {
    			minrow = cell.getRow();
    			if (DBG || DBGgridsize) System.err.println("  ^^  minrow = " + minrow + DBGci);
    		}
    	}
   
    	if (DBG || DBGforce) System.err.println("grid: mincol/minrow=" + mincol + "/" + minrow + " maxcol/maxrow=" + maxcol + "/" + maxrow);

    	if (shrinkWrap) {
        	if (DBG || DBGforce) System.err.println("shrink");
    		setGridSize(
    				maxcol-mincol+1+2*shrinkPan, 
    				maxrow-minrow+1+2*shrinkPan);

        	it = compTable.values().iterator();
        	while (it.hasNext()) {
        		cell = it.next();
        		cell.col -= (mincol - shrinkPan);
        		cell.row -= (minrow - shrinkPan);
        	}
    	} else {
	    	// if maxrow is 0, we have 1 rows:
	    	maxcol++;
	    	maxrow++;
	    	if ((maxcol>gridSize.width) || (maxrow>gridSize.height)) {
	    		setGridSize(maxcol, maxrow);
	    	}
    	}
    }
    
    /**
     * Algorithm for calculating layout size (minimum or preferred).
     * <p>
     * The width of a graph paper layout is the largest cell width
     * (calculated in <code>getLargestCellSize()</code> times the number of
     * columns, plus the horizontal padding times the number of columns
     * plus one, plus the left and right insets of the target container.
     * <p>
     * The height of a graph paper layout is the largest cell height
     * (calculated in <code>getLargestCellSize()</code> times the number of
     * rows, plus the vertical padding times the number of rows
     * plus one, plus the top and bottom insets of the target container.
     *
     * @param parent the container in which to do the layout.
     * @param isPreferred true for calculating preferred size, false for
     *                    calculating minimum size.
     * @return the dimensions to lay out the subcomponents of the specified
     *         container.
     * @see #getLargestCellSize
     */
    protected Dimension getLayoutSize(Container parent, boolean isPreferred) {
    	//updateGridSize();
    	boolean DBG = false;
        if (DBG || DBGforce) System.err.println("getLayoutSize:");
        Dimension largestSize = getLargestCellSize(parent, isPreferred);
        if (DBG || DBGforce) System.err.println("ccl: ls-cell: " + largestSize + " (gs=" + gridSize + ")");
        Insets insets = parent.getInsets();
        if (DBG || DBGforce) System.err.println("ccl: ls-inst: " + insets);
        largestSize.width = ( largestSize.width * gridSize.width ) +
            insets.left + insets.right;
        largestSize.height = ( largestSize.height * gridSize.height ) +
            insets.top + insets.bottom;
        if (DBG || DBGforce) System.err.println("ccl: ls-layt: " + largestSize);
        return largestSize;
    }

    /**
     * Algorithm for calculating the largest minimum or preferred cell size.
     * <p>
     * Largest cell size is calculated by getting the applicable size of each
     * component and keeping the maximum value, dividing the component's width
     * by the number of columns it is specified to occupy and dividing the
     * component's height by the number of rows it is specified to occupy.
     *
     * @param parent the container in which to do the layout.
     * @param isPreferred true for calculating preferred size, false for
     *                    calculating minimum size.
     * @return the largest cell size required.
     */
    protected Dimension getLargestCellSize(
    		Container parent,
            boolean isPreferred) {
        resetStretch(parent);
        processFitBuildConHash(parent);
    	processFit(parent);
    	updateGridSize();
    	processStretch(parent);
    	boolean DBG = false;

    	// if fixedCellSize, return minCellSize.
    	// if that is not set, fixedCellSize has no effect and the
    	// cell size is calculated:
    	if (fixedCellSize) {
    		if (minCellSize!=null && minCellSize.height!=0 && minCellSize.width!=0)
    			if (DBG || DBGforce) System.err.println("getLargestCellSize: fixed");
    			return new Dimension(minCellSize);
    	}
    	if (DBG || DBGforce) System.err.println("getLargestCellSize: dynamic");
    	
        int ncomponents = parent.getComponentCount();
        Dimension maxCellSize = new Dimension(0,0);
        if (minCellSize!=null) {
        	maxCellSize = new Dimension(minCellSize);
        }
        if (DBG || DBGforce) System.out.println("largest cell size:");
        for ( int i = 0; i < ncomponents; i++ ) {
            Component c = parent.getComponent(i);
            CalcCompCellConstraints cell = compTable.get(c);
            if ( c != null && cell != null ) {
                Dimension componentSize;
                if ( isPreferred ) {
                    componentSize = c.getPreferredSize();
                } else {
                    componentSize = c.getMinimumSize();
                }
                // Note: rect dimensions are already asserted to be > 0 when the
                // component is added with constraints
                if ((componentSize.width / cell.colspan)>maxCellSize.width) {
                	maxCellSize.width = componentSize.width / cell.colspan;
                	if (DBG || DBGforce) System.out.println("layout: grow width: (" + maxCellSize.width + ") " + c);
                }
                if ((componentSize.height / cell.rowspan)>maxCellSize.height) {
                	maxCellSize.height = componentSize.height / cell.rowspan;
                	if (DBG || DBGforce) System.out.println("layout: grow height: (" + maxCellSize.height + ") " + c);
                }
                //org:
//                maxCellSize.width = Math.max(maxCellSize.width,
//                        componentSize.width / cell.colspan);
//                    maxCellSize.height = Math.max(maxCellSize.height,
//                        componentSize.height / cell.rowspan);
            }
        }
        if (DBG || DBGforce) System.out.println("largest cell size (" + isPreferred + ") = " + maxCellSize);
        return maxCellSize;
    }

    /**
     * Allocate cell bounds arrays.
     */
    protected void initCellBounds() {
    	// allocate cell grid, if it is not initialized or
    	// if the dimensions have changed
    	if ((colBounds==null) || (colBounds.length!=gridSize.width)) {
    		colBounds = new HorzSpec[gridSize.width];
    	}
    	if ((rowBounds==null) || (rowBounds.length!=gridSize.height)) {
    		rowBounds = new VertSpec[gridSize.height];
    	}
    }

    /**
     * Lays out the container in the specified container.
     * @param parent the component which needs to be laid out
     */
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
        	boolean DBG = false;
        	if (DBG || DBGforce) System.err.println("layoutContainer");
            int ncomponents = parent.getComponentCount();

            if (ncomponents == 0) {
                return;
            }

            resetStretch(parent);
            processFitBuildConHash(parent);
        	processFit(parent);
        	updateGridSize();
        	processStretch(parent);

        	// allocate the bounds arrays if necessary
        	initCellBounds();

            // Total parent dimensions
        	Insets insets = parent.getInsets();
            Dimension size = parent.getSize();
            // BUGFIX: there is a bug where the preferred size is not passed
            // as the initial layout size. The height differs by 1.
            if (lastPreferredSize!=null) {
            	if (Math.abs(lastPreferredSize.height - size.height)<=1) {
            		size = new Dimension(lastPreferredSize);
            	}
            }
            // Cell dimensions, including padding
            int totalCellW;
            int totalCellH;
            // Cell dimensions, without padding
            int cellW;
            int cellH;
            int totalW;
            int totalH;

            if (DBG || DBGforce) System.err.println(
            		"size=" + size + 
            		" insets=" + insets);

            totalW = size.width - (insets.left + insets.right);
            totalH = size.height - (insets.top + insets.bottom);

            if (isForceFixedCellSize()) {
            	totalCellW = minCellSize.width;
            	totalCellH = minCellSize.height;
            	cellW = minCellSize.width;
            	cellH = minCellSize.height;
            } else {
            	totalCellW = totalW / gridSize.width;
            	totalCellH = totalH / gridSize.height;
                cellW = (totalW) / gridSize.width;
                cellH = (totalH) / gridSize.height;
            }

            if (DBG || DBGforce) System.err.println(
            		"gridSize=" + gridSize + 
            		" totalCellW/H=" + totalCellW + "," + totalCellH); 

            // set bounds for rows and columns
            for (int i = 0; i<gridSize.width; i++) {
            	colBounds[i] = new HorzSpec(
            			insets.left + ( totalCellW * i ),
            			insets.left + ( totalCellW * i ) + cellW);
            }
            for (int i = 0; i<gridSize.height; i++) {
            	rowBounds[i] = new VertSpec(
            			insets.top + ( totalCellH * i ),
            			insets.top + ( totalCellH * i ) + cellH);
            }

            for ( int i = 0; i < ncomponents; i++ ) {
            	int x, y, w, h;
                Component c = parent.getComponent(i);
                CalcCompCellConstraints cell = compTable.get(c);
                if ( cell != null ) {
                	try {
                        x = colBounds[cell.col].xl;
                        y = rowBounds[cell.row].yt;
                        w = colBounds[cell.getMaxCol()].xr - x;
                        h = rowBounds[cell.getMaxRow()].yb - y;
                        c.setBounds(x, y, w, h);
                	} catch (ArrayIndexOutOfBoundsException e) {
                		e.printStackTrace();
                		System.err.println(e);
                		System.err.println("comp: " + c.getName() + " / ...");
                		System.err.println("cell: " + cell);
                		// FIXME: is it a good idea to have exit() in that place?
                		System.exit(1);
                	}
                }
            }
        }
    }

    /**
     * Build a hashtable cellname -&gt; Constraints.
     * @param parent
     */
    protected void processFitBuildConHash(Container parent) {
    	conHash = new Hashtable <String,CalcCompCellConstraints>();
        int ncomponents = parent.getComponentCount();
        if (ncomponents == 0) {
            return;
        }
        for ( int i = 0; i < ncomponents; i++ ) {
            Component c = parent.getComponent(i);
            CalcCompCellConstraints cell = compTable.get(c);
            if ( cell != null ) {
            	conHash.put(cell.getName(), cell);
            }
        }
    }

    /**
     * Fit the cells.
     * @param parent
     */
    protected void processFit(Container parent) {
        int ncomponents = parent.getComponentCount();
        if (ncomponents == 0) {
            return;
        }

        for ( int i = 0; i < ncomponents; i++ ) {
            Component c = parent.getComponent(i);
            CalcCompCellConstraints cell = compTable.get(c);
            if ( cell != null ) {
           		processFitCell(cell);
            }
        }
    }

    /**
     * Stretch the cells.
     * @param parent
     */
    protected void processStretch(Container parent) {
        int ncomponents = parent.getComponentCount();
        if (ncomponents == 0) {
            return;
        }

        for ( int i = 0; i < ncomponents; i++ ) {
            Component c = parent.getComponent(i);
            CalcCompCellConstraints cell = compTable.get(c);
            if ( cell != null ) {
           		processStretchCell(cell);
            }
        }
    }

    /**
     * Reset the stretch data for each cell.
     * @param parent
     */
    protected void resetStretch(Container parent) {
        int ncomponents = parent.getComponentCount();
        if (ncomponents == 0) {
            return;
        }

        for ( int i = 0; i < ncomponents; i++ ) {
            Component c = parent.getComponent(i);
            CalcCompCellConstraints cell = compTable.get(c);
            if ( cell != null ) {
           		cell.updateSpans();
            }
        }
    }

    /**
     * Do a fit for the specified cell.
     * @param cell
     */
    protected void processFitCell(CalcCompCellConstraints cell) {
    	if (cell.isFixed()) return;
    	if (cell.isFitted()) return;
    	if (cell.isFitting()) {
    		throw new Error(this.getClass() + ": constraints are cyclic (found at cell " + cell.getName() + ")");
    	}
    	if (conHash==null) {
    		throw new Error(this.getClass() + ".processFitCell: conHash has not been built");
    	}
    	cell.setFitting(true);

    	boolean DBG = false;

    	if (DBG || DBGfitting) System.err.println("->fitting cell " + cell.name);
   
    	// FIXME: HACK 
		cell.setMajorGrid(false);
//    	if (cell.isMajorGrid()) {
//    		cell.setMajorGrid(false);
//    		cell.setCol(cell.getCol()*majorGridSize.width);
//    		cell.setRow(cell.getRow()*majorGridSize.height);
//    		int colspan;
//    		int rowspan;
//    		colspan = cell.getColspan()*majorGridSize.width;
//    		colspan -= cell.getInnerPan();
//    		rowspan = cell.getRowspan()*majorGridSize.height;
//    		rowspan -= cell.getInnerPan();
//    		cell.setColspan(colspan);
//    		cell.setRowspan(rowspan);
//    	}

    	if (cell.hasNorthLink()) {
//    		System.out.println("cell " + cell.getName() + " has north link");
    		CalcCompCellConstraints refCell = conHash.get(cell.getLinkNorth());
    		if (refCell==null) {
    			// error?
    		} else {
    			// now we may go into a recursive call:
    			if (!refCell.isFitted()) processFitCell(refCell);
    			cell.setRow(refCell.getMaxRow()+1+cell.getOffsetNorth()+cell.getInnerPan());
    		}
    	} else {
    		if (!cell.hasSouthLink()) {
    			// upper maximum
    			cell.setRow(0+cell.getOuterPan());
    		}
    	}

    	if (cell.hasWestLink()) {
    		CalcCompCellConstraints refCell = conHash.get(cell.getLinkWest());
    		if (refCell==null) {
    			// error?
    		} else {
    			// now we may go into a recursive call:
    			if (!refCell.isFitted()) processFitCell(refCell);
    			cell.setCol(refCell.getMaxCol()+1+cell.getOffsetWest()+cell.getInnerPan());
    		}
    	} else {
    		if (!cell.hasEastLink()) {
    			// upper maximum
    			cell.setCol(0+cell.getOuterPan());
    		}
    	}

    	if (cell.hasSouthLink()) {
    		CalcCompCellConstraints refCell = conHash.get(cell.getLinkSouth());
    		if (refCell==null) {
    			// error?
    		} else {
    			// now we may go into a recursive call:
    			if (!refCell.isFitted()) processFitCell(refCell);
    			cell.setRow(
    					refCell.getRow()-
    					cell.getInnerPan()-cell.getOffsetSouth()-cell.getRowspan());
    		}
    	}

    	if (cell.hasEastLink()) {
    		CalcCompCellConstraints refCell = conHash.get(cell.getLinkEast());
    		if (refCell==null) {
    			// error?
    		} else {
    			// now we may go into a recursive call:
    			if (!refCell.isFitted()) processFitCell(refCell);
    			cell.setCol(
    					refCell.getCol()-
    					cell.getInnerPan()-cell.getOffsetEast()-cell.getColspan());
    		}
    	}

    	if (DBG || DBGfitting) System.err.println("<-don fit cell " + cell.name + " :(c[s]/r[s]) " +
    			cell.getCol() + "[" + cell.getColspan() + "]/" +
    			cell.getRow() + "[" + cell.getRowspan() + "]");
    	cell.setFitted(true);
    	cell.setFitting(false);
    }

    /**
     * Process the stretch for the specified cell.
     * @param cell
     */
    protected void processStretchCell(CalcCompCellConstraints cell) {
    	// FIXME: check logic!
    	//if (!cell.isFixed()) return;
    	if (!cell.isFitted()) return;
    	if (!cell.isStretching()) return;
    	if (conHash==null) {
    		throw new Error(this.getClass() + ".processFitCell: conHash has not been built");
    	}

    	if (cell.isStretchColspan()) {
    		int colw = cell.col;
    		int cole = gridSize.width - 1 - shrinkPan;
    		if (cell.hasWestLink()) {
    			colw = conHash.get(cell.getLinkWest()).getMaxCol()+1;
    			colw += cell.getInnerPan();
    		}
    		if (cell.hasEastLink()) {
    			cole = conHash.get(cell.getLinkEast()).getCol()-1;
    			cole -= cell.getInnerPan();
    		}
    		int span = cole - colw + 1;
    		if (span<1) span = 1;
    		cell.setColspan(span);
    	}
    	if (cell.isStretchRowspan()) {
    		int rown = cell.row;
    		int rows = gridSize.height - 1 - shrinkPan;
    		if (cell.hasNorthLink()) {
    			rown = conHash.get(cell.getLinkNorth()).getMaxRow()+1;
    			rown += cell.getInnerPan();
    		}
    		if (cell.hasSouthLink()) {
    			rows = conHash.get(cell.getLinkSouth()).getRow()-1;
    			rows -= cell.getInnerPan();
    		}
    		int span = rows - rown + 1;
    		if (span<1) span = 1;
    		cell.setRowspan(span);
    	}
    }

    // LayoutManager2 /////////////////////////////////////////////////////////

    /**
     * Adds the specified component to the layout, using the specified
     * constraint object.
     * @param comp the component to be added
     * @param constraints  where/how the component is added to the layout.
     */
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof CalcCompCellConstraints) {
            CalcCompCellConstraints cell = (CalcCompCellConstraints)constraints;
            if ( cell.colspan <= 0 || cell.rowspan <= 0 ) {
                throw new IllegalArgumentException(
                    "cannot add to layout: cell must have positive colspan and rowspan");
            }
            if ( cell.col < 0 || cell.row < 0 ) {
                throw new IllegalArgumentException(
                    "cannot add to layout: cell col and row must be >= 0");
            }
            setConstraints(comp, cell);
        } else if (constraints != null) {
            throw new IllegalArgumentException(
                "cannot add to layout: constraint must be a CellConstraint");
        }
    }

    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     * @param target 
     * @return Returns the alignment in X direction.
     */
    public float getLayoutAlignmentX(Container target) {
        return alignmentX;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     * @param target 
     * @return Returns the alignment in Y direction.
     */
    public float getLayoutAlignmentY(Container target) {
        return alignmentY;
    }

    /**
     * Invalidates the layout, indicating that if the layout manager
     * has cached information it should be discarded.
     * @param target 
     */
    public void invalidateLayout(Container target) {
    	boolean DBG = false;
    	if (DBG || DBGforce) System.err.println("invalidateLayout: " + target);
        int ncomponents = target.getComponentCount();
        if (ncomponents == 0) {
            return;
        }

        for ( int i = 0; i < ncomponents; i++ ) {
            Component c = target.getComponent(i);
            CalcCompCellConstraints cell = compTable.get(c);
            if ( cell != null ) {
            	if (DBG || DBGreset) System.err.println("  reset cell " + cell.name);
           		cell.setFitted(false);
           		cell.setFitting(false);
           		// reset stretch:
           		cell.updateSpans();
            }
        }
    }

	/**
	 * @return Returns the alignmentX.
	 */
	public float getAlignmentX() {
		return alignmentX;
	}

	/**
	 * @param alignmentX The alignmentX to set.
	 */
	public void setAlignmentX(float alignmentX) {
		this.alignmentX = alignmentX;
	}

	/**
	 * @return Returns the alignmentY.
	 */
	public float getAlignmentY() {
		return alignmentY;
	}

	/**
	 * @param alignmentY The alignmentY to set.
	 */
	public void setAlignmentY(float alignmentY) {
		this.alignmentY = alignmentY;
	}

	/**
	 * @return Returns the shrinkWrap.
	 */
	public boolean isShrinkWrap() {
		return shrinkWrap;
	}

	/**
	 * @param shrinkWrap The shrinkWrap to set.
	 */
	public void setShrinkWrap(boolean shrinkWrap) {
		this.shrinkWrap = shrinkWrap;
	}

	/**
	 * @return Returns the majorGridSize.
	 */
	public Dimension getMajorGridSize() {
		return majorGridSize;
	}

	/**
	 * @param majorGridSize The majorGridSize to set.
	 */
	public void setMajorGridSize(Dimension majorGridSize) {
		this.majorGridSize = majorGridSize;
	}

	/**
	 * @return Returns the shrinkPan.
	 */
	public int getShrinkPan() {
		return shrinkPan;
	}

	/**
	 * @param shrinkPan The shrinkPan to set.
	 */
	public void setShrinkPan(int shrinkPan) {
		this.shrinkPan = shrinkPan;
	}

	/**
	 * @return Returns the minCellSize.
	 */
	public Dimension getMinCellSize() {
		return minCellSize;
	}

	/**
	 * @param minCellSize The minCellSize to set.
	 */
	public void setMinCellSize(Dimension minCellSize) {
		this.minCellSize = minCellSize;
	}

	/**
	 * @return Returns the fixedCellSize.
	 */
	public boolean isFixedCellSize() {
		return fixedCellSize;
	}

	/**
	 * @param fixedCellSize The fixedCellSize to set.
	 */
	public void setFixedCellSize(boolean fixedCellSize) {
		this.fixedCellSize = fixedCellSize;
	}

    /**
	 * @return Returns the forceFixedCellSize.
	 */
	public boolean isForceFixedCellSize() {
		return forceFixedCellSize;
	}

	/**
	 * @param forceFixedCellSize The forceFixedCellSize to set.
	 */
	public void setForceFixedCellSize(boolean forceFixedCellSize) {
		this.forceFixedCellSize = forceFixedCellSize;
	}
}
