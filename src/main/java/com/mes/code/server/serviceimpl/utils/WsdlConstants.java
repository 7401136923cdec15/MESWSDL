package com.mes.code.server.serviceimpl.utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.service.po.cfg.CFGUnit;
import com.mes.code.server.service.po.crm.CRMCustomer;
import com.mes.code.server.service.po.fmc.FMCBusinessUnit;
import com.mes.code.server.service.po.fmc.FMCFactory;
import com.mes.code.server.service.po.fmc.FMCLine;
import com.mes.code.server.service.po.fmc.FMCStation;
import com.mes.code.server.service.po.fmc.FMCWorkShop;
import com.mes.code.server.service.po.fmc.FMCWorkspace;
import com.mes.code.server.service.po.fpc.FPCPart;
import com.mes.code.server.service.po.fpc.FPCPartPoint;
import com.mes.code.server.service.po.fpc.FPCProduct;
import com.mes.code.server.service.po.fpc.FPCRoute;
import com.mes.code.server.service.po.fpc.FPCRoutePart;
import com.mes.code.server.service.po.fpc.FPCRoutePartPoint;
import com.mes.code.server.service.po.tcm.ThreadOP;
import com.mes.code.server.serviceimpl.CoreServiceImpl;
import com.mes.code.server.serviceimpl.FMCServiceImpl;
import com.mes.code.server.serviceimpl.SCMServiceImpl;
import com.mes.code.server.serviceimpl.dao.BaseDAO;

public class WsdlConstants {
	private static Logger logger = LoggerFactory.getLogger(WsdlConstants.class);

	public WsdlConstants() {
		// TODO Auto-generated constructor stub
	}

	private static String ConfigPath = null;

	public static synchronized String getConfigPath() {
		if (ConfigPath == null) {
			try {
				ConfigPath = ResourceUtils.getURL("classpath:config").getPath().replace("%20", " ");

				if (ConfigPath != null && ConfigPath.length() > 3 && ConfigPath.indexOf(":") > 0) {
					if (ConfigPath.indexOf("/") == 0)
						ConfigPath = ConfigPath.substring(1);

					if (!ConfigPath.endsWith("/"))
						ConfigPath = ConfigPath + "/";
				}
			} catch (FileNotFoundException e) {
				return "config/";
			}
		}
		return ConfigPath;
	}

	// endRegion

	// region 台位全局数据
	private static Calendar RefreshWorkspaceTime = Calendar.getInstance();

	private static Map<Integer, FMCWorkspace> FMCWorkspaceDic = new HashMap<Integer, FMCWorkspace>();

