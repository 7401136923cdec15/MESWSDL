package com.mes.code.server.service.po.tcm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 工艺BOP
 */
@XmlRootElement(name = "BOP")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class BOP {
	/**
	 * 工艺BOPID
	 */
	private String BOPID = "";
	/**
	 * 修程
	 */
	private String ZXIUC = "";
	/**
	 * 车型
	 */
	private String ZCHEX = "";
	/**
	 * 局段
	 */
	private String ZJDXX = "";
	/**
	 * BOM唯一标识(制造目标)
	 */
	private String MATNR = "";
	/**
	 * 工艺集
	 */
	private List<PROCESS> PROCESSLIST = new ArrayList<PROCESS>();

	public String getBOPID() {
		return BOPID;
	}

	public void setBOPID(String bOPID) {
		BOPID = bOPID;
	}

	public String getZXIUC() {
		return ZXIUC;
	}

	public void setZXIUC(String zXIUC) {
		ZXIUC = zXIUC;
	}

	public String getZCHEX() {
		return ZCHEX;
	}

	public void setZCHEX(String zCHEX) {
		ZCHEX = zCHEX;
	}

	public String getZJDXX() {
		return ZJDXX;
	}

	public void setZJDXX(String zJDXX) {
		ZJDXX = zJDXX;
	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public List<PROCESS> getPROCESSLIST() {
		return PROCESSLIST;
	}

	public void setPROCESSLIST(List<PROCESS> pROCESSLIST) {
		PROCESSLIST = pROCESSLIST;
	}
}
