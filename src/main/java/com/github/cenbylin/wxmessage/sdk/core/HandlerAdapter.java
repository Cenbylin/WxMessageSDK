package com.github.cenbylin.wxmessage.sdk.core;

import com.github.cenbylin.wxmessage.sdk.annotation.EvenMapping;
import com.github.cenbylin.wxmessage.sdk.annotation.EvenType;
import com.github.cenbylin.wxmessage.sdk.annotation.MessageMapping;
import com.github.cenbylin.wxmessage.sdk.annotation.MessageType;
import com.github.cenbylin.wxmessage.sdk.dev.BasicMessageProcessor;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 适配器，包含 匹配 和 执行
 * Created by Cenbylin on 2017/8/9.
 */
public class HandlerAdapter {
    Logger logger = Logger.getLogger(HandlerAdapter.class);
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
                        "event",
                        evenAnn.value(),
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
        if (handlerMethod==null){
            logger.info("No matched handler");
            return null;
        }
        //初始化参数列表
        Object[] param = new Object[handlerMethod.params.length];
        if (MessageType.TEXT.equals(msb.getMsgType())){
            //参数列表 openid text
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (String.class.isAssignableFrom(handlerMethod.params[i])){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();break;
                        case 1:param[i] = msb.getContent();break;
                    }
                    pre++;
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        } else if (MessageType.IMAGE.equals(msb.getMsgType())){
            //参数列表 openid picurl
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (String.class.isAssignableFrom(handlerMethod.params[i])){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();break;
                        case 1:param[i] = msb.getPicUrl();break;
                    }
                    pre++;
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        } else if (MessageType.LINK.equals(msb.getMsgType())){
            //参数列表 openid Title Description Url
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (String.class.isAssignableFrom(handlerMethod.params[i])){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();break;
                        case 1:param[i] = msb.getTitle();break;
                        case 2:param[i] = msb.getDescription();break;
                        case 3:param[i] = msb.getUrl();break;
                    }
                    pre++;
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        } else if (MessageType.VOICE.equals(msb.getMsgType())){
            //参数列表 openid MediaID Format Recognition
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (String.class.isAssignableFrom(handlerMethod.params[i])){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();break;
                        case 1:param[i] = msb.getMediaId();break;
                        case 2:param[i] = msb.getFormat();break;
                        case 3:param[i] = msb.getRecognition();break;
                    }
                    pre++;
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        } else if (MessageType.VIDEO.equals(msb.getMsgType())){
            //参数列表 openid MediaID ThumbMediaId
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (String.class.isAssignableFrom(handlerMethod.params[i])){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();break;
                        case 1:param[i] = msb.getMediaId();break;
                        case 2:param[i] = msb.getThumbMediaId();break;
                    }
                    pre++;
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        } else if (MessageType.SHORTVIDEO.equals(msb.getMsgType())){
            //参数列表 openid MediaID ThumbMediaId
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (String.class.isAssignableFrom(handlerMethod.params[i])){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();break;
                        case 1:param[i] = msb.getMediaId();break;
                        case 2:param[i] = msb.getThumbMediaId();break;
                    }
                    pre++;
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        } else if (MessageType.LOCATION.equals(msb.getMsgType())){
            //参数列表 openid Location_X Location_Y Scale Label
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (String.class.isAssignableFrom(handlerMethod.params[i])){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();break;
                        case 1:param[i] = msb.getLocation_X();break;
                        case 2:param[i] = msb.getLocation_Y();break;
                        case 3:param[i] = msb.getScale();break;
                        case 4:param[i] = msb.getLabel();break;
                    }
                    pre++;
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        }else{
            //不是事件消息，无执行结果
            if (!("event".equals(msb.getMsgType()))){
                return null;
            }
        }

        //事件类型
        if (EvenType.EVEN_SUBSCRIBE.equals(msb.getEvent())
                || EvenType.EVEN_SCAN.equals(msb.getEvent())){
            //参数列表 openid qrscene_二维码 ticket
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (String.class.isAssignableFrom(handlerMethod.params[i])){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();break;
                        case 1:param[i] = msb.getEventKey();break;
                        case 2:param[i] = msb.getTicket();break;
                    }
                    pre++;
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        }else if (EvenType.EVEN_UNSUBSCRIBE.equals(msb.getEvent())){
            //参数列表 openid
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (String.class.isAssignableFrom(handlerMethod.params[i])){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();break;
                    }
                    pre++;
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        }else if (EvenType.EVEN_LOCATION.equals(msb.getEvent())){
            //参数列表 openid Latitude Longitude Precision
            int pre = 0;
            for (int i=0; i<handlerMethod.params.length; i++){
                if (String.class.isAssignableFrom(handlerMethod.params[i])){
                    switch (pre){
                        case 0:param[i] = msb.getFromUserName();break;
                        case 1:param[i] = msb.getLatitude();break;
                        case 2:param[i] = msb.getLongitude();break;
                        case 3:param[i] = msb.getPrecision();break;
                    }
                    pre++;
                }
            }
            return handlerMethod.m.invoke(handlerMethod.processor, param);
        }else if (EvenType.EVEN_CLICK.equals(msb.getEvent())){

        }else if (EvenType.EVEN_VIEW.equals(msb.getEvent())){

        }

        return null;
    }
}
