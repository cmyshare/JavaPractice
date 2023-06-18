# 简单网络通信编程websocketDemo——实时通讯简单案例-类似聊天室

# websocketDemo实现步骤
* 一、首先，创建springboot项目，在pox.xml中加入（下面是我的pom.xml的dependencies里的全部依赖，因为，这个是最简单的入门例子,所以只有主要的websocket和web依赖）
* 二、创建一个页面index.html，前端跳转后端的一些必要代码
* 三、后端的socket处理类 WebSockTest.java
* 四、在WebSocketConfig配置类，springboot要注入ServerEndpointExporter
* 五、在WebSockTest.java创建发送自定义消息sendInfo，创建DemoController通过调用push api，向客户端的全体用户推送自定义信息

# 参考连接*
https://zhuanlan.zhihu.com/p/145628937
https://help.aliyun.com/document_detail/127179.html
http://ruanyifeng.com/blog/2017/05/websocket.html#comment-text
https://blog.csdn.net/clmmei_123/article/details/82822456
https://www.jianshu.com/p/d79bf8174196
https://www.jianshu.com/p/964cef2359e7
https://zhengkai.blog.csdn.net/article/details/80275084?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_default&utm_relevant_index=2





