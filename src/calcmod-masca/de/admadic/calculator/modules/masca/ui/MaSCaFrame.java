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
package de.admadic.calculator.modules.masca.ui;

import java.awt.Component;
import java.awt.GridLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.appctx.IAppContext;
import de.admadic.calculator.modules.masca.MaSCaActions;
import de.admadic.calculator.modules.masca.MaSCaCfg;
import de.admadic.calculator.modules.masca.core.AbstractCalculation;
import de.admadic.calculator.modules.masca.core.CalcManager;
import de.admadic.ui.util.AboutDialog;
import de.admadic.ui.util.Dialog;
import de.admadic.units.core.MeasureFormatter;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class MaSCaFrame extends JFrame implements 
		TreeSelectionListener, ActionListener {
	/** */
	private static final long serialVersionUID = 1L;
	final static boolean LOG = true;
	Logger logger = (LOG) ? Logger.getLogger("de.admadic") : null;

	UnitManager um;
	CalcManager cm;
	MaSCaCfg cfg; 
	MaSCaActions actions; 
	IAppContext appContext;

	MeasureFormatter mf;	// global measure formatter.

	JPanel panelMain;
	JPanel panelContent;
	JScrollPane scrollContent;
	CalcPanel panelCalc;
	JTree treeSelector;
	JScrollPane scrollSelector;

	ImageIcon iconLarge;
	ImageIcon iconSmall;
	
	boolean setupComplete = false;
	
	AbstractCalculation activeCalcEngine;
	CopyPasteCore copyPasteCore;

	final static String CMD_FILE_EXIT = "mnu.file.exit";

	final static String CMD_EDIT_COPY = "mnu.edit.copy";
	final static String CMD_EDIT_PASTE = "mnu.edit.paste";

	final static String CMD_TOOLS_OPTIONS = "mnu.tools.options";

	final static String CMD_HELP_ABOUT = "mnu.help.about";

	/**
	 * @param um 
	 * @param cm 
	 * @param cfg 
	 * @param actions 
	 * @param appContext 
	 * @throws HeadlessException
	 */
	public MaSCaFrame(
			UnitManager um, CalcManager cm, 
			MaSCaCfg cfg, MaSCaActions actions, IAppContext appContext) 
	throws HeadlessException {
		super();
		this.um = um;
		this.cm = cm;
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
		mf = MeasureFormatter.createFormatter(msrFmt, Locale.getDefault());
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
		String rname = "de/admadic/calculator/modules/masca/res/" + name;
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

		this.setTitle("MaSCa Module");
		
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
		
		FormLayout fl = new FormLayout(
				"12px, 150dlu, 5px, d:grow, 12px",
				"12px, d:grow, 12px");
//		"12px, 150dlu, 5px, max(p;200dlu):grow, 12px",
//		"12px, max(200dlu;p):grow, 12px");
		CellConstraints cc = new CellConstraints();
		
		panelMain.setLayout(fl);

		treeSelector = new CalculationTree();
		treeSelector.setVisibleRowCount(10);
		scrollSelector = new JScrollPane(
				treeSelector,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panelMain.add(
				scrollSelector, 
				cc.xy(2, 2, CellConstraints.FILL, CellConstraints.FILL));
		panelContent = new JPanel();
		panelContent.setLayout(new GridLayout(1, 1));
		scrollContent = new JScrollPane();
		scrollContent.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollContent.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panelContent.add(scrollContent);
		panelCalc = new CalcPanel(um, mf);
		panelCalc.initPlain(
				"Calculations", 
				"Please select the desired calculation from the list on the "+
				"left hand side of this window.", 
				1, 1);
//		activeCalcEngine = new CalcEngineYoungsModulusFromPoisShrMod(um);
		panelMain.add(
				panelContent, 
				cc.xy(4, 2, CellConstraints.FILL, CellConstraints.FILL));
		// panelContent.setBorder(BorderFactory.createLineBorder(Color.RED));

		scrollContent.setViewportView(panelCalc);
		treeSelector.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		treeSelector.addTreeSelectionListener(this);
		treeSelector.setModel(new DefaultTreeModel(null));

		this.pack();
		setLocationRelativeTo(null);

//		setupComplete = true;
//		initTreeData();
	}

	/**
	 * 
	 */
	public void initTreeData() {
		DefaultTreeModel tm = new DefaultTreeModel(
				cm.getCategoryTree().getRoot());
		treeSelector.setModel(tm);
		treeSelector.setRootVisible(false);
	}

	/**
	 * @param e
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeSelector
			.getLastSelectedPathComponent();
		if (node==null) return;

		Object nodeInfo = node.getUserObject();
		if (node.isLeaf() && (nodeInfo instanceof AbstractCalculation)) {
			AbstractCalculation ce = (AbstractCalculation)nodeInfo;
			displayCalcEngine(ce);
		} else {
			// nope
		}
	}

	private void displayCalcEngine(AbstractCalculation ce) {
		final AbstractCalculation ac = ce;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (activeCalcEngine!=null) activeCalcEngine.unlinkFromCalcPanel();
				activeCalcEngine = ac;
				ac.linkToCalcPanel(panelCalc);
			}
		});
	}

	/**
	 * 
	 */
	public void doHelpAbout() {
		AboutDialog dialog = new AboutDialog(this);
		dialog.setAboutInfo(
				"admaDIC Calculator Module\n"+
				"MaSCa - Materials Science Calculations\n"+
				"Version " + 
				de.admadic.calculator.modules.masca.Version.version,
				"Copyright (c) 2005-2022 - admaDIC\n"+
				"Germany",
				"For updates or more information \n"+
				"please visit http://www.admadic.de/",
				"This product includes the software JGoodies Forms:\n"+
				"Copyright (c) 2002-2004 JGoodies Karsten Lentzsch.\n"+
				"All rights reserved.");	// no soft libs
		dialog.setTitle("About MaSCa Module");
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
	}

	private void doEditPaste() {
		Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager()
						.getPermanentFocusOwner();
		if (copyPasteCore==null) copyPasteCore = new CopyPasteCore();
		if (c instanceof JTextField) {
			JTextField tf = (JTextField)c;
			copyPasteCore.doPasteInto(tf);
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
			mf.setFormatString(msrFmt);
			panelCalc.refreshDisplay();
		}
	}
}
