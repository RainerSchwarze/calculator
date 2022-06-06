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
public interface IAppContext {

	/**
	 * @return Returns the cfg.
	 */
	public abstract Cfg getCfg();

	/**
	 * @return Returns the lafManager.
	 */
	public abstract LaFManager getLafManager();

	/**
	 * @return Returns the pathManager.
	 */
	public abstract PathManager getPathManager();

	/**
	 * @return	Returns the ModuleManager if there is one.
	 */
	public abstract ModuleManager getModuleManager();

	/**
	 * @return	Returns the LocaleManager, if there is one.
	 */
	public abstract LocaleManager getLocaleManager();
	
	/**
	 * Note: use the prefix as follows: PREFIX + KEY.
	 * If this method returns null, treat it as "". 
	 * @return	Returns the prefix which goes before the settings of modules.
	 */
	public abstract String getCfgModulesPrefix();
	
	/**
	 * @return	Returns the SplashWindow if there is one.
	 */
	public abstract ISplashWindow getSplashWindow();
	
	/**
	 * @param l
	 * @see de.admadic.calculator.appctx.AppEventListenerSupport#addAppListener(de.admadic.calculator.appctx.AppEventListener)
	 */
	public abstract void addAppListener(AppEventListener l);

	/**
	 * @param l
	 * @see de.admadic.calculator.appctx.AppEventListenerSupport#removeAppListener(de.admadic.calculator.appctx.AppEventListener)
	 */
	public abstract void removeAppListener(AppEventListener l);
}
