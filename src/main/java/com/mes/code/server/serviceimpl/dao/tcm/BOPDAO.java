package com.mes.code.server.serviceimpl.dao.tcm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.code.server.service.mesenum.BFCMessageType;
import com.mes.code.server.service.mesenum.BPMEventModule;
import com.mes.code.server.service.mesenum.MESDBSource;
import com.mes.code.server.service.mesenum.TCMChangeType;
import com.mes.code.server.service.po.OutResult;
import com.mes.code.server.service.po.ServiceResult;
import com.mes.code.server.service.po.bfc.BFCMessage;
import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.service.po.bms.BMSRoleItem;
import com.mes.code.server.service.po.fmc.FMCLineUnit;
import com.mes.code.server.service.po.fpc.FPCPart;
import com.mes.code.server.service.po.fpc.FPCPartPoint;
import com.mes.code.server.service.po.fpc.FPCRoute;
import com.mes.code.server.service.po.fpc.FPCRoutePart;
import com.mes.code.server.service.po.fpc.FPCRoutePartPoint;
import com.mes.code.server.service.po.fpc.FPCStepSOP;
import com.mes.code.server.service.po.mcs.MCSLogInfo;
import com.mes.code.server.service.po.mss.MSSBOM;
import com.mes.code.server.service.po.mss.MSSBOMItem;
import com.mes.code.server.service.po.mss.MSSMaterial;
import com.mes.code.server.service.po.oms.OMSOrder;
import com.mes.code.server.service.po.tcm.BOP;
import com.mes.code.server.service.po.tcm.FPCCommonFile;
import com.mes.code.server.service.po.tcm.ITEM;
import com.mes.code.server.service.po.tcm.OP;
import com.mes.code.server.service.po.tcm.OPDOC;
import com.mes.code.server.service.po.tcm.OPITEM;
import com.mes.code.server.service.po.tcm.PROCESS;
import com.mes.code.server.service.po.tcm.PROCESSDOC;
import com.mes.code.server.service.po.tcm.TCMChangeInfo;
import com.mes.code.server.service.po.tcm.TCMMaterialChangeItems;
import com.mes.code.server.service.po.tcm.TCMMaterialChangeLog;
import com.mes.code.server.service.po.tcm.ThreadOP;
import com.mes.code.server.service.utils.CloneTool;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.CoreServiceImpl;
import com.mes.code.server.serviceimpl.FMCServiceImpl;
import com.mes.code.server.serviceimpl.dao.BaseDAO;
import com.mes.code.server.serviceimpl.dao.mcs.MCSLogInfoDAO;
import com.mes.code.server.serviceimpl.utils.WsdlConstants;
import com.mes.code.server.utils.Configuration;

