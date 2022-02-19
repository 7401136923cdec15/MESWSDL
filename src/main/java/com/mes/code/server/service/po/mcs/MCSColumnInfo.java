package com.mes.code.server.service.po.mcs;

import java.io.Serializable;

/**
 * 字段信息
 * 
 * @author PengYouWang
 * @CreateTime 2020-1-9 14:10:44
 * @LastEditTime 2020-1-9 14:10:48
 *
 */
public class MCSColumnInfo implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public int ID = 0;
	public String Name = "";
	public String DatabaseType = "";
	public String ClassType = "";
	public boolean IsPrimaryKey;
	public boolean IsQuery;

	public MCSColumnInfo() {
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

	public String getDatabaseType() {
		return DatabaseType;
	}

	public void setDatabaseType(String databaseType) {
		DatabaseType = databaseType;
	}

	public String getClassType() {
		return ClassType;
	}

	public void setClassType(String classType) {
		ClassType = classType;
	}

	public boolean isIsPrimaryKey() {
		return IsPrimaryKey;
	}

	public void setIsPrimaryKey(boolean isPrimaryKey) {
		IsPrimaryKey = isPrimaryKey;
	}

	public boolean isIsQuery() {
		return IsQuery;
	}

	public void setIsQuery(boolean isQuery) {
		IsQuery = isQuery;
	}
}
