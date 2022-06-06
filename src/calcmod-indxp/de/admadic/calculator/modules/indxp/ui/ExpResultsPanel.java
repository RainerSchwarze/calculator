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
package de.admadic.calculator.modules.indxp.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumnModel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.modules.indxp.core.DataEvent;
import de.admadic.calculator.modules.indxp.core.DataEventDispatcher;
import de.admadic.calculator.modules.indxp.core.DataEventListener;
import de.admadic.calculator.modules.indxp.core.DataEventServer;
import de.admadic.calculator.modules.indxp.core.DataItemStatus;
import de.admadic.calculator.modules.indxp.core.ExpResults;
import de.admadic.calculator.modules.indxp.core.ExpResultsTableModel;
import de.admadic.calculator.modules.indxp.core.Run;
import de.admadic.ui.util.ColorGradient;
import de.admadic.ui.util.VetoableToggleButtonModel;

/**
 * @author Rainer Schwarze
 *
 */
public class ExpResultsPanel extends DataPanel 
implements ActionListener, TableColumnModelListener, DataEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTable tableExpResults;
	JScrollPane scrollExpResults;
	ExpResultsTableModel tableModelExpResults;

	FloatingPointFormatter fpf;

	JPanel panelButtons;
	JButton btnCreate;
	JButton btnRemove;
	JButton btnReset;
	JToggleButton btnLock;
	JButton btnCalculate;
	JToggleButton btnColorize;

	final static String CMD_CREATE = "expres.create";
	final static String CMD_RESET = "expres.reset";
	final static String CMD_REMOVE = "expres.remove";
	final static String CMD_LOCK = "expres.lock";
	final static String CMD_CALCULATE = "expres.calculate";
	final static String CMD_COLORIZE = "expres.colorize";

	ExpResultsCellRenderer cellRendererStat;
	
	ArrayList<ExpResults> expResultsLink;
	ArrayList<Run> runsLink;
	
	/**
	 * @param fpf 
	 * 
	 */
	public ExpResultsPanel(FloatingPointFormatter fpf) {
		super();
		setFloatingPointFormatter(fpf);
		initContents(false);
	}

	/**
	 * @param noframe 
	 * 
	 */
	public ExpResultsPanel(boolean noframe) {
		super();
		initContents(noframe);
	}

	/**
	 * helper function.
	 * @param noframe
	 */
	private void initContents(boolean noframe) {
        FormLayout fl;
        if (noframe) {
	        fl = new FormLayout(
	        		"0px, p:grow, 5px, p, 0px",
	        		"0px, p:grow, 0px");
        } else {
	        fl = new FormLayout(
	        		"12px, p:grow, 5px, p, 12px",
	        		"12px, p:grow, 12px");
        }
        this.setLayout(fl);
        CellConstraints cc = new CellConstraints();
		
        tableModelExpResults = new ExpResultsTableModel();
        
        tableExpResults = new JTable();
        tableExpResults.setModel(tableModelExpResults);
        tableExpResults.getColumnModel().addColumnModelListener(this);
        scrollExpResults = new JScrollPane();
        this.add(
        		scrollExpResults, 
        		cc.xy(2, 2, CellConstraints.DEFAULT, CellConstraints.FILL));
        scrollExpResults.setViewportView(tableExpResults);
        scrollExpResults.setHorizontalScrollBarPolicy(
        		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollExpResults.setVerticalScrollBarPolicy(
        		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tableExpResults.setPreferredScrollableViewportSize(
        		new Dimension(300, 200));
        tableExpResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        cellRendererStat = new ExpResultsCellRenderer(
        		ColorGradient.JET(), 
        		GradientCellRenderer.BORDER_LEFT,
        		GradientCellRenderer.RANGE_VERT);
        cellRendererStat.setFloatingPointFormatter(fpf);
        tableExpResults.setDefaultRenderer(Double.class, cellRendererStat);

        panelButtons = new JPanel();
        this.add(
        		panelButtons, 
        		cc.xy(4, 2, CellConstraints.DEFAULT, CellConstraints.FILL));

        {
        	FormLayout flb = new FormLayout(
        			"p",
        			"p, 5px, p, 5px, p, 5px, p, 5px, p, 5px, p");
        	panelButtons.setLayout(flb);
        	CellConstraints ccb = new CellConstraints();

        	btnCreate = new JButton("Create");
        	btnCreate.setActionCommand(CMD_CREATE);
        	btnCreate.addActionListener(this);
        	btnRemove = new JButton("Remove");
        	btnRemove.setActionCommand(CMD_REMOVE);
        	btnRemove.addActionListener(this);
        	btnReset = new JButton("Reset...");
        	btnReset.setActionCommand(CMD_RESET);
        	btnReset.addActionListener(this);
        	btnLock = new JToggleButton("Lock");
        	btnLock.setActionCommand(CMD_LOCK);
        	btnLock.addActionListener(this);
        	VetoableToggleButtonModel vtbm = new VetoableToggleButtonModel();
        	vtbm.addVetoableChangeListener(new VetoableChangeListener() {
				public void vetoableChange(PropertyChangeEvent evt) 
				throws PropertyVetoException {
					String name = evt.getPropertyName();
					if (!name.equals(VetoableToggleButtonModel.SELECTED_PROPERTY)) return;
					if (evt.getSource()!=btnLock.getModel()) return;
					Boolean oldV = (Boolean)evt.getOldValue();
					Boolean newV = (Boolean)evt.getNewValue();
					if (!allowLockOp(oldV.booleanValue(), newV.booleanValue())) {
						throw new PropertyVetoException(name, evt);
					}
				}
        	});
        	btnLock.setModel(vtbm);
        	btnLock.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange()==ItemEvent.SELECTED) {
						doLock(true);
					} else {
						doLock(false);
					}
				}
        	});
        	btnCalculate = new JButton("Calculate");
        	btnCalculate.setActionCommand(CMD_CALCULATE);
        	btnCalculate.addActionListener(this);
        	btnColorize = new JToggleButton("Colorize");
        	btnColorize.setActionCommand(CMD_COLORIZE);
        	btnColorize.addActionListener(this);
        	btnColorize.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange()==ItemEvent.SELECTED) {
						doColorize(true);
					} else {
						doColorize(false);
					}
				}
        	});
        	
        	panelButtons.add(btnCreate, ccb.xy(1, 1));
        	panelButtons.add(btnRemove, ccb.xy(1, 3));
        	panelButtons.add(btnReset, ccb.xy(1, 5));
        	panelButtons.add(btnLock, ccb.xy(1, 7));
        	panelButtons.add(btnCalculate, ccb.xy(1, 9));
        	panelButtons.add(btnColorize, ccb.xy(1, 11));
        }
	}

	/**
	 * @param expResults 
	 * @param runs
	 */
	public void linkData(
			ArrayList<ExpResults> expResults, 
			ArrayList<Run> runs) {
		this.expResultsLink = expResults;
		this.runsLink = runs;
		tableModelExpResults.setData(expResultsLink);
	}

	/**
	 * Registeres the data event dispatcher. Pass null to remove it.
	 * @param v
	 */
	@Override
	public void setDataEventDispatcher(DataEventDispatcher v) {
		super.setDataEventDispatcher(v);
		tableModelExpResults.setDataEventDispatcher(v);
	}

	/**
	 * @param dataEventServer The dataEventServer to set.
	 */
	@Override
	public void setDataEventServer(DataEventServer dataEventServer) {
		if (this.dataEventServer!=null) {
			this.dataEventServer.removeDataListener(tableModelExpResults);
			this.dataEventServer.removeDataListener(this);
		}
		super.setDataEventServer(dataEventServer);
		if (this.dataEventServer!=null) {
			this.dataEventServer.addDataListener(tableModelExpResults);
			this.dataEventServer.addDataListener(this);
		}
	}


	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_CREATE)) {
			doCreate();
		} else if (cmd.equals(CMD_RESET)) {
			doReset();
		} else if (cmd.equals(CMD_REMOVE)) {
			doRemove();
		} else if (cmd.equals(CMD_LOCK)) {
			// doLock();
			// done in the ItemListener
		} else if (cmd.equals(CMD_CALCULATE)) {
			doCalculate();
		}
	}

	/**
	 * Perform lock or unlock on the tablemodel / data.
	 * @param b
	 */
	protected void doLock(boolean b) {
		tableModelExpResults.setLocked(b);
		DataPanel.enableComponents(
				panelButtons, !b, new Component[]{btnLock, btnColorize});
		if (getDataItemStatusServer()!=null) {
			DataItemStatus dis;
			dis = getDataItemStatusServer().getDataItemStatus(
					DataItemStatus.DI_EXPRESULTS);
			if (b) {
				dis.lock(DataItemStatus.LOCKED_ALL);
			} else {
				dis.unlock(DataItemStatus.LOCKED_ALL);
			}
		}
	}

	protected boolean allowLockOp(boolean oldLock, boolean newLock) {
		if (getDataItemStatusServer()!=null) {
			DataItemStatus dis;
			dis = getDataItemStatusServer().getDataItemStatus(
					DataItemStatus.DI_EXPRESULTS);
			if (dis.isLocked() && oldLock && !newLock) {
				int res = JOptionPane.showConfirmDialog(
						null,
						"Are you sure, that you want to unlock the data?\n"+
						"If you unlock, the data may be changed which may\n"+
						"influence subsequent calculations.\n"+
						"The results of these calculations may then be invalid.",
						"Confirm data unlock",
						JOptionPane.YES_NO_OPTION);
				if (res==JOptionPane.NO_OPTION) return false;
			}
		}
		return true;
	}

	protected void doColorize(boolean b) {
		cellRendererStat.setOn(b);
		updateRanges();
	}
	
	private void doCreate() {
		if (expResultsLink.size()>0) {
			int res = JOptionPane.showConfirmDialog(
					null,
					"There are already existing replicates which"+
					" would be removed.\n"+
					"Do you want to create new replicates?",
					"Confirm creation of new replicates",
					JOptionPane.YES_NO_OPTION);
			if (res!=JOptionPane.YES_OPTION) return;
		}
		expResultsLink.clear();
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(this, DataEvent.DATA_IS_DIRTY);
		}

		int expCount = -1;
		String defValue = "1"; 

		while (true) {
			String result = JOptionPane.showInputDialog(
					null, 
					"Please enter the number of replicates to support (1..100)",
					defValue);
			if (result==null) return; // cancel
			expCount = -1;
			try {
				expCount = Integer.parseInt(result);
			} catch (NumberFormatException e) {
				defValue = result;
				JOptionPane.showMessageDialog(
						null,
						"Number parsing error.\n"+
						"Please correct the input.",
						"Invalid number format",
						JOptionPane.ERROR_MESSAGE);
				continue;
			}
			if (expCount<1) {
				JOptionPane.showMessageDialog(
						null,
						"Number out of range.\n"+
						"The number of runs must 1 or greater.",
						"Invalid number range",
						JOptionPane.ERROR_MESSAGE);
				defValue = result;
				continue;
			}
			if (expCount>100) {
				JOptionPane.showMessageDialog(
						null,
						"Number out of range.\n"+
						"The number of runs must not be larger than 100.",
						"Invalid number range",
						JOptionPane.ERROR_MESSAGE);
				defValue = result;
				continue;
			}
			// here we are ok.
			break;
		}
		// expCount is ok now.

		for (int i = 0; i < runsLink.size(); i++) {
			ExpResults er = new ExpResults(expCount);
			expResultsLink.add(er);
		}
		// already dirty from beginning

		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.EXPRESULTS_STRUCT_CHANGED);
		}
	}

	private void doReset() {
		int res = JOptionPane.showConfirmDialog(
				null,
				"Please confirm that the all result sets shall be reset.\n"+
				"All entered values will be lost.\n"+
				"Do you want to reset them?",
				"Confirm reset of all result sets",
				JOptionPane.YES_NO_OPTION);
		if (res!=JOptionPane.YES_OPTION) return;

		for (ExpResults er : expResultsLink) {
			er.resetData();
		}
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, 
					DataEvent.EXPRESULTS_DATA_CHANGED |
					DataEvent.DATA_IS_DIRTY);
		}
	}

	private void doRemove() {
		int res = JOptionPane.showConfirmDialog(
				null,
				"Please confirm that the all result sets shall be deleted.\n"+
				"Do you want to delete them?",
				"Confirm deletion of all result sets",
				JOptionPane.YES_NO_OPTION);
		if (res!=JOptionPane.YES_OPTION) return;

		expResultsLink.clear();
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, 
					DataEvent.EXPRESULTS_STRUCT_CHANGED |
					DataEvent.DATA_IS_DIRTY);
		}
	}

	private void doCalculate() {
		for (ExpResults er : expResultsLink) {
			er.calculate();
		}
		updateRanges();
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, 
					DataEvent.EXPRESULTS_DATA_CHANGED |
					DataEvent.DATA_IS_DIRTY);
		}
	}

	private Double[] getRangeYAvg() {
		Double [] rg = new Double[2];
		for (ExpResults er : expResultsLink) {
			Double v = er.getYAvg();
			if (rg[0]==null || rg[0].doubleValue()>v.doubleValue()) {
				rg[0] = v;
			}
			if (rg[1]==null || rg[1].doubleValue()<v.doubleValue()) {
				rg[1] = v;
			}
		}
		// System.out.println("range yavg  = " + rg[0] + " .. " + rg[1]);
		return rg;
	}

	private Double[] getRangeSigma() {
		Double [] rg = new Double[2];
		for (ExpResults er : expResultsLink) {
			Double v = er.getSigma();
			if (rg[0]==null || rg[0].doubleValue()>v.doubleValue()) {
				rg[0] = v;
			}
			if (rg[1]==null || rg[1].doubleValue()<v.doubleValue()) {
				rg[1] = v;
			}
		}
		// System.out.println("range sigma = " + rg[0] + " .. " + rg[1]);
		return rg;
	}

	protected void updateRanges() {
		Double [] rg;
		ExpResults er;
		if (expResultsLink.size()<1) return;
		er = expResultsLink.get(0);
		cellRendererStat.clearRanges();
		rg = getRangeYAvg();
		cellRendererStat.addCellRange(
				Integer.valueOf(er.getReplicateCount()), rg[0], rg[1]);
		rg = getRangeSigma();
		cellRendererStat.addCellRange(
				Integer.valueOf(er.getReplicateCount()+1), rg[0], rg[1]);
		tableModelExpResults.fireTableDataChanged();
	}

	/**
	 * @param e
	 * @see javax.swing.event.TableColumnModelListener#columnAdded(javax.swing.event.TableColumnModelEvent)
	 */
	public void columnAdded(TableColumnModelEvent e) {
		int col = e.getToIndex();
		TableColumnModel tcm = tableExpResults.getColumnModel();
		tcm.getColumn(col).setPreferredWidth(80);
	}

	/**
	 * @param e
	 * @see javax.swing.event.TableColumnModelListener#columnRemoved(javax.swing.event.TableColumnModelEvent)
	 */
	public void columnRemoved(TableColumnModelEvent e) {
		// nothing
	}

	/**
	 * @param e
	 * @see javax.swing.event.TableColumnModelListener#columnMoved(javax.swing.event.TableColumnModelEvent)
	 */
	public void columnMoved(TableColumnModelEvent e) {
		// nothing
	}

	/**
	 * @param e
	 * @see javax.swing.event.TableColumnModelListener#columnMarginChanged(javax.swing.event.ChangeEvent)
	 */
	public void columnMarginChanged(ChangeEvent e) {
		// nothing
	}

	/**
	 * @param e
	 * @see javax.swing.event.TableColumnModelListener#columnSelectionChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void columnSelectionChanged(ListSelectionEvent e) {
		// nothing
	}

	/**
	 * @param e
	 * @see de.admadic.calculator.modules.indxp.core.DataEventListener#dataEventSignalled(de.admadic.calculator.modules.indxp.core.DataEvent)
	 */
	public void dataEventSignalled(DataEvent e) {
		if (e.getSource()==this) return;
		if ((e.getMask() & DataEvent.EXPRESULTS_STRUCT_CHANGED)!=0) {
			if (getDataItemStatusServer()!=null) {
				DataItemStatus dis;
				dis = getDataItemStatusServer().getDataItemStatus(
						DataItemStatus.DI_EXPRESULTS);
				btnLock.setSelected(dis.isLocked());
			}
		}
	}

	/**
	 * @param fpf
	 */
	public void setFloatingPointFormatter(FloatingPointFormatter fpf) {
		this.fpf = fpf;
	}
}
