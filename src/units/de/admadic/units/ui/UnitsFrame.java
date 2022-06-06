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
package de.admadic.units.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import de.admadic.ui.util.FileChooserUtil;
import de.admadic.units.core.UnitManager;
import de.admadic.units.core.UnitsIo;

/**
 * 
 */
public class UnitsFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean DBGtesting = false;

	UnitManager unitManager;
	
	JPanel panelMain;
	ConvertPanel convertPanel;

	/**
	 * 
	 */
	public UnitsFrame() {
		super();
		{
			String tmp;
			tmp = System.getProperty("admadic.testing");
			if (tmp!=null && tmp.toLowerCase().equals("yes")) {
				DBGtesting = true;
			}
		}
		initCore();
		initComponents();
	}
	
	private void initCore() {
		unitManager = new UnitManager();
//		UnitsIo uio = new UnitsIo(unitManager);
//		uio.readFile(new File("./src/de/admadic/units/unitstdset-save.xml"));
	}
	
	private void initComponents() {
		panelMain = (JPanel)this.getContentPane();
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			/**
			 * @param e
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				setVisible(false);
				dispose();
			}
		});

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Actions");
		menuBar.add(menu);
		String [][] nameAction = {
				{ "Load...", "load" },
				{ "Save...", "save" },
				{ "Convert", "convert" },
				{ "Manage", "manage" },
		};
		for (String[] sa : nameAction) {
			JMenuItem mi = new JMenuItem(sa[0]);
			mi.setActionCommand(sa[1]);
			menu.add(mi);
			mi.addActionListener(this);
		}
		this.setJMenuBar(menuBar);
		
		this.pack();
		setLocationRelativeTo(null);
	}

	/**
	 * @param e
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("load")) {
			File file = FileChooserUtil.getFileWithExt(
					this, "xml", FileChooserUtil.OPEN);
			if (file==null) return;
			UnitsIo uio = new UnitsIo(unitManager);
			uio.readFile(file);
		} else if (cmd.equals("save")) {
			File file = FileChooserUtil.getFileWithExt(
					this, "xml", 
					FileChooserUtil.SAVE | FileChooserUtil.ASKOVERWRITE);
			if (file==null) return;
			UnitsIo uio = new UnitsIo(unitManager);
			uio.saveFileUnits(file);
		} else if (cmd.equals("convert")) {
			ConvertFrame f = new ConvertFrame(unitManager);
			f.setVisible(true);
		} else if (cmd.equals("manage")) {
			ManageFrame f = new ManageFrame(unitManager);
			f.setVisible(true);
		} else {
			// nothing
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
		        new UnitsFrame().setVisible(true);
			}
		});
	}
}
