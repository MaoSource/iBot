package com.source.service.impl;

import com.source.bean.bot.Message;
import com.source.factory.GroupFactory;
import com.source.service.GroupHandler;
import com.source.service.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * 群消息处理器
 *
 * @author Source
 * @date 2022/11/23/15:33
 */
@Service
@Slf4j
public class GroupMessageHandler implements MessageHandler {

    private static final String GROUP = "group";
    @Resource
    private GroupFactory groupFactory;

    /**
     * 根据Message进行解析
     *
     * @param message 消息对象
     * @return 消息类型
     */
    @Override
    public boolean canMessage(Message message) {
        String msg = message.getMessage();
        return StringUtils.isNotBlank(msg) && GROUP.equals(message.getMessage_type());
    }

    /**
     * 消息处理
     *
     * @param message 消息对象
     */
    @Override
    public void message(Message message) {
        log.info("群聊消息->" + message.getMessage());
        GroupHandler groupHandler = groupFactory.getGroupHandler(message);
        if (groupHandler != null) {
            groupHandler.analyze(message);
        }
    }
}
