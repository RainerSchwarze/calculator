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
package de.admadic.units.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 * @author Rainer Schwarze
 *
 */
public class UnitsUtil {

	/**
	 * No instance.
	 */
	protected UnitsUtil() {
		super();
	}

	/**
	 * 
	 * @param id
	 * @return	Return an inverted id.
	 */
	public static String invertIdDivToMul(String id) {
		// note: using setCharAt caused warnings in ProGuard about not
		// finding StringBuilder.setCharAt(int,char)
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<id.length(); i++) {
			char c = id.charAt(i);
			switch (c) {
			case Constants.CH_FAC_MUL: c = Constants.CH_FAC_DIV; break;
			case Constants.CH_UNT_MUL: c = Constants.CH_UNT_DIV; break;
			case Constants.CH_FAC_DIV: c = Constants.CH_FAC_MUL; break;
			case Constants.CH_UNT_DIV: c = Constants.CH_UNT_MUL; break;
			default:
				// continue;
				break;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * @param symbol
	 * @return	Returns an inverted symbol string.
	 */
	public static String invertSymbolDivToMul(String symbol) {
		// note: using setCharAt caused warnings in ProGuard about not
		// finding StringBuilder.setCharAt(int,char)
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<symbol.length(); i++) {
			char c = symbol.charAt(i);
			switch (c) {
			case Constants.CH_UNTSYM_MUL: c = Constants.CH_UNTSYM_DIV; break;
			case Constants.CH_UNTSYM_DIV: c = Constants.CH_UNTSYM_MUL; break;
			default:
				// continue;
				break;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * Strips the factor ids from an id.
	 * @param input
	 * @return	Returns the stripped id string.
	 */
	public static String stripFactorIds(String input) {
		String output = "";
		String [] sa = input.split(Constants.REGEX_SPLIT_ID);
		for (String s : sa) {
			if (s.equals("")) continue;
			char c = s.charAt(0);
			switch (c) {
			case Constants.CH_UNT_MUL: output += s; break;
			case Constants.CH_UNT_DIV: output += s; break;
			case Constants.CH_FAC_MUL: break;
			case Constants.CH_FAC_DIV: break;
			}
		}
		return output;
	}
	
	/**
	 * Sorts the normalized id sequence
	 * @param input
	 * @return	returns the sorted normalized id sequence.
	 */
	public static String sortNormalizedCache(String input) {
		// sort the entries in mul and div
		// reduce them (m.s:s -> .m)
		Vector<String> mulVec = new Vector<String>();
		Vector<String> divVec = new Vector<String>();
		Vector<String> fmulVec = new Vector<String>();
		Vector<String> fdivVec = new Vector<String>();
		String [] sa = input.split(Constants.REGEX_SPLIT_ID);
		for (String s : sa) {
			if (s.equals("")) continue;
			char c = s.charAt(0);
			switch (c) {
			case Constants.CH_UNT_MUL: mulVec.add(s.substring(1)); break;
			case Constants.CH_UNT_DIV: divVec.add(s.substring(1)); break;
			case Constants.CH_FAC_MUL: fmulVec.add(s.substring(1)); break;
			case Constants.CH_FAC_DIV: fdivVec.add(s.substring(1)); break;
			}
		}
		Collections.sort(mulVec);
		Collections.sort(divVec);
		Collections.sort(fmulVec);
		Collections.sort(fdivVec);

		Vector<String> testVec = new Vector<String>();

		testVec.clear();
		testVec.addAll(mulVec);
		for (String e : testVec) {
			while (mulVec.contains(e) && divVec.contains(e)) {
				mulVec.remove(e);
				divVec.remove(e);
			}
		}
		testVec.clear();
		testVec.addAll(fmulVec);
		for (String e : testVec) {
			while (fmulVec.contains(e) && fdivVec.contains(e)) {
				fmulVec.remove(e);
				fdivVec.remove(e);
			}
		}

		String output = "";
		for (String e : fmulVec) {
			output += Constants.CH_FAC_MUL + e;
		}
		for (String e : mulVec) {
			output += Constants.CH_UNT_MUL + e;
		}
		for (String e : fdivVec) {
			output += Constants.CH_FAC_DIV + e;
		}
		for (String e : divVec) {
			output += Constants.CH_UNT_DIV + e;
		}
		return output;
	}

	/**
	 * 
	 * @param id
	 * @return	Sort the ids, first mul, then div.
	 */
	public static String sortIdMulDiv(String id) {
		String res = "";
		// create array which includes the elements and the tag:
		String [] ida = id.split(Constants.REGEX_SPLIT_ID);
		String m = "";
		String d = "";
		for (String s : ida) {
			if (s.equals("")) continue;
			String v = s.substring(1);
			char c = s.charAt(0);
			switch (c) {
			case Constants.CH_FAC_MUL: m += Constants.CH_FAC_MUL + v; break;
			case Constants.CH_UNT_MUL: m += Constants.CH_UNT_MUL + v; break;
			case Constants.CH_FAC_DIV: d += Constants.CH_FAC_DIV + v; break;
			case Constants.CH_UNT_DIV: d += Constants.CH_UNT_DIV + v; break;
			default:
				throw new Error("invalid element id: [" + c + "] with " + s);
			}
		}
		res = m + d;
		return res;
	}

	/**
	 * @param id
	 * @param symbol
	 * @return	Returns the view optimized symbol string.
	 */
	public static String createSymbolView(String id, String symbol) {
		String res;
		SymbolViewCreator svc = new SymbolViewCreator();
		svc.init(id, symbol);
		svc.run();
		res = svc.getSymbolView();
		return res;
	}

	/**
	 * @author Rainer Schwarze
	 */
	public static class SymbolViewCreator {
		// samples: 
		// - N_m = Nm
		// - m~s = m/s
		// - kg_m~s~s = kg m/s^2 or kg m s^-2
		// - kg~N~m = kg/(N m)
		// bullet op: ? = \u2219
		// middle dot: ? = \u00b7
		// high 0: ? = \u00ba
		// high 1: ? = \u00b9
		// high 2: ? = \u00b2
		// high 3: ? = \u00b3
		// overline: ? = \u203e (??)
		ArrayList<Integer> imulList;
		ArrayList<Integer> idivList;
		ArrayList<String> mulList;
		ArrayList<String> divList;
		String inputSymbol;
		String inputId;
		String outputSymbol;

		/**
		 * 
		 */
		public SymbolViewCreator() {
			super();
			imulList = new ArrayList<Integer>();
			idivList = new ArrayList<Integer>();
			mulList = new ArrayList<String>();
			divList = new ArrayList<String>();
		}

		/**
		 * @param id
		 * @param symbol
		 */
		public void init(String id, String symbol) {
			inputId = id;
			inputSymbol = symbol;
		}

		/**
		 * 
		 */
		public void run() {
			String mulS = "";
			String divS = "";
			disectSymbol();
			if (imulList.size()<1 && idivList.size()<1) {
				outputSymbol = "";
				return;
			}
			if (imulList.size()<1) {
				mulS = "1";
			} else {
				mulS = createSeq(mulList, imulList, true);
			}
			if (idivList.size()<1) {
				// nothing, divS stays empty.
			} else {
				divS = createSeq(divList, idivList, true);	// false for m.s-1
			}
			if (divList.size()>1) {
				divS = "(" + divS + ")";
			}
			outputSymbol = mulS;
			if (divS.length()>0)
				outputSymbol += "/" + divS;
		}
	
		/**
		 * @param list
		 * @param ilist
		 * @param isMul
		 * @return	Returns the mul or div symbol sequence.
		 */
		protected String createSeq(
				ArrayList<String> list, 
				ArrayList<Integer> ilist, 
				boolean isMul) {
			String powercodes = "\u00ba\u00b9\u00b2\u00b3";
			String seq = "";
			String mincharUni = "";
			String mincharAsc = "";
			if (!isMul) {
				mincharUni = "\u203e";
				mincharAsc = "-";
			}
			for (int i = 0; i < list.size(); i++) {
				String sy = list.get(i);
				if (seq.length()>0) seq += "\u00b7";	// middle dot
				seq += sy;
				int powi = ilist.get(i).intValue();
				if (powi>1) {
					if (powi<4) {
						seq += mincharUni + powercodes.charAt(powi);
					} else {
						seq += "^" + mincharAsc + powi;
					}
				}
			}
			return seq;
		}

		protected void disectSymbol() {
			// kg_m~s~s
			String tmp = inputSymbol;
			if (tmp.length()>0 && tmp.charAt(0)!='~') tmp = "_" + tmp;
			String [] sa = tmp.split(Constants.REGEX_SPLIT_SYMBOL);
			for (String s : sa) {
				if (s.equals("")) continue;
				char op = s.charAt(0);
				String s1 = s.substring(1);
				switch (op) {
				case '_':
					updateHash(imulList, mulList, s1);
					break;
				case '~':
					updateHash(idivList, divList, s1);
					break;
				default:
					throw new Error("Invalid symbol id: " + 
							op + " <- " + inputSymbol);
				}
			}
		}

		protected void updateHash(
				ArrayList<Integer> ilist, 
				ArrayList<String> list, 
				String s) {
			if (list.contains(s)) {
				int i = list.indexOf(s);
				Integer itg = ilist.get(i);
				itg = Integer.valueOf(itg.intValue()+1);
				ilist.set(i, itg);
			} else {
				list.add(s);
				ilist.add(Integer.valueOf(1));
			}
		}

		/**
		 * @return	Returns the symbol view.
		 */
		public String getSymbolView() {
			return outputSymbol;
		}
	}
}
