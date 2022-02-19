package com.mes.code.server.service.po.tcm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * MESBOP中包含的工序物料
 */
@XmlRootElement(name = "OPITEM")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class OPITEM {
	private String KTTXT = "";
	private String KTTXTY = "";
	private String FROMOPID = "";
	private String MATNR = "";
	private String MEINS = "";
	private String ZZZBZ = "";
	// 以下为物料bom所需的几个属性
	private String MENGE = "";
	private String ZBHOH = "";
	private String ZZZWW = "";
	private String ZYCYZ = "";
	private String ZCJXC = "";

	public String getKTTXT() {
		return KTTXT;
	}

	public void setKTTXT(String kTTXT) {
		KTTXT = kTTXT;
	}

	public String getFROMOPID() {
		return FROMOPID;
	}

	public void setFROMOPID(String fROMOPID) {
		FROMOPID = fROMOPID;
	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public String getKTTXTY() {
		return KTTXTY;
	}

	public void setKTTXTY(String kTTXTY) {
		KTTXTY = kTTXTY;
	}

	public String getMEINS() {
		return MEINS;
	}

	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}

	public String getZZZBZ() {
		return ZZZBZ;
	}

	public void setZZZBZ(String zZZBZ) {
		ZZZBZ = zZZBZ;
	}

	public String getMENGE() {
		return MENGE;
	}

	public String getZBHOH() {
		return ZBHOH;
	}

	public String getZZZWW() {
		return ZZZWW;
	}

	public String getZYCYZ() {
		return ZYCYZ;
	}

	public String getZCJXC() {
		return ZCJXC;
	}

	public void setMENGE(String mENGE) {
		MENGE = mENGE;
	}

	public void setZBHOH(String zBHOH) {
		ZBHOH = zBHOH;
	}

	public void setZZZWW(String zZZWW) {
		ZZZWW = zZZWW;
	}

	public void setZYCYZ(String zYCYZ) {
		ZYCYZ = zYCYZ;
	}

	public void setZCJXC(String zCJXC) {
		ZCJXC = zCJXC;
	}
}
