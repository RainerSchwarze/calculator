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
package de.admadic.laf;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;
import com.l2fprod.gui.plaf.skin.SkinUtils;

import de.admadic.laf.LaF;
import de.admadic.laf.LaFSkin;

/**
 * @author Rainer Schwarze
 *
 */
public class LaFSkinLF extends LaF {
	private Logger logger = getLogger();
	private String lastSkinName;

	/**
	 * @param name
	 * @param className
	 * @param doesDecoration
	 */
	public LaFSkinLF(String name, String className, boolean doesDecoration) {
		super(name, className, doesDecoration);
		if (logger!=null) logger.fine("LaFSkinLF: accessing SkinUtils...");
		if (logger!=null) logger.fine("LaFSkinLF: class name = " + 
				SkinUtils.class.getName());
	}

	/**
	 * @param name
	 * @param className
	 * @param doesDecoration
	 * @param typei
	 */
	public LaFSkinLF(
			String name, String className, boolean doesDecoration, int typei) {
		super(name, className, doesDecoration, typei);
		if (logger!=null) logger.fine("LaFSkinLF: accessing SkinUtils...");
		if (logger!=null) logger.fine("LaFSkinLF: class name = " + 
				SkinUtils.class.getName());
	}

	/**
	 * @return	Returns true, if the activity done in postSelect was successful.
	 * @see de.admadic.laf.LaF#postSelect()
	 */
	@Override
	public boolean postSelect() {
		try {
			if (com.l2fprod.util.OS.isOneDotFourOrMore()) {
				boolean winDecYes;
				winDecYes = UIManager.getLookAndFeel().getSupportsWindowDecorations(); 
				if (winDecYes) {
					java.lang.reflect.Method method;
						method = javax.swing.JFrame.class.getMethod(
							"setDefaultLookAndFeelDecorated",
							new Class[] { boolean.class });
					method.invoke(null, new Object[] { Boolean.TRUE });
	
					method = javax.swing.JDialog.class.getMethod(
							"setDefaultLookAndFeelDecorated",
							new Class[] { boolean.class });
					method.invoke(null, new Object[] { Boolean.TRUE });
				} else {
					java.lang.reflect.Method method = 
						javax.swing.JFrame.class.getMethod(
							"setDefaultLookAndFeelDecorated",
							new Class[] { boolean.class });
					method.invoke(null, new Object[] { Boolean.FALSE });
	
					method = javax.swing.JDialog.class.getMethod(
							"setDefaultLookAndFeelDecorated",
							new Class[] { boolean.class });
					method.invoke(null, new Object[] { Boolean.FALSE });
				}
			}
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
		} catch (Exception e) {
			// e.printStackTrace();
			if (logger!=null) logger.severe(
					"Error selecting LaF: " + e.getMessage());
		}
		return true;
	}

	/**
	 * @return	Returns true, if the operations were successful.
	 * @see de.admadic.laf.LaF#preSelect()
	 */
	@Override
	public boolean preSelect() {
		//return super.preSelect();
		return true;
	}

	/**
	 * @return	Returns true, if the operations were successful.
	 * @see de.admadic.laf.LaF#select()
	 */
	@Override
	public boolean select() {
		if (getLastSkinName()!=null && getLastSkinName().startsWith("theme:")) {
			return true;
		} else {
			return super.select();
		}
	}

	/**
	 * @param skin
	 * @return	Returns true, if the operations were successful.
	 * @see de.admadic.laf.LaF#selectSkin(de.admadic.laf.LaFSkin)
	 */
	@Override
	public boolean selectSkin(LaFSkin skin) {
		try {
			String themepack = skin.getDataName();

			if (themepack.endsWith(".xml")) {
				
				SkinLookAndFeel.setSkin(
					SkinLookAndFeel
					.loadThemePackDefinition(
							SkinUtils.toURL(
								new File(getThemepackPath() + themepack))));
			} else if (themepack.startsWith("class:")) {
				String classname = themepack.substring("class:".length());
				SkinLookAndFeel.setSkin(
						(com.l2fprod.gui.plaf.skin.Skin)Class.forName(classname)
						.newInstance());
			} else if (themepack.startsWith("theme:")) {
				String classname = themepack.substring("theme:".length());
				MetalTheme theme = (MetalTheme) Class.forName(classname)
						.newInstance();
				MetalLookAndFeel metal = new MetalLookAndFeel();
				MetalLookAndFeel.setCurrentTheme(theme);

				// FIXME: setLookAndFeel should not be at this place:
				UIManager.setLookAndFeel(metal);
			} else {
				// FIXME: support URL based jar integrated themepacks
				String thfn = getThemepackPath() + themepack;
				File thf = new File(thfn);
				if (thf.exists()) {
					SkinLookAndFeel.setSkin(
							SkinLookAndFeel
									.loadThemePack(getThemepackPath() + themepack));
				} else {
					// Look in jar:./laf/SkinLF/themes/
					String resname = "/laf/SkinLF/themes/" + themepack;
					URL url = this.getClass().getResource(resname);
					SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack(url));
				}
			}

			setLastSkinName(themepack);
		} catch (Exception e) {
			// e.printStackTrace();
			if (logger!=null) logger.severe(
					"Error selecting Skin: " + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the lastSkinName.
	 */
	public String getLastSkinName() {
		return lastSkinName;
	}

	/**
	 * @param lastSkinName The lastSkinName to set.
	 */
	public void setLastSkinName(String lastSkinName) {
		this.lastSkinName = lastSkinName;
	}
}
