package com.moon.exception;

import lombok.Getter;

import static com.moon.enums.StatusCodeEnum.FAIL;

/**
 * 业务异常
 * @author:Y.0
 * @date:2023/9/21
 */
@Getter
public final class ServiceException extends RuntimeException {

    /**
     * 返回失败状态码
     */
    private Integer code = FAIL.getCode();

    /**
     * 返回信息
     */
    private final String message;

    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}