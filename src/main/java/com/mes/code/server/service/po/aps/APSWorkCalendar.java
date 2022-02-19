package com.mes.code.server.service.po.aps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class APSWorkCalendar implements Serializable {
	private static final long serialVersionUID = 1L;

	// [DataMember(Name = "ID", Order = 0)]
	public int ID = 0;
	// [DataMember(Name = "WeekDay", Order = 1)]
	public int WeekDay = 0; // 周几
	// [DataMember(Name = "WorkDate", Order = 2)]
	public Calendar WorkDate = Calendar.getInstance(); // 日期
	// [DataMember(Name = "WorkShiftList", Order = 3)]
	public List<APSWorkShift> WorkShiftList = new ArrayList<>(); // 工作日历
	// [DataMember(Name = "DayOfWeekText", Order = 4)]
	public String DayOfWeekText; // 周几文本
	// [DataMember(Name = "Active", Order = 5)]
	public boolean Active = false; // 是否工作日

	public APSWorkCalendar() {
		this.WorkShiftList = new ArrayList<>();
		this.DayOfWeekText = "";
		this.ID = 0;
		this.Active = true;
		this.WorkDate = Calendar.getInstance();
		this.WeekDay = this.WorkDate.get(Calendar.DAY_OF_WEEK);
		switch (this.WeekDay) {
		case 2:
			this.DayOfWeekText = "星期一";
			break;
		case 3:
			this.DayOfWeekText = "星期二";
			break;
		case 4:
			this.DayOfWeekText = "星期三";
			break;
		case 5:
			this.DayOfWeekText = "星期四";
			break;
		case 6:
			this.DayOfWeekText = "星期五";
			break;
		case 7:
			this.DayOfWeekText = "星期六";
			break;
		case 1:
			this.DayOfWeekText = "星期天";
			break;
		}
	}

	public APSWorkCalendar(int wID, Calendar wWorkDate) {
		this.WorkShiftList = new ArrayList<>();

		this.ID = wID;
		this.WorkDate = wWorkDate;
		this.WeekDay = this.WorkDate.get(Calendar.DAY_OF_WEEK);
		this.Active = true;
		switch (this.WeekDay) {
		case 2:
			this.DayOfWeekText = "星期一";
			break;
		case 3:
			this.DayOfWeekText = "星期二";
			break;
		case 4:
			this.DayOfWeekText = "星期三";
			break;
		case 5:
			this.DayOfWeekText = "星期四";
			break;
		case 6:
			this.DayOfWeekText = "星期五";
			break;
		case 7:
			this.DayOfWeekText = "星期六";
			break;
		case 1:
			this.DayOfWeekText = "星期天";
			break;
		}
	}
}
