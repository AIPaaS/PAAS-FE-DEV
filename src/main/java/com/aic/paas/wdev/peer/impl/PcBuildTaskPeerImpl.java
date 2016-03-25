package com.aic.paas.wdev.peer.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.bean.CWsMerchent;
import com.aic.paas.frame.cross.bean.SysOp;
import com.aic.paas.frame.cross.bean.WsMerchent;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.frame.cross.rest.MerchentSvc;
import com.aic.paas.wdev.bean.BuildTaskRecord;
import com.aic.paas.wdev.bean.CPcBuildTask;
import com.aic.paas.wdev.bean.CPcImageRepository;
import com.aic.paas.wdev.bean.PcBuildTask;
import com.aic.paas.wdev.bean.PcImageRepository;
import com.aic.paas.wdev.peer.PcBuildTaskPeer;
import com.aic.paas.wdev.rest.PcBuildSvc;
import com.aic.paas.wdev.rest.PcBuildTaskSvc;
import com.aic.paas.wdev.rest.PcImageRepositorySvc;
import com.aic.paas.wdev.rest.SysOpSvc;
import com.aic.paas.wdev.util.HttpClientUtil;
import com.aic.paas.wdev.util.bean.PcBuildTaskCallBack;
import com.binary.core.util.BinaryUtils;
import com.binary.json.JSON;
import com.binary.json.JSONObject;

public class PcBuildTaskPeerImpl implements PcBuildTaskPeer {
	static final Logger logger = LoggerFactory.getLogger(PcBuildTaskPeerImpl.class);
	private String paasTaskUrl;
	public void setPaasTaskUrl(String paasTaskUrl) {
		if(paasTaskUrl != null) {
			this.paasTaskUrl = paasTaskUrl.trim();
		}
	}
	
	@Autowired
	PcBuildTaskSvc buildTaskSvc;
	
	@Autowired
	MerchentSvc merchentSvc;
	
	@Autowired
	PcBuildSvc buildSvc;
	
	@Autowired
	PcImageRepositorySvc imageRepositorySvc;
	@Autowired
	SysOpSvc opSvc;	
	public Long saveBuildTask(PcBuildTask record,String buildName,String imageFullName){
		BinaryUtils.checkEmpty(record, "record");
		BinaryUtils.checkEmpty(buildName, "buildName");
		BinaryUtils.checkEmpty(imageFullName, "imageFullName");
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		String namespace = user.getMerchent().getMntCode() +"_____"+user.getUserCode();
		record.setTaskUserId(user.getId());
		record.setTaskUserName(user.getUserName());
		Long backBuildId = 0l;
		try {
			backBuildId =  buildTaskSvc.saveBuildTask(record,namespace,buildName,imageFullName);
		} catch (Exception e) {
			logger.error("", e);
		}
		return backBuildId;
		
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
		logger.info("---------构建删除-----------wdev工程------查询是否存在构建中的状态 入参buildDefId："+buildDefId+",statuss:"+statuss);
		List<PcBuildTask> list =buildTaskSvc.selectTaskListByStatueId(buildDefId,statuss);
		logger.info("---------构建删除-----------wdev工程------查询是否存在构建中的状态 回参list.size()："+list.size());
		return list;
	}

	@Override
	public int updatePcBuildTaskCdt(PcBuildTask record, CPcBuildTask cdt) {
		if(BinaryUtils.isEmpty(record) || BinaryUtils.isEmpty(cdt)) {
			return 0;
		}else{
			int cc = buildTaskSvc.updatePcBuildTaskCdt(record, cdt);
			logger.info("---------构建中止-----------wdev工程------掉dev后场updatePcBuildTaskCdt回餐cc："+cc);
			return cc;
		}
	}

