package com.aic.paas.wdev.mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.bean.DropRecord;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.frame.util.ComponentUtil;
import com.aic.paas.wdev.bean.CPcBuildTask;
import com.aic.paas.wdev.bean.CPcImage;
import com.aic.paas.wdev.bean.CPcImageDef;
import com.aic.paas.wdev.bean.CPcImageDeploy;
import com.aic.paas.wdev.bean.PcBuildTask;
import com.aic.paas.wdev.bean.PcImage;
import com.aic.paas.wdev.bean.PcImageDef;
import com.aic.paas.wdev.bean.PcImageDefInfo;
import com.aic.paas.wdev.bean.PcImageDeploy;
import com.aic.paas.wdev.bean.PcImageInfo;
import com.aic.paas.wdev.peer.PcBuildTaskPeer;
import com.aic.paas.wdev.peer.PcImageDefPeer;
import com.aic.paas.wdev.peer.PcImagePeer;
import com.binary.core.util.BinaryUtils;
import com.binary.framework.util.ControllerUtils;
import com.binary.jdbc.Page;

@Controller
@RequestMapping("/dev/image")
public class PcImageMvc {
	
	@Autowired
	PcImagePeer pcImagePeer;
	
	@Autowired
	PcBuildTaskPeer buildTaskPeer;
	
	@Autowired
	PcImageDefPeer imageDefPeer;
	
	@RequestMapping("/getDefDropList")
	public void getDefDropList(HttpServletRequest request, HttpServletResponse response, Boolean addEmpty, Boolean addAttr, CPcImageDef cdt) {
		if(cdt==null) cdt = new CPcImageDef();
		List<PcImageDef> list = pcImagePeer.queryDefList(cdt, "DIR_NAME , IMAGE_NAME");
		List<DropRecord> dropList = ComponentUtil.toDropList(list, "ID", "imageName", addAttr, addEmpty);
	
		ControllerUtils.returnJson(request, response, dropList);
	}
	
	@RequestMapping("/getImageDropList")
	public void getImageDropList(HttpServletRequest request, HttpServletResponse response, Boolean addEmpty, Boolean addAttr, CPcImage cdt, String orders) {
		if(cdt==null) cdt = new CPcImage();
		if(BinaryUtils.isEmpty(orders)) orders = "IMAGE_FULL_NAME";
		List<PcImage> list = pcImagePeer.queryImageList(cdt, orders);
		List<DropRecord> dropList = ComponentUtil.toDropList(list, "ID", "imageName", addAttr, addEmpty);
	
		ControllerUtils.returnJson(request, response, dropList);
	}
	
	@RequestMapping("/queryDefInfoPage")
	public void  queryDefInfoPage(HttpServletRequest request,HttpServletResponse response, Integer pageNum, Integer pageSize, CPcImageDef cdt, String orders){
		Page<PcImageDefInfo> page = pcImagePeer.queryDefInfoPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	
	
	@RequestMapping("/queryImageInfoPage")
	public void  queryImageInfoPage(HttpServletRequest request,HttpServletResponse response, Integer pageNum, Integer pageSize, CPcImage cdt, String orders){
		Page<PcImageInfo> page = pcImagePeer.queryImageInfoPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	
	@RequestMapping("/queryDefInfoById")
	public void  queryDefInfoById(HttpServletRequest request,HttpServletResponse response, Long id){
		PcImageDefInfo info = pcImagePeer.queryDefInfoById(id);
		ControllerUtils.returnJson(request, response, info);
	}

	
	@RequestMapping("/queryDefById")
	public void  queryDefById(HttpServletRequest request,HttpServletResponse response, Long id){
		PcImageDef def = pcImagePeer.queryDefById(id);
		ControllerUtils.returnJson(request, response, def);
	}
	
	@RequestMapping("/saveOrUpdateDef")
	public void  saveOrUpdateDef(HttpServletRequest request,HttpServletResponse response, PcImageDef record){
		
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		Long mntId = user.getMerchent().getId();

		Integer isExternal = record.getIsExternal();  //1=是 0=否
		String fullName = "";
		String imageName = record.getImageName();
		String versionNo = record.getVersionNo();
		String dirName =record.getDirName();
		
		if(isExternal!=null && isExternal.intValue()==1){
			fullName = "/" + dirName +"/"+ imageName + "-" +versionNo;	
		}
		if(isExternal!=null && isExternal.intValue()==0){
			fullName = dirName + "/"+ imageName + "-" +versionNo;		
		} 
		/*CPcImageDef cid = new CPcImageDef();
		cid.setImageFullName(fullName);
		List<PcImageDef> list =imageDefPeer.selectTaskListByCdt(cid, null);*/
		PcImageDef pcImageDef =imageDefPeer.selectDefByFullName(mntId, fullName);
		
		Long res_id = -999999l;
		if(pcImageDef==null || "".equals(pcImageDef)){
			res_id = pcImagePeer.saveOrUpdateDef(record);
		}
		ControllerUtils.returnJson(request, response, res_id);
	}

	@RequestMapping("/removeDefById")
	public void  removeDefById(HttpServletRequest request,HttpServletResponse response, Long id){
		int c = -1;
		CPcBuildTask cdt =new CPcBuildTask();
		cdt.setImageDefId(id);
		cdt.setStatus(2);//构建中的
		List<PcBuildTask> list =buildTaskPeer.selectTaskListByCdt(cdt, null);
		if(list!=null && list.size()==0){
		    c = pcImagePeer.removeDefById(id);
		}
		ControllerUtils.returnJson(request, response, c);
	}
	
	@RequestMapping("/queryDeployPageByImageId")
	public void  queryDeployPageByImageId(HttpServletRequest request,HttpServletResponse response, Integer pageNum, Integer pageSize, Long imageId, CPcImageDeploy cdt, String orders){
		Page<PcImageDeploy> page = pcImagePeer.queryDeployPageByImageId(pageNum, pageSize, imageId, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	
		
	@RequestMapping("/queryImageById")
	public void  queryImageById(HttpServletRequest request,HttpServletResponse response, Long imageId){
		PcImage p = pcImagePeer.queryImageById(imageId);
		ControllerUtils.returnJson(request, response, p);
	}
	
	@RequestMapping("/removeImageById")
	public void  removeImageById(HttpServletRequest request,HttpServletResponse response, Long id){
		int c = pcImagePeer.removeImageById(id);
		ControllerUtils.returnJson(request, response, c);
	}
	
	
	@RequestMapping("/deployImage2Dev")
	public void  deployImage2Dev(HttpServletRequest request,HttpServletResponse response, Long imageId, Long dataCenterId, Long resCenterId, String remark){
		String msg = pcImagePeer.deployImage2Dev(imageId, dataCenterId, resCenterId, remark);
		ControllerUtils.returnJson(request, response, msg);
	}

	
	@RequestMapping("/deployImage2Test")
	public void  deployImage2Test(HttpServletRequest request,HttpServletResponse response, Long imageId, Long dataCenterId, Long resCenterId, String remark){
		String msg = pcImagePeer.deployImage2Test(imageId, dataCenterId, resCenterId, remark);
		ControllerUtils.returnJson(request, response, msg);
	}
	
	@RequestMapping("/deployImage2Release")
	public void  deployImage2Release(HttpServletRequest request,HttpServletResponse response, Long imageId, Long dataCenterId, Long resCenterId, String remark){
		String msg = pcImagePeer.deployImage2Release(imageId, dataCenterId, resCenterId, remark);
		ControllerUtils.returnJson(request, response, msg);
	}
	
	
}
