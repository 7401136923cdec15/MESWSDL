package com.mes.code.server.service.po.erp;

import java.io.Serializable;

/**
 * ERP设备资产
 */
public class MESDevice implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 设备编号
	 */
	public String equnr = "";
	/**
	 * 设备名称
	 */
	public String eqktu = "";
	/**
	 * 制造商模型号
	 */
	public String typbz = "";
	/**
	 * 设资产制造商
	 */
	public String herst = "";
	/**
	 * 制造商图纸号
	 */
	public String hzein = "";
	/**
	 * 设备状态
	 */
	public String txt30 = "";

	public String getEqunr() {
		return equnr;
	}

	public void setEqunr(String equnr) {
		this.equnr = equnr;
	}

	public String getEqktu() {
		return eqktu;
	}

	public void setEqktu(String eqktu) {
		this.eqktu = eqktu;
	}

	public String getTypbz() {
		return typbz;
	}

	public void setTypbz(String typbz) {
		this.typbz = typbz;
	}

	public String getHerst() {
		return herst;
	}

	public void setHerst(String herst) {
		this.herst = herst;
	}

	public String getHzein() {
		return hzein;
	}

	public void setHzein(String hzein) {
		this.hzein = hzein;
	}

	public String getTxt30() {
		return txt30;
	}

	public void setTxt30(String txt30) {
		this.txt30 = txt30;
	}
}
