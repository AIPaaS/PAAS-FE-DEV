package com.aic.paas.wdev.bean;

import java.io.Serializable;

public class ProductInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	/** 产品 **/
	private PcProduct product;
	
	
	
	/** 产品管理员 **/
	private Long[] mgrIds;
	
	



	public PcProduct getProduct() {
		return product;
	}



	public void setProduct(PcProduct product) {
		this.product = product;
	}



	public Long[] getMgrIds() {
		return mgrIds;
	}



	public void setMgrIds(Long[] mgrIds) {
		this.mgrIds = mgrIds;
	}
	
	
	
	
	
	
	
	

}
