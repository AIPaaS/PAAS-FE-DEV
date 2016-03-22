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
		String address = paasTaskUrl+"/dev/buildTaskMvc/stopBuilding"; 
		String param = "namespace="+namespace+"&build_id="+back_build_id+"&repo_name="+repo_name;	
		//String result  = HttpRequestUtil.sendPost(address, param);
		String result = null;
		try {
			result = HttpClientUtil.sendPostRequest(address, param);
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
		String data = HttpClientUtil.sendPostRequest(paasTaskUrl+"/dev/buildTaskMvc/queryTaskRecord",
				param.toString());
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


	@Override
	public String updateBuildTaskByCallBack(PcBuildTaskCallBack pbtc){
		String updateResult = "error";
		List<WsMerchent> list = new ArrayList<WsMerchent>();
		CWsMerchent cdt = new  CWsMerchent();
		cdt.setMntCodeEqual(pbtc.getNamespace());
		cdt.setStatus(1);//0=待审核  1=审核通过  2=审核退回
		cdt.setDataStatus(1);//数据状态：1-正常 0-删除
		list = merchentSvc.queryList(cdt, null);
		//1.根据租户code namespace[mnt_code]，获取租户id mnt_id []
		if(list!=null && list.size()>0){
			pbtc.setMnt_id(list.get(0).getId().toString());
		}
		if(pbtc.getMnt_id()==null ||"".equals(pbtc.getMnt_id().toString())){
			logger.info("========paas-wdev：PcBuildTaskPeerImpl：updateBuildTaskByCallBack：pbtc.getMnt_id() = 查询不到对应的租户Id");
			return updateResult;
		}
		//2.根据	根据回调函数，查询所属机房的Id
		String compRoomId = buildSvc.queryCompRoomIdByCallBack(pbtc);
		if("".equals(updateResult)||"error".equals(compRoomId)){
			return updateResult;
		}
		//3.根据机房Id，查询镜像库Id
		CPcImageRepository cir = new CPcImageRepository();
		cir.setRoomId(Long.parseLong(compRoomId));
		cir.setImgRespType(1);//imgRespType=1(是否快照镜像库)
		cir.setDataStatus(1);
		
		List<PcImageRepository> pirlist =imageRepositorySvc.queryList(cir, "ID");
		
		String imgRespId = "";//所属镜像库
		if(pirlist != null && pirlist.size()>0){
			if(pirlist.get(0).getId()!=null)imgRespId = pirlist.get(0).getId().toString();
		}		
		if("".equals(imgRespId)){
			logger.info("查询不到镜像库记录！");
			return updateResult;
		}
		return buildTaskSvc.updateBuildTaskByCallBack(pbtc,imgRespId);
	}
	 
	 
	
	public List<PcBuildTask> selectTaskListByCdt(CPcBuildTask cdt,String orders){
		if(BinaryUtils.isEmpty(cdt) ){
			return null;
		}
		return buildTaskSvc.selectTaskListByCdt(cdt, orders);
	}
	
	 
}
