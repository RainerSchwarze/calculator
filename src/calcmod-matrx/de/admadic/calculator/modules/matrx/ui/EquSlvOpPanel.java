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
import javax.swing.JToolBar;

import de.admadic.calculator.modules.matrx.MatrxActions;
import de.admadic.calculator.modules.matrx.MatrxCfg;
import de.admadic.calculator.modules.matrx.core.DMatrix;
import de.admadic.calculator.modules.matrx.core.MatrixLinearDependentException;
import de.admadic.calculator.modules.matrx.core.MatrixOp;

/**
 * @author Rainer Schwarze
 *
 */
public class EquSlvOpPanel extends JPanel {
	/** */
	private static final long serialVersionUID = 1L;

	FloatingPointFormatter fpf;
	MatrxCfg cfg;
	ICfgHandler cfghdl;

	QuarterSplitPane splitPane;
	MatrixPanel mtxPanelInputCoeff;
	MatrixPanel mtxPanelInputAbsEl;
	MatrixPanel mtxPanelResultGauss;
	MatrixPanel mtxPanelResultSol;

	DMatrix mtxInputCoeff;
	DMatrix mtxInputAbsEl;
	DMatrix mtxResultGauss;
	DMatrix mtxResultSol;
	
	JToolBar toolBar;

	CreateNewAction actionNew;
	CreateNewInputAction actionNewInput;
	CreateNewInputAbsElAction actionNewInputAbsEl;
	SolveEquationAction actionSolve;
	
	/**
	 * @param fpf 
	 * @param cfg 
	 * @param cfghdl
	 * 
	 */
	public EquSlvOpPanel(FloatingPointFormatter fpf, MatrxCfg cfg, ICfgHandler cfghdl) {
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
		splitPane = new QuarterSplitPane();
		this.add(splitPane, BorderLayout.CENTER);
		mtxPanelInputCoeff = new MatrixPanel("Coefficient Matrix:", fpf);
		mtxPanelInputAbsEl = new MatrixPanel("Absolute Elements:", fpf);
		mtxPanelResultGauss = new MatrixPanel("Gaussian Elimination:", fpf);
		mtxPanelResultSol = new MatrixPanel("Solution:", fpf);
		mtxPanelInputCoeff.setPreferredSize(new Dimension(100, 100));
		mtxPanelInputAbsEl.setPreferredSize(new Dimension(100, 100));
		mtxPanelResultGauss.setPreferredSize(new Dimension(100, 100));
		mtxPanelResultSol.setPreferredSize(new Dimension(100, 100));
		splitPane.setTopLeftComponent(mtxPanelInputCoeff);
		splitPane.setTopRightComponent(mtxPanelInputAbsEl);
		splitPane.setBottomLeftComponent(mtxPanelResultGauss);
		splitPane.setBottomRightComponent(mtxPanelResultSol);
		splitPane.setDividerLocation(0.5, 0.75);

		toolBar = new JToolBar();
		this.add(toolBar, BorderLayout.PAGE_START);

		toolBar.add(new JButton(actionNew = new CreateNewAction()));
		toolBar.add(new JButton(actionNewInput = new CreateNewInputAction()));
		toolBar.add(new JButton(actionNewInputAbsEl = new CreateNewInputAbsElAction()));
		toolBar.add(new JButton(actionSolve = new SolveEquationAction()));

		updateActions();
	}

	/**
	 * @param mtx
	 */
	public void setInputMatrixCoeff(DMatrix mtx) {
		this.mtxInputCoeff = mtx;
		this.mtxPanelInputCoeff.setMatrix(mtx);

		updateActions();
		cfghdl.notifyCfgUpdate();
	}

	/**
	 * @param mtx
	 */
	public void setInputMatrixAbsEl(DMatrix mtx) {
		this.mtxInputAbsEl = mtx;
		this.mtxPanelInputAbsEl.setMatrix(mtx);

		updateActions();
		cfghdl.notifyCfgUpdate();
	}

	/**
	 * @param mtx
	 */
	public void setResultMatrixGauss(DMatrix mtx) {
		mtxResultGauss = mtx;
		mtxPanelResultGauss.setMatrix(mtx);
		cfghdl.notifyCfgUpdate();
	}

	/**
	 * @param mtx
	 */
	public void setResultMatrixSol(DMatrix mtx) {
		mtxResultSol = mtx;
		mtxPanelResultSol.setMatrix(mtx);
		cfghdl.notifyCfgUpdate();
	}

	/**
	 * 
	 */
	private void updateActions() {
		boolean state = true;
		if (mtxInputCoeff==null) state = false;
		if (mtxInputAbsEl==null) state = false;

		actionSolve.setEnabled(state);
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
			putValue(Action.NAME, "New");
			putValue(Action.SHORT_DESCRIPTION, 
					"Create New Coefficient and Absolute Elements Matrix");
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
			DMatrix tmp2 = new DMatrix(tmp.getRowCount(), 1);

			setInputMatrixCoeff(tmp);
			setInputMatrixAbsEl(tmp2);
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
			putValue(Action.NAME, "New[]");
			putValue(Action.SHORT_DESCRIPTION, "Create New Coefficient Matrix from Input");
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

			setInputMatrixCoeff(tmp);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CreateNewInputAbsElAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CreateNewInputAbsElAction() {
			super();
			putValue(Action.NAME, "New|");
			putValue(Action.SHORT_DESCRIPTION, "Create New Absolute Elements Matrix from Input");
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
			if (tmp.getColumnCount()!=1) {
				JOptionPane.showMessageDialog(
						null,
						"Absolute elements matrix must have 1 column",
						"Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			setInputMatrixAbsEl(tmp);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SolveEquationAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public SolveEquationAction() {
			super();
			putValue(Action.NAME, "Solve");
			putValue(Action.SHORT_DESCRIPTION, "Solve Equation");
			putValue(
					Action.SMALL_ICON, 
					MatrxActions.loadIcon(this.getClass(), "btn-equ.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			DMatrix tmp1 = new DMatrix(mtxInputAbsEl);
			MatrixOp.mul(tmp1, -1.0);
			DMatrix tmp2 = MatrixOp.appendMatrixColumns(mtxInputCoeff, tmp1);
			try {
				MatrixOp.convertToUpperTriangularForm(tmp2);
			} catch (MatrixLinearDependentException e1) {
				// e1.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						"Equations are linear dependent\n"+
						"(Error: " + e1.getMessage() + ")",
						"Cannot solve LES",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			setResultMatrixGauss(tmp2);
			DMatrix tmp3 = new DMatrix(mtxInputCoeff.getColumnCount(), 1);
			MatrixOp.calcSolution(tmp2, tmp3);
			setResultMatrixSol(tmp3);
		}
	}
}
