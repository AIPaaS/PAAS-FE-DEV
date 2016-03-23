package com.aic.paas.wdev.peer;

public interface EmailSenderPeer {
	
	
	
	
	/**
	 * 构建邮件通知
	 * @param taskUserId  构建人的Id
	 * @param buildTaskResult 1=审核通过  2=审核退回
	 * @param buildTaskRemark 
	 */
	public void sendBuildTaskResult(String taskUserId, Integer buildTaskResult, String buildTaskRemark, String email);
	
	
}
