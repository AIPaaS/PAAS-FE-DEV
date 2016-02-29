package com.aic.paas.wdev.peer.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.bean.CSysOp;
import com.aic.paas.frame.cross.bean.SysOp;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.wdev.bean.CPcProduct;
import com.aic.paas.wdev.bean.ContType;
import com.aic.paas.wdev.bean.PcProduct;
import com.aic.paas.wdev.bean.ProductInfo;
import com.aic.paas.wdev.bean.ProductMgrInfo;
import com.aic.paas.wdev.bean.RespType;
import com.aic.paas.wdev.bean.SourceType;
import com.aic.paas.wdev.peer.PcProductPeer;
import com.aic.paas.wdev.peer.UserAuthentication;
import com.aic.paas.wdev.rest.PcProductSvc;
import com.aic.paas.wdev.rest.SysOpSvc;
import com.aic.paas.wdev.rest.VcMntRepositoryApplySvc;
import com.binary.core.lang.ArrayUtils;
import com.binary.core.util.BinaryUtils;
import com.binary.jdbc.Page;

public class PcProductPeerImpl implements PcProductPeer {
	
	
	@Autowired
	PcProductSvc productSvc;
	
	@Autowired
	SysOpSvc sysOpSvc;
	
	
	@Autowired
	UserAuthentication userAuth;
	
	
	@Autowired
	VcMntRepositoryApplySvc mntRespApplySvc;
	

	
	private List<ProductMgrInfo> fillMgrInfo(List<ProductInfo> ls) {
		List<ProductMgrInfo> infos = new ArrayList<ProductMgrInfo>();
		if(ls!=null && ls.size()>0) {
			Set<Long> opids = new HashSet<Long>();
			for(int i=0; i<ls.size(); i++) {
				ProductInfo pif = ls.get(i);
				ProductMgrInfo info = new ProductMgrInfo();
				info.setProduct(pif.getProduct());
				info.setMgrIds(pif.getMgrIds());
				infos.add(info);
				
				Long[] mids = pif.getMgrIds();
				if(mids!=null && mids.length>0) {
					opids.addAll(ArrayUtils.toList(mids));
				}
			}
			
			if(opids.size() > 0) {
				CSysOp cdt = new CSysOp();
				cdt.setIds(opids.toArray(new Long[0]));
				List<SysOp> ops = sysOpSvc.queryList(cdt, null);
				
				if(ops.size() > 0) {
					Map<Long, SysOp> opmap = new HashMap<Long, SysOp>();
					for(int i=0; i<ops.size(); i++) {
						SysOp op = ops.get(i);
						opmap.put(op.getId(), op);
					}
					
					for(int i=0; i<infos.size(); i++) {
						ProductMgrInfo info = infos.get(i);
						Long[] mgrIds = info.getMgrIds();
						if(mgrIds==null || mgrIds.length==0) continue ;
						
						List<SysOp> opls = new ArrayList<SysOp>();
						for(int j=0; j<mgrIds.length; j++) {
							SysOp op = opmap.get(mgrIds[j]);
							if(op != null) {
								opls.add(op);
							}
						}
						if(opls.size() > 0) {
							info.setMgrOps(opls);
						}
					}
				}
				
			}
		}
		
		return infos;
	}
	
	
	
	
	@Override
	public Page<ProductMgrInfo> queryInfoPage(Integer pageNum, Integer pageSize, CPcProduct cdt, String orders) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		if(cdt == null) cdt = new CPcProduct();
		cdt.setMntId(user.getMerchent().getId());
		Page<ProductInfo> page = productSvc.queryInfoPage(pageNum, pageSize, cdt, orders);
		List<ProductInfo> ls = page.getData();
		List<ProductMgrInfo> infols = fillMgrInfo(ls);
		return new Page<ProductMgrInfo>(page.getPageNum(), page.getPageSize(), page.getTotalRows(), page.getTotalPages(), infols);
	}
	
	

	@Override
	public List<ProductMgrInfo> queryInfoList(CPcProduct cdt, String orders) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		if(cdt == null) cdt = new CPcProduct();
		cdt.setMntId(user.getMerchent().getId());
		List<ProductInfo> ls = productSvc.queryInfoList(cdt, null);
		return fillMgrInfo(ls);
	}
	
	

	@Override
	public ProductMgrInfo queryInfoById(Long id) {
		BinaryUtils.checkEmpty(id, "id");
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		CPcProduct cdt = new CPcProduct();
		cdt.setMntId(user.getMerchent().getId());
		cdt.setId(id);
		List<ProductInfo> ls = productSvc.queryInfoList(cdt, null);
		if(ls.size() == 0) return null;
		
		return fillMgrInfo(ls).get(0);
	}

	@Override
	public Page<PcProduct> queryMgrPage(Integer pageNum, Integer pageSize, CPcProduct cdt, String orders) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		if(cdt == null) cdt = new CPcProduct();
		cdt.setMntId(user.getMerchent().getId());
		return productSvc.queryMgrPage(pageNum, pageSize, user.getId(), cdt, orders);
	}

	
	
	@Override
	public List<PcProduct> queryMgrList(CPcProduct cdt, String orders) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		if(cdt == null) cdt = new CPcProduct();
		cdt.setMntId(user.getMerchent().getId());
		return productSvc.queryMgrList(user.getId(), cdt, orders);
	}

	

	@Override
	public List<PcProduct> queryProductList(CPcProduct cdt, String orders) {
		if(cdt == null) cdt = new CPcProduct();
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		cdt.setMntId(user.getMerchent().getId());
		return productSvc.queryList(cdt, orders);
	}
	
	
	
	
	
	@Override
	public Long saveOrUpdate(PcProduct record) {
		BinaryUtils.checkEmpty(record, "record");
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		record.setMntId(user.getMerchent().getId());
		
		boolean isadd = record.getId() == null;
		if(isadd) {
			BinaryUtils.checkEmpty(record.getCode(), "record.code");
			BinaryUtils.checkEmpty(record.getName(), "record.name");
			BinaryUtils.checkEmpty(record.getStatus(), "record.status");
		}else {
			if(record.getCode() != null) BinaryUtils.checkEmpty(record.getCode(), "record.code");
			if(record.getName() != null) BinaryUtils.checkEmpty(record.getName(), "record.name");
			if(record.getStatus() != null) BinaryUtils.checkEmpty(record.getStatus(), "record.status");
			userAuth.verifyUserProductAuth(record.getId());
		}
		return productSvc.saveOrUpdate(record);
	}
	
	

	@Override
	public int removeById(Long id) {
		BinaryUtils.checkEmpty(id, "id");
		userAuth.verifyUserProductAuth(id);
		return productSvc.removeById(id);
	}

	
	
	
	@Override
	public void setProductMgrs(Long productId, Long[] mgrIds) {
		BinaryUtils.checkEmpty(productId, "productId");
		userAuth.verifyUserProductAuth(productId);
		productSvc.setProductMgrs(productId, mgrIds);
	}
	
	
	@Override
	public void applyRespDoc(Long productId, Integer respType) {
		BinaryUtils.checkEmpty(productId, "productId");
		BinaryUtils.checkEmpty(respType, "respType");
		userAuth.verifyUserProductAuth(productId);
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		Long mntId = user.getMerchent().getId();
		mntRespApplySvc.createApply(mntId, RespType.valueOf(respType), ContType.DOC, SourceType.PRODUCT, productId, null);
	}




	

}
