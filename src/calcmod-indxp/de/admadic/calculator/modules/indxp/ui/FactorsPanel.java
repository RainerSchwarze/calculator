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

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.admadic.calculator.modules.indxp.core.DataEvent;
import de.admadic.calculator.modules.indxp.core.DataEventDispatcher;
import de.admadic.calculator.modules.indxp.core.DataEventListener;
import de.admadic.calculator.modules.indxp.core.DataEventServer;
import de.admadic.calculator.modules.indxp.core.DataItemStatus;
import de.admadic.calculator.modules.indxp.core.Factor;
import de.admadic.calculator.modules.indxp.core.FactorsTableModel;
import de.admadic.ui.util.VetoableToggleButtonModel;

/**
 * @author Rainer Schwarze
 *
 */
public class FactorsPanel extends DataPanel 
implements ActionListener, DataEventListener {
	JTable tableFactors;
	JScrollPane scrollFactors;
	FactorsTableModel tableModelFactors;
	JPanel panelButtons;
	JButton btnAdd;
	JButton btnRemove;
	JButton btnUp;
	JButton btnDown;
	JToggleButton btnLock;
	final static String CMD_ADD = "factor.add";
	final static String CMD_REMOVE = "factor.remove";
	final static String CMD_UP = "factor.up";
	final static String CMD_DOWN = "factor.down";
	final static String CMD_LOCK = "factor.lock";

	ArrayList<Factor> factorsLink;
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

		tableModelFactors = new FactorsTableModel();
        // tableModelFactors.setData(ideData.getFactorList());
        
        tableFactors = new JTable();
        tableFactors.setModel(tableModelFactors);
        scrollFactors = new JScrollPane();
        this.add(
        		scrollFactors, 
        		cc.xy(2, 2, CellConstraints.DEFAULT, CellConstraints.FILL));
        scrollFactors.setViewportView(tableFactors);
        scrollFactors.setHorizontalScrollBarPolicy(
        		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollFactors.setVerticalScrollBarPolicy(
        		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tableFactors.setPreferredScrollableViewportSize(
        		new Dimension(300, 75));

        panelButtons = new JPanel();
        this.add(
        		panelButtons, 
        		cc.xy(4, 2, CellConstraints.DEFAULT, CellConstraints.FILL));

        { // create buttons:
            FormLayout flb;
	        flb = new FormLayout(
	        		"0px, p, 0px",
	        		"0px, p, 5px, p, 5px, p, 5px, p, 5px, p, 0px");
	        panelButtons.setLayout(flb);
	        CellConstraints ccb = new CellConstraints();

	        btnAdd = new JButton("Add");
	        btnAdd.setActionCommand(CMD_ADD);
	        btnAdd.addActionListener(this);
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

	        panelButtons.add(btnAdd, ccb.xy(2, 2));
	        panelButtons.add(btnRemove, ccb.xy(2, 4));
	        panelButtons.add(btnUp, ccb.xy(2, 6));
	        panelButtons.add(btnDown, ccb.xy(2, 8));
	        panelButtons.add(btnLock, ccb.xy(2, 10));
        }
	}

	/**
	 * 
	 */
	public FactorsPanel() {
		super();
		initContents(false);
	}

	/**
	 * @param noframe 
	 * 
	 */
	public FactorsPanel(boolean noframe) {
		super();
		initContents(noframe);
	}

	/**
	 * @param list
	 */
	public void linkData(ArrayList<Factor> list) {
		factorsLink = list;
		tableModelFactors.setData(list);
	}


	/**
	 * Registeres the data event dispatcher. Pass null to remove it.
	 * @param v
	 */
	@Override
	public void setDataEventDispatcher(DataEventDispatcher v) {
		super.setDataEventDispatcher(v);
		tableModelFactors.setDataEventDispatcher(v);
	}

	/**
	 * @param dataEventServer The dataEventServer to set.
	 */
	@Override
	public void setDataEventServer(DataEventServer dataEventServer) {
		if (getDataEventServer()!=null) {
			getDataEventServer().removeDataListener(tableModelFactors);
			getDataEventServer().removeDataListener(this);
		}
		super.setDataEventServer(dataEventServer);
		if (getDataEventServer()!=null) {
			getDataEventServer().addDataListener(tableModelFactors);
			getDataEventServer().addDataListener(this);
		}
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_ADD)) {
			doAdd();
		} else if (cmd.equals(CMD_REMOVE)) {
			doRemove();
		} else if (cmd.equals(CMD_UP)) {
			doUp();
		} else if (cmd.equals(CMD_DOWN)) {
			doDown();
		}
	}

	private void doAdd() {
		int sel = tableFactors.getSelectedRow();
		Factor f = new Factor();
		if (sel<0) {
			factorsLink.add(f);
		} else {
			factorsLink.add(sel, f);
		}
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.FACTORS_STRUCT_CHANGED);
		}
	}

	private void doRemove() {
		int sel = tableFactors.getSelectedRow();
		if (sel<0) {
			JOptionPane.showMessageDialog(
					null,
					"No factor selected.\n"+
					"You must select a factor which shall be removed.",
					"No factor selected",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		Factor f = factorsLink.get(sel);
		int res = JOptionPane.showConfirmDialog(
				null,
				"Do you want to delete the following factor?\n"+
				f.getName() + ", " + 
				f.getEntity() + " [" + f.getUnit() + "], " +
				f.getValueHigh() + ", " + f.getValueLow(),
				"Confirm removal of factor",
				JOptionPane.YES_NO_OPTION);
		if (res!=JOptionPane.YES_OPTION) 
			return;
		factorsLink.remove(sel);
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.FACTORS_STRUCT_CHANGED);
		}
	}

	private void doUp() {
		int sel = tableFactors.getSelectedRow();
		if (sel<0) return;	// no selection, no comment
		if (sel<1) return;	// first element, no comment
		Collections.swap(factorsLink, sel, sel-1);
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.FACTORS_DATA_CHANGED);
		}
		tableFactors.setRowSelectionInterval(sel-1, sel-1);
	}

	private void doDown() {
		int sel = tableFactors.getSelectedRow();
		if (sel<0) return;	// no selection, no comment
		if (sel>=(factorsLink.size()-1)) return; // last element, no comment
		Collections.swap(factorsLink, sel, sel+1);
		if (getDataEventDispatcher()!=null) {
			getDataEventDispatcher().notifyEvent(
					this, DataEvent.FACTORS_DATA_CHANGED);
		}
		tableFactors.setRowSelectionInterval(sel+1, sel+1);
	}

	protected void doLock(boolean b) {
		// lock the data
		DataItemStatus di = getDataItemStatusServer().getDataItemStatus(
				DataItemStatus.DI_FACTORS);
		DataPanel.enableComponents(panelButtons, !b, btnLock);
		tableModelFactors.setLocked(b);
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
					DataItemStatus.DI_FACTORS);
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
	 * @param e
	 * @see de.admadic.calculator.modules.indxp.core.DataEventListener#dataEventSignalled(de.admadic.calculator.modules.indxp.core.DataEvent)
	 */
	public void dataEventSignalled(DataEvent e) {
		if (e.getSource()==this) return;
		if ((e.getMask() & DataEvent.FACTORS_STRUCT_CHANGED)!=0) {
			if (getDataItemStatusServer()!=null) {
				DataItemStatus dis;
				dis = getDataItemStatusServer().getDataItemStatus(
						DataItemStatus.DI_FACTORS);
				btnLock.setSelected(dis.isLocked());
			}
		}
	}
}
