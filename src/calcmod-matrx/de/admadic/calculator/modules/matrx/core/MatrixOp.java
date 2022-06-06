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

import java.util.Collections;

/**
 * @author Rainer Schwarze
 *
 */
public class MatrixOp {

	/**
	 * 
	 */
	public MatrixOp() {
		super();
	}

	/**
	 * @param mtx 
	 * @param row1
	 * @param row2
	 */
	public static void swapRows(DMatrix mtx, int row1, int row2) {
		Collections.swap(mtx.data, row1, row2);
	}

	/**
	 * @param mtx 
	 * @param col1
	 * @param col2
	 */
	public static void swapColumns(DMatrix mtx, int col1, int col2) {
		for (DVector dv : mtx.data) {
			Collections.swap(dv.data, col1, col2);
		}
	}

	/**
	 * @param mtx
	 * @param rowSrc
	 * @param rowDst
	 */
	public static void addRowToRow(DMatrix mtx, int rowSrc, int rowDst) {
		DVector vsrc = mtx.data.get(rowSrc);
		DVector vdst = mtx.data.get(rowDst);
		VectorOp.add(vdst, vsrc);
	}

	/**
	 * @param mtx
	 * @param row
	 * @param offset 
	 */
	public static void addOffsetToRow(DMatrix mtx, int row, double offset) {
		DVector v = mtx.data.get(row);
		VectorOp.add(v, offset);
	}

	/**
	 * @param mtx
	 * @param row
	 * @param factor
	 */
	public static void mulRowByFactor(DMatrix mtx, int row, double factor) {
		DVector v = mtx.data.get(row);
		VectorOp.mul(v, factor);
	}

	/**
	 * @param mtx
	 * @throws MatrixLinearDependentException 
	 */
	public static void convertToUpperTriangularForm(DMatrix mtx) 
	throws MatrixLinearDependentException {
		if (mtx.getRowCount()<2) return;
		if (mtx.getColumnCount()<2) return;

		int mincnt = Math.min(mtx.getRowCount(), mtx.getColumnCount());

		// for (int col=0; col<(mtx.getColumnCount()-1); col++) {
		for (int col=0; col<mincnt-1; col++) {
			int fixrow = (mtx.getRowCount() - mincnt) + col;
			Double el = mtx.elementAt(fixrow, col);
			if (el==null || el.doubleValue()==0.0) {
				// the primary element is 0, look for a row to
				// swap with:
				int swaprow = -1;
				for (int row=fixrow+1; row<mtx.getRowCount(); row++) {
					Double el2 = mtx.elementAt(row, col);
					if (el2==null || el2.doubleValue()==0.0) continue;
					swaprow = row;
					break;
				}
				if (swaprow==-1) {
					throw new MatrixLinearDependentException(
							"Matrix is linear dependent");
					// return;
				}
				Collections.swap(mtx.data, fixrow, swaprow);
				// we want to keep the determinant identical.
				// therefore, the sign of one row should be inverted
				// after the swap:
				VectorOp.mul(mtx.getRow(swaprow), -1.0);
			}
			for (int row=fixrow+1; row<mtx.getRowCount(); row++) {
				double factor;
				factor = mtx.elementAt(fixrow, col).doubleValue();
				factor = mtx.elementAt(row, col).doubleValue() / factor;
				factor = -factor;

				VectorOp.add(
						mtx.getRow(row), mtx.getRow(fixrow), factor,
						col, mtx.getColumnCount());
				// the value at (row,col) is 0.0:
				mtx.setElementAt(row, col, Double.valueOf(0.0));
			}
		}
	}

	/**
	 * @param mtx
	 */
	public static void makeMainDiagonalOne(DMatrix mtx) {
		int mincnt = Math.min(mtx.getRowCount(), mtx.getColumnCount());
		for (int row=0; row<mincnt; row++) {
			VectorOp.div(
					mtx.getRow(row), 
					mtx.elementAt(row, row).doubleValue());
		}
	}
	
	/**
	 * @param lumtx
	 * @param vmtx
	 */
	public static void calcSolution(DMatrix lumtx, DMatrix vmtx) {
		if (lumtx.getRowCount()!=vmtx.getRowCount()) return;

		for (int row=lumtx.getRowCount()-1; row>=0; row--) {
			double x = -lumtx.elementAt(row, lumtx.getColumnCount()-1).doubleValue();
			for (int col=lumtx.getColumnCount()-2; col>row; col--) {
				x -= lumtx.elementAt(row, col).doubleValue() * 
						vmtx.elementAt(col, 0).doubleValue();
			}
			x /= lumtx.elementAt(row, row).doubleValue();
			vmtx.setElementAt(row, 0, Double.valueOf(x));
		}
	}

