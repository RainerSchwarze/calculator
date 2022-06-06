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

/**
 * 
 * @author Rainer Schwarze
 *
 */
public interface PlacementListener {

	/**
	 * Notify the AttachedWindowManager (which usually is the listener)
	 * that the AttachedWindow has moved.
	 * 
	 * @param atw
	 */
	public abstract void notifyMove(AttachedWindow atw);

	/**
	 * Notify the AttachedWindowManager (which usually is the listener)
	 * that the AttachedWindow has resized.
	 * 
	 * @param atw
	 */
	public abstract void notifyResize(AttachedWindow atw);

	/**
	 * Notify the AttachedWindowManager (which usually is the listener)
	 * that the AttachedWindow should be snapped to the standard position.
	 * (Ignore specific sizes and offsets)
	 * 
	 * @param atw
	 */
	public abstract void snap(AttachedWindow atw);

	/**
	 * @param atw
	 * @param motionLock
	 */
	public abstract void setMotionLock(AttachedWindow atw, boolean motionLock);
}