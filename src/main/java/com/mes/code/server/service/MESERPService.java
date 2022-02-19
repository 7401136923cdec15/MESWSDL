package com.mes.code.server.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.mes.code.server.service.po.ServiceResult;
import com.mes.code.server.service.po.bms.BMSEmployee;
import com.mes.code.server.service.po.mcs.MCSLogInfo;
import com.mes.code.server.service.po.tcm.TCMMaterialChangeItems;
import com.mes.code.server.service.po.tcm.TCMMaterialChangeLog;

public interface MESERPService {

	/**
	 * 车型接口
	 */
	ServiceResult<Map<String, Object>> MES_SaveProductList(Map<String, Object> wParam);

	/**
	 * 保存局段信息
	 */
	ServiceResult<Map<String, Object>> MES_SaveCustomerList(Map<String, Object> wParam);

	/**
	 * 保存物料主数据
	 */
	ServiceResult<Map<String, Object>> MES_SaveMaterialList(Map<String, Object> wParam);

	/**
	 * 创建台车BOM接口
	 */
	ServiceResult<Map<String, Object>> MES_CreatePartNoBOMList(Map<String, Object> wParam);

	/**
	 * 生产订单接口
	 */
	ServiceResult<Map<String, Object>> MES_SaveOrderList(Map<String, Object> wParam);

	/**
	 * 设备资产接口
	 */
	ServiceResult<Map<String, Object>> MES_SaveDeviceList(Map<String, Object> wParam);

	/**
	 * 查询传输日志集合
	 */
	ServiceResult<List<MCSLogInfo>> MCS_QueryLogList();

	/**
	 * 下载文件
	 */
	void MCSG_DownloadLogFileByFilePath(String wFilePath, String wFileName, HttpServletResponse response)
			throws IOException;

	/**
	 * 删除文件
	 */
	ServiceResult<String> MCS_DeleteLogFileList(List<String> wPathList);

	/**
	 * 查询单条信息详情
	 */
	ServiceResult<TCMMaterialChangeLog> TCM_QueryMaterialChangeLog(BMSEmployee wLoginUser, int wID);

	ServiceResult<Integer> TCM_UpdateList(BMSEmployee wLoginUser, List<TCMMaterialChangeItems> wItemList);

	ServiceResult<List<MCSLogInfo>> MCS_QueryCatalinaLogList();

	ServiceResult<Map<String, Object>> MES_UpdateMaterialCheckoutInfo(Map<String, Object> wParam);

	/**
	 * WMS入库回传通用接口
	 */
	ServiceResult<Map<String, Object>> MES_ReceiptReturn(Map<String, Object> wParam);

	/**
	 * WMS拣货状态回传
	 */
	ServiceResult<Map<String, Object>> MES_PickReturn(Map<String, Object> wParam);
}
