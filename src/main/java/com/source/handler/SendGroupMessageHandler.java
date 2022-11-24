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
 * @date 2022/11/23/16:45
 */
@Component
public class SendGroupMessageHandler implements MessageTypeHandler {

    private static final String GROUP = "group";

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
        return StringUtils.isNotBlank(message.getMessage()) && GROUP.equals(message.getMessage_type());
    }

    /**
     * 消息处理
     *
     * @param message 消息对象
     */
    @Override
    public void message(Message message, String msg) {
        messageService.sendAssignLog(msg, message.getGroup_id());
    }
}
