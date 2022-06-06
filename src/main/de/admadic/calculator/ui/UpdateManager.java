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

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.Version;
import de.admadic.cfg.Cfg;
import de.admadic.ui.util.Dialog;
import de.admadic.util.FileUtil;
import de.admadic.util.PathManager;
import de.admadic.util.VersionName;
import de.admadic.util.VersionUtil;

/**
 * @author Rainer Schwarze
 *
 */
public class UpdateManager {
	// FIXME: fixup the code for file copying. insert retries and 
	// extract common code

	final static boolean LOG = true;
	Logger logger = null;

	CfgCalc cfg;
	PathManager pathMan;
	Vector<String> versions; // list of directory names (w/o path!)
	Vector<VersionName> versionNames;	// list of VersionName records
							// (sync'ed to versions)
	Hashtable<VersionName,String> versionNamesToVersions;
	Hashtable<String,File> cfgfiles;
	String curVer;
	int curVerIdx;
	String mergeSrcVer;

	Cfg cachePrevCfg;

	
	/**
	 * @param cfg 
	 * @param pathMan 
	 * 
	 */
	public UpdateManager(CfgCalc cfg, PathManager pathMan) {
		super();
		this.cfg = cfg;
		this.pathMan = pathMan;
		versions = new Vector<String>();
		versionNames = new Vector<VersionName>();
		versionNamesToVersions = new Hashtable<VersionName,String>();
		cfgfiles = new Hashtable<String,File>();
		curVer = null;
		curVerIdx = -1;
	}

	/**
	 * 
	 */
	public void initLogging() {
		if (LOG) logger = (LOG) ? Logger.getLogger("de.admadic") : null;
	}

	/**
	 * @return	Returns true, if this installation is fresh.
	 */
	public boolean isFresh() {
		return !cfg.existsCfgFile();
	}

	protected int compareVersion(VersionName v0, VersionName v1) {
		if ((v0.getVersionRecord()==null) && (v1.getVersionRecord()==null)) {
			return v0.getCombinedName().compareTo(v1.getCombinedName());
		} else if ((v0.getVersionRecord()==null) && (v1.getVersionRecord()!=null)) {
			return -1;	// version is always larger than no version
		} else if ((v0.getVersionRecord()!=null) && (v1.getVersionRecord()==null)) {
			return +1;	// version is always larger than no version
		} else { // both have a version record:
			return v0.getVersionRecord().compareTo(v1.getVersionRecord());
		}
	}

	/**
	 * 
	 */
	public void detectInstalledVersions() {
		String dirForVer = pathMan.getPathString(PathManager.USR_APPGRP_DIR);
		if (dirForVer==null) {
			if (logger!=null) logger.warning(
					"could not detect user application group directory."+
					" update check cancelled.");
			return;	// no chance without that
		}
		if (logger!=null) logger.config(
				"user application group directory = " + dirForVer);
		File dir = new File(dirForVer);
		File [] dlist = dir.listFiles();
		if (dlist==null) {
			if (logger!=null) logger.warning(
					"could not get list of entries in"+
					" user application group directory."+
					" update check cancelled.");
			return;		// no chance without list of dirs
		}

		File curCfg = null;

		for (File d : dlist) {
			File tmpF = new File(d, "cfg/cfg.cfg");
			if (tmpF==null) continue;
			if (!tmpF.exists()) continue;

			String vs = d.getName();
			versions.add(vs);
			VersionName vn = VersionUtil.getVersionNameFromFileName(vs);
			versionNames.add(vn);
			versionNamesToVersions.put(vn, vs);

			if (logger!=null) logger.config(
					"found cfg file at " + tmpF.toString() + 
					" (as version " + vs + ")");
			if (dir.getName().equals(Version.versionFS)) {
				if (logger!=null) logger.config(
						"this was the cfg file for the current version." + 
						" (app version " + Version.versionFS + ")");
				curVer = vs;
			}
			cfgfiles.put(vs, tmpF);
		}

		Comparator<VersionName> vc = new Comparator<VersionName>() {
			public int compare(VersionName v0, VersionName v1) {
				return compareVersion(v0, v1);
			}
		};
		if (curCfg==null) {
			if (logger!=null) logger.config(
					"a cfg file could not be found for the current version."+
					" creating an entry for it.");
			curCfg = new File(
					pathMan.getPathString(PathManager.USR_CFG_DIR), "cfg.cfg");
			versions.add(Version.versionFS);
			cfgfiles.put(Version.versionFS, curCfg);
			curVer = Version.versionFS;
		}
		Collections.sort(versionNames, vc);
		sortListByListAndHash(versions, versionNames, versionNamesToVersions);

		curVerIdx = versions.indexOf(curVer);

//		curVerIdx =
//		if (idx<0) {
////			System.out.println("no curCfg Entry");
//			return;
//		}
//
//		idx--;
//		if (idx<0) {
////			System.out.println("nothing before cur");
//			return;
//		}
	}

