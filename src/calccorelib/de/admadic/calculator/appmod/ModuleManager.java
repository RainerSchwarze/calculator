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

import java.lang.reflect.Constructor;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import de.admadic.calculator.appctx.IAppContext;
import de.admadic.util.VersionRecord;

/**
 * @author Rainer Schwarze
 *
 */
public class ModuleManager {
	final static boolean LOG = true;
	final Logger logger = (LOG) ? Logger.getLogger("de.admadic") : null;

	Vector<IModule> modules;
	Vector<ModuleSpec> moduleSpecs; 

	/**
	 * 
	 */
	public ModuleManager() {
		super();
		modules = new Vector<IModule>();
		moduleSpecs = new Vector<ModuleSpec>();
	}

	/**
	 * @param module
	 */
	public void add(IModule module) {
		modules.add(module);
	}

	/**
	 * @param module
	 */
	public void remove(IModule module) {
		modules.remove(module);
	}

	/**
	 * @return	Returns the number of registered modules.
	 */
	public int getModuleCount() {
		return modules.size();
	}

	/**
	 * @param idx
	 * @return	Returns the module at the given index.
	 */
	public IModule getModule(int idx) {
		return modules.elementAt(idx);
	}

	/**
	 * @param modSpec
	 */
	public void add(ModuleSpec modSpec) {
		moduleSpecs.add(modSpec);
		if (logger!=null) logger.fine(
				"adding ModuleSpec: " + modSpec.getDiagnosticInfo());
	}

	/**
	 * @param modSpec
	 */
	public void remove(ModuleSpec modSpec) {
		moduleSpecs.remove(modSpec);
		if (logger!=null) logger.fine(
				"removing ModuleSpec: " + modSpec.getDiagnosticInfo());
	}

	/**
	 * @return	Returns the number of registered ModuleSpecs.
	 */
	public int getModuleSpecCount() {
		return moduleSpecs.size();
	}

	/**
	 * @param idx
	 * @return	Returns the ModuleSpec at the given index.
	 */
	public ModuleSpec getModuleSpec(int idx) {
		return moduleSpecs.elementAt(idx);
	}

	/**
	 * 
	 */
	public void collectDescriptions() {
		for (ModuleSpec ms : moduleSpecs) {
			if (!ms.isEnabled()) {
				if (logger!=null) logger.fine(
						"module skipped (not enabled): " + ms.getName());
				continue;
			}
			retrieveDescription(ms);
		}
	}

	private void retrieveDescription(ModuleSpec ms) {
		Class<?> cls;
		Constructor<?> ctr;
		Object obj;
		IModule imo;
		try {
			cls = this.getClass().getClassLoader().loadClass(ms.getClassName());
			ctr = cls.getConstructor((Class[])null);
			obj = ctr.newInstance((Object[])null);
			imo = (IModule)obj;
			this.add(imo);
			if (logger!=null) logger.fine(
					"module successfully accessed: " + ms.getName());
		} catch (Exception e) {
			// e.printStackTrace();
			if (logger!=null) logger.fine(
					"module could not be accessed, disabling: " + ms.getName() +
					" (cause: " + e.getClass().getName() + ": " + 
								e.getMessage() + ")");
			// doesn't work.
			ms.setEnabled(false);
		}
	}

	/**
	 * @param appContext
	 */
	public void initializeModules(IAppContext appContext) {
		// FIXME: there should be an exception wrapper to protect the main
		// application to go down just because of a module goes down
		for (IModule mod : modules) {
			mod.initialize(appContext);
		}
	}

	/**
	 * @param menu 
	 */
	public void createModulesMenu(JMenu menu) {
		int count = 0;

		for (IModule mod : modules) {
			JMenu modm = mod.createModuleMenu();
			if (modm==null) continue;
			menu.add(modm);
			count++;
		}
		if (count==0) {
			JMenuItem mi = new JMenuItem("<none>");
			mi.setEnabled(false);
			menu.add(mi);
		}
	}

	/**
	 * @param appVer
	 * @return	Returns a list of ModuleSpecs which failed the version
	 * requirements, or null, if no failing entries exist. 
	 */
	public Vector<ModuleSpec> validateVersionRequirements(VersionRecord appVer) {
		Vector<ModuleSpec> v = null;
		for (ModuleSpec ms : moduleSpecs) {
			if (!ms.isEnabled()) continue;
			VersionRecord modReqAppVer = ms.getRequiredAppVersion();
			if (modReqAppVer==null) continue;

			if (modReqAppVer.compareTo(appVer, VersionRecord.MICRO)>0) {
				if (v==null) { // lazily create the vector:
					v = new Vector<ModuleSpec>();
				}
				ms.setEnabled(false);
				v.add(ms);
			}
		}
		return v;
	}
}
