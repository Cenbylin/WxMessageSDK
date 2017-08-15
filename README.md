![Logo of the project](./project-resource/logo.png)
# WxMessageSDK 轻量的微信公众号消息处理SDK
> 简化和微信服务器交互的繁琐部分，支持未认证/认证公众号。

## 快速开始

本SDK所做大部分工作仅仅是封装了微信文档的开发工作，为了保证拓展性，安装和使用是非常简单的。
### 0. 安装
将SDK的jar包添加到你的classpath，下载地址：
如果你使用 Maven，你只需要在 pom.xml 中添加下面的依赖：
```xml
<dependency>
    <groupId>com.github.cenbylin</groupId>
    <artifactId>WxMessage-SDK</artifactId>
    <version>0.1.1.RELEASE</version>
</dependency>
```
### 1. 获取你的公众号开发配置
请自行去微信公众平台获取你的appid和secret
![mp](./project-resource/mp.png)
### 2. 创建配置
创建一个继承WxConig类的MyConfig类，并把appid和secret写在覆盖的方法里

![myconfig](./project-resource/myconfig.png)
```java
import com.github.cenbylin.wxmessage.sdk.dev.WxConfig;
/**
 * MyConfig.java
 */
public class MyConfig extends WxConfig {
    public String getAppID() {
        return "{你的appid}";
    }
    public String getSecret() {
        return "{你的secret}";
    }
}
```
### 3.编写处理器代码
继承AbstractMessageProcessor即可，采用注解的形式绑定处理方法。

当接收了微信消息，会调用相应的这些方法，将一些信息作为实参传入。

| 消息类型 | 传入参数 |
|--------|--------|
|TEXT|openid text|
|IMAGE|openid picurl|
|LINK|openid Title Description Url|
|VOICE|openid MediaID Format Recognition|
|VIDEO|openid MediaID ThumbMediaId|
|SHORTVIDEO|openid MediaID ThumbMediaId|
|LOCATION|openid Location_X Location_Y Scale Label|
|EVEN_SUBSCRIBE|openid qrscene_二维码 ticket|
|EVEN_UNSUBSCRIBE|openid|
|EVEN_LOCATION|openid Latitude Longitude Precision|

返回值对应了不同的微信回复内容。

| 返回类型 | 回复 |
|--------|--------|
|String|文本|
|NewsResBean|图文|
|ImageResBean|图片|

**示例**

```java
@Component
public class SimpleProcessor extends AbstractMessageProcessor {
    @MessageMapping(MessageType.TEXT)
    public Object doText(String openid, String text) {
        // 回复图文
        NewsResBean n = new NewsResBean();
        n.addArticle(
                "标题1",
                "神奇的东方树叶",
                "http://www.baidu.com",
                "bd_logo1_31bdc765.png"
        );
        n.addArticle(
                "标题2",
                "神奇的东方树叶",
                "http://www.baidu.com",
                "bd_logo1_31bdc765.png"
        );
        return n;
    }
    
    @MessageMapping(MessageType.IMAGE)
    public Object doImg(String openid, String picurl) throws Exception {
        // 获得输入流
        URL url = new URL("picurl");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        // 回复图片
        ImageResBean res = new ImageResBean(is,"jpg");
        return res;
    }
    
    @EvenMapping(EvenType.EVEN_SUBSCRIBE)
    public Object doSubscribe(String openid) {
        // 回复文本
        return "欢迎关注本公众号";
    }
}
```

注：你可以写多个processor并使用，认证公众号可以多个回复，而未认证的公众号随机取一个processor。
**详细示例见 example/processorExam.java**
### 4. 实例化接入对象
#### 4.1 集成spring方式（推荐）
只需要在applicationContext.xml中增加如下配置：
```xml
<!--配置-->
    <bean id="wxconfig" class="cn.cenbylin.mp.message.MyConfig"/>
    <bean id="processor" class="cn.cenbylin.mp.message.processor.SimpleProcessor"/>
    <bean class="com.github.cenbylin.wxmessage.sdk.web.WebMessageAccess">
        <constructor-arg ref="wxconfig"/>
        <constructor-arg ref="processor"/>
    </bean>
```
#### 4.2 普通模式
同样地生成消息接入的WebMessageAccess对象，不过这个实例需要自行管理
```java
WxConfig myConfig = new WxConfig() {
        @Override
        public String getAppID() {
            return "{appid}";
        }
        @Override
        public String getSecret() {
            return "{secret}";
        }
    };
// 创建消息接入
WebMessageAccess webMessageAccess = new WebMessageAccess(myConfig, new SimpleProcessor());
```
### 5. 接入
不管是用servlet还是springmvc等方式，只需要在微信消息处理的地方托管WebMessageAccess
```java
/**
 * 微信公众号消息处理
 * @param request
 * @param response
 * @throws Exception
 */
@RequestMapping("/doMessage.do")
public void doMsg(HttpServletRequest request,
                  HttpServletResponse response) throws Exception{
    //未认证公众号
    webMessageAccess.processForNoAuthorization(request, response);
    //认证公众号
    //webMessageAccess.processForAuthorization(request, response);
}
```
### 6. 在微信公众平台做接入配置
![mpConfig](./project-resource/mpConfig.png)
