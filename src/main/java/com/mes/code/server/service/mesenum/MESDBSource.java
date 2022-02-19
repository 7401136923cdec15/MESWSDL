package com.mes.code.server.service.mesenum;

import com.mes.code.server.utils.Configuration;

public enum MESDBSource {
	/**
	 * SCH
	 */
	Default(0, "MySQL"),
	/**
	 * SCH
	 */
	Basic(1, "Basic"),
	/**
	 * APP
	 */
	Instance(2, "Instance"),
	/**
	 * APP
	 */
	MDS(3, "MDS"),
	EXC(4, "EXC"),
	DMS(4, "DMS"), 
	ERP(5, "ERP"),
	APS(6,"APS");
	private int value;
	private String lable;

	private MESDBSource(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static MESDBSource getEnumType(int val) {
		for (MESDBSource type : MESDBSource.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return Default;
	}
	
	public String getDBName() {
		String wResult="iplantmes";
		switch (this) {
		case Default:
			wResult= DefaultDBName;
			break;
		case Basic:
			wResult= BasicDBName;
			break;
		case Instance:
			wResult= InstanceDBName;
			break;
		case DMS:
			wResult= DMSDBName;
			break;
		case EXC:
			wResult= EXCDBName;
			break;
		case ERP:
			wResult= ERPDBName;
			break;
		case MDS:
			wResult= MDSDBName;
		case APS:
			wResult=APSDBName;
			break;
		default:
			break;
		}
		return wResult;
		
	}

	public int getValue() {
		return value;
	}

	public String getLable() {
		return lable;
	}
	
	private static String DefaultDBName = Configuration.readConfigString("default.db.name", "config/config");
	private static String InstanceDBName = Configuration.readConfigString("instance.db.name", "config/config");

	private static String BasicDBName = Configuration.readConfigString("mes.db.name", "config/config");
	private static String DMSDBName = Configuration.readConfigString("dms.db.name", "config/config");
	private static String EXCDBName = Configuration.readConfigString("exc.db.name", "config/config");
	private static String ERPDBName = Configuration.readConfigString("erp.db.name", "config/config");
	private static String MDSDBName = Configuration.readConfigString("mds.db.name", "config/config");
	private static String APSDBName = Configuration.readConfigString("aps.db.name", "config/config");
}
