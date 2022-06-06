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
package de.admadic.calculator.ui.settings;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import de.admadic.calculator.appmod.ModuleZipReader;
import de.admadic.calculator.appmod.ModuleZipReaderException;
import de.admadic.util.FileUtil;
import de.admadic.util.PathManager;
import de.admadic.util.VersionName;
import de.admadic.util.VersionUtil;

/**
 * @author Rainer Schwarze
 *
 */
public class SettingsUtils {
	final static boolean LOG = true;
	final static Logger logger = (LOG) ? Logger.getLogger("de.admadic") : null;

	/** item has not been installed, maybe license failure */
	public final static int RCINS_NOINSTALL = 0;
	/** item has been installed, but no restart necessary */
	public final static int RCINS_NORESTART = 1;
	/** item has been installed, restart necessary */
	public final static int RCINS_RESTART = 2;

	/**
	 * Protected ctor, use only static methods.
	 */
	protected SettingsUtils() {
		super();
	}

	/**
	 * @param parent 
	 * @return	Returns a Jar file selected by the user.
	 */
	public static File getJarFile(Component parent) {
		class JarFilter extends FileFilter {
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
		        	retcode = ext.equals("jar");
		            return retcode;
		        }
		        return false;
		    }

		    //The description of this filter
		    @Override
			public String getDescription() {
		        return "Jar Files (*.jar)";
		    }
		}

		return getFile(parent, new JarFilter());
	}
	
	/**
	 * @param parent 
	 * @return	Returns a Zip-File selected by the user.
	 */
	public static File getZipFile(Component parent) {
		class ZipFilter extends FileFilter {
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
		        	retcode = ext.equals("zip");
		            return retcode;
		        }
		        return false;
		    }

		    //The description of this filter
		    @Override
			public String getDescription() {
		        return "Zip Files (*.zip)";
		    }
		}

		return getFile(parent, new ZipFilter());
	}

	/**
	 * Selects a module file which can be a JAR or a ZIP.
	 * @param parent
	 * @return	Returns the selected file, or null, if none is found.
	 */
	public static File getModFile(Component parent) {
		class ModFilter extends FileFilter {
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
		        	boolean retcode = false;
		        	if (ext.equals("zip")) return true;
		        	if (ext.equals("jar")) return true;
		            return retcode;
		        }
		        return false;
		    }

		    //The description of this filter
		    @Override
			public String getDescription() {
		        return "Module Files (*.zip;*.jar)";
		    }
		}

		return getFile(parent, new ModFilter());
	}
	
	/**
	 * @param parent 
	 * @param ff
	 * @return	Returns a file selected by the user.
	 */
	public static File getFile(Component parent, FileFilter ff) {
		File file = null; 
		JFileChooser fc;
		fc = new JFileChooser();
		fc.addChoosableFileFilter(ff);
		fc.setFileHidingEnabled(false);
		{
			// wd
		}
		int result = fc.showOpenDialog(parent);
//		lastCurDir = fc.getCurrentDirectory();
		{
			// wd
		}
		switch (result) {
		case JFileChooser.APPROVE_OPTION: // open or save
			file = fc.getSelectedFile();
			return file;
			// unreachable
			// break;
		case JFileChooser.CANCEL_OPTION:
			break;
		case JFileChooser.ERROR_OPTION:
			break;
		}

		return null;
	}

	/**
	 * @param parent
	 * @param pathMan
	 * @param file
	 * @param pathType
	 * @return Returns ins code
	 */
	public static int installFile(
			Component parent, PathManager pathMan, File file, int pathType) {
		if (pathMan==null) {
			if (logger!=null) logger.severe("pathManager is not available.");
			if (logger!=null) logger.warning(
					"could not install file " + file.getPath());
			JOptionPane.showMessageDialog(
					parent,
					"Could not detect directory structure of the calculator.\n"+
					"Please install the file by yourself:\n"+
					"Filename = " + file.getPath());
			return RCINS_NOINSTALL;
		}
		String dstname = pathMan.getPathString(pathType);
		if (dstname==null) {
			if (logger!=null) logger.severe(
					"could not retrieve path for id " + pathType);
			if (logger!=null) logger.warning(
					"could not install file " + file.getPath());
			JOptionPane.showMessageDialog(
					parent,
					"Could not detect the destination directory for the file.\n"+
					"Please install the file by yourself:\n"+
					"Filename = " + file.getPath());
			return RCINS_NOINSTALL;
		}
		File dstfile = new File(dstname);
		try {
			if (file.getCanonicalPath().startsWith(dstfile.getCanonicalPath())) {
				if (logger!=null) logger.config(
						"install: src and dst are the same, no need to install " + 
						file.getPath());
				// its already there.
				return RCINS_NORESTART;
			}
		} catch (IOException e) {
			// e.printStackTrace();
			if (logger!=null) logger.warning(
					"access error:"+
					" file=" + file.getPath() +
					" destdir=" + dstname);
			JOptionPane.showMessageDialog(
					parent,
					"Could not access the file or the destination directory.\n"+
					"Please install the file by yourself:\n"+
					"Filename = " + file.getPath() + "\n" +
					"destination-dir = " + dstname);
			return RCINS_RESTART;
		}

		// first check for versioned filename, ask, if newer version is 
		// already there
		if (!checkNewerVersion(parent, file.getName(), dstname)) {
			return RCINS_NORESTART;
		}

		dstfile = new File(dstfile, file.getName());
		if (dstfile.exists()) {
			long dstmod = dstfile.lastModified();
			long srcmod = file.lastModified();
			if (logger!=null) logger.config(
					"install: file exists already: "+
					" file=" + file.getPath() +
					" destfile=" + dstfile.getPath());
			int result = JOptionPane.showConfirmDialog(
				parent,
				"The file already exists in the destination directory."+
				"Do you want to copy this file\n"+
				"File = " + file.getPath() + "\n"+
				"Time = " + new Date(srcmod).toString() + "\n"+
				"over this file\n"+
				"File = " + dstfile.getPath() + "\n"+
				"Time = " + new Date(dstmod).toString() + "\n"+
				"?",
				"Confirm overwrite",
				JOptionPane.OK_CANCEL_OPTION);
			if (result!=JOptionPane.OK_OPTION) {
				if (logger!=null) logger.config(
						"install: file exists already: user cancelled overwrite.");
				// cancel
				return RCINS_NORESTART;
			}
			if (logger!=null) logger.config(
					"install: file exists already: user confirmed overwrite.");
		}

		if (!FileUtil.copyFile(file.getPath(), dstfile.getPath())) {
			JOptionPane.showMessageDialog(
					parent,
					"Could not copy the file to the destination directory.\n"+
					"Please install the file by yourself:\n"+
					"Filename = " + file.getPath() + "\n" +
					"Destination Directory = " + dstname);
			return RCINS_NORESTART;
		}
		// ok
		return RCINS_RESTART;
		// FIXME: move that message into the outer calling location:
//		JOptionPane.showMessageDialog(
//				parent,
//				"Copied the jar file into the system LaF-directory of the \n"+
//				"Calculator. Please restart the calculator.");
	}

	/**
	 * @param parent 
	 * @param file
	 * @param dstname
	 * @return	Returns false, if a newer version exists and the user 
	 * cancelled install, otherwise true.
	 */
	private static boolean checkNewerVersion(
			Component parent, String file, String dstname) {
		VersionName vn = VersionUtil.getVersionNameFromFileName(file);
		if (vn==null) return true;	// no version, no check
		// we have a version. check the directory for all files:
		Vector<VersionName> vflist = new Vector<VersionName>();
		File dir = new File(dstname);
		File [] flist = dir.listFiles();
		if (flist==null) return true;
		for (File f : flist) {
			VersionName vnf = VersionUtil.getVersionNameFromFileName(f.getName());
			if (vnf==null) continue;
			if (!vnf.getPrefix().equals(vn.getPrefix())) continue;
			if (!vnf.getSuffix().equals(vn.getSuffix())) continue;
			vflist.add(vnf);
		}
		if (vflist.size()<1) return true;
		boolean hasNewer = false;
		String listFiles = null;
		for (VersionName vnf : vflist) {
			if (vnf.getVersionRecord().compareTo(vn.getVersionRecord())>0) {
				hasNewer = true;
				String t = vnf.getCombinedName();
				if (listFiles==null) {
					listFiles = t;
				} else {
					listFiles += "\n" + t;
				}
			}
		}
		if (hasNewer) {
			int result = JOptionPane.showConfirmDialog(
					parent,
					"Newer versions of the file already exist in"+
					" the destination directory.\n"+
					"Do wish to install the file anyway?\n"+
					"File = " + file + "\n"+
					"Newer Versions:\n" + 
					listFiles,
					"Confirm installation",
					JOptionPane.OK_CANCEL_OPTION);
			if (result==JOptionPane.OK_OPTION) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param parent
	 * @param pathMan
	 * @param filename 
	 * @param istream
	 * @param pathType
	 * @return Returns true, if the file was actually installed.
	 */
	public static boolean installFile(
			Component parent, PathManager pathMan, 
			String filename, InputStream istream, int pathType) {
		if (pathMan==null) {
			if (logger!=null) logger.severe("pathManager is not available.");
			if (logger!=null) logger.warning(
					"could not install file (from stream) " + filename);
			JOptionPane.showMessageDialog(
					parent,
					"Could not detect directory structure of the calculator.\n"+
					"Please install the file by yourself:\n"+
					"Filename = " + filename);
			return false;
		}
		String dstname = pathMan.getPathString(pathType);
		if (dstname==null) {
			if (logger!=null) logger.severe(
					"could not retrieve path for id " + pathType);
			if (logger!=null) logger.warning(
					"could not install file (from stream) " + filename);
			JOptionPane.showMessageDialog(
					parent,
					"Could not detect the destination directory for the file.\n"+
					"Please install the file by yourself:\n"+
					"Filename = " + filename);
			return false;
		}
		File dstfile = new File(dstname);

		// first check for versioned filename, ask, if newer version is 
		// already there
		if (!checkNewerVersion(parent, filename, dstname)) {
			return false;
		}

		dstfile = new File(dstfile, filename);
		// FIXME: maybe implement check for existing file later on
		// that would need the timestamp from the ZIP archive from which
		// InputStream is taken from.

		if (!FileUtil.copyFile(istream, dstfile.getPath())) {
			JOptionPane.showMessageDialog(
					parent,
					"Could not copy the file to the destination directory.\n"+
					"Please install the file by yourself:\n"+
					"Filename = " + filename + "\n" +
					"Destination Directory = " + dstname);
			return false;
		}
		// ok
		return true;
		// FIXME: move that message into the outer calling location:
//		JOptionPane.showMessageDialog(
//				parent,
//				"Copied the jar file into the system LaF-directory of the \n"+
//				"Calculator. Please restart the calculator.");
	}

	/**
	 * @param parent
	 * @param pathMan
	 * @param file
	 * @return Returns one of the return codes
	 */
	public static int installModFile(
			Component parent, PathManager pathMan, File file) {
		if (pathMan==null) {
			JOptionPane.showMessageDialog(
					parent,
					"Could not detect the system module-directory of the \n"+
					"calculator. Please contact customer support.\n"+
					"Filename = " + file.getPath());
			return RCINS_NOINSTALL;
		}
		String ext = file.getName();
		ext.toLowerCase();
		if (ext.endsWith(".jar")) {
			return installModFileImplJar(parent, pathMan, file);
		} else if (ext.endsWith(".zip")) {
			return installModFileImplZip(parent, pathMan, file);
		} else {
			JOptionPane.showMessageDialog(
					parent,
					"This file type is not supported. Only zip and jar are allowed.\n"+
					"Filename = " + file.getPath());
			return RCINS_NOINSTALL;
		}
	}

	/**
	 * @param parent
	 * @param pathMan
	 * @param file
	 * @return Returns install code
	 */
	public static int installModFileImplZip(
			Component parent, PathManager pathMan, File file) {
		/*
		 * Algorithm:
		 * - open ZIP file
		 * - read install.cfg
		 * - check version
		 * - install libs
		 * - install jars
		 */
		ModuleZipReader mzr = new ModuleZipReader(file.toString());
		try {
			mzr.open();
			mzr.buildEntryList();
		} catch (ModuleZipReaderException e) {
			// e.printStackTrace();
			if (logger!=null) logger.severe(
					"error accessing zip file " + file.getPath() +
					" error=" + e.getMessage());
			JOptionPane.showMessageDialog(
					parent,
					"Could not access the ZIP file.\n"+
					"Filename = " + file.getPath());
			return RCINS_NOINSTALL;
		}
		try {
			if (!mzr.hasInstallCfg()) {
				if (logger!=null) logger.severe(
						"no install.cfg found in file " + file.getPath());
				JOptionPane.showMessageDialog(
						parent,
						"The ZIP file is not a valid module archive.\n"+
						"Filename = " + file.getPath());
				return RCINS_NOINSTALL;
			}
			mzr.readInstallCfg();
			mzr.parseInstallCfg();
		} catch (ModuleZipReaderException e) {
			// e.printStackTrace();
			if (logger!=null) logger.severe(
					"error reading zip file " + file.getPath() +
					" error=" + e.getMessage());
			JOptionPane.showMessageDialog(
					parent,
					"Error reading data from the ZIP file.\n"+
					"It is not a valid or supported module archive.\n"+
					"Filename = " + file.getPath() + "\n"+
					"Error = " + e.getMessage());
			return RCINS_NOINSTALL;
		}
		// here we should have a list of mod and lib entries
		Vector<String> entries;

		entries = mzr.getEntries("lic");
		if (entries!=null) {
			for (String e : entries) {
				InputStream is = mzr.getStream(e);
				if (is==null) {
					if (logger!=null) logger.severe(
							"error getting stream from zip file " + 
							file.getPath() + " for entry " + e);
					JOptionPane.showMessageDialog(
							parent,
							"Error reading data from the ZIP file.\n"+
							"Filename = " + file.getPath());
					return RCINS_NOINSTALL;
				}
				installFile(parent, pathMan, e, is, PathManager.SYS_DOC_DIR);
			}
		}

		entries = mzr.getEntries("lib");
		if (entries!=null) {
			for (String e : entries) {
				InputStream is = mzr.getStream(e);
				if (is==null) {
					if (logger!=null) logger.severe(
							"error getting stream from zip file " + 
							file.getPath() + " for entry " + e);
					JOptionPane.showMessageDialog(
							parent,
							"Error reading data from the ZIP file.\n"+
							"Filename = " + file.getPath());
					return RCINS_NOINSTALL;
				}
				installFile(parent, pathMan, e, is, PathManager.SYS_LIB_DIR);
			}
		}

		entries = mzr.getEntries("mod");
		if (entries!=null) {
			for (String e : entries) {
				InputStream is = mzr.getStream(e);
				if (is==null) {
					if (logger!=null) logger.severe(
							"error getting stream from zip file " + 
							file.getPath() + " for entry " + e);
					JOptionPane.showMessageDialog(
							parent,
							"Error reading data from the ZIP file.\n"+
							"Filename = " + file.getPath());
					return RCINS_NOINSTALL;
				}
				installFile(parent, pathMan, e, is, PathManager.SYS_MOD_DIR);
			}
		}
		return RCINS_RESTART;
	}
	
	
	/**
	 * @param parent
	 * @param pathMan
	 * @param file
	 * @return Returns install code
	 */
	public static int installModFileImplJar(
			Component parent, PathManager pathMan, File file) {
		return installFile(parent, pathMan, file, PathManager.SYS_MOD_DIR);
	}
}
