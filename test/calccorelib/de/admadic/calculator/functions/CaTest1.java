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

import de.admadic.calculator.functions.Function;
import de.admadic.calculator.functions.FunctionFactory;
import de.admadic.calculator.math.MathException;
import de.admadic.calculator.types.CaDouble;

/**
 * @author Rainer Schwarze
 *
 */
public class CaTest1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FunctionFactory ff;
		try {
			ff = new FunctionFactory();
			ff.registerFunction("de.admadic.calculator.functions.FuncAdd", "add");
			ff.registerFunction("de.admadic.calculator.functions.FuncSub", "sub");
			ff.registerFunction("de.admadic.calculator.functions.FuncMul", "mul");
			ff.registerFunction("de.admadic.calculator.functions.FuncDiv", "div");
			ff.registerFunction("de.admadic.calculator.functions.FuncNeg", "neg");
			System.out.println("Tester.");
			System.out.println("function factory: create function:");
			Function fa, fs;
			fa = ff.createFunction("add");
			System.out.println("fa: " + fa.getName() + " / " + fa.getClass().getCanonicalName());
			fs = ff.createFunction("sub");
			System.out.println("fs: " + fs.getName() + " / " + fs.getClass().getCanonicalName());
	
			CaDouble d1 = new CaDouble(10.0);
			CaDouble d2 = new CaDouble(20.0);
			CaDouble da[] = {d1, d2};
			CaDouble res;
	
			res = (CaDouble)fa.calculate(da);
			System.out.println("op: " + d1.toString() + "+" + d2.toString() + "=" + res.toString());
			res = (CaDouble)fs.calculate(da);
			System.out.println("op: " + d1.toString() + "-" + d2.toString() + "=" + res.toString());
		} catch (MathException e) {
			e.printStackTrace();
		}
	}

}
