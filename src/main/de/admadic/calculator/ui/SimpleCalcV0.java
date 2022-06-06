/**
 *
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
 */
package de.admadic.calculator.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import de.admadic.calculator.Version;
import de.admadic.calculator.appctx.AppContext;
import de.admadic.calculator.appctx.AppEvent;
import de.admadic.calculator.appctx.AppEventListener;
import de.admadic.calculator.appctx.CancelPhaseException;
import de.admadic.calculator.appmod.IModule;
import de.admadic.calculator.appmod.ModuleSpec;
import de.admadic.calculator.core.LocaleConstants;
import de.admadic.calculator.processor.ProcessorAction;
import de.admadic.calculator.processor.ProcessorEvent;
import de.admadic.calculator.processor.ProcessorEventListener;
import de.admadic.calculator.processor.ProcessorException;
import de.admadic.calculator.processor.ProcessorUIActionFactory;
import de.admadic.calculator.processor.SimpleProcessor;
import de.admadic.calculator.types.CaDouble;
import de.admadic.calculator.types.CaDoubleFormat;
import de.admadic.calculator.types.CaDoubleFormatter;
import de.admadic.calculator.types.CaNumberFormatterContext;
import de.admadic.calculator.ui.settings.SettingsDialog;
import de.admadic.cfg.Cfg;
import de.admadic.laf.LaFChangeEvent;
import de.admadic.laf.LaFChangeListener;
import de.admadic.laf.LaFManager;
import de.admadic.ui.gmx.SnowConstants;
import de.admadic.ui.gmx.SnowEngine;
import de.admadic.ui.util.AboutDialog;
import de.admadic.ui.util.Dialog;
import de.admadic.ui.util.StringPrinter;
import de.admadic.ui.util.SyncButtonGroup;
import de.admadic.util.PathManager;
import de.admadic.util.VersionRecord;

/**
 * Application class for admaDIC calculator.
 * 
 * @author Rainer Schwarze
 */
