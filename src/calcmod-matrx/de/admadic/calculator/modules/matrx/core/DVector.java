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
public class DVector {
	Vector<Double> data;
	int rows;


	/**
	 * @param rows 
	 * 
	 */
	public DVector(int rows) {
		super();
		this.rows = rows;
		data = new Vector<Double>(rows);
		for (int i = 0; i < rows; i++) {
			data.add(null);
		}
	}

	/**
	 * @param rows
	 * @param stdv
	 */
	public DVector(int rows, double stdv) {
		super();
		this.rows = rows;
		data = new Vector<Double>(rows);
		for (int i = 0; i < rows; i++) {
			data.add(Double.valueOf(stdv));
		}
	}

	/**
	 * @param dt
	 */
	public DVector(Double[] dt) {
		super();
		this.rows = dt.length;
		data = new Vector<Double>(rows);
		for (int i = 0; i < rows; i++) {
			data.add(dt[i]);
		}
	}

	/**
	 * @param row
	 * @return	Returns the element at the specified location.
	 */
	public Double elementAt(int row) {
		if (row<0 || row>=rows) throw new ArrayIndexOutOfBoundsException();
		return data.get(row);
	}

	/**
	 * @param index
	 * @return	Returns the value at the given index.
	 * @see java.util.Vector#get(int)
	 */
	public synchronized Double get(int index) {
		return data.get(index);
	}

	/**
	 * @param obj
	 * @param index
	 * @see java.util.Vector#setElementAt(Object, int)
	 */
	public synchronized void setElementAt(Double obj, int index) {
		data.setElementAt(obj, index);
	}

	/**
	 * @param o
	 * @return	Returns true, if successul
	 * @see java.util.Vector#add(Object)
	 */
	public synchronized boolean add(Double o) {
		return data.add(o);
	}
}
