package com.mes.code.server.service.po.wms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * WMS入库回传视图头
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-1-24 15:05:45
 */
public class WMSPickBackHead implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * WMS单号
	 */
	public String WMSNo = "";
	/**
	 * MES需求单号
	 */
	public String MESNo = "";
	/**
	 * 出库明细
	 */
	public List<WMSPickBackItem> ItemList = new ArrayList<WMSPickBackItem>();

	public WMSPickBackHead() {
		super();
	}

	public WMSPickBackHead(String wMSNo, String mESNo, List<WMSPickBackItem> itemList) {
		super();
		WMSNo = wMSNo;
		MESNo = mESNo;
		ItemList = itemList;
	}

	public String getWMSNo() {
		return WMSNo;
	}

	public void setWMSNo(String wMSNo) {
		WMSNo = wMSNo;
	}

	public String getMESNo() {
		return MESNo;
	}

	public void setMESNo(String mESNo) {
		MESNo = mESNo;
	}

	public List<WMSPickBackItem> getItemList() {
		return ItemList;
	}

	public void setItemList(List<WMSPickBackItem> itemList) {
		ItemList = itemList;
	}
}
