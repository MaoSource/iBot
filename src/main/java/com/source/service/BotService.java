package com.source.service;

import com.alibaba.fastjson.JSON;
import com.source.bean.bot.Message;
import com.source.factory.MessageFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/10/13:32
 */
@Service
@Slf4j
public class BotService {

    @Resource
    private MessageFactory messageFactory;

    public void onMessage(String messageStr) {
        try {
            Message message = JSON.parseObject(messageStr, Message.class);
            MessageHandler messageApi = messageFactory.getMessageApi(message);
            if (messageApi != null) {
                messageApi.message(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
