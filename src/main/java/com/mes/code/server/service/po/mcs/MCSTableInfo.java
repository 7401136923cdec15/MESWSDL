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
public class MCSTableInfo implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	public int ID = 0;
	public String TableName = "";
	public String ClassName = "";
	public List<MCSColumnInfo> ColumnInfoList = new ArrayList<MCSColumnInfo>();

	public MCSTableInfo() {
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTableName() {
		return TableName;
	}

	public void setTableName(String tableName) {
		TableName = tableName;
	}

	public String getClassName() {
		return ClassName;
	}

	public void setClassName(String className) {
		ClassName = className;
	}

	public List<MCSColumnInfo> getColumnInfoList() {
		return ColumnInfoList;
	}

	public void setColumnInfoList(List<MCSColumnInfo> columnInfoList) {
		ColumnInfoList = columnInfoList;
	}
}
