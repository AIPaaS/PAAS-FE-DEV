package com.aic.paas.wdev.peer.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.wdev.bean.CPcImage;
import com.aic.paas.wdev.bean.CPcProduct;
import com.aic.paas.wdev.bean.PcImage;
import com.aic.paas.wdev.bean.PcProduct;
import com.aic.paas.wdev.bean.PcProject;
import com.aic.paas.wdev.peer.UserAuthentication;
import com.aic.paas.wdev.rest.PcImageSvc;
import com.aic.paas.wdev.rest.PcProductSvc;
import com.aic.paas.wdev.rest.PcProjectSvc;
import com.binary.framework.exception.ServiceException;


/**
 * 用户认证
 * @author wanwb
 */
public class UserAuthenticationPeerImpl implements UserAuthentication {

	
	@Autowired
	PcProductSvc productSvc;
	
	@Autowired
	PcProjectSvc projectSvc;
	
	@Autowired
	PcImageSvc imageSvc;
	
	
	
	@Override
	public void verifyUserProductAuth(Long productId) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		CPcProduct cdt = new CPcProduct();
		cdt.setMntId(user.getMerchent().getId());
		cdt.setId(productId);
		List<PcProduct> ls = productSvc.queryList(cdt, null);
		if(ls.size() == 0) {
			throw new ServiceException(" You don't have permission to operate product '"+productId+"' or product is not exists! ");
		}
	}
	
	
	
	
	
	@Override
	public void verifyUserProjectAuth(Long projectId) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		
		PcProject pro = projectSvc.queryById(projectId);
		if(pro == null) {
			throw new ServiceException(" not found project by id '"+projectId+"'! ");
		}
		
		Long productId = pro.getProductId();
		CPcProduct cdt = new CPcProduct();
		cdt.setMntId(user.getMerchent().getId());
		cdt.setId(productId);
		List<PcProduct> ls = productSvc.queryList(cdt, null);
		if(ls.size() == 0) {
			throw new ServiceException(" You don't have permission to operate project '"+projectId+"' or project is not exists! ");
		}
	}
	
	
	
	@Override
	public void verifyImageAuth(Long imageId) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		CPcImage imgcdt = new CPcImage();
		imgcdt.setMntId(user.getMerchent().getId());
		imgcdt.setId(imageId);
		List<PcImage> ls = imageSvc.queryImageList(imgcdt, null);
		if(ls.size() == 0) {
			throw new ServiceException(" You don't have permission to operate image '"+imageId+"' or image is not exists! ");
		}
	}
	
	
	
	
	
}