	@Override
	public String updatePcBuildTaskApi(String namespace, String back_build_id, String repo_name) {
		String address = paasTaskUrl+"/dev/buildTaskMvc/stopBuilding"; 
		String param = "namespace="+namespace+"&build_id="+back_build_id+"&repo_name="+repo_name;	
		//String result  = HttpRequestUtil.sendPost(address, param);
		String result = null;
		try {
			logger.info("---------构建中止-----------wdev工程------掉task工程入参param："+param);
			logger.info("---------构建中止-----------wdev工程------掉task工程入参address："+address);
			result = HttpClientUtil.sendPostRequest(address, param);
			logger.info("---------构建中止-----------wdev工程------掉task工程回餐result："+result);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public BuildTaskRecord queryTaskRecord(String repo_name, String build_id) throws IOException, URISyntaxException {
		BuildTaskRecord record = new BuildTaskRecord();
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser) SystemUtil.getLoginUser();
		BinaryUtils.checkEmpty(repo_name, "repo_name");
		BinaryUtils.checkEmpty(build_id, "build_id");
		BinaryUtils.checkEmpty(user.getUserCode(), "userCode");
		BinaryUtils.checkEmpty(user.getMerchent().getMntCode(), "mntCode");
		JSONObject param=new JSONObject();
		param.put("namespace", user.getMerchent().getMntCode()+"_____"+user.getUserCode());
		param.put("repo_name", repo_name);
		param.put("build_id", build_id);
		logger.info("查询构建记录入参："+param.toString());
		String data = HttpClientUtil.sendPostRequest(paasTaskUrl+"/dev/buildTaskMvc/queryTaskRecord",
				param.toString());
		logger.info("查询构建记录回参： "+data);
		if (data == null || data.equals("")) {
			record.setError_code("999999");
			record.setError_info("请求失败");
			return record;
		}
		record = JSON.toObject(data, BuildTaskRecord.class);
		if(!record.getStatus().equals("")&&record.getStatus()!=null){
			String status=record.getStatus();
			switch (status) {
			case "success":
				record.setStatus("成功");
				break;
			case "error":
				record.setStatus("失败");
				break;
			case "started":
				record.setStatus("构建运行中");
				break;
			case "aborted":
				record.setStatus("构建中断中");
				break;
			default:
				break;
			}
		}
		return record;
	}

	public List<PcBuildTask> selectTaskListByCdt(CPcBuildTask cdt,String orders){
		if(BinaryUtils.isEmpty(cdt) ){
			return null;
		}
		return buildTaskSvc.selectTaskListByCdt(cdt, orders);
	}
	@Override
	public String updateBuildTaskByCallBack(PcBuildTaskCallBack pbtc,String imgRespId){
		String taskUserId = buildTaskSvc.updateBuildTaskByCallBack(pbtc,imgRespId);
		if("error".equals(taskUserId)){
			return "error";
		}
		return taskUserId;
	}
	@Override
	public String queryEmailAdressByTaskUserId(String taskUserId){
		//根据主键Id[taskUserId]，查询SYS_OP表中的邮箱地址
		SysOp op = opSvc.queryById(Long.parseLong(taskUserId));
		return op.getEmailAdress();
	}
	@Override
	public BuildTaskRecord queryTaskRecordToEmail(String namespace,String repo_name, String build_id) {
		String stdout = "";
		BuildTaskRecord record = new BuildTaskRecord();
		BinaryUtils.checkEmpty(namespace, "namespace");
		BinaryUtils.checkEmpty(repo_name, "repo_name");
		BinaryUtils.checkEmpty(build_id, "build_id");

		JSONObject param=new JSONObject();
		param.put("namespace", namespace);
		param.put("repo_name", repo_name.substring(1));
		param.put("build_id", build_id);
		String data = "";
		try {
			data = HttpClientUtil.sendPostRequest(paasTaskUrl+"/dev/buildTaskMvc/queryTaskRecord",param.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		record = JSON.toObject(data, BuildTaskRecord.class);
		return record;
	}
	
	@Override
	public PcBuildTask searchBuildtaskStatus(Long[] buildDefIds) throws InterruptedException {
		PcBuildTask buildTask;
		for(int i=0;;i++){
			logger.info("执行次数："+i);
			buildTask=buildTaskSvc.selectLastestBuildTask(buildDefIds);
			if(buildTask.getStatus().equals(2)){
				Thread.sleep(20000);
			}else{
				logger.info("执行结束:"+buildTask.getBuildDefId());
				logger.info("执行结果："+buildTask.getStatus());
				break;
			}
			
		}
		return buildTask;
	}
	
	
}
