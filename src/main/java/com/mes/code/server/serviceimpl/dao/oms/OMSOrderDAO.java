package com.mes.code.server.serviceimpl.dao.oms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mes.code.server.service.mesenum.MESDBSource;
import com.mes.code.server.service.mesenum.MESException;
import com.mes.code.server.service.po.OutResult;
import com.mes.code.server.service.po.ServiceResult;
import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.service.po.dms.DMSDeviceLedger;
import com.mes.code.server.service.po.oms.OMSOrder;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.dao.BaseDAO;
import com.mes.code.server.serviceimpl.utils.WsdlConstants;

public class OMSOrderDAO extends BaseDAO {
	private static Logger logger = LoggerFactory.getLogger(OMSOrderDAO.class);

	private static OMSOrderDAO Instance = null;

	public static OMSOrderDAO getInstance() {
		if (Instance == null)
			Instance = new OMSOrderDAO();
		return Instance;
	}

	public long Update(BMSEmployee wLoginUser, OMSOrder wOMSOrder, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			if (wOMSOrder == null) {
				return 0L;
			}
			String wSQL = "";
			if (wOMSOrder.getID() <= 0) {
				wSQL = StringUtils.Format("INSERT INTO {0}.oms_order(CommandID,ERPID,OrderNo,LineID,"
						+ "ProductID,BureauSectionID,PartNo,BOMNo,Priority,Status,PlanReceiveDate,"
						+ "RealReceiveDate,PlanFinishDate,RealStartDate,RealFinishDate,RealSendDate,"
						+ "Remark,CreateID,CreateTime,EditID,EditTime,AuditorID,AuditTime,Active,RouteID,TelegraphTime,TelegraphRealTime) "
						+ "VALUES(:CommandID,:ERPID,:OrderNo,:LineID,:ProductID,:BureauSectionID,:PartNo,"
						+ ":BOMNo,:Priority,:Status,:PlanReceiveDate,:RealReceiveDate,:PlanFinishDate,"
						+ ":RealStartDate,:RealFinishDate,:RealSendDate,:Remark,:CreateID,:CreateTime,"
						+ ":EditID,:EditTime,:AuditorID,:AuditTime,:Active,:RouteID,:TelegraphTime,:TelegraphRealTime);",
						new Object[] { wInstance.Result });
//				wOMSOrder.OrderNo = SelectSubOrderNo(wLoginUser, wOMSOrder.CommandID, wErrorCode);
			} else {
				wSQL = StringUtils.Format(
						"UPDATE {0}.oms_order SET CommandID=:CommandID,ERPID = :ERPID,OrderNo = :OrderNo,"
								+ "LineID = :LineID,ProductID = :ProductID,BureauSectionID = :BureauSectionID,"
								+ "PartNo = :PartNo,BOMNo = :BOMNo,Priority = :Priority,Status = :Status,"
								+ "PlanReceiveDate = :PlanReceiveDate,RealReceiveDate = :RealReceiveDate,"
								+ "PlanFinishDate = :PlanFinishDate,RealStartDate = :RealStartDate,"
								+ "RealFinishDate = :RealFinishDate,RealSendDate = :RealSendDate,"
								+ "Remark = :Remark,CreateID = :CreateID,CreateTime = :CreateTime,"
								+ "EditID = :EditID,EditTime = :EditTime,AuditorID = :AuditorID,"
								+ "AuditTime = :AuditTime,Active = :Active,RouteID=:RouteID,TelegraphTime=:TelegraphTime,TelegraphRealTime=:TelegraphRealTime WHERE ID = :ID;",
						new Object[] {

								wInstance.Result });
			}
			wSQL = DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("ID", Integer.valueOf(wOMSOrder.ID));
			wParamMap.put("CommandID", Integer.valueOf(wOMSOrder.CommandID));
			wParamMap.put("ERPID", Integer.valueOf(wOMSOrder.ERPID));
			wParamMap.put("OrderNo", wOMSOrder.OrderNo);
			wParamMap.put("LineID", Integer.valueOf(wOMSOrder.LineID));
			wParamMap.put("ProductID", Integer.valueOf(wOMSOrder.ProductID));
			wParamMap.put("BureauSectionID", Integer.valueOf(wOMSOrder.BureauSectionID));
			wParamMap.put("PartNo", wOMSOrder.PartNo);
			wParamMap.put("BOMNo", wOMSOrder.BOMNo);
			wParamMap.put("Priority", Integer.valueOf(wOMSOrder.Priority));
			wParamMap.put("Status", Integer.valueOf(wOMSOrder.Status));
			wParamMap.put("PlanReceiveDate", wOMSOrder.PlanReceiveDate);
			wParamMap.put("RealReceiveDate", wOMSOrder.RealReceiveDate);
			wParamMap.put("PlanFinishDate", wOMSOrder.PlanFinishDate);
			wParamMap.put("RealStartDate", wOMSOrder.RealStartDate);
			wParamMap.put("RealFinishDate", wOMSOrder.RealFinishDate);
			wParamMap.put("RealSendDate", wOMSOrder.RealSendDate);
			wParamMap.put("Remark", wOMSOrder.Remark);
			wParamMap.put("CreateID", Integer.valueOf(wOMSOrder.CreateID));
			wParamMap.put("CreateTime", wOMSOrder.CreateTime);
			wParamMap.put("EditID", Integer.valueOf(wOMSOrder.EditID));
			wParamMap.put("EditTime", wOMSOrder.EditTime);
			wParamMap.put("AuditorID", Integer.valueOf(wOMSOrder.AuditorID));
			wParamMap.put("AuditTime", wOMSOrder.AuditTime);
			wParamMap.put("Active", Integer.valueOf(wOMSOrder.Active));
			wParamMap.put("RouteID", Integer.valueOf(wOMSOrder.RouteID));
			wParamMap.put("TelegraphTime", wOMSOrder.TelegraphTime);
			wParamMap.put("TelegraphRealTime", wOMSOrder.TelegraphRealTime);

			GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(wParamMap);

			this.nameJdbcTemplate.update(wSQL, (SqlParameterSource) mapSqlParameterSource,
					(KeyHolder) generatedKeyHolder);

			if (wOMSOrder.getID() <= 0) {
				wResult = generatedKeyHolder.getKey().intValue();
				wOMSOrder.setID(wResult);
			} else {
				wResult = wOMSOrder.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	public void DeleteList(BMSEmployee wLoginUser, List<OMSOrder> wList, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return;
			}

			if (wList == null || wList.size() <= 0) {
				return;
			}
			List<String> wIDList = new ArrayList<>();
			for (OMSOrder wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete {1}.from oms_order WHERE ID IN({0}) ;", String.join(",", wIDList),
					wErrorCode);
			ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
	}

	public OMSOrder SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		OMSOrder wResult = new OMSOrder();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 1, 1);

