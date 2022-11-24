package com.source.handler;

import com.source.bean.bot.CQCode;
import com.source.bean.bot.Message;
import com.source.factory.MessageTypeFactory;
import com.source.service.GroupHandler;
import com.source.service.MessageTypeHandler;
import com.source.service.PrivateHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/23/16:17
 */
@Component
public class TutorialHandler implements GroupHandler, PrivateHandler {

    private final static String TUT = "教程";

    @Resource
    private MessageTypeFactory messageTypeFactory;

    /**
     * 根据Message进行解析
     *
     * @param message 消息对象
     * @return 消息类型
     */
    @Override
    public boolean canAnalyze(Message message) {
        return TUT.equals(message.getMessage());
    }

    /**
     * 消息解析
     *
     * @param message 消息
     */
    @Override
    public void analyze(Message message) {
        CQCode cqCode = CQCode.getInstance();
        String at = cqCode.at(message.getUser_id());
        MessageTypeHandler messageTypeHandler = messageTypeFactory.getMessageTypeHandler(message);
        messageTypeHandler.message(message, at + "群文件有获取ck软件，把ck发给机器人就行了机器人的密码是群主的账号，再问打死~");
    }
}
