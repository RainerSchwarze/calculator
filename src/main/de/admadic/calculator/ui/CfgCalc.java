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

import java.io.*;
import java.net.URL;
import java.util.Properties;

import javax.swing.JOptionPane;

import de.admadic.cfg.Cfg;
import de.admadic.cfg.CfgItem;
import de.admadic.cfg.CfgPersistenceXML;
import de.admadic.util.FileUtil;
import de.admadic.util.PathManager;


/**
 * @author Rainer Schwarze
 *
 */
public class CfgCalc extends Cfg {
	static boolean DBG = false;

	// FIXME: the ITEM_SEPARATOR is used as a regexp in String.split.
	// that handling should be fixed in case we get regexp characters here!
	/** Separator character for multi-entry string values. (is ':') */
	public final static char ITEM_SEPARATOR = ':';
	/** Convenience String for ITEM_SEPARATOR */
	public final static String ITEM_SEPARATOR_STR = ""+ITEM_SEPARATOR;

	/** The path for use with the java.util.prefs.Preferences classes */
	public final static String PREFERENCES_PATH = "/de.admadic.calculator";

	/** Value for LaF type PRIMARY */
	public final static String 
	UI_LAF_TYPE_PRIMARY = "p";
	/** Value for LaF type SYSTEM */
	public final static String 
	UI_LAF_TYPE_SYSTEM = "s";
	/** Value for LaF type EXTRA/CUSTOM */
	public final static String 
	UI_LAF_TYPE_EXTRA = "x";

	/** Key for testing whether LaF is enabled or not */
	public final static String 
	KEY_UI_LAF_ENABLE = "ui.laf.enable";
	/** Key for getting the selected LaF */
	public final static String 
	KEY_UI_LAF_SELECT = "ui.laf.select";
	/** Key-base for getting the selected theme for a LaF */
	public final static String 
	KEY_UI_LAF_SKIN_SELECT_BASE = "ui.laf.skin.";
	/** Key - implementation specific for SkinLF: path to the themepacks */
	public final static String 
	KEY_UI_LAF_IMPL_SKINLF_THEMEPACK_PATH = "ui.laf.impl.skinlf.themepack.path";
//	/**
//	 * obsolete  
//	 */
//	public final static String KEY_UI_LAF_IMPL_SKINLF_THEMEPACK = 
//	"ui.laf.impl.skinlf.themepack";
//	/**
//	 * obsolete  
//	 */
//	public final static String KEY_UI_LAF_IMPL_SKINLF_THEMEPACK_INDEX = 
//	"ui.laf.impl.skinlf.themepack.index";
//	/**
//	 * obsolete  
//	 */
//	public final static String KEY_UI_LAF_IMPL_SKINLF_THEMEPACK_BASE = 
//	"ui.laf.impl.skinlf.themepack.";

	/** Key-base for list of available look and feels */
	public final static String 
	KEY_UI_LAF_LISTAVAIL_BASE = "ui.laf.listavail."; 
	/** Key-base for list of usable look and feels */
	public final static String 
	KEY_UI_LAF_LISTUSE_BASE = "ui.laf.listuse.";
	/** Key-base for list of available themepacks (append LaF!) */
	public final static String 
	KEY_UI_LAF_SKIN_LISTAVAIL_BASE = "ui.laf.skin.listavail."; 
	/** Key-base for list of usable themepacks (append LaF!) */
	public final static String 
	KEY_UI_LAF_SKIN_LISTUSE_BASE = "ui.laf.skin.listuse.";
	/** Key for flag: fill the use lists if they are empty on startup */
	public final static String 
	KEY_UI_LAF_LISTUSEFILLIFEMPTY = "ui.laf.listusefillifempty"; 

	/** Key-base for list of to be loaded jar's */
	public final static String 
	KEY_BASE_CLASSPATH_JAR = "main.classpath.jar."; 
	/** Key for directory to look for jar's */
	public final static String 
	KEY_CLASSPATH_DIRS = "main.classpath.dirs"; 
	/** Key for flag: use class path extender or not */
	public final static String 
	KEY_CLASSPATH_USE_EXTENDER = "main.classpath.useextender"; 

