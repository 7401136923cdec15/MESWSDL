package com.mes.code.server.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.jws.WebParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;
import com.mes.code.server.service.MESService;
import com.mes.code.server.service.po.APIResult;
import com.mes.code.server.service.po.OutResult;
import com.mes.code.server.service.po.erp.MESProduct;
import com.mes.code.server.service.po.erp.MESResult;
import com.mes.code.server.service.po.fpc.FPCPart;
import com.mes.code.server.service.po.fpc.FPCRoute;
import com.mes.code.server.service.po.fpc.FPCRoutePart;
import com.mes.code.server.service.po.fpc.FPCRoutePartPoint;
import com.mes.code.server.service.po.tcm.BOP;
import com.mes.code.server.service.po.tcm.DOC;
import com.mes.code.server.service.po.tcm.FPCCommonFile;
import com.mes.code.server.service.po.tcm.ITEM;
import com.mes.code.server.service.po.tcm.MESResultData;
import com.mes.code.server.service.po.tcm.MESStation;
import com.mes.code.server.service.po.tcm.OP;
import com.mes.code.server.service.po.tcm.PROCESS;
import com.mes.code.server.service.po.tcm.TCMBOM;
import com.mes.code.server.service.po.tcm.ThreadBOM;
import com.mes.code.server.service.po.tcm.ThreadOP;
import com.mes.code.server.service.po.wms.WMSCheckoutInfo;
import com.mes.code.server.service.po.wms.WMSReturn;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.service.utils.XmlTool;
import com.mes.code.server.serviceimpl.dao.BaseDAO;
import com.mes.code.server.serviceimpl.dao.tcm.BOPDAO;
import com.mes.code.server.serviceimpl.dao.tcm.DOCDAO;
import com.mes.code.server.serviceimpl.dao.tcm.FPCCommonFileDAO;
import com.mes.code.server.serviceimpl.dao.tcm.MESStationDAO;
import com.mes.code.server.serviceimpl.dao.tcm.TCMBOMDAO;
import com.mes.code.server.serviceimpl.utils.WsdlConstants;
import com.mes.code.server.shristool.CalendarDAO;

/**
 * TCM???MES?????????????????????
 * 
 * @author PengYouWang
 * @CreateTime 2020-11-5 16:27:19
 * @LastEditTime 2020-11-5 16:27:23
 *
 */
//@WebService(targetNamespace = "http://service.server.code.mes.com", // wsdl????????????
//		serviceName = "MESService", // portType?????? ???????????????????????? ???????????????
//		endpointInterface = "com.mes.code.server.service.MESService") // ????????????webservcie????????????????????????????????????@WebService??????
@Configuration
public class MESServiceImpl implements MESService {
	private static Logger logger = LoggerFactory.getLogger(MESServiceImpl.class);

	public MESServiceImpl() {
	}

	private static MESService Instance;

	public static MESService getInstance() {
		if (Instance == null)
			Instance = new MESServiceImpl();
		return Instance;
	}

	@Override
	public String MES_SaveStationList(@WebParam(name = "DATA") String DATA) {
		String wResult = "";
		List<ITEM> wITEMList = new ArrayList<ITEM>();
		MESResultData wData = new MESResultData();
		try {
			// ???????????????
			CalendarDAO.getInstance().WriteLogFile(DATA, "TCMStation", "", "", "", "", 0, 0);

			List<MESStation> wList = MESStationDAO.getInstance().GetMESStationList(DATA);
			for (MESStation wMESStation : wList) {
				FPCPart wPart = new FPCPart();
				wPart.Active = 1;
				wPart.Code = wMESStation.getMEStationID();
				wPart.CreateTime = Calendar.getInstance();
				wPart.Name = wMESStation.getMEStationName();
				wPart.PartType = 1;
				wPart.Status = 3;
				APIResult wApiResult = FMCServiceImpl.getInstance().FPC_SavePart(BaseDAO.SysAdmin, wPart);
				int wResultCode = wApiResult.getResultCode();
				if (wResultCode != 1000) {
					ITEM wItem = new ITEM();
					wItem.setINFO("?????????????????????????????????");
					wItem.setMATNR(wPart.Code);
					wItem.setRESULT(1);
					wITEMList.add(wItem);
				} else {
					ITEM wItem = new ITEM();
					wItem.setINFO("????????????");
					wItem.setMATNR(wPart.Code);
					wItem.setRESULT(0);
					wITEMList.add(wItem);
				}
			}

			wData.setLIST(wITEMList);
			wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
		} catch (Exception ex) {
			logger.error(ex.toString());

			ITEM wItem = new ITEM();
			wItem.setINFO("???????????????" + ex.toString() + "???");
			wItem.setMATNR(ex.toString());
			wItem.setRESULT(1);
			wITEMList.add(wItem);

			wData.setLIST(wITEMList);
			wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
		}

		return wResult;
	}

