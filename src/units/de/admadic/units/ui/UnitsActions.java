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
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * @author Rainer Schwarze
 *
 */
public class UnitsActions {
	Vector<Action> actions;
	Hashtable<Class,Action> classToAction;
	UnitsCmdItf itf;

	/**
	 * @param itf 
	 */
	public UnitsActions(UnitsCmdItf itf) {
		super();
		this.itf = itf;
		actions = new Vector<Action>();
		classToAction = new Hashtable<Class,Action>();
		initStandard();
	}

	/**
	 * 
	 */
	public void initStandard() {
		actions.add(new FilterFieldsSetAction(itf));
		actions.add(new FilterFieldsClearAction(itf));
		actions.add(new FilterFieldsAddAction(itf));
		actions.add(new FilterFieldsRemoveAction(itf));
		actions.add(new FilterSousSetAction(itf));
		actions.add(new FilterSousClearAction(itf));
		actions.add(new FilterSousAddAction(itf));
		actions.add(new FilterSousRemoveAction(itf));
		actions.add(new FilterDomainsSetAction(itf));
		actions.add(new FilterDomainsClearAction(itf));
		actions.add(new FilterDomainsAddAction(itf));
		actions.add(new FilterDomainsRemoveAction(itf));

		actions.add(new SetFieldsSetAction(itf));
		actions.add(new SetFieldsClearAction(itf));
		actions.add(new SetFieldsAddAction(itf));
		actions.add(new SetFieldsRemoveAction(itf));
		actions.add(new SetSousSetAction(itf));
		actions.add(new SetSousClearAction(itf));
		actions.add(new SetSousAddAction(itf));
		actions.add(new SetSousRemoveAction(itf));
		actions.add(new SetDomainsSetAction(itf));
		actions.add(new SetDomainsClearAction(itf));
		actions.add(new SetDomainsAddAction(itf));
		actions.add(new SetDomainsRemoveAction(itf));

		for (Action a : actions) {
			classToAction.put(a.getClass(), a);
		}
	}

	/**
	 * @param cls
	 * @return	Returns the action for the specified class.
	 */
	public Action getAction(Class cls) {
		return classToAction.get(cls);
	}

	
	
	/**
	 * @author Rainer Schwarze
	 *
	 */
	public abstract class UnitsAction extends AbstractAction {
		UnitsCmdItf uif;

		/**
		 * @param itf
		 */
		public UnitsAction(UnitsCmdItf itf) {
			super();
			this.uif = itf;
		}

		/**
		 * @return Returns the itf.
		 */
		public UnitsCmdItf getItf() {
			return uif;
		}
	}
	
	/**
	 * @author Rainer Schwarze
	 */
	public class FilterFieldsSetAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public FilterFieldsSetAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "Set"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Set Fields Filter Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doFilterFieldsSet();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class FilterFieldsClearAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public FilterFieldsClearAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "Clr"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Clears Fields Filter Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doFilterFieldsClear();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class FilterFieldsAddAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public FilterFieldsAddAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "+"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Add Fields Filter Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doFilterFieldsAdd();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class FilterFieldsRemoveAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public FilterFieldsRemoveAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "-"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Remove Fields Filter Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doFilterFieldsRemove();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class FilterSousSetAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public FilterSousSetAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "Set"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Set Fields Filter Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doFilterSousSet();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class FilterSousClearAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public FilterSousClearAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "Clr"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Clears Fields Filter Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doFilterSousClear();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class FilterSousAddAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public FilterSousAddAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "+"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Add Fields Filter Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doFilterSousAdd();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class FilterSousRemoveAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public FilterSousRemoveAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "-"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Remove Fields Filter Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doFilterSousRemove();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class FilterDomainsSetAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public FilterDomainsSetAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "Set"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Set Fields Filter Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doFilterDomainsSet();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class FilterDomainsClearAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public FilterDomainsClearAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "Clr"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Clears Fields Filter Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doFilterDomainsClear();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class FilterDomainsAddAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public FilterDomainsAddAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "+"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Add Fields Filter Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doFilterDomainsAdd();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class FilterDomainsRemoveAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public FilterDomainsRemoveAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "-"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Remove Fields Filter Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doFilterDomainsRemove();
		}
	}


	/**
	 * @author Rainer Schwarze
	 */
	public class SetFieldsSetAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SetFieldsSetAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "Set"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Set Fields Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doSetFieldsSet();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SetFieldsClearAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SetFieldsClearAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "Clr"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Clears Fields Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doSetFieldsClear();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SetFieldsAddAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SetFieldsAddAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "+"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Add Fields Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doSetFieldsAdd();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SetFieldsRemoveAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SetFieldsRemoveAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "-"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Remove Fields Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doSetFieldsRemove();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SetSousSetAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SetSousSetAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "Set"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Set Fields Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doSetSousSet();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SetSousClearAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SetSousClearAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "Clr"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Clears Fields Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doSetSousClear();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SetSousAddAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SetSousAddAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "+"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Add Fields Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doSetSousAdd();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SetSousRemoveAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SetSousRemoveAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "-"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Remove Fields Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doSetSousRemove();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SetDomainsSetAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SetDomainsSetAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "Set"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Set Fields Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doSetDomainsSet();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SetDomainsClearAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SetDomainsClearAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "Clr"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Clears Fields Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doSetDomainsClear();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SetDomainsAddAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SetDomainsAddAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "+"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Add Fields Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doSetDomainsAdd();
		}
	}

	/**
	 * @author Rainer Schwarze
	 */
	public class SetDomainsRemoveAction extends UnitsAction {
		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * @param itf 
		 */
		public SetDomainsRemoveAction(UnitsCmdItf itf) {
			super(itf);
			putValue(Action.NAME, "-"); // InDXp - nothing because we have an icon?
			putValue(Action.SHORT_DESCRIPTION, "Remove Fields Elements");
			// putValue(Action.SMALL_ICON, InDXpActions.this.loadIcon("btn-sglbtn.png"));
		}

		/**
		 * @param e
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			getItf().doSetDomainsRemove();
		}
	}
}
