package com.mes.code.server.service.po.tcm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 标准bOM
 */
@XmlRootElement(name = "TCMBOM")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class TCMBOM {

	/**
	 * BOM编号
	 */
	public String METargetID = "";
	/**
	 * BOM类型
	 */
	public String ZSCLX = "";
	/**
	 * 车型
	 */
	public String ZCHEX = "";
	/**
	 * 修程
	 */
	public String ZXIUC = "";
	/**
	 * 局段
	 */
	public String ZJDXX = "";
	/**
	 * 工厂
	 */
	public String WERKS = "";
	/**
	 * 工位编码
	 */
	public String USR00 = "";
	/**
	 * 工序
	 */
	public String KTEXT = "";
	/**
	 * 物料编号
	 */
	public String MATNR = "";
	/**
	 * 单位
	 */
	public String MENGE = "";
	/**
	 * 数量
	 */
	public String MEINS = "";
	/**
	 * 必换偶换
	 */
	public String ZBHOH = "";
	/**
	 * 
	 */
	public String ZZOHL = "";
	/**
	 * 必修偶修
	 */
	public String ZZZWW = "";
	/**
	 * 原拆原装
	 */
	public String ZYCYZ = "";
	/**
	 * 拆解下车
	 */
	public String ZCJXC = "";
	/**
	 * 备注
	 */
	public String ZZZBZ = "";
	/**
	 * 物料名称
	 */
	public String KTTXT = "";

	public String getMETargetID() {
		return METargetID;
	}

	public void setMETargetID(String mETargetID) {
		METargetID = mETargetID;
	}

	public String getZSCLX() {
		return ZSCLX;
	}

	public void setZSCLX(String zSCLX) {
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

	public String getKTTXT() {
		return KTTXT;
	}

	public void setKTTXT(String kTTXT) {
		KTTXT = kTTXT;
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

	public String getMEINS() {
		return MEINS;
	}

	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}

	public String getZBHOH() {
		return ZBHOH;
	}

	public void setZBHOH(String zBHOH) {
		ZBHOH = zBHOH;
	}

	public String getZZOHL() {
		return ZZOHL;
	}

	public void setZZOHL(String zZOHL) {
		ZZOHL = zZOHL;
	}

	public String getZZZWW() {
		return ZZZWW;
	}

	public void setZZZWW(String zZZWW) {
		ZZZWW = zZZWW;
	}

	public String getZYCYZ() {
		return ZYCYZ;
	}

	public void setZYCYZ(String zYCYZ) {
		ZYCYZ = zYCYZ;
	}

	public String getZCJXC() {
		return ZCJXC;
	}

	public void setZCJXC(String zCJXC) {
		ZCJXC = zCJXC;
	}

	public String getZZZBZ() {
		return ZZZBZ;
	}

	public void setZZZBZ(String zZZBZ) {
		ZZZBZ = zZZBZ;
	}
}