	@Override
	public synchronized String MES_SaveBOMList(@WebParam(name = "INPUT") String INPUT) {
		String wResult = "";
		List<ITEM> wITEMList = new ArrayList<ITEM>();
		MESResultData wData = new MESResultData();
		try {
			List<TCMBOM> wList = TCMBOMDAO.getInstance().GetTCMBOMList(INPUT);

			// ???????????????
			String ZCHEX = "";
			String ZXIUC = "";
			String ZJDXX = "";
			String METargetID = "";
			if (wList.size() > 0) {
				ZCHEX = wList.get(0).ZCHEX;
				ZXIUC = wList.get(0).ZXIUC;
				ZJDXX = wList.get(0).ZJDXX;
				METargetID = wList.get(0).METargetID;
			}
			int wLogID = CalendarDAO.getInstance().WriteLogFile(INPUT, "TCMBOM", ZCHEX, ZJDXX, ZXIUC, METargetID, 0, 0);

			if (wList.size() <= 0) {
				ITEM wItem = new ITEM();
				wItem.setINFO("????????????");
				wItem.setMATNR("?????????????????????????????????????????????????????????");
				wItem.setRESULT(1);
				wITEMList.add(wItem);

				wData.setLIST(wITEMList);
				wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
				return wResult;
			}

			for (TCMBOM wTCMBOM : wList) {
				ITEM wItem = new ITEM();
				wItem.setINFO("????????????");
				wItem.setMATNR(wTCMBOM.METargetID);
				wItem.setRESULT(0);
				wITEMList.add(wItem);
			}

			ThreadBOM wThreadBOM = new ThreadBOM();
			wThreadBOM.BOMList = wList;
			wThreadBOM.LogID = wLogID;

			ExecutorService wES = Executors.newFixedThreadPool(1);
			wES.submit(() -> FMCServiceImpl.getInstance().MES_AutoSaveBOM(BaseDAO.SysAdmin, wThreadBOM));
			wES.shutdown();

//			WsdlConstants.mBomList = wThreadBOM;

			wData.setLIST(wITEMList);
			wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
		} catch (Exception ex) {
			logger.error(ex.toString());

			ITEM wItem = new ITEM();
			wItem.setINFO("???????????????" + ex.toString() + "???");
			wItem.setMATNR(ex.toString());
			wItem.setRESULT(1);
			wITEMList.add(wItem);

			wData.setLIST(wITEMList);
			wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
		}

		return wResult;
	}