public class SimpleCalcV0 
		extends javax.swing.JFrame 
		implements ComponentListener, 
		           ProcessorEventListener, AppEventListener {

	final static boolean DBGforce = false;
	static boolean DBGtesting = false;
	final static boolean LOG = true;
	Logger logger = null;

	/**
	 * The application string for the about dialog 
	 */
	final static String 
	UI_ABOUT_APP = 
		"admaDIC Calculator\n"+
		"Version " + Version.version;
	/**
	 * Copyright string for about dialog 
	 */
	final static String 
	UI_ABOUT_COPY = 
		"Copyright (c) 2005-2022 - admaDIC\n"+
		"Germany";
	/**
	 * Further information for about dialog 
	 */
	final static String 
	UI_ABOUT_INF = 
		"For updates or more information \n"+
		"please visit http://www.admadic.de/";

	/**
	 * External Software Libraries information for about dialog 
	 */
	final static String 
	UI_ABOUT_SOFTLIBS = 
		"This product includes software developed by\n" +
		"L2FProd.com (http://www.L2FProd.com/).\n"+
		"\n"+
		"This product includes the software JGoodies Forms:\n"+
		"Copyright (c) 2002-2004 JGoodies Karsten Lentzsch.\n"+
		"All rights reserved.";

	/**
	 * Title of about dialog 
	 */
	final static String 
	UI_ABOUT_TITLE = "About admaDIC Calculator";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	class ClipboardHandler implements ClipboardOwner {
		/**
		 * @param arg0
		 * @param arg1
		 * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
		 */
		public void lostOwnership(Clipboard arg0, Transferable arg1) {
			// nothing - we don't care about it
		}
	}


	/**
	 * The configuration instance
	 */
	Cfg cfg = null;

	/**
	 * The Look-and-Feel-Manager for this application
	 */
	LaFManager lafMan = null;

	/**
	 * The path manager for this instance:
	 */
	PathManager pathMan = null;

	AppContext appContext = null;
	
	/**
	 * The actual processor for the calculator
	 */
	SimpleProcessor calcProc = null;
	CmdSetSimple commandSet = null;
	CalcCompStyles styles = null;
	ClipboardHandler clipboardHandler = null;
	AttachedWindowManager atwpl = null;

	File lastCurDir = null;

	JPopupMenu popupMenu;

	SyncButtonGroup btnGrpProtocolWindow;
	SyncButtonGroup btnGrpMathWindow;
	SyncButtonGroup btnGrpStackWindow;
	SyncButtonGroup btnGrpAlwaysOnTop;
	SyncButtonGroup btnGrpMotionLockProtTxt;
	SyncButtonGroup btnGrpMotionLockProtTab;
	SyncButtonGroup btnGrpTestDialog;
	ButtonGroup btnGrpAngularArgMode;
	CalcToggleButton btnAngArgModRad;
	CalcToggleButton btnAngArgModDeg;
	CalcToggleButton btnAngArgModGra;

	LaFTestDialog lafTestDialog;
	
	CalcPanel panelCalc;
	JTextField/*Pane*/ textDisplay;
	JTextPane textStatus;
	CalcDisplay display;
	CalcDisplayExt displayCombo;
	CalcPanel panelLog;
	CalcPanel panelLogButtons;
	CalcPanel panelLogProtocol;
	CalcPanel panelMath;
	CalcPanel panelStack;
	CalcProtocol protocol;
	CalcPanel panelLogTab;
	CalcPanel panelLogProtocolTab;
	CalcProtocolTabular protocolTabular;

	JList listStack;
	
	JMenu skinMenu;

	Hashtable<String,CalcButton> cmd2btn = null;
	CalcCompLayout panelCalcLayout;
	CalcCompLayout panelMathLayout;
	CalcCompLayout panelLogButtonsLayout;
	
	AttachedWindow windowLogTab;
	AttachedWindow windowLog;
	AttachedWindow windowMath;
	AttachedWindow windowStack;

	SnowEngine snowEngine;
	
	ActionListener commandActionListener = null;

	JFileChooser cached_FileChooser;
	SettingsDialog cached_SettingsDialog;

	CaNumberFormatterContext numberFormatterContext;

	String appTitle;
	boolean valueInTitle;

	boolean useTxtProtocol;
	boolean useTabProtocol;
	
	/**
	 * Constructs an instance of the calculator application.
	 * The AppContext is passed to this constructor.
	 * The AppContext contains information about Cfg, PathManager and
	 * LaFManager.
	 * The LaFManager can be null.
	 * Cfg must not be null.
	 * 
	 * @param appContext 
	 */
	public SimpleCalcV0(AppContext appContext) {
		super();
		initLogging();
		this.appContext = appContext;
		this.cfg = appContext.getCfg();
		this.lafMan = appContext.getLafManager();
		this.pathMan = appContext.getPathManager();
		{
			String tmp;
			tmp = System.getProperty("admadic.testing");
			if (tmp!=null && tmp.toLowerCase().equals("yes")) {
				DBGtesting = true;
			}
		}
		this.appContext.addAppListener(this);
	}

	/**
	 * 
	 */
	public void initLogging() {
		if (LOG) logger = (LOG) ? Logger.getLogger("de.admadic") : null;
	}

	/**
	 * Completely start the application.
	 * (Call this method after the constructor and the app is running.)
	 */
	public void start() {
//		PHASE_INIT_CORE = 1;
//		PHASE_INIT_FRAME = 2;
//		PHASE_SHOW_FRAME = 3;
//		PHASE_SHOW_ALL = 4;
//		PHASE_RUN = 5;

		doPhaseFindModules();

		doPhaseBuildCore();
		appContext.fireAppEventNoExc(AppEvent.PHASE_INIT_CORE);
		doPhaseBuildGUI();
		appContext.fireAppEventNoExc(AppEvent.PHASE_INIT_FRAME);
		doPhasePlace();
		// no PHASE? for place?
        if (appContext.getSplashWindow()!=null) {
        	appContext.getSplashWindow().updateStatus("Done.", 100);
        }
		appContext.fireAppEventNoExc(AppEvent.PHASE_SHOW_FRAME);
		doPhaseShow();
		appContext.fireAppEventNoExc(AppEvent.PHASE_SHOW_ALL);
		// ...
		appContext.fireAppEventNoExc(AppEvent.PHASE_RUN);
	}

	private void doPhaseFindModules() {
		Object [] modules;
		String [] sa;
		modules = cfg.getValueArray(CfgCalc.KEY_MODULE_LIST_BASE);

		for (int i = 0; i < modules.length; i++) {
			sa = ((String)modules[i]).split(CfgCalc.ITEM_SEPARATOR_STR);
			ModuleSpec ms;
			boolean b = Boolean.parseBoolean(sa[2]);
			ms = new ModuleSpec(sa[0], sa[1], b);
			appContext.getModuleManager().add(ms);
		}
		Vector<ModuleSpec> failedMS = appContext.getModuleManager()
			.validateVersionRequirements(VersionRecord.valueOf(Version.version));
		if (failedMS!=null) {
			String s = null;
			for (ModuleSpec spec : failedMS) {
				String t = spec.getName() + " requires calculator version " +
					spec.getRequiredAppVersion().getMjMnMcRvVersionString();
				if (s==null) {
					s = t;
				} else {
					s += "\n" + t;
				}
			}
			JOptionPane.showMessageDialog(
					this,
					"The following modules have been disabled, because"+
					" they require a newer version of the Calculator:\n"+
					s,
					"Modules requiring newer version of Calculator",
					JOptionPane.ERROR_MESSAGE);
		}

		appContext.getModuleManager().collectDescriptions();
		appContext.getModuleManager().initializeModules(appContext);
		// tag all modules in cfg space:
		for (int i=0; i<appContext.getModuleManager().getModuleCount(); i++) {
			IModule im = appContext.getModuleManager().getModule(i);
			cfg.putBooleanValue(
					CfgCalc.KEY_MODULE_INAMES_BASE + 
					im.getModuleDesc().getCfgName(),
					true);
		}
	}

	/**
	 * @param ae
	 * @throws CancelPhaseException
	 * @see de.admadic.calculator.appctx.AppEventListener#processPhase(de.admadic.calculator.appctx.AppEvent)
	 */
	public void processPhase(AppEvent ae) throws CancelPhaseException {
//		System.out.println("AppEvent: " + AppEvent.phaseToString(ae.getPhase()));
	}

	private void initGUI() {
		try {
//			KeyboardFocusManager.getCurrentKeyboardFocusManager()
//				.addKeyEventDispatcher(this);
			commandActionListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					processActionCommand(e.getActionCommand());
				}
			};

			// lets create the numberFormatterContext here:
			numberFormatterContext = new CaNumberFormatterContext();
			String fmt = cfg.getStringValue(
					CfgCalc.KEY_UI_NUMBER_FORMAT, 
					CfgCalc.UI_NUMBER_FORMAT_DEFAULT);
			CaDoubleFormatter cdf = CaDoubleFormatter.valueOf(fmt);
			numberFormatterContext.put(CaDouble.class, cdf);
			

			this.setResizable(false);
			// this.setTitle("admaDIC Calculator");
			appTitle = "admaDIC Calculator";
			setValueInTitle(cfg.getBooleanValue(
					CfgCalc.KEY_UI_MAIN_VALUE_IN_TITLE, true));
			setUseTxtProtocol(cfg.getBooleanValue(
					CfgCalc.KEY_UI_MAIN_SHOW_TXT_PROT, false));
			setUseTabProtocol(cfg.getBooleanValue(
					CfgCalc.KEY_UI_MAIN_SHOW_TAB_PROT, true));
			
			this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//			this.setUndecorated(
//					cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_UNDECORATED, false));
			initGUICalcCompStyles();
			initGUIIcons();
			initGUIMenu();
			initGUIPanelCalc();
			initGUIPanelLog();
			initGUIPanelLogTab();
			initGUIPanelMath();
			initGUIPanelStack();
		    this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					doPhaseExit();
				}
			});
			this.pack();
			this.setLocationRelativeTo(null);
		    this.addComponentListener(this);
		    // fixup protocol pin:
		    if (windowLogTab!=null) {
		    	updateCalcComp(
		    			protocolTabular.getMotionLockButton(), 
		    			"atw.pin.prottab");
		    }
			{
				windowLog.setDefaultCloseOperation(
						WindowConstants.DO_NOTHING_ON_CLOSE);
				windowLog.addWindowListener(new WindowAdapter() {
					/**
					 * @param e
					 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
					 */
					@Override
					public void windowClosing(WindowEvent e) {
						super.windowClosing(e);
						btnGrpProtocolWindow.setSelected(false);
					}
				});
			}
			{
				windowLogTab.setDefaultCloseOperation(
						WindowConstants.DO_NOTHING_ON_CLOSE);
				windowLogTab.addWindowListener(new WindowAdapter() {
					/**
					 * @param e
					 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
					 */
					@Override
					public void windowClosing(WindowEvent e) {
						super.windowClosing(e);
						btnGrpProtocolWindow.setSelected(false);
					}
				});
			}
			{
				windowMath.setDefaultCloseOperation(
						WindowConstants.DO_NOTHING_ON_CLOSE);
				windowMath.addWindowListener(new WindowAdapter() {
					/**
					 * @param e
					 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
					 */
					@Override
					public void windowClosing(WindowEvent e) {
						super.windowClosing(e);
						btnGrpMathWindow.setSelected(false);
					}
				});
			}
			{
				windowStack.setDefaultCloseOperation(
						WindowConstants.DO_NOTHING_ON_CLOSE);
				windowStack.addWindowListener(new WindowAdapter() {
					/**
					 * @param e
					 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
					 */
					@Override
					public void windowClosing(WindowEvent e) {
						super.windowClosing(e);
						btnGrpStackWindow.setSelected(false);
					}
				});
			}
			{
				boolean aot = cfg.getBooleanValue(
						CfgCalc.KEY_UI_MAIN_ALWAYSONTOP, false);
				btnGrpAlwaysOnTop.setSelected(aot);
			}
			if (DBGtesting) {
				btnGrpTestDialog = new SyncButtonGroup();
				lafTestDialog = new LaFTestDialog(SimpleCalcV0.this);
				//dialog.setVisible(true);
				lafMan.addComponentToHead(lafTestDialog);
				btnGrpTestDialog.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						boolean state = (e.getStateChange()==ItemEvent.SELECTED);
						if (DBGtesting) {
							lafTestDialog.setVisible(state);
						}
					}
				});
				lafTestDialog.setDefaultCloseOperation(
						WindowConstants.DO_NOTHING_ON_CLOSE);
				lafTestDialog.addWindowListener(new WindowAdapter() {
					/**
					 * @param e
					 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
					 */
					@Override
					public void windowClosing(WindowEvent e) {
						super.windowClosing(e);
						btnGrpTestDialog.setSelected(false);
					}
				});

			}
			// FIXME: decouple placers:
			atwpl.setPlacerCfgPath(CfgCalc.KEY_UI_MAIN_ATWM_PLACERS);
			//atwpl.registerConfigurationClients(); 
			//cfg.loadObjectsFromPreferences();

			popupMenu.pack();
		
			if (lafMan!=null) { 
				lafMan.addComponent(windowLog);
				lafMan.addComponent(windowLogTab);
				lafMan.addComponent(windowMath);
				lafMan.addComponent(windowStack);
				lafMan.addComponent(popupMenu);
				lafMan.addComponent(this);
			}
			initKeyCommands();
			initGUIPreload();
		} catch (Exception e) {
			// e.printStackTrace();
			// FIXME: make that a nice error?
		}
	}

	/**
	 * Process the core build phase.
	 */
	public void doPhaseBuildCore() {
		configureLocaleManager();
		calcProc = new SimpleProcessor();
		calcProc.setLocaleProvider(appContext.getLocaleManager());
		CaDoubleFormat numFmt;
		String tmp = cfg.getStringValue(
				CfgCalc.KEY_UI_NUMBER_FORMAT,
				CfgCalc.UI_NUMBER_FORMAT_DEFAULT);
		numFmt = CaDoubleFormat.valueOf(tmp);
		calcProc.setNumberFormat(numFmt);

		calcProc.addProcessorListener(this);
		commandSet = new CmdSetSimple(this);
		styles = new CalcCompStyles();
		clipboardHandler = new ClipboardHandler();
		cmd2btn = new Hashtable<String,CalcButton>();
		atwpl = new AttachedWindowManager(this, this.cfg);
	}

	protected void configureLocaleManager() {
		int [] ids = {
				LocaleConstants.LOCALE_DEFAULT,
				LocaleConstants.LOCALE_INPUT,
				LocaleConstants.LOCALE_OUTPUT,
				LocaleConstants.LOCALE_EXPORT,
		};
		String [] keys = {
				CfgCalc.KEY_UI_MAIN_LOCALE_DEFAULT,
				CfgCalc.KEY_UI_MAIN_LOCALE_INPUT,
				CfgCalc.KEY_UI_MAIN_LOCALE_OUTPUT,
				CfgCalc.KEY_UI_MAIN_LOCALE_EXPORT,
		};
		String s, la, co;
		String [] sa;

		for (int i=0; i<ids.length; i++) {
			s = cfg.getStringValue(keys[i], null);
			if (s==null) continue;
			sa = s.split(CfgCalc.ITEM_SEPARATOR_STR);
			la = "";
			co = "";
			if (sa.length>0) la = sa[0];
			if (sa.length>1) co = sa[1];
			appContext.getLocaleManager().setLocale(
					ids[i], new Locale(la, co));
		}

		boolean isSame = cfg.getBooleanValue(
				CfgCalc.KEY_UI_MAIN_LOCALE_SAMEASDEFAULT, true);
		appContext.getLocaleManager().setSameAsDefault(isSame);

		// set environments locale:
		Locale defLoc = appContext.getLocaleManager().getDefaultLocale();
		if (defLoc!=null) {
			if (logger!=null) 
				logger.config("setting default locale " + defLoc.toString());
			Locale.setDefault(defLoc);
		}
		appContext.getLocaleManager().notifyLocaleNumberFormatChanged();
	}


	/**
	 * Process the GUI build phase.
	 */
	public void doPhaseBuildGUI() {
		initGUI();
		calcProc.fireAll(); // FIXME: <- make that better
	}

	/**
	 * Process the placement phase.
	 */
	public void doPhasePlace() {
	    {
	    	Rectangle r = cfg.getRectangleValue(CfgCalc.KEY_UI_MAIN_WINDOW_BOUNDS, null);
	    	if (r!=null) {
	    		//System.out.println("rect = " + r.toString());
	    		this.setLocation(r.getLocation());
	    	} else {
	    		this.setLocationRelativeTo(null);
	    	}
	    }

	    // that's primarily positioning:
		atwpl.loadSettings();
		{
			// fixup motion lock:
			boolean state = atwpl.isMotionLock(windowLog);
			btnGrpMotionLockProtTxt.setSelected(state);
		}
		{
			// fixup motion lock:
			boolean state = atwpl.isMotionLock(windowLogTab);
			btnGrpMotionLockProtTab.setSelected(state);
		}
		atwpl.placeWindows(true);
	}

	/**
	 * Process the show phase.
	 */
	public void doPhaseShow() {
		boolean vis;

		setVisible(true);

		atwpl.setEnableNotifications(true);

		vis = cfg.getBooleanValue(
				CfgCalc.KEY_UI_MAIN_PROTOCOLWINDOW_ON, false);
		btnGrpProtocolWindow.setSelected(vis);

		vis = cfg.getBooleanValue(
				CfgCalc.KEY_UI_MAIN_MATHWINDOW_ON, false);
		btnGrpMathWindow.setSelected(vis);

		if (cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_GMX_SNOW, true)) {
			installSnow(true);
		}
	}

	protected void installSnow(boolean state) {
		if (state) {
			try {
				if (lafMan.getSelectedLaFSkin()
						.getDataName().toLowerCase()
						.equals("xmasthemepack.zip")) {
					snowEngine = SnowEngine.createInFirstScrollpane(
							protocol, SnowConstants.REST_SOUTH);
				}
			} catch (NullPointerException e) {
				// something did not work. dont activate it.
			}
		} else {
			if (snowEngine!=null) {
				SnowEngine.removeFromFirstScrollpane(protocol);
			}
		}
	}

	void doPhaseExit() {
		//atwpl.storeSettings();
		try {
			appContext.fireAppEvent(AppEvent.PHASE_TRY_EXIT);
		} catch (CancelPhaseException e) {
			// someone vetoed.
			return;
		}
		cfg.storeObjectsToPreferences();
		cfg.storePreferences(CfgCalc.PREFERENCES_PATH);
		appContext.fireAppEventNoExc(AppEvent.PHASE_EXIT);
		System.exit(0);
	}
	
	private void initGUIIcons() {
		java.net.URL url = this.getClass().getClassLoader().getResource(
				CfgCalc.RSC_ICON);
		Image img = Toolkit.getDefaultToolkit().getImage(url);
		setIconImage( img );
	}

	private void initGUIMenu() {
		String [][] menuSpec = {
				{"Copy (Display)", "mnu.cpy.display", "ctrl", "C"},	
				{"Copy (Log)", "mnu.cpy.log", null, null},
				// FIXME: activate Pasting
				//{"Paste", "mnu.pst", null, null},	
				{"<separator>", null, null, null},	
				{"Save Log As...", "mnu.sav.log", "ctrl", "S"},
				{"<separator>", null, null, null},	
				{"Settings...", "mnu.settings", null, null},	
				{"About...", "mnu.about", null, null},	
				{"<separator>", null, null, null},
				{"Skins", "mnu.skins", null, null},	
				{"Modules", "mnu.modules", null, null},	
				{"<separator>", null, null, null},	
				{"Exit", "mnu.exit", null, null},	
				{"View Stack", "mnu.viewstack", null, null},	
				{"Testdialog", "mnu.testdlg", null, null},	
		};

		JComponent menu;
		popupMenu = new JPopupMenu();
		menu = popupMenu;
		for (int i = 0; i < menuSpec.length; i++) {
			String name, cmd;
			name = menuSpec[i][0];
			if (name.equals("<separator>")) {
				menu.add(new JSeparator());
			} else {
				if (name.equals("Testdialog") && !DBGtesting) continue;
				if (name.equals("View Stack") && !DBGtesting) continue;

				cmd = menuSpec[i][1];
				if (cmd.equals("mnu.skins")) {
					// make skin submenu
					if (lafMan!=null) {
						skinMenu = new JMenu();
						menu.add(skinMenu);
						skinMenu.setText(name);
						boolean flat;
						flat = cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_SKINMENU_FLAT, true);
						lafMan.createLaFSkinMenu(skinMenu, !flat);
						lafMan.addLaFChangeListener(new LaFChangeListener() {
							public void lafChanged(LaFChangeEvent e) {
								atwpl.setEnableNotifications(true);
								atwpl.placeWindows(true);
								String lafName, skinName;
								lafName = e.getLafName();
								skinName = e.getSkinName();
								if (skinName==null) skinName = "";
								cfg.putStringValue(CfgCalc.KEY_UI_LAF_SELECT, lafName);
								cfg.putStringValue(CfgCalc.KEY_UI_LAF_SKIN_SELECT_BASE + lafName, skinName);
							}

							public void lafChangeBegin(LaFChangeEvent e) {
								atwpl.setEnableNotifications(false);
								if (snowEngine!=null) {
									installSnow(false);
								}
							}

							public void lafChangeFailed(LaFChangeEvent e) {
								atwpl.setEnableNotifications(true);
								atwpl.placeWindows(true);
							}

							public void lafChangedUI(LaFChangeEvent e) {
								if (cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_GMX_SNOW, true)) {
 									installSnow(true);
								}
							}
						});
					}
				} else if (cmd.equals("mnu.modules")) {
					// make modules submenu
					if (appContext.getModuleManager()!=null) {
						JMenu modulesMenu = new JMenu();
						menu.add(modulesMenu);
						modulesMenu.setText(name);
						appContext.getModuleManager().createModulesMenu(modulesMenu);
					}
				} else {
					// make menu item
					JMenuItem mi = new JMenuItem();
					menu.add(mi);
					mi.setText(name);
					mi.setActionCommand(cmd);
					mi.addActionListener(commandActionListener);
					String mod, key;
					mod = menuSpec[i][2];
					key = menuSpec[i][3];
					int mod_val = 0;
					char key_val = ' ';
					if (key!=null && mod!=null) {
						if (mod.equals("ctrl")) {
							mod_val = java.awt.event.InputEvent.CTRL_DOWN_MASK;
						} else if (mod.equals("alt")) {
							mod_val = java.awt.event.InputEvent.ALT_DOWN_MASK;
						} else if (mod.equals("shift")) {
							mod_val = java.awt.event.InputEvent.SHIFT_DOWN_MASK;
						} else if (mod.equals("meta")) {
							mod_val = java.awt.event.InputEvent.META_DOWN_MASK;
						} else {
							key = null;
						}
						key_val = key.charAt(0);
					}
					if (key!=null && mod!=null) {
						mi.setAccelerator(
								KeyStroke.getKeyStroke(key_val, mod_val));
					}
				} // (if not mnu.skins)
			} // (if not separator)
		} // (for menuSpec entries)
	} // menu

	protected void initGUIPreload() {
		if (cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_PRELOAD_UI, false)==false)
			return;

		// preload FileChooser:
		cached_FileChooser = new JFileChooser();
		if (lafMan!=null) { 
			lafMan.addComponentToHead(cached_FileChooser);
		}
		// preload Settings:
		cached_SettingsDialog = new SettingsDialog(
				appContext, SimpleCalcV0.this);
		if (lafMan!=null) { 
			lafMan.addComponentToHead(cached_SettingsDialog);
		}
	}

	private void initGUICalcCompStyles() {
		styles.addStyle(CfgCalc.UI_STYLE_BUTTON_STANDARD, 
				cfg.getIntValue(CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + CfgCalc.UI_STYLE_BUTTON_STANDARD + ".width", 7), 
				cfg.getIntValue(CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + CfgCalc.UI_STYLE_BUTTON_STANDARD + ".height", 6), 
				1, 1);
		styles.addStyle(CfgCalc.UI_STYLE_BUTTON_SMALL, 
				cfg.getIntValue(CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + CfgCalc.UI_STYLE_BUTTON_SMALL + ".width", 7), 
				cfg.getIntValue(CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + CfgCalc.UI_STYLE_BUTTON_SMALL + ".height", 5), 
				1, 1);
	}

	private void updateCalcComp(JComponent comp, String cellName) {
		CmdEntry cmdE;
		CmdStyleGrp cmdG;

		cmdE = commandSet.get(cellName);
		if (cmdE==null) {
			System.err.println("Error: could not retrieve CommandEntry for " + cellName);
			return;
		}
		cmdG = commandSet.getGroupOfCommand(cmdE.getCmd());
		if (cmdG==null) {
			System.err.println("Error: could not retrieve CommandGroup for " + cellName);
		}

		if ((comp instanceof CalcButton) || (comp instanceof CalcToggleButton)) {
			if (comp instanceof CalcButton) {
				CalcButton btn = (CalcButton)comp;
				cmd2btn.put(cmdE.getCmd(), btn);
			}
			if (comp instanceof CalcToggleButton) {
				// nothing
			}
//			Color stdFG = comp.getForeground();
			AbstractButton abtn = (AbstractButton)comp;
			if (cmdE.getGroupName().equals("mod")) { // module?
				// no modif.
			} else {
				if ((cmdE.getToolTip()!=null) && !cmdE.getToolTip().equals("")) {
					comp.setToolTipText(cmdE.getToolTip());
				}
				abtn.setText(cmdE.getDisplay());
				abtn.setActionCommand(cmdE.getCmd());
			}
			abtn.setMargin(new Insets(1, 1, 1, 1));	// reduce std
			abtn.setHorizontalTextPosition(SwingConstants.CENTER);
			abtn.setVerticalTextPosition(SwingConstants.CENTER);
			abtn.setFocusPainted(
					cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_PAINTFOCUS, false)
					);
			
			if (comp instanceof CalcButton) {
				((CalcButton)comp).setUseCustomFont(
						cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_CUSTOMFONT, 
								false));
				((CalcButton)comp).setUseCustomGfx(
						cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_LOADGFX, 
								false));
				((CalcButton)comp).setCmdStyle(cmdE, cmdG);
			} else if (comp instanceof CalcToggleButton) {
				((CalcToggleButton)comp).setUseCustomFont(
						cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_CUSTOMFONT, 
								false));
				((CalcToggleButton)comp).setUseCustomGfx(
						cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_LOADGFX, 
								false));
				((CalcToggleButton)comp).setCmdStyle(cmdE, cmdG);
			} else {
				// ?
			}

			// to inner handling