	/** Value for CrossPlatform LaF (the java LaF) */
	public final static String 
	UI_LAF_STD_XPLATFORM_NAME = "Java";
	/** Value for system LaF */
	public final static String 
	UI_LAF_STD_SYSTEM_NAME = "System";
	/** Value for Windows LaF */
	public final static String 
	UI_LAF_STD_WINDOWS_NAME = "Windows";
	/** Value for GTK LaF  */
	public final static String 
	UI_LAF_STD_GTK_NAME = "GTK";
	/** Value for Motif LaF  */
	public final static String 
	UI_LAF_STD_MOTIF_NAME = "Motif";

	/** Value for windows LaF class name  */
	public final static String 
	UI_LAF_STD_WINDOWS_CLASS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	/** Value for GTK LaF class name  */
	public final static String 
	UI_LAF_STD_GTK_CLASS = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	/** Value for Motif LaF class name  */
	public final static String 
	UI_LAF_STD_MOTIF_CLASS = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";

	/** Value for SkinLF LaF */
	public final static String 
	UI_LAF_IMPL_SKINLF_NAME = "SkinLF";
	/** Value for SkinLF LaF class name  */
	public final static String 
	UI_LAF_IMPL_SKINLF_CLASS = "com.l2fprod.gui.plaf.skin.SkinLookAndFeel";
	/** Value for SkinLF LaF-Manager implementation class name  */
	public final static String 
	UI_LAF_IMPL_SKINLF_IMPLCLASS = "de.admadic.laf.LaFSkinLF";

	/** Value for Liquid LaF */
	public final static String 
	UI_LAF_IMPL_BIROSOFT_LIQUID_NAME = "Birosoft Liquid";
	/** Value for Liquid LaF class name */
	public final static String 
	UI_LAF_IMPL_BIROSOFT_LIQUID_CLASS = "com.birosoft.liquid.LiquidLookAndFeel";
	/** Value for Liquid LaF-Manager implementation class name */
	public final static String 
	UI_LAF_IMPL_BIROSOFT_LIQUID_IMPLCLASS = "de.admadic.laf.LaFBirosoftLiquid";

	/** Value for Kunststoff LaF */
	public final static String 
	UI_LAF_IMPL_KUNSTSTOFF_NAME = "Kunststoff";
	/** Value for Kunststoff LaF class name */
	public final static String 
	UI_LAF_IMPL_KUNSTSTOFF_CLASS = "com.incors.plaf.kunststoff.KunststoffLookAndFeel";
//	/**
//	 * Value for Kunststoff LaF-Manager implementation class name  
//	 */
//	public final static String UI_LAF_IMPL_KUNSTSTOFF_IMPLCLASS = "de.admadic.calculator.ui.LaFKunststoff";

	/** Value for Metouia LaF */
	public final static String 
	UI_LAF_IMPL_METOUIA_NAME = "Metouia";
	/** Value for Kunststoff LaF class name */
	public final static String 
	UI_LAF_IMPL_METOUIA_CLASS = "net.sourceforge.mlf.metouia.MetouiaLookAndFeel";
//	/**
//	 * Value for Kunststoff LaF-Manager implementation class name  
//	 */
//	public final static String UI_LAF_IMPL_KUNSTSTOFF_IMPLCLASS = "de.admadic.calculator.ui.LaFKunststoff";


	/** Value for Plastics LaF */
	public final static String 
	UI_LAF_IMPL_JGOODIESPLASTICS_NAME = "JGoodies-Plastics";
	/** Value for Kunststoff LaF class name  */
	public final static String 
	UI_LAF_IMPL_JGOODIESPLASTICS_CLASS = "com.jgoodies.looks.plastic.PlasticLookAndFeel";
//	/**
//	 * Value for Kunststoff LaF-Manager implementation class name  
//	 */
//	public final static String UI_LAF_IMPL_PLASTICS_IMPLCLASS = "de.admadic.calculator.ui.LaFKunststoff";


	/** Value for Plastics LaF */
	public final static String 
	UI_LAF_IMPL_JGOODIESWINDOWS_NAME = "JGoodies-Windows";
	/** Value for Kunststoff LaF class name  */
	public final static String 
	UI_LAF_IMPL_JGOODIESWINDOWS_CLASS = "com.jgoodies.looks.windows.WindowsLookAndFeel";
//	/**
//	 * Value for Kunststoff LaF-Manager implementation class name  
//	 */
//	public final static String UI_LAF_IMPL_PLASTICS_IMPLCLASS = "de.admadic.calculator.ui.LaFKunststoff";

	
	/** Style name for standard buttons  */
	public final static String 
	UI_STYLE_BUTTON_STANDARD = "button.standard";
	/** Style name for small buttons  */
	public final static String 
	UI_STYLE_BUTTON_SMALL = "button.small";
	/** Key-base for CalcCompLayout styles  */
	public final static String 
	KEY_UI_LAYOUT_CCL_STYLE_BASE = "ui.layout.ccl.style.";
	/** Key-base for CalcCompLayout minimum cell definitions  */
	public final static String 
	KEY_UI_LAYOUT_CCL_MINCELL_BASE = "ui.layout.ccl.mincell";
	/** Key for fixed-cell-size flag for CalcCompLayout  */
	public final static String 
	KEY_UI_LAYOUT_CCL_FIXEDCELLSIZE = "ui.layout.ccl.fixedcellsize";

