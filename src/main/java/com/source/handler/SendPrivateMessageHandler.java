package com.source.handler;

import com.source.bean.bot.Message;
import com.source.service.MessageService;
import com.source.service.MessageTypeHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/23/16:48
 */
@Component
public class SendPrivateMessageHandler implements MessageTypeHandler {

    private static final String PRIVATE = "private";

    @Resource
    private MessageService messageService;

    /**
     * 根据Message进行解析
     *
     * @param message 消息对象
     * @return 消息类型
     */
    @Override
    public boolean canMessage(Message message) {
        return StringUtils.isNotBlank(message.getMessage()) && PRIVATE.equals(message.getMessage_type());
    }

    /**
     * 消息处理
     *
     * @param message 消息对象
     */
    @Override
    public void message(Message message, String msg) {
        messageService.sendPrivateMsg(msg, message.getUser_id(), -1);
    }
}
