package com.aic.paas.wdev.peer.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.wdev.bean.CPcBuildTask;
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

	@Override
	public List<PcBuildTask> queryBuildTaskList(Integer pageNum, Integer pageSize, CPcBuildTask cdt, String orders) {
		BinaryUtils.checkEmpty(cdt.getBuildDefId(), "buildDefId");
		return buildTaskSvc.queryPcBuildTaskListForPage(pageNum, pageSize, cdt, orders);
	}

	@Override
	public List<PcBuildTask> selectTaskListByStatueId(Long buildDefId, Integer[] statuss) {
		BinaryUtils.checkEmpty(buildDefId, "buildDefId");
		BinaryUtils.checkEmpty(statuss, "statuss");
		List<PcBuildTask> list =buildTaskSvc.selectTaskListByStatueId(buildDefId,statuss);
		return list;
	}

}
