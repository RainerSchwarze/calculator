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
import java.util.Date;

import javax.swing.JOptionPane;

import de.admadic.util.FileUtil;
import de.admadic.util.PathManager;

/**
 * Storage for methods which are to be removed from SettingsUtils.
 * @author Rainer Schwarze
 */
public class SettingsUtilsRemove {

	/**
	 * 
	 */
	public SettingsUtilsRemove() {
		super();
	}

	/**
	 * @param parent
	 * @param pathMan
	 * @param file
	 */
	public static void installLaFFile(
			Component parent, PathManager pathMan, File file) {
		if (pathMan==null) {
			JOptionPane.showMessageDialog(
					parent,
					"Could not detect the system LaF-directory of the \n"+
					"calculator. Please install the jar file by yourself:\n"+
					"Filename = " + file.getPath());
			return;
		}

		String dstname = pathMan.getPathString(PathManager.SYS_LAF_DIR);
		if (dstname==null) {
			JOptionPane.showMessageDialog(
					parent,
					"Could not detect the system LaF-directory of the \n"+
					"calculator. Please install the jar file by yourself:\n"+
					"Filename = " + file.getPath());
			return;
		}
		File dstfile = new File(dstname);
		try {
			if (file.getCanonicalPath().startsWith(dstfile.getCanonicalPath())) {
				// its already there.
				return;
			}
		} catch (IOException e) {
			// e.printStackTrace();
			JOptionPane.showMessageDialog(
					parent,
					"Could not access the Jar file or the system LaF-directory of the \n"+
					"calculator. Please install the jar file by yourself:\n"+
					"Filename = " + file.getPath() + "\n" +
					"System-LaF-dir = " + dstname);
			return;
		}


		
		dstfile = new File(dstfile, file.getName());
		if (dstfile.exists()) {
			long dstmod = dstfile.lastModified();
			long srcmod = file.lastModified();
			int result = JOptionPane.showConfirmDialog(
				parent,
				"The jar file already exists in the system LaF-directory of"+
				" the calculator.\n"+
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
				// cancel
				return;
			}
		}

		if (!FileUtil.copyFile(file.getPath(), dstfile.getPath())) {
			JOptionPane.showMessageDialog(
					parent,
					"Could not copy the Jar file to the system LaF-directory of the \n"+
					"calculator. Please install the jar file by yourself:\n"+
					"Filename = " + file.getPath() + "\n" +
					"System-LaF-dir = " + dstname);
			return;
		}
		// ok
		JOptionPane.showMessageDialog(
				parent,
				"Copied the jar file into the system LaF-directory of the \n"+
				"Calculator. Please restart the calculator.");
	}

	/**
	 * @param parent
	 * @param pathMan
	 * @param file
	 */
	public static void installLibFile(
			Component parent, PathManager pathMan, File file) {
		if (pathMan==null) {
			JOptionPane.showMessageDialog(
					parent,
					"Could not detect the system lib-directory of the \n"+
					"calculator. Please install the jar file by yourself:\n"+
					"Filename = " + file.getPath());
			return;
		}

		String dstname = pathMan.getPathString(PathManager.SYS_LIB_DIR);
		if (dstname==null) {
			JOptionPane.showMessageDialog(
					parent,
					"Could not detect the system lib-directory of the \n"+
					"calculator. Please install the jar file by yourself:\n"+
					"Filename = " + file.getPath());
			return;
		}
		File dstfile = new File(dstname);
		try {
			if (file.getCanonicalPath().startsWith(dstfile.getCanonicalPath())) {
				// its already there.
				return;
			}
		} catch (IOException e) {
			// e.printStackTrace();
			JOptionPane.showMessageDialog(
					parent,
					"Could not access the Jar file or the system lib-directory of the \n"+
					"calculator. Please install the jar file by yourself:\n"+
					"Filename = " + file.getPath() + "\n" +
					"System-lib-dir = " + dstname);
			return;
		}


		
		dstfile = new File(dstfile, file.getName());
		if (dstfile.exists()) {
			long dstmod = dstfile.lastModified();
			long srcmod = file.lastModified();
			int result = JOptionPane.showConfirmDialog(
				parent,
				"The jar file already exists in the system lib-directory of"+
				" the calculator.\n"+
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
				// cancel
				return;
			}
		}

		if (!FileUtil.copyFile(file.getPath(), dstfile.getPath())) {
			JOptionPane.showMessageDialog(
					parent,
					"Could not copy the Jar file to the system lib-directory of the \n"+
					"calculator. Please install the jar file by yourself:\n"+
					"Filename = " + file.getPath() + "\n" +
					"System-lib-dir = " + dstname);
			return;
		}
		// ok
		JOptionPane.showMessageDialog(
				parent,
				"Copied the jar file into the system lib-directory of the \n"+
				"Calculator. Please restart the calculator.");
	}

}
