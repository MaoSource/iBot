package com.source.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/11/16:46
 */
public class QQUtil {

    public static String hash33(String qrsig) {
        int e = 0;
        for (int i = 0; i < qrsig.length(); i++) {
            e += (e << 5) + qrsig.charAt(i);
        }
        return String.valueOf(2147483647 & e);
    }

    public static String getGTK(String skey) {
        int hash = 5381;
        for (int i = 0; i < skey.length(); ++i) {
            hash += (hash << 5) + skey.charAt(i);
        }
        return String.valueOf(hash & 0x7fffffff);
    }

    public static String uinToQQ(String uin) {
        int startIndex = 1;
        for (int i = 1; i < uin.length(); i++) {
            if (uin.charAt(i) != '0') {
                break;
            }
            startIndex++;
        }
        return uin.substring(startIndex);
    }

    /**
     * cookie转Map
     *
     * @param value cookie
     * @return map
     */
    public static Map<String, Object> cookieToMap(String value) {
        Map<String, Object> map = new HashMap<>(8);
        value = value.replace(" ", "");
        if (value.contains(";")) {
            String values[] = value.split(";");
            for (String val : values) {
                String vals[] = val.split("=");
                map.put(vals[0], vals[1]);
            }
        } else {
            String values[] = value.split("=");
            map.put(values[0], values[1]);
        }
        return map;
    }

    /**
     * 获取g_tk
     * <p>
     * [url=home.php?mod=space&uid=161696]@Return[/url] g_tk
     */
    public static String getG_tk(String cookie) {
        String p_skey = String.valueOf(cookieToMap(cookie).get("skey"));
        if (p_skey == null) {
            return "";
        }
        long hash = 5381;
        for (int i = 0; i < p_skey.length(); i++) {
            hash += (hash << 5) + p_skey.charAt(i);
        }
        return Long.toString(hash & 0x7fffffff);
    }

    /**
     * 获取iruleId
     *
     * @return 返回iruleId
     */
    public static String getIRuleId() {
        String[] weekDays = {"401573", "401567", "401568", "401569", "401570", "401571", "401572"};
        Calendar calendar = Calendar.getInstance();
        return weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }
}
