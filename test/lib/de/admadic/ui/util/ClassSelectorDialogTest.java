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
package de.admadic.ui.util;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import de.admadic.util.JarLister;

/**
 * @author Rainer Schwarze
 *
 */
public class ClassSelectorDialogTest {

	/**
	 * 
	 */
	public ClassSelectorDialogTest() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        new ClassSelectorFrame().setVisible(true);
	}

	/**
	 * 
	 */
	public static class ClassSelectorFrame extends JFrame {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		public ClassSelectorFrame() {
	        //splash.close();
			initComponents();
			ClassSelectorDialog dlg = new ClassSelectorDialog(this);
			JarLister jl = new JarLister();
			try {
				jl.appendList("./libx/kunststoff.jar", null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dlg.addClasses(jl.getClassListDotted());
			dlg.updateControls();
			dlg.setFilter("LookAndFeel");
			dlg.setVisible(true);
	    }
	    private void initComponents() {
	        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        setTitle("Class Selector Test");
	        setSize(400,300);
	        setLocationRelativeTo(null);
	    }
	}

}
