package com.open.websocket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/4/2 16:56
 * @description 向客户端全体用户推送发送自定义信息控制类
 */

@RestController
public class DemoController {

    @RequestMapping("/push")
    public ResponseEntity<String> pushToWeb(String message) throws IOException {
        WebSockTest.sendInfo(message);
        return ResponseEntity.ok("向客户端发送自定义信息 发送成功");
    }
}
