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
package de.admadic.calculator.appctx;

import de.admadic.calculator.appmod.ModuleManager;
import de.admadic.calculator.core.LocaleManager;
import de.admadic.laf.LaFManager;
import de.admadic.cfg.Cfg;
import de.admadic.ui.util.ISplashWindow;
import de.admadic.util.PathManager;

/**
 * @author Rainer Schwarze
 *
 */
public class AppContext implements IAppContext, IAppController {
	Cfg cfg;
	PathManager pathManager;
	LaFManager lafManager;
	ModuleManager moduleManager;
	ISplashWindow splashWindow;
	String cfgModulesPrefix;
	LocaleManager localeManager;

	AppEventListenerSupport aels;

	/**
	 * @param cfg
	 * @param pathMan
	 * @param lafMan
	 */
	public AppContext(Cfg cfg, PathManager pathMan, LaFManager lafMan) {
		super();
		this.cfg = cfg;
		this.pathManager = pathMan;
		this.lafManager = lafMan;
		this.moduleManager = null;
		aels = new AppEventListenerSupport(this, this);
	}

	/**
	 * @param cfg
	 * @param pathMan
	 * @param lafMan
	 * @param modMan
	 */
	public AppContext(
			Cfg cfg, PathManager pathMan, 
			LaFManager lafMan, ModuleManager modMan) {
		super();
		this.cfg = cfg;
		this.pathManager = pathMan;
		this.lafManager = lafMan;
		this.moduleManager = modMan;
		aels = new AppEventListenerSupport(this, this);
	}

	/**
	 * @return	Returns the Cfg instance.
	 * @see de.admadic.calculator.appctx.IAppContext#getCfg()
	 */
	public Cfg getCfg() {
		return cfg;
	}

	/**
	 * @param cfg The cfg to set.
	 */
	public void setCfg(Cfg cfg) {
		this.cfg = cfg;
	}

	/**
	 * @return	Returns the LaFManager
	 * @see de.admadic.calculator.appctx.IAppContext#getLafManager()
	 */
	public LaFManager getLafManager() {
		return lafManager;
	}

	/**
	 * @param lafManager The lafManager to set.
	 */
	public void setLafManager(LaFManager lafManager) {
		this.lafManager = lafManager;
	}

	/**
	 * @return	Returns the PathManager.
	 * @see de.admadic.calculator.appctx.IAppContext#getPathManager()
	 */
	public PathManager getPathManager() {
		return pathManager;
	}

	/**
	 * @param pathManager The pathManager to set.
	 */
	public void setPathManager(PathManager pathManager) {
		this.pathManager = pathManager;
	}


	/**
	 * @param l
	 * @see de.admadic.calculator.appctx.IAppContext#addAppListener(de.admadic.calculator.appctx.AppEventListener)
	 */
	public void addAppListener(AppEventListener l) {
		aels.addAppListener(l);
	}


	/**
	 * @param l
	 * @see de.admadic.calculator.appctx.IAppContext#removeAppListener(de.admadic.calculator.appctx.AppEventListener)
	 */
	public void removeAppListener(AppEventListener l) {
		aels.removeAppListener(l);
	}


	/**
	 * FIXME: should we put this in the IAppContext interface? maybe not...
	 * @return Returns the moduleManager.
	 */
	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	/**
	 * @param moduleManager The moduleManager to set.
	 */
	public void setModuleManager(ModuleManager moduleManager) {
		this.moduleManager = moduleManager;
	}

	/**
	 * @return Returns the splashWindow.
	 */
	public ISplashWindow getSplashWindow() {
		return splashWindow;
	}

	/**
	 * @param splashWindow The splashWindow to set.
	 */
	public void setSplashWindow(ISplashWindow splashWindow) {
		this.splashWindow = splashWindow;
	}


	/**
	 * @param phase
	 * @throws CancelPhaseException
	 * @see de.admadic.calculator.appctx.IAppController#fireAppEvent(int)
	 */
	public void fireAppEvent(int phase) throws CancelPhaseException {
		aels.fireAppEvent(phase);
	}

	/**
	 * @param phase
	 * @see de.admadic.calculator.appctx.IAppController#fireAppEventNoExc(int)
	 */
	public void fireAppEventNoExc(int phase) {
		try {
			aels.fireAppEvent(phase);
		} catch (CancelPhaseException e) { /* nothing */ }
	}


	/**
	 * @return	Returns the prefix for modules settings.
	 * @see de.admadic.calculator.appctx.IAppContext#getCfgModulesPrefix()
	 */
	public String getCfgModulesPrefix() {
		return cfgModulesPrefix;
	}


	/**
	 * @param cfgModulesPrefix The cfgModulesPrefix to set.
	 */
	public void setCfgModulesPrefix(String cfgModulesPrefix) {
		this.cfgModulesPrefix = cfgModulesPrefix;
	}


	/**
	 * @return Returns the localeManager.
	 * @see de.admadic.calculator.appctx.IAppContext#getLocaleManager()
	 */
	public LocaleManager getLocaleManager() {
		return localeManager;
	}


	/**
	 * @param localeManager The localeManager to set.
	 */
	public void setLocaleManager(LocaleManager localeManager) {
		this.localeManager = localeManager;
	}
}
