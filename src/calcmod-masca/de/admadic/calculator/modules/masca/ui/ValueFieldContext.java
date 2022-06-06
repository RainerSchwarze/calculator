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
package de.admadic.calculator.modules.masca.ui;

import java.util.Vector;

/**
 * @author Rainer Schwarze
 *
 */
public class ValueFieldContext {
	/** field parsing was ok. should not be stored here! */
	public final static int STATUS_OK = 0;
	/** field parsing was bad. */
	public final static int STATUS_BAD = 1;
	/** field parsing was warning level. */
	public final static int STATUS_WARN = 2;

	/**
	 * @author Rainer Schwarze
	 */
	public static class Record {
		ValueField field;
		String message;
		int status;
		/**
		 * @param field
		 * @param message
		 * @param status
		 */
		public Record(ValueField field, String message, int status) {
			super();
			this.field = field;
			this.message = message;
			this.status = status;
		}
		/**
		 * @return Returns the field.
		 */
		public ValueField getField() {
			return field;
		}
		/**
		 * @return Returns the message.
		 */
		public String getMessage() {
			return message;
		}
		/**
		 * @return Returns the status.
		 */
		public int getStatus() {
			return status;
		}
	}
	Vector<Record> records;

	/**
	 * 
	 */
	public ValueFieldContext() {
		super();
		records = new Vector<Record>();
	}

	/**
	 * @param field
	 * @param message
	 * @param status 
	 */
	public void addEntry(ValueField field, String message, int status) {
		records.add(new Record(field, message, status));
	}

	/**
	 * 
	 */
	public void clear() {
		records.clear();
	}

	/**
	 * @return	Returns the number of entries.
	 */
	public int getRecordCount() {
		return records.size();
	}

	/**
	 * @param i
	 * @return	Returns the i'th record.
	 */
	public Record getRecord(int i) {
		return records.get(i);
	}
}
