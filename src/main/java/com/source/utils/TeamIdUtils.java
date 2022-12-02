package com.source.utils;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/12/01/17:28
 */
public class TeamIdUtils {

    private static final TeamIdUtils TEAM_ID_UTILS = new TeamIdUtils();

    private TeamIdUtils() {

    }

    public static TeamIdUtils getInstance() {
        return TEAM_ID_UTILS;
    }

    private static final HashMap<String, Integer> MAP = new HashMap<>(32);

    static {
        MAP.put("阿根廷", 13727);
        MAP.put("厄瓜多尔", 11082);
        MAP.put("卡塔尔", 11289);
        MAP.put("伊朗", 14298);
        MAP.put("英格兰", 11079);
        MAP.put("荷兰", 10870);
        MAP.put("塞内加尔", 13365);
        MAP.put("威尔士", 19907);
        MAP.put("美国", 13611);
        MAP.put("沙特阿拉伯", 10584);
        MAP.put("突尼斯", 13613);
        MAP.put("丹麦", 10867);
        MAP.put("墨西哥", 11764);
        MAP.put("澳大利亚", 13832);
        MAP.put("法国", 10234);
        MAP.put("克罗地亚", 11606);
        MAP.put("摩洛哥", 11763);
        MAP.put("日本", 14529);
        MAP.put("德国", 11746);
        MAP.put("哥斯达黎加", 11837);
        MAP.put("西班牙", 14227);
        MAP.put("加拿大", 11138);
        MAP.put("比利时", 10316);
        MAP.put("喀麦隆", 11707);
        MAP.put("瑞士", 10871);
        MAP.put("韩国", 10586);
        MAP.put("乌拉圭", 13447);
        MAP.put("加纳", 11017);
        MAP.put("葡萄牙", 13152);
        MAP.put("塞尔维亚", 10315);
        MAP.put("巴西", 10477);
        MAP.put("波兰", 10823);
    }

    /**
     * 通过团队名字查询ID
     *
     * @param teamName 团队名称
     * @return 团队编号
     */
    public static Integer getTeamId(String teamName) {
        return MAP.get(teamName);
    }
}
