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
package de.admadic.calculator.modules.matrx.core;

import java.util.Vector;

/**
 * @author Rainer Schwarze
 *
 */
public class DMatrix {
	Vector<DVector> data;
	int rows;
	int columns;

	/**
	 * @param rows 
	 * @param columns 
	 * 
	 */
	public DMatrix(int rows, int columns) {
		super();
		this.rows = rows;
		this.columns = columns;
		data = new Vector<DVector>(rows);
		for (int i = 0; i < rows; i++) {
			DVector v = new DVector(columns, 0.0);
			data.add(v);
		}
	}

	/**
	 * @param dt
	 */
	public DMatrix(Double[][] dt) {
		super();
		this.rows = dt.length;
		this.columns = dt[0].length;
		data = new Vector<DVector>(rows);
		for (int i = 0; i < dt.length; i++) {
			DVector v = new DVector(dt[i]);
			data.add(v);
		}
	}

	/**
	 * @param mtx
	 */
	public DMatrix(DMatrix mtx) {
		super();
		this.rows = mtx.getRowCount();
		this.columns = mtx.getColumnCount();
		data = new Vector<DVector>(rows);
		for (int i = 0; i < this.rows; i++) {
			DVector v = new DVector(this.columns, 0.0);
			data.add(v);
		}
		for (int r=0; r<rows; r++) {
			for (int c=0; c<columns; c++) {
				this.setElementAt(r, c, mtx.elementAt(r, c));
			}
		}
	}

	/**
	 * @param row
	 * @param column
	 * @return	Returns the element at the specified location.
	 */
	public Double elementAt(int row, int column) {
		if (row<0 || row>=rows) throw new ArrayIndexOutOfBoundsException();
		if (column<0 || column>=columns) throw new ArrayIndexOutOfBoundsException();
		return data.get(row).get(column);
	}

	/**
	 * @param row
	 * @param column
	 * @param v
	 */
	public void setElementAt(int row, int column, Double v) {
		if (row<0 || row>=rows) throw new ArrayIndexOutOfBoundsException();
		if (column<0 || column>=columns) throw new ArrayIndexOutOfBoundsException();
		data.get(row).setElementAt(v, column);
	}

	/**
	 * @return	Returns the number of rows.
	 */
	public int getRowCount() {
		return rows;
	}

	/**
	 * @return	Returns the number of columns.
	 */
	public int getColumnCount() {
		return columns;
	}

	/**
	 * @param row
	 * @return	Returns the specified row.
	 */
	public DVector getRow(int row) {
		return data.get(row);
	}
}
