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

import java.io.File;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import de.admadic.cfg.Cfg;
import de.admadic.laf.LaF;
import de.admadic.laf.LaFManager;
import de.admadic.util.PathManager;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcLaFManager {
	final static boolean LOG = true;
	final static Logger logger = (LOG) ? Logger.getLogger("de.admadic") : null;


	static LaFManager init_lafMan;
	static Cfg init_cfg;
	static ClassLoader init_customCL;
	static PathManager init_pathMan;

	/**
	 * Class is static, constructor should not be necessary.
	 */
	protected CalcLaFManager() {
		super();
	}

	/**
	 * Updates the LaFManager on base of the current cfg settings.
	 */
	public static void updateLaFManager() {
		Object [] lafNamesObj;
		Object [] skinNamesObj;
		Object [] useIndexObj;
		Object [] skinUseIndexObj;

		init_lafMan.setClassLoader(init_customCL);

		init_lafMan.clearAllLaFs();

		if (!init_cfg.getBooleanValue(CfgCalc.KEY_UI_LAF_ENABLE, false)) {
			// LaF not enabled!
			if (logger!=null) logger.info("updateLaFManager: laf disabled.");
			return;
		}

		//lafMan.initStandardLaFs();
		lafNamesObj = init_cfg.getValueArray(CfgCalc.KEY_UI_LAF_LISTAVAIL_BASE);
		useIndexObj = init_cfg.getValueArray(CfgCalc.KEY_UI_LAF_LISTUSE_BASE);
		for (int i=0; i<useIndexObj.length; i++) {
			Object obj = useIndexObj[i];
			if (obj==null) continue;
			if (!(obj instanceof Integer)) continue;
			Integer index = (Integer)obj;
			Object availval = lafNamesObj[index.intValue()];
			if (!(availval instanceof String)) continue;
			String s = (String)availval;
			if (logger!=null) logger.fine("updateLaFManager: doing " + s);
			String [] sa = s.split(CfgCalc.ITEM_SEPARATOR_STR);
			String type, name, cname, icname;
			int typei;
			if (sa.length<3) {
				continue;
				// FIXME: make this an error
			}
			type = sa[0];
			name = sa[1];
			cname = sa[2];
			icname = (sa.length>=4) ? sa[3] : null;
			if (cname.equals("<xplatform>")) {
				cname = UIManager.getCrossPlatformLookAndFeelClassName();
			} else if (cname.equals("<system>")) {
				cname = UIManager.getSystemLookAndFeelClassName();
			} else {
				// ?
			}

			Class testAccess;
			if (cname!=null) {
				testAccess = null;
				try {
					if (init_customCL!=null) {
						if (logger!=null) logger.info("updateLaFManager: using custom CL...");
						testAccess = init_customCL.loadClass(cname);
					} else {
						if (logger!=null) logger.info("updateLaFManager: using Class.forName...");
						testAccess = Class.forName(cname);
					}
					//testAccess = Class.forName(cname);
				} catch (ClassNotFoundException e) {
					//e.printStackTrace();
					if (logger!=null) logger.info("Could not access class " + cname);
					if (logger!=null) logger.info("The Look and Feel " + name + " will be disabled.");
				}
				if (testAccess==null) continue;
			}

			if (icname!=null) {
				testAccess = null;
				try {
					if (init_customCL!=null) {
						testAccess = init_customCL.loadClass(icname);
					} else {
						testAccess = Class.forName(icname);
					}
				} catch (ClassNotFoundException e) {
					//e.printStackTrace();
					if (logger!=null) logger.info("Could not access class " + icname);
					if (logger!=null) logger.info("The Look and Feel " + name + " will be disabled.");
				}
				if (testAccess==null) continue;
			}

			typei = LaF.TYPE_NONE;
			if (type.equals(CfgCalc.UI_LAF_TYPE_PRIMARY)) {
				typei = LaF.TYPE_PRIMARY;
			} else if (type.equals(CfgCalc.UI_LAF_TYPE_SYSTEM)) {
				typei = LaF.TYPE_SYSTEM;
			} else if (type.equals(CfgCalc.UI_LAF_TYPE_EXTRA)) {
				typei = LaF.TYPE_EXTRA;
			} else {
				// nothing?
			}
			init_lafMan.addLaF(name, cname, true, icname, typei);
			if (logger!=null) logger.info("updateLaFManager: "+
					"added LaF: " + name + ", " + cname + ", " + icname);

			{ // update skins
				LaF laf = init_lafMan.getLaF(name, null);
				skinNamesObj = init_cfg.getValueArray(
						CfgCalc.KEY_UI_LAF_SKIN_LISTAVAIL_BASE + name + ".");
				skinUseIndexObj = init_cfg.getValueArray(
						CfgCalc.KEY_UI_LAF_SKIN_LISTUSE_BASE + name + ".");
				for (int j=0; j<skinUseIndexObj.length; j++) {
					Object skiobj = skinUseIndexObj[j];
					if (skiobj==null) continue;
					if (!(skiobj instanceof Integer)) continue;
					Integer skindex = (Integer)skiobj;
					Object skavailval = skinNamesObj[skindex.intValue()];
					if (!(skavailval instanceof String)) continue;

					String sk = (String)skavailval;
					String [] ska = sk.split(CfgCalc.ITEM_SEPARATOR_STR);
					init_lafMan.addSkin(laf, ska[0], ska[1]);
					if (logger!=null) logger.info(
							"updateLaFManager: "+
							"added LaF-Skin: " + ska[0] + ":" + ska[1]);
				}
			} // (update skins)
		}

//		lafMan.addLaF(
//				"Squareness", 
//				"net.beeger.squareness.SquarenessLookAndFeel", 
//				true, 
//				null);

		//Thread.currentThread().setContextClassLoader(init_customCL);

		LaF laf = init_lafMan.getLaF("SkinLF", null);

		logger.info("got laf: " + laf.getClassName());
		logger.info("laf-CL: " + laf.getClass().getClassLoader().getClass().getName());

//		LaFSkinLF laf2 = (LaFSkinLF)laf;

//		logger.info("got laf2: " + laf2.getClassName());
//		logger.info("laf2-CL: " + laf2.getClass().getClassLoader().getClass().getName());
//
		// FIXME: check whether we need better File conforming path creation:
		laf.setThemepackPath(
				init_pathMan.getPathString(PathManager.SYS_APP_DIR) + 
				File.separator +
				init_cfg.getStringValue(
						CfgCalc.KEY_UI_LAF_IMPL_SKINLF_THEMEPACK_PATH, 
						null));
	}

	/**
	 * Activate the selected look and feel.
	 */
	public static void activateLaF() {
		// ---now setup LaF-------------------------------
		String laFName;
		String skinName;

		if (!init_cfg.getBooleanValue(CfgCalc.KEY_UI_LAF_ENABLE, false)) {
			// no LaF enabled
			if (logger!=null) logger.info("activateLaF: laf is disabled");
			return;
		}

		laFName = init_cfg.getStringValue(
				CfgCalc.KEY_UI_LAF_SELECT, 
				null);
		if (laFName==null) {
			return;
		}
		skinName = init_cfg.getStringValue(
				CfgCalc.KEY_UI_LAF_SKIN_SELECT_BASE + laFName, 
				null);
		if (skinName==null) {
			// ??
		} else {
			String [] ska = skinName.split(CfgCalc.ITEM_SEPARATOR_STR);
			skinName = ska[0];
		}
		if (logger!=null) logger.info("activateLaF: selecting " + laFName + ":" + skinName);
		if (!init_lafMan.selectLaF(laFName, skinName)) {
			JOptionPane.showMessageDialog(
					null, 
					"An Error occured during activation of\n"+
					"Look-And-Feel = '" + laFName + 
					"' , Skin = '" + skinName + "'.\n" + 
					"You may want to contact customer support.",
					"Error selecting Skin",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Initializes the Look-and-Feel-Manager.
	 * The LaFManager has to be created already.
	 * 
	 * @param lafMan	the LaFManager to be initialized
	 * @param cfg		the applications configuration space
	 * @param customCL 
	 * @param pathMan 
	 */
	public static void initLaFManager(
			LaFManager lafMan, 
			Cfg cfg,
			ClassLoader customCL,
			PathManager pathMan) {

		init_lafMan = lafMan;
		init_cfg = cfg;
		init_customCL = customCL;
		init_pathMan = pathMan;

		updateLaFManager();
		activateLaF();
	}
}
