package com.source.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/23/15:20
 */
public class AppException extends RuntimeException{
    /**
     * 错误码
     */
    private final String errorCode;

    public AppException() {
        // 默认错误码 -1
        this.errorCode = "-1";
    }

    public AppException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    public AppException(String errorCode, String message,Throwable cause) {
        super(message,cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {return this.errorCode;}
}