	/** Key for status "always on top" */
	public final static String 
	KEY_UI_MAIN_ALWAYSONTOP = "ui.main.alwaysontop";
	/** Key for status "protocol window on" */
	public final static String 
	KEY_UI_MAIN_PROTOCOLWINDOW_ON = "ui.main.protocolwindow.on";
	/** Key for status "math window on" */
	public final static String 
	KEY_UI_MAIN_MATHWINDOW_ON = "ui.main.mathwindow.on";
	/** Key-base for status values of attached window placers */
	public final static String 
	KEY_UI_MAIN_ATWM_PLACERS = "ui.main.atwm.placers";
	/** Key for flag background enabled */
	public final static String 
	KEY_UI_MAIN_PANEL_BACKGROUND = "ui.main.panel.background";
	/** Key for background file */
	public final static String 
	KEY_UI_MAIN_PANEL_BACKGROUND_FILE = "ui.main.panel.background.file";

	/** Key for flag whether extended settings should be shown */
	public final static String 
	KEY_UI_MAIN_EXTENDEDSETTINGS = "ui.extendedsettings";
	/** Key for flag whether custom fonts from cmd style definition shall be loaded */
	public final static String 
	KEY_UI_BUTTON_CUSTOMFONT = "ui.button.customfont";
	/** Key for flag whether custom gfx from cmd style definition shall be loaded */
	public final static String 
	KEY_UI_BUTTON_LOADGFX = "ui.button.loadgfx";
	/** Key for flag whether buttons paint the focus or not */
	public final static String 
	KEY_UI_BUTTON_PAINTFOCUS = "ui.button.paintfocus";
	/** Key for flag whether window move by dragging the main panel is allowed */
	public final static String 
	KEY_UI_MAIN_PANELMOVE = "ui.main.panelmove";
	/** Key for status of main window position */
	public final static String 
	KEY_UI_MAIN_WINDOW_BOUNDS = "ui.main.window.bounds";
	/** Key for working directory */
	public final static String 
	KEY_UI_MAIN_WORKINGDIR = "ui.main.workingdir";
	/** Key for preloading ui*/
	public final static String 
	KEY_UI_MAIN_PRELOAD_UI = "ui.main.preloadui";
	/** Key for value in title */
	public final static String 
	KEY_UI_MAIN_VALUE_IN_TITLE = "ui.main.valueintitle";
	/** Key for showing text protocol */
	public final static String 
	KEY_UI_MAIN_SHOW_TXT_PROT = "ui.main.showtextprot";
	/** Key for showing tabular protocol */
	public final static String 
	KEY_UI_MAIN_SHOW_TAB_PROT = "ui.main.showtabprot";
	/** Key for flag: skin menu as tree or flat */
	public final static String 
	KEY_UI_MAIN_SKINMENU_FLAT = "ui.main.skinmenu.flat";
	/** Key for number format */
	public final static String 
	KEY_UI_NUMBER_FORMAT = "ui.number.format";
	/** Default value for number format */
	public final static String 
	UI_NUMBER_FORMAT_DEFAULT = "fixed,12,2";
	/** Key for activating snow */
	public final static String 
	KEY_UI_MAIN_GMX_SNOW = "ui.main.gmx.snow";

	/** Key for locale default settings */
	public final static String 
	KEY_UI_MAIN_LOCALE_DEFAULT = "ui.main.locale.default";
	/** Key for locale other same settings */
	public final static String 
	KEY_UI_MAIN_LOCALE_SAMEASDEFAULT = "ui.main.locale.sameasdefault";
	/** Key for locale input settings */
	public final static String 
	KEY_UI_MAIN_LOCALE_INPUT = "ui.main.locale.input";
	/** Key for locale output settings */
	public final static String 
	KEY_UI_MAIN_LOCALE_OUTPUT = "ui.main.locale.output";
	/** Key for locale export settings */
	public final static String 
	KEY_UI_MAIN_LOCALE_EXPORT = "ui.main.locale.export";
	
