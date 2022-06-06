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
package de.admadic.calculator.modules.matrx.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import de.admadic.calculator.modules.matrx.MatrxActions;
import de.admadic.calculator.modules.matrx.MatrxCfg;
import de.admadic.calculator.modules.matrx.core.DMatrix;
import de.admadic.calculator.modules.matrx.core.MatrixDimensionException;
import de.admadic.calculator.modules.matrx.core.MatrixOp;

/**
 * @author Rainer Schwarze
 *
 */
public class BinaryOpPanel extends JPanel {
	/** */
	private static final long serialVersionUID = 1L;

	JSplitPane splitPane;
	JSplitPane splitPaneInput;
	MatrixPanel mtxPanelInput;
	MatrixPanel mtxPanelParam;
	MatrixPanel mtxPanelResult;

	FloatingPointFormatter fpf;
	MatrxCfg cfg;
	ICfgHandler cfghdl;

	JToolBar toolBar;
	CreateNewAction actionNewFirst;
	CreateNewInputAction actionNewInputFirst;
	CreateNewSecondAction actionNewSecond;
	CreateNewInputSecondAction actionNewInputSecond;
	CalcAddAction actionAdd;
	CalcMulAction actionMul;

	DMatrix mtxInput;
	DMatrix mtxParam;
	DMatrix mtxResult;
	
	/**
	 * @param fpf 
	 * @param cfg 
	 * @param cfghdl
	 * 
	 */
	public BinaryOpPanel(
			FloatingPointFormatter fpf, MatrxCfg cfg, ICfgHandler cfghdl) {
		super();
		this.fpf = fpf;
		this.cfg = cfg;
		this.cfghdl = cfghdl;
		initContents();
	}

	/**
	 * 
	 */
	private void initContents() {
		this.setLayout(new BorderLayout());
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.add(splitPane, BorderLayout.CENTER);
		splitPaneInput = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mtxPanelInput = new MatrixPanel("First Input Matrix:", fpf);
		mtxPanelParam = new MatrixPanel("Second Input Matrix:", fpf);
		mtxPanelResult = new MatrixPanel("Result Matrix:", fpf);
		mtxPanelInput.setPreferredSize(new Dimension(100, 100));
		mtxPanelParam.setPreferredSize(new Dimension(100, 100));
		mtxPanelResult.setPreferredSize(new Dimension(100, 100));
		splitPaneInput.setLeftComponent(mtxPanelInput);
		splitPaneInput.setRightComponent(mtxPanelParam);
		splitPane.setTopComponent(splitPaneInput);
		splitPane.setBottomComponent(mtxPanelResult);
		splitPane.setDividerLocation(0.5);
		splitPaneInput.setDividerLocation(0.5);
		splitPane.setResizeWeight(0.5);
		splitPaneInput.setResizeWeight(0.5);

		toolBar = new JToolBar();
		this.add(toolBar, BorderLayout.PAGE_START);

		toolBar.add(new JButton(actionNewFirst = new CreateNewAction()));
		toolBar.add(new JButton(actionNewInputFirst = new CreateNewInputAction()));
		toolBar.addSeparator();
		toolBar.add(new JButton(actionNewSecond = new CreateNewSecondAction()));
		toolBar.add(new JButton(actionNewInputSecond = new CreateNewInputSecondAction()));
		toolBar.addSeparator();
		toolBar.add(new JButton(actionAdd = new CalcAddAction()));
		toolBar.add(new JButton(actionMul = new CalcMulAction()));

		updateActions();
	}

	/**
	 * @param mtx
	 */
	public void setFirstInputMatrix(DMatrix mtx) {
		mtxInput = mtx;
		mtxPanelInput.setMatrix(mtx);

		updateActions();
		cfghdl.notifyCfgUpdate();
	}

	/**
	 * @param mtx
	 */
	public void setSecondInputMatrix(DMatrix mtx) {
		mtxParam = mtx;
		mtxPanelParam.setMatrix(mtx);

		updateActions();
		cfghdl.notifyCfgUpdate();
	}

