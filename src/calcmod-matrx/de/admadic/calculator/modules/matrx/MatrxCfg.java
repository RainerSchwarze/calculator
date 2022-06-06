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
package de.admadic.calculator.modules.matrx;

import java.awt.Rectangle;

import de.admadic.calculator.modules.matrx.core.DMatrix;
import de.admadic.calculator.modules.matrx.core.MatrixUtil;
import de.admadic.cfg.Cfg;

/**
 * @author Rainer Schwarze
 *
 */
public class MatrxCfg {
	// FIXME: fix the key name/prefix handling, it is too clumsy for maintenance

	/** name of module in cfg space */
	public final static String
	MODNAME = "Matrx";
	
	/** key for flag whether module frame is shown or not */
	public final static String 
	KEY_SHOW = MODNAME + ".show";
	/** key for position information */
	public final static String 
	KEY_POS = MODNAME + ".pos";
	/** key for last working directory */
	public final static String 
	KEY_LASTWD = MODNAME + ".lastwd";
	/** key for display precision information */
	public final static String 
	KEY_DISPLAY_FORMAT = MODNAME + ".displayformat";

	/** key for matrix edit window contents */
	public final static String 
	KEY_MTX_EDIT_CONTENT = MODNAME + ".mtxeditcontent";
	/** key for matrix data contents */
	public final static String 
	KEY_MTX_DATA_CONTENT = MODNAME + ".mtxdatacontent";
	
	/** index for simple input */
	public final static int MTXIDX_SPL_INP = 0;
	/** index for simple result */
	public final static int MTXIDX_SPL_RES = 1;
	/** index for binary input */
	public final static int MTXIDX_BIN_INP = 2;
	/** index for binary param */
	public final static int MTXIDX_BIN_PAR = 3;
	/** index for binary result */
	public final static int MTXIDX_BIN_RES = 4;
	/** index for equ coeff */
	public final static int MTXIDX_EQU_COE = 5;
	/** index for equ absel */
	public final static int MTXIDX_EQU_ABS = 6;
	/** index for equ gauss */
	public final static int MTXIDX_EQU_GAU = 7;
	/** index for equ solution */
	public final static int MTXIDX_EQU_SOL = 8;
	/** count for matrices */
	public final static int MTXCNT = 9;

	/** prefix used for cfg in application space */
	String prefix = "";

	Cfg cfg;
	
	/**
	 * @param cfg 
	 * 
	 */
	public MatrxCfg(Cfg cfg) {
		super();
		this.cfg = cfg;
	}


	/**
	 * @return Returns the cfg.
	 */
	public Cfg getCfg() {
		return cfg;
	}


	/**
	 * @return Returns the prefix.
	 */
	public String getPrefix() {
		return prefix;
	}


	/**
	 * @param prefix The prefix to set.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
		if (this.prefix==null) {
			this.prefix = "";
		}
	}

	/**
	 * Check whether there are values from previous versions.
	 * If there are any, they will be deleted after this method call.
	 */
	public void checkPrevCfg() {
		checkPrevCfgImpl(prefix + MatrxCfg.KEY_SHOW);
		checkPrevCfgImpl(prefix + MatrxCfg.KEY_POS);
		checkPrevCfgImpl(prefix + MatrxCfg.KEY_LASTWD);
		// FIXME: maybe add taking over of previous matrix data
	}
	
	private void checkPrevCfgImpl(String key) {
		// "prefix"."mod".prev."setting"
		String k1 = key.substring((prefix + MODNAME).length());
		String tk = prefix + MODNAME + ".prev" + k1;
		Object v = cfg.getValue(tk, null);
		if (v==null) return; // nope.
		cfg.putValue(key, v);
		cfg.removeValue(tk);
	}


	/**
	 * @param b
	 */
	public void putShow(boolean b) {
		cfg.putBooleanValue(prefix + MatrxCfg.KEY_SHOW, b);
	}

	/**
	 * @param defb
	 * @return	Returns the show settings, or defb, if not found
	 */
	public boolean getShow(boolean defb) {
		return cfg.getBooleanValue(prefix + MatrxCfg.KEY_SHOW, defb);
	}

	/**
	 * @param r
	 */
	public void putPos(Rectangle r) {
		cfg.putRectangleValue(prefix + MatrxCfg.KEY_POS, r);
	}

	/**
	 * @return	Returns the stored frame position, null if not there
	 */
	public Rectangle getPos() {
		return cfg.getRectangleValue(prefix + MatrxCfg.KEY_POS, null);
	}

	/**
	 * @param fmt
	 */
	public void putDisplayFormat(String fmt) {
		cfg.putStringValue(prefix + MatrxCfg.KEY_DISPLAY_FORMAT, fmt);
	}

	/**
	 * @return	Returns the stored display precision.
	 */
	public String getDisplayFormat() {
		return cfg.getStringValue(prefix + MatrxCfg.KEY_DISPLAY_FORMAT, null);
	}

	/**
	 * @param wd
	 */
	public void putLastWD(String wd) {
		cfg.putStringValue(prefix + MatrxCfg.KEY_LASTWD, wd);
	}
	
	/**
	 * @return	Return the last working dir setting, null if not found.
	 */
	public String getLastWD() {
		return cfg.getStringValue(prefix + MatrxCfg.KEY_LASTWD, null);
	}

	/**
	 * @return	Returns the actual license sn.
	 */
	public String getMtxEditContent() {
		String s = cfg.getStringValue(
				prefix + MatrxCfg.KEY_MTX_EDIT_CONTENT, "");
		s = s.replace(';', '\n');
		return s;
	}

	/**
	 * @param content
	 */
	public void putMtxEditContent(String content) {
		content = content.replace('\n', ';');
		cfg.putStringValue(prefix + MatrxCfg.KEY_MTX_EDIT_CONTENT, content);
	}

	/**
	 * @return	Returns the actual license sn.
	 */
	public DMatrix[] getMtxDataContent() {
		Object [] oa = cfg.getValueArray(
				prefix + MatrxCfg.KEY_MTX_DATA_CONTENT + ".");
		if (oa==null) return null;
		DMatrix [] ma = new DMatrix[oa.length];
		for (int i = 0; i < ma.length; i++) {
			DMatrix mtx = MatrixUtil.parseMatrixString((String)oa[i]);
			ma[i] = mtx;
		}
		return ma;
	}

	/**
	 * @param ma
	 */
	public void putMtxDataContent(DMatrix [] ma) {
		Object [] oa = new String[ma.length];
		for (int i = 0; i < oa.length; i++) {
			oa[i] = MatrixUtil.makeString(ma[i]);
		}
		cfg.putValueArray(prefix + MatrxCfg.KEY_MTX_DATA_CONTENT + ".", oa);
	}
}
