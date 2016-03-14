package com.aic.paas.wdev.mvc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.wdev.bean.BuildTaskRecord;
import com.aic.paas.wdev.bean.CPcBuildTask;
import com.aic.paas.wdev.bean.PcBuildTask;
import com.aic.paas.wdev.peer.PcBuildTaskPeer;
import com.alibaba.dubbo.common.json.JSONObject;
import com.binary.core.util.BinaryUtils;
import com.binary.framework.util.ControllerUtils;

@Controller
@RequestMapping("/dev/buildtask")
public class PcBuildTaskMvc {
	
	@Autowired
	PcBuildTaskPeer buildTaskPeer;
	
	
	@RequestMapping("/saveBuildTask")
	public void  saveBuildTask(HttpServletRequest request,HttpServletResponse response, Long id){
		
		PcBuildTask pbt = new PcBuildTask();
		pbt.setBuildDefId(id);
		Long backBuildId = buildTaskPeer.saveBuildTask(pbt);
		ControllerUtils.returnJson(request, response, backBuildId);
	}
	
	@RequestMapping("/queryBuildTaskInfoList")
	public void queryBuildTaskInfoList(HttpServletRequest request, HttpServletResponse response,Integer pageNum,Integer pageSize,CPcBuildTask cdt,String orders){
		List<PcBuildTask> buildTasks = buildTaskPeer.queryBuildTaskList(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, buildTasks);
	}
	
	@RequestMapping("/updateBuildTaskStatusByBackId")
	public void updateBuildTaskStatusByBackId(HttpServletRequest request,HttpServletResponse response, Long backBuildId, String alls){
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		String namespace = user.getMerchent().getMntCode();
		String back_build_id = backBuildId.toString();
		String repo_name = alls; //产品code/工程code/构建名
		
		String result = buildTaskPeer.updatePcBuildTaskApi(namespace, back_build_id, repo_name);
		//String address = paasTaskUrl+"/dev/buildTaskMvc/stopBuilding"; //http://localhost:16009/paas-task/dev/buildTaskMvc/stopBuilding";
		//String param = "namespace="+namespace+"&build_id="+back_build_id+"&repo_name="+repo_name;	
		//String result  = HttpRequestUtil.sendPost(address, param);
		
		int cc = -1;
		if(result!=null && "success".equals(result)){
			 PcBuildTask record =new PcBuildTask();//更新的映射对象
			 record.setStatus(3);// 3=构建已中断
			 Long timeL =BinaryUtils.getNumberDateTime();
			 record.setModifyTime(timeL);// yyyyMMddHHmmss    
			 
			 CPcBuildTask cdt = new  CPcBuildTask();//条件对象
			 cdt.setBackBuildId(backBuildId.toString());
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
	

	
}
