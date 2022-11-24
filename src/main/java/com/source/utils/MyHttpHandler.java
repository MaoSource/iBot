package com.source.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/14/16:47
 */
@Slf4j
public class MyHttpHandler {

    public static String addDjcSignParams(String QQuin) {
        try {
            String paramString = "";
            paramString = QQuin;
            String DeviceToken = "A5837B41-E39B-4593-91C9-2DB832232B27";
            long l2 = System.currentTimeMillis();
            long l3 = 0L;
            int i = 128;
            String nowMillSecond = String.valueOf(l2);
            paramString = QQuin + "+A5837B41-E39B-4593-91C9-2DB832232B27+" + l2 + "+128";
            l3 = System.currentTimeMillis();
            byte[] bytes = EncDecUtils.aesEncrypt(paramString, "84e6c6dc0f9p4a56");
            byte[] bytes1 = EncDecUtils.rsaEncrypt(bytes);
            paramString = EncDecUtils.byte2hex(bytes1);
            return paramString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}
