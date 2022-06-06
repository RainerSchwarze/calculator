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
package de.admadic.calculator.modules.indxp;

import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import de.admadic.calculator.appctx.AppEvent;
import de.admadic.calculator.appctx.AppEventListener;
import de.admadic.calculator.appctx.CancelPhaseException;
import de.admadic.calculator.appctx.IAppContext;
import de.admadic.calculator.appmod.IModule;
import de.admadic.calculator.appmod.ModuleDesc;
import de.admadic.calculator.modules.indxp.ui.InDXpFrame;
import de.admadic.cfg.Cfg;


/**
 * @author Rainer Schwarze
 *
 */
public class InDXpModule implements 
		IModule, AppEventListener, 
		ComponentListener, InDXpItf {
	ModuleDesc moduleDesc;
	InDXpFrame frame;
	IAppContext appContext;
	Cfg cfg;
	InDXpCfg modCfg;
	InDXpActions actions;

	/**
	 * 
	 */
	public InDXpModule() {
		super();
		createModuleDesc();
	}

	private void createModuleDesc() {
		moduleDesc = new ModuleDesc();
		moduleDesc.setImplDetails("admaDIC", "InDXp", Version.versionLC);
		moduleDesc.setSpecDetails("admaDIC", "InDXp", Version.versionLC);
		moduleDesc.setCfgName(InDXpCfg.MODNAME);
	}


	/**
	 * @param appContext
	 * @see de.admadic.calculator.appmod.IModule#initialize(de.admadic.calculator.appctx.IAppContext)
	 */
	public void initialize(IAppContext appContext) {
		this.appContext = appContext;
		this.cfg = appContext.getCfg();
		actions = new InDXpActions();
		modCfg = new InDXpCfg(cfg);
		modCfg.setPrefix(appContext.getCfgModulesPrefix());
		modCfg.checkPrevCfg();
		initActions();
		appContext.addAppListener(this);
	}

	/**
	 * Initialize the actions which are externally available.
	 */
	protected void initActions() {
		actions.initialize(this);
	}
	
	/**
	 * @return	Returns the module description.
	 * @see de.admadic.calculator.appmod.IModule#getModuleDesc()
	 */
	public ModuleDesc getModuleDesc() {
		return moduleDesc;
	}

	/**
	 * @return	Returns the menu for the module.
	 * @see de.admadic.calculator.appmod.IModule#createModuleMenu()
	 */
	public JMenu createModuleMenu() {
		JMenu m = new JMenu("InDXp");
		JMenuItem mi;

		if (actions!=null && actions.getActions()!=null) {
			// ignore the 1st!
			for (int i=1; i<actions.getActions().size(); i++) {
				mi = new JMenuItem(actions.getActions().get(i));
				m.add(mi);
			}
		}
		
//		mi = new JMenuItem("Show");
//		mi.addActionListener(this);
//		mi.setActionCommand(CMD_SHOW);
//		m.add(mi);
//		mi = new JMenuItem("Close");
//		mi.addActionListener(this);
//		mi.setActionCommand(CMD_CLOSE);
//		m.add(mi);
//		mi = new JMenuItem("Settings...");
//		mi.addActionListener(this);
//		mi.setActionCommand(CMD_SETTINGS);
//		m.add(mi);
//		mi = new JMenuItem("Register...");
//		mi.addActionListener(this);
//		mi.setActionCommand(CMD_SETTINGS);
//		m.add(mi);

		return m;
	}

	/**
	 * @return	Returns the actions supported by the module.
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

	private void doInitFrame() {
		frame = new InDXpFrame(modCfg, actions, appContext);
		if (appContext!=null && appContext.getLafManager()!=null) {
			appContext.getLafManager().addComponent(frame);
		}
		frame.addComponentListener(this);
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
		if (frame.getData().isDirty()) {
			if (!frame.checkSave()) {
				throw new CancelPhaseException("User prohibited exit");
			}
		}
	}

	private void doExit() {
		frame.dispose();
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
	}


	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e) {
		modCfg.putShow(frame.isVisible());
	}


	/**
	 * 
	 * @see de.admadic.calculator.modules.indxp.InDXpItf#cmdShow()
	 */
	public void cmdShow() {
		if (!frame.isVisible()) frame.setVisible(true);
	}


	/**
	 * 
	 * @see de.admadic.calculator.modules.indxp.InDXpItf#cmdHide()
	 */
	public void cmdHide() {
		if (frame.isVisible()) frame.setVisible(false);
	}


	/**
	 * 
	 * @see de.admadic.calculator.modules.indxp.InDXpItf#cmdSingleButton()
	 */
	public void cmdSingleButton() {
		frame.setVisible(!frame.isVisible());
	}


	/**
	 * 
	 * @see de.admadic.calculator.modules.indxp.InDXpItf#cmdSettings()
	 */
	public void cmdSettings() {
		frame.doToolsOptions();
	}


	/**
	 * 
	 * @see de.admadic.calculator.modules.indxp.InDXpItf#cmdAbout()
	 */
	public void cmdAbout() {
		frame.doHelpAbout();
	}
}
