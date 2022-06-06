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
package de.admadic.calculator.modules.indxp.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.appctx.IAppContext;
import de.admadic.calculator.core.LocaleConstants;
import de.admadic.calculator.modules.indxp.InDXpActions;
import de.admadic.calculator.modules.indxp.InDXpCfg;
import de.admadic.calculator.modules.indxp.core.DataEvent;
import de.admadic.calculator.modules.indxp.core.Factor;
import de.admadic.calculator.modules.indxp.core.InDXpData;
import de.admadic.calculator.modules.indxp.core.InDXpDataIO;
import de.admadic.calculator.modules.indxp.core.TabularCopyPasteCore;
import de.admadic.calculator.modules.indxp.core.XmlIoError;
import de.admadic.ui.util.AboutDialog;
import de.admadic.ui.util.Dialog;

/**
 * 
 */
public class InDXpFrame extends JFrame implements 
ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static boolean LOG = true;
	Logger logger = (LOG) ? Logger.getLogger("de.admadic") : null;

	boolean DBGtesting = false;

	InDXpCfg cfg;
	IAppContext appContext;

	String title;
	JPanel panelMain;
	WizardPanel wizardPanel;
	
	FactorsPanel factorsPanel;
	FactorInteractionsPanel factorInteractionsPanel;
	RunsPanel runsPanel;
	ExpResultsPanel expResultsPanel;
	JPanel panelLevelAnalysis;
	LevelAnalysisPanel levelAnalysisYAvgPanel;
	LevelAnalysisPanel levelAnalysisSigmaPanel;

	InDXpData ideData;
	InDXpActions actions;

	ImageIcon iconSmall;
	ImageIcon iconLarge;
	
	FloatingPointFormatter fpf;
	
	JMenuBar menuBar;

	final static String CMD_FILE_NEW = "mnu.file.new";
	final static String CMD_FILE_LOAD = "mnu.file.load";
	final static String CMD_FILE_SAVE = "mnu.file.save";
	final static String CMD_FILE_SAVEAS = "mnu.file.saveas";
	final static String CMD_FILE_EXPORT_TXT = "mnu.file.export.txt";
	final static String CMD_FILE_EXIT = "mnu.file.exit";

	final static String CMD_EDIT_COPY = "mnu.edit.copy";
	final static String CMD_EDIT_PASTE = "mnu.edit.paste";
	final static String CMD_EDIT_RNDDATA = "mnu.edit.rnddata";

	final static String CMD_TOOLS_OPTIONS = "mnu.tools.options";

	final static String CMD_HELP_ABOUT = "mnu.help.about";

	/**
	 * @param cfg 
	 * @param actions 
	 * @param appContext 
	 * 
	 */
	public InDXpFrame(InDXpCfg cfg, InDXpActions actions, IAppContext appContext) {
		super();
		this.appContext = appContext;
		this.cfg = cfg;
		this.actions = actions;
		this.fpf = FloatingPointFormatter.createSimpleFormatter();
		{
			String tmp = cfg.getDisplayFormat();
			if (tmp==null) {
				tmp = "%g";
			}
			fpf.setFormatString(tmp);
		}
		{
			String tmp;
			tmp = System.getProperty("admadic.testing");
			if (tmp!=null && tmp.toLowerCase().equals("yes")) {
				DBGtesting = true;
			}
		}
		initComponents();
	}
	
	private void addMenuItem(JMenu menu, String name, String aCmd) {
		JMenuItem mi = new JMenuItem(name);
		menu.add(mi);
		mi.setActionCommand(aCmd);
		mi.addActionListener(this);
	}

	/**
	 * @param name
	 * @return	Returns a newly created instance of ImageIcon for the given 
	 * 			icon resource. 
	 */
	public ImageIcon loadIcon(String name) {
		String rname = "de/admadic/calculator/modules/indxp/res/" + name;
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

	private void initComponents() {
		panelMain = (JPanel)this.getContentPane();

		iconSmall = loadIcon("icon-16.png");
		iconLarge = loadIcon("icon-48.png");
		
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
				// FIXME: hide is possible, exit is done another way
				// doExit();
				setVisible(false);
			}
		});
		title = "InDXp Module";
		setTitleImpl();

		BorderLayout l = new BorderLayout();
		panelMain.setLayout(l);
		wizardPanel = new WizardPanel();
		panelMain.add(wizardPanel, BorderLayout.CENTER);
		
		ideData = new InDXpData();
		if (DBGtesting) {
			ideData.getFactorList().add(new Factor("A", "Temperature", "\u00B0C", "10", "20")); // °C
			ideData.getFactorList().add(new Factor("B", "Pressure", "psi", "5", "10"));
			ideData.getFactorList().add(new Factor("C", "Diameter", "mm", "1.5", "1.7"));
		}
		
		factorsPanel = new FactorsPanel();
		wizardPanel.addPanel(factorsPanel, "Factors");
		factorsPanel.linkData(ideData.getFactorList());
		factorsPanel.setDataEventDispatcher(ideData);
		factorsPanel.setDataEventServer(ideData);
		factorsPanel.setDataItemStatusServer(ideData);
		
		factorInteractionsPanel = new FactorInteractionsPanel();
		wizardPanel.addPanel(factorInteractionsPanel, "Factor Interactions");
		factorInteractionsPanel.linkData(
				ideData.getFactorInteractionList(),
				ideData.getFactorList());
		factorInteractionsPanel.setDataEventDispatcher(ideData);
		factorInteractionsPanel.setDataEventServer(ideData);
		factorInteractionsPanel.setFactorInteractionBuilder(
				ideData.getFactorInteractionBuilder());
		factorInteractionsPanel.setDataItemStatusServer(ideData);
		
		runsPanel = new RunsPanel();
		wizardPanel.addPanel(runsPanel, "Runs");
		runsPanel.linkData(
				ideData.getRunList(), 
				ideData.getFactorInteractionList());
		runsPanel.setDataEventDispatcher(ideData);
		runsPanel.setDataEventServer(ideData);
		runsPanel.setRunsBuilder(ideData.getRunsBuilder());
		runsPanel.setDataItemStatusServer(ideData);
		
		expResultsPanel = new ExpResultsPanel(fpf);
		wizardPanel.addPanel(expResultsPanel, "Exp. Results");
		expResultsPanel.linkData(
				ideData.getExpResultsList(),
				ideData.getRunList());
		expResultsPanel.setDataEventDispatcher(ideData);
		expResultsPanel.setDataEventServer(ideData);
		expResultsPanel.setDataItemStatusServer(ideData);
		
		panelLevelAnalysis = new JPanel();
		panelLevelAnalysis.setLayout(
				new FormLayout("p:grow", "p:grow, 5px, p:grow"));
		wizardPanel.addPanel(panelLevelAnalysis, "Level Analysis");
		
		levelAnalysisYAvgPanel = new LevelAnalysisPanel(fpf);
		panelLevelAnalysis.add(
				levelAnalysisYAvgPanel, 
				new CellConstraints(1, 1));
		levelAnalysisYAvgPanel.linkData(
				ideData.getLevelAnalysisYAvg(),
				ideData.getExpResultsList(),
				ideData.getFactorInteractionList());
		levelAnalysisYAvgPanel.setDataEventDispatcher(ideData);
		levelAnalysisYAvgPanel.setDataEventServer(ideData);
		
		levelAnalysisSigmaPanel = new LevelAnalysisPanel(fpf);
		panelLevelAnalysis.add(
				levelAnalysisSigmaPanel, 
				new CellConstraints(1, 3));
		levelAnalysisSigmaPanel.linkData(
				ideData.getLevelAnalysisSigma(),
				ideData.getExpResultsList(),
				ideData.getFactorInteractionList());
		levelAnalysisSigmaPanel.setDataEventDispatcher(ideData);
		levelAnalysisSigmaPanel.setDataEventServer(ideData);
		
		JMenu menu;
		menuBar = new JMenuBar();
		menuBar.add(menu = new JMenu("File"));
		addMenuItem(menu, "New", CMD_FILE_NEW);
		menu.add(new JSeparator());
		addMenuItem(menu, "Load...", CMD_FILE_LOAD);
		addMenuItem(menu, "Save", CMD_FILE_SAVE);
		addMenuItem(menu, "Save As...", CMD_FILE_SAVEAS);
		addMenuItem(menu, "Export as Text...", CMD_FILE_EXPORT_TXT);
		menu.add(new JSeparator());
		// we call it close, since we do not exit the application:
		addMenuItem(menu, "Close", CMD_FILE_EXIT);
		menuBar.add(menu = new JMenu("Edit"));
		addMenuItem(menu, "Copy", CMD_EDIT_COPY);
		addMenuItem(menu, "Paste", CMD_EDIT_PASTE);
		if (DBGtesting) {
			menu.add(new JSeparator());
			addMenuItem(menu, "Create Random Results", CMD_EDIT_RNDDATA);
		}
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
		
		this.pack();
		setLocationRelativeTo(null);
	}

	/**
	 * @param arg0
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		String cmd = arg0.getActionCommand();
		if (cmd.equals(CMD_EDIT_RNDDATA)) {
			ideData.createRandomResults();
		} else if (cmd.equals(CMD_FILE_NEW)) {
			// new nothing?
			if (ideData.isDirty()) {
				if (!checkSave()) {
					return;
				}
			}
			ideData.resetData();
		} else if (cmd.equals(CMD_FILE_LOAD)) {
			if (ideData.isDirty()) {
				if (!checkSave()) {
					return;
				}
			}
			doLoad();
		} else if (cmd.equals(CMD_FILE_SAVE)) {
			doSave();
		} else if (cmd.equals(CMD_FILE_EXIT)) {
			doExit();
		} else if (cmd.equals(CMD_FILE_SAVEAS)) {
			doSaveAs();
		} else if (cmd.equals(CMD_FILE_EXPORT_TXT)) {
			doExportTxt();
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
			expResultsPanel.repaint();
			levelAnalysisYAvgPanel.repaint();
			levelAnalysisSigmaPanel.repaint();
		}
	}

	/**
	 * 
	 */
	public void doHelpAbout() {
		AboutDialog dialog = new AboutDialog(this);
		dialog.setAboutInfo(
				"admaDIC Calculator Module\n"+
				"InDXp - Industrial Designed Experiments\n"+
				"Version " + 
				de.admadic.calculator.modules.indxp.Version.version,
				"Copyright (c) 2005-2022 - admaDIC\n"+
				"Germany",
				"For updates or more information \n"+
				"please visit http://www.admadic.de/",
				"This product includes the software JGoodies Forms:\n"+
				"Copyright (c) 2002-2004 JGoodies Karsten Lentzsch.\n"+
				"All rights reserved.");	// no soft libs
		dialog.setTitle("About InDXp Module");
		if (iconLarge!=null) {
			dialog.setLogo(iconLarge.getImage());
		}
		dialog.setVisible(true);
	}

	private void doEditCopy() {
		Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager()
						.getPermanentFocusOwner();
		// FIXME: improve that test:
		if (!(c instanceof JTable)) return;

		JTable table = (JTable)c;

		TabularCopyPasteCore tcpc = new TabularCopyPasteCore();
		tcpc.doCopyFrom(table);
	}

	private void doEditPaste() {
		Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager()
						.getPermanentFocusOwner();
		// FIXME: improve that test:
		if (!(c instanceof JTable)) return;

		JTable table = (JTable)c;

		TabularCopyPasteCore tcpc = new TabularCopyPasteCore();
		tcpc.doPasteInto(table, false);
	}

	// protected because of anonymous class access
	protected void doExit() {
		if (ideData.isDirty()) {
			if (!checkSave()) {
				return;
			}
		}
		this.dispose();
	}
	
	private void doLoad() {
		File file = ideData.getFileName();
		file = getXmlFileNameJFC(false, false, file);
		if (file==null) return;

		InDXpDataIO dataIO = new InDXpDataIO(ideData, null);
		try {
			dataIO.load(file);
			// success:
			ideData.setFileName(file);
			setTitleImpl();
		} catch (XmlIoError e) {
			// error!
			// e.printStackTrace();
			JOptionPane.showMessageDialog(
					this,
					"Error loading the file:\n"+
					file.toString() + "\n"+
					"Maybe it is not a valid data file.\n"+
					"The internal error message was:\n"+
					"> " + e.getMessage() + " <",
					"Error loading data file",
					JOptionPane.ERROR_MESSAGE);
			ideData.resetData();
		}
		// ideData.notifyEvent(this, DataEvent.ALL_CHANGED);
		ideData.notifyEvent(this, DataEvent.FACTORS_STRUCT_CHANGED);
		ideData.notifyEvent(this, DataEvent.FACTORINTERACTIONS_STRUCT_CHANGED);
		ideData.notifyEvent(this, DataEvent.RUNS_STRUCT_CHANGED);
		ideData.notifyEvent(this, DataEvent.EXPRESULTS_STRUCT_CHANGED);
		ideData.notifyEvent(this, DataEvent.LEVELANALYSIS_STRUCT_CHANGED);

		// dirty will be set, because of all updates.
		// clear it, because it is still a plain load op.
		ideData.setDirty(false);
	}

	/**
	 * @return	Returns false, if the user pressed cancel -> then cancel 
	 * other ops following.
	 */
	public boolean checkSave() {
		boolean retcode = true;
		boolean oldV = isVisible();
		if (!oldV) setVisible(true);
		int result = JOptionPane.showConfirmDialog(
				this,
				"The data has been modified and not yet saved.\n"+
				"If you continue without saving, the changes will be lost.\n"+
				"Do you wish to save the data?\n"+
				"(Yes - save, No - don't save and continue, Cancel - abort",
				"Save data?",
				JOptionPane.YES_NO_CANCEL_OPTION);
		if (result==JOptionPane.YES_OPTION) {
			if (!doSave()) {	// cancelled
				retcode = false;
			} else {
				retcode = true;
			}
		} else if (result==JOptionPane.NO_OPTION) {
			// no save - continue
			retcode = true;
		} else if (result==JOptionPane.CANCEL_OPTION) {
			retcode = false;
		} else {
			retcode = false;
		}
		if (isVisible()!=oldV) {
			setVisible(oldV);
		}
		return retcode;
	}

	private boolean doSave() {
		File file = ideData.getFileName();
		if (file==null) {
			file = getXmlFileNameJFC(true, true, null);
		}
		if (file==null) return false;
		doSaveImpl(file);
		return true;
	}

	private boolean doSaveAs() {
		File file = ideData.getFileName();
		file = getXmlFileNameJFC(true, true, file);
		if (file==null) return false;
		doSaveImpl(file);
		return true;
	}

	private boolean doExportTxt() {
		File file = ideData.getFileName();
		file = new File(file.toString() + "-export.txt");
		file = getTxtFileNameJFC(true, true, file);
		if (file==null) return false;
		doExportImpl(file);
		return true;
	}

	private void doExportImpl(File file) {
		Locale locale = null;
		if (appContext!=null && appContext.getLocaleManager()!=null)
			locale = appContext.getLocaleManager().queryLocale(LocaleConstants.LOCALE_EXPORT);
		InDXpDataIO dataIO = new InDXpDataIO(ideData, locale);
		try {
			dataIO.exportTxt(file);
		} catch (XmlIoError e) {
			// error!
			// e.printStackTrace();
			JOptionPane.showMessageDialog(
					this,
					"Error exporting the file:\n"+
					file.toString() + "\n"+
					"The internal error message was:\n"+
					"> " + e.getMessage() + " <",
					"Error exporting data file",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void doSaveImpl(File file) {
		InDXpDataIO dataIO = new InDXpDataIO(ideData, null);
		try {
			dataIO.save(file);
			// success! -> store the file name:
			ideData.setFileName(file);
			ideData.setDirty(false);
			setTitleImpl();
		} catch (XmlIoError e) {
			// error!
			// e.printStackTrace();
			JOptionPane.showMessageDialog(
					this,
					"Error saving the file:\n"+
					file.toString() + "\n"+
					"The internal error message was:\n"+
					"> " + e.getMessage() + " <",
					"Error saving data file",
					JOptionPane.ERROR_MESSAGE);
		}
	}


	protected File getTxtFileNameJFC(
			boolean bSave, 
			boolean bAskOverwrite,
			File stdFile) {
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
		return getFileNameJFC(bSave, bAskOverwrite, stdFile, new TextFilter());
	}

	protected File getXmlFileNameJFC(
			boolean bSave, 
			boolean bAskOverwrite,
			File stdFile) {
		class XMLFilter extends FileFilter {
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
		        	retcode = ext.equals("xml");
		            return retcode;
		        }
		        return false;
		    }

		    //The description of this filter
		    @Override
			public String getDescription() {
		        return "XML Files (*.xml)";
		    }
		}
		return getFileNameJFC(bSave, bAskOverwrite, stdFile, new XMLFilter());
	}

	
	/**
	 * 
	 * @param bSave
	 * @param bAskOverwrite
	 * @param stdFile 
	 * @param ff 
	 * @return	Returns the chosen file or null, if cancel has been pressed.
	 */
	protected File getFileNameJFC(
			boolean bSave, 
			boolean bAskOverwrite,
			File stdFile,
			FileFilter ff) {
		File file = null; 
		JFileChooser fc;
		fc = new JFileChooser();
		fc.addChoosableFileFilter(ff);
		fc.setFileHidingEnabled(false);
		if (stdFile!=null) {
			fc.setSelectedFile(stdFile);
		}
		int result;
		
		while (true) {
			if (bSave) {
				result = fc.showSaveDialog(this);
			} else {
				result = fc.showOpenDialog(this);
			}
			switch (result) {
			case JFileChooser.APPROVE_OPTION: // open or save
				file = fc.getSelectedFile();
				if (bAskOverwrite && file.exists()) {
					int returnValue = JOptionPane.showConfirmDialog(
							this, 
							"The file " + file.toString() + " already exist.\n"+
							"Overwrite?",
							"File exists",
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (returnValue==JOptionPane.YES_OPTION) {
						return file;
					} else if (returnValue==JOptionPane.NO_OPTION) {
						continue;	// once more
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
				return null;
				// break;
			case JFileChooser.ERROR_OPTION:
				return null;
				// break;
			}
		} // (while loop)
		// return null;
	}
	
	private void setTitleImpl() {
		String tmp = title;
		if (ideData!=null && ideData.getFileName()!=null) {
			tmp += " - " + ideData.getFileName().getPath();
		} else {
			tmp += " - untitled";
		}
		setTitle(tmp);
	}

	/**
	 * @return	Returns the data instance for this frame.
	 */
	public InDXpData getData() {
		return ideData;
	}
}
