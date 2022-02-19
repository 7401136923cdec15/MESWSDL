package com.mes.code.server.service.po.erp;

import java.io.Serializable;

/**
 * ERP物料主数据
 */
public class MESMaterial implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 物料号
	 */
	public String matnr = "";
	/**
	 * 物料组
	 */
	public String matkl = "";
	/**
	 * 大小/量纲
	 */
	public String groes = "";
	/**
	 * 工业标准描
	 */
	public String normt = "";
	/**
	 * 物料类型
	 */
	public String mtart = "";
	/**
	 * 净重
	 */
	public String ntgew = "";
	/**
	 * 毛重
	 */
	public String brgew = "";
	/**
	 * 基本计量单位
	 */
	public String meins = "";
	/**
	 * 订单单位
	 */
	public String bstme = "";
	/**
	 * 工厂
	 */
	public String werks = "";
	/**
	 * 生产单位
	 */
	public String frtme = "";
	/**
	 * 安全库存
	 */
	public String eisbe = "";
	/**
	 * 物料描述
	 */
	public String maktx = "";
	/**
	 * 删除标识
	 */
	public String lvorm = "";

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getMatkl() {
		return matkl;
	}

	public void setMatkl(String matkl) {
		this.matkl = matkl;
	}

	public String getGroes() {
		return groes;
	}

	public void setGroes(String groes) {
		this.groes = groes;
	}

	public String getNormt() {
		return normt;
	}

	public void setNormt(String normt) {
		this.normt = normt;
	}

	public String getMtart() {
		return mtart;
	}

	public void setMtart(String mtart) {
		this.mtart = mtart;
	}

	public String getNtgew() {
		return ntgew;
	}

	public void setNtgew(String ntgew) {
		this.ntgew = ntgew;
	}

	public String getBrgew() {
		return brgew;
	}

	public void setBrgew(String brgew) {
		this.brgew = brgew;
	}

	public String getMeins() {
		return meins;
	}

	public void setMeins(String meins) {
		this.meins = meins;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getFrtme() {
		return frtme;
	}

	public void setFrtme(String frtme) {
		this.frtme = frtme;
	}

	public String getEisbe() {
		return eisbe;
	}

	public void setEisbe(String eisbe) {
		this.eisbe = eisbe;
	}

	public String getMaktx() {
		return maktx;
	}

	public void setMaktx(String maktx) {
		this.maktx = maktx;
	}

	public String getLvorm() {
		return lvorm;
	}

	public void setLvorm(String lvorm) {
		this.lvorm = lvorm;
	}
}