	/**
	 * @param mtx
	 * @param rowIdx	0..n-1 insert before, n=append
	 * @param rowVec 
	 * @return	Returns the new DMatrix
	 */
	public static DMatrix insertRowAt(DMatrix mtx, int rowIdx, DVector rowVec) {
		int newcol = mtx.getColumnCount();
		int newrow = mtx.getRowCount() + 1;
		DMatrix dstMtx = new DMatrix(newrow, newcol);
		for (int r = 0; r<rowIdx; r++) {
			dstMtx.data.set(r, mtx.data.elementAt(r));
		}
		dstMtx.data.set(rowIdx, rowVec);
		for (int r = rowIdx; r<mtx.getRowCount(); r++) {
			dstMtx.data.set(r+1, mtx.data.elementAt(r));
		}
		return dstMtx;
	}

	/**
	 * @param mtx
	 * @param colIdx	0..n-1 insert before, n=append
	 * @param colVec 
	 * @return	Returns the new DMatrix
	 */
	public static DMatrix insertColumnAt(DMatrix mtx, int colIdx, DVector colVec) {
		int newcol = mtx.getColumnCount() + 1;
		int newrow = mtx.getRowCount();
		DMatrix dstMtx = new DMatrix(newrow, newcol);
		for (int r = 0; r<newrow; r++) {
			DVector srcVec = mtx.getRow(r);
			DVector dstVec = dstMtx.getRow(r);
			for (int c = 0; c<colIdx; c++) {
				dstVec.data.set(c, srcVec.data.elementAt(c));
			}
			dstVec.data.set(colIdx, colVec.elementAt(r));
			for (int c = colIdx; c<mtx.getColumnCount(); c++) {
				dstVec.data.set(c+1, srcVec.data.elementAt(c));
			}
		}
		return dstMtx;
	}

	/**
	 * @param mtx
	 * @param par 
	 * @return	Returns the new DMatrix
	 */
	public static DMatrix appendMatrixColumns(DMatrix mtx, DMatrix par) {
		int newcol = mtx.getColumnCount() + par.getColumnCount();
		int newrow = mtx.getRowCount();
		DMatrix dstMtx = new DMatrix(newrow, newcol);
		for (int r = 0; r<newrow; r++) {
			DVector srcVec = mtx.getRow(r);
			DVector dstVec = dstMtx.getRow(r);
			for (int c = 0; c<mtx.getColumnCount(); c++) {
				dstVec.data.set(c, srcVec.data.elementAt(c));
			}
			srcVec = par.getRow(r);
			for (int c = 0; c<par.getColumnCount(); c++) {
				dstVec.data.set(c+mtx.getColumnCount(), srcVec.data.elementAt(c));
			}
		}
		return dstMtx;
	}

	/**
	 * @param mtx
	 * @return	Returns the determinant of the given Matrix.
	 * @throws MatrixDimensionException 
	 * @throws MatrixLinearDependentException 
	 * @throws MatrixDataException 
	 */
	public static Double calcDeterminant(DMatrix mtx) 
	throws MatrixDimensionException, MatrixLinearDependentException, 
			MatrixDataException 
	{
		if (mtx.getRowCount()!=mtx.getColumnCount()) {
			throw new MatrixDimensionException(
					"Matrix is not quadratic for calculation of determinant");
		}
		double v = 1.0;
		DMatrix mtxTmp = mtx;
		if (!isUpperTriangularForm(mtx)) {
			mtxTmp = new DMatrix(mtx);
			convertToUpperTriangularForm(mtxTmp);
		}
		for (int i=0; i<mtxTmp.getRowCount(); i++) {
			Double cv = mtxTmp.elementAt(i, i);
			if (cv==null) {
				throw new MatrixDataException(
						"Matrix element must not be undefined for "+
						"calculation of determinant");
				//return null;
			}
			if (cv.doubleValue()==0.0) return Double.valueOf(0.0);
			v *= cv.doubleValue();
		}
		return Double.valueOf(v);
	}

// we may need to calculate the maximum rank for all possible main diagonals!
//	/**
//	 * @param mtx
//	 * @return	Returns the rank of the given Matrix.
//	 */
//	public static Integer calcRank(DMatrix mtx) {
//		int cnt = 0;
//		DMatrix mtxTmp = mtx;
//		if (!isUpperTriangularForm(mtx)) {
//			mtxTmp = new DMatrix(mtx);
//			convertToUpperTriangularForm(mtxTmp);
//		}
//		int mincnt = Math.min(mtxTmp.getRowCount(), mtxTmp.getColumnCount());
//		for (int i=0; i<mincnt; i++) {
//			Double cv = mtxTmp.elementAt(i, i);
//			if (cv==null) continue;
//			if (cv.doubleValue()==0.0) continue;
//			cnt++;
//		}
//		return Integer.valueOf(cnt);
//	}

