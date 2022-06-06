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
package de.admadic.units.ui;

import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.units.core.Field;
import de.admadic.units.core.IField;
import de.admadic.units.core.SubField;
import de.admadic.units.core.UnitManager;

/**
 * @author Rainer Schwarze
 *
 */
public class FieldsPanel extends JPanel {
	UnitManager um;
	UnitsActions ua;
	JTree treeFields;
	JScrollPane scrollFields;

	boolean enaFil;
	boolean enaSet;
	
	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static class FieldsTree extends JTree {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param value
		 * @param selected
		 * @param expanded
		 * @param leaf
		 * @param row
		 * @param hasFocus
		 * @return	Returns the String representation.
		 * @see javax.swing.JTree#convertValueToText(java.lang.Object, boolean, boolean, boolean, int, boolean)
		 */
		@Override
		public String convertValueToText(
				Object value, 
				boolean selected, boolean expanded, boolean leaf, 
				int row, boolean hasFocus) {
			if (value!=null) {
				Object v = ((DefaultMutableTreeNode)value).getUserObject();
				if (v!=null && v instanceof Field) {
					return ((Field)v).getName();
				} else if (v!=null && v instanceof SubField) {
					return ((SubField)v).getName();
				} else {
					return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
				}
			} else {
				return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
			}
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param um 
	 * @param ua 
	 * @param enaFil 
	 * @param enaSet 
	 * 
	 */
	public FieldsPanel(UnitManager um, UnitsActions ua, 
			boolean enaFil, boolean enaSet) {
		super();
		this.enaFil = enaFil;
		this.enaSet = enaSet;
		this.um = um;
		this.ua = ua;
		initContents();
	}

	/**
	 * 
	 */
	public void initContents() {
		FormLayout fl = new FormLayout(
				"12px, p, 5px, p:grow, 12px", 
				"12px, p:grow, 5px, p, 12px");
		CellConstraints cc = new CellConstraints();
		this.setLayout(fl);

		treeFields = new FieldsTree();
		scrollFields = new JScrollPane(
				treeFields,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// treeFields.setVisibleRowCount(8);
		scrollFields.setPreferredSize(new java.awt.Dimension(200, 150));

		createData();
		
		this.add(scrollFields, cc.xy(4, 2));

		if (enaFil) {
			JPanel panFil = new JPanel();
			this.add(panFil, cc.xy(4, 4));
			UnitsUiUtil.createHorizontalButtonPanel(
				panFil,
				new AbstractButton[]{
					new JButton(ua.getAction(UnitsActions.FilterFieldsSetAction.class)),
					new JButton(ua.getAction(UnitsActions.FilterFieldsClearAction.class)),
					new JButton(ua.getAction(UnitsActions.FilterFieldsAddAction.class)),
					new JButton(ua.getAction(UnitsActions.FilterFieldsRemoveAction.class)),
				}
			);
		}

		if (enaSet) {
			JPanel panSet = new JPanel();
			this.add(panSet, cc.xy(2, 2));
			UnitsUiUtil.createVerticalButtonPanel(
				panSet,
				new AbstractButton[]{
					new JButton(ua.getAction(UnitsActions.SetFieldsSetAction.class)),
					new JButton(ua.getAction(UnitsActions.SetFieldsClearAction.class)),
					new JButton(ua.getAction(UnitsActions.SetFieldsAddAction.class)),
					new JButton(ua.getAction(UnitsActions.SetFieldsRemoveAction.class)),
				}
			);
		}
	}

	private void createData() {
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		Hashtable<Field,DefaultMutableTreeNode> nodehash = new Hashtable<Field,DefaultMutableTreeNode>(); 
		for (Field f : um.getFields()) {
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(f);
			rootNode.add(n);
			nodehash.put(f, n);
		}
		for (SubField sf : um.getSubFields()) {
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(sf);
			nodehash.get(sf.getField()).add(n);
		}
		DefaultTreeModel tm = new DefaultTreeModel(rootNode);
		treeFields.setModel(tm);
		treeFields.setRootVisible(false);
	}

	/**
	 * @return	Returns a list of selected fields.
	 */
	public IField[] getSelectedFields() {
		IField [] fa = null;
		TreePath [] sel = treeFields.getSelectionPaths();
		fa = new IField[sel.length];
		for (int i=0; i<sel.length; i++) {
			TreePath tp = sel[i];
			DefaultMutableTreeNode n = (DefaultMutableTreeNode)tp.getLastPathComponent();
			fa[i] = (IField)n.getUserObject();
		}
		return fa;
	}
}
