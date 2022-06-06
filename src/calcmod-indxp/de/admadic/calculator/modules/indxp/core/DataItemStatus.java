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
package de.admadic.calculator.modules.indxp.core;

/**
 * @author Rainer Schwarze
 *
 */
public class DataItemStatus {
	/** Factors data item id */
	public static final int DI_FACTORS = 0;
	/** FactorInteractions data item id */
	public static final int DI_FACTORINTERACTIONS = 1;
	/** Runs data item id */
	public static final int DI_RUNS = 2;
	/** ExpResults data item id */
	public static final int DI_EXPRESULTS = 3;
	// FIXME: dont know whether we need ids for level analysis or not.

	
	/** data not initialized */
	public final static int DATA_UNINITIALIZED = 0;
	/** data structure created */
	public final static int DATA_CREATED = 1;
	/** data structure partially filled with data */
	public final static int DATA_FILLED_PARTIALLY = 2;
	/** data structure completely filled with data */
	public final static int DATA_FILLED = 3;

	/** nothing locked */
	public final static int LOCKED_NONE = 0;
	/** structure locked */
	public final static int LOCKED_STRUCT = 1<<0;
	/** data locked */
	public final static int LOCKED_DATA = 1<<1;
	/** all locked */
	public final static int LOCKED_ALL = LOCKED_STRUCT | LOCKED_DATA;
	

	int dataStatus;
	int lockStatus;
	
	/**
	 * 
	 */
	public DataItemStatus() {
		super();
		setDataStatus(DATA_UNINITIALIZED);
		lockStatus = LOCKED_NONE;
	}

	/**
	 * @param dataStatus
	 */
	public void setDataStatus(int dataStatus) {
		this.dataStatus = dataStatus;
	}

	/**
	 * @return	Returns the data status
	 */
	public int getDataStatus() {
		return this.dataStatus;
	}
	
	/**
	 * @return	Returns true, if the data or structure is locked
	 */
	public boolean isLocked() {
		return (lockStatus!=LOCKED_NONE);
	}

	/**
	 * @return	Returns true, if the data is locked
	 */
	public boolean isDataLocked() {
		return ((lockStatus & LOCKED_DATA)!=0);
	}

	/**
	 * @return	Returns true, if the structure is locked
	 */
	public boolean isStructureLocked() {
		return ((lockStatus & LOCKED_STRUCT)!=0);
	}

	/**
	 * @param flags
	 */
	public void lock(int flags) {
		lockStatus |= flags;
	}

	/**
	 * @param flags
	 */
	public void unlock(int flags) {
		lockStatus &= ~flags;
	}

	/**
	 * 
	 */
	public void reset() {
		setDataStatus(DATA_UNINITIALIZED);
		lockStatus = LOCKED_NONE;
	}
}
