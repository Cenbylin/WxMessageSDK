package com.github.cenbylin.wxmessage.sdk.dev;

/**
 * Created by Cenbylin on 2017/7/9.
 */
public abstract class AbstractMessageProcessor implements BasicMessageProcessor{
    /**
     * 图片消息
     * @param openid
     * @param picUrl
     * @return
     */
    public Object doPic(String openid, String picUrl) {
        return true;
    }

    /**
     * 文本消息
     * @param openid
     * @param text
     * @return
     */
    public Object doText(String openid, String text) {
        return true;
    }

    /**
     * 链接消息
     * @param openid
     * @param url
     * @return
     */
    public Object doUrl(String openid, String url) {
        return true;
    }
}
