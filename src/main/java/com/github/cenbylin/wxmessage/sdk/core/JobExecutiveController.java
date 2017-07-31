package com.github.cenbylin.wxmessage.sdk.core;

import com.github.cenbylin.wxmessage.sdk.dev.BasicMessageProcessor;
import com.github.cenbylin.wxmessage.sdk.dev.WxConfig;
import com.github.cenbylin.wxmessage.sdk.util.HttpRequestTool;
import com.github.cenbylin.wxmessage.sdk.util.MessageCreator;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Cenbylin on 2017/7/9.
 */
public class JobExecutiveController implements Runnable {
    Logger logger = Logger.getLogger(JobExecutiveController.class);
    //任务队列
    private LinkedList<MessageBean> jobQueue = new LinkedList<MessageBean>();
    //是否阻塞
    public volatile boolean wait;
    //消息创建工具
    MessageCreator messageCreator = new MessageCreator();

    private WxConfig wxConfig;
    private LinkedList messageProcessorList;
    private ResultProcessor resultProcessor;
    /**
     * 接收配置的控制器
     * @param wxConfig
     */
    public JobExecutiveController(WxConfig wxConfig) {
        this.wxConfig = wxConfig;
        this.messageProcessorList = wxConfig.getMessageProcessorList();
        resultProcessor = new ResultProcessor(wxConfig);
        //创建后自启
        new Thread(this).start();
    }

    /**
     * 增加一个任务
     * @param msb
     * @return
     */
    public synchronized boolean addJob(MessageBean msb){
        jobQueue.addLast(msb);
        return true;
    }

    /**
     * 获得一个任务
     * @return
     */
    public synchronized MessageBean getJob(){
        if (jobQueue!=null && jobQueue.size()>0){
            MessageBean msb = jobQueue.getFirst();
            jobQueue.removeFirst();
            return msb;
        }
        return null;
    }

    public void run(){
        while (true){
            MessageBean msb = getJob();
            if (msb==null){
                //阻塞等待唤醒，以自己为锁
                synchronized (this) {
                    this.wait = true;
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                logger.info("process message:" + msb.getContent());
                //开始处理
                try {
                    processJob(msb);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("处理消息错误", e);
                }
            }
        }
    }

    /**
     * 消息任务处理（core）
     * @param msb
     */
    private void processJob(MessageBean msb) throws Exception {
        /**
         * proccess
         */
        Object res = "";
        if("text".equals(msb.getMsgType())){
            for(Object o:messageProcessorList){
                res = ((BasicMessageProcessor)o)
                        .doText(msb.getFromUserName(), msb.getContent());
                resultProcessor.executeRes(res, msb.getFromUserName());
            }
        } else if ("url".equals(msb.getMsgType())){
            for(Object o:messageProcessorList){
                res = ((BasicMessageProcessor)o)
                        .doUrl(msb.getFromUserName(), msb.getContent());
                resultProcessor.executeRes(res, msb.getFromUserName());
            }
        }
    }
}
