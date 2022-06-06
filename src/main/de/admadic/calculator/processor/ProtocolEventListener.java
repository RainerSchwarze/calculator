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

import java.util.EventListener;

/**
 * @author Rainer Schwarze
 *
 */
public interface ProtocolEventListener extends EventListener {
	/**
	 * Adds an op-line to the protocol, such as "+ 30".
	 * event:op	an op, usually "+", "-", "/", ...
	 * event:value	maybe null for ops like "sin".
	 * @param event 
	 */
	public abstract void addOp(ProtocolEvent event);

	/**
	 * event:op		an op, usually "+", "-", ...
	 * event:subExpr	a subexpr, must not be null.
	 * event:value		a value, can be null.
	 * @param event 
	 */
	public abstract void addSubExprOp(ProtocolEvent event);
	
	/**
	 * Adds a result-line to the protocol, such as "= ..."
	 * event:op	Usually "="
	 * event:value Usually the result value, but can be null.
	 * @param event 
	 */
	public abstract void addResult(ProtocolEvent event);

	/**
	 * Signals that a clear operation has been executed.
	 * @param event 
	 */
	public abstract void addClear(ProtocolEvent event);
}
