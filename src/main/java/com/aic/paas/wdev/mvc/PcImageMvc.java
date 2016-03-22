package com.aic.paas.wdev.mvc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
import com.binary.framework.exception.ServiceException;
import com.binary.framework.util.ControllerUtils;
import com.binary.jdbc.Page;
import com.binary.json.JSONObject;

@Controller
@RequestMapping("/dev/image")
public class PcImageMvc {
	static final Logger logger = LoggerFactory.getLogger(PcImageMvc.class);
	
	@Autowired
	PcImagePeer pcImagePeer;

	@Autowired
	PcBuildTaskPeer buildTaskPeer;

	@Autowired
	PcImageDefPeer imageDefPeer;
	
	@Value("${upLoad.folder.url}")
	String folderUrl;
	
	@Value("${project.http.root}")
	String rootURl;
	
	@RequestMapping("/getDefDropList")
	public void getDefDropList(HttpServletRequest request, HttpServletResponse response, Boolean addEmpty,
			Boolean addAttr, CPcImageDef cdt) {
		if (cdt == null)
			cdt = new CPcImageDef();
		List<PcImageDef> list = pcImagePeer.queryDefList(cdt, "DIR_NAME , IMAGE_NAME");
		List<DropRecord> dropList = ComponentUtil.toDropList(list, "ID", "imageName", addAttr, addEmpty);
		ControllerUtils.returnJson(request, response, dropList);
	}

	@RequestMapping("/getImageDropList")
	public void getImageDropList(HttpServletRequest request, HttpServletResponse response, Boolean addEmpty,
			Boolean addAttr, CPcImage cdt, String orders) {
		
		if (cdt == null)
			cdt = new CPcImage();
		if (BinaryUtils.isEmpty(orders))
			orders = "IMAGE_FULL_NAME";
		List<PcImage> list = pcImagePeer.queryImageList(cdt, orders);
		List<DropRecord> dropList = ComponentUtil.toDropList(list, "ID", "imageName", addAttr, addEmpty);
		ControllerUtils.returnJson(request, response, dropList);
	}

	@RequestMapping("/queryDefInfoPage")
	public void queryDefInfoPage(HttpServletRequest request, HttpServletResponse response, Integer pageNum,
		Integer pageSize, CPcImageDef cdt, String orders) {
		Page<PcImageDefInfo> page = pcImagePeer.queryDefInfoPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}

	@RequestMapping("/queryImageInfoPage")
	public void queryImageInfoPage(HttpServletRequest request, HttpServletResponse response, Integer pageNum,
			Integer pageSize, CPcImage cdt, String orders) {
		Page<PcImageInfo> page = pcImagePeer.queryImageInfoPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}

	@RequestMapping("/queryDefInfoById")
	public void queryDefInfoById(HttpServletRequest request, HttpServletResponse response, Long id) {
		PcImageDefInfo info = pcImagePeer.queryDefInfoById(id);
		ControllerUtils.returnJson(request, response, info);
	}

	@RequestMapping("/queryDefById")
	public void queryDefById(HttpServletRequest request, HttpServletResponse response, Long id) {
		PcImageDef def = pcImagePeer.queryDefById(id);
		ControllerUtils.returnJson(request, response, def);
	}

	@RequestMapping("/saveOrUpdateDef")
	public void saveOrUpdateDef(HttpServletRequest request, HttpServletResponse response, PcImageDef record) {

		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser) SystemUtil.getLoginUser();
		Long mntId = user.getMerchent().getId();

		Integer isExternal = record.getIsExternal(); // 1=是 0=否
		String fullName = "";
		String imageName = record.getImageName();
		String versionNo = record.getVersionNo();
		String dirName = record.getDirName();

		if (isExternal != null && isExternal.intValue() == 1) {
			fullName = "/" + dirName + "/" + imageName + "-" + versionNo;
		}
		if (isExternal != null && isExternal.intValue() == 0) {
			fullName = dirName + "/" + imageName + "-" + versionNo;
		}
		/*
		 * CPcImageDef cid = new CPcImageDef(); cid.setImageFullName(fullName);
		 * List<PcImageDef> list =imageDefPeer.selectTaskListByCdt(cid, null);
		 */
		PcImageDef pcImageDef = imageDefPeer.selectDefByFullName(mntId, fullName);

