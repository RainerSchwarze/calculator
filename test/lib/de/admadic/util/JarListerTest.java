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
package de.admadic.util;

import java.util.Enumeration;
import java.util.Hashtable;

import junit.framework.TestCase;

/**
 * @author Rainer Schwarze
 *
 */
public class JarListerTest extends TestCase {
	JarLister jl;
	Hashtable<String,Boolean> classTest;
	Hashtable<String,Boolean> pathTest;
	String [] classes = {
		"net/n3/nanoxml/CDATAReader.class",
		"net/n3/nanoxml/ContentReader.class",
		"net/n3/nanoxml/IXMLBuilder.class",
		"net/n3/nanoxml/IXMLElement.class",
		"net/n3/nanoxml/IXMLEntityResolver.class",
		"net/n3/nanoxml/IXMLParser.class",
		"net/n3/nanoxml/IXMLReader.class",
		"net/n3/nanoxml/IXMLValidator.class",
		"net/n3/nanoxml/NonValidator.class",
		"net/n3/nanoxml/PIReader.class",
		"net/n3/nanoxml/StdXMLBuilder.class",
		"net/n3/nanoxml/StdXMLParser.class",
		"net/n3/nanoxml/StdXMLReader$1.class",
		"net/n3/nanoxml/StdXMLReader$StackedReader.class",
		"net/n3/nanoxml/StdXMLReader.class",
		"net/n3/nanoxml/ValidatorPlugin.class",
		"net/n3/nanoxml/XMLAttribute.class",
		"net/n3/nanoxml/XMLElement.class",
		"net/n3/nanoxml/XMLEntityResolver.class",
		"net/n3/nanoxml/XMLException.class",
		"net/n3/nanoxml/XMLParseException.class",
		"net/n3/nanoxml/XMLParserFactory.class",
		"net/n3/nanoxml/XMLUtil.class",
		"net/n3/nanoxml/XMLValidationException.class",
		"net/n3/nanoxml/XMLWriter.class",
	};

