package com.github.cenbylin.wxmessage.sdk.core;

import com.github.cenbylin.wxmessage.sdk.dev.BasicMessageProcessor;
import com.github.cenbylin.wxmessage.sdk.dev.WxConfig;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Cenbylin on 2017/7/9.
 */
public class JobExecutiveController implements Runnable {
    private Random r = new Random();
    Logger logger = Logger.getLogger(JobExecutiveController.class);
    //任务队列
    private LinkedList<MessageBean> jobQueue = new LinkedList<MessageBean>();
    //是否阻塞
    public volatile boolean wait;

    private WxConfig wxConfig;
    private LinkedList<? extends BasicMessageProcessor> messageProcessorList;
    private ResultProcessor resultProcessor;
    /**
     * 接收配置的控制器
     * @param wxConfig 配置
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
     * @param msb 任务实体
     * @return 是否成功
     */
    public synchronized boolean addJob(MessageBean msb){
        jobQueue.addLast(msb);
        return true;
    }

    /**
     * 获得一个任务
     * @return 任务实体
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
     * 消息任务处理（core）（异步方式）
     * @param msb 任务实体
     */
    private void processJob(MessageBean msb) throws Exception {
        /**
         * proccess
         */
        Object res = "";
        if("text".equals(msb.getMsgType())){
            logger.info("process text message:" + msb.getContent());
            for(BasicMessageProcessor o:messageProcessorList){
                res = o.doText(msb.getFromUserName(), msb.getContent());
                resultProcessor.executeRes(res, msb.getFromUserName());
            }
        } else if ("link".equals(msb.getMsgType())){
            logger.info("process link message:" + msb.getUrl());
            for(BasicMessageProcessor o:messageProcessorList){
                res = o.doLink(msb.getFromUserName(), msb.getUrl());
                resultProcessor.executeRes(res, msb.getFromUserName());
            }
        } else if ("image".equals(msb.getMsgType())){
            logger.info("process image message:" + msb.getPicUrl());
            for(BasicMessageProcessor o:messageProcessorList){
                res = o.doPic(msb.getFromUserName(), msb.getPicUrl());
                resultProcessor.executeRes(res, msb.getFromUserName());
            }
        }
        logger.info("process finish");
    }

    /**
     * 消息任务处理（core）（返回结果方式）
     * @param msb 任务实体
     * @return 处理结果
     * @throws Exception 异常
     */
    public String getJobRes(MessageBean msb) throws Exception {
        //确定处理器(随机方式)
        int length = messageProcessorList.size();
        BasicMessageProcessor processor = messageProcessorList.get(r.nextInt(length));
        /**
         * proccess
         */
        Object res = "";
        if("text".equals(msb.getMsgType())) {
            logger.info("process text message:" + msb.getContent());
            res = processor.doText(msb.getFromUserName(), msb.getContent());
            return resultProcessor.convertRes(res, msb.getFromUserName(), msb.getToUserName());
        } else if ("link".equals(msb.getMsgType())) {
            logger.info("process link message:" + msb.getUrl());
            res = processor.doLink(msb.getFromUserName(), msb.getUrl());
            return resultProcessor.convertRes(res, msb.getFromUserName(), msb.getToUserName());
        } else if ("image".equals(msb.getMsgType())) {
            logger.info("process image message:" + msb.getPicUrl());
            res = processor.doPic(msb.getFromUserName(), msb.getPicUrl());
            return resultProcessor.convertRes(res, msb.getFromUserName(), msb.getToUserName());
        }
        logger.info("process finish");
        return null;
    }
}
