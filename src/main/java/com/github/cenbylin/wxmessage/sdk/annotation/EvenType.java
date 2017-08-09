package com.github.cenbylin.wxmessage.sdk.annotation;

/**
 * Created by Cenbylin on 2017/8/9.
 */
public class EvenType {
    //事件
    /**关注事件，含扫二维码关注*/
    public static final String EVEN_SUBSCRIBE = "subscribe";
    /**取消关注事件*/
    public static final String EVEN_UNSUBSCRIBE = "unsubscribe";
    /**二维码扫描事件*/
    public static final String EVEN_SCAN = "SCAN";
    /**上报地理位置事件*/
    public static final String EVEN_LOCATION = "LOCATION";
    /**点击自定义菜单事件*/
    public static final String EVEN_CLICK = "CLICK";
    /**点击自定义菜单跳转事件*/
    public static final String EVEN_VIEW = "VIEW";
}
