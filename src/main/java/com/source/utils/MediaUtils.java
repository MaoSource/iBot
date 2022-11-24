package com.source.utils;

import com.source.bean.bot.Message;
import com.source.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/10/13:34
 */
@Component
@Slf4j
public class MediaUtils {

    private static final String GROUP = "group";
    private static final String PRIVATE = "private";

    @Resource
    private MessageService messageService;

    public void sendMegs(Message message, String msg) {
        if (GROUP.equals(message.getMessage_type()) && StringUtils.isNotBlank(msg)) {
            messageService.sendAssignLog(msg, message.getGroup_id());
        } else if (PRIVATE.equals(message.getMessage_type()) && StringUtils.isNotBlank(msg)) {
            messageService.sendPrivateMsg(msg, message.getUser_id(), -1);
        }
    }

}
