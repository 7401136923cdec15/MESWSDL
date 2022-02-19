package com.mes.code.server.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mes.code.server.service.MESERPService;
import com.mes.code.server.service.mesenum.MESException;
import com.mes.code.server.service.po.OutResult;
import com.mes.code.server.service.po.ServiceResult;
import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.service.po.cfg.CFGUnit;
import com.mes.code.server.service.po.crm.CRMCustomer;
import com.mes.code.server.service.po.dms.DMSDeviceLedger;
import com.mes.code.server.service.po.erp.ERPFilterChar;
import com.mes.code.server.service.po.erp.MESCustomer;
import com.mes.code.server.service.po.erp.MESDevice;
import com.mes.code.server.service.po.erp.MESMaterial;
import com.mes.code.server.service.po.erp.MESOrder;
import com.mes.code.server.service.po.erp.MESPartNoBOM;
import com.mes.code.server.service.po.erp.MESPartNoBOMDetails;
import com.mes.code.server.service.po.erp.MESProduct;
import com.mes.code.server.service.po.fpc.FPCProduct;
import com.mes.code.server.service.po.mcs.MCSLogInfo;
import com.mes.code.server.service.po.mss.MSSMaterial;
import com.mes.code.server.service.po.oms.OMSCommand;
import com.mes.code.server.service.po.oms.OMSOrder;
import com.mes.code.server.service.po.tcm.TCMMaterialChangeItems;
import com.mes.code.server.service.po.tcm.TCMMaterialChangeLog;
import com.mes.code.server.service.po.wms.WMSCheckoutInfo;
import com.mes.code.server.service.po.wms.WMSInstockBackHead;
import com.mes.code.server.service.po.wms.WMSPickBackHead;
import com.mes.code.server.service.po.wms.WMSReturn;
import com.mes.code.server.service.utils.CloneTool;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.dao.BaseDAO;
import com.mes.code.server.serviceimpl.dao.erp.ERPFilterCharDAO;
import com.mes.code.server.serviceimpl.dao.mcs.MCSLogInfoDAO;
import com.mes.code.server.serviceimpl.dao.oms.OMSCommandDAO;
import com.mes.code.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.code.server.serviceimpl.dao.tcm.TCMMaterialChangeItemsDAO;
import com.mes.code.server.serviceimpl.dao.tcm.TCMMaterialChangeLogDAO;
import com.mes.code.server.serviceimpl.dao.tcm.TCMVerificationDAO;
import com.mes.code.server.serviceimpl.utils.WsdlConstants;
import com.mes.code.server.shristool.CalendarDAO;
import com.mes.code.server.utils.Constants;

/**
 * MES与SAP对接实现类
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-12-8 21:09:01
 * @LastEditTime 2020-12-8 21:09:04
 *
 */
@Service
public class MESERPServiceImpl implements MESERPService {

	private static Logger logger = LoggerFactory.getLogger(MESERPServiceImpl.class);

	public MESERPServiceImpl() {
	}

	private static MESERPService Instance;

	public static MESERPService getInstance() {
		if (Instance == null)
			Instance = new MESERPServiceImpl();
		return Instance;
	}

	@Override
	public ServiceResult<Map<String, Object>> MES_SaveProductList(Map<String, Object> wParam) {
		ServiceResult<Map<String, Object>> wResult = new ServiceResult<Map<String, Object>>();
		wResult.Result = new HashMap<String, Object>();
		try {
			List<MESProduct> wList = CloneTool.CloneArray(wParam.get("data"), MESProduct.class);

			// ①日志保存
			CalendarDAO.getInstance().WriteLogFile(JSON.toJSONString(wList), "ERPProduct", "", "", "", "", 0, 0);

			if (wList != null && wList.size() > 0) {

				for (MESProduct wMESProduct : wList) {
					FPCProduct wFPCProduct = new FPCProduct();
					wFPCProduct.Active = 0;
					wFPCProduct.AuditorID = BaseDAO.SysAdmin.ID;
					wFPCProduct.AuditTime = Calendar.getInstance();
					wFPCProduct.CreateTime = Calendar.getInstance();
					wFPCProduct.EditTime = Calendar.getInstance();
					wFPCProduct.ID = 0;
					wFPCProduct.ProductName = wMESProduct.getPRATX();
					wFPCProduct.ProductNo = wMESProduct.getPRATX();
					wFPCProduct.ProductCode = wMESProduct.getPRATX();
					wFPCProduct.ProductTypeID = 0;
					wFPCProduct.Status = 3;
					wFPCProduct.TransportType = 1;
					FMCServiceImpl.getInstance().FPC_SaveProduct(BaseDAO.SysAdmin, wFPCProduct);
				}

				wResult.Result.put("STATUS", "S");
				wResult.Result.put("MSG", "接收成功");
			} else {
				wResult.Result.put("STATUS", "E");
				wResult.Result.put("MSG", "接收失败");
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());

			wResult.Result.put("STATUS", "E");
			wResult.Result.put("MSG", "接收失败");
		}
		return wResult;
	}

