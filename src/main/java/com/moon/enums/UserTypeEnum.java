package com.moon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举
 * @author:Y.0
 * @date:2023/8/29
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    /**
     * 用户
     */
    USER(1, "用户"),
    /**
     * 游客
     */
    VISITOR(2, "游客");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 获取用户类型
     *
     * @param type 类型
     * @return {@link UserTypeEnum} 用户类型枚举
     */
    public static UserTypeEnum getUserType(Integer type) {
        for (UserTypeEnum value : UserTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
