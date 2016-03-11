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
import com.binary.core.util.BinaryUtils;
import com.binary.framework.util.ControllerUtils;

@Controller
@RequestMapping("/dev/buildtask")
public class PcBuildTaskMvc {
	
	@Autowired
	PcBuildTaskPeer buildTaskPeer;
	
	
	@RequestMapping("/saveOrUpdateBuildTask")
	public void  saveOrUpdateBuildTask(HttpServletRequest request,HttpServletResponse response, Long buildDefId){
		System.out.println("aaaaa");
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
	
	@RequestMapping("/updateBuildTaskStatusByBackId")
	public void updateBuildTaskStatusByBackId(HttpServletRequest request,HttpServletResponse response, Long backBuildId){
		 PcBuildTask record =new PcBuildTask();//更新的映射对象
		 record.setStatus(3);// 3=构建已中断
		 Long timeL =BinaryUtils.getNumberDateTime();
		 record.setModifyTime(timeL);// yyyyMMddHHmmss    
		 
		 CPcBuildTask cdt = new  CPcBuildTask();//条件对象
		 cdt.setBackBuildId(backBuildId.toString());
		 int cc = buildTaskPeer.updatePcBuildTaskCdt(record, cdt);
		ControllerUtils.returnJson(request, response, cc);
	}
	
	

	
}
