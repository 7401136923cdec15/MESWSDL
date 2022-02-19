package com.mes.code.server.service.po.mcs;

import java.io.Serializable;

/**
 * 实体类信息
 * 
 * @author PengYouWang
 * @CreateTime 2020-1-9 14:10:44
 * @LastEditTime 2020-1-9 14:10:48
 *
 */
public class MCSDatabaseInfo implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	public String Server = "";
	public String Database = "";
	public String User = "";
	public String Password = "";

	public MCSDatabaseInfo() {
	}

	public String getServer() {
		return Server;
	}

	public void setServer(String server) {
		Server = server;
	}

	public String getDatabase() {
		return Database;
	}

	public void setDatabase(String database) {
		Database = database;
	}

	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}
}
