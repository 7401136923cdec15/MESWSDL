package com.mes.code.server.service.po.erp;

import java.io.Serializable;

/**
 * EPR订单
 */
public class MESOrder implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * wbs元素
	 */
	public String pspnr = "";
	/**
	 * 车型
	 */
	public String zchex = "";
	/**
	 * 车号
	 */
	public String ztch = "";
	/**
	 * 计划入厂时间
	 */
	public String zrcDate = "";

	public String getPspnr() {
		return pspnr;
	}

	public void setPspnr(String pspnr) {
		this.pspnr = pspnr;
	}

	public String getZchex() {
		return zchex;
	}

	public void setZchex(String zchex) {
		this.zchex = zchex;
	}

	public String getZtch() {
		return ztch;
	}

	public void setZtch(String ztch) {
		this.ztch = ztch;
	}

	public String getZrcDate() {
		return zrcDate;
	}

	public void setZrcDate(String zrcDate) {
		this.zrcDate = zrcDate;
	}
}
