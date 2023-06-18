package com.open.websocket.controller;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/4/2 15:12
 * @description 简单网络通信编程websocketDemo——实时通讯简单案例-类似聊天室
 */

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * websocketDemo实现过程
 * 一、首先，创建springboot项目，在pox.xml中加入（下面是我的pom.xml的dependencies里的全部依赖，因为，这个是最简单的入门例子,所以只有主要的websocket和web依赖）
 * 二、创建一个页面index.html，前端跳转后端的一些必要代码
 * 三、后端的socket处理类 WebSockTest.java
 * 四、在WebSocketConfig配置类，springboot要注入ServerEndpointExporter
 * 五、各环节日志输出
 */

/**
 * Http与WebSocket区别
 * HTTP 协议有一个缺陷：通信只能由客户端发起。了解今天的天气，只能是客户端向服务器发出请求，服务器返回查询结果。HTTP 协议做不到服务器主动向客户端推送信息。
 * WebSocket 是一种网络通信协议，很多高级功能都需要它。WebSocket 协议在2008年诞生，2011年成为国际标准。所有浏览器都已经支持了。
 * 它的最大特点就是，服务器可以主动向客户端推送信息，客户端也可以主动向服务器发送信息，是真正的双向平等对话，属于服务器推送技术的一种。
 */

/**
 * @ServerEndPoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端，
 * 注解的值将被用于监听用户连接的终端访问URL地址，客户端可以通过这个URL连接到websocket服务器端
 */
@ServerEndpoint("/websocket")
@Component
public class WebSockTest {
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount=0;
    /**
     * 当前WebSockTest连接数
     */
    private static CopyOnWriteArrayList<WebSockTest> webSocketSet=new CopyOnWriteArrayList<WebSockTest>();

    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSockTest> webSocketMap = new ConcurrentHashMap<>();

    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;

    //开启连接
    @OnOpen
    public void onOpen(Session session){
        //获取会话session
        this.session=session;
        //加入set中
        webSocketSet.add(this);
        //开启链接
        addOnlineCount();
        System.out.println("有新连接加入！当前在线人数为"+getOnlineCount());
    }

    //关闭连接
    @OnClose
    public void onClose(){
        //从set中去除
        webSocketSet.remove(this);
        //关闭链接
        subOnlineCount();
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    //接收客户端信息
    @OnMessage
    public void onMessage(String message,Session session){
        //接送客户端的消息
        System.out.println("来自客户端的消息："+message);

        //群发消息 从服务端向去客户端信息界面发送消息
        for (WebSockTest item:webSocketSet){
            try {
                //发送标准信息
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }

        ////发送自定义信息
        //try {
        //    sendInfo(message);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
    }

    //捕捉错误
    @OnError
    public void onError(Session session,Throwable throwable){
        System.out.println("发生错误！");
        throwable.printStackTrace();
    }

    //下面是自定义的一些方法 如发送自定义消息
    public void sendMessage(String message) throws IOException {
        System.out.println("发送自定义消息" + message);
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message) throws IOException {
        if(!message.isEmpty()){
            for (WebSockTest item:webSocketSet){
                try {
                    item.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }else{
            System.out.println("自定义消息为空！");
        }
    }

    //synchronized：控制多个并发线程的访问 实现同步。当前在线人数
    public static synchronized int getOnlineCount(){
        return onlineCount;
    }
    //synchronized：控制多个并发线程的访问 实现同步。开启连接 人数++
    public static synchronized void addOnlineCount(){
        WebSockTest.onlineCount++;
    }
    //synchronized：控制多个并发线程的访问 实现同步。关闭连接 人数--
    public static synchronized void subOnlineCount(){
        WebSockTest.onlineCount--;
    }
}