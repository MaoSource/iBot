package com.source.bean.bot;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/10/13:36
 */
@Data
public class Message {

    private String request_type;
    private String comment;
    private String flag;

    private String type;
    private long self_id;
    private String sub_type;
    private long message_id;
    private long user_id;
    private long group_id;
    private String message;
    private String raw_message;
    private long font;
    private Sender sender;
    private long time;
    private String post_type;
    private String message_type;

}
