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
package de.admadic.cfg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;

import de.admadic.util.FileUtil;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLException;
import net.n3.nanoxml.XMLParserFactory;

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class CfgPersistenceXMLTest extends TestCase {
	CfgPersistenceXML cpx;
	Cfg testCfg;
	String persPath;
	String fileName;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(CfgPersistenceXMLTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		testCfg = new Cfg();
		cpx = new CfgPersistenceXML(testCfg);
		persPath = "/test.de.admadic.cfg";
		fileName = "./tmp/CfgPersistenceXML.xml";
		cpx.setFileName(fileName);
		testCfg.putStringValue("a string", "string value");
		testCfg.putIntValue("a int", 17);
		testCfg.putBooleanValue("a boolean", true);
		File f = new File(fileName);
		f.delete();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected void testNanoXMLCase(String fname) {
		try {
			IXMLParser parser;
			parser = XMLParserFactory.createDefaultXMLParser();
			IXMLReader reader = StdXMLReader.fileReader(fname);
			parser.setReader(reader);
			IXMLElement xmlPref = (IXMLElement)parser.parse();
			if (xmlPref==null) { /* nothing - no warn */ }
		} catch (ClassNotFoundException e) {
			// FIXME: fix the exception printing
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void testGroupInterface() {
		cpx.store(persPath);

		{
			Cfg readCfg = new Cfg();
			CfgPersistenceXML readCpx = new CfgPersistenceXML(readCfg);
			readCpx.setFileName(fileName);
			readCpx.load(persPath);
			assertEquals(
					"testGroupInterface: a string not loaded",
					"string value", readCfg.getStringValue("a string", null));
			assertEquals(
					"testGroupInterface: a int not loaded",
					17, readCfg.getIntValue("a int", 0));
			assertEquals(
					"testGroupInterface: a boolean not loaded",
					true, readCfg.getBooleanValue("a boolean", false));
	
			readCpx.removeKeys(persPath, readCfg.getCfgItemKeys());
			Enumeration<String> keys = readCfg.getCfgItemKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				readCfg.removeCfgItem(key);
			}
			readCpx.store(persPath);
		}

		{
			Cfg removeCfg = new Cfg();
			CfgPersistenceXML removeCpx = new CfgPersistenceXML(removeCfg);
			removeCpx.setFileName(fileName);
			removeCpx.load(persPath);

			assertEquals(
					"testGroupInterface: a string was loaded which should be removed",
					"dummy", removeCfg.getStringValue("a string", "dummy"));
			assertEquals(
					"testGroupInterface: a int was loaded which should be removed",
					42, removeCfg.getIntValue("a int", 42));
			assertEquals(
					"testGroupInterface: a boolean was loaded which should be removed",
					false, removeCfg.getBooleanValue("a boolean", false));
		}
		{
			String wd = System.getProperty("user.dir");
			System.out.println("wd = " + wd);
			testNanoXMLCase("/D:/data/java/calculator/tmp/CfgPersistenceXML.xml");
			testNanoXMLCase(FileUtil.fixFileName1(wd + "/tmp/CfgPersistenceXML.xml"));
		}
	}

	/**
	 * 
	 */
	public void testLoad() {
		// not yet done
	}

	/**
	 * 
	 */
	public void testStore() {
		// not yet done
	}

	/**
	 * 
	 */
	public void testRemoveKeys() {
		// not yet done
	}

	/**
	 * 
	 */
	public void testClear() {
		// not yet done
	}

}
