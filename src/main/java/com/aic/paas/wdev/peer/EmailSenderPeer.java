package com.aic.paas.wdev.peer;

public interface EmailSenderPeer {
	
	
	
	
	/**
	 * 构建邮件通知
	 * @param taskUserId  构建人的Id
	 * @param buildTaskResult 1=构建成功  2=构建失败
	 * @param build_id 构建Id
	 * @param buildTaskRemark 构建详细信息 
	 * @param email 收件人邮箱地址
	 */
	public void sendBuildTaskResult(String taskUserId, Integer buildTaskResult,String build_id, String buildTaskRemark, String email);
	
	
}
