package com.mes.code.server.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mes.code.server.service.FMCService;
import com.mes.code.server.service.mesenum.APSBOMSourceType;
import com.mes.code.server.service.mesenum.BFCMessageType;
import com.mes.code.server.service.mesenum.BPMEventModule;
import com.mes.code.server.service.mesenum.MESException;
import com.mes.code.server.service.mesenum.TCMChangeType;
import com.mes.code.server.service.po.APIResult;
import com.mes.code.server.service.po.OutResult;
import com.mes.code.server.service.po.ServiceResult;
import com.mes.code.server.service.po.aps.APSBOMItem;
import com.mes.code.server.service.po.bfc.BFCMessage;
import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.service.po.bms.BMSRoleItem;
import com.mes.code.server.service.po.dms.DMSDeviceLedger;
import com.mes.code.server.service.po.fmc.FMCLine;
import com.mes.code.server.service.po.fmc.FMCLineUnit;
import com.mes.code.server.service.po.fmc.FMCWorkspace;
import com.mes.code.server.service.po.fpc.FPCPart;
import com.mes.code.server.service.po.fpc.FPCPartPoint;
import com.mes.code.server.service.po.fpc.FPCProduct;
import com.mes.code.server.service.po.fpc.FPCRoute;
import com.mes.code.server.service.po.fpc.FPCRoutePart;
import com.mes.code.server.service.po.fpc.FPCRoutePartPoint;
import com.mes.code.server.service.po.mcs.MCSLogInfo;
import com.mes.code.server.service.po.mss.MSSBOM;
import com.mes.code.server.service.po.mss.MSSBOMItem;
import com.mes.code.server.service.po.mss.MSSMaterial;
import com.mes.code.server.service.po.oms.OMSOrder;
import com.mes.code.server.service.po.tcm.BOP;
import com.mes.code.server.service.po.tcm.TCMBOM;
import com.mes.code.server.service.po.tcm.TCMChangeInfo;
import com.mes.code.server.service.po.tcm.TCMMaterialChangeItems;
import com.mes.code.server.service.po.tcm.TCMMaterialChangeLog;
import com.mes.code.server.service.po.tcm.ThreadBOM;
import com.mes.code.server.service.utils.CloneTool;
import com.mes.code.server.service.utils.RemoteInvokeUtils;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.dao.BaseDAO;
import com.mes.code.server.serviceimpl.dao.mcs.MCSLogInfoDAO;
import com.mes.code.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.code.server.serviceimpl.dao.tcm.BOPDAO;
import com.mes.code.server.serviceimpl.dao.tcm.TCMBOMDAO;
import com.mes.code.server.serviceimpl.dao.tcm.TCMChangeInfoDAO;
import com.mes.code.server.serviceimpl.dao.tcm.TCMMaterialChangeItemsDAO;
import com.mes.code.server.serviceimpl.dao.tcm.TCMMaterialChangeLogDAO;
import com.mes.code.server.serviceimpl.dao.tcm.TCMVerificationDAO;
import com.mes.code.server.serviceimpl.utils.WsdlConstants;
import com.mes.code.server.utils.Configuration;

@Service
public class FMCServiceImpl implements FMCService {
	private static Logger logger = LoggerFactory.getLogger(FMCServiceImpl.class);

	public FMCServiceImpl() {
		super();
	}

	private static FMCService Instance;

	public static FMCService getInstance() {
		if (Instance == null)
			Instance = new FMCServiceImpl();
		return Instance;
	}

	@Override
	public APIResult FMC_QueryFactory(BMSEmployee wLoginUser, int wID, String wCode) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("ID", wID);

