package com.source.exception.code;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/23/15:21
 */
public interface IErrorCode {
    /**
     * 获取错误码
     * @return
     */
    String getCode();

    /**
     * 获取错误信息
     * @return
     */
    String getMessage();
}
