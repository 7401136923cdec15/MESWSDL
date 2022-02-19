package com.mes.code.server.service.po.tcm;

import java.io.Serializable;

/**
 * 工艺变更信息
 */
public class TCMChangeInfo implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 工艺路线ID
	 */
	public int RouteID = 0;
	/**
	 * 工艺变更单 号
	 */
	public String PROCESSCNO = "";
	/**
	 * 工艺变更单
	 */
	public String PROCESSC = "";
	/**
	 * 工艺变更创建人信息
	 */
	public String PROCESSCUSER = "";
	/**
	 * 工艺变更类型
	 */
	public String PROCESSCTYPE = "";

	public int getID() {
		return ID;
	}

	public int getRouteID() {
		return RouteID;
	}

	public String getPROCESSCNO() {
		return PROCESSCNO;
	}

	public String getPROCESSC() {
		return PROCESSC;
	}

	public String getPROCESSCUSER() {
		return PROCESSCUSER;
	}

	public String getPROCESSCTYPE() {
		return PROCESSCTYPE;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setRouteID(int routeID) {
		RouteID = routeID;
	}

	public void setPROCESSCNO(String pROCESSCNO) {
		PROCESSCNO = pROCESSCNO;
	}

	public void setPROCESSC(String pROCESSC) {
		PROCESSC = pROCESSC;
	}

	public void setPROCESSCUSER(String pROCESSCUSER) {
		PROCESSCUSER = pROCESSCUSER;
	}

	public void setPROCESSCTYPE(String pROCESSCTYPE) {
		PROCESSCTYPE = pROCESSCTYPE;
	}
}