	/** key-base for list of modules */
	public final static String
	KEY_MODULE_LIST_BASE = "module.list.";

	/** key-base for internal module names */
	public final static String
	KEY_MODULE_INAMES_BASE = "module.inames.";

	/** prefix for all module specific settings */
	public final static String 
	PREFIX_MODULES = "mod.";

	/** infix for all module specific settings which were copied from previous
	 *  versions */
	public final static String 
	INFIX_MODULES_PREV = "prev";
	
	/** Value of application icon resource */
	public final static String 
	RSC_ICON = "de/admadic/calculator/ui/res/icon.jpg";

	/** Value of splash image */
	public final static String 
	RSC_SPLASH_IMAGE = "de/admadic/calculator/ui/res/splash.png";

//	 FIXME: remove these lines upon next code cleaning after thorough testing:
//	/** Version for lic etc. */
//	public final static String
//	UI_APP_VER = Version.version;
////	UI_APP_VER = "0.2.0";
	
//	/**
//	 * The application string for the about dialog 
//	 */
//	public final static String 
//	UI_ABOUT_APP = 
//		"admaDIC Calculator\n"+
//		"Version " + Version.version;
//	/**
//	 * Copyright string for about dialog 
//	 */
//	public final static String 
//	UI_ABOUT_COPY = 
//		"Copyright (c) 2005-2006 - admaDIC\n"+
//		"04179 Leipzig, Germany";
//	/**
//	 * Further information for about dialog 
//	 */
//	public final static String 
//	UI_ABOUT_INF = 
//		"For updates or more information \n"+
//		"please visit http://www.admadic.de/";
//	/**
//	 * External Software Libraries information for about dialog 
//	 */
//	public final static String 
//	UI_ABOUT_SOFTLIBS = 
//		"This product includes software developed by\n" +
//		"L2FProd.com (http://www.L2FProd.com/).\n"+
//		"\n"+
//		"This product includes the software JGoodies Forms:\n"+
//		"Copyright (c) 2002-2004 JGoodies Karsten Lentzsch.\n"+
//		"All rights reserved.";
//	/**
//	 * Title of about dialog 
//	 */
//	public final static String 
//	UI_ABOUT_TITLE = "About admaDIC Calculator";

	/** Name of licensee */
	public final static String 
	KEY_UI_MAIN_LIC_NAME = "ui.main.lic.name";

	/** Company of licensee */
	public final static String 
	KEY_UI_MAIN_LIC_COMPANY = "ui.main.lic.company";

	/** Serial Number of licensee */
	public final static String 
	KEY_UI_MAIN_LIC_SN = "ui.main.lic.sn";

	/** Key for serial number */
	public final static String 
	KEY_UI_SN_STAT_SN = "ui.sns.sn";