//			if (cmdG==null) {
//				if (cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_CUSTOMFONT, false)) {
//					comp.setFont(new java.awt.Font("Arial",1,12));
//				}
//			} else {
//				if (cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_CUSTOMFONT, false)) {
//					Font f = cmdG.getFont();
//					if (f!=null) comp.setFont(f);
//				}
//				Color c = cmdG.getTextColor();
//				if (c!=null) {
//					Color stdC = comp.getForeground();
//					if (stdC!=null) {
//						c = Colorizer.adjustColor(null, stdC, c);
//					}
//					comp.setForeground(c);
//				}
//			}
//			if ((cmdE.getToolTip()!=null) && !cmdE.getToolTip().equals("")) {
//				comp.setToolTipText(cmdE.getToolTip());
//			}

			// FIXME: move the btngrp init to another place.
			// here only add the buttons to the btnGrps
			if (cmdE.getCmd().equals("aot.toggle")) {
				btnGrpAlwaysOnTop = new SyncButtonGroup();
				btnGrpAlwaysOnTop.add(abtn);
				btnGrpAlwaysOnTop.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						boolean state = (e.getStateChange()==ItemEvent.SELECTED);
						SimpleCalcV0.this.setAlwaysOnTop(state);
						cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_ALWAYSONTOP, state);					
					}
				});
			} else if (cmdE.getCmd().equals("log.toggle")) {
				btnGrpProtocolWindow = new SyncButtonGroup();
				btnGrpProtocolWindow.add(abtn);
				btnGrpProtocolWindow.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						boolean state = (e.getStateChange()==ItemEvent.SELECTED);
						if (SimpleCalcV0.this.isUseTxtProtocol()) {
							SimpleCalcV0.this.windowLog.setVisible(state);
						}
						if (SimpleCalcV0.this.isUseTabProtocol()) {
							SimpleCalcV0.this.windowLogTab.setVisible(state);
						}
						cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_PROTOCOLWINDOW_ON, state);					
					}
				});
			} else if (cmdE.getCmd().equals("math.toggle")) {
				btnGrpMathWindow = new SyncButtonGroup();
				btnGrpMathWindow.add(abtn);
				btnGrpMathWindow.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						boolean state = (e.getStateChange()==ItemEvent.SELECTED);
						SimpleCalcV0.this.windowMath.setVisible(state);
						cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_MATHWINDOW_ON, state);					
					}
				});
			} else if (cmdE.getCmd().equals("stack.toggle")) {
				// FIXME: do not reach...
				btnGrpStackWindow = new SyncButtonGroup();
				btnGrpStackWindow.add(abtn);
				btnGrpStackWindow.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						boolean state = (e.getStateChange()==ItemEvent.SELECTED);
						SimpleCalcV0.this.windowStack.setVisible(state);
						//cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_MATHWINDOW_ON, state);					
					}
				});
			} else if (cmdE.getCmd().equals("atw.pin.prottxt")) {
				btnGrpMotionLockProtTxt = new SyncButtonGroup();
				btnGrpMotionLockProtTxt.add(abtn);
				btnGrpMotionLockProtTxt.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						boolean state = (e.getStateChange()==ItemEvent.SELECTED);
						atwpl.setMotionLock(windowLog, state);
						//cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_MATHWINDOW_ON, state);					
					}
				});
			} else if (cmdE.getCmd().equals("atw.pin.prottab")) {
				btnGrpMotionLockProtTab = new SyncButtonGroup();
				btnGrpMotionLockProtTab.add(abtn);
				btnGrpMotionLockProtTab.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						boolean state = (e.getStateChange()==ItemEvent.SELECTED);
						atwpl.setMotionLock(windowLogTab, state);
						//cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_MATHWINDOW_ON, state);					
					}
				});
			} else {
				abtn.addActionListener(commandActionListener);
			}

