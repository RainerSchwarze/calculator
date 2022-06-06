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
import java.util.Locale;

/**
 * @author Rainer Schwarze
 *
 */
public class UnitTester {
	UnitManager unitManager;

	/**
	 * 
	 */
	public UnitTester() {
		super();
		unitManager = new UnitManager();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UnitTester app = new UnitTester();
		app.run();
	}

	private void createFactors() {
		unitManager.addFactor(UnitFactory.createFactor("*T", "T", "Tera", 1e12));
		unitManager.addFactor(UnitFactory.createFactor("*G", "G", "Giga", 1e9));
		unitManager.addFactor(UnitFactory.createFactor("*M", "M", "Mega", 1e6));
		unitManager.addFactor(UnitFactory.createFactor("*k", "k", "kilo", 1e3));
		unitManager.addFactor(UnitFactory.createFactor("*c", "c", "centi", 1e-2));
		unitManager.addFactor(UnitFactory.createFactor("*m", "m", "milli", 1e-3));
		unitManager.addFactor(UnitFactory.createFactor("*\u00b5", "\u00b5", "micro", 1e-6)); // µ
		unitManager.addFactor(UnitFactory.createFactor("*n", "n", "nano", 1e-9));
		unitManager.addFactor(UnitFactory.createFactor("*p", "p", "pico", 1e-12));
		unitManager.addFactor(UnitFactory.createFactor("*f", "f", "femto", 1e-15));
	}

	private void createPrimaryUnits() {
		unitManager.addUnit(UnitFactory.createUnit(".m", "m", "meter"));
		unitManager.addUnit(UnitFactory.createUnit(".s", "s", "second"));
		unitManager.addUnit(UnitFactory.createUnit(".g", "g", "gram"));
		unitManager.addUnit(UnitFactory.createUnit(".K", "K", "Kelvin"));
		unitManager.addUnit(UnitFactory.createUnit(".A", "A", "Ampere"));
		unitManager.addUnit(UnitFactory.createUnit(".cd", "cd", "Candela"));
		unitManager.addUnit(UnitFactory.createUnit(".mol", "mol", "Mole"));
	}

	private void createOtherUnits() {
		unitManager.addUnit(UnitFactory.createUnitFM(unitManager, "*k", ".g"));
		unitManager.addUnit(UnitFactory.createUnitMD(unitManager, ".m", ".s"));
		unitManager.addUnit(UnitFactory.createUnitFM(unitManager, "*k", ".m"));
		unitManager.addUnit(UnitFactory.createUnitFMMDD(unitManager, null, "*k.g", ".m", ".s", ".s"));
		unitManager.addUnit(UnitFactory.createUnitFSMMDD(
				unitManager, null,
				".N", "N", "Newton",
				"*k.g", ".m", ".s", ".s"));
		unitManager.addUnit(UnitFactory.createUnitFSBMD(
				unitManager, null,
				".h", "h", "Hour",
				new ConverterMul(Double.valueOf(3600.0)),
				".s", null));
		unitManager.addUnit(UnitFactory.createUnitMD(unitManager, "*k.m", ".h"));
	}

	private void run() {
		// createFactors();
		// createPrimaryUnits();
		// createOtherUnits();
		UnitsIo uio = new UnitsIo(unitManager);
		uio.readFile(new File("./src/de/admadic/calculator/units/unitstdset-save.xml"));
		uio.readFile(new File("./src/de/admadic/calculator/units/quantdef.xml"));

		uio.saveFileQuantities(new File("./src/de/admadic/calculator/units/quantdef-tmp.xml"));

		unitManager.addMeasure(UnitFactory.createMeasure(
				unitManager, "mF0", "@force", 10.5, ".N"));
		unitManager.addMeasure(UnitFactory.createMeasure(
				unitManager, "mv0", "@velocity", 20.5, ".m:s"));
		unitManager.addMeasure(UnitFactory.createMeasure(
				unitManager, "mv1", "@velocity", 2.0, "*k.m:h"));
		unitManager.addMeasure(UnitFactory.createMeasure(
				unitManager, "ms0", "@length", 50.1, ".m"));
		unitManager.addMeasure(UnitFactory.createMeasure(
				unitManager, "mF1", "@force", 100.1, "*k.g.m:s:s"));

		System.out.println("factor dump:");
		for (Factor f : unitManager.getFactors()) {
			System.out.println("factor = " + f.toString());
		}
		System.out.println("");

		System.out.println("unit dump:");
		for (IUnit u : unitManager.getUnits()) {
			System.out.println("unit = " + u.getDiagnosticInfo());
		}
		System.out.println("");

		System.out.println("quantity dump:");
		for (IQuantity q : unitManager.getQuantities()) {
			System.out.println("quantity = " + q.getDiagnosticInfo());
		}
		System.out.println("");

		System.out.println("measure dump:");
		for (IMeasure m : unitManager.getMeasures()) {
			System.out.println("measure = " + m.getDiagnosticInfo());
		}
		System.out.println("");

		IMeasure mF0 = unitManager.getMeasure("mF0");
		IMeasure mF1 = unitManager.getMeasure("mF1");
		IMeasure mv0 = unitManager.getMeasure("mv0");
		IMeasure mv1 = unitManager.getMeasure("mv1");

		System.out.println(
				"mF0: " + 
				mF0.getDiagnosticInfo() + 
				"  --> rootValue=" + mF0.getRootValue());
		System.out.println(
				"mF1: " + 
				mF1.getDiagnosticInfo() + 
				"  --> rootValue=" + mF1.getRootValue());
		System.out.println(
				"mv0: " + 
				mv0.getDiagnosticInfo() + 
				"  --> rootValue=" + mv0.getRootValue());
		System.out.println(
				"mv1: " + 
				mv1.getDiagnosticInfo() + 
				"  --> rootValue=" + mv1.getRootValue());

		testXValue();
		testConversion();

		System.out.println(mv0.getDiagnosticInfo());
		mv0.changeUnit(unitManager.getUnit("*k.m:h"));
		System.out.println(mv0.getDiagnosticInfo());
		((Measure)mv0).setMeasureFormatter(
				MeasureFormatter.createFormatter("%.8g", Locale.GERMANY));
		System.out.println(mv0.getValueView());
	}

	protected void testXValue() {
		String xv = "1.5*2.0/45.3";
		String [] xa = xv.split("(?=\\*|\\/)");
		for (String s : xa) {
			System.out.println(">" + s + "<");
		}
	}

	protected void testConversion() {
		unitManager.addQuantity(UnitFactory.createQuantity(
				unitManager, ".p", "p", "Pressure", ".Pa"));
		unitManager.addMeasure(UnitFactory.createMeasure(
				unitManager, "mp0", ".p", 10.0, "*M.Pa"));
		IMeasure m = unitManager.getMeasure("mp0");
		Double v = m.getValue();
		Double rv = m.getRootValue();
		IUnit du = unitManager.getUnit("*G.Pa");
		IConverter ic = du.getRootConverter();
		Double dv = ic.iconvert(rv);

		// ATN: check whether the ncid matches! otherwise it does not work!
		System.out.println("measure : " + m.getDiagnosticInfo());
		System.out.println("converted into " + du.getDiagnosticInfo() + " gives:");
		System.out.println("v = " + v + " rv = " + rv + " dv = " + dv);
	}

}
