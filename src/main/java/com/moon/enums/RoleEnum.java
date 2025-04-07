package com.moon.enums;

import lombok.*;

/**
 * 角色枚举
 * @author:Y.0
 * @date:2023/9/22
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {

    /**
     * 管理员
     */
    ADMIN("1","ADMIN"),

    /**
     * 用户
     */
    USER("2","USER"),

    /**
     * 测试用户
     */
    TEST("3","TEST");


    private final String roleId;


    private final String roleName;

}
