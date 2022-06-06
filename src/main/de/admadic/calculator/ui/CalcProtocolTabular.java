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
package de.admadic.calculator.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import de.admadic.calculator.processor.IProcessorCalculation;
import de.admadic.calculator.processor.ProcessorUnknownOpException;
import de.admadic.calculator.processor.ProtocolEvent;
import de.admadic.calculator.processor.ProtocolEventListener;
import de.admadic.calculator.types.CaDouble;
import de.admadic.calculator.types.CaDoubleFormatter;
import de.admadic.calculator.types.CaNumber;
import de.admadic.calculator.types.CaNumberFormatterContext;
import de.admadic.ui.util.AbstractRendererEditorColumnAccessor;
import de.admadic.ui.util.ListRefTableModel;
import de.admadic.ui.util.RendererEditorColumnAccessor;
import de.admadic.ui.util.StringPrinter;
import de.admadic.util.StringUtil;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcProtocolTabular extends JPanel 
implements ProtocolEventListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CaNumberFormatterContext numFmtCtx;
	
	ListRefTableModel tableModel;
	AbstractRendererEditorColumnAccessor columnAccessor;
	JTable tableProtocol;
	JScrollPane scrollPane;
	CalcSequence calcSequence;
	JLabel labelStatus;

	TableRenderer tableRenderer;
	TableEditor tableEditor;

	JToolBar toolBar;

	String [] columnNames;
	int columnCount;
	int [] columnWidths;

	IProcessorCalculation processorCalculation;
	UpdateAction actionUpdate;
	CalcToggleButton btnMotionLock;

	String statusText = "ok";
	boolean dirty = false;

	static String strLF = "\n";
	
	/**
	 * 
	 */
	public CalcProtocolTabular() {
		super();
		initContents();
	}

	/**
	 * @param numFmtCtx 
	 * 
	 */
	public CalcProtocolTabular(CaNumberFormatterContext numFmtCtx) {
		super();
		this.numFmtCtx = numFmtCtx;
		initContents();
	}

	private String[] sublist(String [] list, int count) {
		String [] nl = new String[count];
		System.arraycopy(list, 0, nl, 0, count);
		return nl;
	}
	
	private void initContents() {
		try {
			String tmp = System.getProperty("line.separator", "\n");
			strLF = tmp;
		} catch (Exception e) {
			// keep silent
		}
		columnNames = new String[] {
			"Comment", "Op", "Expression", "Value", "(subres)", "(res)"
		};
		columnCount = 4; // public releases should have 4 columns
		columnWidths = new int[] {
			100, 20, 100, 100, 60, 60
		};

		calcSequence = new CalcSequence();

		this.setLayout(new BorderLayout());

		tableProtocol = new JTable();
		scrollPane = new JScrollPane(
				tableProtocol,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane, BorderLayout.CENTER);

		labelStatus = new JLabel();
		this.add(labelStatus, BorderLayout.PAGE_END);
		setStatusText("ok");
		
		scrollPane.setBorder(
				BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		columnAccessor = new CalcRowColumnAccessor();
		tableModel = new ListRefTableModel();
		tableModel.setData(calcSequence.getRowsList());
		tableModel.setColumnAccessor(columnAccessor);
		tableModel.setColumns(sublist(columnNames, columnCount));
		tableRenderer = new TableRenderer(columnAccessor);
		if (numFmtCtx!=null) tableRenderer.setNumFmtCtx(numFmtCtx);
		tableEditor = new TableEditor(columnAccessor);
		if (numFmtCtx!=null) tableEditor.setNumFmtCtx(numFmtCtx);
		tableProtocol.setDefaultRenderer(CalcRowBase.class, tableRenderer);
		tableProtocol.setDefaultEditor(CalcRowBase.class, tableEditor);
		tableProtocol.setModel(tableModel);
		tableProtocol.setShowHorizontalLines(false);
		tableProtocol.getTableHeader().setReorderingAllowed(false);
		for (int i=0; i<columnCount; i++)  {
			tableProtocol.getColumnModel().getColumn(i).setPreferredWidth(
					columnWidths[i]);
		}

		toolBar = new JToolBar();
		this.add(toolBar, BorderLayout.PAGE_START);
		
		toolBar.add(new SaveAction());
		toolBar.add(new PrintAction());
		toolBar.add(new CopyAction());
		toolBar.addSeparator();
		toolBar.add(actionUpdate = new UpdateAction());
		toolBar.addSeparator();
		toolBar.add(new InsertAction());
		toolBar.add(new InsertResultAction());
		toolBar.add(new InsertCommentAction());
		toolBar.addSeparator();
		toolBar.add(new ClearAllAction());
		toolBar.add(new DeleteAction());
		toolBar.addSeparator();
		toolBar.add(new MoveUpAction());
		toolBar.add(new MoveDownAction());
		toolBar.addSeparator();
		toolBar.add(btnMotionLock = new CalcToggleButton());
	}

	/**
	 * @return	Returns the button used for motion lock.
	 */
	public CalcToggleButton getMotionLockButton() {
		return btnMotionLock;
	}
	
	protected void setStatusText(String text) {
		String dirtyTxt;
		if (isDirty()) {
			dirtyTxt = "(Update Calculation!)";
		} else {
			dirtyTxt = "";
		}
		if (text!=null) {
			statusText = text;
		}
		labelStatus.setText("Status: " + statusText + " " + dirtyTxt);
	}
	
	/**
	 * 
	 */
	public void refreshDisplay() {
		tableModel.fireTableDataChanged();
	}

	protected void updateScrollPos() {
		tableProtocol.scrollRectToVisible(
				tableProtocol.getCellRect(
						tableProtocol.getRowCount()-1, 0, true));
	}
	
	/**
	 * @param event
	 * @see de.admadic.calculator.processor.ProtocolEventListener#addOp(de.admadic.calculator.processor.ProtocolEvent)
	 */
	public void addOp(ProtocolEvent event) {
		CalcRowOp cr = new CalcRowOp();
		cr.setOperation(event.getOperation());
		try {
			cr.setValue(event.getValue().clone());
			if (event.getLastSubResult()!=null) {
				cr.setLastResultValue(event.getLastSubResult().clone());
			}
			if (event.getCurResult()!=null) {
				cr.setResultValue(event.getCurResult().clone());
			}
		} catch (CloneNotSupportedException e) { /* nope */ }
		//cr.setAccuValue(?)
		calcSequence.add(cr);
		int row = calcSequence.getRowsList().size() - 1;
		tableModel.fireTableRowsInserted(row, row);
		updateScrollPos();
	}

	/**
	 * @param event
	 * @see de.admadic.calculator.processor.ProtocolEventListener#addSubExprOp(de.admadic.calculator.processor.ProtocolEvent)
	 */
	public void addSubExprOp(ProtocolEvent event) {
		CalcRowOp cr = new CalcRowOp();
		cr.setOperation(event.getOperation());
		try {
			cr.setValue(event.getValue().clone());
			if (event.getLastSubResult()!=null) {
				cr.setLastResultValue(event.getLastSubResult().clone());
			}
			if (event.getCurResult()!=null) {
				cr.setResultValue(event.getCurResult().clone());
			}
		} catch (CloneNotSupportedException e) { /* nope */ }
		cr.setSubExpr(event.getSubExpression());
//		System.out.println("received cr: " + cr.toString());
		//cr.setAccuValue(?)
		calcSequence.add(cr);
		int row = calcSequence.getRowsList().size() - 1;
		tableModel.fireTableRowsInserted(row, row);
		updateScrollPos();
	}

	/**
	 * @param event
	 * @see de.admadic.calculator.processor.ProtocolEventListener#addResult(de.admadic.calculator.processor.ProtocolEvent)
	 */
	public void addResult(ProtocolEvent event) {
		CalcRowResult cr = new CalcRowResult();
		cr.setOperation(event.getOperation());
		try {
			cr.setValue(event.getValue().clone());
			if (event.getCurResult()!=null) {
				cr.setResultValue(event.getCurResult().clone());
			}
		} catch (CloneNotSupportedException e) { /* nope */ }
		//cr.setAccuValue(?)
		calcSequence.add(cr);
		CalcRowBase crb = new CalcRowComment();
		calcSequence.add(crb);
		int row = calcSequence.getRowsList().size() - 1;
		tableModel.fireTableRowsInserted(row-1, row);
		updateScrollPos();
	}

	/**
	 * @param event
	 * @see de.admadic.calculator.processor.ProtocolEventListener#addClear(de.admadic.calculator.processor.ProtocolEvent)
	 */
	public void addClear(ProtocolEvent event) {
		CalcRowComment cr = new CalcRowComment();
		cr.setOperation(".clear");
		//cr.setAccuValue(?)
		calcSequence.add(cr);
		int row = calcSequence.getRowsList().size() - 1;
		tableModel.fireTableRowsInserted(row, row);
		updateScrollPos();
	}

	protected void updateProtocolCalculation() {
		if (getProcessorCalculation()==null) {
			// no calculation interface registered
			return;
		}

		boolean allOk = true;

		List<CalcRowBase> rowList = calcSequence.getRowsList();
		CaDouble lastResult = new CaDouble();
		CaDouble currentResult = new CaDouble();
		for (CalcRowBase base : rowList) {
			CaDouble argument = null;
			String op = null;
			String subEx = null;

			base.setStatus(CalcRowBase.OK);
			try {
				// System.out.println("row: " + base.toString());
				if (base instanceof CalcRowComment) continue;
				
				// FIXME: maybe put that into the actual CalcRow-subclasses
				if (base instanceof CalcRowOp || base instanceof CalcRowResult) {
					if (base.getLastResultValue()==null) {
						base.setLastResultValue(new CaDouble());
					}
					lastResult.cloneTo((CaDouble)base.getLastResultValue());
				}
				if (base instanceof CalcRowResult) {
					lastResult.cloneTo((CaDouble)base.getValue());
					base.setResultValue(new CaDouble());
					lastResult.cloneTo((CaDouble)base.getResultValue());
					continue;
				}
				if (base instanceof CalcRowOp) {
					op = base.getOperation();
				}
				// that should not be necessary:
//				if (base.getLastResultValue()!=null) {
//					((CaDouble)base.getLastResultValue()).cloneTo(lastResult);
//				}
				subEx = base.getSubExpr();
				if (subEx!=null && !subEx.equals("")) {
					// special case for %:
					if (subEx.contains("ans")) {
						subEx = subEx.replaceAll(
								"ans", 
								lastResult.toString());
					}
					if (subEx.contains("%")) {
						if (op!=null && (op.equals("+") || (op.equals("-")))) {
							subEx += " * " + lastResult.toString();
						} else {
							// nothing, just value in %
						}
					}
					argument = new CaDouble();
					try {
						getProcessorCalculation().calcSubExpr(subEx, argument);
					} catch (ProcessorUnknownOpException e) {
						// e.printStackTrace();
						throw new Exception(e.getMessage());
					}
					if (base.getValue()==null) {
						base.setValue(new CaDouble());
					}
					argument.cloneTo((CaDouble)base.getValue());
				} else {
					// take the value from the row
					argument = new CaDouble();
					((CaDouble)base.getValue()).cloneTo(argument);
				}
	
				if (op!=null && !op.equals("")) {
					try {
						int order = getProcessorCalculation().getOpOrder(op);
						switch (order) {
						case 0:
							getProcessorCalculation().calcConstantOp(
									op,
									currentResult);
							break;
						case 1:
							getProcessorCalculation().calcUnaryOp(
									op,
									argument, currentResult);
							// for unary ops, we need to copy the result
							// to the value field:
							if (base.getValue()==null) {
								base.setValue(new CaDouble());
							}
							currentResult.cloneTo((CaDouble)base.getValue());
							break;
						case 2:
							getProcessorCalculation().calcBinaryOp(
									op,
									lastResult, argument, currentResult);
							break;
						}
					} catch (ProcessorUnknownOpException e) {
						// e.printStackTrace();
						throw new Exception(e.getMessage());
					}
				} else {
					argument.cloneTo(currentResult);
				}
				if (base.getResultValue()==null) {
					base.setResultValue(new CaDouble());
				}
				currentResult.cloneTo((CaDouble)base.getResultValue());
				currentResult.cloneTo(lastResult);
			} catch (Exception e) {
				// e.printStackTrace();
				// if that happens, we have a bad error:
				base.setStatus(CalcRowBase.BAD);
				setStatusText("Error: " + e.getMessage());
				allOk = false;
				break; // out of the for loop
			}
		}
		if (allOk) {
			setDirty(false);
			setStatusText("ok");
		}
	}

	protected File getSaveFileNameJFC() {
		class TextFilter extends FileFilter {
		    @Override
			public boolean accept(File f) {
		        if (f.isDirectory()) {
		            return true;
		        }
		        String ext = null;
		        String s = f.getName();
		        int i = s.lastIndexOf('.');
		        if (i > 0 &&  i < s.length() - 1) {
		            ext = s.substring(i+1).toLowerCase();
		        }
		        if (ext != null) {
		        	boolean retcode;
		        	retcode = ext.equals("txt");
		            return retcode;
		        }
		        return false;
		    }

		    //The description of this filter
		    @Override
			public String getDescription() {
		        return "Text Files (*.txt)";
		    }
		}

		File file = null; 
		JFileChooser fc;
		fc = new JFileChooser();
		fc.addChoosableFileFilter(new TextFilter());
		fc.setFileHidingEnabled(false);
		int result = fc.showSaveDialog(this);
		switch (result) {
		case JFileChooser.APPROVE_OPTION: // open or save
			file = fc.getSelectedFile();
			if (file.exists()) {
				int returnValue = JOptionPane.showConfirmDialog(
						this, 
						"The file " + file.toString() + " already exist.\n"+
				"Overwrite?");
				if (returnValue == JOptionPane.YES_OPTION) {
					return file;
				} else {
					// cancel
					return null;
				}	  	    	
			} else {
				return file;
			}
			// unreachable
			// break;
		case JFileChooser.CANCEL_OPTION:
			break;
		case JFileChooser.ERROR_OPTION:
			break;
		}

		return null;
	}

	protected void saveProtocolTxt() {
		File file = getSaveFileNameJFC();
		if (file==null) return;

		CalcSequenceExportTxt cseqexp = new CalcSequenceExportTxt(
				calcSequence, numFmtCtx);
		String data = cseqexp.createExportString();

		FileOutputStream fos;

		try {
			fos = new FileOutputStream(file);
			fos.write(data.getBytes());
			fos.close();
		} catch (Exception e) {
			// e.printStackTrace();
			JOptionPane.showMessageDialog(
					null, 
					"Could not write the export data to the file.\n"+
					"Error: " + e.getMessage(),
					"Error saving file",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			fos = null;
			file = null;
		}
	}
	
	/**
	 * @author Rainer Schwarze
	 */
	public static class CalcRowColumnAccessor 
	extends AbstractRendererEditorColumnAccessor {
		/** Comment is column 0 */
		public final static int COL_CMT = 0;
		/** OP is column 1 */
		public final static int COL_OP = 1;
		/** SubExpr is column 2 */
		public final static int COL_SUBEXPR = 2;
		/** value is column 3 */
		public final static int COL_VALUE = 3;
		/** lastsubresult is column 4 */
		public final static int COL_LASTSUBRESULT = 4;
		/** result is column 5 */
		public final static int COL_RESULT = 5;

		/**
		 * @param rowItem
		 * @param value
		 * @param columnIndex
		 * @see de.admadic.ui.util.ColumnAccessor#setValueAt(java.lang.Object, java.lang.Object, int)
		 */
		public void setValueAt(Object rowItem, Object value, int columnIndex) {
			// should never be called
		}

		/**
		 * @param rowItem
		 * @param columnIndex
		 * @return	Returns the value at the specified column
		 * @see de.admadic.ui.util.ColumnAccessor#getValueAt(java.lang.Object, int)
		 */
		public Object getValueAt(Object rowItem, int columnIndex) {
			if (rowItem==null) return null;
			return rowItem;
		}

		/**
		 * @param rowItem
		 * @param columnIndex
		 * @return	Returns whether the cell is editable
		 * @see de.admadic.ui.util.ColumnAccessor#isCellEditable(java.lang.Object, int)
		 */
		@Override
		public boolean isCellEditable(Object rowItem, int columnIndex) {
			// results: only comment is editable
			// others: anything except lastsubresult and result value
			if (rowItem instanceof CalcRowResult) {
				if (columnIndex==COL_CMT)
					return true;
				return false;
			}
			if (columnIndex==COL_LASTSUBRESULT || columnIndex==COL_RESULT)
				return false;
			return true;
		}

		/**
		 * @param columnIndex
		 * @return	Return the class of the column
		 * @see de.admadic.ui.util.ColumnAccessor#getColumnClass(int)
		 */
		public Class getColumnClass(int columnIndex) {
			return CalcRowBase.class;
//			switch (columnIndex) {
//			case COL_CMT: return String.class;
//			case COL_OP: return String.class;
//			case COL_SUBEXPR: return String.class;
//			case COL_VALUE: return CaNumber.class;
//			}
//			return null;
		}

		/**
		 * @param rowItem
		 * @param columnIndex
		 * @return	Returns the value to be rendered for the specified column.
		 * @see de.admadic.ui.util.AbstractRendererEditorColumnAccessor#getRendererValueAt(java.lang.Object, int)
		 */
		@Override
		public Object getRendererValueAt(Object rowItem, int columnIndex) {
			if (rowItem==null) return null;
			CalcRowBase cr = (CalcRowBase)rowItem;
			switch (columnIndex) {
			case COL_CMT: return cr.getComment();
			case COL_OP: return cr.getOperation();
			case COL_SUBEXPR: return cr.getSubExpr();
			case COL_VALUE: {
				if (rowItem instanceof CalcRowComment) {
					return cr.getCommentValue();
				} else {
					return cr.getValue();
				}
			}
			case COL_LASTSUBRESULT: return cr.getLastResultValue();
			case COL_RESULT: return cr.getResultValue();
			}
			return null;
		}

		/**
		 * @param rowItem
		 * @param value
		 * @param columnIndex
		 * @see de.admadic.ui.util.AbstractRendererEditorColumnAccessor#setRendererValueAt(java.lang.Object, java.lang.Object, int)
		 */
		@Override
		public void setRendererValueAt(Object rowItem, Object value, int columnIndex) {
			// should never be called
			CalcRowBase cr = (CalcRowBase)rowItem;
			switch (columnIndex) {
			case COL_CMT: cr.setComment((String)value); break;
			case COL_OP: cr.setOperation((String)value); break;
			case COL_SUBEXPR: cr.setSubExpr((String)value); break;
			case COL_VALUE: {
				if (rowItem instanceof CalcRowComment) {
					cr.setCommentValue((String)value); break;
				} else {
					cr.setValue((CaNumber)value); break;
				}
			}
			}
		}
	}

    /**
     * @author Rainer Schwarze
     *
     */
    public static class TableRenderer extends DefaultTableCellRenderer {
		/** */
		private static final long serialVersionUID = 1L;

		Border borderSimpleStyle;
		Border borderExtendedStyle;
		RendererEditorColumnAccessor columnAccessor;
		CaNumberFormatterContext numFmtCtx;
		CaDoubleFormatter numFmt;
		Border badBorder;

		/**
		 * @param columnAccessor 
		 * 
		 */
		public TableRenderer(RendererEditorColumnAccessor columnAccessor) {
			super();
			this.columnAccessor = columnAccessor;
			borderSimpleStyle = BorderFactory.createMatteBorder(
					0, 0, 1, 0, java.awt.Color.BLACK);
			borderExtendedStyle = BorderFactory.createCompoundBorder( 
					BorderFactory.createCompoundBorder(
							BorderFactory.createMatteBorder(
									0, 0, 1, 0, java.awt.Color.BLACK),
							BorderFactory.createEmptyBorder(
									0, 0, 1, 0)),
							BorderFactory.createMatteBorder(
									1, 0, 1, 0, java.awt.Color.BLACK));
			badBorder = BorderFactory.createMatteBorder(
					2, 0, 2, 0, java.awt.Color.RED);
			numFmt = new CaDoubleFormatter();
		}

		/**
		 * @param table
		 * @param value
		 * @param isSelected
		 * @param hasFocus
		 * @param row
		 * @param column
		 * @return	Returns the renderer component
		 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
		 */
		@Override
		public Component getTableCellRendererComponent(
				JTable table, Object value, 
				boolean isSelected, boolean hasFocus, 
				int row, int column) {
			Component c;
			Object o = columnAccessor.getRendererValueAt(value, column);
			if (o instanceof CaDouble) {
				o = ((CalcRowBase)value).getValueRenderString(numFmt);
//				CaDouble o2 = (CaDouble)o;
//				if (numFmtCtx!=null) {
//					String s = o2.formatNumber(numFmtCtx);
//					o = s;
//				}
			}
			if (column==CalcRowColumnAccessor.COL_SUBEXPR) {
				o = ((CalcRowBase)value).getSubExpressionRenderString(numFmt);
//				if (value instanceof CalcRowOp) {
//					CalcRowOp cr = (CalcRowOp)value;
//					String subEx = cr.getSubExpr();
//					if (subEx!=null) { 
//						String s2 = subEx;
//						CaNumber lastR = cr.getLastResultValue();
//						if (s2.contains("ans")) {
//							s2 = s2.replaceAll(
//									"ans", 
//									lastR.formatNumber(numFmtCtx).trim());
//						}
//						if (s2.contains("%")) {
//							if (s2.startsWith("ex")) {
//								s2 = s2 + " : " + lastR.formatNumber(numFmtCtx).trim();
//							} else {
//								s2 = s2 + " x " + lastR.formatNumber(numFmtCtx).trim();
//							}
//						}
//						o = s2;
//					}
//				}
			}

			c = super.getTableCellRendererComponent(
					table, o, isSelected, hasFocus,
					row, column);
			switch (column) {
			case CalcRowColumnAccessor.COL_CMT: 
				this.setHorizontalAlignment(SwingConstants.LEFT); break;
			case CalcRowColumnAccessor.COL_OP: 
				this.setHorizontalAlignment(SwingConstants.CENTER); break;
			case CalcRowColumnAccessor.COL_VALUE: 
				this.setHorizontalAlignment(SwingConstants.RIGHT); break;
			case CalcRowColumnAccessor.COL_SUBEXPR: 
				this.setHorizontalAlignment(SwingConstants.LEFT); break;
			default:
				this.setHorizontalAlignment(SwingConstants.LEFT); break;
			}
//			if (value instanceof SimpleCell) {
//				((JLabel)c).setBorder(borderSimpleStyle);
//			}
//			System.out.println("having " + 
//					((value!=null) ? value.getClass().getName() : "<null>") + 
//					" at " +
//					" row=" + row + " col=" + column);
			if (value instanceof CalcRowResult) {
				((JLabel)c).setBorder(borderExtendedStyle);
			}
			if (((CalcRowBase)value).getStatus()==CalcRowBase.BAD && !hasFocus) {
				((JLabel)c).setBorder(badBorder);
			} else {
				// c.setBackground(stdColor);
			}
			return c;
		}

		/**
		 * @return Returns the numFmtCtx.
		 */
		public CaNumberFormatterContext getNumFmtCtx() {
			return numFmtCtx;
		}

		/**
		 * @param numFmtCtx The numFmtCtx to set.
		 */
		public void setNumFmtCtx(CaNumberFormatterContext numFmtCtx) {
			this.numFmtCtx = numFmtCtx;
			if (numFmtCtx!=null) {
				numFmt = (CaDoubleFormatter)numFmtCtx.get(CaDouble.class);
			}
		}
    	
    }

    /**
     * @author Rainer Schwarze
     */
    public class TableEditor extends DefaultCellEditor 
    /*implements TableCellEditor*/ {
		/** */
		private static final long serialVersionUID = 1L;

		CaDoubleFormatter doubleFormatter;
		JTextField cellEditor;
		CalcRowBase lastValue;
		int lastColumn;
		RendererEditorColumnAccessor columnAccessor;
		CaNumberFormatterContext numFmtCtx;

		/**
		 * @param columnAccessor 
		 * 
		 */
		public TableEditor(RendererEditorColumnAccessor columnAccessor) {
			super(new JTextField());
			this.columnAccessor = columnAccessor;
			// cellEditor = new JTextField();
			cellEditor = (JTextField)this.editorComponent;
			lastValue = null;
			lastColumn = -1;
			doubleFormatter = new CaDoubleFormatter();
			// this.setClickCountToStart(1);
		}

		/**
		 * @return	Returns the cell editor value
		 * @see javax.swing.CellEditor#getCellEditorValue()
		 */
		@Override
		public Object getCellEditorValue() {
			return cellEditor.getText();
		}

		/**
		 * @param table
		 * @param value
		 * @param isSelected
		 * @param row
		 * @param column
		 * @return	Returns the component
		 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
		 */
		@Override
		public Component getTableCellEditorComponent(
				JTable table, Object value, 
				boolean isSelected, int row, int column) {
			lastValue = (CalcRowBase)value;
			lastColumn = column;
			Object o;
			String s;
			o = columnAccessor.getEditorValueAt(lastValue, column);
			if (o instanceof String) {
				s = (String)o;
			} else if (o instanceof CaNumber) {
				s = doubleFormatter.formatNumber((CaNumber)o);
			} else {
				s = "";
			}
			cellEditor.setText(s.trim());
			return cellEditor;
		}

		/**
		 * 
		 * @see javax.swing.AbstractCellEditor#cancelCellEditing()
		 */
		@Override
		public void cancelCellEditing() {
			super.cancelCellEditing();
			// may be called even if no edit is in progress
			lastValue = null;
			lastColumn = -1;
		}

		/**
		 * @return	Returns true, if stop cell editing is successful.
		 * @see javax.swing.AbstractCellEditor#stopCellEditing()
		 */
		@Override
		public boolean stopCellEditing() {
			if (lastValue!=null) {
				String s = cellEditor.getText();
				Object o = s;
				if (lastColumn==CalcRowColumnAccessor.COL_VALUE && 
						!(lastValue instanceof CalcRowComment)) {
					CaNumber num = (CaNumber)columnAccessor.getEditorValueAt(
							lastValue, lastColumn);
					try {
						doubleFormatter.parseNumber(s, num);
						o = num;
					} catch (NumberFormatException e) {
						CalcProtocolTabular.this.setStatusText("number format error");
						return false;
					}
				}
				// from now on, it is dirty:
				CalcProtocolTabular.this.setDirty(true);
				columnAccessor.setEditorValueAt(lastValue, o, lastColumn);
				
				lastValue = null;
				lastColumn = -1;
			}
			return super.stopCellEditing();
		}

		/**
		 * @return Returns the numFmtCtx.
		 */
		public CaNumberFormatterContext getNumFmtCtx() {
			return numFmtCtx;
		}

		/**
		 * @param numFmtCtx The numFmtCtx to set.
		 */
		public void setNumFmtCtx(CaNumberFormatterContext numFmtCtx) {
			this.numFmtCtx = numFmtCtx;
			this.doubleFormatter = (CaDoubleFormatter)numFmtCtx.get(CaDouble.class);
		}
    	
    }
	
	
	/**
	 * @author Rainer Schwarze
	 */
	public static class CalcSequence {
		ArrayList<CalcRowBase> rows;

		/**
		 * 
		 */
		public CalcSequence() {
			super();
			rows = new ArrayList<CalcRowBase>();
		}

		/**
		 * @return	Returns the List of rows.
		 */
		public List<CalcRowBase> getRowsList() {
			return rows;
		}


		/**
		 * @param o
		 * @return	Returns true, if success
		 * @see java.util.ArrayList#add(Object)
		 */
		public boolean add(CalcRowBase o) {
			return rows.add(o);
		}

		/**
		 * 
		 */
		public void clear() {
			rows.clear();
		}
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static class CalcSequenceExportTxt {
		CalcSequence calcSeq;
		CaNumberFormatterContext numFmtCtx;

		/**
		 * @param calcSeq 
		 * @param numFmtCtx 
		 * 
		 */
		public CalcSequenceExportTxt(
				CalcSequence calcSeq, CaNumberFormatterContext numFmtCtx) {
			super();
			this.calcSeq = calcSeq;
			this.numFmtCtx = numFmtCtx;
		}

		/**
		 * @return	Returns the String value of the text representation.
		 */
		public String createExportString() {
			StringBuffer sb = new StringBuffer();
			CaDoubleFormatter fmt = (CaDoubleFormatter)numFmtCtx.get(CaDouble.class);

			sb.append("Comment\tOp\tSub-Expression\tValue\n");
			for (CalcRowBase base : calcSeq.getRowsList()) {
				if (base.getComment()!=null) {
					sb.append(base.getCommentRenderString());
				}
				sb.append('\t');
				if (base.getOperation()!=null) {
					sb.append(base.getOperationRenderString());
				}
				sb.append('\t');
				if (base.getSubExpr()!=null) {
					sb.append(base.getSubExpressionRenderString(fmt));
				}
				sb.append('\t');
				if (base.getValue()!=null) {
					sb.append(base.getValueRenderString(fmt));
				}
				sb.append(strLF);
			}
			
			return sb.toString();
		}
	}
	
	/**
	 * @author Rainer Schwarze
	 */
	public static class CalcRowBase {
		boolean editable;
		String operation;
		String subExpr;
		CaNumber value;
		CaNumber resultValue;
		String comment;
		CaNumber lastResultValue;
		String commentValue;	// the value field if it is a comment row

		/** row is ok. */
		public final static int OK = 0;
		/** row is bad. */
		public final static int BAD = 1;
		// status?
		int status = OK;

		/**
		 * 
		 */
		public CalcRowBase() {
			super();
		}

		/**
		 * @return	Returns the render string for this field.
		 */
		public String getCommentRenderString() {
			return getComment();
		}
		/**
		 * @return	Returns the render string for this field.
		 */
		public String getOperationRenderString() {
			return getOperation();
		}
		/**
		 * @param numFmt 
		 * @return	Returns the render string for this field.
		 */
		public String getSubExpressionRenderString(CaDoubleFormatter numFmt) {
			if (this instanceof CalcRowOp) {
				String subEx = this.getSubExpr();
				if (subEx!=null) { 
					String s2 = subEx;
					CaNumber lastR = this.getLastResultValue();
					if (s2.contains("ans")) {
						s2 = s2.replaceAll(
								"ans", 
								numFmt.formatNumber(lastR).trim());
					}
					if (s2.contains("%")) {
						String op = getOperation();
						if (op==null) op = "";
						if (op.equals("+") || op.equals("-")) {
							if (s2.startsWith("ex")) {
								s2 = s2 + " : " + numFmt.formatNumber(lastR).trim();
							} else {
								s2 = s2 + " x " + numFmt.formatNumber(lastR).trim();
							}
						} else {
							// stay same ("10%" -> "10%")
						}
					}
					subEx = s2;
				}
				return subEx;
			} else {
				return getSubExpr();
			}
		}
		/**
		 * @param numFmt 
		 * @return	Returns the render string for this field.
		 */
		public String getValueRenderString(CaDoubleFormatter numFmt) {
			if (this instanceof CalcRowComment) {
				return getCommentValue();
			} else {
				String s;
				CaDouble v = (CaDouble)this.getValue();
				s = numFmt.formatNumber(v).trim();
				return s;
			}
		}

		/**
		 * @return Returns the editable.
		 */
		public boolean isEditable() {
			return editable;
		}
		/**
		 * @param editable The editable to set.
		 */
		public void setEditable(boolean editable) {
			this.editable = editable;
		}
		/**
		 * @return Returns the operation.
		 */
		public String getOperation() {
			return operation;
		}
		/**
		 * @param operation The operation to set.
		 */
		public void setOperation(String operation) {
			this.operation = operation;
		}
		/**
		 * @return Returns the subExpr.
		 */
		public String getSubExpr() {
			return subExpr;
		}
		/**
		 * @param subExpr The subExpr to set.
		 */
		public void setSubExpr(String subExpr) {
			this.subExpr = subExpr;
		}
		/**
		 * @return Returns the value.
		 */
		public CaNumber getValue() {
			return value;
		}
		/**
		 * @param value The value to set.
		 */
		public void setValue(CaNumber value) {
			this.value = value;
		}
		/**
		 * @return Returns the accuValue.
		 */
		public CaNumber getResultValue() {
			return resultValue;
		}
		/**
		 * @param accuValue The accuValue to set.
		 */
		public void setResultValue(CaNumber accuValue) {
			this.resultValue = accuValue;
		}
		/**
		 * @return Returns the comment.
		 */
		public String getComment() {
			return comment;
		}
		/**
		 * @param comment The comment to set.
		 */
		public void setComment(String comment) {
			this.comment = comment;
		}
		/**
		 * @return Returns the lastResultValue.
		 */
		public CaNumber getLastResultValue() {
			return lastResultValue;
		}
		/**
		 * @param lastResultValue The lastResultValue to set.
		 */
		public void setLastResultValue(CaNumber lastResultValue) {
			this.lastResultValue = lastResultValue;
		}
		/**
		 * @return	Returns a String representation of this object.
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			String s = this.getClass().getSimpleName();
			s += " ed=" + editable;
			s += " op=" + operation;
			s += " sx=" + subExpr;
			s += " va=" + ((value!=null) ? value.toString() : "./.");
			s += " rv=" + ((resultValue!=null) ? resultValue.toString() : "./.");
			s += " co=" + comment;
			s += " lr=" + ((lastResultValue!=null) ? lastResultValue.toString() : "./.");
			return s.toString();
		}
		/**
		 * @return Returns the status.
		 */
		public int getStatus() {
			return status;
		}
		/**
		 * @param status The status to set.
		 */
		public void setStatus(int status) {
			this.status = status;
		}
		/**
		 * @return Returns the commentValue.
		 */
		public String getCommentValue() {
			return commentValue;
		}
		/**
		 * @param commentValue The commentValue to set.
		 */
		public void setCommentValue(String commentValue) {
			this.commentValue = commentValue;
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public static class CalcRowOp extends CalcRowBase {
		/**
		 * 
		 */
		public CalcRowOp() {
			super();
			setOperation("");
			setValue(new CaDouble());
		}
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static class CalcRowResult extends CalcRowBase {

		/**
		 * 
		 */
		public CalcRowResult() {
			super();
			setOperation("=");
			setValue(new CaDouble());
		}
		
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static class CalcRowComment extends CalcRowBase {

		/**
		 * 
		 */
		public CalcRowComment() {
			super();
		}
		
	}

	/**
	 * @param name
	 * @return	Returns a newly created instance of ImageIcon for the given 
	 * 			icon resource. 
	 */
	public ImageIcon loadIcon(String name) {
		String rname = "de/admadic/calculator/ui/res/" + name;
		java.net.URL url = this.getClass().getClassLoader().getResource(
				rname);
		if (url==null) {
			return null;
		}
		Image img = Toolkit.getDefaultToolkit().getImage(url);
		if (img==null) {
			return null;
		}
		return new ImageIcon(img);
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static abstract class AbstractProtocolAction extends AbstractAction {

		/**
		 */
		public AbstractProtocolAction() {
			super();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class UpdateAction extends AbstractProtocolAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public UpdateAction() {
			super();
			putValue(Action.NAME, "Update");
			putValue(Action.SHORT_DESCRIPTION, "Update the protocols calculations.");
			putValue(Action.SMALL_ICON, CalcProtocolTabular.this.loadIcon("btn-prot-update.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			// call the update
			CalcProtocolTabular.this.updateProtocolCalculation();
			CalcProtocolTabular.this.refreshDisplay();
		}
	}
	
	/**
	 * @author Rainer Schwarze
	 */
	public class DeleteAction extends AbstractProtocolAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public DeleteAction() {
			super();
			putValue(Action.NAME, "Delete");
			putValue(Action.SHORT_DESCRIPTION, "Delete the selected row.");
			putValue(Action.SMALL_ICON, CalcProtocolTabular.this.loadIcon("btn-prot-clrll.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			CalcProtocolTabular.this.removeSelectedRows();
		}
	}
	
	/**
	 * @author Rainer Schwarze
	 */
	public class InsertAction extends AbstractProtocolAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public InsertAction() {
			super();
			putValue(Action.NAME, "Insert");
			putValue(Action.SHORT_DESCRIPTION, "Insert a row.");
			putValue(Action.SMALL_ICON, CalcProtocolTabular.this.loadIcon("btn-prot-addline.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			CalcProtocolTabular.this.insertRowOp();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class InsertResultAction extends AbstractProtocolAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public InsertResultAction() {
			super();
			putValue(Action.NAME, "Insert Result");
			putValue(Action.SHORT_DESCRIPTION, "Insert a result row.");
			putValue(Action.SMALL_ICON, CalcProtocolTabular.this.loadIcon("btn-prot-addres.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			CalcProtocolTabular.this.insertRowResult();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class InsertCommentAction extends AbstractProtocolAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public InsertCommentAction() {
			super();
			putValue(Action.NAME, "Insert Comment");
			putValue(Action.SHORT_DESCRIPTION, "Insert a comment row.");
			putValue(Action.SMALL_ICON, CalcProtocolTabular.this.loadIcon("btn-prot-addcmt.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			CalcProtocolTabular.this.insertRowComment();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class CopyAction extends AbstractProtocolAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public CopyAction() {
			super();
			putValue(Action.NAME, "Copy");
			putValue(Action.SHORT_DESCRIPTION, "Copy to clipboard.");
			putValue(Action.SMALL_ICON, CalcProtocolTabular.this.loadIcon("btn-copy.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			CalcProtocolTabular.this.copyData();
		}
	}
	
	/**
	 * @author Rainer Schwarze
	 */
	public class PrintAction extends AbstractProtocolAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public PrintAction() {
			super();
			putValue(Action.NAME, "Print");
			putValue(Action.SHORT_DESCRIPTION, "Print the protocol.");
			putValue(Action.SMALL_ICON, CalcProtocolTabular.this.loadIcon("btn-print.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			CalcProtocolTabular.this.printData();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class MoveUpAction extends AbstractProtocolAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public MoveUpAction() {
			super();
			putValue(Action.NAME, "Up");
			putValue(Action.SHORT_DESCRIPTION, "Move the line upwards.");
			putValue(Action.SMALL_ICON, CalcProtocolTabular.this.loadIcon("btn-arw-up.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			CalcProtocolTabular.this.moveUp();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class MoveDownAction extends AbstractProtocolAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public MoveDownAction() {
			super();
			putValue(Action.NAME, "Down");
			putValue(Action.SHORT_DESCRIPTION, "Move the line downwards.");
			putValue(Action.SMALL_ICON, CalcProtocolTabular.this.loadIcon("btn-arw-down.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			CalcProtocolTabular.this.moveDown();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class ClearAllAction extends AbstractProtocolAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public ClearAllAction() {
			super();
			putValue(Action.NAME, "Clear All");
			putValue(Action.SHORT_DESCRIPTION, "Clear the protocol.");
			putValue(Action.SMALL_ICON, CalcProtocolTabular.this.loadIcon("btn-prot-clr.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) { 
			CalcProtocolTabular.this.clearSequence();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SaveAction extends AbstractProtocolAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public SaveAction() {
			super();
			putValue(Action.NAME, "Save");
			putValue(Action.SHORT_DESCRIPTION, "Save the protocol.");
			putValue(Action.SMALL_ICON, CalcProtocolTabular.this.loadIcon("btn-save.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			CalcProtocolTabular.this.saveProtocolTxt();
		}
	}

	/**
	 * @return Returns the processorCalculation.
	 */
	public IProcessorCalculation getProcessorCalculation() {
		return processorCalculation;
	}

	/**
	 * 
	 */
	public void printData() {
		if (calcSequence.getRowsList().size()<1) {
			setStatusText("Nothing to print.");
			return;
		}
		ProtocolPrintStringRenderer ppsr = new ProtocolPrintStringRenderer(
				calcSequence,
				numFmtCtx);
		String data = ppsr.createString();
		StringPrinter sp = new StringPrinter((JFrame)null, data);
		sp.doPrint(false);
	}

	/**
	 * 
	 */
	public void copyData() {
		TabularCopyPasteCore cp = new TabularCopyPasteCore(
				columnAccessor, numFmtCtx);
		cp.doCopyFrom(tableProtocol);
	}

	/**
	 * 
	 */
	public void cancelCellEditing() {
		if (tableProtocol==null) return;
		TableCellEditor ce = tableProtocol.getCellEditor();
		if (ce!=null) {
			ce.cancelCellEditing();
		}
	}
	
	/**
	 * 
	 */
	public void insertRowComment() {
		int insidx = tableProtocol.getSelectedRow();
		if (insidx<0) {
			insidx = tableProtocol.getRowCount();
		}
		CalcRowComment cr = new CalcRowComment();
		calcSequence.getRowsList().add(insidx, cr);
		tableModel.fireTableRowsInserted(insidx, insidx);
	}

	/**
	 * 
	 */
	public void insertRowResult() {
		int insidx = tableProtocol.getSelectedRow();
		if (insidx<0) {
			insidx = tableProtocol.getRowCount();
		}
		CalcRowResult cr = new CalcRowResult();
		calcSequence.getRowsList().add(insidx, cr);
		tableModel.fireTableRowsInserted(insidx, insidx);
	}

	/**
	 * 
	 */
	public void insertRowOp() {
		int insidx = tableProtocol.getSelectedRow();
		if (insidx<0) {
			insidx = tableProtocol.getRowCount();
		}
		CalcRowOp cr = new CalcRowOp();
		//cr.setOperation("+");
		//cr.setValue(new CaDouble());
		calcSequence.getRowsList().add(insidx, cr);
		tableModel.fireTableRowsInserted(insidx, insidx);
	}

	/**
	 * 
	 */
	public void removeSelectedRows() {
		int [] sels = tableProtocol.getSelectedRows();
		if (sels==null) return; // should not happen
		if (sels.length==0) {
			setStatusText("cannot delete rows: no rows selected");
			return;
		}
		int result = JOptionPane.showConfirmDialog(
				null,
				"Please confirm, that the selected rows shall be deleted.\n"+
				"Do you want to delete the selected rows?",
				"Confirm delete rows",
				JOptionPane.YES_NO_OPTION);
		if (result!=JOptionPane.YES_OPTION) return;

		cancelCellEditing();

		if (sels.length==1) {
			calcSequence.getRowsList().remove(sels[0]);
			tableModel.fireTableRowsDeleted(sels[0], sels[0]);
		} else {
			for (int i=sels.length-1; i>=0; i--) {
				calcSequence.getRowsList().remove(sels[i]);
			}
			tableModel.fireTableDataChanged();
		}
	}

	/**
	 * 
	 */
	public void moveDown() {
		int rowCnt = tableProtocol.getRowCount();
		int row = tableProtocol.getSelectedRow();
		if (row<0) {
			setStatusText("Must select a row for move down.");
			return;
		}
		if (row>=rowCnt-1) {
			// ignore
			return;
		}

		cancelCellEditing();

		Collections.swap(calcSequence.getRowsList(), row, row+1);
		tableModel.fireTableRowsUpdated(row, row+1);
		tableProtocol.setRowSelectionInterval(row+1, row+1);
	}

	/**
	 * 
	 */
	public void moveUp() {
		int row = tableProtocol.getSelectedRow();
		if (row<0) {
			setStatusText("Must select a row for move up.");
			return;
		}
		if (row<1) {
			// ignore
			return;
		}

		cancelCellEditing();

		Collections.swap(calcSequence.getRowsList(), row, row-1);
		tableModel.fireTableRowsUpdated(row-1, row);
		tableProtocol.setRowSelectionInterval(row-1, row-1);
	}

	/**
	 * 
	 */
	public void clearSequence() {
		int rows = calcSequence.getRowsList().size();
		if (rows<1) return;

		int res = JOptionPane.showConfirmDialog(
				null, 
				"Do you wish to clear the protocol completely?",
				"Confirm clearing protocol",
				JOptionPane.YES_NO_OPTION);
		if (res!=JOptionPane.YES_OPTION) return;

		cancelCellEditing();
		
		calcSequence.clear();
		tableModel.fireTableRowsDeleted(0, rows-1);
		refreshDisplay();
	}

	/**
	 * @param processorCalculation The processorCalculation to set.
	 */
	public void setProcessorCalculation(IProcessorCalculation processorCalculation) {
		this.processorCalculation = processorCalculation;
		if (actionUpdate!=null) {
			actionUpdate.setEnabled(processorCalculation!=null);
		}
	}

	/**
	 * @return Returns the dirty.
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @param dirty The dirty to set.
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		setStatusText(null); // update
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public class CopyPasteCore {
		Clipboard clipboard;
		
		/**
		 * 
		 */
		public CopyPasteCore() {
			super();
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		}
		
		/**
		 * @param tf
		 */
		public void doCopyFrom(JTextField tf) {
			StringBuffer sb = new StringBuffer();
			sb.append(tf.getText());
			StringSelection ss = new StringSelection(sb.toString());
			clipboard.setContents(ss, ss);
		}
		
		/**
		 * @param tf 
		 */
		public void doPasteInto(JTextField tf) {
			try {
				String trS = (String) (clipboard.getContents(this)
						.getTransferData(DataFlavor.stringFlavor));
				if (tf.isEditable()) {
					tf.setText(trS);
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}
	
	/**
	 * @author Rainer Schwarze
	 * 
	 */
	public class TabularCopyPasteCore {
		Clipboard clipboard;
		AbstractRendererEditorColumnAccessor columnAccessor;
		CaNumberFormatterContext numFmtCtx;
		CaDoubleFormatter numFmt;
		
		/**
		 * @param columnAccessor 
		 * @param numFmtCtx
		 * 
		 */
		public TabularCopyPasteCore(
				AbstractRendererEditorColumnAccessor columnAccessor,
				CaNumberFormatterContext numFmtCtx) {
			super();
			this.columnAccessor = columnAccessor;
			this.numFmtCtx = numFmtCtx;
			if (numFmtCtx!=null) {
				numFmt = (CaDoubleFormatter)numFmtCtx.get(CaDouble.class);
			} else {
				numFmt = new CaDoubleFormatter();
			}
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		}
		
		/**
		 * @param table
		 */
		public void doCopyFrom(JTable table) {
			StringBuffer sb = new StringBuffer();
			
			// we need a contiguous block
			int numrows = table.getSelectedRowCount();
			int[] rowssel = table.getSelectedRows();
			// int numcols = table.getSelectedColumnCount();
			// int [] colssel = table.getSelectedColumns();
			
			int selrowcnt = 0;
			if (rowssel.length > 0) {
				if (rowssel.length == 1) {
					selrowcnt = 1;
				} else {
					selrowcnt += rowssel[rowssel.length - 1] - rowssel[0] + 1;
				}
			}
			// if no row is selected, select all rows:
			if (numrows == 0) {
				numrows = table.getRowCount();
				rowssel = new int[numrows];
				for (int i = 0; i < rowssel.length; i++) {
					rowssel[i] = i;
				}
				selrowcnt = numrows;
			}
			if ((numrows == rowssel.length) && (numrows == selrowcnt)) {
				// ok.
			} else {
				JOptionPane
						.showMessageDialog(
								null, 
								"Not a continuous selection.",
								"Invalid selection for Copy",
								JOptionPane.ERROR_MESSAGE);
				return;
			}

			// we want only the first 4 columns:
			// int colcount = 4; //table.getColumnCount();
			for (int i = 0; i < rowssel.length; i++) {
				Object o = table.getValueAt(rowssel[i], 0);
				// System.out.println("row: " + o.getClass().getName());
				CalcRowBase cr = (CalcRowBase)o;
				String s;

				s = cr.getCommentRenderString();
				sb.append((s!=null) ? s : "");
				sb.append("\t");

				s = cr.getOperationRenderString();
				sb.append((s!=null) ? s : "");
				sb.append("\t");

				s = cr.getSubExpressionRenderString(numFmt);
				sb.append((s!=null) ? s : "");
				sb.append("\t");

				s = cr.getValueRenderString(numFmt);
				sb.append((s!=null) ? s : "");

				sb.append(strLF);

//				for (int j = 0; j < colcount; j++) {
//					Object o2 = columnAccessor.getRendererValueAt(o, j);
//					String s;
//					if (o2==null) {
//						s = "";
//					} else if (o2 instanceof String) {
//						s = (String)o2;
//					} else if (o2 instanceof CaDouble) {
//						s = numFmt.formatNumber((CaDouble)o2);
//					} else {
//						s = o2.toString();
//					}
//					if (j==CalcRowColumnAccessor.COL_SUBEXPR) {
//						String subEx = s;
//						if (subEx!=null && subEx.contains("%")) {
//							String s2;
//							CaNumber lastR = (CaNumber)columnAccessor.getRendererValueAt(
//									o, CalcRowColumnAccessor.COL_LASTSUBRESULT);
//							if (lastR==null) {
//								System.out.println("Warning: no last sub result!");
//								// FIXME: what then?
//								lastR = new CaDouble();
//							}
//							if (subEx.startsWith("ex")) {
//								s2 = subEx + " : " + numFmt.formatNumber(lastR).trim();
//							} else {
//								s2 = subEx + " x " + numFmt.formatNumber(lastR).trim();
//							}
//							s = s2;
//						}
//					}
//					sb.append(s);
//					if (j != colcount - 1)
//						sb.append("\t");
//				}
//				sb.append("\n");
			}

			StringSelection ss = new StringSelection(sb.toString());
			clipboard.setContents(ss, ss);
		}

		/**
		 * DONT CALL THAT HERE!
		 * 
		 * @param table
		 * @param addRows
		 */
		public void doPasteInto(JTable table, boolean addRows) {
			// DONT CALL THAT HERE!
//			GenericObjectCreator goc = new GenericObjectCreator();
//			int[] rowssel = table.getSelectedRows();
//			int[] colssel = table.getSelectedColumns();
//
//			int startRow = rowssel.length > 0 ? rowssel[0] : 0;
//			int startCol = colssel.length > 0 ? colssel[0] : 0;
//
//			TableModel tm = table.getModel();
//			TableModelClipboardReceiver tmcr = null;
//			if (tm instanceof TableModelClipboardReceiver) {
//				tmcr = (TableModelClipboardReceiver) tm;
//			}
//
//			try {
//				String trS = (String) (clipboard.getContents(this)
//						.getTransferData(DataFlavor.stringFlavor));
//
//				StringTokenizer stlf = new StringTokenizer(trS, "\n");
//				for (int i = 0; stlf.hasMoreTokens(); i++) {
//					String rowS = stlf.nextToken();
//					StringTokenizer sttab = new StringTokenizer(rowS, "\t");
//					for (int j = 0; sttab.hasMoreTokens(); j++) {
//						String value = sttab.nextToken();
//						if (startRow + i >= table.getRowCount()) {
//							if (!addRows)
//								continue;
//						}
//						if (startCol + j >= table.getColumnCount()) {
//							continue;
//						}
//
//						if (!table.isCellEditable(startRow + i, startCol + j)) {
//							continue;
//						}
//						if (tmcr != null) {
//							tmcr.setStringValueAt(value, startRow + i, startCol
//									+ j);
//						} else {
//							Object hint;
//							Object newval;
//							hint = table.getValueAt(startRow + i, startCol + j);
//							if (!(hint instanceof String)) {
//								newval = goc.valueOf(hint, value);
//							} else {
//								newval = value;
//							}
//							if (newval == null)
//								continue;
//							table
//									.setValueAt(newval, startRow + i, startCol
//											+ j);
//						}
//					}
//				}
//
//			} catch (Exception e) {
//				// e.printStackTrace();
//				// FIXME: any message here on error?
//			}
		}
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public class ProtocolPrintStringRenderer {
		CalcSequence calcSeq;
		CaNumberFormatterContext numFmtCtx;
		CaDoubleFormatter numFmt;
		int [] fieldWidths = {20, 6, 20, 15};	// 61
		int pageWidth = 61;
		final static int ALIGN_LEFT = 0;
		final static int ALIGN_CENTER = 1;
		final static int ALIGN_RIGHT = 2;

		/**
		 * @param calcSeq
		 * @param numFmtCtx 
		 */
		public ProtocolPrintStringRenderer(
				CalcSequence calcSeq,
				CaNumberFormatterContext numFmtCtx) {
			super();
			this.calcSeq = calcSeq;
			this.numFmtCtx = numFmtCtx;
			if (numFmtCtx==null) {
				numFmt = new CaDoubleFormatter();
			} else {
				numFmt = (CaDoubleFormatter)numFmtCtx.get(CaDouble.class);
			}
		}

		/**
		 * @return	Returns a String representation
		 */
		public String createString() {
			StringBuffer sb = new StringBuffer("");

			String cmt;
			String op;
			String subEx;
			String val;
			CaDouble value;
			for (CalcRowBase base : calcSeq.getRowsList()) {
				cmt = base.getComment();
				op = base.getOperation();
				subEx = base.getSubExpr();
				val = base.getValueRenderString(numFmt);
//				if (base instanceof CalcRowComment) {
//					val = base.getCommentValue();
//					value = null;
//				} else {
//					value = (CaDouble)base.getValue();
//					val = null;
//				}

				StringBuffer line = new StringBuffer("");
				line.append(formatField(cmt, fieldWidths[0], ALIGN_LEFT));
				line.append(formatField(op, fieldWidths[1], ALIGN_CENTER));
				if (subEx==null) {
					subEx = "";
				} else {
					subEx = base.getSubExpressionRenderString(numFmt);
//					if (subEx!=null && subEx.contains("%")) {
//						String s2;
//						CaNumber lastR = base.getLastResultValue();
//						if (lastR==null) {
//							System.out.println("Warning: no last sub result!");
//							// FIXME: what then?
//							lastR = new CaDouble();
//						}
//						if (subEx.startsWith("ex")) {
//							s2 = subEx + " : " + numFmt.formatNumber(lastR).trim();
//						} else {
//							s2 = subEx + " x " + numFmt.formatNumber(lastR).trim();
//						}
//						subEx = s2;
//					}
				}
				line.append(formatField(subEx, fieldWidths[2], ALIGN_LEFT));
//				if (value!=null) {
//					val = numFmt.formatNumber(value);
//				}
				line.append(formatField(val, fieldWidths[3], ALIGN_RIGHT));
				// System.out.println(">" + line + "<");
				line.append(strLF);
				if (base instanceof CalcRowResult) {
					sb.append(StringUtil.fill('-', pageWidth));
					sb.append(strLF);
				}
				sb.append(line);
				if (base instanceof CalcRowResult) {
					sb.append(StringUtil.fill('=', pageWidth));
					sb.append(strLF);
				}
			}

			return sb.toString();
		}

		/**
		 * @param cmt
		 * @param width
		 * @param align 
		 * @return	Returns a String formatted for the fieldWidth
		 */
		private String formatField(String cmt, int width, int align) {
			String s = cmt = (cmt==null) ? "" : cmt;
			switch (align) {
			case ALIGN_LEFT:
				if (cmt.length()<width-1) {
					s = cmt + StringUtil.fill(' ', width-cmt.length());
				} else {
					s = cmt.substring(0, width-4) + "... ";
				}
				break;
			case ALIGN_CENTER:
				int lc, rc;
				if (cmt.length()<width-1) {
					lc = (width - cmt.length()) / 2;
					rc = width - cmt.length() - lc;
					s = StringUtil.fill(' ', lc) + 
						cmt +
						StringUtil.fill(' ', rc);
				} else {
					s = cmt.substring(0, width-4) + "... ";
				}
				break;
			case ALIGN_RIGHT:
				if (cmt.length()<width-1) {
					s = StringUtil.fill(' ', width-cmt.length()) + cmt;
				} else {
					s = " ..." + cmt.substring(
							cmt.length()-width+4, cmt.length());
				}
				break;
			}
			return s;
		}
	}
}
