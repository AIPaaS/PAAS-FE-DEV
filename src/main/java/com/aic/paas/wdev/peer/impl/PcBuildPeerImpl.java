package com.aic.paas.wdev.peer.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.wdev.bean.CPcBuildDef;
import com.aic.paas.wdev.bean.PcBuildDef;
import com.aic.paas.wdev.bean.PcBuildDefInfo;
import com.aic.paas.wdev.peer.PcBuildPeer;
import com.aic.paas.wdev.rest.PcBuildSvc;
import com.aic.paas.wdev.util.HttpClientUtil;
import com.aic.paas.wdev.util.bean.PcBuildTaskCallBack;
import com.binary.core.util.BinaryUtils;
import com.binary.jdbc.Page;

public class PcBuildPeerImpl implements PcBuildPeer {
	static final Logger logger = LoggerFactory.getLogger(PcBuildPeerImpl.class);
	
	private String paasTaskUrl;
	public void setPaasTaskUrl(String paasTaskUrl) {
		if(paasTaskUrl != null) {
			this.paasTaskUrl = paasTaskUrl.trim();
		}
	}
	
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
		String userCode = user.getUserCode();
		String mntCode = user.getMerchent().getMntCode();
		
		boolean isadd = record.getId() == null;
		if(isadd) {
			BinaryUtils.checkEmpty(record.getBuildName(), "record.buildName");
			record.setIsExternal(0);//新增构建定义时，默认0不是外部工程
//			BinaryUtils.checkEmpty(record.getIsExternal(), "record.isExternal");
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
			BinaryUtils.checkEmpty(record.getImageDefId(), "record.imageDefId");
			BinaryUtils.checkEmpty(record.getDockerFilePath(), "record.dockerFilePath");
		}else {
			if(record.getBuildName() != null) BinaryUtils.checkEmpty(record.getBuildName(), "record.buildName");
			if(record.getIsExternal() != null) BinaryUtils.checkEmpty(record.getIsExternal(), "record.isExternal");
			if(record.getProductId() != null) BinaryUtils.checkEmpty(record.getProductId(), "record.productId");
			if(record.getProjectId() != null) BinaryUtils.checkEmpty(record.getProjectId(), "record.projectId");
			if(record.getRespType() != null) BinaryUtils.checkEmpty(record.getRespType(), "record.respType");
			if(record.getRespUrl() != null) BinaryUtils.checkEmpty(record.getRespUrl(), "record.respUrl");
			if(record.getRespUser() != null) BinaryUtils.checkEmpty(record.getRespUser(), "record.respUser");
			if(record.getRespPwd() != null) BinaryUtils.checkEmpty(record.getRespPwd(), "record.respPwd");
			if(record.getImageDefId() != null) BinaryUtils.checkEmpty(record.getImageDefId(), "record.imageDefId");
			if(record.getDockerFilePath() != null) BinaryUtils.checkEmpty(record.getDockerFilePath(), "record.dockerFilePath");
			BinaryUtils.checkEmpty(record.getImageDefId(), "record.imageDefId");
			BinaryUtils.checkEmpty(record.getDockerFilePath(), "record.dockerFilePath");
			
			BinaryUtils.checkEmpty(record.getRespBranch(), "record.respBranch");
			BinaryUtils.checkEmpty(record.getDepTag(), "record.depTag");
		}
		return buildSvc.saveOrUpdateDef(record,userCode,mntCode);
	}

	
	
	
	@Override
	public int removeDefById(Long build_id,String namespace,String repo_name) {
		BinaryUtils.checkEmpty(build_id, "build_id");

		PcBuildDef def = buildSvc.queryDefById(build_id);
		if(def == null) return 0;
		
		String address = paasTaskUrl+"/dev/buildMvc/deleteBuild"; //"http://localhost:16009/paas-task/dev/buildMvc/deleteBuild";
		String param = "namespace="+namespace+"&repo_name="+repo_name;
		String result = null;
		try {
			logger.info("---------构建删除-----------wdev工程------调task工程 入参address+param："+address+"?"+param);
			result = HttpClientUtil.sendPostRequest(address, param);
			logger.info("---------构建删除-----------wdev工程------调task工程 回参result："+result);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		if(result!=null && "success".equals(result)){ //"status": "success", //error
			int rc = buildSvc.removeDefById(build_id);
			logger.info("---------构建删除-----------wdev工程------调dev后场 回参rc："+rc);
			return rc;
		}else{
			return -2;
		}

	}




	@Override
	public int checkBuildFullName(PcBuildDef record) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		record.setMntId(user.getMerchent().getId());
		return buildSvc.checkBuildFullName(record);
	}
	
	@Override
	public String queryCompRoomIdByCallBack(PcBuildTaskCallBack pbtc){
		return buildSvc.queryCompRoomIdByCallBack(pbtc);
	}
	
	

}
