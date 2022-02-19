package com.mes.code.server.service.po.fmc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 工区表
 * @author ShrisJava
 *
 */
public class FMCWorkShop implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public int ID= 0;

    public String Name= "";

    public String Code= "";

    public Calendar CreateTime= Calendar.getInstance();

    public int CreatorID= 0;

    public Calendar EditTime= Calendar.getInstance();

    public int EditorID= 0;

    public int Status= 0;              //状态

    public Calendar AuditTime= Calendar.getInstance();

    public int AuditorID= 0;

    public int FactoryID= 0;

    public int BusinessUnitID= 0;

    public String Factory= "";

    public String BusinessUnit= "";

    public String Creator= "";

    public String Auditor= "";

    public String Editor= "";

    public String StatusText= "";         //审批状态

    public boolean Active=false;           //状态

    public String BusinessCode= "";

    public String FactoryCode= "";

    public int Shifts= 0;              //默认一天几班：1:早；2中；3：晚

    public int ShiftID= 0;             //班次模板

    public String ShiftName= "";           //班次模板名称

    public List<FMCLine> LineList= new ArrayList<>();    //工位

    public int IPTModuleID= 0;         //工位点检模板ID

    public int SCPeriod= 0;             //车间生产巡检周期;
    
    
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
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	public String getBusinessCode() {
		return BusinessCode;
	}
	public void setBusinessCode(String businessCode) {
		BusinessCode = businessCode;
	}
	public String getFactoryCode() {
		return FactoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		FactoryCode = factoryCode;
	}
	public int getShifts() {
		return Shifts;
	}
	public void setShifts(int shifts) {
		Shifts = shifts;
	}
	public int getShiftID() {
		return ShiftID;
	}
	public void setShiftID(int shiftID) {
		ShiftID = shiftID;
	}
	public String getShiftName() {
		return ShiftName;
	}
	public void setShiftName(String shiftName) {
		ShiftName = shiftName;
	}
	public List<FMCLine> getLineList() {
		return LineList;
	}
	public void setLineList(List<FMCLine> lineList) {
		LineList = lineList;
	}
	public int getIPTModuleID() {
		return IPTModuleID;
	}
	public void setIPTModuleID(int iPTModuleID) {
		IPTModuleID = iPTModuleID;
	}
	public int getSCPeriod() {
		return SCPeriod;
	}
	public void setSCPeriod(int sCPeriod) {
		SCPeriod = sCPeriod;
	}
	public FMCWorkShop()
    {
        this.Name = "";
        this.Code = "";
        this.Factory = "";
        this.BusinessUnit = "";
        this.Editor = "";
        this.Creator = "";
        this.Auditor = "";
        this.CreateTime = Calendar.getInstance();
        this.AuditTime = Calendar.getInstance();
        this.EditTime = Calendar.getInstance();
        this.StatusText = "";
        this.LineList = new ArrayList<>();
    }
    public FMCWorkShop Clone()
    {
        FMCWorkShop wItem = new FMCWorkShop();
        wItem.ID = this.ID;
        wItem.Name = this.Name;
        wItem.Code = this.Code;
        wItem.FactoryID = this.FactoryID;
        wItem.BusinessUnitID = this.BusinessUnitID;
        wItem.CreatorID = this.CreatorID;
        wItem.EditorID = this.EditorID;
        wItem.AuditorID = this.AuditorID;
        wItem.Status = this.Status;
        wItem.Active = this.Active;
        wItem.CreateTime = this.CreateTime;
        wItem.AuditTime = this.AuditTime;
        wItem.EditTime = this.EditTime;

        wItem.Factory = this.Factory;
        wItem.BusinessUnit = this.BusinessUnit;
        wItem.Creator = this.Creator;
        wItem.Editor = this.Editor;
        wItem.Auditor = this.Auditor;
        wItem.Status = this.Status;
        wItem.LineList = new ArrayList<FMCLine>(this.LineList);
        return wItem;
    }
}
