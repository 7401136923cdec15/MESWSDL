package com.mes.code.server.service.po.erp;

import java.io.Serializable;

/**
 * 返回结果
 */
public class MESResult implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 状态
	 */
	public String STATUS = "";

	/**
	 * 消息
	 */
	public String MSG = "";

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}