	@Override
	public ServiceResult<Map<String, Object>> MES_SaveCustomerList(Map<String, Object> wParam) {
		ServiceResult<Map<String, Object>> wResult = new ServiceResult<Map<String, Object>>();
		wResult.Result = new HashMap<String, Object>();
		try {
			List<MESCustomer> wList = CloneTool.CloneArray(wParam.get("data"), MESCustomer.class);

			// ①日志保存
			CalendarDAO.getInstance().WriteLogFile(JSON.toJSONString(wList), "ERPCustomer", "", "", "", "", 0, 0);

			if (wList != null && wList.size() > 0) {
				for (MESCustomer wMESCustomer : wList) {
					CRMCustomer wCRMCustomer = new CRMCustomer();

					wCRMCustomer.ID = 0;
					wCRMCustomer.Active = 1;
					wCRMCustomer.Status = 1;
					wCRMCustomer.AuditTime = Calendar.getInstance();
					wCRMCustomer.CreateTime = Calendar.getInstance();
					wCRMCustomer.TaxCode = wMESCustomer.zjdxx;
					wCRMCustomer.CustomerCode = wMESCustomer.zjdxx;
					wCRMCustomer.CustomerName = wMESCustomer.ktext;

					SCMServiceImpl.getInstance().CRM_SaveCustomer(BaseDAO.SysAdmin, wCRMCustomer);
				}

				wResult.Result.put("STATUS", "S");
				wResult.Result.put("MSG", "接收成功");
			} else {
				wResult.Result.put("STATUS", "E");
				wResult.Result.put("MSG", "接收失败");
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());

			wResult.Result.put("STATUS", "E");
			wResult.Result.put("MSG", "接收失败");
		}
		return wResult;
	}

	@Override
	public ServiceResult<Map<String, Object>> MES_SaveMaterialList(Map<String, Object> wParam) {
		ServiceResult<Map<String, Object>> wResult = new ServiceResult<Map<String, Object>>();
		wResult.Result = new HashMap<String, Object>();
		try {
			// MD5校验，防止重复提交
			String wContent = JSON.toJSONString(wParam.get("data"));
			OutResult<Integer> wNewIDResult = new OutResult<Integer>(0);
			String wCheckResult = TCMVerificationDAO.getInstance().Check(BaseDAO.SysAdmin, wContent, wNewIDResult);
			if (StringUtils.isNotEmpty(wCheckResult)) {
				wResult.Result.put("STATUS", "E");
				wResult.Result.put("MSG", "接收失败::" + wCheckResult);
				return wResult;
			}

			List<MESMaterial> wList = CloneTool.CloneArray(wParam.get("data"), MESMaterial.class);

			// ①日志保存
			CalendarDAO.getInstance().WriteLogFile(JSON.toJSONString(wList), "ERPMaterial", "", "", "", "", 0, 0);

			// 单位列表
			List<CFGUnit> wCFGUnitList = CoreServiceImpl.getInstance().CFG_QueryUnitList(BaseDAO.SysAdmin)
					.List(CFGUnit.class);

			if (wList != null && wList.size() > 0) {
				List<MSSMaterial> wMaterialList = new ArrayList<MSSMaterial>();
				for (MESMaterial wMESMaterial : wList) {
					MSSMaterial wMSSMaterial = GetMSSMaterial(wMESMaterial, wCFGUnitList);
					wMaterialList.add(wMSSMaterial);
				}

				FMCServiceImpl.getInstance().MSS_SaveMaterial(BaseDAO.SysAdmin, wMaterialList);

				wResult.Result.put("STATUS", "S");
				wResult.Result.put("MSG", "接收成功");
			} else {
				wResult.Result.put("STATUS", "E");
				wResult.Result.put("MSG", "接收失败");
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());

			wResult.Result.put("STATUS", "E");
			wResult.Result.put("MSG", "接收失败");
		}
		return wResult;
	}

