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
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

import de.admadic.calculator.processor.ProtocolEvent;
import de.admadic.calculator.processor.ProtocolEventListener;
import de.admadic.calculator.types.CaDouble;
import de.admadic.calculator.types.CaDoubleFormatter;
import de.admadic.calculator.types.CaNumber;
import de.admadic.calculator.types.CaNumberFormatterContext;
import de.admadic.util.StringUtil;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcProtocol extends JPanel 
implements ProtocolEventListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JScrollPane scrollPane;
	JTextArea text;
	Font font;
	int columns;
	int opColumns = 6;
	CaNumberFormatterContext numFmtCtx;
	CaDoubleFormatter numFmt;
	String protocolData = "";

	/**
	 * 
	 */
	public CalcProtocol() {
		super();
		numFmt = new CaDoubleFormatter();
		initContents();
	}

	/**
	 * @param numFmtCtx
	 */
	public CalcProtocol(CaNumberFormatterContext numFmtCtx) {
		super();
		this.numFmtCtx = numFmtCtx;
		if (numFmtCtx!=null) {
			numFmt = (CaDoubleFormatter)numFmtCtx.get(CaDouble.class);
		} else {
			numFmt = new CaDoubleFormatter();
		}
		initContents();
	}

	/**
	 * 
	 */
	private void initContents() {
		this.setLayout(new BorderLayout());

		scrollPane = new JScrollPane();
		this.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		text = new JTextArea();

		scrollPane.setViewportView(text);

		font = new Font("Courier New", 0, 12);
		text.setFont(font);
		text.setEditable(false);
		columns = 25;
		text.setColumns(columns);

		scrollPane.setBorder(
				BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		updateMinimumSize();
	}

	/**
	 * 
	 */
	public void updateMinimumSize() {
//		FontMetrics fm = text.getFontMetrics(font);
//		String s = "";
//		for (int i=0; i<columns; i++) {
//			s += (i % 10);
//		}
//		int width = fm.stringWidth(s);
//		int height = fm.getHeight();
		//FIXME: won't scroll with that:
		//text.setMinimumSize(new Dimension(width, height));
		//text.setPreferredSize(new Dimension(width, height));
	}
	
	/**
	 * @return Returns the columns.
	 */
	public int getColumns() {
		return columns;
	}
	/**
	 * @param columns The columns to set.
	 */
	public void setColumns(int columns) {
		this.columns = columns;
		this.text.setColumns(columns);
		updateMinimumSize();
	}
	/**
	 * @return Returns the font.
	 */
	@Override
	public Font getFont() {
		return font;
	}
	/**
	 * @param font The font to set.
	 */
	public void setProtocolFont(Font font) {
		this.font = font;
		this.text.setFont(font);
		updateMinimumSize();
	}

	/**
	 * @param s
	 */
	public void setProtocolText(String s) {
		this.protocolData = s;
		this.text.setText(s);
	}

	/**
	 * @param rows
	 */
	public void setRows(int rows) {
		if (text!=null) {
			text.setRows(rows);
		}
	}


	/**
	 * @param event
	 * @see de.admadic.calculator.processor.ProtocolEventListener#addOp(de.admadic.calculator.processor.ProtocolEvent)
	 */
	public void addOp(ProtocolEvent event) {
		String cmd = event.getOperation();
		String cmddisp = cmd;
		String logstr = "";
		CaDouble value = (CaDouble)event.getValue();

		if (cmd.equals("")) {
			cmddisp = ".";
		}
		String cmddisp2 = cmddisp;
		if (cmddisp2.length()>getOpColumns()) {
			logstr += "\n" + 
				"-> " + cmddisp2 + "\n" +
				(cmd + "                 ").substring(0, getOpColumns()) + " " + 
				((value!=null) ? numFmt.format(value) : "./.");
		} else {
			logstr += "\n" + 
				(cmddisp2 + "            ").substring(0, getOpColumns()) + " " + 
				((value!=null) ? numFmt.format(value) : "./.");
		}
		protocolData += logstr;
		text.append(logstr);
	}


	/**
	 * @param event
	 * @see de.admadic.calculator.processor.ProtocolEventListener#addSubExprOp(de.admadic.calculator.processor.ProtocolEvent)
	 */
	public void addSubExprOp(ProtocolEvent event) {
		String cmd = event.getOperation();
		String cmddisp = cmd;
		String subExpr = event.getSubExpression();
		String logstr = "";
		CaDouble value = (CaDouble)event.getValue();

		if (subExpr!=null) {
			CaNumber lastR = event.getLastSubResult();
			if (subExpr.contains("ans")) {
				subExpr = subExpr.replaceAll(
						"ans", 
						numFmt.formatNumber(lastR).trim());
			}
			logstr += "\n-> " + subExpr;
			if (subExpr.contains("%")) {
				if (subExpr.startsWith("ex")) {
					logstr += " : " + numFmt.formatNumber(lastR).trim();
				} else {
					logstr += " x " + numFmt.formatNumber(lastR).trim();
				}
			}
		}
		String cmddisp2 = cmddisp;
		if (cmddisp2.length()>getOpColumns()) {
			logstr += "\n" + 
				"-> " + cmddisp2 + "\n" +
				(cmd + "                 ").substring(0, getOpColumns()) + " " + 
				((value!=null) ? numFmt.format(value) : "./.");
		} else {
			logstr += "\n" + 
				(cmddisp2 + "            ").substring(0, getOpColumns()) + " " + 
				((value!=null) ? numFmt.format(value) : "./.");
		}
		
		protocolData += logstr;
		text.append(logstr);
	}


	/**
	 * @param event
	 * @see de.admadic.calculator.processor.ProtocolEventListener#addResult(de.admadic.calculator.processor.ProtocolEvent)
	 */
	public void addResult(ProtocolEvent event) {
		String cmd = event.getOperation();
		String logstr = "";
		CaDouble value = (CaDouble)event.getValue();
//		if (cmd.equals(ProcessorAction.PA_EXE)) {
		logstr += "\n" + StringUtil.fill('-', getColumns());
		logstr += "\n" + 
			(cmd + "                  ").substring(0, getOpColumns()) + " " + 
			((value!=null) ? numFmt.format(value) : "./.");
		
		logstr += "\n" + StringUtil.fill('=', getColumns());
		logstr += "\n";
		protocolData += logstr;
		text.append(logstr);
	}


	/**
	 * @param event
	 * @see de.admadic.calculator.processor.ProtocolEventListener#addClear(de.admadic.calculator.processor.ProtocolEvent)
	 */
	public void addClear(ProtocolEvent event) {
		String logstr = "";
		logstr += "\n" + ".clear" + "\n";
		protocolData += logstr;
		text.append(logstr);
	}

	/**
	 * @return Returns the opColumns.
	 */
	public int getOpColumns() {
		return opColumns;
	}

	/**
	 * @param opColumns The opColumns to set.
	 */
	public void setOpColumns(int opColumns) {
		this.opColumns = opColumns;
	}
}
