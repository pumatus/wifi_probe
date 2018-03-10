package com.opencv.danbing.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Pumatus on 2018/3/10.
 */
@Entity
public class ScanTasks {
	@Id(autoincrement = true)
	private Long id;
	private String taskName;
	private String taskDT;
	private String startTime;
	private String endTime;
	private String defaultTaskID;
	@Generated(hash = 148239092)
	public ScanTasks(Long id, String taskName, String taskDT, String startTime,
			String endTime, String defaultTaskID) {
		this.id = id;
		this.taskName = taskName;
		this.taskDT = taskDT;
		this.startTime = startTime;
		this.endTime = endTime;
		this.defaultTaskID = defaultTaskID;
	}
	@Generated(hash = 686040606)
	public ScanTasks() {
	}
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTaskName() {
		return this.taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskDT() {
		return this.taskDT;
	}
	public void setTaskDT(String taskDT) {
		this.taskDT = taskDT;
	}
	public String getStartTime() {
		return this.startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return this.endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDefaultTaskID() {
		return this.defaultTaskID;
	}
	public void setDefaultTaskID(String defaultTaskID) {
		this.defaultTaskID = defaultTaskID;
	}
}
