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
package de.admadic.calculator.appmod;

import javax.swing.Action;
import javax.swing.JMenu;

import de.admadic.calculator.appctx.IAppContext;

/**
 * It is specifically allowed to call getModuleDesc before initialize.
 * A Module will be created by invoking the no-arg constructor of the modules
 * main class. This ctor should therefore allocate the ModuleDesc instance
 * and prepare for successful calling of initialize and/or getModuleDesc.
 * There should not be lengthy activity before the initialize method has been
 * called!
 * 
 * @author Rainer Schwarze
 *
 */
public interface IModule {
	// instantiation is done by calling a (void) ctor?
	
	/**
	 * @param appContext
	 */
	public abstract void initialize(IAppContext appContext);

	/**
	 * @return	Returns a module description.
	 */
	public abstract ModuleDesc getModuleDesc();

	/**
	 * @return	Returns the menu for the module, or null, if there is none.
	 */
	public abstract JMenu createModuleMenu();

	/**
	 * Returns the list of actions defined by the module.
	 * The list may be <code>null</code> or have zero elements.
	 * The first element is always the "SingleButtonAction" which is
	 * executed, when only one button is available to the user.
	 * All subsequent actions may be used in menus and the like.
	 * (Maybe the createModuleMenu is removed sooner or later so that
	 * the menu is created on base of the action list.)
	 * 
	 * @return	Returns the list of actions defined by the module.
	 */
	public abstract Action[] getActions();
}
