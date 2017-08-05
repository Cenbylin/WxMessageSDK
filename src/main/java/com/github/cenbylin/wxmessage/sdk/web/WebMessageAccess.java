package com.github.cenbylin.wxmessage.sdk.web;

import com.github.cenbylin.wxmessage.sdk.core.MessageBean;
import com.github.cenbylin.wxmessage.sdk.core.JobExecutiveController;
import com.github.cenbylin.wxmessage.sdk.dev.WxConfig;
import com.github.cenbylin.wxmessage.sdk.util.XMLUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * Created by Cenbylin on 2017/7/29.
 */
public class WebMessageAccess {
    JobExecutiveController jobExecutiveController;

    public WebMessageAccess(WxConfig wxConfig) {
        this.jobExecutiveController = new JobExecutiveController(wxConfig);
    }

    /**
     * 认证的公众号消息处理
     * @param request request
     * @param response response
     * @throws Exception 异常
     */
    public void processForAuthorization(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1、预处理
        preDoMsg(request, response);

        //2、初始化消息模型，装载信息
        InputStream inputStream = request.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        final StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }
        String resp = stringBuffer.toString();
        MessageBean msb = new MessageBean();
        Map<String, String> map = XMLUtil.xmlToMap(resp);
        //装载
        msb.loadMap(map);

        //3、直接返回success(在客服接口异步处理)
        writeString(response, "success");

        //4、加入处理队列
        jobExecutiveController.addJob(msb);

        //5、唤醒
        if (jobExecutiveController.wait){
            synchronized (jobExecutiveController){
                jobExecutiveController.wait = false;
                jobExecutiveController.notify();
            }
        }
    }
    /**
     * 未认证的公众号消息处理
     * @param request request
     * @param response response
     * @throws Exception 异常
     */
    public void processForNoAuthorization(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1、预处理
        preDoMsg(request, response);

        //2、初始化消息模型，装载信息
        InputStream inputStream = request.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        final StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }
        String resp = stringBuffer.toString();
        MessageBean msb = new MessageBean();
        Map<String, String> map = XMLUtil.xmlToMap(resp);
        //装载
        msb.loadMap(map);

        //3、处理结果
        String content = jobExecutiveController.getJobRes(msb);

        //4、直接返回success(在客服接口异步处理)
        writeString(response, content);
    }
    /**
     * 直接写出字符流
     * @param response response
     * @param content content
     * @throws Exception 异常
     */
    public void writeString(HttpServletResponse response, String content) throws Exception{
        PrintWriter out = response.getWriter();
        out.write(content);
        out.flush();
        out.close();
    }
    /**
     * 预处理（开发者接入等）
     * @param request request
     * @param response response
     * @throws Exception 异常
     */
    private void preDoMsg(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //判断是否初次接入
        String echostr = request.getParameter("echostr");
        if(echostr!=null){
            PrintWriter out = response.getWriter();
            out.print(echostr);
            out.flush();
            out.close();
            return;
        }
        response.setContentType("text/xml;charset=utf-8");
        request.setCharacterEncoding("utf-8");
    }
}
