package com.github.cenbylin.wxmessage.sdk.dev;

/**
 * Created by Cenbylin on 2017/7/9.
 */
public abstract class AbstractMessageProcessor implements BasicMessageProcessor{
    public Object doDefault(String openid) {
        //默认不做处理
        return null;
    }
}

