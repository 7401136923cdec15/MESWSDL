package com.mes.code.server.controller.test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mes.code.server.controller.BaseController;
import com.mes.code.server.service.MESERPService;
import com.mes.code.server.service.po.OutResult;
import com.mes.code.server.service.po.ServiceResult;
import com.mes.code.server.service.po.mcs.MCSLogInfo;
import com.mes.code.server.service.utils.CloneTool;
import com.mes.code.server.service.utils.StringUtils;
import com.mes.code.server.serviceimpl.MESERPServiceImpl;
import com.mes.code.server.serviceimpl.MESServiceImpl;
import com.mes.code.server.serviceimpl.dao.BaseDAO;
import com.mes.code.server.serviceimpl.dao.mcs.MCSLogInfoDAO;
import com.mes.code.server.utils.RetCode;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2020-4-2 16:57:38
 * @LastEditTime 2020-4-2 16:57:41
 */
@RestController
@RequestMapping("/api/Test")
public class TestController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	MESERPService wMESERPService;

	/**
	 * 接口测试
	 */
	@GetMapping("/Test")
	public Object Test(HttpServletRequest request, HttpServletResponse response) {
		Object wResult = new Object();
		try {

			long startTime = System.currentTimeMillis();

			Export(response);

			long endTime = System.currentTimeMillis();

			ServiceResult<Integer> wServiceResult = new ServiceResult<Integer>();
			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "程序运行时间： " + (endTime - startTime) + "ms", null,
						wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 接口测试
	 */
	@GetMapping("/Test1")
	public Object Test1(HttpServletRequest request, HttpServletResponse response) {
		Object wResult = new Object();
		try {

			long startTime = System.currentTimeMillis();

			String wFilePath = StringUtils.parseString(request.getParameter("FilePath"));
			int wType = StringUtils.parseInt(request.getParameter("Type"));

			Export(wFilePath, wType);

			long endTime = System.currentTimeMillis();

			ServiceResult<Integer> wServiceResult = new ServiceResult<Integer>();
			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "程序运行时间： " + (endTime - startTime) + "ms", null,
						wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	public void Export(String wFilePath, int wType) {
		try {
			String INPUT = StringUtils.ReadAllText(wFilePath);
			switch (wType) {
			case 1:
				INPUT = StringUtils.ReadAllText("/data/0104/bom.xml");
				logger.info(INPUT);
				MESServiceImpl.getInstance().MES_SaveBOMList(INPUT);
				break;
			case 2:
				INPUT = StringUtils.ReadAllText("/data/0104/op.xml");
				logger.info(INPUT);
				MESServiceImpl.getInstance().MES_SaveMEOPList(INPUT);
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public void Export(HttpServletResponse response) {
		try {
//			String INPUT = StringUtils
//					.ReadAllText("C:\\Users\\Shris\\Desktop\\TCMBOM7e61eb30568a4fe39dc11142cfddcf00.xml");
//			String wMsg = MESServiceImpl.getInstance().MES_SaveBOMList(INPUT);
//			System.out.println(wMsg);

//			List<WMSCheckoutItem> wItemList = new ArrayList<WMSCheckoutItem>();
//
//			WMSCheckoutItem wItem = new WMSCheckoutItem("GQ-A603.P.CS.004", "M000000013558", 2, "000347", "6A电源线安装", 1);
//			wItemList.add(wItem);
//			wItem = new WMSCheckoutItem("GQ-A603.P.CS.005", "M000000013559", 1, "000348", "折角塞门更换", 2);
//			wItemList.add(wItem);
//
//			WMSCheckoutInfo wWMSCheckoutInfo = new WMSCheckoutInfo("MESPS001", Calendar.getInstance(), "WMSPS001",
//					"HXD3C", "C6", "CS", "0087", "下车部件检修工位", "JCJX-027", wItemList);
//			String wData = JSON.toJSONString(wWMSCheckoutInfo);

			String wData = "{" + "    \"MESPSOrderNo\": \"MESPS001\"," + "    \"WMSReadyTime\": \"2022-1-5 11:44:57\","
					+ "    \"WMSPSOrderNo\": \"WMSPS001\"," + "    \"ProductNo\": \"HXD3C\","
					+ "    \"LineName\": \"C6\"," + "    \"CustomerCode\": \"CS\"," + "    \"PartNo\": \"0087\","
					+ "    \"PartName\": \"下车部件检修工位\"," + "    \"PartCode\": \"JCJX-027\"," + "    \"ItemList\": ["
					+ "        {" + "            \"WBSNo\": \"GQ-A603.P.CS.004\","
					+ "            \"MaterialCode\": \"M000000013558\"," + "            \"FQTY\": 2,"
					+ "            \"PartPointCode\": \"000347\"," + "            \"PartPointName\": \"6A电源线安装\","
					+ "            \"RowNo\": 1" + "        }," + "        {"
					+ "            \"WBSNo\": \"GQ-A603.P.CS.005\","
					+ "            \"MaterialCode\": \"M000000013559\"," + "            \"FQTY\": 1,"
					+ "            \"PartPointCode\": \"000348\"," + "            \"PartPointName\": \"折角塞门更换\","
					+ "            \"RowNo\": 2" + "        }" + "    ]" + "}";

			String wResult = MESServiceImpl.getInstance().MES_UpdateMaterialCheckoutInfo(wData);
			System.out.println(wResult);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 保存历史记录的日志，7月5号之前的数据
	 */
	@SuppressWarnings("unused")
	private void SaveHistoryLog() {
		try {
			List<MCSLogInfo> wLogList = MESERPServiceImpl.getInstance().MCS_QueryLogList().Result;

			Calendar wCalendar = Calendar.getInstance();
			wCalendar.set(2021, 6, 5, 0, 0, 0);

			wLogList = wLogList.stream().filter(p -> p.CreateTime.compareTo(wCalendar) <= 0)
					.collect(Collectors.toList());
			for (MCSLogInfo wMCSLogInfo : wLogList) {
				MCSLogInfoDAO.getInstance().Update(BaseDAO.SysAdmin, wMCSLogInfo, new OutResult<Integer>());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取传输日志集合
	 */
	@GetMapping("/LogList")
	public Object LogList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			ServiceResult<List<MCSLogInfo>> wServiceResult = wMESERPService.MCS_QueryLogList();

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
	 * 获取Catalina日志集合
	 */
	@GetMapping("/CatalinaLogList")
	public Object CatalinaLogList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			ServiceResult<List<MCSLogInfo>> wServiceResult = wMESERPService.MCS_QueryCatalinaLogList();

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

	@GetMapping("/FileDownload")
	void FileDownload(HttpServletRequest request, HttpServletResponse response) {
		try {
			String wFilePath = StringUtils.parseString(request.getParameter("FilePath"));
			String wFileName = StringUtils.parseString(request.getParameter("FileName"));

			wMESERPService.MCSG_DownloadLogFileByFilePath(wFilePath, wFileName, response);
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	@PostMapping("/DeleteList")
	Object DeleteList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			List<String> wPathList = CloneTool.CloneArray(wParam.get("data"), String.class);

			ServiceResult<String> wServiceResult = wMESERPService.MCS_DeleteLogFileList(wPathList);

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
		} catch (Exception e) {
			wResult = GetResult(RetCode.SERVER_CODE_SUC, RetCode.SERVER_CODE_ERR_MSG);
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取获取文件详情
	 */
	@GetMapping("/FileInfo")
	public Object FileInfo(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			String wFilePath = StringUtils.parseString(request.getParameter("FilePath"));

			String wINPUT = StringUtils.ReadAllText(wFilePath);

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wINPUT);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}
}
