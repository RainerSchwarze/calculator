package de.admadic.calculator.modules.masca.ui;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import de.admadic.calculator.modules.masca.core.AbstractCalculation;
import de.admadic.calculator.modules.masca.core.CalcCategory;

/**
 * @author Rainer Schwarze
 *
 */
public class CalculationTree extends JTree {
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
		if (value==null)
			return super.convertValueToText(
					value, selected, expanded, leaf, row, hasFocus);
		Object v = ((DefaultMutableTreeNode)value).getUserObject();
		if (v==null)
			return super.convertValueToText(
					value, selected, expanded, leaf, row, hasFocus);

		if (v instanceof AbstractCalculation) {
			return ((AbstractCalculation)v).getTitle();
		} else if (v instanceof CalcCategory) { 
			return ((CalcCategory)v).getName();
		} else {
			return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
		}
	}
}