package com.mes.code.server.controller.tcm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mes.code.server.controller.BaseController;
import com.mes.code.server.service.FMCService;
import com.mes.code.server.service.MESERPService;
import com.mes.code.server.service.po.ServiceResult;
import com.mes.code.server.service.po.tcm.TCMMaterialChangeItems;
import com.mes.code.server.service.po.tcm.TCMMaterialChangeLog;
import com.mes.code.server.service.utils.CloneTool;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.dao.BaseDAO;
import com.mes.code.server.utils.RetCode;

@RestController
@RequestMapping("/api/TCMMaterialChangeLog")
public class TCMMaterialChangeLogController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(TCMMaterialChangeLogController.class);

	@Autowired
	MESERPService wMESERPService;

	@Autowired
	FMCService wFMCService;

	/**
	 * 根据ID获取详情
	 */
	@GetMapping("/Info")
	public Object Info(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			// 获取参数
			int wID = StringUtils.parseInt(request.getParameter("ID"));

			ServiceResult<TCMMaterialChangeLog> wServiceResult = wMESERPService
					.TCM_QueryMaterialChangeLog(BaseDAO.SysAdmin, wID);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 批量保存处理意见
	 */
	@PostMapping("/UpdateList")
	public Object UpdateList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			// 获取参数
			List<TCMMaterialChangeItems> wItemList = CloneTool.CloneArray(wParam.get("data"),
					TCMMaterialChangeItems.class);

			ServiceResult<Integer> wServiceResult = wMESERPService.TCM_UpdateList(BaseDAO.SysAdmin, wItemList);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 工艺变更-手动
	 */
	@GetMapping("/TechChange")
	public Object TechChange(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			// 获取参数
			int wNewBOMID = StringUtils.parseInt(request.getParameter("NewBOMID"));

			ServiceResult<Integer> wServiceResult = wFMCService.FMC_TechChange(BaseDAO.SysAdmin, wNewBOMID);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}
}