//			if (cfg.getBooleanValue(CfgCalc.KEY_UI_BUTTON_LOADGFX, false)) {
//				// update images
//				Color c = cmdG.getTextColor();
//				Color cc = stdFG; //comp.getForeground();
//				Color bc = comp.getBackground();
//				
//				Image img;
//				boolean hasAnyImg;
//				hasAnyImg = false;
//				img = cmdE.getIconImage(CmdEntry.ICON_TEMPLATE);
//				if (img!=null) {
//					((CalcButton)abtn).initBgImage(img, true);
//				}
//				if (img!=null) hasAnyImg = true;
//				img = cmdE.getIconImage(CmdEntry.ICON_STANDARD);
//				if (img!=null) abtn.setIcon(
//						makeIcon(img, cmdE.isAdjustIconColor(), bc, cc, c));
//				if (img!=null) hasAnyImg = true;
//				img = cmdE.getIconImage(CmdEntry.ICON_PRESSED);
//				if (img!=null) abtn.setPressedIcon(
//						makeIcon(img, cmdE.isAdjustIconColor(), bc, cc, c));
//				if (img!=null) hasAnyImg = true;
//				img = cmdE.getIconImage(CmdEntry.ICON_ROLLOVER);
//				if (img!=null) abtn.setRolloverIcon(
//						makeIcon(img, cmdE.isAdjustIconColor(), bc, cc, c));
//				if (img!=null) hasAnyImg = true;
//				img = cmdE.getIconImage(CmdEntry.ICON_SELECTED);
//				if (img!=null) abtn.setSelectedIcon(
//						makeIcon(img, cmdE.isAdjustIconColor(), bc, cc, c));
//				if (img!=null) hasAnyImg = true;
//				img = cmdE.getIconImage(CmdEntry.ICON_DISABLED);
//				if (img!=null) abtn.setDisabledIcon(
//						makeIcon(img, cmdE.isAdjustIconColor(), bc, cc, c));
//				if (img!=null) hasAnyImg = true;
//				img = cmdE.getIconImage(CmdEntry.ICON_ROLLOVER_SELECTED);
//				if (img!=null) abtn.setRolloverSelectedIcon(
//						makeIcon(img, cmdE.isAdjustIconColor(), bc, cc, null));
//				if (img!=null) hasAnyImg = true;
//			
//				if (hasAnyImg) {
//					abtn.setText(null);
////					abtn.setBorderPainted(false);
////					abtn.setContentAreaFilled(false);
//					//btn.setAlignmentY(0.5f);
//				}
//			} // (update images)
		// <- Buttons
		} else if (comp instanceof JTextField) {
			// no support for JTextField yet
		} else if (comp instanceof JTextArea) {
			// no support for JTextArea yet
		} else if (comp instanceof JTextPane) {
			// no support for JTextArea yet
		} else if (comp instanceof JPanel) {
			// no support for JPanel yet
		}
	}
	
	private void initGUICalcComponents() {
		CalcCompCellConstraints [] cons;
		CalcCompCellConstraints con;
		int stdInnerPan = 1;
		int stdOuterPan = 1;

		CalcCompStyles.Style styleSmall;
		CalcCompStyles.Style styleStandard;
		styleSmall = styles.getStyle(CfgCalc.UI_STYLE_BUTTON_SMALL);
		styleStandard = styles.getStyle(CfgCalc.UI_STYLE_BUTTON_STANDARD);

		{
			displayCombo = new CalcDisplayExt();
			panelCalc.add(
					displayCombo, 
					new CalcCompCellConstraints("dsp.display")
						.defineSpan(5, 5)
						.defineStretch(true, false)
						.defineMajorGrid(1, 1) // FIXME: lets use a real cell grid
						.definePan(1, 1)
						.defineFit(CalcCompCellConstraints.FIT_BY_ROW)
				);
			setPopup(displayCombo);
		}

		{
			con = new CalcCompCellConstraints("sep1")
					.defineLinkNorth("dsp.display", 0)
					.definePan(stdInnerPan, stdOuterPan)
					//.defineSpan(1, 1)
					.defineMajorGrid(1, 1)
					.defineStretch(true, false)
					;
			JSeparator sep = new JSeparator();
			panelCalc.add(sep, con);
			//updateCalcComp(btn, con);
		}

		cons = CalcCompCellConstraints.createGrid(
				5, 1,
				(
						ProcessorAction.PA_MEM_ADD + "," +
						ProcessorAction.PA_MEM_SUB + "," +
						ProcessorAction.PA_MEM_STORE + "," +
						ProcessorAction.PA_MEM_READ + "," +
						ProcessorAction.PA_MEM_CLR
						).split(","), 
				null, 
				CalcCompCellConstraints.FIT_BY_ROW, 
				new CalcCompCellConstraints("<template>")
					.defineLinkNorth("sep1", 0)
					//.defineSpan(1, 1)
					.defineMajorGrid(styleSmall.width, styleSmall.height)
					.definePan(styleSmall.ipan, styleSmall.opan)
		);
//		cons = CalcCompCellConstraints.mergeCells(cons, 
//				new String[]{"m.c", "dummy1"});
		styles.addCellsForStyle(CfgCalc.UI_STYLE_BUTTON_SMALL, cons);
		for (int i=0; i<cons.length; i++) {
			CalcButton btn = new CalcButton(); 
			panelCalc.add(
					btn,
					cons[i]);
			updateCalcComp(btn, cons[i].getName());
		}

		{
			con = new CalcCompCellConstraints("sep2")
					.defineLinkNorth("m.c", 0)
					.definePan(stdInnerPan, stdOuterPan)
					//.defineSpan(1, 1)
					.defineMajorGrid(1, 1)
					.defineStretch(true, false)
					;
			JSeparator sep = new JSeparator();
			panelCalc.add(sep, con);
			//updateCalcComp(btn, con);
		}

		cons = CalcCompCellConstraints.createGrid(
				3, 4, 
				(
						ProcessorAction.PA_7 + "," +
						ProcessorAction.PA_8 + "," +
						ProcessorAction.PA_9 + "," +
						ProcessorAction.PA_4 + "," +
						ProcessorAction.PA_5 + "," +
						ProcessorAction.PA_6 + "," +
						ProcessorAction.PA_1 + "," +
						ProcessorAction.PA_2 + "," +
						ProcessorAction.PA_3 + "," +
						ProcessorAction.PA_0 + "," +
						ProcessorAction.PA_DOT + "," +
						ProcessorAction.PA_SIGN
						).split(","), null, 
				CalcCompCellConstraints.FIT_BY_ROW, 
				new CalcCompCellConstraints("<template>")
					.defineMajorGrid(styleStandard.width, styleStandard.height)
					.defineLinkNorth("sep2", 0)
					.definePan(styleStandard.ipan, styleStandard.opan)
		);
		styles.addCellsForStyle(CfgCalc.UI_STYLE_BUTTON_STANDARD, cons);
		for (int i=0; i<cons.length; i++) {
			CalcButton btn = new CalcButton(); 
			panelCalc.add(
					btn,
					cons[i]);
			updateCalcComp(btn, cons[i].getName());
		}

		cons = CalcCompCellConstraints.createGrid(
				1, 4, 
				(
						ProcessorAction.PA_MUL + "," +
						ProcessorAction.PA_DIV + "," +
						ProcessorAction.PA_ADD + "," +
						ProcessorAction.PA_SUB
						).split(","), null, 
				CalcCompCellConstraints.FIT_BY_ROW, 
				new CalcCompCellConstraints("<template>")
					.defineMajorGrid(styleStandard.width, styleStandard.height)
					.defineLinkNorth("sep2", 0)
					.defineLinkWest("9", 0)
					.definePan(styleStandard.ipan, styleStandard.opan)
		);
//		cons = CalcCompCellConstraints.mergeCells(cons, new String[]{"=", "dummy1"});
//		styles.addCellsForStyle(CfgCalc.UI_STYLE_BUTTON_STANDARD, cons);
		for (int i=0; i<cons.length; i++) {
			CalcButton btn = new CalcButton(); 
			panelCalc.add(
					btn,
					cons[i]);
			updateCalcComp(btn, cons[i].getName());
		}

		cons = CalcCompCellConstraints.createGrid(
				1, 4, 
				(
						ProcessorAction.PA_CLR_ALL + "," +
						ProcessorAction.PA_CLR_ENTRY + "," +
						ProcessorAction.PA_EXE + "," +
						"dummy1"
						).split(","), null, 
				CalcCompCellConstraints.FIT_BY_COL, 
				new CalcCompCellConstraints("<template>")
					.defineMajorGrid(styleStandard.width, styleStandard.height)
					.defineLinkNorth("sep2", 0)
					.defineLinkWest("*", 0)
					.definePan(stdInnerPan, stdOuterPan)
		);
		cons = CalcCompCellConstraints.mergeCells(cons, 
				new String[]{ProcessorAction.PA_EXE, "dummy1"});
		styles.addCellsForStyle(CfgCalc.UI_STYLE_BUTTON_STANDARD, cons);
		for (int i=0; i<cons.length; i++) {
			CalcButton btn = new CalcButton(); 
			panelCalc.add(
					btn,
					cons[i]);
			updateCalcComp(btn, cons[i].getName());
		}

		cons = CalcCompCellConstraints.createGrid(
				2, 1, 
				(
						ProcessorAction.PA_PERCENT + "," +
//						ProcessorAction.PA_PERCENT_SUB + "," +
//						ProcessorAction.PA_PERCENT_MUL + "," +
//						ProcessorAction.PA_PERCENT_DIV + "," +
						ProcessorAction.PA_PERCENT_EX
						).split(","), null, 
				CalcCompCellConstraints.FIT_BY_COL, 
				new CalcCompCellConstraints("<template>")
					.defineMajorGrid(styleStandard.width, styleStandard.height)
					.defineLinkNorth("0", 0)
					.definePan(stdInnerPan, stdOuterPan)
		);
		styles.addCellsForStyle(CfgCalc.UI_STYLE_BUTTON_SMALL, cons);
		for (int i=0; i<cons.length; i++) {
			CalcButton btn = new CalcButton(); 
			panelCalc.add(
					btn,
					cons[i]);
			updateCalcComp(btn, cons[i].getName());
		}

		{
			con = new CalcCompCellConstraints("sep3")
					.defineLinkNorth(ProcessorAction.PA_PERCENT, 0)
					.definePan(stdInnerPan, stdOuterPan)
					//.defineSpan(31, 1)
					.defineMajorGrid(1, 1)
					.defineStretch(true, false)
					;
			JSeparator sep = new JSeparator();
			panelCalc.add(sep, con);
			//updateCalcComp(btn, con);
		}

		cons = CalcCompCellConstraints.createGrid(
				5, 1, 
				"log.toggle,math.toggle,atw.snap,aot.toggle,mnu.show".split(","), null, 
				CalcCompCellConstraints.FIT_BY_COL, 
				new CalcCompCellConstraints("<template>")
					.defineMajorGrid(styleSmall.width, styleSmall.height)
					.defineLinkNorth("sep3", 0)
					.definePan(stdInnerPan, stdOuterPan)
		);
		styles.addCellsForStyle(CfgCalc.UI_STYLE_BUTTON_SMALL, cons);
		for (int i=0; i<cons.length; i++) {
			AbstractButton btn;
			if (
					cons[i].getName().equals("atw.snap") ||
					cons[i].getName().equals("mnu.show")
				) {
				btn = new CalcButton();
			} else {
				btn = new CalcToggleButton();
			}
			panelCalc.add(
					btn,
					cons[i]);
			updateCalcComp(btn, cons[i].getName());
		}

		int modcnt = appContext.getModuleManager().getModuleCount();
		if (modcnt>0) {
			int [] modidxs = new int[modcnt];
			int modidxscnt = 0;
			Action [] modactions;
			String modstring = null;
			for (int i=0; i<modcnt; i++) {
				modactions = appContext.getModuleManager().getModule(i).getActions();
				if (modactions==null || modactions.length<1) continue;

				modidxs[modidxscnt++] = i;
				if (modstring==null) {
					modstring = "mod." + i;
				} else {
					modstring += ",mod." + i;
				}
			}
			if (modidxscnt>0) {
				cons = CalcCompCellConstraints.createGrid(
						modidxscnt, 1, 
						modstring.split(","), null, 
						CalcCompCellConstraints.FIT_BY_COL, 
						new CalcCompCellConstraints("<template>")
							.defineMajorGrid(styleSmall.width, styleSmall.height)
							.defineLinkNorth("log.toggle", 0)
							.definePan(stdInnerPan, stdOuterPan)
				);
				styles.addCellsForStyle(CfgCalc.UI_STYLE_BUTTON_SMALL, cons);
				for (int i=0; i<cons.length; i++) {
					AbstractButton btn;
					btn = new CalcButton();
					modactions = appContext.getModuleManager().getModule(
							modidxs[i]).getActions();
					btn.setAction(modactions[0]);
					panelCalc.add(btn, cons[i]);
					updateCalcComp(btn, cons[i].getName());
				}
			}
		}
	}

	void updateUISettings() {
		if (DBGforce) System.out.println("updateUISettings:");

		if (cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_GMX_SNOW, true)) {
			installSnow(true);
		} else {
			installSnow(false);
		}

		CaDoubleFormat numFmt;
		String tmp = cfg.getStringValue(
				CfgCalc.KEY_UI_NUMBER_FORMAT,
				CfgCalc.UI_NUMBER_FORMAT_DEFAULT);
		numFmt = CaDoubleFormat.valueOf(tmp);
		calcProc.setNumberFormat(numFmt);

		setValueInTitle(cfg.getBooleanValue(
				CfgCalc.KEY_UI_MAIN_VALUE_IN_TITLE, true));
		setUseTxtProtocol(cfg.getBooleanValue(
				CfgCalc.KEY_UI_MAIN_SHOW_TXT_PROT, false));
		setUseTabProtocol(cfg.getBooleanValue(
				CfgCalc.KEY_UI_MAIN_SHOW_TAB_PROT, true));

		if (numberFormatterContext!=null) {
			CaDoubleFormatter cdf = (CaDoubleFormatter)numberFormatterContext.get(CaDouble.class);
			CaDoubleFormatter.valueOf(tmp, cdf);
		}
		if (protocolTabular!=null) {
			protocolTabular.refreshDisplay();
		}

		configureLocaleManager();
		
		Dimension dim = new Dimension();
		dim.width = cfg.getIntValue(
				CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".width", 4);
		dim.height = cfg.getIntValue(
				CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".height", 4);
		if (DBGforce) System.out.println("mincell = " + dim);
		boolean fixedCellSize = cfg.getBooleanValue(
				CfgCalc.KEY_UI_LAYOUT_CCL_FIXEDCELLSIZE, true);
		if (panelCalcLayout!=null) {
			panelCalcLayout.setMinCellSize(new Dimension(dim));
			panelCalcLayout.setFixedCellSize(fixedCellSize);
		}
		if (panelMathLayout!=null) {
			panelMathLayout.setMinCellSize(new Dimension(dim));
			panelMathLayout.setFixedCellSize(fixedCellSize);
		}
		if (panelLogButtonsLayout!=null) {
			panelLogButtonsLayout.setMinCellSize(new Dimension(dim));
			panelLogButtonsLayout.setFixedCellSize(fixedCellSize);
		}
		CalcCompStyles.Style style;

		style = styles.getStyle(CfgCalc.UI_STYLE_BUTTON_STANDARD);
		style.width = cfg.getIntValue(
				CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + CfgCalc.UI_STYLE_BUTTON_STANDARD + ".width", 7);
		style.height = cfg.getIntValue(
				CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + CfgCalc.UI_STYLE_BUTTON_STANDARD + ".height", 7);

		style = styles.getStyle(
				CfgCalc.UI_STYLE_BUTTON_SMALL);
		style.width = cfg.getIntValue(
				CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + CfgCalc.UI_STYLE_BUTTON_SMALL + ".width", 7);
		style.height = cfg.getIntValue(
				CfgCalc.KEY_UI_LAYOUT_CCL_STYLE_BASE + CfgCalc.UI_STYLE_BUTTON_SMALL + ".height", 5);

		styles.updateCellsForAllStyles();

		CalcLaFManager.updateLaFManager();
		skinMenu.removeAll();
		lafMan.createLaFSkinMenu(
				skinMenu, 
				!cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_SKINMENU_FLAT, true));

		this.protocol.setColumns(calcProc.getProtocolWidth());
		
		if (panelCalcLayout!=null) {
			panelCalcLayout.invalidateLayout(panelCalc);
		}
		if (panelMathLayout!=null) {
			panelMathLayout.invalidateLayout(panelMath);
		}
		//this.doLayout();
		this.invalidate();
		panelCalc.invalidate();
		panelMath.invalidate();
		this.pack();
	}
	
	private void initGUIPanelCalc() {
		BorderLayout thisLayout = new BorderLayout();
		this.getContentPane().setLayout(thisLayout);
		panelCalc = new CalcPanel();
		setPopup(panelCalc);
		this.getContentPane().add(panelCalc, BorderLayout.CENTER);
//		panelCalc.setOpaque(false);
		panelCalcLayout = new CalcCompLayout();
		panelCalcLayout.setShrinkWrap(true);
		panelCalcLayout.setShrinkPan(2);
		panelCalcLayout.setAlignmentY(0.0f);
		panelCalcLayout.setMinCellSize(new Dimension(
				cfg.getIntValue(CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".width", 4), 
				cfg.getIntValue(CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".height", 4)));
		if (cfg.getBooleanValue(CfgCalc.KEY_UI_LAYOUT_CCL_FIXEDCELLSIZE, true)) {
			panelCalcLayout.setFixedCellSize(true);
			panelCalcLayout.setForceFixedCellSize(true);
			//System.out.println("setup mincell = " + panelCalc2Layout.getMinCellSize());
		}
		panelCalc.setLayout(panelCalcLayout);
		if (cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_PANELMOVE, false)) {
			panelCalc.linkSurfaceDragging(this);
		}
		//panelCalc2.setBorder(new LineBorder(Color.red, 1, false));
		{
			initGUICalcComponents();
		}
	}

	private void initGUIPanelLogWindow() {
		panelLogButtons = new CalcPanel();
		panelLog.add(panelLogButtons, BorderLayout.NORTH);
		panelLogProtocol = new CalcPanel();
		panelLog.add(panelLogProtocol, BorderLayout.CENTER);

		panelLogButtonsLayout = new CalcCompLayout();
		panelLogButtons.setLayout(panelLogButtonsLayout);
		panelLogButtonsLayout.setShrinkWrap(true);
		panelLogButtonsLayout.setShrinkPan(2);
		panelLogButtonsLayout.setAlignmentY(0.0f);
		panelLogButtonsLayout.setMinCellSize(new Dimension(
				cfg.getIntValue(CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".width", 4), 
				cfg.getIntValue(CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".height", 4)));
		if (cfg.getBooleanValue(CfgCalc.KEY_UI_LAYOUT_CCL_FIXEDCELLSIZE, true)) {
			panelLogButtonsLayout.setFixedCellSize(true);
			panelLogButtonsLayout.setForceFixedCellSize(true);
			//System.out.println("setup mincell = " + panelCalc2Layout.getMinCellSize());
		}

		panelLogProtocol.setLayout(new BorderLayout());

		{
			CalcCompCellConstraints [] cons;
	
			CalcCompStyles.Style styleSmall;
			styleSmall = styles.getStyle(CfgCalc.UI_STYLE_BUTTON_SMALL);
			
			cons = CalcCompCellConstraints.createGrid(
					5, 1,
					// FIXME: use the ProcessorActions:
					"l.c,l.cl,l.p,l.s,atw.pin.prottxt".split(","), null, 
					CalcCompCellConstraints.FIT_BY_ROW, 
					new CalcCompCellConstraints("<template>")
						.defineMajorGrid(styleSmall.width, styleSmall.height)
						.definePan(styleSmall.ipan, styleSmall.opan)
			);
			styles.addCellsForStyle(CfgCalc.UI_STYLE_BUTTON_SMALL, cons);
			for (int i=0; i<cons.length; i++) {
				AbstractButton btn;
				if (cons[i].getName().equals("atw.pin.prottxt")) {
					btn = new CalcToggleButton(); 
				} else {
					btn = new CalcButton(); 
				}
				panelLogButtons.add(
						btn,
						cons[i]);
				updateCalcComp(btn, cons[i].getName());
			}
		}

		protocol = new CalcProtocol(numberFormatterContext);
		panelLogProtocol.add(protocol, BorderLayout.CENTER);
		protocol.setColumns(calcProc.getProtocolWidth());
		calcProc.addProtocolListener(protocol);
		//protocol.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	}
	
	private void initGUIPanelLog() {
		panelLog = new CalcPanel();
		setPopup(panelLog);
		//panelMain.add(panelLog);
		windowLog = new AttachedWindow(this);
		windowLog.setName("protocol");
		windowLog.setTitle("Protocol");
//		panelLog.setOpaque(false);
		BorderLayout panelLogLayout;
		panelLogLayout = new BorderLayout();

//		CalcCompLayout panelLogLayout = new CalcCompLayout();
//		panelLogLayout.setShrinkWrap(true);
//		panelLogLayout.setShrinkPan(2);
//		panelLogLayout.setAlignmentY(0.0f);
//		panelLogLayout.setMajorGridSize(new Dimension(7, 7));
//		panelLogLayout.setMinCellSize(new Dimension(4, 4));

		panelLog.setLayout(panelLogLayout);
		//panelLog.setBorder(new LineBorder(Color.red, 1, false));
		//panelLog.setBackground(new java.awt.Color(192,192,192));
		//panelLog.initBgImage("panel.jpg");
		{
			initGUIPanelLogWindow();
		}
		windowLog.placePanel(panelLog);
		windowLog.pack();
		windowLog.setResizable(true);
		atwpl.addEast(windowLog);
		atwpl.enableMethod(
				windowLog,
				AttachedWindowManager.Placer.HEIGHT, true);
	}

	private void initGUIPanelLogWindowTab() {
		panelLogProtocolTab = new CalcPanel();
		panelLogTab.add(panelLogProtocolTab, BorderLayout.CENTER);

		panelLogProtocolTab.setLayout(new BorderLayout());

		protocolTabular = new CalcProtocolTabular(numberFormatterContext);
		panelLogProtocolTab.add(protocolTabular, BorderLayout.CENTER);
		//protocol.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		calcProc.addProtocolListener(protocolTabular);
		protocolTabular.setProcessorCalculation(calcProc);
	}
	
	private void initGUIPanelLogTab() {
		panelLogTab = new CalcPanel();
		setPopup(panelLogTab);
		windowLogTab = new AttachedWindow(this);
		windowLogTab.setName("protocoltab");
		windowLogTab.setTitle("Tabular Protocol");
		BorderLayout panelLogLayout;
		panelLogLayout = new BorderLayout();

		panelLogTab.setLayout(panelLogLayout);
		{
			initGUIPanelLogWindowTab();
		}
		windowLogTab.placePanel(panelLogTab);
		windowLogTab.pack();
		windowLogTab.setResizable(true);
		atwpl.addEast(windowLogTab);
		atwpl.enableMethod(
				windowLogTab,
				AttachedWindowManager.Placer.HEIGHT, true);
	}

	private void initGUIPanelMathWindow() {
		int stdInnerPan = 1;
		int stdOuterPan = 1;
		CalcCompCellConstraints [] cons;
		CalcCompCellConstraints con;
//		int stdInnerPan = 1;
//		int stdOuterPan = 1;

		CalcCompStyles.Style styleSmall;
//		CalcCompStyles.Style styleStandard;
		styleSmall = styles.getStyle(CfgCalc.UI_STYLE_BUTTON_SMALL);
//		styleStandard = styles.getStyle(CfgCalc.UI_STYLE_BUTTON_STANDARD);

		{
			cons = CalcCompCellConstraints.createGrid(
					3, 1,
					(
							ProcessorAction.PA_AM_RAD + "," +
							ProcessorAction.PA_AM_DEG + "," +
							ProcessorAction.PA_AM_GRA
					).split(","), 
					null, 
					CalcCompCellConstraints.FIT_BY_ROW, 
					new CalcCompCellConstraints("<template>")
						//.defineLinkNorth("sep1", 2)
						//.defineSpan(1, 1)
						//.defineMajorGrid(styleSmall.width, styleSmall.height)
						.defineMajorGrid(styleSmall.width, 4)
						//.definePan(0, styleSmall.opan)
						.definePan(styleSmall.ipan, styleSmall.opan)
			);
			styles.addCellsForStyle(CfgCalc.UI_STYLE_BUTTON_SMALL, cons);
			for (int i=0; i<cons.length; i++) {
				CalcToggleButton btn = new CalcToggleButton();
				btnGrpAngularArgMode.add(btn);
				panelMath.add(
						btn,
						cons[i]);
				updateCalcComp(btn, cons[i].getName());
				if (i==0) btnAngArgModRad = btn;
				if (i==1) btnAngArgModDeg = btn;
				if (i==2) btnAngArgModGra = btn;
			}
			btnAngArgModRad.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange()==ItemEvent.SELECTED) {
						calcProc.setAngularArgMode(
								SimpleProcessor.ANGULARARG_RAD);
					}
				}
			});
			btnAngArgModDeg.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange()==ItemEvent.SELECTED) {
						calcProc.setAngularArgMode(
								SimpleProcessor.ANGULARARG_DEG);
					}
				}
			});
			btnAngArgModGra.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange()==ItemEvent.SELECTED) {
						calcProc.setAngularArgMode(
								SimpleProcessor.ANGULARARG_GRA);
					}
				}
			});
		}

		{
			con = new CalcCompCellConstraints("sep1")
					.defineLinkNorth(ProcessorAction.PA_AM_RAD, 1)
					.definePan(stdInnerPan, stdOuterPan)
					//.defineSpan(1, 1)
					.defineMajorGrid(1, 1)
					.defineStretch(true, false)
					;
			JSeparator sep = new JSeparator();
			panelMath.add(sep, con);
			//updateCalcComp(btn, con);
		}

		{
			cons = CalcCompCellConstraints.createGrid(
					5, 6,
					(
							ProcessorAction.PA_M_XINV + "," +
							ProcessorAction.PA_M_SQR + "," +
							ProcessorAction.PA_M_SQRT + "," +
							ProcessorAction.PA_M_POW + "," +
							ProcessorAction.PA_M_X_RT + "," +
	
							ProcessorAction.PA_M_E + "," + 
							ProcessorAction.PA_M_EXP + "," +
							ProcessorAction.PA_M_EXP10 + "," +
							ProcessorAction.PA_M_LN + "," +
							ProcessorAction.PA_M_LOG + "," +
	
							ProcessorAction.PA_M_PI + "," +
							ProcessorAction.PA_M_SIN + "," +
							ProcessorAction.PA_M_COS + "," +
							ProcessorAction.PA_M_TAN + "," +
							ProcessorAction.PA_M_COT + "," +
	
							ProcessorAction.PA_M_SEC + "," +
							ProcessorAction.PA_M_ARCSIN + "," +
							ProcessorAction.PA_M_ARCCOS + "," +
							ProcessorAction.PA_M_ARCTAN + "," +
							ProcessorAction.PA_M_ARCCOT + "," +
	
							ProcessorAction.PA_M_COSEC + "," +
							ProcessorAction.PA_M_SINH + "," +
							ProcessorAction.PA_M_COSH + "," +
							ProcessorAction.PA_M_TANH + "," +
							ProcessorAction.PA_M_COTH + "," +
	
							ProcessorAction.PA_M_GAMMA + "," +
							ProcessorAction.PA_M_ARSINH + "," +
							ProcessorAction.PA_M_ARCOSH + "," +
							ProcessorAction.PA_M_ARTANH + "," +
							ProcessorAction.PA_M_ARCOTH
					).split(","), 
					null, 
					CalcCompCellConstraints.FIT_BY_ROW, 
					new CalcCompCellConstraints("<template>")
						.defineLinkNorth("sep1", 1)
						//.defineSpan(1, 1)
						.defineMajorGrid(styleSmall.width, styleSmall.height)
						//.definePan(0, styleSmall.opan)
						.definePan(styleSmall.ipan, styleSmall.opan)
			);
			styles.addCellsForStyle(CfgCalc.UI_STYLE_BUTTON_SMALL, cons);
			for (int i=0; i<cons.length; i++) {
				CalcButton btn = new CalcButton(); 
				panelMath.add(
						btn,
						cons[i]);
				updateCalcComp(btn, cons[i].getName());
			}
		}
	}

	private void initGUIPanelMath() {
		panelMath = new CalcPanel();
		setPopup(panelMath);
		//panelMain.add(panelLog);
		windowMath = new AttachedWindow(this);
		windowMath.setName("math");
		windowMath.setTitle("Mathematics");
//		panelMath.setOpaque(false);

		panelMathLayout = new CalcCompLayout();
		panelMathLayout.setShrinkWrap(true);
		panelMathLayout.setShrinkPan(2);
		panelMathLayout.setAlignmentY(0.0f);

		panelMathLayout.setMinCellSize(new Dimension(
				cfg.getIntValue(CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".width", 4), 
				cfg.getIntValue(CfgCalc.KEY_UI_LAYOUT_CCL_MINCELL_BASE + ".height", 4)));
		if (cfg.getBooleanValue(CfgCalc.KEY_UI_LAYOUT_CCL_FIXEDCELLSIZE, true)) {
			panelMathLayout.setFixedCellSize(true);
			panelMathLayout.setForceFixedCellSize(true);
			//System.out.println("setup mincell = " + panelCalc2Layout.getMinCellSize());
		}
		panelMath.setLayout(panelMathLayout);
//		if (cfg.getBooleanValue(CfgCalc.KEY_UI_MAIN_PANELMOVE, false)) {
//			panelMath.linkSurfaceDragging(this);
//		}

//		CalcCompLayout panelLogLayout = new CalcCompLayout();
//		panelLogLayout.setShrinkWrap(true);
//		panelLogLayout.setShrinkPan(2);
//		panelLogLayout.setAlignmentY(0.0f);
//		panelLogLayout.setMajorGridSize(new Dimension(7, 7));
//		panelLogLayout.setMinCellSize(new Dimension(4, 4));

		//panelMath.setLayout(panelMathLayout);
		//panelLog.setBorder(new LineBorder(Color.red, 1, false));
		//panelLog.setBackground(new java.awt.Color(192,192,192));
		//panelLog.initBgImage("panel.jpg");

		btnGrpAngularArgMode = new ButtonGroup();
		
		{
			initGUIPanelMathWindow();
		}
		windowMath.placePanel(panelMath, 0, 0, 5, 0);
		windowMath.pack();
		windowMath.setResizable(false);
		atwpl.addSouth(windowMath);
		atwpl.enableMethod(
				windowMath,
				AttachedWindowManager.Placer.WIDTH, true);
	}

	private void initGUIPanelStack() {
		btnGrpStackWindow = new SyncButtonGroup();
		btnGrpStackWindow.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean state = (e.getStateChange()==ItemEvent.SELECTED);
				SimpleCalcV0.this.windowStack.setVisible(state);
			}
		});

		panelStack = new CalcPanel();
		setPopup(panelStack);
		windowStack = new AttachedWindow(this);
		windowStack.setName("stack");
		windowStack.setTitle("Stack");
