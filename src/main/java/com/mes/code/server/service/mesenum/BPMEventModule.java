package com.mes.code.server.service.mesenum;

import java.util.ArrayList;
import java.util.List;

import com.mes.code.server.service.po.cfg.CFGItem;

public enum BPMEventModule {
	Default(0, "默认"),

	SCLogin(1001, "上岗打卡"), SCDJ(1002, "点检"), SCZJ(1003, "自检"), SCSJ(1004, "首检"), SCNCR(1005, "NCR报告"), SCLL(1006, "领料"),
	SCBG(1007, "报工"), SCReady(1008, "SCReady"), SCSCBL(1009, "补料"), SCSCTL(1010, "退料"), SCLock(1011, "叫停"),
	SCCall(1012, "呼叫"), SCMsg(1013, "消息"), SCXJ(1014, "生产巡检"), SCBeforeDJ(1015, "班前点检"), SCAfterDJ(1016, "班后点检"),
	SCLayout(1017, "下岗打卡"), SCRepair(1018, "返工"),

	QTLLJY(2001, "来料检验"), QTSJ(2002, "首检"), QTXJ(2003, "巡检"), QTRKJ(2004, "入库检"), QTCKJ(2005, "出库检"),
	QTNCR(2006, "NCR报告"), QTLock(2007, "叫停"), QTCall(2008, "呼叫"), QTMsg(2009, "消息"), QTLogin(2010, "打卡"),
	QTRepair(2011, "返工"), QTJLXJ(2012, "计量巡检"), QTLayout(2013, "下岗打卡"), QTReCheck(2014, "复测"),

	TechXJ(3001, "巡检"), TechNCR(3002, "NCR报告"), TechLock(3003, "叫停"), TechCall(3004, "呼叫"), TechMsg(3005, "消息"),
	TechLogin(3006, "上岗打卡"), TechLayout(3007, "下岗打卡"), TechRepair(3008, "返工"),

	DeviceDJ(4001, "点检"), DeviceBY(4002, "维保"), DeviceWX(4003, "维修"), DeviceCall(4004, "呼叫"), DeviceMsg(4005, "消息"),
	DeviceLogin(4006, "上岗打卡"), DeviceLayout(4007, "下岗打卡"),

	CKSCM(5001, "采购入库"), CKSCPL(5002, "生产配料"), CKSL(5003, "送料"), CKSCRK(5004, "生产入库"), CKFHCK(5005, "发货出库"),
	CKCall(5006, "呼叫"), CKMsg(5007, "消息"), CKLogin(5008, "上岗打卡"), CKLayout(5009, "下岗打卡"), CKRepair(5010, "返工"),

	SCMonthAudit(

			8100, "月计划审批"),
	SCWeekAudit(

			8101, "周计划审批"),
	SCDayAudit(

			8102, "日计划审批"),
	SCDispatching(

			8103, "生产派工"),
	ToLoan(

			8104, "借调"),
	DayPlanIssue(

			8105, "日计划下达"),
	ProblemPG(

			8106, "问题项派工"),
	YJReport(

			8107, "预检报告审批"),
	QTDispatching(

			8108, "质量派工"),
	OutPlantApply(

			8109, "出厂申请"),
	TurnOrder(

			8110, "转序"),
	FinishClock(

			8111, "完工打卡"),
	MutualCheck(

			8112, "互检"),
	SpecialCheck(

			8113, "专检"),
	PreCheck(

			8114, "预检"),
	PreProblemHandle(

			8115, "预检问题项处理"),
	StationTip(

			8116, "工位提示"),
	FinalCheck(

			8117, "终检"),
	OutCheck(

			8118, "出厂检"),
	OccasionNCR(

			8201, "偶换件不合格评审"),
	/**
	 * 试运申请
	 */
	AttemptRun(8202, "试运申请"),
	/**
	 * 借调申请(本工区)
	 */
	ToLoanApply(8203, "本工区借调申请"),
	/**
	 * 积压物资退库申请
	 */
	ReturnOverMaterial(8204, "积压物资退库申请"),
	/**
	 * 借调申请(跨工区)
	 */
	ToLoanApplyK(8205, "跨工区借调申请"),
	/**
	 * 临时性检查
	 */
	TempCheck(8206, "临时性检查"),
	/**
	 * 标准审批
	 */
	StandardAudit(8207, "标准审批"),
	/**
	 * 生产日志
	 */
	ProductionLog(8208, "生产日志"),
	/**
	 * 工艺变更-返修
	 */
	SBOMChange_Repair(8209, "工艺变更-返修"),
	/**
	 * 工艺变更-异常评审
	 */
	SBOMChange_Exception(8210, "工艺变更-异常评审"),
	/**
	 * 转向架互换
	 */
	BogiesChange(8211, "转向架互换"),
	/**
	 * 标准BOM变更
	 */
	SBOMChange(8214, "标准BOM变更"),
	/**
	 * 物料采购通知
	 */
	MaterialPurchase(8216, "物料采购通知");

	private int value;
	private String lable;

	BPMEventModule(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	public static BPMEventModule getEnumType(int val) {

		for (BPMEventModule type : BPMEventModule.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return BPMEventModule.Default;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();

		for (BPMEventModule type : BPMEventModule.values()) {
			CFGItem wItem = new CFGItem();
			wItem.ID = type.getValue();
			wItem.ItemName = type.getLable();
			wItem.ItemText = type.getLable();
			wItemList.add(wItem);
		}
		return wItemList;
	}

	public int getValue() {
		return this.value;
	}

	public String getLable() {
		return this.lable;
	}
}
