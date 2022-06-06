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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

/**
 * @author Rainer Schwarze
 *
 */
public class CmdStyleGrp {
	final static boolean LOG = true;
	Logger logger = Logger.getLogger("de.admadic");

	String name;
	Color textColor;
	Font font;
	boolean hasPanel;
	Color panelColor;

	/**
	 * @param name
	 * @param textColor
	 * @param font
	 */
	public CmdStyleGrp(String name, Color textColor, Font font) {
		super();
		this.name = name;
		this.textColor = textColor;
		this.font = font;
		this.hasPanel = false;
		this.panelColor = null;
	}

	/**
	 * @param name
	 * @param textColor
	 * @param font
	 * @param hasPanel
	 * @param panelColor
	 */
	public CmdStyleGrp(
			String name, 
			Color textColor, Font font, 
			boolean hasPanel, Color panelColor) {
		super();
		this.name = name;
		this.textColor = textColor;
		this.font = font;
		this.hasPanel = hasPanel;
		this.panelColor = panelColor;
	}

	protected Color parseColor(String cstr) {
		if (cstr.length()==0) return null;
		if (cstr.charAt(0)!='#') return null;
		Color c;
		c = Color.decode(cstr);
		return c;
	}

	protected Font parseFont(String fontName, String fontFlag, String fontSize) {
		Font f;
		int style;
		int size;
		String tmp;
		if (fontName.length()==0) return null;
		tmp = fontFlag;
		tmp.toLowerCase();
		if (tmp.equals("bold")) {
			style = Font.BOLD;
		} else if (tmp.equals("plain")) {
			style = Font.PLAIN;
		} else if (tmp.equals("italic")) {
			style = Font.ITALIC;
		} else {
			style = Font.PLAIN;
		}
		size = Integer.parseInt(fontSize);
		if (fontName.startsWith("#")) {
			String fn = fontName.substring(1);
			URL url  = null;
			InputStream is = null;
			Font font = null;
			try {
				url = this.getClass().getClassLoader().getResource(
						"de/admadic/calculator/ui/res/" + fn);
				is = url.openStream();
				font = Font.createFont(Font.TRUETYPE_FONT, is);
			} catch (IOException e) {
				// FIXME: make these exceptions a good error
				// e.printStackTrace();
				if (logger!=null) logger.severe(
						"Could not font resource: " + e.getMessage());
				font = null;
			} catch (FontFormatException e) {
				// e.printStackTrace();
				if (logger!=null) logger.severe(
						"Invalid font format of font resource: " + 
						e.getMessage());
				font = null;
			}
			f = font.deriveFont(style, size);
		} else {
			f = new Font(fontName, style, size);
		}
		return f;
	}

	/**
	 * @param name
	 * @param textColor
	 * @param fontName
	 * @param fontFlag
	 * @param fontSize
	 * @param hasPanel
	 * @param panelColor
	 */
	public CmdStyleGrp(
			String name, 
			String textColor, 
			String fontName, String fontFlag, String fontSize,
			String hasPanel, String panelColor) {
		super();
		this.name = name;
		this.textColor = parseColor(textColor);
		this.font = parseFont(fontName, fontFlag, fontSize);
		this.hasPanel = Boolean.parseBoolean(hasPanel);
		this.panelColor = parseColor(panelColor);
	}

	/**
	 * @param name
	 * @param textColor
	 * @param fontName
	 * @param fontFlag
	 * @param fontSize
	 */
	public CmdStyleGrp(
			String name, 
			String textColor, 
			String fontName, String fontFlag, String fontSize) {
		super();
		this.name = name;
		this.textColor = parseColor(textColor);
		this.font = parseFont(fontName, fontFlag, fontSize);
		this.hasPanel = false;
		this.panelColor = null;
	}

	/**
	 * @param name
	 */
	public CmdStyleGrp(String name) {
		super();
		this.name = name;
		this.textColor = null;
		this.font = null;
	}

	/**
	 * @return Returns the font.
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * @param font The font to set.
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the textColor.
	 */
	public Color getTextColor() {
		return textColor;
	}

	/**
	 * @param textColor The textColor to set.
	 */
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
}
