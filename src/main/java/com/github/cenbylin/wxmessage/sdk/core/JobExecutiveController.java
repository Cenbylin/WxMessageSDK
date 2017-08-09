package com.github.cenbylin.wxmessage.sdk.core;

import com.github.cenbylin.wxmessage.sdk.dev.BasicMessageProcessor;
import com.github.cenbylin.wxmessage.sdk.dev.WxConfig;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
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
    private HandlerAdapter handlerAdapter = new HandlerAdapter();
    private ResultProcessor resultProcessor;
    /**
     * 接收配置的控制器，目前是单处理器
     * @param wxConfig 配置
     */
    public JobExecutiveController(WxConfig wxConfig, BasicMessageProcessor messageProcessor) {
        this.wxConfig = wxConfig;
        handlerAdapter.loadProcessor(messageProcessor);
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
        // 获得处理器
        List<HandlerMethod> list = handlerAdapter.findHandlerMethods(msb);
        // 执行处理器
        for (HandlerMethod handlerMethod:list){
            res = handlerAdapter.handle(msb, handlerMethod);
            resultProcessor.executeRes(res, msb.getFromUserName());
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
        /**
         * proccess
         */
        Object res = "";
        HandlerMethod handlerMethod = handlerAdapter.findOneHandlerMethod(msb);
        res = handlerAdapter.handle(msb, handlerMethod);
        logger.info("process finish");
        return resultProcessor.convertRes(res, msb.getFromUserName(), msb.getToUserName());
    }
}
