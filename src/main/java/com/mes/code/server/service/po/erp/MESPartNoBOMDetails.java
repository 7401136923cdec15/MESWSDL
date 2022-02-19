package com.mes.code.server.service.po.erp;

import java.io.Serializable;

/**
 * ERP台车BOM明细
 */
public class MESPartNoBOMDetails implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 预留/相关需求的编号
	 */
	public String ZRSNUM = "";
	/**
	 * 预留/相关需求的项目必填
	 */
	public String RSPOS = "";
	/**
	 * 台位
	 */
	public String USR00 = "";
	/**
	 * 仓库号
	 */
	public String ZLGORT = "";
	/**
	 * 评估类型
	 */
	public String BWTAR = "";
	/**
	 * 标准文本码
	 */
	public String KTEXT = "";
	/**
	 * 物料编号
	 */
	public String MATNR = "";
	/**
	 * 基本计量单位
	 */
	public String MEINS = "";
	/**
	 * 数量
	 */
	public String MENGE = "";
	/**
	 * 必换(1)/偶换(2)
	 */
	public String ZBHOH = "";
	/**
	 * 委外必修件(1)/委外偶修件(2)
	 */
	public String ZZZWW = "";
	/**
	 * 是否是互换件
	 */
	public String ZSFHH = "";
	/**
	 * 是否超修程
	 */
	public String ZSCXC = "";
	/**
	 * 领料部门
	 */
	public String ZZFC = "";
	/**
	 * 质量损失大类
	 */
	public String ZZLDL = "";
	/**
	 * 客供料标识
	 */
	public String ZFLAGKGL = "";

	public String getZRSNUM() {
		return ZRSNUM;
	}

	public void setZRSNUM(String zRSNUM) {
		ZRSNUM = zRSNUM;
	}

	public String getRSPOS() {
		return RSPOS;
	}

	public void setRSPOS(String rSPOS) {
		RSPOS = rSPOS;
	}

	public String getUSR00() {
		return USR00;
	}

	public void setUSR00(String uSR00) {
		USR00 = uSR00;
	}

	public String getZLGORT() {
		return ZLGORT;
	}

	public void setZLGORT(String zLGORT) {
		ZLGORT = zLGORT;
	}

	public String getBWTAR() {
		return BWTAR;
	}

	public void setBWTAR(String bWTAR) {
		BWTAR = bWTAR;
	}

	public String getKTEXT() {
		return KTEXT;
	}

	public void setKTEXT(String kTEXT) {
		KTEXT = kTEXT;
	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public String getMEINS() {
		return MEINS;
	}

	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}

	public String getMENGE() {
		return MENGE;
	}

	public void setMENGE(String mENGE) {
		MENGE = mENGE;
	}

	public String getZBHOH() {
		return ZBHOH;
	}

	public void setZBHOH(String zBHOH) {
		ZBHOH = zBHOH;
	}

	public String getZZZWW() {
		return ZZZWW;
	}

	public void setZZZWW(String zZZWW) {
		ZZZWW = zZZWW;
	}

	public String getZSFHH() {
		return ZSFHH;
	}

	public void setZSFHH(String zSFHH) {
		ZSFHH = zSFHH;
	}

	public String getZSCXC() {
		return ZSCXC;
	}

	public void setZSCXC(String zSCXC) {
		ZSCXC = zSCXC;
	}

	public String getZZFC() {
		return ZZFC;
	}

	public void setZZFC(String zZFC) {
		ZZFC = zZFC;
	}

	public String getZZLDL() {
		return ZZLDL;
	}

	public void setZZLDL(String zZLDL) {
		ZZLDL = zZLDL;
	}

	public String getZFLAGKGL() {
		return ZFLAGKGL;
	}

	public void setZFLAGKGL(String zFLAGKGL) {
		ZFLAGKGL = zFLAGKGL;
	}
}
