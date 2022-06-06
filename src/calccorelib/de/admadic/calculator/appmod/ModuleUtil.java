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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import de.admadic.util.VersionRecord;

/**
 * @author Rainer Schwarze
 *
 */
public class ModuleUtil {

	/**
	 * Protected ctor. Use static functions only.
	 */
	protected ModuleUtil() {
		super();
	}

	/**
	 * Returns a ModuleSpec from the module archive.
	 * The archive may be a jar file or a zip file.
	 * 
	 * @param file
	 * @return	Returns the ModuleSpec from the module archive.
	 */
	public static ModuleSpec extractModuleSpec(File file) {
		String ext = file.getName();
		ext.toLowerCase();
		if (ext.endsWith(".jar")) {
			return extractModuleSpecImplJar(file);
		} else if (ext.endsWith(".zip")) {
			return extractModuleSpecImplZip(file);
		} else {
			return null;
		}
	}

	/**
	 * @param file
	 * @return	Returns a ModuleSpec if one could be found in the jar.
	 */
	public static ModuleSpec extractModuleSpecImplZip(File file) {
		ModuleSpec ms = null;
		ModuleZipReader mzr = new ModuleZipReader(file.toString());
		try {
			mzr.open();
			mzr.buildEntryList();
		} catch (ModuleZipReaderException e) {
			// e.printStackTrace();
//			JOptionPane.showMessageDialog(
//					null,
//					"Could not access the ZIP file.\n"+
//					"Filename = " + file.getPath());
			return null;
		}
		try {
			if (!mzr.hasInstallCfg()) {
//				JOptionPane.showMessageDialog(
//						null,
//						"The ZIP file is not a valid module archive.\n"+
//						"Filename = " + file.getPath());
				return null;
			}
			mzr.readInstallCfg();
			mzr.parseInstallCfg();
		} catch (ModuleZipReaderException e) {
			// e.printStackTrace();
//			JOptionPane.showMessageDialog(
//					null,
//					"Error reading data from the ZIP file.\n"+
//					"It is not a valid or supported module archive.\n"+
//					"Filename = " + file.getPath() + "\n"+
//					"Error = " + e.getMessage());
			return null;
		}
		Vector<String> entries;
		entries = mzr.getEntries("mod");
		if (entries==null || entries.size()<1) {
//			JOptionPane.showMessageDialog(
//					null,
//					"Could not find the module core file in the ZIP archive.\n"+
//					"It is not a valid or supported module archive.\n"+
//					"Filename = " + file.getPath());
			return null;
		}
		String modName = entries.firstElement();
		InputStream is = mzr.getStream(modName);
		ms = extractModuleSpec(is);

		return ms;
	}

	/**
	 * @param jarfile
	 * @return	Returns the ModuleSpec from the Jars Manifest, or null, if 
	 * there is none.
	 */
	protected static ModuleSpec extractModuleSpecImplJar(File jarfile) {
		ModuleSpec ms = null;
		JarFile jf = null;
		Manifest mf;
		try {
			jf = new JarFile(jarfile);
			if (jf==null) return null;
			mf = jf.getManifest();
			if (mf==null) return null;
		} catch (IOException e) {
			// error - skip
			return null;
		}
		ms = extractModuleSpec(mf);
		return ms;
	}
	
	/**
	 * @param jarfile	An InputStream to read the contents of a JAR / Manifest
	 * from.
	 * @return	Returns a ModuleSpec if one could be found in the jar.
	 * Since admadiclib v1.0.1
	 */
	public static ModuleSpec extractModuleSpec(InputStream jarfile) {
		ModuleSpec ms = null;
		JarInputStream jis = null;
		Manifest mf;
		try {
			jis = new JarInputStream(jarfile);
			if (jis==null) return null;
			mf = jis.getManifest();
			if (mf==null) return null;
		} catch (IOException e) {
			// error - skip
			return null;
		}
		ms = extractModuleSpec(mf);
		return ms;
	}

	/**
	 * @param mf
	 * @return	Returns the ModuleSpec from the given Manifest, or null
	 * if there is none.
	 */
	public static ModuleSpec extractModuleSpec(Manifest mf) {
		ModuleSpec ms = null;
		Attributes mat;
		String modName, modClassName, modReqAppVer;
		mat = mf.getMainAttributes();
		if (mat==null) return null;
		modName = mat.getValue("X-admaDIC-Module-Name");
		modClassName = mat.getValue("X-admaDIC-Module-ClassName");
		modReqAppVer = mat.getValue("X-admaDIC-Req-App-Ver");
		if (modName!=null && modClassName!=null) {
			ms = new ModuleSpec(
					modName, 
					modClassName, 
					true);
			if (modReqAppVer!=null) {
				ms.setRequiredAppVersion(VersionRecord.valueOf(modReqAppVer));
			}
		}
		return ms;
	}
	
	/**
	 * @param dir
	 * @return	Returns a list of module specs for the jars in the given 
	 * 			directory.
	 */
	public static Vector<ModuleSpec> scanModuleSpecs(File dir) {
		Vector<ModuleSpec> v = new Vector<ModuleSpec>();
		File [] flist = dir.listFiles();
		for (File file : flist) {
			if (file==null) continue;
			if (file.isDirectory()) continue;
			String fn = file.getName();
			if (!fn.toLowerCase().endsWith(".jar")) continue;

			ModuleSpec ms = ModuleUtil.extractModuleSpec(file);
			if (ms!=null) 
				v.add(ms);
		}
		return v;
	}
}
