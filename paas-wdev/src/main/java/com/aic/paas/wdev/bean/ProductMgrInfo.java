package com.aic.paas.wdev.bean;

import java.io.Serializable;
import java.util.List;

import com.aic.paas.frame.cross.bean.SysOp;

public class ProductMgrInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** 产品 **/
	private PcProduct product;
	
	/** 产品管理员ID **/
	private Long[] mgrIds;
	
	/** 产品管理员对象 **/
	private List<SysOp> mgrOps;



	public List<SysOp> getMgrOps() {
		return mgrOps;
	}



	public void setMgrOps(List<SysOp> mgrOps) {
		this.mgrOps = mgrOps;
	}



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
