package com.aic.paas.wdev.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aic.paas.wdev.bean.CPcBuildDef;
import com.aic.paas.wdev.bean.PcBuildDef;
import com.aic.paas.wdev.bean.PcBuildDefInfo;
import com.aic.paas.wdev.peer.PcBuildPeer;
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
		int c = buildPeer.removeDefById(id);
		ControllerUtils.returnJson(request, response, c);
	}
	
	
}
