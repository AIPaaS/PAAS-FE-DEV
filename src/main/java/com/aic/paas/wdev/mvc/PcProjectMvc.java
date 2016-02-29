package com.aic.paas.wdev.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aic.paas.frame.cross.bean.DropRecord;
import com.aic.paas.frame.util.ComponentUtil;
import com.aic.paas.wdev.bean.CPcCompRoom;
import com.aic.paas.wdev.bean.CPcProduct;
import com.aic.paas.wdev.bean.CPcProject;
import com.aic.paas.wdev.bean.PcCompRoom;
import com.aic.paas.wdev.bean.PcProduct;
import com.aic.paas.wdev.bean.PcProject;
import com.aic.paas.wdev.bean.ProjectMgrInfo;
import com.aic.paas.wdev.bean.ProjectProdInfo;
import com.aic.paas.wdev.peer.PcProductPeer;
import com.aic.paas.wdev.peer.PcProjectPeer;
import com.aic.paas.wdev.peer.PsResPeer;
import com.binary.core.lang.Conver;
import com.binary.core.util.BinaryUtils;
import com.binary.framework.util.ControllerUtils;
import com.binary.jdbc.Page;

@Controller
@RequestMapping("/dev/project")
public class PcProjectMvc {
	
	
	@Autowired
	PcProjectPeer projectPeer;
	
	@Autowired
	PcProductPeer productPeer;
	
	@Autowired
	PsResPeer resPeer;
	
	@RequestMapping("/getProjectDropList")
	public void getProjectDropList(HttpServletRequest request, HttpServletResponse response, Boolean addEmpty, Boolean addAttr, Long productId) {
		CPcProject cdt = new CPcProject();
		cdt.setStatus(1);
		List<PcProject> list = projectPeer.queryProjectList(productId, cdt, "CODE , ID");
		List<DropRecord> dropList = ComponentUtil.toDropList(list, "ID", "name", addAttr, addEmpty);
		ControllerUtils.returnJson(request, response, dropList);
	}
	
	
	/**
	 * @param opts pro|room
	 */
	@RequestMapping("/getProductInfoDropListMap")
	public void getProductInfoDropListMap(HttpServletRequest request, HttpServletResponse response, Boolean addEmpty, Boolean addAttr) {
		Map<String,List<DropRecord>> map = new HashMap<String,List<DropRecord>>();
		
		CPcProduct cdt = new CPcProduct();
		cdt.setStatus(1);
		List<PcProduct> list = productPeer.queryMgrList(cdt, "CODE , ID");
		List<DropRecord> dropList = ComponentUtil.toDropList(list, "ID", "name", addAttr, addEmpty);
		map.put("pro", dropList);
		
		CPcCompRoom roomCdt = new CPcCompRoom();
		roomCdt.setStatus(1);
        List<PcCompRoom> roomlist = resPeer.queryCompRoomList(roomCdt, "ROOM_CODE, ID");
        List<DropRecord> roomDropList = ComponentUtil.toDropList(roomlist, "ID", "roomName", addAttr, addEmpty);
        map.put("room", roomDropList);
	
		ControllerUtils.returnJson(request, response, map);
	}
	
	@RequestMapping("/queryInfoPage")
	public void queryInfoPage(HttpServletRequest request,HttpServletResponse response,Integer pageNum, Integer pageSize, CPcProject cdt, String orders){
		Page<ProjectMgrInfo> page = projectPeer.queryInfoPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	
	@RequestMapping("/queryInfoById")
	public void queryInfoById(HttpServletRequest request,HttpServletResponse response,Integer pageNum, Integer pageSize,Long id){
		ProjectMgrInfo p = projectPeer.queryInfoById(id);
		ControllerUtils.returnJson(request, response, p);
	}
	
	@RequestMapping("/queryMgrProdInfoPage")
	public void queryMgrPage(HttpServletRequest request,HttpServletResponse response, Integer pageNum, Integer pageSize, CPcProject cdt, String orders){
		Page<ProjectProdInfo> page = projectPeer.queryMgrProdInfoPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	 
	@RequestMapping("/saveOrUpdate")
	public void saveOrUpdate(HttpServletRequest request,HttpServletResponse response,PcProject record,String strMgrIds){
		long projectId = projectPeer.saveOrUpdate(record);
		Long[] mgrIds = null;
		if(!BinaryUtils.isEmpty(strMgrIds)) {
			mgrIds = Conver.to(strMgrIds.trim().split(","), Long.class);
		}
		projectPeer.setProjectMgrs(projectId, mgrIds);
		ControllerUtils.returnJson(request, response, projectId);
	}
	
	@RequestMapping("/updateImageReg")
	public void updateRespSettings(HttpServletRequest request, HttpServletResponse response, Long id, Long productId, String imageRegUrl) {
		BinaryUtils.checkEmpty(id, "id");
		BinaryUtils.checkEmpty(productId, "productId");
		BinaryUtils.checkEmpty(imageRegUrl, "imageRegUrl");
		PcProject record = new PcProject();
		record.setId(id);
		record.setProductId(productId);
		record.setImageRegUrl(imageRegUrl);
		Long c = projectPeer.saveOrUpdate(record);
		ControllerUtils.returnJson(request, response, c);
	}
	
	@RequestMapping("/applyRespDoc")
	public void applyRespDoc(HttpServletRequest request, HttpServletResponse response,Long projectId, Integer respType){
		projectPeer.applyRespDoc(projectId, respType);
		ControllerUtils.returnJson(request, response, null);
	}

	
	@RequestMapping("/applyRespCode")
	public void applyRespCode(HttpServletRequest request, HttpServletResponse response,Long projectId, Integer respType){
		projectPeer.applyRespCode(projectId, respType);
		ControllerUtils.returnJson(request, response, null);
	}
	
}
