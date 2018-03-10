package com.opencv.danbing.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Pumatus on 2018/3/9.
 */
@Entity
public class ScanResults {
	@Id(autoincrement = true)
	private Long id;
	private String dt;
	private String macAddress;
	private String deviceType;
	private String defaultTaskID;
	private String dataStr;
	private String timeStr;
	@Generated(hash = 551516541)
	public ScanResults(Long id, String dt, String macAddress, String deviceType,
			String defaultTaskID, String dataStr, String timeStr) {
		this.id = id;
		this.dt = dt;
		this.macAddress = macAddress;
		this.deviceType = deviceType;
		this.defaultTaskID = defaultTaskID;
		this.dataStr = dataStr;
		this.timeStr = timeStr;
	}
	@Generated(hash = 1406085879)
	public ScanResults() {
	}
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDt() {
		return this.dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	public String getMacAddress() {
		return this.macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getDeviceType() {
		return this.deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDefaultTaskID() {
		return this.defaultTaskID;
	}
	public void setDefaultTaskID(String defaultTaskID) {
		this.defaultTaskID = defaultTaskID;
	}
	public String getDataStr() {
		return this.dataStr;
	}
	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}
	public String getTimeStr() {
		return this.timeStr;
	}
	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
}
