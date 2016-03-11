package com.aic.paas.wdev.peer.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.wdev.bean.PcBuildTask;
import com.aic.paas.wdev.peer.PcBuildTaskPeer;
import com.aic.paas.wdev.rest.PcBuildTaskSvc;
import com.binary.core.util.BinaryUtils;

public class PcBuildTaskPeerImpl implements PcBuildTaskPeer {
	
	
	@Autowired
	PcBuildTaskSvc buildTaskSvc;
	
	public Long saveOrUpdateBuildTask(PcBuildTask pbt){
		BinaryUtils.checkEmpty(pbt.getBuildDefId(), "id");
		return buildTaskSvc.saveOrUpdateBuildTask(pbt);
	}

}
