package com.aic.paas.wdev.peer;



/**
 * 用户认证
 * @author wanwb
 */
public interface UserAuthentication {

	
	
	/**
	 * 验证当前系统用户是否具有操作指定产品权限
	 * @param productId
	 */
	public void verifyUserProductAuth(Long productId);
	
	
	
	
	/**
	 * 验证当前系统用户是否具有操作指定工程权限
	 * @param projectId
	 */
	public void verifyUserProjectAuth(Long projectId);
	
	
	
	/**
	 * 验证当前系统用户是否具有操作指定镜像权限
	 * @param imageId
	 */
	public void verifyImageAuth(Long imageId);

	
	
}
