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
package de.admadic.ui.gmx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;


/**
 * @author Rainer Schwarze
 *
 */
public class SnowWorld {
	final double RESTLIMIT = 0.5;
	final double EDGELIMIT = 0.5;
	final double CREALIMIT = 0.1;

	Rectangle frame;
	Vector<Rectangle> bounds;
	Hashtable<Integer,Vector<Rectangle>> boundsHash;
	Vector<SnowFlake> restingSnowFlakes;
	Hashtable<Integer,Hashtable<Integer,SnowFlake>> restHash;
	Vector<SnowFlake> snow;
	Vector<SnowFlake> deadSnow;
	BufferedImage restImage;
	BufferedImage compImage;
	Graphics2D restGfx;
	Graphics2D compGfx;
	Vector<SnowFlake> avalanche;
	Semaphore sem;
	int restFlags;

	/**
	 * 
	 */
	public SnowWorld() {
		this(SnowConstants.NONE);
	}

	/**
	 * @param flags
	 */
	public SnowWorld(int flags) {
		super();
		sem = new Semaphore(1);
		sem.acquireUninterruptibly();
		bounds = new Vector<Rectangle>();
		frame = new Rectangle(0, 0, 0, 0);
		restingSnowFlakes = new Vector<SnowFlake>();
		restHash = new Hashtable<Integer,Hashtable<Integer,SnowFlake>>();
		snow = new Vector<SnowFlake>();
		deadSnow = new Vector<SnowFlake>();
		avalanche = new Vector<SnowFlake>();
		restFlags = flags;
		sem.release();
	}

	/**
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void setFrame(int x, int y, int w, int h) {
		frame = new Rectangle(x, y, w, h);
//		System.out.println("frame = " + frame);
		if (snow.size()>0 || restingSnowFlakes.size()>0) {
			shake();
		}
	}

	/**
	 * @return	Returns the bounding frame.
	 */
	public Rectangle getFrame() {
		return frame;
	}

	protected void updateBoundsHash() {
		boundsHash = new Hashtable<Integer,Vector<Rectangle>>();
//		System.out.println("bounds = " + bounds.size() + " entries");
		int count = 0;
		for (Rectangle r : bounds) {
			Integer itg;
			Vector<Rectangle> rv;

			if ((restFlags & SnowConstants.REST_NORTH)!=0) {
				itg = Integer.valueOf(r.y);
				if (boundsHash.containsKey(itg)) {
					rv = boundsHash.get(itg);
				} else {
					rv = new Vector<Rectangle>();
					boundsHash.put(itg, rv);
				}
				rv.add(r);
				System.out.println("bound at " + r.y);
				count++;
			}

			if ((restFlags & SnowConstants.REST_SOUTH)!=0) {
				itg = Integer.valueOf(r.y + r.height - 1);
				if (boundsHash.containsKey(itg)) {
					rv = boundsHash.get(itg);
				} else {
					rv = new Vector<Rectangle>();
					boundsHash.put(itg, rv);
				}
				rv.add(r);
//				System.out.println("bound at " + (r.y + r.height - 1));
				count++;
			}
		}
//		System.out.println("entries: " + count);
	}

	/**
	 * @param c
	 */
	public void initFromContainer(Container c) {
		JRootPane rp = SwingUtilities.getRootPane(c);
		Component gp = rp.getGlassPane();
		Point p = SwingUtilities.convertPoint(c, 0, 0, gp);
//		System.out.println("p = " + p);
		setFrame(
				p.x, p.y,
				c.getWidth(), c.getHeight());
		Component [] ca = c.getComponents();
		bounds.clear();
		for (Component cp : ca) {
			if (
					!(cp instanceof JButton) &&
					!(cp instanceof JToggleButton) &&
					!(cp instanceof JTextField)
					) {
				continue;
			}
			Rectangle r = cp.getBounds();
			r.translate(p.x, p.y);
			bounds.add(r);
		}
		{
			Rectangle r = c.getBounds();
			r.translate(p.x, p.y);
			bounds.add(r);
		}

		updateBoundsHash();
		newRestImage();
	}

	/**
	 * @param c
	 * @param sizeComp 
	 */
	public void initFromComponent(Component c, Component sizeComp) {
		Point p = new Point(0, 0);
		if (sizeComp!=null) {
			p = SwingUtilities.convertPoint(sizeComp, 0, 0, c);
//			System.out.println("p = " + p);
		}
		setFrame(
				c.getX() - p.x, c.getY() - p.y, 
				c.getWidth(), 
				c.getHeight());
		bounds.clear();
		Rectangle r = (sizeComp!=null) ? sizeComp.getBounds() : c.getBounds();
		r.translate(-r.x, -r.y);
		bounds.add(r);
//		System.out.println("cb = " + r);

		updateBoundsHash();
		newRestImage();
	}

