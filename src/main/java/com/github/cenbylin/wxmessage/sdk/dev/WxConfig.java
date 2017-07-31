package com.github.cenbylin.wxmessage.sdk.dev;

import com.github.cenbylin.wxmessage.sdk.util.HttpRequestTool;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.Map;

/**
 * 配置获取器
 * Created by Cenbylin on 2017/7/29.
 */
public abstract class WxConfig {
    LinkedList messageProcessorList = new LinkedList();
    private String accessToken;
    //此方法被覆盖，证明托管
    //定时任务线程
    private Runnable runnable = new Runnable() {
        @SuppressWarnings("unchecked")
        public void run() {
            int timeInterval = 7000000;//时间(毫秒)
            while (true) {
                String json = HttpRequestTool.sendGet(
                        "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                                + getAppID() + "&secret=" + getSecret(), "utf-8");
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

                try {
                    Thread.sleep(timeInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private Thread thread = new Thread(runnable);

    public LinkedList getMessageProcessorList() {
        return messageProcessorList;
    }

    public void addProcessor(BasicMessageProcessor o){
        messageProcessorList.add(o);
    }

    public abstract String getAppID();
    public abstract String getSecret();
    public String getAccessToken(){
        //被覆盖表示需要托管
        while (accessToken==null){
            try {
                thread.start();
                Thread.sleep(100);
            } catch (Exception e) {

            }
        }
        return accessToken;
    }
}
