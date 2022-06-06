package de.admadic.calculator.processor.func;

import com.graphbuilder.math.func.Function;

/**
 * The square root function.
*/
public class FuncE implements Function {

	/**
	 * 
	 */
	public FuncE() {
		// nothing
	}

	/**
	 * Returns the square of the value at index location 0.
	 * @param d 
	 * @param numParam 
	 * @return	Returns the square 
	 */
	public double of(double[] d, int numParam) {
		return Math.E;
	}

	/**
	 * Returns true only for 0 parameter, false otherwise.
	 * @param numParam 
	 * @return Returns true for 0
	 */
	public boolean acceptNumParam(int numParam) {
		return numParam == 0;
	}

	/**
	 * @return	Returns a string rep.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "e()";
	}
}