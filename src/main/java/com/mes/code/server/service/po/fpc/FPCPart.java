package com.mes.code.server.service.po.fpc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mes.code.server.service.po.bms.BMSWorkCharge;

public class FPCPart implements Serializable {

	private static final long serialVersionUID = 1L;

	public int ID = 0;

	public String Name = "";

	public String Code = "";

	public Calendar CreateTime = Calendar.getInstance();

	public int CreatorID = 0;

	public Calendar EditTime = Calendar.getInstance();

	public int EditorID = 0;

	public int Status = 0; // 审批状态

	public Calendar AuditTime = Calendar.getInstance();

	public int AuditorID = 0;

	public int FactoryID = 0; // 工厂

	public int BusinessUnitID = 0; // 事业部

	public int ProductTypeID = 0; // 产品类型

	/**
	 * 工位类型 普通工位 -1 预检工位-2 质量工位-3 普查工位-4
	 */
	public int PartType = 0;

	public int QTPartID = 0;

	public String Factory = "";

	public String BusinessUnit = "";

	public String ProductType = "";

	public String Creator = "";

	public String Auditor = "";

	public String Editor = "";

	public String StatusText = ""; // 审批状态

	public int Active = 0; // 状态

	public int ERPID = 0; // ERP关联ID

	/**
	 * 共同责任班组
	 */
	public List<Integer> DepartmentIDList = new ArrayList<Integer>();

	public String DepartmentName = "";
	/**
	 * 现场工艺人员
	 */
	public List<Integer> TechnicianList = new ArrayList<Integer>();

	public String TechnicianName = "";

	public List<BMSWorkCharge> WorkChargeList = new ArrayList<BMSWorkCharge>();

	public List<Integer> CheckerList = new ArrayList<Integer>();

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

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
	}

	public int getCreatorID() {
		return CreatorID;
	}

	public void setCreatorID(int creatorID) {
		CreatorID = creatorID;
	}

	public Calendar getEditTime() {
		return EditTime;
	}

	public void setEditTime(Calendar editTime) {
		EditTime = editTime;
	}

	public int getEditorID() {
		return EditorID;
	}

	public void setEditorID(int editorID) {
		EditorID = editorID;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public Calendar getAuditTime() {
		return AuditTime;
	}

	public void setAuditTime(Calendar auditTime) {
		AuditTime = auditTime;
	}

	public int getAuditorID() {
		return AuditorID;
	}

	public void setAuditorID(int auditorID) {
		AuditorID = auditorID;
	}

	public int getFactoryID() {
		return FactoryID;
	}

	public void setFactoryID(int factoryID) {
		FactoryID = factoryID;
	}

	public int getBusinessUnitID() {
		return BusinessUnitID;
	}

	public void setBusinessUnitID(int businessUnitID) {
		BusinessUnitID = businessUnitID;
	}

	public int getProductTypeID() {
		return ProductTypeID;
	}

	public void setProductTypeID(int productTypeID) {
		ProductTypeID = productTypeID;
	}

	public String getFactory() {
		return Factory;
	}

	public void setFactory(String factory) {
		Factory = factory;
	}

	public String getBusinessUnit() {
		return BusinessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		BusinessUnit = businessUnit;
	}

	public String getProductType() {
		return ProductType;
	}

	public void setProductType(String productType) {
		ProductType = productType;
	}

	public String getCreator() {
		return Creator;
	}

	public void setCreator(String creator) {
		Creator = creator;
	}

	public String getAuditor() {
		return Auditor;
	}

	public void setAuditor(String auditor) {
		Auditor = auditor;
	}

	public String getEditor() {
		return Editor;
	}

	public void setEditor(String editor) {
		Editor = editor;
	}

	public String getStatusText() {
		return StatusText;
	}

	public void setStatusText(String statusText) {
		StatusText = statusText;
	}

	public int isActive() {
		return Active;
	}

	public void setActive(int active) {
		Active = active;
	}

	public int getERPID() {
		return ERPID;
	}

	public void setERPID(int eRPID) {
		ERPID = eRPID;
	}

	public FPCPart() {
		this.Name = "";
		this.Code = "";
		this.Factory = "";
		this.BusinessUnit = "";
		this.ProductType = "";
		this.Editor = "";
		this.Creator = "";
		this.Auditor = "";
		this.CreateTime = Calendar.getInstance();
		this.AuditTime = Calendar.getInstance();
		this.EditTime = Calendar.getInstance();
		this.StatusText = "";
		this.Active = 0;
	}

	public FPCPart Clone() {
		FPCPart wItem = new FPCPart();
		wItem.ID = this.ID;
		wItem.Name = this.Name;
		wItem.Code = this.Code;

		wItem.CreatorID = this.CreatorID;
		wItem.EditorID = this.EditorID;
		wItem.AuditorID = this.AuditorID;
		wItem.Status = this.Status;
		wItem.Active = this.Active;
		wItem.CreateTime = this.CreateTime;
		wItem.AuditTime = this.AuditTime;
		wItem.EditTime = this.EditTime;

		wItem.FactoryID = this.FactoryID;
		wItem.BusinessUnitID = this.BusinessUnitID;
		wItem.ProductTypeID = this.ProductTypeID;

		wItem.Factory = this.Factory;
		wItem.BusinessUnit = this.BusinessUnit;
		wItem.ProductType = this.ProductType;
		wItem.Creator = this.Creator;
		wItem.Editor = this.Editor;
		wItem.Auditor = this.Auditor;
		wItem.Status = this.Status;
		return wItem;
	}

	public int getPartType() {
		return PartType;
	}

	public void setPartType(int partType) {
		PartType = partType;
	}

	public int getActive() {
		return Active;
	}

	public int getQTPartID() {
		return QTPartID;
	}

	public void setQTPartID(int qTPartID) {
		QTPartID = qTPartID;
	}

	public List<Integer> getDepartmentIDList() {
		return DepartmentIDList;
	}

	public void setDepartmentIDList(List<Integer> departmentIDList) {
		DepartmentIDList = departmentIDList;
	}

	public String getDepartmentName() {
		return DepartmentName;
	}

	public void setDepartmentName(String departmentName) {
		DepartmentName = departmentName;
	}

	public List<Integer> getTechnicianList() {
		return TechnicianList;
	}

	public void setTechnicianList(List<Integer> technicianList) {
		TechnicianList = technicianList;
	}

	public String getTechnicianName() {
		return TechnicianName;
	}

	public void setTechnicianName(String technicianName) {
		TechnicianName = technicianName;
	}

	public List<BMSWorkCharge> getWorkChargeList() {
		return WorkChargeList;
	}

	public void setWorkChargeList(List<BMSWorkCharge> workChargeList) {
		WorkChargeList = workChargeList;
	}

	public List<Integer> getCheckerList() {
		return CheckerList;
	}

	public void setCheckerList(List<Integer> checkerList) {
		CheckerList = checkerList;
	}
}
