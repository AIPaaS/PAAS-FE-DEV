package com.aic.paas.wdev.bean;

import java.io.Serializable;

public class ProjectProdInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	/** 工程 **/
	private PcProject project;
	
	/** 工程对应所属产品 **/
	private PcProduct product;
	
	
	

	public PcProject getProject() {
		return project;
	}

	public void setProject(PcProject project) {
		this.project = project;
	}

	public PcProduct getProduct() {
		return product;
	}

	public void setProduct(PcProduct product) {
		this.product = product;
	}
	
	
	
	

}