			List<OMSOrder> wList = SelectList(wLoginUser, wID, -1, "", -1, -1, -1, "", "", -1, null, wBaseTime,
					wBaseTime, wBaseTime, wBaseTime, wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);
		} catch (Exception e) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(e.toString());
		}
		return wResult;
	}

	public List<OMSOrder> SelectList(BMSEmployee wLoginUser, int wID, int wCommandID, String wOrderNo, int wLineID,
			int wProductID, int wBureauSectionID, String wPartNo, String wBOMNo, int wActive,
			List<Integer> wStateIDList, Calendar wPreStartTime, Calendar wPreEndTime, Calendar wRelStartTime,
			Calendar wRelEndTime, OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<>();
			}
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 1, 1);
			if (wPreStartTime == null || wPreStartTime.compareTo(wBaseTime) < 0)
				wPreStartTime = wBaseTime;
			if (wPreEndTime == null || wPreEndTime.compareTo(wBaseTime) < 0)
				wPreEndTime = wBaseTime;
			if (wPreStartTime.compareTo(wPreEndTime) > 0) {
				return wResultList;
			}
			if (wRelStartTime == null || wRelStartTime.compareTo(wBaseTime) < 0)
				wRelStartTime = wBaseTime;
			if (wRelEndTime == null || wRelEndTime.compareTo(wBaseTime) < 0)
				wRelEndTime = wBaseTime;
			if (wRelStartTime.compareTo(wRelEndTime) > 0) {
				return wResultList;
			}
			String wSQL = StringUtils.Format("SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,"
					+ "t2.FactoryID,t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual "
					+ "FROM {0}.oms_order t1 left join {0}.oms_command t2 on t1.CommandID=t2.ID "
					+ "WHERE  1=1  and ( :wID <= 0 or :wID = t1.ID ) "
					+ "and ( :wCommandID <= 0 or :wCommandID = t1.CommandID ) "
					+ "and ( :wOrderNo is null or :wOrderNo = '''' or :wOrderNo = t1.OrderNo ) "
					+ "and ( :wLineID <= 0 or :wLineID = t1.LineID ) "
					+ "and ( :wProductID <= 0 or :wProductID = t1.ProductID ) "
					+ "and ( :wBureauSectionID <= 0 or :wBureauSectionID = t1.BureauSectionID ) "
					+ "and ( :wPartNo is null or :wPartNo = '''' or :wPartNo = t1.PartNo ) "
					+ "and ( :wBOMNo is null or :wBOMNo = '''' or :wBOMNo = t1.BOMNo ) "
					+ "and ( :wStatus is null or :wStatus = '''' or t1.Status in ({1})) "
					+ "and ( :wPreStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wPreStartTime <= t1.PlanReceiveDate) "
					+ "and ( :wPreEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wPreEndTime >= t1.PlanReceiveDate) "
					+ "and ( :wRelStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wRelStartTime <= t1.RealFinishDate) "
					+ "and ( :wRelEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wRelEndTime >= t1.RealReceiveDate) "
					+ "and ( :wActive <= 0 or :wActive = t1.Active );",
					new Object[] { wInstance.Result,
							(wStateIDList.size() > 0) ? StringUtils.Join(",", wStateIDList) : "0" });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wID", Integer.valueOf(wID));
			wParamMap.put("wCommandID", Integer.valueOf(wCommandID));
			wParamMap.put("wOrderNo", wOrderNo);
			wParamMap.put("wLineID", Integer.valueOf(wLineID));
			wParamMap.put("wProductID", Integer.valueOf(wProductID));
			wParamMap.put("wBureauSectionID", Integer.valueOf(wBureauSectionID));
			wParamMap.put("wPartNo", wPartNo);
			wParamMap.put("wBOMNo", wBOMNo);
			wParamMap.put("wActive", Integer.valueOf(wActive));
			wParamMap.put("wPreStartTime", wPreStartTime);
			wParamMap.put("wPreEndTime", wPreEndTime);
			wParamMap.put("wRelStartTime", wRelStartTime);
			wParamMap.put("wRelEndTime", wRelEndTime);
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResultList, wQueryResult, wErrorCode);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<OMSOrder> SelectListByIDList(BMSEmployee wLoginUser, List<Integer> wIDList,
			OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			if (wIDList == null || wIDList.size() <= 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,t2.FactoryID,"
							+ "t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual FROM {0}.oms_order t1 left join {0}.oms_command t2 "
							+ "on t1.CommandID=t2.ID WHERE 1=1  and ( :wIDs is null or :wIDs = '''' or t1.ID in ({1}));",
					new Object[] {

							wInstance.Result, (wIDList.size() > 0) ? StringUtils.Join(",", wIDList) : "0" });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wIDs", StringUtils.Join(",", wIDList));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResultList, wQueryResult, wErrorCode);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<OMSOrder> SelectFinishListByTime(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}

			String wSQL = StringUtils.Format("SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,"
					+ "t2.FactoryID,t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual "
					+ "FROM {0}.oms_order t1 left join {0}.oms_command t2 on t1.CommandID=t2.ID "
					+ "WHERE 1=1  and :wStartTime <= t1.RealFinishDate and t1.RealFinishDate <= :wEndTime "
					+ " and t1.Status in(5,6,7,8);", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResultList, wQueryResult, wErrorCode);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<OMSOrder> SelectList_RF(BMSEmployee wLoginUser, int wCustomerID, int wLineID, int wProductID,
			String wPartNo, int wActive, Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 1, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0) {
				wEndTime = wBaseTime;
			}
			if (wEndTime.compareTo(wStartTime) < 0) {
				return wResultList;
			}
			String wSQL = StringUtils
					.Format("SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,t2.FactoryID,"
							+ "t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual "
							+ "FROM {0}.oms_order t1 left join {0}.oms_command t2 on t1.CommandID=t2.ID  "
							+ "WHERE 1=1 and ( :wCustomerID <= 0 or :wCustomerID = t1.BureauSectionID ) "
							+ "and ( :wLineID <= 0 or :wLineID = t1.LineID ) "
							+ "and ( :wProductID <= 0 or :wProductID = t1.ProductID ) "
							+ "and ( :wPartNo is null or :wPartNo = '''' or :wPartNo = t1.PartNo ) "
							+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') "
							+ "or :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') "
							+ "or (:wStartTime <= t1.PlanReceiveDate and t1.PlanReceiveDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealReceiveDate and t1.RealReceiveDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.PlanFinishDate and t1.PlanFinishDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealStartDate and t1.RealStartDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealFinishDate and t1.RealFinishDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealSendDate and t1.RealSendDate<=:wEndTime) ) "
							+ "and ( :wActive <= 0 or :wActive = t1.Active );", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wCustomerID", Integer.valueOf(wCustomerID));
			wParamMap.put("wLineID", Integer.valueOf(wLineID));
			wParamMap.put("wProductID", Integer.valueOf(wProductID));
			wParamMap.put("wPartNo", wPartNo);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wActive", Integer.valueOf(wActive));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResultList, wQueryResult, wErrorCode);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<OMSOrder> ConditionAll(BMSEmployee wLoginUser, int wProductID, int wLine, int wCustomerID,
			String wWBSNo, Calendar wStartTime, Calendar wEndTime, int wStatus, OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 1, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0) {
				wEndTime = wBaseTime;
			}
			if (wEndTime.compareTo(wStartTime) < 0) {
				return wResult;
			}
			if (StringUtils.isEmpty(wWBSNo)) {
				wWBSNo = "";
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,t2.FactoryID,"
							+ "t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual FROM {0}.oms_order t1 , {0}.oms_command t2 "
							+ "where t1.CommandID=t2.ID and ( :wCustomerID <= 0 or :wCustomerID = t2.CustomerID ) "
							+ "and ( :wLineID <= 0 or :wLineID = t1.LineID ) "
							+ "and ( :wStatus <= 0 or :wStatus = t1.Status ) "
							+ "and ( :wProductID <= 0 or :wProductID = t1.ProductID ) "
							+ "and ( :wWBSNo is null or :wWBSNo = '''' or t2.WBSNo like ''%{1}%'') "
							+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') "
							+ "or :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') "
							+ "or (:wStartTime <= t1.PlanReceiveDate and t1.PlanReceiveDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealReceiveDate and t1.RealReceiveDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.PlanFinishDate and t1.PlanFinishDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealStartDate and t1.RealStartDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealFinishDate and t1.RealFinishDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealSendDate and t1.RealSendDate<=:wEndTime) );",
					wInstance.Result, wWBSNo);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wProductID", Integer.valueOf(wProductID));
			wParamMap.put("wLineID", Integer.valueOf(wLine));
			wParamMap.put("wCustomerID", Integer.valueOf(wCustomerID));
			wParamMap.put("wWBSNo", wWBSNo);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wStatus", wStatus);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResult, wQueryResult, wErrorCode);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public String SelectSubOrderNo(BMSEmployee wLoginUser, int wCommandID, OutResult<Integer> wErrorCode) {
		String wResult = "";
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = StringUtils
					.Format("SELECT (select WBSNo from {0}.oms_command where ID={1}) as WBSNo, COUNT(*)+1 as MaxID"
							+ " FROM {0}.oms_order;", new Object[] { wInstance.Result, wCommandID });

			Map<String, Object> wParamMap = new HashMap<>();

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			int wMaxID = 0;
			String wWBSNo = "";
			for (Map<String, Object> wMap : wQueryResult) {
				if (wMap.containsKey("MaxID")) {
					wMaxID = StringUtils.parseInt(wMap.get("MaxID")).intValue();
				}
				if (wMap.containsKey("MaxID")) {
					wWBSNo = StringUtils.parseString(wMap.get("WBSNo"));
				}
			}

			if (wMaxID > 0 && StringUtils.isNotEmpty(wWBSNo)) {
				wResult = StringUtils.Format("{0}.{1}", wWBSNo, String.format("%03d", wMaxID));
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据季度开始时间和结束时间和修程获取完成数
	 * 
	 * @param wLoginUser
	 * @param wQStart    季度开始时刻
	 * @param wQEnd      季度结束时刻
	 * @param wLineID    修程
	 * @param wErrorCode 错误码
	 * @return 订单完成数
	 */
	public int SelectCountByQuarter(BMSEmployee wLoginUser, Calendar wQStart, Calendar wQEnd, int wLineID,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select count(*) as Number From {0}.oms_order "
					+ "where :wStartTime <= RealFinishDate and RealFinishDate <= :wEndTime "
					+ "and LineID = :wLineID and Status in(5,6,7,8);", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wStartTime", wQStart);
			wParamMap.put("wEndTime", wQEnd);
			wParamMap.put("wLineID", wLineID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				Integer wNumber = StringUtils.parseInt(wReader.get("Number"));
				if (wNumber > 0) {
					return wNumber;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public String CreateNo(int wNumber) {
		String wResult = "";
		try {
			wResult = StringUtils.Format("PO-{0}",
					new Object[] { String.format("%07d", new Object[] { Integer.valueOf(wNumber) }) });
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private void SetVaue(BMSEmployee wLoginUser, List<OMSOrder> wResultList, List<Map<String, Object>> wQueryResult,
			OutResult<Integer> wErrorCode) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				OMSOrder wItem = new OMSOrder();

				wItem.ID = StringUtils.parseInt(wReader.get("ID")).intValue();
				wItem.CommandID = StringUtils.parseInt(wReader.get("CommandID")).intValue();
				wItem.ERPID = StringUtils.parseInt(wReader.get("ERPID")).intValue();
				wItem.OrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID")).intValue();
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID")).intValue();
				wItem.BureauSectionID = StringUtils.parseInt(wReader.get("BureauSectionID")).intValue();
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.BOMNo = StringUtils.parseString(wReader.get("BOMNo"));
				wItem.Priority = StringUtils.parseInt(wReader.get("Priority")).intValue();
				wItem.Status = StringUtils.parseInt(wReader.get("Status")).intValue();
				wItem.PlanReceiveDate = StringUtils.parseCalendar(wReader.get("PlanReceiveDate"));
				wItem.RealReceiveDate = StringUtils.parseCalendar(wReader.get("RealReceiveDate"));
				wItem.PlanFinishDate = StringUtils.parseCalendar(wReader.get("PlanFinishDate"));
				wItem.RealStartDate = StringUtils.parseCalendar(wReader.get("RealStartDate"));
				wItem.RealFinishDate = StringUtils.parseCalendar(wReader.get("RealFinishDate"));
				wItem.RealSendDate = StringUtils.parseCalendar(wReader.get("RealSendDate"));
				wItem.Remark = StringUtils.parseString(wReader.get("Remark"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID")).intValue();
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EditID = StringUtils.parseInt(wReader.get("EditID")).intValue();
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.AuditorID = StringUtils.parseInt(wReader.get("AuditorID")).intValue();
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active")).intValue();
				wItem.RouteID = StringUtils.parseInt(wReader.get("RouteID")).intValue();
				wItem.TelegraphTime = StringUtils.parseCalendar(wReader.get("TelegraphTime"));
				wItem.TelegraphRealTime = StringUtils.parseCalendar(wReader.get("TelegraphRealTime"));

				wItem.LineName = WsdlConstants.GetFMCLineName(wItem.LineID);
				wItem.ProductNo = (WsdlConstants.GetFPCProduct(wItem.ProductID) == null) ? ""
						: (WsdlConstants.GetFPCProduct(wItem.ProductID)).ProductNo;
				wItem.BureauSection = WsdlConstants.GetCRMCustomerName(wItem.BureauSectionID);

				wItem.WBSNo = StringUtils.parseString(wReader.get("WBSNo"));
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID")).intValue();
				wItem.Customer = WsdlConstants.GetCRMCustomerName(wItem.CustomerID);
				wItem.ContactCode = StringUtils.parseString(wReader.get("ContactCode"));
				wItem.No = StringUtils.parseString(wReader.get("No"));
				wItem.LinkManID = StringUtils.parseInt(wReader.get("LinkManID")).intValue();
				wItem.FactoryID = StringUtils.parseInt(wReader.get("FactoryID")).intValue();
				wItem.BusinessUnitID = StringUtils.parseInt(wReader.get("BusinessUnitID")).intValue();
				wItem.FQTYPlan = StringUtils.parseInt(wReader.get("FQTYPlan")).intValue();
				wItem.FQTYActual = StringUtils.parseInt(wReader.get("FQTYActual")).intValue();

				if (wItem.RouteID == 0
						&& (WsdlConstants.GetFPCRoute(wItem.ProductID, wItem.LineID, wItem.CustomerID).ID > 0)) {
					wItem.RouteID = WsdlConstants.GetFPCRoute(wItem.ProductID, wItem.LineID, wItem.CustomerID).ID;
					Update(wLoginUser, wItem, wErrorCode);
				}

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public void Active(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return;
			}

			if (wIDList == null || wIDList.size() <= 0)
				return;
			for (Integer wItem : wIDList) {
				OMSOrder wOMSOrder = SelectByID(wLoginUser, wItem.intValue(), wErrorCode);
				if (wOMSOrder == null || wOMSOrder.ID <= 0) {
					continue;
				}
				if (wActive == 2 && wOMSOrder.Active != 1) {
					wErrorCode.set(Integer.valueOf(MESException.Logic.getValue()));
					return;
				}
				wOMSOrder.Active = wActive;
				long wID = Update(wLoginUser, wOMSOrder, wErrorCode);
				if (wID <= 0L)
					break;
			}
		} catch (Exception e) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(e.toString());
		}
	}

	public List<OMSOrder> SelectList(BMSEmployee wLoginUser, List<Integer> wCustomerList, List<Integer> wLineList,
			Calendar wStartTime, Calendar wEndTime, List<Integer> wStatusList, List<Integer> wProductList,
			String wPartNo, List<Integer> wActiveList, OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			if (wStatusList == null) {
				wStatusList = new ArrayList<>();
			}
			if (wCustomerList == null) {
				wCustomerList = new ArrayList<Integer>();
			}
			if (wLineList == null) {
				wLineList = new ArrayList<Integer>();
			}
			if (wActiveList == null) {
				wActiveList = new ArrayList<Integer>();
			}
			if (wProductList == null) {
				wProductList = new ArrayList<Integer>();
			}
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) > 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,"
					+ "t2.FactoryID,t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual "
					+ "FROM {0}.oms_order t1 left join {0}.oms_command t2 on t1.CommandID=t2.ID " + "WHERE  1=1 "
					+ "and ( :wPartNo is null or :wPartNo = '''' or t1.PartNo like ''%{1}%'' ) "
					+ "and ( :wLine is null or :wLine = '''' or t1.LineID in ({2})) "
					+ "and ( :wProduct is null or :wProduct = '''' or t1.ProductID in ({3})) "
					+ "and ( :wCustomer is null or :wCustomer = '''' or t2.CustomerID in ({4})) "
					+ "and ( :wActive is null or :wActive = '''' or t1.Active in ({5})) "
					+ "and ( :wStatus is null or :wStatus = '''' or t1.Status in ({6})) "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= t1.RealReceiveDate) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= t1.RealReceiveDate);",
					wInstance.Result, wPartNo, (wLineList.size() > 0) ? StringUtils.Join(",", wLineList) : "0",
					(wProductList.size() > 0) ? StringUtils.Join(",", wProductList) : "0",
					(wCustomerList.size() > 0) ? StringUtils.Join(",", wCustomerList) : "0",
					(wActiveList.size() > 0) ? StringUtils.Join(",", wActiveList) : "0",
					(wStatusList.size() > 0) ? StringUtils.Join(",", wStatusList) : "0");
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wPartNo", wPartNo);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wStatus", StringUtils.Join(",", wStatusList));
			wParamMap.put("wCustomer", StringUtils.Join(",", wCustomerList));
			wParamMap.put("wLine", StringUtils.Join(",", wLineList));
			wParamMap.put("wProduct", StringUtils.Join(",", wProductList));
			wParamMap.put("wActive", StringUtils.Join(",", wActiveList));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResultList, wQueryResult, wErrorCode);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 获取所有的在厂线上的订单ID集合，状态为“已进场”的。
	 * 
	 * @param wPartNoList
	 * @param wErrorCode
	 */
	public List<Integer> OMS_QueryInPlantCarList(BMSEmployee wLoginUser, List<String> wPartNoList,
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
					"SELECT ID FROM {0}.oms_order where " + "PartNo in (''{1}'') and Status=3;", wInstance.Result,
					StringUtils.Join("','", wPartNoList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

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
	 * 获取所有的在厂线上的车号列表
	 * 
	 * @param wErrorCode
	 */
	public List<String> OMS_QueryPartNoListInPlant(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<String> wResult = new ArrayList<String>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.PartNo FROM {0}.fmc_workspace t1,{1}.lfs_storehouse t2 "
							+ "where t1.ParentID=t2.ID and t2.Type=2 and t1.PartNo != '''';",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				if (StringUtils.isNotEmpty(wPartNo)) {
					wResult.add(wPartNo);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 判断设备号是否存在
	 */
	public DMSDeviceLedger ExistDevice(BMSEmployee wLoginUser, String wDeviceNo, OutResult<Integer> wErrorCode) {
		DMSDeviceLedger wResult = new DMSDeviceLedger();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.DMS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.device_ledger where DeviceNo =:DeviceNo;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("DeviceNo", wDeviceNo);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.ID = StringUtils.parseLong(wReader.get("ID"));
				wResult.Name = StringUtils.parseString(wReader.get("Name"));
				wResult.ApplyID = StringUtils.parseLong(wReader.get("ApplyID"));
				wResult.DeviceNo = StringUtils.parseString(wReader.get("DeviceNo"));
				wResult.ModelID = StringUtils.parseLong(wReader.get("ModelID"));
				wResult.AssetID = StringUtils.parseInt(wReader.get("AssetID"));
				wResult.NetValue = StringUtils.parseDouble(wReader.get("NetValue"));
				wResult.DeviceLife = StringUtils.parseDouble(wReader.get("DeviceLife"));
				wResult.ScrapValue = StringUtils.parseDouble(wReader.get("ScrapValue"));
				wResult.LimitCount = StringUtils.parseLong(wReader.get("LimitCount"));
				wResult.Status = StringUtils.parseInt(wReader.get("Status"));
				wResult.BusinessUnitID = StringUtils.parseInt(wReader.get("BusinessUnitID"));
				wResult.BaseID = StringUtils.parseInt(wReader.get("BaseID"));
				wResult.FactoryID = StringUtils.parseInt(wReader.get("FactoryID"));
				wResult.WorkShopID = StringUtils.parseInt(wReader.get("WorkShopID"));
				wResult.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wResult.PositionID = StringUtils.parseInt(wReader.get("PositionID"));
				wResult.OperatorID = StringUtils.parseLong(wReader.get("OperatorID"));
				wResult.OperatorTime = StringUtils.parseCalendar(wReader.get("OperatorTime"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

}
