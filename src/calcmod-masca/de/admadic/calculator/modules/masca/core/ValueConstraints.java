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
package de.admadic.calculator.modules.masca.core;

import java.util.Vector;

/**
 * @author Rainer Schwarze
 *
 */
public class ValueConstraints {
	/**
	 * @author Rainer Schwarze
	 */
	public interface Interval {
		/**
		 * @param v
		 * @return	Returns true, if the value is inside the interval.
		 */
		public abstract boolean isInside(double v);
	}
	/**
	 * @author Rainer Schwarze
	 */
	public abstract static class AbstractInterval implements Interval {
		double begin;
		double end;
		/**
		 * @param begin
		 * @param end
		 */
		public AbstractInterval(double begin, double end) {
			super();
			this.begin = begin;
			this.end = end;
		}
	}
	/**
	 * @author Rainer Schwarze
	 */
	public static class IntervalOpenOpen extends AbstractInterval {
		/**
		 * @param begin
		 * @param end
		 */
		public IntervalOpenOpen(double begin, double end) {
			super(begin, end);
		}

		/**
		 * @param v
		 * @return	Returns true, if v is in (begin,end)
		 * @see de.admadic.calculator.modules.masca.core.ValueConstraints.AbstractInterval#isInside(double)
		 */
		public boolean isInside(double v) {
			return ((v>begin) && (v<end));
		}
	}
	/**
	 * @author Rainer Schwarze
	 */
	public static class IntervalClosedClosed extends AbstractInterval {
		/**
		 * @param begin
		 * @param end
		 */
		public IntervalClosedClosed(double begin, double end) {
			super(begin, end);
		}

		/**
		 * @param v
		 * @return	Returns true, if v is in [begin,end]
		 * @see de.admadic.calculator.modules.masca.core.ValueConstraints.AbstractInterval#isInside(double)
		 */
		public boolean isInside(double v) {
			return ((v>=begin) && (v<=end));
		}
	}
	/**
	 * @author Rainer Schwarze
	 */
	public static class IntervalOpenClosed extends AbstractInterval {
		/**
		 * @param begin
		 * @param end
		 */
		public IntervalOpenClosed(double begin, double end) {
			super(begin, end);
		}

		/**
		 * @param v
		 * @return	Returns true, if v is in (begin,end]
		 * @see de.admadic.calculator.modules.masca.core.ValueConstraints.AbstractInterval#isInside(double)
		 */
		public boolean isInside(double v) {
			return ((v>begin) && (v<=end));
		}
	}
	/**
	 * @author Rainer Schwarze
	 */
	public static class IntervalClosedOpen extends AbstractInterval {
		/**
		 * @param begin
		 * @param end
		 */
		public IntervalClosedOpen(double begin, double end) {
			super(begin, end);
		}

		/**
		 * @param v
		 * @return	Returns true, if v is in [begin,end)
		 * @see de.admadic.calculator.modules.masca.core.ValueConstraints.AbstractInterval#isInside(double)
		 */
		public boolean isInside(double v) {
			return ((v>=begin) && (v<end));
		}
	}

	Vector<Interval> allowedIntervals;
	Vector<Interval> prohibitedIntervals;

	/** type id for (a,b) */
	public final static int TYPE_OPEN_OPEN = 0;
	/** type id for (a,b] */
	public final static int TYPE_OPEN_CLOSED = 1;
	/** type id for [a,b) */
	public final static int TYPE_CLOSED_OPEN = 2;
	/** type id for [a,b] */
	public final static int TYPE_CLOSED_CLOSED = 3;

	/**
	 * 
	 */
	public ValueConstraints() {
		super();
	}

	/**
	 * @param begin
	 * @param end
	 * @param type
	 */
	public void addAllowedInterval(double begin, double end, int type) {
		if (allowedIntervals==null) {
			allowedIntervals = new Vector<Interval>();
		}
		addInterval(allowedIntervals, begin, end, type);
	}

	/**
	 * @param begin
	 * @param end
	 * @param type
	 */
	public void addProhibitedInterval(double begin, double end, int type) {
		if (prohibitedIntervals==null) {
			prohibitedIntervals = new Vector<Interval>();
		}
		addInterval(prohibitedIntervals, begin, end, type);
	}

	private void addInterval(Vector<Interval> intervals, double begin, double end, int type) {
		Interval itv;
		switch (type) {
		case TYPE_OPEN_OPEN:
			itv = new IntervalOpenOpen(begin, end);
			break;
		case TYPE_OPEN_CLOSED:
			itv = new IntervalOpenClosed(begin, end);
			break;
		case TYPE_CLOSED_OPEN:
			itv = new IntervalClosedOpen(begin, end);
			break;
		case TYPE_CLOSED_CLOSED:
			itv = new IntervalClosedClosed(begin, end);
			break;
		default:
			throw new Error("invalid id in ValueConstraints.addInterval");
		}
		intervals.add(itv);
	}

	/**
	 * @param v
	 * @return	Returns true, if the value satisfies the constraints.
	 */
	public boolean checkValue(double v) {
		if (allowedIntervals!=null) {
			boolean ok = false;
			// if at least one entry matches, we are ok:
			for (Interval itv : allowedIntervals) {
				if (itv.isInside(v)) {
					ok = true;
					break;
				}
			}
			if (!ok) return false;
		}
		if (prohibitedIntervals!=null) {
			boolean ok = true;
			// if at least one entry matches, we fail:
			for (Interval itv : prohibitedIntervals) {
				if (itv.isInside(v)) {
					ok = false;
					break;
				}
			}
			if (!ok) return false;
		}
		return true;
	}
}
