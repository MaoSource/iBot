package com.source.exception;

import com.source.response.CommonResponse;
import com.source.response.CommonResponseUtil;
import com.source.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/23/15:24
 */
@RestControllerAdvice
public class GlobalRestControllerResolver {

    private final Logger log = LoggerFactory.getLogger(GlobalRestControllerResolver.class);

    /**
     * JSR303
     *
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse handleBindException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        log.error("参数校验异常：{}({})", fieldError.getDefaultMessage(), fieldError.getField());
        log.error("Cause: ", exception);
        return CommonResponseUtil.failed(1000, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(CustomerException.class)
    public CommonResponse customerError(CustomerException customerException) {
        log.error("自定义异常：{}", customerException.getMessage());
        if (customerException.isPrintStackTrace()) {
            // 打印错误
            log.error("Cause: ", customerException);
        }
        return CommonResponseUtil.failed(customerException.getErrorCode(), customerException.getMessage());
    }
    /**
     * 通用异常处理
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = {Exception.class})
    public CommonResponse commonError(Exception exception) {
        log.error("未知异常：{}", exception.getMessage());
        log.error("Cause: ", exception);
        return CommonResponseUtil.failed(5000, ExceptionUtil.getAllExceptionMsg(exception));
    }
}
