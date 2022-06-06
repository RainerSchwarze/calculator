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
package de.admadic.calculator.math;

import de.admadic.calculator.types.CaDouble;
import de.admadic.calculator.types.CaNumber;

/**
 * @author Rainer Schwarze
 *
 */
public class DMath {	
	protected static void addsubimp(int which, CaDouble arg0, CaNumber arg1) {
		// FIXME: improve NaN like state mixing
		double tmp, tmp2;
		// there's nothing to change, if arg0 is not normal. (NaN, Inf...)
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg1.isNaN()) { 
			arg0.setState(CaNumber.STATE_NAN); 
			return; 
		}
		if (arg0.isInf()) return;
		if (arg1.isInf()) {
			arg0.setState(arg1.getState());
			return;
		}
		
		tmp = arg1.doubleValue().value;
		// tmp2 is for range testing:
		tmp2 = (which>=0) ? tmp : -tmp;
		// will we leave the valid range?
		// arg0 + arg1 > arg0.upperlimit?
		// arg0 - arg1 < arg0.lowerlimit?
		if (tmp2>=0) {
			if (arg0.value > arg0.upperlimit-tmp2) {
				arg0.setPosInf();
				return;
			}
		} else {
			if (arg0.value < arg0.lowerlimit+tmp2) {
				arg0.setNegInf();
				return;
			}
		}
		// which == +1 -> add else sub 
		if (which>0) {
			arg0.value += tmp;
		} else {
			arg0.value -= tmp;
		}
	}

	protected static void mulimp(CaDouble arg0, CaNumber arg1) {
		// FIXME: improve NaN like state mixing
		double tmp;
		// there's nothing to change, if arg0 is not normal. (NaN, Inf...)
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg1.isNaN()) { 
			arg0.setNaN(); 
			return; 
		}
		if (
				(arg0.isPosInf() && arg1.isPosInf()) ||
				(arg0.isNegInf() && arg1.isNegInf())
			) {
			arg0.setPosInf();
			return;
		}
		if (
				(arg0.isPosInf() && arg1.isNegInf()) ||
				(arg0.isNegInf() && arg1.isPosInf())
			) {
			arg0.setNegInf();
			return;
		}
		if (arg0.isNotNormal() || arg1.isNotNormal()) {
			arg0.setNaN();
			return;
		}

		tmp = arg1.doubleValue().value;
		// tmp2 is for range testing:
		// will we leave the valid range?
		// arg0 * arg1 > upperlimit | lowerlimit?
		/*
		 * +100 * +100 > +1000 ?  =>  +100 > +1000 / +100 ?
		 * +100 * -100 < -1000 ?  =>  +100 < -1000 / -100 ?
		 * -100 * +100 < -1000 ?  =>  -100 < -1000 / +100 ?
		 * -100 * -100 > +1000 ?  =>  -100 > +1000 / -100 ?
		 */
		if ((arg0.value==0) || (tmp==0)) {
			arg0.setZero();
			return;
		}

		// limit checking temporarily disabled:
		// FIXME: limit checking for mul
