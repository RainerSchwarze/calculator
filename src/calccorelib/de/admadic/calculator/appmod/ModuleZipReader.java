package de.admadic.calculator.appmod;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Rainer Schwarze
 *
 */
public class ModuleZipReader {
	String zipName;
	ZipFile zipFile;
	Vector<String> entries;
	boolean flgHasInstallCfg;
	String installCfgName;
	boolean flgHasLicenseTxt;
	String licenseTxtName;
	Properties installCfgProp;
	ArrayList<String> cfgEntries;
	ArrayList<String> cfgEntryTypes;

	/**
	 * @param zipName 
	 * 
	 */
	public ModuleZipReader(String zipName) {
		super();
		this.zipName = zipName;
		entries = new Vector<String>();
	}

	/**
	 * @throws ModuleZipReaderException 
	 * 
	 */
	public void open() throws ModuleZipReaderException {
		if (zipFile!=null) {
			// already open!
			return;
		}
		try {
			zipFile = new ZipFile(zipName);
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			throw new ModuleZipReaderException("Error opening ZIP file: " + 
					e.getMessage());
		} catch (IOException e) {
			// e.printStackTrace();
			throw new ModuleZipReaderException("Error opening ZIP file: " + 
					e.getMessage());
		}
	}

	/**
	 * @throws ModuleZipReaderException 
	 * 
	 */
	public void close() throws ModuleZipReaderException {
		if (zipFile==null) {
			// already closed
			return;
		}
		try {
			zipFile.close();
		} catch (IOException e) {
			// e.printStackTrace();
			throw new ModuleZipReaderException("Error closing ZIP file: " + 
					e.getMessage());
		}
		zipFile = null;
	}
	