			wParms.put("Code", wCode);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCFactory/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryFactoryList(BMSEmployee wLoginUser) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCFactory/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryBusinessUnitByID(BMSEmployee wLoginUser, int wID, String wCode) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);

			wParms.put("Code", wCode);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/BusinessUnit/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryBusinessUnitList(BMSEmployee wLoginUser) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/BusinessUnit/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryWorkShopByID(BMSEmployee wLoginUser, int wID, String wCode) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);

			wParms.put("Code", wCode);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCWorkShop/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryWorkShopList(BMSEmployee wLoginUser, int wFactoryID, int wBusinessUnitID) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", wFactoryID);

			wParms.put("BusinessUnitID", wBusinessUnitID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCWorkShop/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryLineByID(BMSEmployee wLoginUser, int wID, String wCode) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);

			wParms.put("Code", wCode);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCLine/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryLineList(BMSEmployee wLoginUser, int wBusinessUnitID, int wFactoryID, int wWorkShopID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wParms.put("FactoryID", wFactoryID);
			wParms.put("WorkShopID", wWorkShopID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCLine/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryLineUnitListByLineID(BMSEmployee wLoginUser, int wLineID, int wID, int wProductID,
			boolean wIsList) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wParms.put("LineID", wLineID);
			wParms.put("ProductID", wProductID);
			wParms.put("IsList", wIsList);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCLineUnit/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryLineUnitListByProductNo(BMSEmployee wLoginUser, int wLineID, String wProductNo) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("LineID", wLineID);
			wParms.put("ProductNo", wProductNo);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCLineUnit/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryResourceByID(BMSEmployee wLoginUser, int wID) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCResource/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryResourceList(BMSEmployee wLoginUser, int wBusinessUnitID, int wFactoryID, int wLineID) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wParms.put("FactoryID", wFactoryID);
			wParms.put("LineID", wLineID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCResource/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryStationByID(BMSEmployee wLoginUser, int wID, String wCode) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wParms.put("Code", wCode);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCStation/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryStationList(BMSEmployee wLoginUser, int wLineID, int wWorkShopID) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("LineID", wLineID);
			wParms.put("WorkShopID", wWorkShopID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCStation/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_IsLineContainStation(BMSEmployee wLoginUser, int wLineID, int wPartID, int wStepID,
			int wStationID) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("LineID", wLineID);
			wParms.put("PartID", wPartID);
			wParms.put("StepID", wStepID);
			wParms.put("StationID", wStationID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCStation/Contains?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryWorkDayByID(BMSEmployee wLoginUser, int wID) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCWorkDay/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryActiveWorkDayByWorkShop(BMSEmployee wLoginUser, int wWorkShopID) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("WorkShopID", wWorkShopID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCWorkDay/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryWorkDayList(BMSEmployee wLoginUser, int wFactoryID, int wActive) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", wFactoryID);
			wParms.put("Active", wActive);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCWorkDay/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryShiftTimeZoneList(BMSEmployee wLoginUser, int wShiftID) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ShiftID", wShiftID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCTimeZone/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryShiftList(BMSEmployee wLoginUser, int wWorkDayID) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("WorkDayID", wWorkDayID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCShift/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryShiftByID(BMSEmployee wLoginUser, int wID) {
		// TODO Auto-generated method stub
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCShift/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
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
			wParms.put("PlaceType", wPlaceType);
			wParms.put("Active", wActive);
			wParms.put("PartNo", wPartNo);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCWorkspace/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_GetFMCWorkspace(BMSEmployee wLoginUser, int wID, String wCode) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wParms.put("Code", wCode);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCWorkspace/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_SaveFMCWorkspace(BMSEmployee wLoginUser, FMCWorkspace wFMCWorkspace) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wFMCWorkspace);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCWorkspace/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_GetFMCWorkspaceRecordList(BMSEmployee wLoginUser, int wProductID, int wPartID, String wPartNo,
			int wPlaceType, int wActive, Calendar wStartTime, Calendar wEndTime) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ProductID", wProductID);
			wParms.put("PartID", wPartID);
			wParms.put("PlaceType", wPlaceType);
			wParms.put("Active", wActive);
			wParms.put("PartNo", wPartNo);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCWorkspace/Record?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryProductTypeByID(BMSEmployee wLoginUser, int wID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCProductType/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryProductTypeList(BMSEmployee wLoginUser, int wBusinessUnitID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCProductType/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryProductByID(BMSEmployee wLoginUser, int wID, String wProductNo) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wParms.put("Code", wProductNo);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCProduct/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryProductList(BMSEmployee wLoginUser, int wBusinessUnitID, int wProductTypeID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wParms.put("ProductTypeID", wProductTypeID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCProduct/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryProductRouteByID(BMSEmployee wLoginUser, int wID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCProductRoute/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryWorkHourByProductCode(BMSEmployee wLoginUser, String wProductCode, int wUnitLevel,
			int wLineID, int wPartID, int wStepID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ProductCode", wProductCode);
			wParms.put("UnitLevel", wUnitLevel);
			wParms.put("LineID", wLineID);
			wParms.put("PartID", wPartID);
			wParms.put("StepID", wStepID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCProductRoute/WorkHour?cadv_ao={0}&cade_po={1}",
							wLoginUser.getLoginName(), wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryProductRouteList(BMSEmployee wLoginUser, int wFactoryID, int wBusinessUnitID,
			int wProductTypeID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wParms.put("FactoryID", wFactoryID);
			wParms.put("ProductTypeID", wProductTypeID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCProductRoute/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryManuCapacityByID(BMSEmployee wLoginUser, int wID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCManuCapacity/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryManuCapacityList(BMSEmployee wLoginUser, int wFactoryID, int wBusinessUnitID,
			int wWorkShopID, int wLineID, int wProductTypeID, int wProductID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wParms.put("FactoryID", wFactoryID);
			wParms.put("ProductTypeID", wProductTypeID);
			wParms.put("LineID", wLineID);
			wParms.put("WorkShopID", wWorkShopID);
			wParms.put("ProductID", wProductID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCManuCapacity/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_GenerateManuCapacityListByLineID(BMSEmployee wLoginUser, int wLineID, int wProductID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("LineID", wLineID);
			wParms.put("ProductID", wProductID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCManuCapacity/LineAll?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryPart(BMSEmployee wLoginUser, int wID, String wCode) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wParms.put("Code", wCode);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCPart/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryPartList(BMSEmployee wLoginUser, int wFactoryID, int wBusinessUnitID, int wProductTypeID,
			int wPartType) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wParms.put("FactoryID", wFactoryID);
			wParms.put("ProductTypeID", wProductTypeID);
			wParms.put("PartType", wPartType);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCPart/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryPartPoint(BMSEmployee wLoginUser, int wID, String wCode) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wParms.put("Code", wCode);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCPartPoint/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryPartPointList(BMSEmployee wLoginUser, int wFactoryID, int wBusinessUnitID,
			int wProductTypeID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wParms.put("FactoryID", wFactoryID);
			wParms.put("ProductTypeID", wProductTypeID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCPartPoint/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryRouteByID(BMSEmployee wLoginUser, int wID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCRoute/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryRouteList(BMSEmployee wLoginUser, int wFactoryID, int wBusinessUnitID,
			int wProductTypeID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wParms.put("FactoryID", wFactoryID);
			wParms.put("ProductTypeID", wProductTypeID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCRoute/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryRouteByProduct(BMSEmployee wLoginUser, int wLineID, int wProductID, String wProductCode) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("LineID", wLineID);
			wParms.put("ProductID", wProductID);
			wParms.put("ProductNo", wProductCode);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCProductRoute/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryRoutePartByID(BMSEmployee wLoginUser, int wID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCRoutePart/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryRoutePartListByRouteID(BMSEmployee wLoginUser, int wRouteID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("RouteID", wRouteID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCRoutePart/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryRoutePartPointByID(BMSEmployee wLoginUser, int wID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCRoutePartPoint/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryRoutePartPointListByRouteID(BMSEmployee wLoginUser, int wRouteID, int wPartID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("RouteID", wRouteID);
			wParms.put("PartID", wPartID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCRoutePartPoint/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryProductCustomByID(BMSEmployee wLoginUser, int wID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCProductCustom/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryProductCustomListByProductID(BMSEmployee wLoginUser, int wProductID, int wRouteID,
			int wPartID, int wPartPointID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("RouteID", wRouteID);
			wParms.put("PartID", wPartID);
			wParms.put("ProductID", wProductID);
			wParms.put("PartPointID", wPartPointID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCProductCustom/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_SaveLine(BMSEmployee wLoginUser, FMCLine wFMCLine) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wFMCLine);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCLine/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_SaveProduct(BMSEmployee wLoginUser, FPCProduct wFPCProduct) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wFPCProduct);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCProduct/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_SavePartPoint(BMSEmployee wLoginUser, FPCPartPoint wFPCPartPoint) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wFPCPartPoint);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCPartPoint/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_SaveLineUnit(BMSEmployee wLoginUser, FMCLineUnit wFMCLineUnit) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wFMCLineUnit);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FMCLineUnit/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_SaveRoute(BMSEmployee wLoginUser, FPCRoute wFPCRoute) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wFPCRoute);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCRoute/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_SavePart(BMSEmployee wLoginUser, FPCPart wFPCPart) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wFPCPart);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCPart/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_SaveRoutePart(BMSEmployee wLoginUser, FPCRoutePart wFPCRoutePart) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wFPCRoutePart);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCRoutePart/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_SaveRoutePartPoint(BMSEmployee wLoginUser, FPCRoutePartPoint wFPCRoutePartPoint) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wFPCRoutePartPoint);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCRoutePartPoint/Update?cadv_ao={0}&cade_po={1}",
							wLoginUser.getLoginName(), wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryStepSOPList(BMSEmployee wLoginUser, int wRoutePartPointID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("RoutePartPointID", wRoutePartPointID);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/FPCStepSOP/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult DMS_SaveDeviceLedger(BMSEmployee wSysAdmin, DMSDeviceLedger wDMSDeviceLedger) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("data", wDMSDeviceLedger);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/DeviceLedger/Update?cadv_ao={0}&cade_po={1}", wSysAdmin.getLoginName(),
							wSysAdmin.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_SaveMaterial(BMSEmployee wSysAdmin, List<MSSMaterial> wMSSMaterialList) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wMSSMaterialList);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Material/UpdateList?cadv_ao={0}&cade_po={1}", wSysAdmin.getLoginName(),
							wSysAdmin.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_SaveBOM(BMSEmployee wSysAdmin, MSSBOM wMSSBOM) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wMSSBOM);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Bom/Update?cadv_ao={0}&cade_po={1}", wSysAdmin.getLoginName(),
							wSysAdmin.getPassword()),
					wParms, HttpMethod.POST);

			logger.info("MSS_SaveBOM");
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_SaveBOMItem(BMSEmployee wLoginUser, MSSBOMItem wMSSBOMItem) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wMSSBOMItem);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/BomItem/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_QueryMaterialByID(BMSEmployee wLoginUser, int wMaterialID, String wMaterialNo) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("material_id", wMaterialID);
			wParms.put("MaterialNo", wMaterialNo);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Material/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_QueryBOM(BMSEmployee wLoginUser, int wBOMID, String wBOMNo) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("bom_no", wBOMNo);
			wParms.put("bom_id", wBOMID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Bom/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_QueryBOMItemByID(BMSEmployee wLoginUser, int wBOMItemID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("bom_id", Integer.valueOf(wBOMItemID));
			wParms.put("RouteID", -1);
			wParms.put("PlaceID", Integer.valueOf(-1));
			wParms.put("LineID", Integer.valueOf(-1));
			wParms.put("ProductID", Integer.valueOf(-1));
			wParms.put("CustomerID", Integer.valueOf(-1));
			wParms.put("ReplaceType", Integer.valueOf(-1));
			wParms.put("BOMType", Integer.valueOf(-1));
			wParms.put("PartPointID", Integer.valueOf(-1));
			wParms.put("OutSourceType", Integer.valueOf(-1));
			wParms.put("IsList", Integer.valueOf(-1));

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/BomItem/All?cadv_ao={0}&cade_po={1}",
							new Object[] { wLoginUser.getLoginName(), wLoginUser.getPassword() }),
					wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_SaveMaterial(BMSEmployee wLoginUser, MSSMaterial wMSSMaterial) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wMSSMaterial);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Material/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

//	@Override
//	public void MES_AutoSaveBOM(BMSEmployee adminUser) {
//		try {
//			if (WsdlConstants.mBomList != null) {
//
//				int wLineID = WsdlConstants.GetFMCLine(WsdlConstants.mBomList.BOMList.get(0).ZXIUC).ID;
//				int wProductID = WsdlConstants.GetFPCProduct(WsdlConstants.mBomList.BOMList.get(0).ZCHEX).ID;
//				int wCustomerID = WsdlConstants.GetCRMCustomerByCode(WsdlConstants.mBomList.BOMList.get(0).ZJDXX).ID;
//
//				// ???????????????BOP
//				List<FPCRoute> wRouteList = FMCServiceImpl.getInstance()
//						.FPC_QueryRouteList(BaseDAO.SysAdmin, -1, -1, -1).List(FPCRoute.class);
//
//				// ???????????????BOM
//				MSSBOM wNewBom = SaveMSSBom(wLineID, wProductID, wCustomerID, wRouteList);
//
//				// ?????????????????????
//				List<FPCPartPoint> wFPCStepList = FMCServiceImpl.getInstance()
//						.FPC_QueryPartPointList(BaseDAO.SysAdmin, 0, 0, 0).List(FPCPartPoint.class);
//				// ?????????????????????
//				List<FPCPart> wFPCPartList = FMCServiceImpl.getInstance()
//						.FPC_QueryPartList(BaseDAO.SysAdmin, 0, 0, 0, -1).List(FPCPart.class);
//
//				// ?????????BOM??????
//				List<MSSBOMItem> wItemList = new ArrayList<MSSBOMItem>();
//				AddMssBomItem(wNewBom, wFPCStepList, wFPCPartList, wItemList, wLineID, wProductID, wCustomerID);
//				FMCServiceImpl.getInstance().MSS_SaveBOMItemList(BaseDAO.SysAdmin, wItemList);
//
//				List<String> wUseCodeList = WsdlConstants.mBomList.BOMList.stream().map(p -> p.USR00).distinct()
//						.collect(Collectors.toList());
//				// ????????????????????????
//				if (wUseCodeList.size() == 1) {
//					// ?????????????????????
//					HandleTechChange01(wNewBom, wItemList, wRouteList, WsdlConstants.mBomList.LogID);
//				}
//
//				// ???????????????bom
//				wNewBom.Status = 3;
//				FMCServiceImpl.getInstance().MSS_SaveBOM(BaseDAO.SysAdmin, wNewBom);
//
//				// ??????????????????bom
//				FMCServiceImpl.getInstance().MSS_BOMStandard(BaseDAO.SysAdmin, wNewBom.ID);
//
//				// ??????Log???BOMID
//				UpdateBOMID(wNewBom.ID, WsdlConstants.mBomList.LogID);
//
//				WsdlConstants.mBomList = null;
//			}
//		} catch (Exception ex) {
//			logger.error(ex.toString());
//		}
//	}

	@Override
	public synchronized void MES_AutoSaveBOM(BMSEmployee adminUser, ThreadBOM wThreadBOM) {
		try {
			// MD5??????
			String wContent = JSON.toJSONString(wThreadBOM);
			OutResult<Integer> wNewIDResult = new OutResult<Integer>(0);
			String wCheckResult = TCMVerificationDAO.getInstance().Check(BaseDAO.SysAdmin, wContent, wNewIDResult);
			if (StringUtils.isNotEmpty(wCheckResult)) {
				return;
			}

			int wLineID = WsdlConstants.GetFMCLine(wThreadBOM.BOMList.get(0).ZXIUC).ID;
			int wProductID = WsdlConstants.GetFPCProduct(wThreadBOM.BOMList.get(0).ZCHEX).ID;
			int wCustomerID = WsdlConstants.GetCRMCustomerByCode(wThreadBOM.BOMList.get(0).ZJDXX).ID;

			// ???????????????BOP
			List<FPCRoute> wRouteList = FMCServiceImpl.getInstance().FPC_QueryRouteList(BaseDAO.SysAdmin, -1, -1, -1)
					.List(FPCRoute.class);

			// ???????????????BOM
			MSSBOM wNewBom = SaveMSSBom(wLineID, wProductID, wCustomerID, wRouteList, wThreadBOM);

			// ?????????????????????
			List<FPCPartPoint> wFPCStepList = FMCServiceImpl.getInstance()
					.FPC_QueryPartPointList(BaseDAO.SysAdmin, 0, 0, 0).List(FPCPartPoint.class);
			// ?????????????????????
			List<FPCPart> wFPCPartList = FMCServiceImpl.getInstance().FPC_QueryPartList(BaseDAO.SysAdmin, 0, 0, 0, -1)
					.List(FPCPart.class);

			// ?????????BOM??????
			List<MSSBOMItem> wItemList = new ArrayList<MSSBOMItem>();
			AddMssBomItem(wNewBom, wFPCStepList, wFPCPartList, wItemList, wLineID, wProductID, wCustomerID, wThreadBOM);
			FMCServiceImpl.getInstance().MSS_SaveBOMItemList(BaseDAO.SysAdmin, wItemList);

			List<String> wUseCodeList = wThreadBOM.BOMList.stream().map(p -> p.USR00).distinct()
					.collect(Collectors.toList());
			// ????????????????????????
			if (wUseCodeList.size() == 1) {
				// ?????????????????????
				HandleTechChange01(wNewBom, wItemList, wRouteList, wThreadBOM.LogID);
			}

			// ???????????????bom
			wNewBom.Status = 3;
			if (wNewBom.ID > 0) {
				FMCServiceImpl.getInstance().MSS_SaveBOM(BaseDAO.SysAdmin, wNewBom);
			}

			// ??????????????????bom
			FMCServiceImpl.getInstance().MSS_BOMStandard(BaseDAO.SysAdmin, wNewBom.ID);

			// ??????Log???BOMID
			UpdateBOMID(wNewBom.ID, wThreadBOM.LogID);

			logger.info("??????BOM????????????");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ?????????????????????BOMID
	 */
	private void UpdateBOMID(int iD, int logID) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			MCSLogInfo wLog = MCSLogInfoDAO.getInstance().SelectByID(BaseDAO.SysAdmin, logID, wErrorCode);
			if (wLog.ID > 0) {
				wLog.BOMID = iD + "";
				MCSLogInfoDAO.getInstance().Update(BaseDAO.SysAdmin, wLog, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ????????????bom
	 */
	private MSSBOM SaveMSSBom(int wLineID, int wProductID, int wCustomerID, List<FPCRoute> wRouteList,
			ThreadBOM wThreadBOM) {
		MSSBOM wNewBom = new MSSBOM();
		try {
			MSSBOM wMSSBOM = new MSSBOM();
			wMSSBOM.ID = 0;
			wMSSBOM.BOMNo = wThreadBOM.BOMList.get(0).METargetID;
			wMSSBOM.BOMName = StringUtils.Format("{0}-{1}-{2}", wThreadBOM.BOMList.get(0).ZJDXX,
					wThreadBOM.BOMList.get(0).ZXIUC, wThreadBOM.BOMList.get(0).ZCHEX);
			wMSSBOM.Status = 1;
			wMSSBOM.EditTime = Calendar.getInstance();
			wMSSBOM.AuditTime = Calendar.getInstance();
			wMSSBOM.Author = BaseDAO.SysAdmin.Name;
			wMSSBOM.Auditor = BaseDAO.SysAdmin.Name;
			wMSSBOM.MaterialID = 22;
			wMSSBOM.LineID = wLineID;
			wMSSBOM.ProductID = wProductID;
			wMSSBOM.CustomerID = wCustomerID;
			// ??????RouteID
			if (wRouteList != null && wRouteList.size() > 0
					&& wRouteList.stream().anyMatch(p -> p.ProductID == wMSSBOM.ProductID && p.LineID == wMSSBOM.LineID
							&& p.CustomerID == wMSSBOM.CustomerID)) {
				List<FPCRoute> wTempList = wRouteList.stream().filter(p -> p.ProductID == wMSSBOM.ProductID
						&& p.LineID == wMSSBOM.LineID && p.CustomerID == wMSSBOM.CustomerID)
						.collect(Collectors.toList());
				wMSSBOM.RouteID = wTempList.stream().max(Comparator.comparing(FPCRoute::getCreateTime)).get().ID;
			}
			wNewBom = FMCServiceImpl.getInstance().MSS_SaveBOM(BaseDAO.SysAdmin, wMSSBOM).Custom("list", MSSBOM.class);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}

		return wNewBom;
	}

	/**
	 * ????????????bom??????
	 */
	private void AddMssBomItem(MSSBOM wNewBom, List<FPCPartPoint> wFPCStepList, List<FPCPart> wFPCPartList,
			List<MSSBOMItem> wItemList, int wLineID, int wProductID, int wCustomerID, ThreadBOM wThreadBOM) {
		try {
			// ?????????????????????????????????????????????BOM
			List<String> wUseCodeList = wThreadBOM.BOMList.stream().map(p -> p.USR00).distinct()
					.collect(Collectors.toList());
			// ????????????????????????
			if (wUseCodeList.size() == 1) {
				OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
				// ?????????????????????????????????????????????????????????BOM
				int wBOMID = TCMBOMDAO.getInstance().SelectNewBOMID(BaseDAO.SysAdmin, wLineID, wProductID, wCustomerID,
						wErrorCode);
				// ???????????????BOMID??????BOM??????
				List<MSSBOMItem> wBOMItemList = CoreServiceImpl.getInstance()
						.MSS_QueryBOMItemAll(BaseDAO.SysAdmin, wBOMID, -1).List(MSSBOMItem.class);
				if (wBOMItemList != null && wBOMItemList.size() > 0) {
					// ????????????????????????ID
					String wPartCode = wThreadBOM.BOMList.get(0).getUSR00();
					int wPartID = WsdlConstants.GetFPCPartByCode(wPartCode).ID;
					// ???????????????????????????????????????????????????BOMID???ID???????????????
					if (wPartID > 0) {
						List<MSSBOMItem> wOtherList = wBOMItemList.stream().filter(p -> p.PlaceID != wPartID)
								.collect(Collectors.toList());
						wOtherList.forEach(p -> {
							p.ID = 0;
							p.BOMID = wNewBom.ID;
						});
						if (wOtherList.size() > 0) {
							FMCServiceImpl.getInstance().MSS_SaveBOMItemList(BaseDAO.SysAdmin, wOtherList);
						}
					}
				}
				// ??????????????????????????????????????????
				SaveList(wNewBom, wFPCStepList, wFPCPartList, wItemList, wThreadBOM);
			} else if (wUseCodeList.size() > 1) {
				// ????????????BOM????????????????????????
				SaveList(wNewBom, wFPCStepList, wFPCPartList, wItemList, wThreadBOM);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	private void SaveList(MSSBOM wNewBom, List<FPCPartPoint> wFPCStepList, List<FPCPart> wFPCPartList,
			List<MSSBOMItem> wItemList, ThreadBOM wThreadBOM) {
		try {
			for (TCMBOM wTCMBOM : wThreadBOM.BOMList) {

				MSSMaterial wMaterial = FMCServiceImpl.getInstance()
						.MSS_QueryMaterialByID(BaseDAO.SysAdmin, -1, wTCMBOM.MATNR).Info(MSSMaterial.class);
				if (wMaterial == null || wMaterial.ID <= 0) {

					wMaterial = new MSSMaterial();

					wMaterial.ID = 0;
					wMaterial.MaterialNo = wTCMBOM.MATNR;
					wMaterial.MaterialName = wTCMBOM.KTTXT;
					wMaterial.Status = 1;
					wMaterial.StockID = 1;

					wMaterial.ID = FMCServiceImpl.getInstance().MSS_SaveMaterial(BaseDAO.SysAdmin, wMaterial)
							.Info(Integer.class);
				}

				if (wMaterial.ID <= 0) {
					continue;
				}

				// ??????BOM??????
				MSSBOMItem wMSSBOMItem = new MSSBOMItem();

				wMSSBOMItem.Active = 1;
				wMSSBOMItem.Auditor = BaseDAO.SysAdmin.Name;
				wMSSBOMItem.AuditTime = Calendar.getInstance();
				wMSSBOMItem.Author = BaseDAO.SysAdmin.Name;
				wMSSBOMItem.BOMID = wNewBom.ID;
				wMSSBOMItem.BOMType = StringUtils.parseInt(wTCMBOM.ZSCLX);
				wMSSBOMItem.ProductQD = wTCMBOM.ZJDXX;
				wMSSBOMItem.DeviceNo = wTCMBOM.WERKS;
				if (wFPCPartList.stream().anyMatch(p -> p.Code.equals(wTCMBOM.USR00))) {
					wMSSBOMItem.PlaceID = wFPCPartList.stream().filter(p -> p.Code.equals(wTCMBOM.USR00)).findFirst()
							.get().ID;
				}
				if (wFPCStepList.stream().anyMatch(p -> p.Name.equals(wTCMBOM.KTEXT))) {
					wMSSBOMItem.PartPointID = wFPCStepList.stream().filter(p -> p.Name.equals(wTCMBOM.KTEXT))
							.findFirst().get().ID;
				}
				wMSSBOMItem.MaterialNo = wTCMBOM.MATNR;
				if (wMaterial != null && wMaterial.ID > 0) {
					wMSSBOMItem.MaterialID = wMaterial.ID;
					wMSSBOMItem.MaterialName = wMaterial.MaterialName;
				}
				wMSSBOMItem.MaterialNumber = StringUtils.parseDouble(wTCMBOM.MEINS);
				wMSSBOMItem.UnitID = WsdlConstants.GetCFGUnit(wTCMBOM.MENGE).ID;
				wMSSBOMItem.ReplaceType = StringUtils.parseInt(wTCMBOM.ZBHOH);
				wMSSBOMItem.ReplaceRatio = 0.0f;
				wMSSBOMItem.OutsourceType = StringUtils.parseInt(wTCMBOM.ZZZWW);
				wMSSBOMItem.OriginalType = StringUtils.parseBoolean(wTCMBOM.ZYCYZ) ? 1 : 0;
				wMSSBOMItem.DisassyType = StringUtils.parseBoolean(wTCMBOM.ZCJXC) ? 1 : 0;
				wMSSBOMItem.Remark = wTCMBOM.ZZZBZ;
				wMSSBOMItem.ParentID = 0;
				// ??????????????????
				wMSSBOMItem.TypeID = wThreadBOM.LogID;

				wItemList.add(wMSSBOMItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ??????????????????
	 */
	private void HandleTechChange01(MSSBOM wNewBom, List<MSSBOMItem> wItemList, List<FPCRoute> wRouteList, int wLogID) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ????????????????????????????????????????????????????????????BOM?????????????????????
//			int wBOMID = TCMBOMDAO.getInstance().GetMSSBomID(BaseDAO.SysAdmin, wNewBom.ProductID, wNewBom.LineID,
//					wNewBom.CustomerID, wNewBom.ID, wErrorCode);
			int wBOMID = TCMBOMDAO.getInstance().SelectNewBOMID(BaseDAO.SysAdmin, wNewBom.LineID, wNewBom.ProductID,
					wNewBom.CustomerID, wErrorCode);
			if (wBOMID <= 0) {
				return;
			}

			String wBOMNo2 = wNewBom.BOMNo;

			// ??????bomid????????????????????????????????????
			List<OMSOrder> wRowsOrderList = BOPDAO.getInstance().GetInspectOrderList(BaseDAO.SysAdmin,
					new ArrayList<Integer>(Arrays.asList(wNewBom.ID)), wErrorCode);

			/**
			 * ???????????????????????????????????????
			 */
			if (wRowsOrderList.size() <= 0) {
				return;
			}

			TCMMaterialChangeLog wTCMMaterialChangeLog = new TCMMaterialChangeLog();

			wTCMMaterialChangeLog.CreateID = BaseDAO.SysAdmin.ID;
			wTCMMaterialChangeLog.CreateTime = Calendar.getInstance();
			wTCMMaterialChangeLog.ID = 0;
			wTCMMaterialChangeLog.OrderIDList = StringUtils.Join(",",
					wRowsOrderList.stream().map(p -> p.ID).collect(Collectors.toList()));
			wTCMMaterialChangeLog.PartNoList = StringUtils.Join(",",
					wRowsOrderList.stream().map(p -> p.PartNo).collect(Collectors.toList()));
			wTCMMaterialChangeLog.ProductID = wNewBom.ProductID;
			wTCMMaterialChangeLog.LineID = wNewBom.LineID;
			wTCMMaterialChangeLog.CustomerID = wNewBom.CustomerID;
			wTCMMaterialChangeLog.Customer = WsdlConstants.GetCRMCustomerName(wNewBom.CustomerID);

			wTCMMaterialChangeLog.ID = TCMMaterialChangeLogDAO.getInstance().Update(BaseDAO.SysAdmin,
					wTCMMaterialChangeLog, wErrorCode);

			List<TCMMaterialChangeItems> wChangeItemList = new ArrayList<TCMMaterialChangeItems>();

			// ???????????????bom????????????????????????bop
			FPCRoute wMaxRoute = wRouteList.stream()
					.filter(p -> p.ProductID == wNewBom.ProductID && p.CustomerID == wNewBom.CustomerID
							&& p.LineID == wNewBom.LineID)
					.collect(Collectors.toList()).stream().max(Comparator.comparing(FPCRoute::getID)).get();
			// ???????????????????????????????????????
			if (wMaxRoute != null && wMaxRoute.ID > 0) {
				// ???????????????????????????????????????
				SaveChangeInfo(wMaxRoute.ID, wTCMMaterialChangeLog);
				// ???????????????????????????
				StepChangeCreate(wRouteList, wTCMMaterialChangeLog, wChangeItemList, wMaxRoute.ID,
						wItemList.get(0).PlaceID);
			}

			MSSBOM wOldBOM = FMCServiceImpl.getInstance().MSS_QueryBOM(BaseDAO.SysAdmin, wBOMID, "").Info(MSSBOM.class);
			String wBOMNo1 = wOldBOM.BOMNo;
			// ???????????????BOMID????????????BOM????????????
			List<MSSBOMItem> wBOMItemList = CoreServiceImpl.getInstance()
					.MSS_QueryBOMItemAll(BaseDAO.SysAdmin, wBOMID, -1).List(MSSBOMItem.class);
			if (wBOMItemList == null || wBOMItemList.size() <= 0) {
				return;
			}
			// ???????????????
			List<Integer> wStepIDList = wItemList.stream().map(p -> p.PartPointID).distinct()
					.collect(Collectors.toList());
			wStepIDList.removeIf(p -> p <= 0);

			List<Integer> wOldStepIDList = wBOMItemList.stream().map(p -> p.PartPointID).distinct()
					.collect(Collectors.toList());
			for (int wStepID : wOldStepIDList) {
				if (!wStepIDList.stream().anyMatch(p -> p == wStepID)) {
					wStepIDList.add(wStepID);
				}
			}

			// ????????????(????????????????????????????????????????????????)
			wBOMItemList = wBOMItemList.stream().filter(p -> p.PlaceID == wItemList.get(0).PlaceID)
					.collect(Collectors.toList());

			MaterialChange(wBOMItemList, wBOMNo2, wTCMMaterialChangeLog, wChangeItemList, wBOMNo1, wStepIDList,
					wItemList);
			// ??????????????????
			for (TCMMaterialChangeItems wItem : wChangeItemList) {
				if (wItem.Annex == null) {
					wItem.Annex = "";
				}
			}
			// ????????????????????????
			TCMMaterialChangeItemsDAO.getInstance().InsertList(BaseDAO.SysAdmin, wChangeItemList, wErrorCode);

			// ??????????????????(?????????)
//			SendMessageToTest(wTCMMaterialChangeLog);

			// ???????????????????????????
			SendMessageToCraft(wTCMMaterialChangeLog.ID, wNewBom.RouteID, wLogID);

			// ??????????????????????????????
//			SendMessageToPurchase(BaseDAO.SysAdmin, wTCMMaterialChangeLog.ID);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ????????????????????????
	 */
	private void StepChangeCreate(List<FPCRoute> wRouteList, TCMMaterialChangeLog wTCMMaterialChangeLog,
			List<TCMMaterialChangeItems> wChangeItemList, int wMaxRouteID, int wPartID) {
		try {
			APIResult wAPIResult = FMCServiceImpl.getInstance().FPC_DynamicCompareBop(BaseDAO.SysAdmin, wMaxRouteID);
			int wOldRouteID = wAPIResult.Custom("OldRouteID", Integer.class);
			int wNewRouteID = wAPIResult.Custom("NewRouteID", Integer.class);
			// ???????????????
			List<FPCRoutePartPoint> wAddedList = wAPIResult.CustomArray("AddedList", FPCRoutePartPoint.class);
			wAddedList = wAddedList.stream().filter(p -> p.PartID == wPartID).collect(Collectors.toList());
			for (FPCRoutePartPoint wFPCRoutePartPoint : wAddedList) {
				TCMMaterialChangeItems wItem = new TCMMaterialChangeItems();
				wItem.ID = 0;
				wItem.ChangeLogID = wTCMMaterialChangeLog.ID;
				wItem.Active = 1;
				wItem.Author = "SHRIS";
				wItem.PlaceID = wFPCRoutePartPoint.PartID;
				wItem.PartPointID = wFPCRoutePartPoint.PartPointID;
				wItem.PartPointName = WsdlConstants.GetFPCStepName(wFPCRoutePartPoint.PartPointID);
				wItem.ChangeType = TCMChangeType.StepInsert.getValue();
				wItem.RouteNo1 = "";
				if (wRouteList.stream().anyMatch(p -> p.ID == wOldRouteID)) {
					wItem.RouteNo1 = wRouteList.stream().filter(p -> p.ID == wOldRouteID).findFirst().get().VersionNo;
				}
				wItem.RouteID1 = wOldRouteID;
				wItem.RouteID2 = wNewRouteID;
				wItem.RouteNo2 = "";
				if (wRouteList.stream().anyMatch(p -> p.ID == wNewRouteID)) {
					wItem.RouteNo2 = wRouteList.stream().filter(p -> p.ID == wNewRouteID).findFirst().get().VersionNo;
				}
				wChangeItemList.add(wItem);
			}
			// ???????????????
			List<FPCRoutePartPoint> wRemovedList = wAPIResult.CustomArray("RemovedList", FPCRoutePartPoint.class);
			wRemovedList = wRemovedList.stream().filter(p -> p.PartID == wPartID).collect(Collectors.toList());
			for (FPCRoutePartPoint wFPCRoutePartPoint : wRemovedList) {
				TCMMaterialChangeItems wItem = new TCMMaterialChangeItems();
				wItem.ID = 0;
				wItem.ChangeLogID = wTCMMaterialChangeLog.ID;
				wItem.Active = 1;
				wItem.Author = "SHRIS";
				wItem.PlaceID = wFPCRoutePartPoint.PartID;
				wItem.PartPointID = wFPCRoutePartPoint.PartPointID;
				wItem.PartPointName = WsdlConstants.GetFPCStepName(wFPCRoutePartPoint.PartPointID);
				wItem.ChangeType = TCMChangeType.StepDelete.getValue();
				wItem.RouteNo1 = "";
				if (wRouteList.stream().anyMatch(p -> p.ID == wOldRouteID)) {
					wItem.RouteNo1 = wRouteList.stream().filter(p -> p.ID == wOldRouteID).findFirst().get().VersionNo;
				}
				wItem.RouteNo2 = "";
				if (wRouteList.stream().anyMatch(p -> p.ID == wNewRouteID)) {
					wItem.RouteNo2 = wRouteList.stream().filter(p -> p.ID == wNewRouteID).findFirst().get().VersionNo;
				}
				wItem.RouteID1 = wOldRouteID;
				wItem.RouteID2 = wNewRouteID;
				wChangeItemList.add(wItem);
			}
			// ???????????????????????????
			List<FPCRoutePartPoint> wUpdatedList = wAPIResult.CustomArray("UpdatedList", FPCRoutePartPoint.class);
			wUpdatedList = wUpdatedList.stream().filter(p -> p.PartID == wPartID).collect(Collectors.toList());
			for (FPCRoutePartPoint wFPCRoutePartPoint : wUpdatedList) {
				TCMMaterialChangeItems wItem = new TCMMaterialChangeItems();
				wItem.ID = 0;
				wItem.ChangeLogID = wTCMMaterialChangeLog.ID;
				wItem.Active = 1;
				wItem.Author = "SHRIS";
				wItem.PlaceID = wFPCRoutePartPoint.PartID;
				wItem.PartPointID = wFPCRoutePartPoint.PartPointID;
				wItem.PartPointName = WsdlConstants.GetFPCStepName(wFPCRoutePartPoint.PartPointID);
				wItem.ChangeType = TCMChangeType.StepUpdate.getValue();
				wItem.RouteNo1 = "";
				if (wRouteList.stream().anyMatch(p -> p.ID == wOldRouteID)) {
					wItem.RouteNo1 = wRouteList.stream().filter(p -> p.ID == wOldRouteID).findFirst().get().VersionNo;
				}
				wItem.RouteNo2 = "";
				if (wRouteList.stream().anyMatch(p -> p.ID == wNewRouteID)) {
					wItem.RouteNo2 = wRouteList.stream().filter(p -> p.ID == wNewRouteID).findFirst().get().VersionNo;
				}
				wItem.RouteID1 = wOldRouteID;
				wItem.RouteID2 = wNewRouteID;
				wChangeItemList.add(wItem);
			}
			// ?????????????????????
			List<FPCRoutePartPoint> wChangedList = wAPIResult.CustomArray("ChangedList", FPCRoutePartPoint.class);
			wChangedList = wChangedList.stream().filter(p -> p.PartID == wPartID).collect(Collectors.toList());
			for (FPCRoutePartPoint wFPCRoutePartPoint : wChangedList) {
				TCMMaterialChangeItems wItem = new TCMMaterialChangeItems();
				wItem.NewPartID = wFPCRoutePartPoint.NewPartID;
				wItem.ID = 0;
				wItem.ChangeLogID = wTCMMaterialChangeLog.ID;
				wItem.Active = 1;
				wItem.Author = "SHRIS";
				wItem.PlaceID = wFPCRoutePartPoint.PartID;
				wItem.PartPointID = wFPCRoutePartPoint.PartPointID;
				wItem.PartPointName = WsdlConstants.GetFPCStepName(wFPCRoutePartPoint.PartPointID);
				wItem.ChangeType = TCMChangeType.StepChange.getValue();
				wItem.RouteNo1 = "";
				if (wRouteList.stream().anyMatch(p -> p.ID == wOldRouteID)) {
					wItem.RouteNo1 = wRouteList.stream().filter(p -> p.ID == wOldRouteID).findFirst().get().VersionNo;
				}
				wItem.RouteNo2 = "";
				if (wRouteList.stream().anyMatch(p -> p.ID == wNewRouteID)) {
					wItem.RouteNo2 = wRouteList.stream().filter(p -> p.ID == wNewRouteID).findFirst().get().VersionNo;
				}
				wItem.RouteID1 = wOldRouteID;
				wItem.RouteID2 = wNewRouteID;
				wChangeItemList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ???????????????????????????????????????
	 */
	private void SaveChangeInfo(int wRouteID, TCMMaterialChangeLog wTCMMaterialChangeLog) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<TCMChangeInfo> wList = TCMChangeInfoDAO.getInstance().SelectList(BaseDAO.SysAdmin, -1, wRouteID,
					wErrorCode);
			if (wList == null || wList.size() <= 0) {
				return;
			}

			wTCMMaterialChangeLog.ChangeFormNo = wList.get(0).PROCESSCNO;
			wTCMMaterialChangeLog.ChangeFormUri = wList.get(0).PROCESSC;
			wTCMMaterialChangeLog.ChangeType = wList.get(0).PROCESSCTYPE;
			wTCMMaterialChangeLog.ChangeUser = wList.get(0).PROCESSCUSER;

			TCMMaterialChangeLogDAO.getInstance().Update(BaseDAO.SysAdmin, wTCMMaterialChangeLog, wErrorCode);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ???????????????kaifa(?????????)
	 */
	@SuppressWarnings("unused")
	private void SendMessageToTest(TCMMaterialChangeLog wTCMMaterialChangeLog) {
		try {
			List<BFCMessage> wBFCMessageList = new ArrayList<>();
			BFCMessage wMessage = null;
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			int wShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));
			// ???????????????????????????
			wMessage = new BFCMessage();
			wMessage.Active = 0;
			wMessage.CompanyID = 0;
			wMessage.CreateTime = Calendar.getInstance();
			wMessage.EditTime = Calendar.getInstance();
			wMessage.ID = 0;
			wMessage.MessageID = wTCMMaterialChangeLog.ID;
			wMessage.Title = StringUtils.Format("??????????????????{0}", String.valueOf(wShiftID));
			wMessage.MessageText = "??????????????????????????????????????????????????????????????????";
			wMessage.ModuleID = BPMEventModule.SBOMChange.getValue();
			wMessage.ResponsorID = 10915;
			wMessage.ShiftID = wShiftID;
			wMessage.StationID = 0;
			wMessage.Type = BFCMessageType.Task.getValue();
			wBFCMessageList.add(wMessage);
			CoreServiceImpl.getInstance().BFC_UpdateMessageList(BaseDAO.SysAdmin, wBFCMessageList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ????????????????????????????????????
	 */
	private void SendMessageToCraft(int wChangeLogID, int wRouteID, int wLogID) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<BFCMessage> wBFCMessageList = new ArrayList<>();
			BFCMessage wMessage = null;
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			int wShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));

			String wChangNo = "";

			List<Integer> wUserList = new ArrayList<Integer>();
			if (wRouteID > 0) {
				List<TCMChangeInfo> wList = TCMChangeInfoDAO.getInstance().SelectList(BaseDAO.SysAdmin, -1, wRouteID,
						wErrorCode);
				if (wList.size() > 0) {
					wChangNo = wList.get(0).PROCESSCNO;

					if (StringUtils.isNotEmpty(wList.get(0).getPROCESSCUSER())) {
						if (WsdlConstants.GetBMSEmployeeList().values().stream()
								.anyMatch(p -> wList.get(0).getPROCESSCUSER().contains(p.LoginID))) {
							wUserList.add(WsdlConstants.GetBMSEmployeeList().values().stream()
									.filter(p -> wList.get(0).getPROCESSCUSER().contains(p.LoginID)).findFirst()
									.get().ID);
						} else {
							SetTechNoticeUser(wUserList);
						}
					} else {
						SetTechNoticeUser(wUserList);
					}
				} else {
					SetTechNoticeUser(wUserList);
				}
			} else {
				SetTechNoticeUser(wUserList);
			}

//			List<BMSRoleItem> wRoleItemList = CoreServiceImpl.getInstance().BMS_UserAll(BaseDAO.SysAdmin, 10)
//					.List(BMSRoleItem.class);
			// ?????????????????????
//			wRoleItemList = wRoleItemList.stream().filter(p -> p.FunctionID == 10997).collect(Collectors.toList());
			for (int wUserID : wUserList) {
				// ???????????????????????????
				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = wChangeLogID;
				wMessage.Title = StringUtils.Format("??????????????????{0}", wChangNo);
				wMessage.MessageText = "??????????????????????????????????????????????????????????????????";
				wMessage.ModuleID = BPMEventModule.SBOMChange.getValue();
				wMessage.ResponsorID = wUserID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0;
				wMessage.Type = BFCMessageType.Task.getValue();
				wMessage.StepID = wLogID;
				wBFCMessageList.add(wMessage);
			}

			CoreServiceImpl.getInstance().BFC_UpdateMessageList(BaseDAO.SysAdmin, wBFCMessageList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ??????????????????????????????????????????
	 */
	private void SetTechNoticeUser(List<Integer> wUserList) {
		try {
			String wUserStr = Configuration.readConfigString("techchanger", "config/config");
			String[] wStrList = wUserStr.split(",");
			for (String wStr : wStrList) {
				int wUID = StringUtils.parseInt(wStr);
				if (wUID > 0 && !wUserList.stream().anyMatch(p -> p == wUID)) {
					wUserList.add(wUID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@SuppressWarnings("unused")
	private void SendMessageToPurchase(BMSEmployee wLoginUser, int wChangeLogID) {
		try {
			// ??????????????????????????????
			List<BMSRoleItem> wRoleItemList = CoreServiceImpl.getInstance().BMS_UserAll(wLoginUser, 21)
					.List(BMSRoleItem.class);
			List<BFCMessage> wBFCMessageList = new ArrayList<>();
			BFCMessage wMessage = null;
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			int wShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));
			for (BMSRoleItem wItem : wRoleItemList) {
				// ???????????????????????????
				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = wChangeLogID;
				wMessage.Title = StringUtils.Format("????????????-???????????? {0}", String.valueOf(wShiftID));
				wMessage.MessageText = StringUtils.Format("?????????????????????????????????????????????????????????");
				wMessage.ModuleID = BPMEventModule.MaterialPurchase.getValue();
				wMessage.ResponsorID = wItem.FunctionID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0;
				wMessage.Type = BFCMessageType.Task.getValue();
				wBFCMessageList.add(wMessage);
			}
			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wBFCMessageList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ????????????
	 */
	private void MaterialChange(List<MSSBOMItem> wItemList, String wBOMNo2, TCMMaterialChangeLog wTCMMaterialChangeLog,
			List<TCMMaterialChangeItems> wChangeItemList, String wBOMNo1, List<Integer> wStepIDList,
			List<MSSBOMItem> wNewItemList) {
		try {
			for (Integer wStepID : wStepIDList) {
				int wPlaceID = 0;
				if (wItemList.stream().anyMatch(p -> p.PartPointID == wStepID)) {
					wPlaceID = wItemList.stream().filter(p -> p.PartPointID == wStepID).findFirst().get().PlaceID;
				}
				if (wPlaceID <= 0) {
					if (wNewItemList.stream().anyMatch(p -> p.PartPointID == wStepID)) {
						wPlaceID = wNewItemList.stream().filter(p -> p.PartPointID == wStepID).findFirst()
								.get().PlaceID;
					}
				}

				// ???????????????
				List<MSSBOMItem> wStepAllList = wNewItemList.stream().filter(p -> p.PartPointID == wStepID)
						.collect(Collectors.toList());
				List<MSSBOMItem> wAddedList = wStepAllList.stream()
						.filter(p -> !wItemList.stream().anyMatch(q -> q.PlaceID == p.PlaceID
								&& q.PartPointID == p.PartPointID && q.MaterialID == p.MaterialID))
						.collect(Collectors.toList());

				if (!wItemList.stream().anyMatch(p -> p.PartPointID == wStepID)) {
					wAddedList = wStepAllList;
				}

				for (MSSBOMItem wMSSBOMItem : wAddedList) {
					TCMMaterialChangeItems wItem = CloneTool.Clone(wMSSBOMItem, TCMMaterialChangeItems.class);
					wItem.ID = 0;
					wItem.ChangeLogID = wTCMMaterialChangeLog.ID;
					wItem.Active = 1;
					wItem.Author = "SHRIS";
					wItem.PartPointName = WsdlConstants.GetFPCStepName(wItem.PartPointID);
					wItem.ChangeType = TCMChangeType.MaterialInsert.getValue();
					wItem.BOMNo1 = wBOMNo1;
					wItem.BOMNo2 = wBOMNo2;
					wItem.Methods = "";
					wItem.RouteNo1 = "";
					wItem.RouteNo2 = "";
					wChangeItemList.add(wItem);
				}
				// ???????????????
				if (wPlaceID > 0) {
					int wClonedPlaceID = wPlaceID;
					List<MSSBOMItem> wAllList = wItemList.stream()
							.filter(p -> p.PlaceID == wClonedPlaceID && p.PartPointID == wStepID)
							.collect(Collectors.toList());
					List<MSSBOMItem> wDeleteList = wAllList.stream()
							.filter(p -> !wNewItemList.stream().anyMatch(q -> q.PlaceID == p.PlaceID
									&& q.PartPointID == p.PartPointID && q.MaterialID == p.MaterialID))
							.collect(Collectors.toList());
					for (MSSBOMItem wMSSBOMItem : wDeleteList) {
						TCMMaterialChangeItems wItem = CloneTool.Clone(wMSSBOMItem, TCMMaterialChangeItems.class);
						wItem.ID = 0;
						wItem.ChangeLogID = wTCMMaterialChangeLog.ID;
						wItem.ChangeType = TCMChangeType.MaterialDelete.getValue();
						wItem.BOMNo1 = wBOMNo1;
						wItem.BOMNo2 = wBOMNo2;
						wItem.Methods = "";
						wItem.RouteNo1 = "";
						wItem.RouteNo2 = "";
						wChangeItemList.add(wItem);
					}
				}
				// ?????????????????????(???????????????????????????)
				if (wPlaceID > 0) {
					int wClonedPlaceID = wPlaceID;
					List<MSSBOMItem> wAllList = wItemList.stream()
							.filter(p -> p.PlaceID == wClonedPlaceID && p.PartPointID == wStepID)
							.collect(Collectors.toList());
					List<MSSBOMItem> wChangeList = wNewItemList.stream().filter(p -> wAllList.stream()
							.anyMatch(q -> q.PartPointID == p.PartPointID && q.PlaceID == p.PlaceID
									&& p.ReplaceType == q.ReplaceType && p.OutsourceType == q.OutsourceType
									&& p.MaterialID == q.MaterialID && p.MaterialNumber != q.MaterialNumber)
							&& !wAllList.stream()
									.anyMatch(q -> q.PartPointID == p.PartPointID && q.PlaceID == p.PlaceID
											&& q.ReplaceType == p.ReplaceType && q.OutsourceType == p.OutsourceType
											&& p.MaterialID == q.MaterialID && p.MaterialNumber == q.MaterialNumber))
							.collect(Collectors.toList());
					for (MSSBOMItem wMSSBOMItem : wChangeList) {
						double wOldMaterialNumber = wAllList.stream()
								.filter(p -> p.PartPointID == wMSSBOMItem.PartPointID
										&& p.PlaceID == wMSSBOMItem.PlaceID && p.MaterialID == wMSSBOMItem.MaterialID
										&& p.ReplaceType == wMSSBOMItem.ReplaceType
										&& p.OutsourceType == wMSSBOMItem.OutsourceType
										&& p.MaterialNumber != wMSSBOMItem.MaterialNumber)
								.findFirst().get().MaterialNumber;

						TCMMaterialChangeItems wItem = CloneTool.Clone(wMSSBOMItem, TCMMaterialChangeItems.class);

						wItem.ID = 0;
						wItem.ChangeLogID = wTCMMaterialChangeLog.ID;
						wItem.ChangeType = TCMChangeType.MaterialNumberChange.getValue();
						wItem.BOMNo1 = wBOMNo1;
						wItem.BOMNo2 = wBOMNo2;
						wItem.PartPointName = WsdlConstants.GetFPCStepName(wItem.PartPointID);
						wItem.Methods = "";
						wItem.RouteNo1 = "";
						wItem.RouteNo2 = "";
						wItem.OldMaterialNumber = wOldMaterialNumber;

						wChangeItemList.add(wItem);
					}
				}
				// ?????????????????????(?????????????????????????????????)
				if (wPlaceID > 0) {
					int wClonedPlaceID = wPlaceID;
					List<MSSBOMItem> wAllList = wItemList.stream()
							.filter(p -> p.PlaceID == wClonedPlaceID && p.PartPointID == wStepID)
							.collect(Collectors.toList());
//					List<MSSBOMItem> wChangeList = wNewItemList.stream()
//							.filter(p -> wAllList.stream()
//									.anyMatch(q -> q.PartPointID == p.PartPointID && q.PlaceID == p.PlaceID
//											&& p.MaterialID == q.MaterialID && p.ReplaceType != q.ReplaceType)
//									&& !wAllList.stream()
//											.anyMatch(q -> q.PartPointID == p.PartPointID && q.PlaceID == p.PlaceID
//													&& p.MaterialID == q.MaterialID && p.ReplaceType == q.ReplaceType))
//							.collect(Collectors.toList());
//					for (MSSBOMItem wMSSBOMItem : wChangeList) {
//						TCMMaterialChangeItems wItem = CloneTool.Clone(wMSSBOMItem, TCMMaterialChangeItems.class);
//						wItem.ID = 0;
//						wItem.ChangeLogID = wTCMMaterialChangeLog.ID;
//						wItem.ChangeType = TCMChangeType.MaterialPropertyChange.getValue();
//						wItem.BOMNo1 = wBOMNo1;
//						wItem.BOMNo2 = wBOMNo2;
//						wItem.PartPointName = WsdlConstants.GetFPCStepName(wItem.PartPointID);
//						wItem.Methods = "";
//						wItem.RouteNo1 = "";
//						wItem.RouteNo2 = "";
//						wChangeItemList.add(wItem);
//					}

					List<MSSBOMItem> wChangeList = wNewItemList.stream()
							.filter(p -> p.PlaceID == wClonedPlaceID && p.PartPointID == wStepID)
							.collect(Collectors.toList());
					for (MSSBOMItem wMSSBOMItem : wChangeList) {
						if (wAllList.stream().anyMatch(p -> p.PlaceID == wMSSBOMItem.PlaceID
								&& p.PartPointID == wMSSBOMItem.PartPointID && p.MaterialID == wMSSBOMItem.MaterialID
								&& p.ReplaceType == wMSSBOMItem.ReplaceType
								&& p.OutsourceType == wMSSBOMItem.OutsourceType)) {
							continue;
						}

						if (!wAllList.stream()
								.anyMatch(p -> p.PlaceID == wMSSBOMItem.PlaceID
										&& p.PartPointID == wMSSBOMItem.PartPointID
										&& p.MaterialID == wMSSBOMItem.MaterialID)) {
							continue;
						}

						MSSBOMItem wExitItem = wAllList.stream()
								.filter(p -> p.PlaceID == wMSSBOMItem.PlaceID
										&& p.PartPointID == wMSSBOMItem.PartPointID
										&& p.MaterialID == wMSSBOMItem.MaterialID)
								.findFirst().get();

						String wPCText = "";
						// ??????????????????
						if (wExitItem.ReplaceType == 1 && wMSSBOMItem.ReplaceType == 2 && wExitItem.OutsourceType == 0
								&& wMSSBOMItem.OutsourceType == 0) {
							wPCText = "???????????????";
						}
						// ????????????????????????
						else if (wExitItem.ReplaceType == 1 && wExitItem.OutsourceType == 0
								&& wMSSBOMItem.OutsourceType == 1) {
							wPCText = "?????????????????????";
						}
						// ????????????????????????
						else if (wExitItem.ReplaceType == 1 && wExitItem.OutsourceType == 0
								&& wMSSBOMItem.OutsourceType == 2) {
							wPCText = "?????????????????????";
						}
						// ??????????????????
						else if (wExitItem.ReplaceType == 2 && wExitItem.OutsourceType == 0
								&& wMSSBOMItem.ReplaceType == 1 && wMSSBOMItem.OutsourceType == 0) {
							wPCText = "???????????????";
						}
						// ????????????????????????
						else if (wExitItem.ReplaceType == 2 && wExitItem.OutsourceType == 0
								&& wMSSBOMItem.ReplaceType == 0 && wMSSBOMItem.OutsourceType == 2) {
							wPCText = "?????????????????????";
						}
						// ????????????????????????
						else if (wExitItem.ReplaceType == 2 && wExitItem.OutsourceType == 0
								&& wMSSBOMItem.OutsourceType == 1) {
							wPCText = "?????????????????????";
						}
						// ???????????????????????????
						else if (wExitItem.ReplaceType != 1 && wExitItem.OutsourceType == 1
								&& wMSSBOMItem.ReplaceType == 1 && wMSSBOMItem.OutsourceType != 1) {
							wPCText = "?????????????????????";
						}
						// ???????????????????????????
						else if (wExitItem.ReplaceType != 1 && wExitItem.OutsourceType == 1
								&& wMSSBOMItem.ReplaceType == 2 && wMSSBOMItem.OutsourceType == 0) {
							wPCText = "?????????????????????";
						}
						// ???????????????????????????
						else if (wExitItem.ReplaceType == 0 && wExitItem.OutsourceType == 2
								&& wMSSBOMItem.ReplaceType == 1 && wMSSBOMItem.OutsourceType == 0) {
							wPCText = "?????????????????????";
						}
						// ???????????????????????????
						else if (wExitItem.ReplaceType == 0 && wExitItem.OutsourceType == 2
								&& wMSSBOMItem.ReplaceType == 2 && wMSSBOMItem.OutsourceType == 0) {
							wPCText = "?????????????????????";
						}
						// ?????????????????????????????????
						else if (wExitItem.OutsourceType == 1 && wMSSBOMItem.ReplaceType == 0
								&& wMSSBOMItem.OutsourceType == 2) {
							wPCText = "???????????????????????????";
						}
						// ?????????????????????????????????
						else if (wExitItem.OutsourceType == 2 && wMSSBOMItem.OutsourceType == 1) {
							wPCText = "???????????????????????????";
						}
						// ??????????????????(??????)????????????
						else if (wExitItem.ReplaceType == 0 && wExitItem.OutsourceType == 1
								&& wMSSBOMItem.ReplaceType == 2 && wMSSBOMItem.OutsourceType == 1) {
							wPCText = "???????????????(??????)????????????";
						}
						// ??????????????????(??????)????????????
						else if (wExitItem.ReplaceType == 0 && wExitItem.OutsourceType == 2
								&& wMSSBOMItem.ReplaceType == 2 && wMSSBOMItem.OutsourceType == 2) {
							wPCText = "???????????????(??????)????????????";
						}

						TCMMaterialChangeItems wItem = CloneTool.Clone(wMSSBOMItem, TCMMaterialChangeItems.class);
						wItem.ID = 0;
						wItem.ChangeLogID = wTCMMaterialChangeLog.ID;
						wItem.ChangeType = TCMChangeType.MaterialPropertyChange.getValue();
						wItem.BOMNo1 = wBOMNo1;
						wItem.BOMNo2 = wBOMNo2;
						wItem.Methods = "";
						wItem.RouteNo1 = "";
						wItem.RouteNo2 = "";
						wItem.PropertyChangeText = wPCText;
						wItem.OldReplaceType = wExitItem.ReplaceType;
						wItem.OldOutSourceType = wExitItem.OutsourceType;
						wItem.PartPointName = WsdlConstants.GetFPCStepName(wItem.PartPointID);
						wChangeItemList.add(wItem);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ??????????????????
	 */
	@SuppressWarnings("unused")
	private void HandleTechChange(MSSBOM wNewBom, List<MSSBOMItem> wItemList) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ????????????????????????????????????????????????????????????BOM?????????????????????
			int wBOMID = TCMBOMDAO.getInstance().GetMSSBomID(BaseDAO.SysAdmin, wNewBom.ProductID, wNewBom.LineID,
					wNewBom.CustomerID, wNewBom.ID, wErrorCode);
			if (wBOMID <= 0) {
				return;
			}
			// ???????????????BOMID????????????BOM????????????
			List<MSSBOMItem> wBOMItemList = CoreServiceImpl.getInstance()
					.MSS_QueryBOMItemAll(BaseDAO.SysAdmin, wBOMID, -1).List(MSSBOMItem.class);
			if (wBOMItemList == null || wBOMItemList.size() <= 0) {
				return;
			}
			// ?????????????????????????????????????????????????????????????????????
			List<Integer> wOrderIDList = TCMBOMDAO.getInstance().GetRepairingOrderIDList(BaseDAO.SysAdmin,
					wNewBom.ProductID, wNewBom.LineID, wNewBom.CustomerID, wErrorCode);
			if (wOrderIDList == null || wOrderIDList.size() <= 0) {
				return;
			}
			// ???????????????
			List<Integer> wStepIDList = wItemList.stream().map(p -> p.PartPointID).distinct()
					.collect(Collectors.toList());
			wStepIDList.removeIf(p -> p <= 0);
			// ???????????????????????????????????????????????????
			for (Integer wStepID : wStepIDList) {
				// ???????????????
				if (!wBOMItemList.stream().anyMatch(p -> p.PartPointID == wStepID)) {
					continue;
				}

				List<MSSBOMItem> wOldList = wBOMItemList.stream().filter(p -> p.PartPointID == wStepID)
						.collect(Collectors.toList());

				// ??????????????????????????????????????????SAP
				Situation01(wNewBom, wItemList, wErrorCode, wOrderIDList, wStepID, wOldList);
				// ????????????????????????????????????????????????(??????????????????????????????)?????????SAP
				Situation02(wNewBom, wItemList, wErrorCode, wOrderIDList, wStepID, wOldList);
				// ???????????????????????????????????????????????????
				Situation03(wNewBom, wItemList, wErrorCode, wOrderIDList, wStepID, wOldList);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ?????????????????????????????????????????????(??????????????????????????????,??????SAP)
	 */
	private void Situation02(MSSBOM wNewBom, List<MSSBOMItem> wItemList, OutResult<Integer> wErrorCode,
			List<Integer> wOrderIDList, Integer wStepID, List<MSSBOMItem> wOldList) {
		try {
			List<MSSBOMItem> wRemovedItemList = wOldList.stream().filter(
					p -> p.PartPointID == wStepID && !wItemList.stream().anyMatch(q -> q.MaterialID == p.MaterialID))
					.collect(Collectors.toList());
			if (wRemovedItemList.size() > 0) {
				for (Integer wOrderID : wOrderIDList) {
					OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(BaseDAO.SysAdmin, wOrderID, wErrorCode);
					if (!TCMBOMDAO.getInstance().JudgeIsStepFinish(BaseDAO.SysAdmin, wOrderID,
							wRemovedItemList.get(0).PlaceID, wStepID, wErrorCode)) {
						// ????????????????????????
						FPCPart wFPCPart = WsdlConstants.GetFPCPart(wRemovedItemList.get(0).PlaceID);
						// ????????????????????????
						List<BFCMessage> wBFCMessageList = new ArrayList<>();
						BFCMessage wMessage = null;

						SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
						int wShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));
						for (Integer wUserID : wFPCPart.CheckerList) {
							// ???????????????????????????
							wMessage = new BFCMessage();
							wMessage.Active = 0;
							wMessage.CompanyID = 0;
							wMessage.CreateTime = Calendar.getInstance();
							wMessage.EditTime = Calendar.getInstance();
							wMessage.ID = 0;
							wMessage.MessageID = wOrderID;
							wMessage.Title = StringUtils.Format("???{0}???-{1}??????BOM??????????????????????????????????????????",
									BPMEventModule.SBOMChange_Exception.getLable(), wOrder.PartNo);
							wMessage.MessageText = StringUtils.Format("???{0}???-???{1}??????????????????{2}???({3})???", wFPCPart.Name,
									WsdlConstants.GetFPCStepName(wRemovedItemList.get(0).PartPointID),
									StringUtils.Join(",",
											wRemovedItemList.stream().map(p -> p.MaterialName)
													.collect(Collectors.toList())),
									StringUtils.Join(",", wRemovedItemList.stream().map(p -> p.MaterialNo)
											.collect(Collectors.toList())));
							wMessage.ModuleID = BPMEventModule.SBOMChange_Exception.getValue();
							wMessage.ResponsorID = wUserID;
							wMessage.ShiftID = wShiftID;
							wMessage.StationID = wRemovedItemList.get(0).PlaceID;
							wMessage.StepID = wRemovedItemList.get(0).PartPointID;
							wMessage.Type = BFCMessageType.Task.getValue();
							wBFCMessageList.add(wMessage);
						}
						CoreServiceImpl.getInstance().BFC_UpdateMessageList(BaseDAO.SysAdmin, wBFCMessageList);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ????????????????????????????????????????????????
	 */
	private void Situation03(MSSBOM wNewBom, List<MSSBOMItem> wItemList, OutResult<Integer> wErrorCode,
			List<Integer> wOrderIDList, Integer wStepID, List<MSSBOMItem> wOldList) {
		try {
			List<MSSBOMItem> wAddedItemList = wItemList.stream().filter(
					p -> p.PartPointID == wStepID && !wOldList.stream().anyMatch(q -> q.MaterialID == p.MaterialID))
					.collect(Collectors.toList());
			if (wAddedItemList.size() > 0) {
				for (Integer wOrderID : wOrderIDList) {
					OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(BaseDAO.SysAdmin, wOrderID, wErrorCode);
					if (TCMBOMDAO.getInstance().JudgeIsStepFinish(BaseDAO.SysAdmin, wOrderID,
							wAddedItemList.get(0).PlaceID, wStepID, wErrorCode)) {
						// ???????????????BOM
						wAddedItemList = wAddedItemList.stream().filter(p -> p.ReplaceType == 1 || p.OutsourceType == 1)
								.collect(Collectors.toList());
						// ?????????SAP
						for (MSSBOMItem wMSSBOMItem : wAddedItemList) {
							APSBOMItem wBOMItem = new APSBOMItem(wMSSBOMItem, wOrder.LineID, wOrder.ProductID,
									wNewBom.CustomerID, wOrderID, wOrder.OrderNo, wOrder.PartNo);

							wBOMItem.SourceType = APSBOMSourceType.BOMChange.getValue();
							wBOMItem.SourceID = wMSSBOMItem.ID;
							wBOMItem.AuditorID = BaseDAO.SysAdmin.ID;
							wBOMItem.EditorID = BaseDAO.SysAdmin.ID;
							wBOMItem.AuditTime = Calendar.getInstance();
							wBOMItem.EditTime = Calendar.getInstance();

							CoreServiceImpl.getInstance().APS_SaveUpdateBomItem(BaseDAO.SysAdmin, wBOMItem);
						}
						// ????????????????????????
						FPCPart wFPCPart = WsdlConstants.GetFPCPart(wAddedItemList.get(0).PlaceID);
						// ????????????????????????
						List<BFCMessage> wBFCMessageList = new ArrayList<>();
						BFCMessage wMessage = null;

						SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
						int wShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));
						for (Integer wUserID : wFPCPart.CheckerList) {
							// ???????????????????????????
							wMessage = new BFCMessage();
							wMessage.Active = 0;
							wMessage.CompanyID = 0;
							wMessage.CreateTime = Calendar.getInstance();
							wMessage.EditTime = Calendar.getInstance();
							wMessage.ID = 0;
							wMessage.MessageID = wOrderID;
							wMessage.Title = StringUtils.Format("???{0}???-{1}??????BOM????????????????????????????????????",
									BPMEventModule.SBOMChange_Repair.getLable(), wOrder.PartNo);
							wMessage.MessageText = StringUtils.Format("???{0}???-???{1}??????????????????{2}??????", wFPCPart.Name,
									WsdlConstants.GetFPCStepName(wAddedItemList.get(0).PartPointID),
									StringUtils.Join(",", wAddedItemList.stream().map(p -> p.MaterialName)
											.collect(Collectors.toList())));
							wMessage.ModuleID = BPMEventModule.SBOMChange_Repair.getValue();
							wMessage.ResponsorID = wUserID;
							wMessage.ShiftID = wShiftID;
							wMessage.StationID = wAddedItemList.get(0).PlaceID;
							wMessage.StepID = wAddedItemList.get(0).PartPointID;
							wMessage.Type = BFCMessageType.Task.getValue();
							wBFCMessageList.add(wMessage);
						}
						CoreServiceImpl.getInstance().BFC_UpdateMessageList(BaseDAO.SysAdmin, wBFCMessageList);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ???????????????????????????????????????SAP
	 */
	private void Situation01(MSSBOM wNewBom, List<MSSBOMItem> wItemList, OutResult<Integer> wErrorCode,
			List<Integer> wOrderIDList, Integer wStepID, List<MSSBOMItem> wOldList) {
		try {
			List<MSSBOMItem> wAddedItemList = wItemList.stream().filter(
					p -> p.PartPointID == wStepID && !wOldList.stream().anyMatch(q -> q.MaterialID == p.MaterialID))
					.collect(Collectors.toList());
			if (wAddedItemList.size() > 0) {
				for (Integer wOrderID : wOrderIDList) {
					OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(BaseDAO.SysAdmin, wOrderID, wErrorCode);
					if (!TCMBOMDAO.getInstance().JudgeIsStepFinish(BaseDAO.SysAdmin, wOrderID,
							wAddedItemList.get(0).PlaceID, wStepID, wErrorCode)) {
						// ???????????????BOM
						wAddedItemList = wAddedItemList.stream().filter(p -> p.ReplaceType == 1 || p.OutsourceType == 1)
								.collect(Collectors.toList());
						// ?????????SAP
						for (MSSBOMItem wMSSBOMItem : wAddedItemList) {
							APSBOMItem wBOMItem = new APSBOMItem(wMSSBOMItem, wOrder.LineID, wOrder.ProductID,
									wNewBom.CustomerID, wOrderID, wOrder.OrderNo, wOrder.PartNo);

							wBOMItem.SourceType = APSBOMSourceType.BOMChange.getValue();
							wBOMItem.SourceID = wMSSBOMItem.ID;
							wBOMItem.AuditorID = BaseDAO.SysAdmin.ID;
							wBOMItem.EditorID = BaseDAO.SysAdmin.ID;
							wBOMItem.AuditTime = Calendar.getInstance();
							wBOMItem.EditTime = Calendar.getInstance();

							CoreServiceImpl.getInstance().APS_SaveUpdateBomItem(BaseDAO.SysAdmin, wBOMItem);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public APIResult MSS_BOMStandard(BMSEmployee wLoginUser, int wBOMID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("ID", wBOMID);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Bom/Standard?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_SaveBOMItemList(BMSEmployee wLoginUser, List<MSSBOMItem> wMSSBOMItemList) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wMSSBOMItemList);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/BomItem/UpdateList?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_DynamicCompareBop(BMSEmployee wLoginUser, int wNewRouteID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("NewRouteID", wNewRouteID);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, "MESQMS",
					StringUtils.Format("api/FPCRoute/DynamicCompareBop?cadv_ao={0}&cade_po={1}",
							wLoginUser.getLoginName(), wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> FMC_TechChange(BMSEmployee wLoginUser, int wNewBOMID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			MSSBOM wNewBOM = FMCServiceImpl.getInstance().MSS_QueryBOM(BaseDAO.SysAdmin, wNewBOMID, "")
					.Info(MSSBOM.class);
			List<MSSBOMItem> wItemList = CoreServiceImpl.getInstance()
					.MSS_QueryBOMItemAll(BaseDAO.SysAdmin, wNewBOMID, -1).List(MSSBOMItem.class);
			List<FPCRoute> wRouteList = FMCServiceImpl.getInstance().FPC_QueryRouteList(BaseDAO.SysAdmin, -1, -1, -1)
					.List(FPCRoute.class);

			HandleTechChange01(wNewBOM, wItemList, wRouteList, 0);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public void StepTechChange(BOP wBOP, int wRouteID, FPCRoute wRoute, int wLogID) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ????????????????????????????????????????????????????????????BOM?????????????????????
			int wBOMID = TCMBOMDAO.getInstance().SelectNewBOMID(BaseDAO.SysAdmin, wRoute.LineID, wRoute.ProductID,
					wRoute.CustomerID, wErrorCode);
			if (wBOMID <= 0) {
				return;
			}

			// ??????bomid????????????????????????????????????
			List<OMSOrder> wRowsOrderList = BOPDAO.getInstance().GetInspectOrderList(BaseDAO.SysAdmin,
					new ArrayList<Integer>(Arrays.asList(wBOMID)), wErrorCode);

			/**
			 * ???????????????????????????????????????
			 */
			if (wRowsOrderList.size() <= 0) {
				return;
			}

			// ???????????????BOP
			List<FPCRoute> wRouteList = FMCServiceImpl.getInstance().FPC_QueryRouteList(BaseDAO.SysAdmin, -1, -1, -1)
					.List(FPCRoute.class);

			TCMMaterialChangeLog wTCMMaterialChangeLog = new TCMMaterialChangeLog();

			wTCMMaterialChangeLog.CreateID = BaseDAO.SysAdmin.ID;
			wTCMMaterialChangeLog.CreateTime = Calendar.getInstance();
			wTCMMaterialChangeLog.ID = 0;
			wTCMMaterialChangeLog.OrderIDList = StringUtils.Join(",",
					wRowsOrderList.stream().map(p -> p.ID).collect(Collectors.toList()));
			wTCMMaterialChangeLog.PartNoList = StringUtils.Join(",",
					wRowsOrderList.stream().map(p -> p.PartNo).collect(Collectors.toList()));
			wTCMMaterialChangeLog.ProductID = wRoute.ProductID;
			wTCMMaterialChangeLog.LineID = wRoute.LineID;
			wTCMMaterialChangeLog.CustomerID = wRoute.CustomerID;
			wTCMMaterialChangeLog.Customer = WsdlConstants.GetCRMCustomerName(wRoute.CustomerID);

			wTCMMaterialChangeLog.ChangeFormNo = wBOP.getPROCESSLIST().get(0).getPROCESSCNO();
			wTCMMaterialChangeLog.ChangeFormUri = wBOP.getPROCESSLIST().get(0).getPROCESSC();
			wTCMMaterialChangeLog.ChangeType = wBOP.getPROCESSLIST().get(0).getPROCESSCTYPE();
			wTCMMaterialChangeLog.ChangeUser = wBOP.getPROCESSLIST().get(0).getPROCESSCUSER();

			wTCMMaterialChangeLog.ID = TCMMaterialChangeLogDAO.getInstance().Update(BaseDAO.SysAdmin,
					wTCMMaterialChangeLog, wErrorCode);

			List<TCMMaterialChangeItems> wChangeItemList = new ArrayList<TCMMaterialChangeItems>();

			// ???????????????????????????????????????
			// ???????????????????????????
			int wPartID = WsdlConstants.GetFPCPartByCode(wBOP.getPROCESSLIST().get(0).getUSR00()).ID;
			StepChangeCreate(wRouteList, wTCMMaterialChangeLog, wChangeItemList, wRouteID, wPartID);

			// ??????????????????
			for (TCMMaterialChangeItems wItem : wChangeItemList) {
				if (wItem.Annex == null) {
					wItem.Annex = "";
				}
			}
			// ????????????????????????
			TCMMaterialChangeItemsDAO.getInstance().InsertList(BaseDAO.SysAdmin, wChangeItemList, wErrorCode);

			// ???????????????????????????
			SendMessageToCraft(wTCMMaterialChangeLog.ID, wRouteID, wLogID, wBOP);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ????????????????????????
	 */
	private void SendMessageToCraft(int iD, int wRouteID, int wLogID, BOP wBOP) {
		try {
			List<BFCMessage> wBFCMessageList = new ArrayList<>();
			BFCMessage wMessage = null;
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			int wShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));

			String wChangNo = "";

			List<Integer> wUserList = new ArrayList<Integer>();
			if (wRouteID > 0) {
				wChangNo = wBOP.getPROCESSLIST().get(0).getPROCESSCNO();

				if (StringUtils.isNotEmpty(wBOP.getPROCESSLIST().get(0).getPROCESSCUSER())) {
					if (WsdlConstants.GetBMSEmployeeList().values().stream()
							.anyMatch(p -> wBOP.getPROCESSLIST().get(0).getPROCESSCUSER().contains(p.LoginID))) {
						wUserList.add(WsdlConstants.GetBMSEmployeeList().values().stream()
								.filter(p -> wBOP.getPROCESSLIST().get(0).getPROCESSCUSER().contains(p.LoginID))
								.findFirst().get().ID);
					} else {
						SetTechNoticeUser(wUserList);
					}
				} else {
					SetTechNoticeUser(wUserList);
				}
			} else {
				SetTechNoticeUser(wUserList);
			}

			// ?????????????????????
			for (int wUserID : wUserList) {
				// ???????????????????????????
				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = wLogID;
				wMessage.Title = StringUtils.Format("??????????????????{0}", wChangNo);
				wMessage.MessageText = "??????????????????????????????????????????????????????????????????";
				wMessage.ModuleID = BPMEventModule.SBOMChange.getValue();
				wMessage.ResponsorID = wUserID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0;
				wMessage.Type = BFCMessageType.Task.getValue();
				wMessage.StepID = wLogID;
				wBFCMessageList.add(wMessage);
			}

			CoreServiceImpl.getInstance().BFC_UpdateMessageList(BaseDAO.SysAdmin, wBFCMessageList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}
}