	public static synchronized Map<Integer, FMCWorkspace> GetFMCWorkspaceList() {
		if (FMCWorkspaceDic == null || FMCWorkspaceDic.size() <= 0
				|| RefreshWorkspaceTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCWorkspace> wFMCWorkspaceList = FMCServiceImpl.getInstance()
					.FMC_GetFMCWorkspaceList(BaseDAO.SysAdmin, 0, 0, "", 0, 0).List(FMCWorkspace.class);
			if (wFMCWorkspaceList != null && wFMCWorkspaceList.size() > 0) {
				FMCWorkspaceDic = wFMCWorkspaceList.stream()
						.collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshWorkspaceTime = Calendar.getInstance();
			RefreshWorkspaceTime.add(Calendar.MINUTE, 3);
		}
		return FMCWorkspaceDic;
	}

	public static String GetFMCWorkspaceName(int wID) {
		String wResult = "";
		if (WsdlConstants.GetFMCWorkspaceList().containsKey(wID)) {
			if (WsdlConstants.GetFMCWorkspaceList().get(wID) != null) {
				wResult = WsdlConstants.GetFMCWorkspaceList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCWorkspace GetFMCWorkspace(int wID) {
		FMCWorkspace wResult = new FMCWorkspace();
		if (WsdlConstants.GetFMCWorkspaceList().containsKey(wID)) {
			if (WsdlConstants.GetFMCWorkspaceList().get(wID) != null) {
				wResult = WsdlConstants.GetFMCWorkspaceList().get(wID);
			}
		}
		return wResult;
	}

	// endRegion

	// region 用户全局数据
	private static Calendar RefreshEmployeeTime = Calendar.getInstance();

	private static Map<Integer, BMSEmployee> BMSEmployeeDic = new HashMap<Integer, BMSEmployee>();

	public static synchronized Map<Integer, BMSEmployee> GetBMSEmployeeList() {
		if (BMSEmployeeDic == null || BMSEmployeeDic.size() <= 0
				|| RefreshEmployeeTime.compareTo(Calendar.getInstance()) < 0) {
			List<BMSEmployee> wBMSEmployeeList = CoreServiceImpl.getInstance()
					.BMS_GetEmployeeAll(BaseDAO.SysAdmin, 0, 0, 1).List(BMSEmployee.class);
			if (wBMSEmployeeList != null && wBMSEmployeeList.size() > 0) {
				BMSEmployeeDic = wBMSEmployeeList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshEmployeeTime = Calendar.getInstance();
			RefreshEmployeeTime.add(Calendar.MINUTE, 3);
		}
		return BMSEmployeeDic;
	}

	public static BMSEmployee GetBMSEmployee(int wID) {
		if (WsdlConstants.GetBMSEmployeeList().containsKey(wID)) {
			if (WsdlConstants.GetBMSEmployeeList().get(wID) != null) {
				return WsdlConstants.GetBMSEmployeeList().get(wID);
			}
		}
		return new BMSEmployee();
	}

	public static String GetBMSEmployeeName(int wID) {
		String wResult = "";
		if (WsdlConstants.GetBMSEmployeeList().containsKey(wID)) {
			if (WsdlConstants.GetBMSEmployeeList().get(wID) != null) {
				wResult = WsdlConstants.GetBMSEmployeeList().get(wID).getName();
			}
		}
		return wResult;
	}
	// endRegion

	// region 产线全局数据
	private static Calendar RefreshLineTime = Calendar.getInstance();

	private static Map<Integer, FMCLine> FMCLineDic = new HashMap<Integer, FMCLine>();

	public static synchronized Map<Integer, FMCLine> GetFMCLineList() {
		if (FMCLineDic == null || FMCLineDic.size() <= 0 || RefreshLineTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCLine> wFMCLineList = FMCServiceImpl.getInstance().FMC_QueryLineList(BaseDAO.SysAdmin, 0, 0, 0)
					.List(FMCLine.class);
			if (wFMCLineList != null && wFMCLineList.size() > 0) {
				FMCLineDic = wFMCLineList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshLineTime = Calendar.getInstance();
			RefreshLineTime.add(Calendar.MINUTE, 3);
		}
		return FMCLineDic;
	}

	public static String GetFMCLineName(int wID) {
		String wResult = "";
		if (WsdlConstants.GetFMCLineList().containsKey(wID)) {
			if (WsdlConstants.GetFMCLineList().get(wID) != null) {
				wResult = WsdlConstants.GetFMCLineList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCLine GetFMCLine(int wID) {
		FMCLine wResult = new FMCLine();
		if (WsdlConstants.GetFMCLineList().containsKey(wID)) {
			if (WsdlConstants.GetFMCLineList().get(wID) != null) {
				wResult = WsdlConstants.GetFMCLineList().get(wID);
			}
		}
		return wResult;
	}

	public static FMCLine GetFMCLine(String wName) {
		for (FMCLine wFMCLine : WsdlConstants.GetFMCLineList().values()) {
			if (wFMCLine.Name.equals(wName))
				return wFMCLine;
		}
		return new FMCLine();
	}

	// endRegion

	// region 车间全局数据
	private static Calendar RefreshWorkShopTime = Calendar.getInstance();

	private static Map<Integer, FMCWorkShop> FMCWorkShopDic = new HashMap<Integer, FMCWorkShop>();

	public static synchronized Map<Integer, FMCWorkShop> GetFMCWorkShopList() {
		if (FMCWorkShopDic == null || FMCWorkShopDic.size() <= 0
				|| RefreshWorkShopTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCWorkShop> wFMCWorkShopList = FMCServiceImpl.getInstance()
					.FMC_QueryWorkShopList(BaseDAO.SysAdmin, 0, 0).List(FMCWorkShop.class);
			if (wFMCWorkShopList != null && wFMCWorkShopList.size() > 0) {
				FMCWorkShopDic = wFMCWorkShopList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshWorkShopTime = Calendar.getInstance();
			RefreshWorkShopTime.add(Calendar.MINUTE, 3);
		}
		return FMCWorkShopDic;
	}

	public static String GetFMCWorkShopName(int wID) {
		String wResult = "";
		if (WsdlConstants.GetFMCWorkShopList().containsKey(wID)) {
			if (WsdlConstants.GetFMCWorkShopList().get(wID) != null) {
				wResult = WsdlConstants.GetFMCWorkShopList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCWorkShop GetFMCWorkShop(int wID) {
		FMCWorkShop wResult = new FMCWorkShop();
		if (WsdlConstants.GetFMCWorkShopList().containsKey(wID)) {
			if (WsdlConstants.GetFMCWorkShopList().get(wID) != null) {
				wResult = WsdlConstants.GetFMCWorkShopList().get(wID);
			}
		}
		return wResult;
	}

	// endRegion

	// region 工段全局数据
	private static Calendar RefreshPartTime = Calendar.getInstance();

	private static Map<Integer, FPCPart> FPCPartDic = new HashMap<Integer, FPCPart>();

	public static synchronized Map<Integer, FPCPart> GetFPCPartList() {
		if (FPCPartDic == null || FPCPartDic.size() <= 0 || RefreshPartTime.compareTo(Calendar.getInstance()) < 0) {
			List<FPCPart> wFPCPartList = FMCServiceImpl.getInstance().FPC_QueryPartList(BaseDAO.SysAdmin, 0, 0, 0, -1)
					.List(FPCPart.class);
			if (wFPCPartList != null && wFPCPartList.size() > 0) {
				FPCPartDic = wFPCPartList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshPartTime = Calendar.getInstance();
			RefreshPartTime.add(Calendar.MINUTE, 3);
		}
		return FPCPartDic;
	}

	public static String GetFPCPartName(int wID) {
		String wResult = "";
		if (WsdlConstants.GetFPCPartList().containsKey(wID)) {
			if (WsdlConstants.GetFPCPartList().get(wID) != null) {
				wResult = WsdlConstants.GetFPCPartList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static String GetFPCPartCode(int wID) {
		String wResult = "";
		if (WsdlConstants.GetFPCPartList().containsKey(wID)) {
			if (WsdlConstants.GetFPCPartList().get(wID) != null) {
				wResult = WsdlConstants.GetFPCPartList().get(wID).getCode();
			}
		}
		return wResult;
	}

	public static FPCPart GetFPCPart(String wName) {
		for (FPCPart wFPCPart : WsdlConstants.GetFPCPartList().values()) {
			if (wFPCPart.Name.equals(wName))
				return wFPCPart;
		}
		return new FPCPart();
	}

	public static FPCPart GetFPCPart(int wPartID) {
		for (FPCPart wFPCPart : WsdlConstants.GetFPCPartList().values()) {
			if (wFPCPart.ID == wPartID)
				return wFPCPart;
		}
		return new FPCPart();
	}

	public static FPCPart GetFPCPartByCode(String wCode) {
		for (FPCPart wFPCPart : WsdlConstants.GetFPCPartList().values()) {
			if (wFPCPart.Code.equals(wCode))
				return wFPCPart;
		}
		return new FPCPart();
	}

	// endRegion

	// region 产品全局数据
	private static Calendar RefreshProductTime = Calendar.getInstance();

	private static Map<Integer, FPCProduct> FPCProductDic = new HashMap<Integer, FPCProduct>();

	public static synchronized Map<Integer, FPCProduct> GetFPCProductList() {
		if (FPCProductDic == null || FPCProductDic.size() <= 0
				|| RefreshProductTime.compareTo(Calendar.getInstance()) < 0) {
			List<FPCProduct> wFPCProductList = FMCServiceImpl.getInstance().FPC_QueryProductList(BaseDAO.SysAdmin, 0, 0)
					.List(FPCProduct.class);
			if (wFPCProductList != null && wFPCProductList.size() > 0) {
				FPCProductDic = wFPCProductList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshProductTime = Calendar.getInstance();
			RefreshProductTime.add(Calendar.MINUTE, 3);
		}
		return FPCProductDic;
	}

	public static FPCProduct GetFPCProduct(int wID) {
		if (WsdlConstants.GetFPCProductList().containsKey(wID)) {
			if (WsdlConstants.GetFPCProductList().get(wID) != null) {
				return WsdlConstants.GetFPCProductList().get(wID);
			}
		}
		return new FPCProduct();
	}

	public static FPCProduct GetFPCProduct(String wProductNo) {
		for (FPCProduct wFPCProduct : WsdlConstants.GetFPCProductList().values()) {
			if (wFPCProduct.ProductNo.equals(wProductNo)) {
				return wFPCProduct;
			}
		}
		return new FPCProduct();
	}

	public static String GetFPCProductName(int wID) {
		String wResult = "";
		if (WsdlConstants.GetFPCProductList().containsKey(wID)) {
			if (WsdlConstants.GetFPCProductList().get(wID) != null) {
				wResult = WsdlConstants.GetFPCProductList().get(wID).getProductName();
			}
		}
		return wResult;
	}

	public static String GetFPCProductNo(int wID) {
		String wResult = "";
		if (WsdlConstants.GetFPCProductList().containsKey(wID)) {
			if (WsdlConstants.GetFPCProductList().get(wID) != null) {
				wResult = WsdlConstants.GetFPCProductList().get(wID).getProductNo();
			}
		}
		return wResult;
	}

	// endRegion

	// region 工序全局数据
	private static Calendar RefreshStepTime = Calendar.getInstance();

	private static Map<Integer, FPCPartPoint> FPCStepDic = new HashMap<Integer, FPCPartPoint>();

	public static synchronized Map<Integer, FPCPartPoint> GetFPCStepList() {
		if (FPCStepDic == null || FPCStepDic.size() <= 0 || RefreshStepTime.compareTo(Calendar.getInstance()) < 0) {
			List<FPCPartPoint> wFPCStepList = FMCServiceImpl.getInstance()
					.FPC_QueryPartPointList(BaseDAO.SysAdmin, 0, 0, 0).List(FPCPartPoint.class);
			if (wFPCStepList != null && wFPCStepList.size() > 0) {
				FPCStepDic = wFPCStepList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshStepTime = Calendar.getInstance();
			RefreshStepTime.add(Calendar.MINUTE, 1);
		}
		return FPCStepDic;
	}

	public static String GetFPCStepName(int wID) {
		String wResult = "";
		if (WsdlConstants.GetFPCStepList().containsKey(wID)) {
			if (WsdlConstants.GetFPCStepList().get(wID) != null) {
				wResult = WsdlConstants.GetFPCStepList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FPCPartPoint GetFPCStep(String wName) {
		for (FPCPartPoint wFPCPartPoint : WsdlConstants.GetFPCStepList().values()) {
			if (wFPCPartPoint.Name.equals(wName))
				return wFPCPartPoint;
		}
		return new FPCPartPoint();
	}

	// endRegion

	// region 工位全局数据
	private static Calendar RefreshStationTime = Calendar.getInstance();

	private static Map<Integer, FMCStation> FMCStationDic = new HashMap<Integer, FMCStation>();

	public static synchronized Map<Integer, FMCStation> GetFMCStationList() {
		if (FMCStationDic == null || FMCStationDic.size() <= 0
				|| RefreshStationTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCStation> wFMCStationList = FMCServiceImpl.getInstance().FMC_QueryStationList(BaseDAO.SysAdmin, 0, 0)
					.List(FMCStation.class);
			if (wFMCStationList != null && wFMCStationList.size() > 0) {
				FMCStationDic = wFMCStationList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshStationTime = Calendar.getInstance();
			RefreshStationTime.add(Calendar.MINUTE, 3);
		}
		return FMCStationDic;
	}

	public static String GetFMCStationName(int wID) {
		String wResult = "";
		if (WsdlConstants.GetFMCStationList().containsKey(wID)) {
			if (WsdlConstants.GetFMCStationList().get(wID) != null) {
				wResult = WsdlConstants.GetFMCStationList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCStation GetFMCStation(int wID) {
		FMCStation wResult = new FMCStation();
		if (WsdlConstants.GetFMCStationList().containsKey(wID)) {
			if (WsdlConstants.GetFMCStationList().get(wID) != null) {
				wResult = WsdlConstants.GetFMCStationList().get(wID);
			}
		}
		return wResult;
	}

	// endRegion

	// region 工厂全局数据
	private static Calendar RefreshFactoryTime = Calendar.getInstance();

	private static Map<Integer, FMCFactory> FMCFactoryDic = new HashMap<Integer, FMCFactory>();

	public static synchronized Map<Integer, FMCFactory> GetFMCFactoryList() {
		if (FMCFactoryDic == null || FMCFactoryDic.size() <= 0
				|| RefreshFactoryTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCFactory> wFMCFactoryList = FMCServiceImpl.getInstance().FMC_QueryFactoryList(BaseDAO.SysAdmin)
					.List(FMCFactory.class);
			if (wFMCFactoryList != null && wFMCFactoryList.size() > 0) {
				FMCFactoryDic = wFMCFactoryList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshFactoryTime = Calendar.getInstance();
			RefreshFactoryTime.add(Calendar.MINUTE, 3);
		}
		return FMCFactoryDic;
	}

	public static String GetFMCFactoryName(int wID) {
		String wResult = "";
		if (WsdlConstants.GetFMCFactoryList().containsKey(wID)) {
			if (WsdlConstants.GetFMCFactoryList().get(wID) != null) {
				wResult = WsdlConstants.GetFMCFactoryList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCFactory GetFMCFactory(int wID) {
		FMCFactory wResult = new FMCFactory();
		if (WsdlConstants.GetFMCFactoryList().containsKey(wID)) {
			if (WsdlConstants.GetFMCFactoryList().get(wID) != null) {
				wResult = WsdlConstants.GetFMCFactoryList().get(wID);
			}
		}
		return wResult;
	}
	// endRegion

	// region 事业部全局数据
	private static Calendar RefreshBusinessUnitTime = Calendar.getInstance();

	private static Map<Integer, FMCBusinessUnit> FMCBusinessUnitDic = new HashMap<Integer, FMCBusinessUnit>();

	public static synchronized Map<Integer, FMCBusinessUnit> GetFMCBusinessUnitList() {
		if (FMCBusinessUnitDic == null || FMCBusinessUnitDic.size() <= 0
				|| RefreshBusinessUnitTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCBusinessUnit> wFMCBusinessUnitList = FMCServiceImpl.getInstance()
					.FMC_QueryBusinessUnitList(BaseDAO.SysAdmin).List(FMCBusinessUnit.class);
			if (wFMCBusinessUnitList != null && wFMCBusinessUnitList.size() > 0) {
				FMCBusinessUnitDic = wFMCBusinessUnitList.stream()
						.collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshBusinessUnitTime = Calendar.getInstance();
			RefreshBusinessUnitTime.add(Calendar.MINUTE, 3);
		}
		return FMCBusinessUnitDic;
	}

	public static String GetFMCBusinessUnitName(int wID) {
		String wResult = "";
		if (WsdlConstants.GetFMCBusinessUnitList().containsKey(wID)) {
			if (WsdlConstants.GetFMCBusinessUnitList().get(wID) != null) {
				wResult = WsdlConstants.GetFMCBusinessUnitList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCBusinessUnit GetFMCBusinessUnit(int wID) {
		FMCBusinessUnit wResult = new FMCBusinessUnit();
		if (WsdlConstants.GetFMCBusinessUnitList().containsKey(wID)) {
			if (WsdlConstants.GetFMCBusinessUnitList().get(wID) != null) {
				wResult = WsdlConstants.GetFMCBusinessUnitList().get(wID);
			}
		}
		return wResult;
	}
	// endRegion

	// region 客户全局数据
	private static Calendar RefreshCustomerTime = Calendar.getInstance();

	private static Map<Integer, CRMCustomer> CRMCustomerDic = new HashMap<Integer, CRMCustomer>();

	public static synchronized Map<Integer, CRMCustomer> GetCRMCustomerList() {
		if (CRMCustomerDic == null || CRMCustomerDic.size() <= 0
				|| RefreshCustomerTime.compareTo(Calendar.getInstance()) < 0) {
			List<CRMCustomer> wCRMCustomerList = SCMServiceImpl.getInstance()
					.CRM_QueryCustomerList(BaseDAO.SysAdmin, "", 0, 0, 0, 1).List(CRMCustomer.class);
			if (wCRMCustomerList != null && wCRMCustomerList.size() > 0) {
				CRMCustomerDic = wCRMCustomerList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshCustomerTime = Calendar.getInstance();
			RefreshCustomerTime.add(Calendar.MINUTE, 3);
		}
		return CRMCustomerDic;
	}

	public static String GetCRMCustomerName(int wID) {
		String wResult = "";
		if (WsdlConstants.GetCRMCustomerList().containsKey(wID)) {
			if (WsdlConstants.GetCRMCustomerList().get(wID) != null) {
				wResult = WsdlConstants.GetCRMCustomerList().get(wID).getCustomerName();
			}
		}
		return wResult;
	}

	public static CRMCustomer GetCRMCustomer(int wID) {
		CRMCustomer wResult = new CRMCustomer();
		if (WsdlConstants.GetCRMCustomerList().containsKey(wID)) {
			if (WsdlConstants.GetCRMCustomerList().get(wID) != null) {
				wResult = WsdlConstants.GetCRMCustomerList().get(wID);
			}
		}
		return wResult;
	}

	public static CRMCustomer GetCRMCustomer(String wCustomerName) {
		for (CRMCustomer wCRMCustomer : WsdlConstants.GetCRMCustomerList().values()) {
			if (wCRMCustomer.CustomerName.equals(wCustomerName)) {
				return wCRMCustomer;
			}
		}
		return new CRMCustomer();
	}

	public static CRMCustomer GetCRMCustomerByCode(String wCustomerCode) {
		for (CRMCustomer wCRMCustomer : WsdlConstants.GetCRMCustomerList().values()) {
			if (wCRMCustomer.CustomerCode.equals(wCustomerCode)) {
				return wCRMCustomer;
			}
		}
		return new CRMCustomer();
	}
	// endRegion

	/**
	 * 工艺路线全局数据
	 */
	private static Calendar RefreshRouteTime = Calendar.getInstance();

	private static List<FPCRoute> FPCRouteList = new ArrayList<FPCRoute>();

	public static synchronized List<FPCRoute> GetFPCRouteList() {
		if (FPCRouteList == null || FPCRouteList.size() <= 0
				|| RefreshRouteTime.compareTo(Calendar.getInstance()) < 0) {
			List<FPCRoute> wFPCRouteList = FMCServiceImpl.getInstance().FPC_QueryRouteList(BaseDAO.SysAdmin, -1, -1, -1)
					.List(FPCRoute.class);
//			if (wFPCRouteList != null && wFPCRouteList.size() > 0) {
//				wFPCRouteList = wFPCRouteList.stream().filter(p -> p.Active == 1).collect(Collectors.toList());
//			}
			RefreshRouteTime = Calendar.getInstance();
			RefreshRouteTime.add(Calendar.MINUTE, 3);
			FPCRouteList = wFPCRouteList;
		}
		return FPCRouteList;
	}

	public static FPCRoute GetFPCRoute(int wProductID, int wLineID, int wCustomerID) {
		FPCRoute wResult = new FPCRoute();
		try {
			if (WsdlConstants.GetFPCRouteList().stream()
					.anyMatch(p -> p.ProductID == wProductID && p.LineID == wLineID && p.CustomerID == wCustomerID)) {
				return WsdlConstants.GetFPCRouteList().stream()
						.filter(p -> p.ProductID == wProductID && p.LineID == wLineID && p.CustomerID == wCustomerID)
						.findFirst().get();
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 工艺工位全局数据
	 */
	private static Calendar RefreshRoutePartTime = Calendar.getInstance();

	private static List<FPCRoutePart> FPCRoutePartList = new ArrayList<FPCRoutePart>();

	public static synchronized List<FPCRoutePart> GetFPCRoutePartList() {
		if (FPCRoutePartList == null || FPCRoutePartList.size() <= 0
				|| RefreshRoutePartTime.compareTo(Calendar.getInstance()) < 0) {
			List<FPCRoutePart> wFPCRoutePartList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartListByRouteID(BaseDAO.SysAdmin, -1).List(FPCRoutePart.class);
			RefreshRoutePartTime = Calendar.getInstance();
			RefreshRoutePartTime.add(Calendar.MINUTE, 3);
			FPCRoutePartList = wFPCRoutePartList;
		}
		return FPCRoutePartList;
	}

	/**
	 * 工艺工序全局数据
	 */
	private static Calendar RefreshRoutePartPointTime = Calendar.getInstance();

	private static List<FPCRoutePartPoint> FPCRoutePartPointList = new ArrayList<>();

	public static synchronized List<FPCRoutePartPoint> GetFPCRoutePartPointList() {
		if (FPCRoutePartPointList == null || FPCRoutePartPointList.size() <= 0
				|| RefreshRoutePartPointTime.compareTo(Calendar.getInstance()) < 0) {
			List<FPCRoutePartPoint> wFPCRoutePartPointList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartPointListByRouteID(BaseDAO.SysAdmin, -1, -1).List(FPCRoutePartPoint.class);
			RefreshRoutePartPointTime = Calendar.getInstance();
			RefreshRoutePartPointTime.add(Calendar.MINUTE, 3);
			FPCRoutePartPointList = wFPCRoutePartPointList;
		}
		return FPCRoutePartPointList;
	}

	// region 单位全局数据
	private static Calendar RefreshUnitTime = Calendar.getInstance();

	private static Map<Integer, CFGUnit> CFGUnitDic = new HashMap<Integer, CFGUnit>();

	public static synchronized Map<Integer, CFGUnit> GetCFGUnitList() {
		if (CFGUnitDic == null || CFGUnitDic.size() <= 0 || RefreshUnitTime.compareTo(Calendar.getInstance()) < 0) {
			List<CFGUnit> wCFGUnitList = CoreServiceImpl.getInstance().CFG_QueryUnitList(BaseDAO.SysAdmin)
					.List(CFGUnit.class);
			if (wCFGUnitList != null && wCFGUnitList.size() > 0) {
				CFGUnitDic = wCFGUnitList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshUnitTime = Calendar.getInstance();
			RefreshUnitTime.add(Calendar.MINUTE, 3);
		}
		return CFGUnitDic;
	}

	public static CFGUnit GetCFGUnit(int wID) {
		if (WsdlConstants.GetCFGUnitList().containsKey(wID)) {
			if (WsdlConstants.GetCFGUnitList().get(wID) != null) {
				return WsdlConstants.GetCFGUnitList().get(wID);
			}
		}
		return new CFGUnit();
	}

	public static CFGUnit GetCFGUnit(String wUnit) {
		for (CFGUnit wCFGUnit : WsdlConstants.GetCFGUnitList().values()) {
			if (wCFGUnit.Name.equals(wUnit))
				return wCFGUnit;
		}
		return new CFGUnit();
	}

	public static String GetCFGUnitName(int wID) {
		String wResult = "";
		if (WsdlConstants.GetCFGUnitList().containsKey(wID)) {
			if (WsdlConstants.GetCFGUnitList().get(wID) != null) {
				wResult = WsdlConstants.GetCFGUnitList().get(wID).getName();
			}
		}
		return wResult;
	}
	// endRegion

	// 线程执行升版数据源
	public static List<ThreadOP> mThreadOPList = new ArrayList<ThreadOP>();

	// 线程执行保存标准bom
//	public static ThreadBOM mBomList = null;
}