public class BOPDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(BOPDAO.class);

	private static BOPDAO Instance = null;

	private BOPDAO() {
		super();
	}

	public static BOPDAO getInstance() {
		if (Instance == null)
			Instance = new BOPDAO();
		return Instance;
	}

	/**
	 * 将xml字符串解析成BOP数据
	 */
	@SuppressWarnings("unchecked")
	public BOP GetBOP(String wXmlStr) {
		BOP wResult = new BOP();
		try {
			Document wDocument = DocumentHelper.parseText(wXmlStr);

			Node wBOPNode = wDocument.selectSingleNode("//BOP");
			// ①解析BOP元素
			wResult.setBOPID(wBOPNode.selectSingleNode("BOPID").getText());
			wResult.setZXIUC(wBOPNode.selectSingleNode("ZXIUC").getText());
			wResult.setZCHEX(wBOPNode.selectSingleNode("ZCHEX").getText());
			wResult.setZJDXX(wBOPNode.selectSingleNode("ZJDXX").getText());
			wResult.setMATNR(wBOPNode.selectSingleNode("MATNR").getText());
			// ②解析PROCESSLIST元素
			List<PROCESS> wPROCESSList = new ArrayList<PROCESS>();
			wResult.setPROCESSLIST(wPROCESSList);
			Node wPROCESSLISTNode = wBOPNode.selectSingleNode("PROCESSLIST");
			List<Node> wPROCESSNodes = wPROCESSLISTNode.selectNodes("PROCESS");
			for (Node wPROCESSNode : wPROCESSNodes) {
				if (StringUtils.isEmpty(wPROCESSNode.selectSingleNode("USR00").getText())) {

					PROCESS wPROCESS = new PROCESS();
					wPROCESS.setPROCESSID(wPROCESSNode.selectSingleNode("PROCESSID").getText());
					wPROCESS.setPROCESSNAME(wPROCESSNode.selectSingleNode("PROCESSNAME").getText());
					wPROCESS.setFROMBOPID(wPROCESSNode.selectSingleNode("FROMBOPID").getText());
					wPROCESS.setUSR00(wPROCESSNode.selectSingleNode("USR00").getText());
					wPROCESS.setPROCESSID_P(wPROCESSNode.selectSingleNode("PROCESSID_P").getText());
					wPROCESS.setPROCESSID_S(wPROCESSNode.selectSingleNode("PROCESSID_S").getText());

					// ①解析新字段
					wPROCESS.setPROCESSVERSION(GetProcessNodeValue(wPROCESSNode, "PROCESSVERSION"));
					wPROCESS.setPROCESSCNO(GetProcessNodeValue(wPROCESSNode, "PROCESSCNO"));
					wPROCESS.setPROCESSC(GetProcessNodeValue(wPROCESSNode, "PROCESSC"));
					wPROCESS.setPROCESSCUSER(GetProcessNodeValue(wPROCESSNode, "PROCESSCUSER"));
					wPROCESS.setPROCESSCTYPE(GetProcessNodeValue(wPROCESSNode, "PROCESSCTYPE"));

					wPROCESSList.add(wPROCESS);

					continue;
				}

				PROCESS wPROCESS = new PROCESS();
				wPROCESS.setPROCESSID(wPROCESSNode.selectSingleNode("PROCESSID").getText());
				wPROCESS.setPROCESSNAME(wPROCESSNode.selectSingleNode("PROCESSNAME").getText());
				wPROCESS.setFROMBOPID(wPROCESSNode.selectSingleNode("FROMBOPID").getText());
				wPROCESS.setUSR00(wPROCESSNode.selectSingleNode("USR00").getText());
				wPROCESS.setPROCESSID_P(wPROCESSNode.selectSingleNode("PROCESSID_P").getText());
				wPROCESS.setPROCESSID_S(wPROCESSNode.selectSingleNode("PROCESSID_S").getText());

				// ①解析新字段
				wPROCESS.setPROCESSVERSION(GetProcessNodeValue(wPROCESSNode, "PROCESSVERSION"));
				wPROCESS.setPROCESSCNO(GetProcessNodeValue(wPROCESSNode, "PROCESSCNO"));
				wPROCESS.setPROCESSC(GetProcessNodeValue(wPROCESSNode, "PROCESSC"));
				wPROCESS.setPROCESSCUSER(GetProcessNodeValue(wPROCESSNode, "PROCESSCUSER"));
				wPROCESS.setPROCESSCTYPE(GetProcessNodeValue(wPROCESSNode, "PROCESSCTYPE"));

				wPROCESSList.add(wPROCESS);
				// ③解析PROCESSDOCList
				List<PROCESSDOC> wPROCESSDOCList = new ArrayList<PROCESSDOC>();
				wPROCESS.setPROCESSDOCList(wPROCESSDOCList);
				Node wPROCESSDOCListNode = wPROCESSNode.selectSingleNode("PROCESSDOCLIST");
				List<Node> wPROCESSDOCNodes = wPROCESSDOCListNode.selectNodes("PROCESSDOC");
				for (Node wPROCESSDOCNode : wPROCESSDOCNodes) {
					PROCESSDOC wPROCESSDOC = new PROCESSDOC();
					wPROCESSDOC.setPROCESSDOCSIGN(wPROCESSDOCNode.selectSingleNode("PROCESSDOCSIGN").getText());
					wPROCESSDOC.setPROCESSDOCID(wPROCESSDOCNode.selectSingleNode("PROCESSDOCID").getText());
					wPROCESSDOC.setPROCESSDOCDATE(wPROCESSDOCNode.selectSingleNode("PROCESSDOCDATE").getText());
					wPROCESSDOCList.add(wPROCESSDOC);
				}
				// ④解析OPLIST
				List<OP> wOPList = new ArrayList<OP>();
				wPROCESS.setOPLIST(wOPList);
				Node wOPLISTNode = wPROCESSNode.selectSingleNode("OPLIST");
				if (wOPLISTNode == null) {
					continue;
				}
				List<Node> wOPNodes = wOPLISTNode.selectNodes("OP");
				for (Node wOPNode : wOPNodes) {
					// ③解析OP元素
					OP wOP = new OP();
					wOP.setOPID(wOPNode.selectSingleNode("OPID").getText());
					wOP.setOPNAME(wOPNode.selectSingleNode("OPNAME").getText());
					wOP.setFROMPROCESSID(wOPNode.selectSingleNode("FROMPROCESSID").getText());
					wOP.setOPNO(wOPNode.selectSingleNode("OPNO").getText());
					wOP.setOPID_P(wOPNode.selectSingleNode("OPID_P").getText());
					wOP.setOPID_S(wOPNode.selectSingleNode("OPID_S").getText());
					wOP.setOPTIME(wOPNode.selectSingleNode("OPTIME").getText());

					// ①解析新字段
					wOP.setOPVERSION(GetProcessNodeValue(wOPNode, "OPVERSION"));
					wOP.setPROCESSCNO(GetProcessNodeValue(wOPNode, "PROCESSCNO"));
					wOP.setPROCESSC(GetProcessNodeValue(wOPNode, "PROCESSC"));
					wOP.setPROCESSCUSER(GetProcessNodeValue(wOPNode, "PROCESSCUSER"));
					wOP.setPROCESSCTYPE(GetProcessNodeValue(wOPNode, "PROCESSCTYPE"));

					wOPList.add(wOP);
					// ④解析OPDOCLIST元素
					List<OPDOC> wOPDOCList = new ArrayList<OPDOC>();
					wOP.setOPDOCLIST(wOPDOCList);
					Node wOPDOCLISTNode = wOPNode.selectSingleNode("OPDOCLIST");
					List<Node> wOPDOCNodes = wOPDOCLISTNode.selectNodes("OPDOC");
					for (Node wOPDOCNode : wOPDOCNodes) {
						OPDOC wOPDOC = new OPDOC();
						wOPDOC.setOPDOCSIGN(wOPDOCNode.selectSingleNode("OPDOCSIGN").getText());
						wOPDOC.setOPDOCID(wOPDOCNode.selectSingleNode("OPDOCID").getText());
						wOPDOC.setOPDOCDATE(wOPDOCNode.selectSingleNode("OPDOCDATE").getText());
						wOPDOCList.add(wOPDOC);
					}
					// ⑤解析OPITEMLIST元素
					List<OPITEM> wOPITEMList = new ArrayList<OPITEM>();
					wOP.setOPITEMLIST(wOPITEMList);
					Node wOPITEMLISTNode = wOPNode.selectSingleNode("OPITEMLIST");
					if (wOPITEMLISTNode != null) {
						List<Node> wOPITEMNodes = wOPITEMLISTNode.selectNodes("OPITEM");
						for (Node wOPITEMNode : wOPITEMNodes) {
							OPITEM wOPITEM = new OPITEM();
							wOPITEM.setKTTXT(wOPITEMNode.selectSingleNode("KTTXT").getText());
							wOPITEM.setKTTXTY(wOPITEMNode.selectSingleNode("KTTXTY").getText());
							wOPITEM.setFROMOPID(wOPITEMNode.selectSingleNode("FROMOPID").getText());
							wOPITEM.setMATNR(wOPITEMNode.selectSingleNode("MATNR").getText());
							wOPITEM.setMEINS(wOPITEMNode.selectSingleNode("MEINS").getText());
							wOPITEM.setZZZBZ(wOPITEMNode.selectSingleNode("ZZZBZ").getText());
							wOPITEMList.add(wOPITEM);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工艺集制定节点的值
	 */
	private String GetProcessNodeValue(Node wPROCESSNode, String wNodeName) {
		String wResult = "";
		try {
			Node wTempNode = wPROCESSNode.selectSingleNode(wNodeName);
			if (wTempNode != null) {
				wResult = wTempNode.getText();
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 判断工位数据是否存在
	 */
	public List<FPCPart> IsExistStation(BOP wBOP, List<ITEM> wITEMList) {
		List<FPCPart> wResult = new ArrayList<FPCPart>();
		try {
			List<FPCPart> wPartList = FMCServiceImpl.getInstance().FPC_QueryPartList(BaseDAO.SysAdmin, 0, 0, 0, -1)
					.List(FPCPart.class);
			for (PROCESS wPROCESS : wBOP.getPROCESSLIST()) {
				if (!wPartList.stream().anyMatch(p -> p.Code.equals(wPROCESS.getUSR00()))) {
					FPCPart wPart = new FPCPart();

					wPart.Active = 1;
					wPart.Code = wPROCESS.getUSR00();
					wPart.CreateTime = Calendar.getInstance();
					wPart.Name = wPROCESS.getPROCESSNAME();
					wPart.Name.replace("工位工艺集", "");
					wPart.PartType = 1;
					wPart.Status = 3;
					FPCPart wNewPart = FMCServiceImpl.getInstance().FPC_SavePart(BaseDAO.SysAdmin, wPart)
							.Info(FPCPart.class);
					if (wNewPart.ID > 0) {
						wPart.ID = wNewPart.ID;
						wPartList.add(wNewPart);
					}
				}
			}
			wResult = wPartList;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 创建工序
	 */
	public Map<String, Integer> CreateStep(BOP wBOP) {
		Map<String, Integer> wResult = new HashMap<String, Integer>();
		try {
			List<FPCPartPoint> wStepList = FMCServiceImpl.getInstance()
					.FPC_QueryPartPointList(BaseDAO.SysAdmin, 0, 0, 0).List(FPCPartPoint.class);

			for (PROCESS wPROCESS : wBOP.getPROCESSLIST()) {
				for (OP wOP : wPROCESS.getOPLIST()) {
					// 名称存在，编码变了
					if (wStepList.stream().anyMatch(p -> p.Name.toLowerCase().equals(wOP.getOPNAME().toLowerCase()))) {

						FPCPartPoint wNameStep = wStepList.stream()
								.filter(p -> p.Name.toLowerCase().equals(wOP.getOPNAME().toLowerCase())).findFirst()
								.get();

						if (wNameStep.Code.replace("PS-", "").equals(wOP.getOPID())) {
						} else {
							FPCPartPoint wStep = wStepList.stream()
									.filter(p -> p.Name.toLowerCase().equals(wOP.getOPNAME().toLowerCase())).findFirst()
									.get();
							wStep.Name = wOP.getOPNAME();
							wStep.Code = "PS-" + wOP.getOPID();
							FMCServiceImpl.getInstance().FPC_SavePartPoint(BaseDAO.SysAdmin, wStep);
						}
					}
					// 编码不变，名称变了
					else if (wStepList.stream().anyMatch(p -> p.Code.replace("PS-", "").equals(wOP.getOPID()))) {

						FPCPartPoint wOPIDStep = wStepList.stream()
								.filter(p -> p.Code.replace("PS-", "").equals(wOP.getOPID())).findFirst().get();

						if (wOPIDStep.Name.equals(wOP.getOPNAME())) {
						} else {
							FPCPartPoint wStep = wStepList.stream()
									.filter(p -> p.Code.replace("PS-", "").equals(wOP.getOPID())).findFirst().get();
							wStep.Name = wOP.getOPNAME();
							FMCServiceImpl.getInstance().FPC_SavePartPoint(BaseDAO.SysAdmin, wStep);
						}
					} else {
						if (!wStepList.stream().anyMatch(p -> p.Code.replace("PS-", "").equals(wOP.getOPID()))) {
							FPCPartPoint wFPCPartPoint = new FPCPartPoint();
							wFPCPartPoint.ID = 0;
							wFPCPartPoint.Name = wOP.getOPNAME();
							wFPCPartPoint.Code = wOP.getOPID();
							wFPCPartPoint.CreateTime = Calendar.getInstance();
							wFPCPartPoint.EditTime = Calendar.getInstance();
							wFPCPartPoint.AuditTime = Calendar.getInstance();
							wFPCPartPoint.Active = 1;
							wFPCPartPoint.Status = 3;
							wFPCPartPoint.FactoryID = 1;
							wFPCPartPoint.StepType = 1;
							wFPCPartPoint = FMCServiceImpl.getInstance()
									.FPC_SavePartPoint(BaseDAO.SysAdmin, wFPCPartPoint).Info(FPCPartPoint.class);
							if (wFPCPartPoint != null && wFPCPartPoint.ID > 0) {
								wResult.put(wOP.getOPNAME(), wFPCPartPoint.ID);
							}
						}
					}
				}
			}

			for (FPCPartPoint wFPCPartPoint : wStepList) {
				wResult.put(wFPCPartPoint.Name, wFPCPartPoint.ID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 创建工艺BOP
	 */
	public int CreateBOP(BOP wBOP, List<ITEM> wITEMList) {
		int wResult = 0;
		try {
			FPCRoute wFPCRoute = new FPCRoute();

			wFPCRoute.ID = 0;
			wFPCRoute.VersionNo = wBOP.getMATNR();
			wFPCRoute.BusinessUnitID = 1;
			wFPCRoute.ProductTypeID = 0;
			wFPCRoute.Status = 1;
			wFPCRoute.Active = 1;
			wFPCRoute.CreateTime = Calendar.getInstance();
			wFPCRoute.EditTime = Calendar.getInstance();
			wFPCRoute.AuditTime = Calendar.getInstance();
			wFPCRoute.Description = wBOP.getMATNR();
			wFPCRoute.FactoryID = 1;
			wFPCRoute.LineID = FPC_GetLineID(wBOP.getZXIUC());
			wFPCRoute.Name = wBOP.getBOPID();
			wFPCRoute.ProductID = WsdlConstants.GetFPCProduct(wBOP.getZCHEX()).ID;
			wFPCRoute.CustomerID = WsdlConstants.GetCRMCustomerByCode(wBOP.getZJDXX()).ID;
			wFPCRoute.IsStandard = 0;

			wFPCRoute = FMCServiceImpl.getInstance().FPC_SaveRoute(BaseDAO.SysAdmin, wFPCRoute).Info(FPCRoute.class);
			if (wFPCRoute.ID > 0) {
				wResult = wFPCRoute.ID;

				// ①保存变更信息
				SaveChangeInfo(wBOP, wResult);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());

			ITEM wItem = new ITEM();
			wItem.setINFO("处理失败【创建工艺路线失败】");
			wItem.setMATNR(ex.toString());
			wItem.setRESULT(1);
			wITEMList.add(wItem);
		}
		return wResult;
	}

	/**
	 * 保存变更信息
	 */
	private void SaveChangeInfo(BOP wBOP, int wRouteID) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			for (PROCESS wPROCESS : wBOP.getPROCESSLIST()) {
				if (StringUtils.isEmpty(wPROCESS.getPROCESSCNO())) {
					continue;
				}

				TCMChangeInfo wInfo = new TCMChangeInfo();
				wInfo.ID = 0;
				wInfo.PROCESSC = wPROCESS.getPROCESSC();
				wInfo.PROCESSCNO = wPROCESS.getPROCESSCNO();
				wInfo.PROCESSCTYPE = wPROCESS.getPROCESSCTYPE();
				wInfo.PROCESSCUSER = wPROCESS.getPROCESSCUSER();
				wInfo.RouteID = wRouteID;

				TCMChangeInfoDAO.getInstance().Update(BaseDAO.SysAdmin, wInfo, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取修程ID
	 */
	public int FPC_GetLineID(String wLineName) {
		int wResult = 0;
		try {
			if (WsdlConstants.GetFMCLineList().values().stream().anyMatch(p -> p.Name.contains(wLineName))) {
				wResult = WsdlConstants.GetFMCLineList().values().stream().filter(p -> p.Name.contains(wLineName))
						.findFirst().get().ID;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 创建工艺工位
	 * 
	 * @param wPartChangeControlMap
	 */
	public boolean CreateRoutePart(BOP wBOP, List<ITEM> wITEMList, int wRouteID, Map<String, Integer> wOrderMap,
			List<FPCPart> wPartList, Map<Integer, Integer> wPartChangeControlMap) {
		boolean wResult = true;
		try {
			for (PROCESS wPROCESS : wBOP.getPROCESSLIST()) {
				FPCRoutePart wFPCRoutePart = new FPCRoutePart();

				wFPCRoutePart.ID = 0;
				wFPCRoutePart.RouteID = wRouteID;
				wFPCRoutePart.RouteName = wPROCESS.getPROCESSNAME();
				wFPCRoutePart.Name = wPROCESS.getPROCESSNAME();
				wFPCRoutePart.Code = wPROCESS.getPROCESSID();
				wFPCRoutePart.PartID = wPartList.stream().anyMatch(p -> p.Code.equals(wPROCESS.getUSR00()))
						? wPartList.stream().filter(p -> p.Code.equals(wPROCESS.getUSR00())).findFirst().get().ID
						: 0;
				wFPCRoutePart.CreateTime = Calendar.getInstance();
				wFPCRoutePart.OrderID = wOrderMap.get(wPROCESS.getPROCESSID());
				wFPCRoutePart.PrevPartID = GetPrevPartID(wBOP, wPROCESS);
				wFPCRoutePart.NextPartIDMap = GetNextPartID(wBOP, wPROCESS);
				wFPCRoutePart.Version = wPROCESS.getPROCESSVERSION();
				wFPCRoutePart.ChangeControl = 1;

				// 继承转序控制
				if (wPartChangeControlMap != null && wPartChangeControlMap.containsKey(wFPCRoutePart.PartID)) {
					wFPCRoutePart.ChangeControl = wPartChangeControlMap.get(wFPCRoutePart.PartID);
				}

				wFPCRoutePart = FMCServiceImpl.getInstance().FPC_SaveRoutePart(BaseDAO.SysAdmin, wFPCRoutePart)
						.Info(FPCRoutePart.class);
				if (wFPCRoutePart.ID <= 0) {
					wResult = false;

					ITEM wItem = new ITEM();
					wItem.setINFO(
							"处理失败【创建工艺集失败，请检查工位编码。】" + "工位编码：" + wPROCESS.getUSR00() + "工位ID：" + wFPCRoutePart.PartID);
					wItem.setMATNR(wPROCESS.getPROCESSID());
					wItem.setRESULT(1);
					wITEMList.add(wItem);
				} else {
					ITEM wItem = new ITEM();
					wItem.setINFO("处理成功");
					wItem.setMATNR(wPROCESS.getPROCESSID());
					wItem.setRESULT(0);
					wITEMList.add(wItem);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取后工艺集
	 */
	private Map<String, String> GetNextPartID(BOP wBOP, PROCESS wPROCESS) {
		Map<String, String> wResult = new HashMap<String, String>();
		try {
			if (StringUtils.isNotEmpty(wPROCESS.getPROCESSID_S())) {
				String[] wStrs = wPROCESS.getPROCESSID_S().split(";");
				for (String wStr : wStrs) {
					if (wBOP.getPROCESSLIST().stream().anyMatch(p -> p.getPROCESSID().equals(wStr))) {
						PROCESS wProcess = wBOP.getPROCESSLIST().stream().filter(p -> p.getPROCESSID().equals(wStr))
								.findFirst().get();
						wResult.put(String.valueOf(WsdlConstants.GetFPCPartByCode(wProcess.getUSR00()).ID), "0");
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取前工艺集
	 */
	private int GetPrevPartID(BOP wBOP, PROCESS wPROCESS) {
		int wResult = 0;
		try {
			if (StringUtils.isNotEmpty(wPROCESS.getPROCESSID_P())) {
				String[] wStrs = wPROCESS.getPROCESSID_P().split(",");
				if (wBOP.getPROCESSLIST().stream().anyMatch(p -> p.getPROCESSID().equals(wStrs[0]))) {
					PROCESS wProcess = wBOP.getPROCESSLIST().stream().filter(p -> p.getPROCESSID().equals(wStrs[0]))
							.findFirst().get();
					wResult = WsdlConstants.GetFPCPartByCode(wProcess.getUSR00()).ID;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 创建工艺工序
	 */
	public boolean CreateRoutePartPoint(BOP wBOP, List<ITEM> wITEMList, int wRouteID, Map<String, Integer> wStepMap,
			List<FPCPart> wPartList) {
		boolean wResult = true;
		try {
			// 工艺文件
			List<FPCCommonFile> wFileList = new ArrayList<FPCCommonFile>();

			for (PROCESS wPROCESS : wBOP.getPROCESSLIST()) {
				Map<String, Integer> wStepOrderMap = GetStepOrdrMap(wPROCESS);
				for (OP wOP : wPROCESS.getOPLIST()) {
					FPCRoutePartPoint wFPCRoutePartPoint = new FPCRoutePartPoint();
					wFPCRoutePartPoint.ID = 0;
					wFPCRoutePartPoint.RouteID = wRouteID;
					wFPCRoutePartPoint.Code = wOP.getOPID();
					wFPCRoutePartPoint.PartID = wPartList.stream().anyMatch(p -> p.Code.equals(wPROCESS.getUSR00()))
							? wPartList.stream().filter(p -> p.Code.equals(wPROCESS.getUSR00())).findFirst().get().ID
							: 0;
					wFPCRoutePartPoint.PartPointID = wStepMap.containsKey(wOP.getOPNAME())
							? wStepMap.get(wOP.getOPNAME())
							: 0;
					wFPCRoutePartPoint.CreateTime = Calendar.getInstance();
					wFPCRoutePartPoint.OrderID = wStepOrderMap.get(wOP.getOPID());
					wFPCRoutePartPoint.PrevStepID = GetPrevStepID(wPROCESS.getOPLIST(), wOP, wStepMap);
					wFPCRoutePartPoint.NextStepIDMap = GetNextStepID(wPROCESS.getOPLIST(), wOP, wStepMap);
					wFPCRoutePartPoint.StandardPeriod = StringUtils.parseDouble(wOP.getOPTIME());
					wFPCRoutePartPoint.ActualPeriod = wFPCRoutePartPoint.StandardPeriod;
					wFPCRoutePartPoint.MaterialID = 0;
					wFPCRoutePartPoint.DefaultOrder = "";
					wFPCRoutePartPoint.Version = wOP.getOPVERSION();
					wFPCRoutePartPoint = FMCServiceImpl.getInstance()
							.FPC_SaveRoutePartPoint(BaseDAO.SysAdmin, wFPCRoutePartPoint).Info(FPCRoutePartPoint.class);
					if (wFPCRoutePartPoint == null || wFPCRoutePartPoint.ID <= 0) {
						wResult = false;

						ITEM wItem = new ITEM();
						wItem.setINFO("处理失败【创建工艺工序失败，请检查工序名称和编号。】" + wOP.getOPNAME() + wOP.getOPID());
						wItem.setMATNR(wOP.getOPID());
						wItem.setRESULT(1);
						wITEMList.add(wItem);
					} else {
						ITEM wItem = new ITEM();
						wItem.setINFO("处理成功");
						wItem.setMATNR(wOP.getOPID());
						wItem.setRESULT(0);
						wITEMList.add(wItem);

						// ①导入工艺卡
						int wRoutePartPointID = wFPCRoutePartPoint.ID;
						SaveRouteStepSOP(wOP, wRoutePartPointID, wFileList);
					}

				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());

			ITEM wItem = new ITEM();
			wItem.setINFO("处理失败【" + ex.toString() + "】");
			wItem.setMATNR(ex.toString());
			wItem.setRESULT(1);
			wITEMList.add(wItem);
		}
		return wResult;
	}

	/**
	 * 导入工艺卡
	 */
	private void SaveRouteStepSOP(OP wOP, int wRoutePartPointID, List<FPCCommonFile> wFileList) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			for (OPDOC wOPDOC : wOP.getOPDOCLIST()) {
				// 1.图片 2.视频 3.PDF 4.Word
				FPCStepSOP wFPCStepSOP = new FPCStepSOP();
				wFPCStepSOP.ID = 0;
				wFPCStepSOP.RoutePartPointID = wRoutePartPointID;
				wFPCStepSOP.FileType = wOPDOC.getOPDOCDATE().contains(".doc") ? 4
						: wOPDOC.getOPDOCDATE().contains(".pdf") ? 3 : 2;
				wFPCStepSOP.SourceType = 1;
				wFPCStepSOP.Active = 0;
				wFPCStepSOP.FilePath = wOPDOC.getOPDOCDATE();
				wFPCStepSOP.FileName = wOPDOC.getOPDOCID();
				FPCCommonFileDAO.getInstance().UpdateSOP(BaseDAO.SysAdmin, wFPCStepSOP, wErrorCode);

//				if (wOPDOC.getOPDOCSIGN().equals("0")) {
//					FPCStepSOP wFPCStepSOP = new FPCStepSOP();
//					wFPCStepSOP.ID = 0;
//					wFPCStepSOP.RoutePartPointID = wRoutePartPointID;
//					wFPCStepSOP.FileType = 3;
//					wFPCStepSOP.SourceType = 1;
//					wFPCStepSOP.Active = 0;
//					wFPCStepSOP.FilePath = wOPDOC.getOPDOCDATE();
//					wFPCStepSOP.FileName = "PDF文件";
//					CoreServiceImpl.getInstance().FPC_UpdateStepSOP(BaseDAO.SysAdmin, wFPCStepSOP);
//				} else if (wOPDOC.getOPDOCSIGN().equals("1")) {
//					if (wFileList != null && wFileList.size() > 0
//							&& wFileList.stream().anyMatch(p -> p.Code.equals(wOPDOC.getOPDOCID()))) {
//						FPCCommonFile wFPCCommonFile = wFileList.stream()
//								.filter(p -> p.Code.equals(wOPDOC.getOPDOCID())).findFirst().get();
//
//						if (StringUtils.isNotEmpty(wFPCCommonFile.FilePath)) {
//							String[] wStrs = wFPCCommonFile.FilePath.split(",");
//							for (String wStr : wStrs) {
//								FPCStepSOP wFPCStepSOP = new FPCStepSOP();
//
//								wFPCStepSOP.ID = 0;
//								wFPCStepSOP.RoutePartPointID = wRoutePartPointID;
//								wFPCStepSOP.FileType = 3;
//								wFPCStepSOP.SourceType = 1;
//								wFPCStepSOP.Active = 0;
//								wFPCStepSOP.FilePath = wStr;
//								wFPCStepSOP.FileName = "PDF文件";
//
//								CoreServiceImpl.getInstance().FPC_UpdateStepSOP(BaseDAO.SysAdmin, wFPCStepSOP);
//							}
//						}
//					}
//				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取后工序集
	 */
	private Map<String, String> GetNextStepID(List<OP> wOPList, OP wOP, Map<String, Integer> wStepMap) {
		Map<String, String> wResult = new HashMap<String, String>();
		try {
			if (StringUtils.isNotEmpty(wOP.getOPID_S())) {
				String[] wStrs = wOP.getOPID_S().split(",");
				for (String wStr : wStrs) {
					if (wOPList.stream().anyMatch(p -> p.getOPID().equals(wStr))) {
						OP wOp = wOPList.stream().filter(p -> p.getOPID().equals(wStr)).findFirst().get();
						wResult.put(String.valueOf(wStepMap.get(wOp.getOPNAME())), "0");
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取前工序
	 */
	private int GetPrevStepID(List<OP> wOPList, OP wOP, Map<String, Integer> wStepMap) {
		int wResult = 0;
		try {
			if (StringUtils.isNotEmpty(wOP.getOPID_P())) {
				String[] wStrs = wOP.getOPID_P().split(",");
				if (wOPList.stream().anyMatch(p -> p.getOPID().equals(wStrs[0]))) {
					OP wOp = wOPList.stream().filter(p -> p.getOPID().equals(wStrs[0])).findFirst().get();
					wResult = wStepMap.get(wOp.getOPNAME());
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工序集顺序映射
	 */
	private Map<String, Integer> GetStepOrdrMap(PROCESS wPROCESS) {
		Map<String, Integer> wResult = new HashMap<String, Integer>();
		try {
			List<String> wNOList = wPROCESS.getOPLIST().stream().map(p -> p.getOPID()).distinct()
					.collect(Collectors.toList());
			if (wNOList == null || wNOList.size() <= 0) {
				return wResult;
			}

			int wMaxLevel = 0;
			for (String wNo : wNOList) {
				if (wResult.containsKey(wNo)) {
					continue;
				}

				wMaxLevel = this.GetMaxLevelStep(wNo, wPROCESS.getOPLIST(), wResult);
				wResult.put(wNo, wMaxLevel);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工序集编号的最大层数
	 * 
	 * @param wNo   工艺集编号
	 * @param wList Excel数据源
	 * @return 最大层数
	 */
	private int GetMaxLevelStep(String wNo, List<OP> wList, Map<String, Integer> wMap) {
		int wResult = 1;
		try {
			// ①获取此工序集的前工序集编号
			String wPreNo = wList.stream().filter(p -> p.getOPID().equals(wNo)).findFirst().get().getOPID_P();
			if (wPreNo.contains(";")) {
				wPreNo = "";
			}
			if (StringUtils.isNotEmpty(wPreNo)) {
				String[] wNOs = wPreNo.split(",");
				int wTempLevel = 0;
				for (String wItem : wNOs) {
					if (wMap.containsKey(wItem)) {
						wTempLevel = wMap.get(wItem);
					} else {
						wTempLevel = GetMaxLevelStep(wItem, wList, wMap);
					}
					if (!wMap.containsKey(wItem)) {
						wMap.put(wItem, wTempLevel);
					}
					if (wResult < wTempLevel + 1) {
						wResult = wTempLevel + 1;
					}
				}
			}
			// ②获取其他工序集的后工序集包含此工序集的工序集
			List<OP> wPreNoList = wList.stream()
					.filter(p -> p.getOPID_S() != null && p.getOPID_S().contains(wNo) && !p.getOPID().equals(wNo))
					.collect(Collectors.toList());

			if (wPreNoList != null && wPreNoList.size() > 0) {
				int wTempLevel = 0;
				for (OP wFPCRouteImport : wPreNoList) {
					if (wMap.containsKey(wFPCRouteImport.getOPID())) {
						wTempLevel = wMap.get(wFPCRouteImport.getOPID());
					} else {
						wTempLevel = GetMaxLevelStep(wFPCRouteImport.getOPID(), wList, wMap);
					}
					if (!wMap.containsKey(wFPCRouteImport.getOPID())) {
						wMap.put(wFPCRouteImport.getOPID(), wTempLevel);
					}
					if (wResult < wTempLevel + 1) {
						wResult = wTempLevel + 1;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 创建产线单元明细
	 */
	public void CreateLineUnit(BOP wBOP, Map<String, Integer> wStepMap, List<FPCPart> wPartList) {
		try {
			List<FMCLineUnit> wLineUnitList = FMCServiceImpl.getInstance()
					.FMC_QueryLineUnitListByLineID(BaseDAO.SysAdmin, -1, -1, -1, false).List(FMCLineUnit.class);

			int wLineID = FPC_GetLineID(wBOP.getZXIUC());
			int wProductID = WsdlConstants.GetFPCProduct(wBOP.getZCHEX()).ID;

			List<FMCLineUnit> wTempUnitList = null;
			for (PROCESS wFPCRouteImport : wBOP.getPROCESSLIST()) {
				wTempUnitList = wLineUnitList.stream().filter(p -> p.LineID == wLineID && p.ProductID == wProductID)
						.collect(Collectors.toList());

				int wPartID = wPartList.stream().anyMatch(p -> p.Code.equals(wFPCRouteImport.getUSR00()))
						? wPartList.stream().filter(p -> p.Code.equals(wFPCRouteImport.getUSR00())).findFirst().get().ID
						: 0;
				// 工位级
				if (wTempUnitList == null || wTempUnitList.size() <= 0
						|| !wTempUnitList.stream().anyMatch(p -> p.UnitID == wPartID && p.LevelID == 2)) {
					int wMaxOrderID = 1;
					List<FMCLineUnit> wUnitList1 = wTempUnitList.stream().filter(p -> p.LevelID == 2)
							.collect(Collectors.toList());
					if (wUnitList1 != null && wUnitList1.size() > 0) {
						wMaxOrderID = wUnitList1.stream().max(Comparator.comparing(FMCLineUnit::getOrderID))
								.get().OrderID + 1;
					}

					FMCLineUnit wFMCLineUnit = new FMCLineUnit();
					wFMCLineUnit.Active = 1;
					wFMCLineUnit.LineID = wLineID;
					wFMCLineUnit.UnitID = wPartID;
					wFMCLineUnit.OrderID = wMaxOrderID;
					wFMCLineUnit.LevelID = 2;
					wFMCLineUnit.ProductID = wProductID;
					wFMCLineUnit.CreateTime = Calendar.getInstance();
					wFMCLineUnit.Status = 1;
					wFMCLineUnit.ParentUnitID = wLineID;
					FMCServiceImpl.getInstance().FMC_SaveLineUnit(BaseDAO.SysAdmin, wFMCLineUnit);
					wLineUnitList.add(wFMCLineUnit);
				}

				// 找到该工位下所有工序
				for (OP wStepImport : wFPCRouteImport.getOPLIST()) {
					int wStepID = wStepMap.get(wStepImport.getOPNAME());
					if (!wTempUnitList.stream()
							.anyMatch(p -> p.LevelID == 3 && p.ParentUnitID == wPartID && p.UnitID == wStepID)) {
						int wMaxOrderID = 1;

						List<FMCLineUnit> wUnitList1 = wLineUnitList.stream().filter(p -> p.LineID == wLineID
								&& p.ProductID == wProductID && p.LevelID == 3 && p.ParentUnitID == wPartID)
								.collect(Collectors.toList());

//						List<FMCLineUnit> wUnitList1 = wTempUnitList.stream().filter(p -> p.LevelID == 3)
//								.collect(Collectors.toList());
						if (wUnitList1 != null && wUnitList1.size() > 0) {
							wMaxOrderID = wUnitList1.stream().max(Comparator.comparing(FMCLineUnit::getOrderID))
									.get().OrderID + 1;
						}
						FMCLineUnit wFMCLineUnit = new FMCLineUnit();
						wFMCLineUnit.Active = 1;
						wFMCLineUnit.LineID = wLineID;
						wFMCLineUnit.UnitID = wStepID;
						wFMCLineUnit.OrderID = wMaxOrderID;
						wFMCLineUnit.LevelID = 3;
						wFMCLineUnit.CreateTime = Calendar.getInstance();
						wFMCLineUnit.Status = 1;
						wFMCLineUnit.ProductID = wProductID;
						wFMCLineUnit.ParentUnitID = wPartID;
						FMCServiceImpl.getInstance().FMC_SaveLineUnit(BaseDAO.SysAdmin, wFMCLineUnit);
						wLineUnitList.add(wFMCLineUnit);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取工艺工位层级映射
	 */
	public Map<String, Integer> GetOrderMap(BOP wBOP) {
		Map<String, Integer> wResult = new HashMap<String, Integer>();
		try {
			List<String> wNOList = wBOP.getPROCESSLIST().stream().map(p -> p.getPROCESSID()).distinct()
					.collect(Collectors.toList());
			if (wNOList == null || wNOList.size() <= 0) {
				return wResult;
			}

			int wMaxLevel = 0;
			for (String wNo : wNOList) {
				if (wResult.containsKey(wNo)) {
					continue;
				}

				wMaxLevel = this.GetMaxLevel(wNo, wBOP.getPROCESSLIST(), wResult);
				wResult.put(wNo, wMaxLevel);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工艺集编号的最大层数
	 * 
	 * @param wNo   工艺集编号
	 * @param wList Excel数据源
	 * @return 最大层数
	 */
	private int GetMaxLevel(String wNo, List<PROCESS> wList, Map<String, Integer> wMap) {
		int wResult = 1;
		try {
			// ①获取此工序集的前工序集编号
			if (wList.stream().anyMatch(p -> p.getPROCESSID().equals(wNo))) {
				String wPreNo = wList.stream().filter(p -> p.getPROCESSID().equals(wNo)).findFirst().get()
						.getPROCESSID_P();
				if (StringUtils.isNotEmpty(wPreNo)) {
					String[] wNOs = wPreNo.split(",");
					int wTempLevel = 0;
					for (String wItem : wNOs) {
						if (wMap.containsKey(wItem)) {
							wTempLevel = wMap.get(wItem);
						} else {
							wTempLevel = GetMaxLevel(wItem, wList, wMap);
						}
						if (!wMap.containsKey(wItem)) {
							wMap.put(wItem, wTempLevel);
						}
						if (wResult < wTempLevel + 1) {
							wResult = wTempLevel + 1;
						}
					}
				}
			}
			// ②获取其他工序集的后工序集包含此工序集的工序集
			List<PROCESS> wPreNoList = wList.stream().filter(p -> p.getPROCESSID_S() != null
					&& p.getPROCESSID_S().contains(wNo) && !p.getPROCESSID().equals(wNo)).collect(Collectors.toList());

			if (wPreNoList != null && wPreNoList.size() > 0) {
				int wTempLevel = 0;
				for (PROCESS wFPCRouteImport : wPreNoList) {
					if (wMap.containsKey(wFPCRouteImport.getPROCESSID())) {
						wTempLevel = wMap.get(wFPCRouteImport.getPROCESSID());
					} else {
						wTempLevel = GetMaxLevel(wFPCRouteImport.getPROCESSID(), wList, wMap);
					}
					if (!wMap.containsKey(wFPCRouteImport.getPROCESSID())) {
						wMap.put(wFPCRouteImport.getPROCESSID(), wTempLevel);
					}
					if (wResult < wTempLevel + 1) {
						wResult = wTempLevel + 1;
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 将xml字符串解析成工序列表
	 */
	@SuppressWarnings("unchecked")
	public List<OP> GetOPList(String wXmlStr) {
		List<OP> wResult = new ArrayList<OP>();
		try {
			Document wDocument = DocumentHelper.parseText(wXmlStr);
			// ④解析OPLIST
			Node wOPLISTNode = wDocument.selectSingleNode("//OPLIST");
			List<Node> wOPNodes = wOPLISTNode.selectNodes("OP");
			for (Node wOPNode : wOPNodes) {
				// ③解析OP元素
				OP wOP = new OP();
				wOP.setOPID(wOPNode.selectSingleNode("OPID").getText());
				wOP.setOPNAME(wOPNode.selectSingleNode("OPNAME").getText());
				wOP.setOPNO(wOPNode.selectSingleNode("OPNO").getText());
				wOP.setOPTIME(wOPNode.selectSingleNode("OPTIME").getText());

				// ①解析新字段
				wOP.setOPVERSION(GetProcessNodeValue(wOPNode, "OPVERSION"));
				wOP.setPROCESSCNO(GetProcessNodeValue(wOPNode, "PROCESSCNO"));
				wOP.setPROCESSC(GetProcessNodeValue(wOPNode, "PROCESSC"));
				wOP.setPROCESSCUSER(GetProcessNodeValue(wOPNode, "PROCESSCUSER"));
				wOP.setPROCESSCTYPE(GetProcessNodeValue(wOPNode, "PROCESSCTYPE"));

				wResult.add(wOP);
				// ④解析OPDOCLIST元素
				List<OPDOC> wOPDOCList = new ArrayList<OPDOC>();
				wOP.setOPDOCLIST(wOPDOCList);
				Node wOPDOCLISTNode = wOPNode.selectSingleNode("OPDOCLIST");
				List<Node> wOPDOCNodes = wOPDOCLISTNode.selectNodes("OPDOC");
				for (Node wOPDOCNode : wOPDOCNodes) {
					OPDOC wOPDOC = new OPDOC();
					wOPDOC.setOPDOCSIGN(wOPDOCNode.selectSingleNode("OPDOCSIGN").getText());
					wOPDOC.setOPDOCID(wOPDOCNode.selectSingleNode("OPDOCID").getText());
					wOPDOC.setOPDOCDATE(wOPDOCNode.selectSingleNode("OPDOCDATE").getText());
					wOPDOCList.add(wOPDOC);
				}
				// ⑤解析OPITEMLIST元素
				List<OPITEM> wOPITEMList = new ArrayList<OPITEM>();
				wOP.setOPITEMLIST(wOPITEMList);
				Node wOPITEMLISTNode = wOPNode.selectSingleNode("OPITEMLIST");
				if (wOPITEMLISTNode != null) {
					List<Node> wOPITEMNodes = wOPITEMLISTNode.selectNodes("OPITEM");
					for (Node wOPITEMNode : wOPITEMNodes) {
						OPITEM wOPITEM = new OPITEM();
						wOPITEM.setKTTXT(wOPITEMNode.selectSingleNode("KTTXT").getText());
						wOPITEM.setKTTXTY(wOPITEMNode.selectSingleNode("KTTXTY").getText());
						wOPITEM.setFROMOPID(wOPITEMNode.selectSingleNode("FROMOPID").getText());
						wOPITEM.setMATNR(wOPITEMNode.selectSingleNode("MATNR").getText());
						wOPITEM.setMEINS(wOPITEMNode.selectSingleNode("MEINS").getText());
						wOPITEM.setZZZBZ(wOPITEMNode.selectSingleNode("ZZZBZ").getText());
						// 以下为物料BOM所需的几个属性
						wOPITEM.setMENGE(wOPITEMNode.selectSingleNode("MENGE").getText());
						wOPITEM.setZBHOH(wOPITEMNode.selectSingleNode("ZBHOH").getText());
						wOPITEM.setZZZWW(wOPITEMNode.selectSingleNode("ZZZWW").getText());
						wOPITEM.setZYCYZ(wOPITEMNode.selectSingleNode("ZYCYZ").getText());
						wOPITEM.setZCJXC(wOPITEMNode.selectSingleNode("ZCJXC").getText());

						wOPITEMList.add(wOPITEM);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 工序变更升级版本(工艺bop、标注bom)
	 */
	public void AddVersion(OP wOP) {
		try {
			// ①查询所有工序列表
			List<FPCPartPoint> wStepList = WsdlConstants.GetFPCStepList().values().stream().filter(p -> p.Active == 1)
					.collect(Collectors.toList());
			if (wStepList == null || wStepList.size() <= 0) {
				return;
			}
			// ②根据工序ID查找MES系统工序ID
			if (!wStepList.stream().anyMatch(p -> p.Code.replace("PS-", "").equals(wOP.getOPID()))) {
				return;
			}
			FPCPartPoint wStep = wStepList.stream().filter(p -> p.Code.replace("PS-", "").equals(wOP.getOPID()))
					.findFirst().get();
			if (wStep == null || wStep.ID <= 0) {
				return;
			}
			// ③查询所有用到此工序ID的激活的工艺BOP
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<Integer> wRouteIDList = FPC_QueryRouteIDListByStepID(BaseDAO.SysAdmin, wStep.ID, wErrorCode);
			if (wRouteIDList.size() > 0) {
				for (Integer wRouteID : wRouteIDList) {
					// ④工艺bop升版
					ProcessBOPUpgrade(wRouteID, wOP, wStep.ID);
				}
			}
			// ⑤查询所有用到此工序的激活的标准BOM
			List<Integer> wBOMIDList = MSS_QueryBOMIDListByStepID(BaseDAO.SysAdmin, wStep.ID, wErrorCode);
			if (wBOMIDList.size() > 0) {
				for (Integer wBOMID : wBOMIDList) {
					// ⑥标准BOM升版
					StandardBOMUpgrade(wBOMID, wOP, wStep.ID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 标准BOM升版
	 */
	private void StandardBOMUpgrade(Integer wBOMID, OP wOP, int wStepID) {
		try {
			// ①根据标准bomID获取标准bom
			MSSBOM wBOM = FMCServiceImpl.getInstance().MSS_QueryBOM(BaseDAO.SysAdmin, wBOMID, "").Info(MSSBOM.class);
			if (wBOM == null || wBOM.ID <= 0) {
				return;
			}
			// ②根据标准bomID获取标准bomitem
			List<MSSBOMItem> wItemList = FMCServiceImpl.getInstance().MSS_QueryBOMItemByID(BaseDAO.SysAdmin, wBOM.ID)
					.List(MSSBOMItem.class);
			if (wItemList == null || wItemList.size() <= 0) {
				return;
			}
			// ③保存标准bom得到新的bomID
			wBOM.ID = 0;
			MSSBOM wNewBom = FMCServiceImpl.getInstance().MSS_SaveBOM(BaseDAO.SysAdmin, wBOM).Custom("list",
					MSSBOM.class);
			if (wNewBom == null || wNewBom.ID <= 0) {
				return;
			}

			// ④移除指定工序的标准bomitem
			wItemList.removeIf(p -> p.PartPointID == wStepID);
			// ⑤遍历保存标准bomitem
			for (MSSBOMItem wMSSBOMItem : wItemList) {
				wMSSBOMItem.BOMID = wNewBom.ID;
				wMSSBOMItem.ID = 0;
				FMCServiceImpl.getInstance().MSS_SaveBOMItem(BaseDAO.SysAdmin, wMSSBOMItem);
			}
			// ③获取工位ID、局段
			int wPlaceID = 0;
			String wProductQD = "";
			int wBOMType = 0;
			if (wItemList.stream().anyMatch(p -> p.PartPointID == wStepID)) {
				wPlaceID = wItemList.stream().filter(p -> p.PartPointID == wStepID).findFirst().get().PlaceID;
				wProductQD = wItemList.stream().filter(p -> p.PartPointID == wStepID).findFirst().get().ProductQD;
				wBOMType = wItemList.stream().filter(p -> p.PartPointID == wStepID).findFirst().get().BOMType;
			}
			// ⑥根据op创建标准bomitem
			for (OPITEM wOPITEM : wOP.getOPITEMLIST()) {
				if (!wOPITEM.getKTTXTY().equals("1")) {
					continue;
				}

				MSSMaterial wMaterial = FMCServiceImpl.getInstance()
						.MSS_QueryMaterialByID(BaseDAO.SysAdmin, -1, wOPITEM.getMATNR()).Info(MSSMaterial.class);
				if (wMaterial == null || wMaterial.ID <= 0) {
					continue;
				}

				// 新增BOM子项
				MSSBOMItem wMSSBOMItem = new MSSBOMItem();

				wMSSBOMItem.Active = 0;
				wMSSBOMItem.Auditor = BaseDAO.SysAdmin.Name;
				wMSSBOMItem.AuditTime = Calendar.getInstance();
				wMSSBOMItem.Author = BaseDAO.SysAdmin.Name;
				wMSSBOMItem.BOMID = wNewBom.ID;
				wMSSBOMItem.BOMType = wBOMType;
				wMSSBOMItem.ProductQD = wProductQD;
				wMSSBOMItem.DeviceNo = "";
				wMSSBOMItem.PlaceID = wPlaceID;
				wMSSBOMItem.PartPointID = wStepID;
				wMSSBOMItem.MaterialNo = wOPITEM.getMATNR();
				if (wMaterial != null && wMaterial.ID > 0) {
					wMSSBOMItem.MaterialID = wMaterial.ID;
					wMSSBOMItem.MaterialName = wMaterial.MaterialName;
				}
				wMSSBOMItem.MaterialNumber = StringUtils.parseDouble(wOPITEM.getMEINS());
				wMSSBOMItem.UnitID = WsdlConstants.GetCFGUnit(wOPITEM.getMENGE()).ID;
				wMSSBOMItem.ReplaceType = StringUtils.parseInt(wOPITEM.getZBHOH());
				wMSSBOMItem.ReplaceRatio = 0.0f;
				wMSSBOMItem.OutsourceType = StringUtils.parseInt(wOPITEM.getZZZWW());
				wMSSBOMItem.OriginalType = StringUtils.parseBoolean(wOPITEM.getZYCYZ()) ? 1 : 0;
				wMSSBOMItem.DisassyType = StringUtils.parseBoolean(wOPITEM.getZCJXC()) ? 1 : 0;
				wMSSBOMItem.Remark = "";
				wMSSBOMItem.ParentID = 0;

				// ⑦保存标准bomitem
				FMCServiceImpl.getInstance().MSS_SaveBOMItem(BaseDAO.SysAdmin, wMSSBOMItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 标准BOM升版
	 */
	private void StandardBOMUpgrade(Integer wBOMID, List<OP> wOPList, List<FPCPartPoint> wStepList,
			Map<Integer, OP> wOPMap, int wChangeLogID, List<String> wBOMNO1List, List<String> wBOMNO2List,
			List<Integer> wChangeLogIDList, ThreadOP wThreadOP) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ①根据标准bomID获取标准bom
			MSSBOM wBOM = FMCServiceImpl.getInstance().MSS_QueryBOM(BaseDAO.SysAdmin, wBOMID, "").Info(MSSBOM.class);
			if (wBOM == null || wBOM.ID <= 0) {
				return;
			}
			String wBOMNo1 = wBOM.BOMNo;
			// ②根据标准bomID获取标准bomitem
			List<MSSBOMItem> wItemList = FMCServiceImpl.getInstance().MSS_QueryBOMItemByID(BaseDAO.SysAdmin, wBOM.ID)
					.List(MSSBOMItem.class);
			if (wItemList == null || wItemList.size() <= 0) {
				return;
			}
			// ③保存标准bom得到新的bomID
			wBOM.ID = 0;
			wBOM.IsStandard = 1;
			wBOM.Status = 1;
			if (wBOM.BOMNo.contains(".")) {
				wBOM.BOMNo = wBOM.BOMNo.substring(0, wBOM.BOMNo.indexOf('.'));
			}
			MSSBOM wNewBom = FMCServiceImpl.getInstance().MSS_SaveBOM(BaseDAO.SysAdmin, wBOM).Custom("list",
					MSSBOM.class);
			if (wNewBom == null || wNewBom.ID <= 0) {
				return;
			}

			String wBOMNo2 = wNewBom.BOMNo;

			// 保存标准bom子项
			List<MSSBOMItem> wNewItemList = new ArrayList<MSSBOMItem>();
			SetMSSBOMItemList(wBOMID, wStepList, wOPMap, wErrorCode, wItemList, wNewBom, wNewItemList, wThreadOP);
			if (wNewItemList.size() > 0) {
				FMCServiceImpl.getInstance().MSS_SaveBOMItemList(BaseDAO.SysAdmin, wNewItemList);
			}

			// 保存变更物料
			wBOMNO1List.add(wBOMNo1);
			wBOMNO2List.add(wBOMNo2);
			SaveChangeItems(wStepList, wChangeLogID, wItemList, wNewItemList, wBOMNo1, wBOMNo2, wOPList,
					wChangeLogIDList, wThreadOP);

			// ④移除指定工序的标准bomitem
			wItemList.removeIf(p -> wStepList.stream().anyMatch(q -> q.ID == p.PartPointID));
			// ⑤遍历保存标准bomitem
			wItemList.forEach(p -> {
				p.BOMID = wNewBom.ID;
				p.ID = 0;
			});
			FMCServiceImpl.getInstance().MSS_SaveBOMItemList(BaseDAO.SysAdmin, wItemList);
			// 启用标准bom
			wNewBom.Status = 3;
			FMCServiceImpl.getInstance().MSS_SaveBOM(BaseDAO.SysAdmin, wNewBom);
			// 设置为当前bom
			FMCServiceImpl.getInstance().MSS_BOMStandard(BaseDAO.SysAdmin, wNewBom.ID);
			// 更新日志的BOMID
			UpdateBOMIDs(wThreadOP.LogID, wNewBom.ID);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 更新日志的BOMID(多个)
	 */
	private void UpdateBOMIDs(int logID, int iD) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			MCSLogInfo wLog = MCSLogInfoDAO.getInstance().SelectByID(BaseDAO.SysAdmin, logID, wErrorCode);
			if (wLog.ID > 0) {
				if (StringUtils.isEmpty(wLog.BOMID)) {
					wLog.BOMID = iD + "";
				} else {
					wLog.BOMID = wLog.BOMID + "," + iD;
				}
				MCSLogInfoDAO.getInstance().Update(BaseDAO.SysAdmin, wLog, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 保存标准bom子项
	 */
	private void SetMSSBOMItemList(Integer wBOMID, List<FPCPartPoint> wStepList, Map<Integer, OP> wOPMap,
			OutResult<Integer> wErrorCode, List<MSSBOMItem> wItemList, MSSBOM wNewBom, List<MSSBOMItem> wNewItemList,
			ThreadOP wThreadOP) {
		try {
			for (FPCPartPoint wStep : wStepList) {
				// ③获取工位ID、局段
				int wPlaceID = 0;
				String wProductQD = "";
				int wBOMType = 0;
				if (wItemList.stream().anyMatch(p -> p.PartPointID == wStep.ID)) {
					wPlaceID = wItemList.stream().filter(p -> p.PartPointID == wStep.ID).findFirst().get().PlaceID;
					wProductQD = wItemList.stream().filter(p -> p.PartPointID == wStep.ID).findFirst().get().ProductQD;
					wBOMType = wItemList.stream().filter(p -> p.PartPointID == wStep.ID).findFirst().get().BOMType;
				} else {
					wPlaceID = GetPlaceID(BaseDAO.SysAdmin, wStep.ID, wBOMID, wErrorCode);
					wProductQD = WsdlConstants.GetCRMCustomer(wNewBom.CustomerID).CustomerCode;
					wBOMType = 2;
				}
				if (wPlaceID <= 0) {
					continue;
				}
				OP wOP = wOPMap.get(wStep.ID);
				// ⑥根据op创建标准bomitem
				for (OPITEM wOPITEM : wOP.getOPITEMLIST()) {
					if (!wOPITEM.getKTTXTY().equals("1")) {
						continue;
					}

					// 保存物料
					MSSMaterial wMaterial = SaveMaterial(wOPITEM);
					if (wMaterial.ID <= 0) {
						continue;
					}

					// 新增BOM子项
					MSSBOMItem wMSSBOMItem = new MSSBOMItem();
					// 子项赋值
					SetValue(wNewBom, wStep.ID, wPlaceID, wProductQD, wBOMType, wOPITEM, wMaterial, wMSSBOMItem,
							wThreadOP);
					// 添加子项
					wNewItemList.add(wMSSBOMItem);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 保存物料
	 */
	private MSSMaterial SaveMaterial(OPITEM wOPITEM) {
		MSSMaterial wMaterial = new MSSMaterial();
		try {
			wMaterial = FMCServiceImpl.getInstance().MSS_QueryMaterialByID(BaseDAO.SysAdmin, -1, wOPITEM.getMATNR())
					.Info(MSSMaterial.class);

			if (wMaterial == null || wMaterial.ID <= 0) {

				wMaterial = new MSSMaterial();
				wMaterial.ID = 0;
				wMaterial.MaterialNo = wOPITEM.getMATNR();
				wMaterial.MaterialName = wOPITEM.getKTTXT();
				wMaterial.Status = 1;
				wMaterial.StockID = 1;

				wMaterial.ID = FMCServiceImpl.getInstance().MSS_SaveMaterial(BaseDAO.SysAdmin, wMaterial)
						.Info(Integer.class);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wMaterial;
	}

	/**
	 * 子项赋值
	 */
	private void SetValue(MSSBOM wNewBom, Integer wStepID, int wPlaceID, String wProductQD, int wBOMType,
			OPITEM wOPITEM, MSSMaterial wMaterial, MSSBOMItem wMSSBOMItem, ThreadOP wThreadOP) {
		try {
			wMSSBOMItem.Active = 0;
			wMSSBOMItem.Auditor = BaseDAO.SysAdmin.Name;
			wMSSBOMItem.AuditTime = Calendar.getInstance();
			wMSSBOMItem.Author = BaseDAO.SysAdmin.Name;
			wMSSBOMItem.BOMID = wNewBom.ID;
			wMSSBOMItem.BOMType = wBOMType;
			wMSSBOMItem.ProductQD = wProductQD;
			wMSSBOMItem.DeviceNo = "";
			wMSSBOMItem.PlaceID = wPlaceID;
			wMSSBOMItem.PartPointID = wStepID;
			wMSSBOMItem.MaterialNo = wOPITEM.getMATNR();
			if (wMaterial != null && wMaterial.ID > 0) {
				wMSSBOMItem.MaterialID = wMaterial.ID;
				wMSSBOMItem.MaterialName = wMaterial.MaterialName;
			}
			wMSSBOMItem.MaterialNumber = StringUtils.parseDouble(wOPITEM.getMEINS());
			wMSSBOMItem.UnitID = WsdlConstants.GetCFGUnit(wOPITEM.getMENGE()).ID;
			wMSSBOMItem.ReplaceType = StringUtils.parseInt(wOPITEM.getZBHOH());
			wMSSBOMItem.ReplaceRatio = 0.0f;
			wMSSBOMItem.OutsourceType = StringUtils.parseInt(wOPITEM.getZZZWW());
			wMSSBOMItem.OriginalType = StringUtils.parseBoolean(wOPITEM.getZYCYZ()) ? 1 : 0;
			wMSSBOMItem.DisassyType = StringUtils.parseBoolean(wOPITEM.getZCJXC()) ? 1 : 0;
			wMSSBOMItem.Remark = "";
			wMSSBOMItem.ParentID = 0;
			// 关联变更文件
//			wMSSBOMItem.TypeID = wThreadOP.LogID;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 根据工序和标准bom获取工位
	 */
	private int GetPlaceID(BMSEmployee wLoginUser, Integer wStepID, Integer wBOMID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select PartID from {0}.fpc_routepartpoint "
					+ "where routeid in (select RouteID from {0}.mss_bom where ID=:wBOMID) and partpointid=:wStepID;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wBOMID", wBOMID);
			wParamMap.put("wStepID", wStepID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("PartID"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 保存变更的物料
	 */
	private void SaveChangeItems(List<FPCPartPoint> wStepList, int wChangeLogID, List<MSSBOMItem> wItemList,
			List<MSSBOMItem> wNewItemList, String wBOMNo1, String wBOMNo2, List<OP> wOPList,
			List<Integer> wChangeLogIDList, ThreadOP wThreadOP) {
		try {
			/**
			 * 若变更日志未保存，不执行后续操作
			 */
			if (wChangeLogID <= 0) {
				return;
			}

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			int wCount = TCMMaterialChangeItemsDAO.getInstance().SelectCount(BaseDAO.SysAdmin, wChangeLogID,
					wErrorCode);
			if (wCount <= 0) {
				int wIndex = 0;
				int wLogID = 0;

				// 提取变更单
				List<String> wNoList = wOPList.stream().map(p -> p.getPROCESSCNO()).distinct()
						.collect(Collectors.toList());
				wNoList.removeIf(p -> StringUtils.isEmpty(p));
				for (String wChangeNo : wNoList) {

					if (wIndex > 0) {
						TCMMaterialChangeLog wCLog = TCMMaterialChangeLogDAO.getInstance().SelectByID(BaseDAO.SysAdmin,
								wChangeLogID, wErrorCode);
						wCLog.ID = 0;
						wLogID = TCMMaterialChangeLogDAO.getInstance().Update(BaseDAO.SysAdmin, wCLog, wErrorCode);
					} else {
						wLogID = wChangeLogID;
					}

					wChangeLogIDList.add(wLogID);

					List<OP> wOPNOList = wOPList.stream().filter(p -> p.getPROCESSCNO().equals(wChangeNo))
							.collect(Collectors.toList());
					boolean wIsSend = false;
					for (OP wOP : wOPNOList) {
						List<TCMMaterialChangeItems> wChangeItemList = new ArrayList<TCMMaterialChangeItems>();

						if (!wStepList.stream().anyMatch(p -> p.Name.equals(wOP.getOPNAME()))) {
							continue;
						}

						FPCPartPoint wStep = wStepList.stream().filter(p -> p.Name.equals(wOP.getOPNAME())).findFirst()
								.get();

						int wPlaceID = 0;
						if (wItemList.stream().anyMatch(p -> p.PartPointID == wStep.ID)) {
							wPlaceID = wItemList.stream().filter(p -> p.PartPointID == wStep.ID).findFirst()
									.get().PlaceID;
						}

						// 变更子项集合赋值
						AsignChangeItemList(wLogID, wItemList, wNewItemList, wBOMNo1, wBOMNo2, wStep.ID,
								wChangeItemList, wPlaceID);

						// 工序名称赋值
						for (TCMMaterialChangeItems wItem : wChangeItemList) {
							wItem.Annex = "";
							if (StringUtils.isNotEmpty(wItem.PartPointName)) {
								continue;
							}
							wItem.PartPointName = WsdlConstants.GetFPCStepName(wItem.PartPointID);
						}

						if (wChangeItemList.size() > 0) {
							wIsSend = true;
						}

						// 保存
						TCMMaterialChangeItemsDAO.getInstance().InsertList(BaseDAO.SysAdmin, wChangeItemList,
								wErrorCode);
					}

					OP wOP = wOPNOList.get(0);

					if (wIsSend) {
						// 推送消息给现场工艺
						SendMessageToCraft(wLogID, wOP, wOP.getPROCESSCNO(), wThreadOP);
					}

					// 推送消息给物流采购部
//					SendMessageToPurchase(BaseDAO.SysAdmin, wChangeLogID);

					// 更新变更单信息(变更单、变更用户)
					UpdateChangeInfo(wOP, wLogID);
//					ExecutorService wES = Executors.newFixedThreadPool(1);
//					wES.submit(() -> UpdateChangeInfo(wOP, wCloneLogID));
//					wES.shutdown();

					wIndex++;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 子项变更赋值
	 */
	private void AsignChangeItemList(int wChangeLogID, List<MSSBOMItem> wItemList, List<MSSBOMItem> wNewItemList,
			String wBOMNo1, String wBOMNo2, int wStepID, List<TCMMaterialChangeItems> wChangeItemList, int wPlaceID) {
		try {
			// 新增的物料
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
				wItem.ChangeLogID = wChangeLogID;
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
			// 删除的物料
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
					wItem.ChangeLogID = wChangeLogID;
					wItem.ChangeType = TCMChangeType.MaterialDelete.getValue();
					wItem.BOMNo1 = wBOMNo1;
					wItem.BOMNo2 = wBOMNo2;
					wItem.Methods = "";
					wItem.RouteNo1 = "";
					wItem.RouteNo2 = "";
					wChangeItemList.add(wItem);
				}
			}
			// 数量变化的物料(数量新增或数量减少)
			if (wPlaceID > 0) {
				int wClonedPlaceID = wPlaceID;
				List<MSSBOMItem> wAllList = wItemList.stream()
						.filter(p -> p.PlaceID == wClonedPlaceID && p.PartPointID == wStepID)
						.collect(Collectors.toList());
				List<MSSBOMItem> wChangeList = wNewItemList.stream().filter(p -> wAllList.stream()
						.anyMatch(q -> q.PartPointID == p.PartPointID && q.PlaceID == p.PlaceID
								&& q.ReplaceType == p.ReplaceType && q.OutsourceType == p.OutsourceType
								&& p.MaterialID == q.MaterialID && p.MaterialNumber != q.MaterialNumber)
						&& !wAllList.stream()
								.anyMatch(q -> q.PartPointID == p.PartPointID && q.PlaceID == p.PlaceID
										&& q.ReplaceType == p.ReplaceType && q.OutsourceType == p.OutsourceType
										&& p.MaterialID == q.MaterialID && p.MaterialNumber == q.MaterialNumber))
						.collect(Collectors.toList());
				for (MSSBOMItem wMSSBOMItem : wChangeList) {
					double wOldMaterialNumber = wAllList.stream().filter(p -> p.PartPointID == wMSSBOMItem.PartPointID
							&& p.PlaceID == wMSSBOMItem.PlaceID && p.MaterialID == wMSSBOMItem.MaterialID
							&& p.ReplaceType == wMSSBOMItem.ReplaceType && p.OutsourceType == wMSSBOMItem.OutsourceType
							&& p.MaterialNumber != wMSSBOMItem.MaterialNumber).findFirst().get().MaterialNumber;

					TCMMaterialChangeItems wItem = CloneTool.Clone(wMSSBOMItem, TCMMaterialChangeItems.class);

					wItem.ID = 0;
					wItem.ChangeLogID = wChangeLogID;
					wItem.ChangeType = TCMChangeType.MaterialNumberChange.getValue();
					wItem.BOMNo1 = wBOMNo1;
					wItem.BOMNo2 = wBOMNo2;
					wItem.Methods = "";
					wItem.RouteNo1 = "";
					wItem.RouteNo2 = "";
					wItem.OldMaterialNumber = wOldMaterialNumber;

					wChangeItemList.add(wItem);
				}
			}
			// 属性变化的物料(必换变偶换或偶换变必换)
			if (wPlaceID > 0) {
				int wClonedPlaceID = wPlaceID;
				List<MSSBOMItem> wAllList = wItemList.stream()
						.filter(p -> p.PlaceID == wClonedPlaceID && p.PartPointID == wStepID)
						.collect(Collectors.toList());
//				List<MSSBOMItem> wChangeList = wNewItemList.stream()
//						.filter(p -> wAllList.stream()
//								.anyMatch(q -> q.PartPointID == p.PartPointID && q.PlaceID == p.PlaceID
//										&& p.MaterialID == q.MaterialID && p.ReplaceType != q.ReplaceType)
//								&& !wAllList.stream()
//										.anyMatch(q -> q.PartPointID == p.PartPointID && q.PlaceID == p.PlaceID
//												&& p.MaterialID == q.MaterialID && p.ReplaceType == q.ReplaceType))
//						.collect(Collectors.toList());
//				for (MSSBOMItem wMSSBOMItem : wChangeList) {
//					TCMMaterialChangeItems wItem = CloneTool.Clone(wMSSBOMItem, TCMMaterialChangeItems.class);
//					wItem.ID = 0;
//					wItem.ChangeLogID = wChangeLogID;
//					wItem.ChangeType = TCMChangeType.MaterialPropertyChange.getValue();
//					wItem.BOMNo1 = wBOMNo1;
//					wItem.BOMNo2 = wBOMNo2;
//					wItem.Methods = "";
//					wItem.RouteNo1 = "";
//					wItem.RouteNo2 = "";
//					wChangeItemList.add(wItem);
//				}

				List<MSSBOMItem> wChangeList = wNewItemList.stream()
						.filter(p -> p.PlaceID == wClonedPlaceID && p.PartPointID == wStepID)
						.collect(Collectors.toList());
				for (MSSBOMItem wMSSBOMItem : wChangeList) {
					if (wAllList.stream()
							.anyMatch(p -> p.PlaceID == wMSSBOMItem.PlaceID && p.PartPointID == wMSSBOMItem.PartPointID
									&& p.MaterialID == wMSSBOMItem.MaterialID
									&& p.ReplaceType == wMSSBOMItem.ReplaceType
									&& p.OutsourceType == wMSSBOMItem.OutsourceType)) {
						continue;
					}

					if (!wAllList.stream().anyMatch(p -> p.PlaceID == wMSSBOMItem.PlaceID
							&& p.PartPointID == wMSSBOMItem.PartPointID && p.MaterialID == wMSSBOMItem.MaterialID)) {
						continue;
					}

					MSSBOMItem wExitItem = wAllList.stream().filter(p -> p.PlaceID == wMSSBOMItem.PlaceID
							&& p.PartPointID == wMSSBOMItem.PartPointID && p.MaterialID == wMSSBOMItem.MaterialID)
							.findFirst().get();

					String wPCText = "";
					// ①必换变偶换
					if (wExitItem.ReplaceType == 1 && wMSSBOMItem.ReplaceType == 2 && wExitItem.OutsourceType == 0
							&& wMSSBOMItem.OutsourceType == 0) {
						wPCText = "必换变偶换";
					}
					// ③必换变委外必修
					else if (wExitItem.ReplaceType == 1 && wExitItem.OutsourceType == 0
							&& wMSSBOMItem.OutsourceType == 1) {
						wPCText = "必换变委外必修";
					}
					// ④必换变委外偶修
					else if (wExitItem.ReplaceType == 1 && wExitItem.OutsourceType == 0
							&& wMSSBOMItem.OutsourceType == 2) {
						wPCText = "必换变委外偶修";
					}
					// ②偶换变必换
					else if (wExitItem.ReplaceType == 2 && wExitItem.OutsourceType == 0 && wMSSBOMItem.ReplaceType == 1
							&& wMSSBOMItem.OutsourceType == 0) {
						wPCText = "偶换变必换";
					}
					// ⑤偶换变委外偶修
					else if (wExitItem.ReplaceType == 2 && wExitItem.OutsourceType == 0 && wMSSBOMItem.ReplaceType == 0
							&& wMSSBOMItem.OutsourceType == 2) {
						wPCText = "偶换变委外偶修";
					}
					// ⑥偶换变委外必修
					else if (wExitItem.ReplaceType == 2 && wExitItem.OutsourceType == 0
							&& wMSSBOMItem.OutsourceType == 1) {
						wPCText = "偶换变委外必修";
					}
					// ⑦委外必修件变必换
					else if (wExitItem.ReplaceType != 1 && wExitItem.OutsourceType == 1 && wMSSBOMItem.ReplaceType == 1
							&& wMSSBOMItem.OutsourceType != 1) {
						wPCText = "委外必修变必换";
					}
					// ⑧委外必修件变偶换
					else if (wExitItem.ReplaceType != 1 && wExitItem.OutsourceType == 1 && wMSSBOMItem.ReplaceType == 2
							&& wMSSBOMItem.OutsourceType == 0) {
						wPCText = "委外必修变偶换";
					}
					// ⑨委外偶修件变必换
					else if (wExitItem.ReplaceType == 0 && wExitItem.OutsourceType == 2 && wMSSBOMItem.ReplaceType == 1
							&& wMSSBOMItem.OutsourceType == 0) {
						wPCText = "委外偶修变必换";
					}
					// ⑩委外偶修件变偶换
					else if (wExitItem.ReplaceType == 0 && wExitItem.OutsourceType == 2 && wMSSBOMItem.ReplaceType == 2
							&& wMSSBOMItem.OutsourceType == 0) {
						wPCText = "委外偶修变偶换";
					}
					// ①委外必修件变委外偶修
					else if (wExitItem.OutsourceType == 1 && wMSSBOMItem.ReplaceType == 0
							&& wMSSBOMItem.OutsourceType == 2) {
						wPCText = "委外必修变委外偶修";
					}
					// ②委外偶修件变委外必修
					else if (wExitItem.OutsourceType == 2 && wMSSBOMItem.OutsourceType == 1) {
						wPCText = "委外偶修变委外必修";
					}
					// ③委外必修变(偶换)委外必修
					else if (wExitItem.ReplaceType == 0 && wExitItem.OutsourceType == 1 && wMSSBOMItem.ReplaceType == 2
							&& wMSSBOMItem.OutsourceType == 1) {
						wPCText = "委外必修变(偶换)委外必修";
					}
					// ④委外偶修变(偶换)委外偶修
					else if (wExitItem.ReplaceType == 0 && wExitItem.OutsourceType == 2 && wMSSBOMItem.ReplaceType == 2
							&& wMSSBOMItem.OutsourceType == 2) {
						wPCText = "委外偶修变(偶换)委外必修";
					}

					TCMMaterialChangeItems wItem = CloneTool.Clone(wMSSBOMItem, TCMMaterialChangeItems.class);
					wItem.ID = 0;
					wItem.ChangeLogID = wChangeLogID;
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
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 保存工艺变更数据
	 */
	private void UpdateChangeInfo(OP wOP, int wChangeLogID) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wOP == null || StringUtils.isEmpty(wOP.getPROCESSCNO())) {
				return;
			}
			// ①查询变更业务单
			TCMMaterialChangeLog wTask = TCMMaterialChangeLogDAO.getInstance().SelectByID(BaseDAO.SysAdmin,
					wChangeLogID, wErrorCode);
			if (wTask == null || wTask.ID <= 0) {
				return;
			}
			// ②赋值变更信息
			wTask.ChangeFormNo = wOP.getPROCESSCNO();
			wTask.ChangeFormUri = wOP.getPROCESSC();
			wTask.ChangeUser = wOP.getPROCESSCUSER();
			wTask.ChangeType = wOP.getPROCESSCTYPE();
			// ③保存变更业务单
			TCMMaterialChangeLogDAO.getInstance().Update(BaseDAO.SysAdmin, wTask, wErrorCode);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	private void SendMessageToCraft(int wChangeLogID, OP wOP, String wChangeNo, ThreadOP wThreadOP) {
		try {
			List<BFCMessage> wBFCMessageList = new ArrayList<>();
			BFCMessage wMessage = null;
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			int wShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));

//			List<BMSRoleItem> wRoleItemList = CoreServiceImpl.getInstance().BMS_UserAll(BaseDAO.SysAdmin, 10)
//					.List(BMSRoleItem.class);
			List<Integer> wUserList = new ArrayList<Integer>();
			if (StringUtils.isNotEmpty(wOP.getPROCESSCUSER())) {
				if (WsdlConstants.GetBMSEmployeeList().values().stream()
						.anyMatch(p -> wOP.getPROCESSCUSER().contains(p.LoginID))) {
					wUserList.add(WsdlConstants.GetBMSEmployeeList().values().stream()
							.filter(p -> wOP.getPROCESSCUSER().contains(p.LoginID)).findFirst().get().ID);
				} else {
					SetTechNoticeUser(wUserList);
				}
			} else {
				SetTechNoticeUser(wUserList);
			}
			// 先只推送给周厚新
//			wRoleItemList = wRoleItemList.stream().filter(p -> p.FunctionID == 10997).collect(Collectors.toList());
			for (Integer wUserID : wUserList) {
				// 发送任务消息到人员
				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = wChangeLogID;
				wMessage.Title = StringUtils.Format("【工艺变更】{0}", wChangeNo);
				wMessage.MessageText = "工艺员变更了工艺，请查看变更详情并及时处理。";
				wMessage.ModuleID = BPMEventModule.SBOMChange.getValue();
				wMessage.ResponsorID = wUserID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0;
				wMessage.Type = BFCMessageType.Task.getValue();
				wMessage.StepID = wThreadOP.LogID;
				wBFCMessageList.add(wMessage);
			}

			CoreServiceImpl.getInstance().BFC_UpdateMessageList(BaseDAO.SysAdmin, wBFCMessageList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 设置默认的通知人，黄刚，李骏
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
			// 发送给物流采购部采购
			List<BMSRoleItem> wRoleItemList = CoreServiceImpl.getInstance().BMS_UserAll(wLoginUser, 21)
					.List(BMSRoleItem.class);
			List<BFCMessage> wBFCMessageList = new ArrayList<>();
			BFCMessage wMessage = null;
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			int wShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));
			for (BMSRoleItem wItem : wRoleItemList) {
				// 发送任务消息到人员
				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = wChangeLogID;
				wMessage.Title = StringUtils.Format("工艺变更-新增物料 {0}", String.valueOf(wShiftID));
				wMessage.MessageText = StringUtils.Format("工艺新增了物料，请查看详情，及时采购。");
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

	@SuppressWarnings("unused")
	private void MSS_SetNotStandard(BMSEmployee wLoginUser, MSSBOM wBOM, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("update {0}.mss_bom set IsStandard=0 "
					+ "where ProductID=:ProductID and LineID=:LineID and CustomerID=:CustomerID and RouteID=:RouteID and ID>0;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ProductID", wBOM.ProductID);
			wParamMap.put("LineID", wBOM.LineID);
			wParamMap.put("RouteID", wBOM.RouteID);
			wParamMap.put("CustomerID", wBOM.CustomerID);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 工艺BOP升版
	 */
	private void ProcessBOPUpgrade(Integer wRouteID, OP wOP, int wStepID) {
		try {
			BMSEmployee wLoginUser = BaseDAO.SysAdmin;
			// ①根据工艺路线ID获取工艺路线
			FPCRoute wRoute = FMCServiceImpl.getInstance().FPC_QueryRouteByID(wLoginUser, wRouteID)
					.Info(FPCRoute.class);
			if (wRoute == null || wRoute.ID <= 0) {
				return;
			}
			// ②根据工艺路线ID获取工艺工位
			List<FPCRoutePart> wRoutePartList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartListByRouteID(wLoginUser, wRoute.ID).List(FPCRoutePart.class);
			// ③根据工艺路线ID获取工艺工序
			List<FPCRoutePartPoint> wRoutePartPointList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartPointListByRouteID(wLoginUser, wRoute.ID, -1).List(FPCRoutePartPoint.class);
			// ④复制工艺路线
			wRoute.ID = 0;
			FPCRoute wNewRoute = FMCServiceImpl.getInstance().FPC_SaveRoute(wLoginUser, wRoute).Info(FPCRoute.class);
			if (wNewRoute == null || wNewRoute.ID <= 0) {
				return;
			}
			// ⑤复制工艺工位
			wRoutePartList.forEach(p -> p.RouteID = wNewRoute.ID);
			for (FPCRoutePart wFPCRoutePart : wRoutePartList) {
				FMCServiceImpl.getInstance().FPC_SaveRoutePart(wLoginUser, wFPCRoutePart);
			}
			// ⑥复制工艺工序
			Map<Integer, Integer> wPartPointRoutePartPointIDMap = new HashMap<Integer, Integer>();
			wRoutePartPointList.forEach(p -> p.RouteID = wNewRoute.ID);
			for (FPCRoutePartPoint wFPCRoutePartPoint : wRoutePartPointList) {
				FPCRoutePartPoint wNewRoutePartPoint = FMCServiceImpl.getInstance()
						.FPC_SaveRoutePartPoint(wLoginUser, wFPCRoutePartPoint).Info(FPCRoutePartPoint.class);
				if (wNewRoutePartPoint != null && wNewRoutePartPoint.ID > 0) {
					wPartPointRoutePartPointIDMap.put(wFPCRoutePartPoint.PartPointID, wNewRoutePartPoint.ID);
				}
			}
			// ⑦遍历工艺工序，若不是此工序，复制工艺文件
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<FPCCommonFile> wFileList = FPCCommonFileDAO.getInstance().SelectList(BaseDAO.SysAdmin, -1, "", null,
					null, wErrorCode);

			for (FPCRoutePartPoint wFPCRoutePartPoint : wRoutePartPointList) {
				int wNewID = 0;
				if (wPartPointRoutePartPointIDMap.containsKey(wFPCRoutePartPoint.PartPointID)) {
					wNewID = wPartPointRoutePartPointIDMap.get(wFPCRoutePartPoint.PartPointID);
				}
				// ⑧遍历工艺工序，若是此工序，创建工艺文件
				if (wFPCRoutePartPoint.PartPointID == wStepID) {
					SaveRouteStepSOP(wOP, wNewID, wFileList);
				} else {
					List<FPCStepSOP> wStepSOPList = FMCServiceImpl.getInstance()
							.FPC_QueryStepSOPList(wLoginUser, wFPCRoutePartPoint.ID).List(FPCStepSOP.class);

					if (wStepSOPList != null && wStepSOPList.size() > 0) {
						wStepSOPList.forEach(p -> p.ID = 0);
						int wNewCloneID = wNewID;
						wStepSOPList.forEach(p -> p.RoutePartPointID = wNewCloneID);
						for (FPCStepSOP wFPCStepSOP : wStepSOPList) {
							CoreServiceImpl.getInstance().FPC_UpdateStepSOP(BaseDAO.SysAdmin, wFPCStepSOP);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 根据工序ID获取工艺bopID集合
	 */
	public List<Integer> FPC_QueryRouteIDListByStepID(BMSEmployee wLoginUser, int wStepID,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT ID FROM {0}.fpc_route where ID in(SELECT distinct RouteID "
							+ "FROM {0}.fpc_routepartpoint where PartPointID=:StepID) and Active in(0,1);",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("StepID", wStepID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				if (wID > 0) {
					wResult.add(wID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据工序ID获取标准BOMID集合
	 */
	public List<Integer> MSS_QueryBOMIDListByStepID(BMSEmployee wLoginUser, int wStepID,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT distinct BOMID FROM {0}.mss_bomitem where PartPointID=:StepID order by BOMID asc;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("StepID", wStepID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("BOMID"));
				if (wID > 0) {
					wResult.add(wID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 标准bom升版
	 */
	public synchronized void AddVersion(ThreadOP wThreadOP) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			// ①查询所有工序列表
			List<FPCPartPoint> wStepList = WsdlConstants.GetFPCStepList().values().stream().filter(p -> p.Active == 1)
					.collect(Collectors.toList());
			if (wStepList == null || wStepList.size() <= 0) {
				return;
			}
			// ②根据工序ID查找MES系统工序ID
			if (!wStepList.stream().anyMatch(p -> wThreadOP.OPList.stream()
					.anyMatch(q -> p.Code.replace("PS-", "").equals(q.getOPID()) || p.Name.equals(q.getOPNAME())))) {
				return;
			}
			wStepList = wStepList.stream()
					.filter(p -> wThreadOP.OPList.stream().anyMatch(
							q -> p.Code.replace("PS-", "").equals(q.getOPID()) || p.Name.equals(q.getOPNAME())))
					.collect(Collectors.toList());
			if (wStepList == null || wStepList.size() <= 0) {
				return;
			}

			Map<Integer, OP> wOPMap = new HashMap<Integer, OP>();
			for (FPCPartPoint wFPCPartPoint : wStepList) {
				OP wItem = wThreadOP.OPList.stream()
						.filter(p -> p.getOPID().equals(wFPCPartPoint.Code.replace("PS-", ""))
								|| wFPCPartPoint.Name.equals(p.getOPNAME()))
						.findFirst().get();
				wOPMap.put(wFPCPartPoint.ID, wItem);
			}

			// ⑤查询所有用到此工序的当前的标准BOM
			List<Integer> wBOMIDList = MSS_QueryBOMIDListByStepIDList(BaseDAO.SysAdmin,
					wStepList.stream().map(p -> p.ID).collect(Collectors.toList()), wThreadOP.OPList.get(0).getOPID(),
					wErrorCode);
			if (wBOMIDList.size() > 0) {
				// 根据bomid集合查询受影响的在修车辆
				List<OMSOrder> wRowsOrderList = GetInspectOrderList(BaseDAO.SysAdmin, wBOMIDList, wErrorCode);
				wRowsOrderList = new ArrayList<OMSOrder>(wRowsOrderList.stream()
						.collect(Collectors.toMap(OMSOrder::getID, account -> account, (k1, k2) -> k2)).values());

				TCMMaterialChangeLog wTCMMaterialChangeLog = new TCMMaterialChangeLog();

				MSSBOM wBOM = FMCServiceImpl.getInstance().MSS_QueryBOM(BaseDAO.SysAdmin, wBOMIDList.get(0), "")
						.Info(MSSBOM.class);

				wTCMMaterialChangeLog.CreateID = BaseDAO.SysAdmin.ID;
				wTCMMaterialChangeLog.CreateTime = Calendar.getInstance();
				wTCMMaterialChangeLog.ID = 0;
				wTCMMaterialChangeLog.OrderIDList = StringUtils.Join(",",
						wRowsOrderList.stream().map(p -> p.ID).collect(Collectors.toList()));
				wTCMMaterialChangeLog.PartNoList = StringUtils.Join(",",
						wRowsOrderList.stream().map(p -> p.PartNo).collect(Collectors.toList()));
				wTCMMaterialChangeLog.ProductID = wBOM.ProductID;
				wTCMMaterialChangeLog.LineID = wBOM.LineID;
				wTCMMaterialChangeLog.CustomerID = wBOM.CustomerID;
				wTCMMaterialChangeLog.Customer = TCMMaterialChangeLogDAO.getInstance().GetCustomer(BaseDAO.SysAdmin,
						wBOMIDList, wErrorCode);

				/**
				 * 有受影响的车辆，才执行保存操作
				 */
				if (wRowsOrderList.size() > 0) {
					wTCMMaterialChangeLog.ID = TCMMaterialChangeLogDAO.getInstance().Update(BaseDAO.SysAdmin,
							wTCMMaterialChangeLog, wErrorCode);
				}

				List<String> wBOMNO1List = new ArrayList<String>();
				List<String> wBOMNO2List = new ArrayList<String>();
				List<Integer> wChangeLogIDList = new ArrayList<Integer>();
				for (Integer wBOMID : wBOMIDList) {
					// ⑥标准BOM升版
					StandardBOMUpgrade(wBOMID, wThreadOP.OPList, wStepList, wOPMap, wTCMMaterialChangeLog.ID,
							wBOMNO1List, wBOMNO2List, wChangeLogIDList, wThreadOP);
				}
				// 处理BOMNO
				String wBOMNO1 = StringUtils.Join(",", wBOMNO1List);
				String wBOMNO2 = StringUtils.Join(",", wBOMNO2List);
				for (int wChangeLogID : wChangeLogIDList) {
					TCMMaterialChangeLogDAO.getInstance().UpdateBOMNo(BaseDAO.SysAdmin, wBOMNO1, wBOMNO2, wChangeLogID,
							wErrorCode);
				}
			}
			System.out.println("工序变更结束");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取受影响的车辆列表
	 */
	public List<OMSOrder> GetInspectOrderList(BMSEmployee wLoginUser, List<Integer> wBOMIDList,
			OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResult = new ArrayList<OMSOrder>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select t1.* from {0}.oms_order t1,{2}.mss_bom t2 "
							+ "where t1.ProductID=t2.ProductID and t2.LineID=t2.LineID "
							+ "and t1.BureauSectionID=t2.CustomerID and t2.ID in ({1}) and t1.Status in(3,4);",
					wInstance.Result, StringUtils.Join(",", wBOMIDList), wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				OMSOrder wItem = new OMSOrder();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据工序ID集合获取所有当前bom
	 */
	private List<Integer> MSS_QueryBOMIDListByStepIDList(BMSEmployee wLoginUser, List<Integer> wStepIDList,
			String wOPID, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			// ①获取所有当前bomID集合
			wResult = GetBOMIDList(wLoginUser, wStepIDList, wOPID, wErrorCode);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取当前标准BOM集合
	 */
	private List<Integer> GetBOMIDList(BMSEmployee wLoginUser, List<Integer> wStepIDList, String wCode,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wStepIDList == null || wStepIDList.size() <= 0) {
				return wResult;
			}

//			String wSQL = StringUtils.Format(
//					"select * from {0}.mss_bom where routeid in (select distinct RouteID "
//							+ "from {0}.fpc_routepartpoint t where PartPointID in ({1}) "
//							+ "and {2} in (select count(*) from {0}.fpc_routepartpoint where RouteID =t.RouteID "
//							+ "and PartPointID in ({1}))) and isstandard=1;",
//					wInstance.Result, StringUtils.Join(",", wStepIDList), String.valueOf(wStepIDList.size()));

			String wSQL = StringUtils
					.Format("select ID from {0}.mss_bom where RouteID in (select ID from {0}.fpc_route "
							+ "where id in (select RouteID from {0}.fpc_routepartpoint where code =:wCode) "
							+ "and active=1 and IsStandard=1) and IsStandard=1;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wCode", wCode);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				wResult.add(wID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据RouteID获取最新的转序控制属性字典
	 */
	public Map<Integer, Integer> GetPartChangeControlMap(BMSEmployee wLoginUser, int wRouteID,
			OutResult<Integer> wErrorCode) {
		Map<Integer, Integer> wResult = new HashMap<Integer, Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select PartID,ChangeControl from {0}.fpc_routepart where routeid =:wRouteID;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wRouteID", wRouteID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {

				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				int wChangeControl = StringUtils.parseInt(wReader.get("ChangeControl"));
				if (!wResult.containsKey(wPartID)) {
					wResult.put(wPartID, wChangeControl);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取最大的标准BOMID
	 */
	public int SelectMaxBoMID(BMSEmployee wLoginUser, int wRouteID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select Max(t1.ID) ID from {0}.mss_bom t1,{0}.fpc_route t2 where t1.IsStandard=1 "
							+ "and t1.ProductID=t2.ProductID "
							+ "and t1.LineID=t2.LineID and t1.CustomerID=t2.CustomerID and t2.ID=:ID;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wRouteID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("ID"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 更新工艺路线
	 */
	public void UpdateRouteID(BMSEmployee wLoginUser, int wRouteID, int wMaxBOMID, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("update {0}.mss_bom set RouteID=:RouteID,EditTime=now() where ID = :ID;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("RouteID", wRouteID);
			wParamMap.put("ID", wMaxBOMID);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 关联工艺变更文件
	 */
	public void UpdateDefaultOrder(BMSEmployee wLoginUser, int wLogID, int wBOPID, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("update {0}.fpc_routepartpoint set DefaultOrder=:DefaultOrder "
					+ "where RouteID=:RouteID and DefaultOrder='''' and ID>0;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("DefaultOrder", String.valueOf(wLogID));
			wParamMap.put("RouteID", wBOPID);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public List<Integer> GetInPlantOrderIDList(BMSEmployee wLoginUser, int wBOPID, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select t1.ID from {0}.oms_order t1,{1}.fpc_route t2 "
					+ "where t1.ProductID=t2.ProductID and t1.LineID=t2.LineID and t1.BureauSectionID=t2.CustomerID "
					+ " and t1.Active=1 and t1.Status=3 and t2.ID=:wBOPID;", wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wBOPID", wBOPID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				wResult.add(wID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 更新在厂车RouteID
	 */
	public void UpdateInPlantRoute(BMSEmployee wLoginUser, int wBOPID, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			List<Integer> wOrderIDList = GetInPlantOrderIDList(wLoginUser, wBOPID, wErrorCode);
			if (wOrderIDList == null || wOrderIDList.size() <= 0) {
				return;
			}

			String wSQL = StringUtils.Format("update {0}.oms_order set RouteID=:RouteID where ID in ({1});",
					wInstance.Result, StringUtils.Join(",", wOrderIDList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("RouteID", wBOPID);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}
}
