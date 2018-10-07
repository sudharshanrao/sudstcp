package com.clearmarkets.cleartcp.domain;

import java.io.Serializable;

public class MonitoringDataWrapper implements Serializable{
	private static final long serialVersionUID = 2479195482169504973L;
	private CMUser cmUser;
	private String monitorData;
	
	public MonitoringDataWrapper(CMUser cmUserByLoginId,
			String monitorData) {
		this.cmUser = cmUserByLoginId;
		this.monitorData = monitorData;
	}
	
	public CMUser getCmUser() {
		return cmUser;
	}
	public void setCmUser(CMUser cmUser) {
		this.cmUser = cmUser;
	}

	public String getMonitorData() {
		return monitorData;
	}

	public void setMonitorData(String monitorData) {
		this.monitorData = monitorData;
	}

	@Override
	public String toString() {
		return "MonitoringDataWrapper [cmUser=" + cmUser
				+ ", monitoringDataRecent=" + monitorData + "]";
				
	}
}
