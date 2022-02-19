package com.mes.code.server.service.po.erp;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * erp车型
 */
public class MESProduct implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 车型编码
	 */
	private String PRART = "";
	/**
	 * 车型描述
	 */
	private String PRATX = "";

	public MESProduct() {
		super();
	}

	public void setPRART(String pRART) {
		PRART = pRART;
	}

	public void setPRATX(String pRATX) {
		PRATX = pRATX;
	}

	@JSONField(name = "PRART")
	public String getPRART() {
		return PRART;
	}

	@JSONField(name = "PRATX")
	public String getPRATX() {
		return PRATX;
	}
}
