package com.mes.code.server.service;

import java.util.List;
import java.util.Map;

import com.mes.code.server.service.mesenum.QRTypes;
import com.mes.code.server.service.po.APIResult;
import com.mes.code.server.service.po.ServiceResult;
import com.mes.code.server.service.po.aps.APSBOMItem;
import com.mes.code.server.service.po.bfc.BFCMessage;
import com.mes.code.server.service.po.bms.BMSDepartment;
import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.service.po.bms.BMSPosition;
import com.mes.code.server.service.po.cfg.CFGCalendar;
import com.mes.code.server.service.po.cfg.CFGUnit;
import com.mes.code.server.service.po.dms.DMSDeviceLedger;
import com.mes.code.server.service.po.dms.DMSLedgerStatus;
import com.mes.code.server.service.po.fmc.FMCLine;
import com.mes.code.server.service.po.fmc.FMCLineUnit;
import com.mes.code.server.service.po.fmc.FMCShift;
import com.mes.code.server.service.po.fmc.FMCTimeZone;
import com.mes.code.server.service.po.fmc.FMCWorkCharge;
import com.mes.code.server.service.po.fmc.FMCWorkDay;
import com.mes.code.server.service.po.fmc.FMCWorkShop;
import com.mes.code.server.service.po.fmc.FMCWorkspace;
import com.mes.code.server.service.po.fpc.FPCPart;
import com.mes.code.server.service.po.fpc.FPCProduct;
import com.mes.code.server.service.po.fpc.FPCProductRoute;
import com.mes.code.server.service.po.fpc.FPCRoute;
import com.mes.code.server.service.po.fpc.FPCRoutePart;
import com.mes.code.server.service.po.fpc.FPCStepSOP;
import com.mes.code.server.service.po.lfs.LFSWorkAreaStation;

public interface CoreService {

	APIResult BMS_LoginEmployee(String wLoginName, String wPassword, String wToken, long wMac, int wnetJS);

	APIResult BMS_GetEmployeeAll(BMSEmployee wLoginUser, int wDepartmentID, int wPosition, int wActive);

	APIResult BMS_QueryEmployeeByID(BMSEmployee wLoginUser, int wID);

	APIResult BMS_CheckPowerByAuthorityID(int wCompanyID, int wUserID, int wFunctionID, int wRangeID, int wTypeID);

	APIResult SFC_QueryShiftID(BMSEmployee wLoginUser, int Shifts);

	APIResult FMC_QueryWorkspace(BMSEmployee wLoginUser, int wID, String wCode);

	APIResult FMC_GetFMCWorkspaceList(BMSEmployee wLoginUser, int wProductID, int wPartID, String wPartNo,
			int wPlaceType, int wActive);

	APIResult SCH_QueryLeadWorkerByPositionID(BMSEmployee wLoginUser, int wPositionID, int wShiftID);

	APIResult SCH_QueryWorkerByPositionID(BMSEmployee wLoginUser, int wPositionID, int wShiftID);

	APIResult BFC_GetQRCode(BMSEmployee wLoginUser, QRTypes wQRTypes, long wQRCodeID);

	APIResult BFC_GetQRType(BMSEmployee wLoginUser, String wQRCode);

	APIResult FMC_SaveFMCWorkspace(BMSEmployee wBMSEmployee, FMCWorkspace wFMCWorkspace);

	ServiceResult<List<DMSDeviceLedger>> DMS_GetDeviceLedgerList(BMSEmployee wBMSEmployee, int wBusinessUnitID,
			int wBaseID, int wFactoryID, int wWorkShopID, int wLineID, int wModelID, DMSLedgerStatus wDMSLedgerStatus);

	ServiceResult<List<FPCProduct>> FPC_QueryProductList(BMSEmployee wBMSEmployee, int wBusinessUnitID,
			int wProductTypeID);

	APIResult FPC_GetPartByID(BMSEmployee wLoginEmployee, long wID);

