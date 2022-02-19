package com.mes.code.server.service.po.tcm;

import java.util.ArrayList;
import java.util.List;

/**
 * 标准BOM传输线程类
 */
public class ThreadBOM {
	/**
	 * 变更BOM集合
	 */
	public List<TCMBOM> BOMList = new ArrayList<TCMBOM>();
	/**
	 * 传输日志ID
	 */
	public int LogID = 0;

	public List<TCMBOM> getBOMList() {
		return BOMList;
	}

	public void setBOMList(List<TCMBOM> bOMList) {
		BOMList = bOMList;
	}

	public int getLogID() {
		return LogID;
	}

	public void setLogID(int logID) {
		LogID = logID;
	}
}
