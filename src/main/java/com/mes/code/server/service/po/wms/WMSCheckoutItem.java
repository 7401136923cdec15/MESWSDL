package com.mes.code.server.service.po.wms;

import java.io.Serializable;

/**
 * WMS出库信息-明细
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-1-5 11:26:56
 */
public class WMSCheckoutItem implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * WBS项目号
	 */
	public String WBSNo = "";
	/**
	 * 物料编码
	 */
	public String MaterialCode = "";
	/**
	 * 数量
	 */
	public double FQTY = 0.0;
	/**
	 * 工序编码
	 */
	public String PartPointCode = "";
	/**
	 * 工序名称
	 */
	public String PartPointName = "";
	/**
	 * 行号
	 */
	public int RowNo = 0;

	public WMSCheckoutItem() {
		super();
	}

	public WMSCheckoutItem(String wBSNo, String materialCode, double fQTY, String partPointCode, String partPointName,
			int rowNo) {
		super();
		WBSNo = wBSNo;
		MaterialCode = materialCode;
		FQTY = fQTY;
		PartPointCode = partPointCode;
		PartPointName = partPointName;
		RowNo = rowNo;
	}

	public String getWBSNo() {
		return WBSNo;
	}

	public void setWBSNo(String wBSNo) {
		WBSNo = wBSNo;
	}

	public String getMaterialCode() {
		return MaterialCode;
	}

	public void setMaterialCode(String materialCode) {
		MaterialCode = materialCode;
	}

	public double getFQTY() {
		return FQTY;
	}

	public void setFQTY(double fQTY) {
		FQTY = fQTY;
	}

	public String getPartPointCode() {
		return PartPointCode;
	}

	public void setPartPointCode(String partPointCode) {
		PartPointCode = partPointCode;
	}

	public String getPartPointName() {
		return PartPointName;
	}

	public void setPartPointName(String partPointName) {
		PartPointName = partPointName;
	}

	public int getRowNo() {
		return RowNo;
	}

	public void setRowNo(int rowNo) {
		RowNo = rowNo;
	}
}
