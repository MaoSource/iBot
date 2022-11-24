package com.source.utils;

import com.source.bean.bot.CQCode;
import com.source.constant.Constant;
import com.source.properties.ConfigProperties;
import com.source.service.MessageService;
import com.source.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/11/16:39
 */
@Component
@Slf4j
public class GetCookieUtils {

    @Resource
    private ConfigProperties configProperties;
    @Resource
    private MessageService messageService;
    @Resource
    private RedisService redisService;

    ArrayList<String> list = new ArrayList<>();

    public String getQrCode() {
        String t = Double.toString(Math.random());
        String url = "https://ssl.ptlogin2.qq.com/ptqrshow?appid=" + configProperties.getApp_id() + "&e=2&l=M&s=3&d=72&v=4&t=" + t + "&daid=" + configProperties.getDaid() + "&pt_3rd_aid=" + configProperties.getPt_3rd_aid();
        try {
            HashMap<String, Object> header = new HashMap<>(8);
            header.put("Host", "ssl.ptlogin2.qq.com");
            header.put("Referer", "https://xui.ptlogin2.qq.com/");
            header.put("Connection", "keep-alive");
            header.put("Accept-Language", "zh-CN,zh;q=0.9");
            header.put("Accept-Encoding", "gzip, deflate, br");
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
            HttpUtil.ResEntity resEntity = HttpUtil.doDownload(url, header, null, null);
            byte[] data = resEntity.getBytes();
            if (data != null && data.length > 0) {
                Map<String, Object> cookies = resEntity.getCookies();
                if (cookies != null) {
                    list.add(cookies.get("qrsig").toString());
                    return "base64://" + Base64.getEncoder().encodeToString(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "error";
    }

    public String loginListener(String qrsig) {
        String ptqrtoken = QQUtil.hash33(qrsig);
        String action = "0-0-" + System.currentTimeMillis();
        String url = "https://ssl.ptlogin2.qq.com/ptqrlogin?u1=" + configProperties.getU1() + "&ptqrtoken=" + ptqrtoken + "&ptredirect=0&h=1&t=1&g=1&from_ui=1&ptlang=2052&action=" + action + "&js_ver=22080914&js_type=1&pt_uistyle=40&aid=" + configProperties.getApp_id() + "&daid=" + configProperties.getDaid() + "&has_onekey=1&&o1vId=9993c2330b8d6ea4e45829d0f5f8c59a" + "&pt_3rd_aid=" + configProperties.getPt_3rd_aid();
        Map<String, Object> cookies = new HashMap<>(8);
        cookies.put("qrsig", qrsig);
        HttpUtil.ResEntity resEntity = null;
        try {
            resEntity = HttpUtil.doGet(url, null, cookies, null);
            String result = resEntity.getResponse();
            if (result != null) {
                if (result.contains(Constant.NOTEXPIRED)) {
                    return Constant.NOTEXPIRED;
                } else if (result.contains(Constant.UNDERCERTIFICATION)) {
                    return Constant.UNDERCERTIFICATION;
                } else if (result.contains(Constant.LOGINREJECT)) {
                    list.remove(qrsig);
                    return Constant.LOGINREJECT;
                } else if (result.contains(Constant.LOGINSUCCESSFUL)) {
                    list.remove(qrsig);
                    Map<String, Object> loginSuccess = resEntity.getCookies();
                    CQCode cqCode = new CQCode();
                    String uin = QQUtil.uinToQQ(loginSuccess.get("uin").toString());
                    String at = cqCode.at(Long.parseLong(uin));
                    messageService.sendAssignLog(at + "登录成功~", Constant.SOURCEGROUPID);
                    redisService.set(uin, loginSuccess, Constant.TIME);
                    return Constant.LOGINSUCCESSFUL;
                } else if (result.contains(Constant.EXPIRED)) {
                    list.remove(qrsig);
                    return Constant.EXPIRED;
                } else {
                    return "error";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    public void loginListeners() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ScheduledExecutorService scheduledService = Executors.newSingleThreadScheduledExecutor();
        // 开一个线程挂后台
        executorService.execute(() -> {
            // 定时检查是否登录
            scheduledService.scheduleAtFixedRate(() -> {
                log.info("检查code");
                list.forEach(this::loginListener);
                if (list.size() == 0) {
                    // 结束定时器
                    scheduledService.shutdown();
                }
            }, 0, 3, TimeUnit.SECONDS);
            try {
                scheduledService.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
    }

    public static void main(String[] args) {
        ArrayList<String> objects = new ArrayList<>();
        objects.add("111");
        objects.forEach(System.out::println);

        objects.remove("111");
        objects.forEach(System.out::println);
        System.out.println(objects.size());

    }
}