//		if (
//				(arg0.value>0) && (tmp>0) ||
//				(arg0.value<0) && (tmp<0)
//			) {
//			if (arg0.value > arg0.upperlimit / tmp) {
//				arg0.setPosInf();
//				return;
//			}
//		}
//		if (
//				(arg0.value>0) && (tmp<0) ||
//				(arg0.value<0) && (tmp>0)
//			) {
//			if (arg0.value < arg0.lowerlimit / tmp) {
//				arg0.setNegInf();
//				return;
//			}
//		}
		
		// now do it
		arg0.value *= tmp;
	}

	protected static void divimp(CaDouble arg0, CaNumber arg1) {
		// FIXME: improve NaN like state mixing
		double tmp;
		// there's nothing to change, if arg0 is not normal. (NaN, Inf...)
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg1.isNaN()) { 
			arg0.setNaN(); 
			return; 
		}
		if (arg0.isNotNormal()) {
			arg0.setNaN();
			return;
		}
		if (arg0.value==0.0) {
			arg0.setZero();
			return;
		}
		tmp = arg1.doubleValue().value;
		if (arg0.value>0) {
			if (arg1.isPosInf()) {
				arg0.setPosZero();
				return;
			}
			if (arg1.isNegInf()) {
				arg0.setNegZero();
				return;
			}
			if (arg1.isPosZero()) {
				arg0.setPosInf();
				return;
			}
			if (arg1.isNegZero()) {
				arg0.setNegInf();
				return;
			}
			if (tmp==0.0) {
				arg0.setPosInf();
				return;
			}
		} else { // arg0 < 0
			if (arg1.isPosInf()) {
				arg0.setNegZero();
				return;
			}
			if (arg1.isNegInf()) {
				arg0.setPosZero();
				return;
			}
			if (arg1.isPosZero()) {
				arg0.setNegInf();
				return;
			}
			if (arg1.isNegZero()) {
				arg0.setPosInf();
				return;
			}
			if (tmp==0.0) {
				arg0.setNegInf();
				return;
			}
		}

		// tmp2 is for range testing:
		// will we leave the valid range?
		// arg0 * arg1 > upperlimit | lowerlimit?
		/*
		 * +100 / +0.01 > +1000 ?  =>  +100 > +1000 * +0.01 ?
		 * +100 / -0.01 < -1000 ?  =>  +100 < -1000 * -0.01 ?
		 * -100 / +0.01 < -1000 ?  =>  -100 < -1000 * +0.01 ?
		 * -100 / -0.01 > +1000 ?  =>  -100 > +1000 * -0.01 ?
		 */

		// FIXME: implement range checking:
