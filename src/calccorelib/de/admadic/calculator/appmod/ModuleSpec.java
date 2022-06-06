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

import de.admadic.util.VersionRecord;

/**
 * This class shall serve as a data container with no connection to actual
 * plugins/modules or other code. Multiple instances of ModuleSpecs may refer
 * to the same module as long as they are not loaded multiple times.
 * 
 * @author Rainer Schwarze
 */
public class ModuleSpec {
	String name;
	String className;
	boolean enabled;
	VersionRecord requiredAppVersion;	// since corelib v1.0.0

	/**
	 * @param name
	 * @param className
	 * @param enabled
	 */
	public ModuleSpec(String name, String className, boolean enabled) {
		super();
		this.name = name;
		this.className = className;
		this.enabled = enabled;
	}

	/**
	 * @return Returns the enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled The enabled to set.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return Returns the className.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return	Returns information about the instance.
	 */
	public String getDiagnosticInfo() {
		String s = "";
		s += "name=" + getName();
		s += " className=" + getClassName();
		s += " enabled=" + isEnabled();
		if (getRequiredAppVersion()!=null) {
			s += " reqAppVer=" + getRequiredAppVersion().getMjMnMcRvVersionString();
		}
		return s;
	}


	/**
	 * @return Returns the requiredAppVersion.
	 * Since calccorelib v1.0.0
	 */
	public VersionRecord getRequiredAppVersion() {
		return requiredAppVersion;
	}


	/**
	 * @param requiredAppVersion The requiredAppVersion to set.
	 * Since calccorelib v1.0.0
	 */
	public void setRequiredAppVersion(VersionRecord requiredAppVersion) {
		this.requiredAppVersion = requiredAppVersion;
	}
}
