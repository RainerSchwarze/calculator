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
package de.admadic.units.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLParserFactory;
import net.n3.nanoxml.XMLWriter;
import de.admadic.util.FileUtil;

/**
 * @author Rainer Schwarze
 *
 */
public class UnitsIo {
	UnitManager um;
	DateFormat df;
	DateFormat fallbackDf;

	/**
	 * @param um 
	 * 
	 */
	public UnitsIo(UnitManager um) {
		super();
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
			IXMLElement xml2;

			String rootName = xml.getName();
			if (rootName.equals("unitdef")) {
				readUnitDef(xml);
			} else if (rootName.equals("quantdef")) {
				readQuantDef(xml);
			} else if (rootName.equals("measdef")) {
				readMeasDef(xml);
			} else {
				throw new UnitsIoError(
						"invalid root: only unitdef, quantdef, measdef allowed.");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new UnitsIoError("Load Error: " + e.getMessage());
		}
	}

	/**
	 * @param url
	 */
	public void readFile(URL url) {
		try {
			IXMLParser parser;
			// String fname = file.toString();
			// fname = FileUtil.fixFileName1(fname);
			parser = XMLParserFactory.createDefaultXMLParser();
			IXMLReader reader = new StdXMLReader(null, url.toString());
			parser.setReader(reader);
			IXMLElement xml = (IXMLElement)parser.parse();
			IXMLElement xml2;

			String rootName = xml.getName();
			if (rootName.equals("unitdef")) {
				readUnitDef(xml);
			} else if (rootName.equals("quantdef")) {
				readQuantDef(xml);
			} else if (rootName.equals("measdef")) {
				readMeasDef(xml);
			} else {
				throw new UnitsIoError(
						"invalid root: only unitdef, quantdef, measdef allowed.");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new UnitsIoError("Load Error: " + e.getMessage());
		}
	}

	/**
	 * @param root
	 * @param file
	 */
	protected void saveImpl(IXMLElement root, File file) {
		try {
			java.io.Writer output;
			output = new FileWriter(file);
			output.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			// output.append("<!DOCTYPE preferences SYSTEM \"cfgtest.dtd\">\n");
			IXMLElement xmltree = root;
			XMLWriter xmlwriter = new XMLWriter(output);
			xmlwriter.write(xmltree, true);	
		} catch (IOException e) {
			// e.printStackTrace();
			throw new UnitsIoError("Save Error: " + e.getMessage());
		}
	}
	
	/**
	 * @param file
	 */
	public void saveFileUnits(File file) {
		IXMLElement root;
		root = new XMLElement("unitdef");
		createFields(root);
		createSubFields(root);
		createSystemOfUnits(root);
		createDomains(root);
		createFactors(root);
		createUnits(root);
		createUnitContexts(root);

		saveImpl(root, file);
	}

	/**
	 * @param file
	 */
	public void saveFileQuantities(File file) {
		IXMLElement root;
		root = new XMLElement("quantdef");
		createQuantities(root);

		saveImpl(root, file);
	}

	/**
	 * @param xml
	 */
	protected void readUnitDef(IXMLElement xml) {
		Vector<IXMLElement> groups;
		groups = xml.getChildren();
		for (IXMLElement xmlE : groups) {
			String nameE = xmlE.getName();
			if (nameE.equals("units")) {
				readUnits(xmlE);
			} else if (nameE.equals("unitcontexts")) {
				readUnitContexts(xmlE);
			} else if (nameE.equals("factors")) {
				readFactors(xmlE);
			} else if (nameE.equals("domains")) {
				readDomains(xmlE);
			} else if (nameE.equals("fields")) {
				readFields(xmlE);
			} else if (nameE.equals("subfields")) {
				readSubFields(xmlE);
			} else if (nameE.equals("systemofunits")) {
				readSystemOfUnits(xmlE);
			} else {
				// unknown entry. ignore it.
			}
		}
	}

	/**
	 * @param xml
	 */
	protected void readUnitContexts(IXMLElement xml) {
		Vector<IXMLElement> elements;
		elements = xml.getChildrenNamed("uc");
		for (IXMLElement xmlE : elements) {
			// 		<factor id="T" symbol="T" name="Tera" value="1e12" />
			String dom = xmlE.getAttribute("dom", null);
			String uid = xmlE.getAttribute("uid", null);
			IUnit u = um.getUnitFromDomain(uid, dom);
			if (u==null) {
				throw new UnitsIoError("could not find unit with " + dom + "/" + uid);
			}
			UnitContext uc = u.getContext();
			uc.clear();
			String sou = xmlE.getAttribute("sou", null);
			if (sou!=null) {
				String [] soua = sou.split(",");
				for (String s : soua) {
					SystemOfUnits s1 = um.getSystemOfUnits(s);
					uc.addSystemOfUnits(s1);
				}
			}
			String sf = xmlE.getAttribute("sf", null);
			if (sf!=null) {
				String [] sfa = sf.split(",");
				for (String s : sfa) {
					SubField s1 = um.getSubField(s);
					uc.addSubField(s1);
				}
			}
		}
	}


	/**
	 * @param xml
	 */
	protected void readDomains(IXMLElement xml) {
		Vector<IXMLElement> elements;
		elements = xml.getChildrenNamed("domain");
		for (IXMLElement xmlE : elements) {
			// 		<factor id="T" symbol="T" name="Tera" value="1e12" />
			String id = xmlE.getAttribute("id", null);
			String name = xmlE.getAttribute("name", null);
			Domain d = UnitFactory.createDomain(id, name);
			um.addDomain(d);
		}
	}

	/**
	 * @param xml
	 */
	protected void readFields(IXMLElement xml) {
		Vector<IXMLElement> elements;
		elements = xml.getChildrenNamed("field");
		for (IXMLElement xmlE : elements) {
			String id = xmlE.getAttribute("id", null);
			String name = xmlE.getAttribute("name", null);
			Field f = UnitFactory.createField(id, name);
			um.addField(f);
		}
	}

	/**
	 * @param xml
	 */
	protected void readSubFields(IXMLElement xml) {
		Vector<IXMLElement> elements;
		elements = xml.getChildrenNamed("subfield");
		for (IXMLElement xmlE : elements) {
			String fid = xmlE.getAttribute("fid", null);
			String id = xmlE.getAttribute("id", null);
			String name = xmlE.getAttribute("name", null);
			SubField sf = UnitFactory.createSubField(um, fid, id, name);
			um.addSubField(sf);
		}
	}


	/**
	 * @param xml
	 */
	protected void readSystemOfUnits(IXMLElement xml) {
		Vector<IXMLElement> elements;
		elements = xml.getChildrenNamed("sou");
		for (IXMLElement xmlE : elements) {
			String id = xmlE.getAttribute("id", null);
			String symbol = xmlE.getAttribute("symbol", null);
			String name = xmlE.getAttribute("name", null);
			SystemOfUnits sou = UnitFactory.createSystemOfUnits(id, symbol, name);
			um.addSystemOfUnits(sou);
		}
	}

	/**
	 * 
	 * @param xml
	 */
	protected void readFactors(IXMLElement xml) {
		Vector<IXMLElement> elements;
		elements = xml.getChildrenNamed("factor");
		for (IXMLElement xmlE : elements) {
			// 		<factor id="T" symbol="T" name="Tera" value="1e12" />
			String id = xmlE.getAttribute("id", null);
			String symbol = xmlE.getAttribute("symbol", null);
			String name = xmlE.getAttribute("name", null);
			String value = xmlE.getAttribute("value", null);
			Factor f = createFactor(id, symbol, name, value);
			um.addFactor(f);
		}
	}

	/**
	 * 
	 * @param id
	 * @param symbol
	 * @param name
	 * @param value
	 * @return	Returns the Factor created.
	 */
	protected Factor createFactor(String id, String symbol, String name, String value) {
		double d = Double.parseDouble(value);
		return new Factor(id, symbol, name, d);
	}

	/**
	 * 
	 * @param xml
	 */
	protected void readUnits(IXMLElement xml) {
		Vector<IXMLElement> elements;
		elements = xml.getChildren();
		for (IXMLElement xmlE : elements) {
			// <baseunit>, <compunit>
			String name = xmlE.getName();
			if (name.equals("baseunit")) {
				readBaseUnit(xmlE);
			} else if (name.equals("compunit")) {
				readCompUnit(xmlE);
			} else if (name.equals("substunit")) {
				readSubstUnit(xmlE);
			} else if (name.equals("factorunit")) {
				readFactorUnit(xmlE);
			} else {
				// unknown -> ignore
			}
		}
		
	}

	/**
	 * 
	 * @param qtyId
	 * @return	Returns the PRI_QTY_ constant.
	 */
	protected int convertStringToQtyId(String qtyId) {
		qtyId = qtyId.toLowerCase();
		if (qtyId.equals("length")) return Constants.PRI_QTY_LENGTH;
		if (qtyId.equals("time")) return Constants.PRI_QTY_TIME;
		if (qtyId.equals("mass")) return Constants.PRI_QTY_MASS;
		if (qtyId.equals("temperature")) return Constants.PRI_QTY_TEMPERATURE;
		if (qtyId.equals("elcurrent")) return Constants.PRI_QTY_ELCURRENT;
		if (qtyId.equals("luminosity")) return Constants.PRI_QTY_LUMINOSITY;
		if (qtyId.equals("amtsubstance")) return Constants.PRI_QTY_AMTSUBSTANCE;
		throw new UnitsIoError("Invalid qtyId: " + qtyId);
	}
	
	/**
	 * 
	 * @param xmlE
	 */
	protected void readBaseUnit(IXMLElement xmlE) {
		// <baseunit qtyId="LENGTH" id=".m" symbol="m" name="meter" />
		String id = xmlE.getAttribute("id", null);
		String symbol = xmlE.getAttribute("symbol", null);
		String name = xmlE.getAttribute("name", null);
		String dom = xmlE.getAttribute("dom", null);
		Domain domain = null;
		if (dom!=null) {
			domain = um.getDomain(dom);
		}
		IUnit u = new BaseUnit(domain, id, symbol, name);
		String ts = xmlE.getAttribute("ts", null);
		Date tsd = null;
		if (ts!=null && !ts.equals("")) {
			tsd = decodeTime(ts);
		}
		if (tsd!=null) u.setLastChange(tsd);
		checkUnit(u);
		um.addUnit(u);
	}

	/**
	 * 
	 * @param xmlE
	 */
	protected void readFactorUnit(IXMLElement xmlE) {
		// <baseunit qtyId="LENGTH" id=".m" symbol="m" name="meter" />
		// auto String id = xmlE.getAttribute("id", null);
		String dom = xmlE.getAttribute("dom", null);
		String ubasid = xmlE.getAttribute("ubasid", null);
		String fid = xmlE.getAttribute("fid", null);
		Domain domain = null;
		Factor factor = null;
		IUnit unit = null;
		if (dom==null || fid==null || ubasid==null) {
			throw new UnitsIoError("factor unit must have domain, ubasid and fid!");
		}
		domain = um.getDomain(dom);
		factor = um.getFactor(fid);
		unit = um.getUnit(ubasid);
			
		IUnit u = new FactorUnit(domain, unit, factor);
		String ts = xmlE.getAttribute("ts", null);
		Date tsd = null;
		if (ts!=null && !ts.equals("")) {
			tsd = decodeTime(ts);
		}
		if (tsd!=null) u.setLastChange(tsd);
		checkUnit(u);
		um.addUnit(u);
	}

	protected void checkUnit(IUnit u) {
		if (um.existsUnit(u.getId())) {
			System.out.println(
					"Warning: a unit with the id " + u.getId() + 
					" already exists (this is " + u.getName() + ")");
		}
	}
	
	/**
	 * 
	 * @param xml
	 */
	protected void readCompUnit(IXMLElement xml) {
// samples:
//	<compunit>
//		<converter type="mul">
//			<param name="m" value="3600" />
//		</converter>
//		<umul id=".h" />
//	</compunit>
//	<compunit>
//		<!-- TK = (TF + 459,67) / 1,8 -->
//		<!-- TK = 0.5555555556*TF + 255.372222222 -->
//		<converter type="lin">
//			<param name="m" value="0.5555555555555556" xvalue="10/18" />
//			<param name="n" value="255.37222222222222" xvalue="459.67/1.8" />
//		</converter>
//		<umul id=".°F" />
//	</compunit>
		CompositeUnit cu = new CompositeUnit();

		Vector<IXMLElement> elements;

		String dom = xml.getAttribute("dom", null);
		Domain domain = null;
		if (dom!=null) {
			domain = um.getDomain(dom);
		}
		cu.setDomain(domain);

		String idvec = xml.getAttribute("idvec", null);
		if (idvec!=null) {
			String [] ida = idvec.split(Constants.REGEX_SPLIT_ID);
			for (String id : ida) {
				if (id.equals("")) continue;
				char op = id.charAt(0);
				switch (op) {
				case Constants.CH_FAC_MUL: 
					cu.setFactor(um.getFactor(id));
					break;
				case Constants.CH_UNT_MUL:
					cu.appendUnit(um.getUnit(id), CompositeUnit.MUL);
					break;
				case Constants.CH_UNT_DIV:
					String newid = Constants.CH_UNT_MUL + id.substring(1);
					cu.appendUnit(um.getUnit(newid), CompositeUnit.DIV);
					break;
				default:
					throw new UnitsIoError("op code in idvec not supported: " + op);
				}
			}
		}
		String ubasid = xml.getAttribute("ubasid", null);
		if (ubasid!=null) {
			// base units are currently simply added to mul
			cu.appendUnit(um.getUnit(ubasid), CompositeUnit.MUL);
		}
		String substid = xml.getAttribute("substid", null);
		String substsymbol = xml.getAttribute("substsymbol", null);
		String substname = xml.getAttribute("substname", null);
		if (substid!=null || substsymbol!=null || substname!=null) {
			if (substid==null || substsymbol==null || substname==null) {
				throw new UnitsIoError("if then all of subst* attributes must be set");
			}
			cu.setSubst(substid, substsymbol, substname);
		}
		elements = xml.getChildren();
		for (IXMLElement xmlE : elements) {
			String name = xmlE.getName();
			if (name.equals("ufac")) {
				String id = xmlE.getAttribute("id", null);
				cu.setFactor(um.getFactor(id));
			} else if (name.equals("ubas")) {
				// base units are currently simply added to mul
				String id = xmlE.getAttribute("id", null);
				cu.appendUnit(um.getUnit(id), CompositeUnit.MUL);
			} else if (name.equals("umul")) {
				String id = xmlE.getAttribute("id", null);
				cu.appendUnit(um.getUnit(id), CompositeUnit.MUL);
			} else if (name.equals("udiv")) {
				String id = xmlE.getAttribute("id", null);
				cu.appendUnit(um.getUnit(id), CompositeUnit.DIV);
			} else if (name.equals("subst")) {
				String sid = xmlE.getAttribute("id", null);
				String ssymbol = xmlE.getAttribute("symbol", null);
				String sname = xmlE.getAttribute("name", null);
				cu.setSubst(sid, ssymbol, sname);
			} else if (name.equals("converter")) {
				IConverter co = readConverter(xmlE);
				cu.setBaseConverter(co);
			}
		}
		String ts = xml.getAttribute("ts", null);
		Date tsd = null;
		if (ts!=null && !ts.equals("")) {
			tsd = decodeTime(ts);
		}
		if (tsd!=null) cu.setLastChange(tsd);
		checkUnit(cu);
		um.addUnit(cu);
	}

	/**
	 * 
	 * @param xml
	 */
	protected void readSubstUnit(IXMLElement xml) {
		IUnit baseUnit = null;
		Factor factor = null;
		IConverter conv = null;
		String id = xml.getAttribute("id", null);
		String symbol = xml.getAttribute("symbol", null);
		String name = xml.getAttribute("name", null);
		String dom = xml.getAttribute("dom", null);
		Domain domain = null;
		if (dom!=null) {
			domain = um.getDomain(dom);
		}
		String ubasid = xml.getAttribute("ubasid", null);
		if (ubasid!=null) {
			// base units are currently simply added to mul
			baseUnit = um.getUnit(ubasid);
			if (ubasid!=null && baseUnit==null) {
				throw new UnitsIoError("Could not find unit " + ubasid);
			}
		}

		Vector<IXMLElement> elements;

		if (id!=null || symbol!=null || name!=null) {
			if (id==null || symbol==null || name==null) {
				throw new UnitsIoError("if then all of subst* attributes must be set");
			}
			//cu.setSubst(substid, substsymbol, substname);
		}
		elements = xml.getChildren();
		for (IXMLElement xmlE : elements) {
			String elname = xmlE.getName();
			if (elname.equals("ufac")) {
				String fid = xmlE.getAttribute("id", null);
				factor = um.getFactor(id);
			} else if (elname.equals("ubas")) {
				// base units are currently simply added to mul
				String uid = xmlE.getAttribute("id", null);
				baseUnit = um.getUnit(id);
				if (uid!=null && baseUnit==null) {
					throw new UnitsIoError("Could not find unit " + uid);
				}
			} else if (elname.equals("converter")) {
				conv = readConverter(xmlE);
			}
		}
		SubstUnit cu = new SubstUnit(id, symbol, name, baseUnit);
		if (conv!=null) cu.setSubstConverter(conv);
		if (factor!=null) cu.setFactor(factor);
		cu.setDomain(domain);
		String ts = xml.getAttribute("ts", null);
		Date tsd = null;
		if (ts!=null && !ts.equals("")) {
			tsd = decodeTime(ts);
		}
		if (tsd!=null) cu.setLastChange(tsd);
		checkUnit(cu);
		um.addUnit(cu);
	}

	/**
	 * 
	 * @param xml
	 * @return	Returns the IConverter created from the xml entry.
	 */
	protected IConverter readConverter(IXMLElement xml) {
		AbstractConverter co = null;
		Double parM = null;
		Double parN = null;
		String type = xml.getAttribute("type", null);
		ConverterContext cc = null;

		String m = xml.getAttribute("m", null);
		String n = xml.getAttribute("n", null);
		String xm = xml.getAttribute("xm", null);
		String xn = xml.getAttribute("xn", null);
		if (xm!=null || xn!=null) {
			cc = new ConverterContext();
		}
		if (m!=null) parM = Double.valueOf(m);
		if (n!=null) parN = Double.valueOf(n);
		if (xm!=null) parM = evaluateXValue(xm);
		if (xn!=null) parN = evaluateXValue(xn);
		if (xm!=null) cc.add("xm", xm);
		if (xn!=null) cc.add("xn", xn);

		Vector<IXMLElement> elements;
		elements = xml.getChildrenNamed("param");
		for (IXMLElement xmlE : elements) {
			String name = xmlE.getAttribute("name", null);
			String value = xmlE.getAttribute("value", null);
			String xvalue = xmlE.getAttribute("xvalue", null);
			Double v = Double.valueOf(value);
			if (xvalue!=null) {
				if (cc==null) cc = new ConverterContext();
				String keyname = null;
				if (name.equals("m")) keyname = "xm";
				if (name.equals("n")) keyname = "xn";
				if (keyname==null) {
					throw new UnitsIoError("invalid parameter name in converter: " + name);
				}
				cc.add(keyname, xvalue);
				v = evaluateXValue(xvalue);
			}
			if (name.equals("m")) parM = v;
			if (name.equals("n")) parN = v;
		}

		if (type.equals("mul")) {
			co = new ConverterMul(parM);
		} else if (type.equals("lin")) {
			co = new ConverterLin(parM, parN);
		} else if (type.equals("idt")) {
			co = new ConverterIdent();
		} else {
			throw new UnitsIoError("converter type not supported: " + type);
		}
		if (cc!=null) co.setContext(cc);
		return co;
	}

	/**
	 * 
	 * @param xvalue
	 * @return	Returns the Double evaluated from the expression.
	 */
	protected Double evaluateXValue(String xvalue) {
		double v = 1.0;
		String [] xa = xvalue.split("(?=\\*|\\/)");
		for (String s : xa) {
			if (s.equals("")) continue;
			char op = s.charAt(0);
			String vs = s.substring(1);
			switch (op) {
			case '/': 
				break; 
			case '*': 
				break;
			default: 
				op = '*'; 
				vs = s; 
				break;
			}
			double v1 = Double.parseDouble(vs);
			if (op=='*') {
				v *= v1;
			} else {
				v /= v1;
			}
		}
		return Double.valueOf(v);
	}

	/**
	 * 
	 * @param xml
	 */
	protected void readQuantDef(IXMLElement xml) {
		Vector<IXMLElement> groups;
		groups = xml.getChildren();
		for (IXMLElement xmlE : groups) {
			String nameE = xmlE.getName();
			if (nameE.equals("quantities")) {
				readQuantities(xmlE);
			} else {
				// unknown entry. ignore it.
			}
		}
	}

	protected void readQuantities(IXMLElement xml) {
		Vector<IXMLElement> elements;
		elements = xml.getChildrenNamed("quantity");
		for (IXMLElement xmlE : elements) {
			// 		<factor id="T" symbol="T" name="Tera" value="1e12" />
			String id = xmlE.getAttribute("id", null);
			String symbol = xmlE.getAttribute("symbol", null);
			String name = xmlE.getAttribute("name", null);
			String uid = xmlE.getAttribute("unit", null);
			IQuantity q = UnitFactory.createQuantity(um, id, symbol, name, uid);
			um.addQuantity(q);
		}
	}

	/**
	 * 
	 * @param xml
	 */
	protected void readMeasDef(IXMLElement xml) {
		
	}


	protected void createFields(IXMLElement root) {
		IXMLElement xml = root.createElement("fields");
		root.addChild(xml);
		for (Field e : um.getFields()) {
			IXMLElement xmlE = xml.createElement("field");
			xml.addChild(xmlE);
			xmlE.setAttribute("id", e.getId());
			xmlE.setAttribute("name", e.getName());
		}
	}

	protected void createSubFields(IXMLElement root) {
		IXMLElement xml = root.createElement("subfields");
		root.addChild(xml);
		for (SubField e : um.getSubFields()) {
			IXMLElement xmlE = xml.createElement("subfield");
			xml.addChild(xmlE);
			xmlE.setAttribute("fid", e.getField().getId());
			xmlE.setAttribute("id", e.getId());
			xmlE.setAttribute("name", e.getName());
		}
	}

	protected void createSystemOfUnits(IXMLElement root) {
		IXMLElement xml = root.createElement("systemofunits");
		root.addChild(xml);
		for (SystemOfUnits e : um.getSystemOfUnits()) {
			IXMLElement xmlE = xml.createElement("sou");
			xml.addChild(xmlE);
			xmlE.setAttribute("id", e.getId());
			xmlE.setAttribute("symbol", e.getSymbol());
			xmlE.setAttribute("name", e.getName());
		}
	}

	protected void createDomains(IXMLElement root) {
		IXMLElement xml = root.createElement("domains");
		root.addChild(xml);
		for (Domain e : um.getDomains()) {
			IXMLElement xmlE = xml.createElement("domain");
			xml.addChild(xmlE);
			xmlE.setAttribute("id", e.getId());
			xmlE.setAttribute("name", e.getName());
		}
	}

	protected void createFactors(IXMLElement root) {
		IXMLElement xml = root.createElement("factors");
		root.addChild(xml);
		for (Factor e : um.getFactors()) {
			IXMLElement xmlE = xml.createElement("factor");
			xml.addChild(xmlE);
			xmlE.setAttribute("id", e.getId());
			xmlE.setAttribute("symbol", e.getSymbol());
			xmlE.setAttribute("name", e.getName());
			xmlE.setAttribute("value", Double.toString(e.getFactor()));
		}
	}


	protected void createUnits(IXMLElement root) {
		IXMLElement xml = root.createElement("units");
		root.addChild(xml);
		for (IUnit e : um.getUnits()) {
			if (e instanceof BaseUnit) {
				createUnitsBase(xml, (BaseUnit)e);
			} else if (e instanceof FactorUnit) {
				createUnitsFactor(xml, (FactorUnit)e);
			} else if (e instanceof SubstUnit) {
				createUnitsSubst(xml, (SubstUnit)e);
			} else if (e instanceof CompositeUnit) {
				createUnitsComp(xml, (CompositeUnit)e);
			}
		}
	}

	protected void createUnitsBase(IXMLElement root, BaseUnit e) {
		IXMLElement xmlE = root.createElement("baseunit");
		root.addChild(xmlE);
		xmlE.setAttribute("dom", e.getDomain().getId());
		xmlE.setAttribute("id", e.getId());
		xmlE.setAttribute("symbol", e.getSymbol());
		xmlE.setAttribute("name", e.getName());
		xmlE.setAttribute("ts", encodeTime(e.getLastChange()));
	}

	protected String encodeTime(Date date) {
		if (df==null) {
			// df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);
			df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
		return (date!=null) ? df.format(date) : "";
	}

	protected Date decodeTime(String code) {
		if (df==null) {
			/*
			Expected by Java: "Wednesday, January 11, 2006 at 8:25:25 PM GMT"
			Original, no bad: "Wednesday, January 11, 2006 8:25:25 PM GMT"
			 */
			df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
			fallbackDf = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
		try {
			return df.parse(code);
		} catch (ParseException e) {
			// e.printStackTrace();
			try {
				return fallbackDf.parse(code);
			} catch (ParseException e2) {
				code = code.replaceFirst("(, [\\d]{4})", "$1 at");
				try {
					return fallbackDf.parse(code);
				} catch (ParseException e3) {
					throw new UnitsIoError("Could not parse timestamp: " + code);
				}
			}
		}
	}

	protected void createUnitsFactor(IXMLElement root, FactorUnit e) {
		IXMLElement xmlE = root.createElement("factorunit");
		root.addChild(xmlE);
		xmlE.setAttribute("dom", e.getDomain().getId());
		xmlE.setAttribute("fid", e.getFactor().getId());
		xmlE.setAttribute("ubasid", e.getBaseUnit().getId());
		xmlE.setAttribute("ts", encodeTime(e.getLastChange()));
	}

//	<substunit dom="@tem" ubasid=".K" id=".°N" symbol="°N" name="Newton">
//	<!-- TK = TN*(100/33) + 273.15 -->
//	<converter type="lin">
//		<param name="m" value="3.030303030" xvalue="100/33" />
//		<param name="n" value="273.15" />
//	</converter>
//	</substunit>

	protected void createUnitsSubst(IXMLElement root, SubstUnit e) {
		IXMLElement xmlE = root.createElement("substunit");
		root.addChild(xmlE);
		xmlE.setAttribute("dom", e.getDomain().getId());
		xmlE.setAttribute("ubasid", e.getBaseUnit().getId());
		xmlE.setAttribute("id", e.getId());
		xmlE.setAttribute("symbol", e.getSymbol());
		xmlE.setAttribute("name", e.getName());
		xmlE.setAttribute("ts", encodeTime(e.getLastChange()));
		if (e.getSubstConverter()!=null) 
			createUnitsConverter(xmlE, e.getSubstConverter());
	}

	protected void createUnitsConverter(IXMLElement root, IConverter e) {
		IXMLElement xmlE = root.createElement("converter");
		root.addChild(xmlE);
		String type = null;
		String m = null;
		String xm = null; 
		String n = null;
		String xn = null;
		if (e instanceof ConverterIdent) {
			type = "idt";
		} else if (e instanceof ConverterMul) {
			type = "mul";
			m = ((ConverterMul)e).getM().toString();
		} else if (e instanceof ConverterLin) {
			type = "lin";
			m = ((ConverterLin)e).getM().toString();
			n = ((ConverterLin)e).getN().toString();
		} else {
			throw new UnitsIoError("invalid converter type: " + 
					e.getClass().getSimpleName());
		}
		xmlE.setAttribute("type", type);
		ConverterContext cc = e.getContext();
		if (cc!=null) {
			String t;
			t = cc.getValue("xm");
			if (t!=null) xm = t;
			t = cc.getValue("xn");
			if (t!=null) xn = t;
		}

		if (m!=null) {
			IXMLElement xmlP = xmlE.createElement("param");
			xmlE.addChild(xmlP);
			xmlP.setAttribute("name", "m");
			xmlP.setAttribute("value", m);
			if (xm!=null)
				xmlP.setAttribute("xvalue", xm);
		}
		if (n!=null) {
			IXMLElement xmlP = xmlE.createElement("param");
			xmlE.addChild(xmlP);
			xmlP.setAttribute("name", "n");
			xmlP.setAttribute("value", n);
			if (xn!=null)
				xmlP.setAttribute("xvalue", xn);
		}
	}

//	<compunit dom="@frc" >
//	<subst id=".N" symbol="N" name="Newton" />
//	<umul id="*k.g" />
//	<umul id=".m" />
//	<udiv id=".s" />
//	<udiv id=".s" />
//	</compunit>
//	<compunit dom="@sts">
//	<ufac id="*M" />
//	<umul id=".Pa" />
//	</compunit>
	protected void createUnitsComp(IXMLElement root, CompositeUnit e) {
		IXMLElement xmlE = root.createElement("compunit");
		root.addChild(xmlE);
		xmlE.setAttribute("dom", e.getDomain().getId());
		xmlE.setAttribute("ts", encodeTime(e.getLastChange()));

		String substid = e.getSubstId();
		String substsymbol = e.getSubstSymbol();
		String substname = e.getSubstName();
		if (substid!=null || substsymbol!=null || substname!=null) {
			if (substid==null || substsymbol==null || substname==null) {
				throw new UnitsIoError("subst information structure error");
			}
			IXMLElement xmlP = xmlE.createElement("subst");
			xmlE.addChild(xmlP);
			xmlP.setAttribute("id", substid);
			xmlP.setAttribute("symbol", substsymbol);
			xmlP.setAttribute("name", substname);
		}

		Factor factor = e.getFactor();
		if (factor!=null) {
			IXMLElement xmlP = xmlE.createElement("ufac");
			xmlE.addChild(xmlP);
			xmlP.setAttribute("id", factor.getId());
		}

		if (e.getBaseConverter()!=null) 
			createUnitsConverter(xmlE, e.getBaseConverter());

		Vector<IUnit> v;
		v = e.getUnits(CompositeUnit.MUL);
		for (IUnit u : v) {
			IXMLElement xmlP = xmlE.createElement("umul");
			xmlE.addChild(xmlP);
			xmlP.setAttribute("id", u.getId());
		}
		v = e.getUnits(CompositeUnit.DIV);
		for (IUnit u : v) {
			IXMLElement xmlP = xmlE.createElement("udiv");
			xmlE.addChild(xmlP);
			xmlP.setAttribute("id", u.getId());
		}
	}

	protected void createUnitContexts(IXMLElement root) {
		IXMLElement xml = root.createElement("unitcontexts");
		root.addChild(xml);
		for (IUnit u : um.getUnits()) {
			UnitContext uc = u.getContext();
			if (uc==null) continue;
			IXMLElement xmlE = xml.createElement("uc");
			xml.addChild(xmlE);
			xmlE.setAttribute("dom", u.getDomain().getId());
			xmlE.setAttribute("uid", u.getId());

			String idlist;

			idlist = null;
			for (SystemOfUnits e : uc.getSystemOfUnits()) {
				if (idlist==null) {
					idlist = e.getId();
				} else {
					idlist += "," + e.getId();
				}
			}
			if (idlist!=null) xmlE.setAttribute("sou", idlist);

			idlist = null;
			for (SubField e : uc.getSubFields()) {
				if (idlist==null) {
					idlist = e.getId();
				} else {
					idlist += "," + e.getId();
				}
			}
			if (idlist!=null) xmlE.setAttribute("sf", idlist);
		}
	}

	protected void createQuantities(IXMLElement root) {
		IXMLElement xml = root.createElement("quantities");
		root.addChild(xml);
		for (IQuantity e : um.getQuantities()) {
			IXMLElement xmlE = xml.createElement("quantity");
			xml.addChild(xmlE);
			xmlE.setAttribute("id", e.getId());
			xmlE.setAttribute("symbol", e.getSymbol());
			xmlE.setAttribute("name", e.getName());
			xmlE.setAttribute("unit", e.getUnit().getId());
		}
	}
}
