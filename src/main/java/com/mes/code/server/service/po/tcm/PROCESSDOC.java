package com.mes.code.server.service.po.tcm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 工位级别
 */
@XmlRootElement(name = "PROCESSDOC")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class PROCESSDOC {
	private String PROCESSDOCSIGN = "";
	private String PROCESSDOCID = "";
	private String PROCESSDOCDATE = "";

	public String getPROCESSDOCSIGN() {
		return PROCESSDOCSIGN;
	}

	public void setPROCESSDOCSIGN(String pROCESSDOCSIGN) {
		PROCESSDOCSIGN = pROCESSDOCSIGN;
	}

	public String getPROCESSDOCID() {
		return PROCESSDOCID;
	}

	public void setPROCESSDOCID(String pROCESSDOCID) {
		PROCESSDOCID = pROCESSDOCID;
	}

	public String getPROCESSDOCDATE() {
		return PROCESSDOCDATE;
	}

	public void setPROCESSDOCDATE(String pROCESSDOCDATE) {
		PROCESSDOCDATE = pROCESSDOCDATE;
	}

}
