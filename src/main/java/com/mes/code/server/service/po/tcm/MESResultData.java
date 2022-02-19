package com.mes.code.server.service.po.tcm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 返回结构数据
 */
@XmlRootElement(name = "MESResultData")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@XStreamAlias("DATA")
public class MESResultData {
	/**
	 * 结果集合
	 */
	private List<ITEM> LIST = new ArrayList<ITEM>();

	public List<ITEM> getLIST() {
		return LIST;
	}

	public void setLIST(List<ITEM> lIST) {
		LIST = lIST;
	}
}