	APIResult BMS_QueryPositionList(BMSEmployee wLoginUser, int wCompanyID);

	APIResult BMS_QueryDepartmentList(BMSEmployee wLoginUser, int wCompanyID, int wLoginID);

	List<BMSEmployee> BMS_QueryEmployeeList(BMSEmployee wLoginUser, int wCompanyID, int wLoginID);

	List<BMSDepartment> BMS_QueryDepartmentList(BMSEmployee wLoginUser);

	List<BMSPosition> BMS_QeuryPositionList(BMSEmployee wLoginUser);

	List<FMCLineUnit> FMC_QueryLineUnitList(BMSEmployee wLoginUser, int wLineID);

	List<CFGCalendar> CFG_QueryCalendarList(BMSEmployee wLoginUser, int wYear, int wWorkShopID);

	List<LFSWorkAreaStation> LFS_QueryWorkAreaStationList(BMSEmployee wLoginUser);

	List<FMCWorkDay> FMC_QueryWorkDayLists(BMSEmployee wLoginUser);

	List<FMCShift> FMC_QueryShiftList(BMSEmployee wLoginUser, int wFactoryID);

	List<FMCTimeZone> FMC_QueryTimeZoneList(BMSEmployee wLoginUser, int wShiftID);

	List<FPCRoute> FPC_QueryRouteList(BMSEmployee wLoginUser);

	List<FPCRoutePart> FPC_QueryRoutePartList(BMSEmployee wLoginUser, int wRouteID);

	List<FMCLine> FMC_QueryLineList(BMSEmployee wLoginUser);

	List<FPCPart> FPC_QueryPartList(BMSEmployee wLoginUser);

	List<FPCProductRoute> FPC_QueryProductRouteList(BMSEmployee wLoginUser);

	List<FMCWorkShop> FMC_QueryWorkShopList(BMSEmployee wLoginUser);

	FMCWorkDay FMC_QueryActiveWorkDay(BMSEmployee wLoginUser, int wActive);

	/**
	 * 班组工位列表
	 */
	List<FMCWorkCharge> FMC_QueryWorkChargeList(BMSEmployee wLoginUser);

	/**
	 * 获取工区班组映射
	 */
	Map<FMCWorkShop, List<BMSDepartment>> BMS_QueryWorkShopDepartmentMap(BMSEmployee wLoginUser);

	/**
	 * 获取工位工序映射
	 */
	Map<Integer, List<Integer>> FMC_QueryStationStepMap(BMSEmployee wLoginUser, int wLineID);

	/**
	 * 保存工艺卡文件
	 */
	APIResult FPC_UpdateStepSOP(BMSEmployee wBMSEmployee, FPCStepSOP wFPCStepSOP);

	/**
	 * 查询单位集合
	 */
	APIResult CFG_QueryUnitList(BMSEmployee wLoginUser);

	/**
	 * 标准BOM子项查询
	 */
	APIResult MSS_QueryBOMItemAll(BMSEmployee wLoginUser, int wBOMID, int wRouteID);

	/**
	 * 台车BOM更新
	 */
	APIResult APS_SaveUpdateBomItem(BMSEmployee wLoginUser, APSBOMItem wAPSBOMItem);

	/**
	 * 创建系统消息
	 */
	APIResult BFC_UpdateMessageList(BMSEmployee wLoginUser, List<BFCMessage> wBFCMessageList);

	APIResult BMS_UserAll(BMSEmployee wLoginUser, int wRoleID);

	/**
	 * 启用工艺路线
	 */
	APIResult FPC_ActiveRoute(BMSEmployee wLoginUser, int wActive, List<FPCRoute> wDataList);

	/**
	 * 设为当前
	 */
	APIResult FPC_StandardRoute(BMSEmployee wLoginUser, int wID);

	APIResult CFG_SaveUnit(BMSEmployee wLoginUser, CFGUnit wUnit);
}
