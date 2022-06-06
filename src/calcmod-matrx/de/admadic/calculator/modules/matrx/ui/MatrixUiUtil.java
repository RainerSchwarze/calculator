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
package de.admadic.calculator.modules.matrx.ui;

import de.admadic.calculator.modules.matrx.MatrxCfg;
import de.admadic.calculator.modules.matrx.core.DMatrix;
import de.admadic.ui.util.Dialog;

/**
 * @author Rainer Schwarze
 *
 */
public class MatrixUiUtil {

	/**
	 * 
	 */
	protected MatrixUiUtil() {
		super();
	}

	/**
	 * @return	Returns an empty matrix with the dimensions specified
	 * by the user
	 */
	public static DMatrix getNewMatrix() {
		NumberInputDialog nidlg;
		NumberInputSet [] inputSet = new NumberInputSet[]{
				new NumberInputSet(
						"Rows:", "(1..10)",
						3, 1, 10, 1),
				new NumberInputSet(
						"Columns:", "(1..10)",
						3, 1, 10, 1)
		};
		nidlg = new NumberInputDialog(
				"Please enter the number of rows and columns for the matrix.",
				inputSet);
		nidlg.setVisible(true);
		int rc = nidlg.getResultCode();
		if (rc!=Dialog.RESULT_OK) return null;

		int rows = inputSet[0].getValue().intValue();
		int columns = inputSet[1].getValue().intValue();

		return new DMatrix(rows, columns);
	}

	/**
	 * @param cfg 
	 * @return	Returns the matrix 
	 */
	public static DMatrix getNewMatrixInput(MatrxCfg cfg) {
		String data = cfg.getMtxEditContent();
		MatrixInputDialog mid = new MatrixInputDialog(null, data);
		mid.setVisible(true);
		if (mid.getResultCode()!=Dialog.RESULT_OK) return null;
		cfg.putMtxEditContent(mid.getInputData());
		return mid.getMatrix();
	}
}
