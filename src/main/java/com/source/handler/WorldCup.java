package com.source.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.source.bean.bot.CQCode;
import com.source.bean.bot.Message;
import com.source.service.GroupHandler;
import com.source.service.MessageService;
import com.source.utils.HttpUtil;
import com.source.utils.TeamIdUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/12/01/17:23
 */
@Component
public class WorldCup implements GroupHandler {

    private final static String RECORD = "战绩查询";

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
        String msg = message.getMessage();
        return msg.startsWith(RECORD);
    }

    /**
     * 消息解析
     *
     * @param message 消息
     */
    @Override
    public void analyze(Message message) {
        String msg = message.getMessage();
        String substring = msg.substring(RECORD.length());
        Integer teamId = TeamIdUtils.getTeamId(substring);
        CQCode cqCode = CQCode.getInstance();
        String at = cqCode.at(message.getUser_id());
        if (teamId == null) {
            messageService.sendAssignLog(at + message.getMessage() + "为空,请输入正确的团队~", message.getGroup_id());
        } else {
            JSONArray data = getData(teamId);
            if (data != null) {
                StringBuilder builder = new StringBuilder();
                data.forEach(s -> {
                    JSONObject s1 = (JSONObject) s;
                    builder.append(s1.getString("groupName")).append("-").append(s1.getString("venue")).append("\n");
                    builder.append(s1.getString("away")).append("--->").append(s1.getIntValue("awayScore")).append("\n");
                    builder.append(s1.getString("home")).append("--->").append(s1.getIntValue("homeScore")).append("\n");
                    if (s1.getIntValue("status") == 4) {
                        if (s1.getIntValue("awayScore") > s1.getIntValue("homeScore")) {
                            builder.append(s1.getString("away")).append("--->").append("胜").append("\n");
                        } else if (s1.getIntValue("awayScore") == s1.getIntValue("homeScore")) {
                            builder.append("平").append("\n");
                        } else {
                            builder.append(s1.getString("home")).append("--->").append("胜").append("\n");
                        }
                    } else {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(s1.getLong("date"));
                        String res = simpleDateFormat.format(date);
                        builder.append("比赛时间--->").append(res).append("\n");
                    }
                    builder.append("----------------------\n");
                });
                messageService.sendAssignLog(at + message.getMessage() + "\n\n" + builder, message.getGroup_id());
            } else {
                messageService.sendAssignLog(at + message.getMessage() + ",获取失败~", message.getGroup_id());
            }
        }
    }

    public JSONArray getData(Integer teamId) {
        HashMap<String, Object> param = new HashMap<>(8);
        param.put("teamId", teamId);
        param.put("source", "pc");
        try {
            HttpUtil.ResEntity resEntity = HttpUtil.doGet("https://gw.m.163.com/base/worldCup/qatar/schedule/groupByTeam", param);
            String response = resEntity.getResponse();
            JSONObject dataObj = JSON.parseObject(response);
            JSONObject data = dataObj.getJSONObject("data");
            JSONArray teamScheduleList = data.getJSONArray("teamScheduleList");
            if (teamScheduleList.size() != 0) {
                return teamScheduleList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
