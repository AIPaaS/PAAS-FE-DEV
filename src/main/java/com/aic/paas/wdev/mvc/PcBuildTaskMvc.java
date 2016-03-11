package com.aic.paas.wdev.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aic.paas.wdev.bean.CPcBuildDef;
import com.aic.paas.wdev.bean.PcBuildDef;
import com.aic.paas.wdev.bean.PcBuildDefInfo;
import com.aic.paas.wdev.bean.PcBuildTask;
import com.aic.paas.wdev.peer.PcBuildPeer;
import com.aic.paas.wdev.peer.PcBuildTaskPeer;
import com.aic.paas.wdev.peer.PcDataCenterPeer;
import com.aic.paas.wdev.peer.PcResCenterPeer;
import com.binary.framework.util.ControllerUtils;
import com.binary.jdbc.Page;

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

	
}
