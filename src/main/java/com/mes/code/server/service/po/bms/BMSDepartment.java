package com.mes.code.server.service.po.bms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门表
 * @author ShrisJava
 *
 */
public class BMSDepartment implements Serializable {

	private static final long serialVersionUID = 1L;

	public int ID = 0;

	public String Name = "";

	public boolean Active = false;

	public int ParentID = 0;

	public List<BMSDepartment> SonList = new ArrayList<>();

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

	public boolean getActive() {
		return Active;
	}

	public void setActive(boolean active) {
		Active = active;
	}

	public int getParentID() {
		return ParentID;
	}

	public void setParentID(int parentID) {
		ParentID = parentID;
	}

	public List<BMSDepartment> getSonList() {
		return SonList;
	}

	public void setSonList(List<BMSDepartment> sonList) {
		SonList = sonList;
	}

	public BMSDepartment() {
		this.ID = -1;
		this.Name = "";
		this.SonList = new ArrayList<>();
		this.Active = true;
		this.ParentID = 0;
	}
}