	/**
	 * @throws ModuleZipReaderException 
	 * 
	 */
	public void buildEntryList() throws ModuleZipReaderException {
		if (zipFile==null) {
			throw new ModuleZipReaderException("ZIP file not opened");
		}
		try {
			ZipEntry zipEntry;
			Enumeration<? extends ZipEntry> en;

			en = zipFile.entries();
			while (en.hasMoreElements()) {
				zipEntry = en.nextElement();
				if (zipEntry == null) {
					break;
				}
				String tmp = zipEntry.getName();
				entries.add(tmp);
				if (tmp.toLowerCase().equals("install.cfg")) {
					flgHasInstallCfg = true;
					installCfgName = tmp;
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ModuleZipReaderException("Error reading ZIP file entries: " + 
					e.getMessage());

		}
	}

	/**
	 * @return	Returns true, if the ZIP archive contains an install.cfg.
	 */
	public boolean hasInstallCfg() {
		return flgHasInstallCfg;
	}
	
	/**
	 * @throws ModuleZipReaderException 
	 * 
	 */
	public void readInstallCfg() throws ModuleZipReaderException {
		if (!flgHasInstallCfg) return;
		ZipFile zf;
		ZipEntry ze;
		InputStream is;
		BufferedInputStream bis;
		byte [] data;
		int datalen;
		StringBuffer sb = new StringBuffer();
		try {
			zf = new ZipFile(zipName);
			ze = zf.getEntry(installCfgName);
			is = zf.getInputStream(ze);
			bis = new BufferedInputStream(is);
			long es = ze.getSize();
			if (es<0 || es>Integer.MAX_VALUE) {
				// throw new error?
				throw new ModuleZipReaderException(
						"entry too long or unknown: " + es);
			}
			data = new byte[(int)es];
			datalen = bis.read(data);
			for (int i = 0; i < datalen; i++) {
				sb.append((char)data[i]);
			}
			// System.out.println("install.cfg:\n" + sb.toString());
			is = zf.getInputStream(ze);
			installCfgProp = new Properties();
			installCfgProp.load(is);
		} catch (IOException e) {
			// e.printStackTrace();
			throw new ModuleZipReaderException(
					"Error reading install.cfg: " + e.getMessage());
		}
	}

	/**
	 * @throws ModuleZipReaderException 
	 * 
	 */
	public void parseInstallCfg() throws ModuleZipReaderException {
		if (installCfgProp==null) return;
		cfgEntries = new ArrayList<String>();
		cfgEntryTypes = new ArrayList<String>();
		for (Object o : installCfgProp.keySet()) {
			String k = (String)o;
			String v = (String)installCfgProp.get(o);
			if (k.trim().toLowerCase().equals("version")) {
				if (!v.trim().toLowerCase().equals("1")) {
					throw new ModuleZipReaderException(
							"Version not supported: " + v);
				}
			} else if (k.startsWith("lib")) {
				cfgEntries.add(v);
				cfgEntryTypes.add("lib");
			} else if (k.startsWith("mod")) {
				cfgEntries.add(v);
				cfgEntryTypes.add("mod");
			} else if (k.startsWith("lic")) {
				cfgEntries.add(v);
				cfgEntryTypes.add("lic");
			}
		}
	}

	/**
	 * @param type
	 * @return	Returns a list of entries for the given type.
	 */
	public Vector<String> getEntries(String type) {
		Vector<String> v = null;
		if (cfgEntryTypes==null) return null;
		int ecnt = Collections.frequency(cfgEntryTypes, type);
		if (ecnt==0) return null;

		v = new Vector<String>();
		for (int i = 0; i < cfgEntryTypes.size(); i++) {
			String t = cfgEntryTypes.get(i);
			if (t.equals(type)) {
				v.add(cfgEntries.get(i));
			}
		}
		return v;
	}
	
	/**
	 * @param entryName
	 * @param dstFile
	 * @throws ModuleZipReaderException 
	 */
	public void extractFileTo(String entryName, File dstFile) 
	throws ModuleZipReaderException {
		ZipEntry ze;
		InputStream is;
		FileOutputStream fos;
		BufferedInputStream bis;
		BufferedOutputStream bos;
		byte [] data = new byte[16384];
		int datalen;
		try {
			ze = zipFile.getEntry(entryName);
			is = zipFile.getInputStream(ze);
			bis = new BufferedInputStream(is);

			fos = new FileOutputStream(dstFile);
			bos = new BufferedOutputStream(fos);
			
			while (true) {
				datalen = bis.read(data);
				if (datalen<0) break; // eof
				bos.write(data, 0, datalen);
			}
		} catch (IOException e) {
			// e.printStackTrace();
			throw new ModuleZipReaderException(
					"Error reading ZIP file: " + e.getMessage());
		}
	}

	/**
	 * @param entryName
	 * @throws ModuleZipReaderException 
	 */
	public void extractJarInfo(String entryName) 
	throws ModuleZipReaderException {
		ZipFile zf;
		ZipEntry ze;
		InputStream is;
		Manifest mf;
		Attributes mat;
		JarInputStream jis;
		try {
			zf = new ZipFile(zipName);
			ze = zf.getEntry(entryName);
			is = zf.getInputStream(ze);
			jis = new JarInputStream(is);
			mf = jis.getManifest();
			mat = mf.getMainAttributes();
			for (Object okey : mat.keySet()) {
				Object v = mat.get(okey);
				System.out.println("attr: " + okey + " => " + v);
			}
		} catch (IOException e) {
			// e.printStackTrace();
			throw new ModuleZipReaderException(
					"Error reading ZIP file: " + e.getMessage());
		}
	}

	/**
	 * @param entry
	 * @return	Returns an InputStream for the given entry, or null, if there 
	 * is none.
	 */
	public InputStream getStream(String entry) {
		if (zipFile==null) {
			return null; // maybe error?
		}
		ZipEntry ze = zipFile.getEntry(entry);
		if (ze==null) return null;
		try {
			return zipFile.getInputStream(ze);
		} catch (IOException e) {
			// e.printStackTrace();
			return null;
		}
	}
}