	Object [][] stdValues = {
			{KEY_UI_LAF_ENABLE, Boolean.TRUE},
			// standard LaFs:
			{KEY_UI_LAF_LISTAVAIL_BASE + "0", 
				UI_LAF_TYPE_PRIMARY + ITEM_SEPARATOR + 
				UI_LAF_IMPL_SKINLF_NAME + ITEM_SEPARATOR + 
				UI_LAF_IMPL_SKINLF_CLASS + ITEM_SEPARATOR +
				UI_LAF_IMPL_SKINLF_IMPLCLASS},
			// built in:
			{KEY_UI_LAF_LISTAVAIL_BASE + "1", 
				UI_LAF_TYPE_SYSTEM + ITEM_SEPARATOR + 
				UI_LAF_STD_XPLATFORM_NAME + ITEM_SEPARATOR + 
				"<xplatform>"},
			{KEY_UI_LAF_LISTAVAIL_BASE + "2", 
				UI_LAF_TYPE_SYSTEM + ITEM_SEPARATOR + 
				UI_LAF_STD_SYSTEM_NAME + ITEM_SEPARATOR + 
				"<system>"},
			{KEY_UI_LAF_LISTAVAIL_BASE + "3", 
				UI_LAF_TYPE_SYSTEM + ITEM_SEPARATOR + 
				UI_LAF_STD_WINDOWS_NAME + ITEM_SEPARATOR + 
				UI_LAF_STD_WINDOWS_CLASS},
			{KEY_UI_LAF_LISTAVAIL_BASE + "4", 
				UI_LAF_TYPE_SYSTEM + ITEM_SEPARATOR + 
				UI_LAF_STD_GTK_NAME + ITEM_SEPARATOR + 
				UI_LAF_STD_GTK_CLASS},
			{KEY_UI_LAF_LISTAVAIL_BASE + "5", 
				UI_LAF_TYPE_SYSTEM + ITEM_SEPARATOR + 
				UI_LAF_STD_MOTIF_NAME + ITEM_SEPARATOR + 
				UI_LAF_STD_MOTIF_CLASS},
/*
			// other LaFs:
			{KEY_UI_LAF_LISTAVAIL_BASE + "6", 
				UI_LAF_TYPE_EXTRA + ITEM_SEPARATOR + 
				UI_LAF_IMPL_BIROSOFT_LIQUID_NAME + ITEM_SEPARATOR + 
				UI_LAF_IMPL_BIROSOFT_LIQUID_CLASS + ITEM_SEPARATOR +
				UI_LAF_IMPL_BIROSOFT_LIQUID_IMPLCLASS},
			{KEY_UI_LAF_LISTAVAIL_BASE + "7", 
				UI_LAF_TYPE_EXTRA + ITEM_SEPARATOR + 
				UI_LAF_IMPL_KUNSTSTOFF_NAME + ITEM_SEPARATOR + 
				UI_LAF_IMPL_KUNSTSTOFF_CLASS},// + ITEM_SEPARATOR +
				//UI_LAF_IMPL_KUNSTSTOFF_IMPLCLASS},
			{KEY_UI_LAF_LISTAVAIL_BASE + "8", 
				UI_LAF_TYPE_EXTRA + ITEM_SEPARATOR + 
				UI_LAF_IMPL_METOUIA_NAME + ITEM_SEPARATOR + 
				UI_LAF_IMPL_METOUIA_CLASS},// + ITEM_SEPARATOR +
				//UI_LAF_IMPL_METOUIA_IMPLCLASS},
			{KEY_UI_LAF_LISTAVAIL_BASE + "9", 
				UI_LAF_TYPE_EXTRA + ITEM_SEPARATOR + 
				UI_LAF_IMPL_JGOODIESPLASTICS_NAME + ITEM_SEPARATOR + 
				UI_LAF_IMPL_JGOODIESPLASTICS_CLASS},// + ITEM_SEPARATOR +
				//UI_LAF_IMPL_JGOODIESPLASTICS_IMPLCLASS},
			{KEY_UI_LAF_LISTAVAIL_BASE + "10", 
				UI_LAF_TYPE_EXTRA + ITEM_SEPARATOR + 
				UI_LAF_IMPL_JGOODIESWINDOWS_NAME + ITEM_SEPARATOR + 
				UI_LAF_IMPL_JGOODIESWINDOWS_CLASS},// + ITEM_SEPARATOR +
				//UI_LAF_IMPL_JGOODIESPLASTICS_IMPLCLASS},
*/
			{KEY_UI_LAF_LISTUSEFILLIFEMPTY, Boolean.TRUE},
			{KEY_UI_LAF_SELECT, "SkinLF"},

			{KEY_UI_LAF_SKIN_SELECT_BASE + "SkinLF", 
				"admaDIC" + ITEM_SEPARATOR + "admadicthemepack.zip"},

			{KEY_UI_LAF_SKIN_LISTAVAIL_BASE + "SkinLF." + "0", 
				"admaDIC" + ITEM_SEPARATOR + "admadicthemepack.zip"},
			{KEY_UI_LAF_SKIN_LISTAVAIL_BASE + "SkinLF." + "1", 
				"SkinLF" + ITEM_SEPARATOR + "themepack.zip"},
			{KEY_UI_LAF_SKIN_LISTAVAIL_BASE + "SkinLF." + "2", 
				"Xmas" + ITEM_SEPARATOR + "xmasthemepack.zip"},
/*
			{KEY_UI_LAF_SKIN_LISTAVAIL_BASE + "SkinLF." + "2", 
				"TigerGraphite" + ITEM_SEPARATOR + "tigerGraphitethemepack.zip"},
			{KEY_UI_LAF_SKIN_LISTAVAIL_BASE + "SkinLF." + "3", 
				"Tiger" + ITEM_SEPARATOR + "tigerthemepack.zip"},
			{KEY_UI_LAF_SKIN_LISTAVAIL_BASE + "SkinLF." + "4", 
				"Cougar" + ITEM_SEPARATOR + "cougarthemepack.zip"},
			{KEY_UI_LAF_SKIN_LISTAVAIL_BASE + "SkinLF." + "5", 
				"GfxOasis" + ITEM_SEPARATOR + "gfxOasisthemepack.zip"},
			{KEY_UI_LAF_SKIN_LISTAVAIL_BASE + "SkinLF." + "6", 
				"iBar" + ITEM_SEPARATOR + "iBarthemepack.zip"},
			{KEY_UI_LAF_SKIN_LISTAVAIL_BASE + "SkinLF." + "7", 
				"Midnight" + ITEM_SEPARATOR + "midnightthemepack.zip"},
			{KEY_UI_LAF_SKIN_LISTAVAIL_BASE + "SkinLF." + "8", 
				"QuickSilveR" + ITEM_SEPARATOR + "quickSilverRthemepack.zip"},
			{KEY_UI_LAF_SKIN_LISTAVAIL_BASE + "SkinLF." + "9", 
				"Aqua" + ITEM_SEPARATOR + "aquathemepack.zip"},
*/
			{KEY_UI_LAF_IMPL_SKINLF_THEMEPACK_PATH, "./laf/SkinLF/themes/"},
			{KEY_UI_BUTTON_LOADGFX, Boolean.TRUE},	
			{KEY_UI_BUTTON_CUSTOMFONT, Boolean.TRUE},	
			{KEY_UI_BUTTON_PAINTFOCUS, Boolean.TRUE},	
			{KEY_UI_MAIN_PANEL_BACKGROUND, Boolean.FALSE},	
			{KEY_UI_MAIN_PANEL_BACKGROUND_FILE, ""},	
			{KEY_UI_MAIN_ALWAYSONTOP, Boolean.FALSE},
			{KEY_UI_MAIN_PROTOCOLWINDOW_ON, Boolean.TRUE},
			{KEY_UI_MAIN_MATHWINDOW_ON, Boolean.TRUE},
			{KEY_UI_MAIN_PANELMOVE, Boolean.FALSE},	
			{KEY_UI_MAIN_PRELOAD_UI, Boolean.TRUE},	
			{KEY_UI_MAIN_EXTENDEDSETTINGS, Boolean.FALSE},	
			{KEY_UI_MAIN_SKINMENU_FLAT, Boolean.TRUE},
			{KEY_UI_LAYOUT_CCL_FIXEDCELLSIZE, Boolean.TRUE},
			{KEY_UI_LAYOUT_CCL_STYLE_BASE + UI_STYLE_BUTTON_STANDARD + ".width", Integer.valueOf(7)},
			{KEY_UI_LAYOUT_CCL_STYLE_BASE + UI_STYLE_BUTTON_STANDARD + ".height", Integer.valueOf(6)},
			{KEY_UI_LAYOUT_CCL_STYLE_BASE + UI_STYLE_BUTTON_SMALL + ".width", Integer.valueOf(7)},
			{KEY_UI_LAYOUT_CCL_STYLE_BASE + UI_STYLE_BUTTON_SMALL + ".height", Integer.valueOf(5)},
			{KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".width", Integer.valueOf(4)},
			{KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".height", Integer.valueOf(4)},
			{KEY_UI_MAIN_LIC_NAME, ""},
			{KEY_UI_MAIN_LIC_COMPANY, ""},
			{KEY_UI_MAIN_LIC_SN, ""},
			{KEY_UI_SN_STAT_SN, ""},
			//{KEY_BASE_CLASSPATH_JAR + "0", ""},
			{KEY_CLASSPATH_USE_EXTENDER, Boolean.TRUE},
			{KEY_CLASSPATH_DIRS, "./laf/:./lib/:./mod/"},
			{KEY_UI_NUMBER_FORMAT, UI_NUMBER_FORMAT_DEFAULT},
			{KEY_UI_MAIN_GMX_SNOW, Boolean.TRUE},
			{KEY_UI_MAIN_VALUE_IN_TITLE, Boolean.TRUE},
			{KEY_UI_MAIN_SHOW_TAB_PROT, Boolean.TRUE},
			{KEY_UI_MAIN_SHOW_TXT_PROT, Boolean.FALSE},
			//{KEY_MODULE_LIST_BASE + "0", "modulename:moduleclass:true"},
			{KEY_MODULE_LIST_BASE + "0", "Matrx:de.admadic.calculator.modules.matrx.MatrxModule:true"},
			{KEY_MODULE_LIST_BASE + "1", "MaSCa:de.admadic.calculator.modules.masca.MaSCaModule:true"},
			{KEY_MODULE_LIST_BASE + "2", "InDXp:de.admadic.calculator.modules.indxp.InDXpModule:true"},
			{KEY_UI_MAIN_LOCALE_DEFAULT, "en" + ITEM_SEPARATOR + "US"},
			{KEY_UI_MAIN_LOCALE_SAMEASDEFAULT, Boolean.TRUE},
			{KEY_UI_MAIN_LOCALE_INPUT, "en" + ITEM_SEPARATOR + "US"},
			{KEY_UI_MAIN_LOCALE_OUTPUT, "en" + ITEM_SEPARATOR + "US"},
			{KEY_UI_MAIN_LOCALE_EXPORT, "en" + ITEM_SEPARATOR + "US"},
	};
	

