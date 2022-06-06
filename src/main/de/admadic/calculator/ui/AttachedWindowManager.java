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

import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Hashtable;

import de.admadic.cfg.Cfg;

/**
 * @author Rainer Schwarze
 *
 */
public class AttachedWindowManager 
		implements PlacementListener, ComponentListener {

	/**
	 * 
	 * @author Rainer Schwarze
	 *
	 */
	public static class Placer {
		// TODO: add methods as CENTERW, CENTERH
		/**
		 * Method for placement north of main window
		 */
		public final static int NORTH = 1<<0;
		/**
		 * Method for placement west of main window
		 */
		public final static int WEST = 1<<1;
		/**
		 * Method for placement east of main window
		 */
		public final static int EAST = 1<<2;
		/**
		 * Method for placement south of main window
		 */
		public final static int SOUTH = 1<<3;
		/**
		 * Method for placement same width as main window
		 */
		public final static int WIDTH = 1<<4;
		/**
		 * Method for placement same height of main window
		 */
		public final static int HEIGHT = 1<<5;

		int method;
		int offsetX = 0;
		int offsetY = 0;
		int sizeW = 0;
		int sizeH = 0;
		boolean motionLock;

		/**
		 * 
		 */
		public Placer() {
			this(0);
		}

		/**
		 * @param method
		 */
		public Placer(int method) {
			super();
			this.method = method;
			motionLock = false;
		}

		/**
		 * Copy the values from the given Placer to this instance.
		 * @param src
		 */
		public void copyFrom(Placer src) {
			this.method = src.method;
			this.offsetX = src.offsetX;
			this.offsetY = src.offsetY;
			this.sizeW = src.sizeW;
			this.sizeH = src.sizeH;
			this.motionLock = src.motionLock;
		}
		
		/**
		 * Turn on or off placement methods.
		 * @param method
		 * @param flag
		 */
		public void enableMethod(int method, boolean flag) {
			if (flag) {
				this.method |= method;
			} else {
				this.method &= (~method);
			}
		}
		
		/**
		 * @param main
		 * @param window
		 * @return	Returns the bounds of the attached window with new placement
		 */
		public Rectangle place(Rectangle main, Rectangle window) {
			return place(main, window, true);
		}

		/**
		 * @param main
		 * @param window
		 * @param doOffset
		 * @return	Returns the bounds of the attached window with new placement
		 */
		public Rectangle place(Rectangle main, Rectangle window, boolean doOffset) {
			Rectangle r = new Rectangle(window);
			r.x = main.x;
			r.y = main.y;
			if ((method & NORTH)!=0) {
				r.y = main.y - window.height;
			}
			if ((method & WEST)!=0) {
				r.x = main.x - window.width;
			}
			if ((method & SOUTH)!=0) {
				r.y = main.y + main.height;
			}
			if ((method & EAST)!=0) {
				r.x = main.x + main.width;
			}
			if ((method & WIDTH)!=0) {
				r.width = main.width;
			}
			if ((method & HEIGHT)!=0) {
				r.height = main.height;
			}
			if (doOffset) {
				r.x += offsetX;
				r.y += offsetY;
			}
			if (sizeW>0) {
				r.width = sizeW;
			}
			if (sizeH>0) {
				r.height = sizeH;
			}
			return r;
		}

		/**
		 * @param main
		 * @param window
		 */
		public void updateOffset(Rectangle main, Rectangle window) {
			Rectangle r;
			r = place(main, window, false);
			offsetX = window.x - r.x;
			offsetY = window.y - r.y;
			//System.out.println("offset updated: " + offsetX + "," + offsetY);
		}

		/**
		 * @param main
		 * @param window
		 */
		public void updateSize(Rectangle main, Rectangle window) {
			if (main==null) { /* no warn */ }
			sizeW = window.width;
			sizeH = window.height;
		}

		/**
		 * Reset the offset to zero.
		 */
		public void resetOffset() {
			offsetX = 0;
			offsetY = 0;
		}
	
		/**
		 * Reset the size to zero 
		 */
		public void resetSize() {
			sizeW = 0;
			sizeH = 0;
		}
		/**
		 * @param x
		 * @param y
		 */
		public void setOffset(int x, int y) {
			offsetX = x;
			offsetY = y;
		}

		/**
		 * @return Returns the motionLock.
		 */
		public boolean isMotionLock() {
			return motionLock;
		}

		/**
		 * @param motionLock The motionLock to set.
		 */
		public void setMotionLock(boolean motionLock) {
			this.motionLock = motionLock;
		}

		/**
		 * @return	Returns the string representation of this object
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			String s = "";
			s += method;
			s += "," + offsetX;
			s += "," + offsetY;
			s += "," + sizeW;
			s += "," + sizeH;
			s += "," + motionLock;
			//System.out.println("Placer.toString: " + s);
			return s;
		}

		/**
		 * @param s
		 * @return	Returns a Placer generated from the string
		 */
		public static Placer valueOf(String s) {
			Placer p = null;
			String [] sa = s.split(",");
			if (sa.length!=6) return null;
			p = new Placer();
			try {
				//System.out.println("Placer.valueOf: s=" + s + " sa=" + sa);
				p.method = Integer.parseInt(sa[0]);
				p.offsetX = Integer.parseInt(sa[1]);
				p.offsetY = Integer.parseInt(sa[2]);
				p.sizeW = Integer.parseInt(sa[3]);
				p.sizeH = Integer.parseInt(sa[4]);
				p.motionLock = Boolean.parseBoolean(sa[5]);
			} catch (Exception e) {
				// FIXME: make a useful exception with that
				//e.printStackTrace();
				return null;
			}
			return p;
		}
	}

	ArrayList<AttachedWindow> windows;
	Hashtable<AttachedWindow, Placer> placers;
	Window mainWindow;
	Cfg cfg;
	String placerCfgPath;

	boolean enableNotifications;

	/**
	 * @param mainWindow 
	 */
	public AttachedWindowManager(Window mainWindow) {
		this(mainWindow, null);
	}

	/**
	 * @param mainWindow
	 * @param cfg
	 */
	public AttachedWindowManager(Window mainWindow, Cfg cfg) {
		super();
		this.enableNotifications = false;
		this.cfg = cfg;
		this.mainWindow = mainWindow;
		windows = new ArrayList<AttachedWindow>();
		placers = new Hashtable<AttachedWindow, Placer>();
		mainWindow.addComponentListener(this);
	}

	/**
	 * @param enableNotifications
	 */
	public void setEnableNotifications(boolean enableNotifications) {
		this.enableNotifications = enableNotifications;
	}

	/**
	 * @param w
	 * @param placer
	 */
	public void add(AttachedWindow w, Placer placer) {
		if (placer==null) {
			placer = new Placer();
		}
		windows.add(w);
		placers.put(w, placer);
		w.addPlacementListener(this);
	}

	/**
	 * Add the AttachedWindow to north of main window.
	 * @param w
	 */
	public void addNorth(AttachedWindow w) {
		add(w, new Placer(Placer.NORTH));
	}

	/**
	 * Add the AttachedWindow to west of main window.
	 * @param w
	 */
	public void addWest(AttachedWindow w) {
		add(w, new Placer(Placer.WEST));
	}

	/**
	 * Add the AttachedWindow to south of main window.
	 * @param w
	 */
	public void addSouth(AttachedWindow w) {
		add(w, new Placer(Placer.SOUTH));
	}

	/**
	 * Add the AttachedWindow to east of main window.
	 * @param w
	 */
	public void addEast(AttachedWindow w) {
		add(w, new Placer(Placer.EAST));
	}

	/**
	 * Remove the AttachedWindow from the Manager.
	 * @param w
	 */
	public void remove(AttachedWindow w) {
		windows.remove(w);
		w.removePlacementListener(this);
	}

	/**
	 * Enable or disable the given placement method for the 
	 * AttachedWindow specified.
	 * 
	 * @param atw
	 * @param method
	 * @param flag
	 */
	public void enableMethod(AttachedWindow atw, int method, boolean flag) {
		Placer placer = placers.get(atw);
		placer.enableMethod(method, flag);
	}

	/**
	 * @return Returns the placerCfgPath.
	 */
	public String getPlacerCfgPath() {
		return placerCfgPath;
	}

	/**
	 * @param placerCfgPath The placerCfgPath to set.
	 */
	public void setPlacerCfgPath(String placerCfgPath) {
		this.placerCfgPath = placerCfgPath;
	}

	/**
	 * Load the settings of the placers from the configuration.
	 */
	public void loadSettings() {
		if (cfg==null) return;
		for (AttachedWindow atw : windows) {
			if (atw==null || atw.getName()==null || atw.getName().equals(""))
				continue;
			if (placerCfgPath==null) 
				continue;
			Object o = cfg.getValue(
					placerCfgPath + "." + atw.getName() + ".placer", 
					null);
			if (o==null) {
				// not there
				continue;
			}
			if (!(o instanceof Placer)) {
				// error?
				continue;
			}
			Placer p = (Placer)o;

			// FIXME: let it do not load the method.
			// for that copy the method from the existing to the 
			// newly created one, so that it overwrites the loaded one
			p.method = placers.get(atw).method;
			
			placers.get(atw).copyFrom(p);
			//cfg.loadObjectFromPreferences(placers.get(atw));
			//placers.get(atw).loadSettings(cfg, atw.getName());
			//System.out.println("placer: " + placers.get(atw).toString());
		}
	}

	/**
	 * Store the settings of the placers to the configuration.
	 */
	public void storeSettings() {
		if (cfg==null) return;
		for (AttachedWindow atw : windows) {
			if (atw==null || atw.getName()==null || atw.getName().equals(""))
				continue;
			if (placerCfgPath==null) 
				continue;
			cfg.putValue(
					placerCfgPath + "." + atw.getName() + ".placer", 
					placers.get(atw));
			//cfg.storeObjectToPreferences(placers.get(atw));
			//placers.get(atw).storeSettings(cfg, atw.getName());
		}
	}

	/**
	 * Place the specified AttachedWindow with the given placer.
	 * 
	 * @param atw
	 * @param placer
	 */
	public void placeWindow(AttachedWindow atw, Placer placer) {
		Rectangle rMain = mainWindow.getBounds();
		Rectangle rWindow = atw.getBounds();
		if (placer==null) {
			placer = placers.get(atw);
		}
		rWindow = placer.place(rMain, rWindow);
		atw.placeWindow(rWindow);
		storeSettings();
	}

	/**
	 * Place all AttachedWindow's according to the registered placers.
	 */
	public void placeWindows() {
		placeWindows(false);
	}

	/**
	 * @param ignoreMotionLock
	 */
	public void placeWindows(boolean ignoreMotionLock) {
		Rectangle rMain;
		Rectangle rWindow = new Rectangle();
		Placer placer;

		rMain = mainWindow.getBounds();
		for (AttachedWindow atw : windows) {
			rWindow = atw.getBounds(rWindow);
			placer = placers.get(atw);
			if (placer.isMotionLock() && !ignoreMotionLock) {
				placer.updateOffset(rMain, rWindow);
			} else {
				rWindow = placer.place(rMain, rWindow);
				atw.placeWindow(rWindow);
			}
		}
		storeSettings();
	}


	/*
	public void registerConfigurationClients(String path) {
		if (cfg==null) return;
		placerCfgPath = path;
		for (AttachedWindow atw : windows) {
			Placer placer = placers.get(atw);
			String name = atw.getName();
			cfg.registerObject(
					path, name, 
					new String[]{
							"method",
							"offsetX",
							"offsetY",
							"sizeW",
							"sizeH",
							"motionLock",
					},
					placer);
		}
	}
	*/


	/**
	 * @param atw
	 * @see de.admadic.calculator.ui.PlacementListener#notifyMove(de.admadic.calculator.ui.AttachedWindow)
	 */
	public void notifyMove(AttachedWindow atw) {
		if (mainWindow==null) return; // nothing to do!
		if (!enableNotifications) return;
		Rectangle rMain = mainWindow.getBounds();
		Rectangle rWindow = atw.getBounds();
		placers.get(atw).updateOffset(rMain, rWindow);
		storeSettings();
	}

	/**
	 * @param atw
	 * @see de.admadic.calculator.ui.PlacementListener#notifyResize(de.admadic.calculator.ui.AttachedWindow)
	 */
	public void notifyResize(AttachedWindow atw) {
		if (mainWindow==null) return; // nothing to do!
		if (!enableNotifications) return;
		Rectangle rMain = mainWindow.getBounds();
		Rectangle rWindow = atw.getBounds();
		placers.get(atw).updateSize(rMain, rWindow);
		storeSettings();
	}

	/**
	 * @param atw
	 * @see de.admadic.calculator.ui.PlacementListener#snap(de.admadic.calculator.ui.AttachedWindow)
	 */
	public void snap(AttachedWindow atw) {
		Placer placer = placers.get(atw);
		placer.resetOffset();
		placer.resetSize();
		placeWindow(atw, placer);
	}

	/**
	 * Snap all attached windows to the main window
	 */
	public void snapAll() {
		for (AttachedWindow atw : windows) {
			snap(atw);
		}
	}

	/**
	 * @param atw
	 * @param motionLock
	 * @see de.admadic.calculator.ui.PlacementListener#setMotionLock(de.admadic.calculator.ui.AttachedWindow, boolean)
	 */
	public void setMotionLock(AttachedWindow atw, boolean motionLock) {
		Placer placer = placers.get(atw);
		placer.setMotionLock(motionLock);
	}

	/**
	 * @param atw
	 * @return	Returns true, if the motionLock for that window is on.
	 */
	public boolean isMotionLock(AttachedWindow atw) {
		Placer placer = placers.get(atw);
		return placer.isMotionLock();
	}

	/**
	 * Sets motion lock for all attached windows.
	 * @param motionLock
	 */
	public void setMotionLockForAll(boolean motionLock) {
		for (AttachedWindow atw : windows) {
			setMotionLock(atw, motionLock);
		}
	}

	/**
	 * @return	Returns true, if any of the AttachedWindows has a motion lock.
	 */
	public boolean isAnyMotionLock() {
		for (AttachedWindow atw : windows) {
			Placer placer = placers.get(atw);
			if (placer.isMotionLock()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
		if (!e.getSource().equals(mainWindow)) return;
		if (enableNotifications)
			placeWindows();
	}
	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
		if (!e.getSource().equals(mainWindow)) return;
		if (enableNotifications)
			placeWindows();
	}
	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent e) {
		if (!e.getSource().equals(mainWindow)) return;
		if (enableNotifications)
			placeWindows();
	}
	/**
	 * @param e
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e) {
		//if (!e.getSource().equals(mainWindow)) return;
		// nothing
	}
}