//		if (
//				(arg0.value>0) && (tmp>0) ||
//				(arg0.value<0) && (tmp<0)
//			) {
//			if (arg0.value > arg0.upperlimit * tmp) {
//				arg0.setPosInf();
//				return;
//			}
//		}
//		if (
//				(arg0.value>0) && (tmp<0) ||
//				(arg0.value<0) && (tmp>0)
//			) {
//			if (arg0.value < arg0.lowerlimit * tmp) {
//				arg0.setNegInf();
//				return;
//			}
//		}
		// now do it
		arg0.value /= tmp;
	}

	protected static void negimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) {
			arg0.setNegInf();
			return;
		}
		if (arg0.isNegInf()) {
			arg0.setPosInf();
			return;
		}
		if (arg0.isPosZero()) {
			arg0.setNegZero();
			return;
		}
		if (arg0.isNegZero()) {
			arg0.setPosZero();
			return;
		}

		if (arg0.value>0.0) {
			if (-arg0.value < arg0.lowerlimit) {
				arg0.setNegInf();
				return;
			}
		}
		if (arg0.value<0.0) {
			if (-arg0.value > arg0.upperlimit) {
				arg0.setPosInf();
				return;
			}
		}
		
		arg0.value = -arg0.value;
	}

	protected static void sqrimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		if (arg0.value<0.0) arg0.value = -arg0.value;
		if (arg0.value>(arg0.upperlimit/arg0.value)) {
			arg0.setPosInf();
			return;
		}

		arg0.value *= arg0.value;
	}

	protected static void sqrtimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		if (arg0.value<0.0) {
			arg0.setNaN();
			return;
		}

		arg0.value = Math.sqrt(arg0.value);
	}

	protected static void lnimp(CaDouble arg0, Double base) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		if (arg0.value<0.0) {
			arg0.setNaN();
			return;
		}

		arg0.value = Math.log(arg0.value);
		if (base!=null) {
			arg0.value /= Math.log(base.doubleValue());
		}
	}

	protected static void expimp(CaDouble arg0, Double base) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		if (arg0.value>0.0) {
			double ulim = Math.log(arg0.upperlimit);
			if (base!=null) {
				ulim /= Math.log(base.doubleValue());
			}
			if (arg0.value>ulim) {
				arg0.setPosInf();
				return;
			}
		}

		if (base!=null) {
			arg0.value *= Math.log(base.doubleValue());
		}
		arg0.value = Math.exp(arg0.value);
	}

	protected static void expximp(CaDouble arg0, CaDouble arg1) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		if (arg1.isNaN()) {
			arg0.setNaN();
			return; // if arg0 is NaN -> nothing
		}
		if (arg0.isPosInf()) {
			arg0.setPosInf();
			return;
		}
		if (arg0.isNegInf()) {
			arg0.setNaN();
			return;
		}
		if (arg0.isPosZero()) {
			arg0.setNaN();
			return;
		}
		if (arg0.isNegZero()) {
			arg0.setNaN();
			return;
		}

		arg0.value = Math.log(arg0.value);
		arg0.value *= arg1.value;
		arg0.value = Math.exp(arg0.value);
	}

	protected static void sqrtximp(CaDouble arg0, CaDouble arg1) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		if (arg1.isNaN()) {
			arg0.setNaN();
			return; // if arg0 is NaN -> nothing
		}
		if (arg0.isPosInf()) {
			arg0.setPosInf();
			return;
		}
		if (arg0.isNegInf()) {
			arg0.setNaN();
			return;
		}
		if (arg0.isPosZero()) {
			arg0.setNaN();
			return;
		}
		if (arg0.isNegZero()) {
			arg0.setNaN();
			return;
		}

		double tmp;
		
		tmp = Math.log(arg1.value);
		tmp /= arg0.value;
		arg0.value = Math.exp(tmp);
	}

	protected static void sinimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = Math.sin(arg0.value);
	}

	protected static void cosimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = Math.cos(arg0.value);
	}

	protected static void tanimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = Math.tan(arg0.value);
	}

	protected static void cotimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = 1.0/Math.tan(arg0.value);
	}

	protected static void arcsinimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = Math.asin(arg0.value);
	}

	protected static void arccosimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = Math.acos(arg0.value);
	}

	protected static void arctanimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = Math.atan(arg0.value);
	}

	protected static void arccotimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		if (arg0.value==0.0) {
			arg0.setNaN();
			return;
		}
		// arccot(y) = arctan(1/y)
		arg0.value = Math.atan(1.0/arg0.value);
	}

	protected static void sinhimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = Math.sinh(arg0.value);
	}

	protected static void coshimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = Math.sinh(arg0.value);
	}

	protected static void tanhimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = Math.tanh(arg0.value);
	}

	protected static void cothimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = 1.0/Math.tanh(arg0.value);
	}

	protected static void arsinhimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = Math.log(arg0.value + Math.sqrt(arg0.value*arg0.value + 1));
	}

	protected static void arcoshimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		if (arg0.value<0) {
			arg0.value = Math.log(arg0.value - Math.sqrt(arg0.value*arg0.value - 1));
		} else {
			arg0.value = Math.log(arg0.value + Math.sqrt(arg0.value*arg0.value - 1));
		}
	}

	protected static void artanhimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = 0.5 * Math.log((1.0 + arg0.value)/(1 - arg0.value));
	}

	protected static void arcothimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = 0.5 * Math.log((arg0.value + 1.0)/(arg0.value - 1.0));
	}


	protected static void secimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = 1.0 / Math.cos(arg0.value);
	}

	protected static void cosecimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		arg0.value = 1.0 / Math.sin(arg0.value);
	}

	
	protected static double gammalnimp(double v) {
		int j;
		double x,y,tmp,ser;
		final double [] cof = {
				76.18009172947146,
				-86.50532032941677,
				24.01409824083091,
				-1.231739572450155,
				0.1208650973866179e-2,
				-0.5395239384953e-5
		};

		y = x = v;
		tmp = x + 5.5;
		tmp -= (x + 0.5)*Math.log(tmp);
		ser = 1.000000000190015;
		for (j=0; j<6; j++) {
			ser += cof[j] / ++y;
		}
		return -tmp + Math.log(2.5066282746310005 * ser / x);	
	}
	
	protected static void gammaimp(CaDouble arg0) {
		// FIXME: improve NaN like state mixing
		if (arg0.isNaN()) return; // if arg0 is NaN -> nothing
		if (arg0.isPosInf()) return;
		if (arg0.isNegInf()) return;
		if (arg0.isPosZero()) return;
		if (arg0.isNegZero()) return;

		double v = arg0.value;
		if (v==0.0) {
			arg0.setNaN();
			return;
		}

		if (v>0.0) {
			v = Math.exp(gammalnimp(v));
		} else { // substitution case
			v = 1 - v;
			double v1, v2;
			v1 = Math.exp(gammalnimp(v));
			v2 = Math.sin(Math.PI * v);
			v = (Math.PI / v1) / v2;
		}
		if (Double.isNaN(v)) {
			arg0.setNaN();
		} else if (Double.isInfinite(v)) {
			arg0.setNaN();	// FIXME: make a good +Inf/-Inf from that!
		} else {
			arg0.value = v;
		}
	}


	/**
	 * @param arg0
	 * @param arg1
	 */
	public static void add(CaDouble arg0, CaNumber arg1) {
		addsubimp(+1, arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public static void sub(CaDouble arg0, CaNumber arg1) {
		addsubimp(-1, arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public static void mul(CaDouble arg0, CaNumber arg1) {
		mulimp(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public static void div(CaDouble arg0, CaNumber arg1) {
		divimp(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public static void neg(CaDouble arg0) {
		negimp(arg0);
	}

	/**
	 * @param arg0
	 */
	public static void sqr(CaDouble arg0) {
		sqrimp(arg0);
	}

	/**
	 * @param arg0
	 */
	public static void sqrt(CaDouble arg0) {
		sqrtimp(arg0);
	}

	/**
	 * @param arg0
	 */
	public static void ln(CaDouble arg0) {
		lnimp(arg0, null);
	}

	final static Double ln_base10 = Double.valueOf(10.0);
	/**
	 * @param arg0
	 */
	public static void log(CaDouble arg0) {
		lnimp(arg0, ln_base10);
	}

	/**
	 * @param arg0
	 */
	public static void exp(CaDouble arg0) {
		expimp(arg0, null);
	}

	/**
	 * @param arg0
	 */
	public static void exp10(CaDouble arg0) {
		expimp(arg0, ln_base10);
	}

	/**
	 * @param arg0
	 */
	public static void invx(CaDouble arg0) {
		CaDouble tmp = new CaDouble();
		arg0.cloneTo(tmp);
		arg0.setValue(1.0);
		divimp(arg0, tmp);
	}

	/**
	 * @param arg0
	 */
	public static void sin(CaDouble arg0) {
		sinimp(arg0);
	}

	/**
	 * @param arg0
	 */
	public static void cos(CaDouble arg0) {
		cosimp(arg0);
	}

	/**
	 * @param arg0
	 */
	public static void tan(CaDouble arg0) {
		tanimp(arg0);
	}

	/**
	 * @param arg0
	 */
	public static void cot(CaDouble arg0) {
		cotimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void arcsin(CaDouble arg0) {
		arcsinimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void arccos(CaDouble arg0) {
		arccosimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void arctan(CaDouble arg0) {
		arctanimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void arccot(CaDouble arg0) {
		arccotimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void sinh(CaDouble arg0) {
		sinhimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void cosh(CaDouble arg0) {
		coshimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void tanh(CaDouble arg0) {
		tanhimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void coth(CaDouble arg0) {
		cothimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void arsinh(CaDouble arg0) {
		arsinhimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void arcosh(CaDouble arg0) {
		arcoshimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void artanh(CaDouble arg0) {
		artanhimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void arcoth(CaDouble arg0) {
		arcothimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void sec(CaDouble arg0) {
		secimp(arg0);
	}

	/**
	 * @param arg0
	 * @since 1.0.2
	 */
	public static void cosec(CaDouble arg0) {
		cosecimp(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public static void expx(CaDouble arg0, CaDouble arg1) {
		expximp(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public static void sqrtx(CaDouble arg0, CaDouble arg1) {
		sqrtximp(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public static void gamma(CaDouble arg0) {
		gammaimp(arg0);
	}
}
