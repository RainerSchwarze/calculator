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
package de.admadic.calculator;

import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import de.admadic.util.ClassPathExtender;
import de.admadic.util.PathManager;

/**
 * @author Rainer Schwarze
 *
 */
public class LaunchBase {
	final static boolean DBG = false;
	final static boolean LOG = true;
	Logger logger = null;

	ClassPathExtender cpe;

	/**
	 * Creates an empty LaunchCalc instance. 
	 */
	public LaunchBase() {
		super();
	}

	/**
	 * Main entry for program start.
	 * 
	 * @param args	The arguments passed on the command line.
	 */
	public static void main(String[] args) {
		LaunchBase launch = new LaunchBase();
		launch.init(args);
		launch.run(args);
		launch.shutdown();
	}

	void init(String[] args) {
		// no warn:
		if (args!=null) { /* nothing */ }

		String sysbaseStr;
		sysbaseStr = PathManager.getCodeBase(this.getClass());
		
//		URL sysbaseUrl;
//		CodeSource source;
//		source = this.getClass().getProtectionDomain().getCodeSource();
//		sysbaseUrl = source.getLocation();
//		sysbaseStr = sysbaseUrl.getPath();
//		if (sysbaseStr.toLowerCase().endsWith(".jar")) {
//			if (DBG) System.out.println("LaunchBase: is jar! stripping");
//			File f = new File(sysbaseStr);
//			sysbaseStr = f.getParent();
//		}

//		if (!sysbaseStr.endsWith(File.separator)) {
//			sysbaseStr += File.separator;
//		}
		if (DBG) System.out.println("LaunchBase: sysbase = " + sysbaseStr);

		if (LOG) logger = (LOG) ? Logger.getLogger("de.admadic") : null;

		cpe = new ClassPathExtender();
		String paths = "./lib/:./laf/:./mod/";
		String [] pa = paths.split(":");
		for (String path : pa) {
			File dir = new File(new File(sysbaseStr), path);
			if (dir.isDirectory()) {
				cpe.setURLs(dir, "*.jar");
			}
		}
		{
			File dir = new File(new File(sysbaseStr), "./");
			cpe.setURLs(dir, "calculator-core*.jar");
		}
//		cpe.dumpUrls();
		cpe.generateClassLoader();
//		cpe.registerToThread(null);
	}

	void run(String [] args) {
		Class<?> cls;
		Object obj;
		Method mth_setCpe;
		Method mth_init;
		Method mth_run;
		Method mth_shutdown;
		try {
			cls = cpe.getClassLoader().loadClass(
					"de.admadic.calculator.LaunchCalc");
			obj = cls.newInstance();
			mth_setCpe = cls.getDeclaredMethod(
					"setCpe", new Class[]{ClassPathExtender.class});
			mth_init = cls.getDeclaredMethod(
					"init", new Class[]{String[].class});
			mth_run = cls.getDeclaredMethod(
					"run", (Class[])null);
			mth_shutdown = cls.getDeclaredMethod(
					"shutdown", (Class[])null);

			mth_setCpe.invoke(obj, new Object[]{cpe});
			mth_init.invoke(obj, new Object[]{args});
			mth_run.invoke(obj, (Object[])null);
			mth_shutdown.invoke(obj, (Object[])null);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					null,
					"Error starting the calculator: " + e.getMessage() + "\n"+
					"Please contact customer support.",
					"Error Starting the Calculator",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	void shutdown() {
		// nothing for now
	}
}
