package com.mes.code.server.service.po.tcm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 输出结果
 */
@XmlRootElement(name = "ITEM")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@XStreamAlias("ITEM")
public class ITEM {
	/**
	 * 返回码
	 */
	private String MATNR = "";
	/**
	 * 结果标识 1成功 0失败
	 */
	private int RESULT = 0;
	/**
	 * 提示信息
	 */
	private String INFO = "";

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public int getRESULT() {
		return RESULT;
	}

	public void setRESULT(int rESULT) {
		RESULT = rESULT;
	}

	public String getINFO() {
		return INFO;
	}

	public void setINFO(String iNFO) {
		INFO = iNFO;
	}
}
