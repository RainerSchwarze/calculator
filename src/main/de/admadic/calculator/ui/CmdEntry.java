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

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

/**
 * @author Rainer Schwarze
 *
 */
public class CmdEntry {
	String display;		// display on button
	String cmd;			// send to processor
	String groupName;	// name of group the button belongs to
	String toolTip;		// tooltip text
	/**
	 * Icon serving as a template.
	 */
	public static final int ICON_TEMPLATE = 0;
	/**
	 * Icon for standard case.
	 */
	public static final int ICON_STANDARD = 1;
	/**
	 * Icon for pressed case.
	 */
	public static final int ICON_PRESSED  = 2;
	/**
	 * Icon for rollover case.
	 */
	public static final int ICON_ROLLOVER = 3;
	/**
	 * Icon for selected case.
	 */
	public static final int ICON_SELECTED = 4;
	/**
	 * Icon for disabled case.
	 */
	public static final int ICON_DISABLED = 5;
	/**
	 * Icon for rollover selected case.
	 */
	public static final int ICON_ROLLOVER_SELECTED = 6;

	// make sure that one reflects the number of items above:
	static final int ICON_ARRAY_SIZE = 7;

	String [] iconNames;	// filenames of icons to load
	Image [] iconImages;	// image files loaded

	enum CmdType {
		/**
		 * Empty command type.
		 */
		NONE,		// unknown
		/**
		 * Special command type - need sophisticated handling on base of the
		 * name of the command entry (cmd).
		 */
		SPECIAL,	// need special handling by name
		/**
		 * Button command type.
		 */
		BUTTON,		// is a button
		/**
		 * Display command type.
		 */
		DISPLAY,	// is a display
		/**
		 * Separator command type (only visual).
		 */
		SEPARATOR,	// is a separator
		/**
		 * Toggle button command type.
		 */
		TOGGLE,		// is a toggle button
	}
	CmdType cmdType;

	boolean adjustIconColor;
	
	/**
	 * @param cmd
	 * @param display
	 */
	public CmdEntry(String cmd, String display) {
		super();
		this.cmd = cmd;
		this.display = display;
		this.groupName = "";
		iconNames = new String[ICON_ARRAY_SIZE];
		iconImages = new Image[ICON_ARRAY_SIZE];
		cmdType = CmdType.NONE;
		this.adjustIconColor = false;
	}

	/**
	 * @param cmd
	 * @param display
	 * @param group
	 */
	public CmdEntry(String cmd, String display, String group) {
		super();
		this.cmd = cmd;
		this.display = display;
		this.groupName = group;
		iconNames = new String[ICON_ARRAY_SIZE];
		iconImages = new Image[ICON_ARRAY_SIZE];
		cmdType = CmdType.NONE;
		this.adjustIconColor = false;
	}

	/**
	 * @param cmd
	 * @param display
	 * @param group
	 * @param toolTip
	 */
	public CmdEntry(
			String cmd, 
			String display, 
			String group,
			String toolTip) {
		super();
		this.cmd = cmd;
		this.display = display;
		this.groupName = group;
		this.toolTip = toolTip;
		iconNames = new String[ICON_ARRAY_SIZE];
		iconImages = new Image[ICON_ARRAY_SIZE];
		cmdType = CmdType.NONE;
		this.adjustIconColor = false;
	}

	/**
	 * @return Returns the cmdType.
	 */
	public CmdType getCmdType() {
		return cmdType;
	}

	/**
	 * @param cmdType The cmdType to set.
	 */
	public void setCmdType(CmdType cmdType) {
		this.cmdType = cmdType;
	}

	/**
	 * @param cmdTN
	 */
	public void setCmdType(String cmdTN) {
		if (cmdTN.equals("btn")) {
			cmdType = CmdType.BUTTON;
		} else if (cmdTN.equals("dsp")) {
			cmdType = CmdType.DISPLAY;
		} else if (cmdTN.equals("spc")) {
			cmdType = CmdType.SPECIAL;
		} else if (cmdTN.equals("sep")) {
			cmdType = CmdType.SEPARATOR;
		} else if (cmdTN.equals("tgl")) {
			cmdType = CmdType.TOGGLE;
		} else {
			// FIXME: make this an error
			System.err.println("CmdEntry: type "+cmdTN+" is not valid");
			cmdType = CmdType.NONE;
		}
	}
	
	/**
	 * @param type
	 * @param name
	 */
	public void setIconName(int type, String name) {
		if ((type<0) || (type>=iconNames.length)) {
			System.err.println("CommandEndtry.setIconName: type out of range");
			return;
		}
		if (name.equals("")) return; // empty
		iconNames[type] = name;
	}

	/**
	 * @param mt Maybe null
	 * 
	 */
	public void loadImages(MediaTracker mt) {
		for (int i=0; i<ICON_ARRAY_SIZE; i++) {
			loadImage(i);
			if (mt!=null && iconImages[i]!=null) {
				mt.addImage(iconImages[i], 0);
			}
		}
	}

	/**
	 * @param type
	 */
	public void loadImage(int type) {
		if ((type<0) || (type>=iconNames.length)) {
			System.err.println("CommandEndtry.loadImage: type out of range");
			return;
		}
		if (iconNames.length!=iconImages.length) {
			System.err.println("CommandEndtry.loadImage: icon arrays not same size");
			return;
		}
		if (iconNames[type]==null) {
			return; // simply nothing to do
		}
		if (iconNames[type].equals("")) {
			return; // simply nothing to do
		}

		java.net.URL url = this.getClass().getClassLoader().getResource(
				"de/admadic/calculator/ui/res/" + iconNames[type]);
		iconImages[type] = Toolkit.getDefaultToolkit().getImage(url);
	}

	/**
	 * @param type
	 * @return	Returns the Image for the specified Icon type
	 */
	public Image getIconImage(int type) {
		if ((type<0) || (type>=iconImages.length)) {
			return null;
		}
		return iconImages[type];
	}

	/**
	 * @return Returns the cmd.
	 */
	public String getCmd() {
		return cmd;
	}
	/**
	 * @param cmd The cmd to set.
	 */
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	/**
	 * @return Returns the display.
	 */
	public String getDisplay() {
		return display;
	}
	/**
	 * @param display The display to set.
	 */
	public void setDisplay(String display) {
		this.display = display;
	}

	/**
	 * @return Returns the groupName.
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName The groupName to set.
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * @return Returns the toolTip.
	 */
	public String getToolTip() {
		return toolTip;
	}
	/**
	 * @param toolTip The toolTip to set.
	 */
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	/**
	 * @return Returns the adjustIconColor.
	 */
	public boolean isAdjustIconColor() {
		return adjustIconColor;
	}

	/**
	 * @param adjustIconColor The adjustIconColor to set.
	 */
	public void setAdjustIconColor(boolean adjustIconColor) {
		this.adjustIconColor = adjustIconColor;
	}
}