	/**
	 * @param <T1> 
	 * @param <T2> 
	 * @param sortList
	 * @param sortedList
	 * @param sortedToSortHash
	 */
	protected static <T1, T2> void sortListByListAndHash(
			List<T1> sortList, 
			List<T2> sortedList, 
			Hashtable<T2, T1> sortedToSortHash) {
		ListIterator<T2> sdit = sortedList.listIterator();
		while (sdit.hasPrevious()) {
			T2 el = sdit.previous();
			T1 oel = sortedToSortHash.get(el);
			sortList.remove(oel);
			sortList.add(0, oel);
		}
	}

	/**
	 * 
	 */
	public void useClosestMatch() {
		if (logger!=null) logger.config(
				"trying to automatically use version which matches best.");
		mergeSrcVer = null;
		if (curVerIdx<0) return;
		int selVer = curVerIdx - 1;
		if (selVer<0) return;
		mergeSrcVer = versions.get(selVer);
		if (logger!=null) logger.config(
				"best match is " + mergeSrcVer);
	}


	/**
	 * 
	 */
	public void selectMergeSource() {
		ChoosePreviousVersionDialog dlg = new ChoosePreviousVersionDialog();
		dlg.initGUI(curVer, versions);
		dlg.setVisible(true);
		if (dlg.getResultCode()!=Dialog.RESULT_OK) {
			if (logger!=null) logger.config(
					"choose previous version: user cancelled");
			mergeSrcVer = null;
		} else {
			mergeSrcVer = dlg.getSelectedVersion();
			if (logger!=null) logger.config(
					"choose previous version: user chose " + mergeSrcVer);
		}
//		useClosestMatch();
//		int result = JOptionPane.showConfirmDialog(
//				null,
//				"A configuration of a previous application version was found:\n"+
//				"File = " + cfgfiles.get(mergeSrcVer).toString() + "\n"+
//				"Do you want to copy it to the current configuration space?\n"+
//				"(Then your settings and licensing data will be used automatically)",
//				"Confirm copying previous configuration",
//				JOptionPane.YES_NO_OPTION);
//		if (result==JOptionPane.YES_OPTION) {
//			// ?
//		} else {
//			// no
//			mergeSrcVer = null;
//		}
	}

	/**
	 * @return	Returns true, if a merge source is there and should be used.
	 */
	public boolean shouldMerge() {
		return mergeSrcVer!=null;
	}

	/**
	 * 
	 */
	public void doMerge() {
		if (mergeSrcVer==null) {
			if (logger!=null) logger.warning(
					"doMerge: no previous version selected."+
					" aborting update process.");
			return; // FIXME: maybe Error
		}

		cachePrevCfg = CfgCalc.createTmpCfg(
				cfgfiles.get(mergeSrcVer).toString());
		cachePrevCfg.loadPreferences(CfgCalc.PREFERENCES_PATH);

		doMerge_CfgFile();
		doMerge_Modules();
		doMerge_LaFs();
		doMerge_Skins();
	}

	protected void copyCfgElement(String key) {
		Object o = cachePrevCfg.getValue(key, null);
		if (o==null) {
			if (logger!=null) logger.config(
					"skipping - key not in previous cfg: " + key);
			return; // not there, so skip
		} else {
			cfg.putValue(key, o);
			if (logger!=null) logger.config(
					"copying key/value: " + key + "/" + o.toString());
		}
	}
	
