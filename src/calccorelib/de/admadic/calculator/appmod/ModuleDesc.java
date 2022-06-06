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

/**
 * @author Rainer Schwarze
 *
 */
public class ModuleDesc {
	String specName;
	String specVendor;
	String specVersion;

	String implName;
	String implVendor;
	String implVersion;

	String cfgName;	// module name in cfg spaces
	
	/**
	 * 
	 */
	public ModuleDesc() {
		super();
	}

	/**
	 * @return Returns the implName.
	 */
	public String getImplName() {
		return implName;
	}

	/**
	 * @param implName The implName to set.
	 */
	public void setImplName(String implName) {
		this.implName = implName;
	}

	/**
	 * @return Returns the implVendor.
	 */
	public String getImplVendor() {
		return implVendor;
	}

	/**
	 * @param implVendor The implVendor to set.
	 */
	public void setImplVendor(String implVendor) {
		this.implVendor = implVendor;
	}

	/**
	 * @return Returns the implVersion.
	 */
	public String getImplVersion() {
		return implVersion;
	}

	/**
	 * @param implVersion The implVersion to set.
	 */
	public void setImplVersion(String implVersion) {
		this.implVersion = implVersion;
	}

	/**
	 * @return Returns the specName.
	 */
	public String getSpecName() {
		return specName;
	}

	/**
	 * @param specName The specName to set.
	 */
	public void setSpecName(String specName) {
		this.specName = specName;
	}

	/**
	 * @return Returns the specVendor.
	 */
	public String getSpecVendor() {
		return specVendor;
	}

	/**
	 * @param specVendor The specVendor to set.
	 */
	public void setSpecVendor(String specVendor) {
		this.specVendor = specVendor;
	}

	/**
	 * @return Returns the specVersion.
	 */
	public String getSpecVersion() {
		return specVersion;
	}

	/**
	 * @param specVersion The specVersion to set.
	 */
	public void setSpecVersion(String specVersion) {
		this.specVersion = specVersion;
	}

	/**
	 * @param vendor
	 * @param name
	 * @param version
	 */
	public void setImplDetails(String vendor, String name, String version) {
		setImplVendor(vendor);
		setImplName(name);
		setImplVersion(version);
	}

	/**
	 * @param vendor
	 * @param name
	 * @param version
	 */
	public void setSpecDetails(String vendor, String name, String version) {
		setSpecVendor(vendor);
		setSpecName(name);
		setSpecVersion(version);
	}

	/**
	 * @return	Returns a summary of the implementation information.
	 */
	public String getImplInfo() {
		return getImplVendor() + "/" + getImplName() + "/" + getImplVersion();
	}

	/**
	 * @return	Returns a summary of the specification information.
	 */
	public String getSpecInfo() {
		return getSpecVendor() + "/" + getSpecName() + "/" + getSpecVersion();
	}

	/**
	 * @return Returns the cfgName.
	 */
	public String getCfgName() {
		return cfgName;
	}

	/**
	 * @param cfgName The cfgName to set.
	 */
	public void setCfgName(String cfgName) {
		this.cfgName = cfgName;
	}
}
