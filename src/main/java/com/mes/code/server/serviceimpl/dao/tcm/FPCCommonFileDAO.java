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
import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.service.po.fpc.FPCStepSOP;
import com.mes.code.server.service.po.tcm.FPCCommonFile;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.dao.BaseDAO;

public class FPCCommonFileDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(FPCCommonFileDAO.class);

	private static FPCCommonFileDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wFPCCommonFile
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, FPCCommonFile wFPCCommonFile, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.Basic, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wFPCCommonFile == null)
				return 0;

			String wSQL = "";
			if (wFPCCommonFile.getID() <= 0) {
				wSQL = MessageFormat.format("INSERT INTO {0}.fpc_commonfile(Code,FilePath,FileName,DocRev,CreateTime) "
						+ "VALUES(:Code,:FilePath,:FileName,:DocRev,now());", wInstance.Result);
			} else {
				wSQL = MessageFormat
						.format("UPDATE {0}.fpc_commonfile SET Code = :Code,FilePath = :FilePath,FileName = :FileName,"
								+ "DocRev = :DocRev,CreateTime=now() WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wFPCCommonFile.ID);
			wParamMap.put("Code", wFPCCommonFile.Code);
			wParamMap.put("FilePath", wFPCCommonFile.FilePath);
			wParamMap.put("FileName", wFPCCommonFile.FileName);
			wParamMap.put("DocRev", wFPCCommonFile.DocRev);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wFPCCommonFile.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wFPCCommonFile.setID(wResult);
			} else {
				wResult = wFPCCommonFile.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 添加或修改
	 * 
	 * @param wFPCCommonFile
	 * @return
	 */
	public int UpdateSOP(BMSEmployee wLoginUser, FPCStepSOP wFPCStepSOP, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.Basic, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wFPCStepSOP == null)
				return 0;

			String wSQL = "";
			if (wFPCStepSOP.getID() <= 0) {
				wSQL = MessageFormat
						.format("INSERT INTO {0}.`fpc_stepsop` (`RoutePartPointID`,`FileName`,`FilePath`,`FileType`,"
								+ "`SourceType`,`Active`,`CreatorID`,`EditorID`,`CreateTime`,`EditTime`) "
								+ "VALUES (:RoutePartPointID,:FileName,:FilePath,:FileType,:SourceType,"
								+ ":Active,:CreatorID,:EditorID,:CreateTime,:EditTime);", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("RoutePartPointID", wFPCStepSOP.RoutePartPointID);
			wParamMap.put("FileName", wFPCStepSOP.FileName);
			wParamMap.put("FilePath", wFPCStepSOP.FilePath);
			wParamMap.put("FileType", wFPCStepSOP.FileType);
			wParamMap.put("SourceType", wFPCStepSOP.SourceType);
			wParamMap.put("Active", wFPCStepSOP.Active);
			wParamMap.put("CreatorID", wLoginUser.ID);
			wParamMap.put("EditorID", wLoginUser.ID);
			wParamMap.put("CreateTime", Calendar.getInstance());
			wParamMap.put("EditTime", Calendar.getInstance());

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wFPCStepSOP.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wFPCStepSOP.setID(wResult);
			} else {
				wResult = wFPCStepSOP.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<FPCCommonFile> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.Basic, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (FPCCommonFile wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.fpc_commonfile WHERE ID IN({0}) ;",
					StringUtils.Join(",", wIDList), wInstance.Result);
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
	public FPCCommonFile SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		FPCCommonFile wResult = new FPCCommonFile();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.Basic, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<FPCCommonFile> wList = SelectList(wLoginUser, wID, "", null, null, wErrorCode);
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
	 * @param wEndTime
	 * @param wStartTime
	 * 
	 * @return
	 */
	public List<FPCCommonFile> SelectList(BMSEmployee wLoginUser, int wID, String wCode, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<FPCCommonFile> wResultList = new ArrayList<FPCCommonFile>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.Basic, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
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

			String wSQL = MessageFormat.format("SELECT * FROM {0}.fpc_commonfile WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= CreateTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= CreateTime) "
					+ "and ( :wCode is null or :wCode = '''' or :wCode = Code );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wCode", wCode);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				FPCCommonFile wItem = new FPCCommonFile();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.Code = StringUtils.parseString(wReader.get("Code"));
				wItem.FilePath = StringUtils.parseString(wReader.get("FilePath"));
				wItem.FileName = StringUtils.parseString(wReader.get("FileName"));
				wItem.DocRev = StringUtils.parseString(wReader.get("DocRev"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private FPCCommonFileDAO() {
		super();
	}

	public static FPCCommonFileDAO getInstance() {
		if (Instance == null)
			Instance = new FPCCommonFileDAO();
		return Instance;
	}
}
