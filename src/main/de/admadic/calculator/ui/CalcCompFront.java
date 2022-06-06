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
import java.util.LinkedList;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcCompFront {
	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static class Constraints {
		String northRef;
		String westRef;
		/**
		 * @param west
		 * @param north
		 */
		public Constraints(String west, String north) {
			super();
			northRef = north;
			westRef = west;
		}
	}
	final static int SPACER = 1; 
	String name;
	LinkedList <CalcCompGroup> ccGroups;
	Hashtable <String,CalcCompGroup> ccGroupsHash;
	Hashtable <String,Constraints> constraints;

	Hashtable<String,CalcCompCellConstraints> cells;
	Dimension gridSize;

	/**
	 * @param name
	 */
	public CalcCompFront(String name) {
		super();
		this.name = name;
		ccGroups = new LinkedList <CalcCompGroup>();
		constraints = new Hashtable <String,Constraints>();
		cells = new Hashtable<String,CalcCompCellConstraints>();
	}

	/**
	 * @return	Returns an iterator of the CalcCompGroup list.
	 */
	public Iterator<CalcCompGroup> getCalcCompGroupsIterator() {
		return ccGroups.iterator();
	}

	/**
	 * @param ccg
	 * @param c
	 */
	public void addCalcCompGroup(CalcCompGroup ccg, Constraints c) {
		if (ccg==null) return;
		if (ccGroups.contains(ccg.getName())) {
			// already there!?
			return;
		}
		if (c==null) return;
		if (constraints.contains(ccg.getName())) {
			// already there!?
			return;
		}
		ccGroups.add(ccg);
		constraints.put(ccg.getName(), c);
	}

	/**
	 * for each group create a cell info and store that into the cells
	 * list.
	 */
	public void doLayout() {
		Iterator <CalcCompGroup> it;
		CalcCompGroup ccg;
		CalcCompCellConstraints cell;
		Dimension ccgSize;
		String curName;
		Constraints con;
		int col;
		int row;
		int maxcol;
		int maxrow;

		it = ccGroups.iterator();
		if (it==null) return;

		// prepare the lookup hash:
		ccGroupsHash = new Hashtable<String,CalcCompGroup>();
		while (it.hasNext()) {
			ccg = it.next();
			ccGroupsHash.put(ccg.getName(), ccg);
		}

		maxcol = 0;
		maxrow = 0;
		it = ccGroups.iterator();
		while (it.hasNext()) {
			int limit, i;
			ccg = it.next();

			col = 0;
			// find minimum x:
			curName = ccg.getName();
			limit = constraints.size();
			i = 0;
			while (true) {
				i++;
				if (i>limit) {
					//cycling!
					throw new Error(
							"The constraints for elements of "+
							"CalcCompFront " + name + " are cyclic");
				}
				con = constraints.get(curName);
				if (con==null) {
					break;
				}
				if (con.westRef.equals("")) {
					break;
				}
				col += ccGroupsHash.get(con.westRef).getGridSize().width + SPACER;
				curName = con.westRef;
			}

			row = 0;
			// find minimum y:
			curName = ccg.getName();
			limit = constraints.size();
			i = 0;
			while (true) {
				i++;
				if (i>limit) {
					//cycling!
					throw new Error(
							"The constraints for elements of "+
							"CalcCompFront " + name + " are cyclic");
				}
				con = constraints.get(curName);
				if (con==null) {
					break;
				}
				if (con.northRef.equals("")) {
					break;
				}
				row += ccGroupsHash.get(con.northRef).getGridSize().height + SPACER;
				curName = con.northRef;
			}

			ccgSize = ccg.getGridSize();
			cell = new CalcCompCellConstraints(
					ccg.getName(), 
					col, row, ccgSize.width, ccgSize.height);
			cells.put(ccg.getName(), cell);
			ccg.translate(col, row);

			if ((cell.getMaxCol())>maxcol) {
				maxcol = cell.getMaxCol();
			}
			if ((cell.getMaxRow())>maxrow) {
				maxrow = cell.getMaxRow();
			}
		} // while loop (find max & find pos of cells)
		gridSize = new Dimension(maxcol+1, maxrow+1);
	} // doLayout
}
