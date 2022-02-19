package com.mes.code.server.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.mes.code.server.service.CoreService;
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
import com.mes.code.server.service.po.sch.SCHWorker;
import com.mes.code.server.service.utils.CloneTool;
import com.mes.code.server.service.utils.RemoteInvokeUtils;
import com.mes.code.server.service.utils.StringUtils;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2019年12月27日12:45:42
 * @LastEditTime 2020-1-8 10:29:23
 *
 */
@Service
public class CoreServiceImpl implements CoreService {
	private static Logger logger = LoggerFactory.getLogger(CoreServiceImpl.class);

	public CoreServiceImpl() {
	}

	private static CoreService Instance;

	public static CoreService getInstance() {
		if (Instance == null)
			Instance = new CoreServiceImpl();
		return Instance;
	}

	@Override
	public APIResult BMS_LoginEmployee(String wLoginName, String wPassword, String wToken, long wMac, int wnetJS) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("user_id", wLoginName);
			wParms.put("passWord", wPassword);
			wParms.put("token", wToken);
			wParms.put("PhoneMac", wMac);
			wParms.put("netJS", wMac);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(RemoteInvokeUtils.CoreServer_Url,
					RemoteInvokeUtils.CoreServerName, "api/User/Login", wParms, HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BMS_GetEmployeeAll(BMSEmployee wLoginUser, int wDepartmentID, int wPosition, int wActive) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("active", wActive);
			wParms.put("DepartmentID", wDepartmentID);
			wParms.put("Position", wPosition);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(RemoteInvokeUtils.CoreServer_Url,
					RemoteInvokeUtils.CoreServerName, StringUtils.Format("api/User/All?cadv_ao={0}&cade_po={1}",
							wLoginUser.getLoginName(), wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BMS_QueryEmployeeByID(BMSEmployee wLoginUser, int wID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("user_info", wID);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(RemoteInvokeUtils.CoreServer_Url,
					RemoteInvokeUtils.CoreServerName, StringUtils.Format("api/User/Info?cadv_ao={0}&cade_po={1}",
							wLoginUser.getLoginName(), wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BMS_CheckPowerByAuthorityID(int wCompanyID, int wUserID, int wFunctionID, int wRangeID,
			int wTypeID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("AuthortyID", wFunctionID);
			wParms.put("RangeID", wRangeID);
			wParms.put("TypeID", wTypeID);
			wParms.put("CompanyID", wCompanyID);
			wParms.put("UserID", wUserID);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(RemoteInvokeUtils.CoreServer_Url,
					RemoteInvokeUtils.CoreServerName, "api/Role/Check", wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult SFC_QueryShiftID(BMSEmployee wLoginUser, int Shifts) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			String wUri = StringUtils.Format(
					"MESCore/api/SCHShift/CurrentShifID?cadv_ao={0}&cade_po={1}&company_id={2}", wLoginUser.LoginName,
					wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryWorkspace(BMSEmployee wLoginUser, int wID, String wCode) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wParms.put("Code", wCode);
			String wUri = StringUtils.Format("MESCore/api/FMCWorkspace/Info?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult SCH_QueryLeadWorkerByPositionID(BMSEmployee wLoginUser, int wPositionID, int wShiftID) {
		APIResult wResult = new APIResult();
		try {
//			Map<String, Object> wParms = new HashMap<String, Object>();
//			wParms.put("PositionID", wPositionID);
//			wParms.put("ShiftID", wShiftID);
//			String wUri = StringUtils.Format("MESCore/api/SCHWorker/PositionWorker?cadv_ao={0}&cade_po={1}&company_id={2}",
//					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
//			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);

			// 自己构造APIResult
			APIResult wResultList = BMS_GetEmployeeAll(wLoginUser, -1, wLoginUser.Manager, 1);
			List<BMSEmployee> wList = wResultList.List(BMSEmployee.class);
			List<SCHWorker> wWorkerList = new ArrayList<SCHWorker>();
			if (wList != null && wList.size() > 0) {
				for (BMSEmployee wItem : wList) {
					SCHWorker wSCHWorker = new SCHWorker();
					wSCHWorker.WorkerID = wItem.ID;
					wSCHWorker.WorkerName = wItem.Name;
					wWorkerList.add(wSCHWorker);
				}
			}
			Map<String, Object> wReturnObject = new HashMap<String, Object>();
			wReturnObject.put("msg", "");
			wReturnObject.put("list", wWorkerList);
			wReturnObject.put("info", null);
			Map<String, Object> wResultData = new HashMap<String, Object>();
			wResultData.put("resultCode", 1000);
			wResultData.put("returnObject", wReturnObject);
			// 转换
			wResult = CloneTool.Clone(wResultData, APIResult.class);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult SCH_QueryWorkerByPositionID(BMSEmployee wLoginUser, int wPositionID, int wShiftID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("PositionID", wPositionID);
			wParms.put("ShiftID", wShiftID);
			String wUri = StringUtils.Format(
					"MESCore/api/SCHWorker/PositionLeader?cadv_ao={0}&cade_po={1}&company_id={2}", wLoginUser.LoginName,
					wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_GetQRCode(BMSEmployee wLoginUser, QRTypes wQRType, long wQRCodeID) {
		APIResult wResult = new APIResult();
		try {
//			Map<String, Object> wParms = new HashMap<String, Object>();
//			wParms.put("Type", wQRType);
//			wParms.put("ID", wQRCodeID);
//			String wUri = StringUtils.Format("MESCore/api/BFCQR/Code?cadv_ao={0}&cade_po={1}&company_id={2}",
//					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
//			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_GetFMCWorkspaceList(BMSEmployee wLoginUser, int wProductID, int wPartID, String wPartNo,
			int wPlaceType, int wActive) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ProductID", wProductID);
			wParms.put("PartID", wPartID);
			wParms.put("PartNo", wPartNo);
			wParms.put("PlaceType", wPlaceType);
			wParms.put("Active", wActive);
			String wUri = StringUtils.Format("MESCore/api/FMCWorkspace/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_SaveFMCWorkspace(BMSEmployee wBMSEmployee, FMCWorkspace wFMCWorkspace) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wFMCWorkspace);
			String wUri = StringUtils.Format("MESCore/api/FMCWorkspace/Update?cadv_ao={0}&cade_po={1}&company_id={2}",
					wBMSEmployee.LoginName, wBMSEmployee.Password, wBMSEmployee.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<DMSDeviceLedger>> DMS_GetDeviceLedgerList(BMSEmployee wBMSEmployee, int wBusinessUnitID,
			int wBaseID, int wFactoryID, int wWorkShopID, int wLineID, int wModelID, DMSLedgerStatus wDMSLedgerStatus) {
		ServiceResult<List<DMSDeviceLedger>> wResult = new ServiceResult<List<DMSDeviceLedger>>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ModelID", wModelID);
			wParms.put("WorkShopID", wWorkShopID);
			wParms.put("LineID", wLineID);
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wParms.put("BaseID", wBaseID);
			wParms.put("FactoryID", wFactoryID);
			wParms.put("Status", wDMSLedgerStatus.getValue());
			String wUri = StringUtils.Format("MESCore/api/DeviceLedger/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wBMSEmployee.LoginName, wBMSEmployee.Password, wBMSEmployee.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult.Result = wApiResult.List(DMSDeviceLedger.class);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<FPCProduct>> FPC_QueryProductList(BMSEmployee wBMSEmployee, int wBusinessUnitID,
			int wProductTypeID) {
		ServiceResult<List<FPCProduct>> wResult = new ServiceResult<List<FPCProduct>>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wParms.put("ProductTypeID", wProductTypeID);
			wParms.put("OAGetType", -1);
			String wUri = StringUtils.Format("MESCore/api/FPCProduct/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wBMSEmployee.LoginName, wBMSEmployee.Password, wBMSEmployee.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult.Result = wApiResult.List(FPCProduct.class);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_GetQRType(BMSEmployee wLoginUser, String wQRCode) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("QRCode", wQRCode);
			String wUri = StringUtils.Format("MESCore/api/BFCQR/QRType?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据ID获取工位
	 * 
	 * @param wLoginUser
	 * @param wID
	 * @return
	 */
	@Override
	public APIResult FPC_GetPartByID(BMSEmployee wLoginUser, long wID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			String wUri = StringUtils.Format("MESCore/api/FPCPartPoint/Info?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取岗位列表
	 * 
	 * @param wLoginUser
	 * @param wCompanyID
	 * @return
	 */
	public APIResult BMS_QueryPositionList(BMSEmployee wLoginUser, int wCompanyID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			String wUri = StringUtils.Format(
					"MESCore/api/Department/AllPosition?cadv_ao={0}&cade_po={1}&company_id={2}", wLoginUser.LoginName,
					wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取部门列表
	 * 
	 * @param wLoginUser
	 * @param wCompanyID
	 * @param wLoginID
	 * @return
	 */
	public APIResult BMS_QueryDepartmentList(BMSEmployee wLoginUser, int wCompanyID, int wLoginID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			String wUri = StringUtils.Format(
					"MESCore/api/Department/AllDepartment?cadv_ao={0}&cade_po={1}&company_id={2}", wLoginUser.LoginName,
					wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取人员列表
	 */
	@Override
	public List<BMSEmployee> BMS_QueryEmployeeList(BMSEmployee wLoginUser, int wCompanyID, int wLoginID) {
		List<BMSEmployee> wResult = new ArrayList<BMSEmployee>();
		try {
			APIResult wApiResult = BMS_GetEmployeeAll(wLoginUser, -1, -1, 1);
			wResult = wApiResult.List(BMSEmployee.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取部门列表
	 */
	@Override
	public List<BMSDepartment> BMS_QueryDepartmentList(BMSEmployee wLoginUser) {
		List<BMSDepartment> wResult = new ArrayList<BMSDepartment>();
		try {
			APIResult wApiResult = BMS_QueryDepartmentList(wLoginUser, 0, 0);
			wResult = wApiResult.List(BMSDepartment.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取岗位列表
	 */
	@Override
	public List<BMSPosition> BMS_QeuryPositionList(BMSEmployee wLoginUser) {
		List<BMSPosition> wResult = new ArrayList<BMSPosition>();
		try {
			APIResult wApiResult = BMS_QueryPositionList(wLoginUser, 0);
			wResult = wApiResult.List(BMSPosition.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取产线工艺列表
	 */
	@Override
	public List<FMCLineUnit> FMC_QueryLineUnitList(BMSEmployee wLoginUser, int wLineID) {
		List<FMCLineUnit> wResult = new ArrayList<FMCLineUnit>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("LineID", wLineID);
			wParms.put("ID", -1);
			String wUri = StringUtils.Format("MESCore/api/FMCLineUnit/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(FMCLineUnit.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public List<CFGCalendar> CFG_QueryCalendarList(BMSEmployee wLoginUser, int wYear, int wWorkShopID) {
		List<CFGCalendar> wResult = new ArrayList<CFGCalendar>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("year", wYear);
			wParms.put("WorkShopID", wWorkShopID);
			String wUri = StringUtils.Format("MESCore/api/Holiday/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(CFGCalendar.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public List<LFSWorkAreaStation> LFS_QueryWorkAreaStationList(BMSEmployee wLoginUser) {
		List<LFSWorkAreaStation> wResult = new ArrayList<LFSWorkAreaStation>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", -1);
			wParms.put("WorkAreaID", -1);
			wParms.put("StationID", -1);
			wParms.put("Active", 1);

			String wUri = StringUtils.Format("MESLFS/api/LFS/WorkAreaAll?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(LFSWorkAreaStation.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public List<FMCWorkDay> FMC_QueryWorkDayLists(BMSEmployee wLoginUser) {
		List<FMCWorkDay> wResult = new ArrayList<FMCWorkDay>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			String wUri = StringUtils.Format("MESCore/api/FMCWorkDay/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(FMCWorkDay.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public List<FMCShift> FMC_QueryShiftList(BMSEmployee wLoginUser, int wFactoryID) {
		List<FMCShift> wResult = new ArrayList<FMCShift>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", wFactoryID);

			String wUri = StringUtils.Format("MESCore/api/FMCShift/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(FMCShift.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public List<FMCTimeZone> FMC_QueryTimeZoneList(BMSEmployee wLoginUser, int wShiftID) {
		List<FMCTimeZone> wResult = new ArrayList<FMCTimeZone>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ShiftID", wShiftID);

			String wUri = StringUtils.Format("MESCore/api/FMCTimeZone/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(FMCTimeZone.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取所有产线工艺集合
	 */
	@Override
	public List<FPCRoute> FPC_QueryRouteList(BMSEmployee wLoginUser) {
		List<FPCRoute> wResult = new ArrayList<FPCRoute>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", -1);
			wParms.put("BusinessUnitID", -1);
			wParms.put("ProductTypeID", -1);
			wParms.put("OAGetType", -1);

			String wUri = StringUtils.Format("MESCore/api/FPCRoute/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(FPCRoute.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public List<FPCRoutePart> FPC_QueryRoutePartList(BMSEmployee wLoginUser, int wRouteID) {
		List<FPCRoutePart> wResult = new ArrayList<FPCRoutePart>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("RouteID", wRouteID);

			String wUri = StringUtils.Format("MESCore/api/FPCRoutePart/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(FPCRoutePart.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public List<FMCLine> FMC_QueryLineList(BMSEmployee wLoginUser) {
		List<FMCLine> wResult = new ArrayList<FMCLine>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", 0);
			wParms.put("BusinessUnitID", 0);
			wParms.put("WorkShopID", 0);

			String wUri = StringUtils.Format("MESCore/api/FMCLine/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(FMCLine.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public List<FPCPart> FPC_QueryPartList(BMSEmployee wLoginUser) {
		List<FPCPart> wResult = new ArrayList<FPCPart>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", 0);
			wParms.put("BusinessUnitID", 0);

			String wUri = StringUtils.Format("MESCore/api/FPCPart/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(FPCPart.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public List<FPCProductRoute> FPC_QueryProductRouteList(BMSEmployee wLoginUser) {
		List<FPCProductRoute> wResult = new ArrayList<FPCProductRoute>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("RouteID", 0);
			wParms.put("ProductID", 0);

			String wUri = StringUtils.Format("MESCore/api/FPCProductRoute/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(FPCProductRoute.class);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public List<FMCWorkShop> FMC_QueryWorkShopList(BMSEmployee wLoginUser) {
		List<FMCWorkShop> wResult = new ArrayList<FMCWorkShop>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", 0);
			wParms.put("BusinessUnitID", 0);

			String wUri = StringUtils.Format("MESCore/api/FMCWorkShop/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(FMCWorkShop.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public FMCWorkDay FMC_QueryActiveWorkDay(BMSEmployee wLoginUser, int wActive) {
		FMCWorkDay wResult = new FMCWorkDay();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", 0);
			wParms.put("Active", wActive);

			String wUri = StringUtils.Format("MESCore/api/FMCWorkDay/Info?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.Info(FMCWorkDay.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BMS_UserAll(BMSEmployee wLoginUser, int wRoleID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("role_id", wRoleID);

			String wUri = StringUtils.Format("MESCore/api/Role/UserAll?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public List<FMCWorkCharge> FMC_QueryWorkChargeList(BMSEmployee wLoginUser) {
		List<FMCWorkCharge> wResult = new ArrayList<FMCWorkCharge>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("StationID", 0);
			wParms.put("Active", 1);
			wParms.put("ClassID", 0);

			String wUri = StringUtils.Format("MESCore/api/WorkCharge/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			APIResult wApiResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
			wResult = wApiResult.List(FMCWorkCharge.class);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 根据修程获取工位工序映射
	 */
	@Override
	public Map<Integer, List<Integer>> FMC_QueryStationStepMap(BMSEmployee wLoginUser, int wLineID) {
		Map<Integer, List<Integer>> wResult = new HashMap<Integer, List<Integer>>();
		try {
			List<FMCLineUnit> wFMCLineUnitList = this.FMC_QueryLineUnitList(wLoginUser, wLineID);
			if (wFMCLineUnitList == null || wFMCLineUnitList.size() <= 0)
				return wResult;
			List<FMCLineUnit> wStationUnitList = wFMCLineUnitList.stream().filter(p -> p.LevelID == 2 && p.Active == 1)
					.collect(Collectors.toList());
			for (FMCLineUnit wItem : wStationUnitList) {
				if (wResult.containsKey(wItem.UnitID))
					continue;
				List<FMCLineUnit> wStepUnitList = wFMCLineUnitList.stream()
						.filter(p -> p.LevelID == 3 && p.Active == 1 && p.ParentUnitID == wItem.UnitID)
						.collect(Collectors.toList());
				if (wStepUnitList == null || wStepUnitList.size() <= 0)
					continue;
				wResult.put(wItem.UnitID, wStepUnitList.stream().map(p -> p.UnitID).collect(Collectors.toList()));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public Map<FMCWorkShop, List<BMSDepartment>> BMS_QueryWorkShopDepartmentMap(BMSEmployee wLoginUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public APIResult FPC_UpdateStepSOP(BMSEmployee wBMSEmployee, FPCStepSOP wFPCStepSOP) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wFPCStepSOP);
			String wUri = StringUtils.Format("MESCore/api/FPCStepSOP/Update?cadv_ao={0}&cade_po={1}&company_id={2}",
					wBMSEmployee.LoginName, wBMSEmployee.Password, wBMSEmployee.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult CFG_QueryUnitList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			String wUri = StringUtils.Format("MESCore/api/Unit/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_QueryBOMItemAll(BMSEmployee wLoginUser, int wBOMID, int wRouteID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();

			wParms.put("bom_id", Integer.valueOf(wBOMID));
			wParms.put("RouteID", wRouteID);
			wParms.put("PlaceID", Integer.valueOf(-1));
			wParms.put("LineID", Integer.valueOf(-1));
			wParms.put("ProductID", Integer.valueOf(-1));
			wParms.put("CustomerID", Integer.valueOf(-1));
			wParms.put("ReplaceType", Integer.valueOf(-1));
			wParms.put("BOMType", Integer.valueOf(-1));
			wParms.put("PartPointID", Integer.valueOf(-1));
			wParms.put("OutSourceType", Integer.valueOf(-1));
			wParms.put("IsList", Integer.valueOf(-1));

			String wUri = StringUtils.Format("MESCore/api/BomItem/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult APS_SaveUpdateBomItem(BMSEmployee wLoginUser, APSBOMItem wAPSBOMItem) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();

			wParms.put("data", wAPSBOMItem);

			String wUri = StringUtils.Format("MESLOCOAPS/api/APSBOM/Update?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_UpdateMessageList(BMSEmployee wLoginUser, List<BFCMessage> wBFCMessageList) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wBFCMessageList);
			wParms.put("Send", 0);
			String wUri = StringUtils.Format("MESCore/api/HomePage/MsgUpdate?cadv_ao={0}&cade_po={1}",
					wLoginUser.LoginName, wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_ActiveRoute(BMSEmployee wLoginUser, int wActive, List<FPCRoute> wDataList) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wDataList);
			wParms.put("Active", wActive);
			String wUri = StringUtils.Format("MESCore/api/FPCRoute/Active?cadv_ao={0}&cade_po={1}",
					wLoginUser.LoginName, wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_StandardRoute(BMSEmployee wLoginUser, int wID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);

			String wUri = StringUtils.Format("MESCore/api/FPCRoute/Standard?cadv_ao={0}&cade_po={1}",
					wLoginUser.LoginName, wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult CFG_SaveUnit(BMSEmployee wLoginUser, CFGUnit wUnit) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wUnit);

			String wUri = StringUtils.Format("MESCore/api/Unit/Update?cadv_ao={0}&cade_po={1}", wLoginUser.LoginName,
					wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI("", wUri, wParms, HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
