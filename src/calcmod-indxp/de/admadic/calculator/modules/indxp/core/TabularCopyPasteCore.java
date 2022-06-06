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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * @author Rainer Schwarze
 *
 */
public class TabularCopyPasteCore {
		Clipboard clipboard;

		/**
		 * 
		 */
		public TabularCopyPasteCore() {
			super();
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		}

		/**
		 * @param table
		 */
		public void doCopyFrom(JTable table) {
			StringBuffer sb = new StringBuffer();

			// we need a contiguous block
			int numrows = table.getSelectedRowCount();
			int [] rowssel = table.getSelectedRows();
			// int numcols = table.getSelectedColumnCount();
			// int [] colssel = table.getSelectedColumns();

			int selrowcnt = 0;
			if (rowssel.length>0) {
				if (rowssel.length==1) {
					selrowcnt = 1;
				} else {
					selrowcnt += rowssel[rowssel.length-1] - rowssel[0] + 1;
				}
			}
			if ((numrows==rowssel.length) && (numrows==selrowcnt)) {
				// ok.
			} else {
				JOptionPane.showMessageDialog(
						null,
						"Not a continuous selection.",
						"Invalid selection for Copy",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			int colcount = table.getColumnCount();
			for (int i=0; i<rowssel.length; i++) {
				for (int j=0; j<colcount; j++) {
					sb.append(table.getValueAt(rowssel[i], j));
					if (j!=colcount-1)
						sb.append("\t");
				}
				sb.append("\n");
			}
			
			StringSelection ss = new StringSelection(sb.toString());
			clipboard.setContents(ss, ss);
		}

		/**
		 * @param table
		 * @param addRows
		 */
		public void doPasteInto(JTable table, boolean addRows) {
			GenericObjectCreator goc = new GenericObjectCreator();
			int [] rowssel = table.getSelectedRows();
			int [] colssel = table.getSelectedColumns();

			int startRow = rowssel.length>0 ? rowssel[0] : 0;
			int startCol = colssel.length>0 ? colssel[0] : 0;

			TableModel tm = table.getModel();
			TableModelClipboardReceiver tmcr = null;
			if (tm instanceof TableModelClipboardReceiver) {
				tmcr = (TableModelClipboardReceiver)tm;
			}

			try {
				String trS = (String)(clipboard.getContents(this)
						.getTransferData(DataFlavor.stringFlavor));

				StringTokenizer stlf = new StringTokenizer(trS, "\n");
				for (int i = 0; stlf.hasMoreTokens(); i++) {
					String rowS = stlf.nextToken();
					StringTokenizer sttab = new StringTokenizer(rowS, "\t");
					for (int j = 0; sttab.hasMoreTokens(); j++) {
						String value = sttab.nextToken();
						if (startRow + i >= table.getRowCount()) {
							if (!addRows)
								continue;
						}
						if (startCol + j >= table.getColumnCount()) {
							continue;
						}

						if (!table.isCellEditable(startRow + i, startCol + j)) {
							continue;
						}
						if (tmcr!=null) {
							tmcr.setStringValueAt(
									value, 
									startRow + i, 
									startCol + j);
						} else {
							Object hint;
							Object newval;
							hint = table.getValueAt(startRow + i, startCol + j);
							if (!(hint instanceof String)) {
								newval = goc.valueOf(hint, value);
							} else {
								newval = value;
							}
							if (newval==null) continue;
							table.setValueAt(
									newval, 
									startRow + i, 
									startCol + j);
						}
					}
				}
				
			} catch (Exception e) {
				// e.printStackTrace();
				// FIXME: any message here on error?
			}
		}
	}