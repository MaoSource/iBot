package com.source.exception;

import com.source.exception.code.IErrorCode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/23/15:20
 */
public class CustomerException extends AppException{
    /**
     * 是否打印错误堆栈信息，默认false
     */
    private boolean printStackTrace = false;

    /**
     * @param iErrorCode 错误码
     * @param cause 错误
     */
    public CustomerException(IErrorCode iErrorCode, Throwable cause) {
        super(iErrorCode.getCode(), iErrorCode.getMessage(), cause);
        if (cause != null) {
            printStackTrace = true;
        }
    }
    public CustomerException(IErrorCode iErrorCode, String errorMessage) {
        super(iErrorCode.getCode(), errorMessage);
    }

    public CustomerException(IErrorCode iErrorCode) {
        super(iErrorCode.getCode(), iErrorCode.getMessage());
    }

    public CustomerException(int errorCode, String errorMessage) {
        super(String.valueOf(errorCode), errorMessage);
    }

    public CustomerException(int errorCode, String errorMessage, Throwable cause) {
        super(String.valueOf(errorCode), errorMessage);
        if (cause != null) {
            this.printStackTrace = true;
        }
    }

    public CustomerException(String errorMessage) {
        super("-1", errorMessage);
    }
    public CustomerException(String errorMessage, Throwable cause) {
        super("-1", errorMessage);
        if (cause != null) {
            this.printStackTrace = true;
        }
    }
    public boolean isPrintStackTrace() {
        return printStackTrace;
    }
}
