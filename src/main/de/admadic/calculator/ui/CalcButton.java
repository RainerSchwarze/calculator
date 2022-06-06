/**
 *  
 *  Based on <code>GraphPaperLayout</code> written by Micheal Martak/sun
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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIManager;

import de.admadic.ui.util.Colorizer;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Image bgImage = null;
	// FIXME: make the bufferedimage one that is shared, because
	// it is readonly for all buttons which use one.
	BufferedImage bufImg = null;
	int iw;
	int ih;
	boolean regionCheckEnabled = false;

	// FIXME: combine that with CalcToggleButton and extract it.
	CmdEntry cmdE = null;
	CmdStyleGrp cmdG = null;
	boolean useCustomFont = false;
	boolean useCustomGfx = false;

	/**
	 * 
	 */
	public CalcButton() {
		super();
	}

	/**
	 * @param arg0
	 */
	public CalcButton(Action arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public CalcButton(Icon arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CalcButton(String arg0, Icon arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public CalcButton(String arg0) {
		super(arg0);
	}


	/**
	 * @param img
	 * @param resize
	 */
	public void initBgImage(Image img, boolean resize) {
		bgImage = img;
		java.awt.Dimension d = new java.awt.Dimension(
				bgImage.getWidth(null), bgImage.getHeight(null)); 
		if (resize) {
			setPreferredSize(d);
		}
		setMinimumSize(d);
	}

	/**
	 * @param imgname
	 * @param resize
	 */
	public void initBgImage(String imgname, boolean resize) {
		MediaTracker mt = new MediaTracker(this);
		java.net.URL url = this.getClass().getClassLoader().getResource(
				"de/admadic/calculator/ui/res/" + imgname);
		bgImage = Toolkit.getDefaultToolkit().getImage(url);
		mt.addImage(bgImage, 0);
		try {
			mt.waitForAll();
			java.awt.Dimension d = new java.awt.Dimension(
					bgImage.getWidth(null), bgImage.getHeight(null)); 
			if (resize) {
				setPreferredSize(d);
			}
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics arg0) {
		if (bgImage!=null) {
			int w = getWidth();
			int h = getHeight();
			arg0.drawImage(bgImage, 
					(w - bgImage.getWidth(null))/2, 
					(h - bgImage.getHeight(null))/2, 
					null, null);
		}
		Graphics2D g2;
		g2 = (Graphics2D)arg0.create();
		Object o = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    	g2.setRenderingHint(
    			RenderingHints.KEY_ANTIALIASING,
    			RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintComponent(g2);
    	g2.setRenderingHint(
    			RenderingHints.KEY_ANTIALIASING,
    			o);
	}

	/**
	 * @param x
	 * @param y
	 * @return	Returns whether the point is contained in the button shape.
	 * @see javax.swing.JComponent#contains(int, int)
	 */
	@Override
	public boolean contains(int x, int y) {
		if (bufImg==null) {
			return super.contains(x, y);
		} else {
			return isInRegion(x, y);
		}
	}

	/**
	 * Turn on the check for click in the region of the button.
	 */
	public void enableRegionCheck() {
		if (bgImage==null) return;
		bufImg = new BufferedImage(
				bgImage.getWidth(null), bgImage.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
	    Graphics bg = bufImg.getGraphics();
	    bg.drawImage(bgImage, 0, 0, null);
	    bg.dispose();
	    iw = bufImg.getWidth();
	    ih = bufImg.getHeight();
	    regionCheckEnabled = true;
	}

	/**
	 * @param x
	 * @param y
	 * @return	Returns whether the specified pixel is opaque or transparent. 
	 */
	public boolean isInRegion(int x, int y) {
		if (bufImg==null) return true;
		int bw, bh;
		int tx, ty;
		boolean inRegion;
		int pix[] = new int[4];
		bw = getWidth();
		bh = getHeight();
		tx = x - (bw - iw) / 2;
		ty = y - (bh - ih) / 2;
		inRegion = true;
		if (tx<0) inRegion = false;
		if (ty<0) inRegion = false;
		if (tx>=iw) inRegion = false;
		if (ty>=ih) inRegion = false;
		if (inRegion) {
			bufImg.getData().getPixel(tx, ty, pix);
			if (pix[3]==0) { // alpha?
				inRegion = false;
			}
		}
		return inRegion;
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
			this.initBgImage(img, true);
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
	 * @see javax.swing.JButton#updateUI()
	 */
	@Override
	public void updateUI() {
		super.updateUI();
		updateCmdStyle();
	}

	
}
