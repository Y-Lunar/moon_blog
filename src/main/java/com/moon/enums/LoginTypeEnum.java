package com.moon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 第三方登录方式枚举
 *
 * @author:Y.0
 * @date:2023/8/29
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {

    /**
     * 邮箱
     */
    EMAIL(1, "邮箱登录",""),

    /**
     * Gitee
     */
    GITEE(4,"Gitee登录","giteeLoginStrategyImpl"),

    /**
     * Github
     */
    GITHUB(5,"Github登录","githubLoginStrategyImpl");

    /**
     * 登录方式
     */
    private final Integer loginType;

    /**
     * 描述
     */
    private final String description;

    /**
     * 策略
     */
    private final String strategy;
}
