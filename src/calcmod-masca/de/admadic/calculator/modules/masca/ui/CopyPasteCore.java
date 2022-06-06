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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

import javax.swing.JTextField;

/**
 * @author Rainer Schwarze
 *
 */
public class CopyPasteCore {
		Clipboard clipboard;

		/**
		 * 
		 */
		public CopyPasteCore() {
			super();
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		}

		/**
		 * @param tf
		 */
		public void doCopyFrom(JTextField tf) {
			StringBuffer sb = new StringBuffer();
			sb.append(tf.getText());
			StringSelection ss = new StringSelection(sb.toString());
			clipboard.setContents(ss, ss);
		}

		/**
		 * @param tf 
		 */
		public void doPasteInto(JTextField tf) {
			try {
				String trS = (String)(clipboard.getContents(this)
						.getTransferData(DataFlavor.stringFlavor));
				if (tf.isEditable()) {
					tf.setText(trS);
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}