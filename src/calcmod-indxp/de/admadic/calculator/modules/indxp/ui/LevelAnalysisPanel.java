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
import de.admadic.calculator.modules.indxp.core.ExpResults;
import de.admadic.calculator.modules.indxp.core.FactorInteraction;
import de.admadic.calculator.modules.indxp.core.LevelAnalysis;
import de.admadic.calculator.modules.indxp.core.LevelAnalysisTableModel;
import de.admadic.ui.util.ColorGradient;

/**
 * @author Rainer Schwarze
 *
 */
public class LevelAnalysisPanel extends DataPanel 
implements ActionListener, DataEventListener, TableColumnModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTable tableLevelAnalysis;
	JScrollPane scrollLevelAnalysis;
	LevelAnalysisTableModel tableModelLevelAnalysis;
	GradientCellRenderer gradientCellRenderer;

	JPanel panelButtons;
	JButton btnCalculate;
	JButton btnReset;
	JToggleButton btnColorize;

	final static String CMD_CALCULATE = "levan.calculate";
	final static String CMD_RESET = "levan.reset";
	final static String CMD_COLORIZE = "levan.colorize";

	LevelAnalysis levelAnalysisLink;
	ArrayList<FactorInteraction> factorInteractionsLink;
	ArrayList<ExpResults> expResultsLink;

	FloatingPointFormatter fpf;
	
	/**
	 * @param fpf 
	 * 
	 */
	public LevelAnalysisPanel(FloatingPointFormatter fpf) {
		super();
		setFloatingPointFormatter(fpf);
		initContents(false);
	}

	/**
	 * @param noframe 
	 * 
	 */
	public LevelAnalysisPanel(boolean noframe) {
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
		
        tableModelLevelAnalysis = new LevelAnalysisTableModel();
        
        tableLevelAnalysis = new JTable();
        tableLevelAnalysis.setModel(tableModelLevelAnalysis);
        tableLevelAnalysis.getColumnModel().addColumnModelListener(this);
        scrollLevelAnalysis = new JScrollPane();
        this.add(
        		scrollLevelAnalysis, 
        		cc.xy(2, 2, CellConstraints.DEFAULT, CellConstraints.FILL));
        scrollLevelAnalysis.setViewportView(tableLevelAnalysis);
        scrollLevelAnalysis.setHorizontalScrollBarPolicy(
        		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollLevelAnalysis.setVerticalScrollBarPolicy(
        		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tableLevelAnalysis.setPreferredScrollableViewportSize(
        		new Dimension(300, 80));
        tableLevelAnalysis.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        gradientCellRenderer = new GradientCellRenderer(
        		ColorGradient.JET(), 
        		GradientCellRenderer.BORDER_BOTTOM,
        		GradientCellRenderer.RANGE_HORZ);
        gradientCellRenderer.setFloatingPointFormatter(fpf);
        tableLevelAnalysis.setDefaultRenderer(
        		Double.class, gradientCellRenderer);

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

        	btnCalculate = new JButton("Create");
        	btnCalculate.setActionCommand(CMD_CALCULATE);
        	btnCalculate.addActionListener(this);
        	btnReset = new JButton("Reset...");
        	btnReset.setActionCommand(CMD_RESET);
        	btnReset.addActionListener(this);
        	btnColorize = new JToggleButton("Color");
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
        	
        	panelButtons.add(btnCalculate, ccb.xy(1, 1));
        	panelButtons.add(btnReset, ccb.xy(1, 3));
        	panelButtons.add(btnColorize, ccb.xy(1, 5));
        }
	}

	/**
	 * @param levelAnalysis 
	 * @param expResults 
	 * @param factorInteractions 
	 */
	public void linkData(
			LevelAnalysis levelAnalysis,
			ArrayList<ExpResults> expResults, 
			ArrayList<FactorInteraction> factorInteractions) {
		this.levelAnalysisLink = levelAnalysis;
		this.expResultsLink = expResults;
		this.factorInteractionsLink = factorInteractions;
		tableModelLevelAnalysis.setData(
				factorInteractionsLink, levelAnalysis);
	}

	/**
	 * Registeres the data event dispatcher. Pass null to remove it.
	 * @param v
	 */
	@Override
	public void setDataEventDispatcher(DataEventDispatcher v) {
		super.setDataEventDispatcher(v);
		tableModelLevelAnalysis.setDataEventDispatcher(v);
	}

	/**
	 * @param dataEventServer The dataEventServer to set.
	 */
	@Override
	public void setDataEventServer(DataEventServer dataEventServer) {
		if (this.dataEventServer!=null) {
			this.dataEventServer.removeDataListener(tableModelLevelAnalysis);
			this.dataEventServer.removeDataListener(this);
		}
		super.setDataEventServer(dataEventServer);
		if (this.dataEventServer!=null) {
			this.dataEventServer.addDataListener(tableModelLevelAnalysis);
			this.dataEventServer.addDataListener(this);
		}
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_CALCULATE)) {
			doCalculate();
		} else if (cmd.equals(CMD_RESET)) {
			doReset();
		} else if (cmd.equals(CMD_COLORIZE)) {
			// doColorize();
			// done in the ItemListener
		}
	}

	/**
	 * Perform lock or unlock on the tablemodel / data.
	 * @param b
	 */
	protected void doColorize(boolean b) {
		// FIXME: install the colorized cell renderer
		// tableModelLevelAnalysis.setLocked(b);
		gradientCellRenderer.setOn(b);
		tableModelLevelAnalysis.fireTableDataChanged();
	}

	private void doCalculate() {
		levelAnalysisLink.calculate();
		updateRanges();
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.LEVELANALYSIS_STRUCT_CHANGED);
		}
	}

	private void doReset() {
		int res = JOptionPane.showConfirmDialog(
				null,
				"Please confirm that the analysis data shall be reset.\n"+
				"Do you want to reset them?",
				"Confirm reset of analysis data",
				JOptionPane.YES_NO_OPTION);
		if (res!=JOptionPane.YES_OPTION) return;

		levelAnalysisLink.resetData();
		if (dataEventDispatcher!=null) {
			dataEventDispatcher.notifyEvent(
					this, DataEvent.EXPRESULTS_DATA_CHANGED);
		}
	}

	/**
	 * @param e
	 * @see javax.swing.event.TableColumnModelListener#columnAdded(javax.swing.event.TableColumnModelEvent)
	 */
	public void columnAdded(TableColumnModelEvent e) {
		int col = e.getToIndex();
		TableColumnModel tcm = tableLevelAnalysis.getColumnModel();
//		tcm.getColumn(col).setCellRenderer(new ExpResultsCellRenderer());
		if (col==0) {
			tcm.getColumn(col).setPreferredWidth(75);
		} else {
			tcm.getColumn(col).setPreferredWidth(50);
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

	protected void updateRanges() {
		Double [] rg;
		gradientCellRenderer.clearRanges();
		rg = levelAnalysisLink.getRange(LevelAnalysis.RES_LEV_HIGH);
		gradientCellRenderer.addCellRange(Integer.valueOf(0), rg[0], rg[1]);
		rg = levelAnalysisLink.getRange(LevelAnalysis.RES_LEV_LOW);
		gradientCellRenderer.addCellRange(Integer.valueOf(1), rg[0], rg[1]);
		rg = levelAnalysisLink.getRange(LevelAnalysis.RES_LEV_DELTA);
		gradientCellRenderer.addCellRange(Integer.valueOf(2), rg[0], rg[1]);
		tableModelLevelAnalysis.fireTableDataChanged();
	}
	
	/**
	 * @param e
	 * @see de.admadic.calculator.modules.indxp.core.DataEventListener#dataEventSignalled(de.admadic.calculator.modules.indxp.core.DataEvent)
	 */
	public void dataEventSignalled(DataEvent e) {
		if (e.getSource()==this) return;
		if (
				((e.getMask() & DataEvent.LEVELANALYSIS_DATA_CHANGED)!=0) ||
				((e.getMask() & DataEvent.LEVELANALYSIS_STRUCT_CHANGED)!=0)
			) {
			updateRanges();
		}		
	}

	/**
	 * @param fpf
	 */
	public void setFloatingPointFormatter(FloatingPointFormatter fpf) {
		this.fpf = fpf;
	}
}
