package com.github.cenbylin.wxmessage.sdk.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class InfoProvider {
	private static String appid = "wx2e7d71280147d5a5";
	private static String secret = "bb54272fc561876a7c4f82d347f9c584";
	private static String accessToken;
	private static String jsapiTicket;
	static{
		//定时任务线程
		Runnable runnable = new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				int timeInterval = 7000000;//时间(毫秒)
				while (true) {
					String json = HttpRequestTool.sendGet(
							"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
									+ appid + "&secret=" + secret, "utf-8");
					//解析数据
					Gson gson = new Gson();
					Map<String, Object> jsonObject = gson.fromJson(json, Map.class);
					try {
						// 调用一系列get方法获取object的直接子对象
						accessToken = jsonObject.get("access_token").toString();
						timeInterval = ((Double)jsonObject.get("expires_in")).intValue()*1000 - 10000;
						System.err.println("刷新token时间间隔："+timeInterval);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("获取token出错"+json);
					}
					
					String json1 = HttpRequestTool.sendGet(
							"https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
									+ accessToken + "&type=jsapi", "utf-8");
					//解析数据
					Map<String, Object> jsonObject1 = gson.fromJson(json1, Map.class);
					try {
						// 调用一系列get方法获取object的直接子对象
						jsapiTicket = jsonObject1.get("ticket").toString();
					} catch (Exception e) { }
					
					try {
						Thread.sleep(timeInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	public static String getAccessToken() {
		return accessToken;
	}
}
