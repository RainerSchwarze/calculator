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
import java.awt.Image;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import de.admadic.ui.util.Colorizer;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcToggleButton extends JToggleButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CmdEntry cmdE = null;
	CmdStyleGrp cmdG = null;
	boolean useCustomFont = false;
	boolean useCustomGfx = false;
	
	/**
	 * 
	 */
	public CalcToggleButton() {
		super();
	}

	/**
	 * @param arg0
	 */
	public CalcToggleButton(Icon arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CalcToggleButton(Icon arg0, boolean arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public CalcToggleButton(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CalcToggleButton(String arg0, boolean arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public CalcToggleButton(Action arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CalcToggleButton(String arg0, Icon arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public CalcToggleButton(String arg0, Icon arg1, boolean arg2) {
		super(arg0, arg1, arg2);
	}


	/**
	 * @param cmdE
	 * @param cmdG
	 */
	public void setCmdStyle(CmdEntry cmdE, CmdStyleGrp cmdG) {
		this.cmdE = cmdE;
		this.cmdG = cmdG;
		updateCmdStyle();
	}

	private void updateCmdStyle() {
		updateColor();
		if (isUseCustomFont()) updateFont();
		if (isUseCustomGfx()) updateGfx();
	}

	private void updateGfx() {
		if (cmdG==null || cmdE==null) return;

		// update images
		Color c = cmdG.getTextColor();
		Color stdFG = UIManager.getColor("Button.foreground");
		if (stdFG==null) stdFG = UIManager.getColor("controlText");
		Color cc = stdFG; //comp.getForeground();
		Color bc = this.getBackground();
		
		Image img;
		boolean hasAnyImg;
		hasAnyImg = false;
		img = cmdE.getIconImage(CmdEntry.ICON_TEMPLATE);
		if (img!=null) {
//			this.initBgImage(img, true);
		}
		if (img!=null) hasAnyImg = true;
		img = cmdE.getIconImage(CmdEntry.ICON_STANDARD);
		if (img!=null) this.setIcon(
				Colorizer.makeIcon(img, cmdE.isAdjustIconColor(), bc, cc, c));
		if (img!=null) hasAnyImg = true;
		img = cmdE.getIconImage(CmdEntry.ICON_PRESSED);
		if (img!=null) this.setPressedIcon(
				Colorizer.makeIcon(img, cmdE.isAdjustIconColor(), bc, cc, c));
		if (img!=null) hasAnyImg = true;
		img = cmdE.getIconImage(CmdEntry.ICON_ROLLOVER);
		if (img!=null) this.setRolloverIcon(
				Colorizer.makeIcon(img, cmdE.isAdjustIconColor(), bc, cc, c));
		if (img!=null) hasAnyImg = true;
		img = cmdE.getIconImage(CmdEntry.ICON_SELECTED);
		if (img!=null) this.setSelectedIcon(
				Colorizer.makeIcon(img, cmdE.isAdjustIconColor(), bc, cc, c));
		if (img!=null) hasAnyImg = true;
		img = cmdE.getIconImage(CmdEntry.ICON_DISABLED);
		if (img!=null) this.setDisabledIcon(
				Colorizer.makeIcon(img, cmdE.isAdjustIconColor(), bc, cc, c));
		if (img!=null) hasAnyImg = true;
		img = cmdE.getIconImage(CmdEntry.ICON_ROLLOVER_SELECTED);
		if (img!=null) this.setRolloverSelectedIcon(
				Colorizer.makeIcon(img, cmdE.isAdjustIconColor(), bc, cc, null));
		if (img!=null) hasAnyImg = true;
	
		if (hasAnyImg) {
			this.setText(null);
//			abtn.setBorderPainted(false);
//			abtn.setContentAreaFilled(false);
			//btn.setAlignmentY(0.5f);
		}
	}

	private void updateFont() {
		if (cmdG==null) return;

		Font f = cmdG.getFont();
		if (f!=null) this.setFont(f);
	}

	private void updateColor() {
		if (cmdG==null) return;

		Color c = cmdG.getTextColor();
		if (c!=null) {
			Color stdC = UIManager.getColor("Button.foreground");
			if (stdC==null) stdC = UIManager.getColor("controlText");
			if (stdC!=null) {
				c = Colorizer.adjustColor(null, stdC, c);
			}
			if (c!=null) {
				this.setForeground(c);
			}
		}
	}

	/**
	 * @return Returns the useCustomFont.
	 */
	public boolean isUseCustomFont() {
		return useCustomFont;
	}

	/**
	 * @param useCustomFont The useCustomFont to set.
	 */
	public void setUseCustomFont(boolean useCustomFont) {
		this.useCustomFont = useCustomFont;
	}

	/**
	 * @return Returns the useCustomGfx.
	 */
	public boolean isUseCustomGfx() {
		return useCustomGfx;
	}

	/**
	 * @param useCustomGfx The useCustomGfx to set.
	 */
	public void setUseCustomGfx(boolean useCustomGfx) {
		this.useCustomGfx = useCustomGfx;
	}

	/**
	 * 
	 * @see javax.swing.JToggleButton#updateUI()
	 */
	@Override
	public void updateUI() {
		super.updateUI();
		updateCmdStyle();
	}
}
