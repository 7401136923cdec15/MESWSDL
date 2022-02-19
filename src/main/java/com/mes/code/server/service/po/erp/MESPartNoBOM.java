package com.mes.code.server.service.po.erp;

import java.io.Serializable;

/**
 * ERP台车BOM
 */
public class MESPartNoBOM implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 预留/相关需求的编号
	 */
	public String ZRSNUM = "";
	/**
	 * BOM类型
	 */
	public String ZSCLX = "";
	/**
	 * WBS要素
	 */
	public String MAT_PSPNR = "";
	/**
	 * 
	 */
	public String KTSCH = "";
	/**
	 * 工作中心
	 */
	public String ARBPL = "";
	/**
	 * 领料部门
	 */
	public String ZZFC = "";
	/**
	 * 车型
	 */
	public String ZCHEX = "";
	/**
	 * 修程
	 */
	public String ZXIUC = "";
	/**
	 * 局段信息
	 */
	public String ZJDXX = "";
	/**
	 * 工厂
	 */
	public String WERKS = "";
	/**
	 * 删除标识
	 */
	public String ZDELE = "";

	public String getZRSNUM() {
		return ZRSNUM;
	}

	public void setZRSNUM(String zRSNUM) {
		ZRSNUM = zRSNUM;
	}

	public String getZSCLX() {
		return ZSCLX;
	}

	public void setZSCLX(String zSCLX) {
		ZSCLX = zSCLX;
	}

	public String getMAT_PSPNR() {
		return MAT_PSPNR;
	}

	public void setMAT_PSPNR(String mAT_PSPNR) {
		MAT_PSPNR = mAT_PSPNR;
	}

	public String getKTSCH() {
		return KTSCH;
	}

	public void setKTSCH(String kTSCH) {
		KTSCH = kTSCH;
	}

	public String getARBPL() {
		return ARBPL;
	}

	public void setARBPL(String aRBPL) {
		ARBPL = aRBPL;
	}

	public String getZZFC() {
		return ZZFC;
	}

	public void setZZFC(String zZFC) {
		ZZFC = zZFC;
	}

	public String getZCHEX() {
		return ZCHEX;
	}

	public void setZCHEX(String zCHEX) {
		ZCHEX = zCHEX;
	}

	public String getZXIUC() {
		return ZXIUC;
	}

	public void setZXIUC(String zXIUC) {
		ZXIUC = zXIUC;
	}

	public String getZJDXX() {
		return ZJDXX;
	}

	public void setZJDXX(String zJDXX) {
		ZJDXX = zJDXX;
	}

	public String getWERKS() {
		return WERKS;
	}

	public void setWERKS(String wERKS) {
		WERKS = wERKS;
	}

	public String getZDELE() {
		return ZDELE;
	}

	public void setZDELE(String zDELE) {
		ZDELE = zDELE;
	}
}
