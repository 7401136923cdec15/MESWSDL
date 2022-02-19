package com.mes.code.server.service.po.sfc;

import java.io.Serializable;
import java.util.Calendar;

import com.mes.code.server.shristool.LoggerTool;

public class SFCWorkFlow implements Serializable {
	private static final long serialVersionUID = 1L;

	// [DataMember(Name = "ID", Order = 0)]
	public int ID= 0; // 索引值
	// [DataMember(Name = "EntryID", Order = 1)]
	public int EntryID= 0; // 实体ID
	// [DataMember(Name = "EntryName", Order = 2)]
	public String EntryName = ""; // 实体名称
	// [DataMember(Name = "EntryType", Order = 3)]
	public int EntryType= 0; // 实体类别（车间、产线、工序、工位）
	// [DataMember(Name = "WorkFlowID", Order = 4)]
	public int WorkFlowID= 0; // 作业工作流ID
	// [DataMember(Name = "CreateTime", Order = 5)]
	public Calendar CreateTime= Calendar.getInstance();
	// [DataMember(Name = "CreatorID", Order = 6)]
	public int CreatorID= 0;
	// [DataMember(Name = "AuditTime", Order = 7)]
	public Calendar AuditTime= Calendar.getInstance();
	// [DataMember(Name = "AuditorID", Order = 8)]
	public int AuditorID= 0;
	// [DataMember(Name = "Status", Order = 9)]
	public int Status= 0;
	// [DataMember(Name = "Active", Order = 10)]
	public boolean Active=false;
	// [DataMember(Name = "Creator", Order = 11)]
	public String Creator = "";
	// [DataMember(Name = "Auditor", Order = 12)]
	public String Auditor = "";
	// [DataMember(Name = "WorkShopName", Order = 13)]
	public String WorkShopName = "";
	// [DataMember(Name = "LineName", Order = 14)]
	public String LineName = "";
	// [DataMember(Name = "WorkFlowName", Order = 15)]
	public String WorkFlowName = "";
	// [DataMember(Name = "LineID", Order = 16)]
	public int LineID= 0; // 产线ID
	// [DataMember(Name = "WorkShopID", Order = 17)]
	public int WorkShopID= 0; // 车间ID

	public SFCWorkFlow() {
		this.ID=0;
		this.EntryID=0;
		this.EntryType=0;
		this.WorkFlowID=0;
		this.CreatorID=0;
		this.AuditorID=0;
		this.Status=0;
		
		this.LineID=0;
		this.WorkShopID=0;
		this.Active=false;
		this.Creator = "";
		this.Auditor = "";
		this.WorkShopName = "";
		this.LineName = "";
		this.WorkFlowName = "";
		this.AuditTime = Calendar.getInstance();
		this.CreateTime = Calendar.getInstance();
		this.Active = true;
	}

	public SFCWorkFlow Clone() {
		SFCWorkFlow wItem = new SFCWorkFlow();
		try {
			wItem.ID = this.ID;
			wItem.EntryID = this.EntryID;
			wItem.EntryName = this.EntryName;
			wItem.EntryType = this.EntryType;

			wItem.WorkFlowID = this.WorkFlowID;
			wItem.AuditTime = this.AuditTime;
			wItem.AuditorID = this.AuditorID;
			wItem.CreateTime = this.CreateTime;
			wItem.CreatorID = this.CreatorID;
			wItem.Status = this.Status;
			wItem.Active = this.Active;
			wItem.Creator = this.Creator;
			wItem.Auditor = this.Auditor;
			wItem.WorkShopName = this.WorkShopName;
			wItem.LineName = this.LineName;
			wItem.WorkFlowName = this.WorkFlowName;
		} catch (Exception ex) {
			LoggerTool.SaveException("SFCService", "SFCWorkFlow Clone", "Function Exception:" + ex.toString());
		}
		return wItem;
	}
}
