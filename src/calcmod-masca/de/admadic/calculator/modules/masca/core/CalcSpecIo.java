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
package de.admadic.calculator.modules.masca.core;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLParserFactory;
import de.admadic.units.core.UnitManager;
import de.admadic.util.FileUtil;

/**
 * @author Rainer Schwarze
 *
 */
public class CalcSpecIo {
// example:
//	<?xml version="1.0" encoding="UTF-8"?>
//	<calcspec>
//		<categories>
//			<category id=".youngsmod" name="Young's Modulus"/>
//			<category id=".strain" name="Strain">
//				<category id=".fromlengths" name="From Lengths" />
//				<category id=".fromstress" name="From Stress" />
//			</category>
//			<category id=".stress" name="Stress"/>
//		</categories>
//
//		<calculations>
//			<calculation id="sigma=force/area">
//				<title>Sigma = Force/Area</title>
//				<desc>Calculate Stress from Force and Area (Cross Section)</desc>
//				<output mid=".sigma" qid="@stress" value="1.0" uid="*M.Pa"
//						symbol="S" name="Stress" >
//				</output>
//				<input mid=".force" qid="@force" value="1.0" uid="*k.N"
//						symbol="F" name="Force" >
//				</input>
//				<input mid=".area" qid="@area" value="1.0" uid="*m.m*m.m"
//						symbol="A" name="Area" >
//					<constraint>
//						<interval kind="prohibited" type="closedclosed" begin="0.0" end="0.0" />
//					</constraint>
//				</input>
//				<expression expr="force / area" />
//			</calculation>
//		</calculations>
//	</calcspec>
	
	CalcManager cm;
	UnitManager um;
	
	/**
	 * @param cm 
	 * @param um 
	 */
	public CalcSpecIo(CalcManager cm, UnitManager um) {
		super();
		this.cm = cm;
		this.um = um;
	}

