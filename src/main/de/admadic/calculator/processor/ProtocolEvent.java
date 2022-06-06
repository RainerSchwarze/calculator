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

import java.util.EventObject;

import de.admadic.calculator.types.CaNumber;

/**
 * @author Rainer Schwarze
 *
 */
public class ProtocolEvent extends EventObject {
	/** */
	private static final long serialVersionUID = 1L;

	String operation;
	CaNumber value;
	String subExpression;
	CaNumber lastSubResult;
	CaNumber curResult;

	/**
	 * @param source
	 */
	public ProtocolEvent(Object source) {
		super(source);
	}

	/**
	 * @param source
	 * @param operation
	 * @param subExpression
	 * @param value
	 * @param lastSubResult 
	 * @param curResult 
	 */
	public ProtocolEvent(
			Object source, 
			String operation, String subExpression, CaNumber value,
			CaNumber lastSubResult, CaNumber curResult) {
		super(source);
		this.operation = operation;
		this.subExpression = subExpression;
		this.value = value;
		this.lastSubResult = lastSubResult;
		this.curResult = curResult;
	}

	/**
	 * @return Returns the operation.
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation The operation to set.
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return Returns the subExpression.
	 */
	public String getSubExpression() {
		return subExpression;
	}

	/**
	 * @param subExpression The subExpression to set.
	 */
	public void setSubExpression(String subExpression) {
		this.subExpression = subExpression;
	}

	/**
	 * @return Returns the value.
	 */
	public CaNumber getValue() {
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(CaNumber value) {
		this.value = value;
	}

	/**
	 * @return Returns the lastSubResult - may be null.
	 */
	public CaNumber getLastSubResult() {
		return lastSubResult;
	}

	/**
	 * @param lastSubResult The lastSubResult to set.
	 */
	public void setLastSubResult(CaNumber lastSubResult) {
		this.lastSubResult = lastSubResult;
	}

	/**
	 * @return Returns the curResult.
	 */
	public CaNumber getCurResult() {
		return curResult;
	}

	/**
	 * @param curResult The curResult to set.
	 */
	public void setCurResult(CaNumber curResult) {
		this.curResult = curResult;
	}
}
