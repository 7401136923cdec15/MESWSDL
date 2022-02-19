package com.mes.code.server.service.po.tcm;

import java.util.ArrayList;
import java.util.List;

/**
 * 工序变更线程类
 */
public class ThreadOP {
	/**
	 * 变更工序集合
	 */
	public List<OP> OPList = new ArrayList<OP>();
	/**
	 * 传输日志ID
	 */
	public int LogID = 0;

	public List<OP> getOPList() {
		return OPList;
	}

	public int getLogID() {
		return LogID;
	}

	public void setOPList(List<OP> oPList) {
		OPList = oPList;
	}

	public void setLogID(int logID) {
		LogID = logID;
	}
}
