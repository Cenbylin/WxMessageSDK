package com.github.cenbylin.wxmessage.sdk.core;

import com.github.cenbylin.wxmessage.sdk.dev.ImageResBean;
import com.github.cenbylin.wxmessage.sdk.dev.NewsResBean;
import com.github.cenbylin.wxmessage.sdk.dev.WxConfig;
import com.github.cenbylin.wxmessage.sdk.util.HttpRequestTool;
import com.github.cenbylin.wxmessage.sdk.util.XMLUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Cenbylin on 2017/7/29.
 */
public class ResultProcessor {
    Logger logger = Logger.getLogger(JobExecutiveController.class);
    private WxConfig wxConfig;

    public ResultProcessor(WxConfig wxConfig) {
        this.wxConfig = wxConfig;
    }

    /**
     * 执行proccessor的结果
     * @param res
     * @param openid 默认的对方openid
     */
    public void executeRes(Object res, String openid) throws Exception {
        //客服接口
        String postUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
                + wxConfig.getAccessToken();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String postText = null;

        if (res==null){
            return;
        }else if (res instanceof String) {//文本回复
            logger.info("execute:reply text '" + (String) res + "'");
            jsonMap.put("touser", openid);
            jsonMap.put("msgtype", "text");
            Map<String, Object> textMap = new HashMap<String, Object>();
            textMap.put("content", (String) res);
            jsonMap.put("text", textMap);
            Gson gson = new Gson();
            postText = gson.toJson(jsonMap);
        } else if (res instanceof ImageResBean){//图片回复
            // 上传媒体
            ImageResBean i = (ImageResBean)res;
            String mediaId = HttpRequestTool.uploadMedia(wxConfig.getAccessToken(), i.getIn(), "image/." + i.getFormat());
            jsonMap.put("touser", openid);
            jsonMap.put("msgtype", "image");
            Map<String, Object> inMap = new HashMap<String, Object>();
            inMap.put("media_id", mediaId);
            jsonMap.put("image", inMap);
            Gson gson = new Gson();
            postText = gson.toJson(jsonMap);
        } else if (res instanceof NewsResBean){//图文回复
            jsonMap.put("touser", openid);
            jsonMap.put("msgtype", "news");
            Map<String, Object> inMap = new HashMap<String, Object>();
            inMap.put("articles", ((NewsResBean)res).list);
            jsonMap.put("news", inMap);
            Gson gson = new Gson();
            postText = gson.toJson(jsonMap);
        } else if (res instanceof List){//多重回复，递归本函数
            for (Object o : (List)res){
                executeRes(o, openid);
            }
        }
        if (postText==null){
            return;
        }
        //发送请求
        logger.info(HttpRequestTool.sendPost(postUrl, postText, "utf-8"));
    }

    /**
     * 转换成xml（微信服务器接收方式）
     * @param res
     * @param openid
     * @return
     * @throws Exception
     */
    public String convertRes(Object res, String openid, String from) throws Exception {
        Map<String, Object> xmlMap = new HashMap<String, Object>();
        String resText = null;
        if (res==null){
            return null;
        }else if (res instanceof String) {//文本回复
            logger.info("execute:reply text '" + (String) res + "'");
            xmlMap.put("ToUserName", openid);
            xmlMap.put("FromUserName", from);
            xmlMap.put("CreateTime", Long.toString(new Date().getTime()));
            xmlMap.put("MsgType", "text");
            xmlMap.put("Content", (String) res);
            resText = XMLUtil.mapToXmlDeep(xmlMap);
        } else if (res instanceof ImageResBean){//图片回复
            // 上传媒体
            ImageResBean i = (ImageResBean)res;
            String mediaId = HttpRequestTool.uploadMedia(wxConfig.getAccessToken(), i.getIn(), "image/." + i.getFormat());
            xmlMap.put("ToUserName", openid);
            xmlMap.put("FromUserName", from);
            xmlMap.put("CreateTime", Long.toString(new Date().getTime()));
            xmlMap.put("MsgType", "image");
            xmlMap.put("MediaId", mediaId);
            resText = XMLUtil.mapToXmlDeep(xmlMap);
        } else if (res instanceof NewsResBean){//图文回复
            xmlMap.put("ToUserName", openid);
            xmlMap.put("FromUserName", from);
            xmlMap.put("MsgType", "news");
            xmlMap.put("CreateTime", Long.toString(new Date().getTime()));
            List<Map<String, String>> mapList = new LinkedList<Map<String, String>>();
            for (NewsResBean.Article bean : ((NewsResBean)res).list){
                Map<String, String> articleMap = new HashMap<String, String>();
                articleMap.put("Title", bean.getTitle());
                articleMap.put("Description", bean.getDescription());
                articleMap.put("PicUrl", bean.getPicurl());
                articleMap.put("Url", bean.getUrl());
                mapList.add(articleMap);
            }
            xmlMap.put("ArticleCount", String.valueOf(mapList.size()));
            xmlMap.put("Articles", mapList);
            resText = XMLUtil.mapToXmlDeep(xmlMap);
        } else if (res instanceof List){//多重回复，递归本函数
            for (Object o : (List)res){
                executeRes(o, openid);
            }
        }
        return resText;
    }

    public static void main(String[] args) throws Exception {
        WxConfig config = new WxConfig() {
            @Override
            public String getAppID() {
                return "wx2e7d71280147d5a5";
            }

            @Override
            public String getSecret() {
                return "bb54272fc561876a7c4f82d347f9c584";
            }
        };
        ResultProcessor r = new ResultProcessor(config);
        r.executeRes("消息", "openid");

        File f = new File("D:\\1.jpg");
        InputStream in = new FileInputStream(f);
        r.executeRes( new ImageResBean(in, "jpg"), "1231");

        NewsResBean n = new NewsResBean();
        n.addArticle("211","222","http://aas.com", "http://www.baidu.com");
        n.addArticle("111","222","http://aas.com", "http://www.baidu.com");
        r.executeRes(n, "openid");
    }
}