		Long res_id = -999999l;
		if (pcImageDef == null || "".equals(pcImageDef)) {
			res_id = pcImagePeer.saveOrUpdateDef(record);
		}
		ControllerUtils.returnJson(request, response, res_id);
	}

	@RequestMapping("/removeDefById")
	public void removeDefById(HttpServletRequest request, HttpServletResponse response, Long id) {
		int c = -1;
		CPcBuildTask cdt = new CPcBuildTask();
		cdt.setImageDefId(id);
		cdt.setStatus(2);// 构建中的
		List<PcBuildTask> list = buildTaskPeer.selectTaskListByCdt(cdt, null);
		if (list != null && list.size() == 0) {
			c = pcImagePeer.removeDefById(id);
		}
		ControllerUtils.returnJson(request, response, c);
	}

	@RequestMapping("/queryDeployPageByImageId")
	public void queryDeployPageByImageId(HttpServletRequest request, HttpServletResponse response, Integer pageNum,
			Integer pageSize, Long imageId, CPcImageDeploy cdt, String orders) {
		Page<PcImageDeploy> page = pcImagePeer.queryDeployPageByImageId(pageNum, pageSize, imageId, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}

	@RequestMapping("/queryImageById")
	public void queryImageById(HttpServletRequest request, HttpServletResponse response, Long imageId) {
		PcImage p = pcImagePeer.queryImageById(imageId);
		ControllerUtils.returnJson(request, response, p);
	}

	@RequestMapping("/removeImageById")
	public void removeImageById(HttpServletRequest request, HttpServletResponse response, Long id) {
		int c = pcImagePeer.removeImageById(id);
		ControllerUtils.returnJson(request, response, c);
	}

	@RequestMapping("/deployImage2Dev")
	public void deployImage2Dev(HttpServletRequest request, HttpServletResponse response, Long imageId,
			Long dataCenterId, Long resCenterId, String remark) {
		String msg = pcImagePeer.deployImage2Dev(imageId, dataCenterId, resCenterId, remark);
		ControllerUtils.returnJson(request, response, msg);
	}

	@RequestMapping("/deployImage2Test")
	public void deployImage2Test(HttpServletRequest request, HttpServletResponse response, Long imageId,
			Long dataCenterId, Long resCenterId, String remark) {
		String msg = pcImagePeer.deployImage2Test(imageId, dataCenterId, resCenterId, remark);
		ControllerUtils.returnJson(request, response, msg);
	}

	@RequestMapping("/deployImage2Release")
	public void deployImage2Release(HttpServletRequest request, HttpServletResponse response, Long imageId,
			Long dataCenterId, Long resCenterId, String remark) {
		String msg = pcImagePeer.deployImage2Release(imageId, dataCenterId, resCenterId, remark);
		ControllerUtils.returnJson(request, response, msg);
	}

	@RequestMapping("/queryImageTags")
	public void queryImageTags(HttpServletRequest request, HttpServletResponse response, CPcImage cdt, String orders) {
		List<PcImage> pcImages = pcImagePeer.queryImageList(cdt, orders);
		ControllerUtils.returnJson(request, response, pcImages);
	}

	
	@RequestMapping("/uploadImage")
	@ResponseBody
	public String uploadImage(MultipartHttpServletRequest request, HttpServletResponse response) throws FileUploadException {
		JSONObject result=new JSONObject();
		String tag = request.getParameter("tag");
		String defid = request.getParameter("defid");
		String image_name = request.getParameter("image_name");
		String extName=request.getParameter("ext_name");
		BinaryUtils.checkEmpty(tag, "tag");
		BinaryUtils.checkEmpty(defid, "defid");
		BinaryUtils.checkEmpty(image_name, "image_name");
		BinaryUtils.checkEmpty(extName, "ext_name");
		boolean flag = false;
		String export_file_url=new String();
		Iterator<String> itr =  request.getFileNames();
		MultipartFile mpf = request.getFile(itr.next());
		File file=new File(folderUrl);
		if(!file.exists()&&!file.isDirectory()){
			file.mkdir();
		}
		String fileName=mpf.getOriginalFilename();
		fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
		if(fileName!=null&&!fileName.equals("")){
			String ExtName=this.getNewName(extName,fileName);
			File extFile=new File(folderUrl+"/"+ExtName);
			if(!extFile.exists()){
				try {
					mpf.transferTo(extFile);
					logger.info("上传成功");
					export_file_url=rootURl+"/downloadImage/"+ExtName;
					flag=true;
				} catch (IllegalStateException | IOException e) {
					flag=false;
					e.printStackTrace();
				}
			}
			
		}else{
			flag=false;
		}
		logger.info("export_file_url:"+export_file_url);
		if(flag){
			result = pcImagePeer.upLoadImage(defid, image_name, tag,export_file_url);
		}else{
			result.put("result","error");
		}
		return result.toString();
	}
	

	@RequestMapping("/downloadImage/**")
	public void downloadImage(HttpServletRequest request, HttpServletResponse response) {
		String url = request.getRequestURI();
		int idx = url.indexOf("/downloadImage/");
		if(idx < 0) throw new ServiceException(" is wrong url '"+url+"'! ");
		String fileName = url.substring(idx+15).trim();
		File file=new File(folderUrl+"/"+fileName);
	    BufferedInputStream bis = null;  
	    BufferedOutputStream bos = null;  
		if(!BinaryUtils.isEmpty(fileName)){
			 if(file.exists()&&file.isFile()){
				 try {
						//获取文件的长度
					    long fileLength = file.length();  
					    logger.info("fileName:" +fileName);
					    //设置文件输出类型
					    response.setContentType("application/octet-stream");  
					    response.setHeader("Content-disposition", "attachment; filename="  
					        + new String(fileName.getBytes("utf-8"), "ISO8859-1")); 
					    //设置输出长度
					    response.setHeader("Content-Length", String.valueOf(fileLength));  
					    //获取输入流
					    bis = new BufferedInputStream(new FileInputStream(file));  
					    //输出流
					    bos = new BufferedOutputStream(response.getOutputStream());  
					    byte[] buff = new byte[2048];  
					    int bytesRead;  
					    while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
					      bos.write(buff, 0, bytesRead);  
					    }  
					    //关闭流
					    bis.close();  
					    bos.close();  
				} catch (Exception e) {
					logger.info("下载失败");
					// TODO: handle exception
				}
				
				 
			 }
			
			 
		}
		return;
		
		
		  
		  
		 
		  
		   
	}
	
	
	private String  getNewName(String ext_name, String fileName) {
		 String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
		 if(fileName.equals("gz")){
			 String nameArray=fileName.substring(fileName.indexOf("."));
			return BinaryUtils.getNumberDateTime()+"_"+ext_name+"."+nameArray;
		 }else{
			 return BinaryUtils.getNumberDateTime()+"_"+ext_name+"."+suffix;
		 }
		
	}

	@RequestMapping("/delteImage/**")
	public void delteImage(HttpServletRequest request, HttpServletResponse response){
		String url = request.getRequestURI();
		int idx = url.indexOf("/delteImage/");
		if(idx < 0) throw new ServiceException(" is wrong url '"+url+"'! ");
		String fileName = url.substring(idx+12).trim();
		logger.info("fileName :"+ fileName);
		if(!BinaryUtils.isEmpty(fileName)){
			File file=new File(folderUrl+"/"+fileName);
			if(file.exists()){
				this.deleteFile(file);
				logger.info(fileName +"删除成功！");
			}
		}else{
			
		}
		
	}

	private void deleteFile(File file) {
		if(file.isFile()&&file.exists()){
			file.delete();
		}
	}
	
	 
	
	

}
