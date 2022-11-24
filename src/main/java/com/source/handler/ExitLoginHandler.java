package com.source.handler;

import com.source.bean.bot.CQCode;
import com.source.bean.bot.Message;
import com.source.service.GroupHandler;
import com.source.service.MessageService;
import com.source.service.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/23/16:10
 */
@Component
public class ExitLoginHandler implements GroupHandler {

    private final static String EXIT = "退出";

    @Resource
    private RedisService redisService;

    @Resource
    private MessageService messageService;

    /**
     * 根据Message进行解析
     *
     * @param message 消息对象
     * @return 消息类型
     */
    @Override
    public boolean canAnalyze(Message message) {
        return EXIT.equals(message.getMessage());
    }

    /**
     * 消息解析
     *
     * @param message 消息
     */
    @Override
    public void analyze(Message message) {
        CQCode cqCode = CQCode.getInstance();
        redisService.del(String.valueOf(message.getUser_id()));
        String at = cqCode.at(message.getUser_id());
        messageService.sendAssignLog(at + "退出成功~", message.getGroup_id());
    }
}
