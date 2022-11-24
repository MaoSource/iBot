package com.source.bean.bot;

import com.source.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/10/13:44
 */
abstract class BaseMsg<E extends ActionMsg> extends ArrayList<E> {

    private static final long serialVersionUID = -2822321344276400849L;

    /**
     * 获取相匹配的 CQ消息
     *
     * @param e 要匹配的消息
     * @return CQ消息
     */
    public E get(E e) {
        int index = indexOf(e);
        return index == -1 ? null : get(index);
    }

    /**
     * 修改CQ消息，不存在则添加至末尾(覆盖)
     *
     * @param e CQ消息
     * @return 覆盖前CQ消息
     */
    public E set(E e) {
        int index = indexOf(e);
        if (index == -1)
            add(e);
        else
            set(index, e);
        return e;
    }

    /**
     * 解析CQ码
     *
     * @param code 要解析的CQ码
     * @return 本消息对象
     */
    public abstract BaseMsg<E> analysis(String code);

    /**
     * 追加CQ码
     *
     * @param ac CQ码
     */
    protected abstract void addCode(com.source.bean.bot.ActionCode ac);

    /**
     * 修改CQ码，不存在则添加至末尾(覆盖)
     *
     * @param ac CQ码
     */
    protected abstract void setCode(com.source.bean.bot.ActionCode ac);

    /**
     * 根据功能名称，获取CQ码
     *
     * @param ac 包含功能名称的CQ码
     * @return CQ码
     */
    protected abstract com.source.bean.bot.ActionCode getCode(com.source.bean.bot.ActionCode ac);

    /**
     * 根据索引，获取CQ码
     *
     * @param index 索引
     * @return CQ码
     */
    protected abstract com.source.bean.bot.ActionCode getCode(int index);

    /**
     * 反转内部元素,元素是文字则连文字也一并反转
     *
     * @return 本消息对象
     */
    public BaseMsg<E> reverse() {
        Collections.reverse(this);
        StringBuilder sb = new StringBuilder();
        for (ActionMsg msg : this)
            if (!(msg instanceof com.source.bean.bot.ActionCode))
                msg.setMsg(sb.delete(0, sb.length()).append(msg.getMsg()).reverse().toString());
        return this;
    }