	Properties props;
	String cfgPath;
	String cfgFile;

	String cfgFullName;

	/**
	 * Constructs an instance of CfgCalc.
	 * CfGCalc(true) is called 
	 */
	public CfgCalc() {
		// a Logger cannot be used yet.
		super();
		{
			String tmp = System.getProperty("admadic.debug");
			if (tmp!=null && tmp.toLowerCase().equals("yes")) {
				DBG = true;
			}
		}
		props = new Properties();
	}

	protected void error(String msg) {
		error(msg, true);
	}

	protected void error(String msg, boolean gui) {
		// a Logger cannot always be used, therefore a plain screen output 
		// needs to be done.
		System.err.println("Calculator: " + msg);
		if (gui) {
			JOptionPane.showMessageDialog(
					null,
					msg,
					"Configuration Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @param pm 
	 * 
	 */
	public void initCfgPaths(PathManager pm) {
		File file = null;
		FileInputStream fin;

		if (DBG) System.out.println("CfgCalc: initCfgPaths");
		try {
			file = new File(
					pm.getPathString(PathManager.SYS_CFG_DIR) + 
					"/cfg.cfg");
			fin = new FileInputStream(file);
			props.load(fin);
		} catch (FileNotFoundException e) {
			// FIXME: make that a real error!
			if (DBG) e.printStackTrace();

			// Not in file system. Try jar file:
			InputStream ins = this.getClass().getResourceAsStream("/cfg/cfg.cfg");
			try {
				props.load(ins);
			} catch (IOException ex) {
				if (DBG) ex.printStackTrace();
				error(
						"The main cfg file could not be found! (looking for cfg.cfg)\n"+
								"sys-cfg-dir = " + pm.getPathString(PathManager.SYS_CFG_DIR));
			}
		} catch (IOException e) {
			if (DBG) e.printStackTrace();
			error("a cfg file exists, but could not be read! (" + 
					((file!=null) ? file.toString() : "<null>") + ")");
		}

		cfgPath = props.getProperty("de.admadic.calculator.ui.CfgCalc.cfgpath");
		cfgFile = props.getProperty("de.admadic.calculator.ui.CfgCalc.cfgfile");

		if (cfgPath==null) {
			cfgPath = pm.getPathString(PathManager.USR_CFG_DIR);
		}

		// FIXME: we may not need that...
//		FileUtil.copyFile(
//				pm.getPathString(PathManager.SYS_CFG_DIR) + "/cfg.dtd",
//				pm.getPathString(PathManager.USR_CFG_DIR) + "/cfg.dtd");

		file = PathManager.expandFilename(cfgPath);
		cfgPath = file.toString();
		file = new File(file, cfgFile);

		cfgFullName = file.toString();

//		System.out.println("cfgfile = " + cfgFullName);
	}

	/**
	 * @return	Returns true, if the cfg file exists, false otherwise.
	 */
	public boolean existsCfgFile() {
		File f = new File(cfgFullName);
//		System.out.println("file " + f + " exists?: " + f.exists());
		return f.exists();
	}

	/**
	 * @param src
	 * @return Returns true, if successfully copied
	 */
	public boolean copyCfgFile(String src) {
		return FileUtil.copyTextFile(src, cfgFullName);
	}

	protected void tryPath() {
		File dir = new File(cfgPath);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				error("Could not create cfg path (" + cfgPath + ")");
				// FIXME: maybe make that an error
			}
		}
	}

	/**
	 * The standard preferences declared in this class are installed.
	 * If loadPref is true, the preferences are loaded from preferences space.
	 * The preferences space is initialized to work on base of XML storage.
	 * 
	 * @param loadPref	a boolean indicating whether to load preferences
	 * 					or not 
	 */
	public void initialize(boolean loadPref) {
		initStandard();

//		// make it work with Preferences classes:
//		registerPersistanceBackend(new CfgPersistencePref(this));
		// (the Preferences classes create awfully named directories in
		// Unix which is impossible to maintain and support for end customers)

		// lets use XML:
		registerPersistanceBackend(
				new CfgPersistenceXML(this, cfgFullName));

		if (loadPref) {
			loadPreferences(CfgCalc.PREFERENCES_PATH);
		}

		postLoadFixup();
	}

	/**
	 * @param cfgname
	 * @return	Returns a Cfg instance with registered backend.
	 */
	public static Cfg createTmpCfg(String cfgname) {
		Cfg tmpCfg = new Cfg();
		tmpCfg.registerPersistanceBackend(
				new CfgPersistenceXML(tmpCfg, cfgname));
		return tmpCfg;
	}
	
	/**
	 * Initializes the standard settings defined in the source code of this 
	 * class. 
	 */
	public void initStandard() {
		String key;
		for (int i=0; i<stdValues.length; i++) {
			if (stdValues[i].length!=2) {
				error(
						"stdCfgEntry x[" + i + "] does not have 2 elements.",
						false); // no gui error
			} else {
				key = (String)stdValues[i][0]; 
				getDefaultCfg().putValue(key, stdValues[i][1]);
			}
		}
		getDefaultCfg().getCfgItem(KEY_UI_MAIN_LIC_NAME).setCiFlags(
				CfgItem.FLG_NORESET, true);
		getDefaultCfg().getCfgItem(KEY_UI_MAIN_LIC_COMPANY).setCiFlags(
				CfgItem.FLG_NORESET, true);
		getDefaultCfg().getCfgItem(KEY_UI_MAIN_LIC_SN).setCiFlags(
				CfgItem.FLG_NORESET, true);
		getDefaultCfg().getCfgItem(KEY_UI_SN_STAT_SN).setCiFlags(
				CfgItem.FLG_NORESET, true);

		setDefaults();
	}


	/**
	 * Fixes some settings in case they are not present or inconsistent.
	 * <p>
	 * Especially the different LISTUSE settings are initialized,
	 * if they are not present and the LISTUSEFILLIFEMPTY is enabled. 
	 */
	public void postLoadFixup() {
		if (getBooleanValue(KEY_UI_LAF_LISTUSEFILLIFEMPTY, true)) {
			if (!entries.containsKey(KEY_UI_LAF_LISTUSE_BASE+"0")) {
				Object [] values;
				Integer [] indices;
				values = getValueArray(KEY_UI_LAF_LISTAVAIL_BASE);
				indices = new Integer[values.length];
				for (int i = 0; i < indices.length; i++) {
					indices[i] = new Integer(i);
				}
				putValueArray(KEY_UI_LAF_LISTUSE_BASE, indices);
			} // if not laf_listuse
			{
				Object [] lafs = getValueArray(KEY_UI_LAF_LISTAVAIL_BASE);
				String [] lafInfo;
				String lafName;
				for (Object object : lafs) {
					if (!(object instanceof String)) continue;
					lafInfo = ((String)object).split(ITEM_SEPARATOR_STR);
					if (lafInfo.length<2) continue;
					lafName = lafInfo[1];
					if (entries.containsKey(KEY_UI_LAF_SKIN_LISTAVAIL_BASE+lafName+".0")) {
						// check LISTUSE:
						if (entries.containsKey(KEY_UI_LAF_SKIN_LISTUSE_BASE+lafName+".0")) {
							continue;
						}
						// now we have to copy it
						Object [] values;
						Integer [] indices;
						values = getValueArray(
								KEY_UI_LAF_SKIN_LISTAVAIL_BASE + lafName + ".");
						indices = new Integer[values.length];
						for (int i = 0; i < indices.length; i++) {
							indices[i] = new Integer(i);
						}
						putValueArray(
								KEY_UI_LAF_SKIN_LISTUSE_BASE + lafName + ".", 
								indices);
					} // if has avail laf-skins
				}
			} // check skins for each laf
		} // if fillifempty
	} // (postLoadFixup)
}