	String [] extClasses = {
//		"com/",
//		"com/incors/",
//		"com/incors/plaf/",
		"com/incors/plaf/ColorUIResource2.class",
		"com/incors/plaf/FastGradientPaint.class",
		"com/incors/plaf/FastGradientPaintContext$1.class",
		"com/incors/plaf/FastGradientPaintContext$Gradient.class",
		"com/incors/plaf/FastGradientPaintContext$GradientInfo.class",
		"com/incors/plaf/FastGradientPaintContext.class",
//		"com/incors/plaf/kunststoff/",
		"com/incors/plaf/kunststoff/GradientTheme.class",
		"com/incors/plaf/kunststoff/KunststoffButtonUI.class",
		"com/incors/plaf/kunststoff/KunststoffCheckBoxIcon.class",
		"com/incors/plaf/kunststoff/KunststoffCheckBoxUI.class",
		"com/incors/plaf/kunststoff/KunststoffComboBoxUI$MyMetalComboBoxButton.class",
		"com/incors/plaf/kunststoff/KunststoffComboBoxUI.class",
		"com/incors/plaf/kunststoff/KunststoffGradientTheme.class",
		"com/incors/plaf/kunststoff/KunststoffInternalFrameTitlePane.class",
		"com/incors/plaf/kunststoff/KunststoffInternalFrameUI$PaletteListener.class",
		"com/incors/plaf/kunststoff/KunststoffInternalFrameUI.class",
		"com/incors/plaf/kunststoff/KunststoffListUI.class",
		"com/incors/plaf/kunststoff/KunststoffLookAndFeel.class",
		"com/incors/plaf/kunststoff/KunststoffMenuBarUI.class",
		"com/incors/plaf/kunststoff/KunststoffMenuUI.class",
		"com/incors/plaf/kunststoff/KunststoffPasswordFieldUI.class",
		"com/incors/plaf/kunststoff/KunststoffProgressBarUI.class",
		"com/incors/plaf/kunststoff/KunststoffScrollBarUI.class",
		"com/incors/plaf/kunststoff/KunststoffScrollButton.class",
		"com/incors/plaf/kunststoff/KunststoffTabbedPaneUI.class",
		"com/incors/plaf/kunststoff/KunststoffTableHeaderUI.class",
		"com/incors/plaf/kunststoff/KunststoffTextAreaUI.class",
		"com/incors/plaf/kunststoff/KunststoffTextFieldUI.class",
		"com/incors/plaf/kunststoff/KunststoffTheme.class",
		"com/incors/plaf/kunststoff/KunststoffToggleButtonUI.class",
		"com/incors/plaf/kunststoff/KunststoffToolBarUI.class",
		"com/incors/plaf/kunststoff/KunststoffTreeUI.class",
		"com/incors/plaf/kunststoff/KunststoffUtilities.class",
		"com/incors/plaf/kunststoff/ModifiedDefaultListCellRenderer.class",
//		"com/incors/plaf/kunststoff/themes/",
		"com/incors/plaf/kunststoff/themes/KunststoffDesktopTheme.class",
		"com/incors/plaf/kunststoff/themes/KunststoffNotebookTheme.class",
		"com/incors/plaf/kunststoff/themes/KunststoffPresentationTheme.class",
//		"com/incors/plaf/kunststoff/icons/",
//		"com/incors/plaf/kunststoff/icons/Error.gif",
//		"com/incors/plaf/kunststoff/icons/Inform.gif",
//		"com/incors/plaf/kunststoff/icons/Question.gif",
//		"com/incors/plaf/kunststoff/icons/treecol.gif",
//		"com/incors/plaf/kunststoff/icons/treeex.gif",
//		"com/incors/plaf/kunststoff/icons/Warn.gif",
//		"META-INF/INDEX.LIST",
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(JarListerTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		jl = new JarLister();
		classTest = new Hashtable<String,Boolean>();
		System.out.println("Note: not implemented are tests for:");
		System.out.println("- class filters");
		System.out.println("- path filters");
	}

	@Override
	protected void tearDown() throws Exception {
		jl = null;
		super.tearDown();
		System.out.println("Summary repeated:");
		System.out.println("Note: not implemented are tests for:");
		System.out.println("- class filters");
		System.out.println("- path filters");
	}

	protected void initClassTest() {
		for (String s : classes) {
			classTest.put(s, Boolean.FALSE);
		}
	}

	protected void initExtClassTest() {
		for (String s : extClasses) {
			classTest.put(s, Boolean.FALSE);
		}
	}

	protected void resetClassTest() {
		Enumeration<String> it = classTest.keys();
		while (it.hasMoreElements()) {
			classTest.put(it.nextElement(), Boolean.FALSE);
		}
	}

	protected void checkClassTest() {
		Enumeration<String> it = classTest.keys();
		while (it.hasMoreElements()) {
			String cl = it.nextElement();
			if (classTest.get(cl).booleanValue()==false) {
				fail("testJarLister: class " + cl + " was not in jar.");
			}
		}
	}

	protected void resetPathTest() {
		Enumeration<String> it = pathTest.keys();
		while (it.hasMoreElements()) {
			pathTest.put(it.nextElement(), Boolean.FALSE);
		}
	}

	/**
	 * 
	 */
	public void testJarLister() {
		String [] res;
		
		// check that initial state is empty
		res = jl.getClassList();
		assertEquals(
				"testJarLister: initial class list not empty",
				0, res.length);
		res = jl.getPathList();
		assertEquals(
				"testJarLister: initial path list not empty",
				0, res.length);

		try {
			// check that contents match a single jar (nanoxml)
			initClassTest();
			jl.appendList("lib/nanoxml-2.2.1.jar", null, null);
			res = jl.getClassList();
			for (String string : res) {
				if (!classTest.containsKey(string)) {
					fail("testJarLister: additional class " + string + 
							" found, but not expected.");
				}
				classTest.put(string, Boolean.TRUE);
			}
			checkClassTest();

			// check that contents match the two jars (nanoxml, kunststoff)
			initExtClassTest();
			jl.appendList("libx/kunststoff.jar", null, null);
			res = jl.getClassList();
			for (String string : res) {
				if (!classTest.containsKey(string)) {
					fail("testJarLister: additional class " + string + 
							" found, but not expected.");
				}
				classTest.put(string, Boolean.TRUE);
			}
			checkClassTest();

			// check dotted representation:
			res = jl.getClassListDotted();
			for (String string : res) {
				System.out.println("cls = " + string);
			}

			// check that clear works:
			jl.clearList();
			res = jl.getClassList();
			assertEquals(
					"testJarLister: cleared class list not empty",
					0, res.length);
			res = jl.getPathList();
			assertEquals(
					"testJarLister: cleared path list not empty",
					0, res.length);
		} catch (Exception e) {
			fail("Exception caught: " + e.getMessage());
		}
	}
}
