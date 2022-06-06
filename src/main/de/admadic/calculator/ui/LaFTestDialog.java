/**
 *
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
 */
package de.admadic.calculator.ui;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import com.jgoodies.forms.layout.FormLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JScrollBar;
import javax.swing.JProgressBar;
import javax.swing.ListModel;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import com.jgoodies.forms.layout.CellConstraints;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JFrame;

/**
 * 
 * @author Rainer Schwarze
 *
 */
public class LaFTestDialog extends javax.swing.JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton compButton;
	JTextPane compTextPane;
	JProgressBar compProgressV;
	JSeparator compSepV;
	JSeparator compSep;
	JTree compTree;
	JTable compTable;
	JSlider compSliderH;
	JSlider compSliderV;
	JScrollBar compScrollV;
	JScrollBar compScrollH;
	JProgressBar compProgress;
	JList compList;
	ButtonGroup compBtnGroup;
	JTextArea compTextArea;
	JEditorPane compEditorPane;
	JTextField compTextField;
	JLabel compLabel;
	JSpinner compSpinner;
	JComboBox compCombo;
	JToggleButton compToggle;
	JCheckBox compCheck1;
	JRadioButton compRadio3;
	JRadioButton compRadio2;
	JRadioButton compRadio1;

	Vector<JComponent> comps;


	/**
	 * @param frame
	 */
	public LaFTestDialog(JFrame frame) {
		super(frame);
		comps = new Vector<JComponent>();
		initGUI();
	}
	
	private void initGUI() {
		try {
			//setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			FormLayout thisLayout = new FormLayout(
					"max(p;5px), max(p;5px), max(p;5px), "+
					"max(p;5px), max(p;5px), max(p;5px), "+
					"max(p;5px), max(p;5px), max(p;5px), "+
					"max(p;5px), max(p;5px), "+
					"max(p;5px), max(p;5px), max(p;5px), max(p;5px)", 
					"max(p;5px), max(p;5px), max(p;5px), "+
					"max(p;5px), max(p;5px), max(p;5px), "+
					"max(p;5px), max(p;5px), max(p;5px), "+
					"max(p;5px), max(p;5px), max(p;5px), "+
					"max(p;5px), max(p;5px), max(p;5px), "+
					"max(p;5px), max(p;5px), max(p;5px), max(p;5px), "+
					"max(p;5px), max(p;5px), max(p;5px), max(p;5px)");
			this.getContentPane().setLayout(thisLayout);
			compButton = new JButton();
			this.getContentPane().add(
				compButton,
				new CellConstraints("2, 2, 1, 1, default, default"));
			compButton.setText("Button");

			compBtnGroup = new ButtonGroup();
			compRadio1 = new JRadioButton();
			this.getContentPane().add(
				compRadio1,
				new CellConstraints("2, 6, 1, 1, default, default"));
			compRadio1.setText("Radio 1");
			compRadio2 = new JRadioButton();
			this.getContentPane().add(
				compRadio2,
				new CellConstraints("2, 8, 1, 1, default, default"));
			compRadio2.setText("Radio 2");
			compRadio3 = new JRadioButton();
			this.getContentPane().add(
				compRadio3,
				new CellConstraints("2, 10, 1, 1, default, default"));
			compRadio3.setText("Radio 3");
			compBtnGroup.add(compRadio1);
			compBtnGroup.add(compRadio2);
			compBtnGroup.add(compRadio3);

			compCheck1 = new JCheckBox();
			this.getContentPane().add(
				compCheck1,
				new CellConstraints("2, 12, 1, 1, default, default"));
			compCheck1.setText("Checkbox 1");

			compToggle = new JToggleButton();
			this.getContentPane().add(
				compToggle,
				new CellConstraints("2, 4, 1, 1, default, default"));
			compToggle.setText("Toggle");

			ComboBoxModel compComboModel = new DefaultComboBoxModel(
					new String[] { 
							"Item One", "Item Two", 
							"Item Three", "Item Four", 
							"Item Five" });
			compCombo = new JComboBox();
			this.getContentPane().add(
				compCombo,
				new CellConstraints("4, 2, 1, 1, default, default"));
			compCombo.setModel(compComboModel);

			SpinnerListModel compSpinnerModel = new SpinnerListModel(
				new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri",
						"Sat" });
			compSpinner = new JSpinner();
			this.getContentPane().add(
				compSpinner,
				new CellConstraints("4, 4, 1, 1, default, default"));
			compSpinner.setModel(compSpinnerModel);

			compLabel = new JLabel();
			this.getContentPane().add(
				compLabel,
				new CellConstraints("4, 6, 1, 1, default, default"));
			compLabel.setText("Label");

			compTextField = new JTextField();
			this.getContentPane().add(
				compTextField,
				new CellConstraints("4, 8, 1, 1, default, default"));
			compTextField.setText("Textfield");

			compEditorPane = new JEditorPane();
			this.getContentPane().add(
				compEditorPane,
				new CellConstraints("4, 10, 1, 3, default, default"));
			compEditorPane.setText(
					"Editor Pane\n"+
					"This is a complex component, which has\n"+
					"multiple lines to display and optionally \n"+
					"a few scrollers around it."
					);

			compTextArea = new JTextArea();
			this.getContentPane().add(
				compTextArea,
				new CellConstraints("6, 2, 1, 3, default, default"));
			compTextArea.setText(
					"Textarea\n"+
					"This is a complex component, which has\n"+
					"multiple lines to display and optionally \n"+
					"a few scrollers around it."					);

			compTextPane = new JTextPane();
			this.getContentPane().add(
				compTextPane,
				new CellConstraints("6, 6, 1, 3, default, default"));
			compTextPane.setText(
					"Textpane\n"+
					"This is a complex component, which has\n"+
					"multiple lines to display and optionally \n"+
					"a few scrollers around it."					);
			ListModel compListModel = new DefaultComboBoxModel(
				new String[] { 
						"Item One", "Item Two", 
						"Item Three", "Item Four", 
						"Item Five" });
			compList = new JList();
			this.getContentPane().add(
				compList,
				new CellConstraints("6, 10, 1, 3, default, default"));
			compList.setModel(compListModel);

			compProgress = new JProgressBar();
			this.getContentPane().add(
				compProgress,
				new CellConstraints("2, 14, 3, 1, default, default"));

			compScrollH = new JScrollBar();
			this.getContentPane().add(
				compScrollH,
				new CellConstraints("2, 16, 3, 1, default, default"));
			compScrollH.setOrientation(SwingConstants.HORIZONTAL);

			compProgressV = new JProgressBar();
			this.getContentPane().add(
				compProgressV,
				new CellConstraints("8, 2, 1, 11, default, default"));
			compProgressV.setOrientation(SwingConstants.VERTICAL);

			compScrollV = new JScrollBar();
			this.getContentPane().add(
				compScrollV,
				new CellConstraints("10, 2, 1, 11, default, default"));

			compSliderV = new JSlider();
			this.getContentPane().add(
				compSliderV,
				new CellConstraints("12, 2, 1, 11, default, default"));
			compSliderV.setOrientation(1);
			compSliderV.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					//e.getSource();
					// we know its from V
					int v = compSliderV.getValue();
					compProgressV.setValue(v);
					compScrollV.setValue(v);
				}
			});

			compSliderH = new JSlider();
			this.getContentPane().add(
				compSliderH,
				new CellConstraints("2, 18, 3, 1, default, default"));
			compSliderH.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					//e.getSource();
					// we know its from H
					int v = compSliderH.getValue();
					compProgress.setValue(v);
					compScrollH.setValue(v);
				}
			});

			compSliderV.setMaximum(100);
			compSliderV.setMinimum(0);
			compSliderV.setPaintLabels(true);
			compSliderV.setPaintTicks(true);
			compSliderV.setMajorTickSpacing(20);
			compSliderV.setMinorTickSpacing(5);
			compSliderH.setMaximum(100);
			compSliderH.setMinimum(0);
			compSliderH.setPaintLabels(true);
			compSliderH.setPaintTicks(true);
			compSliderH.setMajorTickSpacing(20);
			compSliderH.setMinorTickSpacing(5);
			compProgressV.setMaximum(100);
			compProgressV.setMinimum(0);
			compProgress.setMaximum(100);
			compProgress.setMinimum(0);
			compScrollH.setMaximum(100);
			compScrollH.setMinimum(0);
			compScrollV.setMaximum(100);
			compScrollV.setMinimum(0);

			TableModel compTableModel = new DefaultTableModel(
				new String[][] { { "One", "Two" }, { "Three", "Four" } },
				new String[] { "Column 1", "Column 2" });
			compTable = new JTable();
			JScrollPane scrollpane = new JScrollPane(compTable);
			this.getContentPane().add(
				scrollpane, //compTable,
				new CellConstraints("14, 2, 1, 5, default, default"));
			compTable.setModel(compTableModel);

			compTree = new JTree();
			this.getContentPane().add(
				compTree,
				new CellConstraints(
						//"14, 8, 1, 5, default, default"
						"6, 14, 1, 5, default, default"
						));

			compSep = new JSeparator();
			this.getContentPane().add(
				compSep,
				new CellConstraints("8, 14, 5, 1, default, default"));

