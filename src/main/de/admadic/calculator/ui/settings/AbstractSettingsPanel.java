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
package de.admadic.calculator.ui.settings;

import javax.swing.JPanel;

/**
 * @author Rainer Schwarze
 *
 */
public class AbstractSettingsPanel extends JPanel implements
		ISettingsPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AbstractSettingsPanel() {
		super();
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#initContents()
	 */
	public void initContents() {
		// nothing
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#loadSettings()
	 */
	public void loadSettings() {
		// nothing
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#storeSettings()
	 */
	public void storeSettings() {
		// nothing
	}

	/**
	 * 
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#resetSettings()
	 */
	public void resetSettings() {
		// nothing
	}

	/**
	 * @return	Returns false as a default behaviour.
	 * @see de.admadic.calculator.ui.settings.ISettingsPanel#isNeedRestart()
	 */
	public boolean isNeedRestart() {
		return false;
	}

}
