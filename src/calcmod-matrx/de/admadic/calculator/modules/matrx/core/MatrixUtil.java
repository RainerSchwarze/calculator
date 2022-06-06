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
public class MatrixUtil {

	/**
	 * 
	 */
	protected MatrixUtil() {
		super();
	}

	/**
	 * String encoding as "1 2 3; 3 2 1; 3 1 2"
	 * @param string
	 * @return	Returns a matrix parsed from the string.
	 */
	public static DMatrix parseMatrixString(String string) {
		DMatrix matrix;
		String mtxT = string;
		String [] ra = mtxT.split(";");
		Vector<String> rowsVec = new Vector<String>();
		int cols = -1;
		for (String s : ra) {
			s = s.trim();
			if (s.length()<1) continue;
			rowsVec.add(s);
		}
		if (rowsVec.size()<1) {
			return null;
		}
		for (String rowS : rowsVec) {
			int tmpcols = 0;
			String [] ca = rowS.split("\\s");
			for (String s : ca) {
				s = s.trim();
				if (s.length()<1) continue;
				tmpcols++;
			}
			if (cols<0) {
				cols = tmpcols;
			} else {
				if (cols!=tmpcols) {
					return null;
				}
			}
		}

		matrix = new DMatrix(rowsVec.size(), cols);
		for (int row = 0; row<rowsVec.size(); row++) {
			int tmpcol = 0;
			String [] ca = rowsVec.get(row).split("\\s");
			for (String s : ca) {
				s = s.trim();
				if (s.length()<1) continue;
				Double v;
				try {
					v = Double.valueOf(s);
				} catch (NumberFormatException e) {
					matrix = null;
					return null;
				}
				matrix.setElementAt(row, tmpcol, v);
				tmpcol++;
			}
		}
		return matrix;
	}

	/**
	 * @param matrix
	 * @return	Returns a String encoding of the given matrix.
	 */
	public static String makeString(DMatrix matrix) {
		String string = "";
		if (matrix==null) return string;
		for (int row=0; row<matrix.getRowCount(); row++) {
			if (row!=0) string += ";";
			DVector v = matrix.getRow(row);
			for (int col=0; col<matrix.getColumnCount(); col++) {
				if (col!=0) string += "\t";
				string += v.get(col).toString();
			}
		}
		return string;
	}

}
