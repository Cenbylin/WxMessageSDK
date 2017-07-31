package com.github.cenbylin.wxmessage.sdk.dev;

/**
 * 消息处理器接口
 * Created by Cenbylin on 2017/7/9.
 */
public interface BasicMessageProcessor {

    Object doText(String openid, String text);
    Object doPic(String openid, String picUrl);
    Object doLink(String openid, String url);
}
