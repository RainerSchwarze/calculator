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
package de.admadic.calculator.modules.indxp.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
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
 * FIXME: fix error handling
 */
public class InDXpDataIO {
	InDXpData data;
	Locale locale;

	Hashtable<Integer,Factor> idToFactor;
	Hashtable<Integer,FactorInteraction> idToFactorInteraction;
	Hashtable<Integer,Integer> idToLevel;
	Hashtable<Integer,Run> idToRun;
	Hashtable<Integer,ExpResults> idToExpResults;
	
	Hashtable<Factor,Integer> factorToId;
	Hashtable<FactorInteraction,Integer> factorInteractionToId;
	Hashtable<Integer,Integer> levelToId;
	Hashtable<Run,Integer> runToId;
	Hashtable<ExpResults,Integer> expResultsToId;

	/**
	 * @param data 
	 * @param locale 
	 * 
	 */
	public InDXpDataIO(InDXpData data, Locale locale) {
		super();
		this.data = data;
		this.locale = locale;
	}

	/**
	 * @param file
	 */
	public void load(File file) {
		try {
			IXMLParser parser;
			String fname = file.toString();
			fname = FileUtil.fixFileName1(fname);
			parser = XMLParserFactory.createDefaultXMLParser();
			IXMLReader reader = StdXMLReader.fileReader(fname);
			parser.setReader(reader);
			IXMLElement xml = (IXMLElement)parser.parse();
			IXMLElement xml2;

			if (!xml.getName().equals("indxpdata")) {
				throw new XmlIoError("root is not indxpdata.");
			}

			xml2 = xml.getFirstChildNamed("dataitemstatuslist");
			if (xml2==null) {
				throw new XmlIoError("indxpdata.dataitemstatuslist not found.");
			}
			readDataItemStatusList(xml2);

			xml2 = xml.getFirstChildNamed("factors");
			if (xml2==null) {
				throw new XmlIoError("indxpdata.factors not found.");
			}
			readFactors(xml2);

			xml2 = xml.getFirstChildNamed("factorinteractions");
			if (xml2==null) {
				throw new XmlIoError("indxpdata.factorinteractions not found.");
			}
			readFactorInteractions(xml2);

			xml2 = xml.getFirstChildNamed("levels");
			if (xml2==null) {
				throw new XmlIoError("indxpdata.levels not found.");
			}
			// levels are not yet read, only initialised
			readLevels(xml2);

			xml2 = xml.getFirstChildNamed("runs");
			if (xml2==null) {
				throw new XmlIoError("indxpdata.runs not found.");
			}
			readRuns(xml2);

			xml2 = xml.getFirstChildNamed("expresults");
			if (xml2==null) {
				throw new XmlIoError("indxpdata.expresults not found.");
			}
			readExpResults(xml2);

			readLevelAnalysis(xml);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new XmlIoError("Load Error: " + e.getMessage());
		}
	}

	/**
	 * @param file
	 */
	public void save(File file) {
		IXMLElement indxpRoot, tmp;
		indxpRoot = new XMLElement("indxpdata");

		tmp = new XMLElement("dataitemstatuslist");
		generateDataItemStatusList(tmp);
		indxpRoot.addChild(tmp);
		tmp = new XMLElement("factors");
		generateFactors(tmp);
		indxpRoot.addChild(tmp);
		tmp = new XMLElement("factorinteractions");
		generateFactorInteractions(tmp);
		indxpRoot.addChild(tmp);
		tmp = new XMLElement("levels");
		generateLevels(tmp);
		indxpRoot.addChild(tmp);
		tmp = new XMLElement("runs");
		generateRuns(tmp);
		indxpRoot.addChild(tmp);
		tmp = new XMLElement("expresults");
		generateExpResults(tmp);
		indxpRoot.addChild(tmp);
		tmp = new XMLElement("levelanalysis");
		generateExpResults(tmp, data.getLevelAnalysisYAvg());
		indxpRoot.addChild(tmp);
		tmp = new XMLElement("levelanalysis");
		generateExpResults(tmp, data.getLevelAnalysisSigma());
		indxpRoot.addChild(tmp);

		try {
			java.io.Writer output;
			output = new FileWriter(file);
			output.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			IXMLElement xmltree = indxpRoot;
			XMLWriter xmlwriter = new XMLWriter(output);
			xmlwriter.write(xmltree, true);	
		} catch (Exception e) {
			// e.printStackTrace();
			throw new XmlIoError("Save Error: " + e.getMessage());
		}
	}


