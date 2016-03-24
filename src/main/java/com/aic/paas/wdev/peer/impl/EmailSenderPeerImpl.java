package com.aic.paas.wdev.peer.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.wdev.peer.EmailSenderPeer;
import com.binary.core.io.FileSystem;
import com.binary.core.io.Resource;
import com.binary.core.io.ResourceResolver;
import com.binary.core.lang.StringLinker;
import com.binary.core.lang.StringUtils;
import com.binary.core.util.BinaryUtils;
import com.binary.framework.exception.ServiceException;
import com.binary.framework.util.ControllerUtils;
import com.binary.tools.mail.BinaryEmailSender;
public class EmailSenderPeerImpl implements EmailSenderPeer {

	
	@Autowired
	BinaryEmailSender emailSender;
	
	/** 当前系统入口 **/
	private String httpRoot;
		
	/** PAAS主页 **/
	private String homeUrl;
	
	
	
	public String getHomeUrl() {
		return homeUrl;
	}
	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}
	
	public String getHttpRoot() {
		return httpRoot;
	}
	public void setHttpRoot(String httpRoot) {
		BinaryUtils.checkEmpty(httpRoot, "httpRoot");
		this.httpRoot = ControllerUtils.formatContextPath(httpRoot).substring(1);
	}
	
	
	@Override
	public void sendBuildTaskResult(String taskUserId, Integer buildTaskResult, String buildTaskRemark, String email) {
		BinaryUtils.checkEmpty(taskUserId, "taskUserId");
		BinaryUtils.checkEmpty(buildTaskResult, "buildTaskResult");
		
		String title = "构建结果邮件通知";
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("logo_url", this.httpRoot+"/layout/img/paas_web_logo.png");
		param.put("taskUserId", taskUserId);
		param.put("home_url", this.homeUrl);
		param.put("buildTaskResult", buildTaskResult.intValue()==1 ? "<font color='#008800'>[成功]</font>" : "<font color='#ff0000'>[失败]</font>");
		if(!BinaryUtils.isEmpty(buildTaskRemark)) {
			buildTaskRemark = "<font color='red'>构建记录："+buildTaskRemark.replaceAll("\n", "<br>")+"</font>";
		}else {
			buildTaskRemark = "";
		}
		buildTaskRemark += "<br><br><br>如果疑问，请与平台管理员联系。";
		param.put("buildTaskRemark", buildTaskRemark);
		
		String content = "";
		try {
			InputStream is = null;
			try {
				Resource rs = ResourceResolver.getResource("classpath:tpl/mail/buildtask_check_result.tpl");
				is = rs.getInputStream();
				String s = FileSystem.read(is, "UTF-8");
				StringLinker sl = StringUtils.parseExpression(s);
				content = sl.link(param).toString();
			}finally {
				if(is != null) is.close();
			}
		}catch(Exception e) {
			throw BinaryUtils.transException(e, ServiceException.class);
		}
		
		emailSender.send(title, email, content);
	}
	
	
	
	
	
}
