package com.github.cenbylin.wxmessage.sdk.core;

import com.github.cenbylin.wxmessage.sdk.dev.BasicMessageProcessor;

import java.lang.reflect.Method;

/**
 * Created by Cenbylin on 2017/8/9.
 */
public class HandlerMethod {
    // 处理器
    protected BasicMessageProcessor processor;
    // 类型 message的子类型 / even
    private String type = "";
    // 事件类型
    private String even = "";
    // 处理方法
    protected Method m;
    // 参数列表
    protected Class[] params;

    public HandlerMethod(BasicMessageProcessor processor, String type, Method m, Class[] params) {
        this.processor = processor;
        this.type = type;
        this.m = m;
        this.params = params;
    }

    public HandlerMethod(BasicMessageProcessor processor, String type, String even, Method m, Class[] params) {
        this.processor = processor;
        this.type = type;
        this.even = even;
        this.m = m;
        this.params = params;
    }

    /**
     * 是否匹配
     * @param msb
     * @return
     */
    public boolean match(MessageBean msb){
        //类型匹配
        if (type.equals(msb.getMsgType())){
            //事件不匹配
            if ("even".equals(msb.getEvent()) && !even.equals(msb.getEvent())){
                return false;
            }
            return true;
        }
        return false;
    }
}
