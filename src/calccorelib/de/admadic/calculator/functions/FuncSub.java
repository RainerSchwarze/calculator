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
package de.admadic.calculator.functions;

import de.admadic.calculator.math.CaMath;
import de.admadic.calculator.math.MathException;
import de.admadic.calculator.types.CaNumber;

/**
 * @author Rainer Schwarze
 *
 */
public class FuncSub extends Function {
	public FuncSub(String name) {
		super(name);
		setOrder(2);
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.functions.Function#calculate()
	 */
	@Override
	public CaNumber calculate() throws MathException {
		output = CaMath.sub(inputs[0], inputs[1]);
		return output;
	}

	/* (non-Javadoc)
	 * @see de.admadic.calculator.functions.Function#calculate(java.lang.Number[])
	 */
	@Override
	public CaNumber calculate(CaNumber[] args) throws MathException {
		output = CaMath.sub(args[0], args[1]);
		return output;
	}
}
