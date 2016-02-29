package com.aic.paas.wdev.bean;

import java.io.Serializable;
import java.util.List;

import com.aic.paas.frame.cross.bean.SysOp;

public class ProjectMgrInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 工程 **/
	private PcProject project;
	
	/** 工程管理员ID **/
	private Long[] mgrIds;
	
	/** 工程管理员对象 **/
	private List<SysOp> mgrOps;



	public List<SysOp> getMgrOps() {
		return mgrOps;
	}



	public void setMgrOps(List<SysOp> mgrOps) {
		this.mgrOps = mgrOps;
	}



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
