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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

import javax.swing.JOptionPane;

import de.admadic.log.SimpleOneLineFormatter;
import de.admadic.util.PathManager;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcLog {
	Properties props;
	String logPath;
	String baseDir;
	Hashtable<String,Logger> loggers;

	/**
	 * 
	 */
	public CalcLog() {
		super();
		props = new Properties();
		loggers = new Hashtable<String,Logger>();
	}

	/**
	 * @param pm 
	 * 
	 */
	public void init(PathManager pm) {
		load(pm);
		setup(pm);
		initialMessage();
	}

	protected void initialMessage() {
		Enumeration<String> lns = LogManager.getLogManager().getLoggerNames();
		String lnames = "";
		while (lns.hasMoreElements()) {
			lnames += lns.nextElement() + " ";
		}
		Logger logger = Logger.getLogger("de.admadic.calculator");
		logger.info("-------------------");
		logger.info("Logging has been set up for " + loggers.size() + 
				" loggers: " + lnames);
	}

	protected void error(String msg) {
		System.err.println("Calculator: " + msg);
	}

	protected void load(PathManager pm) {
		File file = null;
		FileInputStream fin;

		try {
			file = new File(
					pm.getPathString(PathManager.USR_CFG_DIR) + 
					"/log.cfg");
			if (!file.exists()) { 
				file = new File(
						pm.getPathString(PathManager.SYS_CFG_DIR) + 
						"/log.cfg");
			}
//			System.out.println("logcfg = " + file.toString());
			fin = new FileInputStream(file);
			props.load(fin);
		} catch (FileNotFoundException e) {
			// FIXME: make that a real error!
//			e.printStackTrace();
			error("a cfg file could not be found!");
		} catch (IOException e) {
//			e.printStackTrace();
			error("a cfg file exists, but could not be read! (" + 
					((file!=null) ? file.toString() : "<null>") + ")");
		}
	}

	protected void tryLogPath() {
		File dir = new File(logPath);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				// FIXME: error?
			}
		}
	}

	protected void setup(PathManager pm) {
		String tmp;

		tmp = props.getProperty("de.admadic.calculator.CalcLog.logpath");
		if (tmp!=null) {
			File f;
			f = PathManager.expandFilename(tmp);
			logPath = f.toString();
			// error("logpath=" + logPath);
			logPath = tmp;
		} else {
			logPath = pm.getPathString(PathManager.USR_LOG_DIR);
		}
		tryLogPath();

		if (logPath.endsWith("/") || logPath.endsWith(File.separator)) {
			// already ends with a '/'
		} else {
			logPath += File.separator;
		}

		int ploggers = 0;
		tmp = props.getProperty("de.admadic.calculator.CalcLog.loggers");
		if (tmp!=null) {
			ploggers = Integer.parseInt(tmp);
			for (int i=0; i<ploggers; i++) {
				Logger logger;
				String name;
				String levelname;
				String useparent;
				name = props.getProperty(
						"de.admadic.calculator.CalcLog.logger" + i + 
						".name");
				if (name==null) {
					name = "";
				}
				levelname = props.getProperty(
						"de.admadic.calculator.CalcLog.logger" + i + 
						".level");
				useparent = props.getProperty(
						"de.admadic.calculator.CalcLog.logger" + i + 
						".useparent");

				logger = Logger.getLogger(name);
				loggers.put(name, logger);
				if (levelname!=null) {
					levelname = levelname.toUpperCase();
					Level level;
					try {
						level = Level.parse(levelname);
						logger.setLevel(level);
					} catch (Exception e) {
						// nothing
						error("level information could not be parsed: " + levelname);
					}
				}
				if (useparent!=null) {
					useparent.toLowerCase();
					if (useparent.equals("true")) {
						logger.setUseParentHandlers(true);
					}
					if (useparent.equals("false")) {
						logger.setUseParentHandlers(false);
					}
				}
			} // (for loggers)
		} // (if has loggers)

		int handlers = 0;
		tmp = props.getProperty("de.admadic.calculator.CalcLog.handlers");
		if (tmp!=null) {
			handlers = Integer.parseInt(tmp);
			for (int i=0; i<handlers; i++) {
				Formatter fmtr = null;
				FileHandler fhdlr = null;
				String formatter;
				String filename;
				formatter = props.getProperty(
						"de.admadic.calculator.CalcLog.handler" + i + 
						".formatter");
				filename =  props.getProperty(
						"de.admadic.calculator.CalcLog.handler" + i + 
						".filename");
				if (formatter==null) continue;
				if (filename==null) continue;

				if (formatter.equals("txt")) {
					fmtr = new SimpleFormatter();
				} else if (formatter.equals("txt1")) {
					fmtr = new SimpleOneLineFormatter();
				} else if (formatter.equals("xml")) {
					fmtr = new XMLFormatter();
				} else {
					error("invalid formatter value in cfg: " + formatter);
					continue;
				}
				if (logPath!=null) {
					filename = logPath + filename;
				}
				try {
					fhdlr = new FileHandler(filename, 100000, 5, true);
					fhdlr.setFormatter(fmtr);
					for (Logger logger : loggers.values()) {
						logger.addHandler(fhdlr);
					}
				} catch (SecurityException e) {
					// e.printStackTrace();
					JOptionPane.showMessageDialog(
							null,
							"Could not set up logging system: " + e.getMessage() + "\n"+
							"Please contact customer support.",
							"Error Starting Logging System",
							JOptionPane.ERROR_MESSAGE);
				} catch (IOException e) {
					// e.printStackTrace();
					JOptionPane.showMessageDialog(
							null,
							"Could not set up logging system: " + e.getMessage() + "\n"+
							"Please contact customer support.",
							"Error Starting Logging System",
							JOptionPane.ERROR_MESSAGE);
				}
			} // (for handlers)
		} // (if has handlers)
	} // (setup)
}
