package com.aic.paas.wdev.peer.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.wdev.bean.CPcImage;
import com.aic.paas.wdev.bean.CPcImageDef;
import com.aic.paas.wdev.bean.CPcImageDeploy;
import com.aic.paas.wdev.bean.ImageStatus;
import com.aic.paas.wdev.bean.PcBuildTask;
import com.aic.paas.wdev.bean.PcImage;
import com.aic.paas.wdev.bean.PcImageDef;
import com.aic.paas.wdev.bean.PcImageDefInfo;
import com.aic.paas.wdev.bean.PcImageDeploy;
import com.aic.paas.wdev.bean.PcImageInfo;
import com.aic.paas.wdev.peer.PcImagePeer;
import com.aic.paas.wdev.peer.UserAuthentication;
import com.aic.paas.wdev.rest.PcImageSvc;
import com.binary.core.http.HttpUtils;
import com.binary.core.util.BinaryUtils;
import com.binary.framework.exception.ServiceException;
import com.binary.jdbc.Page;
import com.binary.json.JSONObject;

import sun.util.logging.resources.logging;

public class PcImagePeerImpl implements PcImagePeer {
	
	static final Logger logger = LoggerFactory.getLogger(PcImagePeerImpl.class);
	
	@Autowired
	PcImageSvc imageSvc;
	
	@Autowired
	UserAuthentication userAuth;

	
	 
	