	/**
	 * @param sf
	 */
	public void restSnowFlake(SnowFlake sf) {
		sf.status = SnowFlake.ST_REST;
		Integer itg = Integer.valueOf(sf.y);
		Hashtable<Integer,SnowFlake> sv;
		if (!restHash.containsKey(itg)) {
			sv = new Hashtable<Integer,SnowFlake>();
			restHash.put(itg, sv);
		} else {
			sv = restHash.get(itg);
		}
		Integer itgx = Integer.valueOf(sf.x);
		if (sv.containsKey(itgx)) {
			sf.status = SnowFlake.ST_DEAD;
			deadSnow.add(sf);
			sf.y = -1;
			return;
		}
		sv.put(Integer.valueOf(sf.x), sf);
		restingSnowFlakes.add(sf);
		if (restGfx!=null) {
			restGfx.setColor(Color.WHITE);
			sf.paint(restGfx);
		}
		{
			// update squeeziness:
			int tmpy = sf.y + 1;
			SnowFlake sqsf;
			itg = Integer.valueOf(tmpy);
			itgx = Integer.valueOf(sf.x);
			if (restHash.containsKey(itg)) {
				sv = restHash.get(itg);
				if (sv.containsKey(itgx)) {
					sqsf = sv.get(itgx);
					sf.sitsOnArg(sqsf);
					SnowFlake root = sf.getRoot();
					SnowFlake avtest = root;
					if (restImage!=null) {
						while (root!=null) {
							root.paintSqueeze(restGfx);
							root = root.aboveme;
						}
					}
					if (avtest.squeeze>8) {
						avalanche.add(avtest);
					}
				}
			}
		}
	}
	
	/**
	 * @param sf
	 * @return	True, if on edge
	 */
	public boolean isOnEdge(SnowFlake sf) {
		boolean flag = false;
		if (!sf.isFalling()) return flag;
		Integer itg = Integer.valueOf(sf.y);
		if (boundsHash.containsKey(itg)) {
			Vector<Rectangle> vl = boundsHash.get(itg);
			for (Rectangle r : vl) {
				if ((r.y!=sf.y) && ((r.y+r.height-1)!=sf.y)) continue;
				if (sf.x>=r.x && sf.x<(r.x+r.width)) {
//					System.out.println("rests edge");
					return true;
				}
			}
		}
		return flag;
	}

	/**
	 * @param x
	 * @param y
	 * @return	True, if any snow is resting at x,y
	 */
	public boolean isAnyRestingAt(int x, int y) {
		Integer itgy = Integer.valueOf(y);
		Integer itgx = Integer.valueOf(x); 
		if (!restHash.containsKey(itgy)) return false;
		Hashtable<Integer,SnowFlake> sv;
		sv = restHash.get(itgy);
		if (!sv.containsKey(itgx)) return false;
		return true;
	}
	
	/**
	 * @param sf
	 * @return	True, if a resting snow flake is below
	 */
	public boolean isOnRestingSnowFlake(SnowFlake sf) {
		if (!sf.isFalling()) return false;
		Integer itgy = Integer.valueOf(sf.y+1);
		Integer itgx = Integer.valueOf(sf.x);
		Hashtable<Integer,SnowFlake> sv;
		if (!restHash.containsKey(itgy)) return false;
		sv = restHash.get(itgy);
		boolean bl, b, br;
		b = sv.containsKey(itgx);
		bl = sv.containsKey(Integer.valueOf(sf.x-1));
		br = sv.containsKey(Integer.valueOf(sf.x+1));
		if (!b) return false;
		if (!bl && !br) return false;
		return true;
	}

	/**
	 * 
	 */
	public void newRestImage() {
		if (frame.width<1) {
			restImage = null;
			restGfx = null;
			return;
		}
		restImage = new BufferedImage(
				frame.width, frame.height, BufferedImage.TYPE_4BYTE_ABGR);
		restGfx = restImage.createGraphics();
		restGfx.setColor(Color.WHITE);
	}

	/**
	 * 
	 */
	public void newCompImage() {
		if (frame.width<1) {
			compImage = null;
			compGfx = null;
			return;
		}
		compImage = new BufferedImage(
				frame.width, frame.height, BufferedImage.TYPE_4BYTE_ABGR);
		compGfx = compImage.createGraphics();
		compGfx.setColor(Color.WHITE);
	}