	/**
	 * @param file
	 */
	public void readFile(File file) {
		try {
			IXMLParser parser;
			String fname = file.toString();
			fname = FileUtil.fixFileName1(fname);
			parser = XMLParserFactory.createDefaultXMLParser();
			IXMLReader reader = StdXMLReader.fileReader(fname);
			parser.setReader(reader);
			IXMLElement xml = (IXMLElement)parser.parse();

			String rootName = xml.getName();
			if (rootName.equals("calcspec")) {
				readCalcSpec(xml);
			} else {
				throw new CalcSpecIoError(
						"invalid root: only calcspec allowed.");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new CalcSpecIoError("Load Error: " + e.getMessage());
		}
	}

	/**
	 * @param url
	 */
	public void readFile(URL url) {
		try {
			IXMLParser parser;
			parser = XMLParserFactory.createDefaultXMLParser();
			IXMLReader reader = new StdXMLReader(null, url.toString());
			parser.setReader(reader);
			IXMLElement xml = (IXMLElement)parser.parse();

			String rootName = xml.getName();
			if (rootName.equals("calcspec")) {
				readCalcSpec(xml);
			} else {
				throw new CalcSpecIoError(
						"invalid root: only calcspec allowed.");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new CalcSpecIoError("Load Error: " + e.getMessage());
		}
	}

	/**
	 * @param xml
	 */
	protected void readCalcSpec(IXMLElement xml) {
		Vector<?> groups;
		groups = xml.getChildren();
		for (Object o : groups) {
			IXMLElement xmlE = (IXMLElement)o;
			String nameE = xmlE.getName();
			if (nameE.equals("calculations")) {
				readCalculations(xmlE);
			} else if (nameE.equals("categories")) {
				readCategories(xmlE, null);
			} else {
				// unknown entry. ignore it.
			}
		}
	}


	/**
	 * @param xml
	 */
	protected void readCalculations(IXMLElement xml) {
		Vector<?> elements;
		elements = xml.getChildrenNamed("calculation");
		for (Object o : elements) {
			IXMLElement xmlE = (IXMLElement)o;
			String id = xmlE.getAttribute("id", null);
			String catid = xmlE.getAttribute("catid", null);
			CalculationGeneric ceg = CalcFactory.createGenericCalculation(um, id);
			readCalculation(xmlE, ceg);
			cm.addCalculation(ceg);
			cm.addCalculationToCategory(ceg, catid);
		}
	}

	protected void readCalculation(IXMLElement xml, CalculationGeneric ceg) {
		IXMLElement xmlE;
		xmlE = xml.getFirstChildNamed("title");
		if (xmlE!=null) ceg.setTitle(xmlE.getContent());
		xmlE = xml.getFirstChildNamed("description");
		if (xmlE!=null) ceg.setDescription(xmlE.getContent());
		
		xmlE = xml.getFirstChildNamed("expression");
		if (xmlE!=null) {
			ceg.setExpression(xmlE.getContent());
		} else {
			throw new CalcSpecIoError("no element <expression> found for " + 
					ceg.getId());
		}

		xmlE = xml.getFirstChildNamed("output");
		if (xmlE!=null) {
			readCalculationVariable(xmlE, ceg);
		} else {
			throw new CalcSpecIoError("no element <output> found for " + 
					ceg.getId());
		}

		Vector<?> v = xml.getChildrenNamed("input");
		for (Object o : v) {
			IXMLElement xmlE2 = (IXMLElement)o;
			readCalculationVariable(xmlE2, ceg);
		}

		// make a final test:
		ceg.testExpression();
	}

	protected void readCalculationVariable(
			IXMLElement xml, CalculationGeneric ceg) {
		String mid = xml.getAttribute("mid", null);
		String qid = xml.getAttribute("qid", null);
		String val = xml.getAttribute("value", null);
		String uid = xml.getAttribute("uid", null);
		String symbol = xml.getAttribute("symbol", null);
		String name = xml.getAttribute("name", null);
		if (mid==null || qid==null || val==null || uid==null) {
			throw new CalcSpecIoError(
					"Not all of mid, qid, value, uid are specified for " + 
					ceg.getId());
		}
		if (xml.getName().equals("output")) {
			ceg.addOutput(mid, qid, val, uid, symbol, name);
		} else if (xml.getName().equals("input")) {
			ceg.addInput(mid, qid, val, uid, symbol, name);
		} else {
			throw new CalcSpecIoError(
					"Internal error. only <output> or <input> allowed for " + 
					ceg.getId());
		}

		Vector<?> v = xml.getChildrenNamed("constraint");
		for (Object o : v) {
			IXMLElement xmlE = (IXMLElement)o;
			readCalculationInputConstraint(xmlE, mid, ceg);
		}
	}

	protected void readCalculationInputConstraint(
			IXMLElement xml, String mid, CalculationGeneric ceg) {
		// only <interval> supported so far
		ValueConstraints vc = new ValueConstraints();
		Vector<?> v = xml.getChildrenNamed("interval");
		for (Object o : v) {
			IXMLElement xmlE = (IXMLElement)o;
			String kind = xmlE.getAttribute("kind", "allowed");
			String type = xmlE.getAttribute("type", "closedclosed");
			String begin = xmlE.getAttribute("begin", null);
			String end = xmlE.getAttribute("end", null);
			if (begin==null || end==null) {
				throw new CalcSpecIoError(
						"no interval bounds given in constraints for " + 
						ceg.getId());
			}
			int typei;
			if (type.equals("openopen")) {
				typei = ValueConstraints.TYPE_OPEN_OPEN;
			} else if (type.equals("openclosed")) {
				typei = ValueConstraints.TYPE_OPEN_CLOSED;
			} else if (type.equals("closedopen")) {
				typei = ValueConstraints.TYPE_CLOSED_OPEN;
			} else if (type.equals("closedclosed")) {
				typei = ValueConstraints.TYPE_CLOSED_CLOSED;
			} else {
				throw new CalcSpecIoError(
						"invalid type given for interval in constraints for " + 
						ceg.getId());
			}
			double bv = Double.parseDouble(begin);
			double ev = Double.parseDouble(end);
			if (kind.equals("allowed")) {
				vc.addAllowedInterval(bv, ev, typei);
			} else if (kind.equals("prohibited")) {
				vc.addProhibitedInterval(bv, ev, typei);
			} else {
				throw new CalcSpecIoError(
						"invalid kind given for interval in constraints for " + 
						ceg.getId());
			}
		}
		ceg.addInputConstraints(mid, vc);
	}

	protected void readCategories(IXMLElement xml, String parentId) {
		Vector<?> v = xml.getChildrenNamed("category");
		for (Object o : v) {
			IXMLElement xmlE = (IXMLElement)o;
			String id = xmlE.getAttribute("id", null);
			String name = xmlE.getAttribute("name", null);
			if (id==null || name==null) {
				throw new CalcSpecIoError(
					"id or name missing in category");
			}
			CalcCategory cc = new CalcCategory(id, name);
			cm.addCategory(parentId, cc);

			// recurse down into the tree leafs:
			readCategories(xmlE, id);
		}
	}
}
