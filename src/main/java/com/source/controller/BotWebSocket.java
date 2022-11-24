package com.source.controller;

import com.source.service.BotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/10/13:32
 */
@Slf4j
@ServerEndpoint(value = "/iBot")
@Controller
public class BotWebSocket {

    //记录当前在线连接数  private static final AtomicInteger onlineCount = new AtomicInteger(0);
    //存放所有在线的客户端  private static final Map<String, Session> clients = new ConcurrentHashMap<>();

    private static BotService botService;

    @Resource
    public void setBotService(BotService botService) {
        BotWebSocket.botService = botService;
    }

    @OnOpen
    public void onOpen() {
        log.info("已连接bot");
    }

    @OnClose
    public void onClose() {
        log.info("已断开bot");
    }

    @OnError
    public void onError(Throwable error) {
        log.error("bot意外掉线");
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(String messageStr) {
        botService.onMessage(messageStr);
    }
}
