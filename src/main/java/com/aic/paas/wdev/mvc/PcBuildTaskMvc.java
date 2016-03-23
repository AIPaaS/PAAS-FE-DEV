package com.aic.paas.wdev.mvc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.wdev.bean.BuildTaskRecord;
import com.aic.paas.wdev.bean.CPcBuildTask;
import com.aic.paas.wdev.bean.PcBuildTask;
import com.aic.paas.wdev.peer.EmailSenderPeer;
import com.aic.paas.wdev.peer.PcBuildTaskPeer;
import com.aic.paas.wdev.util.bean.PcBuildTaskCallBack;
import com.binary.core.util.BinaryUtils;
import com.binary.framework.util.ControllerUtils;
import com.binary.json.JSON;

@Controller
@RequestMapping("/dev/buildtask")
public class PcBuildTaskMvc {
	
	@Autowired
	PcBuildTaskPeer buildTaskPeer;
	@Autowired
	EmailSenderPeer emailSenderPer;
	@RequestMapping("/saveBuildTask")
	public void  saveBuildTask(HttpServletRequest request,HttpServletResponse response, Long id,String depTag,String imageDefId,String buildName,String imageFullName){
		
		PcBuildTask pbt = new PcBuildTask();
		pbt.setBuildDefId(id);
		pbt.setDepTag(depTag);
		pbt.setImageDefId(Long.parseLong(imageDefId));
		Long backBuildId = buildTaskPeer.saveBuildTask(pbt,buildName,imageFullName);
		ControllerUtils.returnJson(request, response, backBuildId);
	}
	
	@RequestMapping("/queryBuildTaskInfoList")
	public void queryBuildTaskInfoList(HttpServletRequest request, HttpServletResponse response,Integer pageNum,Integer pageSize,CPcBuildTask cdt,String orders){
		List<PcBuildTask> buildTasks = buildTaskPeer.queryBuildTaskList(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, buildTasks);
	}
	
	@RequestMapping("/updateBuildTaskStatusByBackId")
	public void updateBuildTaskStatusByBackId(HttpServletRequest request,HttpServletResponse response,Long buildDefId ,String backBuildId, String alls){
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		String namespace = user.getMerchent().getMntCode()+"_____"+user.getUserCode();
		//String back_build_id = backBuildId.toString();
		String repo_name = alls; //产品code/工程code/构建名
		
		String result = buildTaskPeer.updatePcBuildTaskApi(namespace, backBuildId, repo_name);
		int cc = -1;
		if(result!=null && "aborted".equals(result)){  // "status": "aborted",  //error 为不存在此构建
			 PcBuildTask record =new PcBuildTask();//更新的映射对象
			 record.setStatus(3);// 3=构建已中断
			 Long timeL =BinaryUtils.getNumberDateTime();
			 record.setModifyTime(timeL);// yyyyMMddHHmmss    
			 
			 CPcBuildTask cdt = new  CPcBuildTask();//条件对象
			 cdt.setBuildDefId(buildDefId);
			 cdt.setStatus(2);//构建中的
			 cdt.setBackBuildIdEqual(backBuildId.toString());
			  cc = buildTaskPeer.updatePcBuildTaskCdt(record, cdt);
		}
		ControllerUtils.returnJson(request, response, cc);
	}
	
	
	@RequestMapping("/queryTaskRecord")
	public void queryTaskRecord(HttpServletRequest request, HttpServletResponse response, String repo_name, String build_id) throws IOException, URISyntaxException{
		BinaryUtils.checkEmpty(repo_name, "repo_name");
		BinaryUtils.checkEmpty(build_id, "build_id");
		BuildTaskRecord record =buildTaskPeer.queryTaskRecord(repo_name,build_id);
		ControllerUtils.returnJson(request, response, record);
	}
//	@RequestMapping("/updateBuildTaskByCallBack")
//	@ResponseBody
//	public String updateBuildTaskByCallBack(HttpServletRequest request,HttpServletResponse response, 
//			@RequestBody String sendParam){
	@RequestMapping(value="updateBuildTaskByCallBack")
	@ResponseBody
	public String updateBuildTaskByCallBack(@RequestBody String sendParam){
		String result ="error";
		PcBuildTaskCallBack pbtc = new PcBuildTaskCallBack();
		pbtc = JSON.toObject(sendParam ,PcBuildTaskCallBack.class);
		String imgRespId = pbtc.getImgRespId();
		
		String taskUserId = buildTaskPeer.updateBuildTaskByCallBack(pbtc, imgRespId);
		if("error".equals(taskUserId)||taskUserId==null||"".equals(taskUserId)){
			return result ;
		}
		String emailAddress = buildTaskPeer.queryEmailAdressByTaskUserId(taskUserId);
		String namespace = pbtc.getNamespace();
		String repo_name = pbtc.getRepo_name();
		String build_id = pbtc.getBuild_id();
		BuildTaskRecord buildTaskRecord = buildTaskPeer.queryTaskRecordToEmail(namespace, repo_name, build_id);
		//远程调用task接口，获得某一个构建的详情
		String status = buildTaskRecord.getStatus();
		Integer flag = 0;
		if("success".equals(status)){
			flag = 1;
		}
		emailSenderPer.sendBuildTaskResult(taskUserId, flag, buildTaskRecord.getStdout(), emailAddress);
		
		return result ;
//		ControllerUtils.returnJson(request, response, result);
	}


	
}