	/**
	 * 物料结构转化
	 */
	private MSSMaterial GetMSSMaterial(MESMaterial wMESMaterial, List<CFGUnit> wCFGUnitList) {
		MSSMaterial wResult = new MSSMaterial();
		try {
			wResult.Name = wMESMaterial.maktx;
			wResult.AuditTime = Calendar.getInstance();
			wResult.Author = BaseDAO.SysAdmin.Name;
			wResult.Auditor = BaseDAO.SysAdmin.Name;
			wResult.MaterialNo = wMESMaterial.matnr;
			wResult.MaterialName = wMESMaterial.maktx;

			int wUnitID = 0;
			if (wCFGUnitList.stream().anyMatch(p -> p.Name.equals(wMESMaterial.meins))) {
				wUnitID = wCFGUnitList.stream().filter(p -> p.Name.equals(wMESMaterial.meins)).findFirst().get().ID;
			} else {
				CFGUnit wCFGUnit = new CFGUnit();
				wCFGUnit.Name = wMESMaterial.meins;
				wCFGUnit.OperatorID = -100;
				wCFGUnit.EditTime = Calendar.getInstance();
				wCFGUnit.Active = 1;
				wCFGUnit = CoreServiceImpl.getInstance().CFG_SaveUnit(BaseDAO.SysAdmin, wCFGUnit).Info(CFGUnit.class);
				if (wCFGUnit != null && wCFGUnit.ID > 0) {
					wUnitID = wCFGUnit.ID;
					wCFGUnitList.add(wCFGUnit);
				}
			}
			wResult.CYUnitID = wUnitID;

			wResult.Groes = wMESMaterial.groes;
			wResult.Remark = "";
			wResult.GrossWeight = StringUtils.parseDouble(wMESMaterial.brgew);
			wResult.NetWeight = StringUtils.parseDouble(wMESMaterial.ntgew);
			wResult.MaterialGroup = wMESMaterial.matkl;
			wResult.MaterialType = wMESMaterial.mtart;
			wResult.Normt = wMESMaterial.normt;

			wResult.EditTime = Calendar.getInstance();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Map<String, Object>> MES_CreatePartNoBOMList(Map<String, Object> wParam) {
		ServiceResult<Map<String, Object>> wResult = new ServiceResult<Map<String, Object>>();
		wResult.Result = new HashMap<String, Object>();
		try {
			List<MESPartNoBOM> wList = CloneTool.CloneArray(wParam.get("OT_ZPPBOM2H"), MESPartNoBOM.class);
			List<MESPartNoBOMDetails> wListDetailList = CloneTool.CloneArray(wParam.get("OT_ZPPBOM2D"),
					MESPartNoBOMDetails.class);

			if (wList != null && wList.size() > 0 && wListDetailList != null && wListDetailList.size() > 0) {

				wResult.Result.put("STATUS", "S");
				wResult.Result.put("MSG", "接收成功");
			} else {
				wResult.Result.put("STATUS", "E");
				wResult.Result.put("MSG", "接收失败");
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());

			wResult.Result.put("STATUS", "E");
			wResult.Result.put("MSG", "接收失败");
		}
		return wResult;
	}

	@Override
	public ServiceResult<Map<String, Object>> MES_SaveOrderList(Map<String, Object> wParam) {
		ServiceResult<Map<String, Object>> wResult = new ServiceResult<Map<String, Object>>();
		wResult.Result = new HashMap<String, Object>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<MESOrder> wList = CloneTool.CloneArray(wParam.get("data"), MESOrder.class);

			// 过滤数据
			wList = FilterData(wList);

			if (wList != null && wList.size() > 0) {
				Calendar wBaseTime = Calendar.getInstance();
				wBaseTime.set(2000, 0, 1, 0, 0, 0);

				SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");

				OMSOrder wOrder = null;
				for (MESOrder wItem : wList) {
					// 判断订单是否已存在
					List<OMSOrder> wOList = OMSOrderDAO.getInstance().SelectList(BaseDAO.SysAdmin, -1, -1, wItem.pspnr,
							-1, -1, -1, "", "", -1, null, null, null, null, null, wErrorCode);
					if (wOList.size() > 0) {
						wOrder = wOList.get(0);
					}

					int wLineID = GetLineID(wItem);
					int wCustomerID = GetCustomerID(wItem);

					if (wLineID <= 0 || wCustomerID <= 0) {

						wResult.Result.put("STATUS", "E");
						wResult.Result.put("MSG", "接收失败，修程或局段不存在。");

						// ①日志保存
						String wMsg = StringUtils.Format("接收失败，{0}局段不存在。", wItem.pspnr);
						CalendarDAO.getInstance().WriteLogFile(JSON.toJSONString(wList) + "::" + wMsg, "ERPOrder", "",
								"", "", "", 0, 0);

						return wResult;
					}

					String wStr = wItem.pspnr.substring(wItem.pspnr.lastIndexOf("."));
					String wWBSNo = wItem.pspnr.replace(wStr, "");

					OMSCommand wOMSCommand = null;

					List<OMSCommand> wCommandList = OMSCommandDAO.getInstance().SelectList(BaseDAO.SysAdmin, -1, 1, -1,
							-1, wWBSNo, wErrorCode);
					if (wCommandList.size() > 0) {
						wOMSCommand = wCommandList.get(0);
					} else {
						wOMSCommand = new OMSCommand();
						wOMSCommand.ID = 0;
						wOMSCommand.CustomerID = wCustomerID;
						wOMSCommand.No = "O" + wSDF.format(Calendar.getInstance().getTime());
						wOMSCommand.Status = 1;
						wOMSCommand.EditorID = BaseDAO.SysAdmin.ID;
						wOMSCommand.EditTime = Calendar.getInstance();
						wOMSCommand.Active = 1;
						wOMSCommand.AuditorID = BaseDAO.SysAdmin.ID;
						wOMSCommand.AuditTime = Calendar.getInstance();
						wOMSCommand.CreatorID = BaseDAO.SysAdmin.ID;
						wOMSCommand.CreateTime = Calendar.getInstance();
						wOMSCommand.FactoryID = wLineID;// 修程
						wOMSCommand.BusinessUnitID = WsdlConstants.GetFPCProduct(wItem.zchex).ID;// 车型
						wOMSCommand.FQTYPlan = 1;
						wOMSCommand.FQTYActual = 1;
						wOMSCommand.WBSNo = wItem.pspnr.replace(wStr, "");

						if (wOMSCommand.BusinessUnitID <= 0) {
							continue;
						}

						wOMSCommand.ID = OMSCommandDAO.getInstance().Update(BaseDAO.SysAdmin, wOMSCommand, wErrorCode);
					}

					if (wOMSCommand.ID > 0) {
						OMSOrder wOMSOrder = new OMSOrder();
						if (wOrder != null && wOrder.ID > 0) {
							wOMSOrder = wOrder;
						}

//						wOMSOrder.ID = 0;
						wOMSOrder.ERPID = 0;
						wOMSOrder.OrderNo = wItem.pspnr;
						wOMSOrder.LineID = wLineID;
						wOMSOrder.ProductID = WsdlConstants.GetFPCProduct(wItem.zchex).ID;
						wOMSOrder.BureauSectionID = wCustomerID;

						// 計劃進廠日期賦值
//						20211229
						if (StringUtils.isNotEmpty(wItem.zrcDate)) {
							if (wItem.zrcDate.contains("-")) {
								String[] wStrs = wItem.zrcDate.split("-");

								Calendar wCalendar = Calendar.getInstance();
								wCalendar.set(Integer.parseInt(wStrs[0]), Integer.parseInt(wStrs[1]) - 1,
										Integer.parseInt(wStrs[2]), 0, 0, 0);
								wOMSOrder.PlanReceiveDate = wCalendar;
							} else {
								Calendar wCalendar = Calendar.getInstance();
								wCalendar.set(Integer.parseInt(wItem.zrcDate.substring(0, 4)),
										Integer.parseInt(wItem.zrcDate.substring(4, 6)) - 1,
										Integer.parseInt(wItem.zrcDate.substring(6)), 0, 0, 0);
								wOMSOrder.PlanReceiveDate = wCalendar;
							}
						} else {
							wOMSOrder.PlanReceiveDate = wBaseTime;
						}

						if (StringUtils.isEmpty(wItem.ztch)) {
							wOMSOrder.PartNo = "";
						} else {
							wOMSOrder.PartNo = StringUtils.Format("{0}#{1}", wItem.zchex, wItem.ztch);
						}
						wOMSOrder.Status = 1;
//						wOMSOrder.PlanReceiveDate = wBaseTime;
						wOMSOrder.RealReceiveDate = wBaseTime;
						wOMSOrder.PlanFinishDate = wBaseTime;
						wOMSOrder.RealStartDate = wBaseTime;
						wOMSOrder.RealFinishDate = wBaseTime;
						wOMSOrder.RealSendDate = wBaseTime;
						wOMSOrder.CreateID = BaseDAO.SysAdmin.ID;
						wOMSOrder.CreateTime = Calendar.getInstance();
						wOMSOrder.EditID = BaseDAO.SysAdmin.ID;
						wOMSOrder.EditTime = Calendar.getInstance();
						wOMSOrder.AuditorID = BaseDAO.SysAdmin.ID;
						wOMSOrder.AuditTime = Calendar.getInstance();
						wOMSOrder.Active = 1;
						wOMSOrder.CommandID = wOMSCommand.ID;
						wOMSOrder.RouteID = 0;
						wOMSOrder.TelegraphTime = wBaseTime;
						wOMSOrder.TelegraphRealTime = wBaseTime;

						if (wOMSOrder.ProductID <= 0) {
							continue;
						}

						OMSOrderDAO.getInstance().Update(BaseDAO.SysAdmin, wOMSOrder, wErrorCode);
					}
				}

				wResult.Result.put("STATUS", "S");
				wResult.Result.put("MSG", "接收成功");

				// ①日志保存
				String wMsg = "接收成功。";
				CalendarDAO.getInstance().WriteLogFile(JSON.toJSONString(wList) + "::" + wMsg, "ERPOrder", "", "", "",
						"", 0, 0);
			} else {
				wResult.Result.put("STATUS", "E");
				wResult.Result.put("MSG", "接收失败");

				// ①日志保存
				String wMsg = "接收失败，解析数据失败。";
				CalendarDAO.getInstance().WriteLogFile(JSON.toJSONString(wList) + "::" + wMsg, "ERPOrder", "", "", "",
						"", 0, 0);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());

			wResult.Result.put("STATUS", "E");
			wResult.Result.put("MSG", "接收失败");
		}
		return wResult;
	}

	private List<MESOrder> FilterData(List<MESOrder> wList) {
		List<MESOrder> wResult = wList;
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<ERPFilterChar> wCharList = ERPFilterCharDAO.getInstance().SelectList(BaseDAO.SysAdmin, -1, "",
					wErrorCode);
			if (wCharList == null || wCharList.size() <= 0) {
				return wResult;
			}

			wResult = wResult.stream().filter(p -> !wCharList.stream().anyMatch(q -> p.pspnr.contains(q.Text)))
					.collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 解析局段GQ-A502.P.NC.001
	 */
	private int GetCustomerID(MESOrder wItem) {
		int wResult = 0;
		try {
			String[] wStrs = wItem.pspnr.split("\\.");
			if (wStrs.length == 4) {
				wResult = WsdlConstants.GetCRMCustomerByCode(wStrs[2]).ID;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 解析修程GQ-A502.P.NC.001
	 */
	private int GetLineID(MESOrder wItem) {
		int wResult = 0;
		try {
			String[] wStrs = wItem.pspnr.split("\\.");
			if (wStrs.length == 4) {
				wResult = wStrs[0].substring(4, 5).equals("5") ? 1 : wStrs[0].substring(4, 5).equals("6") ? 2 : 0;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Map<String, Object>> MES_SaveDeviceList(Map<String, Object> wParam) {
		ServiceResult<Map<String, Object>> wResult = new ServiceResult<Map<String, Object>>();
		wResult.Result = new HashMap<String, Object>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<MESDevice> wList = CloneTool.CloneArray(wParam.get("data"), MESDevice.class);

			// ①日志保存
			CalendarDAO.getInstance().WriteLogFile(JSON.toJSONString(wList), "ERPDevice", "", "", "", "", 0, 0);

			if (wList != null && wList.size() > 0) {
				for (MESDevice wMESDevice : wList) {
					DMSDeviceLedger wItem = OMSOrderDAO.getInstance().ExistDevice(BaseDAO.SysAdmin, wMESDevice.equnr,
							wErrorCode);
					if (wItem.ID > 0) {
						wItem.Name = wMESDevice.eqktu;
						wItem.DeviceNo = wMESDevice.equnr;
						wItem.Status = 0;
						if (wMESDevice.txt30.equals("可用")) {
							wItem.Status = 1;
						}
						wItem.BusinessUnitID = 2;
						wItem.FactoryID = 1;
						wItem.OperatorID = BaseDAO.SysAdmin.ID;
						wItem.OperatorTime = Calendar.getInstance();
					} else {
						wItem = new DMSDeviceLedger();

						wItem.ID = 0;
						wItem.Name = wMESDevice.eqktu;
						wItem.DeviceNo = wMESDevice.equnr;
						wItem.Status = 0;
						wItem.BusinessUnitID = 2;
						wItem.FactoryID = 1;
						wItem.OperatorID = BaseDAO.SysAdmin.ID;
						wItem.OperatorTime = Calendar.getInstance();
					}

					DMSDeviceLedger wInfo = FMCServiceImpl.getInstance().DMS_SaveDeviceLedger(BaseDAO.SysAdmin, wItem)
							.Info(DMSDeviceLedger.class);

					if (wInfo != null && wInfo.ID > 0 && wItem.ID <= 0) {
						wInfo.Name = wMESDevice.eqktu;
						wInfo.DeviceNo = wMESDevice.equnr;
						if (wMESDevice.txt30.equals("可用")) {
							wInfo.Status = 1;
						}
						FMCServiceImpl.getInstance().DMS_SaveDeviceLedger(BaseDAO.SysAdmin, wInfo);
					}
				}

				wResult.Result.put("STATUS", "S");
				wResult.Result.put("MSG", "接收成功");
			} else {
				wResult.Result.put("STATUS", "E");
				wResult.Result.put("MSG", "接收失败");
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());

			wResult.Result.put("STATUS", "E");
			wResult.Result.put("MSG", "接收失败");
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<MCSLogInfo>> MCS_QueryLogList() {
		ServiceResult<List<MCSLogInfo>> wResult = new ServiceResult<List<MCSLogInfo>>();
		wResult.Result = new ArrayList<MCSLogInfo>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			String wDirPath = Constants.getStaticPath() + "datasource";

			File wFile = new File(wDirPath);
			if (!wFile.isDirectory())
				return wResult;

			Map<String, String> wMap = new HashMap<String, String>();

			wMap.put("ERPProduct", "ERP车型");
			wMap.put("ERPCustomer", "ERP局段");
			wMap.put("ERPMaterial", "ERP物料");
			wMap.put("ERPOrder", "ERP订单");
			wMap.put("ERPDevice", "ERP设备");
			wMap.put("TCMStation", "TCM工位");
			wMap.put("TCMBOM", "TCM标准BOM");
			wMap.put("TCMBOP", "TCM工艺BOP");
			wMap.put("TCMDOC", "TCM工艺文件");
			wMap.put("TCMOP", "TCM变更工序");

			File[] wfss = wFile.listFiles();
			for (File wItem : wfss) {
				MCSLogInfo wSubCatalog = new MCSLogInfo();

				Calendar wTime = Calendar.getInstance();
				wTime.setTime(new Date(wItem.lastModified()));

				SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				wSubCatalog.CreateTime = wTime;
				wSubCatalog.CreateTimeStr = wSDF.format(wTime.getTime());
				wSubCatalog.FilePath = wItem.getAbsolutePath();
				wSubCatalog.FileName = wItem.getName();
				for (String wKey : wMap.keySet()) {
					if (wSubCatalog.FileName.contains(wKey)) {
						wSubCatalog.FileType = wMap.get(wKey);
					}
				}

				wResult.Result.add(wSubCatalog);
			}

			// aps日志
			wDirPath = wDirPath.replace("MESWSDL", "MESLOCOAPS");

			wFile = new File(wDirPath);
			if (!wFile.isDirectory())
				return wResult;

			wMap = new HashMap<String, String>();

			wMap.put("bom", "SAP台车BOM");
			wMap.put("WBS", "SAP台车WBS");

			wfss = wFile.listFiles();
			for (File wItem : wfss) {
				MCSLogInfo wSubCatalog = new MCSLogInfo();

				Calendar wTime = Calendar.getInstance();
				wTime.setTime(new Date(wItem.lastModified()));

				SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				wSubCatalog.CreateTime = wTime;
				wSubCatalog.CreateTimeStr = wSDF.format(wTime.getTime());
				wSubCatalog.FilePath = wItem.getAbsolutePath();
				wSubCatalog.FileName = wItem.getName();
				for (String wKey : wMap.keySet()) {
					if (wSubCatalog.FileName.contains(wKey)) {
						wSubCatalog.FileType = wMap.get(wKey);
					}
				}

				wResult.Result.add(wSubCatalog);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public void MCSG_DownloadLogFileByFilePath(String wFilePath, String wFileName, HttpServletResponse wResponse)
			throws IOException {
		// 获取输出流
		OutputStream wOutputStream = wResponse.getOutputStream();
		try {
			if (!new File(wFilePath).exists())
				return;

			// 清空下载文件的空白行（空白行是因为有的前端代码编译后产生的）
			wResponse.reset();
			// 设置响应头，把文件名字设置好
			wResponse.setHeader("Content-Disposition", "attachment; filename=" + wFileName);
			// 解决编码问题
			wResponse.setContentType("application/octet-stream; charset=utf-8");
			// 创建存储的文件对象（文件存储的路径和文件名字）
			File wTargetFile = new File(wFilePath);
			// 输出流开始写出文件（FileUtils是Apache下的工具类可以直接调用）
			wOutputStream.write(FileUtils.readFileToByteArray(wTargetFile));
			// 刷新流
			wOutputStream.flush();
		} catch (Exception ex) {
			logger.error(ex.toString());
		} finally {
			// 关闭流
			wOutputStream.close();
		}
	}

	@Override
	public ServiceResult<String> MCS_DeleteLogFileList(List<String> wPathList) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			for (String wFilePath : wPathList) {
				// 根据路径删除记录
				MCSLogInfoDAO.getInstance().DeleteByFilePath(BaseDAO.SysAdmin, wFilePath, wErrorCode);

				File wFile = new File(wFilePath);
				if (wFile.exists())
					wFile.delete();
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<TCMMaterialChangeLog> TCM_QueryMaterialChangeLog(BMSEmployee wLoginUser, int wID) {
		ServiceResult<TCMMaterialChangeLog> wResult = new ServiceResult<TCMMaterialChangeLog>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = TCMMaterialChangeLogDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> TCM_UpdateList(BMSEmployee wLoginUser, List<TCMMaterialChangeItems> wItemList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			for (TCMMaterialChangeItems wTCMMaterialChangeItems : wItemList) {
				TCMMaterialChangeItemsDAO.getInstance().Update(wLoginUser, wTCMMaterialChangeItems, wErrorCode);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<MCSLogInfo>> MCS_QueryCatalinaLogList() {
		ServiceResult<List<MCSLogInfo>> wResult = new ServiceResult<List<MCSLogInfo>>();
		wResult.Result = new ArrayList<MCSLogInfo>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			String wDirPath = "/usr/local/share/apache-tomcat-8.5.47/logs";

			File wFile = new File(wDirPath);
			if (!wFile.isDirectory())
				return wResult;

			File[] wfss = wFile.listFiles();
			for (File wItem : wfss) {
				MCSLogInfo wSubCatalog = new MCSLogInfo();

				Calendar wTime = Calendar.getInstance();
				wTime.setTime(new Date(wItem.lastModified()));

				SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				wSubCatalog.CreateTime = wTime;
				wSubCatalog.CreateTimeStr = wSDF.format(wTime.getTime());
				wSubCatalog.FilePath = wItem.getAbsolutePath();
				wSubCatalog.FileName = wItem.getName();
				wSubCatalog.FileType = "系统日志";

				wResult.Result.add(wSubCatalog);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<Map<String, Object>> MES_UpdateMaterialCheckoutInfo(Map<String, Object> wParam) {
		ServiceResult<Map<String, Object>> wResult = new ServiceResult<Map<String, Object>>();
		wResult.Result = new HashMap<String, Object>();
		try {
			WMSCheckoutInfo wWMSCheckoutInfo = CloneTool.Clone(wParam.get("data"), WMSCheckoutInfo.class);
			logger.info(wWMSCheckoutInfo.MESPSOrderNo);

			WMSReturn wWMSReturn = new WMSReturn();
			wWMSReturn.EDISENDFLAG1 = "true";
			wWMSReturn.EDISENDMSG1 = "成功";
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String wCurrentTime = wSDF.format(Calendar.getInstance().getTime());
			wWMSReturn.EDISENDTIME1 = wCurrentTime;

			wResult.Result = CloneTool.Clone(wWMSReturn, Map.class);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());

			WMSReturn wWMSReturn = new WMSReturn();
			wWMSReturn.EDISENDFLAG1 = "false";
			wWMSReturn.EDISENDMSG1 = "失败：" + e.toString();
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String wCurrentTime = wSDF.format(Calendar.getInstance().getTime());
			wWMSReturn.EDISENDTIME1 = wCurrentTime;

			wResult.Result = CloneTool.Clone(wWMSReturn, Map.class);
		}
		return wResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<Map<String, Object>> MES_ReceiptReturn(Map<String, Object> wParam) {
		ServiceResult<Map<String, Object>> wResult = new ServiceResult<Map<String, Object>>();
		wResult.Result = new HashMap<String, Object>();
		try {
			WMSInstockBackHead wWMSInstockBackHead = CloneTool.Clone(wParam.get("data"), WMSInstockBackHead.class);
			logger.info(wWMSInstockBackHead.MESNo);

			WMSReturn wWMSReturn = new WMSReturn();
			wWMSReturn.EDISENDFLAG1 = "true";
			wWMSReturn.EDISENDMSG1 = "成功";
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String wCurrentTime = wSDF.format(Calendar.getInstance().getTime());
			wWMSReturn.EDISENDTIME1 = wCurrentTime;

			wResult.Result = CloneTool.Clone(wWMSReturn, Map.class);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());

			WMSReturn wWMSReturn = new WMSReturn();
			wWMSReturn.EDISENDFLAG1 = "false";
			wWMSReturn.EDISENDMSG1 = "失败：" + e.toString();
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String wCurrentTime = wSDF.format(Calendar.getInstance().getTime());
			wWMSReturn.EDISENDTIME1 = wCurrentTime;

			wResult.Result = CloneTool.Clone(wWMSReturn, Map.class);
		}
		return wResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<Map<String, Object>> MES_PickReturn(Map<String, Object> wParam) {
		ServiceResult<Map<String, Object>> wResult = new ServiceResult<Map<String, Object>>();
		wResult.Result = new HashMap<String, Object>();
		try {
			WMSPickBackHead wWMSPickBackHead = CloneTool.Clone(wParam.get("data"), WMSPickBackHead.class);
			logger.info(wWMSPickBackHead.MESNo);

			WMSReturn wWMSReturn = new WMSReturn();
			wWMSReturn.EDISENDFLAG1 = "true";
			wWMSReturn.EDISENDMSG1 = "成功";
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String wCurrentTime = wSDF.format(Calendar.getInstance().getTime());
			wWMSReturn.EDISENDTIME1 = wCurrentTime;

			wResult.Result = CloneTool.Clone(wWMSReturn, Map.class);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());

			WMSReturn wWMSReturn = new WMSReturn();
			wWMSReturn.EDISENDFLAG1 = "false";
			wWMSReturn.EDISENDMSG1 = "失败：" + e.toString();
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String wCurrentTime = wSDF.format(Calendar.getInstance().getTime());
			wWMSReturn.EDISENDTIME1 = wCurrentTime;

			wResult.Result = CloneTool.Clone(wWMSReturn, Map.class);
		}
		return wResult;
	}
}
