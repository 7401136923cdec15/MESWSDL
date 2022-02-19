package com.mes.code.server.service.po.tcm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 工位级别
 */
@XmlRootElement(name = "PROCESS")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class PROCESS {
	/**
	 * 工艺集编号
	 */
	private String PROCESSID = "";
	/**
	 * 工艺集名称
	 */
	private String PROCESSNAME = "";
	/**
	 * 工艺BOP ID
	 */
	private String FROMBOPID = "";
	/**
	 * 工位编号
	 */
	private String USR00 = "";
	/**
	 * 前工艺集编号
	 */
	private String PROCESSID_P = "";
	/**
	 * 后工艺集编号
	 */
	private String PROCESSID_S = "";
	/**
	 * 工艺文件
	 */
	private List<PROCESSDOC> PROCESSDOCList = new ArrayList<PROCESSDOC>();
	/**
	 * 工序
	 */
	private List<OP> OPLIST = new ArrayList<OP>();

	// 2021-5-29 13:07:06
	/**
	 * 工艺集的版本信息
	 */
	private String PROCESSVERSION = "";
	/**
	 * 工艺变更单 号
	 */
	private String PROCESSCNO = "";
	/**
	 * 工艺变更单
	 */
	private String PROCESSC = "";
	/**
	 * 工艺变更创建人信息
	 */
	private String PROCESSCUSER = "";
	/**
	 * 工艺变更类型
	 */
	private String PROCESSCTYPE = "";

	public String getPROCESSID() {
		return PROCESSID;
	}

	public void setPROCESSID(String pROCESSID) {
		PROCESSID = pROCESSID;
	}

	public String getPROCESSNAME() {
		return PROCESSNAME;
	}

	public void setPROCESSNAME(String pROCESSNAME) {
		PROCESSNAME = pROCESSNAME;
	}

	public String getPROCESSVERSION() {
		return PROCESSVERSION;
	}

	public String getPROCESSCNO() {
		return PROCESSCNO;
	}

	public String getPROCESSC() {
		return PROCESSC;
	}

	public String getPROCESSCUSER() {
		return PROCESSCUSER;
	}

	public String getPROCESSCTYPE() {
		return PROCESSCTYPE;
	}

	public void setPROCESSVERSION(String pROCESSVERSION) {
		PROCESSVERSION = pROCESSVERSION;
	}

	public void setPROCESSCNO(String pROCESSCNO) {
		PROCESSCNO = pROCESSCNO;
	}

	public void setPROCESSC(String pROCESSC) {
		PROCESSC = pROCESSC;
	}

	public void setPROCESSCUSER(String pROCESSCUSER) {
		PROCESSCUSER = pROCESSCUSER;
	}

	public void setPROCESSCTYPE(String pROCESSCTYPE) {
		PROCESSCTYPE = pROCESSCTYPE;
	}

	public String getFROMBOPID() {
		return FROMBOPID;
	}

	public void setFROMBOPID(String fROMBOPID) {
		FROMBOPID = fROMBOPID;
	}

	public String getUSR00() {
		return USR00;
	}

	public void setUSR00(String uSR00) {
		USR00 = uSR00;
	}

	public String getPROCESSID_P() {
		return PROCESSID_P;
	}

	public void setPROCESSID_P(String pROCESSID_P) {
		PROCESSID_P = pROCESSID_P;
	}

	public String getPROCESSID_S() {
		return PROCESSID_S;
	}

	public void setPROCESSID_S(String pROCESSID_S) {
		PROCESSID_S = pROCESSID_S;
	}

	public List<OP> getOPLIST() {
		return OPLIST;
	}

	public void setOPLIST(List<OP> oPLIST) {
		OPLIST = oPLIST;
	}

	public List<PROCESSDOC> getPROCESSDOCList() {
		return PROCESSDOCList;
	}

	public void setPROCESSDOCList(List<PROCESSDOC> pROCESSDOCList) {
		PROCESSDOCList = pROCESSDOCList;
	}
}