	protected void doMerge_CfgFile() {
		if (logger!=null) logger.config(
				"copying simple settings from cfg file.");

		copyCfgElement(CfgCalc.KEY_UI_LAF_ENABLE);
		// dont update: KEY_UI_LAF_LISTUSEFILLIFEMPTY, Boolean.TRUE
		// let the user manually select the current LAF:
		// dont update: KEY_UI_LAF_SELECT, "SkinLF"
		// dont update: KEY_UI_LAF_SKIN_SELECT_BASE + "SkinLF", 
		// dont update: KEY_UI_LAF_IMPL_SKINLF_THEMEPACK_PATH, "./laf/SkinLF/themes/"
		copyCfgElement(CfgCalc.KEY_UI_BUTTON_LOADGFX);	
		copyCfgElement(CfgCalc.KEY_UI_BUTTON_CUSTOMFONT);	
		copyCfgElement(CfgCalc.KEY_UI_BUTTON_PAINTFOCUS);	
		// dont update: KEY_UI_MAIN_PANEL_BACKGROUND, Boolean.FALSE	
		// dont update: KEY_UI_MAIN_PANEL_BACKGROUND_FILE, ""	
		copyCfgElement(CfgCalc.KEY_UI_MAIN_ALWAYSONTOP);
		copyCfgElement(CfgCalc.KEY_UI_MAIN_PROTOCOLWINDOW_ON);
		copyCfgElement(CfgCalc.KEY_UI_MAIN_MATHWINDOW_ON);
		copyCfgElement(CfgCalc.KEY_UI_MAIN_PANELMOVE);	
		copyCfgElement(CfgCalc.KEY_UI_MAIN_PRELOAD_UI);	
		copyCfgElement(CfgCalc.KEY_UI_MAIN_EXTENDEDSETTINGS);	
		copyCfgElement(CfgCalc.KEY_UI_MAIN_SKINMENU_FLAT);
		copyCfgElement(CfgCalc.KEY_UI_LAYOUT_CCL_FIXEDCELLSIZE);
		copyCfgElement(CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + CfgCalc.UI_STYLE_BUTTON_STANDARD + ".width");
		copyCfgElement(CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + CfgCalc.UI_STYLE_BUTTON_STANDARD + ".height");
		copyCfgElement(CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + CfgCalc.UI_STYLE_BUTTON_SMALL + ".width");
		copyCfgElement(CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + CfgCalc.UI_STYLE_BUTTON_SMALL + ".height");
		copyCfgElement(CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".width");		
		copyCfgElement(CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".height");

		// dont update: {KEY_BASE_CLASSPATH_JAR + "0", ""},
		copyCfgElement(CfgCalc.KEY_CLASSPATH_USE_EXTENDER);
		// dont update: KEY_CLASSPATH_DIRS, "./laf/:./lib/"
		copyCfgElement(CfgCalc.KEY_UI_NUMBER_FORMAT);
		copyCfgElement(CfgCalc.KEY_UI_MAIN_GMX_SNOW);	
	}

	protected void doMerge_Modules() {
		if (logger!=null) logger.config(
				"copying module settings from cfg file.");

		// first copy module list
		Object [] oa = cachePrevCfg.getValueArray(CfgCalc.KEY_MODULE_LIST_BASE);
		if (oa!=null) {
			cfg.putValueArray(CfgCalc.KEY_MODULE_LIST_BASE, oa);
			// logging only:
			if (logger!=null) {
				for (int i=0; i<oa.length; i++) {
					Object o = oa[i];
					logger.config("copying key/value: " + 
						CfgCalc.KEY_MODULE_LIST_BASE + i + "/" + 
						o.toString());
				}
			}
		}
		
		Vector<String> modules = new Vector<String>();
		Enumeration<String> en = cachePrevCfg.getCfgItemKeys();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			if (!key.startsWith(CfgCalc.KEY_MODULE_INAMES_BASE)) continue;
			modules.add(key);
			if (logger!=null) logger.config(
					"module tag found for: " + key);
		}

