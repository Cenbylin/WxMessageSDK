package com.github.cenbylin.wxmessage.sdk.dev;

/**
 * Created by Cenbylin on 2017/7/9.
 */
public abstract class AbstractMessageProcessor implements BasicMessageProcessor{
    /**
     * 图片消息
     * @param openid openid
     * @param picUrl 图片地址
     * @return 处理结果
     */
    public Object doPic(String openid, String picUrl) {
        return true;
    }

    /**
     * 文本消息
     * @param openid openid
     * @param text 消息内容
     * @return 处理结果
     */
    public Object doText(String openid, String text) {
        return true;
    }

    /**
     * 链接消息
     * @param openid openid
     * @param url 链接
     * @return 处理结果
     */
    public Object doLink(String openid, String url) {
        return true;
    }
}
