package com.aic.paas.wdev.mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aic.paas.wdev.bean.CPcBuildTask;
import com.aic.paas.wdev.bean.PcBuildTask;
import com.aic.paas.wdev.peer.PcBuildTaskPeer;
import com.binary.framework.util.ControllerUtils;

@Controller
@RequestMapping("/dev/buildtask")
public class PcBuildTaskMvc {
	
	@Autowired
	PcBuildTaskPeer buildTaskPeer;
	
	
	@RequestMapping("/saveOrUpdateBuildTask")
	public void  saveOrUpdateBuildTask(HttpServletRequest request,HttpServletResponse response, Long buildDefId){
		PcBuildTask pbt = new PcBuildTask();
		pbt.setBuildDefId(buildDefId);
		Long id = buildTaskPeer.saveOrUpdateBuildTask(pbt);
		ControllerUtils.returnJson(request, response, id);
	}
	
	@RequestMapping("/queryBuildTaskInfoList")
	public void queryBuildTaskInfoList(HttpServletRequest request, HttpServletResponse response,Integer pageNum,Integer pageSize,CPcBuildTask cdt,String orders){
		List<PcBuildTask> buildTasks = buildTaskPeer.queryBuildTaskList(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, buildTasks);
	}
	
	

	
}
