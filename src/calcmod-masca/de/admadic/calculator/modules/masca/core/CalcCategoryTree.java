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
package de.admadic.calculator.modules.masca.core;

import java.util.Hashtable;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcCategoryTree {
	DefaultMutableTreeNode treeRoot;
	DefaultMutableTreeNode allNode;
	Hashtable<String,DefaultMutableTreeNode> idToNode;

	/**
	 * 
	 */
	public CalcCategoryTree() {
		super();
		treeRoot = new DefaultMutableTreeNode();
		allNode = new DefaultMutableTreeNode(new CalcCategory(".all", "All"));
		treeRoot.add(allNode);
		idToNode = new Hashtable<String,DefaultMutableTreeNode>();
	}

	/**
	 * @param parentId
	 * @param c
	 */
	public void addCategory(String parentId, CalcCategory c) {
		DefaultMutableTreeNode parent = null;
		if (parentId==null) {
			parent = treeRoot;
		} else {
			parent = idToNode.get(parentId);
		}
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(c);
		idToNode.put(c.getId(), node);
		parent.add(node);
	}

	/**
	 * @param parentId
	 * @param ce
	 */
	public void addCalculation(String parentId, AbstractCalculation ce) {
		DefaultMutableTreeNode parent = null;
		if (parentId==null) {
			parent = treeRoot;
		} else {
			parent = idToNode.get(parentId);
		}
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(ce);
		// idToNode.put(c.getId(), node);
		parent.add(node);
		allNode.add(new DefaultMutableTreeNode(ce));
	}

	/**
	 * @return	Returns the root of the tree.
	 */
	public DefaultMutableTreeNode getRoot() {
		return treeRoot;
	}
}
