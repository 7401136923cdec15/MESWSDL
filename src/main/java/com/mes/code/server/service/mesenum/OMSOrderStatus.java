package com.mes.code.server.service.mesenum;

/**
 * -- 台车订单状态
 * @author PengYouWang
 * @CreateTime 2019年12月26日17:54:54
 * @LastEditTime 2019年12月26日17:55:11
 */
public enum OMSOrderStatus {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 有订单(月计划)
	 */
	HasOrder(1, "进车计划"),
	/**
	 * 收到进车电报(月计划)
	 */
	ReceivedTelegraph(2, "已收电报"),
	/**
	 * 已进厂(周计划)(月计划)
	 */
	EnterFactoryed(3, "已进厂"),
	/**
	 * 维修中(周计划)(月计划)
	 */
	Repairing(4, "维修中"),
	/**
	 * 已完工
	 */
	FinishedWork(5, "已完工"),
	/**
	 * 已发车
	 */
	CarSended(6, "已发车");

	private int value;
	private String lable;

	private OMSOrderStatus(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static OMSOrderStatus getEnumType(int val) {
		for (OMSOrderStatus type : OMSOrderStatus.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return Default;
	}

	public int getValue() {
		return value;
	}

	public String getLable() {
		return lable;
	}
}
