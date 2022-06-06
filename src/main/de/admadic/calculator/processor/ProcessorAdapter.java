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

/**
 * @author Rainer Schwarze
 *
 */
public abstract class ProcessorAdapter implements ProcessorEventListener {

	/**
	 * 
	 */
	public ProcessorAdapter() {
		super();
	}

	/**
	 * @param e
	 * @see de.admadic.calculator.processor.ProcessorEventListener#passedDisplayEvent(de.admadic.calculator.processor.ProcessorEvent)
	 */
	public void passedDisplayEvent(ProcessorEvent e) { /* none */ }

	/**
	 * @param e
	 * @see de.admadic.calculator.processor.ProcessorEventListener#passedProtocolEvent(de.admadic.calculator.processor.ProcessorEvent)
	 */
	public void passedProtocolEvent(ProcessorEvent e) { /* none */ }

	/**
	 * @param e
	 * @see de.admadic.calculator.processor.ProcessorEventListener#passedStatusEvent(de.admadic.calculator.processor.ProcessorEvent)
	 */
	public void passedStatusEvent(ProcessorEvent e) { /* none */ }

}
