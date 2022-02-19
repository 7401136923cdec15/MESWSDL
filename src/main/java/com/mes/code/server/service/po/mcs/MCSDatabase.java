package com.mes.code.server.service.po.mcs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体类信息
 * 
 * @author PengYouWang
 * @CreateTime 2020-1-9 14:10:44
 * @LastEditTime 2020-1-9 14:10:48
 *
 */
public class MCSDatabase implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	public int ID = 0;
	public String Name = "";
	public List<MCSTableInfo> TableInfoList = new ArrayList<MCSTableInfo>();

	public MCSDatabase() {
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public List<MCSTableInfo> getTableInfoList() {
		return TableInfoList;
	}

	public void setTableInfoList(List<MCSTableInfo> tableInfoList) {
		TableInfoList = tableInfoList;
	}
}
