package com.aic.paas.wdev.peer.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.wdev.bean.CPcBuildDef;
import com.aic.paas.wdev.bean.PcBuildDef;
import com.aic.paas.wdev.bean.PcBuildDefInfo;
import com.aic.paas.wdev.peer.PcBuildPeer;
import com.aic.paas.wdev.rest.PcBuildSvc;
import com.binary.core.util.BinaryUtils;
import com.binary.jdbc.Page;

public class PcBuildPeerImpl implements PcBuildPeer {
	
	
	@Autowired
	PcBuildSvc buildSvc;
	

	
	@Override
	public Page<PcBuildDefInfo> queryDefInfoPage(Integer pageNum, Integer pageSize, CPcBuildDef cdt, String orders) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		if(cdt == null) cdt = new CPcBuildDef();
		cdt.setMntId(user.getMerchent().getId());
		return buildSvc.queryDefInfoPage(pageNum, pageSize, cdt, orders);
	}
	
	
	

	@Override
	public List<PcBuildDefInfo> queryDefInfoList(CPcBuildDef cdt, String orders) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		if(cdt == null) cdt = new CPcBuildDef();
		cdt.setMntId(user.getMerchent().getId());
		return buildSvc.queryDefInfoList(cdt, orders);
	}
	
	
	

	@Override
	public PcBuildDefInfo queryDefInfoById(Long id) {
		BinaryUtils.checkEmpty(id, "id");
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		CPcBuildDef cdt = new CPcBuildDef();
		cdt.setMntId(user.getMerchent().getId());
		cdt.setId(id);
		
		List<PcBuildDefInfo> ls = buildSvc.queryDefInfoList(cdt, null);
		return ls.size()>0 ? ls.get(0) : null;
	}
	
	

	@Override
	public Long saveOrUpdateDef(PcBuildDef record) {
		BinaryUtils.checkEmpty(record, "record");
		
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		record.setMntId(user.getMerchent().getId());
		
		boolean isadd = record.getId() == null;
		if(isadd) {
			BinaryUtils.checkEmpty(record.getBuildName(), "record.buildName");
			BinaryUtils.checkEmpty(record.getIsExternal(), "record.isExternal");
			if(record.getIsExternal().intValue() == 0) {
				BinaryUtils.checkEmpty(record.getProductId(), "record.productId");
				BinaryUtils.checkEmpty(record.getProjectId(), "record.projectId");
			}
			BinaryUtils.checkEmpty(record.getRespType(), "record.respType");
			BinaryUtils.checkEmpty(record.getRespUrl(), "record.respUrl");
			BinaryUtils.checkEmpty(record.getRespUser(), "record.respUser");
			BinaryUtils.checkEmpty(record.getRespPwd(), "record.respPwd");
			
			BinaryUtils.checkEmpty(record.getRespBranch(), "record.respBranch");
			BinaryUtils.checkEmpty(record.getDepTag(), "record.depTag");
//			BinaryUtils.checkEmpty(record.getBuildCmd(), "record.buildCmd");
//			BinaryUtils.checkEmpty(record.getIsSupportHook(), "record.isSupportHook");
//			BinaryUtils.checkEmpty(record.getIsBuildImage(), "record.isBuildImage");
//			if(record.getIsBuildImage().intValue() == 1) {
				BinaryUtils.checkEmpty(record.getImageDefId(), "record.imageDefId");
				BinaryUtils.checkEmpty(record.getDockerFilePath(), "record.dockerFilePath");
//				BinaryUtils.checkEmpty(record.getIsAutoPush1(), "record.isAutoPush1");
//				if(record.getIsAutoPush1().intValue() == 1) {
//					BinaryUtils.checkEmpty(record.getDataCenterId(), "record.dataCenterId");
//					BinaryUtils.checkEmpty(record.getResCenterId(), "record.resCenterId");
//				}
//			}
		}else {
			if(record.getBuildName() != null) BinaryUtils.checkEmpty(record.getBuildName(), "record.buildName");
			if(record.getIsExternal() != null) BinaryUtils.checkEmpty(record.getIsExternal(), "record.isExternal");
			if(record.getProductId() != null) BinaryUtils.checkEmpty(record.getProductId(), "record.productId");
			if(record.getProjectId() != null) BinaryUtils.checkEmpty(record.getProjectId(), "record.projectId");
			if(record.getRespType() != null) BinaryUtils.checkEmpty(record.getRespType(), "record.respType");
			if(record.getRespUrl() != null) BinaryUtils.checkEmpty(record.getRespUrl(), "record.respUrl");
			if(record.getRespUser() != null) BinaryUtils.checkEmpty(record.getRespUser(), "record.respUser");
			if(record.getRespPwd() != null) BinaryUtils.checkEmpty(record.getRespPwd(), "record.respPwd");
//			if(record.getBuildCmd() != null) BinaryUtils.checkEmpty(record.getBuildCmd(), "record.buildCmd");
//			if(record.getIsSupportHook() != null) BinaryUtils.checkEmpty(record.getIsSupportHook(), "record.isSupportHook");
//			if(record.getIsBuildImage() != null) BinaryUtils.checkEmpty(record.getIsBuildImage(), "record.isBuildImage");
			if(record.getImageDefId() != null) BinaryUtils.checkEmpty(record.getImageDefId(), "record.imageDefId");
			if(record.getDockerFilePath() != null) BinaryUtils.checkEmpty(record.getDockerFilePath(), "record.dockerFilePath");
//			if(record.getIsAutoPush1() != null) BinaryUtils.checkEmpty(record.getIsAutoPush1(), "record.isAutoPush1");
//			if(record.getDataCenterId() != null) BinaryUtils.checkEmpty(record.getDataCenterId(), "record.dataCenterId");
//			if(record.getResCenterId() != null) BinaryUtils.checkEmpty(record.getResCenterId(), "record.resCenterId");
//			if(record.getIsBuildImage().intValue() == 1) {
				BinaryUtils.checkEmpty(record.getImageDefId(), "record.imageDefId");
				BinaryUtils.checkEmpty(record.getDockerFilePath(), "record.dockerFilePath");
				
				BinaryUtils.checkEmpty(record.getRespBranch(), "record.respBranch");
				BinaryUtils.checkEmpty(record.getDepTag(), "record.depTag");
//				BinaryUtils.checkEmpty(record.getIsAutoPush1(), "record.isAutoPush1");
//				if(record.getIsAutoPush1().intValue() == 1) {
//					BinaryUtils.checkEmpty(record.getDataCenterId(), "record.dataCenterId");
//					BinaryUtils.checkEmpty(record.getResCenterId(), "record.resCenterId");
//				}
//			}
		}
		return buildSvc.saveOrUpdateDef(record);
	}

	
	
	
	@Override
	public int removeDefById(Long id) {
		BinaryUtils.checkEmpty(id, "id");
		PcBuildDef def = buildSvc.queryDefById(id);
		if(def == null) return 0;
		return buildSvc.removeDefById(id);
	}
	
	
	
	

}
