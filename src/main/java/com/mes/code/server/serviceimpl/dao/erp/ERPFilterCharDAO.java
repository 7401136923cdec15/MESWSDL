package com.mes.code.server.serviceimpl.dao.erp;

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
import com.mes.code.server.service.po.erp.ERPFilterChar;
import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.dao.BaseDAO;

public class ERPFilterCharDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(ERPFilterCharDAO.class);

	private static ERPFilterCharDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wERPFilterChar
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, ERPFilterChar wERPFilterChar, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wERPFilterChar == null)
				return 0;

			String wSQL = "";
			if (wERPFilterChar.getID() <= 0) {
				wSQL = MessageFormat.format("INSERT INTO {0}.erp_filterchar(Text) VALUES(:Text);", wInstance.Result);
			} else {
				wSQL = MessageFormat.format("UPDATE {0}.erp_filterchar SET Text = :Text WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wERPFilterChar.ID);
			wParamMap.put("Text", wERPFilterChar.Text);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wERPFilterChar.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wERPFilterChar.setID(wResult);
			} else {
				wResult = wERPFilterChar.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<ERPFilterChar> wList,
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
			for (ERPFilterChar wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.erp_filterchar WHERE ID IN({0}) ;",
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
	public ERPFilterChar SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		ERPFilterChar wResult = new ERPFilterChar();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<ERPFilterChar> wList = SelectList(wLoginUser, wID, "", wErrorCode);
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
	public List<ERPFilterChar> SelectList(BMSEmployee wLoginUser, int wID, String wText,
			OutResult<Integer> wErrorCode) {
		List<ERPFilterChar> wResultList = new ArrayList<ERPFilterChar>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.erp_filterchar WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wText is null or :wText = '''' or :wText = Text );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wText", wText);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				ERPFilterChar wItem = new ERPFilterChar();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.Text = StringUtils.parseString(wReader.get("Text"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private ERPFilterCharDAO() {
		super();
	}

	public static ERPFilterCharDAO getInstance() {
		if (Instance == null)
			Instance = new ERPFilterCharDAO();
		return Instance;
	}
}