//			compSepV = new JSeparator();
//			this.getContentPane().add(
//				compSepV,
//				new CellConstraints("6, 14, 1, 5, default, default"));
//			compSepV.setOrientation(SwingConstants.VERTICAL);

			int [] pl = {
					SwingConstants.TOP,
					SwingConstants.LEFT,
					SwingConstants.RIGHT,
					SwingConstants.BOTTOM
			};
			JTabbedPane [] tp = new JTabbedPane[4];
			JPanel [] pan = new JPanel[4];
			JPanel [] pan2 = new JPanel[4];
			for (int i = 0; i < pl.length; i++) {
				tp[i] = new JTabbedPane();
				tp[i].setTabPlacement(pl[i]);
				pan[i] = new JPanel();
				pan[i].setLayout(new BorderLayout());
				pan2[i] = new JPanel();
				pan2[i].setLayout(new BorderLayout());
			}
			this.getContentPane().add(
				tp[0],
				new CellConstraints(
						//"14, 8, 1, 5, default, default"
						"14, 8, 1, 11, default, default"
						//"8, 16, 3, 3, default, default"
						));
			tp[0].addTab("A Tab", pan[0]);
			tp[0].addTab("A Tab", pan2[0]);
			pan[0].add(tp[1], BorderLayout.CENTER);
			tp[1].addTab("A Tab", pan[1]);
			tp[1].addTab("A Tab", pan2[1]);
			pan[1].add(tp[2], BorderLayout.CENTER);
			tp[2].addTab("A Tab", pan[2]);
			tp[2].addTab("A Tab", pan2[2]);
			pan[2].add(tp[3], BorderLayout.CENTER);
			tp[3].addTab("A Tab", pan[3]);
			tp[3].addTab("A Tab", pan2[3]);
			pan[3].add(new JButton("Test"), BorderLayout.CENTER);

			JToggleButton tglbtn = new JToggleButton("ena/dis");
			this.getContentPane().add(
					tglbtn,
					new CellConstraints("8, 16, 5, 1, default, default"));
			tglbtn.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					boolean state = e.getStateChange()==ItemEvent.SELECTED;
					for (JComponent c : comps) {
						if (c==null) continue;
						c.setEnabled(state);
					}
				}
			});

			CalcButton cb;
			cb = new CalcButton();
			cb.setText("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
			cb.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			this.getContentPane().add(
					cb,
					new CellConstraints("2, 20, 5, 1, default, default"));
			cb = new CalcButton();
			cb.setText("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
			cb.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			this.getContentPane().add(
					cb,
					new CellConstraints("10, 20, 5, 1, default, default"));
			

			//this.setSize(531, 423);
			this.pack();
			this.setLocationRelativeTo(this.getParent());

			comps.add(compButton);
			comps.add(compTextPane);
			comps.add(compProgressV);
			comps.add(compSepV);
			comps.add(compSep);
			comps.add(compTree);
			comps.add(compTable);
			comps.add(compSliderH);
			comps.add(compSliderV);
			comps.add(compScrollV);
			comps.add(compScrollH);
			comps.add(compProgress);
			comps.add(compList);
			//comps.add(compBtnGroup;
			comps.add(compTextArea);
			comps.add(compEditorPane);
			comps.add(compTextField);
			comps.add(compLabel);
			comps.add(compSpinner);
			comps.add(compCombo);
			comps.add(compToggle);
			comps.add(compCheck1);
			comps.add(compRadio3);
			comps.add(compRadio2);
			comps.add(compRadio1);
		} catch (Exception e) {
			// since that is only a Testing Code, we can keep the exception 
			// in here:
			e.printStackTrace();
		}
	}
}
