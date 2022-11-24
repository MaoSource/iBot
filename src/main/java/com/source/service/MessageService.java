package com.source.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/10/13:35
 */
@Service
@Slf4j
public class MessageService {
    @Resource
    private RestTemplate restTemplate;
    @Value("${app-config.url}")
    private String url;
    @Value("${app-config.log-group-id}")
    private long LogGroupId;
    @Value("${app-config.admin-qq-id}")
    private long adminQQId;
    @Value("${app-config.admin-qq-token}")
    private String token;

    public void sendMsg(Object msg, String type, long userId, long groupId) {
        try {
            JSONObject json = new JSONObject();
            json.put("message", msg);
            if (groupId != -1) {
                json.put("group_id", groupId);
            }
            if (userId != -1) {
                json.put("user_id", userId);
            }
            JSONObject re = restTemplate.postForObject(url + "/send_" + type + "_msg?access_token=" + token, json, JSONObject.class);
            if (re == null || re.getIntValue("retcode") != 0) {
                log.warn("发送消息失败");
            }
            log.info("-->  QQ[{}]  Message:[{}]", userId, msg.toString().replace("\n", " "));
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("发送消息失败");
        }
    }

    public void sendPrivateMsg(Object msg, long userId, long groupId) {
        sendMsg(msg, "private", userId, groupId);
    }

    /**
     * 禁言
     *
     * @param duration
     * @param userId
     * @param groupId
     */
    public void groupBan(int duration, long userId, long groupId) {
        try {
            JSONObject json = new JSONObject();
            json.put("group_id", groupId);
            json.put("user_id", userId);
            json.put("duration", duration);
            JSONObject re = restTemplate.postForObject(url + "/set_group_ban" + "?access_token=" + token, json, JSONObject.class);
            if (re == null || re.getIntValue("retcode") != 0) {
                log.warn("发送消息失败");
            }
            log.info("-->  QQ[{}]  groupId:[{}]  duration[{}]", userId, groupId, duration);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("发送消息失败");
        }
    }

    /**
     * 撤回消息
     *
     * @param message_id
     */
    public void deleteMsg(long message_id) {
        try {
            JSONObject json = new JSONObject();
            json.put("message_id", message_id);
            JSONObject re = restTemplate.postForObject(url + "/delete_msg" + "?access_token=" + token, json, JSONObject.class);
            if (re == null || re.getIntValue("retcode") != 0) {
                log.warn("发送消息失败");
            }
            log.info("-->  message_id[{}]", message_id);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("发送消息失败");
        }
    }

    public void sendGroupMsg(Object msg, long groupId) {
        sendMsg(msg, "group", -1, groupId);
    }

    @Async
    public void sendLog(String msg) {
        log.info(msg.replace("\n", " "));
        if (LogGroupId == -2) {
            sendPrivateMsg(msg, adminQQId, -1);
        } else if (LogGroupId != -1) {
            sendGroupMsg(msg, LogGroupId);
        }
    }

    @Async
    public void sendAssignLog(Object msg, long gId) {
        log.info(msg.toString().replace("\n", " "));
        if (LogGroupId == -2) {
            sendPrivateMsg(msg, adminQQId, -1);
        } else if (LogGroupId != -1) {
            sendGroupMsg(msg, gId);
        }
    }

}
