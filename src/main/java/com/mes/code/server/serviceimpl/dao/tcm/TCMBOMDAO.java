package com.mes.code.server.serviceimpl.dao.tcm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.code.server.service.mesenum.MESDBSource;
import com.mes.code.server.service.po.OutResult;
import com.mes.code.server.service.po.ServiceResult;
import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.service.po.tcm.MESStation;
import com.mes.code.server.service.po.tcm.TCMBOM;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.dao.BaseDAO;

public class TCMBOMDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(TCMBOMDAO.class);

	private static TCMBOMDAO Instance = null;

	private TCMBOMDAO() {
		super();
	}

	public static TCMBOMDAO getInstance() {
		if (Instance == null)
			Instance = new TCMBOMDAO();
		return Instance;
	}

	/**
	 * 将xml字符串解析成工位数据
	 */
	@SuppressWarnings("unchecked")
	public List<MESStation> GetMESStationList(String wXmlStr) {
		List<MESStation> wResult = new ArrayList<MESStation>();
		try {
			Document wDocument = DocumentHelper.parseText(wXmlStr);

			List<Node> wItemNodes = wDocument.selectNodes("//ITEM");
			for (Node wNode : wItemNodes) {
				Node wMEStationIDNode = wNode.selectSingleNode("MEStationID");
				String wStationCode = wMEStationIDNode.getText();

				Node wMEStationNameCode = wNode.selectSingleNode("MEStationName");
				String wMEStationName = wMEStationNameCode.getText();

				MESStation wMESStation = new MESStation();
				wMESStation.setMEStationID(wStationCode);
				wMESStation.setMEStationName(wMEStationName);
				wResult.add(wMESStation);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取TCM标准BOM
	 */
	@SuppressWarnings("unchecked")
	public List<TCMBOM> GetTCMBOMList(String wXml) {
		List<TCMBOM> wResult = new ArrayList<TCMBOM>();
		try {
			Document wDocument = DocumentHelper.parseText(wXml);

			List<Node> wItemNodes = wDocument.selectNodes("//ITEM");
			for (Node wNode : wItemNodes) {
				TCMBOM wTCMBOM = new TCMBOM();

				wTCMBOM.METargetID = wNode.selectSingleNode("METargetID").getText();
				wTCMBOM.ZSCLX = wNode.selectSingleNode("ZSCLX").getText();
				wTCMBOM.ZCHEX = wNode.selectSingleNode("ZCHEX").getText();
				wTCMBOM.ZXIUC = wNode.selectSingleNode("ZXIUC").getText();
				wTCMBOM.ZJDXX = wNode.selectSingleNode("ZJDXX").getText();
				wTCMBOM.USR00 = wNode.selectSingleNode("USR00").getText();
				wTCMBOM.KTEXT = wNode.selectSingleNode("KTEXT").getText();
				wTCMBOM.MATNR = wNode.selectSingleNode("MATNR").getText();
				wTCMBOM.MEINS = wNode.selectSingleNode("MEINS").getText();
				wTCMBOM.MENGE = wNode.selectSingleNode("MENGE").getText();
				wTCMBOM.ZBHOH = wNode.selectSingleNode("ZBHOH").getText();
				wTCMBOM.ZZZWW = wNode.selectSingleNode("ZZZWW").getText();
				wTCMBOM.ZYCYZ = wNode.selectSingleNode("ZYCYZ").getText();
				wTCMBOM.ZCJXC = wNode.selectSingleNode("ZCJXC").getText();
				wTCMBOM.ZZZBZ = wNode.selectSingleNode("ZZZBZ").getText();
				wTCMBOM.KTTXT = wNode.selectSingleNode("KTTXT").getText();

				wResult.add(wTCMBOM);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据车型、修程、局段获取当前标准BOMID
	 */
	public int GetMSSBomID(BMSEmployee wLoginUser, int wProductID, int wLineID, int wCustomerID, int wNewBOMID,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select ID from {0}.mss_bom where IsStandard=1 and RouteID in "
							+ "(select t1.ID from {0}.fpc_route t1, {0}.mss_bom t2 "
							+ "where t1.IsStandard=1 and t1.ProductID=t2.ProductID and t2.LineID=t2.LineID "
							+ "and t1.CustomerID=t2.CustomerID and t2.ID in ({1}));",
					wInstance.Result, String.valueOf(wNewBOMID));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				return StringUtils.parseInt(wReader.get("ID"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public boolean IsVersionSame(BMSEmployee wLoginUser, int wBOMID1, int wBOMID2, OutResult<Integer> wErrorCode) {
		boolean wResult = false;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select (select name from {0}.fpc_route where id in "
					+ "(select RouteID from {0}.mss_bom where ID=:BOMID1)) V1,"
					+ "(select name from {0}.fpc_route where id in "
					+ "(select RouteID from {0}.mss_bom where ID=:BOMID2)) V2;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("BOMID1", wBOMID1);
			wParamMap.put("BOMID2", wBOMID2);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wV1 = StringUtils.parseString(wReader.get("V1"));
				String wV2 = StringUtils.parseString(wReader.get("V2"));
				if (wV1.equals(wV2)) {
					wResult = true;
					return wResult;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据车型、修程、局段找到正在维修中的订单ID集合
	 */
	public List<Integer> GetRepairingOrderIDList(BMSEmployee wLoginUser, int wProductID, int wLineID, int wCustomerID,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select ID from {0}.oms_order where ProductID=:wProductID "
							+ "and LineID=:wLineID and BureauSectionID=:wBureauSectionID and Status=4;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wProductID", wProductID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wBureauSectionID", wCustomerID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wOrderID = StringUtils.parseInt(wReader.get("ID"));
				if (wOrderID > 0) {
					wResult.add(wOrderID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单、工位、工序判断工序是否完工
	 */
	public boolean JudgeIsStepFinish(BMSEmployee wLoginUser, int wOrderID, int wPartID, int wStepID,
			OutResult<Integer> wErrorCode) {
		boolean wResult = false;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT Status FROM {0}.aps_taskstep "
							+ "where OrderID=:wOrderID and PartID=:wPartID and StepID=:wStepID and Active=1;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wStepID", wStepID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wStatus = StringUtils.parseInt(wReader.get("Status"));
				if (wStatus == 5) {
					wResult = true;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据修程、车型、局段获取最新的标准BOMID
	 */
	public int SelectNewBOMID(BMSEmployee wLoginUser, int wLineID, int wProductID, int wCustomerID,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT max(ID) ID FROM {0}.mss_bom "
					+ "where IsStandard=1 and LineID=:LineID and ProductID=:ProductID and CustomerID=:CustomerID;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("LineID", wLineID);
			wParamMap.put("ProductID", wProductID);
			wParamMap.put("CustomerID", wCustomerID);

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
}
