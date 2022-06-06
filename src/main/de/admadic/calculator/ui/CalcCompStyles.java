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
package de.admadic.calculator.ui;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcCompStyles {

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static class Style {
		final String name;
		int width; // <- cell
		int height; // <- cell
		int ipan;
		int opan;

		/**
		 * @param name
		 */
		public Style(String name) {
			this(name, 1, 1, 0, 0);
		}
		/**
		 * @param name
		 * @param width
		 * @param height
		 * @param ipan 
		 * @param opan 
		 */
		public Style(String name, int width, int height, int ipan, int opan) {
			super();
			this.name = name;
			this.width = width;
			this.height = height;
			this.ipan = ipan;
			this.opan = opan;
		}
	}
	
	Hashtable <String, Style> styles;
	Hashtable <String,ArrayList<CalcCompCellConstraints>> cellsForStyles;

	/**
	 * 
	 */
	public CalcCompStyles() {
		styles = new Hashtable <String, Style>();
		cellsForStyles = new Hashtable <String,ArrayList<CalcCompCellConstraints>>();
	}

	/**
	 * @param name
	 * @param w
	 * @param h
	 * @param ipan
	 * @param opan
	 */
	public void addStyle(String name, int w, int h, int ipan, int opan) {
		styles.put(name, new Style(name, w, h, ipan, opan));
		cellsForStyles.put(name, new ArrayList<CalcCompCellConstraints>());
	}

	/**
	 * @param name
	 * @return	Returns the Style for the given name.
	 */
	public Style getStyle(String name) {
		return styles.get(name);
	}

	/**
	 * @param styleName
	 * @param cell
	 */
	public void addCellForStyle(String styleName, CalcCompCellConstraints cell) {
		boolean DBG = false;
		if (DBG) System.out.println("  adding cell " + cell.name + " to style " + styleName);
		cellsForStyles.get(styleName).add(cell);
	}

	/**
	 * @param styleName
	 * @param cells
	 */
	public void addCellsForStyle(String styleName, CalcCompCellConstraints [] cells) {
		boolean DBG = false;
		for (CalcCompCellConstraints cell : cells) {
			if (DBG) System.out.println("  adding cell " + cell.name + " to style " + styleName);
			cellsForStyles.get(styleName).add(cell);
		}
	}

	/**
	 * 
	 */
	public void updateCellsForAllStyles() {
		boolean DBG = false;
		for (Style style : styles.values()) {
			if (DBG) System.err.println("updating for style: " + style.name);
			updateCellsForStyle(style.name);
		}
	}

	/**
	 * @param styleName
	 */
	public void updateCellsForStyle(String styleName) {
		boolean DBG = false;
		ArrayList<CalcCompCellConstraints> cells;
		Style style;
		cells = cellsForStyles.get(styleName);
		style = styles.get(styleName);

		for (CalcCompCellConstraints cell : cells) {
			if (cell==null) continue;
			if (DBG) System.err.println("  updating cell: " + cell.name);
			updateCellForStyle(style, cell);
		}
	}

	/**
	 * @param style
	 * @param cell
	 */
	public void updateCellForStyle(Style style, CalcCompCellConstraints cell) {
		cell.majorGridWidth = style.width;
		cell.majorGridHeight = style.height;
		cell.innerPan = style.ipan;
		cell.outerPan = style.opan;
		cell.updateSpans();
	}
}
