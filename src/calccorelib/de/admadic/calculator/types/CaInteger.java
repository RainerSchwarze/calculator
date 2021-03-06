/**
 *
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
 */
package de.admadic.calculator.types;

/**
 * @author Rainer Schwarze
 *
 */
public class CaInteger extends CaLong implements Cloneable {
	public CaInteger() {
		super();
		upperlimit = Integer.MAX_VALUE;
		lowerlimit = Integer.MIN_VALUE;
	}
	public CaInteger(int v) {
		super();
		upperlimit = Integer.MAX_VALUE;
		lowerlimit = Integer.MIN_VALUE;
		value = v;
	}
	public CaInteger clone() throws CloneNotSupportedException {
		CaInteger v = (CaInteger)super.clone();
		cloneTo(v);
		return v;
	}
	public void cloneTo(CaInteger v) {
		super.cloneTo(v);
	}
	public void setValue(int v) {
		value = v;
		setStateNormal();
	}
}
