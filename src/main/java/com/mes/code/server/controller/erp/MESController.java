package com.mes.code.server.controller.erp;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mes.code.server.controller.BaseController;
import com.mes.code.server.service.MESERPService;
import com.mes.code.server.service.po.ServiceResult;
import com.mes.code.server.utils.RetCode;

@RestController
@RequestMapping("/api/MESService")
public class MESController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(MESController.class);

	@Autowired
	MESERPService wMESERPService;

	/**
	 * 保存局段信息
	 */
	@PostMapping("/MES_SaveCustomerList")
	public Object MES_SaveCustomerList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			ServiceResult<Map<String, Object>> wServiceResult = wMESERPService.MES_SaveCustomerList(wParam);

			wResult = wServiceResult.Result;
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 车型测试接口
	 */
	@PostMapping("/MES_SaveProductList")
	public Object MES_SaveProductList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			ServiceResult<Map<String, Object>> wServiceResult = wMESERPService.MES_SaveProductList(wParam);

			wResult = wServiceResult.Result;
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 物料主数据保存接口
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/MES_SaveMaterialList")
	public Object MES_SaveMaterialList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			ServiceResult<Map<String, Object>> wServiceResult = wMESERPService.MES_SaveMaterialList(wParam);

			wResult = wServiceResult.Result;
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 台车BOM创建接口
	 */
	@PostMapping("/MES_CreatePartNoBOMList")
	public Object MES_CreatePartNoBOMList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			ServiceResult<Map<String, Object>> wServiceResult = wMESERPService.MES_CreatePartNoBOMList(wParam);

			wResult = wServiceResult.Result;
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 生产订单接口
	 */
	@PostMapping("/MES_SaveOrderList")
	public Object MES_SaveOrderList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			ServiceResult<Map<String, Object>> wServiceResult = wMESERPService.MES_SaveOrderList(wParam);

			wResult = wServiceResult.Result;
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 设备资产接口
	 */
	@PostMapping("/MES_SaveDeviceList")
	public Object MES_SaveDeviceList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			ServiceResult<Map<String, Object>> wServiceResult = wMESERPService.MES_SaveDeviceList(wParam);

			wResult = wServiceResult.Result;
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 产线领料回传
	 */
	@PostMapping("/MES_UpdateMaterialCheckoutInfo")
	public Object MES_UpdateMaterialCheckoutInfo(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			ServiceResult<Map<String, Object>> wServiceResult = wMESERPService.MES_UpdateMaterialCheckoutInfo(wParam);

			wResult = wServiceResult.Result;
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 产线退料入库回传105、委外拆修入库回传300、换料申请入库回传110
	 */
	@PostMapping("/MES_ReceiptReturn")
	public Object MES_ReceiptReturn(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			ServiceResult<Map<String, Object>> wServiceResult = wMESERPService.MES_ReceiptReturn(wParam);

			wResult = wServiceResult.Result;
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 仓库整单拣货状态回传
	 */
	@PostMapping("/MES_PickReturn")
	public Object MES_PickReturn(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			ServiceResult<Map<String, Object>> wServiceResult = wMESERPService.MES_PickReturn(wParam);

			wResult = wServiceResult.Result;
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}
}
