package com.source.handler;

import com.source.bean.bot.CQCode;
import com.source.bean.bot.Message;
import com.source.service.GroupHandler;
import com.source.service.MessageService;
import com.source.utils.GetCookieUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/23/16:03
 */
@Component
public class GetCookieHandler implements GroupHandler {

    private final static String LOGIN = "登录";

    private final static String ERROR = "error";

    @Resource
    private GetCookieUtils getCookieUtils;
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
        return LOGIN.equals(message.getMessage());
    }

    /**
     * 消息解析
     *
     * @param message 消息
     */
    @Override
    public void analyze(Message message) {
        String qrCode = getCookieUtils.getQrCode();
        if (!ERROR.equals(qrCode)) {
            CQCode cqCode = CQCode.getInstance();
            String image = cqCode.image(qrCode);
            String at = cqCode.at(message.getUser_id());
            try {
                messageService.sendAssignLog(at + image, message.getGroup_id());
            } catch (Exception e) {
                e.printStackTrace();
            }
            getCookieUtils.loginListeners();
        }
    }
}
