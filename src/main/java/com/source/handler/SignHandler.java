package com.source.handler;

import com.source.bean.bot.CQCode;
import com.source.bean.bot.Message;
import com.source.service.GroupHandler;
import com.source.service.MessageService;
import com.source.service.RedisService;
import com.source.utils.DaoJuChengUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/23/16:13
 */
@Component
public class SignHandler implements GroupHandler {

    private final static String SIGN = "签到";

    @Resource
    private RedisService redisService;
    @Resource
    private DaoJuChengUtils daoJuChengUtils;
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
        return SIGN.equals(message.getMessage());
    }

    /**
     * 消息解析
     *
     * @param message 消息
     */
    @Override
    public void analyze(Message message) {
        CQCode cqCode = CQCode.getInstance();
        Map<String, Object> cookies = (Map<String, Object>) redisService.get(String.valueOf(message.getUser_id()));
        String at = cqCode.at(message.getUser_id());
        if (cookies == null) {
            messageService.sendAssignLog(at + "请先登录~", message.getGroup_id());
        } else {
            String cookie = "djc_appSource=android; djc_appVersion=106; uin=" + cookies.get("uin") + "; skey=" + cookies.get("skey") + ";";
            daoJuChengUtils.run(cookie, message);
        }
    }
}
