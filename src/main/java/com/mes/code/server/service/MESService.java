package com.mes.code.server.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2020-11-5 16:22:53
 * @LastEditTime 2020-11-5 16:23:00
 *
 */
@WebService(targetNamespace = "http://service.server.code.mes.com", serviceName = "MESService", endpointInterface = "com.mes.code.server.service.MESService")
public interface MESService {

	/**
	 * 3.1 工位信息传递（TCM->MES）
	 */
	@WebMethod(action = "MES_SaveStationList")
	@WebResult(name = "MES_SaveStationList", targetNamespace = "http://service.server.code.mes.com")
	public String MES_SaveStationList(@WebParam(name = "DATA") String DATA);

	/**
	 * 3.2 标准BOM集成（TCM->MES）
	 */
	@WebMethod(action = "MES_SaveBOMList")
	@WebResult(name = "MES_SaveBOMList", targetNamespace = "http://service.server.code.mes.com")
	public String MES_SaveBOMList(@WebParam(name = "INPUT") String INPUT);

	/**
	 * 3.3 工艺BOP集成（TCM->MES）
	 */
	@WebMethod(action = "MES_SaveBOPList")
	@WebResult(name = "MES_SaveBOPList", targetNamespace = "http://service.server.code.mes.com")
	public String MES_SaveBOPList(@WebParam(name = "INPUT") String INPUT);

	/**
	 * 3.4 工艺文档集成（TCM->MES）
	 */
	@WebMethod(action = "MES_SaveDOCList")
	@WebResult(name = "MES_SaveDOCList", targetNamespace = "http://service.server.code.mes.com")
	public String MES_SaveDOCList(@WebParam(name = "INPUT") String INPUT);

	/**
	 * 1.1 车型集成（ERP->MES）
	 */
	@WebMethod(action = "MES_SaveProductList")
	@WebResult(name = "MES_SaveProductList", targetNamespace = "http://service.server.code.mes.com")
	public String MES_SaveProductList(@WebParam(name = "INPUT") String INPUT);

	/**
	 * 1.1 工序变更（TCM->MES）
	 */
	@WebMethod(action = "MES_SaveMEOPList")
	@WebResult(name = "MES_SaveMEOPList", targetNamespace = "http://service.server.code.mes.com")
	public String MES_SaveMEOPList(@WebParam(name = "INPUT") String INPUT);

	/**
	 * 1.1 测试接口
	 */
	@WebMethod(action = "MES_Test")
	@WebResult(name = "MES_Test", targetNamespace = "http://service.server.code.mes.com")
	public String MES_Test(@WebParam(name = "INPUT") String INPUT);

	// WMS相关
	/**
	 * 1.1 WMS配送班点击送达后，WMS将产线领料出库信息回传MES （WMS->MES）
	 */
	@WebMethod(action = "MES_UpdateMaterialCheckoutInfo")
	@WebResult(name = "MES_UpdateMaterialCheckoutInfo", targetNamespace = "http://service.server.code.mes.com")
	public String MES_UpdateMaterialCheckoutInfo(@WebParam(name = "DATA") String DATA);
}
