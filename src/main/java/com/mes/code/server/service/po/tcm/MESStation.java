package com.mes.code.server.service.po.tcm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 工位数据
 */
@XmlRootElement(name = "MESStation")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class MESStation {
	/**
	 * 工位编号
	 */
	private String MEStationID = "";
	/**
	 * 工位名称
	 */
	private String MEStationName = "";
	/**
	 * 工艺周期
	 */
	private double MESTechPeriod = 0.0;

	public String getMEStationID() {
		return MEStationID;
	}

	public void setMEStationID(String mEStationID) {
		MEStationID = mEStationID;
	}

	public String getMEStationName() {
		return MEStationName;
	}

	public void setMEStationName(String mEStationName) {
		MEStationName = mEStationName;
	}

	public double getMESTechPeriod() {
		return MESTechPeriod;
	}

	public void setMESTechPeriod(double mESTechPeriod) {
		MESTechPeriod = mESTechPeriod;
	}
}
