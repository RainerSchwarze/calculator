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
package de.admadic.calculator.ui;

import java.util.Hashtable;

/**
 * @author Rainer Schwarze
 *
 */
public class CmdSet {
	Hashtable<String,CmdEntry> commands;
	Hashtable<String,CmdStyleGrp> groups;

	/**
	 * 
	 */
	public CmdSet() {
		super();
		commands = new Hashtable<String,CmdEntry>();
		groups = new Hashtable<String,CmdStyleGrp>();
	}

	/**
	 * @param cmd 
	 * @return Returns the CmdEntry for the given cmd.
	 * @see java.util.Hashtable#get(java.lang.Object)
	 */
	public CmdEntry get(String cmd) {
		return commands.get(cmd);
	}

	/**
	 * @param cmd
	 * @return	Returns the CmdStyleGrp for the given cmd.
	 */
	public CmdStyleGrp getGroupOfCommand(String cmd) {
		CmdEntry cmdE = commands.get(cmd);
		CmdStyleGrp cmdG = groups.get(cmdE.getGroupName());
		return cmdG;
	}

	/**
	 * @param cmdE 
	 */
	public void add(CmdEntry cmdE) {
		commands.put(cmdE.getCmd(), cmdE);
	}

	/**
	 * @param cmdG
	 */
	public void add(CmdStyleGrp cmdG) {
		groups.put(cmdG.getName(), cmdG);
	}

	/**
	 * @param name
	 * @return	Returns the CmdStyleGrp for the given group name.
	 */
	public CmdStyleGrp getCmdGrp(String name) {
		return groups.get(name);
	}
}
