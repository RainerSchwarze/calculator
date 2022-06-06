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
package de.admadic.calculator.processor;

import de.admadic.calculator.types.CaNumber;

/**
 * @author Rainer Schwarze
 *
 */
public interface IProcessorCalculation {
	/**
	 * @param cmd
	 * @return	Returns the order of the command.
	 * @throws ProcessorUnknownOpException
	 */
	public abstract int getOpOrder(String cmd) 
	throws ProcessorUnknownOpException;
	
	/**
	 * @param subExpr
	 * @param result 
	 * @throws ProcessorUnknownOpException 
	 */
	public abstract void calcSubExpr(String subExpr, CaNumber result) 
	throws ProcessorUnknownOpException;

	/**
	 * @param op
	 * @param arg0
	 * @param arg1
	 * @param result
	 * @throws ProcessorUnknownOpException 
	 */
	public abstract void calcBinaryOp(
			String op, CaNumber arg0, CaNumber arg1, CaNumber result)
	throws ProcessorUnknownOpException;

	/**
	 * @param op
	 * @param arg0
	 * @param result
	 * @throws ProcessorUnknownOpException 
	 */
	public abstract void calcUnaryOp(
			String op, CaNumber arg0, CaNumber result)
	throws ProcessorUnknownOpException;

	/**
	 * @param op
	 * @param result
	 * @throws ProcessorUnknownOpException
	 */
	public abstract void calcConstantOp(
			String op, CaNumber result)
	throws ProcessorUnknownOpException;
}
