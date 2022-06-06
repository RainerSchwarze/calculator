package de.admadic.calculator.processor.func;

import com.graphbuilder.math.func.Function;

import de.admadic.calculator.math.DMath;
import de.admadic.calculator.types.CaDouble;

/**
 * The square root function.
*/
public class FuncExp10 implements Function {

	/**
	 * 
	 */
	public FuncExp10() {
		// nothing
	}

	/**
	 * Returns the square of the value at index location 0.
	 * @param d 
	 * @param numParam 
	 * @return	Returns the square 
	 */
	public double of(double[] d, int numParam) {
		CaDouble dv = new CaDouble(d[0]);
		DMath.exp10(dv);
		return dv.getValue();
	}

	/**
	 * Returns true only for 1 parameter, false otherwise.
	 * @param numParam 
	 * @return Returns true for 1
	 */
	public boolean acceptNumParam(int numParam) {
		return numParam == 1;
	}

	/**
	 * @return	Returns a string rep.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "exp10(x)";
	}
}