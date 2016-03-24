package com.aic.paas.wdev.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.aic.paas.wdev.util.HttpClientUtil;
import com.binary.json.JSON;


public class EmailMvcTest {
	
	
	public static void main(String[] args) {
		Map<String,String> callBackMap = new HashMap<String,String>();
		callBackMap.put("taskUserId", "赵栗婧1111111111");
		callBackMap.put("flag", "1");
		callBackMap.put("stdout", "我是邮件的正文内容11222222222222222！");
		callBackMap.put("emailAddress", "452274335@qq.com");
		String param1 = JSON.toString(callBackMap);
		System.out.println(param1);
		
//		String sendPostRequest = HttpClientUtil.sendPostRequest("http://localhost:16009/paas-task/dev/imageMvc/updateImageByCallBack", param1);
		String sendPostRequest ="";
		try {
			sendPostRequest = HttpClientUtil.sendPostRequest("http://localhost:16203/paas-wdev/dev/buildtask/updateBuildTaskByCallBack", param1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		System.out.println("------------------"+sendPostRequest);
		
	}
}
