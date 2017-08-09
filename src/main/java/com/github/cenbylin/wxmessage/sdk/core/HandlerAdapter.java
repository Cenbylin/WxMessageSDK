package com.github.cenbylin.wxmessage.sdk.core;

import com.github.cenbylin.wxmessage.sdk.annotation.EvenMapping;
import com.github.cenbylin.wxmessage.sdk.annotation.MessageMapping;
import com.github.cenbylin.wxmessage.sdk.annotation.MessageType;
import com.github.cenbylin.wxmessage.sdk.dev.BasicMessageProcessor;

import javax.net.ssl.HandshakeCompletedListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 适配器，包含 匹配 和 执行
 * Created by Cenbylin on 2017/8/9.
 */
public class HandlerAdapter {
    // 存储处理方法集
    private List<HandlerMethod> handlers = new LinkedList<HandlerMethod>();

    /**
     * 加载一个处理器，解析其方法
     * @param processor
     */
    public void loadProcessor(BasicMessageProcessor processor){
        Class clazz = processor.getClass();
        // 分析所有方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods){
            // 获得消息、事件注解
            MessageMapping msgAnn = m.getAnnotation(MessageMapping.class);
            EvenMapping evenAnn = m.getAnnotation(EvenMapping.class);
            if (msgAnn==null && evenAnn==null){
                continue;
            }
            // 加入handlers
            if (msgAnn!=null){
                handlers.add(new HandlerMethod(
                        processor,
                        msgAnn.value(),
                        m,
                        m.getParameterTypes()
                ));
            }
            if (evenAnn!=null){
                handlers.add(new HandlerMethod(
                        processor,
                        "even",
                        msgAnn.value(),
                        m,
                        m.getParameterTypes()
                ));
            }
        }
    }

    /**
     * 寻找一个处理器
     * @param msb
     * @return
     */
    public HandlerMethod findOneHandlerMethod(MessageBean msb){
        for (HandlerMethod h : handlers){
            if (h.match(msb)){
                return h;
            }
        }
        return null;
    }
    /**
     * 寻找所有合适的处理器
     * @param msb
     * @return
     */
    public List<HandlerMethod> findHandlerMethods(MessageBean msb){
        List<HandlerMethod> list = new LinkedList<HandlerMethod>();
        for (HandlerMethod h : handlers){
            if (h.match(msb)){
                list.add(h);
            }
        }
        return list;
    }

    /**
     * 处理(core)
     * @param msb
     * @return
     */
    public Object handle(MessageBean msb, HandlerMethod handlerMethod) throws InvocationTargetException, IllegalAccessException {
        //初始化参数列表
        Object[] param = new Object[handlerMethod.params.length];
        if (MessageType.TEXT.equals(msb.getMsgType())){
            //参数列表 openid text
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (param[i] instanceof String){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();
                        case 1:param[i] = msb.getContent();
                    }
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        } else if (MessageType.IMAGE.equals(msb.getMsgType())){
            //参数列表 openid picurl
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (param[i] instanceof String){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();
                        case 1:param[i] = msb.getPicUrl();
                    }
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        } else if (MessageType.LINK.equals(msb.getMsgType())){
            //参数列表 openid Title Description Url
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (param[i] instanceof String){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();
                        case 1:param[i] = msb.getTitle();
                        case 2:param[i] = msb.getDescription();
                        case 3:param[i] = msb.getUrl();
                    }
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        } else if (MessageType.VOICE.equals(msb.getMsgType())){
            //参数列表 openid MediaID Format Recognition
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (param[i] instanceof String){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();
                        case 1:param[i] = msb.getMediaId();
                        case 2:param[i] = msb.getFormat();
                        case 3:param[i] = msb.getRecognition();
                    }
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        } else if (MessageType.VIDEO.equals(msb.getMsgType())){
            //参数列表 openid MediaID ThumbMediaId
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (param[i] instanceof String){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();
                        case 1:param[i] = msb.getMediaId();
                        case 2:param[i] = msb.getThumbMediaId();
                    }
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        } else if (MessageType.SHORTVIDEO.equals(msb.getMsgType())){
            //参数列表 openid MediaID ThumbMediaId
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (param[i] instanceof String){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();
                        case 1:param[i] = msb.getMediaId();
                        case 2:param[i] = msb.getThumbMediaId();
                    }
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        } else if (MessageType.LOCATION.equals(msb.getMsgType())){
            //参数列表 openid Location_X Location_Y Scale Label
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (param[i] instanceof String){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();
                        case 1:param[i] = msb.getLocation_X();
                        case 2:param[i] = msb.getLocation_Y();
                        case 3:param[i] = msb.getScale();
                        case 4:param[i] = msb.getLabel();
                    }
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        }
        return null;
    }
}
