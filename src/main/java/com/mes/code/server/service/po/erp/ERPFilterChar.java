package com.mes.code.server.service.po.erp;

import java.io.Serializable;

/**
 * ERP过滤字符
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-1-10 15:45:32
 */
public class ERPFilterChar implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 过滤字符
	 */
	public String Text = "";

	public ERPFilterChar() {
		super();
	}

	public ERPFilterChar(int iD, String text) {
		super();
		ID = iD;
		Text = text;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}
}
