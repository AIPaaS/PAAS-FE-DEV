package com.aic.paas.wdev.mvc;

import java.util.List;

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
@RequestMapping("/dev/build")
public class PcBuildMvc {
	
	@Autowired
	PcBuildPeer buildPeer;
	
	@Autowired
	PcDataCenterPeer pcDataCenterPeer;
	
	@Autowired
	PcResCenterPeer pcResCenterPeer;
	
	@Autowired
	PcBuildTaskPeer buildTaskPeer;
	
	
	@RequestMapping("/queryDefInfoPage")
	public void  queryDefInfoPage(HttpServletRequest request,HttpServletResponse response, Integer pageNum, Integer pageSize, CPcBuildDef cdt, String orders){
		Page<PcBuildDefInfo> page = buildPeer.queryDefInfoPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	
	@RequestMapping("/queryDefInfoById")
	public void  queryDefInfoById(HttpServletRequest request,HttpServletResponse response, Long id){
		PcBuildDefInfo p = buildPeer.queryDefInfoById(id);
		ControllerUtils.returnJson(request, response, p);
	}
	
	@RequestMapping("/saveOrUpdateDef")
	public void  saveOrUpdateDef(HttpServletRequest request,HttpServletResponse response, PcBuildDef record){
		record.setIsBuildImage(1);//一定生成镜像
		Long id = buildPeer.saveOrUpdateDef(record);
		ControllerUtils.returnJson(request, response, id);
	}

	
	@RequestMapping("/removeDefById")
	public void  removeDefById(HttpServletRequest request,HttpServletResponse response, Long id){
		
		
		Integer[] statuss = {2,3};  // 1=就绪    2=构建运行中   3=构建中断中     4=成功   5=失败
		List<PcBuildTask> taskList = buildTaskPeer.selectTaskListByStatueId(id, statuss);
		
		int c = -1; //构建运行中 ,构建中断中 标注
		if( taskList ==null || ( taskList!=null && taskList.size()==0 ) ){// （ 2=构建运行中   3=构建中断中 ）
			 c = buildPeer.removeDefById(id);
		}

		ControllerUtils.returnJson(request, response, c);
	}
	@RequestMapping("/checkBuildFullName")
	public void  checkBuildFullName(HttpServletRequest request,HttpServletResponse response, Long id,String buildName){
		PcBuildDef record = new PcBuildDef();
		record.setId(id);
		record.setBuildName(buildName);
		int id2 = buildPeer.checkBuildFullName(record);
		ControllerUtils.returnJson(request, response, id2);
	}
	
}
