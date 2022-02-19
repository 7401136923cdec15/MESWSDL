package com.mes.code.server.serviceimpl.dao.tcm;

import java.text.MessageFormat;
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
import com.mes.code.server.service.po.tcm.TCMVerification;
import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.dao.BaseDAO;
import com.mes.code.server.utils.MD5Util;

public class TCMVerificationDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(TCMVerificationDAO.class);

	private static TCMVerificationDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wTCMVerification
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, TCMVerification wTCMVerification, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wTCMVerification == null)
				return 0;

			String wSQL = "";
			if (wTCMVerification.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.tcm_verification(CheckCode,CreateTime,Status) VALUES(:CheckCode,:CreateTime,:Status);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.tcm_verification SET CheckCode = :CheckCode,CreateTime = :CreateTime,Status = :Status WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wTCMVerification.ID);
			wParamMap.put("CheckCode", wTCMVerification.CheckCode);
			wParamMap.put("CreateTime", wTCMVerification.CreateTime);
			wParamMap.put("Status", wTCMVerification.Status);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wTCMVerification.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wTCMVerification.setID(wResult);
			} else {
				wResult = wTCMVerification.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<TCMVerification> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (TCMVerification wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.tcm_verification WHERE ID IN({0}) ;",
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
	public TCMVerification SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		TCMVerification wResult = new TCMVerification();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<TCMVerification> wList = SelectList(wLoginUser, wID, "", -1, null, null, wErrorCode);
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
	public List<TCMVerification> SelectList(BMSEmployee wLoginUser, int wID, String wCheckCode, int wStatus,
			Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<TCMVerification> wResultList = new ArrayList<TCMVerification>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.tcm_verification WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) "
					+ "and ( :wCheckCode is null or :wCheckCode = '''' or :wCheckCode = CheckCode ) "
					+ "and ( :wStartTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wStartTime <=  CreateTime ) "
					+ "and ( :wEndTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wEndTime >=  CreateTime ) "
					+ "and ( :wStatus <= 0 or :wStatus = Status );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wCheckCode", wCheckCode);
			wParamMap.put("wStatus", wStatus);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				TCMVerification wItem = new TCMVerification();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.CheckCode = StringUtils.parseString(wReader.get("CheckCode"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 文本校验
	 */
	public String Check(BMSEmployee wLoginUser, String wContent, OutResult<Integer> wNewIDResult) {
		String wResult = "";
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			Calendar wStartTime = Calendar.getInstance();
			wStartTime.add(Calendar.MINUTE, -30);
			Calendar wEndTime = Calendar.getInstance();

			String wCheckCode = MD5Util.getMD5String(wContent);

			List<TCMVerification> wList = SelectList(wLoginUser, -1, wCheckCode, -1, wStartTime, wEndTime, wErrorCode);

			if (wList.size() > 0) {
				wResult = "前面的指令正在处理!";
			} else {
				TCMVerification wTCMVerification = new TCMVerification(0, wCheckCode, Calendar.getInstance(), 1);
				wNewIDResult.Result = Update(wLoginUser, wTCMVerification, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private TCMVerificationDAO() {
		super();
	}

	public static TCMVerificationDAO getInstance() {
		if (Instance == null)
			Instance = new TCMVerificationDAO();
		return Instance;
	}
}
