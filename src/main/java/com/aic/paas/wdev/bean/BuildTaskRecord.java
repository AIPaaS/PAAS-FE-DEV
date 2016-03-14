package com.aic.paas.wdev.bean;

import java.util.Date;

public class BuildTaskRecord {
	private String namespace;
	private String repo_name;
	private String build_id;
	private boolean building;
	private Integer duration;
	private String started_at;
	private String status;
	private String stdout;
	private Date startTime;
	private String resultCode;
	private String resultMsg;
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getRepo_name() {
		return repo_name;
	}
	public void setRepo_name(String repo_name) {
		this.repo_name = repo_name;
	}
	public String getBuild_id() {
		return build_id;
	}
	public void setBuild_id(String build_id) {
		this.build_id = build_id;
	}
	public boolean isBuilding() {
		return building;
	}
	public void setBuilding(boolean building) {
		this.building = building;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getStarted_at() {
		return started_at;
	}
	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStdout() {
		return stdout;
	}
	public void setStdout(String stdout) {
		this.stdout = stdout;
	}
}
