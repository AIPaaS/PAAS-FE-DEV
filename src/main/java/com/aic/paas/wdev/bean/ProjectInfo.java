package com.aic.paas.wdev.bean;

import java.io.Serializable;

public class ProjectInfo implements Serializable {
	private static final long serialVersionUID = 1L;



	/** 工程 **/
	private PcProject project;
	
	
	
	/** 产品管理员 **/
	private Long[] mgrIds;



	public PcProject getProject() {
		return project;
	}



	public void setProject(PcProject project) {
		this.project = project;
	}



	public Long[] getMgrIds() {
		return mgrIds;
	}



	public void setMgrIds(Long[] mgrIds) {
		this.mgrIds = mgrIds;
	}
	
	
	
	
	

}