	@Override
	public String MES_SaveBOPList(@WebParam(name = "INPUT") String INPUT) {
		String wResult = "";
		List<ITEM> wITEMList = new ArrayList<ITEM>();
		MESResultData wData = new MESResultData();
		wData.setLIST(wITEMList);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			BOP wBOP = BOPDAO.getInstance().GetBOP(INPUT);

			int wBOPID = 0;

			// ????????????
			String wMsg = JudgeData(wBOP);
			if (StringUtils.isNotEmpty(wMsg)) {
				ITEM wItem = new ITEM();
				wItem.setINFO(wMsg);
				wItem.setMATNR("???????????????????????????????????????");
				wItem.setRESULT(1);
				wITEMList.add(wItem);

				wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
				return wResult;
			}

			// ?????????????????????
			HandleSubLine(wBOP);

			// ???????????????BOP??????????????????????????????
			FPCRoute wRoute = QueryRoute(wBOP);

			@SuppressWarnings("unused")
			int wNewRouteID = 0;

			boolean wIsStepChange = false;

			if (wRoute.ID > 0) {
				// ??????????????????????????????1?????????????????????????????????????????????????????????????????????
				if (wBOP.getPROCESSLIST().size() > 1) {
					// ?????????????????????????????????
					List<FPCPart> wPartList = BOPDAO.getInstance().IsExistStation(wBOP, wITEMList);
					// ???????????????
					Map<String, Integer> wStepMap = BOPDAO.getInstance().CreateStep(wBOP);
					// ???????????????BOP
					int wRouteID = BOPDAO.getInstance().CreateBOP(wBOP, wITEMList);
					if (wRouteID <= 0) {
						wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
						return wResult;
					}
					wNewRouteID = wRouteID;
					wBOPID = wRouteID;
					// ?????????????????????????????????
					Map<String, Integer> wOrderMap = BOPDAO.getInstance().GetOrderMap(wBOP);
					// ?????????RouteID??????????????????????????????????????????
					Map<Integer, Integer> wPartChangeControlMap = BOPDAO.getInstance()
							.GetPartChangeControlMap(BaseDAO.SysAdmin, wRouteID, wErrorCode);
					// ?????????????????????
					boolean wFlag = BOPDAO.getInstance().CreateRoutePart(wBOP, wITEMList, wRouteID, wOrderMap,
							wPartList, wPartChangeControlMap);
					if (!wFlag) {
						wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
						return wResult;
					}
					// ?????????????????????
					wFlag = BOPDAO.getInstance().CreateRoutePartPoint(wBOP, wITEMList, wRouteID, wStepMap, wPartList);
					if (!wFlag) {
						wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
						return wResult;
					}
					// ???????????????????????????
					BOPDAO.getInstance().CreateLineUnit(wBOP, wStepMap, wPartList);

					// ????????????????????????????????????????????????BOM???????????????BOP
					SetProperty(wRouteID);
				} else if (wBOP.getPROCESSLIST().size() == 1) {
					// ?????????????????????????????????
					List<FPCPart> wPartList = BOPDAO.getInstance().IsExistStation(wBOP, wITEMList);
					// ??????????????????????????????1?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
					// ?????????????????????
					List<FPCRoutePart> wRoutePartList = FMCServiceImpl.getInstance()
							.FPC_QueryRoutePartListByRouteID(BaseDAO.SysAdmin, wRoute.ID).List(FPCRoutePart.class);
					// ?????????????????????
					List<FPCRoutePartPoint> wRoutePartPointList = FMCServiceImpl.getInstance()
							.FPC_QueryRoutePartPointListByRouteID(BaseDAO.SysAdmin, wRoute.ID, -1)
							.List(FPCRoutePartPoint.class);
					// ?????????????????????????????????????????????
					if (wPartList.stream().anyMatch(p -> p.Code.equals(wBOP.getPROCESSLIST().get(0).getUSR00()))) {
						FPCPart wPart = wPartList.stream()
								.filter(p -> p.Code.equals(wBOP.getPROCESSLIST().get(0).getUSR00())).findFirst().get();
						wRoutePartPointList.removeIf(p -> p.PartID == wPart.ID);
					}
					// ???????????????bop
					int wRouteID = BOPDAO.getInstance().CreateBOP(wBOP, wITEMList);
					if (wRouteID <= 0) {
						wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
						return wResult;
					}
					wNewRouteID = wRouteID;
					wBOPID = wRouteID;
					// ?????????????????????
					wRoutePartList.forEach(p -> {
						p.RouteID = wRouteID;
						p.ID = 0;
					});
					for (FPCRoutePart wRoutePart : wRoutePartList) {
						FMCServiceImpl.getInstance().FPC_SaveRoutePart(BaseDAO.SysAdmin, wRoutePart);
					}
					// ?????????????????????
					wRoutePartPointList.forEach(p -> {
						p.RouteID = wRouteID;
						p.ID = 0;
					});
					for (FPCRoutePartPoint wFPCRoutePartPoint : wRoutePartPointList) {
						FMCServiceImpl.getInstance().FPC_SaveRoutePartPoint(BaseDAO.SysAdmin, wFPCRoutePartPoint);
					}
					// ?????????????????????
					Map<String, Integer> wStepMap = BOPDAO.getInstance().CreateStep(wBOP);
					boolean wFlag = BOPDAO.getInstance().CreateRoutePartPoint(wBOP, wITEMList, wRouteID, wStepMap,
							wPartList);
					if (!wFlag) {
						wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
						return wResult;
					}
					// ???????????????????????????
					BOPDAO.getInstance().CreateLineUnit(wBOP, wStepMap, wPartList);

					// ????????????????????????????????????????????????BOM???????????????BOP
					SetProperty(wRouteID);

					// ????????????????????????BOP??????????????????????????????????????????????????????????????????
					if (!INPUT.contains("M00000")) {
						wIsStepChange = true;
					}
				}
			} else {
				// ?????????????????????????????????
				List<FPCPart> wPartList = BOPDAO.getInstance().IsExistStation(wBOP, wITEMList);
				// ???????????????
				Map<String, Integer> wStepMap = BOPDAO.getInstance().CreateStep(wBOP);
				// ???????????????BOP
				int wRouteID = BOPDAO.getInstance().CreateBOP(wBOP, wITEMList);
				if (wRouteID <= 0) {
					wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
					return wResult;
				}
				wBOPID = wRouteID;
				// ?????????????????????????????????
				Map<String, Integer> wOrderMap = BOPDAO.getInstance().GetOrderMap(wBOP);
				// ?????????????????????
				boolean wFlag = BOPDAO.getInstance().CreateRoutePart(wBOP, wITEMList, wRouteID, wOrderMap, wPartList,
						null);
				if (!wFlag) {
					wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
					return wResult;
				}
				// ?????????????????????
				wFlag = BOPDAO.getInstance().CreateRoutePartPoint(wBOP, wITEMList, wRouteID, wStepMap, wPartList);
				if (!wFlag) {
					wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
					return wResult;
				}
				// ???????????????????????????
				BOPDAO.getInstance().CreateLineUnit(wBOP, wStepMap, wPartList);
				// ????????????????????????????????????????????????BOM???????????????BOP
				SetProperty(wRouteID);
			}

			// ????????????
			int wLogID = CalendarDAO.getInstance().WriteLogFile(INPUT, "TCMBOP", wBOP.getZCHEX(), wBOP.getZJDXX(),
					wBOP.getZXIUC(), wBOP.getMATNR(), wBOPID, 0);

			// ????????????????????????BOP??????????????????
			int wClonedBOPID1 = wBOPID;
			ExecutorService wES1 = Executors.newFixedThreadPool(1);
			wES1.submit(() -> BOPDAO.getInstance().UpdateInPlantRoute(BaseDAO.SysAdmin, wClonedBOPID1, wErrorCode));
			wES1.shutdown();

			// ??????????????????????????????
			int wCloneBOPID = wBOPID;
			ExecutorService wES = Executors.newFixedThreadPool(1);
			wES.submit(() -> RelateChangeLog(wLogID, wCloneBOPID));
			wES.shutdown();

			// ????????????????????????
			if (wIsStepChange) {
				StepTechChange(wBOP, wBOPID, wRoute, wLogID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());

			ITEM wItem = new ITEM();
			wItem.setINFO("???????????????" + ex.toString() + "???");
			wItem.setMATNR(ex.toString());
			wItem.setRESULT(1);
			wITEMList.add(wItem);
		}

		wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
		return wResult;
	}