//		panelStack.setOpaque(false);

		panelStack.setLayout(new BorderLayout());
		{
			listStack = new JList();
			JScrollPane scrollPane = new JScrollPane(listStack);
			panelStack.add(scrollPane, BorderLayout.CENTER);
			scrollPane.setVerticalScrollBarPolicy(
					ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		}
		windowStack.placePanel(panelStack, 0, 0, 5, 0);
		windowStack.pack();
		windowStack.setResizable(true);
		atwpl.addWest(windowStack);
		atwpl.enableMethod(
				windowStack,
				AttachedWindowManager.Placer.HEIGHT, true);
	}

	File getSaveFileNameJFC() {
		class TextFilter extends FileFilter {
		    @Override
			public boolean accept(File f) {
		        if (f.isDirectory()) {
		            return true;
		        }
		        String ext = null;
		        String s = f.getName();
		        int i = s.lastIndexOf('.');
		        if (i > 0 &&  i < s.length() - 1) {
		            ext = s.substring(i+1).toLowerCase();
		        }
		        if (ext != null) {
		        	boolean retcode;
		        	retcode = ext.equals("txt");
		            return retcode;
		        }
		        return false;
		    }

		    //The description of this filter
		    @Override
			public String getDescription() {
		        return "Text Files (*.txt)";
		    }
		}

		File file = null; 
		JFileChooser fc;
		if (cached_FileChooser!=null) {
			fc = cached_FileChooser;
		} else {
			fc = new JFileChooser();
		}
		fc.addChoosableFileFilter(new TextFilter());
		fc.setFileHidingEnabled(false);
		{
			String wd = cfg.getStringValue(CfgCalc.KEY_UI_MAIN_WORKINGDIR, null);
			if (wd!=null && !wd.equals("")) {
				lastCurDir = new File(wd);
			}
		}
		if (lastCurDir!=null) fc.setCurrentDirectory(lastCurDir);
		int result = fc.showSaveDialog(this);
		lastCurDir = fc.getCurrentDirectory();
		{
			String wd = lastCurDir.getPath();
			cfg.putStringValue(CfgCalc.KEY_UI_MAIN_WORKINGDIR, wd);
		}
		switch (result) {
		case JFileChooser.APPROVE_OPTION: // open or save
			file = fc.getSelectedFile();
			if (file.exists()) {
				int returnValue = JOptionPane.showConfirmDialog(
						this, 
						"The file " + file.toString() + " already exist.\n"+
				"Overwrite?");
				if (returnValue == JOptionPane.YES_OPTION) {
					return file;
				} else {
					// cancel
					return null;
				}	  	    	
			} else {
				return file;
			}
			// unreachable
			// break;
		case JFileChooser.CANCEL_OPTION:
			break;
		case JFileChooser.ERROR_OPTION:
			break;
		}

		return null;
	}

	void saveLogToFile() {
		if (isUseTabProtocol() && protocolTabular!=null) {
			protocolTabular.saveProtocolTxt();
			return;
		} else
		if (isUseTxtProtocol() && protocol!=null) {
			File file = getSaveFileNameJFC();
			if (file==null) return;
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(
						file));
				out.write(calcProc.getLogDisplay());
				out.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(
						this, e, "Error saving file", 
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(
					this, 
					"Cannot copy data - no protocol is active.", 
					"No protocol active", 
					JOptionPane.ERROR_MESSAGE);
		}
	}

	void printLog() {
		if (isUseTabProtocol() && protocolTabular!=null) {
			protocolTabular.printData();
			return;
		} else
		if (isUseTxtProtocol() && protocol!=null) {
			StringPrinter sp = new StringPrinter(this, calcProc.getLogDisplay());
			sp.doPrint(false);
		} else {
			JOptionPane.showMessageDialog(
					this, 
					"Cannot print data - no protocol is active.", 
					"No protocol active", 
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Auto-generated method for setting the popup menu for a component
	 * @param parent 
	 * @param menu 
	 */
	private void setComponentPopupMenu(
			final java.awt.Component parent, 
			final javax.swing.JPopupMenu menu) {
		parent.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				if(e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				if(e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
		});
	}

	private void setPopup(final java.awt.Component parent) {
		setComponentPopupMenu(parent, popupMenu);
	}

	void processKeyCommand(String cmd) {
		CalcButton btn;
		btn = cmd2btn.get(cmd);
		if (btn!=null) {
			btn.doClick();
		} else {
			processCommand(cmd);
		}
	}

	private void processCommand(String cmd) {
		if (cmd.equals("log.toggle")) {
//			boolean vis;
//			vis = windowLog.isVisible();
//			vis = !vis;
//			if (vis) {
//				//windowLog.pack();
//			}
//			windowLog.setVisible(vis);
//			cfg.putValue("ui.logwindow.on", vis);
			return;
		}
		if (cmd.equals("math.toggle")) {
			return;
		}
		if (cmd.equals("aot.toggle")) {
			boolean aot;
			aot = this.isAlwaysOnTop();
			aot = !aot;
			if (aot) {
				//windowLog.pack();
			}
			this.setAlwaysOnTop(aot);
			cfg.putBooleanValue(CfgCalc.KEY_UI_MAIN_ALWAYSONTOP, aot);
			return;
		}
		try {
			calcProc.processCommand(cmd);
		} catch (ProcessorException e) {
			if (logger!=null) logger.warning(
					"Processor exception: " + e.getMessage());
			// e.printStackTrace();
			// FIXME: add handler for that case
			JOptionPane.showMessageDialog(
					this,
					"An error occured in the calculation core:\n"+
					e.getMessage() + "\n" +
					"Please contact customer support.",
					"Calculator error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	void processActionCommand(String cmd) {
//		System.out.println("jButtonTest.actionPerformed, event="+evt);
		//String cmd = evt.getActionCommand();
		if (cmd.equals("l.s")) {
			saveLogToFile();
			return;
		}
		if (cmd.equals("l.p")) {
			printLog();
			return;
		}
		if (cmd.equals("off")) {
			doPhaseExit();
			return; // not reachable
		}
//		if (cmd.equals("atw.pin")) {
//			return;
//		}
		if (cmd.equals("atw.snap")) {
			atwpl.snapAll();
			return;
		}
		if (cmd.equals("mnu.show")) {
			Point p;
			p = this.getMousePosition(true);
			if (p==null) {
				p = this.getLocation();
				p.x += this.getWidth()/2;
				p.y += this.getHeight()/2;
			}
			popupMenu.show(this, (int)p.getX(), (int)p.getY());
			return; // not reachable
		}
		if (cmd.equals("mnu.exit")) {
			doPhaseExit();
			return; // not reachable
		}
		if (cmd.equals("mnu.sav.log")) {
			saveLogToFile();
			return;
		}
		if (cmd.equals("mnu.about")) {
			AboutDialog dialog = new AboutDialog(SimpleCalcV0.this);
			dialog.setAboutInfo(
					UI_ABOUT_APP,
					UI_ABOUT_COPY,
					UI_ABOUT_INF,
					UI_ABOUT_SOFTLIBS);
			dialog.setTitle(UI_ABOUT_TITLE);
			// FIXME: make that a variable from CfgCalc
			dialog.setLogo("logo48x48.png");

			dialog.setVisible(true);
			return;
		}
		if (cmd.equals("mnu.settings")) {
			SettingsDialog dialog;
			if (cached_SettingsDialog!=null) {
				dialog = cached_SettingsDialog;
				dialog.resetSettings();
			} else {
				dialog = new SettingsDialog(appContext, SimpleCalcV0.this);
			}
			dialog.loadSettings();
			dialog.pack();
			dialog.setVisible(true);
			if (dialog.getResultCode()==Dialog.RESULT_OK) {
				// change settings
				updateUISettings();
			}
			return;
		}
		if (cmd.equals("mnu.cpy.display")) {
			setClipboardContents(calcProc.getAccDisplay());
			return;
		}
		if (cmd.equals("mnu.cpy.log")) {
			if (isUseTabProtocol() && protocolTabular!=null) {
				protocolTabular.copyData();
			} else
			if (isUseTxtProtocol() && protocol!=null) {
				setClipboardContents(calcProc.getLogDisplay());
			}
			return;
		}
		if (DBGtesting) {
			if (cmd.equals("mnu.testdlg")) {
				btnGrpTestDialog.setSelected(true);
				return;
			}
			if (cmd.equals("mnu.viewstack")) {
				btnGrpStackWindow.setSelected(!btnGrpStackWindow.isSelected());
				return;
			}
		}
		processCommand(cmd);
	}

	void initKeyCommands() {
		// FIXME: maybe share the inputmaps?
		ProcessorUIActionFactory.fillMaps(calcProc, this.getRootPane());
		ProcessorUIActionFactory.fillMaps(calcProc, windowLog.getRootPane());
		ProcessorUIActionFactory.fillMaps(calcProc, windowMath.getRootPane());
	}

	void setClipboardContents(String aString) {
		StringSelection stringSelection = new StringSelection(aString);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, clipboardHandler);
	}

	/**
	 * Get the String residing on the clipboard.
	 *
	 * @return any text found on the Clipboard; if none found, return an
	 * empty String.
	 */
	public String getClipboardContents() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		//odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null)
				&& contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents
						.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				//highly unlikely since we are using a standard DataFlavor
				// ex.printStackTrace();
				if (logger!=null) logger.warning(
						"Clipboard flavor error: " + ex.getMessage());
			} catch (IOException ex) {
				// ex.printStackTrace();
				if (logger!=null) logger.warning(
						"Clipboard io error: " + ex.getMessage());
			}
		}
		return result;
	}

	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
		//atwpl.placeWindows();
		cfg.putRectangleValue(CfgCalc.KEY_UI_MAIN_WINDOW_BOUNDS, this.getBounds());
	}

	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
		//System.out.println("main: moved");
		//atwpl.placeWindows();
		cfg.putRectangleValue(CfgCalc.KEY_UI_MAIN_WINDOW_BOUNDS, this.getBounds());
		if (snowEngine!=null) {
			snowEngine.shake();
		}
	}

	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent e) {
		atwpl.placeWindows();
	}

	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e) {
		// nothing to be done
	}

	
	/**
	 * @param e
	 * @see de.admadic.calculator.processor.ProcessorEventListener#passedDisplayEvent(de.admadic.calculator.processor.ProcessorEvent)
	 */
	public void passedDisplayEvent(ProcessorEvent e) {
		if (DBGforce) System.err.println("displayEvent");
		if (textDisplay!=null) textDisplay.setText(calcProc.getDisplay());
		if (display!=null) {
			display.updateDisplay(calcProc.getDisplay());
		}
		if (displayCombo!=null) {
			displayCombo.updateOutput(
					calcProc.getDisplay(), calcProc.getStatusDisplay());
		}
		if (isValueInTitle()) {
			updateTitle();
		}
	}

	protected void updateListStack() {
		Stack<Object> stack = calcProc.getActionStack();
		DefaultListModel listModel = new DefaultListModel();

//		System.out.println("--- new stack:");

		for (int i = stack.size()-1; i>=0; i--) {
			Object o = stack.elementAt(i);
			String dsp;
			if (o instanceof String) {
				dsp = i + " " + (String)o;
				listModel.addElement(dsp);
			} else if (o instanceof CaDouble) {
				dsp = i + " " + ((CaDouble)o).toString();
				listModel.addElement(dsp);
			} else {
				dsp = i + " " + "<unknown>";
				listModel.addElement(dsp);
			}
//			System.out.println(dsp);
		}

//		System.out.println("---");

		listStack.setModel(listModel);
	}

	/**
	 * @param e
	 * @see de.admadic.calculator.processor.ProcessorEventListener#passedProtocolEvent(de.admadic.calculator.processor.ProcessorEvent)
	 */
	public void passedProtocolEvent(ProcessorEvent e) {
		if (DBGforce) System.err.println("protocolEvent");
		if (protocol!=null) {
			// protocol.setProtocolText(calcProc.getLogDisplay());
		}
	}

	/**
	 * @param e
	 * @see de.admadic.calculator.processor.ProcessorEventListener#passedStatusEvent(de.admadic.calculator.processor.ProcessorEvent)
	 */
	public void passedStatusEvent(ProcessorEvent e) {
		if (DBGforce) System.err.println("statusEvent (" + e.getMask() + ")");
		if ((e.getMask() & ProcessorEvent.MEMORY)!=0) {
			if (textStatus!=null) textStatus.setText(calcProc.getStatusDisplay());
			//if (memStatus!=null) memStatus.setSelected(calcProc.testMemDisplay());
			if (displayCombo!=null) {
				displayCombo.updateOutput(
						calcProc.getDisplay(), calcProc.getStatusDisplay());
			}
		}
		if ((e.getMask() & ProcessorEvent.STACK)!=0) {
			if (listStack!=null) {
				updateListStack();
			}
		}
		if ((e.getMask() & ProcessorEvent.ANGARGMODE)!=0) {
			switch (calcProc.getAngularArgMode()) {
			case SimpleProcessor.ANGULARARG_RAD:
				if (btnAngArgModRad!=null) btnAngArgModRad.setSelected(true);
				break;
			case SimpleProcessor.ANGULARARG_DEG:
				if (btnAngArgModDeg!=null) btnAngArgModDeg.setSelected(true);
				break;
			case SimpleProcessor.ANGULARARG_GRA:
				if (btnAngArgModGra!=null) btnAngArgModGra.setSelected(true);
				break;
			}
		}
	}


	/**
	 * @param b
	 * @see java.awt.Component#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
//		this.getRootPane().getGlassPane().setVisible(b);
	}


	/**
	 * @return Returns the valueInTitle.
	 */
	public boolean isValueInTitle() {
		return valueInTitle;
	}


	/**
	 * @param valueInTitle The valueInTitle to set.
	 */
	public void setValueInTitle(boolean valueInTitle) {
		this.valueInTitle = valueInTitle;
		updateTitle();
	}

	/**
	 * 
	 */
	public void updateTitle() {
		if (valueInTitle) {
			this.setTitle(calcProc.getDisplay().trim() + " :: " + appTitle);
		} else {
			this.setTitle(appTitle);
		}
	}

	/**
	 * @return Returns the useTabProtocol.
	 */
	public boolean isUseTabProtocol() {
		return useTabProtocol;
	}

	/**
	 * @param useTabProtocol The useTabProtocol to set.
	 */
	public void setUseTabProtocol(boolean useTabProtocol) {
		this.useTabProtocol = useTabProtocol;
		if (!useTabProtocol && windowLogTab!=null && windowLogTab.isVisible()) {
			windowLogTab.setVisible(false);
		}
	}

	/**
	 * @return Returns the useTxtProtocol.
	 */
	public boolean isUseTxtProtocol() {
		return useTxtProtocol;
	}

	/**
	 * @param useTxtProtocol The useTxtProtocol to set.
	 */
	public void setUseTxtProtocol(boolean useTxtProtocol) {
		this.useTxtProtocol = useTxtProtocol;
		if (!useTxtProtocol && windowLog!=null && windowLog.isVisible()) {
			windowLog.setVisible(false);
		}
	}
}