	/**
	 * @param file
	 */
	public void exportTxt(File file) {
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bfw = new BufferedWriter(fw);

			exportTxtFactors(bfw);
			exportTxtFactorInteractions(bfw);
			exportTxtRuns(bfw);
			exportTxtExpResults(bfw);
			exportTxtLevelAnalysis(bfw, data.getLevelAnalysisYAvg());
			exportTxtLevelAnalysis(bfw, data.getLevelAnalysisSigma());
			bfw.close();
		} catch (Exception e) {
			// e.printStackTrace();
			throw new XmlIoError("Export Error: " + e.getMessage());
		}
	}


	private void generateDataItemStatusList(IXMLElement xml) {
		generateDataItemStatusList(xml, "FACTORS", DataItemStatus.DI_FACTORS);
		generateDataItemStatusList(xml, "FACTORINTERACTIONS", DataItemStatus.DI_FACTORINTERACTIONS);
		generateDataItemStatusList(xml, "RUNS", DataItemStatus.DI_RUNS);
		generateDataItemStatusList(xml, "EXPRESULTS", DataItemStatus.DI_EXPRESULTS);
	}

	private void generateDataItemStatusList(IXMLElement xml, String id, int diId) {
		IXMLElement xml2 = new XMLElement("dataitemstatus");
		xml2.setAttribute("id", id);
		String dsV;
		switch (data.getDataItemStatus(diId).getDataStatus()) {
		case DataItemStatus.DATA_UNINITIALIZED: dsV = "UNINITIALIZED"; break;
		case DataItemStatus.DATA_CREATED: dsV = "CREATED"; break;
		case DataItemStatus.DATA_FILLED_PARTIALLY: dsV = "FILLEDPARTIALLY"; break;
		case DataItemStatus.DATA_FILLED: dsV = "FILLED"; break;
		default:
			throw new XmlIoError("Data error in DataItemStatus: unknown dataStatus");
		}
		xml2.setAttribute("dataStatus", dsV);
		String lsV = "";
		if (data.getDataItemStatus(diId).isDataLocked()) {
			lsV += ",DATA";
		}
		if (data.getDataItemStatus(diId).isStructureLocked()) {
			lsV += ",STRUCT";
		}
		if (lsV.startsWith(",")) lsV = lsV.substring(1);
		xml2.setAttribute("lockStatus", lsV);
		xml.addChild(xml2);
	}

	private void generateFactors(IXMLElement xml) {
		factorToId = new Hashtable<Factor,Integer>();
		int curId = 0;
		for (int i=0; i<data.getFactorList().size(); i++) {
			Factor f = data.getFactorList().get(i);
			IXMLElement xmlF = new XMLElement("factor");
			xmlF.setAttribute("id", String.valueOf(curId));
			xmlF.setAttribute("name", f.getName());
			xmlF.setAttribute("entity", f.getEntity());
			xmlF.setAttribute("unit", f.getUnit());
			xmlF.setAttribute("valueLow", f.getValueLow());
			xmlF.setAttribute("valueHigh", f.getValueHigh());
			xml.addChild(xmlF);
			factorToId.put(f, Integer.valueOf(curId));

			curId++;
		}
	}

	private void generateFactorInteractions(IXMLElement xml) {
		factorInteractionToId = new Hashtable<FactorInteraction,Integer>();
		int curId = 0;
		for (int i=0; i<data.getFactorInteractionList().size(); i++) {
			FactorInteraction fi = data.getFactorInteractionList().get(i);
			IXMLElement xmlF = new XMLElement("factorinteraction");
			xmlF.setAttribute("id", String.valueOf(curId));
			if (fi.isAliased()) {
				xmlF.setAttribute("alias", fi.getAliasName());
			}
			for (int j=0; j<fi.getFactors().size(); j++) {
				Factor f = fi.getFactors().get(j);
				IXMLElement xml2 = new XMLElement("item");
				xml2.setAttribute("factorId", factorToId.get(f).toString());
				xmlF.addChild(xml2);
			}
			xml.addChild(xmlF);
			factorInteractionToId.put(fi, Integer.valueOf(curId));

			curId++;
		}
	}

	private void generateLevels(IXMLElement xml) {
		levelToId = new Hashtable<Integer,Integer>();
		int curId = 0;
		IXMLElement xmlF;

		xmlF = new XMLElement("level");
		xmlF.setAttribute("id", String.valueOf(curId));
		levelToId.put(Integer.valueOf(-1), Integer.valueOf(curId));
		xmlF.setAttribute("name", "low");
		xmlF.setAttribute("value", "-1");
		xml.addChild(xmlF);

		curId++;
	
		xmlF = new XMLElement("level");
		xmlF.setAttribute("id", String.valueOf(curId));
		levelToId.put(Integer.valueOf(+1), Integer.valueOf(curId));
		xmlF.setAttribute("name", "high");
		xmlF.setAttribute("value", "+1");
		xml.addChild(xmlF);
	}

	private void generateRuns(IXMLElement xml) {
		runToId = new Hashtable<Run,Integer>();
		int curId = 0;
		for (int i=0; i<data.getRunList().size(); i++) {
			Run r = data.getRunList().get(i);
			IXMLElement xmlF = new XMLElement("run");
			xmlF.setAttribute("id", String.valueOf(curId));
			xmlF.setAttribute("idname", r.getId());
			for (Factor f : data.getFactorList()) {
				IXMLElement xml2 = new XMLElement("item");
				Integer level = r.getFactorLevel(f);
				xml2.setAttribute("factorId", factorToId.get(f).toString());
				xml2.setAttribute("levelId", levelToId.get(level).toString());
				xmlF.addChild(xml2);
			}
			xml.addChild(xmlF);
			runToId.put(r, Integer.valueOf(curId));

			curId++;
		}
	}

	private String Double2String(Double d) {
		if (d==null) return "null";
		return d.toString();
	}
	
	private void generateExpResults(IXMLElement xml) {
		expResultsToId = new Hashtable<ExpResults,Integer>();
		int curId = 0;
		boolean replicateCountMarked = false;
		for (int i=0; i<data.getExpResultsList().size(); i++) {
			ExpResults er = data.getExpResultsList().get(i);
			IXMLElement xmlF = new XMLElement("expresult");
			xmlF.setAttribute("id", String.valueOf(curId));
			if (!replicateCountMarked) {
				xml.setAttribute(
						"replicates", 
						String.valueOf(er.getReplicateCount()));
				replicateCountMarked = true;
			}
			Integer runId = runToId.get(data.getRunList().get(i));
			xmlF.setAttribute("runId", runId.toString());
			for (int j=0; j<er.getReplicateCount(); j++) {
				Double v = er.getResult(j);
				IXMLElement xml2 = new XMLElement("y");
				xml2.setAttribute("value", Double2String(v));
				xmlF.addChild(xml2);
			}
			{
				IXMLElement xml2 = new XMLElement("yavg");
				xml2.setAttribute("value", Double2String(er.getYAvg()));
				xmlF.addChild(xml2);
			}
			{
				IXMLElement xml2 = new XMLElement("sigma");
				xml2.setAttribute("value", Double2String(er.getSigma()));
				xmlF.addChild(xml2);
			}
			xml.addChild(xmlF);
			expResultsToId.put(er, Integer.valueOf(curId));

			curId++;
		}
	}

	private void generateExpResults(
			IXMLElement xml, LevelAnalysis levelAnalysis) {
		xml.setAttribute("name", levelAnalysis.getName());
		IXMLElement xml1;

		xml1 = new XMLElement("resultset");
		xml1.setAttribute("type", "level");
		xml1.setAttribute(
				"levelId", levelToId.get(Integer.valueOf(-1)).toString());
		for (int i=0; i<data.getFactorInteractionList().size(); i++) {
			FactorInteraction fi = data.getFactorInteractionList().get(i);
			IXMLElement xmlF = new XMLElement("fiitem");
			xmlF.setAttribute(
					"fiId", factorInteractionToId.get(fi).toString());
			xmlF.setAttribute(
					"value", Double2String(levelAnalysis.getLowValue(fi)));
			xml1.addChild(xmlF);
		}
		xml.addChild(xml1);

		xml1 = new XMLElement("resultset");
		xml1.setAttribute("type", "level");
		xml1.setAttribute(
				"levelId", levelToId.get(Integer.valueOf(+1)).toString());
		for (int i=0; i<data.getFactorInteractionList().size(); i++) {
			FactorInteraction fi = data.getFactorInteractionList().get(i);
			IXMLElement xmlF = new XMLElement("fiitem");
			xmlF.setAttribute(
					"fiId", factorInteractionToId.get(fi).toString());
			xmlF.setAttribute(
					"value", Double2String(levelAnalysis.getHighValue(fi)));
			xml1.addChild(xmlF);
		}
		xml.addChild(xml1);


		xml1 = new XMLElement("resultset");
		xml1.setAttribute("type", "delta");
		for (int i=0; i<data.getFactorInteractionList().size(); i++) {
			FactorInteraction fi = data.getFactorInteractionList().get(i);
			IXMLElement xmlF = new XMLElement("fiitem");
			xmlF.setAttribute(
					"fiId", factorInteractionToId.get(fi).toString());
			xmlF.setAttribute(
					"value", Double2String(levelAnalysis.getDeltaValue(fi)));
			xml1.addChild(xmlF);
		}
		xml.addChild(xml1);
	}



	private void readDataItemStatusList(IXMLElement xml) {
		Vector<?> v = xml.getChildrenNamed("dataitemstatus");
		for (Object o : v) {
			IXMLElement xmlE = (IXMLElement)o;
			DataItemStatus dis = new DataItemStatus();
			String ds = xmlE.getAttribute("dataStatus", "");
			if (ds.equals("UNINITIALIZED")) {
				dis.setDataStatus(DataItemStatus.DATA_UNINITIALIZED);
			} else if (ds.equals("CREATED")) {
				dis.setDataStatus(DataItemStatus.DATA_CREATED);
			} else if (ds.equals("FILLEDPARTIALLY")) {
				dis.setDataStatus(DataItemStatus.DATA_FILLED_PARTIALLY);
			} else if (ds.equals("FILLED")) {
				dis.setDataStatus(DataItemStatus.DATA_FILLED);
			} else if (ds.equals("")) {
				// nothing
			} else {
				throw new XmlIoError("dataitemstatus:dataStatus invalid.");
			}

			String ls = xmlE.getAttribute("lockStatus", "");
			String [] lsa = ls.split(",");
			for (String lst : lsa) {
				if (lst.equals("DATA")) {
					dis.lock(DataItemStatus.LOCKED_DATA);
				} else if (lst.equals("STRUCT")) {
					dis.lock(DataItemStatus.LOCKED_STRUCT);
				} else if (lst.equals("")) {
					// nothing
				} else {
					throw new XmlIoError("dataitemstatus.lockStatus invalid: " + lst);
				}
			}

			String id = xmlE.getAttribute("id", null);
			if (id==null) {
				throw new XmlIoError("dataitemstatus.id not defined.");
			}
			if (id.equals("FACTORS")) {
				data.setDataItemStatus(DataItemStatus.DI_FACTORS, dis);
			} else if (id.equals("FACTORINTERACTIONS")) {
				data.setDataItemStatus(DataItemStatus.DI_FACTORINTERACTIONS, dis);
			} else if (id.equals("RUNS")) {
				data.setDataItemStatus(DataItemStatus.DI_RUNS, dis);
			} else if (id.equals("EXPRESULTS")) {
				data.setDataItemStatus(DataItemStatus.DI_EXPRESULTS, dis);
			} else {
				throw new XmlIoError("dataitemstatus.id invalid.");
			}
 		} // (for elements in dataitemstatuslist)
	}

	private void readFactors(IXMLElement xml) {
		Vector<?> v = xml.getChildrenNamed("factor");
		idToFactor = new Hashtable<Integer,Factor>();
		data.getFactorList().clear();
		for (Object o : v) {
			IXMLElement xmlE = (IXMLElement)o;
			Factor f = new Factor();
			String id = xmlE.getAttribute("id", null);
			if (id==null) {
				throw new XmlIoError("factor.id not found.");
			}
			Integer idi;
			try {
				idi = Integer.valueOf(id);
			} catch (NumberFormatException e) {
				throw new XmlIoError("factor.id could not be parsed.");
			}
			idToFactor.put(idi, f);
			String tmpS;
			tmpS = xmlE.getAttribute("name", null);
			if (tmpS==null) throw new XmlIoError("factor.name not found.");
			f.setName(tmpS);
			tmpS = xmlE.getAttribute("entity", null);
			if (tmpS==null) throw new XmlIoError("factor.entity not found.");
			f.setEntity(tmpS);
			tmpS = xmlE.getAttribute("unit", null);
			if (tmpS==null) throw new XmlIoError("factor.unit not found.");
			f.setUnit(tmpS);
			tmpS = xmlE.getAttribute("valueLow", null);
			if (tmpS==null) throw new XmlIoError("factor.valueLow not found.");
			f.setValueLow(tmpS);
			tmpS = xmlE.getAttribute("valueHigh", null);
			if (tmpS==null) throw new XmlIoError("factor.valueHigh not found.");
			f.setValueHigh(tmpS);
			data.getFactorList().add(f);
		}
	}

	private void readFactorInteractions(IXMLElement xml) {
		Vector<?> v = xml.getChildrenNamed("factorinteraction");
		idToFactorInteraction = new Hashtable<Integer,FactorInteraction>();
		data.getFactorInteractionList().clear();
		for (Object o : v) {
			IXMLElement xmlE = (IXMLElement)o;
			FactorInteraction fi = new FactorInteraction();
			Integer idi = parseId(xmlE, "id", "factorinteraction:id");
			idToFactorInteraction.put(idi, fi);

			Vector<?> flist = xmlE.getChildrenNamed("item");
			for (Object oi : flist) {
				IXMLElement xmlEi = (IXMLElement)oi;

				Integer fidi = parseId(xmlEi, "factorId", "factorinteraction.item:factorId");
				if (!idToFactor.containsKey(fidi)) {
					throw new XmlIoError("reference to factorId which does not exist");
				}
				fi.addFactor(idToFactor.get(fidi));
			}

			String tmpS;
			tmpS = xmlE.getAttribute("alias", null);
			if (tmpS==null) {
				// no alias
			} else {
				fi.setAliased(true, tmpS);
			}
			data.getFactorInteractionList().add(fi);
		}
	}

	private void readLevels(IXMLElement xml) {
		if (xml!=null) { /* nothing */ }
		idToLevel = new Hashtable<Integer,Integer>();
		idToLevel.put(Integer.valueOf(0), Integer.valueOf(-1));
		idToLevel.put(Integer.valueOf(1), Integer.valueOf(+1));
	}

	private void readRuns(IXMLElement xml) {
		Vector<?> v = xml.getChildrenNamed("run");
		idToRun = new Hashtable<Integer,Run>();
		data.getRunList().clear();
		for (Object o : v) {
			IXMLElement xmlE = (IXMLElement)o;
			Run r = new Run(
					data.getFactorList(), 
					data.getFactorInteractionList());
			Integer idi = parseId(xmlE, "id", "run:id");
			idToRun.put(idi, r);
			String tmpS;
			tmpS = xmlE.getAttribute("idname", null);
			if (tmpS==null) throw new XmlIoError("run.idname not found.");
			r.setId(tmpS);

			Vector<?> flist = xmlE.getChildrenNamed("item");
			for (Object oi : flist) {
				IXMLElement xmlEi = (IXMLElement)oi;

				Integer fidi = parseId(xmlEi, "factorId", "run.item:factorId");
				if (!idToFactor.containsKey(fidi)) {
					throw new XmlIoError("reference to factorId which does not exist");
				}

				Integer lidi = parseId(xmlEi, "levelId", "run.item:levelId");
				if (!idToLevel.containsKey(lidi)) {
					throw new XmlIoError("reference to levelId which does not exist");
				}

				r.setLevel(idToFactor.get(fidi), idToLevel.get(lidi));
			}

			r.updateFactorInteractionLevels();
			data.getRunList().add(r);
		}
	}

	private void readExpResults(IXMLElement xml) {
		int replicateCount = 0;
		String rcS = xml.getAttribute("replicates", null);
		if (rcS==null) throw new XmlIoError("expresults:replicates not found.");
		try {
			replicateCount = Integer.parseInt(rcS);
		} catch (NumberFormatException e) {
			throw new XmlIoError("expresults:replicates could not be parsed.");
		}
		if (replicateCount<0 || replicateCount>=100) {
			throw new XmlIoError("expresults:replicates out of range.");
		}
		Vector<?> v = xml.getChildrenNamed("expresult");
		idToExpResults = new Hashtable<Integer,ExpResults>();
		data.getExpResultsList().clear();
		for (Object o : v) {
			IXMLElement xmlE = (IXMLElement)o;
			ExpResults er = new ExpResults(replicateCount);

			Integer idi = parseId(xmlE, "id", "expresult:id");
			idToExpResults.put(idi, er);

//			Integer ridi = parseId(xmlE, "runId", "expresult:runId");
//			// FIXME: check ordering of ExpResults entries!

			Vector<?> vy = xmlE.getChildrenNamed("y");
			int ridx = -1;
			for (Object oy : vy) {
				ridx++;
				IXMLElement xmlY = (IXMLElement)oy;
				Double dy = parseDouble(xmlY, "value", "expresult.y:value");
				er.setResult(ridx, dy);
			}
			if ((ridx+1)!=replicateCount) {
				// warning?
			}

			IXMLElement xmlS;

			xmlS = xmlE.getFirstChildNamed("yavg");
			if (xmlS!=null) {
				Double dy = parseDouble(xmlS, "value", "expresult.yavg:value");
				er.setYAvg(dy);
			}
			xmlS = xmlE.getFirstChildNamed("sigma");
			if (xmlS!=null) {
				Double dy = parseDouble(xmlS, "value", "expresult.sigma:value");
				er.setSigma(dy);
			}

			data.getExpResultsList().add(er);
		}
	}

	private void readLevelAnalysis(IXMLElement xml) {
		Vector<?> v = xml.getChildrenNamed("levelanalysis");
		for (Object o : v) {
			IXMLElement xmlE = (IXMLElement)o;
			String name = xmlE.getAttribute("name", null);
			if (name==null) {
				throw new XmlIoError("indxpdata.levelanalysis:name not found.");
			}
			if (name.equals("YAvg")) {
				readLevelAnalysisSpec(xmlE, data.getLevelAnalysisYAvg());
			} else if (name.equals("Sigma")) {
				readLevelAnalysisSpec(xmlE, data.getLevelAnalysisSigma());
			} else {
				throw new XmlIoError("indxpdata.levelanalysis:name invalid.");
			}
		}
	}

	private Integer parseId(
			IXMLElement xml, String idname, String desc) {
		String idS = xml.getAttribute(idname, null);
		if (idS==null) 
			throw new XmlIoError(
					desc + " not found.");
		Integer idi;
		try {
			idi = Integer.valueOf(idS);
		} catch (NumberFormatException e) {
			throw new XmlIoError(
					desc + " could not be parsed.");
		}
		return idi;
	}

	private Double parseDouble(
			IXMLElement xml, String vname, String desc) {
		String vs = xml.getAttribute(vname, null);
		if (vs==null) 
			throw new XmlIoError(
					desc + " not found.");
		if (vs.equals("null"))
			return null;
		Double v;
		try {
			v = Double.valueOf(vs);
		} catch (NumberFormatException e) {
			throw new XmlIoError(
					desc + " could not be parsed.");
		}
		return v;
	}

	private void readLevelAnalysisSpec(
			IXMLElement xml, LevelAnalysis levelAnalysis) {
		Vector<?> v = xml.getChildrenNamed("resultset");
		for (Object o : v) {
			IXMLElement xmlE = (IXMLElement)o;
			int levanId;
			String typeS = xmlE.getAttribute("type", null);
			if (typeS==null) {
				throw new XmlIoError("levelanalysis.resultset:type not found.");
			}
			if (typeS.equals("level")) {
				Integer lid = parseId(xmlE, "levelId", "levelanalysis.resultset:levelId");
				if (!idToLevel.containsKey(lid)) {
					throw new XmlIoError("levelanalysis.resultset:levelId level does not exist.");
				}
				Integer level = idToLevel.get(lid);
				if (level.intValue()==-1) {
					levanId = LevelAnalysis.RES_LEV_LOW;
				} else {
					levanId = LevelAnalysis.RES_LEV_HIGH;
				}
			} else if (typeS.equals("delta")) {
				levanId = LevelAnalysis.RES_LEV_DELTA;
			} else {
				throw new XmlIoError("levelanalysis.resultset:type invalid entry.");
			}

			Vector<?> vf = xmlE.getChildrenNamed("fiitem");
			for (Object of : vf) {
				IXMLElement xmlF = (IXMLElement)of;
				Integer fid = parseId(xmlF, "fiId", "resultset.fiitem:fiId");
				Double fiv = parseDouble(xmlF, "value", "resultset.fiitem:value");
				if (!idToFactorInteraction.containsKey(fid))
					throw new XmlIoError("resultset:fiitem:fiId refers to non-existent entry.");
				FactorInteraction fi = idToFactorInteraction.get(fid);
				levelAnalysis.setStatValue(levanId, fi, fiv);
			}
		}
	}


	private void exportTxtFactors(BufferedWriter bfw) throws IOException {
		if (data.getFactorList()==null) return;
		bfw.write("Factors:\n");
		bfw.write("Name\tEntity\tUnit\tLow\tHigh\n");
		for (Factor f : data.getFactorList()) {
			bfw.write(
					f.getName() + "\t" + 
					f.getEntity() + "\t" +
					f.getUnit() + "\t" + 
					f.getValueLow() + "\t" +
					f.getValueHigh() + "\n");
		}
		bfw.write("\n");
	}

	private void exportTxtFactorInteractions(BufferedWriter bfw) throws IOException {
		if (data.getFactorInteractionList()==null) return;
		bfw.write("Factor Interactions:\n");
		bfw.write("Name\tAlias\tEntities\n");
		for (FactorInteraction fi : data.getFactorInteractionList()) {
			if (fi.isAliased()) {
				bfw.write(
						fi.getAliasName() + "\t" + 
						fi.getDisplay() + "\t" +
						fi.getEntitiesDisplay() + "\n");
			} else {
				bfw.write(
						fi.getDisplay() + "\t" + 
						"-" + "\t" +
						fi.getEntitiesDisplay() + "\n");
			}
		}
		bfw.write("\n");
	}

	private void exportTxtRuns(BufferedWriter bfw) throws IOException {
		if (data.getRunList()==null) return;
		bfw.write("Runs:\n");
		bfw.write("Run");
		for (FactorInteraction fi : data.getFactorInteractionList()) {
			bfw.write("\t" + fi.getDisplay());
		}
		bfw.write("\n");
		for (Run r : data.getRunList()) {
			bfw.write(r.getId());
			for (FactorInteraction fi : data.getFactorInteractionList()) {
				bfw.write("\t" + r.getFiLevel(fi).toString());
			}
			bfw.write("\n");
		}
		bfw.write("\n");
	}

	private String formatDouble(Double v) {
		if (v==null) return "null";
		Object [] da = {null};
		da[0] = v;
		String s = String.format(locale, "%f", da);
		da[0] = null;
		return s;
	}

	private void exportTxtExpResults(BufferedWriter bfw) throws IOException {
		if (data.getExpResultsList()==null) return;
		bfw.write("Experimental Results:\n");
		bfw.write("Run");
		for (int i=1; i<=data.replicateCount; i++) {
			bfw.write("\ty" + i);
		}
		bfw.write("\ty-avg\ts*\n");
		for (ExpResults er : data.getExpResultsList()) {
			for (int i=0; i<er.getReplicateCount(); i++) {
				bfw.write(formatDouble(er.getResult(i)) + "\t");
			}
			bfw.write(formatDouble(er.getYAvg()) + "\t");
			bfw.write(formatDouble(er.getSigma()) + "\n");
		}
		bfw.write("\n");
	}

	private void exportTxtLevelAnalysis(
			BufferedWriter bfw, 
			LevelAnalysis levelAnalysis) throws IOException {
		if (levelAnalysis==null) return;
		bfw.write("Level Analysis for " + levelAnalysis.getName() + "\n");
		bfw.write("Group");
		for (FactorInteraction fi : data.getFactorInteractionList()) {
			bfw.write("\t" + fi.getDisplay());
		}
		bfw.write("\n");
		int [] ids = {
				LevelAnalysis.RES_LEV_HIGH, 
				LevelAnalysis.RES_LEV_LOW, 
				LevelAnalysis.RES_LEV_DELTA, 
				};
		for (int id : ids) {
			switch (id) {
			case LevelAnalysis.RES_LEV_HIGH: bfw.write("avg@+1"); break;
			case LevelAnalysis.RES_LEV_LOW: bfw.write("avg@-1"); break;
			case LevelAnalysis.RES_LEV_DELTA: bfw.write("delta"); break;
			}
			for (FactorInteraction fi : data.getFactorInteractionList()) {
				bfw.write("\t" + formatDouble(levelAnalysis.getStatValue(id, fi)));
			}
			bfw.write("\n");
		}
		bfw.write("\n");
	}
}
