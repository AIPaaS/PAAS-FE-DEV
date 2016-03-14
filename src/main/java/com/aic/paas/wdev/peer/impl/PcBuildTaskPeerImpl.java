package com.aic.paas.wdev.peer.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.wdev.bean.BuildTaskRecord;
import com.aic.paas.wdev.bean.CPcBuildTask;
import com.aic.paas.wdev.bean.PcBuildTask;
import com.aic.paas.wdev.peer.PcBuildTaskPeer;
import com.aic.paas.wdev.rest.PcBuildTaskSvc;
import com.aic.paas.wdev.util.HttpClientUtil;
import com.aic.paas.wdev.util.HttpRequestUtil;
import com.alibaba.dubbo.common.json.JSONObject;
import com.binary.core.http.HttpClient;
import com.binary.core.util.BinaryUtils;
import com.binary.json.JSON;

public class PcBuildTaskPeerImpl implements PcBuildTaskPeer {
	
	private String paasTaskUrl;
	public void setPaasTaskUrl(String paasTaskUrl) {
		if(paasTaskUrl != null) {
			this.paasTaskUrl = paasTaskUrl.trim();
		}
	}
	
	@Autowired
	PcBuildTaskSvc buildTaskSvc;
	
	
	public Long saveBuildTask(PcBuildTask record){
		BinaryUtils.checkEmpty(record, "record");
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		String mntCode = user.getMerchent().getMntCode();
		record.setTaskUserId(user.getId());
		record.setTaskUserName(user.getUserName());
			
		return buildTaskSvc.saveBuildTask(record,mntCode);
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

	@Override
	public int updatePcBuildTaskCdt(PcBuildTask record, CPcBuildTask cdt) {
		if(BinaryUtils.isEmpty(record) || BinaryUtils.isEmpty(cdt)) {
			return 0;
		}else{
			return buildTaskSvc.updatePcBuildTaskCdt(record, cdt);
		}
	}

	@Override
	public String updatePcBuildTaskApi(String namespace, String back_build_id, String repo_name) {
		//http://localhost:16009/paas-task/dev/buildTaskMvc/stopBuilding";
		String address = paasTaskUrl+"/dev/buildTaskMvc/stopBuilding"; 
		String param = "namespace="+namespace+"&build_id="+back_build_id+"&repo_name="+repo_name;	
		String result  = HttpRequestUtil.sendPost(address, param);
		return result;
	}
	
	@Override
	public BuildTaskRecord queryTaskRecord(JSONObject object) throws IOException, URISyntaxException {
		BuildTaskRecord record=new BuildTaskRecord();
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		BinaryUtils.checkEmpty(user.getMerchent().getMntCode(), "mntCode");
		object.put("namespace", user.getMerchent().getMntCode());
		String data=HttpClientUtil.sendPostRequest("http://127.0.0.1:16009/paas-task/dev/buildTask/queryTaskRecord", object.toString());
		if(data==null ||data.equals("")){
			record.setResultCode("999999");
			record.setResultMsg("请求失败");
			return record;
		}
		record=JSON.toObject(data, BuildTaskRecord.class);
		return record;
	}

	
	
	 
}
