package com.mes.code.server.service.po.tcm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * TCM与MES对接bom数据
 */
@XmlRootElement(name = "MESBOM")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType 
public class MESBOM {
	/**
	 * BOM编号
	 */
	private String METargetID = "";
	/**
	 * BOM类型
	 */
	private int ZSCLX = 0;
	/**
	 * 车型
	 */
	private String ZCHEX = "";
	/**
	 * 修程
	 */
	private String ZXIUC = "";
	/**
	 * 局段
	 */
	private String ZJDXX = "";
	/**
	 * 
	 */
	private String WERKS = "";
	/**
	 * 工位
	 */
	private String USR00 = "";
	/**
	 * 工序
	 */
	private String KTEXT = "";
	/**
	 * 物料编号
	 */
	private String MATNR = "";
	/**
	 * 单位
	 */
	private String MENGE = "";
	/**
	 * 数量
	 */
	private int MEINS = 0;
	/**
	 * 必换偶换
	 */
	private int ZBHOH = 0;
	/**
	 * 
	 */
	private String ZZOHL = "";
	/**
	 * 必修偶修
	 */
	private int ZZZWW = 0;
	/**
	 * 原拆原装
	 */
	private boolean ZYCYZ = false;
	/**
	 * 拆解下车
	 */
	private boolean ZCJXC = false;
	/**
	 * 备注
	 */
	private String ZZZBZ = "";

	public String getMETargetID() {
		return METargetID;
	}

	public void setMETargetID(String mETargetID) {
		METargetID = mETargetID;
	}

	public int getZSCLX() {
		return ZSCLX;
	}

	public void setZSCLX(int zSCLX) {
		ZSCLX = zSCLX;
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

	public String getUSR00() {
		return USR00;
	}

	public void setUSR00(String uSR00) {
		USR00 = uSR00;
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

	public String getMENGE() {
		return MENGE;
	}

	public void setMENGE(String mENGE) {
		MENGE = mENGE;
	}

	public int getMEINS() {
		return MEINS;
	}

	public void setMEINS(int mEINS) {
		MEINS = mEINS;
	}

	public int getZBHOH() {
		return ZBHOH;
	}

	public void setZBHOH(int zBHOH) {
		ZBHOH = zBHOH;
	}

	public String getZZOHL() {
		return ZZOHL;
	}

	public void setZZOHL(String zZOHL) {
		ZZOHL = zZOHL;
	}

	public int getZZZWW() {
		return ZZZWW;
	}

	public void setZZZWW(int zZZWW) {
		ZZZWW = zZZWW;
	}

	public boolean isZYCYZ() {
		return ZYCYZ;
	}

	public void setZYCYZ(boolean zYCYZ) {
		ZYCYZ = zYCYZ;
	}

	public boolean isZCJXC() {
		return ZCJXC;
	}

	public void setZCJXC(boolean zCJXC) {
		ZCJXC = zCJXC;
	}

	public String getZZZBZ() {
		return ZZZBZ;
	}

	public void setZZZBZ(String zZZBZ) {
		ZZZBZ = zZZBZ;
	}
}