		for (String iname : modules) {
			iname = iname.substring(CfgCalc.KEY_MODULE_INAMES_BASE.length());
			doMerge_Module(iname);
		}

		doMerge_ModuleFiles();
	}

	private void doMerge_Module(String iname) {
		String keyname = CfgCalc.PREFIX_MODULES + iname + ".";
		Vector<String> keys = new Vector<String>();
		Enumeration<String> en = cachePrevCfg.getCfgItemKeys();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			if (!key.startsWith(keyname)) continue;
			keys.add(key);
		}

		for (String key : keys) {
			// key is mod.{iname}.{keyrest}
			String keyrest = key.substring(keyname.length());
			Object o = cachePrevCfg.getValue(key, null);
			if (o==null) {
				if (logger!=null) logger.config(
						"skipping - key not in previous cfg: " + key);
				continue; // not there, so skip
			} else {
				String newkey = keyname + CfgCalc.INFIX_MODULES_PREV + "." + keyrest;
				cfg.putValue(newkey, o);
				if (logger!=null) logger.config(
						"copying key/value: " + key + "/" + o.toString());
			}
		}
	}

	private void doMerge_ModuleFiles() {
		if (logger!=null) logger.config(
			"copying module files.");

		//		now copy the files
		String modPathS = pathMan.getPathString(PathManager.SYS_MOD_DIR);
		String prevModPathS = pathMan.getPathString(PathManager.SYS_APPGRP_DIR);
		if (logger!=null) logger.config("sys-mod-dir = " + modPathS);
		if (logger!=null) logger.config("sys-appgrp-dir = " + prevModPathS);
		File modPath = new File(modPathS);
		File prevModPath = new File(prevModPathS);
		prevModPath = new File(prevModPath, mergeSrcVer);
		prevModPath = new File(prevModPath, "mod");
		if (logger!=null) logger.config(
				"trying prev-sys-mod-dir = " + prevModPath.toString());
		if (!prevModPath.exists()) {
			if (logger!=null) logger.config(
					"Could not find previous laf-directory."+
					" cancelling laf-file copy.");
			// modules are available since 1.0.3, so maybe
			// add that check and if the source is >= 1.0.3 raise an error
//			JOptionPane.showMessageDialog(
//					null,
//					"The path to the Look-and-Feel files of the previous" +
//					" calculator version could not be found.\n"+
//					"Please copy the missing Look-and-Feel files by yourself.",
//					"Could not find LaF-path of previous version",
//					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		File [] thisModfiles = modPath.listFiles();
		Vector<String> thisModnames = new Vector<String>();
		for (int i = 0; i < thisModfiles.length; i++) {
			File file = thisModfiles[i];
			thisModnames.add(file.getName());
		}
		
		File [] prevModfiles = prevModPath.listFiles();
		for (File file : prevModfiles) {
//			if (!thisLafnames.contains(file.getName())) {
//			continue;
//			}
			// copy
			File src = file;
			File dst = new File(modPath, file.getName());
			if (dst.exists()) {
				if (logger!=null) logger.config(
						"mod-file exists in current version. skipping: " + 
						src.toString());
				continue;
			}
			if (!src.isFile()) {
				if (logger!=null) logger.warning(
						"src is not a file: src: " + 
						src.toString());
			}
			if (!src.toString().toLowerCase().endsWith(".jar")) {
				if (logger!=null) logger.warning(
						"src is not a jar file: src: " + 
						src.toString());
				JOptionPane.showMessageDialog(
						null,
						"Could not copy the file:\n"+
						src + "\n"+
						"It is not a jar-file - only jar files are"+
						" supported for updating.\n"+
						"Please copy the file or directory by yourself.",
						"Could not copy file",
						JOptionPane.ERROR_MESSAGE);
				continue;
			}
			
			if (logger!=null) logger.config(
					"copying: src->dst: " + 
					src.toString() + " -> " + dst.toString());
			// FIXME: maybe a retry on error?
			if (!FileUtil.copyFile(src.toString(), dst.toString())) {
				if (logger!=null) logger.warning(
						"copy failed: src->dst: " + 
						src.toString() + " -> " + dst.toString());
				JOptionPane.showMessageDialog(
						null,
						"Could not copy the file:\n"+
						src + "\n"+
						"over\n"+
						dst + "\n"+
						"Please copy the file by yourself.",
						"Could not copy file",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		
		JOptionPane.showMessageDialog(
				null,
				"Some Modules were copied from the previous version"+
				" to this version.\n"+
				"You should restart the calculator after the update is finished.\n"+
				"If the Modules are not visible, please go to Settings and check,\n"+
				"whether they are enabled or not.",
				"Installed Modules",
				JOptionPane.INFORMATION_MESSAGE);
	}

	protected void doMerge_LaFs() {
		if (logger!=null) logger.config(
				"copying Look-and-Feel settings.");

		Vector<String> tobecopied = new Vector<String>();
		Object [] prevLafs = cachePrevCfg.getValueArray(
				CfgCalc.KEY_UI_LAF_LISTAVAIL_BASE);
		Object [] thisLafs = cfg.getValueArray(
				CfgCalc.KEY_UI_LAF_LISTAVAIL_BASE);
		Vector<String> thisLafClassNames = new Vector<String>();
		ArrayList<String> thisLafsV = new ArrayList<String>();
		
		for (Object o : thisLafs) {
			String s = (String)o;
			String [] sa = s.split(CfgCalc.ITEM_SEPARATOR_STR);
			thisLafClassNames.add(sa[2]);
			thisLafsV.add(s);
		}
		for (Object o : prevLafs) {
			String s = (String)o;
			String [] sa = s.split(CfgCalc.ITEM_SEPARATOR_STR);
			if (thisLafClassNames.contains(sa[2])) {
				// already there.
			} else {
				// not there, must be new:
				tobecopied.add(s);
			}
		}
		if (tobecopied.size()<1) {
			if (logger!=null) logger.config(
					"there are no Look-and-Feels to be copied.");
			return; // nothing to do
		}

		for (String s : tobecopied) {
			if (logger!=null) logger.config(
					"will copy Look-and-Feel: " + s);
			thisLafsV.add(s);
		}
		cfg.putValueArray(
				CfgCalc.KEY_UI_LAF_LISTAVAIL_BASE,
				thisLafsV.toArray());
		if (logger!=null) logger.config(
				"copied the listed Look-and-Feel settings.");
		
		// now copy the files
		String lafPathS = pathMan.getPathString(PathManager.SYS_LAF_DIR);
		String prevLafPathS = pathMan.getPathString(PathManager.SYS_APPGRP_DIR);
		if (logger!=null) logger.config("sys-laf-dir = " + lafPathS);
		if (logger!=null) logger.config("sys-appgrp-dir = " + prevLafPathS);
		File lafPath = new File(lafPathS);
		File prevLafPath = new File(prevLafPathS);
		prevLafPath = new File(prevLafPath, mergeSrcVer);
		prevLafPath = new File(prevLafPath, "laf");
		if (logger!=null) logger.config(
				"trying prev-sys-laf-dir = " + prevLafPath.toString());
		if (!prevLafPath.exists()) {
			if (logger!=null) logger.config(
					"Could not find previous laf-directory."+
					" cancelling laf-file copy.");
			JOptionPane.showMessageDialog(
					null,
					"The path to the Look-and-Feel files of the previous" +
					" calculator version could not be found.\n"+
					"Please copy the missing Look-and-Feel files by yourself.",
					"Could not find LaF-path of previous version",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		File [] thisLaffiles = lafPath.listFiles();
		Vector<String> thisLafnames = new Vector<String>();
		for (int i = 0; i < thisLaffiles.length; i++) {
			File file = thisLaffiles[i];
			thisLafnames.add(file.getName());
		}

		File [] prevLaffiles = prevLafPath.listFiles();
		for (File file : prevLaffiles) {
//			if (!thisLafnames.contains(file.getName())) {
//				continue;
//			}
			// copy
			File src = file;
			File dst = new File(lafPath, file.getName());
			if (dst.exists()) {
				if (logger!=null) logger.config(
						"laf-file exists in current version. skipping: " + 
						src.toString());
				continue;
			}
			if (!src.isFile()) {
				if (logger!=null) logger.warning(
						"src is not a file: src: " + 
						src.toString());
			}
			if (!src.toString().toLowerCase().endsWith(".jar")) {
				if (logger!=null) logger.warning(
						"src is not a jar file: src: " + 
						src.toString());
				JOptionPane.showMessageDialog(
						null,
						"Could not copy the file:\n"+
						src + "\n"+
						"It is not a jar-file - only jar files are"+
						" supported for updating.\n"+
						"Please copy the file or directory by yourself.",
						"Could not copy file",
						JOptionPane.ERROR_MESSAGE);
				continue;
			}

			if (logger!=null) logger.config(
					"copying: src->dst: " + 
					src.toString() + " -> " + dst.toString());
			// FIXME: maybe a retry on error?
			if (!FileUtil.copyFile(src.toString(), dst.toString())) {
				if (logger!=null) logger.warning(
						"copy failed: src->dst: " + 
						src.toString() + " -> " + dst.toString());
				JOptionPane.showMessageDialog(
						null,
						"Could not copy the file:\n"+
						src + "\n"+
						"over\n"+
						dst + "\n"+
						"Please copy the file by yourself.",
						"Could not copy file",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		JOptionPane.showMessageDialog(
				null,
				"Some Look-and-Feels were copied from the previous version"+
				" to this version.\n"+
				"You should restart the calculator after the update is finished.\n"+
				"If the Look-and-Feels are not visible, please go to Settings and check,\n"+
				"whether they are enabled or not.",
				"Installed Look-and-Feels",
				JOptionPane.INFORMATION_MESSAGE);
	}

	protected void doMerge_Skins() {
		if (logger!=null) logger.config(
				"copying themepacks/skins settings.");

		Vector<String> tobecopied = new Vector<String>();
		Object [] prevSkins = cachePrevCfg.getValueArray(
				CfgCalc.KEY_UI_LAF_SKIN_LISTAVAIL_BASE + 
				CfgCalc.UI_LAF_IMPL_SKINLF_NAME + ".");
		Object [] thisSkins = cfg.getValueArray(
				CfgCalc.KEY_UI_LAF_SKIN_LISTAVAIL_BASE + 
				CfgCalc.UI_LAF_IMPL_SKINLF_NAME + ".");
		Vector<String> thisSkinDataNames = new Vector<String>();
		ArrayList<String> thisSkinsV = new ArrayList<String>();
		
		for (Object o : thisSkins) {
			String s = (String)o;
			if (logger!=null) logger.config(
				"this has: " + s);
			String [] sa = s.split(CfgCalc.ITEM_SEPARATOR_STR);
			thisSkinDataNames.add(sa[1]);
			thisSkinsV.add(s);
		}
		for (Object o : prevSkins) {
			String s = (String)o;
			if (logger!=null) logger.config(
					"prev has: " + s);
			String [] sa = s.split(CfgCalc.ITEM_SEPARATOR_STR);
			if (thisSkinDataNames.contains(sa[1])) {
				// already there.
			} else {
				// not there, must be new:
				tobecopied.add(s);
			}
		}
		if (tobecopied.size()<1) {
			if (logger!=null) logger.config(
					"there are no themepack/skins files to be copied.");
			return; // nothing to do
		}

		for (String s : tobecopied) {
			if (logger!=null) logger.config(
					"will copy themepack/skin: " + s);
			thisSkinsV.add(s);
		}
		cfg.putValueArray(
				CfgCalc.KEY_UI_LAF_SKIN_LISTAVAIL_BASE + 
				CfgCalc.UI_LAF_IMPL_SKINLF_NAME + ".",
				thisSkinsV.toArray());
		if (logger!=null) logger.config(
				"copied the listed cfg entries.");
		
		// now copy the files
		String skinPathS = pathMan.getPathString(PathManager.SYS_APP_DIR);
		String prevSkinPathS = pathMan.getPathString(PathManager.SYS_APPGRP_DIR);
		if (logger!=null) logger.config("sys-app-dir = " + skinPathS);
		if (logger!=null) logger.config("sys-appgrp-dir = " + prevSkinPathS);
		File skinPath = new File(skinPathS);
		skinPath = new File(
				skinPath, 
				cfg.getStringValue(
						CfgCalc.KEY_UI_LAF_IMPL_SKINLF_THEMEPACK_PATH, "."));
		File prevSkinPath = new File(prevSkinPathS); 
		prevSkinPath = new File(prevSkinPath, mergeSrcVer);
		// no laf manually, already stored in cfg:
		// prevSkinPath = new File(prevSkinPath, "laf");
		prevSkinPath = new File(
				prevSkinPath, 
				cachePrevCfg.getStringValue(
						CfgCalc.KEY_UI_LAF_IMPL_SKINLF_THEMEPACK_PATH, "."));
		if (logger!=null) logger.config(
				"trying prev-sys-laf-theme-dir = " + prevSkinPath.toString());
		if (!prevSkinPath.exists()) {
			if (logger!=null) logger.config(
					"Could not find previous laf-theme-directory."+
					" cancelling themepack/skin-file copy.");
			JOptionPane.showMessageDialog(
					null,
					"The path to the Themepack files of the previous" +
					" calculator version could not be found.\n"+
					"Please copy the missing Themepack files by yourself.",
					"Could not find Themepack-path of previous version",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		File [] thisSkinfiles = skinPath.listFiles();
		Vector<String> thisSkinnames = new Vector<String>();
		for (int i = 0; i < thisSkinfiles.length; i++) {
			File file = thisSkinfiles[i];
			thisSkinnames.add(file.getName());
		}

		File [] prevSkinfiles = prevSkinPath.listFiles();
		for (File file : prevSkinfiles) {
			// if (!thisSkinnames.contains(file.getName())) continue;
			// copy
			File src = file;
			File dst = new File(skinPath, file.getName());
			if (dst.exists()) {
				if (logger!=null) logger.config(
						"themepack/skin-file exists in current version. skipping: " + 
						src.toString());
				continue;
			}

			if (!src.toString().toLowerCase().endsWith(".zip")) {
				if (logger!=null) logger.config(
						"not a zip file. skipping: " + src.toString());
				JOptionPane.showMessageDialog(
						null,
						"Could not copy the file:\n"+
						src + "\n"+
						"It is not a zip-file - only zip files are"+
						" supported for updating.\n"+
						"Please copy the file or directory by yourself.",
						"Could not copy file",
						JOptionPane.ERROR_MESSAGE);
				continue;
			}

			if (logger!=null) logger.config(
					"copying: src->dst: " + 
					src.toString() + " -> " + dst.toString());
			// FIXME: maybe a retry on error?
			if (!FileUtil.copyFile(src.toString(), dst.toString())) {
				if (logger!=null) logger.warning(
						"copy failed: src->dst: " + 
						src.toString() + " -> " + dst.toString());
				JOptionPane.showMessageDialog(
						null,
						"Could not copy the file:\n"+
						src + "\n"+
						"over\n"+
						dst + "\n"+
						"Please copy the file by yourself.",
						"Could not copy file",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		JOptionPane.showMessageDialog(
				null,
				"Some themepack files (skins) were copied from the previous"+
				" version to this version.\n"+
				"If the themepacks are not visible, please go to Settings \n"+
				"and check, whether they are enabled or not.",
				"Installed Themepacks",
				JOptionPane.INFORMATION_MESSAGE);
	}

	class ChoosePreviousVersionDialog extends Dialog implements ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		JTextArea textMsg;
		JList versionList;
		JScrollPane versionScroll;
		JPanel buttonPanel;
		JButton btnOk;
		JButton btnCancel;
		Vector<String> versionsV;

		final static String CMD_OK = "cmd.ok";
		final static String CMD_CANCEL = "cmd.cancel";

		int selectedIndex;
		String selectedValue;

		String insVerStr;
		int insVerIdx;

		/**
		 * @throws HeadlessException
		 */
		public ChoosePreviousVersionDialog() throws HeadlessException {
			super();
			selectedIndex = -1;
		}

		/**
		 * @return	Returns the selected version as a String.
		 */
		public String getSelectedVersion() {
			return selectedValue;
		}

		/**
		 * @param insVer
		 * @param versions
		 */
		public void initGUI(String insVer, Vector<String> versions) {
			FormLayout fl = new FormLayout(
					"12px, p, 12px",
					"12px, p, 5px, p, 5px, p, 12px");
			this.setLayout(fl);
			CellConstraints cc = new CellConstraints();

			insVerStr = insVer;
			insVerIdx = versions.indexOf(insVerStr);
			versionsV = new Vector<String>();
			versionsV.addAll(versions);
			if (versionsV.contains(insVerStr)) {
				versionsV.remove(insVerStr);
			}
			
			this.setTitle("admaDIC Calculator - Update settings from");
			this.setModal(true);

			textMsg = new JTextArea();
			textMsg.setText(
					"The software seems to be started for the first time.\n"+
					"\n"+
					"The following versions could be detected\n"+
					"on the computer. You may select a version from which\n"+
					"the settings will be imported into this version,\n"+
					"or press Cancel to skip this step.\n"+
					"\n"+
					"Please select the version from which you want to \n"+
					"copy the settings.\n"+
					"(The current version is " + insVerStr + ")");
			textMsg.setEditable(false);
			this.add(textMsg, cc.xy(2, 2));
			
			versionList = new JList(versionsV);
			versionList.getSelectionModel().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
			versionScroll = new JScrollPane(
					versionList,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			this.add(versionScroll, cc.xy(2, 4));
			{
				int tmp = versions.indexOf(insVer);
				if (tmp>=0) {
					versionList.setSelectedIndex(tmp);
				}
			}

			btnOk = new JButton("OK");
			btnOk.addActionListener(this);
			btnOk.setActionCommand(CMD_OK);

			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(this);
			btnCancel.setActionCommand(CMD_CANCEL);

			buttonPanel = new JPanel();
			buttonPanel.add(btnOk);
			buttonPanel.add(btnCancel);
			this.add(buttonPanel, cc.xy(2, 6));

			this.pack();
			this.setLocationRelativeTo(null);
		}

		protected boolean shouldWarnAboutVersion(int selIdx) {
			if (insVerIdx<0) return false;	// nothing to compare with
			// smaller versions have smaller idx
			if (selIdx<insVerIdx) return false;
			return true;
		}
		
		/**
		 * @param arg0
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			String cmd = arg0.getActionCommand();
			if (cmd.equals(CMD_OK)) {
				// selected version?
				selectedIndex = versionList.getSelectedIndex();
				selectedValue = (String)versionList.getSelectedValue();

				if (selectedIndex<0) {
					JOptionPane.showMessageDialog(
							null,
							"No version selected. Please select a version from which"+
							" the configuration shall be copied.",
							"No version selected",
							JOptionPane.YES_NO_OPTION);
					return;
				}
				if (shouldWarnAboutVersion(selectedIndex)) {
					int result = JOptionPane.showConfirmDialog(
							null,
							"The installed version is " + insVerStr + ".\n"+
							"The version to use the data from is " + 
								selectedValue + ".\n"+
							"It looks like you want to take the settings from"+
							" a higher version than the one currently running.\n"+
							"Are you sure you want to take settings from " + 
								selectedValue + "?",
							"Check version",
							JOptionPane.YES_NO_OPTION);
					if (result!=JOptionPane.YES_OPTION) {
						return;
					}
				}
				
				setWindowCloseCode(RESULT_OK);
				setResultCode(RESULT_OK);
				setVisible(false);
			} else if (cmd.equals(CMD_CANCEL)) {
				selectedIndex = -1;
				selectedValue = null;
				setWindowCloseCode(RESULT_CANCEL);
				setResultCode(RESULT_CANCEL);
				setVisible(false);
			} else {
				// ?
			}
		}
	}// (ChoosePreviousVersionDialog)
}
