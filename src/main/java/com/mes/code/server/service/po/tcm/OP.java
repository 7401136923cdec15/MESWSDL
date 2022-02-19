package com.mes.code.server.service.po.tcm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 工序级别
 */
@XmlRootElement(name = "OP")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class OP {
	/**
	 * 工序ID
	 */
	private String OPID = "";
	/**
	 * 工序名称
	 */
	private String OPNAME = "";
	/**
	 * 工艺集编号
	 */
	private String FROMPROCESSID = "";
	/**
	 * 工序序号
	 */
	private String OPNO = "";
	/**
	 * 前工序ID
	 */
	private String OPID_P = "";
	/**
	 * 后工序ID
	 */
	private String OPID_S = "";
	/**
	 * 标准作业时间
	 */
	private String OPTIME = "";
	/**
	 * 工序卡文件
	 */
	private List<OPDOC> OPDOCLIST = new ArrayList<OPDOC>();
	/**
	 * 工序BOM
	 */
	private List<OPITEM> OPITEMLIST = new ArrayList<OPITEM>();

	// 2021-5-29 13:12:21
	/**
	 * 工序的版本信息
	 */
	private String OPVERSION = "";
	/**
	 * 工序变更单号
	 */
	private String PROCESSCNO = "";
	/**
	 * 工序变更单
	 */
	private String PROCESSC = "";
	/**
	 * 工艺变更的创建人信息
	 */
	private String PROCESSCUSER = "";
	/**
	 * 工艺变更类型
	 */
	private String PROCESSCTYPE = "";

	public String getOPID() {
		return OPID;
	}

	public String getOPVERSION() {
		return OPVERSION;
	}

	public void setOPVERSION(String oPVERSION) {
		OPVERSION = oPVERSION;
	}

	public void setOPID(String oPID) {
		OPID = oPID;
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

	public String getOPNAME() {
		return OPNAME;
	}

	public void setOPNAME(String oPNAME) {
		OPNAME = oPNAME;
	}

	public String getFROMPROCESSID() {
		return FROMPROCESSID;
	}

	public void setFROMPROCESSID(String fROMPROCESSID) {
		FROMPROCESSID = fROMPROCESSID;
	}

	public String getOPNO() {
		return OPNO;
	}

	public void setOPNO(String oPNO) {
		OPNO = oPNO;
	}

	public String getOPID_P() {
		return OPID_P;
	}

	public void setOPID_P(String oPID_P) {
		OPID_P = oPID_P;
	}

	public String getOPID_S() {
		return OPID_S;
	}

	public void setOPID_S(String oPID_S) {
		OPID_S = oPID_S;
	}

	public String getOPTIME() {
		return OPTIME;
	}

	public void setOPTIME(String oPTIME) {
		OPTIME = oPTIME;
	}

	public List<OPDOC> getOPDOCLIST() {
		return OPDOCLIST;
	}

	public void setOPDOCLIST(List<OPDOC> oPDOCLIST) {
		OPDOCLIST = oPDOCLIST;
	}

	public List<OPITEM> getOPITEMLIST() {
		return OPITEMLIST;
	}

	public void setOPITEMLIST(List<OPITEM> oPITEMLIST) {
		OPITEMLIST = oPITEMLIST;
	}
}