	@Override
	public Page<PcImageDefInfo> queryDefInfoPage(Integer pageNum, Integer pageSize, CPcImageDef cdt, String orders) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		if(cdt == null) cdt = new CPcImageDef();
		cdt.setMntId(user.getMerchent().getId());
		return imageSvc.queryDefInfoPage(pageNum, pageSize, cdt, orders);
	}

	
	
	 


	@Override
	public List<PcImageDefInfo> queryDefInfoList(CPcImageDef cdt, String orders) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		if(cdt == null) cdt = new CPcImageDef();
		cdt.setMntId(user.getMerchent().getId());
		return imageSvc.queryDefInfoList(cdt, orders);
	}


	@Override
	public List<PcImageDef> queryDefList(CPcImageDef cdt, String orders) {
		if(cdt == null) cdt = new CPcImageDef();
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		cdt.setMntId(user.getMerchent().getId());
		return imageSvc.queryDefList(cdt, orders);
	}
	
	@Override
	public PcImageDefInfo queryDefInfoById(Long id) {
		BinaryUtils.checkEmpty(id, "id");
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		CPcImageDef cdt = new CPcImageDef();
		cdt.setMntId(user.getMerchent().getId());
		cdt.setId(id);
		
		List<PcImageDefInfo> ls = imageSvc.queryDefInfoList(cdt, null);
		return ls.size()>0 ? ls.get(0) : null;
	}


	@Override
	public PcImageDef queryDefById(Long id) {
		BinaryUtils.checkEmpty(id, "id");
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		CPcImageDef cdt = new CPcImageDef();
		cdt.setMntId(user.getMerchent().getId());
		cdt.setId(id);
		
		List<PcImageDef> ls = imageSvc.queryDefList(cdt, null);
		return ls.size()>0 ? ls.get(0) : null;
	}
	
	
	@Override
	public Long saveOrUpdateDef(PcImageDef record) {
		BinaryUtils.checkEmpty(record, "record");
		
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		record.setMntId(user.getMerchent().getId());
		
		boolean isadd = record.getId() == null;
		if(isadd) {
			BinaryUtils.checkEmpty(record.getDirName(), "record.dirName");
			BinaryUtils.checkEmpty(record.getImageName(), "record.imageName");
			BinaryUtils.checkEmpty(record.getIsExternal(), "record.isExternal");
			BinaryUtils.checkEmpty(record.getVersionNo(), "record.versionNo");
			if(record.getIsExternal().intValue() == 0) {
				BinaryUtils.checkEmpty(record.getProductId(), "record.productId");
				BinaryUtils.checkEmpty(record.getProjectId(), "record.projectId");
			}
		}else {
			if(record.getDirName() != null) BinaryUtils.checkEmpty(record.getDirName(), "record.dirName");
			if(record.getImageName() != null) BinaryUtils.checkEmpty(record.getImageName(), "record.imageName");
			if(record.getVersionNo() != null) BinaryUtils.checkEmpty(record.getVersionNo(), "record.versionNo");
			if(record.getIsExternal() != null) BinaryUtils.checkEmpty(record.getIsExternal(), "record.isExternal");
			if(record.getProductId() != null) BinaryUtils.checkEmpty(record.getProductId(), "record.productId");
			if(record.getProjectId() != null) BinaryUtils.checkEmpty(record.getProjectId(), "record.projectId");
		}
		
		
		if(record.getVersionNo() != null) {
			if(!record.getVersionNo().matches("[a-zA-Z0-9\\.]+")) {
				throw new ServiceException(" is wrong version-no '"+record.getVersionNo()+"'! ");
			}
		}
		
		if(record.getDirName() != null) {
			String dirName = record.getDirName();
			dirName = HttpUtils.formatContextPath(dirName);
			record.setDirName(dirName);
		}
		
		return imageSvc.saveOrUpdateDef(record);
	}

	@Override
	public int removeDefById(Long id) {
		BinaryUtils.checkEmpty(id, "id");
		PcImageDefInfo def = queryDefInfoById(id);
		if(def == null) return 0;
		return imageSvc.removeDefById(id);
	}
	
	
	
	@Override
	public List<PcImage> queryImageList(CPcImage cdt, String orders) {
		if(cdt == null) cdt = new CPcImage();
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		cdt.setMntId(user.getMerchent().getId());
		return imageSvc.queryImageList(cdt, orders);
	}

	
	@Override
	public PcImage queryImageById(Long imageId) {
		BinaryUtils.checkEmpty(imageId, "imageId");
		CPcImage cdt = new CPcImage();
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		cdt.setMntId(user.getMerchent().getId());
		cdt.setId(imageId);
		List<PcImage> ls = imageSvc.queryImageList(cdt, null);
		return ls.size()>0 ? ls.get(0) : null;
	}

	
	@Override
	public Page<PcImageInfo> queryImageInfoPage(Integer pageNum, Integer pageSize, CPcImage cdt, String orders) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		if(cdt == null) cdt = new CPcImage();
		cdt.setMntId(user.getMerchent().getId());
		return imageSvc.queryImageInfoPage(pageNum, pageSize, cdt, orders);
	}

	
	
	@Override
	public List<PcImageInfo> queryImageInfoList(CPcImage cdt, String orders) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		if(cdt == null) cdt = new CPcImage();
		cdt.setMntId(user.getMerchent().getId());
		return imageSvc.queryImageInfoList(cdt, orders);
	}
	
	

	@Override
	public PcImageInfo queryImageInfoById(Long id) {
		BinaryUtils.checkEmpty(id, "id");
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		CPcImage cdt = new CPcImage();
		cdt.setMntId(user.getMerchent().getId());
		cdt.setId(id);
		
		List<PcImageInfo> ls = imageSvc.queryImageInfoList(cdt, null);
		return ls.size()>0 ? ls.get(0) : null;
	}
	
	
	

	
	
	
	
	@Override
	public int removeImageById(Long id) {
		userAuth.verifyImageAuth(id);
		return imageSvc.removeImageById(id);
	}
	
	
	
	
	@Override
	public String deployImage2Dev(Long imageId, Long dataCenterId, Long resCenterId, String remark) {
		return deployImage(imageId, ImageStatus.SNAPSHOOT, ImageStatus.DEV, dataCenterId, resCenterId, remark);
	}
	
	
	@Override
	public String deployImage2Test(Long imageId, Long dataCenterId, Long resCenterId, String remark) {
		return deployImage(imageId, ImageStatus.SNAPSHOOT, ImageStatus.TEST, dataCenterId, resCenterId, remark);
	}
	
	@Override
	public String deployImage2Release(Long imageId, Long dataCenterId, Long resCenterId, String remark) {
		return deployImage(imageId, ImageStatus.SNAPSHOOT, ImageStatus.RELEASE, dataCenterId, resCenterId, remark);
	}
	
	
	
	private String verifyDeployImage(Long imageId, ImageStatus beforeStatus, ImageStatus afterStatus, Long dataCenterId, Long resCenterId) {
		userAuth.verifyImageAuth(imageId);
		
		if(beforeStatus == afterStatus) {
			return "发布目标状态错误["+afterStatus+"]!";
		}
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		CPcImage cdt = new CPcImage();
		cdt.setId(imageId);
		cdt.setMntId(user.getMerchent().getId());
		cdt.setStatus(beforeStatus.getValue());
		List<PcImage> imgls = imageSvc.queryImageList(cdt, null);
		if(imgls.size() == 0) {
			return "镜像["+imageId+"]不存在或状态["+beforeStatus+"]已被改变!";
		}
		PcImage image = imgls.get(0);
				
		//判断目标状态下资源环境下境像是否已存在
		cdt = new CPcImage();
		cdt.setDataCenterId(dataCenterId);
		cdt.setResCenterId(resCenterId);
		cdt.setStatus(afterStatus.getValue());
		List<PcImage> ls = imageSvc.queryImageListByFullName(image.getImageFullName(), cdt, null);
		if(ls.size() > 0) {
			return "目标环境["+afterStatus+"]资源中心已存在镜像["+image.getImageFullName()+"].";
		}
		
		//判断目标状态下资源环境下境像是否正在发布
		CPcImageDeploy depcdt = new CPcImageDeploy();
		depcdt.setImageId(image.getId());
		depcdt.setDepStatuss(new Integer[]{1,2});	//1=就绪  2=发布中  3=成功  4=失败
		depcdt.setDataCenterId(dataCenterId);
		depcdt.setResCenterId(resCenterId);
		List<PcImageDeploy> depls = imageSvc.queryDeployList(depcdt, null);
		if(depls.size() > 0) {
			return "目标环境["+afterStatus+"]资源中心正在发布当前镜像["+image.getImageFullName()+"].";
		}
		return null;
	}
	
	
	
	private String deployImage(Long imageId, ImageStatus beforeStatus, ImageStatus afterStatus, Long dataCenterId, Long resCenterId, String remark) {
		BinaryUtils.checkEmpty(imageId, "imageId");
		BinaryUtils.checkEmpty(beforeStatus, "beforeStatus");
		BinaryUtils.checkEmpty(afterStatus, "afterStatus");
		BinaryUtils.checkEmpty(dataCenterId, "dataCenterId");
		BinaryUtils.checkEmpty(resCenterId, "resCenterId");
		
		String msg = verifyDeployImage(imageId, beforeStatus, afterStatus, dataCenterId, resCenterId);
		if(!BinaryUtils.isEmpty(msg)) return msg;
		Long deployId = imageSvc.deployImage(imageId, beforeStatus, afterStatus, dataCenterId, resCenterId, remark);
		
		System.out.println("===========================调用发布镜像接口: " + deployId);
		return null;
	}



	@Override
	public Page<PcImageDeploy> queryDeployPageByImageId(Integer pageNum, Integer pageSize, Long imageId, CPcImageDeploy cdt, String orders) {
		BinaryUtils.checkEmpty(imageId, "imageId");
		userAuth.verifyImageAuth(imageId);
		if(cdt == null) cdt = new CPcImageDeploy();
		cdt.setImageId(imageId);
		return imageSvc.queryDeployPage(pageNum, pageSize, cdt, orders);
	}



	@Override
	public JSONObject upLoadImage(String defid, String image_name, String tag,String export_file_url) {
		JSONObject resultinfo=new JSONObject();
		PcBuildTask buildTask=new PcBuildTask();
		Map<String, String> uploadMap=new HashMap<>();
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		buildTask.setImageDefId(Long.parseLong(defid));
		buildTask.setTaskUserId(user.getId());
		buildTask.setTaskUserName(user.getUserName());
		uploadMap.put("image_name",image_name);
		uploadMap.put("tag",tag);
		uploadMap.put("export_file_url", export_file_url);
		logger.info("buildTask: "+ buildTask.toString());
		logger.info("upLoadMap:" + uploadMap.toString());
		String data=imageSvc.uploadImage(buildTask, uploadMap);
		logger.info("result :" +data);
		if(BinaryUtils.isEmpty(data)){
			resultinfo.put("result", "error");
		}else{
			resultinfo.put("result", data);
		}
		  
		return resultinfo;
	}



	 


	
	
	
	












}
