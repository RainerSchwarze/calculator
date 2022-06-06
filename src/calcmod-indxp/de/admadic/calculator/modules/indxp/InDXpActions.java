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
package de.admadic.calculator.modules.indxp;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 * @author Rainer Schwarze
 *
 */
public class InDXpActions {
	final static boolean LOG = true;
	Logger logger = (LOG) ? Logger.getLogger("de.admadic") : null;
	Vector<Action> actions;

	/**
	 * 
	 */
	public InDXpActions() {
		super();
		
		actions = new Vector<Action>();
	}

	/**
	 * create the instances.
	 * @param itf 
	 */
	public void initialize(InDXpItf itf) {
		actions.add(new SingleButtonAction(itf));
		actions.add(new ShowAction(itf));
		actions.add(new HideAction(itf));
		actions.add(new SettingsAction(itf));
		actions.add(new AboutAction(itf));
	}

	/**
	 * @return Returns the actions.
	 */
	public Vector<Action> getActions() {
		return actions;
	}

	/**
	 * @return	Return the actions as an array.
	 */
	public Action[] getActionArray() {
		Action [] aa = new Action[actions.size()];
		aa = actions.toArray(aa);
		return aa;
	}

	/**
	 * @param name
	 * @return	Returns a newly created instance of ImageIcon for the given 
	 * 			icon resource. 
	 */
	public ImageIcon loadIcon(String name) {
		String rname = "de/admadic/calculator/modules/indxp/res/" + name;
		java.net.URL url = this.getClass().getClassLoader().getResource(
				rname);
		if (url==null) {
			if (logger!=null) logger.warning(
					"could not get url for " + rname + 
					" (icon name = " + name + ")");
			return null;
		}
		Image img = Toolkit.getDefaultToolkit().getImage(url);
		if (img==null) {
			if (logger!=null) logger.warning(
					"could not get image for " + url.toString() + 
					" (icon name = " + name + ")");
			return null;
		}
		return new ImageIcon(img);
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public static abstract class ModuleAction extends AbstractAction {
		InDXpItf itf;

		/**
		 * @param itf 
		 * 
		 */
		public ModuleAction(InDXpItf itf) {
			super();
			setItf(itf);
		}

		/**
		 * @return Returns the itf.
		 */
		public InDXpItf getItf() {
			return itf;
		}

		/**
		 * @param itf The itf to set.
		 */
		public void setItf(InDXpItf itf) {
			this.itf = itf;
		}
	}
	
	/**
	 * @author Rainer Schwarze
	 *
	 */
	public class SingleButtonAction extends ModuleAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SingleButtonAction(InDXpItf itf) {
			super(itf);
			putValue(Action.NAME, ""); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Module 'Industrial Designed Experiments'");
			putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("icon-16.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().cmdSingleButton();
		}
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public class ShowAction extends ModuleAction {
		/**
		 * @param itf
		 */
		public ShowAction(InDXpItf itf) {
			super(itf);
			putValue(Action.NAME, "Show");
			putValue(Action.SHORT_DESCRIPTION, "Show InDXp Window");
			// putValue(Action.SMALL_ICON, null);
		}

		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().cmdShow();
		}
		
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public class HideAction extends ModuleAction {
		/**
		 * @param itf
		 */
		public HideAction(InDXpItf itf) {
			super(itf);
			putValue(Action.NAME, "Hide");
			putValue(Action.SHORT_DESCRIPTION, "Hide InDXp Window");
			// putValue(Action.SMALL_ICON, null);
		}

		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().cmdHide();
		}
	}

	/**
	 * @author Rainer Schwarze
	 *
	 */
	public class SettingsAction extends ModuleAction {
		/**
		 * @param itf
		 */
		public SettingsAction(InDXpItf itf) {
			super(itf);
			putValue(Action.NAME, "Settings");
			putValue(Action.SHORT_DESCRIPTION, "Change Settings of InDXp Module");
			// putValue(Action.SMALL_ICON, null);
		}

		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().cmdSettings();
		}
	}


	/**
	 * @author Rainer Schwarze
	 *
	 */
	public class AboutAction extends ModuleAction {
		/**
		 * @param itf
		 */
		public AboutAction(InDXpItf itf) {
			super(itf);
			putValue(Action.NAME, "About");
			putValue(Action.SHORT_DESCRIPTION, "About the InDXp Module");
			// putValue(Action.SMALL_ICON, null);
		}

		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().cmdAbout();
		}
	}
}
