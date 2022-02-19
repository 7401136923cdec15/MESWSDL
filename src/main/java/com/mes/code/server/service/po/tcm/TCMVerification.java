package com.mes.code.server.service.po.tcm;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 传输校验类
 * 
 * @author YouWang·Peng
 * @CreateTime 2021-10-29 14:14:03
 */
public class TCMVerification implements Serializable {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 字符校验码
	 */
	public String CheckCode = "";
	/**
	 * 创建时刻
	 */
	public Calendar CreateTime = Calendar.getInstance();
	/**
	 * 处理状态 0处理中 1处理完成
	 */
	public int Status = 0;

	public TCMVerification() {
		super();
	}

	public TCMVerification(int iD, String checkCode, Calendar createTime, int status) {
		super();
		ID = iD;
		CheckCode = checkCode;
		CreateTime = createTime;
		Status = status;
	}

	public int getID() {
		return ID;
	}

	public String getCheckCode() {
		return CheckCode;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public int getStatus() {
		return Status;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setCheckCode(String checkCode) {
		CheckCode = checkCode;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
	}

	public void setStatus(int status) {
		Status = status;
	}
}
