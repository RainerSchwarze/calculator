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
import de.admadic.calculator.modules.matrx.core.MatrixException;
import de.admadic.calculator.modules.matrx.core.MatrixLinearDependentException;
import de.admadic.calculator.modules.matrx.core.MatrixOp;

/**
 * @author Rainer Schwarze
 *
 */
public class SimpleOpPanel extends JPanel {
	/** */
	private static final long serialVersionUID = 1L;
	FloatingPointFormatter fpf;
	MatrxCfg cfg;
	ICfgHandler cfghdl;

	JToolBar toolBar;
	JSplitPane splitPane;
	MatrixPanel mtxPanelInput;
	MatrixPanel mtxPanelResult;
	DMatrix mtxInput;
	DMatrix mtxResult;

	CreateNewAction actionNew;
	CreateNewInputAction actionNewInput;
	CreateUTFormAction actionUTForm;
	CalcTransposeAction actionTranspose;
	CalcCofactorAction actionCofactor;
	CalcAdjointAction actionAdjoint;
	CalcInverseAction actionInverse;
	CalcDeterminantAction actionDet;
	CalcTraceAction actionTrace;
	CopyResultToInputAction actionCopy;

	/**
	 * @param fpf 
	 * @param cfg 
	 * @param cfghdl
	 * 
	 */
	public SimpleOpPanel(
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
		toolBar = new JToolBar();
		this.add(splitPane, BorderLayout.CENTER);
		this.add(toolBar, BorderLayout.PAGE_START);
		mtxPanelInput = new MatrixPanel("Input Matrix:", fpf);
		mtxPanelResult = new MatrixPanel("Result Matrix:", fpf);
		mtxPanelInput.setPreferredSize(new Dimension(100, 100));
		mtxPanelResult.setPreferredSize(new Dimension(100, 100));
		splitPane.setTopComponent(mtxPanelInput);
		splitPane.setBottomComponent(mtxPanelResult);
		splitPane.setDividerLocation(0.5);
		splitPane.setResizeWeight(0.5);
		toolBar.add(new JButton(actionNew = new CreateNewAction()));
		toolBar.add(new JButton(actionNewInput = new CreateNewInputAction()));
		toolBar.add(new JButton(actionUTForm = new CreateUTFormAction()));
		toolBar.add(new JButton(actionTranspose = new CalcTransposeAction()));
		toolBar.add(new JButton(actionCofactor = new CalcCofactorAction()));
		toolBar.add(new JButton(actionAdjoint = new CalcAdjointAction()));
		toolBar.add(new JButton(actionInverse = new CalcInverseAction()));
		toolBar.add(new JButton(actionDet = new CalcDeterminantAction()));
		toolBar.add(new JButton(actionTrace = new CalcTraceAction()));
		toolBar.add(new JButton(actionCopy = new CopyResultToInputAction()));
		toolBar.setRollover(true);

		setInputMatrix(null);
	}

	/**
	 * @param mtx
	 */
	public void setInputMatrix(DMatrix mtx) {
		this.mtxInput = mtx;
		this.mtxPanelInput.setMatrix(mtx);
		boolean state = (mtx!=null);
		
		actionUTForm.setEnabled(state);
		actionDet.setEnabled(state);
		actionTranspose.setEnabled(state);
		actionTrace.setEnabled(state);
		actionCofactor.setEnabled(state);
		actionInverse.setEnabled(state);
		actionAdjoint.setEnabled(state);

		cfghdl.notifyCfgUpdate();
	}

