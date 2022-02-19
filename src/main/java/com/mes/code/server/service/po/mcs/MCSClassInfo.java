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
public class MCSClassInfo implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 命名空间
	 */
	public String Namespace = "";
	/**
	 * 前缀名
	 */
	public String PrefixName = "";
	/**
	 * 类名集合
	 */
	public List<String> ClassNameList = new ArrayList<String>();

	public MCSClassInfo() {
	}

	public String getNamespace() {
		return Namespace;
	}

	public void setNamespace(String namespace) {
		Namespace = namespace;
	}

	public String getPrefixName() {
		return PrefixName;
	}

	public void setPrefixName(String prefixName) {
		PrefixName = prefixName;
	}

	public List<String> getClassNameList() {
		return ClassNameList;
	}

	public void setClassNameList(List<String> classNameList) {
		ClassNameList = classNameList;
	}
}
