package com.moon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态码枚举
 * @author:Y.0
 * @date:2023/8/29
 */
@Getter
@AllArgsConstructor
public enum StatusCodeEnum {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 操作失败
     */
    FAIL(500, "操作失败"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(501, "系统异常"),

    /**
     * 用户信息异常
     */
    USER_ERROR(502,"用户信息异常"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 未登录
     */
    UNLOGIN(402, "未登录"),

    /**
     * 参数校验失败
     */
    VALID_ERROR(417, "参数格式不正确");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 返回信息
     */
    private final String msg;

}