	/**
	 * @param mtx
	 * @return	Returns true, if it is in UT form.
	 */
	public static boolean isUpperTriangularForm(DMatrix mtx) {
		for (int row=1; row<mtx.getRowCount(); row++) {
			DVector v = mtx.getRow(row);
			for (int col=0; col<row && col<mtx.getColumnCount(); col++) {
				Double tv = v.elementAt(col);
				if (tv==null) continue;
				// FIXME: eps test?
				if (tv.doubleValue()==0.0) continue;
				return false;
			}
		}
		return true;
	}

	/**
	 * @param mtx
	 * @return	Returns the transposed matrix.
	 */
	public static DMatrix createTranspose(DMatrix mtx) {
		int newcol = mtx.getRowCount();
		int newrow = mtx.getColumnCount();
		DMatrix mtxDst = new DMatrix(newrow, newcol);
		for (int row=0; row<newrow; row++) {
			for (int col=0; col<newcol; col++) {
				mtxDst.setElementAt(
						row, col, 
						mtx.elementAt(col, row));
						/* col & row switched because of transpose! */
			}
		}
		return mtxDst;
	}

	/**
	 * @param mtx
	 * @return	Returns the trace of a matrix.
	 */
	public static Double calcTrace(DMatrix mtx) {
		double v = 0.0;
		int mincnt = Math.min(mtx.getRowCount(), mtx.getColumnCount());
		for (int i=0; i<mincnt; i++) {
			Double cv = mtx.elementAt(i, i);
			if (cv==null) continue;
			if (cv.doubleValue()==0.0) continue;
			v += cv.doubleValue();
		}
		return Double.valueOf(v);
	}

	/**
	 * @param mtx
	 * @param row
	 * @param column
	 * @return	Returns the cofactor of the matrix for row and column.
	 * @throws MatrixDataException 
	 * @throws MatrixLinearDependentException 
	 * @throws MatrixDimensionException 
	 */
	public static Double calcCofactor(DMatrix mtx, int row, int column) 
	throws MatrixDimensionException, MatrixLinearDependentException, 
	MatrixDataException 
	{
		DMatrix tmp = MatrixOp.stripRowAndColumn(mtx, row, column);
		Double det = calcDeterminant(tmp);
		if (det==null) {
			throw new MatrixLinearDependentException();
			// return null;
		}
//		if (det.doubleValue()==0.0) {
//			System.err.println("warning: calcCofactor: cofactor is 0!");
//			return null;	// FIXME: make error?
//		}
		if (((row + column) & 0x01)!=0) {
			// is odd:
			det = Double.valueOf(-det.doubleValue());
		}
		return det;
	}

	/**
	 * @param mtx
	 * @return	Returns the inverse of mtx.
	 * @throws MatrixDataException 
	 * @throws MatrixLinearDependentException 
	 * @throws MatrixDimensionException 
	 */
	public static DMatrix calcInverse(DMatrix mtx) 
	throws MatrixDimensionException, MatrixLinearDependentException, 
	MatrixDataException 
	{
		Double det = calcDeterminant(mtx);
		if (det==null) return null;
		DMatrix res = calcAdjoint(mtx);
		MatrixOp.mul(res, 1.0/det.doubleValue());
		return res;
	}

	/**
	 * @param mtx
	 * @return	Returns the adjoint of mtx.
	 * @throws MatrixDataException 
	 * @throws MatrixLinearDependentException 
	 * @throws MatrixDimensionException 
	 */
	public static DMatrix calcAdjoint(DMatrix mtx) 
	throws MatrixDimensionException, MatrixLinearDependentException, 
	MatrixDataException 
	{
		DMatrix res = new DMatrix(mtx.getRowCount(), mtx.getColumnCount());
		for (int row=0; row<mtx.getRowCount(); row++) {
			for (int col=0; col<mtx.getColumnCount(); col++) {
				Double v = calcCofactor(mtx, row, col);
				if (v==null) return null;
				res.setElementAt(row, col, v);
			}
		}
		res = createTranspose(res);
		return res;
	}
	
