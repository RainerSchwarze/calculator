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
import java.util.Collections;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumnModel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.modules.indxp.core.DataEvent;
import de.admadic.calculator.modules.indxp.core.DataEventDispatcher;
import de.admadic.calculator.modules.indxp.core.DataEventListener;
import de.admadic.calculator.modules.indxp.core.DataEventServer;
import de.admadic.calculator.modules.indxp.core.DataItemStatus;
import de.admadic.calculator.modules.indxp.core.Factor;
import de.admadic.calculator.modules.indxp.core.FactorInteraction;
import de.admadic.calculator.modules.indxp.core.FactorInteractionBuilder;
import de.admadic.calculator.modules.indxp.core.FactorInteractionsTableModel;
import de.admadic.ui.util.Dialog;
import de.admadic.ui.util.VetoableToggleButtonModel;

/**
 * @author Rainer Schwarze
 *
 */
public class FactorInteractionsPanel extends DataPanel 
implements ActionListener, DataEventListener {
	JTable tableFactorInteractions;
	JScrollPane scrollFactorInteractions;
	FactorInteractionsTableModel tableModelFactorInteractions;
	JPanel panelButtons;
	JButton btnCreate;
	JButton btnCreateRg;
	JButton btnAlias;
	JButton btnDeAlias;
	JButton btnRemove;
	JButton btnUp;
	JButton btnDown;
	JToggleButton btnLock;

	final static String CMD_GEN_FU_FA = "fi.genfufa";
	final static String CMD_GEN_FA_RG = "fi.genfarg";
	final static String CMD_ALIAS = "fi.alias";
	final static String CMD_DEALIAS = "fi.dealias";
	final static String CMD_REMOVE = "fi.remove";
	final static String CMD_UP = "fi.up";
	final static String CMD_DOWN = "fi.down";
	final static String CMD_LOCK = "fi.lock";

	ArrayList<FactorInteraction> factorInteractionsLink;
	ArrayList<Factor> factorsLink;
	FactorInteractionBuilder factorInteractionBuilder;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void initContents(boolean noframe) { 
        FormLayout fl;
        if (noframe) {
	        fl = new FormLayout(
	        		"0px, p:grow, 5px, p, 12px",
	        		"0px, p:grow, 0px");
        } else {
	        fl = new FormLayout(
	        		"12px, p:grow, 5px, p, 12px",
	        		"12px, p:grow, 12px");
        }
        this.setLayout(fl);
        CellConstraints cc = new CellConstraints();

		tableModelFactorInteractions = new FactorInteractionsTableModel();
        // tableModelFactorInteractions.setData(ideData.getFactorList());
        
        tableFactorInteractions = new JTable();
        tableFactorInteractions.setModel(tableModelFactorInteractions);
        scrollFactorInteractions = new JScrollPane();
        this.add(
        		scrollFactorInteractions, 
        		cc.xy(2, 2, CellConstraints.DEFAULT, CellConstraints.FILL));
        scrollFactorInteractions.setViewportView(tableFactorInteractions);
        scrollFactorInteractions.setHorizontalScrollBarPolicy(
        		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollFactorInteractions.setVerticalScrollBarPolicy(
        		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tableFactorInteractions.setPreferredScrollableViewportSize(
        		new Dimension(300, 75));

		TableColumnModel tcm = tableFactorInteractions.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(50);
		tcm.getColumn(1).setPreferredWidth(30);
		tcm.getColumn(2).setPreferredWidth(170);

        panelButtons = new JPanel();
        this.add(
        		panelButtons, 
        		cc.xy(4, 2, CellConstraints.DEFAULT, CellConstraints.FILL));

        { // create buttons:
            FormLayout flb;
	        flb = new FormLayout(
	        		"0px, p, 0px",
	        		"0px, p, 5px, p, 5px, p, 5px, p, 5px, p, 5px, p, 5px, p, 5px, p, 0px");
	        panelButtons.setLayout(flb);
	        CellConstraints ccb = new CellConstraints();

	        btnCreate = new JButton("Create");
	        btnCreate.setActionCommand(CMD_GEN_FU_FA);
	        btnCreate.addActionListener(this);
	        btnCreateRg = new JButton("Create...");
	        btnCreateRg.setActionCommand(CMD_GEN_FA_RG);
	        btnCreateRg.addActionListener(this);
	        btnAlias = new JButton("Alias");
	        btnAlias.setActionCommand(CMD_ALIAS);
	        btnAlias.addActionListener(this);
	        btnDeAlias = new JButton("Dealias");
	        btnDeAlias.setActionCommand(CMD_DEALIAS);
	        btnDeAlias.addActionListener(this);
	        btnRemove = new JButton("Del");
	        btnRemove.setActionCommand(CMD_REMOVE);
	        btnRemove.addActionListener(this);
	        btnUp = new JButton("Up");
	        btnUp.setActionCommand(CMD_UP);
	        btnUp.addActionListener(this);
	        btnDown = new JButton("Down");
	        btnDown.setActionCommand(CMD_DOWN);
	        btnDown.addActionListener(this);
	        btnLock = new JToggleButton("Lock");
	        btnLock.setActionCommand(CMD_LOCK);
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
					boolean b = (e.getStateChange()==ItemEvent.SELECTED);
					doLock(b);
				}
	        });

	        panelButtons.add(btnCreate, ccb.xy(2, 2));
	        panelButtons.add(btnCreateRg, ccb.xy(2, 4));
	        panelButtons.add(btnAlias, ccb.xy(2, 6));
	        panelButtons.add(btnDeAlias, ccb.xy(2, 8));
	        panelButtons.add(btnRemove, ccb.xy(2, 10));
	        panelButtons.add(btnUp, ccb.xy(2, 12));
	        panelButtons.add(btnDown, ccb.xy(2, 14));
	        panelButtons.add(btnLock, ccb.xy(2, 16));
        }
	}

	/**
	 * 
	 */
	public FactorInteractionsPanel() {
		super();
		initContents(false);
	}

	/**
	 * @param noframe 
	 * 
	 */
	public FactorInteractionsPanel(boolean noframe) {
		super();
		initContents(noframe);
	}

	/**
	 * @param factorInteractionsLink 
	 * @param factorsLink 
	 */
	public void linkData(
			ArrayList<FactorInteraction> factorInteractionsLink,
			ArrayList<Factor> factorsLink) {
		this.factorInteractionsLink = factorInteractionsLink;
		this.factorsLink = factorsLink;
		tableModelFactorInteractions.setData(factorInteractionsLink);
	}


	/**
	 * Registeres the data event dispatcher. Pass null to remove it.
	 * @param v
	 */
	@Override
	public void setDataEventDispatcher(DataEventDispatcher v) {
		super.setDataEventDispatcher(v);
		tableModelFactorInteractions.setDataEventDispatcher(v);
	}

	/**
	 * @param dataEventServer The dataEventServer to set.
	 */
	@Override
	public void setDataEventServer(DataEventServer dataEventServer) {
		if (this.dataEventServer!=null) {
			this.dataEventServer.removeDataListener(tableModelFactorInteractions);
			this.dataEventServer.removeDataListener(this);
		}
		super.setDataEventServer(dataEventServer);
		if (this.dataEventServer!=null) {
			this.dataEventServer.addDataListener(tableModelFactorInteractions);
			this.dataEventServer.addDataListener(this);
		}
	}

	/**
	 * @param e
	 * @see de.admadic.calculator.modules.indxp.core.DataEventListener#dataEventSignalled(de.admadic.calculator.modules.indxp.core.DataEvent)
	 */
	public void dataEventSignalled(DataEvent e) {
		// if (e.getSource()==this) return;
		if ((e.getMask() & DataEvent.FACTORINTERACTIONS_STRUCT_CHANGED)!=0) {
			if (getDataItemStatusServer()!=null) {
				DataItemStatus dis;
				dis = getDataItemStatusServer().getDataItemStatus(
						DataItemStatus.DI_FACTORINTERACTIONS);
				btnLock.setSelected(dis.isLocked());
			}
		}
	}
	
	
	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_GEN_FU_FA)) {
			doCreate();
		} else if (cmd.equals(CMD_GEN_FA_RG)) {
			doCreateRg();
		} else if (cmd.equals(CMD_ALIAS)) {
			doAlias();
		} else if (cmd.equals(CMD_DEALIAS)) {
			doDeAlias();
		} else if (cmd.equals(CMD_REMOVE)) {
			doRemove();
		} else if (cmd.equals(CMD_UP)) {
			doUp();
		} else if (cmd.equals(CMD_DOWN)) {
			doDown();
		}
	}

	private void doCreate() {
		factorInteractionBuilder.createFullFactorialInteractions();
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.FACTORINTERACTIONS_DATA_CHANGED);
		}
	}

	private void doCreateRg() {
		NumberInputDialog nidlg;
		NumberInputSet [] inputSet = new NumberInputSet[]{
				new NumberInputSet(
						"Minimum length:", "1.."+factorsLink.size(),
						1, 1, factorsLink.size(), 1),
				new NumberInputSet(
						"Maximum length:", "1.."+factorsLink.size(),
						factorsLink.size(), 1, factorsLink.size(), 1),
		};
		nidlg = new NumberInputDialog(
				"Please enter range of length for the factor interactions\n"+
				"to generate.",
				inputSet);
		nidlg.setValidator(new NumberInputValidator() {
			public boolean validate(NumberInputSet[] inputSets) {
				if (inputSets[0].getValue().intValue()>
					inputSets[1].getValue().intValue()) {
					JOptionPane.showMessageDialog(
							null,
							"Minimum length must be smaller than maximum length.",
							"Invalid data",
							JOptionPane.ERROR_MESSAGE);
					return false;
				}
				return true;
			}
		});
		nidlg.setVisible(true);
		int rc = nidlg.getResultCode();
		if (rc!=Dialog.RESULT_OK) return;

		int min, max;
		min = inputSet[0].getValue().intValue();
		max = inputSet[1].getValue().intValue();
		factorInteractionBuilder.createFactorialInteractions(min, max);
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.FACTORINTERACTIONS_DATA_CHANGED);
		}
	}

	private String getNextFactorName(String lastFactorName) {
		String nextFactorName = lastFactorName.substring(1);
		String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
		int tmpI = charSet.indexOf(lastFactorName.charAt(0));
		if (tmpI<0) {
			nextFactorName = "A" + nextFactorName;
		} else if (tmpI>=charSet.length()) {
			// too large
			nextFactorName = "A1" + nextFactorName;
		} else {
			nextFactorName = charSet.charAt(tmpI+1) + nextFactorName;
		}
		return nextFactorName;
	}
	
	private void doAlias() {
		int [] sels = tableFactorInteractions.getSelectedRows();
		if (sels.length<1) {
			JOptionPane.showMessageDialog(
					null,
					"No factor interaction selected.\n"+
					"You must select a factor interaction which shall be aliased.",
					"No factor interaction selected",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		int res = JOptionPane.showConfirmDialog(
				null,
				"Do you want to alias the " + sels.length + 
				" selected factor interactions?",
				"Confirm aliasing of factor interaction",
				JOptionPane.YES_NO_OPTION);
		if (res!=JOptionPane.YES_OPTION) 
			return;

		if (factorsLink.size()<1) {
			// error?!
			JOptionPane.showMessageDialog(
					null,
					"Factor list is empty.\n"+
					"Please check your data.",
					"Factor list is empty",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		String lastFactorName = null;
		for (Factor f : factorsLink) {
			if (lastFactorName==null) {
				lastFactorName = f.getName();
			}
			if (f.getName().compareTo(lastFactorName)>0) {
				lastFactorName = f.getName();
			}
		}

		String nextFactorName = lastFactorName;
		for (int seli : sels) {
			FactorInteraction fi = factorInteractionsLink.get(seli);
			if (fi.getFactors().size()==1) return; // 1 element -> no alias
			nextFactorName = getNextFactorName(nextFactorName);
			fi.setAliased(true, nextFactorName);
		}

		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.FACTORINTERACTIONS_DATA_CHANGED);
		}
	}

	private void doDeAlias() {
		int [] sels = tableFactorInteractions.getSelectedRows();
		if (sels.length<1) {
			JOptionPane.showMessageDialog(
					null,
					"No factor interaction selected.\n"+
					"You must select a factor interaction which shall be de-aliased.",
					"No factor interaction selected",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		int res = JOptionPane.showConfirmDialog(
				null,
				"Do you want to de-alias the " + sels.length + 
				" selected factor interactions?",
				"Confirm de-aliasing of factor interaction",
				JOptionPane.YES_NO_OPTION);
		if (res!=JOptionPane.YES_OPTION) 
			return;

		for (int seli : sels) {
			FactorInteraction fi = factorInteractionsLink.get(seli);
			fi.setAliased(false, null);
		}

		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.FACTORINTERACTIONS_DATA_CHANGED);
		}
	}
	
	private void doRemove() {
		int [] sels = tableFactorInteractions.getSelectedRows();
		if (sels.length<1) {
			JOptionPane.showMessageDialog(
					null,
					"No factor interaction selected.\n"+
					"You must select a factor interaction which shall be removed.",
					"No factor interaction selected",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		int res = JOptionPane.showConfirmDialog(
				null,
				"Do you want to delete the " + sels.length + 
				" selected factor interactions?",
				"Confirm removal of factor interaction",
				JOptionPane.YES_NO_OPTION);
		if (res!=JOptionPane.YES_OPTION) 
			return;
		Vector<FactorInteraction> removeV = new Vector<FactorInteraction>();
		for (int selsi : sels) {
			removeV.add(factorInteractionsLink.get(selsi));
		}
		factorInteractionsLink.removeAll(removeV);

		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.FACTORINTERACTIONS_DATA_CHANGED);
		}
	}

	private void doUp() {
		int sel = tableFactorInteractions.getSelectedRow();
		if (sel<0) return;	// no selection, no comment
		if (sel<1) return;	// first element, no comment
		Collections.swap(factorInteractionsLink, sel, sel-1);
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.FACTORINTERACTIONS_DATA_CHANGED);
		}
		tableFactorInteractions.setRowSelectionInterval(sel-1, sel-1);
	}

	private void doDown() {
		int sel = tableFactorInteractions.getSelectedRow();
		if (sel<0) return;	// no selection, no comment
		if (sel>=(factorInteractionsLink.size()-1)) return; // last element, no comment
		Collections.swap(factorInteractionsLink, sel, sel+1);
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.FACTORINTERACTIONS_DATA_CHANGED);
		}
		tableFactorInteractions.setRowSelectionInterval(sel+1, sel+1);
	}

	protected void doLock(boolean b) {
		// lock the data
		DataItemStatus di = getDataItemStatusServer().getDataItemStatus(
				DataItemStatus.DI_FACTORINTERACTIONS);
		DataPanel.enableComponents(panelButtons, !b, btnLock);
		tableModelFactorInteractions.setLocked(b);
		if (b) {
			di.lock(DataItemStatus.LOCKED_ALL);
		} else {
			di.unlock(DataItemStatus.LOCKED_ALL);
		}
	}

	protected boolean allowLockOp(boolean oldLock, boolean newLock) {
		if (getDataItemStatusServer()!=null) {
			DataItemStatus dis;
			dis = getDataItemStatusServer().getDataItemStatus(
					DataItemStatus.DI_FACTORINTERACTIONS);
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
	 * @param factorInteractionBuilder The factorInteractionBuilder to set.
	 */
	public void setFactorInteractionBuilder(
			FactorInteractionBuilder factorInteractionBuilder) {
		this.factorInteractionBuilder = factorInteractionBuilder;
	}

	/**
	 * 
	 * @see de.admadic.calculator.modules.indxp.ui.DataPanel#updateLockStatus()
	 */
	@Override
	public void updateLockStatus() {
		if (getDataItemStatusServer()!=null) {
			DataItemStatus dis = getDataItemStatusServer().getDataItemStatus(
					DataItemStatus.DI_FACTORINTERACTIONS);
			btnLock.setSelected(dis.isLocked());
		}
	}

}
