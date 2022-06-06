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
import java.awt.Graphics;

/**
 * @author Rainer Schwarze
 *
 */
public class SnowFlake {
		int x;	// position
		int y;	// position
		int sidedir;	// horizontal direction (+/-1, 0)
		int dirhold;	// how long to hold sidedir
		/** SnowFlake is resting */
		public final static int ST_REST = 0;
		/** SnowFlake is falling */
		public final static int ST_FALLING = 1;
		/** SnowFlake is dead */
		public final static int ST_DEAD = 2;
		/** SnowFlake is an avalanche (a real big snowflake :-) ) */
		public final static int ST_AVALANCHE = 3;
		int status;
		int resting;
		int squeeze;
		/** SnowFlake has not rest status */
		public final static int ST_REST_NONE = 0;
		/** SnowFlake rests on edge */
		public final static int ST_REST_EDGE = 1;
		/** SnowFlake rests on other snow flake */
		public final static int ST_REST_SNOW = 2;
		int reststatus;
		int straight;

		SnowFlake aboveme;
		SnowFlake belowme;

		/**
		 * The this-SnowFlake sits on the given SnowFlake.
		 * @param sf
		 */
		public void sitsOnArg(SnowFlake sf) {
			sf.aboveme = this;
			this.belowme = sf;
			this.updateSqueeze(0);
		}

		/**
		 * Recursively update the squeeze-value by going down... (...belowme)
		 * @param sfsqu
		 */
		public void updateSqueeze(int sfsqu) {
			squeeze = sfsqu;
			if (belowme!=null) 
				belowme.updateSqueeze(squeeze+1);
		}
		
		/**
		 * Destroy the link below this snowflake.
		 */
		public void forgetBelowMe() {
			SnowFlake tmp = this.belowme;
			if (tmp!=null) {
				tmp.aboveme = null;
				this.belowme = null;
			}
		}

		/**
		 * @return	Recursively go down the chain and find the root.
		 */
		public SnowFlake getRoot() {
			SnowFlake tmp = this;
			while (tmp.belowme!=null) {
				tmp = tmp.belowme;
				if (tmp==this) {
					throw new Error("SnowFlake: chaining is circular!");
				}
			}
			return tmp;
		}
		
		/**
		 * @return	Count the weight sitting above this SnowFlake.
		 */
		public int countWeight() {
			SnowFlake tmp = this;
			int weight = 0;
			while (tmp.aboveme!=null) {
				weight++;
				tmp = tmp.aboveme;
			}
			return weight;
		}
		
		/**
		 * Creates a standard SnowFlake.
		 */
		public SnowFlake() {
			super();
			x = 0;
			y = 0;
			sidedir = 0;
			dirhold = 0;
			status = ST_REST;
			resting = 0;
			squeeze = 0;
			reststatus = ST_REST_NONE;
			straight = 0;
		}

		/**
		 * Increases the squeeze.
		 * @deprecated
		 */
		@Deprecated
		public void addSqueeze() {
			squeeze++;
		}
		
		/**
		 * @return	Returns true, if this SnowFlake is dead.
		 */
		public boolean isDead() {
			return status==ST_DEAD;
		}

		/**
		 * @return	Returns true, if this SnowFlake is an avalanche.
		 */
		public boolean isAvalanche() {
			return status==ST_AVALANCHE;
		}

		/**
		 * @return	Returns true, if this SnowFlake is resting.
		 */
		public boolean isRest() {
			return status==ST_REST;
		}

		/**
		 * @return	Returns true, if this SnowFlake is falling.
		 */
		public boolean isFalling() {
			return status==ST_FALLING;
		}
		
		/**
		 * Create a new direction information.
		 */
		public void newDir() {
			sidedir = (int)(Math.round(Math.random()*2)) - 1;
			dirhold = (int)(Math.random()*5);
			straight = 1 - straight;
		}

		/**
		 * Perform one time step in the simulation of this SnowFlake.
		 * @param sw
		 */
		public void timeStep(SnowWorld sw) {
			if (status==ST_DEAD) return;
			if (status==ST_REST) {
				resting++;
				return;
			}
			if (status==ST_AVALANCHE) {
				// x const
				y += dirhold;
				dirhold++;
			}
			if (status==ST_FALLING) {
				if ((dirhold % 2)==0) x += sidedir;
				dirhold--;
				if (dirhold<0) newDir();
				if (x<sw.getFrame().x) x = 0;
				if (x>=(sw.getFrame().x + sw.getFrame().width)) 
					x = sw.getFrame().x + sw.getFrame().width - 1;

				if (Math.random()<0.1) y--;
				y++;
			}
			if (y>(sw.getFrame().y + sw.getFrame().height)) {
//				System.out.println("dead at " + y);
				status = ST_DEAD;
			}
		}

		/**
		 * Creates a standard snowflake in the given SnowWorld.
		 * The SnowWorld is used to specify the size of the world.
		 * The SnowFlake is not actually added to the SnowWorld!
		 * @param sw
		 * @return	Returns a newly created SnowFlake.
		 */
		public static SnowFlake create(SnowWorld sw) {
			SnowFlake sf = new SnowFlake();
			sf.x = (int)(Math.random()*(sw.getFrame().x + sw.getFrame().width));
			sf.y = sw.getFrame().y + 1;
//			sf.x = (int)(Math.random()*(50));
			sf.newDir();
			sf.status = SnowFlake.ST_FALLING;
			return sf;
		}
		
		/**
		 * Paint a resting SnowFlake.
		 * (Does nothing if it is not resting)
		 * @param arg0
		 */
		public void paintResting(Graphics arg0) {
			if (!isRest()) return;
			paint(arg0);
			paintRestingImpl(arg0);
		}

		/**
		 * @param arg0
		 */
		public void paintRestingImpl(Graphics arg0) {
			if (!isRest()) return;
			if (squeeze>0) {
				paintSqueeze(arg0);
			}
		}

		/**
		 * @param arg0
		 */
		public void paint(Graphics arg0) {
			if (status==ST_AVALANCHE) {
				Color c = arg0.getColor();
				arg0.setColor(Color.WHITE);
				arg0.fillOval(x-3, y-2, 7, 7);
				arg0.setColor(c);
			} else {
				if (straight==0) {
					arg0.drawLine(x-1, y, x+1, y);
					arg0.drawLine(x, y-1, x, y+1);
				} else {
					arg0.drawLine(x-1, y-1, x+1, y+1);
					arg0.drawLine(x+1, y-1, x-1, y+1);
				}
				if (status==ST_REST) {
					paintRestingImpl(arg0);
				}
			}
		}

		/**
		 * Paint the squeeziness.
		 * @param arg0
		 */
		public void paintSqueeze(Graphics arg0) {
			Color c = arg0.getColor();
			Color uc = Color.WHITE;
			final int rg[] = {
					250, 245, 245, 245, 240, 230, 220, 210, 200, 180
			};
			int cc = (squeeze>=rg.length) ? rg[rg.length-1] : rg[squeeze];
			if (reststatus==ST_REST_EDGE) {
				uc = new Color(cc, cc, cc);
			} else {
				uc = new Color(cc, cc, 255);
			}
			arg0.setColor(uc);
			arg0.drawLine(x, y, x, y);
			arg0.setColor(c);
		}
	}