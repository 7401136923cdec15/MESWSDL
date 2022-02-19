package com.mes.code.server.service.po.tcm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 工艺文件
 */
@XmlRootElement(name = "DOC")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class DOC {
	private String DOCID = "";
	private String DOCREV = "";
	private String DOCNAME = "";
	private List<String> DOCDATELIST = new ArrayList<String>();

	public String getDOCID() {
		return DOCID;
	}

	public void setDOCID(String dOCID) {
		DOCID = dOCID;
	}

	public String getDOCREV() {
		return DOCREV;
	}

	public void setDOCREV(String dOCREV) {
		DOCREV = dOCREV;
	}

	public String getDOCNAME() {
		return DOCNAME;
	}

	public void setDOCNAME(String dOCNAME) {
		DOCNAME = dOCNAME;
	}

	public List<String> getDOCDATELIST() {
		return DOCDATELIST;
	}

	public void setDOCDATELIST(List<String> dOCDATELIST) {
		DOCDATELIST = dOCDATELIST;
	}
}
