package com.aic.paas.wdev.util;


import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aic.paas.wdev.util.bean.SFTPConstants;
import com.jcraft.jsch.ChannelSftp;

public class SFTPUtils {

	private Logger log = Logger.getLogger(getClass());

	private  static String directory ;
	private  static String host ;
	private  static String username  ;
	private  static String password ;
	private  static String port ;

	 

	public void setDirectory(String directory) {
		this.directory = directory;
	}




	public void setHost(String host) {
		this.host = host;
	}




	public void setUsername(String username) {
		this.username = username;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public void setPort(String port) {
		this.port = port;
	}




	/**
	 * 上传文件
	 * 
	 * @param uploadFile
	 *            要上传的文件
	 */
	public boolean upload(String uploadFile) {
		boolean result = false;
		try {
			SFTPChannel channel = new SFTPChannel();
			Map<String, String> sftpDetails =new HashMap<>();
			sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, host);
			sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, username);
			sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, password);
			sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, port);
			ChannelSftp chSftp = channel.getChannel(sftpDetails , 60000);
			chSftp.cd(directory);
			File file = new File(uploadFile);
			chSftp.put(uploadFile, file.getName()); 
			chSftp.quit();
			channel.closeChannel();
			file = null;
			result = true;
		} catch (Exception e) {
			log.error(this, e);
		}
		return result;
	}

	



	/**
	 * 上传文件
	 * 
	 * @param directory
	 *            上传的目录
	 * @param uploadFile
	 *            要上传的文件
	 * @param sftpDetails
	 */
	public boolean upload(String directory, String uploadFile,
			Map<String, String> sftpDetails) {
		boolean result = false;
		try {
			SFTPChannel channel = new SFTPChannel();
			ChannelSftp chSftp = channel.getChannel(sftpDetails, 60000);
			chSftp.cd(directory);
			File file = new File(uploadFile);
			chSftp.put(uploadFile, file.getName());
			chSftp.quit();
			channel.closeChannel();
			file = null;
			result = true;
		} catch (Exception e) {
			log.error(this, e);
		}
		return result;
	} 
	
	/**
	 * 
	 * @param src
	 *            上传到目标服务器的输入流InputStream
	 * @param fileName
	 *            保存在服务器的文件名
	 * @param serverPath
	 *            Sftp服务器路径
	 * @return
	 */
	public static boolean uploadToSftp(InputStream src, String fileName) {
		boolean result = false;
		try {
			SFTPChannel channel = new SFTPChannel();
		
			Map<String, String> sftpDetails=new  HashMap<>();
			sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, host);
			sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, username);
			sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, password);
			sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, port);
			ChannelSftp chSftp = channel.getChannel(sftpDetails, 60000);
			chSftp.cd(directory);
			chSftp.put(src, fileName);
			src.close();// 关闭输入流
			chSftp.quit();
			channel.closeChannel();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	 

	

}
