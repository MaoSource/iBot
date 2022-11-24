package com.source.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.source.bean.DjCity.RoleInfo;
import com.source.bean.bot.CQCode;
import com.source.bean.bot.Message;
import com.source.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/14/16:38
 */
@Slf4j
@Component
public class DaoJuChengUtils {

    @Value("#{${tao_poly_city.headers}}")
    private Map<String, Object> httpHeaders;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private MessageService messageService;

    /**
     * 判断当前是不是在处理overdueCookie中的Cookie
     * 0:不是，1:是
     */
    public int status = 0;

    public void run(String cookies, Message message) {
        CQCode cqCode = new CQCode();
        String at = cqCode.at(message.getUser_id());
        StringBuffer buffer = new StringBuffer();
        Integer newBalance = 0;
        Integer oldBalance = 0;
        String signIn = signIn(cookies).getResponse();
        if ("非常抱歉，请先登录！".equals(JSONObject.parseObject(signIn).get("msg").toString())) {
            messageService.sendAssignLog(at + "非常抱歉，请先登录！", message.getGroup_id());
        } else {
            System.out.println("-----账号" + QQUtil.cookieToMap(cookies).get("uin") + "-----");
            oldBalance = queryBalance(cookies).getInteger("balance");
            String giveSignIn = giveSignIn(cookies, 324410).getResponse();
            String browsePage = browsePage(cookies);
            String giveBrowsePage = giveBrowsePage(cookies);
            // 安卓游戏礼包
            String giveGamePack = giveGamePack(cookies);
            String giveIosGamePack = giveIosGamePack(cookies);
            String giveGamePackTask = giveGamePackTask(cookies);
            String giveSilverBox = giveSilverBox(cookies);
            String giveGoldBox = giveGoldBox(cookies);
            deleteWishS(cookies);
            addWishs(cookies);
            String giveWishPack = giveWishPack(cookies);
            String giveBonus = giveBonus(cookies);
            newBalance = queryBalance(cookies).getInteger("balance");

            JSONObject giveSignInJson = JSONObject.parseObject(giveSignIn);
            JSONObject giveBrowsePageJson = JSONObject.parseObject(giveBrowsePage);
            JSONObject giveGamePackJson = JSONObject.parseObject(giveIosGamePack);
            JSONObject giveGamePackTaskJson = JSONObject.parseObject(giveGamePackTask);
            JSONObject giveSilverBoxJson = JSONObject.parseObject(giveSilverBox);
            JSONObject giveGoldBoxJson = JSONObject.parseObject(giveGoldBox);
            JSONObject giveWishPackJson = JSONObject.parseObject(giveWishPack);
            buffer.append("\n账号：")
                    .append(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookies).get("uin"))))
                    .append(",\n签到：")
                    .append(StringUtils.isBlank(giveSignInJson.get("msg").toString()) ?
                            giveSignInJson.getJSONObject("modRet").get("sMsg") :
                            giveSignInJson.get("msg").toString())
                    .append("\n浏览：").append(giveBrowsePageJson.get("msg").toString())
                    .append("\n领取游戏礼包：")
                    .append(giveIosGamePack != null ? giveGamePackJson.getJSONObject("data").getJSONArray("list").size() != 0 ||
                            StringUtils.isNotBlank(giveGamePackJson.get("msg").toString()) ?
                            giveGamePackJson
                                    .getJSONObject("data")
                                    .getJSONArray("list")
                                    .getJSONObject(0)
                                    .get("sTaskDesc").toString() :
                            giveGamePackJson.get("msg").toString() : "未绑定王者账号")
                    .append("\n领取游戏礼包任务：").append(giveGamePackTaskJson.get("msg").toString())
                    .append("\n领取许愿任务：").append(giveWishPackJson.get("msg").toString())
                    .append("\n累计签到奖励：").append(giveBonus)
                    .append("\n白银宝箱：").append(giveSilverBoxJson.get("msg").toString())
                    .append("\n黄金宝箱：").append(giveGoldBoxJson.get("msg").toString())
                    .append("\n本月累计签到：").append(findSignInDays(cookies)).append("天")
                    .append("\n本次道聚城操作共获得：").append(newBalance - oldBalance)
                    .append("个豆子\n历史总获取：").append(queryBalance(cookies).getInteger("allin"))
                    .append("个豆子\n当前余额：").append(newBalance)
                    .append("个豆子\n");
            messageService.sendAssignLog(at + buffer, message.getGroup_id());
        }
    }

    /**
     * 签到
     *
     * @param cookie cookie
     * @return json
     */
