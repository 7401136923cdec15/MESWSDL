package com.mes.code.server.service.po.wms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * WMS入库回传视图头
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-1-24 15:05:45
 */
public class WMSInstockBackHead implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * MES退料单号、委外拆修单号、换料单号
	 */
	public String MESNo = "";
	/**
	 * 单号类型，105：退料；300：委外拆修；110：换料；
	 */
	public String OrderType = "";
	/**
	 * 上架完成时间
	 */
	public Calendar WMSReadyTime = Calendar.getInstance();
	/**
	 * WMS流水号
	 */
	public String WMSPSOrderNo = "";
	/**
	 * 车型
	 */
	public String ProductNo = "";
	/**
	 * 修程
	 */
	public String LineName = "";
	/**
	 * 局段
	 */
	public String CustomerCode = "";
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 工位名称
	 */
	public String PartName = "";
	/**
	 * 工位编码
	 */
	public String PartCode = "";
	/**
	 * 出库明细
	 */
	public List<WMSCheckoutItem> ItemList = new ArrayList<WMSCheckoutItem>();

	public WMSInstockBackHead() {
		super();
	}

	public WMSInstockBackHead(String mESNo, String orderType, Calendar wMSReadyTime, String wMSPSOrderNo,
			String productNo, String lineName, String customerCode, String partNo, String partName, String partCode,
			List<WMSCheckoutItem> itemList) {
		super();
		MESNo = mESNo;
		OrderType = orderType;
		WMSReadyTime = wMSReadyTime;
		WMSPSOrderNo = wMSPSOrderNo;
		ProductNo = productNo;
		LineName = lineName;
		CustomerCode = customerCode;
		PartNo = partNo;
		PartName = partName;
		PartCode = partCode;
		ItemList = itemList;
	}

	public String getMESNo() {
		return MESNo;
	}

	public void setMESNo(String mESNo) {
		MESNo = mESNo;
	}

	public String getOrderType() {
		return OrderType;
	}

	public void setOrderType(String orderType) {
		OrderType = orderType;
	}

	public Calendar getWMSReadyTime() {
		return WMSReadyTime;
	}

	public void setWMSReadyTime(Calendar wMSReadyTime) {
		WMSReadyTime = wMSReadyTime;
	}

	public String getWMSPSOrderNo() {
		return WMSPSOrderNo;
	}

	public void setWMSPSOrderNo(String wMSPSOrderNo) {
		WMSPSOrderNo = wMSPSOrderNo;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public String getCustomerCode() {
		return CustomerCode;
	}

	public void setCustomerCode(String customerCode) {
		CustomerCode = customerCode;
	}

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public String getPartName() {
		return PartName;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public String getPartCode() {
		return PartCode;
	}

	public void setPartCode(String partCode) {
		PartCode = partCode;
	}

	public List<WMSCheckoutItem> getItemList() {
		return ItemList;
	}

	public void setItemList(List<WMSCheckoutItem> itemList) {
		ItemList = itemList;
	}
}
