package com.aic.paas.wdev.peer.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.wdev.bean.PcBuildTask;
import com.aic.paas.wdev.peer.PcBuildTaskPeer;
import com.aic.paas.wdev.rest.PcBuildTaskSvc;
import com.binary.core.util.BinaryUtils;

public class PcBuildTaskPeerImpl implements PcBuildTaskPeer {
	
	
	@Autowired
	PcBuildTaskSvc buildTaskSvc;
	
	public Long saveOrUpdateBuildTask(PcBuildTask record){
		BinaryUtils.checkEmpty(record, "record");
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
//		user.getMerchent().getMntCode();
		boolean isadd = record.getId() == null;
		
		if(isadd) {
			record.setTaskUserId(user.getId());
			record.setTaskUserName(user.getUserName());
			record.setStatus(1);//任务状态：就绪
			record.setDataStatus(1);
		}else{
			
		}
			
		return buildTaskSvc.saveOrUpdateBuildTask(record);
	}

}