//    public HttpUtil.ResEntity signIn(String cookie) {
//        HashMap<String, Object> map = new HashMap<>(16);
//        map.put("appSource", "ios");
//        map.put("appSourceDetail", "ios15.1");
//        map.put("appVersion", "128");
//        map.put("djcRequestId", "A5837B41-E39B-4593-91C9-2DB832232B27-1637333481084-7");
//        map.put("g_tk", QQUtil.getG_tk(cookie));
//        map.put("iActivityId", "11117");
//        map.put("iFlowId", "96939");
//        map.put("p_tk", QQUtil.getG_tk(cookie));
//        map.put("sDeviceID", "A5837B41-E39B-4593-91C9-2DB832232B27");
//        if (StringUtils.equals("o1134496699", QQUtil.cookieToMap(cookie).get("uin"))) {
//            map.put("sDjcSign", "93637876c7f8c4c15693755ce0836b62551be0680963f2d5ff0fe115e851855bfed0f4419b7893a6b31cf4a63fa728a" +
//                    "dc0d36254f7f5545f2dae3b7069c2b638e67194a7b5dde9dcbbdeb56a92d6e92b9001d3362580a922f8558f92ec881dba2" +
//                    "dcc9b8c9beb0dd4aa923602760e53aeccdd283b9fb4d60aab158865b666b4b684e6076955deb45b8b43e21e9fbe316a872" +
//                    "26347a3a95d27d1442ea1de8e66ffdfc01b33e4f3ae06d31e9a770c928432ea8a4e8ac8e2f27c978ad66df359135c39ed3" +
//                    "c5d778ecb2f012a919283f06fbdae50c3cc660b55a30838f991448ef6b2e6b694869eee302f7842cc882f80c50d949b44c" +
//                    "ce3037d37e0ec0440e95bd819");
//        } else if (StringUtils.equals("o1134496928", QQUtil.cookieToMap(cookie).get("uin"))) {
//            map.put("sDjcSign", "6650887fe74c6066e3bbb22707d37bc3f446e3aaa6d00c9513124884a609c32f403e4705ce22b619cababcf6094" +
//                    "d700e4d61fb6b64e90238778daa0b5d557038c781e08c329f2803cbc24d5d8b58879f6110cd80075a9913a01ff03c3ad230d56c" +
//                    "5616b27a2e4f1281690b21e92d3ed0eff43378ba5da057c8b8d0279649b63e8da3b8a5867fe6c0aaa52fa1251cf161dbef7e114" +
//                    "fa76c118fa2acdd7fd51d3a4ee4b4163991a69138a713caa9852cb2c9db7ce0a9c2cb2309ded6299834086082790838e1186c5a" +
//                    "04bb282260efdd3002f00bddda8b0712c09f514406542536d98763f529e5cf80fa2d055e681f969ed247357fc5cfdb5545dfaed" +
//                    "024c6e720");
//        } else if (StringUtils.equals("o1043596955", QQUtil.cookieToMap(cookie).get("uin"))) {
//            map.put("sDjcSign", "a55b0e9602d351cc1dad3d5188bce4f2dc156ca7f9c75a9d3118bc35946ddbf117c01fe5be9fd8a4ee2b6f" +
//                    "2d6ca7f11daed4f2bbf28b6e1dc9970d5aee7b9ca254b52a85341f73d8ca935cb0ea00b4cdaaeb9c7fc634dfa512f48002" +
//                    "4a2d068421f37c1ffc3b5544d483bb1990db3906fa8e64cc82508afa5a3c7c12ae9e359adae143a602a17e09e44831ef34" +
//                    "b98fa484544084f814be86bcf27513a818a733e1585f01d7ff13db6a3932bdcb8728c22ea3d379830abe5ccce5d291c58f" +
//                    "57b229847d9401865e20d26d0f73d75e202b2632d80100fb55310251d58c0cb3a8eaf07900d27f9dbfcad8081d08dc8c35" +
//                    "86882f99a829d5e220dfeeea295f9de6d9");
//        } else if (StringUtils.equals("o2698660486", QQUtil.cookieToMap(cookie).get("uin"))) {
//            map.put("sDjcSign", "9479ea069b1184734a2e7df525e99fdb31c3552ac73ea051c66776109fc20a99d4e7a9656592e581941fda" +
//                    "91cef7eed204a8e619911acd8ee5da5673e35df58b82db16dc4c3efa92353441425eca6b0f9f64fe7c91b236cd11430307" +
//                    "fad887d33b364d1fb37846cc262707af0242304a881b182aca84e9aea731001c562ef539acc95fdc246132c2a536886f1" +
//                    "5949495c2b9822248e66f06e88a0d8805d2941669e77cb5005fe51507111817e519311d8b547344e33edf02e49981a2b53" +
//                    "049e9e16c5303087e8624be7a8382975972ff6fb307311f43d97d842536104a2ea7169394ac4262240335fd19e18407e5" +
//                    "89c58cbe8c3564e1bbdb369f2fe62dc22e6d");
//        } else if (StringUtils.equals("o1191369923", QQUtil.cookieToMap(cookie).get("uin"))) {
//            map.put("sDjcSign", "6e9cb1f1e0e344df5b9b039f2cd20342dae166b3498732552af12cc5fc67c1a269a67c9d50e33c912557b" +
//                    "dacf4e74fc2e33f28a84294f43fd10e2831d6d859ddc4816ecdffa1fbc2a886f718032da916d273a29cda7712ca51346206" +
//                    "a72f084b62ca4c9e966e5289ebd325eb7b8cd5df05857697dc46c1bdfdf57049f55514015536c5421e7e17f130e26d49184" +
//                    "784b64753047f01c421fc208bf8f3fd0ad568714b7cfc5abfdaebe740f9435ead4fb667f60cb8373396f1f562d7b3382f09" +
//                    "5e17caf6ea80cd2175d9ec695e1c7d1d7a76c6e8f05b7bc2a9045a98b0dae74d0a78de526f125d42d1f3c4a16f7595f22d" +
//                    "f70778124dbc811524d2000b9cfeaff1");
//        } else if (StringUtils.equals("o362173123", QQUtil.cookieToMap(cookie).get("uin"))) {
//            map.put("sDjcSign", "5037734c613f68e0fb3a718135fc6807e7420905ad3f9d368106c59b75d25c0c69b9a0dcdea5259e200df80" +
//                    "4533cf7dfb75a163648e40306dfbce6f52356f9e0cae43df4dfecb0a65109eacbaaee1c1947a8e125056e496f95b1b8ef61" +
//                    "6887234fcc1e2610ee6d7a6aa21766f32bcdd0388d2ad7ccb6b7a3f204f8a3fbfddc4234301ee221c381dc4013a9efd7637" +
//                    "f9a34df30a8548fe2751553c11486d549ca765780ccee07b1a69184127e5b2922aee88b0069f15967b7c66e7c76c43ea4c9" +
//                    "6574484eae88aeea881f6848ae80c746766ae79006c62fc4137aaf8f49a82c8a333064c0f7c2aac8b568aca2d703bde9dc0" +
//                    "8f34c1d9f946c6242a715f6e9ba24");
//        } else if (StringUtils.equals("o1075703166", QQUtil.cookieToMap(cookie).get("uin"))) {
//            map.put("sDjcSign", "20363a2cb2ba71c5b3465632fcc128cf8baaf64523b40b4958e21d50f51ea466e7dfe44e5376d841804c90" +
//                    "a430b239f09a4c565f728c3b6370c0ed327cc60d3f31e4d5e91db01fac3850467809e70b8c6ae5a074a20bb874458a40477" +
//                    "d21c667a61112bd61631ea9e02f24400c8a823e83fa4e355cb3f008dd15e6acc7f49c039fd033e74e3594e1166a697ff6c7" +
//                    "95b5c39a0a0ccff56923e7427fa5ee892d9f8c78cc52063097dcb6debcd20a11716d392fdbf4c80e50896c6222fadc1888a" +
//                    "273cd3868b9b584ddd311d8a90d6d108af62ad7e8d60b9e042641b82e8cf0bb73bfec6f361b61846c759d437ff9e22c80d3" +
//                    "93874e27e97383c77b73463a50212b");
//        } else if (StringUtils.equals("o1936096680", QQUtil.cookieToMap(cookie).get("uin"))) {
//            map.put("sDjcSign", "8913b7f33a3d542c4c9d52248dbb265646a7c7153c0d1653275b99d44c74c61f6b2c815d80f5c8d2bed1" +
//                    "848513dc8f6b6d34994f6dc764827b33c52dcc5e60828ebdf06770d16e4378d7ca4f1f29dc120969a9120b52f2b886ccf" +
//                    "3fb7462b268306ea278cb676fb228e9d4b4ef57a1685b4f94ff673d97f608506dd547a2afd5eb47553d994da2ec8203c5f" +
//                    "4b60802bf35b3525c7f7bf1383b1f91053cec4189fed44cd9e41930bad01ae6fe228e67d116a523748cc45210fae91ad71" +
//                    "45dac5f176da0730955bb0f928d5afca4bd97d8be186c6de4e6b9c86bbf7ab0a3f17b55ac2f7671445fccbde1ac6bfb54af" +
//                    "3931a5468751cfdd05495d4586a10c6e2622");
//        } else if (StringUtils.equals("o839200091", QQUtil.cookieToMap(cookie).get("uin"))) {
//            map.put("sDjcSign", "7b5f0d68d2585d006db39d90b02cda99afe045a002aaf4e8d64ac4a33a61b549fde25bf07d3d67f4b7b9fa" +
//                    "52358d9be16edff46bdf46a4d2bb67071037253dcdcf8ab8508d94eacc906c0dea524d58dbf17f3ac3f3c272e05cb75b3ff" +
//                    "ca4a520c800d4cdf60ae3f4a4f01136933b130263d2792a5b586b12d31231a9475b788b107e9297f02a8cf880c7591e2f6b" +
//                    "a94705e7cd55d8cc929f0a14e88561e50eda1c2c46d076fab22a7b4191e9293dc5668cf77b7f14b5fbe64ef9658c07296450" +
//                    "30644f5519214f4472a4c09f3977b15c5be25b0161ce07cc35834f6139dcb9395d9c0e0a972d7da654833a132d9ae2c17873" +
//                    "ede8c94fdb0133dacc19a074d68a");
//        } else {
//            map.put("sDjcSign", "6650887fe74c6066e3bbb22707d37bc3f446e3aaa6d00c9513124884a609c32f403e4705ce22b619cababcf6094" +
//                    "d700e4d61fb6b64e90238778daa0b5d557038c781e08c329f2803cbc24d5d8b58879f6110cd80075a9913a01ff03c3ad230d56c" +
//                    "5616b27a2e4f1281690b21e92d3ed0eff43378ba5da057c8b8d0279649b63e8da3b8a5867fe6c0aaa52fa1251cf161dbef7e114" +
//                    "fa76c118fa2acdd7fd51d3a4ee4b4163991a69138a713caa9852cb2c9db7ce0a9c2cb2309ded6299834086082790838e1186c5a" +
//                    "04bb282260efdd3002f00bddda8b0712c09f514406542536d98763f529e5cf80fa2d055e681f969ed247357fc5cfdb5545dfaed" +
//                    "024c6e720");
//        }
//        map.put("sServiceDepartment", "djc");
//        map.put("sServiceType", "dj");
//        try {
//            return HttpUtil.doPosts("https://comm.ams.game.qq.com/ams/ame/amesvr?ameVersion=0.3&sServiceType=dj&" +
//                    "iActivityId=11117&sServiceDepartment=djc&set_info=newterminals&&weexVersion=0.28.0&platform=iOS&" +
//                    "weex_id=45&deviceModel=iPhone13%2C3&&osVersion=15.1&appSource=ios&appSourceDetail=ios15.1&" +
//                    "appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&&appSource=ios&appSourceDetail=ios15.1&" +
//                    "appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&" +
//                    "djcRequestId=A5837B41-E39B-4593-91C9-2DB832232B27-1637333481084-7", httpHeaders, cookie, map);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * 签到
     *
     * @param cookie cookie
     * @return json
     */
    public HttpUtil.ResEntity signIn(String cookie) {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("appSource", "ios");
        map.put("appSourceDetail", "ios15.1");
        map.put("appVersion", "128");
        map.put("djcRequestId", "A5837B41-E39B-4593-91C9-2DB832232B27-1637333481084-7");
        map.put("g_tk", QQUtil.getG_tk(cookie));
        map.put("iActivityId", "11117");
        map.put("iFlowId", "96939");
        map.put("p_tk", QQUtil.getG_tk(cookie));
        map.put("sDeviceID", "A5837B41-E39B-4593-91C9-2DB832232B27");
        map.put("sDjcSign", MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))));
        map.put("sServiceDepartment", "djc");
        map.put("sServiceType", "dj");
        try {
            return HttpUtil.doPosts("https://comm.ams.game.qq.com/ams/ame/amesvr?ameVersion=0.3&sServiceType=dj&" +
                    "iActivityId=11117&sServiceDepartment=djc&set_info=newterminals&&weexVersion=0.28.0&platform=iOS&" +
                    "weex_id=45&deviceModel=iPhone13%2C3&&osVersion=15.1&appSource=ios&appSourceDetail=ios15.1&" +
                    "appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&&appSource=ios&appSourceDetail=ios15.1&" +
                    "appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&" +
                    "djcRequestId=A5837B41-E39B-4593-91C9-2DB832232B27-1637333481084-7", httpHeaders, cookie, map);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 领取签到礼包
     * iFlowId:324410
     *
     * @param cookie cookie
     * @return json
     */
    public HttpUtil.ResEntity giveSignIn(String cookie, Integer iFlowId) {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("appSource", "ios");
        map.put("appSourceDetail", "ios15.1");
        map.put("appVersion", "128");
        map.put("djcRequestId", "A5837B41-E39B-4593-91C9-2DB832232B27-1637333481084-7");
        map.put("g_tk", QQUtil.getG_tk(cookie));
        map.put("iActivityId", "11117");
        map.put("iFlowId", iFlowId);
        map.put("p_tk", QQUtil.getG_tk(cookie));
        map.put("sDeviceID", "A5837B41-E39B-4593-91C9-2DB832232B27");
        map.put("sDjcSign", MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))));
        map.put("sServiceDepartment", "djc");
        map.put("sServiceType", "dj");
        try {
            return HttpUtil.doPosts("https://comm.ams.game.qq.com/ams/ame/amesvr?ameVersion=0.3&sServiceType=dj&" +
                    "iActivityId=11117&sServiceDepartment=djc&set_info=newterminals&&weexVersion=0.28.0&platform=iOS&" +
                    "weex_id=45&deviceModel=iPhone13%2C3&&osVersion=15.1&appSource=ios&appSourceDetail=ios15.1&appVersion" +
                    "=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&&appSource=ios&appSourceDetail=ios15.1&appVersion" +
                    "=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&djcRequestId" +
                    "=A5837B41-E39B-4593-91C9-2DB832232B27-1637333481084-7", httpHeaders, cookie, map);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 浏览页面
     *
     * @param cookie cookie
     * @return json
     */
    public String browsePage(String cookie) {
        String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=app.task.report&&weexVersion=0.28.0&platform=iOS&" +
                "weex_id=115&deviceModel=iPhone13%2C3&&osVersion=15.1&appSource=ios&appSourceDetail=ios15.1&appVersion=128&" +
                "sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&task_type=activity_center&appSource=ios&appSourceDetail=" +
                "ios15.1&appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&" +
                "djcRequestId=A5837B41-E39B-4593-91C9-2DB832232B27-1637661811612-1&" +
                "sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return re.getBody();
    }

    /**
     * 领取浏览页面
     *
     * @param cookie cookie
     * @return json
     */
    public String giveBrowsePage(String cookie) {
        String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=welink.usertask.main&optype=receive_usertask&" +
                "appid=1001&&output_format=json&iruleId=100040&bizcode=&appSource=ios&appSourceDetail=ios15.1&" +
                "appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&" +
                "djcRequestId=A5837B41-E39B-4593-91C9-2DB832232B27-1637227533913-0" +
                "&sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return re.getBody();
    }

    /**
     * 领取游戏礼包
     *
     * @param cookie cookie
     * @return json
     */
    public String giveGamePack(String cookie) {
        if (getWzUserInfo(cookie) != null) {
            String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=app.package.receive&&weexVersion=0.28.0.1&" +
                    "platform=android&weex_id=4&deviceModel=MIX%202&appid=1001&output_format=json&optype=receive_usertask_game" +
                    "&iruleId=" + QQUtil.getIRuleId() + "&bizcode=yxzj&roleCode=DBA45DC46AA3526D916BC795A5347191&systemID=1&channelID=1" +
                    "&sRoleName=" + getWzUserInfo(cookie).get("roleName").toString() +
                    "&sPartition=" + getWzUserInfo(cookie).get("areaID").toString() + "&channelKey=qq" +
                    "&sDeviceID=472b3ab313c79b4f7414fc9c46d818df36b50b7fa9404ff3ef1daae42828a390&" +
                    "djcRequestId=472b3ab313c79b4f7414fc9c46d818df36b50b7fa9404ff3ef1daae42828a390-1638948465648-752&" +
                    "appVersion=128&p_tk=" + QQUtil.getG_tk(cookie) + "&osVersion=Android-28&ch=10008&sVersionName=v4.4.2.1&appSource=android" +
                    "&sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "";
            HttpHeaders headers = setHeaders(cookie);
            HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
            ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            return re.getBody();
        } else {
            return null;
        }
    }

    /**
     * 查询王者角色信息
     *
     * @param cookie cooke
     * @return 角色信息
     */
    public JSONObject getWzUserInfo(String cookie) {
        JSONArray objects = allRoleList(cookie);
        for (Object object : objects) {
            JSONObject jsonObject = (JSONObject) object;
            if (org.apache.commons.lang3.StringUtils.equals("yxzj", (String) jsonObject.get("sBizCode"))) {
                return jsonObject.getJSONObject("sRoleInfo");
            }
        }
        return null;
    }

    /**
     * 领取游戏礼包
     *
     * @param cookie cookie
     * @return json
     */
    public String giveIosGamePack(String cookie) {
        if (getWzUserInfo(cookie) != null) {
            String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=app.package.batch_receive&&weexVersion=0.28.0&" +
                    "platform=iOS&weex_id=4&deviceModel=iPhone13%2C3&&osVersion=15.1&appSource=ios&appSourceDetail=ios15.1&" +
                    "appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&appid=1001&output_format=json&optype=" +
                    "receive_usertask_game&iruleId=" + QQUtil.getIRuleId() + "&iCategory=10&bizcode=yxzj&" +
                    "roleCode=9CA482ED81DDB4EDE9D3F2889552E506&" +
                    "systemID=0&channelID=2&sRoleName=" + getWzUserInfo(cookie).get("roleName").toString() + "&" +
                    "sPartition=" + getWzUserInfo(cookie).get("areaID").toString() + "&channelKey=qq&" +
                    "appSource=ios&" +
                    "appSourceDetail=ios15.1&appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&" +
                    "p_tk=" + QQUtil.getG_tk(cookie) + "&" +
                    "djcRequestId=A5837B41-E39B-4593-91C9-2DB832232B27-1637892727570-2" +
                    "&sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "";
            HttpHeaders headers = setHeaders(cookie);
            HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
            ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            return re.getBody();
        } else {
            return null;
        }
    }

    /**
     * 领取任务游戏豆子
     *
     * @param cookie cookie
     * @return json
     */
    public String giveGamePackTask(String cookie) {
        String url = "https://apps.game.qq.com/daoju/v3/api/we/usertaskv2/Usertask.php?iAppId=1001&_app_id=1001&" +
                "p_tk=" + QQUtil.getG_tk(cookie) + "&output_format=json&_output_fmt=json&weex_id=45&optype=receive_" +
                "usertask&appid=1001&iruleId=100066&appSource=ios&appSourceDetail=ios15.1&appVersion=128&sDeviceID=" +
                "A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=678421038&djcRequestId=A5837B41-E39B-4593-91C9-2DB832232B27-" +
                "1637743875162-6";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return re.getBody();
    }

    /**
     * 白银宝箱
     *
     * @param cookie cookie
     * @return json
     */
    public String giveSilverBox(String cookie) {
        String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=welink.usertask.main&optype=receive_usertask&" +
                "appid=1001&&output_format=json&iruleId=100001&bizcode=&appSource=ios&appSourceDetail=ios15.1&" +
                "appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&" +
                "p_tk=" + QQUtil.getG_tk(cookie) + "&djcRequestId=A5837B41-E39B-4593-91C9-2DB832232B27-1637743999893-5&" +
                "sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return re.getBody();
    }

    /**
     * 黄金宝箱
     *
     * @param cookie cookie
     * @return json
     */
    public String giveGoldBox(String cookie) {
        String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=welink.usertask.main&optype=receive_usertask&" +
                "appid=1001&&output_format=json&iruleId=100002&bizcode=&appSource=ios&appSourceDetail=ios15.1&" +
                "appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&" +
                "djcRequestId=A5837B41-E39B-4593-91C9-2DB832232B27-1637744088769-1" +
                "&sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return re.getBody();
    }

    public HttpHeaders setHeaders(String cookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookie);
        headers.add(HttpHeaders.USER_AGENT, "PolyCity/4.4.2 (iPhone; iOS 15.1; Scale/3.00)");
        headers.add(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        headers.add(HttpHeaders.CONTENT_TYPE, "keep-alive");
        headers.add(HttpHeaders.HOST, "comm.ams.game.qq.com");
        headers.add(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headers.add(HttpHeaders.REFERER, "https://daoju.qq.com/index.shtml");
        return headers;
    }

    /**
     * 获取游戏大区
     *
     * @param cookie
     * @return
     */
    public JSONObject giveGameRegion(String cookie) {
        String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=app.player.role&iAppId=1001&_app_id=1001&" +
                "sBizCode=yxzj&biz=yxzj&appSource=ios&appSource=ios&appSourceDetail=ios15.1.1&appVersion=128&" +
                "sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&" +
                "djcRequestId=A5837B41-E39B-4593-91C9-" +
                "2DB832232B27-1638582693457-0&" +
                "sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return JSONObject.parseObject(re.getBody()).getJSONArray("data").size() > 0 ?
                JSONObject.parseObject(re.getBody()).getJSONArray("data").getJSONObject(0) :
                giveAndroidGameRegion(cookie);
    }

    /**
     * 获取安卓游戏大区
     *
     * @param cookie
     * @return
     */
    public JSONObject giveAndroidGameRegion(String cookie) {
        String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=app.player.role&iAppId=1001&_app_id=1001&" +
                "sBizCode=yxzj&biz=yxzj&appSource=android&appSource=android&appSourceDetail=ios15.1.1&appVersion=128&" +
                "sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&" +
                "djcRequestId=A5837B41-E39B-4593-91C9-" +
                "2DB832232B27-1638582693457-0&" +
                "sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return JSONObject.parseObject(re.getBody()).getJSONArray("data").size() > 0 ?
                JSONObject.parseObject(re.getBody()).getJSONArray("data").getJSONObject(0) :
                null;
    }

    /**
     * 获取许愿列表
     *
     * @param cookie
     * @return
     */
    public JSONArray getWishList(String cookie) {
        String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=app.demand.user.demand&&weexVersion=0.28.0&" +
                "platform=iOS&weex_id=89&deviceModel=iPhone13%2C3&&osVersion=15.1.1&appSource=ios&" +
                "appSourceDetail=ios15.1.1&appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&_app_id=1001&" +
                "_biz_code=&pn=1&ps=5&appUid=" + QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin"))) + "&appSource" +
                "=ios&appSourceDetail=ios15.1.1&appVersion=128&" +
                "sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&djcRequestId=A5837B41-E39B-4593-91C9-" +
                "2DB832232B27-1638950988116-6&sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return JSONObject.parseObject(re.getBody()).getJSONObject("data").getJSONArray("list");
    }

    /**
     * 获取许愿列表
     *
     * @param cookie
     * @return
     */
    public String deleteWish(String cookie, String sKeyId) {
        String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=app.demand.delete&output_format=jsonp&iAppId=" +
                "1001&_app_id=1001&p_tk=" + QQUtil.getG_tk(cookie) + "&sKeyId=" + sKeyId + "&appSource=ios&appSourceDetail" +
                "=ios15.1.1&appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&djcRequestId=" +
                "A5837B41-E39B-4593-91C9-2DB832232B27-1638951529493-6" +
                "&sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return re.getBody();
    }

    /**
     * 获取所有角色列表
     *
     * @param cookie
     * @return
     */
    public JSONArray allRoleList(String cookie) {
        String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=app.role.bind_list&appVersion=128" +
                "&p_tk=" + QQUtil.getG_tk(cookie) + "&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&sDjcSign=" +
                "" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "&" +
                "weexVersion=0.9.4&platform" +
                "=android&deviceModel=MIX%202&type=1&output_format=json&osVersion=Android-28&ch=10003&sVersionName=v4" +
                ".1.6.0&appSource=android";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return JSONObject.parseObject(re.getBody()).getJSONArray("data");
    }

    /**
     * 添加心愿单
     *
     * @param cookie
     * @return
     */
    public JSONObject addWish(String cookie, RoleInfo roleInfo) {
        String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=app.demand.create&p_tk=" + QQUtil.getG_tk(cookie) + "" +
                "&iActionId=" + roleInfo.getIActionId() + "&iGoodsId=" + roleInfo.getIGoodsId() + "&sBizCode=" + roleInfo.getSBizCode() + "&partition=" + roleInfo.getPartition() + "&iZoneId=" + roleInfo.getIZoneId() + "&platid=" + roleInfo.getPlatid() + "&sZoneDesc=" + roleInfo.getSZoneDesc() + "" +
                "&sRoleId=" + roleInfo.getSRoleId() + "&sRoleName=" + roleInfo.getSRoleName() + "&sGetterDream=" + roleInfo.getSGetterDream() + "&" +
                "sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&appVersion=128&p_tk=" + QQUtil.getG_tk(cookie) + "&" +
                "sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "&" +
                "iAppId=1001&_app_id=1001&osVersion=Android-28&ch=10003&sVersionName=v4.1.6.0&" +
                "appSource=android";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return JSONObject.parseObject(re.getBody());
    }

    /**
     * 领取游戏礼包
     *
     * @param cookie cookie
     * @return json
     */
    public String giveWishPack(String cookie) {
        String url = "https://apps.game.qq.com/daoju/v3/api/we/usertaskv2/Usertask.php?iAppId=1001&_app_id=1001" +
                "&p_tk=" + QQUtil.getG_tk(cookie) + "&output_format=json&_output_fmt=json&weex_id=45&optype=receive_usertask&" +
                "appid=1001&iruleId=302124&appSource=ios&appSourceDetail=ios15.1.1&appVersion=128&sDeviceID=A5837B41-" +
                "E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&djcRequestId=A5837B41-E39B-4593-91C9-2DB832232B27-1" +
                "639033768164-0";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return re.getBody();
    }

    /**
     * 查询余额
     *
     * @param cookie cookie
     * @return json
     */
    public JSONObject queryBalance(String cookie) {
        try {
            HashMap<String, Object> cookies = new HashMap<>(8);
            cookies.put("cookie", cookie);
            HttpUtil.ResEntity resEntity = HttpUtil.doGet("https://djcapp.game.qq.com/cgi-bin/daoju/djcapp/v5/solo/jfcloud_flow.cgi?&" +
                    "weexVersion=0.28.0&platform=iOS&weex_id=45&deviceModel=iPhone13%2C3&&osVersion=15.1.1&appSource" +
                    "=ios&appSourceDetail=ios15.1.1&appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&&" +
                    "method=balance&page=0&appSource=ios&appSourceDetail=ios15.1.1&appVersion=128&sDeviceID=A5837B41" +
                    "-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&djcRequestId=A5837B41-E39B-4593-" +
                    "91C9-2DB832232B27-1639044225181-3", null, cookies, null);
            return JSONObject.parseObject(resEntity.getResponse()).getJSONObject("data");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加cf许愿单
     *
     * @param cookie
     */
    public void addWishs(String cookie) {
        JSONArray objects = allRoleList(cookie);
        objects.forEach(s -> {
            JSONObject jsonObject = (JSONObject) s;
            if (org.apache.commons.lang3.StringUtils.equals("cf", (String) jsonObject.get("sBizCode"))) {
                JSONObject sRoleInfo = jsonObject.getJSONObject("sRoleInfo");
                RoleInfo roleInfo = new RoleInfo();
                roleInfo.setPartition(sRoleInfo.getString("areaID"));
                roleInfo.setIZoneId(sRoleInfo.getString("serviceID"));
                roleInfo.setPlatid(0);
                roleInfo.setSZoneDesc(sRoleInfo.getString("serviceName"));
                roleInfo.setSRoleId(sRoleInfo.getString("roleCode"));
                roleInfo.setSRoleName(sRoleInfo.getString("roleName"));
                roleInfo.setSGetterDream("不要888！不要488！9.98带回家");
                JSONObject object = addWish(cookie, roleInfo);
                System.out.println(object.toString());
            }
        });
    }

    /**
     * 删除许愿单
     *
     * @param cookie
     */
    public void deleteWishS(String cookie) {
        JSONArray wishList = getWishList(cookie);
        if (wishList.size() > 0) {
            wishList.forEach(s -> {
                JSONObject object = (JSONObject) s;
                String deleteWish = deleteWish(cookie, object.get("sKeyId").toString());
            });
        }
    }

    /**
     * 查询签到天数
     *
     * @param cookie cookie
     * @return json
     */
    public int findSignInDays(String cookie) {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("appSource", "ios");
        map.put("appSourceDetail", "ios15.1");
        map.put("appVersion", "128");
        map.put("djcRequestId", "A5837B41-E39B-4593-91C9-2DB832232B27-1637333481084-7");
        map.put("g_tk", QQUtil.getG_tk(cookie));
        map.put("iActivityId", "11117");
        map.put("iFlowId", "96938");
        map.put("month", DateUtil.getDate());
        map.put("p_tk", QQUtil.getG_tk(cookie));
        map.put("sDeviceID", "A5837B41-E39B-4593-91C9-2DB832232B27");
        map.put("sDjcSign", MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))));
        map.put("sServiceDepartment", "djc");
        map.put("sServiceType", "dj");
        map.put("sign_version", "1.0");
        try {
            HttpUtil.ResEntity result = HttpUtil.doPosts("https://comm.ams.game.qq.com/ams/ame/amesvr?ameVersion=0.3&sServiceType=dj&" +
                    "iActivityId=11117&sServiceDepartment=djc&set_info=newterminals&&weexVersion=0.28.0&platform=" +
                    "iOS&weex_id=45&deviceModel=iPhone13%2C3&&osVersion=15.1.1&appSource=ios&appSourceDetail=ios15.1.1" +
                    "&appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&&appSource=ios&appSourceDetail=" +
                    "ios15.1.1&appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&" +
                    "djcRequestId=A5837B41-E39B-4593-91C9-2DB832232B27-1639098904263-5", httpHeaders, cookie, map);
            JSONObject findSignInDaysJson = JSONObject.parseObject(result.getResponse()).getJSONObject("modRet");
            return findSignInDaysJson.getJSONArray("data").size();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 查询奖励
     *
     * @param cookie cookie
     * @return json
     */
    public String giveSignInBonus(String cookie) {
        String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=app.reward.sign.rule&output_format=json&" +
                "&weexVersion=0.28.0&platform=iOS&weex_id=45&deviceModel=iPhone13%2C3&&osVersion=15.1.1&appSource=ios&" +
                "appSourceDetail=ios15.1.1&appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&&appSource=ios&" +
                "appSourceDetail=ios15.1.1&appVersion=128&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&p_tk=" + QQUtil.getG_tk(cookie) + "&" +
                "djcRequestId=A5837B41-E39B-4593-91C9-2DB832232B27-1639044225885-0" +
                "&sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin")))) + "";
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return re.getBody();
    }

    /**
     * 领取积累签到奖励
     *
     * @param cookie cookie
     * @return json
     */
    public String giveBonus(String cookie) {
        StringBuilder builder = new StringBuilder();
        String bonus = giveSignInBonus(cookie);
        JSONArray data = JSONObject.parseObject(bonus).getJSONArray("data");
        data.forEach(s -> {
            JSONObject signJson = (JSONObject) s;
            if (signJson.getIntValue("iCanUse") == 1 && findSignInDays(cookie) >= signJson.getIntValue("iDays")) {
                HttpUtil.ResEntity iFlowId = giveSignIn(cookie, signJson.getIntValue("iFlowId"));
                JSONObject iFlowIdJson = JSONObject.parseObject(iFlowId.getResponse());
                builder.append("\n").append(StringUtils.isBlank(
                        iFlowIdJson.getString("msg")) ?
                        iFlowIdJson.getJSONObject("modRet").get("sMsg") :
                        iFlowIdJson.getString("msg"));
            }
        });
        return builder.toString();
    }

    /**
     * 领取浏览页面
     *
     * @param cookie cookie
     * @return json
     */
    public String giveShow(String cookie) {
        String url = "https://djcapp.game.qq.com/daoju/igw/main/?_service=app.comment.goods_comment_list&" +
                "iAppId=1001&_app_id=1001&p_tk=" + QQUtil.getG_tk(cookie) + "&s_type=firstComment&" +
                "docid=28701&sort=ai_djc_release_score_int&page=1&_biz_code=yxzj&page_type=prop_say&" +
                "appSource=ios&appSourceDetail=ios15.4.1&appVersion=130&sDeviceID=A5837B41-E39B-4593-91C9-2DB832232B27&" +
                "djcRequestId=A5837B41-E39B-4593-91C9-2DB832232B27-1649815712958-9&" +
                "sDjcSign=" + MyHttpHandler.addDjcSignParams(QQUtil.uinToQQ(String.valueOf(QQUtil.cookieToMap(cookie).get("uin"))));
        System.out.println(url);
        HttpHeaders headers = setHeaders(cookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return re.getBody();
    }
}