	/**
	 * @param mtx
	 */
	public void setResultMatrix(DMatrix mtx) {
		mtxResult = mtx;
		mtxPanelResult.setMatrix(mtx);
		cfghdl.notifyCfgUpdate();
	}

	/**
	 * 
	 */
	private void updateActions() {
		boolean state = true;
		if (mtxInput==null) state = false;
		if (mtxParam==null) state = false;

		actionAdd.setEnabled(state);
		actionMul.setEnabled(state);
	}


	/**
	 * @author Rainer Schwarze
	 */
	public class CreateNewAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CreateNewAction() {
			super();
			putValue(Action.NAME, "New 1st");
			putValue(Action.SHORT_DESCRIPTION, "Create New 1st Matrix");
//			putValue(
//					Action.SMALL_ICON, 
//					MatrxActions.loadIcon(this.getClass(), "btn-ut.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			DMatrix tmp = MatrixUiUtil.getNewMatrix();
			if (tmp==null) return;

			setFirstInputMatrix(tmp);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CreateNewInputAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CreateNewInputAction() {
			super();
			putValue(Action.NAME, "New[] 1st");
			putValue(Action.SHORT_DESCRIPTION, "Create New 1st Matrix from Input");
//			putValue(
//					Action.SMALL_ICON, 
//					MatrxActions.loadIcon(this.getClass(), "btn-ut.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			DMatrix tmp = MatrixUiUtil.getNewMatrixInput(cfg);
			if (tmp==null) return;

			setFirstInputMatrix(tmp);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CreateNewSecondAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CreateNewSecondAction() {
			super();
			putValue(Action.NAME, "New 2nd");
			putValue(Action.SHORT_DESCRIPTION, "Create New 2nd Matrix");
//			putValue(
//					Action.SMALL_ICON, 
//					MatrxActions.loadIcon(this.getClass(), "btn-ut.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			DMatrix tmp = MatrixUiUtil.getNewMatrix();
			if (tmp==null) return;

			setSecondInputMatrix(tmp);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CreateNewInputSecondAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CreateNewInputSecondAction() {
			super();
			putValue(Action.NAME, "New[] 2nd");
			putValue(Action.SHORT_DESCRIPTION, "Create New 2nd Matrix from Input");
//			putValue(
//					Action.SMALL_ICON, 
//					MatrxActions.loadIcon(this.getClass(), "btn-ut.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			DMatrix tmp = MatrixUiUtil.getNewMatrixInput(cfg);
			if (tmp==null) return;

			setSecondInputMatrix(tmp);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CalcMulAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CalcMulAction() {
			super();
			putValue(Action.NAME, "Mul");
			putValue(Action.SHORT_DESCRIPTION, "Multiply Matrices");
			putValue(
					Action.SMALL_ICON, 
					MatrxActions.loadIcon(this.getClass(), "btn-mul.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			DMatrix tmp = null;
			try {
				tmp = MatrixOp.mul(mtxInput, mtxParam);
			} catch (MatrixDimensionException e1) {
				// e1.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						"Matrix dimensions must be identical for multiplication.\n" +
						"(Error: " + e1.getMessage() + ")",
						"Matrix dimensions do not match",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			setResultMatrix(tmp);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CalcAddAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CalcAddAction() {
			super();
			putValue(Action.NAME, "Add");
			putValue(Action.SHORT_DESCRIPTION, "Add Matrices");
			putValue(
					Action.SMALL_ICON, 
					MatrxActions.loadIcon(this.getClass(), "btn-add.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			DMatrix tmp = new DMatrix(mtxInput);
			try {
				MatrixOp.add(tmp, mtxParam);
			} catch (MatrixDimensionException e1) {
				// e1.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						"Matrix dimensions must be identical for addition.\n" +
						"(Error: " + e1.getMessage() + ")",
						"Matrix dimensions do not match",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			setResultMatrix(tmp);
		}
	}
}