	/**
	 * ???????????????????????????
	 */
	private void StepTechChange(BOP wBOP, int wRouteID, FPCRoute wRoute, int wLogID) {
		try {
			ExecutorService wES = Executors.newFixedThreadPool(1);
			wES.submit(() -> FMCServiceImpl.getInstance().StepTechChange(wBOP, wRouteID, wRoute, wLogID));
			wES.shutdown();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ??????????????????
	 */
	private void RelateChangeLog(int wLogID, int wBOPID) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			BOPDAO.getInstance().UpdateDefaultOrder(BaseDAO.SysAdmin, wLogID, wBOPID, wErrorCode);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ?????????????????????????????????????????????BOM???????????????BOP
	 * 
	 */
	private void SetProperty(int wRouteID) {
		try {
			// ?????????
			FPCRoute wRoute = FMCServiceImpl.getInstance().FPC_QueryRouteByID(BaseDAO.SysAdmin, wRouteID)
					.Info(FPCRoute.class);
			if (wRoute != null && wRoute.ID > 0) {
				List<FPCRoute> wDataList = new ArrayList<FPCRoute>(Arrays.asList(wRoute));
				CoreServiceImpl.getInstance().FPC_ActiveRoute(BaseDAO.SysAdmin, 1, wDataList);
			}
			// ???????????????
			CoreServiceImpl.getInstance().FPC_StandardRoute(BaseDAO.SysAdmin, wRouteID);
			// ???????????????BOM
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			int wMaxBOMID = BOPDAO.getInstance().SelectMaxBoMID(BaseDAO.SysAdmin, wRouteID, wErrorCode);
			if (wMaxBOMID > 0) {
				BOPDAO.getInstance().UpdateRouteID(BaseDAO.SysAdmin, wRouteID, wMaxBOMID, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ?????????????????????????????????
	 */
	private String JudgeData(BOP wBOP) {
		String wResult = "";
		try {
			StringBuffer wSB = new StringBuffer();
			for (PROCESS wPROCESS : wBOP.getPROCESSLIST()) {
				if (!(wPROCESS.getPROCESSNAME().contains("???????????????") || wPROCESS.getPROCESSNAME().contains("??????BOP"))
						&& (wPROCESS.getOPLIST() == null || wPROCESS.getOPLIST().size() <= 0)) {
					wSB.append(StringUtils.Format("???{0}????????????????????????", wPROCESS.getPROCESSNAME()));
				}
			}
			wResult = wSB.toString();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ?????????????????????
	 */
	private void HandleSubLine(BOP wBOP) {
		try {
			if (wBOP.getPROCESSLIST() == null || wBOP.getPROCESSLIST().size() <= 0) {
				return;
			}

			for (PROCESS wPROCESS : wBOP.getPROCESSLIST()) {
				// ????????????????????????????????????
				if (StringUtils.isEmpty(wPROCESS.getPROCESSID_S())) {
					if (wBOP.getPROCESSLIST().stream().anyMatch(p -> p.getPROCESSID().equals(wPROCESS.getFROMBOPID())
							&& p.getPROCESSNAME().contains("???????????????"))) {
						PROCESS wPCess = wBOP.getPROCESSLIST().stream()
								.filter(p -> p.getPROCESSID().equals(wPROCESS.getFROMBOPID())
										&& p.getPROCESSNAME().contains("???????????????"))
								.findFirst().get();
						wPROCESS.setPROCESSID_S(wPCess.getPROCESSID_S());
					}
				}
				// ?????????????????????????????????
				if (StringUtils.isEmpty(wPROCESS.getPROCESSID_P())) {
					if (wBOP.getPROCESSLIST().stream().anyMatch(p -> p.getPROCESSID().equals(wPROCESS.getFROMBOPID())
							&& p.getPROCESSNAME().contains("???????????????"))) {
						PROCESS wPCess = wBOP.getPROCESSLIST().stream()
								.filter(p -> p.getPROCESSID().equals(wPROCESS.getFROMBOPID())
										&& p.getPROCESSNAME().contains("???????????????"))
								.findFirst().get();
						wPROCESS.setPROCESSID_P(wPCess.getPROCESSID_P());
					}
				}

				if (!(StringUtils.isEmpty(wPROCESS.getPROCESSID_P())
						&& StringUtils.isEmpty(wPROCESS.getPROCESSID_S()))) {
					continue;
				}

				if (!wBOP.getPROCESSLIST().stream().anyMatch(p -> p.getPROCESSID().equals(wPROCESS.getFROMBOPID()))) {
					continue;
				}

				PROCESS wProcess = wBOP.getPROCESSLIST().stream()
						.filter(p -> p.getPROCESSID().equals(wPROCESS.getFROMBOPID())).findFirst().get();
				wPROCESS.setPROCESSID_P(wProcess.getPROCESSID_P());
				wPROCESS.setPROCESSID_S(wProcess.getPROCESSID_S());
			}

			wBOP.getPROCESSLIST().removeIf(p -> StringUtils.isEmpty(p.getUSR00()));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ??????????????????
	 */
	private FPCRoute QueryRoute(BOP wBOP) {
		FPCRoute wResult = new FPCRoute();
		try {
			int wLineID = BOPDAO.getInstance().FPC_GetLineID(wBOP.getZXIUC());
			if (wLineID <= 0) {
				return wResult;
			}
			int wProductID = WsdlConstants.GetFPCProduct(wBOP.getZCHEX()).ID;
			if (wProductID <= 0) {
				return wResult;
			}
			int wCustomerID = WsdlConstants.GetCRMCustomerByCode(wBOP.getZJDXX()).ID;
			if (wCustomerID <= 0) {
				return wResult;
			}

			List<FPCRoute> wRouteList = FMCServiceImpl.getInstance().FPC_QueryRouteList(BaseDAO.SysAdmin, -1, -1, -1)
					.List(FPCRoute.class);
			if (wRouteList == null || wRouteList.size() <= 0) {
				return wResult;
			}

			wRouteList = wRouteList.stream()
					.filter(p -> p.LineID == wLineID && p.ProductID == wProductID && p.CustomerID == wCustomerID)
					.collect(Collectors.toList());

			if (wRouteList == null || wRouteList.size() <= 0) {
				return wResult;
			}

			wResult = wRouteList.stream().max(Comparator.comparing(FPCRoute::getCreateTime)).get();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public String MES_SaveProductList(@WebParam(name = "INPUT") String INPUT) {
		String wResult = "";
		try {
			List<MESProduct> wList = JSON.parseArray(INPUT, MESProduct.class);

			if (wList == null || wList.size() <= 0) {
				MESResult wMESResult = new MESResult();
				wMESResult.setMSG("????????????");
				wMESResult.setSTATUS("E");
				wResult = JSON.toJSONString(wMESResult);
				logger.info(INPUT);
				return wResult;
			} else {
				MESResult wMESResult = new MESResult();
				wMESResult.setMSG("????????????");
				wMESResult.setSTATUS("S");
				wResult = JSON.toJSONString(wMESResult);
				logger.info(JSON.toJSONString(wList));
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			MESResult wMESResult = new MESResult();
			wMESResult.setMSG("????????????");
			wMESResult.setSTATUS("E");
			wResult = JSON.toJSONString(wMESResult);
			logger.info(INPUT);
		}
		return wResult;
	}

	@Override
	public String MES_SaveDOCList(@WebParam(name = "INPUT") String INPUT) {
		String wResult = "";
		List<ITEM> wITEMList = new ArrayList<ITEM>();
		MESResultData wData = new MESResultData();
		try {
			// ???????????????
			CalendarDAO.getInstance().WriteLogFile(INPUT, "TCMDOC", "", "", "", "", 0, 0);

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<DOC> wList = DOCDAO.getInstance().GetDOCList(INPUT);

			for (DOC wDoc : wList) {
				FPCCommonFile wFPCCommonFile = new FPCCommonFile();
				wFPCCommonFile.ID = 0;
				wFPCCommonFile.Code = wDoc.getDOCID();
				wFPCCommonFile.DocRev = wDoc.getDOCREV();
				wFPCCommonFile.FileName = wDoc.getDOCNAME();
				wFPCCommonFile.FilePath = StringUtils.Join(",", wDoc.getDOCDATELIST());
				List<FPCCommonFile> wOList = FPCCommonFileDAO.getInstance().SelectList(BaseDAO.SysAdmin, -1,
						wDoc.getDOCID(), null, null, wErrorCode);
				if (wOList.size() > 0) {
					wFPCCommonFile.ID = wOList.get(0).ID;
				}
				int wID = FPCCommonFileDAO.getInstance().Update(BaseDAO.SysAdmin, wFPCCommonFile, wErrorCode);
				if (wID > 0) {
					ITEM wItem = new ITEM();
					wItem.setINFO("????????????");
					wItem.setMATNR(wDoc.getDOCID());
					wItem.setRESULT(0);
					wITEMList.add(wItem);
				} else {
					ITEM wItem = new ITEM();
					wItem.setINFO("??????????????????????????????????????????");
					wItem.setMATNR(wDoc.getDOCID());
					wItem.setRESULT(1);
					wITEMList.add(wItem);
				}
			}

			wData.setLIST(wITEMList);
		} catch (Exception ex) {
			logger.error(ex.toString());

			ITEM wItem = new ITEM();
			wItem.setINFO("???????????????" + ex.toString() + "???");
			wItem.setMATNR(ex.toString());
			wItem.setRESULT(1);
			wITEMList.add(wItem);

			wData.setLIST(wITEMList);
		}

		wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
		return wResult;
	}

	@Override
	public String MES_Test(@WebParam(name = "INPUT") String INPUT) {
		String wResult = INPUT;
		try {

		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public String MES_SaveMEOPList(String INPUT) {
		String wResult = "";
		List<ITEM> wITEMList = new ArrayList<ITEM>();
		MESResultData wData = new MESResultData();
		try {
			// ???????????????
			int wLogID = CalendarDAO.getInstance().WriteLogFile(INPUT, "TCMOP", "", "", "", "", 0, 0);

			List<OP> wOPList = BOPDAO.getInstance().GetOPList(INPUT);

			if (wOPList.size() <= 0) {
				ITEM wItem = new ITEM();
				wItem.setINFO("????????????");
				wItem.setMATNR("?????????????????????????????????????????????????????????");
				wItem.setRESULT(1);
				wITEMList.add(wItem);

				wData.setLIST(wITEMList);
				wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
				return wResult;
			}

			for (OP wOP : wOPList) {
				if (wOP.getOPITEMLIST().stream().anyMatch(p -> (p.getZBHOH().equals("0") || p.getZBHOH().equals(""))
						&& (p.getZZZWW().equals("0") || p.getZZZWW().equals("")))) {
					ITEM wItem = new ITEM();
					wItem.setINFO("??????????????????????????????????????????????????????????????????0???");
					wItem.setMATNR(wOP.getOPID());
					wItem.setRESULT(1);
					wITEMList.add(wItem);
				} else {
					ITEM wItem = new ITEM();
					wItem.setINFO("????????????");
					wItem.setMATNR(wOP.getOPID());
					wItem.setRESULT(0);
					wITEMList.add(wItem);
				}
			}

			if (wITEMList.stream().anyMatch(p -> p.getRESULT() == 1)) {
				wData.setLIST(wITEMList);
				wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
				return wResult;
			}

			// ????????????????????????
			ThreadOP wThreadOP = new ThreadOP();
			wThreadOP.OPList = wOPList;
			wThreadOP.LogID = wLogID;

			ExecutorService wES = Executors.newFixedThreadPool(1);
			wES.submit(() -> BOPDAO.getInstance().AddVersion(wThreadOP));
			wES.shutdown();

//			WsdlConstants.mThreadOPList.add(wThreadOP);

			wData.setLIST(wITEMList);
		} catch (Exception ex) {
			logger.error(ex.toString());

			ITEM wItem = new ITEM();
			wItem.setINFO("???????????????" + ex.toString() + "???");
			wItem.setMATNR(ex.toString());
			wItem.setRESULT(1);
			wITEMList.add(wItem);

			wData.setLIST(wITEMList);
		}

		wResult = XmlTool.ToXmlStr(wData, MESResultData.class);
		return wResult;
	}

	@Override
	public String MES_UpdateMaterialCheckoutInfo(@WebParam(name = "DATA") String DATA) {
		String wResult = "";
		try {
			logger.info("MES_UpdateMaterialCheckoutInfo:" + DATA);

			WMSCheckoutInfo wINfo = JSON.parseObject(DATA, WMSCheckoutInfo.class);
			System.out.println(wINfo.ItemList.size());

			WMSReturn wWMSReturn = new WMSReturn();
			wWMSReturn.EDISENDFLAG1 = "true";
			wWMSReturn.EDISENDMSG1 = "??????";
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String wCurrentTime = wSDF.format(Calendar.getInstance().getTime());
			wWMSReturn.EDISENDTIME1 = wCurrentTime;

			wResult = JSON.toJSONString(wWMSReturn);
		} catch (Exception ex) {

			WMSReturn wWMSReturn = new WMSReturn();
			wWMSReturn.EDISENDFLAG1 = "false";
			wWMSReturn.EDISENDMSG1 = "?????????" + ex.toString();
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String wCurrentTime = wSDF.format(Calendar.getInstance().getTime());
			wWMSReturn.EDISENDTIME1 = wCurrentTime;

			wResult = JSON.toJSONString(wWMSReturn);

			logger.error(ex.toString());
		}
		return wResult;
	}
}