	/**
	 * @param mtx
	 * @param row
	 * @param column
	 * @return	Returns a matrix where row and column are removed.
	 * @throws MatrixDimensionException 
	 */
	private static DMatrix stripRowAndColumn(DMatrix mtx, int row, int column) 
	throws MatrixDimensionException {
		int newrows = mtx.getRowCount() - 1;
		int newcols = mtx.getColumnCount() - 1;
		if (newrows<1 || newcols<1) {
			throw new MatrixDimensionException(
					"Matrix must have at least 2 rows and columns for "+
					"stripping row and column");
		}
		DMatrix tmp = new DMatrix(newrows, newcols);
		for (int r=0; r < row; r++) {
			DVector sv = mtx.getRow(r);
			for (int c=0; c < column; c++) {
				tmp.setElementAt(r, c, sv.elementAt(c));
			}
			for (int c=column+1; c < mtx.getColumnCount(); c++) {
				tmp.setElementAt(r, c-1, sv.elementAt(c));
			}
		}
		for (int r=row+1; r < mtx.getRowCount(); r++) {
			DVector sv = mtx.getRow(r);
			for (int c=0; c < column; c++) {
				tmp.setElementAt(r-1, c, sv.elementAt(c));
			}
			for (int c=column+1; c < mtx.getColumnCount(); c++) {
				tmp.setElementAt(r-1, c-1, sv.elementAt(c));
			}
		}
		return tmp;
	}

	/**
	 * @param mtx
	 * @param offset
	 */
	public static void add(DMatrix mtx, double offset) {
		for (int row=0; row<mtx.getRowCount(); row++) {
			DVector vec = mtx.getRow(row);
			VectorOp.add(vec, offset);
		}
	}

	/**
	 * @param mtx
	 * @param factor
	 */
	public static void mul(DMatrix mtx, double factor) {
		for (int row=0; row<mtx.getRowCount(); row++) {
			DVector vec = mtx.getRow(row);
			VectorOp.mul(vec, factor);
		}
	}

	/**
	 * @param mtx
	 * @param par
	 * @throws MatrixDimensionException 
	 */
	public static void add(DMatrix mtx, DMatrix par) 
	throws MatrixDimensionException {
		if (mtx.getRowCount()!=par.getRowCount()) {
			throw new MatrixDimensionException(
					"invalid matrix dimensions for add (# of rows must match)");
		}
		if (mtx.getColumnCount()!=par.getColumnCount()) {
			throw new MatrixDimensionException(
					"invalid matrix dimensions for add (# of columns must match)");
		}
		for (int row=0; row<mtx.getRowCount(); row++) {
			DVector vec = mtx.getRow(row);
			DVector vecpar = par.getRow(row);
			VectorOp.add(vec, vecpar);
		}
	}

	/**
	 * @param mtx
	 * @param par
	 * @throws MatrixDimensionException 
	 */
	public static void sub(DMatrix mtx, DMatrix par) 
	throws MatrixDimensionException {
		if (mtx.getRowCount()!=par.getRowCount()) {
			throw new MatrixDimensionException(
				"invalid matrix dimensions for sub (# of rows must match)");
		}
		if (mtx.getColumnCount()!=par.getColumnCount()) {
			throw new MatrixDimensionException(
				"invalid matrix dimensions for sub (# of columns must match)");
		}
		for (int row=0; row<mtx.getRowCount(); row++) {
			DVector vec = mtx.getRow(row);
			DVector vecpar = par.getRow(row);
			VectorOp.sub(vec, vecpar);
		}
	}

	/**
	 * @param mtx
	 * @param par
	 * @return Returns the matrix resulting from the multiplication.
	 * @throws MatrixDimensionException 
	 */
	public static DMatrix mul(DMatrix mtx, DMatrix par) 
	throws MatrixDimensionException {
		if (mtx.getRowCount()!=par.getColumnCount()) {
			throw new MatrixDimensionException(
				"invalid matrix dimensions for mul");
		}
		DMatrix dst = new DMatrix(mtx.getRowCount(), par.getColumnCount());
		for (int row=0; row<dst.getRowCount(); row++) {
			for (int col=0; col<dst.getColumnCount(); col++) {
				double v = 0.0;
				for (int i=0; i<mtx.getColumnCount(); i++) {
					Double vm = mtx.elementAt(row, i);
					Double vp = par.elementAt(i, col);
					if (vm==null || vp==null) continue;
					v += vm.doubleValue()*vp.doubleValue();
				}
				dst.setElementAt(row, col, Double.valueOf(v));
			}
		}
		return dst;
	}

}
