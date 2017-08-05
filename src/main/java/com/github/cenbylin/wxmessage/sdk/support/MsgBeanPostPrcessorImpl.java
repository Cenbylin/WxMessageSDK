package com.github.cenbylin.wxmessage.sdk.support;

import com.github.cenbylin.wxmessage.sdk.dev.BasicMessageProcessor;
import com.github.cenbylin.wxmessage.sdk.dev.WxConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 消息处理器的注入
 * Created by Cenbylin on 2017/7/9.
 */
public class MsgBeanPostPrcessorImpl implements BeanPostProcessor {
    WxConfig wxConfig;

    public MsgBeanPostPrcessorImpl(WxConfig wxConfig) {
        this.wxConfig = wxConfig;
    }


    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        if (o instanceof BasicMessageProcessor){
            wxConfig.addProcessor((BasicMessageProcessor)o);
        }
        return o;
    }
}
