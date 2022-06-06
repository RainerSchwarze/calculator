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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Vector;

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
import de.admadic.calculator.modules.indxp.core.FactorInteraction;
import de.admadic.calculator.modules.indxp.core.Run;
import de.admadic.calculator.modules.indxp.core.RunsBuilder;
import de.admadic.calculator.modules.indxp.core.RunsTableModel;
import de.admadic.ui.util.Dialog;
import de.admadic.ui.util.VetoableToggleButtonModel;

/**
 * @author Rainer Schwarze
 *
 */
public class RunsPanel extends DataPanel 
implements ActionListener, DataEventListener, TableColumnModelListener {
	JTable tableRuns;
	JScrollPane scrollRuns;
	RunsTableModel tableModelRuns;

	JPanel panelButtons;
	JButton btnCreate;
	JButton btnCreateRg;
	JButton btnRemove;
	JToggleButton btnLock;

	final static String CMD_CREATE = "runs.create";
	final static String CMD_CREATE_RG = "runs.createrg";
	final static String CMD_REMOVE = "runs.remove";
	final static String CMD_LOCK = "runs.lock";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	RunsBuilder runsBuilder;
	ArrayList<Run> runsLink;
	ArrayList<FactorInteraction> factorInteractionsLink;

	/**
	 * 
	 */
	public RunsPanel() {
		super();
		initContents(false);
	}

	/**
	 * @param noframe 
	 * 
	 */
	public RunsPanel(boolean noframe) {
		super();
		initContents(noframe);
	}

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
		
        tableModelRuns = new RunsTableModel();
        
        tableRuns = new JTable();
        tableRuns.setModel(tableModelRuns);
        tableRuns.getColumnModel().addColumnModelListener(this);
        scrollRuns = new JScrollPane();
        this.add(
        		scrollRuns, 
        		cc.xy(2, 2, CellConstraints.DEFAULT, CellConstraints.FILL));
        scrollRuns.setViewportView(tableRuns);
        scrollRuns.setHorizontalScrollBarPolicy(
        		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollRuns.setVerticalScrollBarPolicy(
        		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tableRuns.setPreferredScrollableViewportSize(
        		new Dimension(300, 200));
        tableRuns.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        panelButtons = new JPanel();
        this.add(
        		panelButtons, 
        		cc.xy(4, 2, CellConstraints.DEFAULT, CellConstraints.FILL));

        {
        	FormLayout flb = new FormLayout(
        			"p",
        			"p, 5px, p, 5px, p, 5px, p");
        	panelButtons.setLayout(flb);
        	CellConstraints ccb = new CellConstraints();

        	btnCreate = new JButton("Create");
        	btnCreate.setActionCommand(CMD_CREATE);
        	btnCreate.addActionListener(this);
        	btnCreateRg = new JButton("Create...");
        	btnCreateRg.setActionCommand(CMD_CREATE_RG);
        	btnCreateRg.addActionListener(this);
        	btnRemove = new JButton("Remove");
        	btnRemove.setActionCommand(CMD_REMOVE);
        	btnRemove.addActionListener(this);
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
        	btnLock.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange()==ItemEvent.SELECTED) {
						doLock(true);
					} else {
						doLock(false);
					}
				}
        	});
        	
        	panelButtons.add(btnCreate, ccb.xy(1, 1));
        	panelButtons.add(btnCreateRg, ccb.xy(1, 3));
        	panelButtons.add(btnRemove, ccb.xy(1, 5));
        	panelButtons.add(btnLock, ccb.xy(1, 7));
        }
	}

	/**
	 * Perform lock or unlock on the tablemodel / data.
	 * @param b
	 */
	protected void doLock(boolean b) {
		tableModelRuns.setLocked(b);
		DataPanel.enableComponents(panelButtons, !b, btnLock);
		if (getDataItemStatusServer()!=null) {
			DataItemStatus dis;
			dis = getDataItemStatusServer().getDataItemStatus(
					DataItemStatus.DI_RUNS);
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
					DataItemStatus.DI_RUNS);
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

	/**
	 * @param runs
	 * @param factorInteractions
	 */
	public void linkData(
			ArrayList<Run> runs, 
			ArrayList<FactorInteraction> factorInteractions) {
		this.runsLink = runs;
		this.factorInteractionsLink = factorInteractions;
		tableModelRuns.setData(runs, factorInteractions);
	}

	/**
	 * Registeres the data event dispatcher. Pass null to remove it.
	 * @param v
	 */
	@Override
	public void setDataEventDispatcher(DataEventDispatcher v) {
		super.setDataEventDispatcher(v);
		tableModelRuns.setDataEventDispatcher(v);
	}

	/**
	 * @param dataEventServer The dataEventServer to set.
	 */
	@Override
	public void setDataEventServer(DataEventServer dataEventServer) {
		if (this.dataEventServer!=null) {
			this.dataEventServer.removeDataListener(tableModelRuns);
			this.dataEventServer.removeDataListener(this);
		}
		super.setDataEventServer(dataEventServer);
		if (this.dataEventServer!=null) {
			this.dataEventServer.addDataListener(tableModelRuns);
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
		} else if (cmd.equals(CMD_CREATE_RG)) {
			doCreateRg();
		} else if (cmd.equals(CMD_REMOVE)) {
			doRemove();
		} else if (cmd.equals(CMD_LOCK)) {
			// doLock();
			// done in the ItemListener
		}
	}

	private void doCreate() {
		int siFaIn = countSingleFactorInteractions();
		if (siFaIn>10) {
			JOptionPane.showMessageDialog(
					null,
					"Too many factors.\n"+
					"There are more than 10 factors to be used. A full\n"+
					"factorial set of runs is not supported for this amount of data.\n"+
					"Try to create a limited/ranged run set or reduce the factors.",
					"Too many factors",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (runsLink.size()>0) {
			int res = JOptionPane.showConfirmDialog(
					null,
					"There are already existing runs which would be removed.\n"+
					"Do you want to create new runs?",
					"Confirm creation of new runs",
					JOptionPane.YES_NO_OPTION);
			if (res!=JOptionPane.YES_OPTION) return;
		}
		runsLink.clear();
		runsBuilder.createFullFactorialRuns();
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.RUNS_STRUCT_CHANGED);
		}
	}

	/**
	 * 
	 * @return	Returns the no. of factor interactions which have only
	 * one factor. 
	 */
	private int countSingleFactorInteractions() {
		int count = 0;
		for (FactorInteraction fi : factorInteractionsLink) {
			if (fi.getFactors().size()==1)
				count++;
		}
		return count;
	}
	
	private void doCreateRg() {
		int runCount = -1;

		NumberInputDialog nidlg;
		NumberInputSet [] inputSet = new NumberInputSet[]{
				new NumberInputSet(
						"Number of runs:", "(1..1024)",
						8, 1, 1024, 1)
		};
		nidlg = new NumberInputDialog(
				"Please enter the number of runs to generate.",
				inputSet);
		nidlg.setVisible(true);
		int rc = nidlg.getResultCode();
		if (rc!=Dialog.RESULT_OK) return;

		runCount = inputSet[0].getValue().intValue();
		
		// runCount is ok now.
		runsBuilder.createRangedRuns(runCount);
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.RUNS_DATA_CHANGED);
		}
	}

	private void doRemove() {
		int [] sels = tableRuns.getSelectedRows();
		if (sels.length<1) {
			JOptionPane.showMessageDialog(
					null,
					"No runs selected.\n"+
					"You need to select runs which are to be removed.",
					"No runs selected",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		int res = JOptionPane.showConfirmDialog(
				null,
				"Please confirm that the " + sels.length + 
				" selected runs shall be deleted.\n"+
				"Do you want to delete these runs?",
				"Confirm deletion of selected runs",
				JOptionPane.YES_NO_OPTION);
		if (res!=JOptionPane.YES_OPTION) return;

		Vector<Run> removeV = new Vector<Run>();
		for (int seli : sels) {
			removeV.add(runsLink.get(seli));
		}
		runsLink.removeAll(removeV);
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(this, DataEvent.RUNS_DATA_CHANGED);
		}
	}

	/**
	 * @param runsBuilder The runsBuilder to set.
	 */
	public void setRunsBuilder(RunsBuilder runsBuilder) {
		this.runsBuilder = runsBuilder;
	}


//	protected void updateCellRenderers() {
//		TableColumnModel tcm = tableRuns.getColumnModel();
//		int fiCount = factorInteractionsLink.size();
//		for (int i=0; i<fiCount; i++) {
//			tcm.getColumn(i+1).setCellRenderer(new LevelCellRenderer(true));
//		}
//	}
	
	/**
	 * @param e
	 * @see de.admadic.calculator.modules.indxp.core.DataEventListener#dataEventSignalled(de.admadic.calculator.modules.indxp.core.DataEvent)
	 */
	public void dataEventSignalled(DataEvent e) {
		if (e.getSource()==this) return;
		if ((e.getMask() & DataEvent.FACTORINTERACTIONS_DATA_CHANGED)!=0) {
			// nothing
		}
		if ((e.getMask() & DataEvent.RUNS_STRUCT_CHANGED)!=0) {
			if (getDataItemStatusServer()!=null) {
				DataItemStatus dis;
				dis = getDataItemStatusServer().getDataItemStatus(
						DataItemStatus.DI_RUNS);
				btnLock.setSelected(dis.isLocked());
			}
		}
	}

	/**
	 * @param e
	 * @see javax.swing.event.TableColumnModelListener#columnAdded(javax.swing.event.TableColumnModelEvent)
	 */
	public void columnAdded(TableColumnModelEvent e) {
		int col = e.getToIndex();
		TableColumnModel tcm = tableRuns.getColumnModel();
		if (col==0) {
			tcm.getColumn(col).setPreferredWidth(75);
		} else {
			tcm.getColumn(col).setPreferredWidth(50);
		}
		if (col>0) {
			FactorInteraction fi = factorInteractionsLink.get(col-1);
			boolean isEditable = (fi.getFactors().size()==1);
			tcm.getColumn(col).setCellRenderer(
					new LevelCellRenderer(true, !isEditable));
			if (isEditable) {
				tcm.getColumn(col).setCellEditor(new LevelCellEditor());
			}
		}
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
}
