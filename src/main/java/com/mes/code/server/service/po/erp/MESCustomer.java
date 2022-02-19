package com.mes.code.server.service.po.erp;

import java.io.Serializable;

/**
 * ERP局段信息
 */
public class MESCustomer implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 局段编码
	 */
	public String zjdxx = "";
	/**
	 * 局段名称
	 */
	public String ktext = "";

	public String getZjdxx() {
		return zjdxx;
	}

	public void setZjdxx(String zjdxx) {
		this.zjdxx = zjdxx;
	}

	public String getKtext() {
		return ktext;
	}

	public void setKtext(String ktext) {
		this.ktext = ktext;
	}
}
