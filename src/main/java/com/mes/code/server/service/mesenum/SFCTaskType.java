package com.mes.code.server.service.mesenum;

import java.util.ArrayList;
import java.util.List;

import com.mes.code.server.service.po.cfg.CFGItem;

public enum SFCTaskType {
	/**
	 * SCH
	 */
	Default(0, "默认"),
	/**
	 * SCH
	 */
	StationSpotCheck(1, "操作点检"),
	/**
	 * APP
	 */
	StationBeforeShift(2, "班前"),
	/**
	 * APP
	 */
	StationAfterShift(3, "班后"),
	/**
	 * APP
	 */
	DeviceSpotCheck(4, "设备点检"),
	/**
	 * APP
	 */
	ProduceSpotCheck(5, "生产巡检"),
	/**
	 * APP
	 */
	QualitySelfCheck(6, "自检"),
	QualityCheck(7, "QualityCheck"),
	QualityOnSiteCheck(8, "质量巡检"),
	TechOnSiteCheck(9, "工艺巡检"),
	ToolOnSiteCheck(10, "计量检查"),
	SCRecheck(11, "生产复测");
	private int value;
	private String lable;

	private SFCTaskType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SFCTaskType getEnumType(int val) {
		for (SFCTaskType type : SFCTaskType.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return Default;
	}
	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();
		
		for (SFCTaskType type : SFCTaskType.values()) {
			CFGItem wItem=new CFGItem();
			wItem.ID=type.getValue();
			wItem.ItemName=type.getLable();
			wItem.ItemText=type.getLable();
			wItemList.add(wItem);
		}
		return wItemList;
	}
	public int getValue() {
		return value;
	}

	public String getLable() {
		return lable;
	}
}
