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
package de.admadic.calculator.modules.masca;

import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import de.admadic.calculator.appctx.AppEvent;
import de.admadic.calculator.appctx.AppEventListener;
import de.admadic.calculator.appctx.CancelPhaseException;
import de.admadic.calculator.appctx.IAppContext;
import de.admadic.calculator.appmod.IModule;
import de.admadic.calculator.appmod.ModuleDesc;
import de.admadic.calculator.modules.masca.core.CalcManager;
import de.admadic.calculator.modules.masca.core.CalcSpecIo;
import de.admadic.calculator.modules.masca.ui.MaSCaFrame;
import de.admadic.cfg.Cfg;
import de.admadic.units.core.UnitManager;
import de.admadic.units.core.UnitsIo;

/**
 * @author Rainer Schwarze
 *
 */
public class MaSCaModule implements 
		IModule, AppEventListener, 
		MaSCaItf, ComponentListener {
	final static boolean LOG = true;
	Logger logger = (LOG) ? Logger.getLogger("de.admadic") : null;

	UnitManager um;
	CalcManager cm;
	MaSCaFrame frame;
	ModuleDesc moduleDesc;
	IAppContext appContext;
	Cfg cfg;
	MaSCaCfg modCfg;
	MaSCaActions actions;

	/**
	 * 
	 */
	public MaSCaModule() {
		super();
		createModuleDesc();
	}

	private void createModuleDesc() {
		moduleDesc = new ModuleDesc();
		moduleDesc.setImplDetails("admaDIC", "MaSCa", Version.versionLC);
		moduleDesc.setSpecDetails("admaDIC", "MaSCa", Version.versionLC);
		moduleDesc.setCfgName(MaSCaCfg.MODNAME);
	}

	/**
	 * @return	Returns the ModuleDesc.
	 * @see de.admadic.calculator.appmod.IModule#getModuleDesc()
	 */
	public ModuleDesc getModuleDesc() {
		return moduleDesc;
	}

	/**
	 * @param appContext
	 * @see de.admadic.calculator.appmod.IModule#initialize(de.admadic.calculator.appctx.IAppContext)
	 */
	public void initialize(IAppContext appContext) {
		this.appContext = appContext;
		this.cfg = appContext.getCfg();
		actions = new MaSCaActions();
		modCfg = new MaSCaCfg(cfg);
		modCfg.setPrefix(appContext.getCfgModulesPrefix());
		modCfg.checkPrevCfg();
		actions.initialize(this);
		appContext.addAppListener(this);
	}

	/**
	 * @return	Returns the created menu, or null, if there is none.
	 * @see de.admadic.calculator.appmod.IModule#createModuleMenu()
	 */
	public JMenu createModuleMenu() {
		JMenu m = new JMenu("MaSCa");
		JMenuItem mi;

		if (actions!=null && actions.getActions()!=null) {
			// ignore the 1st!
			for (int i=1; i<actions.getActions().size(); i++) {
				mi = new JMenuItem(actions.getActions().get(i));
				m.add(mi);
			}
		}
		return m;
	}

	/**
	 * @return	Returns the array of Actions.
	 * @see de.admadic.calculator.appmod.IModule#getActions()
	 */
	public Action[] getActions() {
		return actions.getActionArray();
	}

	/**
	 * @param ae
	 * @throws CancelPhaseException
	 * @see de.admadic.calculator.appctx.AppEventListener#processPhase(de.admadic.calculator.appctx.AppEvent)
	 */
	public void processPhase(AppEvent ae) throws CancelPhaseException {
		switch (ae.getPhase()) {
		case AppEvent.PHASE_INIT_CORE: break;
		case AppEvent.PHASE_INIT_FRAME: doInitFrame(); break;
		case AppEvent.PHASE_SHOW_FRAME: doShowFrame(); break;
		case AppEvent.PHASE_SHOW_ALL: break;
		case AppEvent.PHASE_RUN: break;
		case AppEvent.PHASE_TRY_EXIT: checkExit(); break;
		case AppEvent.PHASE_EXIT: doExit(); break;
		default:
			// unknown phase.
		}
	}

	private void readUnitsResource(UnitsIo uio, String resName) {
		String resBases[] = {
				"de/admadic/calculator/modules/masca/res/",
				"de/admadic/calculator/units/"
		};
		for (String resBase : resBases) {
			URL url;
			url = this.getClass().getClassLoader().getResource(
					resBase + resName);
			if (url==null) {
				if (logger!=null) logger.severe(
						"Could not access standard unit definition." +
								" (" + resName + ")");
			} else {
				uio.readFile(url);
				return;
			}
		}
		showError(
				"Could not access standard unit definition." +
						" (" + resName + ")" + "\n"+
						"Please contact customer support.");
	}

	private void readCalcResource(CalcSpecIo cio, String resName) {
		String resBases[] = {
				"de/admadic/calculator/modules/masca/res/",
				"de/admadic/calculator/units/"
		};
		for (String resBase : resBases) {
			URL url;
			url = this.getClass().getClassLoader().getResource(
					resBase + resName);
			if (url==null) {
				if (logger!=null) logger.severe(
						"Could not access standard calculations definition." +
								" (" + resName + ")");
			} else {
				cio.readFile(url);
				return;
			}
		}
		showError(
				"Could not access standard calculations definition."+
						" (" + resName + ")" + "\n"+
						"Please contact customer support.");
	}

	private void doLoadData() {
		UnitsIo uio = new UnitsIo(um);
		CalcSpecIo cio = new CalcSpecIo(cm, um);
		readUnitsResource(uio, "unitstdset.xml");
		readUnitsResource(uio, "quantdef.xml");
		readCalcResource(cio, "calcspec.xml");
//		uio.readFile(new File("./src/de/admadic/calculator/units/unitstdset-save.xml"));
//		uio.readFile(new File("./src/de/admadic/calculator/units/quantdef.xml"));

		frame.setSetupComplete(true);
		frame.initTreeData();
	}
	
	private void doInitFrame() {
		um = new UnitManager();
		cm = new CalcManager();
		frame = new MaSCaFrame(um, cm, modCfg, actions, appContext);
		frame.initComponents();
		if (appContext!=null && appContext.getLafManager()!=null) {
			appContext.getLafManager().addComponent(frame);
		}
		frame.addComponentListener(this);
	}

	private void showError(String msg) {
		JOptionPane.showMessageDialog(
				frame,	// maybe null, but then still ok.
				msg,
				"MaSCa Module",
				JOptionPane.ERROR_MESSAGE);
	}

	private void doShowFrame() {
		Rectangle r = modCfg.getPos();
		if (r!=null) {
			frame.setBounds(r);
		}
		boolean b = modCfg.getShow(true);
		frame.setVisible(b);
	}

	private void checkExit() throws CancelPhaseException {
		// nope, nothing to check
		if (true==false) throw new CancelPhaseException();
//		if (frame.getData().isDirty()) {
//			if (!frame.checkSave()) {
//				throw new CancelPhaseException("User prohibited exit");
//			}
//		}
	}

	private void doExit() {
		frame.dispose();
	}

	/**
	 * 
	 * @see de.admadic.calculator.modules.masca.MaSCaItf#cmdSingleButton()
	 */
	public void cmdSingleButton() {
		frame.setVisible(!frame.isVisible());
	}

	/**
	 * 
	 * @see de.admadic.calculator.modules.masca.MaSCaItf#cmdShow()
	 */
	public void cmdShow() {
		if (!frame.isVisible()) frame.setVisible(true);
	}

	/**
	 * 
	 * @see de.admadic.calculator.modules.masca.MaSCaItf#cmdHide()
	 */
	public void cmdHide() {
		if (frame.isVisible()) frame.setVisible(false);
	}

	/**
	 * 
	 * @see de.admadic.calculator.modules.masca.MaSCaItf#cmdSettings()
	 */
	public void cmdSettings() {
		frame.doToolsOptions();
	}

	/**
	 * 
	 * @see de.admadic.calculator.modules.masca.MaSCaItf#cmdAbout()
	 */
	public void cmdAbout() {
		frame.doHelpAbout();
	}

	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
		modCfg.putPos(frame.getBounds());
	}

	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
		modCfg.putPos(frame.getBounds());
	}

	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent e) {
		modCfg.putPos(frame.getBounds());
		modCfg.putShow(frame.isVisible());
		if (!frame.isSetupComplete()) {
			doLoadData();
		}
	}

	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e) {
		modCfg.putShow(frame.isVisible());
	}
}
