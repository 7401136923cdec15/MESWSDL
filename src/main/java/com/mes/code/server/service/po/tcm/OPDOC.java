package com.mes.code.server.service.po.tcm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 工艺文件
 */
@XmlRootElement(name = "OPDOC")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class OPDOC {

	private String OPDOCSIGN = "";
	private String OPDOCID = "";
	private String OPDOCDATE = "";

	public String getOPDOCSIGN() {
		return OPDOCSIGN;
	}

	public void setOPDOCSIGN(String oPDOCSIGN) {
		OPDOCSIGN = oPDOCSIGN;
	}

	public String getOPDOCID() {
		return OPDOCID;
	}

	public void setOPDOCID(String oPDOCID) {
		OPDOCID = oPDOCID;
	}

	public String getOPDOCDATE() {
		return OPDOCDATE;
	}

	public void setOPDOCDATE(String oPDOCDATE) {
		OPDOCDATE = oPDOCDATE;
	}
}
