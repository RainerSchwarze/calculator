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
package de.admadic.calculator;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.admadic.calculator.appctx.AppContext;
import de.admadic.calculator.appmod.ModuleManager;
import de.admadic.calculator.core.LocaleManager;
import de.admadic.calculator.ui.CalcLaFManager;
import de.admadic.calculator.ui.CfgCalc;
import de.admadic.calculator.ui.SimpleCalcV0;
import de.admadic.calculator.ui.UpdateManager;
import de.admadic.laf.LaFManager;
import de.admadic.ui.util.SplashWindow;
import de.admadic.util.ClassPathExtender;
import de.admadic.util.PathManager;

/**
 * @author Rainer Schwarze
 *
 */
public class LaunchCalc {
	final static boolean LOG = true;
	Logger logger = null;

	AppContext appContext;
	ModuleManager modMan;
	
	PathManager pathMan;
	SplashWindow splash;
	ClassPathExtender cpe;
	CfgCalc cfg;
	CalcLog calcLog;
	LaFManager lafMan;
	LocaleManager locMan;

	/**
	 * Creates an empty LaunchCalc instance. 
	 */
	public LaunchCalc() {
		super();
	}

	/**
	 * Main entry for program start.
	 * 
	 * @param args	The arguments passed on the command line.
	 */
	public static void main(String[] args) {
		LaunchCalc launch = new LaunchCalc();
		launch.init(args);
		launch.initCpe();	// only when started here, not from LaunchBase!
		launch.run();
		launch.shutdown();
	}

	/**
	 * @param cpe
	 */
	public void setCpe(ClassPathExtender cpe) {
		this.cpe = cpe;
	}

	/**
	 * @param args
	 */
	public void init(String[] args) {
		// I dont want that warning for unused args...
		if (args!=null) { /* nothing*/ }

		pathMan = new PathManager();
		pathMan.init(
				"admadic", 
				"calculator", 
				Version.versionFS, 
				this.getClass());

		cfg = new CfgCalc();
		cfg.initCfgPaths(pathMan);

		calcLog = new CalcLog();
		calcLog.init(pathMan);
		
		UpdateManager updMan = new UpdateManager(cfg, pathMan);
		updMan.initLogging();
		if (updMan.isFresh()) {
			updMan.detectInstalledVersions();
			updMan.selectMergeSource();
		}
		cfg.initialize(true);
		if (updMan.shouldMerge()) {
			updMan.doMerge();
		}

		if (LOG) logger = Logger.getLogger("de.admadic");
	}
	
	/**
	 * 
	 */
	public void initCpe() {
		if (cpe!=null) {
			//cpe.registerToThread(null);
		} else {
			if (cfg.getBooleanValue(CfgCalc.KEY_CLASSPATH_USE_EXTENDER, false)) {
				cpe = new ClassPathExtender();
				String paths = cfg.getStringValue(
						CfgCalc.KEY_CLASSPATH_DIRS, null);
				if (paths!=null) {
					String [] pa = paths.split(":");
					for (String path : pa) {
						File dir = new File(
								pathMan.getPathString(PathManager.SYS_APP_DIR),
								path);
						if (dir.isDirectory()) {
							cpe.setURLs(dir, "*.jar");
						}
					}
					cpe.generateClassLoader();
				}
			}
		} // (cpe-handling)
	}

	/**
	 *
	 */
	void initSplash() {
        URL url = null;
        Icon picture = null;
        if (logger!=null) logger.fine("about to load the splash image");
        url = this.getClass().getClassLoader().getResource(
        		CfgCalc.RSC_SPLASH_IMAGE);
        if (url != null){
            if (logger!=null) logger.fine("creating ImageIcon");
            picture = new ImageIcon(url);
        } else {
            if (logger!=null) logger.fine("could not create splash image url");
        }

        if (logger!=null) logger.fine("creating splash window...");
        splash = new SplashWindow(
        		picture, 
        		null, //"Starting up", 
        		null, //"SplashWindowTest",
        		null, //"Registered to: xxx\n"+"Reg-No: xxx",
        		143);	// <- that depends on the image!
        splash.setDelayedClose(10000);
        splash.setUserClose(true);
        splash.setWaitForCompletion(true);
        if (logger!=null) logger.fine("splash window created...");
	}

	/**
	 * 
	 */
	public void run() {
		initSplash();
        splash.setVisible(true); // <- go!
        splash.repaint();

        splash.updateStatus("Initializing...", 1);
		UIManager.getDefaults().put(
				"Button.focusInputMap",
				new javax.swing.UIDefaults.LazyInputMap(new Object[] { 
						// the default input map contains ENTER for pressed 
						// handling. Interestingly all other look and feels
						// don't include that. Since we very much do NOT want
						// that for a calculator, we replace the parameters
						// at first.

//						"ENTER", "pressed", 
//						"released ENTER", "released", 
						"SPACE", "pressed", 
						"released SPACE", "released" 
				}));
		lafMan = null;
		if (cfg.getBooleanValue(CfgCalc.KEY_UI_LAF_ENABLE, false)) {
	        if (logger!=null) logger.fine("init LaF...");
	        if (logger!=null) logger.fine("cpe = " + cpe);
			lafMan = new LaFManager();
			CalcLaFManager.initLaFManager(
					lafMan, 
					cfg, 
					(cpe!=null) ? 
							cpe.getClassLoader() : 
							this.getClass().getClassLoader(),
					pathMan
				);
		}

		// now build AppContext:
		appContext = new AppContext(cfg, pathMan, lafMan);
 		appContext.setCfgModulesPrefix(CfgCalc.PREFIX_MODULES);
		modMan = new ModuleManager();
		appContext.setModuleManager(modMan);
		appContext.setSplashWindow(splash);

		locMan = new LocaleManager();
		// locMan is set up in main app:
		appContext.setLocaleManager(locMan);
		
        splash.updateStatus("Initializing UI...", 10);
        // Swing should be up and running, so the EDT should be there.
        // -> Register the cpe to the EDT:

        // SimpleCalc is now handling the AppContext. No more write
        // access to it from this place.
        
        try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					if (cpe!=null) {
						//cpe.registerToThread(null);
					}
				    SimpleCalcV0 inst = new SimpleCalcV0(appContext);
				    inst.start();
				}
			});
		} catch (InterruptedException e) {
			// e.printStackTrace();
			// ignore that
			if (splash!=null) splash.notifyCompleted();
		} catch (InvocationTargetException e) {
			// e.printStackTrace();
			Throwable cause = e.getCause();
			String msg = e.getClass().getName() + ": " + e.getMessage();
			if (cause!=null) msg += "\n" + cause.getMessage();
			if (splash!=null) splash.notifyCompleted();
			JOptionPane.showMessageDialog(
					null,
					"Error starting the User Interface of the calculator:\n" + 
					msg + "\n"+
					"Please contact customer support.",
					"Error Starting the Calculator",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/**
	 * 
	 */
	public void shutdown() {
		// nothing for now
	}
}
