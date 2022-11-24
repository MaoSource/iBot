package com.source.bean.bot;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/10/13:45
 */
public class ActionMsg implements Serializable {

    private static final long serialVersionUID = 7333012650050787773L;

    /**
     * 消息
     */
    private String msg;
    /**
     * 第一次访问
     */
    protected boolean first;

    /**
     * 无参构造函数
     */
    public ActionMsg() {
    }

    /**
     * 有参构造函数
     *
     * @param msg 消息
     */
    public ActionMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 有参构造函数
     *
     * @param msg         消息
     * @param firstDecode 首次访问是否解码
     */
    public ActionMsg(String msg, boolean firstDecode) {
        this.msg = msg;
        this.first = !firstDecode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ActionMsg) {
            ActionMsg am = (ActionMsg) obj;
            return am.getMsg() != null && am.getMsg().equals(this.msg);
        }
        return false;
    }

    public String getMsg() {
        if (!first) {
            msg = CQCode.decode(msg);
            first = true;
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return CQCode.encode(msg, true);
    }

}
