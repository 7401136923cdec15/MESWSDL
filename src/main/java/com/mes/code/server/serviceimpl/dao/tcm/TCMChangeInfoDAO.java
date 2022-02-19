package com.mes.code.server.serviceimpl.dao.tcm;

import java.text.MessageFormat;
import java.util.ArrayList;
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
import com.mes.code.server.service.po.tcm.TCMChangeInfo;
import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.dao.BaseDAO;

public class TCMChangeInfoDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(TCMChangeInfoDAO.class);

	private static TCMChangeInfoDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wTCMChangeInfo
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, TCMChangeInfo wTCMChangeInfo, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wTCMChangeInfo == null)
				return 0;

			String wSQL = "";
			if (wTCMChangeInfo.getID() <= 0) {
				wSQL = MessageFormat.format("INSERT INTO {0}.tcm_changeinfo(RouteID,PROCESSCNO,PROCESSC,"
						+ "PROCESSCUSER,PROCESSCTYPE) VALUES(:RouteID,:PROCESSCNO,:PROCESSC,:PROCESSCUSER,"
						+ ":PROCESSCTYPE);", wInstance.Result);
			} else {
				wSQL = MessageFormat.format("UPDATE {0}.tcm_changeinfo SET RouteID = :RouteID,PROCESSCNO = :PROCESSCNO,"
						+ "PROCESSC = :PROCESSC,PROCESSCUSER = :PROCESSCUSER,PROCESSCTYPE = :PROCESSCTYPE "
						+ "WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wTCMChangeInfo.ID);
			wParamMap.put("RouteID", wTCMChangeInfo.RouteID);
			wParamMap.put("PROCESSCNO", wTCMChangeInfo.PROCESSCNO);
			wParamMap.put("PROCESSC", wTCMChangeInfo.PROCESSC);
			wParamMap.put("PROCESSCUSER", wTCMChangeInfo.PROCESSCUSER);
			wParamMap.put("PROCESSCTYPE", wTCMChangeInfo.PROCESSCTYPE);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wTCMChangeInfo.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wTCMChangeInfo.setID(wResult);
			} else {
				wResult = wTCMChangeInfo.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 删除集合
	 * 
	 * @param wList
	 */
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<TCMChangeInfo> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (TCMChangeInfo wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.tcm_changeinfo WHERE ID IN({0}) ;",
					String.join(",", wIDList), wInstance.Result);
			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查单条
	 * 
	 * @return
	 */
	public TCMChangeInfo SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		TCMChangeInfo wResult = new TCMChangeInfo();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<TCMChangeInfo> wList = SelectList(wLoginUser, wID, -1, wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<TCMChangeInfo> SelectList(BMSEmployee wLoginUser, int wID, int wRouteID,
			OutResult<Integer> wErrorCode) {
		List<TCMChangeInfo> wResultList = new ArrayList<TCMChangeInfo>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.tcm_changeinfo WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wRouteID <= 0 or :wRouteID = RouteID );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wRouteID", wRouteID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				TCMChangeInfo wItem = new TCMChangeInfo();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.RouteID = StringUtils.parseInt(wReader.get("RouteID"));
				wItem.PROCESSCNO = StringUtils.parseString(wReader.get("PROCESSCNO"));
				wItem.PROCESSC = StringUtils.parseString(wReader.get("PROCESSC"));
				wItem.PROCESSCUSER = StringUtils.parseString(wReader.get("PROCESSCUSER"));
				wItem.PROCESSCTYPE = StringUtils.parseString(wReader.get("PROCESSCTYPE"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private TCMChangeInfoDAO() {
		super();
	}

	public static TCMChangeInfoDAO getInstance() {
		if (Instance == null)
			Instance = new TCMChangeInfoDAO();
		return Instance;
	}
}
