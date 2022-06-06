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
package de.admadic.calculator.modules.masca.ui;

import java.awt.Container;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.modules.masca.MaSCaCfg;
import de.admadic.ui.util.Dialog;

/**
 * @author Rainer Schwarze
 *
 */
public class SettingsDialog extends Dialog implements ActionListener {
	/** */
	private static final long serialVersionUID = 1L;

	final static String CMD_OK = "cmd.ok";
	final static String CMD_CANCEL = "cmd.cancel";

	JPanel panelButtons;
	JButton btnOK;
	JButton btnCancel;
	JLabel labelPrecision;
	JSlider sliderPrecision;
	JSpinner spinnerPrecision;
	boolean updatePrecisionBusy = false;
	
	MaSCaCfg cfg;
	
	/**
	 * @param arg0
	 * @param cfg 
	 * @throws HeadlessException
	 */
	public SettingsDialog(Frame arg0, MaSCaCfg cfg) throws HeadlessException {
		super(arg0);
		this.cfg = cfg;
	}

	/**
	 * 
	 */
	public void initContents() {
		FormLayout fl = new FormLayout(
				"12px, p, 5px, p, 5px, p, 12px",
				"12px, p, 12px, p, 12px");
		CellConstraints cc = new CellConstraints();
		Container parent = this.getContentPane();
		this.setModal(true);
		this.setTitle("Settings");

		parent.setLayout(fl);
		parent.add(
				labelPrecision = new JLabel("Number Display Precision:"), 
				cc.xy(2, 2));
		parent.add(
				sliderPrecision = new JSlider(),
				cc.xy(4, 2));
		parent.add(
				spinnerPrecision = new JSpinner(),
				cc.xy(6, 2));
		parent.add(
				panelButtons = new JPanel(),
				cc.xywh(2, 4, 5, 1));

		btnOK = new JButton("OK");
		btnOK.setActionCommand(CMD_OK);
		btnOK.addActionListener(this);
		panelButtons.add(btnOK);
		btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand(CMD_CANCEL);
		btnCancel.addActionListener(this);
		panelButtons.add(btnCancel);

		registerEnterAction(btnOK);
		this.pack();
		this.setLocationRelativeTo(getOwner());

		sliderPrecision.setMinimum(0);
		sliderPrecision.setMaximum(15);
		sliderPrecision.setMajorTickSpacing(3);
		sliderPrecision.setMinorTickSpacing(1);
		sliderPrecision.setSnapToTicks(true);
		sliderPrecision.setPaintLabels(true);
		sliderPrecision.setPaintTicks(true);
		sliderPrecision.setValue(0);

		spinnerPrecision.setModel(new SpinnerNumberModel(0, 0, 15, 1));

		spinnerPrecision.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updatePrecisionValue(spinnerPrecision);
			}
		});
		sliderPrecision.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updatePrecisionValue(sliderPrecision);
			}
		});
	}

	/**
	 * @param source
	 */
	protected void updatePrecisionValue(JComponent source) {
		if (updatePrecisionBusy) return;
		updatePrecisionBusy = true;

		if (source==spinnerPrecision) {
			sliderPrecision.setValue(
					((Integer)(spinnerPrecision.getValue())).intValue());
		}
		if (source==sliderPrecision) {
			spinnerPrecision.setValue(
					Integer.valueOf(sliderPrecision.getValue()));
		}
		
		updatePrecisionBusy = false;
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_OK)) {
			storeSettings();
			setResultCode(RESULT_OK);
			setVisible(false);
		} else if (cmd.equals(CMD_CANCEL)) {
			setResultCode(RESULT_CANCEL);
			setVisible(false);
		}
	}

	/**
	 * 
	 */
	public void storeSettings() {
		int v = sliderPrecision.getValue();
		String fmt = "%g";
		if (v>0) {
			fmt = "%." + v + "g";
		}
		cfg.putDisplayFormat(fmt);
	}

	/**
	 * 
	 */
	public void loadSettings() {
		String fmt = cfg.getDisplayFormat();
		int v = 0;
		if (fmt==null || fmt.equals("%g")) {
			v = 0;
		} else {
			try {
				String tmp = fmt.substring(2, fmt.length()-1);
				v = Integer.parseInt(tmp);
			} catch (Exception e) {
				// in case of errors:
				v = 6;
			}
		}
		sliderPrecision.setValue(v);
		this.pack();
		this.setLocationRelativeTo(getOwner());
	}
}
