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

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.modules.matrx.core.DMatrix;
import de.admadic.ui.util.Dialog;

/**
 * @author Rainer Schwarze
 *
 */
public class MatrixInputDialog extends Dialog implements ActionListener {
	/** */
	private static final long serialVersionUID = 1L;

	JTextArea textInfo;
	JTextArea textMatrix;
	JScrollPane scrollMatrix;
	JButton btnOK;
	JButton btnCancel;
	JButton btnCheck;
	JPanel panelButtons;
	JLabel labelStatus;

	final static String CMD_OK = "ok";
	final static String CMD_CANCEL = "cancel";
	final static String CMD_CHECK = "check";

	DMatrix matrix;
	
	/**
	 * @param parent 
	 * @param data 
	 * @throws HeadlessException
	 */
	public MatrixInputDialog(Container parent, String data) throws HeadlessException {
		super();
		initContents(parent, data);
	}

	/**
	 * @param parent 
	 * @param data 
	 * 
	 */
	private void initContents(Container parent, String data) {
		FormLayout fl = new FormLayout(
				"12px, d:grow, 12px",
				"12px, d: 5px, d:grow, 5px, d, 5px, d, 12px");
		this.getContentPane().setLayout(fl);
		CellConstraints cc = new CellConstraints();

		this.setModal(true);
		this.setTitle("Enter Matrix Data");

		textInfo = new JTextArea(
				"Enter matrix data:\n"+
				"For example: 1 2 3; 3 2 1; 1 3 2\n"+
				"or put each row on its own line like:\n"+
				"1 2 3\n"+
				"3 2 1\n"+
				"1 3 2");
		textInfo.setOpaque(false);
		textInfo.setEditable(false);
		this.getContentPane().add(textInfo, cc.xy(2, 2));

		textMatrix = new JTextArea(data);
		textMatrix.setRows(8);
		textMatrix.setColumns(40);
		scrollMatrix = new JScrollPane(
				textMatrix, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.getContentPane().add(scrollMatrix, 
				cc.xy(2, 4, CellConstraints.FILL, CellConstraints.FILL));

		labelStatus = new JLabel("ok");
		this.getContentPane().add(labelStatus, cc.xy(2, 6));
		
		panelButtons = new JPanel();
		this.getContentPane().add(panelButtons, cc.xy(2, 8));

		btnOK = new JButton("OK");
		btnOK.setActionCommand(CMD_OK);
		btnOK.addActionListener(this);
		btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand(CMD_CANCEL);
		btnCancel.addActionListener(this);
		btnCheck = new JButton("Check");
		btnCheck.setActionCommand(CMD_CHECK);
		btnCheck.addActionListener(this);

		panelButtons.add(btnOK);
		panelButtons.add(btnCancel);
		panelButtons.add(btnCheck);
		
		pack();
		setLocationRelativeTo(parent);
	}

	/**
	 * @return	Returns the last entered text.
	 */
	public String getInputData() {
		return textMatrix.getText();
	}
	
	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_OK)) {
			if (!doCheck()) return;
			setResultCode(Dialog.RESULT_OK);
			setVisible(false);
		} else if (cmd.equals(CMD_CANCEL)) {
			setResultCode(Dialog.RESULT_CANCEL);
			setVisible(false);
		} else if (cmd.equals(CMD_CHECK)) {
			if (!doCheck()) return;
		}
	}

	private void setStatus(String msg) {
		labelStatus.setText(msg);
	}
	
	/**
	 * @return Returns true, if check was ok.
	 * 
	 */
	private boolean doCheck() {
		String mtxT = textMatrix.getText();
		mtxT = mtxT.replace(';', '\n');
		String [] ra = mtxT.split("\n");
		Vector<String> rowsVec = new Vector<String>();
		int cols = -1;
		for (String s : ra) {
			s = s.trim();
			if (s.length()<1) continue;
			rowsVec.add(s);
		}
		if (rowsVec.size()<1) {
			setStatus("Number of rows must be at least 1!");
			return false;
		}
		for (String rowS : rowsVec) {
			int tmpcols = 0;
			String [] ca = rowS.split("\\s");
			for (String s : ca) {
				s = s.trim();
				if (s.length()<1) continue;
				tmpcols++;
			}
			if (cols<0) {
				cols = tmpcols;
			} else {
				if (cols!=tmpcols) {
					setStatus("Number of columns is not equal in all rows!");
					return false;
				}
			}
		}

		matrix = new DMatrix(rowsVec.size(), cols);
		for (int row = 0; row<rowsVec.size(); row++) {
			int tmpcol = 0;
			String [] ca = rowsVec.get(row).split("\\s");
			for (String s : ca) {
				s = s.trim();
				if (s.length()<1) continue;
				Double v;
				try {
					v = Double.valueOf(s);
				} catch (NumberFormatException e) {
					setStatus("could not parse number: " + s + "!");
					matrix = null;
					return false;
				}
				matrix.setElementAt(row, tmpcol, v);
				tmpcol++;
			}
		}

		setStatus("ok.");
		return true;
	}

	/**
	 * @return Returns the matrix.
	 */
	public DMatrix getMatrix() {
		return matrix;
	}


}