    /**
     * 获取整段的消息文本
     *
     * @return 消息文本
     */
    public String msg() {
        Iterator<E> it = iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext())
            sb.append(it.next());
        return sb.toString();
    }

    /**
     * 获取指定功能名称的 CQ功能 中键的值<br>
     *
     * @param key       功能名称
     * @param actionKey 功能中的键
     * @return 功能中键的值，不存在返回 {@code null}
     */
    public String get(String key, String actionKey) {
        int index = indexOf(new com.source.bean.bot.ActionCode(key));// 利用重写的equals方法进行判定
        if (index == -1)
            return null;
        com.source.bean.bot.ActionCode ac = getCode(index);
        return ac.get(actionKey);
    }

    /**
     * 获取指定功能名称的 CQ功能 中键下标的值<br>
     *
     * @param key         功能名称
     * @param actionIndex 功能中的键的下标
     * @return 功能中键的值，不存在返回 {@code null}
     */
    public String get(String key, int actionIndex) {
        int index = indexOf(new com.source.bean.bot.ActionCode(key));// 利用重写的equals方法进行判定
        if (index == -1)
            return null;
        com.source.bean.bot.ActionCode ac = getCode(index);
        return ac.get(actionIndex);
    }

    /**
     * 获取指定功能名称的 所有CQ功能<br>
     * 遇到相同功能名称，则返回 符合的所有CQ功能
     *
     * @param key 功能名称
     * @return CQ功能
     */
    public List<com.source.bean.bot.ActionCode> gets(String key) {
        com.source.bean.bot.ActionCode code = new com.source.bean.bot.ActionCode(key);
        List<com.source.bean.bot.ActionCode> list = new ArrayList<com.source.bean.bot.ActionCode>();
        for (E obj : this) {
            if (code.equals(obj))
                list.add((com.source.bean.bot.ActionCode) obj);
        }
        return list;
    }

    /**
     * 获取指定功能名称所有CQ功能中指定键的值<br>
     *
     * @param key       功能名称
     * @param actionKey 功能中的键
     * @return 所有CQ功能中键的值
     */
    public List<String> gets(String key, String actionKey) {
        com.source.bean.bot.ActionCode code = new com.source.bean.bot.ActionCode(key);
        List<String> values = new ArrayList<String>();
        for (E obj : this) {
            if (code.equals(obj))
                values.add(((com.source.bean.bot.ActionCode) obj).get(actionKey));
        }
        return values;
    }

    /**
     * 表情QQ表情(face)
     *
     * @param faceId 表情ID
     * @return 本消息对象
     */
    public BaseMsg<E> face(int... faceId) {
        if (faceId != null && faceId.length != 0) {
            for (long id : faceId) {
                com.source.bean.bot.ActionCode ac = new com.source.bean.bot.ActionCode("face");
                ac.put("id", String.valueOf(id));
                addCode(ac);
            }
        }
        return this;
    }

    /**
     * 从CQ码中获取QQ表情ID，错误返回 -1
     *
     * @return 表情ID
     */
    public int getFaceId() {
        String faceId = get("face", "id");
        return faceId == null ? -1 : Integer.parseInt(faceId);
    }

    /**
     * 从CQ码中获取所有QQ表情ID
     *
     * @return 所有表情ID
     */
    public List<Integer> getFaceIds() {
        List<String> list = gets("face", "id");
        List<Integer> ints = new ArrayList<Integer>();
        for (String faceId : list) {
            if (faceId != null)
                ints.add(Integer.parseInt(faceId));
        }
        return ints;
    }

    /**
     * emoji表情(emoji)
     *
     * @param emojiId emoji的unicode编号
     * @return 本消息对象
     */
    public BaseMsg<E> emoji(int... emojiId) {
        if (emojiId != null && emojiId.length != 0) {
            for (long id : emojiId) {
                com.source.bean.bot.ActionCode ac = new com.source.bean.bot.ActionCode("emoji");
                ac.put("id", String.valueOf(id));
                addCode(ac);
            }
        }
        return this;
    }

    /**
     * 从CQ码中获取emoji表情ID，错误返回 -1
     *
     * @return emoji的unicode编号
     */
    public int getEmoji() {
        String emoji = get("emoji", "id");
        return emoji == null ? -1 : Integer.parseInt(emoji);
    }

    /**
     * 从CQ码中获取所有emoji表情ID
     *
     * @return 所有emoji表情ID
     */
    public List<Integer> getEmojis() {
        List<String> list = gets("emoji", "id");
        List<Integer> ints = new ArrayList<Integer>();
        for (String emoji : list) {
            if (emoji != null)
                ints.add(Integer.parseInt(emoji));
        }
        return ints;
    }

    /**
     * 提醒某人，@某人(at)
     *
     * @param qqId qq号，-1 为全体
     * @return 本消息对象
     */
    public BaseMsg<E> at(long... qqId) {
        if (qqId != null && qqId.length != 0) {
            for (long id : qqId) {
                com.source.bean.bot.ActionCode ac = new com.source.bean.bot.ActionCode("at");
                ac.put("qq", id == -1 ? "all" : String.valueOf(id));
                addCode(ac);
            }
        }
        return this;
    }

    /**
     * 从CQ码中获取at的QQ号，-1 为全体，错误为 -1000
     *
     * @return qq号，-1 为全体，错误为 -1000
     */
    public long getAt() {
        String qqId = get("at", "qq");
        if (qqId == null)
            return -1000;
        if (qqId.equals("all"))
            return -1;
        return Long.parseLong(qqId);
    }


    /**
     * 从CQ码中获取所有at的QQ号，-1 为全体
     *
     * @return qq号集合，-1 为全体
     */
    public List<Long> getAts() {
        List<String> list = gets("at", "qq");
        List<Long> longs = new ArrayList<Long>();
        for (String qqId : list) {
            if (qqId != null) {
                if (qqId.equals("all"))
                    longs.add(-1L);
                else
                    longs.add(Long.parseLong(qqId));
            }
        }
        return longs;
    }

    /**
     * 窗口抖动(shake) - 仅支持好友
     *
     * @return 本消息对象
     */
    public BaseMsg<E> shake() {
        addCode(new com.source.bean.bot.ActionCode("shake"));
        return this;
    }

    /**
     * 判断CQ码中是否包含 窗口抖动(shake)
     *
     * @return 是否包含
     */
    public boolean isShake() {
        return contains(new com.source.bean.bot.ActionCode("shake"));
    }

    /**
     * 匿名发消息(anonymous) - 仅支持群
     *
     * @param ignore 是否不强制，如果希望匿名失败时，将消息转为普通消息发送(而不是取消发送)，请置本参数为true。
     * @return 本消息对象
     */
    public BaseMsg<E> anonymous(boolean ignore) {
        com.source.bean.bot.ActionCode ac = new com.source.bean.bot.ActionCode("anonymous");
        if (ignore) ac.put("ignore", "true");
        addCode(ac);
        return this;
    }

    /**
     * 发送音乐(music)
     *
     * @param musicId 音乐的歌曲数字ID
     * @param type    目前支持 qq/QQ音乐 163/网易云音乐 xiami/虾米音乐，默认为qq
     * @param style   指定分享样式 0/是最古老的版本 1/是QQ音乐特有的大版本 2/是最新的支持
     * @return 本消息对象
     */
    public BaseMsg<E> music(long musicId, String type, int style) {
        com.source.bean.bot.ActionCode ac = new com.source.bean.bot.ActionCode("music");
        ac.put("id", String.valueOf(musicId));
        if (StringUtils.isEmpty(type))
            ac.put("type", "qq");
        else
            ac.put("type", CQCode.encode(type, true));
        ac.put("style", Integer.toString(style));
        addCode(ac);
        return this;
    }

    /**
     * 发送音乐自定义分享(music)
     *
     * @param url     分享链接,点击分享后进入的音乐页面（如歌曲介绍页）
     * @param audio   音频链接,音乐的音频链接（如mp3链接）
     * @param title   标题,音乐的标题，建议12字以内
     * @param content 内容,音乐的简介，建议30字以内
     * @param image   封面图片链接,音乐的封面图片链接，留空则为默认图片
     * @return 本消息对象
     */
    public BaseMsg<E> music(String url, String audio, String title, String content, String image) {
        com.source.bean.bot.ActionCode ac = new com.source.bean.bot.ActionCode("music");
        ac.put("type", "custom");
        ac.put("url", CQCode.encode(url, true));
        ac.put("audio", CQCode.encode(audio, true));
        if (!StringUtils.isEmpty(title))
            ac.put("title", CQCode.encode(title, true));
        if (!StringUtils.isEmpty(content))
            ac.put("content", CQCode.encode(content, true));
        if (!StringUtils.isEmpty(image))
            ac.put("image", CQCode.encode(image, true));
        addCode(ac);
        return this;
    }

    /**
     * 发送名片分享(contact)
     *
     * @param type 目前支持 qq/好友分享 group/群分享
     * @param id   类型为qq，则为QQID；类型为group，则为群号
     * @return 本消息对象
     */
    public BaseMsg<E> contact(String type, long id) {
        com.source.bean.bot.ActionCode ac = new com.source.bean.bot.ActionCode("contact");
        ac.put("type", CQCode.encode(type, true));
        ac.put("id", String.valueOf(id));
        addCode(ac);
        return this;
    }

    /**
     * 发送链接分享(share)
     *
     * @param url     分享的链接
     * @param title   分享的标题，建议12字以内
     * @param content 分享的简介，建议30字以内
     * @param image   分享的图片链接，留空则为默认图片
     * @return 本消息对象
     */
    public BaseMsg<E> share(String url, String title, String content, String image) {
        com.source.bean.bot.ActionCode ac = new com.source.bean.bot.ActionCode("share");
        ac.put("url", CQCode.encode(url, true));
        if (!StringUtils.isEmpty(title))
            ac.put("title", CQCode.encode(title, true));
        if (!StringUtils.isEmpty(content))
            ac.put("content", CQCode.encode(content, true));
        if (!StringUtils.isEmpty(image))
            ac.put("image", CQCode.encode(image, true));
        addCode(ac);
        return this;
    }

    /**
     * 发送位置分享(location)
     *
     * @param lat     纬度
     * @param lon     经度
     * @param zoom    放大倍数，默认为 15
     * @param title   地点名称，建议12字以内
     * @param content 地址，建议20字以内
     * @return 本消息对象
     */
    public BaseMsg<E> location(double lat, double lon, int zoom, String title, String content) {
        com.source.bean.bot.ActionCode ac = new com.source.bean.bot.ActionCode("location");
        ac.put("lat", String.valueOf(lat));
        ac.put("lon", String.valueOf(lon));
        if (zoom > 0)
            ac.put("zoom", String.valueOf(zoom));
        ac.put("title", String.valueOf(CQCode.encode(title, true)));
        ac.put("content", String.valueOf(CQCode.encode(content, true)));
        addCode(ac);
        return this;
    }

    /**
     * 发送图片(image)
     *
     * @param file 将图片放在 data\image 下，并填写相对路径。如 data\image\1.jpg 则填写 1.jpg
     * @return 本消息对象
     */
    public BaseMsg<E> image(String file) {
        com.source.bean.bot.ActionCode ac = new com.source.bean.bot.ActionCode("image");
        ac.put("file", CQCode.encode(file, true));
        addCode(ac);
        return this;
    }

    /**
     * 发送图片(image)
     *
     * @param path 要发送的图片文件对象，位置随意
     * @return 本消息对象
     * @throws IOException IO异常
     */
    public BaseMsg<E> image(File path) throws IOException {
        CQImage image = new CQImage(path, false);
        path = image.download("data/image/", image.getName());
        path.deleteOnExit();
        return image(path.getName());
    }

    /**
     * 发送图片(image)
     *
     * @param image 要发送的CQImage对象
     * @return 本消息对象
     * @throws IOException IO异常
     */
    public BaseMsg<E> image(CQImage image) throws IOException {
        File path = new File("data/image/", image.getName());
        if (!image.getName().endsWith(".cqimg") || !path.isFile())
            image.download(path);
        path.deleteOnExit();
        return image(path.getName());
    }

    /**
     * 发送图片(image),使用GET请求
     *
     * @param url 要发送的图片链接
     * @return 本消息对象
     * @throws IOException IO异常
     */
    public BaseMsg<E> imageUseGet(String url) throws IOException {
        return imageUseGet(url, null);
    }

    /**
     * 发送图片(image),使用GET请求
     *
     * @param url               要发送的图片链接
     * @param requestProperties 请求头信息
     * @return 本消息对象
     * @throws IOException IO异常
     */
    public BaseMsg<E> imageUseGet(String url, Map<String, List<String>> requestProperties) throws IOException {
        CQImage image = new CQImage(url);
        File path = image.downloadUseGet(new File("data/image/", image.getName()), requestProperties);
        path.deleteOnExit();
        return image(path.getName());
    }

    /**
     * 发送图片(image),使用POST请求
     *
     * @param url               要发送的图片链接
     * @param requestProperties 请求头信息
     * @param bytes             请求的数据
     * @return 本消息对象
     * @throws IOException IO异常
     */
    public BaseMsg<E> imageUsePost(String url, Map<String, List<String>> requestProperties, byte[] bytes) throws IOException {
        CQImage image = new CQImage(url);
        File path = image.downloadUsePost(new File("data/image/", image.getName()), requestProperties, bytes);
        path.deleteOnExit();
        return image(path.getName());
    }

    /**
     * 从CQ码中获取图片的路径，如 [CQ:image,file=1.jpg] 则返回 1.jpg
     *
     * @return 图片路径，如 [CQ:image,file=1.jpg] 则返回 1.jpg，错误返回 {@code null}
     */
    public String getImage() {
        return get("image", "file");
    }

    /**
     * 从CQ码中获取图片的路径，如 [CQ:image,file=1.jpg] 则返回 1.jpg
     *
     * @return 图片路径的集合
     */
    public List<String> getImages() {
        return gets("image", "file");
    }

    /**
     * 从CQ码中获取图片的 CQImage 对象
     *
     * @return CQImage 对象，错误返回 {@code null}
     */
    public CQImage getCQImage() {
        try {
            // 获取相对路径
            String path = StringUtils.stringConcat("data", File.separator, "image", File.separator, get("image", "file"), ".cqimg");
            return new CQImage(new IniFile(new File(path)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从CQ码中获取 所有 CQImage 对象
     *
     * @return CQImage 对象集合
     */
    public List<CQImage> getCQImages() {
        List<CQImage> list = new ArrayList<CQImage>();
        List<String> imgs = gets("image", "file");
        try {
            for (String file : imgs) {
                if (file != null) {
                    String path = StringUtils.stringConcat("data", File.separator, "image", File.separator, file, ".cqimg");
                    File iniFile = new File(path);
                    if (iniFile.exists() && iniFile.canRead())
                        list.add(new CQImage(new IniFile(iniFile)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 发送语音(record)
     *
     * @param file  将语音放在 data\record 下，并填写相对路径。如 data\record\1.amr 则填写 1.amr
     * @param magic 是否是魔法语音
     * @return 本消息对象
     */
    public BaseMsg<E> record(String file, boolean magic) {
        com.source.bean.bot.ActionCode ac = new ActionCode("record");
        ac.put("file", CQCode.encode(file, true));
        if (magic) ac.put("magic", "true");
        addCode(ac);
        return this;
    }

    /**
     * 发送语音(record)
     *
     * @param file 将语音放在 data\record 下，并填写相对路径。如 data\record\1.amr 则填写 1.amr
     * @return 本消息对象
     */
    public BaseMsg<E> record(String file) {
        return record(file, false);
    }

    /**
     * 从CQ码中获取语音的路径，如 [CQ:record,file=1.amr] 则返回 1.amr
     *
     * @return 语音路径，如 [CQ:record,file=1.amr] 则返回 1.amr，错误返回 {@code null}
     */
    public String getRecord() {
        return get("record", "file");
    }

}
