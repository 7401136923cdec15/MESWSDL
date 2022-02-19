package com.mes.code.server.service.po.wms;

import java.io.Serializable;

/**
 * WMS拣货状态回传子项
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-1-24 15:19:22
 */
public class WMSPickBackItem implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * MES行号
	 */
	public String RowNo = "";
	/**
	 * 物料编码
	 */
	public String MaterialNo = "";
	/**
	 * 明细状态 1成功、2失败
	 */
	public int Status = 0;

	public WMSPickBackItem() {
		super();
	}

	public WMSPickBackItem(String rowNo, String materialNo, int status) {
		super();
		RowNo = rowNo;
		MaterialNo = materialNo;
		Status = status;
	}

	public String getRowNo() {
		return RowNo;
	}

	public void setRowNo(String rowNo) {
		RowNo = rowNo;
	}

	public String getMaterialNo() {
		return MaterialNo;
	}

	public void setMaterialNo(String materialNo) {
		MaterialNo = materialNo;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}
}
