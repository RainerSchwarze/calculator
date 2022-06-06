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
package de.admadic.calculator.modules.matrx.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import de.admadic.calculator.appctx.IAppContext;
import de.admadic.calculator.modules.matrx.MatrxActions;
import de.admadic.calculator.modules.matrx.MatrxCfg;
import de.admadic.calculator.modules.matrx.core.DMatrix;
import de.admadic.ui.util.AboutDialog;
import de.admadic.ui.util.Dialog;

/**
 * @author Rainer Schwarze
 *
 */
public class MatrxFrame extends JFrame implements 
		ActionListener, ICfgHandler {
	/** */
	private static final long serialVersionUID = 1L;
	final static boolean LOG = true;
	Logger logger = (LOG) ? Logger.getLogger("de.admadic") : null;

	MatrxCfg cfg; 
	MatrxActions actions; 
	IAppContext appContext;

	FloatingPointFormatter fpf;	// global number formatter
	
	CopyPasteCore copyPasteCore;

	JTabbedPane tabbedPaneMain;
	
	JPanel panelMain;
	SimpleOpPanel panelSimpleOp;
	BinaryOpPanel panelBinaryOp;
	EquSlvOpPanel panelEquSlvOp;
	final static int TABIDX_SIMPLE = 0;
	final static int TABIDX_BINARY = 1;
	final static int TABIDX_EQUSLV = 2;

	DMatrix matrixMain;
	MatrixPanel panelMatrix;
	MatrixPanel panelMatrixRes;
	MatrixPanel panelMatrixVec;
	
	ImageIcon iconLarge;
	ImageIcon iconSmall;
	
	boolean setupComplete = false;
	boolean allowStoreSettings = false;
	
	final static String CMD_FILE_EXIT = "mnu.file.exit";

	final static String CMD_EDIT_COPY = "mnu.edit.copy";
	final static String CMD_EDIT_PASTE = "mnu.edit.paste";

	final static String CMD_TOOLS_OPTIONS = "mnu.tools.options";

	final static String CMD_HELP_ABOUT = "mnu.help.about";

	/**
	 * @param cfg 
	 * @param actions 
	 * @param appContext 
	 * @throws HeadlessException
	 */
	public MatrxFrame(MatrxCfg cfg, MatrxActions actions, IAppContext appContext) 
	throws HeadlessException {
		super();
		this.cfg = cfg;
		this.actions = actions;
		this.appContext = appContext;
	}

	/**
	 * 
	 */
	public void initCore() {
		String msrFmt = cfg.getDisplayFormat();
		if (msrFmt==null) {
			msrFmt = "%g";
		}
		fpf = FloatingPointFormatter.createFormatter(msrFmt, Locale.getDefault());
	}

	private void addMenuItem(JMenu menu, String name, String aCmd) {
		JMenuItem mi = new JMenuItem(name);
		menu.add(mi);
		mi.setActionCommand(aCmd);
		mi.addActionListener(this);
	}

	/**
	 * @param name
	 * @return	Returns the icon
	 */
	protected ImageIcon loadIcon(String name) {
		String rname = "de/admadic/calculator/modules/matrx/res/" + name;
		java.net.URL url = this.getClass().getClassLoader().getResource(
				rname);
		if (url==null) {
			if (logger!=null) logger.warning(
					"could not get url for " + rname + 
					" (icon name = " + name + ")");
			return null;
		}
		Image img = Toolkit.getDefaultToolkit().getImage(url);
		if (img==null) {
			if (logger!=null) logger.warning(
					"could not get image for " + url.toString() + 
					" (icon name = " + name + ")");
			return null;
		}
		return new ImageIcon(img);
	}

	/**
	 * 
	 */
	public void initComponents() {
		initCore();

		iconSmall = loadIcon("icon-16.png");
		iconLarge = loadIcon("icon-48.png");
		panelMain = (JPanel)this.getContentPane();
		if (iconSmall!=null) {
			this.setIconImage(iconSmall.getImage());
		}
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			/**
			 * @param e
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				setVisible(false);
				//dispose();
			}
		});

		this.setTitle("Matrx Module");
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu;
		menuBar = new JMenuBar();
		menuBar.add(menu = new JMenu("File"));
		// we call it close, since we do not exit the application:
		addMenuItem(menu, "Close", CMD_FILE_EXIT);

		menuBar.add(menu = new JMenu("Edit"));
		addMenuItem(menu, "Copy", CMD_EDIT_COPY);
		addMenuItem(menu, "Paste", CMD_EDIT_PASTE);

		if (actions!=null && actions.getActions()!=null) {
			JMenu mm = new JMenu("Module");
			menuBar.add(mm);
			// start with the 2nd entry!
			for (int i=1; i<actions.getActions().size(); i++) {
				mm.add(new JMenuItem(actions.getActions().get(i)));
			}
		}
		menuBar.add(menu = new JMenu("Help"));
		addMenuItem(menu, "About...", CMD_HELP_ABOUT);
		this.setJMenuBar(menuBar);
		
		panelMain.setLayout(new BorderLayout());
		tabbedPaneMain = new JTabbedPane();
		panelMain.add(tabbedPaneMain, BorderLayout.CENTER);

		panelSimpleOp = new SimpleOpPanel(fpf, cfg, this);
		panelBinaryOp = new BinaryOpPanel(fpf, cfg, this);
		panelEquSlvOp = new EquSlvOpPanel(fpf, cfg, this);
		
		tabbedPaneMain.addTab("Simple", panelSimpleOp);
		tabbedPaneMain.addTab("Binary", panelBinaryOp);
		tabbedPaneMain.addTab("Equation Solver", panelEquSlvOp);

		this.pack();
		setLocationRelativeTo(null);

		loadSettings();
	}

	/**
	 * 
	 */
	public void doHelpAbout() {
		AboutDialog dialog = new AboutDialog(this);
		dialog.setAboutInfo(
				"admaDIC Calculator Module\n"+
				"Matrx - Matrix Calculations\n"+
				"Version " + 
				de.admadic.calculator.modules.matrx.Version.version,
				"Copyright (c) 2005-2022 - admaDIC\n"+
				"Germany",
				"For updates or more information \n"+
				"please visit http://www.admadic.de/",
				"This product includes the software JGoodies Forms:\n"+
				"Copyright (c) 2002-2004 JGoodies Karsten Lentzsch.\n"+
				"All rights reserved.");	// no soft libs
		dialog.setTitle("About Matrx Module");
		if (iconLarge!=null) {
			dialog.setLogo(iconLarge.getImage());
		}
		dialog.setVisible(true);
	}

	/**
	 * @return	Returns setupComplete
	 */
	public boolean isSetupComplete() {
		return setupComplete;
	}

	/**
	 * @param setupComplete
	 */
	public void setSetupComplete(boolean setupComplete) {
		this.setupComplete = setupComplete;
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_FILE_EXIT)) {
			doExit();
		} else if (cmd.equals(CMD_EDIT_COPY)) {
			doEditCopy();
		} else if (cmd.equals(CMD_EDIT_PASTE)) {
			doEditPaste();
		} else if (cmd.equals(CMD_HELP_ABOUT)) {
			doHelpAbout();
		} else if (cmd.equals(CMD_TOOLS_OPTIONS)) {
			doToolsOptions();
		}
	}

	private void doEditCopy() {
		Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager()
						.getPermanentFocusOwner();
		if (copyPasteCore==null) copyPasteCore = new CopyPasteCore();
		if (c instanceof JTextField) {
			JTextField tf = (JTextField)c;
			copyPasteCore.doCopyFrom(tf);
		}
		if (c instanceof JTable) {
			JTable table = (JTable)c;
			TabularCopyPasteCore tcpc = new TabularCopyPasteCore();
			tcpc.doCopyFrom(table);
		}
	}

	private void doEditPaste() {
		Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager()
						.getPermanentFocusOwner();
		if (copyPasteCore==null) copyPasteCore = new CopyPasteCore();
		if (c instanceof JTextField) {
			JTextField tf = (JTextField)c;
			copyPasteCore.doPasteInto(tf);
		}
		if (c instanceof JTable) {
			JTable table = (JTable)c;
			
			TabularCopyPasteCore tcpc = new TabularCopyPasteCore();
			tcpc.doPasteInto(table, false);
		}
	}

	// protected because of anonymous class access
	protected void doExit() {
		this.dispose();
	}

	/**
	 * 
	 */
	public void doToolsOptions() {
		SettingsDialog dlg = new SettingsDialog(this, cfg);
		dlg.initContents();
		dlg.loadSettings();
		dlg.setVisible(true);
		int rc = dlg.getResultCode();
		if (rc==Dialog.RESULT_OK) {
			String msrFmt = cfg.getDisplayFormat();
			if (msrFmt==null) {
				msrFmt = "%g";
			}
			fpf.setFormatString(msrFmt);
		}
	}

	/**
	 * Stores the settings which are not automatically saved.
	 * (Applies to Matrix data)
	 */
	public void storeSettings() {
		if (!allowStoreSettings) return;
		DMatrix [] ma = new DMatrix[MatrxCfg.MTXCNT];
		ma[MatrxCfg.MTXIDX_SPL_INP] = panelSimpleOp.mtxInput;
		ma[MatrxCfg.MTXIDX_SPL_RES] = panelSimpleOp.mtxResult;
		ma[MatrxCfg.MTXIDX_BIN_INP] = panelBinaryOp.mtxInput;
		ma[MatrxCfg.MTXIDX_BIN_PAR] = panelBinaryOp.mtxParam;
		ma[MatrxCfg.MTXIDX_BIN_RES] = panelBinaryOp.mtxResult;
		ma[MatrxCfg.MTXIDX_EQU_COE] = panelEquSlvOp.mtxInputCoeff;
		ma[MatrxCfg.MTXIDX_EQU_ABS] = panelEquSlvOp.mtxInputAbsEl;
		ma[MatrxCfg.MTXIDX_EQU_GAU] = panelEquSlvOp.mtxResultGauss;
		ma[MatrxCfg.MTXIDX_EQU_SOL] = panelEquSlvOp.mtxResultSol;
		cfg.putMtxDataContent(ma);
	}

	/**
	 * Loads the settings which are not automatically read.
	 * (Applies to Matrix data)
	 */
	public void loadSettings() {
		DMatrix [] ma = cfg.getMtxDataContent();
		allowStoreSettings = true;
		if (ma==null) return;
		if (ma.length<1) return;
		panelSimpleOp.setInputMatrix(ma[MatrxCfg.MTXIDX_SPL_INP]);
		panelSimpleOp.setResultMatrix(ma[MatrxCfg.MTXIDX_SPL_RES]);
		panelBinaryOp.setFirstInputMatrix(ma[MatrxCfg.MTXIDX_BIN_INP]);
		panelBinaryOp.setSecondInputMatrix(ma[MatrxCfg.MTXIDX_BIN_PAR]);
		panelBinaryOp.setResultMatrix(ma[MatrxCfg.MTXIDX_BIN_RES]);
		panelEquSlvOp.setInputMatrixCoeff(ma[MatrxCfg.MTXIDX_EQU_COE]);
		panelEquSlvOp.setInputMatrixAbsEl(ma[MatrxCfg.MTXIDX_EQU_ABS]);
		panelEquSlvOp.setResultMatrixGauss(ma[MatrxCfg.MTXIDX_EQU_GAU]);
		panelEquSlvOp.setResultMatrixSol(ma[MatrxCfg.MTXIDX_EQU_SOL]);
	}

	/**
	 * 
	 * @see de.admadic.calculator.modules.matrx.ui.ICfgHandler#notifyCfgUpdate()
	 */
	public void notifyCfgUpdate() {
		storeSettings();
	}
}