	/**
	 * Shake the world!
	 */
	public void shake() {
		for (SnowFlake sf : restingSnowFlakes) {
			sf.forgetBelowMe();
			sf.status = SnowFlake.ST_FALLING;
		}
		restingSnowFlakes.clear();
		for (Hashtable<Integer,SnowFlake> sv : restHash.values()) {
			sv.clear();
		}
		newRestImage();
	}
	
	/**
	 * Make a new time step
	 */
	public void updateSnow() {
//		System.out.println("we have " + snow.size() + " snow flakes");
		for (SnowFlake sf : snow) {
			if (sf.isDead() || sf.isRest()) continue;
			sf.timeStep(this);
			boolean resting;
			boolean edging;
			resting = isOnRestingSnowFlake(sf);
			if (resting) {
				if (Math.random()<RESTLIMIT) {
					sf.reststatus = SnowFlake.ST_REST_SNOW;
					restSnowFlake(sf);
				}
			} else {
				edging = isOnEdge(sf); 
				if (edging) {
					if (Math.random()<EDGELIMIT) {
						sf.reststatus = SnowFlake.ST_REST_EDGE;
						restSnowFlake(sf);
					}
				}
			}
			if (sf.isDead()) deadSnow.add(sf);
		}
		boolean anyAvalanche = false;
		for (SnowFlake sf : avalanche) {
			if (Math.random()<=1.0) {
				anyAvalanche = true;
				createAvalanche(sf);
				removeFromRestLists(sf);
			}
		}
		avalanche.clear();
		sem.acquireUninterruptibly();
		for (SnowFlake sf : deadSnow) {
			snow.remove(sf);
			removeFromRestLists(sf);
		}
		sem.release();
		deadSnow.clear();
		if (anyAvalanche) {
			newRestImage();
			if (restImage!=null) {
				for (SnowFlake sf : restingSnowFlakes) {
					sf.paintResting(restGfx);
				}
			}
		}
		sem.acquireUninterruptibly();
		for (int i=0; i<(1 + frame.width*3/200); i++) {
			if (Math.random()<CREALIMIT) {
				snow.add(SnowFlake.create(this));
			}
		}
		sem.release();
//		System.out.println("st = " + testRest + " / " + testEdge);
	}

	/**
	 * @param sf
	 */
	public void removeFromRestLists(SnowFlake sf) {
		if (sf.isRest()) {
			restingSnowFlakes.remove(sf);
		}
		Integer itgy = Integer.valueOf(sf.y);
		if (!restHash.containsKey(itgy)) return;
		Hashtable<Integer,SnowFlake> sv = restHash.get(itgy);
		Integer itgx = Integer.valueOf(sf.x);
		if (!sv.containsKey(itgx)) return;
		sv.remove(itgx);
	}

	/**
	 * @param sf
	 */
	public void createAvalanche(SnowFlake sf) {
		int xf, xt, y;
		y = sf.y;
		xf = sf.x;
		xt = sf.x;
		boolean hasAny;

//		System.out.println("creating avalanche at " + sf.x + "," + sf.y);
		
		sf.status = SnowFlake.ST_AVALANCHE;
		sf.dirhold = 0;
		sf.forgetBelowMe();
		
		while (true) {
			xf--;
			xt++;
			y--;
			hasAny = false;

			Hashtable<Integer,SnowFlake> sv;
			Integer itg = Integer.valueOf(y);
			if (!restHash.containsKey(itg)) break;
			sv = restHash.get(itg);
			for (int x=xf; x<=xt; x++) {
				itg = Integer.valueOf(x);
				if (!sv.containsKey(itg)) continue;
				hasAny = true;
				SnowFlake dsf = sv.get(itg);
				dsf.status = SnowFlake.ST_DEAD;
				dsf.forgetBelowMe();
				deadSnow.add(dsf);
//				System.out.println("av: + @ " + dsf.x + "," + dsf.y);
			}
			if (!hasAny) break;
		}
//		System.out.println("av:-------------");
	}
	
	/**
	 * @param arg0
	 */
	public void paint(Graphics arg0) {
		arg0.setColor(Color.WHITE);
		if (restImage!=null) {
			arg0.drawImage(restImage, 0, 0, null);
		}
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			// e.printStackTrace();
			return;
		}
		for (SnowFlake sf : snow) {
			if (restImage==null && sf.isRest()) {
				sf.paint(arg0);
			}
			if (sf.isFalling() || sf.isAvalanche()) sf.paint(arg0);
		}
		sem.release();
	}
}
