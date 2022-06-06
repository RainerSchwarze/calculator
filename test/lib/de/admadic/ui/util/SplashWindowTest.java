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

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author Rainer Schwarze
 *
 */
public class SplashWindowTest {

	/**
	 * 
	 */
	public SplashWindowTest() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        new SplashWindowFrame().setVisible(true);
	}

	/**
	 * Splashscreen_Test.java
	 */
	public static class SplashWindowFrame extends JFrame {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * 
		 */
		public SplashWindowFrame() {
	        URL url = SplashWindowFrame.class.getResource(
	        		// "splash1.png"
	        		"wainting4.png"
	        		);
	        Icon picture = null;
	        if (url != null){
	            picture = new ImageIcon(url);
	        }       
	        splash = new SplashWindow(
	        		picture, 
	        		null, //"Starting up", 
	        		null, //"SplashWindowTest",
	        		"Registered to: Rainer Schwarze, admaDIC GbR\n"+
	        		"Reg-No: IUERY-DISFK-3T534-98EYS-HDUDI",
	        		143);
	        splash.setDelayedClose(20000);
	        splash.setUserClose(true);
	        splash.setWaitForCompletion(true);
	        splash.setVisible(true); // <- go!
	        try{Thread.sleep(500);}catch(InterruptedException ex){/*nothg*/}
	        splash.updateStatus("Initializing", 30);
	        try{Thread.sleep(500);}catch(InterruptedException ex){/*nothg*/}
	        splash.updateStatus("Loading A", 60);
	        try{Thread.sleep(500);}catch(InterruptedException ex){/*nothg*/}
	        splash.updateStatus("Loading B", 90);
	        try{Thread.sleep(500);}catch(InterruptedException ex){/*nothg*/}
	        initComponents();
	        splash.updateStatus("Init completed", 100);
	        //splash.close();
	    }
	    private void initComponents() {
	        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        setTitle("Splashscreen Test");
	        setSize(400,300);
	        setLocationRelativeTo(null);
	    }
	    private SplashWindow splash;
	}

}