	/**
	 * @param mtx
	 */
	public void setResultMatrix(DMatrix mtx) {
		this.mtxResult = mtx;
		this.mtxPanelResult.setMatrix(mtx);

		cfghdl.notifyCfgUpdate();
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
			putValue(Action.SHORT_DESCRIPTION, "Create New Matrix");
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

			setInputMatrix(tmp);
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
			putValue(Action.SHORT_DESCRIPTION, "Create New Matrix from Input");
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

			setInputMatrix(tmp);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CreateUTFormAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CreateUTFormAction() {
			super();
//			putValue(Action.NAME, "Create UT");
			putValue(Action.SHORT_DESCRIPTION, "Create Upper Triangular Form");
			putValue(
					Action.SMALL_ICON, 
					MatrxActions.loadIcon(this.getClass(), "btn-ut.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			mtxResult = new DMatrix(mtxInput);
			try {
				MatrixOp.convertToUpperTriangularForm(mtxResult);
			} catch (MatrixLinearDependentException e1) {
				// e1.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						"Could not calculate upper triangular form.\n" +
						"(Error: " + e1.getMessage() + ")",
						"Calculation error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			setResultMatrix(mtxResult);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CalcDeterminantAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CalcDeterminantAction() {
			super();
//			putValue(Action.NAME, "Det(A)");
			putValue(Action.SHORT_DESCRIPTION, "Calculate Determinant");
			putValue(
					Action.SMALL_ICON, 
					MatrxActions.loadIcon(this.getClass(), "btn-det.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			Double v = null;
			try {
				v = MatrixOp.calcDeterminant(mtxInput);
			} catch (MatrixException e1) {
				// e1.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						"Could not calculate determinant of matrix.\n" +
						"(Error: " + e1.getMessage() + ")",
						"Calculation error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (v==null) {
				mtxResult = null;
			} else {
				mtxResult = new DMatrix(new Double[][]{{v}});
			}
			setResultMatrix(mtxResult);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CalcTransposeAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CalcTransposeAction() {
			super();
//			putValue(Action.NAME, "Calc Transpose");
			putValue(Action.SHORT_DESCRIPTION, "Calculate Transpose");
			putValue(
					Action.SMALL_ICON, 
					MatrxActions.loadIcon(this.getClass(), "btn-tra.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			mtxResult = MatrixOp.createTranspose(mtxInput);
			setResultMatrix(mtxResult);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CalcTraceAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CalcTraceAction() {
			super();
//			putValue(Action.NAME, "trace(A)");
			putValue(Action.SHORT_DESCRIPTION, "Calculate Trace");
			putValue(
					Action.SMALL_ICON, 
					MatrxActions.loadIcon(this.getClass(), "btn-trc.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			Double v = MatrixOp.calcTrace(mtxInput);
			if (v==null) {
				mtxResult = null;
			} else {
				mtxResult = new DMatrix(new Double[][]{{v}});
			}
			setResultMatrix(mtxResult);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CalcCofactorAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CalcCofactorAction() {
			super();
//			putValue(Action.NAME, "cofactor(A)");
			putValue(Action.SHORT_DESCRIPTION, "Calculate Cofactor");
			putValue(
					Action.SMALL_ICON, 
					MatrxActions.loadIcon(this.getClass(), "btn-cof.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			int selrow = mtxPanelInput.getSelectedRow();
			int selcol = mtxPanelInput.getSelectedColumn();
			if (selrow<0 || selcol<0) {
				JOptionPane.showMessageDialog(
						null,
						"Please select a cell",
						"Cannot calculate cofactor",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			Double v = null;
			try {
				v = MatrixOp.calcCofactor(mtxInput, selrow, selcol);
			} catch (MatrixException e1) {
				// e1.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						"Could not calculate cofactor of selected element.\n" +
						"(Error: " + e1.getMessage() + ")",
						"Calculation error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (v==null) {
				mtxResult = null;
			} else {
				mtxResult = new DMatrix(new Double[][]{{v}});
			}
			setResultMatrix(mtxResult);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CalcAdjointAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CalcAdjointAction() {
			super();
//			putValue(Action.NAME, "adj(A)");
			putValue(Action.SHORT_DESCRIPTION, "Calculate Adjoint");
			putValue(
					Action.SMALL_ICON, 
					MatrxActions.loadIcon(this.getClass(), "btn-adj.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				mtxResult = MatrixOp.calcAdjoint(mtxInput);
			} catch (MatrixException e1) {
				// e1.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						"Could not calculate adjoint matrix.\n" +
						"(Error: " + e1.getMessage() + ")",
						"Calculation error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			setResultMatrix(mtxResult);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CalcInverseAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CalcInverseAction() {
			super();
//			putValue(Action.NAME, "inverse(A)");
			putValue(Action.SHORT_DESCRIPTION, "Calculate Inverse");
			putValue(
					Action.SMALL_ICON, 
					MatrxActions.loadIcon(this.getClass(), "btn-inv.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				mtxResult = MatrixOp.calcInverse(mtxInput);
			} catch (MatrixException e1) {
				// e1.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						"Could not calculate inverse matrix.\n" +
						"(Error: " + e1.getMessage() + ")",
						"Calculation error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			setResultMatrix(mtxResult);
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CopyResultToInputAction extends AbstractAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CopyResultToInputAction() {
			super();
			putValue(Action.NAME, "res->inp");
			putValue(Action.SHORT_DESCRIPTION, "Copy result to input");
//			putValue(
//					Action.SMALL_ICON, 
//					MatrxActions.loadIcon(this.getClass(), "btn-inv.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			mtxInput = (mtxResult!=null) ? new DMatrix(mtxResult) : null;
			setInputMatrix(mtxInput);
		}
	}
}
